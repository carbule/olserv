package cn.azhicloud.olserv.infra.repository;

import cn.azhicloud.olserv.infra.model.IPSBResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/26 09:12
 */
@FeignClient(value = "ip.sb", url = "https://api.ip.sb/geoip")
public interface IPSBRepository {

    /**
     * json 返回
     *
     * @param query IP
     * @return json
     */
    @GetMapping("/{query}")
    IPSBResponse json(@PathVariable String query);
}
