package priv.pront.mallchat.common.chat.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import priv.pront.mallchat.common.chat.dao.GroupMemberDao;
import priv.pront.mallchat.common.chat.dao.MessageDao;
import priv.pront.mallchat.common.chat.dao.RoomGroupDao;
import priv.pront.mallchat.common.chat.domain.entity.RoomGroup;

import java.util.List;
import java.util.Objects;


@Component
public class GroupMemberCache {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private RoomGroupDao roomGroupDao;
    @Autowired
    private GroupMemberDao groupMemberDao;

    @Cacheable(cacheNames = "member", key = "'groupMember'+#roomId")
    public List<Long> getMemberUidList(Long roomId) {
        RoomGroup roomGroup = roomGroupDao.getByRoomId(roomId);
        if (Objects.isNull(roomGroup)) {
            return null;
        }
        return groupMemberDao.getMemberUidList(roomGroup.getId());
    }

    @CacheEvict(cacheNames = "member", key = "'groupMember'+#roomId")
    public List<Long> evictMemberUidList(Long roomId) {
        return null;
    }

}
