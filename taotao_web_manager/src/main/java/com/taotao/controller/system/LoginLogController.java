package com.taotao.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.PageResult;
import com.taotao.entity.Result;
import com.taotao.pojo.system.LoginLog;
import com.taotao.service.system.LoginLogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/loginLog")
public class LoginLogController {

    @Reference
    private LoginLogService loginLogService;

    @GetMapping("/findAll")
    public List<LoginLog> findAll(){
        return loginLogService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<LoginLog> findPage(int page, int size){
        return loginLogService.findPage(page, size);
    }

    @GetMapping("/findPageByLogin")
    public PageResult<LoginLog> findPageByLogin(int page, int size){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,Object> map = new HashMap();
        map.put("loginName",name);

        return loginLogService.findPage(map,page,size);
    }

    @PostMapping("/findList")
    public List<LoginLog> findList(@RequestBody Map<String,Object> searchMap){
        return loginLogService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<LoginLog> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  loginLogService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public LoginLog findById(Integer id){
        return loginLogService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody LoginLog loginLog){
        loginLogService.add(loginLog);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody LoginLog loginLog){
        loginLogService.update(loginLog);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        loginLogService.delete(id);
        return new Result();
    }

}
