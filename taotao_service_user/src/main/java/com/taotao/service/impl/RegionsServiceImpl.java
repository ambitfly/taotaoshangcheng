package com.taotao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.taotao.dao.RegionsMapper;
import com.taotao.pojo.user.Regions;
import com.taotao.service.user.RegionsService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class RegionsServiceImpl implements RegionsService{
    @Autowired
    private RegionsMapper regionsMapper;
    public List<Regions> findByParentId(Integer patentId) {
        Example example = new Example(Regions.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentId",patentId);

        return regionsMapper.selectByExample(example);
    }
}
