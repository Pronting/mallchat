package priv.pront.mallchat.common.user.service.impl;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import priv.pront.mallchat.common.common.domain.vo.resp.ApiResult;
import priv.pront.mallchat.common.common.util.JsonUtils;
import priv.pront.mallchat.common.user.dao.UserDao;
import priv.pront.mallchat.common.user.domain.entity.IpDetail;
import priv.pront.mallchat.common.user.domain.entity.IpInfo;
import priv.pront.mallchat.common.user.domain.entity.User;
import priv.pront.mallchat.common.user.service.IpService;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class IpServiceImpl implements IpService, DisposableBean {

    private static ExecutorService executor = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(500), new NamedThreadFactory("refresh-ipDetail", false));

    @Autowired
    private UserDao userDao;

    @Override
    public void refreshIpDetail(Long uid) {
        executor.execute(() -> {
            User user = userDao.getById(uid);
            IpInfo ipInfo = user.getIpInfo();
            if(Objects.isNull(ipInfo)) return;
            String ip = ipInfo.needRefreshIp();
            if(StringUtils.isBlank(ip)) return;
//            ip 刷新
            IpDetail ipDetail = tryGetDetailOrNullThreeTime(ip);
            if (Objects.nonNull(ipDetail)) {
                ipInfo.refreshIpDetail(ipDetail);
                User update = User.builder()
                        .id(uid)
                        .ipInfo(ipInfo)
                        .build();
                userDao.updateById(update);
            }
        });

    }

    private static IpDetail tryGetDetailOrNullThreeTime(String ip) {
        for (int i = 0; i < 3; i++) {
            IpDetail ipDetail = GetDetailOrNull(ip);
            if(Objects.nonNull(ipDetail)) return ipDetail;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error("tryGetDetailOrNullThreeTime InterruptedException ", e);
            }
        }
        return null;
    }

    private static IpDetail GetDetailOrNull(String ip) {
        try {
            String url = "https://ip.taobao.com/outGetIpInfo?ip=" + ip + "&accessKey=alibaba-inc";
            String data = HttpUtil.get(url);
            ApiResult<IpDetail> result = JsonUtils.toObj(data, new TypeReference<ApiResult<IpDetail>>() {});
            return result.getData();
        } catch (Exception e) {
           return null;
        }
    }

    public static void main(String[] args) {
        StopWatch sw = new StopWatch();
        for (int i = 0; i < 100; i++) {
        sw.start("第" + i + "次调用");
            int finalI = i;
            executor.execute(() -> {
                IpDetail ipDetail = tryGetDetailOrNullThreeTime("117.85.133.4");
                if (Objects.nonNull(ipDetail)) {
                    System.out.println(String.format("第%d次成功", finalI));
                }
            });
            sw.stop();

        }
        System.out.println(sw.prettyPrint());
    }

    /**
     * 普通线程池的优雅停机
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        executor.shutdown();  // 线程池不会在接受新的任务了
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {// 最多等30秒
            if (log.isErrorEnabled()) {
                log.error("Timed out while waiting for executor [{}] to terminate", executor);
            }
        }
    }
}
