package com.taotao.service.goods;

import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Album;

import java.util.List;
import java.util.Map;

/**
 * album业务逻辑层
 */
public interface AlbumService {


    List<Album> findAll();


    PageResult<Album> findPage(int page, int size);


    List<Album> findList(Map<String, Object> searchMap);


    PageResult<Album> findPage(Map<String, Object> searchMap, int page, int size);


    Album findById(Long id);

    void add(Album album);


    void update(Album album);


    void delete(Long id);

    void setImageNum(Album album);

    void setImageNums(List<Album> albums);

    String[] getImageItemsString(Long id);

    void addItem(String imgUrl, Long id);

    void deleteItem(String imgUrl, Long id);

    PageResult<String> getImageItemsStringPage(Long id, int page, int size);

    void moveItem(String imgUrl, Long removeId, Long moveId);



}
