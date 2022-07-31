package cn.azhicloud.fzero.repository;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 通过 feign 调用外部系统
 *
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/30 10:17
 */
@FeignClient("out")
public interface OutRepository {

    /**
     * GET
     *
     * @param uri uri
     * @return response
     */
    @GetMapping
    String get(URI uri, @RequestHeader String fromIP);

    /**
     * POST
     *
     * @param uri uri
     * @return response
     */
    @PostMapping
    String post(URI uri, @RequestHeader String fromIP);
}
