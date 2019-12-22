package com.taotao.service.user;

import com.taotao.pojo.user.Regions;


import java.util.List;

public interface RegionsService {
    /**
     * 通过上级Id查找地址列表
     * @param patentId
     * @return
     */
    List<Regions> findByParentId(Integer patentId);
}
