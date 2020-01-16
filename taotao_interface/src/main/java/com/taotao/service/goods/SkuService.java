package com.taotao.service.goods;

import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Sku;
import com.taotao.pojo.order.OrderItem;

import java.util.List;
import java.util.Map;

/**
 * sku业务逻辑层
 */
public interface SkuService {


    List<Sku> findAll();


    PageResult<Sku> findPage(int page, int size);


    List<Sku> findList(Map<String, Object> searchMap);


    PageResult<Sku> findPage(Map<String, Object> searchMap, int page, int size);


    Sku findById(String id);

    void add(Sku sku);


    void update(Sku sku);


    void delete(String id);

    List<Sku> findBySpuId(String id);

    void saveAllPriceToRedis();

    Integer findPrice(String id);

    public boolean dedutionStock(List<OrderItem> orderItemList);

    boolean backStock(List<OrderItem> orderItemList);

    void initSkuData();

    void initSkuData(String spuId);
}
