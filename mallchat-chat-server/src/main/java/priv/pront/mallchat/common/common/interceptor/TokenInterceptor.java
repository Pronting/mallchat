package priv.pront.mallchat.common.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import priv.pront.mallchat.common.common.exception.HttpErrorEnum;
import priv.pront.mallchat.common.user.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

@Controller
public class TokenInterceptor implements HandlerInterceptor {

    public static final String HEAD_AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_SCHEME = "Bearer ";
    public static final String UID = "uid";

    @Autowired
    private LoginService loginService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        Long validUid = loginService.getValidUid(token);
        if (Objects.nonNull(validUid)) {
            request.setAttribute(UID, validUid);
            return true;
        }else{
            boolean isPublicURI = isPublicURI(request);
            if (!isPublicURI) {
                HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
                return false;
            }
        }
        return true;
    }

    private static boolean isPublicURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        return split.length > 2 && "public".equals(split[3]);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HEAD_AUTHORIZATION);
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(AUTHORIZATION_SCHEME))
                .map(h -> h.replaceFirst(AUTHORIZATION_SCHEME, ""))
                .orElse(null);
    }
}
