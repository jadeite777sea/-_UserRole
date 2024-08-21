package com.example.managebackend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.managebackend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper extends BaseMapper<User> {

}
