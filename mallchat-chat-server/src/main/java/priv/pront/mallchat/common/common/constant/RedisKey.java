package priv.pront.mallchat.common.common.constant;

public class RedisKey {

    /**
     * base key
     */
    private static final String BASE_KEY = "mallchat:chat";

    /**
     * 用户 token key
     */
    public static final String USER_TOKEN_STRING = "userToken:uid_%d";

    public static String getKey(String key, Object... o) {
        return BASE_KEY + String.format(key, o);
    }
}
