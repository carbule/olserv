package cn.azhicloud.olserv.service.impl.autotask.bo;

import java.util.List;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/18 13:47
 */
@Data
public class TaskTASK2002BO {

    /**
     * 账户 ID
     */
    private String accountId;

    /**
     * 节点名称
     */
    private List<String> nodes;
}
