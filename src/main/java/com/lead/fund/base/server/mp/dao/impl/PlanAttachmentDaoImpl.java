package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.PlanAttachmentDao;
import com.lead.fund.base.server.mp.entity.douson.PlanAttachmentEntity;
import com.lead.fund.base.server.mp.mapper.douson.PlanAttachmentMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * PlanAttachmentDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class PlanAttachmentDaoImpl extends ServiceImpl<PlanAttachmentMapper, PlanAttachmentEntity> implements PlanAttachmentDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<PlanAttachmentEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
