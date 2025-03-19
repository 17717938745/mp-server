package com.lead.fund.base.server.mp.dao.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.InventoryDao;
import com.lead.fund.base.server.mp.entity.douson.InventoryEntity;
import com.lead.fund.base.server.mp.mapper.douson.InventoryMapper;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * InventoryDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
@Slf4j
public class InventoryDaoImpl extends ServiceImpl<InventoryMapper, InventoryEntity> implements InventoryDao {

    private static final Map<String, AtomicInteger> ORDER_NO_MAP = new HashMap<>(8);

    @PostConstruct
    public void init() {
        final DateTime now = DateTime.now();
        ORDER_NO_MAP.computeIfAbsent(DateUtil.daySimple(now), k -> new AtomicInteger(orderNo(now).intValue()));
        log.info("ORDER_NO_MAP: {}", JSONUtil.toJsonStr(ORDER_NO_MAP));
    }

    private AtomicInteger getOrderNo(Date date) {
        return ORDER_NO_MAP.computeIfAbsent(DateUtil.daySimple(new Date()), k -> new AtomicInteger(orderNo(date).intValue()));
    }

    private Long orderNo(Date date) {
        return getBaseMapper().selectCount(new LambdaQueryWrapper<InventoryEntity>()
                .ge(InventoryEntity::getCreateTime, cn.hutool.core.date.DateUtil.beginOfDay(date))
                .le(InventoryEntity::getCreateTime, cn.hutool.core.date.DateUtil.endOfDay(date))
        );
    }

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<InventoryEntity> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    public String nextSerialNo() {
        final DateTime now = DateTime.now();
        return "JH" + DateUtil.daySimple(now) + StrUtil.padPre(String.valueOf(getOrderNo(now).addAndGet(1)), 3, "0");
    }
}
