package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.AssemblyDao;
import com.lead.fund.base.server.mp.entity.douson.AssemblyEntity;
import com.lead.fund.base.server.mp.mapper.douson.AssemblyMapper;
import java.util.Collection;
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

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<AssemblyEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
