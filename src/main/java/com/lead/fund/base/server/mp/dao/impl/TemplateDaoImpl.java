package com.lead.fund.base.server.mp.dao.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.basic.request.Page;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.NumberUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.TemplateDao;
import com.lead.fund.base.server.mp.entity.douson.TemplateEntity;
import com.lead.fund.base.server.mp.mapper.douson.TemplateMapper;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * TemplateDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class TemplateDaoImpl extends ServiceImpl<TemplateMapper, TemplateEntity> implements TemplateDao {

    private static final AtomicInteger ORDER_NO = new AtomicInteger(1);

    @PostConstruct
    public void init() {
        ORDER_NO.set(
                Optional.ofNullable(
                        CollUtil.getFirst(DatabaseUtil.page(new Page(1, 1), () -> getBaseMapper().selectList(new LambdaQueryWrapper<TemplateEntity>().orderByDesc(TemplateEntity::getTemplateOrderNo))).getList())
                ).map(t -> NumberUtil.defaultInteger(t.getTemplateOrderNo().split("-")[1])).orElse(0)
        );
    }

    @Override
    public String nextOrderNo() {
        return DateUtil.daySimple(new Date()) + "-" + StrUtil.padPre(String.valueOf(ORDER_NO.addAndGet(1)), 2, "0");
    }

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<TemplateEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
