package cn.azhicloud.olserv.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.azhicloud.olserv.BaseResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListAccountsResponse extends BaseResponse {

    private static final long serialVersionUID = -7471322950080780939L;

    private List<Account> accounts;

    @Data
    public static class Account implements Serializable {

        private static final long serialVersionUID = 1114586137360788530L;

        private String hid;

        private String username;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
        private Date created;
    }
}
