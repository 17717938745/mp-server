package com.lead.fund.base.server.mp.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagEntity;
import com.lead.fund.base.server.mp.entity.douson.OrderEntity;

/**
 * OrderDao
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
public interface OrderDao extends IService<OrderEntity> {

    OrderEntity mergeRelevance(OrderEntity e);
}
