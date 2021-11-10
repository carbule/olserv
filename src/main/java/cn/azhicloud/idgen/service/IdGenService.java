package cn.azhicloud.idgen.service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/30 17:48
 */
public interface IdGenService {

    /**
     * id 生成
     *
     * @return seqKey
     */
    Long genNewId();
}
