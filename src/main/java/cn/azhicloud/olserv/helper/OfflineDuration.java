package cn.azhicloud.olserv.helper;

import java.util.concurrent.TimeUnit;

import cn.azhicloud.infra.base.exception.BizException;
import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2023/1/5 16:39
 */
@Data
public class OfflineDuration {

    /**
     * 持续时间
     */
    private int durations;

    /**
     * 时间单位
     */
    private TimeUnit unit;

    public OfflineDuration() {
        this.durations = 5;
        this.unit = TimeUnit.MINUTES;
    }

    public OfflineDuration(int durations, String unit) {
        this.durations = durations;
        this.unit = TimeUnit.valueOf(unit);
    }

    public OfflineDuration(int durations, TimeUnit unit) {
        this.durations = durations;
        this.unit = unit;
    }

    /**
     * 离线时间轮：5分钟、10分钟、30分钟、1小时、2小时、5小时、10小时、1天、2天、5天、10天、永久
     */
    public OfflineDuration nextOfflineDurations() {
        switch (unit) {
            case MINUTES:
                switch (durations) {
                    case 5:
                        return new OfflineDuration(10, TimeUnit.MINUTES);
                    case 10:
                        return new OfflineDuration(30, TimeUnit.MINUTES);
                    case 30:
                        return new OfflineDuration(1, TimeUnit.HOURS);
                }
            case HOURS:
                switch (durations) {
                    case 1:
                        return new OfflineDuration(2, TimeUnit.HOURS);
                    case 2:
                        return new OfflineDuration(5, TimeUnit.HOURS);
                    case 5:
                        return new OfflineDuration(10, TimeUnit.HOURS);
                    case 10:
                        return new OfflineDuration(1, TimeUnit.DAYS);
                }
            case DAYS:
                switch (durations) {
                    case 1:
                        return new OfflineDuration(1, TimeUnit.DAYS);
                    case 2:
                        return new OfflineDuration(5, TimeUnit.DAYS);
                    case 5:
                        return new OfflineDuration(10, TimeUnit.DAYS);
                    case 10:
                        return new OfflineDuration(9999, TimeUnit.DAYS);
                }
        }

        throw BizException.format("不支持的时间轮 %s %s", durations, unit);
    }
}
