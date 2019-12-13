package com.taotao.service.user;

import com.taotao.entity.PageResult;
import com.taotao.pojo.user.User;

import java.util.List;
import java.util.Map;

/**
 * user业务逻辑层
 */
public interface UserService {


    List<User> findAll();


    PageResult<User> findPage(int page, int size);


    List<User> findList(Map<String, Object> searchMap);


    PageResult<User> findPage(Map<String, Object> searchMap, int page, int size);


    User findById(String username);

    void add(User user);


    void update(User user);


    void delete(String username);

    void sendSms(String phone);

    /**
     *增加
     * @param user
     * @param smsCode
     */
    void add(User user, String smsCode);

}
