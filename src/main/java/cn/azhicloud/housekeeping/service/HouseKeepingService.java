package cn.azhicloud.housekeeping.service;

import java.util.Map;

/**
 * Housekeeping service interface define.
 *
 * @author zhouzhifeng
 */
public interface HouseKeepingService {

    /**
     * Do housekeeping.
     */
    default void doHousekeeping() {
        throw new RuntimeException("housekeeping service no implements");
    }

    /**
     * Do housekeeping
     *
     * @param params biz params
     */
    default void doHousekeeping(Map<String, Object> params) {
        throw new RuntimeException("housekeeping service no implements");
    }
}
