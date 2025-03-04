package priv.pront.mallchat.common.websocket.domain.vo.req;

import lombok.Data;
import priv.pront.mallchat.common.websocket.domain.enums.WSReqTypeEnum;

@Data
public class WSBaseReq {


        /**
         * 请求类型 1.请求登录二维码，2心跳检测
         *
         * @see WSReqTypeEnum
         */
        private Integer type;

        /**
         * 每个请求包具体的数据，类型不同结果不同
         */
        private String data;
}
