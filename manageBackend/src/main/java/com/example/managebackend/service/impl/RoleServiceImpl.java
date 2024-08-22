package com.example.managebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.managebackend.entity.Role;
import com.example.managebackend.mapper.RoleMapper;
import com.example.managebackend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public boolean existsByName(String roleName, boolean throwExceptionWhenExists) throws RuntimeException {

        Role role = roleMapper.selectOne(new QueryWrapper<Role>().eq("name", roleName.trim()).eq("status", 1));
        if (role != null && throwExceptionWhenExists) {
            throw new RuntimeException();
        }
        return role != null;
    }

}
