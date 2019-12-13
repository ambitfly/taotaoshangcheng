package com.taotao.service.goods;

import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Audit;
import com.taotao.pojo.goods.Goods;
import com.taotao.pojo.goods.Spu;

import java.util.List;
import java.util.Map;

/**
 * spu业务逻辑层
 */
public interface SpuService {


    List<Spu> findAll();


    PageResult<Spu> findPage(int page, int size);


    List<Spu> findList(Map<String, Object> searchMap);


    PageResult<Spu> findPage(Map<String, Object> searchMap, int page, int size);


    Spu findById(String id);

    void add(Spu spu);


    void update(Spu spu);


    void delete(String id);

    void saveGoods(Goods goods);

    Goods findGoodsById(String id);

    //商品审核
    void audit(String id, String status, String message, String admin_Name);

    List<Audit> findAuditBySpuId(String id);

    //商品下架
    void pull(String id);

    //商品上架
    void put(String id);

    //批量下架
    int pullMany(String[] ids);

    //批量上架
    int putMany(String[] ids);

    //逻辑删除
    void logicDelete(String id);

    //恢复
    void recovery(String id);

    String saveGoodsToRedis(Map<String, Object> pojo, String uuid);

    Map<String, Object> findGoodsToRedis(String uuid);
}
