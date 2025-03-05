package priv.pront.mallchat.common.user.service;

import priv.pront.mallchat.common.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
