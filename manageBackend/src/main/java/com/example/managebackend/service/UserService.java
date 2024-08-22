package com.example.managebackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.managebackend.entity.User;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

//    List<Map<String, Object>> getUserRoleAndPermissionsByUserId(List<Long> userIds);
//
//    Page<User> getUserList(String userName, String minCreateTime, String maxCreateTime, String orderBy, String orderMethod, Integer page, Integer pageSize);
//
//    User addOneUser(User user) throws RuntimeException;
//
//    Map<String, Object> batchCreateUser(List<User> userDTOS);

    boolean existsByUserName(String userName, Boolean throwExceptionWhenExists) throws RuntimeException;

    Map<String, Boolean> batchCheckUserName(List<String> userNames);

}
