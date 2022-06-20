package cn.azhicloud.olserv.service.impl;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.azhicloud.olserv.model.entity.Shadowbox;
import cn.azhicloud.olserv.model.outline.AccessKey;
import cn.azhicloud.olserv.model.outline.Server;
import cn.azhicloud.olserv.repository.ShadowboxRepository;
import cn.azhicloud.olserv.service.AccountService;
import cn.azhicloud.olserv.service.OutlineFeignClient;
import cn.azhicloud.olserv.service.ShadowboxService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/3 20:39
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ShadowboxServiceImpl implements ShadowboxService {

    private final ShadowboxRepository shadowboxRepository;

    private final OutlineFeignClient outlineFeignClient;

    private final AccountService accountService;

    @Override
    public Shadowbox addShadowbox(String apiUrl) {
        if (shadowboxRepository.existsById(apiUrl)) {
            throw new RuntimeException("repeat");
        }

        Server server = outlineFeignClient.returnsInformationAboutTheServer(URI.create(apiUrl));

        Shadowbox shadowbox = new Shadowbox();
        shadowbox.setApiUrl(apiUrl);
        BeanUtils.copyProperties(server, shadowbox);
        Shadowbox saved = shadowboxRepository.save(shadowbox);
        accountService.createAccessKeyForAllAccounts(saved);
        return saved;
    }

    @Override
    @SneakyThrows
    @Transactional
    public List<Shadowbox> listShadowboxes() {
        List<Shadowbox> shadowboxes = shadowboxRepository.findAll();
        CountDownLatch latch = new CountDownLatch(shadowboxes.size());
        ExecutorService executor = Executors.newCachedThreadPool();
        for (Iterator<Shadowbox> it = shadowboxes.iterator(); it.hasNext(); ) {
            Shadowbox box = it.next();
            executor.execute(() -> {
                try {
                    URI uri = URI.create(box.getApiUrl());
                    // 如果服务端有变更，托管态实体自动更新
                    BeanUtils.copyProperties(outlineFeignClient.returnsInformationAboutTheServer(uri), box);
                    box.setAccessKeys(outlineFeignClient.listsTheAccessKeys(uri)
                            .getAccessKeys());
                } catch (Exception e) {
                    log.error("call api {} failed", box.getApiUrl(), e);
                    it.remove();
                }
                latch.countDown();
            });
        }
        latch.await();
        return shadowboxes;
    }

    @Override
    @SneakyThrows
    public void createAccessKeyForAllShadowbox(String keyName) {
        List<Shadowbox> shadowboxes = shadowboxRepository.findAll();
        CountDownLatch latch = new CountDownLatch(shadowboxes.size());
        ExecutorService executor = Executors.newCachedThreadPool();
        shadowboxes.forEach(box -> {
            executor.execute(() -> {
                try {
                    URI uri = URI.create(box.getApiUrl());
                    // 新增一个 key
                    AccessKey accessKey = outlineFeignClient.createsANewAccessKey(uri);

                    // 设置 key 的名称
                    AccessKey renameBody = new AccessKey();
                    renameBody.setName(keyName);
                    try {
                        outlineFeignClient.renamesAnAccessKey(uri, accessKey.getId(), renameBody);
                    } catch (Exception e) {
                        // 如果设置名称失败，删除先前创建的 key
                        outlineFeignClient.deletesAnAccessKey(uri, accessKey.getId());
                        throw e;
                    }
                } catch (Exception e) {
                    log.error("call api {} failed", box.getApiUrl(), e);
                }
                latch.countDown();
            });
        });
        latch.await();
    }
}
