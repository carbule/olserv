package cn.azhicloud.olserv.controller.housekeeping;

import cn.azhicloud.olserv.model.DoHousekeepingRequest;
import cn.azhicloud.olserv.service.housekeeping.HouseKeepingService;
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
    public void doHousekeeping(@RequestBody DoHousekeepingRequest request) {
        HouseKeepingService houseKeepingService = context.getBean(request.getServiceCode(),
                HouseKeepingService.class);
        if (request.getParams() == null) {
            houseKeepingService.doHousekeeping();
        } else {
            houseKeepingService.doHousekeeping(request.getParams());
        }
    }
}
