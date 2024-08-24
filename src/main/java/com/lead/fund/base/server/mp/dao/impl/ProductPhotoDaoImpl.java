package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.ProductPhotoDao;
import com.lead.fund.base.server.mp.dao.ReportPhotoDao;
import com.lead.fund.base.server.mp.entity.douson.ProductPhotoEntity;
import com.lead.fund.base.server.mp.entity.douson.ReportPhotoEntity;
import com.lead.fund.base.server.mp.mapper.douson.ProductPhotoMapper;
import com.lead.fund.base.server.mp.mapper.douson.ReportPhotoMapper;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * ProductPhotoDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class ProductPhotoDaoImpl extends ServiceImpl<ProductPhotoMapper, ProductPhotoEntity> implements ProductPhotoDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<ProductPhotoEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
