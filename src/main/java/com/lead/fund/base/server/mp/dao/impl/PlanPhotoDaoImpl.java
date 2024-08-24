package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.PlanDao;
import com.lead.fund.base.server.mp.dao.PlanPhotoDao;
import com.lead.fund.base.server.mp.dao.ProductPhotoDao;
import com.lead.fund.base.server.mp.entity.douson.PlanEntity;
import com.lead.fund.base.server.mp.entity.douson.PlanPhotoEntity;
import com.lead.fund.base.server.mp.entity.douson.ProductPhotoEntity;
import com.lead.fund.base.server.mp.mapper.douson.PlanMapper;
import com.lead.fund.base.server.mp.mapper.douson.PlanPhotoMapper;
import com.lead.fund.base.server.mp.mapper.douson.ProductPhotoMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * PlanPhotoDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class PlanPhotoDaoImpl extends ServiceImpl<PlanPhotoMapper, PlanPhotoEntity> implements PlanPhotoDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<PlanPhotoEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
