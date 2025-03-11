package priv.pront.mallchat.common.user.service.adapter;

import priv.pront.mallchat.common.common.domain.enums.YesOrNoEnum;
import priv.pront.mallchat.common.user.domain.entity.UserBackpack;

public class UserBackpackAdapter {

    public static UserBackpack buildUserBackpack(Long uid, Long itemId, String idempotent){
       return UserBackpack.builder()
                .uid(uid)
                .itemId(itemId)
                .idempotent(idempotent)
                .status(YesOrNoEnum.NO.getStatus())
                .build();
    }
}
