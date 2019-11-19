package com.taotao.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taotao.dao.SpecMapper;
import com.taotao.dao.TemplateMapper;
import com.taotao.entity.PageResult;
import com.taotao.pojo.goods.Spec;
import com.taotao.pojo.goods.Template;
import com.taotao.service.goods.SpecService;
import com.taotao.service.goods.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service(interfaceClass = SpecService.class)//在dubbo使用事务需要加interfaceClass属性
public class SpecServiceImpl implements SpecService {

    @Autowired
    private SpecMapper specMapper;

    @Autowired
    private TemplateMapper templateMapper;
    /**
     * 返回全部记录
     * @return
     */
    public List<Spec> findAll() {
        return specMapper.selectAll();
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页记录数
     * @return 分页结果
     */
    public PageResult<Spec> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        Page<Spec> specs = (Page<Spec>) specMapper.selectAll();
        return new PageResult<Spec>(specs.getTotal(),specs.getResult());
    }

    /**
     * 条件查询
     * @param searchMap 查询条件
     * @return
     */
    public List<Spec> findList(Map<String, Object> searchMap) {
        Example example = createExample(searchMap);
        return specMapper.selectByExample(example);
    }

    /**
     * 分页+条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public PageResult<Spec> findPage(Map<String, Object> searchMap, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        Page<Spec> specs = (Page<Spec>) specMapper.selectByExample(example);
        return new PageResult<Spec>(specs.getTotal(),specs.getResult());
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Spec findById(Integer id) {
        return specMapper.selectByPrimaryKey(id);
    }

    /**
     * 新增
     * @param spec
     */
    @Transactional
    public void add(Spec spec) {
        specMapper.insert(spec);

        Template template = templateMapper.selectByPrimaryKey(spec.getTemplateId());
        template.setSpecNum(template.getSpecNum()+1);
        templateMapper.updateByPrimaryKey(template);
    }

    /**
     * 修改
     * @param spec
     */
    public void update(Spec spec) {
        specMapper.updateByPrimaryKeySelective(spec);
    }

    /**
     *  删除
     * @param id
     */
    @Transactional
    public void delete(Integer id) {
        Spec spec = specMapper.selectByPrimaryKey(id);
        Template template = templateMapper.selectByPrimaryKey(spec.getTemplateId());
        template.setSpecNum(template.getSpecNum()-1);
        templateMapper.updateByPrimaryKey(template);

        specMapper.deleteByPrimaryKey(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 名称
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
            }
            // 规格选项
            if(searchMap.get("options")!=null && !"".equals(searchMap.get("options"))){
                criteria.andLike("options","%"+searchMap.get("options")+"%");
            }

            // ID
            if(searchMap.get("id")!=null ){
                criteria.andEqualTo("id",searchMap.get("id"));
            }
            // 排序
            if(searchMap.get("seq")!=null ){
                criteria.andEqualTo("seq",searchMap.get("seq"));
            }
            // 模板ID
            if(searchMap.get("templateId")!=null ){
                criteria.andEqualTo("templateId",searchMap.get("templateId"));
            }

        }
        return example;
    }

    public List<Map> findSpecByTemplateId(Integer templateId) {
        Example example = new Example(Spec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("templateId",templateId);
        List<Spec> specs = specMapper.selectByExample(example);
        List<Map> mapList = new ArrayList<Map>();
        for(Spec spec:specs){
            Map map = new HashMap();
            map.put("id",spec.getId());
            map.put("name",spec.getName());
            map.put("templateId",spec.getTemplateId());
            String optionsString = spec.getOptions();
            map.put("options", Arrays.asList(optionsString.split(",")));
            mapList.add(map);
        }
        return mapList;
    }
}
