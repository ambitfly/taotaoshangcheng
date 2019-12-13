package com.taotao.controller.business;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.PageResult;
import com.taotao.entity.Result;
import com.taotao.pojo.business.Ad;
import com.taotao.service.business.AdService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ad")
public class AdController {

    @Reference
    private AdService adService;

    @GetMapping("/findAll")
    public List<Ad> findAll(){
        return adService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Ad> findPage(int page, int size){
        return adService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Ad> findList(@RequestBody Map<String,Object> searchMap){
        return adService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Ad> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  adService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Ad findById(Integer id){
        return adService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Ad ad){
        adService.add(ad);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Ad ad){
        adService.update(ad);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        adService.delete(id);
        return new Result();
    }

}
