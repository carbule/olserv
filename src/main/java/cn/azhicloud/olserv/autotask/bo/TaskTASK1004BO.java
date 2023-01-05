package cn.azhicloud.olserv.autotask.bo;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @see cn.azhicloud.olserv.autotask.AutoTaskTASK1004ServiceImpl
 * @since 2023/1/5 15:01
 */
@Data
public class TaskTASK1004BO {

    /**
     * 服务器唯一标识
     */
    private String serverId;

    /**
     * 是否离线
     */
    private Boolean offline;
}
