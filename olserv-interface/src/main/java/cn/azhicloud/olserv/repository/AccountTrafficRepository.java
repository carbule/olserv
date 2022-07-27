package cn.azhicloud.olserv.repository;

import java.time.LocalDate;

import cn.azhicloud.olserv.model.entity.AccountTraffic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/27 13:44
 */
public interface AccountTrafficRepository extends JpaRepository<AccountTraffic, Long> {

    /**
     * 每个账户一天只允许一条流量统计，单日流量统计依次增加
     *
     * @param accountId 账户 ID
     * @param statsDate 统计日期
     * @return entity
     */
    AccountTraffic findByAccountIdAndStatsDate(String accountId, LocalDate statsDate);
}
