package priv.pront.mallchat.common.common.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import jodd.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import priv.pront.mallchat.common.common.domain.dto.RequestUserInfo;
import priv.pront.mallchat.common.common.exception.HttpErrorEnum;
import priv.pront.mallchat.common.common.util.RequestHolder;
import priv.pront.mallchat.common.user.domain.enums.BlackTypeEnum;
import priv.pront.mallchat.common.user.service.cache.UserCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
public class BlackInterceptor implements HandlerInterceptor {

    @Autowired
    private UserCache userCache;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<Integer, Set<String>> blackMap = userCache.getBlackMap();
        RequestUserInfo requestUserInfo = RequestHolder.get();
        if (inBlackList(requestUserInfo.getUid(), blackMap.get(BlackTypeEnum.UID.getType()))) {
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        }
        if (inBlackList(requestUserInfo.getIp(), blackMap.get(BlackTypeEnum.IP.getType()))) {
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        }
        return true;
    }

    private boolean inBlackList(Object target, Set<String> set) {
        if(Objects.isNull(target) || CollectionUtils.isEmpty(set)) return false;
        return set.contains(target.toString());
    }
}
