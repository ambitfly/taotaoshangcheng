package com.taotao.pojo.goods;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_goods_log")
public class GoodsLog {
    @Id
    private Integer id;


    private String salePrice;
    private String promotePrice;
    private String giveCoin;
    private String coinPrice;
    private String isMarketable;
    private String auditStatus;
    private String operInfor;
    private String spuId;
    private String skuId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getPromotePrice() {
        return promotePrice;
    }

    public void setPromotePrice(String promotePrice) {
        this.promotePrice = promotePrice;
    }

    public String getGiveCoin() {
        return giveCoin;
    }

    public void setGiveCoin(String giveCoin) {
        this.giveCoin = giveCoin;
    }

    public String getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(String coinPrice) {
        this.coinPrice = coinPrice;
    }

    public String getIsMarketable() {
        return isMarketable;
    }

    public void setIsMarketable(String isMarketable) {
        this.isMarketable = isMarketable;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getOperInfor() {
        return operInfor;
    }

    public void setOperInfor(String operInfor) {
        this.operInfor = operInfor;
    }

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
}