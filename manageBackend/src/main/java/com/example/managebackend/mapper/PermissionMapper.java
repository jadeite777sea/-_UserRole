package com.example.managebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.managebackend.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
}
