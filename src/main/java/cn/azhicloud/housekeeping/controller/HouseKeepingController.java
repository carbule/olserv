package cn.azhicloud.housekeeping.controller;

import cn.azhicloud.housekeeping.model.DoHousekeepingRQ;
import cn.azhicloud.housekeeping.service.HouseKeepingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
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
    public Boolean doHousekeeping(@RequestBody DoHousekeepingRQ request) {
        HouseKeepingService houseKeepingService = context.getBean(request.getServiceCode(),
                HouseKeepingService.class);
        if (request.getParams() == null) {
            houseKeepingService.doHousekeeping();
        } else {
            houseKeepingService.doHousekeeping(request.getParams());
        }

        return Boolean.TRUE;
    }
}
