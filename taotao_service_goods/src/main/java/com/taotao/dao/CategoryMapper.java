package com.taotao.dao;

import com.alibaba.dubbo.config.annotation.Service;
import com.taotao.pojo.goods.Category;
import tk.mybatis.mapper.common.Mapper;

@Service
public interface CategoryMapper extends Mapper<Category> {

}
