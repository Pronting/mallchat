package priv.pront.mallchat.common.user.domain.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("User信息返回对象")
public class UserInfoResp {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户性别")
    private Integer sex;

    @ApiModelProperty("改名卡次数")
    private Integer modifyNameChance;
}
