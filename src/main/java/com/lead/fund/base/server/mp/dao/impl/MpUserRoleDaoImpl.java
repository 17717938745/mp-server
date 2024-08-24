package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.MpUserRoleDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserRoleEntity;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserRoleMapper;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * MpUserRoleDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class MpUserRoleDaoImpl extends ServiceImpl<MpUserRoleMapper, MpUserRoleEntity> implements MpUserRoleDao {

    @Override
    @Transactional(value = "dmmpDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public boolean saveBatch(Collection<MpUserRoleEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
