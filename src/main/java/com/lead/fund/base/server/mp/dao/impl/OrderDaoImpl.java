package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.OrderDao;
import com.lead.fund.base.server.mp.entity.douson.OrderEntity;
import com.lead.fund.base.server.mp.mapper.douson.OrderMapper;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * OrderDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class OrderDaoImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<OrderEntity> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    public OrderEntity mergeRelevance(OrderEntity e) {
        final OrderMapper mapper = getBaseMapper();
        OrderEntity order = mapper.selectOne(new LambdaQueryWrapper<OrderEntity>()
                .eq(OrderEntity::getOrderNo, e.getOrderNo())
                .eq(OrderEntity::getProcessProcedure, e.getProcessProcedure())
                .eq(OrderEntity::getTestDevice, e.getTestDevice())
                .eq(OrderEntity::getProjectSequence, e.getProjectSequence())
        );
        // 插入
        if (null == order) {
            mapper.insert(order = e);
        }
        return order;
    }
}
