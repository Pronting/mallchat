package priv.pront.mallchat.common.chat.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.pront.mallchat.common.chat.dao.RoomDao;
import priv.pront.mallchat.common.chat.dao.RoomFriendDao;
import priv.pront.mallchat.common.chat.domain.entity.Room;
import priv.pront.mallchat.common.common.constant.RedisKey;
import priv.pront.mallchat.common.common.service.cache.AbstractRedisStringCache;
import priv.pront.mallchat.common.user.dao.UserDao;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoomCache extends AbstractRedisStringCache<Long, Room> {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private RoomFriendDao roomFriendDao;

    @Override
    protected String getKey(Long roomId) {
        return RedisKey.getKey(RedisKey.ROOM_INFO_STRING, roomId);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, Room> load(List<Long> roomIds) {
        List<Room> rooms = roomDao.listByIds(roomIds);
        return rooms.stream().collect(Collectors.toMap(Room::getId, Function.identity()));
    }
}
