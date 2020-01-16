package com.taotao.dao;

import com.taotao.pojo.goods.Spu;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface SpuMapper extends Mapper<Spu> {
    @Select("select id from tb_spu")
    public String[] findAllId();
}
