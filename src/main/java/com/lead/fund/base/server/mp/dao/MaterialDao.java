package com.lead.fund.base.server.mp.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lead.fund.base.server.mp.entity.douson.MaterialEntity;

/**
 * MaterialDao
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
public interface MaterialDao extends IService<MaterialEntity> {

    void init();

    String nextCheckOrderNo();

    String nextMaterialOrderNo();
}
