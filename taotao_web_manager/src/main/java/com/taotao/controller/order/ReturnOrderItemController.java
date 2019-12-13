package com.taotao.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.PageResult;
import com.taotao.entity.Result;
import com.taotao.pojo.order.ReturnOrderItem;
import com.taotao.service.order.ReturnOrderItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/returnOrderItem")
public class ReturnOrderItemController {

    @Reference
    private ReturnOrderItemService returnOrderItemService;

    @GetMapping("/findAll")
    public List<ReturnOrderItem> findAll(){
        return returnOrderItemService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<ReturnOrderItem> findPage(int page, int size){
        return returnOrderItemService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<ReturnOrderItem> findList(@RequestBody Map<String,Object> searchMap){
        return returnOrderItemService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<ReturnOrderItem> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  returnOrderItemService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public ReturnOrderItem findById(Long id){
        return returnOrderItemService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody ReturnOrderItem returnOrderItem){
        returnOrderItemService.add(returnOrderItem);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody ReturnOrderItem returnOrderItem){
        returnOrderItemService.update(returnOrderItem);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Long id){
        returnOrderItemService.delete(id);
        return new Result();
    }

}
