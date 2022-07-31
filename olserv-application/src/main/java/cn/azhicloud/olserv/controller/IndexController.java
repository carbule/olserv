package cn.azhicloud.olserv.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/31 08:11
 */
@RestController
public class IndexController {

    @GetMapping
    public String index() {
        return "Welcome to olserv !";
    }
}
