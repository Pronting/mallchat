package priv.pront.mallchat.common.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.pront.mallchat.common.user.dao.UserDao;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.user.service.UserService;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public Long register(User registerUser) {
        userDao.save(registerUser);
//        todo 用户注册的事件
        return registerUser.getId();
    }
}
