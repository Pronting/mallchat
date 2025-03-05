package priv.pront.mallchat.common.websocket.service.adapter;

import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.websocket.domain.enums.WSRespTypeEnum;
import priv.pront.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import priv.pront.mallchat.common.websocket.domain.vo.resp.WSLoginSuccess;
import priv.pront.mallchat.common.websocket.domain.vo.resp.WSLoginUrl;

public class WebSocketAdapter {

    public static WSBaseResp<?> buildResq(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        resp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));
        return resp;
    }

    public static WSBaseResp<?> buildResq(User user, String token) {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        WSLoginSuccess build = WSLoginSuccess.builder()
                .avatar(user.getAvatar())
                .name(user.getName())
                .token(token)
                .uid(user.getId())
                .build();
        resp.setData(build);
        return resp;
    }

    public static WSBaseResp<?> buildWaitAuthorizeResq() {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return resp;

    }
}
