package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.QualityAttachmentDao;
import com.lead.fund.base.server.mp.entity.douson.QualityAttachmentEntity;
import com.lead.fund.base.server.mp.mapper.douson.QualityAttachmentMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * QualityAttachmentDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class QualityAttachmentDaoImpl extends ServiceImpl<QualityAttachmentMapper, QualityAttachmentEntity> implements QualityAttachmentDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<QualityAttachmentEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
