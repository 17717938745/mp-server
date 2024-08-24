package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.PlanDao;
import com.lead.fund.base.server.mp.dao.PlanPhotoDao;
import com.lead.fund.base.server.mp.entity.douson.PlanEntity;
import com.lead.fund.base.server.mp.entity.douson.PlanPhotoEntity;
import com.lead.fund.base.server.mp.mapper.douson.PlanMapper;
import com.lead.fund.base.server.mp.mapper.douson.PlanPhotoMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * PlanPhotoDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class PlanDaoImpl extends ServiceImpl<PlanMapper, PlanEntity> implements PlanDao {

}
