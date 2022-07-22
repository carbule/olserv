package cn.azhicloud.olserv.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/22 13:26
 */
@Data
public class CreateAccountRQ {

    @NotBlank(message = "username 不能为空")
    private String username;

    @NotBlank(message = "email 不能为空")
    private String email;
}
