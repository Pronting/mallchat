package priv.pront.mallchat.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import priv.pront.mallchat.common.user.dao.UserDao;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.user.service.LoginService;
import priv.pront.mallchat.common.websocket.domain.dto.WSChannelExtraDTO;
import priv.pront.mallchat.common.websocket.domain.enums.WSRespTypeEnum;
import priv.pront.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import priv.pront.mallchat.common.websocket.domain.vo.resp.WSLoginUrl;
import priv.pront.mallchat.common.websocket.service.WebSocketService;
import priv.pront.mallchat.common.websocket.service.adapter.WebSocketAdapter;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginService loginService;

    /**
     * 管理所有用户的连接
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    public static final Duration DURATION = Duration.ofHours(1);
    public static final int MAXIMUM_SIZE = 1000;

    /**
     * 临时保存登录 code 和 channel 的映射关系
     */
    private static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(DURATION)
            .build();

    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }

    @Override
    public void handleLoginReq(Channel channel) throws WxErrorException {
//        生成随机码
        Integer code = generateLoginCode(channel);
//        找微信申请带参的二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
//        把这个二维码推送给前端
        sendMsg(channel, WebSocketAdapter.buildResq(wxMpQrCodeTicket));
    }

    @Override
    public void remove(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
//        TODO 用户下线
    }

    @Override
    public void scanLoginSuccess(Integer code, Long uid) {
//        确认连接在机器上
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if(Objects.isNull(channel)) return;
        User user = userDao.getById(uid);
//        移出 code
        WAIT_LOGIN_MAP.invalidate(code);
//        TODO 获取调用登录模块获取 token
        String token = loginService.login(uid);
//        用户登录
        sendMsg(channel, WebSocketAdapter.buildResq(user, token));
    }

    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) return;
        sendMsg(channel, WebSocketAdapter.buildWaitAuthorizeResq());
    }

    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        return code;
    }
}
