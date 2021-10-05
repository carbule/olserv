package cn.azhicloud.olserv.service.impl;

import cn.azhicloud.olserv.repository.AccessKeyRepos;
import cn.azhicloud.olserv.service.AccessKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/5 21:15
 */
@Service
@RequiredArgsConstructor
public class AccessKeyServiceImpl implements AccessKeyService {

    private final AccessKeyRepos accessKeyRepos;
}
