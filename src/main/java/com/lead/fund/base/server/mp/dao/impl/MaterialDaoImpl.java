package com.lead.fund.base.server.mp.dao.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.basic.request.Page;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.NumberUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.MaterialDao;
import com.lead.fund.base.server.mp.entity.douson.MaterialEntity;
import com.lead.fund.base.server.mp.mapper.douson.MaterialMapper;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * MaterialPhotoDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
@Slf4j
public class MaterialDaoImpl extends ServiceImpl<MaterialMapper, MaterialEntity> implements MaterialDao {

    private static final AtomicInteger INDEX = new AtomicInteger(0);
    private static final Map<String, AtomicInteger> ORDER_NO_MAP = new HashMap<>(8);

    @PostConstruct
    @Override
    public void init() {
        ORDER_NO_MAP.put(DateUtil.daySimple(new Date()), orderNo());
        INDEX.set(
                Optional.ofNullable(
                                CollUtil.getFirst(DatabaseUtil.page(new Page(1, 1), () -> getBaseMapper().selectList(new LambdaQueryWrapper<MaterialEntity>()
                                        .orderByDesc(MaterialEntity::getMaterialOrderNo))).getList())
                        )
                        .map(t -> NumberUtil.defaultInteger(t.getMaterialOrderNo().split("-", -1)[0])).orElse(0)
        );
        log.info("order no: {}, index: {}", ORDER_NO_MAP, INDEX);
    }

    private AtomicInteger getOrderNo() {
        return ORDER_NO_MAP.computeIfAbsent(DateUtil.daySimple(new Date()), k -> orderNo());
    }

    private AtomicInteger orderNo() {
        return new AtomicInteger(Optional.ofNullable(
                        CollUtil.getFirst(DatabaseUtil.page(new Page(1, 1), () -> getBaseMapper().selectList(new LambdaQueryWrapper<MaterialEntity>()
                                .likeRight(MaterialEntity::getCheckOrderNo, DateUtil.daySimple(new Date()))
                                .orderByDesc(MaterialEntity::getCheckOrderNo))).getList())
                )
                .map(t -> NumberUtil.defaultInteger(t.getCheckOrderNo().split("-", -1)[0].substring(8))).orElse(0)
        );
    }


    @Override
    public String nextOrderNo() {
        final String today = DateUtil.daySimple(new Date());
        return today + StrUtil.padPre(String.valueOf(getOrderNo().addAndGet(1)), 3, "0");
    }

    @Override
    public String nextIndex() {
        return StrUtil.padPre(String.valueOf(INDEX.addAndGet(1)), 7, "0");
    }

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<MaterialEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
