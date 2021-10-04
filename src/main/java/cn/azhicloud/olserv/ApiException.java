package cn.azhicloud.olserv;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/4 8:56
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 4212779831423897314L;

    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
