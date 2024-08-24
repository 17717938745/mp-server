package com.lead.fund.base.server.mp.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lead.fund.base.server.mp.entity.dmmp.MpAccountEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpDeviceEntity;

/**
 * MpDeviceDao
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
public interface MpDeviceDao extends IService<MpDeviceEntity> {

    MpDeviceEntity defaultDevice(String deviceId, String salt, String ip);
}
