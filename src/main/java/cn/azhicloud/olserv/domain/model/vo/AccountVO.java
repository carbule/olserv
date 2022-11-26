package cn.azhicloud.olserv.domain.model.vo;

import cn.azhicloud.olserv.domain.entity.Account;
import cn.azhicloud.olserv.domain.entity.Subscribe;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/11/26 10:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountVO extends Account {

    /**
     * 订阅信息
     */
    private Subscribe subscribe;
}
