package cn.azhicloud.olserv.service.impl.autotask;

import java.net.URI;
import java.util.List;

import cn.azhicloud.infra.exception.BizException;
import cn.azhicloud.infra.helper.ExecutorHelper;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.OutlineRepository;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import cn.azhicloud.olserv.service.impl.autotask.bo.TaskTASK1001BO;
import cn.azhicloud.task.constant.TaskTypeConst;
import cn.azhicloud.task.service.AutoTaskExecuteService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/17 11:37
 */
@Service(TaskTypeConst.ALLOCATE_ACCOUNT_TO_SHADOWBOXES)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskTASK1001ServiceImpl implements AutoTaskExecuteService {

    private final OutlineRepository outlineRepository;

    private final AccountRepository accountRepository;

    private final ShadowboxRepository shadowboxRepository;

    @Override
    public void execute(String taskData) {
        TaskTASK1001BO taskBO = JSON.parseObject(taskData, TaskTASK1001BO.class);

        Account account = accountRepository.findById(taskBO.getAccountId())
                .orElseThrow(() -> BizException.format("账户不存在"));

        List<Shadowbox> shadowboxes = shadowboxRepository.findAll();
        // 拆分成异步任务执行
        ExecutorHelper.execute(shadowboxes, box -> {
            try {
                URI uri = URI.create(box.getApiUrl());
                // 新增一个 key
                AccessKey accessKey = outlineRepository.createsANewAccessKey(uri);

                // 设置 key 的名称
                AccessKey renameBody = new AccessKey();
                renameBody.setName(account.getUsername());
                try {
                    outlineRepository.renamesAnAccessKey(uri, accessKey.getId(), renameBody);
                } catch (Exception e) {
                    // 如果设置名称失败，删除先前创建的 key
                    outlineRepository.deletesAnAccessKey(uri, accessKey.getId());
                    throw e;
                }
            } catch (Exception e) {
                log.error("call api {} failed", box.getApiUrl(), e);
            }
        });
    }
}
