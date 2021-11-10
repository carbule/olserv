package cn.azhicloud.olserv;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/30 17:54
 */
@ConfigurationProperties(prefix = "hash-ids")
@Data
public class HashIdsConfigurationProperties {

    private String salt;

    private Integer length;
}
