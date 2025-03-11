package priv.pront.mallchat.common.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import priv.pront.mallchat.common.common.domain.vo.resp.ApiResult;
import priv.pront.mallchat.common.common.interceptor.TokenInterceptor;
import priv.pront.mallchat.common.common.util.RequestHolder;
import priv.pront.mallchat.common.user.domain.vo.ModifyNameReq;
import priv.pront.mallchat.common.user.domain.vo.resp.BadgeResp;
import priv.pront.mallchat.common.user.domain.vo.resp.UserInfoResp;
import priv.pront.mallchat.common.user.domain.vo.resp.WearingBadgeResp;
import priv.pront.mallchat.common.user.service.LoginService;
import priv.pront.mallchat.common.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/Pronting">pront</a>
 * @since 2025-03-05
 */
@RestController
@RequestMapping("/capi/user")
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo() {
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }


    @PutMapping("/name")
    @ApiOperation("用户改名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq req) {
        userService.modifyName(RequestHolder.get().getUid(), req.getName());
        return ApiResult.success();
    }

    @GetMapping("/badges")
    @ApiOperation("可选徽章预览")
    public ApiResult<List<BadgeResp>> badges() {
        return ApiResult.success(userService.badges(RequestHolder.get().getUid()));
    }

    @PutMapping("/badge")
    @ApiOperation("佩戴徽章")
    public ApiResult<Void> wearingBadge(@Valid @RequestBody WearingBadgeResp req) {
        userService.wearingBadge(RequestHolder.get().getUid(), req.getItemId());

        return ApiResult.success();
    }



}

