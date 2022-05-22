package cn.azhicloud.olserv.service.impl;

import cn.azhicloud.olserv.ApiException;
import cn.azhicloud.olserv.BaseEntity;
import cn.azhicloud.olserv.model.entity.AccessKey;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKeys;
import cn.azhicloud.olserv.model.outline.ServerInformation;
import cn.azhicloud.olserv.repository.AccessKeyRepos;
import cn.azhicloud.olserv.repository.ShadowboxRepos;
import cn.azhicloud.olserv.service.AccessKeyService;
import cn.azhicloud.olserv.service.OutlineManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/5 21:15
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccessKeyServiceImpl implements AccessKeyService {

    private final AccessKeyRepos accessKeyRepos;

    private final ShadowboxRepos shadowboxRepos;

    private final OutlineManagerService outlineManagerService;

    @Override
    @Transactional
    public void flushAccessKeys() {
        accessKeyRepos.deleteAllInBatch();

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
                    // 用于端口转发
                    entity.setRedirectAddress(box.getRedirectAddress());
                    entity.setRedirectPort(box.getRedirectPort());

                    accessKeyRepos.save(entity);
                });
            } catch (ApiException e) {
                // ignore
            }
        });
    }
}
