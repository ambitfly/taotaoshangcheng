package com.taotao.controller.business;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.PageResult;
import com.taotao.entity.Result;
import com.taotao.pojo.business.Activity;
import com.taotao.service.business.ActivityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Reference
    private ActivityService activityService;

    @GetMapping("/findAll")
    public List<Activity> findAll(){
        return activityService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Activity> findPage(int page, int size){
        return activityService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Activity> findList(@RequestBody Map<String,Object> searchMap){
        return activityService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Activity> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  activityService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Activity findById(Integer id){
        return activityService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Activity activity){
        activityService.add(activity);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Activity activity){
        activityService.update(activity);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        activityService.delete(id);
        return new Result();
    }

}
