package priv.pront.mallchat.common.user.service;


import priv.pront.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import priv.pront.mallchat.common.common.domain.vo.req.PageBaseReq;
import priv.pront.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import priv.pront.mallchat.common.common.domain.vo.resp.PageBaseResp;
import priv.pront.mallchat.common.user.domain.vo.req.friend.FriendApplyReq;
import priv.pront.mallchat.common.user.domain.vo.req.friend.FriendApproveReq;
import priv.pront.mallchat.common.user.domain.vo.req.friend.FriendCheckReq;
import priv.pront.mallchat.common.user.domain.vo.resp.friend.FriendApplyResp;
import priv.pront.mallchat.common.user.domain.vo.resp.friend.FriendCheckResp;
import priv.pront.mallchat.common.user.domain.vo.resp.friend.FriendResp;
import priv.pront.mallchat.common.user.domain.vo.resp.friend.FriendUnreadResp;

public interface FriendService {

    /**
     * 检查
     * 检查是否是自己好友
     *
     * @param request 请求
     * @param uid     uid
     * @return {@link FriendCheckResp}
     */
    FriendCheckResp check(Long uid, FriendCheckReq request);

    /**
     * 应用
     * 申请好友
     *
     * @param request 请求
     * @param uid     uid
     */
    void apply(Long uid, FriendApplyReq request);

    /**
     * 分页查询好友申请
     *
     * @param request 请求
     * @return {@link PageBaseResp}<{@link FriendApplyResp}>
     */
    PageBaseResp<FriendApplyResp> pageApplyFriend(Long uid, PageBaseReq request);

    /**
     * 申请未读数
     *
     * @return {@link FriendUnreadResp}
     */
    FriendUnreadResp unread(Long uid);

    /**
     * 同意好友申请
     *
     * @param uid     uid
     * @param request 请求
     */
    void applyApprove(Long uid, FriendApproveReq request);

    /**
     * 删除好友
     *
     * @param uid       uid
     * @param friendUid 朋友uid
     */
    void deleteFriend(Long uid, Long friendUid);

    CursorPageBaseResp<FriendResp> friendList(Long uid, CursorPageBaseReq request);
}
