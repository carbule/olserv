package cn.azhicloud.olserv.controller;

import java.util.List;
import javax.validation.constraints.NotBlank;

import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.service.ShadowboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/add")
    public Shadowbox addShadowboxGetMethod(@NotBlank(message = "apiUrl 不能为空") String apiUrl) {
        return shadowboxService.addShadowbox(apiUrl);
    }

    @GetMapping("/list")
    public List<Shadowbox> listShadowboxAccessKeys() {
        return shadowboxService.listShadowboxes();
    }
}
