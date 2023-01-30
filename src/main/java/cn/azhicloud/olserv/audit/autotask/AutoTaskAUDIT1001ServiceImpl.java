package cn.azhicloud.olserv.audit.autotask;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.azhicloud.infra.base.helper.ExecutorHelper;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.audit.AuditTaskTypeConst;
import cn.azhicloud.olserv.audit.domain.entity.AccessKey;
import cn.azhicloud.olserv.audit.repository.AccessKeyRepository;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/28 17:12
 */
@Service(AuditTaskTypeConst.PERSISTENT_ACCOUNT_OWNED_KEYS)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskAUDIT1001ServiceImpl implements AutoTaskExecuteService {

    private final AccessKeyRepository accessKeyRepository;
    private final OutlineRepository outlineRepository;
    private final ShadowboxRepository shadowboxRepository;

    @Override
    @Transactional
    public void execute(String taskData) {
        accessKeyRepository.deleteAllInBatch();
        List<Shadowbox> shadowboxes = shadowboxRepository.findByOfflineIsFalse();

        List<AccessKey> keys = new CopyOnWriteArrayList<>();

        ExecutorHelper.execute(shadowboxes, box -> {
            URI uri = URI.create(box.getApiUrl());
            // 如果服务端有变更，托管态实体自动更新
            BeanUtils.copyProperties(outlineRepository.returnsInformationAboutTheServer(uri), box);

            // 服务器的 access-key 转换为实体进行批量存储
            outlineRepository.listsTheAccessKeys(uri)
                    .getAccessKeys().forEach(k -> {
                        AccessKey key = new AccessKey();
                        BeanUtils.copyProperties(k, key, AccessKey.FIELD_ID);
                        key.setServerId(box.getApiUrl());
                        key.setServerName(box.getName());
                        key.setKeyId(k.getId());
                        keys.add(key);
                    });
        }, ex -> log.error("获取服务器信息失败：{}", ex.getMessage()));
        accessKeyRepository.saveAll(keys);
    }
}
