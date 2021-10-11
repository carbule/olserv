package cn.azhicloud.olserv.model;

import javax.validation.constraints.NotBlank;

import cn.azhicloud.olserv.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddShadowboxRequest extends BaseRequest {

    private static final long serialVersionUID = 8391656457471030005L;

    @NotBlank(message = "apiUrl.null")
    private String apiUrl;

    private String certSha256;
}
