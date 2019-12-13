package com.taotao.controller.goods;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.entity.PageResult;
import com.taotao.entity.Result;
import com.taotao.pojo.goods.Category;
import com.taotao.service.goods.CategoryService;
import com.taotao.service.goods.TemplateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Reference
    private CategoryService categoryService;
    @Reference
    private TemplateService templateService;
    @GetMapping("/findAll")
    public List<Category> findAll(){
        return categoryService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Category> findPage(int page, int size){
        return categoryService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Category> findList(@RequestBody Map<String,Object> searchMap){
        return categoryService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Category> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  categoryService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Category findById(Integer id){
        return categoryService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Category category){
        categoryService.add(category);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Category category){
        categoryService.update(category);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        categoryService.delete(id);
        return new Result();
    }

    @GetMapping("/isReturnName")
    public Map<Integer,String> idReturnName(){
        return templateService.idReturnName();
    }

    @GetMapping("/findAll1Category")
    public List<Category> findAll1Category(){
        return categoryService.findAll1Category();
    }

    @GetMapping("/findByParentId")
    public List<Category> findByParentId(Integer id){
        return categoryService.findByParentId(id);
    }

    @GetMapping("/findNameByIds")
    public String[] findNameByIds(Integer[] ids){
        return categoryService.findNameByIds(ids);
    }
}
