package priv.pront.mallchat.common.common.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import priv.pront.mallchat.common.common.domain.dto.RequestUserInfo;
import priv.pront.mallchat.common.common.util.RequestHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class CollectorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long uid = Optional.ofNullable(request.getAttribute(TokenInterceptor.UID))
                .map(Object::toString)
                .map(Long::parseLong)
                .orElse(null);
        String clientIP = ServletUtil.getClientIP(request);
        RequestHolder.set(RequestUserInfo.builder().ip(clientIP).uid(uid).build());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
      RequestHolder.remove();
    }
}
