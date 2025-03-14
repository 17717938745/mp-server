package com.lead.fund.base.server.mp.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lead.fund.base.server.mp.entity.douson.ParamEntity;
import com.lead.fund.base.server.mp.entity.douson.PlanEntity;
import com.lead.fund.base.server.mp.response.ParamConfigResponse;

import java.util.List;

/**
 * PlanDao
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
public interface PlanDao extends IService<PlanEntity> {

    String nextSerialNo();
}
