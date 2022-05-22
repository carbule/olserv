package cn.azhicloud.olserv.service.impl;

import cn.azhicloud.olserv.ApiException;
import cn.azhicloud.olserv.BaseEntity;
import cn.azhicloud.olserv.model.entity.ApiError;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.model.outline.AccessKeys;
import cn.azhicloud.olserv.model.outline.ServerInformation;
import cn.azhicloud.olserv.repository.ApiErrorRepos;
import cn.azhicloud.olserv.service.OutlineManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Objects;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 21:00
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OutlineManagerServiceImpl implements OutlineManagerService {

    private final RestTemplate restTemplate;

    private final ApiErrorRepos apiErrorRepos;

    @Override
    public ServerInformation getServerInformation(String apiUrl) {
        String accessUrl = apiUrl + "/server";

        log.info("Call API: {}", accessUrl);
        try {
            return restTemplate.getForObject(accessUrl, ServerInformation.class);
        } catch (RestClientException e) {
            throw new ApiException(String.format("API: %s/server call failed", apiUrl), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ServerInformation getServerInformation(Shadowbox box) {
        try {
            return getServerInformation(box.getApiUrl());
        } catch (ApiException e) {
            throw saveApiFailedReason(box, e);
        }
    }

    @Override
    public AccessKeys listAccessKeys(String apiUrl) {
        String accessUrl = apiUrl + "/access-keys";

        log.info("Call API: {}", accessUrl);
        try {
            return restTemplate.getForObject(accessUrl, AccessKeys.class);
        } catch (RestClientException e) {
            throw new ApiException(String.format("API: %s/access-keys call failed", apiUrl), e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AccessKeys listAccessKeys(Shadowbox box) {
        try {
            return listAccessKeys(box.getApiUrl());
        } catch (ApiException e) {
            throw saveApiFailedReason(box, e);
        }
    }

    @Override
    public AccessKey getAccessKey(String apiUrl, String username) {
        return listAccessKeys(apiUrl)
                .getAccessKeys().stream().filter(o ->
                        Objects.equals(username, o.getName())).findAny().orElse(null);
    }

    /**
     * 异常需要被记录
     */
    private ApiException saveApiFailedReason(Shadowbox box, ApiException e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));

        ApiError apiErr = apiErrorRepos.findByApiUrl(box.getApiUrl());
        if (apiErr == null) {
            ApiError err = BaseEntity.instance(ApiError.class);
            err.setApiName(box.getName());
            err.setApiUrl(box.getApiUrl());
            err.setReason(writer.toString());
            err.setLatest(new Date());
            apiErrorRepos.save(err);
        } else {
            // 托管态
            apiErr.setReason(writer.toString());
            apiErr.setLatest(new Date());
        }
        return e;
    }
}

