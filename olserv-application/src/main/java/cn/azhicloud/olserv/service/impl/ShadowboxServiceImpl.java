package cn.azhicloud.olserv.service.impl;

import java.net.URI;
import java.util.List;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.infra.exception.BizException;
import cn.azhicloud.olserv.infra.helper.ExecutorHelper;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.Server;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import cn.azhicloud.olserv.service.ShadowboxService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK2001BO;
import cn.azhicloud.olserv.task.service.AutoTaskBaseService;
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
        shadowboxRepository.save(shadowbox);

        TaskTASK2001BO taskBO = new TaskTASK2001BO();
        taskBO.setApiUrl(shadowbox.getApiUrl());
        autoTaskBaseService.createAutoTaskAndPublicMQ(TaskTypeConst.ALLOCATE_SHADOWBOX_TO_ACCOUNTS,
                JSON.toJSONString(taskBO));
        return shadowbox;
    }

    @Override
    @SneakyThrows
    @Transactional
    public List<Shadowbox> listShadowboxes() {
        List<Shadowbox> shadowboxes = shadowboxRepository.findAll();

        return ExecutorHelper.execute(shadowboxes, box -> {
            try {
                URI uri = URI.create(box.getApiUrl());
                // 如果服务端有变更，托管态实体自动更新
                BeanUtils.copyProperties(outlineRepository.returnsInformationAboutTheServer(uri), box);
                box.setAccessKeys(outlineRepository.listsTheAccessKeys(uri)
                        .getAccessKeys());
            } catch (Exception e) {
                log.error("call api {} failed", box.getApiUrl(), e);
            }

            return box;
        });
    }

}
