package com.lead.fund.base.server.mp.dao.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.basic.request.Page;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.NumberUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.BoxFlagDao;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagEntity;
import com.lead.fund.base.server.mp.mapper.douson.BoxFlagMapper;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/**
 * BoxFlagDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class BoxFlagDaoImpl extends ServiceImpl<BoxFlagMapper, BoxFlagEntity> implements BoxFlagDao {

    private static final AtomicInteger ORDER_NO = new AtomicInteger(1);

    @PostConstruct
    public void init() {
        ORDER_NO.set(
                Optional.ofNullable(
                        CollUtil.getFirst(DatabaseUtil.page(new Page(1, 1), () -> getBaseMapper().selectList(new LambdaQueryWrapper<BoxFlagEntity>().orderByDesc(BoxFlagEntity::getOrderNo))).getList())
                ).map(t -> NumberUtil.defaultInteger(t.getOrderNo())).orElse(1)
        );
    }

    @Override
    public String viewNextOrderNo() {
        return StrUtil.padPre(String.valueOf(ORDER_NO.get() + 1), 6, "0");
    }

    @Override
    public String nextOrderNo() {
        return StrUtil.padPre(String.valueOf(ORDER_NO.addAndGet(1)), 6, "0");
    }

    @Override
    public String currentOrderNo() {
        return StrUtil.padPre(String.valueOf(ORDER_NO.get()), 6, "0");
    }

    @Override
    public Integer nextBoxNumber(String saleOrderNo, String orderProject) {
        return Optional.ofNullable(
                CollUtil.getFirst(getBaseMapper().selectList(new LambdaQueryWrapper<BoxFlagEntity>()
                        .eq(BoxFlagEntity::getSaleOrderNo, saleOrderNo)
                        .eq(BoxFlagEntity::getOrderProject, orderProject)
                        .select(BoxFlagEntity::getBoxNumber)
                        .orderByDesc(BoxFlagEntity::getBoxNumber)
                ))
        ).map(t -> t.getBoxNumber() + 1).orElse(1);
    }
}
