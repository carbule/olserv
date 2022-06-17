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
    public String handle(HttpRequestMethodNotSupportedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler
    public String handle(HttpMessageNotReadableException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler
    public String handle(MethodArgumentNotValidException ex) {
        StringJoiner joiner = new StringJoiner(", ");
        ex.getBindingResult().getFieldErrors().forEach(err -> joiner.add(err.getDefaultMessage()));

        return joiner.toString();
    }

    @ExceptionHandler
    public String handle(ConstraintViolationException ex) {
        StringJoiner joiner = new StringJoiner(", ");
        ex.getConstraintViolations().forEach(err -> joiner.add(err.getMessage()));

        return joiner.toString();
    }

    @ExceptionHandler
    public String handle(Exception e) {
        log.error(e.getMessage(), e);
        return e.getMessage();
    }
}
