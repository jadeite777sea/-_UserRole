package com.example.managebackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.managebackend.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {
}
