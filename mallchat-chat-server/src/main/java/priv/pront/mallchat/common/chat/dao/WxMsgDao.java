package priv.pront.mallchat.common.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import priv.pront.mallchat.common.chat.domain.entity.WxMsg;
import priv.pront.mallchat.common.chat.mapper.WxMsgMapper;

@Service
public class WxMsgDao extends ServiceImpl<WxMsgMapper, WxMsg> {

}
