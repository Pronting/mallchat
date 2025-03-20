package priv.pront.mallchat.common.common.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import priv.pront.mallchat.common.common.event.UserBlackEvent;
import priv.pront.mallchat.common.user.dao.UserDao;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.user.service.cache.UserCache;
import priv.pront.mallchat.common.websocket.service.WebSocketService;
import priv.pront.mallchat.common.websocket.service.adapter.WebSocketAdapter;

@Component
public class UseBlackListener {

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCache userCache;


    /**
     * 用户被封之后给前端发信息
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void sendMsg(UserBlackEvent event) {
        User user = event.getUser();
        webSocketService.sendMsgToAll(WebSocketAdapter.buildBlack(user));

    }


    /**
     * 用户被封之后修改用户状态
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void changeUserStatus(UserBlackEvent event) {
        userDao.invalidUid(event.getUser().getId());
    }

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase = TransactionPhase.AFTER_COMMIT)
    public void evictCache(UserBlackEvent event) {
        userCache.evictBlackMap();
    }


}
