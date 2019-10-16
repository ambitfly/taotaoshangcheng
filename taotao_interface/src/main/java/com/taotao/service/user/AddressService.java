package com.taotao.service.user;
import com.taotao.entity.PageResult;
import com.taotao.pojo.user.Address;

import java.util.*;

/**
 * address业务逻辑层
 */
public interface AddressService {


    public List<Address> findAll();


    public PageResult<Address> findPage(int page, int size);


    public List<Address> findList(Map<String,Object> searchMap);


    public PageResult<Address> findPage(Map<String,Object> searchMap,int page, int size);


    public Address findById(Integer id);

    public void add(Address address);


    public void update(Address address);


    public void delete(Integer id);

}
