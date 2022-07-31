package cn.azhicloud.fzero.controller;

import java.net.URI;
import javax.servlet.http.HttpServletRequest;

import cn.azhicloud.fzero.model.entity.ShortLink;
import cn.azhicloud.fzero.repository.OutRepository;
import cn.azhicloud.fzero.repository.ShortLinkRepository;
import cn.hutool.extra.servlet.ServletUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouzhifeng
 */
@RestController
@RequiredArgsConstructor
public class IndexController {

    private final HttpServletRequest httpServletRequest;

    private final OutRepository outRepository;

    private final ShortLinkRepository shortLinkRepository;

    @GetMapping
    public String index() {
        return "Welcome to FZero !";
    }

    @GetMapping("/{code}")
    public String get(@PathVariable String code) {
        ShortLink shortLink = shortLinkRepository.findByCode(code);
        if (shortLink == null) {
            throw new RuntimeException("Can't find entity.");
        }

        return outRepository.get(URI.create(shortLink.getLink()),
                ServletUtil.getClientIP(httpServletRequest));
    }

    @PostMapping("/{code}")
    public String post(@PathVariable String code) {
        ShortLink shortLink = shortLinkRepository.findByCode(code);
        if (shortLink == null) {
            throw new RuntimeException("Can't find entity.");
        }

        return outRepository.post(URI.create(shortLink.getLink()),
                ServletUtil.getClientIP(httpServletRequest));
    }
}
