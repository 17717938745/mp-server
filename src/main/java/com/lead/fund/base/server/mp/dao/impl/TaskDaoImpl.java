package com.lead.fund.base.server.mp.dao.impl;

import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.NumberUtil.defaultInt;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;

import cn.hutool.core.date.DateUnit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.server.mp.dao.TaskDao;
import com.lead.fund.base.server.mp.entity.douson.DeviceEntity;
import com.lead.fund.base.server.mp.entity.douson.TaskEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.TaskMapper;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import jakarta.annotation.Resource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * TaskDaoImpl
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
@Component
public class TaskDaoImpl extends ServiceImpl<TaskMapper, TaskEntity> implements TaskDao {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private DeviceMapper deviceMapper;

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public TaskEntity merge(TaskEntity e) {
        e.setDeviceId(defaultIfBlank(e.getDeviceId()));
        e.setSorter(defaultInt(e.getSorter()));
        e.setModifyTime(new Date());
        if (null == e.getProcessWorkingHour()) {
            e.setProductCountHour8(null)
                    .setProductCountHour12(null);
        } else {
            e.setProductCountHour8(new BigDecimal("435").divide(e.getProcessWorkingHour(), 1, RoundingMode.HALF_UP))
                    .setProductCountHour12(new BigDecimal("585").divide(e.getProcessWorkingHour(), 1, RoundingMode.HALF_UP));
        }
        if (null == e.getOrderCount() || null == e.getProcessCount()) {
            e.setSurplus(null);
        } else {
            e.setSurplus(e.getOrderCount().subtract(e.getProcessCount()));
        }
        if (isBlank(e.getSupplierDoneDate())) {
            e.setSupplierDoneDate(null);
        } else {
            final int diff = -10 - ((isNotBlank(e.getNde()) ? 1 : 0) + (isNotBlank(e.getAssemble()) ? 5 : 0) + (isNotBlank(e.getTestPress()) ? 3 : 0) + (isNotBlank(e.getSurfaceTreatment()) ? 3 : 0));
            e.setSupplierDoneDate(DateUtil.day(cn.hutool.core.date.DateUtil.offsetDay(com.lead.fund.base.common.util.DateUtil.parse(e.getSupplierDoneDate()), diff)));
        }
        // update
        if (isNotBlank(e.getId())) {
            if (taskMapper.updateById(e) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            // insert or copy
        } else {
            taskMapper.insert(e);
        }
        updateSummaryInfo(e);
        return e;

    }

    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @Override
    public TaskEntity merge(String deviceId, TaskEntity e) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        e.setModifier(u.getUserId());
        // update
        if (isNotBlank(e.getId())) {
            if (u.getRoleList().stream().noneMatch(t -> "taskManager".equals(t.getRoleCode()) || "supplierManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            // insert
        } else {
            if (u.getRoleList().stream().noneMatch(t -> "taskManager".equals(t.getRoleCode()) || "supplierManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            e.setCreator(u.getUserId());
        }
        return merge(e);
    }

    @Override
    public void deleteById(String taskId) {
        final TaskEntity e = taskMapper.selectById(taskId);
        taskMapper.deleteById(taskId);
        updateSummaryInfo(e);
    }

    private void updateSummaryInfo(TaskEntity e) {
        final DeviceEntity d = isBlank(e.getDeviceId()) ? (DeviceEntity) new DeviceEntity().setSorter(0).setId("") : deviceMapper.selectById(e.getDeviceId());
        final List<TaskEntity> taskList = taskMapper.selectList(new LambdaQueryWrapper<TaskEntity>()
                .eq(TaskEntity::getDeviceId, e.getDeviceId())
                .orderByAsc(TaskEntity::getSorter).orderByDesc(TaskEntity::getModifyTime)
        );
        for (int i = 0; i < taskList.size(); i++) {
            final TaskEntity pdb = i == 0 ? new TaskEntity() : taskList.get(i - 1);
            final TaskEntity db = taskList.get(i);
            final LambdaUpdateWrapper<TaskEntity> lambda = new LambdaUpdateWrapper<TaskEntity>()
                    .set(TaskEntity::getSorter, i)
                    .set(TaskEntity::getDeviceSorter, d.getSorter())
                    .set(TaskEntity::getDeviceId, d.getId())
                    .eq(TaskEntity::getId, db.getId());
            if (isNotBlank(pdb.getOfflineDate())) {
                final int onlineDateDiff = defaultInt(e.getOnlineDateDiff());
                final String onlineDate = DateUtil.day(DateUtil.day(cn.hutool.core.date.DateUtil.offsetDay(com.lead.fund.base.common.util.DateUtil.parse(pdb.getOfflineDate()), onlineDateDiff)));
                db.setOnlineDate(onlineDate);
                lambda.set(TaskEntity::getOnlineDate, onlineDate);
            }
            if (isBlank(db.getOnlineDate()) || null == db.getProcessWorkingHour() || null == db.getPlanReformCount()) {
                lambda.set(TaskEntity::getOfflineDate, null);
            } else {
                final int diff = db.getPlanReformCount().multiply(db.getProcessWorkingHour()).divide(new BigDecimal(60 * 18), 2, RoundingMode.HALF_UP).add(new BigDecimal("0.5")).setScale(0, RoundingMode.HALF_UP).intValue();
                final String offlineDate = DateUtil.day(cn.hutool.core.date.DateUtil.offsetDay(com.lead.fund.base.common.util.DateUtil.parse(db.getOnlineDate()), diff));
                db.setOfflineDate(offlineDate);
                lambda.set(TaskEntity::getOfflineDate, offlineDate);
            }
            if (isBlank(db.getOfflineDate()) || isBlank(db.getPromiseDoneDate())) {
                lambda.set(TaskEntity::getDelay, null);
            } else {
                final BigDecimal delay = new BigDecimal(cn.hutool.core.date.DateUtil.between(
                        DateUtil.parse(db.getPromiseDoneDate()),
                        DateUtil.parse(db.getOfflineDate()),
                        DateUnit.DAY,
                        false
                ));
                lambda.set(TaskEntity::getDelay, delay);
            }
            taskMapper.update(null, lambda);
        }
    }
}
