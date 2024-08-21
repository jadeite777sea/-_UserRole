package com.example.managebackend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 用户
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    // 用户名
    private String userName;
    // 密码
    @JsonIgnore
    private String password;
    // 头像路径
    private String avatarPath;
    // 电话
    private String phone;
    // 邮箱
    private String email;
    // 性别, 0: 男, 1: 女
    private Integer gender;
    // 地址
    private String address;
    // 个人简介
    private String introduction;
    // 真实姓名
    private String trueName;
    // 状态, 0: 禁用, 1: 启用, 2: 删除
    private Integer status;
    // 注册时间
    private Date createTime;
    // 更新时间
    private Date updateTime;

    // 非数据库字段
    @TableField(exist = false)
    private List<String> roleList;

    @TableField(exist = false)
    private List<String> permissionList;

    // 排序字段
    public static final String ORDER_FIELDS = "create_time,user_name,true_name,email,gender,address,introduction,phone,status,update_time";
    public static final List<String> ORDER_FIELD_LIST = Arrays.asList(ORDER_FIELDS.split(","));

}