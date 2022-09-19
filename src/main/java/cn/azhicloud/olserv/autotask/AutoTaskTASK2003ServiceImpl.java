package cn.azhicloud.olserv.autotask;

import javax.transaction.Transactional;

import cn.azhicloud.infra.base.exception.BizException;
import cn.azhicloud.infra.base.model.IPSBResponse;
import cn.azhicloud.infra.base.repository.IPSBRepository;
import cn.azhicloud.infra.task.service.AutoTaskExecuteService;
import cn.azhicloud.olserv.autotask.bo.TaskTASK2003BO;
import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/25 18:10
 */
@Service(TaskTypeConst.SAVE_ACCOUNT_PULL_SUBSCRIBE_LOCATION)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskTASK2003ServiceImpl implements AutoTaskExecuteService {

    private final IPSBRepository ipsbRepository;

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void execute(String taskData) {
        TaskTASK2003BO taskBO = JSON.parseObject(taskData, TaskTASK2003BO.class);
        Account account = accountRepository.findById(taskBO.getAccountId())
                .orElseThrow(() -> new BizException("账户不存在"));
        if (StringUtils.isBlank(account.getFromIp())) {
            throw new BizException("访问者 IP 为空");
        }

        // 如果是内网 IP，无法获取地理位置
        if (!NetUtil.isInnerIP(account.getFromIp())) {
            IPSBResponse response = ipsbRepository.json(account.getFromIp());
            // China Unicom | China Jiangsu Nanjing
            account.setFromLocation(String.join(" ",
                    response.getOrganization() + " |",
                    response.getCountry(), response.getRegion(), response.getCity()));
        }
    }
}
