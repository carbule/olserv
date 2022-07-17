package cn.azhicloud.olserv.service.impl.autotask;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.model.outline.AccessKeys;
import cn.azhicloud.olserv.model.outline.BytesTransferred;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import cn.azhicloud.task.constant.TaskTypeConst;
import cn.azhicloud.task.service.AutoTaskExecuteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/16 14:33
 */
@Service(TaskTypeConst.DO_HOUSEKEEPING_FOR_OUT_OF_TRAFFIC_ACCOUNT)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskHKP1002ServiceImpl implements AutoTaskExecuteService {

    private final AccountRepository accountRepository;

    private final ShadowboxRepository shadowboxRepository;

    private final OutlineRepository outlineRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(String taskData) {
        List<Account> accounts = accountRepository.findAll();
        List<Shadowbox> shadowboxes = shadowboxRepository.findAll();

        List<AccessKey> allKeys = new ArrayList<>();
        for (Shadowbox shadowbox : shadowboxes) {
            try {
                URI uri = URI.create(shadowbox.getApiUrl());
                AccessKeys accessKeys = outlineRepository.listsTheAccessKeys(uri);
                BytesTransferred bytesTransferred = outlineRepository.returnsTheDataTransferredPerAccessKey(uri);

                // 根据 keyId 获取使用流量数
                accessKeys.getAccessKeys().forEach(k -> {
                    k.setApiUri(uri);
                    k.setBytesTransferred(bytesTransferred.getBytesTransferredByUserId()
                            .getOrDefault(k.getId(), 0L));
                });
                allKeys.addAll(accessKeys.getAccessKeys());
            } catch (Exception e) {
                log.error("obtain shadowbox bytes transferred failed", e);
            }
        }

        for (Account account : accounts) {
            List<AccessKey> accountOwnedKeys = allKeys.stream()
                    .filter(k -> Objects.equals(k.getName(), account.getUsername()))
                    .collect(Collectors.toList());
            if (accountOwnedKeys.size() == 0) {
                continue;
            }

            // 汇总并换算成兆字节
            long bytesTransferred = accountOwnedKeys.stream()
                    .mapToLong(AccessKey::getBytesTransferred)
                    .sum();
            long megabytesTransferred = bytesTransferred / (1024 * 1024);
            account.setMegabytesTransferred(megabytesTransferred);

            // 如果使用使用的流量超出分配的，删除所有 key
            if (megabytesTransferred >= account.getMegabytesAllocate()) {
                log.info("account [{}] used out of traffic", account.getUsername());
                accountOwnedKeys.forEach(k -> {
                    try {
                        outlineRepository.deletesAnAccessKey(k.getApiUri(), k.getId());
                    } catch (Exception e) {
                        log.error("delete key failed", e);
                    }
                });
            }
        }
    }
}
