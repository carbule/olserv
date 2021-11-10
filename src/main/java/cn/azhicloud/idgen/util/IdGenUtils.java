package cn.azhicloud.idgen.util;

import cn.azhicloud.idgen.service.IdGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/30 18:19
 */
@Component
public class IdGenUtils {

    private static IdGenService idGenService;

    @Autowired
    public void setIdGenService(IdGenService idGenService) {
        IdGenUtils.idGenService = idGenService;
    }

    public static Long genId() {
        return idGenService.genNewId();
    }
}
