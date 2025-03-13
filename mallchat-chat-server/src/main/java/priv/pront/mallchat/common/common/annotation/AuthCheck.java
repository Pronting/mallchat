package priv.pront.mallchat.common.common.annotation;

import priv.pront.mallchat.common.user.domain.enums.RoleEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

     RoleEnum mustRole();
}
