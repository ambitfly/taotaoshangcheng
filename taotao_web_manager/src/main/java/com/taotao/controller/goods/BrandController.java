package com.taotao.controller.goods;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyun.oss.OSSClient;
import com.taotao.entity.PageResult;
import com.taotao.entity.Result;
import com.taotao.pojo.goods.Brand;
import com.taotao.service.goods.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;
    @Autowired
    OSSClient ossClient;

    @GetMapping("/findAll")
    public List<Brand> findAll(){
        return brandService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Brand> findPage(int page, int size){
        return brandService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Brand> findList(@RequestBody Map<String,Object> searchMap){
        return brandService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Brand> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  brandService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Brand findById(Integer id){
        return brandService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Brand brand){
        brandService.update(brand);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Integer id){
        String bucketName = "qingchengt";
        Brand brand = brandService.findById(id);
        brandService.delete(id);
        String imgUrl = brand.getImage();
        String imageName = imgUrl.replace("https://"+bucketName+".oss-cn-shanghai.aliyuncs.com/","");
        //String imgRealPath = request.getSession().getServletContext().getRealPath(imgUri);
        //File img = new File(imgRealPath);
        // img.delete();
        ossClient.deleteObject(bucketName,imageName);
        ossClient.shutdown();
        return new Result();
    }

}
