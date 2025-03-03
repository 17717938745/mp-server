package com.lead.fund.base.server.mp.dao.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.basic.request.Page;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.NumberUtil;
import com.lead.fund.base.server.mp.dao.AssemblyDao;
import com.lead.fund.base.server.mp.entity.douson.AssemblyEntity;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagEntity;
import com.lead.fund.base.server.mp.mapper.douson.AssemblyMapper;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * AssemblyDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
@Slf4j
public class AssemblyDaoImpl extends ServiceImpl<AssemblyMapper, AssemblyEntity> implements AssemblyDao {

    private static final AtomicInteger ASSEMBLY_INDEX = new AtomicInteger(0);

    @PostConstruct
    public void init() {
        ASSEMBLY_INDEX.set(
                Optional.ofNullable(
                        CollUtil.getFirst(DatabaseUtil.page(new Page(1, 1), () -> getBaseMapper().selectList(new LambdaQueryWrapper<AssemblyEntity>().orderByDesc(AssemblyEntity::getAssemblyIndex))).getList())
                ).map(t -> NumberUtil.defaultInteger(t.getAssemblyIndex())).orElse(0)
        );
    }

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<AssemblyEntity> entityList) {
        return super.saveBatch(entityList);
    }


    @Override
    public Integer assemblyIndex(AssemblyEntity e) {
        return null != e.getAssemblyIndex() ? e.getAssemblyIndex() : ASSEMBLY_INDEX.addAndGet(1);
    }
}
