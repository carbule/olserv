package cn.azhicloud.olserv.model;

import java.io.Serializable;
import java.util.List;

import cn.azhicloud.olserv.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 21:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListShadowboxesResponse extends BaseResponse {

    private static final long serialVersionUID = 8061108569843959570L;

    private List<Shadowbox> shadowboxes;

    @Data
    public static class Shadowbox implements Serializable {

        private static final long serialVersionUID = 6347859387825192548L;

        private String name;

        private String apiUrl;

        private String certSha256;
    }
}
