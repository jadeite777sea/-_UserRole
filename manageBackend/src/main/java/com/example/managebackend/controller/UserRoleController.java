package com.example.managebackend.controller;

import com.example.managebackend.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user-role")
public class UserRoleController {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @GetMapping("/getUserIdsByRoleIds")
    public List<Map<String, Object>> getUserIdsByRoleIds(@RequestParam List<Integer> roleIds) {
        return userRoleMapper.getUserIdsByRoleIds(roleIds);
    }
}
