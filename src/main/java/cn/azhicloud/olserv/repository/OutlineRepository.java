package cn.azhicloud.olserv.repository;

import java.net.URI;

import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.model.outline.AccessKeys;
import cn.azhicloud.olserv.model.outline.Server;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/6/3 21:03
 */
@FeignClient(value = "outline", url = "outline")
public interface OutlineRepository {

    @GetMapping("/server")
    Server returnsInformationAboutTheServer(URI uri);

    @GetMapping("/access-keys")
    AccessKeys listsTheAccessKeys(URI uri);

    @DeleteMapping("/access-keys/{id}")
    void deletesAnAccessKey(URI uri, @PathVariable String id);

    @PostMapping("/access-keys")
    AccessKey createsANewAccessKey(URI uri);

    @PutMapping("/access-keys/{id}/name")
    void renamesAnAccessKey(URI uri, @PathVariable String id, @RequestBody AccessKey key);
}
