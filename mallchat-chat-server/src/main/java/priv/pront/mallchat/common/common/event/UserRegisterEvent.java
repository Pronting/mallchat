package priv.pront.mallchat.common.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import priv.pront.mallchat.common.user.domain.entity.User;

@Getter
public class UserRegisterEvent extends ApplicationEvent {

    private User user;
    public UserRegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
