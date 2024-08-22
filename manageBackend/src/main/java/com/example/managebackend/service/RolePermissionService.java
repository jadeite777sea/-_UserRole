package com.example.managebackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.managebackend.entity.Permission;
import com.example.managebackend.entity.RolePermission;


import java.util.List;

public interface RolePermissionService extends IService<RolePermission> {

    List<Permission> getPermissionsByRoleId(Integer roleId);

}
