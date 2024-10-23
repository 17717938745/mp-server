package com.lead.fund.base.server.mp.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lead.fund.base.server.mp.entity.douson.TemplateEntity;

/**
 * TemplateDao
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
public interface TemplateDao extends IService<TemplateEntity> {

    String nextOrderNo();
}
