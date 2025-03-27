package priv.pront.mallchat.common.chat.service.strategy.msg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.pront.mallchat.common.chat.dao.MessageDao;
import priv.pront.mallchat.common.chat.domain.entity.Message;
import priv.pront.mallchat.common.chat.domain.entity.msg.FileMsgDTO;
import priv.pront.mallchat.common.chat.domain.entity.msg.MessageExtra;
import priv.pront.mallchat.common.chat.domain.enums.MessageTypeEnum;

import java.util.Optional;


@Component
public class FileMsgHandler extends AbstractMsgHandler<FileMsgDTO> {
    @Autowired
    private MessageDao messageDao;

    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.FILE;
    }

    @Override
    public void saveMsg(Message msg, FileMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        Message update = new Message();
        update.setId(msg.getId());
        update.setExtra(extra);
        extra.setFileMsg(body);
        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getFileMsg();
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return "文件:" + msg.getExtra().getFileMsg().getFileName();
    }

    @Override
    public String showContactMsg(Message msg) {
        return "[文件]" + msg.getExtra().getFileMsg().getFileName();
    }
}
