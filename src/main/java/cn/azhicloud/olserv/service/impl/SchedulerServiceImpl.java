package cn.azhicloud.olserv.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.azhicloud.olserv.ApiException;
import cn.azhicloud.olserv.BaseEntity;
import cn.azhicloud.olserv.model.entity.AccessKey;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKeys;
import cn.azhicloud.olserv.model.outline.ServerInformation;
import cn.azhicloud.olserv.repository.AccessKeyRepos;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 1)
    public void persistenceAccessKeys() {
        log.info(">>> Start do persistent access_key.");

        accessKeyRepos.deleteAll();

        List<Shadowbox> shadowboxes = shadowboxRepos.findAll();

        shadowboxes.forEach(box -> {
            try {
                ServerInformation server = outlineManagerService.getServerInformation(box);

                // if server change name
                if (!Objects.equals(server.getName(), box.getName())) {
                    box.setName(server.getName());
                }

                AccessKeys accessKeys = outlineManagerService.listAccessKeys(box);

                accessKeys.getAccessKeys().forEach(key -> {
                    AccessKey entity = BaseEntity.instance(AccessKey.class);
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
                log.error(">>> Do persistent access_key failed, exception message: {}", e.getMessage());
            }
        });

        log.info(">>> End do persistent access_key.");
    }
}
