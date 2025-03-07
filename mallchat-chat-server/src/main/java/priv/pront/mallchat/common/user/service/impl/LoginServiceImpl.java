package priv.pront.mallchat.common.user.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import priv.pront.mallchat.common.common.constant.RedisKey;
import priv.pront.mallchat.common.common.util.JwtUtils;
import priv.pront.mallchat.common.common.util.RedisUtils;
import priv.pront.mallchat.common.user.service.LoginService;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    /**
     * 默认 Token 过期时间
     */
    public static final int TOKEN_EXPIRE_DAYS = 3;

    /**
     * Token 刷新时间 (小于则刷新)
     */
    public static final int TOKEN_RENEWAL_DAYS = 1;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        RedisUtils.set(getUserTokenKey(uid), token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }


    @Override
    @Async
    public void renewalTokenIfNecessary(String token) {
        Long validUid = getValidUid(token);
        String userTokenKey = getUserTokenKey(validUid);
        Long expireDays = RedisUtils.getExpire(userTokenKey, TimeUnit.DAYS);
//        不存在 key
        if (expireDays == -2) return;
        if (expireDays < TOKEN_RENEWAL_DAYS) {
            RedisUtils.expire(getUserTokenKey(validUid), TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        }

    }

    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)) return null;
        String oldToken = RedisUtils.get(getUserTokenKey(uid));
        if(StringUtils.isBlank(oldToken)) return null;
        return Objects.equals(oldToken, token) ? uid : null;
    }

    private String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);

    }
}
