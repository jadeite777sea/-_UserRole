package com.example.managebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.managebackend.entity.User;
import com.example.managebackend.service.RoleService;
import com.example.managebackend.service.UserService;
import com.example.managebackend.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.managebackend.entity.Role;
import java.util.Collections;
import java.util.Map;

@RestController
@Api(tags = "auth-api")
@RequestMapping( "/auth")
public class AuthController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AuthController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * 使用用户名密码登录
     *
     * @param userName 用户名
     * @param password 密码
     * @param platform 登录系统
     */
    @ApiOperation(value = "使用用户名密码登录", notes = "使用用户名密码登录")
    @ApiImplicitParam(paramType = "header", name = "X-Real-IP", value = "ip", required = true, dataType = "string")
    @PostMapping(value = "/login")
    public ResponseVO<Map<String, Object>> login(@RequestParam @ApiParam(value = "用户名", required = true) String userName,
                                                 @RequestParam @ApiParam(value = "密码", required = true) String password,
                                                 @RequestParam @ApiParam(value = "登录系统, " + PlatformEnum.PLATFORM_DESC, required = true) Integer platform) {
        User user = userService.getOne(new QueryWrapper<User>().eq("user_name", userName));
        if (user == null) {
            return ResponseVO.error();
        }
        return UserUtils.handleUserLogin(user, password, platform);
    }

    /**
     * 退出登录
     */
    @ApiOperation(value = "退出登录", notes = "退出登录")
    @PostMapping(value = "/logout")
    public ResponseVO<String> logout() {
        ServletUtils.removeToken();
        return ResponseVO.success();
    }

    /**
     * 检查用户名是否已经被使用
     *
     * @param userName 用户名
     */
    @ApiOperation(value = "检查用户名是否已经被使用")
    @PostMapping("/user-name/exists")
    public ResponseVO<Boolean> checkUserName(@RequestParam @ApiParam(value = "用户名", required = true) String userName) {
        try {
            return ResponseVO.success(userService.existsByUserName(userName, null));
        } catch (RuntimeException e) {
            return ResponseVO.error();
        }
    }

    /**
     * 用户注册
     *
     * @param userName 用户名
     * @param password 密码
     */
    @ApiOperation(value = "用户注册", notes = "根据邮箱，密码和用户名进行用户注册")
    @PostMapping("/register")
    public ResponseVO<String> register(@RequestParam @ApiParam(required = true, value = "用户名") String userName,
                                       @RequestParam @ApiParam(required = true, value = "密码") String password) {
        // 保存用户
        Role role = roleService.getOne(new QueryWrapper<Role>().eq("name", RoleEnum.USER.getName()).eq("status", 1));
        if (role == null) {
            return ResponseVO.error();
        }
        try {
            userService.addOneUser(new UserDTO(userName, password, Collections.singletonList(role.getId())));
        } catch (RuntimeException e) {
            return ResponseVO.error();
        }
        return ResponseVO.success();
    }

}

