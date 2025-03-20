package priv.pront.mallchat.common.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import priv.pront.mallchat.common.user.domain.entity.UserApply;

@Getter
public class UserApplyEvent extends ApplicationEvent {
    private UserApply userApply;

    public UserApplyEvent(Object source, UserApply userApply) {
        super(source);
        this.userApply = userApply;
    }

}
