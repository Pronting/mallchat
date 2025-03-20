package priv.pront.mallchat.common.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.pront.mallchat.common.common.annotation.AuthCheck;
import priv.pront.mallchat.common.common.exception.BusinessException;
import priv.pront.mallchat.common.common.exception.HttpErrorEnum;
import priv.pront.mallchat.common.common.util.AssertUtil;
import priv.pront.mallchat.common.common.util.RequestHolder;
import priv.pront.mallchat.common.user.service.RoleService;

@Aspect
@Component
public class AuthCheckAspect {


    @Autowired
    private RoleService roleService;

    @Around("@annotation(authCheck)")
    private Object check(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable{
        Long uid = RequestHolder.get().getUid();
        if (roleService.hasPower(uid, authCheck.mustRole())) {
            return joinPoint.proceed();
        }else {
            AssertUtil.isTrue(false, "抹茶管理员没权限");
            return null;
        }
    }

    private Object throwAuthException(){
        throw new BusinessException(HttpErrorEnum.ACCESS_DENIED.getErrorCode(), HttpErrorEnum.ACCESS_DENIED.getErrorMsg());
    }
}
