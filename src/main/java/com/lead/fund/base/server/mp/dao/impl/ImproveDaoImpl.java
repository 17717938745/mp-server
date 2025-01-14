package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.ImproveDao;
import com.lead.fund.base.server.mp.entity.douson.ImproveEntity;
import com.lead.fund.base.server.mp.mapper.douson.ImproveMapper;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/**
 * ImproveDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class ImproveDaoImpl extends ServiceImpl<ImproveMapper, ImproveEntity> implements ImproveDao {


    private static final AtomicInteger SERIAL_NO = new AtomicInteger(1);

    @PostConstruct
    public void init() {
        SERIAL_NO.set(getBaseMapper().selectCount(new LambdaQueryWrapper<ImproveEntity>()).intValue() + 1);
    }

    @Override
    public String nextSerialNo() {
        return "GY" + StrUtil.padPre(String.valueOf(SERIAL_NO.getAndAdd(1)), 5, "0");
    }
}
