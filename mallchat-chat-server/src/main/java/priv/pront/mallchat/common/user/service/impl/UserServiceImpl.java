package priv.pront.mallchat.common.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.pront.mallchat.common.common.annotation.RedissonLock;
import priv.pront.mallchat.common.common.exception.BusinessException;
import priv.pront.mallchat.common.common.util.AssertUtil;
import priv.pront.mallchat.common.event.UserRegisterEvent;
import priv.pront.mallchat.common.user.dao.ItemConfigDao;
import priv.pront.mallchat.common.user.domain.entity.ItemConfig;
import priv.pront.mallchat.common.user.domain.entity.UserBackpack;
import priv.pront.mallchat.common.user.domain.enums.ItemEnum;
import priv.pront.mallchat.common.user.dao.UserBackpackDao;
import priv.pront.mallchat.common.user.dao.UserDao;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.user.domain.enums.ItemTypeEnum;
import priv.pront.mallchat.common.user.domain.vo.resp.BadgeResp;
import priv.pront.mallchat.common.user.domain.vo.resp.UserInfoResp;
import priv.pront.mallchat.common.user.service.UserService;
import priv.pront.mallchat.common.user.service.adapter.UserAdapter;
import priv.pront.mallchat.common.user.service.cache.ItemCache;
import springfox.documentation.annotations.Cacheable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBackpackDao userBackpackDao;

    @Autowired
    private ItemCache itemCache;

    @Autowired
    private ItemConfigDao itemConfigDao;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public Long register(User registerUser) {
        userDao.save(registerUser);
//        todo 用户注册的事件
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this, registerUser));
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
    @RedissonLock(key = "#uid")
    public void modifyName(Long uid, String name) {
        User oldUser = userDao.getByName(name);
        AssertUtil.isEmpty(oldUser, "名称重复，换个昵称吧");
        UserBackpack modifyNameItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem, "改名卡不够了，等后续活动吧");
//       使用改名卡
        boolean success = userBackpackDao.useItem(modifyNameItem);
        if (success) userDao.modifyName(uid, name);
    }

    @Override
    public List<BadgeResp> badges(Long uid) {
//        所有徽章
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
//        用户所有
        List<Long> collect = itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList());
        List<UserBackpack> backpacks = userBackpackDao.getByItemId(uid, collect);
//        用户佩戴的徽章
        User user = userDao.getById(uid);
        return UserAdapter.buildBadgeResp(itemConfigs, backpacks, user);

    }

    @Override
    public void wearingBadge(Long uid, Long itemId) {
//        确保有徽章
        UserBackpack firstValidItem = userBackpackDao.getFirstValidItem(uid, itemId);
        AssertUtil.isNotEmpty(firstValidItem, "没有这个徽章，快去获得吧");
//        确保这个物品是徽章
        ItemConfig itemConfig = itemConfigDao.getById(firstValidItem.getItemId());
        AssertUtil.equal(itemConfig.getType(), ItemTypeEnum.BADGE.getType(), "只有徽章才能佩戴");
        userDao.wearingBadge(uid, itemId);
    }
}
