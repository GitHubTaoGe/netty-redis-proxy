package netty.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * description redisson配置信息
 * <p>
 * program triplezan-server
 *
 * @author yozora
 * @date 2020/05/14 16:32
 **/
@Slf4j
@Configuration
public class RedissonConfig extends CachingConfigurerSupport {

    @Value("redisson-dev.yaml")
    private String profiles;

    /**
     * 功能描述: 自定义键生成器
     *
     * @return org.springframework.com.cache.interceptor.KeyGenerator
     * @author yozora
     * @date 2020/5/25 10:40
     */
    @Bean
    public KeyGenerator myKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
//            sb.append(target.getClass().getName());
//            sb.append(method.getName());
//            sb.append("&");
            for (Object obj : params) {
                if (obj != null) {
//                    sb.append(obj.getClass().getName());
//                    sb.append("&");
//                    sb.append(JSON.toJSONString(obj));
//                    sb.append("&");
                    sb.append(String.valueOf(obj));
                }
            }
//            log.info("com.cache.redis com.cache key str: " + sb.toString());

//            log.info("com.cache.redis com.cache key sha256Hex: " + DigestUtils.sha256Hex(sb.toString()));
//            return DigestUtils.sha256Hex(sb.toString());
            return sb.toString();
        };
    }

    /**
     * 功能描述: 采用redissonClient作为缓存管理器
     *
     * @param redissonClient redisson客户端
     * @return org.springframework.com.cache.CacheManager
     * @author yozora
     * @date 2020/6/18 11:16
     */
//    @Bean
//    CacheManager cacheManager(RedissonClient redissonClient) {
//        Map<String, CacheConfig> config = new HashMap<>();
//        return new RedissonSpringCacheManager(redissonClient, config);
//    }

    /**
     * 功能描述: 采用RedisCacheManager作为缓存管理器
     *
     * @param connectionFactory RedisConnectionFactory
     * @return org.springframework.com.cache.CacheManager
     * @author yozora
     * @date 2020/5/25 9:37
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 设置默认失效时间1小时
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1));
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }

    /**
     * 功能描述: 用于配合springboot com.cache
     *
     * @param factory RedisConnectionFactory
     * @return org.springframework.data.com.cache.redis.core.RedisTemplate
     * @author yozora
     * @date 2020/5/25 9:36
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        // 创建一个模板类
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 将刚才的redis连接工厂设置到模板类中
        template.setConnectionFactory(factory);
        // 设置key的序列化器
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化器
        //使用Jackson 2，将对象序列化为JSON
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        //json转对象类，不设置默认的会将json转成hashmap
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        return template;
    }

    /**
     * 功能描述: 单机配置模式(redisson-dev.yaml)/哨兵配置模式(redisson-prod.yaml)/主从配置模式(redisson-masterSlave.yaml)
     *
     * @return org.redisson.com.cache.api.RedissonClient
     * @author yozora
     * @date 2020/5/15 18:06
     * @see <a href="https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95">https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95</a>
     */
    @Bean
    RedissonClient redisson() throws Exception {
        Config config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource("redisson-dev.yaml"));
        config.setThreads(0);
        config.setTransportMode(TransportMode.NIO);

        config.setCodec(new JsonJacksonCodec());

        return Redisson.create(config);
    }

}
