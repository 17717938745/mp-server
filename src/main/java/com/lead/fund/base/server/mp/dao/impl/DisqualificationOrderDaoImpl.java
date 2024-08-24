package com.lead.fund.base.server.mp.dao.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.basic.request.Page;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.NumberUtil;
import com.lead.fund.base.server.mp.dao.DisqualificationOrderDao;
import com.lead.fund.base.server.mp.entity.douson.DisqualificationOrderEntity;
import com.lead.fund.base.server.mp.mapper.douson.DisqualificationOrderMapper;
import java.util.Date;
import java.util.Optional;
import org.springframework.stereotype.Component;

/**
 * DisqualificationOrderDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class DisqualificationOrderDaoImpl extends ServiceImpl<DisqualificationOrderMapper, DisqualificationOrderEntity> implements DisqualificationOrderDao {

    @Override
    public Integer nextOrderNo() {
        return Optional.ofNullable(
                        CollUtil.getFirst(
                                DatabaseUtil.page(
                                        new Page(1, 1),
                                        () -> getBaseMapper().selectList(
                                                new LambdaQueryWrapper<DisqualificationOrderEntity>()
                                                        .ge(DisqualificationOrderEntity::getCreateTime, cn.hutool.core.date.DateUtil.beginOfDay(new Date()))
                                                        .le(DisqualificationOrderEntity::getCreateTime, cn.hutool.core.date.DateUtil.endOfDay(new Date()))
                                                        .orderByDesc(DisqualificationOrderEntity::getDisqualificationOrderNo)
                                        )
                                ).getList()
                        )
                ).map(t -> NumberUtil.defaultInteger(t.getDisqualificationOrderNo()) + 1)
                .orElse(1);
    }
}
