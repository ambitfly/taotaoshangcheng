package com.taotao.controller.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.PageResult;
import com.taotao.entity.Result;
import com.taotao.pojo.system.Resource;
import com.taotao.service.system.ResourceService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Reference
    private ResourceService resourceService;

    @GetMapping("/findAll")
    public List<Resource> findAll(){
        return resourceService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Resource> findPage(int page, int size){
        return resourceService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Resource> findList(@RequestBody Map<String,Object> searchMap){
        return resourceService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Resource> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  resourceService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Resource findById(Integer id){
        return resourceService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Resource resource){
        resourceService.add(resource);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Resource resource){
        resourceService.update(resource);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        resourceService.delete(id);
        return new Result();
    }

    @GetMapping("/listResource")
    public List<Map<String, Object>> listResource(){
        return resourceService.listResource();
    }
}
