package priv.pront.mallchat.common.user.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import priv.pront.mallchat.common.user.dao.ItemConfigDao;
import priv.pront.mallchat.common.user.domain.entity.ItemConfig;

import java.util.List;

@Component
public class ItemCache {

    @Autowired
    private ItemConfigDao itemConfigDao;

    @Cacheable(cacheNames = "item", key = "'itemByType:'+#itemType")
    public List<ItemConfig> getByType(Integer itemType) {
        return itemConfigDao.getByType(itemType);
    }

    @CacheEvict(cacheNames = "item", key = "'itemByType:'+#itemType")
    public void evictByType(Integer itemType) {

    }
}
