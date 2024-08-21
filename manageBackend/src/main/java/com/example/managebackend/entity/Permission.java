package com.example.managebackend.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 权限名称
    private String name;
    // 权限代码
    private String code;
    // 权限描述
    private String description;
    // 父级权限
    private Integer parentId;
    // 创建时间
    private Date createTime;
    // 系统
    private String platform;

}
