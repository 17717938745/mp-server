package com.lead.fund.base.server.mp.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.server.mp.dao.MpAccountDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpAccountEntity;
import com.lead.fund.base.server.mp.mapper.dmmp.MpAccountMapper;
import org.springframework.stereotype.Component;

/**
 * MpAccountDao
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class MpAccountDaoImpl extends ServiceImpl< MpAccountMapper,MpAccountEntity> implements MpAccountDao {

}
