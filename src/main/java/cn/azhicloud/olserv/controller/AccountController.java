package cn.azhicloud.olserv.controller;

import javax.validation.constraints.NotBlank;

import cn.azhicloud.olserv.BaseResponse;
import cn.azhicloud.olserv.model.CreateAccountRequest;
import cn.azhicloud.olserv.model.ListAccessKeysResponse;
import cn.azhicloud.olserv.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 17:37
 */
@RestController
@RequestMapping("/account")
@AllArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public BaseResponse createNew(@RequestBody
                                  @Validated CreateAccountRequest request) {
        return accountService.createNew(request);
    }

    @GetMapping("/list")
    public BaseResponse listAccount() {
        return accountService.listAccounts();
    }

    @GetMapping("/access-keys")
    public ListAccessKeysResponse listAccessKeys(@RequestParam(value = "hid", required = false)
                                                 @NotBlank(message = "hid.null") String hid) {
        return accountService.listAccessKeys(hid);
    }

    @GetMapping("/access-keys/url")
    public String getAccessKeysUrl(@RequestParam(value = "hid", required = false)
                                   @NotBlank(message = "hid.null") String hid) {
        return accountService.getAccessKeysUrl(hid);
    }
}
