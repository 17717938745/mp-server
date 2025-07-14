package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.QuotationItemDao;
import com.lead.fund.base.server.mp.entity.douson.QuotationItemEntity;
import com.lead.fund.base.server.mp.mapper.douson.QuotationItemMapper;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * QuotationItemDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
@Slf4j
public class QuotationItemDaoImpl extends ServiceImpl<QuotationItemMapper, QuotationItemEntity> implements QuotationItemDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<QuotationItemEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
