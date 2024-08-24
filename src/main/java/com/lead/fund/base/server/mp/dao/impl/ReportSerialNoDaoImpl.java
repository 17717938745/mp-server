package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.ReportSerialNoDao;
import com.lead.fund.base.server.mp.entity.douson.ReportSerialNoEntity;
import com.lead.fund.base.server.mp.mapper.douson.ReportSerialNoMapper;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * ReportSerialNoDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class ReportSerialNoDaoImpl extends ServiceImpl<ReportSerialNoMapper, ReportSerialNoEntity> implements ReportSerialNoDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<ReportSerialNoEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
