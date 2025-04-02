package priv.pront.mallchat.common.chat.service.cache;

import cn.hutool.core.lang.Pair;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import priv.pront.mallchat.common.common.constant.RedisKey;
import priv.pront.mallchat.common.common.domain.vo.req.CursorPageBaseReq;
import priv.pront.mallchat.common.common.domain.vo.resp.CursorPageBaseResp;
import priv.pront.mallchat.common.common.util.CursorUtils;
import priv.pront.mallchat.common.common.util.RedisUtils;

import java.util.Date;
import java.util.Set;

@Component
public class HotRoomCache {

    /**
     * 获取热门群聊翻页
     *
     * @return
     */
    public CursorPageBaseResp<Pair<Long, Double>> getRoomCursorPage(CursorPageBaseReq pageBaseReq) {
        return CursorUtils.getCursorPageByRedis(pageBaseReq, RedisKey.getKey(RedisKey.HOT_ROOM_ZET), Long::parseLong);
    }

    public Set<ZSetOperations.TypedTuple<String>> getRoomRange(Double hotStart, Double hotEnd) {
        return RedisUtils.zRangeByScoreWithScores(RedisKey.getKey(RedisKey.HOT_ROOM_ZET), hotStart, hotEnd);
    }

    /**
     * 更新热门群聊的最新时间
     */
    public void refreshActiveTime(Long roomId, Date refreshTime) {
        RedisUtils.zAdd(RedisKey.getKey(RedisKey.HOT_ROOM_ZET), roomId, (double) refreshTime.getTime());
    }
}
