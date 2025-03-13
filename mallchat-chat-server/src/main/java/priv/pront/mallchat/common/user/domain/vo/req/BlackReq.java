package priv.pront.mallchat.common.user.domain.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class BlackReq {

    @ApiModelProperty("用户uid")
    @NotNull
    private Long uid;


}
