package priv.pront.mallchat.common.user.service;

import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

public interface WXMsgService {

    /**
     * 用户扫码处理
     *
     * @param wxMpXmlMessage
     * @return
     */
    WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage);

    /**
     * 用户扫码之后授权逻辑
     *
     * @param userInfo
     */
    void authorize(WxOAuth2UserInfo userInfo);
}
