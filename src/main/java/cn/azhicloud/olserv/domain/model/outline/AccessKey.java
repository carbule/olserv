package cn.azhicloud.olserv.domain.model.outline;

import java.net.URI;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:55
 */
@Data
public class AccessKey {

    private String id;

    /**
     * access-key 名称
     */
    private String name;

    /**
     * access-key 密码
     */
    private String password;

    /**
     * access-key 端口
     */
    private Integer port;

    /**
     * access-key 加密方式
     */
    private String method;

    /**
     * access-key 链接
     */
    private String accessUrl;

    /**
     * 使用流量字节数
     */
    private Long bytesTransferred;

    /**
     * 管理 url
     */
    private URI apiUri;

    /**
     * ss 加盐
     *
     * @param salt 盐值
     * @return 加盐处理后的 ss url
     */
    public String saltEncrypt(String salt) {
        if (salt == null) {
            return accessUrl;
        }
        return accessUrl + "&prefix=" + salt;
    }
}
