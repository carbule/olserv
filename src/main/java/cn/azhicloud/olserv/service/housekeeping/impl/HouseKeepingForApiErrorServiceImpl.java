package cn.azhicloud.olserv.service.housekeeping.impl;

import cn.azhicloud.olserv.model.entity.ApiError;
import cn.azhicloud.olserv.repository.ApiErrorRepos;
import cn.azhicloud.olserv.repository.mapper.ApiErrorMapper;
import cn.azhicloud.olserv.service.housekeeping.HouseKeepingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/5/12 17:10
 */
@Service("houseKeepingForApiErrorServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class HouseKeepingForApiErrorServiceImpl implements HouseKeepingService {

    private final ApiErrorRepos apiErrorRepos;

    private final ApiErrorMapper apiErrorMapper;

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void doHousekeeping() {
        log.info("Start do housekeeping for api_error.");
        List<ApiError> apiErrors = apiErrorMapper.selectCreatedBeforeDays(3);

        apiErrorRepos.deleteAllByIdInBatch(
                apiErrors.stream().map(ApiError::getId).collect(Collectors.toList())
        );
        log.info("End do housekeeping for api_error.");
    }
}
