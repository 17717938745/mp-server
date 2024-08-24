package com.lead.fund.base.server.mp.dao.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.basic.request.Page;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.NumberUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.BoxFlagDao;
import com.lead.fund.base.server.mp.dao.BoxFlagPhotoDao;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagEntity;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagPhotoEntity;
import com.lead.fund.base.server.mp.mapper.douson.BoxFlagMapper;
import com.lead.fund.base.server.mp.mapper.douson.BoxFlagPhotoMapper;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * BoxFlagPhotoDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class BoxFlagPhotoDaoImpl extends ServiceImpl<BoxFlagPhotoMapper, BoxFlagPhotoEntity> implements BoxFlagPhotoDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<BoxFlagPhotoEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
