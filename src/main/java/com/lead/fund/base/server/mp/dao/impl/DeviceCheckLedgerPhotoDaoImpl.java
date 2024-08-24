package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.DeviceCheckLedgerPhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserPhotoEntity;
import com.lead.fund.base.server.mp.entity.douson.DeviceCheckLedgerPhotoEntity;
import com.lead.fund.base.server.mp.mapper.douson.DeviceCheckLedgerPhotoMapper;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DeviceCheckLedgerPhotoPhotoDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class DeviceCheckLedgerPhotoDaoImpl extends ServiceImpl<DeviceCheckLedgerPhotoMapper, DeviceCheckLedgerPhotoEntity> implements DeviceCheckLedgerPhotoDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<DeviceCheckLedgerPhotoEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
