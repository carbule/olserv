package cn.azhicloud.olserv.controller;

import java.util.List;

import cn.azhicloud.olserv.model.CreateAccountRQ;
import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
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
    public Account createNew(@RequestBody @Validated CreateAccountRQ rq) {
        return accountService.createAccount(rq);
    }

    @GetMapping("/{username}/reset")
    public Boolean trafficReset(@PathVariable String username) {
        accountService.trafficReset(username);
        return Boolean.TRUE;
    }

    @GetMapping("/list")
    public List<Account> listAccount() {
        return accountService.listAccounts();
    }


    @GetMapping("/{accountId}/access-keys")
    public List<Shadowbox> listAccessKeys(@PathVariable String accountId) {
        return accountService.listShadowboxOwnedByAccount(accountId);
    }

    @GetMapping("/{accountId}/access-keys/subscribe")
    public String getAccessKeysSubscribe(@PathVariable String accountId) {
        return accountService.getAccessKeysSubscribe(accountId);
    }
}
