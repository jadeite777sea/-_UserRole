package com.example.managebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.managebackend.entity.LoginLog;
import com.example.managebackend.mapper.LoginLogMapper;
import com.example.managebackend.service.LoginLogService;
import org.springframework.stereotype.Service;

@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {
}
