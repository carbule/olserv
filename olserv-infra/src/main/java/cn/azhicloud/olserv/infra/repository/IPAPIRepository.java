package cn.azhicloud.olserv.infra.repository;

import cn.azhicloud.olserv.infra.model.IPAPIResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/25 17:57
 */
@FeignClient(value = "ip-api", url = "http://ip-api.com")
public interface IPAPIRepository {

    /**
     * json 返回
     *
     * @param query IP
     * @return json
     */
    @GetMapping("/json/{query}?lang=zh-CN")
    IPAPIResponse json(@PathVariable String query);
}
