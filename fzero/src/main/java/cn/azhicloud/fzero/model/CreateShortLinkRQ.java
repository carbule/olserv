package cn.azhicloud.fzero.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/31 00:28
 */
@Data
public class CreateShortLinkRQ {

    @NotBlank(message = "link 不能为空")
    private String link;
}
