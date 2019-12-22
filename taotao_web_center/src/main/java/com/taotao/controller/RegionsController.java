package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.pojo.user.Regions;
import com.taotao.service.user.RegionsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("regions")
public class RegionsController {
    @Reference
    private RegionsService regionsService;

    @GetMapping("/findByParentId")
    public List<Regions> findByParentId(Integer parentId){
        return regionsService.findByParentId(parentId);
    }
}
