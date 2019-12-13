package com.taotao.controller.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.PageResult;
import com.taotao.entity.Result;
import com.taotao.pojo.config.FreightTemplate;
import com.taotao.service.config.FreightTemplateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/freightTemplate")
public class FreightTemplateController {

    @Reference
    private FreightTemplateService freightTemplateService;

    @GetMapping("/findAll")
    public List<FreightTemplate> findAll(){
        return freightTemplateService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<FreightTemplate> findPage(int page, int size){
        return freightTemplateService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<FreightTemplate> findList(@RequestBody Map<String,Object> searchMap){
        return freightTemplateService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<FreightTemplate> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  freightTemplateService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public FreightTemplate findById(Integer id){
        return freightTemplateService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody FreightTemplate freightTemplate){
        freightTemplateService.add(freightTemplate);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody FreightTemplate freightTemplate){
        freightTemplateService.update(freightTemplate);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        freightTemplateService.delete(id);
        return new Result();
    }

}
