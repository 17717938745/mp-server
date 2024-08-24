package com.lead.fund.base.server.mp.dao.impl;

import static com.lead.fund.base.server.mp.converter.IndustryConverter.INDUSTRY_INSTANCE;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.entity.douson.ParamEntity;
import com.lead.fund.base.server.mp.mapper.douson.ParamMapper;
import com.lead.fund.base.server.mp.response.ParamConfigResponse;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * ParamDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
@CacheConfig(cacheNames = "ParamDao")
public class ParamDaoImpl extends ServiceImpl<ParamMapper, ParamEntity> implements ParamDao {

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<ParamEntity> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @Cacheable(cacheNames = "listByCategoryId")
    public List<ParamConfigResponse> listByCategoryId(String paramCategoryId) {
        LambdaQueryWrapper<ParamEntity> lambda = new LambdaQueryWrapper<>();
        lambda.eq(ParamEntity::getParamCategoryId, paramCategoryId);
        return getBaseMapper()
                .selectList(lambda.orderByAsc(ParamEntity::getParamCategoryId).orderByAsc(ParamEntity::getSorter).orderByAsc(ParamEntity::getParamCode))
                .stream().map(INDUSTRY_INSTANCE::param)
                .collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = "listByCategoryId", allEntries = true)
    @Override
    public void clear(String paramCategoryId) {
    }

    @CacheEvict(cacheNames = "lislistByCategoryIdt", allEntries = true)
    @Override
    public void clearAll() {
    }
}
