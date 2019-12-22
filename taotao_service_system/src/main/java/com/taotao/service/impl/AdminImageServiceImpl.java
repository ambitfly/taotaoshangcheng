package com.taotao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.taotao.dao.AdminImageMapper;
import com.taotao.pojo.system.AdminImage;
import com.taotao.service.system.AdminImageService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AdminImageServiceImpl implements AdminImageService {

    @Autowired
    AdminImageMapper adminImageMapper;


    public AdminImage findByAdminId(Integer id) {
        Example example = new Example(AdminImage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("adminId", id);
        List<AdminImage> adminImages = adminImageMapper.selectByExample(example);

        return adminImages.get(0);
    }
}
