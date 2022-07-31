package cn.azhicloud.olserv.repository;

import cn.azhicloud.olserv.model.CreateShortLinkRQ;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/31 00:53
 */
@FeignClient(value = "short-link", url = "${service.fzero}", path = "/short-link")
public interface ShortLinkRepository {

    /**
     * 创建短链接
     *
     * @param rq 请求参数
     * @return 短链接
     */
    @PostMapping
    String createShortLink(@RequestBody CreateShortLinkRQ rq);
}
