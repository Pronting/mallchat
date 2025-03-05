package priv.pront.mallchat.common.user.service.handler;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import priv.pront.mallchat.common.user.service.WXMsgService;
import priv.pront.mallchat.common.user.service.adapter.TextBuilder;

import java.net.URLEncoder;
import java.util.Map;

@Component
@Slf4j
public class ScanHandler extends AbstractHandler {



    @Autowired
    private WXMsgService wxMsgService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        // 扫码事件处理
        return wxMsgService.scan(wxMpXmlMessage);
    }

}
