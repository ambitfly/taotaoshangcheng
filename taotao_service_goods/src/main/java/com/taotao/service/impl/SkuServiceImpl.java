package com.taotao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taotao.dao.SkuMapper;
import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Sku;
import com.taotao.pojo.order.OrderItem;
import com.taotao.service.goods.SkuService;
import com.taotao.util.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    /**
     * 返回全部记录
     * @return
     */
    public List<Sku> findAll() {
        return skuMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Sku> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Sku> skus = (Page<Sku>) skuMapper.selectAll();
        return new PageResult<Sku>(skus.getTotal(),skus.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Sku> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return skuMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Sku> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<Sku> skus = (Page<Sku>) skuMapper.selectByExample(example);
        return new PageResult<Sku>(skus.getTotal(),skus.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Sku findById(String id) {
        return skuMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     * @param sku
     */
    public void add(Sku sku) {
        skuMapper.insert(sku);
        redisTemplate.boundHashOps(CacheKey.SKU_PRICE).put(sku.getId(),sku.getPrice());
    }

    /**
     * 修改
     * @param sku
     */
    public void update(Sku sku) {
        skuMapper.updateByPrimaryKeySelective(sku);
        redisTemplate.boundHashOps(CacheKey.SKU_PRICE).put(sku.getId(),sku.getPrice());
    }

    /**
     *  删除
     * @param id
     */
    public void delete(String id) {
        skuMapper.deleteByPrimaryKey(id);
        redisTemplate.boundHashOps(CacheKey.SKU_PRICE).delete(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 商品id
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andLike("id","%"+searchMap.get("id")+"%");
            }
            // 商品条码
            if(searchMap.get("sn")!=null && !"".equals(searchMap.get("sn"))){
                criteria.andLike("sn","%"+searchMap.get("sn")+"%");
            }
            // SKU名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            // 商品图片
            if(searchMap.get("image")!=null && !"".equals(searchMap.get("image"))){
                criteria.andLike("image","%"+searchMap.get("image")+"%");
            }
            // 商品图片列表
            if(searchMap.get("images")!=null && !"".equals(searchMap.get("images"))){
                criteria.andLike("images","%"+searchMap.get("images")+"%");
            }
            // SPUID
            if(searchMap.get("spuId")!=null && !"".equals(searchMap.get("spuId"))){
                criteria.andLike("spuId","%"+searchMap.get("spuId")+"%");
            }
            // 类目名称
            if(searchMap.get("categoryName")!=null && !"".equals(searchMap.get("categoryName"))){
                criteria.andLike("categoryName","%"+searchMap.get("categoryName")+"%");
            }
            // 品牌名称
            if(searchMap.get("brandName")!=null && !"".equals(searchMap.get("brandName"))){
                criteria.andLike("brandName","%"+searchMap.get("brandName")+"%");
            }
            // 规格
            if(searchMap.get("spec")!=null && !"".equals(searchMap.get("spec"))){
                criteria.andLike("spec","%"+searchMap.get("spec")+"%");
            }
            // 商品状态 1-正常，2-下架，3-删除
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andLike("status","%"+searchMap.get("status")+"%");
            }

            // 价格（分）
            if(searchMap.get("price")!=null ){
                criteria.andEqualTo("price",searchMap.get("price"));
            }
            // 库存数量
            if(searchMap.get("num")!=null ){
                criteria.andEqualTo("num",searchMap.get("num"));
            }
            // 库存预警数量
            if(searchMap.get("alertNum")!=null ){
                criteria.andEqualTo("alertNum",searchMap.get("alertNum"));
            }
            // 重量（克）
            if(searchMap.get("weight")!=null ){
                criteria.andEqualTo("weight",searchMap.get("weight"));
            }
            // 类目ID
            if(searchMap.get("categoryId")!=null ){
                criteria.andEqualTo("categoryId",searchMap.get("categoryId"));
            }
            // 销量
            if(searchMap.get("saleNum")!=null ){
                criteria.andEqualTo("saleNum",searchMap.get("saleNum"));
            }
            // 评论数
            if(searchMap.get("commentNum")!=null ){
                criteria.andEqualTo("commentNum",searchMap.get("commentNum"));
            }

        }
        return example;
    }

    public boolean dedutionStock(List<OrderItem> orderItemList) {
        boolean isDedution = true;
        for(OrderItem orderItem:orderItemList){
            Sku sku = findById(orderItem.getSkuId());
//            System.out.println(sku);
            if(sku==null){
                isDedution = false;
                break;
            }
            if(!"1".equals(sku.getStatus())){
                isDedution = false;
                break;
            }
            if(sku.getNum().intValue()<orderItem.getNum().intValue()){
                isDedution  =false;
                break;
            }
        }

        //执行扣减
        if(isDedution){
            for(OrderItem orderItem:orderItemList){
                skuMapper.dedutionStock(orderItem.getSkuId(),orderItem.getNum());
                skuMapper.addSaleNum(orderItem.getSkuId(),orderItem.getNum());
            }
        }
//        System.out.println("isDedution:"+isDedution);
        return isDedution;
    }

    public boolean backStock(List<OrderItem> orderItemList) {
        boolean isDedution = true;
        for(OrderItem orderItem:orderItemList){
            Sku sku = findById(orderItem.getSkuId());
//            System.out.println(sku);
            if(sku==null){
                isDedution = false;
                break;
            }
            if(!"1".equals(sku.getStatus())){
                isDedution = false;
                break;
            }
        }

        //执行恢复
        if(isDedution){
            for(OrderItem orderItem:orderItemList){
                skuMapper.dedutionStock(orderItem.getSkuId(),-orderItem.getNum());
                skuMapper.addSaleNum(orderItem.getSkuId(),-orderItem.getNum());
            }
        }
//        System.out.println("isDedution:"+isDedution);
        return isDedution;
    }

    public List<Sku> findBySpuId(String id) {
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("Spuid",id);

        return skuMapper.selectByExample(example);
    }

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    public void saveAllPriceToRedis() {
        if(!redisTemplate.hasKey(String.valueOf(CacheKey.SKU_PRICE))){
            List<Sku> skuList = skuMapper.selectAll();
            for(Sku sku:skuList){
                if("1".equals(sku.getStatus())){
                    redisTemplate.boundHashOps(CacheKey.SKU_PRICE).put(sku.getId(),sku.getPrice());
                }
            }
        }
    }

    public Integer findPrice(String id) {

        return (Integer)redisTemplate.boundHashOps(CacheKey.SKU_PRICE).get(id);
    }




}
