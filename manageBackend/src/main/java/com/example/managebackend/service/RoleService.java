package com.example.managebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.managebackend.entity.Role;


public interface RoleService extends IService<Role> {

    boolean existsByName(String roleName, boolean throwExceptionWhenExists) throws RuntimeException;

}
