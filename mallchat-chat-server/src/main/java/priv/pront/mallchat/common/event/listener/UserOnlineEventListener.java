package priv.pront.mallchat.common.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import priv.pront.mallchat.common.event.UserOnlineEvent;
import priv.pront.mallchat.common.event.UserRegisterEvent;
import priv.pront.mallchat.common.user.dao.UserDao;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.user.domain.enums.IdempotentEnum;
import priv.pront.mallchat.common.user.domain.enums.ItemEnum;
import priv.pront.mallchat.common.user.domain.enums.UserActiveStatusEnum;
import priv.pront.mallchat.common.user.service.IpService;
import priv.pront.mallchat.common.user.service.UserBackpackService;

@Component
public class UserOnlineEventListener {

    @Autowired
    private IpService ipService;

    @Autowired
    private UserDao userDao;

    /**
     * 更新用户上线信息
     * @param event
     */
    @Async
    @TransactionalEventListener(classes = UserOnlineEvent.class, phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void saveDB(UserOnlineEvent event) {
        User user = event.getUser();
        User update = User.builder()
                .id(user.getId())
                .lastOptTime(user.getLastOptTime())
                .ipInfo(user.getIpInfo())
                .activeStatus(UserActiveStatusEnum.ONLINE.getStatus())
                .build();
        userDao.updateById(update);
//        用户 ip 详情的解析
        ipService.refreshIpDetail(user.getId());

    }


}
