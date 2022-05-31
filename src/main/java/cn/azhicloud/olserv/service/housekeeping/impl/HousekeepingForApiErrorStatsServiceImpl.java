package cn.azhicloud.olserv.service.housekeeping.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.azhicloud.olserv.model.entity.ApiError;
import cn.azhicloud.olserv.model.entity.ApiErrorStats;
import cn.azhicloud.olserv.repository.ApiErrorStatsRepos;
import cn.azhicloud.olserv.repository.mapper.ApiErrorMapper;
import cn.azhicloud.olserv.service.housekeeping.HouseKeepingService;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/5/26 17:15
 */
@Service("housekeepingForApiErrorStatsServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class HousekeepingForApiErrorStatsServiceImpl implements HouseKeepingService {

    private final ApiErrorStatsRepos apiErrorStatsRepos;

    private final ApiErrorMapper apiErrorMapper;

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void doHousekeeping() {
        log.info("Start do housekeeping for api_error_stats.");

        List<ApiError> errs = apiErrorMapper.selectCreatedAtYesterday();
        Map<String, List<ApiError>> errsMap = errs.stream().collect(
                Collectors.groupingBy(ApiError::getApiUrl)
        );

        errsMap.forEach((key, value) -> {
            ApiErrorStats stats = new ApiErrorStats();
            stats.setId(NanoIdUtils.randomNanoId());
            stats.setStatsTime(new Date());
            stats.setApiUrl(key);
            stats.setApiName(value.get(0).getApiName());
            stats.setErrorCount(value.size());

            apiErrorStatsRepos.save(stats);
        });

        log.info("End do housekeeping for api_error_stats.");
    }
}
