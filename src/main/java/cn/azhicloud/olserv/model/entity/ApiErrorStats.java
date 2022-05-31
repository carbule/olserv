package cn.azhicloud.olserv.model.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/5/26 16:52
 */
@Data
@Entity
public class ApiErrorStats {

    @Id
    private String id;

    /**
     * 统计时间
     */
    private Date statsTime;

    /**
     * 服务器名称
     */
    private String apiName;

    /**
     * 服务器管理地址
     */
    private String apiUrl;

    /**
     * 失败次数
     */
    private Integer errorCount;
}
