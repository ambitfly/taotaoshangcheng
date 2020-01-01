package com.taotao.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taotao.dao.OrderConfigMapper;
import com.taotao.dao.OrderItemMapper;
import com.taotao.dao.OrderLogMapper;
import com.taotao.dao.OrderMapper;
import com.taotao.entity.PageResult;
import com.taotao.pojo.order.*;
import com.taotao.service.goods.SkuService;
import com.taotao.service.order.CartService;
import com.taotao.service.order.OrderService;
import com.taotao.service.order.WxPayService;
import com.taotao.util.IdWorker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;


import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Reference
    private SkuService skuService;
    /**
     * 返回全部记录
     * @return
     */
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Order> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Order> orders = (Page<Order>) orderMapper.selectAll();
        return new PageResult<Order>(orders.getTotal(),orders.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Order> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return orderMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Order> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<Order> orders = (Page<Order>) orderMapper.selectByExample(example);
        return new PageResult<Order>(orders.getTotal(),orders.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Order findById(String id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * 新增
     * @param order
     */
    @Override
    public Map<String, Object> add(Order order) {

        List<Map<String,Object>> orderItemList = cartService.findNewOrderItemList(order.getUsername());
        //提取选中状态的
        List<OrderItem> orderItems = orderItemList.stream().filter(cart->(boolean)cart.get("checked"))
                .map(cart->(OrderItem)cart.get("item"))
                .collect(Collectors.toList());

        if(!skuService.dedutionStock(orderItems)){
            throw new RuntimeException("扣减库存失败！");
        }
        try {
            //设置订单
            order.setId(idWorker.nextId()+"");
            IntStream numStream = orderItems.stream().mapToInt(OrderItem::getNum);
            IntStream moneyStream = orderItems.stream().mapToInt(OrderItem::getMoney);
            int totalNum = numStream.sum();
            int totalMoney = moneyStream.sum();
            int preMoney = cartService.preferential(order.getUsername());
            order.setTotalMoney(totalNum);
            order.setTotalNum(totalMoney);
            order.setPreMoney(preMoney);
            order.setPayMoney(totalMoney-preMoney);
            order.setCreateTime(new Date());
            order.setOrderStatus("0");//0待付款、1待发货、2已发货、3已完成、4已关闭
            order.setPayStatus("0");//0未支付、1已支付、2已退款
            order.setConsignStatus("0");//0未发货、1已发货
            orderMapper.insert(order);

            //保存订单明细
            double proportion = (double)order.getPayMoney()/totalMoney;

            for(OrderItem orderItem:orderItems){
                orderItem.setOrderId(order.getId());
                orderItem.setId(idWorker.nextId()+"");
                orderItem.setPayMoney((int)(orderItem.getMoney()*proportion));
                orderItemMapper.insert(orderItem);
            }

        } catch (Exception e) {
            rabbitTemplate.convertAndSend("","queue.skuback", JSON.toJSONString(orderItems));
            e.printStackTrace();
            throw new RuntimeException("订单生成失败");
        }
        //发送消息
        rabbitTemplate.convertAndSend("","queue.ordercreatetest",order.getId());

        //清除选中购物车
        cartService.deleteCheckedGoods(order.getUsername());

        //返回订单编号和支付的金额
        Map<String,Object> map = new HashMap<>();
        map.put("orderId",order.getId());
        map.put("money",order.getPayMoney());
        return map;
    }

    /**
     * 修改
     * @param order
     */
    public void update(Order order) {
        orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     *  删除
     * @param id
     */
    public void delete(String id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 订单id
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andLike("id","%"+searchMap.get("id")+"%");
            }
            // 支付类型，1、在线支付、0 货到付款
            if(searchMap.get("payType")!=null && !"".equals(searchMap.get("payType"))){
                criteria.andLike("payType","%"+searchMap.get("payType")+"%");
            }
            // 物流名称
            if(searchMap.get("shippingName")!=null && !"".equals(searchMap.get("shippingName"))){
                criteria.andLike("shippingName","%"+searchMap.get("shippingName")+"%");
            }
            // 物流单号
            if(searchMap.get("shippingCode")!=null && !"".equals(searchMap.get("shippingCode"))){
                criteria.andLike("shippingCode","%"+searchMap.get("shippingCode")+"%");
            }
            // 用户名称
            if(searchMap.get("username")!=null && !"".equals(searchMap.get("username"))){
                criteria.andLike("username","%"+searchMap.get("username")+"%");
            }
            // 买家留言
            if(searchMap.get("buyerMessage")!=null && !"".equals(searchMap.get("buyerMessage"))){
                criteria.andLike("buyerMessage","%"+searchMap.get("buyerMessage")+"%");
            }
            // 是否评价
            if(searchMap.get("buyerRate")!=null && !"".equals(searchMap.get("buyerRate"))){
                criteria.andLike("buyerRate","%"+searchMap.get("buyerRate")+"%");
            }
            // 收货人
            if(searchMap.get("receiverContact")!=null && !"".equals(searchMap.get("receiverContact"))){
                criteria.andLike("receiverContact","%"+searchMap.get("receiverContact")+"%");
            }
            // 收货人手机
            if(searchMap.get("receiverMobile")!=null && !"".equals(searchMap.get("receiverMobile"))){
                criteria.andLike("receiverMobile","%"+searchMap.get("receiverMobile")+"%");
            }
            // 收货人地址
            if(searchMap.get("receiverAddress")!=null && !"".equals(searchMap.get("receiverAddress"))){
                criteria.andLike("receiverAddress","%"+searchMap.get("receiverAddress")+"%");
            }
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if(searchMap.get("sourceType")!=null && !"".equals(searchMap.get("sourceType"))){
                criteria.andLike("sourceType","%"+searchMap.get("sourceType")+"%");
            }
            // 交易流水号
            if(searchMap.get("transactionId")!=null && !"".equals(searchMap.get("transactionId"))){
                criteria.andLike("transactionId","%"+searchMap.get("transactionId")+"%");
            }
            // 订单状态
            if(searchMap.get("orderStatus")!=null && !"".equals(searchMap.get("orderStatus"))){
                criteria.andLike("orderStatus","%"+searchMap.get("orderStatus")+"%");
            }
            // 支付状态
            if(searchMap.get("payStatus")!=null && !"".equals(searchMap.get("payStatus"))){
                criteria.andLike("payStatus","%"+searchMap.get("payStatus")+"%");
            }
            // 发货状态
            if(searchMap.get("consignStatus")!=null && !"".equals(searchMap.get("consignStatus"))){
                criteria.andLike("consignStatus","%"+searchMap.get("consignStatus")+"%");
            }
            // 是否删除
            if(searchMap.get("isDelete")!=null && !"".equals(searchMap.get("isDelete"))){
                criteria.andLike("isDelete","%"+searchMap.get("isDelete")+"%");
            }

            // 数量合计
            if(searchMap.get("totalNum")!=null ){
                criteria.andEqualTo("totalNum",searchMap.get("totalNum"));
            }
            // 金额合计
            if(searchMap.get("totalMoney")!=null ){
                criteria.andEqualTo("totalMoney",searchMap.get("totalMoney"));
            }
            // 优惠金额
            if(searchMap.get("preMoney")!=null ){
                criteria.andEqualTo("preMoney",searchMap.get("preMoney"));
            }
            // 邮费
            if(searchMap.get("postFee")!=null ){
                criteria.andEqualTo("postFee",searchMap.get("postFee"));
            }
            // 实付金额
            if(searchMap.get("payMoney")!=null ){
                criteria.andEqualTo("payMoney",searchMap.get("payMoney"));
            }

        }
        return example;
    }



    public OrderOrderItem findOrderOrderItemById(String id) {
        OrderOrderItem orderOrderItem = new OrderOrderItem();
        Order order = orderMapper.selectByPrimaryKey(id);
        String orderStatus = order.getOrderStatus();
        int status = Integer.parseInt(orderStatus) + 1;
        order.setOrderStatus(status+"");

        orderOrderItem.setOrder(order);

        Example example = new Example(OrderItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",id);
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);

        orderOrderItem.setOrderItems(orderItems);

        return orderOrderItem;
    }

    public List<Order> findByIds(String[] ids){
        List<Order> orderList = new ArrayList<Order>();
        for(String id:ids){
            Order order = orderMapper.selectByPrimaryKey(id);
            if("1".equals(order.getOrderStatus())){
                orderList.add(order);
            }

        }
        return orderList;
    }

    @Autowired
    OrderLogMapper orderLogMapper;
    public void deliveryMany(List<Order> orders) {
        Date date = new Date();
        for(Order order:orders){
            order.setOrderStatus("2");
            order.setConsignStatus("1");
            order.setConsignTime(date);
            orderMapper.updateByPrimaryKey(order);

            //添加订单日志
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId()+"");
            orderLog.setOperater("me");
            orderLog.setOperateTime(date);
            orderLog.setOrderId(order.getId());
            orderLog.setOrderStatus(order.getOrderStatus());
            orderLog.setPayStatus(order.getPayStatus());
            orderLog.setConsignStatus(order.getConsignStatus());
            orderLogMapper.insertSelective(orderLog);

        }
    }
    @Autowired
    OrderConfigMapper orderConfigMapper;
    //定时删除超时订单


    public void orderTimeOutLogic() {
        OrderConfig orderConfig = orderConfigMapper.selectByPrimaryKey(1);
        Integer orderTimeOut = orderConfig.getOrderTimeout();
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(orderTimeOut);

        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLessThan("createTime",localDateTime);
        criteria.andEqualTo("orderStatus","0");
        criteria.andEqualTo("isDelete","0");

        List<Order> orders = orderMapper.selectByExample(example);

        for(Order order:orders){
            OrderLog orderLog = new OrderLog();
            orderLog.setOperater("system");
            orderLog.setOperateTime(new Date());
            orderLog.setOrderStatus("4");
            orderLog.setPayStatus(order.getPayStatus());
            orderLog.setConsignStatus(order.getConsignStatus());
            orderLog.setRemarks("超时订单，系统自动关闭");
            orderLog.setOrderId(order.getId());
            orderLogMapper.insert(orderLog);



            order.setOrderStatus("4");
            order.setCloseTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }

    }

    @Override
    public void updatePayStatus(String orderId, String transactionId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order!=null&&"0".equals(order.getPayStatus())&&"0".equals(order.getOrderStatus())){
            order.setPayStatus("1");
            order.setOrderStatus("1");
            order.setUpdateTime(new Date());
            order.setPayTime(new Date());
            order.setTransactionId(transactionId);
            orderMapper.updateByPrimaryKeySelective(order);
            //记录订单变动日志
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId()+"");
            orderLog.setOperater("system");
            orderLog.setOperateTime(new Date());
            orderLog.setOrderId(orderId);
            orderLog.setRemarks("支付流水号："+transactionId);
            orderLog.setPayStatus("1");
            orderLog.setOrderStatus("1");
            orderLog.setConsignStatus("0");
            orderLogMapper.insert(orderLog);
        }
    }

    @Autowired
    private WxPayService wxPayService;
    @Override
    public void shutDownOrder(String orderId) {
        //关闭微信订单
        wxPayService.shutDownNative(orderId);

        //更改订单状态
        Order order = orderMapper.selectByPrimaryKey(orderId);
        order.setOrderStatus("4");
        orderMapper.updateByPrimaryKeySelective(order);

        //记录订单日志
        OrderLog orderLog = new OrderLog();
        orderLog.setId(idWorker.nextId()+"");
        orderLog.setOperater("system");
        orderLog.setOperateTime(new Date());
        orderLog.setOrderId(orderId);
        orderLog.setRemarks("关闭订单");
        orderLog.setPayStatus("0");
        orderLog.setOrderStatus("4");
        orderLog.setConsignStatus("0");
        orderLogMapper.insert(orderLog);

        //恢复商品库存
        Example example = new Example(OrderItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        List<OrderItem> orderItemList = orderItemMapper.selectByExample(example);
        skuService.backStock(orderItemList);

    }
}
