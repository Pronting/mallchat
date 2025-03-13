package priv.pront.mallchat.common.user.service;

import priv.pront.mallchat.common.user.domain.enums.RoleEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author <a href="https://github.com/Pronting">pront</a>
 * @since 2025-03-13
 */
public interface RoleService {

    /**
     * 是否拥有某个权限
     * @param uid
     * @param roleEnum
     * @return
     */
    boolean hasPower(Long uid, RoleEnum roleEnum);


}
