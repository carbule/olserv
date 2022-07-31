package cn.azhicloud.fzero;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.azhicloud.fzero.model.CreateShortLinkRQ;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2022/7/31 13:20
 */
@Slf4j
public class ShortLinkControllerTests {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public static void main(String[] args) {
        String to = "http://interface.azhicloud.cn:9999/0000/short-link";

        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                for (; ; ) {
                    CreateShortLinkRQ rq = new CreateShortLinkRQ();
                    rq.setLink("https://www.baidu.com");
                    RequestEntity<String> request = RequestEntity.post(URI.create(to))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(JSON.toJSONString(rq));

                    String response = REST_TEMPLATE.postForObject(to, request, String.class);
                    log.info(response);
                }
            });
        }
    }
}
