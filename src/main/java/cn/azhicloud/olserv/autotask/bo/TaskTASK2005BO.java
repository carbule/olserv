package cn.azhicloud.olserv.autotask.bo;

import cn.azhicloud.olserv.domain.entity.PullHistory;
import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @see cn.azhicloud.olserv.constant.TaskTypeConst#PULL_HISTORY_NOTICE
 * @since 2022/7/25 18:11
 */
@Data
public class TaskTASK2005BO {

    /**
     * @see PullHistory#getId()
     */
    private Long historyId;
}
