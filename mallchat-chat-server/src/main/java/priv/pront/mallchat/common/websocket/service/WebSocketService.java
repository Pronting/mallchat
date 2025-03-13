package priv.pront.mallchat.common.websocket.service;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.device.BaseResp;
import priv.pront.mallchat.common.websocket.domain.vo.resp.WSBaseResp;

public interface WebSocketService {
    void connect(Channel channel);

    void handleLoginReq(Channel channel) throws WxErrorException;

    void remove(Channel channel);

    void scanLoginSuccess(Integer code, Long uid);

    void waitAuthorize(Integer code);

    void authorize(Channel channel, String token);
    void sendMsgToAll(WSBaseResp<?> msg);
}
