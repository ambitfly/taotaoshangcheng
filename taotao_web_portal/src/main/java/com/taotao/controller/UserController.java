package com.taotao.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.Result;
import com.taotao.pojo.user.User;
import com.taotao.service.user.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    @GetMapping("/sendSms")
    public Result sendSms(String phone){
        userService.sendSms(phone);
        return new Result();
    }

    @PostMapping("save")
    public Result save(@RequestBody User user,String smsCode){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(user.getPassword());
        user.setPassword(password);
  //      System.out.println(user.getPhone());
        userService.add(user,smsCode);
        return new Result();
    }
}
