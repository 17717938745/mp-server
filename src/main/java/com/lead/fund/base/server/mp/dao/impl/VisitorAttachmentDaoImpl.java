package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.VisitorAttachmentDao;
import com.lead.fund.base.server.mp.entity.douson.VisitorAttachmentEntity;
import com.lead.fund.base.server.mp.mapper.douson.VisitorAttachmentMapper;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * VisitorAttachmentDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class VisitorAttachmentDaoImpl extends ServiceImpl<VisitorAttachmentMapper, VisitorAttachmentEntity> implements VisitorAttachmentDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<VisitorAttachmentEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
