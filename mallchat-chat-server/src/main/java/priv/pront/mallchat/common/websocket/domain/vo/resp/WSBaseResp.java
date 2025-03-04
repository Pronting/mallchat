package priv.pront.mallchat.common.websocket.domain.vo.resp;

import lombok.Data;

@Data
public class WSBaseResp<T> {

    /**
     * ws推送给前端的消息
     *
     * @see priv.pront.mallchat.common.websocket.domain.enums.WSRespTypeEnum
     */
    private Integer type;

    private T data;
}
