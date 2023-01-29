package cn.azhicloud.olserv.audit;

import cn.azhicloud.infra.task.service.AutoTaskCfgService;
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
public class AuditAutoTaskCfgInitializer implements CommandLineRunner {
    protected final AutoTaskCfgService autoTaskCfgService;

    @Override
    public void run(String... args) throws Exception {
        autoTaskCfgService.init(AuditTaskTypeConst.class);
    }
}
