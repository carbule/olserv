package cn.azhicloud.olserv.service.impl.autotask.hkptask;

import java.time.LocalDate;
import java.util.List;

import cn.azhicloud.olserv.constant.TaskTypeConst;
import cn.azhicloud.olserv.infra.helper.SystemHelper;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.AccountTraffic;
import cn.azhicloud.olserv.repository.AccountRepository;
import cn.azhicloud.olserv.repository.AccountTrafficRepository;
import cn.azhicloud.olserv.repository.mapper.AccountTrafficMapper;
import cn.azhicloud.olserv.task.service.AutoTaskExecuteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/27 14:11
 */
@Service(TaskTypeConst.STATS_ACCOUNT_USED_TRAFFIC_OF_DAILY)
@RequiredArgsConstructor
@Slf4j
public class AutoTaskHKP1004ServiceImpl implements AutoTaskExecuteService {

    private final AccountRepository accountRepository;

    private final AccountTrafficRepository accountTrafficRepository;

    private final AccountTrafficMapper accountTrafficMapper;

    @Override
    @Transactional
    public void execute(String taskData) {
        // 统计昨天的流量使用
        LocalDate statsDate = LocalDate.now().minusDays(1);

        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            AccountTraffic traffic = new AccountTraffic();
            traffic.setId(SystemHelper.nextSerialNo2Long());
            traffic.setStatsDate(statsDate);
            traffic.setAccountId(account.getId());
            traffic.setUsername(account.getUsername());
            AccountTraffic latestStats = accountTrafficMapper.selectLatestAccountTrafficStats(account.getId());
            if (latestStats == null || account.getMegabytesTransferred() == null) {
                traffic.setMegabytesTransferred(account.getMegabytesTransferred());
            } else {
                // 如果统计日期相同，则累计统计结果
                if (statsDate.equals(latestStats.getStatsDate())) {
                    latestStats.plusMegabytesTransferred(account
                            .getMegabytesTransferred() - latestStats.getMegabytesTransferred());
                    accountTrafficRepository.save(latestStats);
                    continue;
                }
                traffic.setMegabytesTransferred(
                        account.getMegabytesTransferred() - latestStats.getMegabytesTransferred()
                );
            }
            accountTrafficRepository.save(traffic);
        }
    }
}
