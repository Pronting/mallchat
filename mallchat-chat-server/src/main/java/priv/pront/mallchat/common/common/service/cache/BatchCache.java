package priv.pront.mallchat.common.common.service.cache;

import java.util.List;
import java.util.Map;

public interface BatchCache<IN, OUT>{

    /**
     * 获取单个
     * @param req
     * @return
     */
    OUT get(IN req);

    /**
     * 获取多个
     * @param reqList
     * @return
     */
    Map<IN, OUT> getBatch(List<IN> reqList);

    /**
     * 删除单个
     * @param req
     */
    void del(IN req);

    /**
     * 删除多个
     * @param reqList
     */

    void delBatch(List<IN> reqList);



}
