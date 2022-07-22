package cn.azhicloud.olserv.model.outline;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 21:07
 */
@Data
public class AccessKeys implements Serializable {

    private static final long serialVersionUID = 6496397816776297822L;

    private List<AccessKey> accessKeys;
}
