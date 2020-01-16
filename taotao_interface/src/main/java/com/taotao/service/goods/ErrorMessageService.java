package com.taotao.service.goods;

public interface ErrorMessageService {
    /**
     * 插入异常数据
     * @param spuId
     * @param skuId
     */
    void insert(String spuId,String skuId);

    void setSpuStatus();
}
