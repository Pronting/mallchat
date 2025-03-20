package priv.pront.mallchat.common.chat.service.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.pront.mallchat.common.chat.domain.entity.GroupMember;
import priv.pront.mallchat.common.chat.domain.enums.GroupRoleEnum;
import priv.pront.mallchat.common.chat.domain.vo.resp.ChatMemberListResp;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.websocket.domain.enums.WSRespTypeEnum;
import priv.pront.mallchat.common.websocket.domain.vo.resp.ChatMemberResp;
import priv.pront.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import priv.pront.mallchat.common.websocket.domain.vo.resp.WSMemberChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static priv.pront.mallchat.common.websocket.domain.vo.resp.WSMemberChange.CHANGE_TYPE_ADD;
import static priv.pront.mallchat.common.websocket.domain.vo.resp.WSMemberChange.CHANGE_TYPE_REMOVE;

@Component
@Slf4j
public class MemberAdapter {

    public static List<ChatMemberListResp> buildMemberList(List<User> memberList) {
        return memberList.stream()
                .map(a -> {
                    ChatMemberListResp resp = new ChatMemberListResp();
                    BeanUtils.copyProperties(a, resp);
                    resp.setUid(a.getId());
                    return resp;
                }).collect(Collectors.toList());
    }

}
