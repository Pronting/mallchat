package priv.pront.mallchat.common.common.service.cache;


import cn.hutool.core.collection.CollectionUtil;
import org.springframework.data.util.Pair;
import priv.pront.mallchat.common.common.util.RedisUtils;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRedisStringCache<IN, OUT> implements BatchCache<IN, OUT> {

    private Class<OUT> outClass;

    protected AbstractRedisStringCache() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.outClass = (Class<OUT>) genericSuperclass.getActualTypeArguments()[1];
    }

    @Override
    public OUT get(IN req) {
        return getBatch(Collections.singletonList(req)).get(req);
    }

    @Override
    public Map<IN, OUT> getBatch(List<IN> reqList) {
        if(CollectionUtil.isEmpty(reqList)) return new HashMap<>();
        reqList = reqList.stream().distinct().collect(Collectors.toList());
//        批量组装key
        List<String> keys = reqList.stream().map(this::getKey).collect(Collectors.toList());
//        批量查询
        List<OUT> values = RedisUtils.mget(keys, outClass);
        List<IN> loadReq = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            if (Objects.isNull(values.get(i))) {
                loadReq.add(reqList.get(i));
            }
        }
        Map<IN, OUT> load = new HashMap<>();
        if (CollectionUtil.isNotEmpty(loadReq)) {
            load = load(loadReq);
            Map<String, OUT> loadMap = load.entrySet()
                    .stream()
                    .map(v -> Pair.of(getKey(v.getKey()), v.getValue()))
                    .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
//            加载进redis
            RedisUtils.mset(loadMap, getExpireSeconds());
        }
//        组装最后的结果
        Map<IN, OUT> resultMap = new HashMap<>();
        for (int i = 0; i < reqList.size(); i++) {
            IN in = reqList.get(i);
            OUT out = Optional.ofNullable(values.get(i)).orElse(load.get(in));
            resultMap.put(in, out);
        }
        return resultMap;
    }

    @Override
    public void del(IN req) {
        delBatch(Collections.singletonList(req));
    }

    @Override
    public void delBatch(List<IN> reqList) {
        List<String> keys = reqList.stream().map(this::getKey).collect(Collectors.toList());
        RedisUtils.del(keys);
    }

    protected abstract String getKey(IN req);

    protected abstract Map<IN, OUT> load(List<IN> loadReq);

    protected abstract Long getExpireSeconds();
}
