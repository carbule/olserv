package cn.azhicloud.olserv.model;

import java.io.Serializable;
import java.util.List;

import cn.azhicloud.olserv.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 21:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListAccessKeysResponse extends BaseResponse {

    private static final long serialVersionUID = 5627529333478065356L;

    private List<AccessKey> accessKeys;

    @Data
    public static class AccessKey implements Serializable {

        private static final long serialVersionUID = 5538835732513266705L;

        /**
         * server 名称
         */
        private String name;

        private String password;

        private Integer port;

        private String method;

        private String accessUrl;
    }
}
