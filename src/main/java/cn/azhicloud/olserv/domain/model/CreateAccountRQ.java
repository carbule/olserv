package cn.azhicloud.olserv.domain.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/22 13:26
 */
@Data
public class CreateAccountRQ {

    /**
     * 用户名，用于创建节点的名称
     */
    @NotBlank(message = "username 不能为空")
    private String username;

    /**
     * 邮箱，用于通知订阅信息
     */
    @NotBlank(message = "email 不能为空")
    private String email;
}
