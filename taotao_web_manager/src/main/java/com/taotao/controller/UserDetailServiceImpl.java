package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.pojo.system.Admin;
import com.taotao.service.system.AdminService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetailServiceImpl implements UserDetailsService {
    @Reference
    private AdminService adminService;

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("loginName", s);
        map.put("status", "1");
        List<Admin> admins = adminService.findList(map);
        if (admins.size() == 0) {
            return null;
        }


        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        /*List<String> resKeyList = adminService.findResKeysByLoginName(s);
        for(String resKey:resKeyList){
            System.out.println("resKey======"+resKey);
            grantedAuthorities.add(new SimpleGrantedAuthority(resKey));
        }*/
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new User(s, admins.get(0).getPassword(), grantedAuthorities);
    }
}
