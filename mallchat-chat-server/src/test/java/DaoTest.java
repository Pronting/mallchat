import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import priv.pront.mallchat.common.MallchatCustomApplication;
import priv.pront.mallchat.common.user.dao.UserDao;
import priv.pront.mallchat.common.user.domain.entity.User;

import javax.annotation.Resource;


@SpringBootTest(classes = {MallchatCustomApplication.class})
@RunWith(SpringRunner.class)
public class DaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private WxMpService wxMpService;

    @Test
    public void test2() throws WxErrorException {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 10000);
//        带参二维码
        String url = wxMpQrCodeTicket.getUrl();
        System.out.println(url);
    }
}
