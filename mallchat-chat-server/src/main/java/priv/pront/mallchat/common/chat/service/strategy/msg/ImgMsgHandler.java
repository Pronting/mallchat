package priv.pront.mallchat.common.chat.service.strategy.msg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.pront.mallchat.common.chat.dao.MessageDao;
import priv.pront.mallchat.common.chat.domain.entity.Message;
import priv.pront.mallchat.common.chat.domain.entity.msg.ImgMsgDTO;
import priv.pront.mallchat.common.chat.domain.entity.msg.MessageExtra;
import priv.pront.mallchat.common.chat.domain.enums.MessageTypeEnum;

import java.util.Optional;


@Component
public class ImgMsgHandler extends AbstractMsgHandler<ImgMsgDTO> {
    @Autowired
    private MessageDao messageDao;

    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.IMG;
    }

    @Override
    public void saveMsg(Message msg, ImgMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        Message update = new Message();
        update.setId(msg.getId());
        update.setExtra(extra);
        extra.setImgMsgDTO(body);
        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getImgMsgDTO();
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return "图片";
    }

    @Override
    public String showContactMsg(Message msg) {
        return "[图片]";
    }
}
