package priv.pront.mallchat.common.common.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import priv.pront.mallchat.common.common.event.UserApplyEvent;
import priv.pront.mallchat.common.user.dao.UserApplyDao;
import priv.pront.mallchat.common.user.domain.entity.UserApply;
import priv.pront.mallchat.common.websocket.domain.vo.resp.WSFriendApply;
import priv.pront.mallchat.common.websocket.service.WebSocketService;

@Slf4j
@Component
public class UserApplyListener {
    @Autowired
    private UserApplyDao userApplyDao;
    @Autowired
    private WebSocketService webSocketService;

//    @Autowired
//    private PushService pushService;

    @Async
    @TransactionalEventListener(classes = UserApplyEvent.class, fallbackExecution = true)
    public void notifyFriend(UserApplyEvent event) {
        UserApply userApply = event.getUserApply();
        Integer unReadCount = userApplyDao.getUnReadCount(userApply.getTargetId());
//        pushService.sendPushMsg(WSAdapter.buildApplySend(new WSFriendApply(userApply.getUid(), unReadCount)), userApply.getTargetId());
    }

}
