package priv.pront.mallchat.common.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import priv.pront.mallchat.common.user.domain.enums.RoleEnum;
import priv.pront.mallchat.common.user.service.RoleService;
import priv.pront.mallchat.common.user.service.cache.UserCache;

import java.util.Set;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/Pronting">pront</a>
 * @since 2025-03-13
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UserCache userCache;

    @Override
    public boolean hasPower(Long uid, RoleEnum roleEnum) {
        Set<Long> roleSet = userCache.getRoleSetByUid(uid);
        return isAdmin(roleSet) || roleSet.contains(roleEnum.getId());
    }

    private boolean isAdmin(Set<Long> roleSet){
        return roleSet.contains(RoleEnum.ADMIN.getId());
    }




}
