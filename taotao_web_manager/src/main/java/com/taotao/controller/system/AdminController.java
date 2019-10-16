package com.taotao.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.PageResult;
import com.taotao.entity.Result;
import com.taotao.pojo.system.Admin;
import com.taotao.service.system.AdminImageService;
import com.taotao.service.system.AdminService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Reference
    private AdminService adminService;
    @Reference
    private AdminImageService adminImageService;


    @GetMapping("/findAll")
    public List<Admin> findAll(){
        return adminService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Admin> findPage(int page, int size){
        return adminService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Admin> findList(@RequestBody Map<String,Object> searchMap){
        return adminService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Admin> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  adminService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Admin findById(Integer id){
        return adminService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Admin admin){
        adminService.add(admin);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Map<String,Object> pojo){
        System.out.println("===================="+pojo);
        String encodePwd = BCrypt.hashpw((String)pojo.get("newPassword"),BCrypt.gensalt());
        pojo.put("newPassword",encodePwd);
        System.out.println("===================="+pojo);
        adminService.update(pojo);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        adminService.delete(id);
        return new Result();
    }
    @GetMapping("/findAdmin")
    public Map<String,Object> findAdmin(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,Object> map = new HashMap();
        map.put("loginName",name);
        Integer adminId = adminService.findList(map).get(0).getId();
        String image = adminImageService.findByAdminId(adminId).getImage();
        map.put("image",image);
        return map;
    }

    @PostMapping("/confirmPassword")
    public Result confirmPassword(String password){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,Object> map = new HashMap();
        map.put("loginName",name);
        String encodePwd = adminService.findList(map).get(0).getPassword();
       // System.out.println("================"+BCrypt.checkpw(password.trim(),encodePwd));

        if(BCrypt.checkpw(password,encodePwd)){
            return new Result(0,"密码校验正确！");
        }else{
            return new Result(1,"密码输入错误！");
        }
    }


}
