package cn.azhicloud.olserv.controller;

import javax.validation.constraints.NotBlank;

import cn.azhicloud.olserv.BaseResponse;
import cn.azhicloud.olserv.model.AddShadowboxRequest;
import cn.azhicloud.olserv.model.ListShadowboxesResponse;
import cn.azhicloud.olserv.service.ShadowboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:39
 */
@RestController
@RequestMapping("/shadowbox")
@RequiredArgsConstructor
@Validated
public class ShadowboxController {

    private final ShadowboxService shadowboxService;

    @PostMapping("/add")
    public BaseResponse addShadowbox(@RequestBody @Validated AddShadowboxRequest request) {
        shadowboxService.addShadowbox(request);
        return new BaseResponse();
    }

    @GetMapping("/add")
    public BaseResponse addShadowboxGetMethod(@NotBlank(message = "apiUrl.null") String apiUrl) {
        AddShadowboxRequest request = new AddShadowboxRequest();
        request.setApiUrl(apiUrl);

        shadowboxService.addShadowbox(request);
        return new BaseResponse();
    }

    @GetMapping("/list")
    public ListShadowboxesResponse listShadowboxes() {
        return shadowboxService.listShadowboxes();
    }
}
