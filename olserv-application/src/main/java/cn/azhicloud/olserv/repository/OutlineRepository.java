package cn.azhicloud.olserv.repository;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import cn.azhicloud.olserv.infra.exception.BizException;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.model.outline.AccessKeys;
import cn.azhicloud.olserv.model.outline.BytesTransferred;
import cn.azhicloud.olserv.model.outline.Server;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/6/3 21:03
 */
@FeignClient(value = "outline", url = "outline")
@CacheConfig(cacheNames = "outline:repository")
public interface OutlineRepository {

    /**
     * 获取服务器基础信息
     *
     * @param uri apiUrl
     * @return server info
     */
    @GetMapping("/server")
    @Cacheable(key = "#uri.toString()")
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

    /**
     * 删除一个 Key
     *
     * @param uri     api
     * @param keyName key name
     */
    @CacheEvict(key = "#uri.toString() + #keyName")
    default void deleteAccessKey(URI uri, String keyName) {
        List<AccessKey> accessKeys = listsTheAccessKeys(uri).getAccessKeys();

        accessKeys.stream().filter(k -> Objects.equals(k.getName(), keyName))
                .findFirst().ifPresent(k -> {
                    deletesAnAccessKey(uri, k.getId());
                });
    }

    /**
     * 新增一个 Key
     *
     * @param uri     shadowbox api
     * @param keyName key name
     * @return key
     */
    @CachePut(key = "#uri.toString() + #keyName")
    default AccessKey createAccessKey(URI uri, String keyName) {
        // Key 名称校验
        AccessKeys accessKeys = listsTheAccessKeys(uri);
        boolean match = accessKeys.getAccessKeys().stream()
                .anyMatch(k -> Objects.equals(k.getName(), keyName));
        if (match) {
            throw BizException.format("Key %s 名称重复", keyName);
        }

        // 创建一个新的 Key
        AccessKey accessKey = createsANewAccessKey(uri);

        // 设置 key 的名称
        AccessKey renameBody = new AccessKey();
        renameBody.setName(keyName);
        try {
            renamesAnAccessKey(uri, accessKey.getId(), renameBody);
        } catch (Exception e) {
            // 如果设置名称失败，删除先前创建的 key
            deletesAnAccessKey(uri, accessKey.getId());
            throw e;
        }

        return accessKey;
    }

    /**
     * 获取 Key
     *
     * @param uri     api
     * @param keyName key name
     * @return key
     */
    @Cacheable(key = "#uri.toString() + #keyName")
    default AccessKey getAccessKey(URI uri, String keyName) {
        List<AccessKey> accessKeys = listsTheAccessKeys(uri).getAccessKeys();
        return accessKeys.stream().filter(k -> Objects.equals(k.getName(), keyName))
                .findFirst().orElse(null);
    }

    /**
     * 清空缓存
     */
    @CacheEvict(allEntries = true)
    default void clearCache() {
    }
}
