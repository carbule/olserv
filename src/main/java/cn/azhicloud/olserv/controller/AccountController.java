package cn.azhicloud.olserv.controller;

import java.util.List;

import cn.azhicloud.olserv.model.entity.Account;
import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/create/{username}")
    public Account createNew(@PathVariable String username) {
        return accountService.createAccount(username);
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
