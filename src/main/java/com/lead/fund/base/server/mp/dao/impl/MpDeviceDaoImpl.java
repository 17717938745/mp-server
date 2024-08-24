package com.lead.fund.base.server.mp.dao.impl;

import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.basic.cons.frame.AdminState;
import com.lead.fund.base.common.basic.cons.frame.AdminUser;
import com.lead.fund.base.common.util.IdUtil;
import com.lead.fund.base.common.util.SecurityUtil;
import com.lead.fund.base.server.mp.dao.MpDeviceDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpDeviceEntity;
import com.lead.fund.base.server.mp.mapper.dmmp.MpDeviceMapper;
import org.springframework.stereotype.Component;

/**
 * MpDeviceDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class MpDeviceDaoImpl extends ServiceImpl<MpDeviceMapper, MpDeviceEntity> implements MpDeviceDao {

    @Override
    public MpDeviceEntity defaultDevice(String deviceId, String salt, String ip) {
        MpDeviceEntity result = isNotBlank(deviceId) ? getBaseMapper().selectById(deviceId) : null;
        if (null == result) {
            final DateTime now = DateTime.now();
            final java.util.Date date = now.toJdkDate();
            result = new MpDeviceEntity();
            result.setSalt(defaultIfBlank(salt, SecurityUtil::randomSalt));
            result.setUserId("");
            result.setAccountId("");
            result.setIp(ip);
            result.setCreator(AdminUser.defaultUser());
            result.setModifier(AdminUser.defaultUser());
            result.setState(AdminState.defaultState());
            result.setCreateTime(date);
            result.setModifyTime(date);
            result.setId(defaultIfBlank(deviceId, IdUtil::nextIdStr));
            getBaseMapper().insert(result);
        }
        return result;
    }

}
