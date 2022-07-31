package cn.azhicloud.fzero.helper;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/30 20:11
 */
public class Validator {

    /**
     * extract validation errs
     *
     * @param err BindingResult
     */
    public static void extractErr(BindingResult err) {
        if (err.hasErrors()) {
            String errs = err.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new RuntimeException(errs);
        }
    }
}
