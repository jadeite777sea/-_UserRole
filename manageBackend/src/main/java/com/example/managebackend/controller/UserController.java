package com.example.managebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.managebackend.constant.PermissionCode;
import com.example.managebackend.entity.User;
import com.example.managebackend.service.UserRoleService;
import com.example.managebackend.service.UserService;
import com.example.managebackend.utils.FileUtils;
import com.example.managebackend.utils.ServletUtils;
import com.example.managebackend.utils.UserUtils;
import com.example.managebackend.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.example.managebackend.utils.DataUtils;
@RestController
@Api(tags = "user-api")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public UserController(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    /**
     * 修改密码输入原密码时，实时检验原密码是否正确
     *
     * @param password 密码
     */
    @ApiOperation(value = "修改密码输入原密码时，实时检验原密码是否正确")
    @PostMapping("/check-password")
    public ResponseVO<Boolean> checkPassword(@RequestParam @ApiParam(value = "密码", required = true) String password) {
        Long userId = ServletUtils.getUserId();
        User user = userService.getOne(new QueryWrapper<User>().eq("id", userId).eq("password", password).ne("status", 0));
        return ResponseVO.success(user != null);
    }

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @ApiOperation(value = "修改密码", notes = "根据原密码，新密码，进行更改密码操作")
    @PutMapping("/password")
    public ResponseVO<String> updatePassword(@RequestParam @ApiParam(value = "旧密码", required = true) String oldPassword,
                                             @RequestParam @ApiParam(value = "新密码", required = true) String newPassword) {
        Long userId = ServletUtils.getUserId();
        User user = userService.getOne(new QueryWrapper<User>().eq("id", userId).eq("password", oldPassword).ne("status", 0));
        if (user == null) {
            return ResponseVO.error();
        }
        if (oldPassword.equals(newPassword)) {
            return ResponseVO.error();
        }
        user.setPassword(newPassword);
        userService.updateById(user);
        ServletUtils.updatePermission(Collections.singletonList(user.getId()));
        ServletUtils.setTokenData(null, null, null, null);
        return ResponseVO.success();
    }

    /**
     * 更新用户头像
     *
     * @param avatar 头像文件
     */
    @ApiOperation(value = "更新用户自己头像", notes = "根据用户id和头像信息进行更改用户头像操作")
    @PutMapping("/me/avatar")
    public ResponseVO<String> updateUserAvatar(@RequestPart MultipartFile avatar) {
        Long userId = ServletUtils.getUserId();
        User user = userService.getById(userId);
        if (UserUtils.updateUserAvatar(user, avatar)) {
            return ResponseVO.success();
        } else {
            return ResponseVO.error();
        }
    }

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     */
    @ApiOperation(value = "修改用户自身信息", notes = "根据用户id和用户信息进行更改用户信息操作")
    @PutMapping("/me")
    public ResponseVO<String> updateAdminInfo(@RequestBody User user) {



        if (userService.updateById(user.setUpdateTime(new Date()))) {
            return ResponseVO.success();
        } else {
            return ResponseVO.error();
        }
    }

    /**
     * 获取用户头像
     */
    @ApiOperation(value = "获取用户自己的头像")
    @GetMapping("/me/avatar")
    public void getAvatar(HttpServletResponse response) {
        Long userId = ServletUtils.getUserId();
        User user = userService.getById(userId);
        String avatarPath=user.getAvatarPath();
        try {
            FileUtils.writeImageToResponse(avatarPath, response);
        } catch (IOException ignored) {

        }
    }

    /**
     * 获取用户信息
     */
    @ApiOperation(value = "获取用户自身信息")
    @GetMapping("/me")
    public ResponseVO<User> getUserInfo() {
        User user = userService.getById(ServletUtils.getUserId());
        System.out.println(user);
        List<Map<String, Object>> roleAndPermissionInfo = userService.getUserRoleAndPermissionsByUserId(Collections.singletonList(user.getId()));
        System.out.println(roleAndPermissionInfo);
        UserUtils.setUserRoleAndPermissionInfo(user, roleAndPermissionInfo);
        System.out.println(user);
        return ResponseVO.success(user);
    }

    /**
     * 添加用户
     *
     * @param user 用户信息
     */
    @PostMapping
    @ApiOperation(value = "添加用户")
    //@PreAuthorize("hasAuthority('" + PermissionCode.USER_MANAGE + "')")
    public ResponseVO<User> addOneUser(@RequestBody User user) {
        System.out.println(user);
        try {
            user = userService.addOneUser(user);
        } catch (RuntimeException e) {
            return ResponseVO.error();
        }
        return ResponseVO.success(user);
    }

    /**
     * 获取用户列表
     *
     * @param userName      用户名
     * @param minCreateTime 最小创建时间
     * @param maxCreateTime 最大创建时间
     * @param orderBy       排序字段
     * @param orderMethod   排序方式
     * @param pageNum       页码
     * @param pageSize      每页数量
     */
    @ApiOperation(value = "获取用户列表")
    @GetMapping
    //@PreAuthorize("hasAuthority('" + PermissionCode.USER_MANAGE + "')")
    public ResponseVO<Page<User>> getUserList(
            @RequestParam(defaultValue = "") @ApiParam(value = "用户名") String userName,
            @RequestParam(required = false) @ApiParam(value = "最小创建时间") String minCreateTime,
            @RequestParam(required = false) @ApiParam(value = "最大创建时间") String maxCreateTime,
            @RequestParam(required = false) @ApiParam(value = "排序字段: " + User.ORDER_FIELDS) String orderBy,
            @RequestParam(required = false) @ApiParam(value = "排序方式: " + DataUtils.ORDER_METHODS) String orderMethod,
            @RequestParam @ApiParam(value = "页码", required = true) int pageNum,
            @RequestParam @ApiParam(value = "每页数量", required = true) int pageSize) {
        if (DataUtils.isTimeFormatInvalid(minCreateTime)) {
            minCreateTime = null;
        }
        if (DataUtils.isTimeFormatInvalid(maxCreateTime)) {
            maxCreateTime = null;
        }
        if (!User.ORDER_FIELD_LIST.contains(orderBy)) {
            orderBy = User.ORDER_FIELD_LIST.get(0);
            orderMethod = "desc";
        } else {
            orderMethod = "desc".equalsIgnoreCase(orderMethod) ? "desc" : "asc";
        }
        System.out.println("用户名信息"+userName);
        System.out.println("页码"+pageNum);
        return ResponseVO.success(userService.getUserList(userName, minCreateTime, maxCreateTime, orderBy, orderMethod, pageNum, pageSize));
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    @ApiOperation(value = "更新用户信息", notes = "根据用户id和用户信息进行更改用户信息操作")
    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    //@PreAuthorize("hasAuthority('" + PermissionCode.USER_MANAGE + "')")
    public ResponseVO<String> updateUserInfo(@RequestBody User user) {

        if (user == null) {
            return ResponseVO.error();
        }

        user.setUpdateTime(new Date());
        userService.updateById(user);
        if (user.getRoleIds() != null) {
            userRoleService.addUserRole(user.getId(), user.getRoleIds(), true);
            ServletUtils.updatePermission(Collections.singletonList(user.getId()));
        }
        return ResponseVO.success("更新成功");
    }

    /**
     * 更新用户头像
     *
     * @param id     用户id
     * @param avatar 头像
     */
    @ApiOperation(value = "更新用户头像")
    @PutMapping("/{id}/avatar")
    //@PreAuthorize("hasAuthority('" + PermissionCode.USER_MANAGE + "')")
    public ResponseVO<String> updateUserAvatar(@PathVariable long id,
                                               @RequestPart MultipartFile avatar) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseVO.error();
        }
        if (UserUtils.updateUserAvatar(user, avatar)) {
            return ResponseVO.success("更新成功");
        } else {
            return ResponseVO.error();
        }
    }

    /**
     * 获取用户头像
     *
     * @param id 用户id
     */
    @ApiOperation(value = "获取用户头像")
    @GetMapping("/{id}/avatar")
    //@PreAuthorize("hasAuthority('" + PermissionCode.USER_MANAGE + "')")
    public void getUserAvatar(@PathVariable long id, HttpServletResponse response) {
        User user = userService.getById(id);
        String avatarPath =  user.getAvatarPath();
        try {
            FileUtils.writeImageToResponse(avatarPath, response);
        } catch (IOException ignored) {
        }
    }

    /**
     * 删除用户
     *
     * @param ids 用户id列表
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping
    @Transactional(rollbackFor = Exception.class)
    //@PreAuthorize("hasAuthority('" + PermissionCode.USER_MANAGE + "')")
    public ResponseVO<String> deleteUsers(@RequestParam @ApiParam(value = "用户id列表") List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseVO.error();
        }
        // 采用逻辑删除而非物理删除
        List<User> userList = (List<User>) userService.listByIds(ids);
        userList.forEach(user -> user.setStatus(0));
        userService.updateBatchById(userList);
        return ResponseVO.success();
    }

    /**
     * 批量创建用户
     *
     * @param userList 用户列表
     */
    @ApiOperation(value = "批量创建用户")
    @PostMapping("/batch")
    @Transactional(rollbackFor = Exception.class)
    //@PreAuthorize("hasAuthority('" + PermissionCode.USER_MANAGE + "')")
    public ResponseVO<Map<String, Object>> batchCreateUser(@RequestBody List<User> userList) {
        Map<String, Object> resultMap = userService.batchCreateUser(userList);
        if (resultMap == null) {
            return ResponseVO.error();
        }
        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) resultMap.get("successUserList");
        resultMap.remove("successUserList");
        resultMap.put("successCount", users.size());
        return ResponseVO.success(resultMap);
    }

    /**
     * 修改用户状态
     *
     * @param id     用户id
     * @param status 用户状态
     */
    @ApiOperation(value = "修改用户状态")
    @PutMapping("/{id}/status")
    //@PreAuthorize("hasAuthority('" + PermissionCode.USER_MANAGE + "')")
    public ResponseVO<String> updateUserStatus(@PathVariable Long id,
                                               @RequestParam Integer status) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseVO.error();
        }
        user.setStatus(status);
        userService.updateById(user);
        return ResponseVO.success();
    }

    /**
     * 批量检查用户名是否存在
     *
     * @param userNameList 用户名列表
     */
    @ApiOperation(value = "批量检查用户名是否存在")
    @PostMapping("/user-name/batch-check-existence")
    //@PreAuthorize("hasAuthority('" + PermissionCode.USER_MANAGE + "')")
    public ResponseVO<Map<String, Boolean>> checkUserNameExists(@RequestBody List<String> userNameList) {
        return ResponseVO.success(userService.batchCheckUserName(userNameList));
    }

}
