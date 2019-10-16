package com.taotao.service.goods;
import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Album;

import java.util.*;

/**
 * album业务逻辑层
 */
public interface AlbumService {


    public List<Album> findAll();


    public PageResult<Album> findPage(int page, int size);


    public List<Album> findList(Map<String,Object> searchMap);


    public PageResult<Album> findPage(Map<String,Object> searchMap,int page, int size);


    public Album findById(Long id);

    public void add(Album album);


    public void update(Album album);


    public void delete(Long id);

    public void setImageNum(Album album);

    public void setImageNums(List<Album> albums);

    public String[] getImageItemsString(Long id);

    public void addItem(String imgUrl,Long id);

    public void deleteItem(String imgUrl,Long id);

    public PageResult<String> getImageItemsStringPage(Long id,int page,int size);

    public void moveItem(String imgUrl,Long removeId,Long moveId);



}
