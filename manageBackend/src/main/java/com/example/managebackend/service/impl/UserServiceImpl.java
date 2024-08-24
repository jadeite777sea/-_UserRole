package com.example.managebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.managebackend.entity.User;
import com.example.managebackend.mapper.UserMapper;
import com.example.managebackend.service.UserRoleService;
import com.example.managebackend.service.UserService;
import com.example.managebackend.utils.DataUtils;
import com.example.managebackend.utils.IdGenerator;
import com.example.managebackend.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final UserRoleService userRoleService;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserRoleService userRoleService) {
        this.userMapper = userMapper;
        this.userRoleService = userRoleService;
    }

    @Override
    public List<Map<String, Object>> getUserRoleAndPermissionsByUserId(List<Long> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return new ArrayList<>();
        }
        return userMapper.getUserRoleAndPermissionsByUserId(userIds);
    }

    @Override
    public Page<User> getUserList(String userName, String minCreateTime, String maxCreateTime, String orderBy, String orderMethod, Integer page, Integer pageSize) {
        List<User> userList = userMapper.getUserList(userName, minCreateTime, maxCreateTime, orderBy, orderMethod, (page - 1) * pageSize, pageSize);
        List<Map<String, Object>> list = userMapper.getUserRoleAndPermissionsByUserId(userList.stream().map(User::getId).collect(Collectors.toList()));
        Map<Long, Map<String, Object>> map = list.stream().collect(Collectors.toMap(m -> (Long) m.get("userId"), m -> m));
        for (User user : userList) {
            UserUtils.setUserRoleAndPermissionInfo(user, Collections.singletonList(map.get(user.getId())));
        }
        int total = userMapper.countUserList(userName, minCreateTime, maxCreateTime);
        return DataUtils.getPage(userList, total, page, pageSize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User addOneUser(User user) throws RuntimeException {
        if (UserUtils.isUserNameContainsIllegalCharacter(user.getUserName())) {
            throw new RuntimeException();
        }
        // 找出是否有相同的userName
        existsByUserName(user.getUserName(), true);

        Date date = new Date();
        user.setId(IdGenerator.nextId());
        user.setStatus(1).setCreateTime(date).setUpdateTime(date);
        userMapper.insert(user);
        userRoleService.addUserRole(user.getId(), user.getRoleIds(), false);
        return user;
    }

    @Override
    public Map<String, Object> batchCreateUser(List<User> users) {
        if (users == null || users.size() == 0) {
            return null;
        }
        // 根据用户名和邮箱查询已有的用户，不允许用户名或者邮箱重复
        List<String> userNameList = users.stream().map(User::getUserName).collect(Collectors.toList());
        List<User> existUsers = userMapper.selectList(new QueryWrapper<User>().in("user_name", userNameList));
        // 已存在的用户名和邮箱
        Set<String> existUserNameSet = existUsers.stream().map(User::getUserName).collect(Collectors.toSet());
        // 需要添加的用户
        List<User> addUserList = new ArrayList<>();
        // 重复的用户名和邮箱
        List<String> repeatedUserNameList = new ArrayList<>();
        // 已经添加过的用户名和邮箱，用来防止待创建列表中有重复的用户名和邮箱
        Set<String> addedUserNameSet = new HashSet<>();
        // 用户和角色的对应关系
        List<Map<String, Object>> userRoleList = new ArrayList<>();
        Date date = new Date();
        for (User user : users) {
            if (DataUtils.checkEmptyString(user.getUserName())) {
                continue;
            }
            user.setUserName(user.getUserName().trim());
            if (existUserNameSet.contains(user.getUserName()) || addedUserNameSet.contains(user.getUserName())) {
                repeatedUserNameList.add(user.getUserName());
                continue;
            }
            addedUserNameSet.add(user.getUserName());

            user.setId(IdGenerator.nextId());
            user.setStatus(1).setCreateTime(date).setUpdateTime(date);
            addUserList.add(user);
            userRoleList.add(new HashMap<String, Object>() {{
                put("user", user);
                put("roleIdList", user.getRoleIds());
            }});
        }
        if (addUserList.size() > 0) {
            saveBatch(addUserList);
        }
        // 批量添加用户角色
        for (Map<String, Object> userRole : userRoleList) {
            userRole.put("userId", ((User) userRole.get("user")).getId());
            userRole.remove("user");
        }
        if (userRoleList.size() > 0) {
            userRoleService.batchAddUserRole(userRoleList);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("existingUserNameList", repeatedUserNameList);
        result.put("successUserList", addUserList);
        return result;
    }


    @Override
    public boolean existsByUserName(String userName, Boolean throwExceptionWhenExists) throws RuntimeException {

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_name", userName.trim()).ne("status", 0));
        if (throwExceptionWhenExists == null) {
            return user != null;
        }
         else {
            return false;
        }
    }

    @Override
    public Map<String, Boolean> batchCheckUserName(List<String> userNames) {
        if (userNames == null || userNames.size() == 0) {
            return Collections.emptyMap();
        }
        List<User> existUsers = userMapper.selectList(new QueryWrapper<User>().in("user_name", userNames).ne("status",0));
        Set<String> existUserNameSet = existUsers.stream().map(user -> user.getUserName().trim()).collect(Collectors.toSet());
        Map<String, Boolean> result = new HashMap<>();
        for (String name : userNames) {
            result.put(name, existUserNameSet.contains(name));
        }
        return result;
    }

}
