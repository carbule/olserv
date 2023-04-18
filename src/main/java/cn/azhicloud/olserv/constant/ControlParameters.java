package cn.azhicloud.olserv.constant;

/**
 * 系统控制参数常量
 *
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/12/10 15:29
 */
public interface ControlParameters {

    /**
     * 订阅链接模版
     */
    String SUBSCRIBE_URL_TEMPLATE = "SUBSCRIBE_URL_TEMPLATE";

    /**
     * outline-client 动态密钥模版
     */
    String SSCONF_URL_TEMPLATE = "SSCONF_URL_TEMPLATE";

    /**
     * ss 自定义盐前缀
     */
    String SS_SALT_PREFIX = "SS_SALT_PREFIX";
}
