package priv.pront.mallchat.common.user.service;

public interface LoginService {

    /**
     * 用户登录
     * @param uid 用户 id
     * @return token
     */
    String login(Long uid);
}
