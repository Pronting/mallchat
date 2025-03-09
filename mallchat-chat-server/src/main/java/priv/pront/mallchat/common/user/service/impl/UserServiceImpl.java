package priv.pront.mallchat.common.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.pront.mallchat.common.common.exception.BusinessException;
import priv.pront.mallchat.common.common.util.AssertUtil;
import priv.pront.mallchat.common.user.domain.entity.UserBackpack;
import priv.pront.mallchat.common.user.domain.enums.ItemEnum;
import priv.pront.mallchat.common.user.dao.UserBackpackDao;
import priv.pront.mallchat.common.user.dao.UserDao;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.user.domain.vo.resp.UserInfoResp;
import priv.pront.mallchat.common.user.service.UserService;
import priv.pront.mallchat.common.user.service.adapter.UserAdapter;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBackpackDao userBackpackDao;

    @Override
    @Transactional
    public Long register(User registerUser) {
        userDao.save(registerUser);
//        todo 用户注册的事件
        return registerUser.getId();
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer countByValidItemId = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.builderUserInfo(user, countByValidItemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyName(Long uid, String name) {
        User oldUser = userDao.getByName(name);
        AssertUtil.isEmpty(oldUser, "名称重复，换个昵称吧");
        UserBackpack modifyNameItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem, "改名卡不够了，等后续活动吧");
//       使用改名卡
        boolean success = userBackpackDao.useItem(modifyNameItem);
        if (success) userDao.modifyName(uid, name);
    }
}
