package priv.pront.mallchat.common.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import priv.pront.mallchat.common.common.annotation.RedissonLock;
import priv.pront.mallchat.common.common.service.LockService;
import priv.pront.mallchat.common.user.dao.UserBackpackDao;
import priv.pront.mallchat.common.user.domain.enums.IdempotentEnum;
import priv.pront.mallchat.common.user.service.UserBackpackService;
import priv.pront.mallchat.common.user.service.adapter.UserBackpackAdapter;

import java.util.Objects;

@Service
public class UserBackpackServiceImpl implements UserBackpackService {

    @Autowired
    private LockService lockService;

    @Autowired
    private UserBackpackDao userBackpackDao;

    @Autowired
    @Lazy
    private UserBackpackServiceImpl userBackpackService; // 切面和事务会失效，需要注入自己

    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId);
        userBackpackService.doAcquireItem(uid, itemId, idempotent);

    }

    @RedissonLock(key = "#idempotent", waitTime = 5000)
    public void doAcquireItem(Long uid, Long itemId, String idempotent) {
        if (Objects.nonNull(userBackpackDao.getByIdempotent(idempotent))) return;
//          TODO  业务检查

//            发放物品
        userBackpackDao.save(UserBackpackAdapter.buildUserBackpack(uid, itemId, idempotent));
    }

    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
    }
}
