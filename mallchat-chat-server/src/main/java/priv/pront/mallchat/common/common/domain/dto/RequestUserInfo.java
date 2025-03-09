package priv.pront.mallchat.common.common.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestUserInfo {

    private Long uid;

    private String ip;
}
