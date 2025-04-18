package priv.pront.mallchat.common.user.service;

import priv.pront.mallchat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import priv.pront.mallchat.common.user.domain.vo.req.BlackReq;
import priv.pront.mallchat.common.user.domain.vo.resp.BadgeResp;
import priv.pront.mallchat.common.user.domain.vo.resp.UserInfoResp;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author <a href="https://github.com/Pronting">pront</a>
 * @since 2025-03-05
 */
public interface UserService  {

    /**
     * 用户注册的事件逻辑
     * @param registerUser 注册的用户信息
     * @return 用户 id
     */
    Long register(User registerUser);

    UserInfoResp getUserInfo(Long uid);

    void modifyName(Long uid, String name);

    List<BadgeResp> badges(Long uid);

    void wearingBadge(Long uid, Long itemId);

    void black(BlackReq req);
}
