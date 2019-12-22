package com.taotao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.taotao.dao.SkuMapper;
import com.taotao.dao.StockBackMapper;
import com.taotao.pojo.goods.StockBack;
import com.taotao.pojo.order.OrderItem;
import com.taotao.service.goods.StockBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service(interfaceClass = StockBackService.class)
public class StockBackServiceImpl implements StockBackService{
    @Autowired
    private StockBackMapper stockBackMapper;

    @Transactional
    public void addList(List<OrderItem> orderItems) {
        for(OrderItem orderItem:orderItems){
            StockBack stockBack = new StockBack();
            stockBack.setOrderId(orderItem.getOrderId());
            stockBack.setSkuId(orderItem.getSkuId());
            stockBack.setCreateTime(new Date());
            stockBack.setNum(orderItem.getNum());
            stockBack.setStatus("0");
            stockBackMapper.insert(stockBack);
        }
    }

    @Autowired
    private SkuMapper skuMapper;

    public void doBack() {
        System.out.println("回滚开始！");
        StockBack stockBack0 = new StockBack();
        stockBack0.setStatus("0");
        List<StockBack> stockBackList = stockBackMapper.select(stockBack0);
        for(StockBack stockBack:stockBackList){
            skuMapper.dedutionStock(stockBack.getSkuId(),-stockBack.getNum());
            skuMapper.addSaleNum(stockBack.getSkuId(),-stockBack.getNum());
            stockBack.setStatus("1");
            stockBack.setBackTime(new Date());
            stockBackMapper.updateByPrimaryKey(stockBack);
        }
        System.out.println("回滚结束！");
    }
}
