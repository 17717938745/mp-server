package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.UserPhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserPhotoEntity;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserPhotoMapper;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserPhotoDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class UserPhotoDaoImpl extends ServiceImpl<MpUserPhotoMapper, MpUserPhotoEntity> implements UserPhotoDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<MpUserPhotoEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
