package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.QuotationAttachmentDao;
import com.lead.fund.base.server.mp.entity.douson.QuotationAttachmentEntity;
import com.lead.fund.base.server.mp.mapper.douson.QuotationAttachmentMapper;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * QuotationAttachmentDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class QuotationAttachmentDaoImpl extends ServiceImpl<QuotationAttachmentMapper, QuotationAttachmentEntity> implements QuotationAttachmentDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<QuotationAttachmentEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
