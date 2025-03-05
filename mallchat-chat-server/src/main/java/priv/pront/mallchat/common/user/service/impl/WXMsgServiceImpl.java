package priv.pront.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import priv.pront.mallchat.common.user.dao.UserDao;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.user.service.UserService;
import priv.pront.mallchat.common.user.service.WXMsgService;
import priv.pront.mallchat.common.user.service.adapter.TextBuilder;
import priv.pront.mallchat.common.user.service.adapter.UserAdapter;
import priv.pront.mallchat.common.websocket.service.WebSocketService;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WXMsgServiceImpl implements WXMsgService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Autowired
    private WebSocketService webSocketService;

    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";

    /**
     * openid 和登录 code 关系 map
     */
    private static final ConcurrentHashMap<String, Integer> WAIT_AUTHORIZE_MAP = new ConcurrentHashMap<>();

    @Value("${wx.mp.callback}")
    private String callback;



    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage) {
        String openId = wxMpXmlMessage.getFromUser();
        Integer code = getEventKey(wxMpXmlMessage);
        if(Objects.isNull(code)) return null;
        User user = userDao.getByOpenId(openId);
//        用户成功注册并授权
        boolean registered = Objects.nonNull(user);
        boolean authorize = StrUtil.isNotBlank(registered ? user.getAvatar() : "");

        if (registered && authorize) {
//            TODO 通过 code 找到 channel 给前端推送消息
            webSocketService.scanLoginSuccess(code, user.getId());
        }
        if (!registered) {
            User registerUser = UserAdapter.buildUserSave(openId);
            userService.register(registerUser);
        }
//        推送链接 让用户扫码授权
        WAIT_AUTHORIZE_MAP.put(openId, code);
        webSocketService.waitAuthorize(code);
        String authorizeUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), callback + "/wx/portal/public/callBack");
        log.debug("请求地址:{}", authorizeUrl);
        return TextBuilder.build("请点击链接授权：<a href=\"" + authorizeUrl + "\">登录</a>", wxMpXmlMessage);
    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openid = userInfo.getOpenid();
        User user = userDao.getByOpenId(openid);
//        更新用户信息
        if (StrUtil.isBlank(user.getAvatar())) {
            fillUserInfo(user.getId(), userInfo);
        }
        Integer code = WAIT_AUTHORIZE_MAP.remove(openid);
        webSocketService.scanLoginSuccess(code, user.getId());
    }

    private void fillUserInfo(Long uid, WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUser(uid, userInfo);
//        TODO 用户昵称唯一处理
        userDao.updateById(user);
    }

    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        try {
            String eventKey = wxMpXmlMessage.getEventKey();
//        事件码 qrscene_2
            String code = eventKey.replace("qrscene", "");
            return Integer.parseInt(code);
        } catch (Exception e) {
            log.error("getEventKey error eventKey:{}", wxMpXmlMessage.getEventKey(), e);
            return null;
        }
    }
}
