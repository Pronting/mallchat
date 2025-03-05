package priv.pront.mallchat.common.user.service.adapter;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import priv.pront.mallchat.common.user.domain.entity.User;

public class UserAdapter {

    public static User buildUserSave(String openId) {
        return User.builder().openId(openId).build();
    }

    public static User buildAuthorizeUser(Long uid, WxOAuth2UserInfo userInfo) {
        return User.builder()
                .id(uid)
                .name(userInfo.getNickname())
                .avatar(userInfo.getHeadImgUrl())
                .build();
    }
}
