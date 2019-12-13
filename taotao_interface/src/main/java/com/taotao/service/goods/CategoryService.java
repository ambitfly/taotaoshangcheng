package com.taotao.service.goods;

import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Category;

import java.util.List;
import java.util.Map;

/**
 * category业务逻辑层
 */
public interface CategoryService {


    List<Category> findAll();


    PageResult<Category> findPage(int page, int size);


    List<Category> findList(Map<String, Object> searchMap);


    PageResult<Category> findPage(Map<String, Object> searchMap, int page, int size);


    Category findById(Integer id);

    void add(Category category);


    void update(Category category);


    void delete(Integer id);

    List<Category> findAll1Category();

    List<Map> findCategoryTree();

    void saveCategoryTreeToRedis();

    /**
     * 通过父级分类查询商品分类
     * @param id   父级id
     * @return
     */
    List<Category> findByParentId(Integer id);

    String[] findNameByIds(Integer[] ids);
}
