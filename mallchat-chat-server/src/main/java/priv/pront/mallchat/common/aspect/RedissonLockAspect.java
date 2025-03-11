package priv.pront.mallchat.common.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import priv.pront.mallchat.common.common.annotation.RedissonLock;
import priv.pront.mallchat.common.common.service.LockService;
import priv.pront.mallchat.common.common.util.SpELUtils;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(0) // 确保比事务注解先执行，分布式锁在事务外
public class RedissonLockAspect {

    @Autowired
    private LockService lockService;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) throws Throwable{
//        组装key
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String prefix = StringUtils.isBlank(redissonLock.prefixKey()) ? SpELUtils.getMethodKey(method) : redissonLock.prefixKey();
        String key = SpELUtils.parseSpEL(method, joinPoint.getArgs(), redissonLock.key());
        return lockService.executeWithLock(prefix + ":" + key, redissonLock.waitTime(), redissonLock.unit(), joinPoint::proceed);
    }
}
