package priv.pront.mallchat.common.chat.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.pront.mallchat.common.chat.domain.entity.GroupMember;
import priv.pront.mallchat.common.chat.domain.entity.Room;
import priv.pront.mallchat.common.chat.domain.entity.RoomFriend;
import priv.pront.mallchat.common.chat.domain.entity.RoomGroup;
import priv.pront.mallchat.common.chat.domain.vo.req.ChatMessageReq;
import priv.pront.mallchat.common.chat.service.ChatService;
import priv.pront.mallchat.common.common.domain.enums.NormalOrNoEnum;
import priv.pront.mallchat.common.common.event.MessageSendEvent;
import priv.pront.mallchat.common.common.util.AssertUtil;



@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 发送消息
     */
    @Override
    @Transactional
    public Long sendMsg(ChatMessageReq request, Long uid) {
        check(request, uid);
        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNoNull(request.getMsgType());
        Long msgId = msgHandler.checkAndSaveMsg(request, uid);
        //发布消息发送事件
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, msgId));
        return msgId;
    }

    private void check(ChatMessageReq request, Long uid) {
        Room room = roomCache.get(request.getRoomId());
        if (room.isHotRoom()) {//全员群跳过校验
            return;
        }
        if (room.isRoomFriend()) {
            RoomFriend roomFriend = roomFriendDao.getByRoomId(request.getRoomId());
            AssertUtil.equal(NormalOrNoEnum.NORMAL.getStatus(), roomFriend.getStatus(), "您已经被对方拉黑");
            AssertUtil.isTrue(uid.equals(roomFriend.getUid1()) || uid.equals(roomFriend.getUid2()), "您已经被对方拉黑");
        }
        if (room.isRoomGroup()) {
            RoomGroup roomGroup = roomGroupCache.get(request.getRoomId());
            GroupMember member = groupMemberDao.getMember(roomGroup.getId(), uid);
            AssertUtil.isNotEmpty(member, "您已经被移除该群");
        }

    }



}
