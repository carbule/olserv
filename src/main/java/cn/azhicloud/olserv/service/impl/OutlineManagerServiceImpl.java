package cn.azhicloud.olserv.service.impl;

import java.util.Objects;

import cn.azhicloud.olserv.ApiException;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.model.outline.AccessKeys;
import cn.azhicloud.olserv.model.outline.ServerInformation;
import cn.azhicloud.olserv.service.OutlineManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
    public AccessKey getAccessKey(String apiUrl, String username) {
        return listAccessKeys(apiUrl)
                .getAccessKeys().stream().filter(o ->
                        Objects.equals(username, o.getName())).findAny().orElse(null);
    }
}

