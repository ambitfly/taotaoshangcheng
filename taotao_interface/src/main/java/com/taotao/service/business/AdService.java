package com.taotao.service.business;
import com.taotao.entity.PageResult;
import com.taotao.pojo.business.Ad;

import java.util.*;

/**
 * ad业务逻辑层
 */
public interface AdService {
    public static final String WEBINDEXLB = "web_index_lb";

    public List<Ad> findAll();


    public PageResult<Ad> findPage(int page, int size);


    public List<Ad> findList(Map<String,Object> searchMap);


    public PageResult<Ad> findPage(Map<String,Object> searchMap,int page, int size);


    public Ad findById(Integer id);

    public void add(Ad ad);


    public void update(Ad ad);


    public void delete(Integer id);

    public List<Ad> findByPosition(String position);

    public void saveAdToRedisByPositon(String position);

    public void saveAllAdtoRedis();

}
