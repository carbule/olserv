package cn.azhicloud.olserv;

import lombok.RequiredArgsConstructor;
import org.hashids.Hashids;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhouzhifeng
 * @version 1.0
 * @since 2021/10/30 17:55
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(HashIdsConfigurationProperties.class)
public class HashIdsConfiguration {

    private final HashIdsConfigurationProperties hashIdsConfigurationProperties;

    @Bean
    public Hashids hashids() {
        return new Hashids(hashIdsConfigurationProperties.getSalt(),
                hashIdsConfigurationProperties.getLength());
    }
}
