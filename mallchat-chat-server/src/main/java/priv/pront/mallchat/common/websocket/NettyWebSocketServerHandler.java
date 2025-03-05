package priv.pront.mallchat.common.websocket;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import priv.pront.mallchat.common.websocket.domain.enums.WSReqTypeEnum;
import priv.pront.mallchat.common.websocket.domain.vo.req.WSBaseReq;
import priv.pront.mallchat.common.websocket.service.WebSocketService;


@Slf4j
@Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    private WebSocketService webSocketService;

    /**
     * 通道激活回调
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        webSocketService.connect(ctx.channel());
    }


    /**
     * 用户下线统一处理
     *
     * @param channel
     */
    private void userOffline(Channel channel) {
        webSocketService.remove(channel);
        channel.close();
    }

    /**
     * 客户端主动下线
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOffline(ctx.channel());
    }

    /**
     * 前端推送事件回调
     *
     * @param channelHandlerContext
     * @param textWebSocketFrame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String text = textWebSocketFrame.text();
        WSBaseReq wsBaseReq = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSReqTypeEnum.of(wsBaseReq.getType())) {
            case AUTHORIZE:
                break;
            case HEARTBEAT:
                break;
            case LOGIN:
                log.info("请求二维码");
                webSocketService.handleLoginReq(channelHandlerContext.channel());
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("握手完成！");
        }else if(evt instanceof IdleStateEvent) {
//            断开连接
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
               log.info("读空闲");
//                TODO 用户下线
                userOffline(ctx.channel());
            }
        }
    }
}
