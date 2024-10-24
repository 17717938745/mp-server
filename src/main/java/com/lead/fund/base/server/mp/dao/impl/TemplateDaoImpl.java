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
import java.util.HashMap;
import java.util.Map;
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

    private static final Map<String, AtomicInteger> ORDER_NO_MAP = new HashMap<>(8);

    @PostConstruct
    public AtomicInteger init() {
        return ORDER_NO_MAP.computeIfAbsent(DateUtil.daySimple(new Date()), k -> new AtomicInteger(Optional.ofNullable(
                CollUtil.getFirst(DatabaseUtil.page(new Page(1, 1), () -> getBaseMapper().selectList(new LambdaQueryWrapper<TemplateEntity>().orderByDesc(TemplateEntity::getTemplateOrderNo))).getList())
        ).map(t -> NumberUtil.defaultInteger(t.getTemplateOrderNo().split("-")[1])).orElse(0)));
    }

    @Override
    public String nextOrderNo() {
        final String today = DateUtil.daySimple(new Date());
        return today + "-" + StrUtil.padPre(String.valueOf(init().addAndGet(1)), 2, "0");
    }

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<TemplateEntity> entityList) {
        return super.saveBatch(entityList);
    }
}
