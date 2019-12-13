package com.taotao.controller.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.PageResult;
import com.taotao.entity.Result;
import com.taotao.pojo.user.Address;
import com.taotao.service.user.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    @GetMapping("/findAll")
    public List<Address> findAll(){
        return addressService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Address> findPage(int page, int size){
        return addressService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Address> findList(@RequestBody Map<String,Object> searchMap){
        return addressService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Address> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  addressService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Address findById(Integer id){
        return addressService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Address address){
        addressService.add(address);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Address address){
        addressService.update(address);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        addressService.delete(id);
        return new Result();
    }

}
