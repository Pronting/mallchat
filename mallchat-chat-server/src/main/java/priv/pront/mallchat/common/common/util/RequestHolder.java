package priv.pront.mallchat.common.common.util;

import lombok.Data;
import priv.pront.mallchat.common.common.domain.dto.RequestUserInfo;

/**
 * 请求上下文
 */

public class RequestHolder {

    private static final ThreadLocal<RequestUserInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestUserInfo requestUserInfo) {
        threadLocal.set(requestUserInfo);
    }
    public static RequestUserInfo get() {
        return threadLocal.get();
    }

    public static void remove(){
        threadLocal.remove();
    }
}
