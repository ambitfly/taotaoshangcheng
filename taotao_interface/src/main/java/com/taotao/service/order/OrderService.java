package com.taotao.service.order;

import com.taotao.entity.PageResult;
import com.taotao.pojo.order.Order;
import com.taotao.pojo.order.OrderOrderItem;

import java.util.List;
import java.util.Map;

/**
 * order业务逻辑层
 */
public interface OrderService {


    List<Order> findAll();


    PageResult<Order> findPage(int page, int size);


    List<Order> findList(Map<String, Object> searchMap);


    PageResult<Order> findPage(Map<String, Object> searchMap, int page, int size);


    Order findById(String id);




    void update(Order order);


    void delete(String id);

    OrderOrderItem findOrderOrderItemById(String id);

    List<Order> findByIds(String[] ids);

    /**
     * 批量发货
     * @param orders
     */
    void deliveryMany(List<Order> orders);

    void orderTimeOutLogic();

    /**
     * 生成用户订单
     * @param order
     * @return
     */
    Map<String,Object> add(Order order);

    /**
     * 修改订单状态
     * @param orderId
     * @param transactionId
     */
    void updatePayStatus(String orderId,String transactionId);

}
