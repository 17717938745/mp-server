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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
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

    private static final AtomicInteger MATERIAL_ORDER_NO = new AtomicInteger(0);
    private static final Map<String, AtomicInteger> CHECK_ORDER_NO_MAP = new HashMap<>(8);
    private static final Map<List<String>, String> MATERIAL_ORDER_NO_STRING_MAP = new ConcurrentHashMap<>();
    private static final Map<List<String>, String> CHECK_ORDER_NO_STRING_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    @Override
    public void init() {
        CHECK_ORDER_NO_MAP.put(DateUtil.daySimple(new Date()), checkOrderNo());
        MATERIAL_ORDER_NO.set(
                Optional.ofNullable(
                                CollUtil.getFirst(DatabaseUtil.page(new Page(1, 1), () -> getBaseMapper().selectList(new LambdaQueryWrapper<MaterialEntity>()
                                        .orderByDesc(MaterialEntity::getMaterialOrderNo))).getList())
                        )
                        .map(t -> NumberUtil.defaultInteger(t.getMaterialOrderNo())).orElse(0)
        );
        log.info("check order no: {}, material order no: {}", CHECK_ORDER_NO_MAP, MATERIAL_ORDER_NO);
    }

    private AtomicInteger getCheckOrderNo() {
        return CHECK_ORDER_NO_MAP.computeIfAbsent(DateUtil.daySimple(new Date()), k -> checkOrderNo());
    }

    private AtomicInteger checkOrderNo() {
        return new AtomicInteger(Optional.ofNullable(
                        CollUtil.getFirst(DatabaseUtil.page(new Page(1, 1), () -> getBaseMapper().selectList(new LambdaQueryWrapper<MaterialEntity>()
                                .likeRight(MaterialEntity::getCheckOrderNo, DateUtil.daySimple(new Date()))
                                .orderByDesc(MaterialEntity::getCheckOrderNo))).getList())
                )
                .map(t -> NumberUtil.defaultInteger(t.getCheckOrderNo().substring(8))).orElse(0)
        );
    }


    @Override
    public String nextCheckOrderNo() {
        final String today = DateUtil.daySimple(new Date());
        return today + StrUtil.padPre(String.valueOf(getCheckOrderNo().addAndGet(1)), 3, "0");
    }

    @Override
    public String nextMaterialOrderNo() {
        return StrUtil.padPre(String.valueOf(MATERIAL_ORDER_NO.addAndGet(1)), 7, "0");
    }

    @Override
    public String checkOrderNo(MaterialEntity e) {
        return getData(e, CHECK_ORDER_NO_STRING_MAP, MaterialEntity::getCheckOrderNo, this::nextCheckOrderNo);
    }

    @Override
    public String materialOrderNo(MaterialEntity e) {
        return getData(e, MATERIAL_ORDER_NO_STRING_MAP, MaterialEntity::getMaterialOrderNo, this::nextMaterialOrderNo);
    }

    private String getData(MaterialEntity e, Map<List<String>, String> cacheMap, Function<MaterialEntity, String> mappingFunction, Supplier<String> defaultSupplier) {
        return cacheMap.computeIfAbsent(CollUtil.toList(e.getSaleOrderNo(), e.getOrderProjectNo(), e.getProductionDate()), k -> getBaseMapper().selectList(new LambdaQueryWrapper<MaterialEntity>()
                .eq(MaterialEntity::getSaleOrderNo, k.get(0))
                .eq(MaterialEntity::getOrderProjectNo, k.get(1))
                .eq(MaterialEntity::getProductionDate, k.get(2))
        ).stream().findFirst().map(mappingFunction).orElse(defaultSupplier.get()));
    }

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<MaterialEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
