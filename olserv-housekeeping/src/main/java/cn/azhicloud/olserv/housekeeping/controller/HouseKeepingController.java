package cn.azhicloud.olserv.housekeeping.controller;

import cn.azhicloud.olserv.housekeeping.model.DoHousekeepingRQ;
import cn.azhicloud.olserv.housekeeping.service.HouseKeepingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouzhifeng
 */
@RestController
@RequestMapping("/housekeeping")
@RequiredArgsConstructor
public class HouseKeepingController {

    private final ApplicationContext context;

    @PostMapping("/do")
    public Boolean doHousekeeping(@RequestBody @Validated DoHousekeepingRQ rq) {
        HouseKeepingService houseKeepingService = context.getBean(rq.getServiceCode(),
                HouseKeepingService.class);
        if (CollectionUtils.isEmpty(rq.getParams())) {
            houseKeepingService.doHousekeeping();
        } else {
            houseKeepingService.doHousekeeping(rq.getParams());
        }

        return Boolean.TRUE;
    }
}
