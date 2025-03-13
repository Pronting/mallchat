package priv.pront.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum BlackTypeEnum {
    IP(1, "IP"),
    UID(2, "UID"),
    ;

    private final Integer type;
    private final String desc;

    private static Map<Integer, BlackTypeEnum> cache;

    static {
        cache = Arrays.stream(BlackTypeEnum.values()).collect(Collectors.toMap(BlackTypeEnum::getType, Function.identity()));
    }

    public static BlackTypeEnum of(Long id) {
        return cache.get(id);
    }

}
