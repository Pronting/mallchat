package priv.pront.mallchat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@ApiModel("徽章信息")
public class WearingBadgeResp {

    @ApiModelProperty(value = "徽章id")
    @NotNull
    private Long itemId;

}
