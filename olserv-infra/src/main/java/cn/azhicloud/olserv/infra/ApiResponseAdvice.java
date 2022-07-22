package cn.azhicloud.olserv.infra;

import java.util.StringJoiner;
import javax.validation.ConstraintViolationException;

import cn.azhicloud.olserv.infra.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/14 17:12
 */
@RestControllerAdvice
@Slf4j
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * ApiResponse.code warn
     */
    private static final String API_RESPONSE_CODE_WARN = "warn";

    /**
     * ApiResponse.code error
     */
    private static final String API_RESPONSE_CODE_ERROR = "error";

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body instanceof ApiResponse || body instanceof String) {
            return body;
        }
        return new ApiResponse(body);
    }


    @ExceptionHandler
    public ApiResponse handle(BizException ex) {
        return new ApiResponse(ex.getBizCode(), ex.getMessage());
    }

    @ExceptionHandler
    public ApiResponse handle(HttpRequestMethodNotSupportedException ex) {
        return new ApiResponse(API_RESPONSE_CODE_WARN, ex.getMessage());
    }

    @ExceptionHandler
    public ApiResponse handle(MissingServletRequestParameterException ex) {
        return new ApiResponse(API_RESPONSE_CODE_WARN, ex.getMessage());
    }

    @ExceptionHandler
    public ApiResponse handle(HttpMessageNotReadableException ex) {
        return new ApiResponse(API_RESPONSE_CODE_WARN, ex.getMessage());
    }

    @ExceptionHandler
    public ApiResponse handle(MethodArgumentNotValidException ex) {
        StringJoiner joiner = new StringJoiner(", ");
        ex.getBindingResult().getFieldErrors().forEach(err -> joiner.add(err.getDefaultMessage()));

        return new ApiResponse(API_RESPONSE_CODE_WARN, joiner.toString());
    }

    @ExceptionHandler
    public ApiResponse handle(ConstraintViolationException ex) {
        StringJoiner joiner = new StringJoiner(", ");
        ex.getConstraintViolations().forEach(err -> joiner.add(err.getMessage()));

        return new ApiResponse(API_RESPONSE_CODE_WARN, joiner.toString());
    }

    @ExceptionHandler
    public ApiResponse handle(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new ApiResponse(API_RESPONSE_CODE_ERROR, ex.getMessage());
    }
}
