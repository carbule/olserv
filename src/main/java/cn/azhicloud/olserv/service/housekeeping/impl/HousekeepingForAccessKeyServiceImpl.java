package cn.azhicloud.olserv.service.housekeeping.impl;

import cn.azhicloud.olserv.repository.AccessKeyRepos;
import cn.azhicloud.olserv.repository.AccountRepos;
import cn.azhicloud.olserv.repository.ShadowboxRepos;
import cn.azhicloud.olserv.service.OutlineManagerService;
import cn.azhicloud.olserv.service.housekeeping.HouseKeepingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/5/22 18:14
 */
@Service("housekeepingForAccessKeyServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class HousekeepingForAccessKeyServiceImpl implements HouseKeepingService {

    private final AccessKeyRepos accessKeyRepos;

    private final AccountRepos accountRepos;

    private final ShadowboxRepos shadowboxRepos;

    private final OutlineManagerService outlineManagerService;

    @Override
    @Transactional
    public void doHousekeeping() {
        log.info("Start do housekeeping for access_key.");

    }
}
