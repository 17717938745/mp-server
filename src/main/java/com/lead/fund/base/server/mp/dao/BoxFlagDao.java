package com.lead.fund.base.server.mp.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagEntity;

/**
 * BoxFlagDao
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
public interface BoxFlagDao extends IService<BoxFlagEntity> {

    String viewNextOrderNo();

    String nextOrderNo();

    String currentOrderNo();

    Integer nextBoxNumber(String saleOrderNo, String orderProject);
}
