package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.Result;
import com.taotao.pojo.user.Address;
import com.taotao.service.user.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference
    private AddressService addressService;
    @GetMapping("/findAddress")
    public List<Address> findAddress(){
        return addressService.findAddressByUserName(this.getUsername());
    }

    @PostMapping("/add")
    public Result addAddress(@RequestBody Address address){
        address.setUsername(this.getUsername());
       addressService.add(address);
       return new Result();
    }
    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
