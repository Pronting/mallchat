package priv.pront.mallchat.common.user.service;

public interface LoginService {

    /**
     * 用户登录, 获取token
     *
     * @param uid 用户 id
     * @return token
     */
    String login(Long uid);


    /**
     * 刷新token有效期
     *
     * @param token
     */
    void renewalTokenIfNecessary(String token);

    /**
     * 如果token有效，返回uid
     *
     * @param token
     * @return
     */
    Long getValidUid(String token);
}
