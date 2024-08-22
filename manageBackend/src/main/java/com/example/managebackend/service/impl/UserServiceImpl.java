package com.example.managebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.managebackend.entity.User;
import com.example.managebackend.mapper.UserMapper;
import com.example.managebackend.service.UserRoleService;
import com.example.managebackend.service.UserService;
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
