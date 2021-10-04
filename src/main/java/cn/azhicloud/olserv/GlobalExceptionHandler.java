package cn.azhicloud.olserv;

import java.util.StringJoiner;
import javax.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 22:08
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public BaseResponse handle(HttpRequestMethodNotSupportedException ex) {
        return new BaseResponse(-1, "bad request");
    }

    @ExceptionHandler
    public BaseResponse handle(HttpMessageNotReadableException ex) {
        return new BaseResponse(-1, "bad request");
    }

    @ExceptionHandler
    public BaseResponse handle(MethodArgumentNotValidException ex) {
        StringJoiner joiner = new StringJoiner(", ");
        ex.getBindingResult().getFieldErrors().forEach(err -> joiner.add(err.getDefaultMessage()));

        return new BaseResponse(-1, joiner.toString());
    }

    @ExceptionHandler
    public BaseResponse handle(ConstraintViolationException ex) {
        StringJoiner joiner = new StringJoiner(", ");
        ex.getConstraintViolations().forEach(err -> joiner.add(err.getMessage()));

        return new BaseResponse(-1, joiner.toString());
    }

    @ExceptionHandler
    public BaseResponse handle(Exception e) {
        log.error("---------------", e);
        return new BaseResponse(-1, e.getMessage());
    }
}
