package priv.pront.mallchat.common.common.service;

import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.pront.mallchat.common.common.exception.BusinessException;
import priv.pront.mallchat.common.common.exception.CommonErrorEnum;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class LockService {

    @Autowired
    private RedissonClient redissonClient;


    @SneakyThrows
    public <T> T executeWithLock(String key, int waitTime, TimeUnit timeUnit, Supplier<T> supplier)  {
        RLock lock = redissonClient.getLock(key);
        boolean success = lock.tryLock(waitTime, timeUnit);
        if (!success) throw new BusinessException(CommonErrorEnum.LOCK_LIMIT);

        try{
            return supplier.get();
        }finally {
            lock.unlock();
        }
    }


    @SneakyThrows
    public <T> T executeWithLock(String key,  Supplier<T> supplier)  {
        return executeWithLock(key, -1, TimeUnit.MINUTES, supplier);
    }

    @SneakyThrows
    public <T> T executeWithLock(String key,  Runnable runnable)  {
        return executeWithLock(key, -1, TimeUnit.MINUTES, () ->{
            runnable.run();
            return null;
        });
    }

    @FunctionalInterface
    public interface Supplier<T> {
        T get() throws Throwable;
    }

}
