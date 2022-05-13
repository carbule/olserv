package cn.azhicloud.olserv.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.azhicloud.olserv.model.entity.AccessLog;
import cn.azhicloud.olserv.model.entity.ApiError;
import cn.azhicloud.olserv.repository.AccessLogRepos;
import cn.azhicloud.olserv.repository.ApiErrorRepos;
import cn.azhicloud.olserv.repository.mapper.AccessLogMapper;
import cn.azhicloud.olserv.repository.mapper.ApiErrorMapper;
import cn.azhicloud.olserv.service.HouseKeepingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/5/12 17:10
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HouseKeepingServiceImpl implements HouseKeepingService {

    private final ApiErrorRepos apiErrorRepos;

    private final ApiErrorMapper apiErrorMapper;

    private final AccessLogRepos accessLogRepos;

    private final AccessLogMapper accessLogMapper;

    @Override
//    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 1)
    @Scheduled(cron = "0 0 0 * * ?")
    public void apiErrorHouseKeeping() {
        log.info(">>> Start do housekeeping for api_error.");
        List<ApiError> apiErrors = apiErrorMapper.selectCreatedBeforeDays(3);

        apiErrorRepos.deleteAllByIdInBatch(
                apiErrors.stream().map(ApiError::getId).collect(Collectors.toList())
        );
        log.info(">>> End do housekeeping for api_error.");
    }

    @Override
//    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 1)
    @Scheduled(cron = "0 0 0 * * ?")
    public void accessLogHouseKeeping() {
        log.info(">>> Start do housekeeping for access_log.");
        List<AccessLog> accessLogs = accessLogMapper.selectCreatedBeforeDays(7);

        accessLogRepos.deleteAllByIdInBatch(
                accessLogs.stream().map(AccessLog::getId).collect(Collectors.toList())
        );
        log.info(">>> End do housekeeping for access_log.");
    }
}
