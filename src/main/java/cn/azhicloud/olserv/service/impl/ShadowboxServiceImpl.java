package cn.azhicloud.olserv.service.impl;

import java.net.URI;
import java.util.List;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.helper.ExecutorHelper;
import cn.azhicloud.infra.task.service.AutoTaskBaseService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK2001BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.domain.entity.Shadowbox;
import cn.azhicloud.olserv.domain.model.outline.Server;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import cn.azhicloud.olserv.service.ShadowboxService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    private final ShadowboxRepository shadowboxRepository;

    private final OutlineRepository outlineRepository;

    private final AutoTaskBaseService autoTaskBaseService;

    @Override
    public Shadowbox addShadowbox(String apiUrl) {
        if (shadowboxRepository.existsById(apiUrl)) {
            throw new BizException("服务器已存在");
        }

        Server server = outlineRepository.returnsInformationAboutTheServer(URI.create(apiUrl));

        Shadowbox shadowbox = new Shadowbox();
        shadowbox.setApiUrl(apiUrl);
        BeanUtils.copyProperties(server, shadowbox);
        shadowbox.setOffline(Boolean.FALSE);
        shadowbox.setForOutlineClient(Boolean.FALSE);
        shadowboxRepository.save(shadowbox);

        TaskTASK2001BO taskBO = new TaskTASK2001BO();
        taskBO.setApiUrl(shadowbox.getApiUrl());
        autoTaskBaseService.createAutoTaskAndPublishMQ(TaskTypeConst.ALLOCATE_SHADOWBOX_TO_ACCOUNTS,
                JSON.toJSONString(taskBO));
        return shadowbox;
    }

    @Override
    @SneakyThrows
    @Transactional
    public List<Shadowbox> listShadowboxes() {
        List<Shadowbox> shadowboxes = shadowboxRepository.findAll();

        return ExecutorHelper.execute(shadowboxes, box -> {
            URI uri = URI.create(box.getApiUrl());
            // 如果服务端有变更，托管态实体自动更新
            BeanUtils.copyProperties(outlineRepository.returnsInformationAboutTheServer(uri), box);
            box.setAccessKeys(outlineRepository.listsTheAccessKeys(uri)
                    .getAccessKeys());
            return box;
        }, ex -> log.error("获取 Key 失败：{}", ex.getMessage()));
    }

}
