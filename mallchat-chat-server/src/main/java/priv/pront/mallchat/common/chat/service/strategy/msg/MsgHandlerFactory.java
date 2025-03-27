package priv.pront.mallchat.common.chat.service.strategy.msg;


import priv.pront.mallchat.common.common.util.AssertUtil;
import priv.pront.mallchat.common.common.exception.CommonErrorEnum;

import java.util.HashMap;
import java.util.Map;

public class MsgHandlerFactory {
    private static final Map<Integer, AbstractMsgHandler> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer code, AbstractMsgHandler strategy) {
        STRATEGY_MAP.put(code, strategy);
    }

    public static AbstractMsgHandler getStrategyNoNull(Integer code) {
        AbstractMsgHandler strategy = STRATEGY_MAP.get(code);
        AssertUtil.isNotEmpty(strategy, CommonErrorEnum.PARAM_INVALID);
        return strategy;
    }
}
