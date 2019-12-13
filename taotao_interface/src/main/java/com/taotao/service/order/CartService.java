package com.taotao.service.order;

import java.util.List;
import java.util.Map;


public interface CartService {

    /**
     * 从redis提取购物车列表
     *
     * @param username
     * @return
     */
    List<Map<String, Object>> findCartList(String username);

    /**
     * 添加商品到购物车
     *
     * @param username
     * @param skuId
     * @param num
     */
    void addItem(String username, String skuId, Integer num);

    /**
     * 更新选中状态
     *
     * @param username
     * @param skuId
     * @param checked
     * @return
     */
    boolean updateChecked(String username, String skuId, boolean checked);

    /**
     * 全选逻辑
     *
     * @param username
     */
    boolean checkedAll(String username);

    /**
     * 删除被选中的商品
     *
     * @param username
     */
    void deleteCheckedGoods(String username);

    /**
     * 根据分类和消费额查询优惠金额
     *
     * @param categoryId
     * @param money
     * @return
     */
    int findPreMoneyByCategoryId(Long categoryId, int money);

    /**
     * 计算当前购物车选中商品的优惠价格
     *
     * @param username
     * @return
     */
    int preferential(String username);
}
