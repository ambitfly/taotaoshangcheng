package com.taotao.service.goods;

import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Brand;

import java.util.List;
import java.util.Map;

/**
 * brand业务逻辑层
 */
public interface BrandService {


    List<Brand> findAll();


    PageResult<Brand> findPage(int page, int size);


    List<Brand> findList(Map<String, Object> searchMap);


    PageResult<Brand> findPage(Map<String, Object> searchMap, int page, int size);


    Brand findById(Integer id);

    void add(Brand brand);


    void update(Brand brand);


    void delete(Integer id);



}
