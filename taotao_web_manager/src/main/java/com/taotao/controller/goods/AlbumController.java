package com.taotao.controller.goods;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyun.oss.OSSClient;
import com.taotao.entity.PageResult;
import com.taotao.entity.Result;
import com.taotao.pojo.goods.Album;
import com.taotao.service.goods.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/album")
public class AlbumController {

    @Reference
    private AlbumService albumService;
    @Autowired
    OSSClient ossClient;

    @GetMapping("/findAll")
    public List<Album> findAll(){
        return albumService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult<Album> findPage(int page, int size){
        return albumService.findPage(page, size);
    }

    @PostMapping("/findList")
    public List<Album> findList(@RequestBody Map<String,Object> searchMap){
        return albumService.findList(searchMap);
    }

    @PostMapping("/findPage")
    public PageResult<Album> findPage(@RequestBody Map<String,Object> searchMap,int page, int size){
        return  albumService.findPage(searchMap,page,size);
    }

    @GetMapping("/findById")
    public Album findById(Long id){
        return albumService.findById(id);
    }


    @PostMapping("/add")
    public Result add(@RequestBody Album album){
        albumService.add(album);
        return new Result();
    }

    @PostMapping("/update")
    public Result update(@RequestBody Album album){
        albumService.update(album);
        return new Result();
    }

    @GetMapping("/delete")
    public Result delete(Long id){
        String bucketName = "qingchengt";
        Album album = albumService.findById(id);
        albumService.delete(id);
        String imgURl = album.getImage();
        String imageName = imgURl.replace("https://"+bucketName+".oss-cn-shanghai.aliyuncs.com/","");
        ossClient.deleteObject(bucketName,imageName);
        ossClient.shutdown();
        return new Result();
    }
    @GetMapping("/item/findAll")
    public String[] getImageItemsString(Long id){
        return albumService.getImageItemsString(id);
    }

    @GetMapping("/item/findPage")
    public PageResult<String> getImageItemsStringPage(Long id,int page,int size){
        return  albumService.getImageItemsStringPage(id, page, size);
    }

    @PostMapping("/item/add")
    public Result addItem(String imgUrl,Long id){
        albumService.addItem(imgUrl,id);
        return new Result();
    }

    @GetMapping("/item/delete")
    public Result deleteItem(String imgUrl, Long id){
        albumService.deleteItem(imgUrl,id);
        String bucketName = "qingchengt";
        String imageName = imgUrl.replace("https://"+bucketName+".oss-cn-shanghai.aliyuncs.com/","");
        ossClient.deleteObject(bucketName,imageName);
        ossClient.shutdown();
       // System.out.println("=====================================");
        return new Result();
    }

    @GetMapping("/item/move")
    public Result moveItem(String imgUrl,Long removeId,Long moveId){
        albumService.moveItem(imgUrl, removeId, moveId);
        return new Result();
    }


}
