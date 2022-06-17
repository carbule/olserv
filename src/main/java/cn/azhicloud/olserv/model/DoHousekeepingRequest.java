package cn.azhicloud.olserv.model;

import java.util.Map;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/6/17 13:02
 */
@Data
public class DoHousekeepingRequest {

    /**
     * service code
     */
    private String serviceCode;

    /**
     * biz params
     */
    private Map<String, Object> params;
}
