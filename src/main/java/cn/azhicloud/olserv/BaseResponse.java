package cn.azhicloud.olserv;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:38
 */
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = -2701769060396567788L;

    private final int code;

    private final String message;

    public BaseResponse() {
        this.code = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getReasonPhrase();
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
