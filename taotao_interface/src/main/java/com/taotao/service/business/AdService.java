package com.taotao.service.business;

import com.taotao.entity.PageResult;
import com.taotao.pojo.business.Ad;

import java.util.List;
import java.util.Map;

/**
 * ad业务逻辑层
 */
public interface AdService {
    String WEBINDEXLB = "web_index_lb";

    List<Ad> findAll();


    PageResult<Ad> findPage(int page, int size);


    List<Ad> findList(Map<String, Object> searchMap);


    PageResult<Ad> findPage(Map<String, Object> searchMap, int page, int size);


    Ad findById(Integer id);

    void add(Ad ad);


    void update(Ad ad);


    void delete(Integer id);

    List<Ad> findByPosition(String position);

    void saveAdToRedisByPositon(String position);

    void saveAllAdtoRedis();

}
