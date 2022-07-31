package cn.azhicloud.fzero.controller;

import java.text.MessageFormat;

import cn.azhicloud.fzero.helper.NanoId;
import cn.azhicloud.fzero.helper.Validator;
import cn.azhicloud.fzero.model.CreateShortLinkRQ;
import cn.azhicloud.fzero.model.entity.ShortLink;
import cn.azhicloud.fzero.repository.ShortLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/31 00:32
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/short-link")
public class ShortLinkController {

    @Value("${hzero.service}")
    private String hzeroService;

    private final ShortLinkRepository shortLinkRepository;

    @PostMapping
    public String createShortLink(@RequestBody @Validated CreateShortLinkRQ rq,
                                  BindingResult err) {
        Validator.extractErr(err);

        ShortLink shortLink = new ShortLink();
        shortLink.setCode(NanoId.next());
        shortLink.setLink(rq.getLink());
        shortLinkRepository.save(shortLink);

        return MessageFormat.format(hzeroService, shortLink.getCode());
    }
}
