package priv.pront.mallchat.common.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.user.domain.entity.UserRole;
import priv.pront.mallchat.common.user.mapper.UserMapper;
import priv.pront.mallchat.common.user.mapper.UserRoleMapper;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/Pronting">pront</a>
 * @since 2025-03-05
 */
@Service
public class UserRoleDao extends ServiceImpl<UserRoleMapper, UserRole> {

    public List<UserRole> listByUid(Long uid) {
        return lambdaQuery()
                .eq(UserRole::getUid, uid)
                .list();
    }

}
