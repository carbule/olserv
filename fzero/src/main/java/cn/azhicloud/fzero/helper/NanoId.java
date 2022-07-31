package cn.azhicloud.fzero.helper;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;


/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/30 21:23
 */
public class NanoId {

    /**
     * The default alphabet used by this class.
     * Creates url-friendly NanoId Strings using 64 unique symbols.
     */
    public static final char[] DEFAULT_ALPHABET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * The default size used by this class.
     * Creates NanoId Strings with slightly more unique values than UUID v4.
     */
    public static final int DEFAULT_SIZE = 10;

    public static String next() {
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, DEFAULT_SIZE);
    }
}
