import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum ChatActiveEnum {

    ONLINE(1, "在线"),
    OFFLINE(2, "离线");

    private final Integer type;

    private final String desc;

    private static Map<Integer, ChatActiveEnum> cache;

    static{
        cache = Arrays.stream(ChatActiveEnum.values()).collect(Collectors.toMap(ChatActiveEnum::getType, Function.identity()));
    }

    public static ChatActiveEnum of(Integer type){
        return cache.get(type);
    }



}
