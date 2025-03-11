package priv.pront.mallchat.common.user.dao;

import priv.pront.mallchat.common.user.domain.entity.ItemConfig;
import priv.pront.mallchat.common.user.mapper.ItemConfigMapper;
import priv.pront.mallchat.common.user.service.ItemConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 功能物品配置表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/Pronting">pront</a>
 * @since 2025-03-09
 */
@Service
public class ItemConfigDao extends ServiceImpl<ItemConfigMapper, ItemConfig>  {

    public List<ItemConfig> getByType(Integer itemType) {
        return lambdaQuery()
                .eq(ItemConfig::getType, itemType)
                .list();
    }
}
