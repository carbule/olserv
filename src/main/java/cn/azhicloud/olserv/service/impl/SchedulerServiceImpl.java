package cn.azhicloud.olserv.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cn.azhicloud.olserv.ApiException;
import cn.azhicloud.olserv.model.entity.AccessKey;
import cn.azhicloud.olserv.model.entity.AccessLog;
import cn.azhicloud.olserv.model.entity.AccessStatistics;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKeys;
import cn.azhicloud.olserv.model.outline.ServerInformation;
import cn.azhicloud.olserv.repository.AccessKeyRepos;
import cn.azhicloud.olserv.repository.AccessLogRepos;
import cn.azhicloud.olserv.repository.AccessStatisticsRepos;
import cn.azhicloud.olserv.repository.ShadowboxRepos;
import cn.azhicloud.olserv.service.OutlineManagerService;
import cn.azhicloud.olserv.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final AccessLogRepos accessLogRepos;

    private final AccessStatisticsRepos accessStatisticsRepos;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 1)
    public void persistenceAccessKeys() {
        log.info("----- persistenceAccessKeys start ----- ");

        accessKeyRepos.deleteAll();

        List<Shadowbox> shadowboxes = shadowboxRepos.findAll();

        shadowboxes.forEach(box -> {
            try {
                ServerInformation server = outlineManagerService.getServerInformation(box);

                AccessKeys accessKeys = outlineManagerService.listAccessKeys(box);

                accessKeys.getAccessKeys().forEach(key -> {
                    AccessKey entity = new AccessKey();
                    entity.setServerName(server.getName());
                    entity.setKeyId(key.getId());
                    entity.setName(key.getName());
                    entity.setPassword(key.getPassword());
                    entity.setPort(key.getPort());
                    entity.setMethod(key.getMethod());
                    entity.setAccessUrl(key.getAccessUrl().replace(OUTLINE_SUFFIX, ""));
                    // 用于端口转发
                    entity.setRedirectAddress(box.getRedirectAddress());
                    entity.setRedirectPort(box.getRedirectPort());

                    accessKeyRepos.save(entity);
                });
            } catch (ApiException e) {
                log.error("------------------", e);
            }
        });

        log.info("----- persistenceAccessKeys finish ----- ");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 5)
    public void accessStatistics() {
        log.info("----- accessStatistics start ----- ");

        List<AccessLog> accesses = accessLogRepos.findAll();

        Map<String, List<AccessLog>> accessesGrpByUser = accesses.stream().
                collect(Collectors.groupingBy(AccessLog::getUsername));

        accessesGrpByUser.forEach((user, logs) -> {

            // 获取最新的 access log
            AccessLog lastLog = logs.stream().max(Comparator.
                    comparing(AccessLog::getCreated)).get();

            AccessStatistics statistics = new AccessStatistics();
            statistics.setUsername(user);
            statistics.setCount(logs.size());
            statistics.setLastAccess(lastLog.getCreated());

            accessStatisticsRepos.save(statistics);

            log.info("user: [{}], accessCount: [{}], lastAccess: [{}]",
                    statistics.getUsername(), statistics.getCount(), statistics.getLastAccessStr());
        });

        log.info("----- accessStatistics finish ----- ");
    }
}
