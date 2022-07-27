package cn.azhicloud.olserv.infra.helper;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.RandomUtils;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/25 14:58
 */
public class SystemHelper {

    private static final AtomicLong SEQ = new AtomicLong(0L);

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    /**
     * 趋势递增序列号生成
     *
     * @return e.q. 2207251435290000
     */
    public static String nextSerialNo() {
        long nextSeq = SEQ.getAndAdd(RandomUtils.nextLong(1, 5));
        return LocalDateTime.now().format(FORMATTER) +
                new DecimalFormat("0000").format(nextSeq % 10000);
    }

    /**
     * 趋势递增序列号生成
     *
     * @return e.q. 2207251435290000
     */
    public static Long nextSerialNo2Long() {
        return Long.valueOf(nextSerialNo());
    }
}
