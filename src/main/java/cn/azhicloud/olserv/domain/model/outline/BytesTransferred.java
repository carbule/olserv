package cn.azhicloud.olserv.domain.model.outline;

import java.util.Map;

import lombok.Data;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/16 07:55
 */
@Data
public class BytesTransferred {

    /**
     * {
     * "bytesTransferredByUserId": {
     * "userId": 1024,
     * "userId1": 2045
     * }
     * }
     */
    private Map<String, Long> bytesTransferredByUserId;
}
