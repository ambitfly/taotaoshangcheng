package com.taotao.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taotao.dao.AlbumMapper;
import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Album;
import com.taotao.service.goods.AlbumService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = AlbumService.class)
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumMapper albumMapper;

    /**
     * 返回全部记录
     *
     * @return
     */
    public List<Album> findAll() {
        List<Album> albums = albumMapper.selectAll();
        setImageNums(albums);
        return albums;
    }

    /**
     * 分页查询
     *
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Album> findPage(int page, int size) {
        PageHelper.startPage(page, size);
        Page<Album> albums = (Page<Album>) albumMapper.selectAll();
        setImageNums(albums.getResult());
        return new PageResult<Album>(albums.getTotal(), albums.getResult());
    }

    /**
     * 条件查询
     *
     * @param searchMap 查询条件
     * @return
     */
    public List<Album> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        List<Album> albums = albumMapper.selectByExample(example);
        setImageNums(albums);
        return albums;
    }

    /**
     * 分页+条件查询
     *
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Album> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page, size);
        Example example = createExample(searchMap);
        Page<Album> albums = (Page<Album>) albumMapper.selectByExample(example);
        setImageNums(albums.getResult());
        return new PageResult<Album>(albums.getTotal(), albums.getResult());
    }

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    public Album findById(Long id) {
        Album album = albumMapper.selectByPrimaryKey(id);
        setImageNum(album);
        return album;
    }

    /**
     * 新增
     *
     * @param album
     */
    public void add(Album album) {
        albumMapper.insert(album);
    }

    /**
     * 修改
     *
     * @param album
     */
    public void update(Album album) {
        albumMapper.updateByPrimaryKeySelective(album);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        albumMapper.deleteByPrimaryKey(id);
    }

    public void setImageNum(Album album) {
        String imageUrls = album.getImageItems();
        if (imageUrls == null||"".equals(imageUrls)) {
            album.setImageNum(0);
            return;
        }
        String[] imageUrlsArray = imageUrls.split(",");
        album.setImageNum(imageUrlsArray.length);
    }

    public void setImageNums(List<Album> albums) {
        for (Album album : albums) {
            setImageNum(album);
        }
    }

    public String[] getImageItemsString(Long id) {
       Album album =  findById(id);
       String imageUrls = album.getImageItems();
       String[] imageUrlsArray = imageUrls.split(",");
       return imageUrlsArray;
    }

    public PageResult<String> getImageItemsStringPage(Long id,int page,int size) {

        Album album =  findById(id);
        String imageUrls = album.getImageItems();
        String[] imageUrlsArray = imageUrls.split(",");

        List<String> imgList = new ArrayList<String>();
        for(String imgUrl:imageUrlsArray){
            imgList.add(imgUrl);
        }
        //分页数据总数
        long total = imgList.size();
        //最大分页数量
        long totalPage = 0==total%size?(total==0?1:(total<=size?1:total/size)):total/size+1;
        //当前页第一个数据
        int start = (page-1)*size;
        List<String> imgPageList = imgList.subList(start,page==totalPage?(int)total:start+size);

        return new PageResult<String>(total,imgPageList);
    }

    public void addItem(String imgUrl,Long id) {
        Album album = findById(id);
        String imgUrls = album.getImageItems();
        if(imgUrls==null){
            imgUrls = "";
        }
        if("".equals(imgUrls)){
            album.setImageItems(imgUrl);
        }else {
            album.setImageItems(imgUrls+","+imgUrl);
        }

        update(album);
    }

    public void deleteItem(String imgUrl, Long id) {
        Album album = findById(id);
        String imgUrls = album.getImageItems();
        /*String temp1 = imgUrls.replace(imgUrl+",","");
        if(temp1.equals(imgUrls)){
            String temp2 = imgUrls.replace(","+imgUrl,"");
            if(temp2.equals(imgUrls)&&imgUrls.equals(imgUrls.replace(",",""))){
                imgUrls = "";
            }else{
                imgUrls = temp2;
            }
        }else{
            imgUrls = temp1;
        }*/
        String[] imgStrings = imgUrls.split(",");
        String imgNewUrls = "";
        for(String imageUrl:imgStrings){
            if(imageUrl!=imgUrl){
                if("".equals(imgNewUrls)){
                    imgNewUrls = imageUrl;
                }else{
                    imgNewUrls = imgNewUrls + "," + imageUrl;
                 }
            }
        }
        album.setImageItems(imgNewUrls);
        update(album);
    }

    @Transactional
    public void moveItem(String imgUrl,Long removeId,Long moveId){
        deleteItem(imgUrl,removeId);
        addItem(imgUrl,moveId);
    }
    /**
     * 构建查询条件
     *
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap) {
        Example example = new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();
        if (searchMap != null) {
            // 相册名称
            if (searchMap.get("title") != null && !"".equals(searchMap.get("title"))) {
                criteria.andLike("title", "%" + searchMap.get("title") + "%");
            }
            // 相册封面
            if (searchMap.get("image") != null && !"".equals(searchMap.get("image"))) {
                criteria.andLike("image", "%" + searchMap.get("image") + "%");
            }
            // 图片列表
            if (searchMap.get("imageItems") != null && !"".equals(searchMap.get("imageItems"))) {
                criteria.andLike("imageItems", "%" + searchMap.get("imageItems") + "%");
            }


        }
        return example;
    }
  /*  @Test
    public void fun(){
        String s ="ac,b,c,d";
        String[] ss = s.split(",");
        System.out.println(Arrays.toString(ss));
    }*/
}
