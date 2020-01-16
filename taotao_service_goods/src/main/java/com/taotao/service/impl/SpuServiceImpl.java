package com.taotao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taotao.dao.*;
import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.*;
import com.taotao.service.goods.SpuService;
import com.taotao.util.IdWorker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service(interfaceClass = SpuService.class )
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private IdWorker idWorker;
    /**
     * 返回全部记录
     * @return
     */
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Spu> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Spu> spus = (Page<Spu>) spuMapper.selectAll();
        return new PageResult<Spu>(spus.getTotal(),spus.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Spu> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Spu> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<Spu> spus = (Page<Spu>) spuMapper.selectByExample(example);
        return new PageResult<Spu>(spus.getTotal(),spus.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Spu findById(String id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     * @param spu
     */
    public void add(Spu spu) {
        spuMapper.insert(spu);
    }

    /**
     * 修改
     * @param spu
     */
    public void update(Spu spu) {
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     *  删除
     * @param id
     */
    public void delete(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if(!"1".equals(spu.getIsDelete())){
            throw new RuntimeException("未执行逻辑删除，不能删除");
        }
        spuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 主键
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andLike("id","%"+searchMap.get("id")+"%");
            }
            // 货号
            if(searchMap.get("sn")!=null && !"".equals(searchMap.get("sn"))){
                criteria.andLike("sn","%"+searchMap.get("sn")+"%");
            }
            // SPU名
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            // 副标题
            if(searchMap.get("caption")!=null && !"".equals(searchMap.get("caption"))){
                criteria.andLike("caption","%"+searchMap.get("caption")+"%");
            }
            // 图片
            if(searchMap.get("image")!=null && !"".equals(searchMap.get("image"))){
                criteria.andLike("image","%"+searchMap.get("image")+"%");
            }
            // 图片列表
            if(searchMap.get("images")!=null && !"".equals(searchMap.get("images"))){
                criteria.andLike("images","%"+searchMap.get("images")+"%");
            }
            // 售后服务
            if(searchMap.get("saleService")!=null && !"".equals(searchMap.get("saleService"))){
                criteria.andLike("saleService","%"+searchMap.get("saleService")+"%");
            }
            // 介绍
            if(searchMap.get("introduction")!=null && !"".equals(searchMap.get("introduction"))){
                criteria.andLike("introduction","%"+searchMap.get("introduction")+"%");
            }
            // 规格列表
            if(searchMap.get("specItems")!=null && !"".equals(searchMap.get("specItems"))){
                criteria.andLike("specItems","%"+searchMap.get("specItems")+"%");
            }
            // 参数列表
            if(searchMap.get("paraItems")!=null && !"".equals(searchMap.get("paraItems"))){
                criteria.andLike("paraItems","%"+searchMap.get("paraItems")+"%");
            }
            // 是否上架
            if(searchMap.get("isMarketable")!=null && !"".equals(searchMap.get("isMarketable"))){
                criteria.andLike("isMarketable","%"+searchMap.get("isMarketable")+"%");
            }
            // 是否启用规格
            if(searchMap.get("isEnableSpec")!=null && !"".equals(searchMap.get("isEnableSpec"))){
                criteria.andLike("isEnableSpec","%"+searchMap.get("isEnableSpec")+"%");
            }
            // 是否删除
            if(searchMap.get("isDelete")!=null && !"".equals(searchMap.get("isDelete"))){
                criteria.andLike("isDelete","%"+searchMap.get("isDelete")+"%");
            }
            // 审核状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andLike("status","%"+searchMap.get("status")+"%");
            }

            // 品牌ID
            if(searchMap.get("brandId")!=null ){
                criteria.andEqualTo("brandId",searchMap.get("brandId"));
            }
            // 一级分类
            if(searchMap.get("category1Id")!=null ){
                criteria.andEqualTo("category1Id",searchMap.get("category1Id"));
            }
            // 二级分类
            if(searchMap.get("category2Id")!=null ){
                criteria.andEqualTo("category2Id",searchMap.get("category2Id"));
            }
            // 三级分类
            if(searchMap.get("category3Id")!=null ){
                criteria.andEqualTo("category3Id",searchMap.get("category3Id"));
            }
            // 模板ID
            if(searchMap.get("templateId")!=null ){
                criteria.andEqualTo("templateId",searchMap.get("templateId"));
            }
            // 运费模板id
            if(searchMap.get("freightId")!=null ){
                criteria.andEqualTo("freightId",searchMap.get("freightId"));
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


    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    CategoryBrandMapper categoryBrandMapper;
    @Autowired
    BrandMapper brandMapper;
    @Transactional
    public void saveGoods(Goods goods) {
        Spu spu = goods.getSpu();

        if(spu.getId()==null){
            Long id = idWorker.nextId();
            spu.setId(id+"");

            spuMapper.insertSelective(spu);
        }else {//修改
            //删除原来的sku列表
            Example example = new Example(Sku.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("spuId",spu.getId());
            skuMapper.deleteByExample(example);
            //执行spu修改
            spuMapper.updateByPrimaryKeySelective(spu);

        }



        Date date = new Date();

        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        //System.out.println(category);
        List<Sku> skuList = goods.getSkuList();
        for(Sku sku:skuList){
            if(sku.getId()==null){//新增
                sku.setId(idWorker.nextId()+"");
                sku.setCreateTime(date);
            }

            sku.setSpuId(spu.getId());

            if(sku.getSpec()==null || "".equals(sku.getSpec())){
                sku.setSpec("{} ");
            }
            StringBuffer name = new StringBuffer(spu.getName());
            Map<String,String> specMap = JSON.parseObject(sku.getSpec(),Map.class);
            for(String value:specMap.values()){
                name.append(" "+value);
            }
            sku.setName(name.toString());
            sku.setBrandName(brand.getName());
             sku.setUpdateTime(date);
             sku.setCategoryId(spu.getCategory3Id());//分类ID

            sku.setCategoryName(category.getName());//分类名称

            sku.setCommentNum(0);//初始化评论数
            sku.setSaleNum(0);

            skuMapper.insertSelective(sku);
        }

        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setBrandId(spu.getBrandId());
        categoryBrand.setCategoryId(spu.getCategory3Id());
        int count = categoryBrandMapper.selectCount(categoryBrand);

        if(count==0) {
            categoryBrandMapper.insert(categoryBrand);
        }
    }

    public Goods findGoodsById(String id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);

        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",id);
        List<Sku> skuList = skuMapper.selectByExample(example);

        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skuList);

        return goods;
    }
    @Autowired
    AuditMapper auditMapper;
    @Autowired
    GoodsLogMapper goodsLogMapper;



    //商品审核,记录审核信息
    @Transactional
    public void audit(String id,String status,String message,String admin_name){




        //记录商品日志未改之前状态
        Map<Sku,GoodsLog> map  = writeBeforeGoodsLog(id);




        //1.修改状态，
        Spu spu = new Spu();
        spu.setId(id);
        spu.setStatus(status);
        if("1".equals(status)){//审核通过自动上架
            spu.setIsMarketable("1");
        }
        spuMapper.updateByPrimaryKeySelective(spu);



        //2.记录 商品审核记录
        Date date = new Date();
        Audit audit = new Audit();
        audit.setAuditTime(date);
        audit.setAuditAdminName(admin_name);
        audit.setAuditStatus(status);
        audit.setAuditSpuId(id);
        audit.setAuditDetails(message);
        auditMapper.insert(audit);



        //3.记录商品日志改之后状态
        DateFormat df = DateFormat.getDateTimeInstance();
        String operInfor = admin_name+" "+df.format(date);

        writeAfterGoodsLog(map,operInfor);


    }



    @Transactional
    public void pull(String id) {


        Map<Sku,GoodsLog> map  = writeBeforeGoodsLog(id);
//==========================================

        Date date = new Date();
        Spu spu = new Spu();
        spu.setId(id);
        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKeySelective(spu);
        //发送消息

        List<String> skuIds = new ArrayList<String>();
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",spu.getId());
        List<Sku> skuList = skuMapper.selectByExample(example);
        for(Sku sku:skuList){
            skuIds.add(sku.getId());
        }
        rabbitTemplate.convertAndSend("exchange.fanout_pull","", JSON.toJSONString(skuIds));


//        ===================================
        //3.记录商品日志改之后状态
        DateFormat df = DateFormat.getDateTimeInstance();
        String operInfor = df.format(date);

        writeAfterGoodsLog(map,operInfor);

    }
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Transactional
    public void put(String id) {

        Map<Sku,GoodsLog> map  = writeBeforeGoodsLog(id);

        Date date = new Date();
        Spu spu = spuMapper.selectByPrimaryKey(id);
        String status = spu.getStatus();
        if(!"1".equals(status)){
            throw new RuntimeException("该商品未通过审核！");
        }

        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
        //发送消息
        Map<String,String> spuMap = new HashMap<String, String>();
        spuMap.put("spu",JSON.toJSONString(spu));
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",spu.getId());
        List<Sku> skuList = skuMapper.selectByExample(example);
        List<String> skuListString = new ArrayList<String>();
        for(Sku sku:skuList){
            skuListString.add(JSON.toJSONString(sku));
        }
        spuMap.put("skuList",JSON.toJSONString(skuListString));
        List<String> categoryList = new ArrayList<String>();
        categoryList.add(categoryMapper.selectByPrimaryKey(spu.getCategory1Id()).getName());//一级分类
        categoryList.add(categoryMapper.selectByPrimaryKey(spu.getCategory2Id()).getName());//二级分类
        categoryList.add(categoryMapper.selectByPrimaryKey(spu.getCategory3Id()).getName());//三级分类
        spuMap.put("categoryList",JSON.toJSONString(categoryList));
        String jsonStringSpuMap = JSON.toJSONString(spuMap);
        System.out.println(jsonStringSpuMap);
        rabbitTemplate.convertAndSend("exchange.fanout_put","", jsonStringSpuMap);

        //3.记录商品日志改之后状态
        DateFormat df = DateFormat.getDateTimeInstance();
        String operInfor = df.format(date);

        writeAfterGoodsLog(map,operInfor);
    }

    public int pullMany(String[] ids) {

        Map<String,Map<Sku,GoodsLog>> mapKeyId = new HashMap<String,Map<Sku,GoodsLog>>();
        for(String id:ids){
            Map<Sku,GoodsLog> map  = writeBeforeGoodsLog(id);
            mapKeyId.put(id,map);
        }
        /*======================================================*/
        Date date = new Date();
        Spu spu = new Spu();
        spu.setIsMarketable("0");

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        criteria.andEqualTo("isMarketable","1");
        criteria.andEqualTo("status","1");

        int i = spuMapper.updateByExampleSelective(spu,example);
        /*======================================================*/
        //3.记录商品日志改之后状态
        DateFormat df = DateFormat.getDateTimeInstance();
        String operInfor = df.format(date);

        for (String id:ids){
            Map<Sku,GoodsLog> map = mapKeyId.get(id);
            writeAfterGoodsLog(map,operInfor);
        }

        return i;
    }

    public int putMany(String[] ids) {

        Map<String,Map<Sku,GoodsLog>> mapKeyId = new HashMap<String,Map<Sku,GoodsLog>>();
        for(String id:ids){
            Map<Sku,GoodsLog> map  = writeBeforeGoodsLog(id);
            mapKeyId.put(id,map);
        }
        /*======================================================*/
        Date date = new Date();
        Spu spu = new Spu();
        spu.setIsMarketable("1");

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        criteria.andEqualTo("isMarketable","0");
        criteria.andEqualTo("status","1");

        int i = spuMapper.updateByExampleSelective(spu,example);
        /*======================================================*/

        //3.记录商品日志改之后状态
        DateFormat df = DateFormat.getDateTimeInstance();
        String operInfor = df.format(date);

        for (String id:ids){
            Map<Sku,GoodsLog> map = mapKeyId.get(id);
            writeAfterGoodsLog(map,operInfor);
        }

        return i;
    }

    public void logicDelete(String id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setIsDelete("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    public void recovery(String id) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setIsDelete("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    public List<Audit> findAuditBySpuId(String id) {
        Example example = new Example(Audit.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("auditSpuId",id);

        return auditMapper.selectByExample(example);
    }

    private Map<Sku,GoodsLog> writeBeforeGoodsLog(String id){

        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",id);
        List<Sku> skuList = skuMapper.selectByExample(example);

        Spu spu = spuMapper.selectByPrimaryKey(skuList.get(0).getSpuId());
//        List<GoodsLog> goodsLogs = new ArrayList<GoodsLog>();
        Map<Sku,GoodsLog> map = new HashMap<Sku, GoodsLog>();
        for(Sku sku:skuList){
            GoodsLog goodsLog = new GoodsLog();
//            System.out.println("==============="+sku.getPrice());
            if("1".equals(spu.getStatus())){
                goodsLog.setAuditStatus("原：审核通过");
            }else {
                goodsLog.setAuditStatus("原：未审核");
            }

            if("1".equals(spu.getIsMarketable())){
                goodsLog.setIsMarketable("原：上架中");
            }else {
                goodsLog.setIsMarketable("原：下架中");
            }

            goodsLog.setSalePrice("原："+sku.getPrice());

            goodsLog.setSpuId(sku.getSpuId());
            goodsLog.setSkuId(sku.getId());
            map.put(sku,goodsLog);
//            goodsLogs.add(goodsLog);
        }
        return map;
    }

    private void writeAfterGoodsLog(Map<Sku,GoodsLog> map,String operInfor){

        Spu spu = spuMapper.selectByPrimaryKey(map.keySet().iterator().next().getSpuId());
        for(Map.Entry<Sku,GoodsLog> entry:map.entrySet()){
            GoodsLog goodsLog = entry.getValue();
            Sku sku = entry.getKey();

            if("1".equals(spu.getStatus())){
                goodsLog.setAuditStatus(goodsLog.getAuditStatus()+",现：审核通过");
            }else {
                goodsLog.setAuditStatus(goodsLog.getAuditStatus()+",现：未审核");
            }

            if("1".equals(spu.getIsMarketable())){
                goodsLog.setIsMarketable(goodsLog.getIsMarketable()+",现：上架中");
            }else {
                goodsLog.setIsMarketable(goodsLog.getIsMarketable()+",现：下架中");
            }

            goodsLog.setSalePrice(goodsLog.getSalePrice()+",现："+sku.getPrice());

            goodsLog.setOperInfor(operInfor);

            goodsLogMapper.insertSelective(goodsLog);


        }
    }

    @Autowired
    StringRedisTemplate redisTemplate;
    public String saveGoodsToRedis(Map<String,Object> pojo,String uuid) {
        if("".equals(uuid)){
            uuid = UUID.randomUUID().toString();
        }

        String pojoString = JSON.toJSONString(pojo);
        redisTemplate.boundValueOps(uuid).set(pojoString);
        redisTemplate.boundValueOps(uuid).expire(10, TimeUnit.MINUTES);
        return uuid;
    }

    public Map<String, Object> findGoodsToRedis(String uuid) {
        String pojoString = redisTemplate.boundValueOps(uuid).get();
        Map<String,Object> pojo = JSON.parseObject(pojoString);
        System.out.println(pojo);
        return pojo;
    }

    /*    @Test
    public void fun(){
        Date date = new Date();
        DateFormat df2 = DateFormat.getDateTimeInstance();
        System.out.println(df2.format(date));
    }*/

    public String[] findAllId() {
        return  spuMapper.findAllId();
    }

    public void deleteSpecItems() {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specItems","{}");
        spuMapper.deleteByExample(example);
    }
}
