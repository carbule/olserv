package cn.azhicloud.housekeeping.model;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/6/17 13:02
 */
@Data
public class DoHousekeepingRQ {

    /**
     * service code
     */
    @NotBlank(message = "serviceCode 不能为空")
    private String serviceCode;

    /**
     * biz params
     */
    private Map<String, Object> params;
}
