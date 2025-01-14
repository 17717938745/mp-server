package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.TroubleDao;
import com.lead.fund.base.server.mp.entity.douson.TroubleEntity;
import com.lead.fund.base.server.mp.mapper.douson.TroubleMapper;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/**
 * TroubleDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class TroubleDaoImpl extends ServiceImpl<TroubleMapper, TroubleEntity> implements TroubleDao {


    private static final AtomicInteger SERIAL_NO = new AtomicInteger(1);

    @PostConstruct
    public void init() {
        SERIAL_NO.set(getBaseMapper().selectCount(new LambdaQueryWrapper<TroubleEntity>()).intValue() + 1);
    }

    @Override
    public String nextSerialNo() {
        return "LJ" + StrUtil.padPre(String.valueOf(SERIAL_NO.getAndAdd(1)), 5, "0");
    }
}
