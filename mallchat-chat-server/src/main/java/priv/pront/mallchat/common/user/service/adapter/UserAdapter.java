package priv.pront.mallchat.common.user.service.adapter;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.beans.BeanUtils;
import priv.pront.mallchat.common.common.domain.enums.YesOrNoEnum;
import priv.pront.mallchat.common.user.domain.entity.ItemConfig;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.user.domain.entity.UserBackpack;
import priv.pront.mallchat.common.user.domain.vo.resp.BadgeResp;
import priv.pront.mallchat.common.user.domain.vo.resp.UserInfoResp;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static UserInfoResp builderUserInfo(User user, Integer countByValidItemId) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtils.copyProperties(user, userInfoResp);
        userInfoResp.setModifyNameChance(countByValidItemId);
        return userInfoResp;
    }

    public static List<BadgeResp> buildBadgeResp(List<ItemConfig> itemConfigs, List<UserBackpack> backpacks, User user) {
        Set<Long> obtainItemSet = backpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());

        return itemConfigs.stream().map(a -> {
            BadgeResp badgeResp = new BadgeResp();
            BeanUtils.copyProperties(a, badgeResp);
            badgeResp.setObtain(obtainItemSet.contains(a.getId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
            badgeResp.setWearing(Objects.equals(a.getId(), user.getItemId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
            return badgeResp;
        }).sorted(Comparator.comparing(BadgeResp::getWearing, Comparator.reverseOrder())
                .thenComparing(BadgeResp::getObtain,  Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
