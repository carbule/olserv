package cn.azhicloud.sequence.service;

/**
 * @author zfzhou
 * @version 1.0
 * @since 2021/11/23 14:44
 */
public interface SequenceService {

    /**
     * 获取新序列
     *
     * @return next long
     */
    Long next();
}
