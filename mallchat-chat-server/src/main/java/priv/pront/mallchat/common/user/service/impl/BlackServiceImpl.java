package priv.pront.mallchat.common.user.service.impl;

import priv.pront.mallchat.common.user.domain.entity.Black;
import priv.pront.mallchat.common.user.mapper.BlackMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import priv.pront.mallchat.common.user.service.BlackService;

/**
 * <p>
 * 黑名单 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/Pronting">pront</a>
 * @since 2025-03-13
 */
@Service
public class BlackServiceImpl extends ServiceImpl<BlackMapper, Black> implements BlackService {

}
