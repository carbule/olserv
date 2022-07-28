package cn.azhicloud.olserv.repository;

import java.net.URI;

import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.model.outline.AccessKeys;
import cn.azhicloud.olserv.model.outline.BytesTransferred;
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

    /**
     * 获取服务器基础信息
     *
     * @param uri apiUrl
     * @return server info
     */
    @GetMapping("/server")
    Server returnsInformationAboutTheServer(URI uri);

    /**
     * 列举服务器下的所有 Key
     *
     * @param uri apiUrl
     * @return keys
     */
    @GetMapping("/access-keys")
    AccessKeys listsTheAccessKeys(URI uri);

    /**
     * 删除一个 Key
     *
     * @param uri apiUrl
     * @param id  keyId
     */
    @DeleteMapping("/access-keys/{id}")
    void deletesAnAccessKey(URI uri, @PathVariable String id);

    /**
     * 新增一个 Key
     *
     * @param uri apiUrl
     * @return key info
     */
    @PostMapping("/access-keys")
    AccessKey createsANewAccessKey(URI uri);

    /**
     * 重新给 Key 命名
     *
     * @param uri apiUrl
     * @param id  keyId
     * @param key body.keyName
     */
    @PutMapping("/access-keys/{id}/name")
    void renamesAnAccessKey(URI uri, @PathVariable String id, @RequestBody AccessKey key);

    /**
     * 获取服务器下所有 Key 流量使用情况
     *
     * @param uri apiUrl
     * @return bytes transferred
     */
    @GetMapping("/metrics/transfer")
    BytesTransferred returnsTheDataTransferredPerAccessKey(URI uri);
}
