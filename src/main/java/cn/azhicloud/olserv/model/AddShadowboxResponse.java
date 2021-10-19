package cn.azhicloud.olserv.model;

import cn.azhicloud.olserv.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/19 19:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddShadowboxResponse extends BaseResponse {

    private static final long serialVersionUID = 4094116422110715692L;

    private String serverName;
}
