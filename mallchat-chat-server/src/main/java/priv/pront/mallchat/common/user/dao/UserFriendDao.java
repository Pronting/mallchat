package priv.pront.mallchat.common.user.dao;

import priv.pront.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import priv.pront.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import priv.pront.mallchat.common.common.util.CursorUtils;
import priv.pront.mallchat.common.user.domain.entity.UserFriend;
import priv.pront.mallchat.common.user.mapper.UserFriendMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserFriendDao extends ServiceImpl<UserFriendMapper, UserFriend> {

    public List<UserFriend> getByFriends(Long uid, List<Long> uidList) {
        return lambdaQuery().eq(UserFriend::getUid, uid)
                .in(UserFriend::getFriendUid, uidList)
                .list();
    }

    public UserFriend getByFriend(Long uid, Long targetUid) {
        return lambdaQuery().eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, targetUid)
                .one();
    }

    public CursorPageBaseResp<UserFriend> getFriendPage(Long uid, CursorPageBaseReq cursorPageBaseReq) {
        return CursorUtils.getCursorPageByMysql(this, cursorPageBaseReq,
                wrapper -> wrapper.eq(UserFriend::getUid, uid), UserFriend::getId);
    }

    public List<UserFriend> getUserFriend(Long uid, Long friendUid) {
        return lambdaQuery()
                .eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, friendUid)
                .or()
                .eq(UserFriend::getFriendUid, uid)
                .eq(UserFriend::getUid, friendUid)
                .select(UserFriend::getId)
                .list();
    }

}
