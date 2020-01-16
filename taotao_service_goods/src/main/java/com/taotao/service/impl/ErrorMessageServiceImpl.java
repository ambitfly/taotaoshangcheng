package com.taotao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.taotao.dao.ErrorMessageMapper;
import com.taotao.dao.SkuMapper;
import com.taotao.dao.SpuMapper;
import com.taotao.pojo.goods.ErrorMessage;
import com.taotao.pojo.goods.Sku;
import com.taotao.pojo.goods.Spu;
import com.taotao.service.goods.ErrorMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service(interfaceClass = ErrorMessageService.class)
public class ErrorMessageServiceImpl implements ErrorMessageService {

    @Autowired
    private ErrorMessageMapper errorMapper;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;
    public void insert(String spuId, String skuId) {
        ErrorMessage error = new ErrorMessage();
        error.setSkuId(skuId);
        error.setSpuId(spuId);
        error.setStatus("0");
        errorMapper.insert(error);
    }

    @Transactional
    public void setSpuStatus() {
        Example example = new Example(ErrorMessage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status","0");
        List<ErrorMessage> errorMessageList = errorMapper.selectByExample(example);
        for(ErrorMessage errorMessage:errorMessageList){
            Sku sku = new Sku();
            sku.setId(errorMessage.getSkuId());
            sku.setStatus("0");
            skuMapper.updateByPrimaryKeySelective(sku);
            Spu spu = new Spu();
            spu.setId(errorMessage.getSpuId());
            spu.setStatus("0");
            spuMapper.updateByPrimaryKeySelective(spu);
            errorMessage.setStatus("1");
            errorMapper.updateByPrimaryKeySelective(errorMessage);
        }
    }
}
