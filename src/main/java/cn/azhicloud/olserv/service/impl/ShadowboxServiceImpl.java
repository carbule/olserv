package cn.azhicloud.olserv.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cn.azhicloud.olserv.ApiException;
import cn.azhicloud.olserv.model.AddShadowboxRequest;
import cn.azhicloud.olserv.model.ListShadowboxesResponse;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.ServerInformation;
import cn.azhicloud.olserv.repository.ShadowboxRepos;
import cn.azhicloud.olserv.service.OutlineManagerService;
import cn.azhicloud.olserv.service.ShadowboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:39
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShadowboxServiceImpl implements ShadowboxService {

    private final ShadowboxRepos shadowboxRepos;

    private final OutlineManagerService outlineManagerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addShadowbox(AddShadowboxRequest request) {
        Optional<Shadowbox> optional = shadowboxRepos.findByApiUrl(request.getApiUrl());
        if (optional.isPresent()) {
            throw new RuntimeException("shadowbox.existed");
        }

        // 添加 outline 服务器时获取服务器名称 用于校验 apiUrl 是否有效
        ServerInformation server = outlineManagerService.getServerInformation(request.getApiUrl());

        Shadowbox shadowbox = new Shadowbox();
        shadowbox.setName(server.getName());
        shadowbox.setApiUrl(request.getApiUrl());
        shadowbox.setCertSha256(request.getCertSha256());

        shadowboxRepos.save(shadowbox);
    }

    @Override
    public ListShadowboxesResponse listShadowboxes() {
        List<Shadowbox> shadowboxes = shadowboxRepos.findAll();

        ListShadowboxesResponse response = new ListShadowboxesResponse();
        response.setShadowboxes(new ArrayList<>());

        shadowboxes.forEach(box -> {
            try {
                ListShadowboxesResponse.Shadowbox boxVO = new ListShadowboxesResponse.Shadowbox();

                ServerInformation server = outlineManagerService.getServerInformation(box.getApiUrl());

                boxVO.setName(server.getName());
                boxVO.setApiUrl(box.getApiUrl());

                response.getShadowboxes().add(boxVO);
            } catch (ApiException e) {
                log.error("--------------------", e);
            }
        });

        return response;
    }
}
