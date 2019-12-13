package com.taotao.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.taotao.dao.PreferentialMapper;
import com.taotao.pojo.goods.Sku;
import com.taotao.pojo.goods.Spu;
import com.taotao.pojo.order.OrderItem;
import com.taotao.pojo.order.Preferential;
import com.taotao.service.goods.SkuService;
import com.taotao.service.goods.SpuService;
import com.taotao.service.order.CartService;
import com.taotao.util.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Reference
    private SkuService skuService;
    @Reference
    private SpuService spuService;
    @Autowired
    private PreferentialMapper preferentialMapper;

    @Override
    public List<Map<String, Object>> findCartList(String username) {
        List<Map<String, Object>> cartList = (List<Map<String, Object>>)
                redisTemplate.boundHashOps(CacheKey.CART_LIST).get(username);
        if (cartList == null) {
            return new ArrayList<>();
        }
        return cartList;
    }

    @Override
    public void addItem(String username, String skuId, Integer num) {
        List<Map<String, Object>> cartList = findCartList(username);
        boolean flag = false;//该商品是否在购物车中存在
        for (Map map : cartList) {
            OrderItem orderItem = (OrderItem) map.get("item");
            if (orderItem.getSkuId().equals(skuId)) {//如果存在该商品
                if (orderItem.getNum() <= 0) {
                    cartList.remove(map);
                    break;
                }
                int weight = orderItem.getWeight() / orderItem.getNum();
                orderItem.setNum(orderItem.getNum() + num);
                orderItem.setWeight(weight * orderItem.getNum());
                orderItem.setMoney(orderItem.getNum() * orderItem.getPrice());
                if (orderItem.getNum() <= 0) {
                    cartList.remove(map);
                }
                flag = true;
                break;
            }
        }

        //不存在添加商品到购物车
        if (!flag) {
            Sku sku = skuService.findById(skuId);
            if (sku == null) {
                throw new RuntimeException("商品不存在");
            }
            if (!"1".equals(sku.getStatus())) {
                throw new RuntimeException("商品已下架");
            }
            if (num < 0) {
                throw new RuntimeException("商品数量异常");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setSkuId(skuId);
            orderItem.setSpuId(sku.getSpuId());
            orderItem.setNum(num);
            orderItem.setImage(sku.getImage());
            orderItem.setPrice(sku.getPrice());
            orderItem.setName(sku.getName());
            orderItem.setMoney(sku.getPrice() * num);
            if (sku.getWeight() == null) {
                sku.setWeight(0);
            }
            orderItem.setWeight(sku.getWeight() * num);

            //商品分类
            Spu spu = spuService.findById(sku.getSpuId());
            orderItem.setCategoryId1(spu.getCategory1Id());
            orderItem.setCategoryId2(spu.getCategory2Id());
            orderItem.setCategoryId3(spu.getCategory3Id());

            Map map = new HashMap();
            map.put("item", orderItem);
            map.put("checked", true); //默认选中状态
            cartList.add(map);
        }

        redisTemplate.boundHashOps(CacheKey.CART_LIST).put(username, cartList);
    }

    @Override
    public boolean updateChecked(String username, String skuId, boolean checked) {
        // System.out.println("checked"+checked);
        List<Map<String, Object>> cartList = findCartList(username);
        boolean isOk = false;
        for (Map map : cartList) {
            OrderItem orderItem = (OrderItem) map.get("item");
            if (orderItem.getSkuId().equals(skuId)) {
                map.put("checked", checked);
                isOk = true;
                break;
            }
        }
        if (isOk) {
            redisTemplate.boundHashOps(CacheKey.CART_LIST).put(username, cartList);
        }

        return isOk;
    }

    @Override
    public boolean checkedAll(String username) {
        List<Map<String, Object>> cartList = findCartList(username);
        boolean flag = true;//判断是否全选
        for (Map map : cartList) {
            if (!(boolean) map.get("checked")) {
                flag = false;
                break;
            }
        }
        if (flag) {//如果全选,置为全不选
            for (Map map : cartList) {
                map.put("checked", false);
            }
        } else {//如果非全选，置为全选
            for (Map map : cartList) {
                map.put("checked", true);
            }
        }
        redisTemplate.boundHashOps(CacheKey.CART_LIST).put(username, cartList);
        return !flag;
    }

    @Override
    public void deleteCheckedGoods(String username) {
        List<Map<String, Object>> cartList = findCartList(username).stream()
                .filter(cart -> !(boolean) cart.get("checked"))
                .collect(Collectors.toList());
        redisTemplate.boundHashOps(CacheKey.CART_LIST).put(username, cartList);
    }

    @Override
    public int findPreMoneyByCategoryId(Long categoryId, int money) {
        Example example = new Example(Preferential.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("state", "1");
        criteria.andEqualTo("categoryId", categoryId);
        criteria.andLessThanOrEqualTo("buyMoney", money);
        Date date = new Date();
        criteria.andGreaterThanOrEqualTo("endTime", date);
        criteria.andLessThanOrEqualTo("startTime", date);
        example.setOrderByClause("buy_money desc");
        List<Preferential> preferentials = preferentialMapper.selectByExample(example);
        if (preferentials.size() >= 1) {
            Preferential preferential = preferentials.get(0);
            if ("1".equals(preferential.getType())) {//不可叠加
                return preferential.getPreMoney();
            } else {//可叠加
                int multiple = money / preferential.getBuyMoney();
                return preferential.getPreMoney() * multiple;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int preferential(String username) {
        //筛选出选中状态的商品
        List<OrderItem> orderItemList = findCartList(username).stream()
                .filter(cart -> (boolean) cart.get("checked"))
                .map(cart -> (OrderItem) cart.get("item"))
                .collect(Collectors.toList());

        //按分类统计每个分类的金额
        Map<Integer, IntSummaryStatistics> collect = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getCategoryId3, Collectors.summarizingInt(OrderItem::getMoney)));

        //累计并得出每个分类的优惠金额
        int allPreMoney = 0;
        for (Integer categoryId : collect.keySet()) {
            int money = (int) collect.get(categoryId).getSum();
            int preMoney = findPreMoneyByCategoryId((long) categoryId, money);
            allPreMoney += preMoney;
        }
        return allPreMoney;
    }
}
