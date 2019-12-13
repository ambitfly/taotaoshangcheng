package com.taotao.service.user;

import com.taotao.entity.PageResult;
import com.taotao.pojo.user.Address;

import java.util.List;
import java.util.Map;

/**
 * address业务逻辑层
 */
public interface AddressService {


    List<Address> findAll();


    PageResult<Address> findPage(int page, int size);


    List<Address> findList(Map<String, Object> searchMap);


    PageResult<Address> findPage(Map<String, Object> searchMap, int page, int size);


    Address findById(Integer id);

    void add(Address address);


    void update(Address address);


    void delete(Integer id);

}
