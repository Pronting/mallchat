package priv.pront.mallchat.common.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedissonLock {

    /**
     * key前缀， 默认取方法全限定名，可以自己指定
     * @return
     */
    String prefixKey() default "";

    /**
     * 支持 Spring EL 表达式的key
     * @return
     */
    String key();

    /**
     * 等待锁的排队时间，默认不等待
     * @return
     */
    int waitTime() default -1;

    /**
     * 等待锁的时间到单位，默认毫秒
     * @return
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;




}
