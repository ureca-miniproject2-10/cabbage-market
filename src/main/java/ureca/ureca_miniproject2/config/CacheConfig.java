package ureca.ureca_miniproject2.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("postViews");
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(30, TimeUnit.SECONDS)  // 30초 TTL ( 30초마다 만료되도록 )
                        .maximumSize(10000)  // 최대 10,000개의 캐시 항목
        );
        return cacheManager;
    }
}
