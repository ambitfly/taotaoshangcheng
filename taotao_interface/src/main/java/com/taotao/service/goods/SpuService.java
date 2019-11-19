package com.taotao.service.goods;
import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Audit;
import com.taotao.pojo.goods.Goods;
import com.taotao.pojo.goods.Spu;

import java.util.*;

/**
 * spu业务逻辑层
 */
public interface SpuService {


    public List<Spu> findAll();


    public PageResult<Spu> findPage(int page, int size);


    public List<Spu> findList(Map<String,Object> searchMap);


    public PageResult<Spu> findPage(Map<String,Object> searchMap,int page, int size);


    public Spu findById(String id);

    public void add(Spu spu);


    public void update(Spu spu);


    public void delete(String id);

    public void saveGoods(Goods goods);

    public Goods findGoodsById(String id);

    //商品审核
    public void audit(String id,String status,String message,String admin_Name);

    public List<Audit> findAuditBySpuId(String id);

    //商品下架
    public void pull(String id);

    //商品上架
    public void put(String id);

    //批量下架
    public int pullMany(String[] ids);

    //批量上架
    public int putMany(String[] ids);

    //逻辑删除
    public void logicDelete(String id);

    //恢复
    public void recovery(String id);

    public String saveGoodsToRedis(Map<String,Object> pojo,String uuid);

    public Map<String,Object> findGoodsToRedis(String uuid);
}
