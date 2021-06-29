package netty.provider;

import com.netty.api.RedisService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisServiceImpl<K, V> implements RedisService<K, V> {

    private RedissonClient redisson;
    @Autowired
    public void setRedisson(RedissonClient redisson) {
        this.redisson = redisson;
    }

    //
//    @Resource
//    private RedisTemplate template;

    @Override
    public V get(K key) {
//        return (V) template.boundValueOps(key).get();
        return (V) redisson.getBucket((String) key).get();
    }

    @Override
    public boolean del(K key) {
        return redisson.getBucket((String) key).delete();
    }

    @Override
    public boolean set(K key, V value) {
        try {
//            template.boundValueOps(key).set(value);
            redisson.getBucket((String) key).set(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean set(K key, V value, int second) {
        try {
            redisson.getBucket((String) key).set(value, second, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
