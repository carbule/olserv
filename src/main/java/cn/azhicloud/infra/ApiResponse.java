package cn.azhicloud.infra;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

/**
 * 接口响应
 *
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/6/24 10:10
 */
@Data
public class ApiResponse {

    /**
     * 成功标识
     */
    private boolean success = true;

    /**
     * 业务码
     */
    private String code = "ok";

    /**
     * 描述信息
     */
    private String message = "ok";

    /**
     * 业务数据
     */
    @JsonUnwrapped
    private Object data;

    public ApiResponse() {
    }

    public ApiResponse(Object data) {
        this.data = data;
    }

    public ApiResponse(String code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }

    public ApiResponse(String code, String message, Object data) {
        this.success = false;
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
