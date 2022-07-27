package cn.azhicloud.olserv.model.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/27 13:41
 */
@Data
@Entity
public class AccountTraffic {

    @Id
    private Long id;

    /**
     * 统计日期
     */
    private LocalDate statsDate;

    /**
     * 账户 ID
     */
    private String accountId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 已使用的兆字节数
     */
    private Long megabytesTransferred;

    /**
     * 在已使用的兆字节数中增加
     *
     * @param megabytes 增加的兆字节
     * @return 增加后的兆字节
     */
    public Long plusMegabytesTransferred(Long megabytes) {
        return megabytesTransferred += megabytes;
    }
}
