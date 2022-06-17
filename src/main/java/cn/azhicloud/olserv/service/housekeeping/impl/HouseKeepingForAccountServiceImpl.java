package cn.azhicloud.olserv.service.housekeeping.impl;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.mapper.AccountMapper;
import cn.azhicloud.olserv.service.AccountService;
import cn.azhicloud.olserv.service.OutlineFeignClient;
import cn.azhicloud.olserv.service.housekeeping.HouseKeepingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/6/17 13:05
 */
@Service("houseKeepingForAccountServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class HouseKeepingForAccountServiceImpl implements HouseKeepingService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final AccountService accountService;

    private final OutlineFeignClient outlineFeignClient;

    @Override
    @Transactional
    public void doHousekeeping() {
        log.info("Start do housekeeping for account.");
        doIt();
        log.info("End do housekeeping for account.");
    }

    private void doIt() {
        List<Account> expiredAccounts = accountMapper.selectExpiredAccounts();
        if (expiredAccounts.size() > 0) {
            expiredAccounts.forEach(account -> deleteAccessKey(account.getId()));
            accountRepository.deleteAllByIdInBatch(
                    expiredAccounts.stream().map(Account::getId)
                            .collect(Collectors.toList())
            );
            return;
        }
        log.info("No expired accounts.");
    }

    private void deleteAccessKey(String accountId) {
        List<Shadowbox> shadowboxes = accountService.listShadowboxOwnedByAccount(accountId);
        for (Shadowbox box : shadowboxes) {
            for (AccessKey key : box.getAccessKeys()) {
                outlineFeignClient.deletesAnAccessKey(URI.create(box.getApiUrl()), key.getId());
            }
        }
    }
}
