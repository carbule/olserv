package cn.azhicloud.olserv.model;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cn.azhicloud.olserv.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:40
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateAccountRequest extends BaseRequest {

    private static final long serialVersionUID = -3608079425115948698L;

    @NotNull(message = "names.null")
    @Size(min = 1, message = "names.null")
    private List<String> names;
}
