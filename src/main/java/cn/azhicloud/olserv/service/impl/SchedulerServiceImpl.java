package cn.azhicloud.olserv.service.impl;

import cn.azhicloud.olserv.repository.AccessKeyRepos;
import cn.azhicloud.olserv.repository.ShadowboxRepos;
import cn.azhicloud.olserv.service.AccessKeyService;
import cn.azhicloud.olserv.service.OutlineManagerService;
import cn.azhicloud.olserv.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/5 21:21
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {

    private static final String OUTLINE_SUFFIX = "/?outline=1";

    private final AccessKeyRepos accessKeyRepos;

    private final OutlineManagerService outlineManagerService;

    private final ShadowboxRepos shadowboxRepos;

    private final AccessKeyService accessKeyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 1)
    public void flushAccessKeys() {
        log.info("Start do persistent access_key.");
        accessKeyService.flushAccessKeys();
        log.info("End do persistent access_key.");
    }
}
