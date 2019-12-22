package com.taotao.pojo.user;

import javax.persistence.Id;
import java.io.Serializable;

public class Regions implements Serializable {
    @Id
    private Integer id;//区域编号
    private String name;//区域名称
    private String statisCode;//区域国标编码
    private String code;//区域编码
    private String fullName;//区域全称
    private Integer regionType;//类型
    private Integer sort;//排序
    private Integer parentId;//上级
    private Integer isDel;//逻辑删除


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatisCode() {
        return statisCode;
    }

    public void setStatisCode(String statisCode) {
        this.statisCode = statisCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getRegionType() {
        return regionType;
    }

    public void setRegionType(Integer regionType) {
        this.regionType = regionType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}
