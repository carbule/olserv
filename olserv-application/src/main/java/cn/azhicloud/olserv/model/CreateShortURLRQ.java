package cn.azhicloud.olserv.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/30 10:24
 */
@Data
public class CreateShortURLRQ {

    /**
     * 原始 URL
     */
    @NotBlank(message = "originURL 不能为空")
    private String originURL;
}
