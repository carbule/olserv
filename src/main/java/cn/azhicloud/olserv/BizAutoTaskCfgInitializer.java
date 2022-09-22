package cn.azhicloud.olserv;

import cn.azhicloud.infra.task.service.AutoTaskCfgService;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/9/22 16:12
 */
@Component
@RequiredArgsConstructor
public class BizAutoTaskCfgInitializer implements CommandLineRunner {
    protected final AutoTaskCfgService autoTaskCfgService;

    @Override
    public void run(String... args) throws Exception {
        autoTaskCfgService.init(TaskTypeConst.class);
    }
}
