package cn.azhicloud.olserv.model.outline;

import java.util.Map;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/16 07:55
 */
@Data
public class BytesTransferred {

    private Map<String, Long> bytesTransferredByUserId;
}
