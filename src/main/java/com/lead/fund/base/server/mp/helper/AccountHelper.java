package com.lead.fund.base.server.mp.helper;

import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lead.fund.base.common.basic.cons.frame.AdminUser;
import com.lead.fund.base.common.basic.cons.frame.ExceptionType;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.server.mp.dao.MpDeviceDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpAccountEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpDeviceEntity;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * AccountHelper
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 10:07
 */
@Component
@Slf4j
public class AccountHelper {


    private static final long TIME_OUT_MILLIS = 30 * 60 * 1000;
    @Resource
    private AccountCacheHelper accountCacheHelper;
    @Resource
    private MpDeviceDao deviceDao;

    public MpAccountEntity getAccount(String deviceId) {
        final MpAccountEntity account = accountCacheHelper.getAccount(deviceId);
        if (null != account && isNotBlank(account.getOpenId())) {
            return account;
        }
        throw new BusinessException(ExceptionType.MP_ACCOUNT_NOT_SIGN_IN);
    }

    public MpUserResponse getUser(String deviceId) {
        return getUser(deviceId, false);
    }

    public MpUserResponse getUser(String deviceId, boolean signIn) {
        final MpUserResponse user = accountCacheHelper.getUser(deviceId);
        if (null != user && isNotBlank(user.getUserId())) {
            if (signIn) {
                user.setAliveMillis(System.currentTimeMillis());
            }
            long lastOperatorGap = System.currentTimeMillis() - user.getAliveMillis();
            log.info("lastOperatorGap: {}ms, user: {}", lastOperatorGap, user);
            if (lastOperatorGap < TIME_OUT_MILLIS) {
                user.setAliveMillis(System.currentTimeMillis());
                return user;
            }
        }
        throw new BusinessException(ExceptionType.AUTHORITY_NOT_LOGIN);
    }

    public MpUserResponse checkUserAdmin(String deviceId) {
        MpUserResponse user = getUser(deviceId);
        if (user.getRoleList().stream().noneMatch(t -> AdminUser.ADMIN.getCode().equals(t.getRoleCode()))) {
            throw new BusinessException(ExceptionType.AUTHORITY_OTHER_DATA_NOT_ALLOW);
        }
        return user;
    }

    public MpUserResponse checkUserAdminOrSelf(String deviceId, String userId) {
        MpUserResponse user = getUser(deviceId);
        if (user.getRoleList().stream().noneMatch(t -> AdminUser.ADMIN.getCode().equals(t.getRoleCode())) &&
                (isBlank(userId) || !userId.equals(user.getUserId()))) {
            throw new BusinessException(ExceptionType.AUTHORITY_OTHER_DATA_NOT_ALLOW);
        }
        return user;
    }

    public void clear(String deviceId) {
        accountCacheHelper.clearAccount(deviceId);
        accountCacheHelper.clearUser(deviceId);
    }

    public void clearAll() {
        accountCacheHelper.clearAllAccount();
        accountCacheHelper.clearAllUser();
    }

    @SuppressWarnings("UnusedReturnValue")
    public MpAccountEntity refresh(String deviceId) {
        accountCacheHelper.clearAccount(deviceId);
        return getAccount(deviceId);
    }

    public void clearByOpenId(String openId) {
        for (MpDeviceEntity t : deviceDao.getBaseMapper().selectList(new LambdaQueryWrapper<MpDeviceEntity>().eq(MpDeviceEntity::getAccountId, openId))) {
            accountCacheHelper.clearAccount(t.getId());
        }
    }
}
