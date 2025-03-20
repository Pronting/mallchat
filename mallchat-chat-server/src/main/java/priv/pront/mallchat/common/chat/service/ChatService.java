package priv.pront.mallchat.common.chat.service;

import priv.pront.mallchat.common.chat.domain.vo.req.ChatMessageReq;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public interface ChatService {

    /**
     * 发送消息
     *
     * @param request
     */
    Long sendMsg(ChatMessageReq request, Long uid);

}
