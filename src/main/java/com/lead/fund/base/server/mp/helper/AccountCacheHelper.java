package com.lead.fund.base.server.mp.helper;

import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_ACCOUNT_FROZEN;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_ACCOUNT_NOT_SIGN_IN;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_USER_FROZEN;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_USER_NOT_SIGN_IN;
import static com.lead.fund.base.server.mp.converter.MpSystemConverter.MP_SYSTEM_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lead.fund.base.common.basic.cons.frame.AdminState;
import com.lead.fund.base.common.basic.cons.frame.ExceptionType;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.MpDeviceDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpAccountEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpDeviceEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpRoleEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserRoleEntity;
import com.lead.fund.base.server.mp.mapper.dmmp.MpAccountMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpRoleMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserRoleMapper;
import com.lead.fund.base.server.mp.response.MpRoleResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * AccountCacheHelper
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-17 19:14
 */
@Component
@CacheConfig(cacheNames = "AccountCacheHelper")
public class AccountCacheHelper {

    @Resource
    private MpAccountMapper accountMapper;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private MpUserRoleMapper userRoleMapper;
    @Resource
    private MpRoleMapper roleMapper;
    @Resource
    private MpDeviceDao deviceDao;

    @Cacheable(cacheNames = "getAccount")
    public MpAccountEntity getAccount(String deviceId) {
        if (isNotBlank(deviceId)) {
            MpDeviceEntity device = deviceDao.defaultDevice(deviceId, null, "null");
            if (isNotBlank(device.getAccountId())) {
                MpAccountEntity account = accountMapper.selectById(device.getAccountId());
                if (null != account) {
                    if (AdminState.DELETE.getCode().equals(account.getState())) {
                        throw new BusinessException(MP_ACCOUNT_FROZEN);
                    }
                    return account;
                }
            }
        }
        throw new BusinessException(MP_ACCOUNT_NOT_SIGN_IN);
    }

    @CacheEvict(cacheNames = "getAccount")
    public void clearAccount(String deviceId) {
    }

    @CacheEvict(cacheNames = "getAccount", allEntries = true)
    public void clearAllAccount() {
    }

    @Cacheable(cacheNames = "getUser")
    public MpUserResponse getUser(String deviceId) {
        if (isNotBlank(deviceId)) {
            MpDeviceEntity device = deviceDao.defaultDevice(deviceId, null, "null");
            if (isNotBlank(device.getUserId())) {
                MpUserEntity user = userMapper.selectById(device.getUserId());
                if (null != user) {
                    if (AdminState.DELETE.getCode().equals(user.getState())) {
                        throw new BusinessException(MP_USER_FROZEN);
                    }
                    final MpUserResponse r = MP_SYSTEM_INSTANCE.data(user);
                    r.setAliveMillis(0L);
                    final List<MpUserRoleEntity> userRoleList = userRoleMapper.selectList(new LambdaQueryWrapper<MpUserRoleEntity>().eq(MpUserRoleEntity::getUserId, r.getUserId()));
                    final List<String> roleIdList = userRoleList.stream().map(MpUserRoleEntity::getRoleId).filter(StrUtil::isNotBlank).distinct().collect(Collectors.toList());
                    r.setRoleList(CollUtil.isNotEmpty(roleIdList) ? roleMapper.selectList(
                                    DatabaseUtil.or(new LambdaQueryWrapper<MpRoleEntity>().eq(MpRoleEntity::getState, AdminState.NORMAL.getCode()),
                                            roleIdList,
                                            (t, l) -> {
                                                t.in(MpRoleEntity::getId, l);
                                            })
                            ).stream().map(MP_SYSTEM_INSTANCE::data).toList() :
                                    new ArrayList<>()
                    );
                    r.setRoleIdList(r.getRoleList().stream().map(MpRoleResponse::getRoleId).collect(Collectors.toList()));
                    r.setRoleCodeList(r.getRoleList().stream().map(MpRoleResponse::getRoleCode).collect(Collectors.toList()));
                    return r;
                }
            }
        }
        throw new BusinessException(MP_USER_NOT_SIGN_IN);
    }

    @CacheEvict(cacheNames = "getUser")
    public void clearUser(String deviceId) {
    }

    @CacheEvict(cacheNames = "getUser", allEntries = true)
    public void clearAllUser() {
    }

}
