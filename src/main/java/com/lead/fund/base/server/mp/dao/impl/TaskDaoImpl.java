package com.lead.fund.base.server.mp.dao.impl;

import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.NumberUtil.defaultDecimal;
import static com.lead.fund.base.common.util.NumberUtil.defaultInt;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;

import cn.hutool.core.date.DateUnit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.util.BeanUtil;
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
import java.util.function.Predicate;
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
        e.setDeviceId(defaultIfBlank(e.getDeviceId()))
                .setSorter(defaultInt(e.getSorter()))
                .setOrderCount(defaultDecimal(e.getOrderCount()))
                .setMaterialCount(defaultDecimal(e.getMaterialCount()))
                .setPlanReformCount(defaultDecimal(e.getPlanReformCount()))
                .setProcessCount(defaultDecimal(e.getProcessCount()))
                .setDeliverCount(defaultDecimal(e.getDeliverCount()))
                .setReceiptCount(defaultDecimal(e.getReceiptCount()))
                .setScrapCount(defaultDecimal(e.getScrapCount()))
                .setModifyTime(new Date());
        final TaskEntity db = null == e.getId() ? new TaskEntity() : BeanUtil.defaultIfNull(taskMapper.selectById(e.getId()), new TaskEntity());
        if (null == e.getProcessWorkingHour()) {
            e.setProductCountHour8(null)
                    .setProductCountHour12(null);
        } else {
            e.setProductCountHour8(new BigDecimal("435").divide(e.getProcessWorkingHour(), 1, RoundingMode.HALF_UP))
                    .setProductCountHour12(new BigDecimal("585").divide(e.getProcessWorkingHour(), 1, RoundingMode.HALF_UP));
        }
        if (isBlank(e.getDeviceId())) {
            e.setOnlineDate("")
                    .setSorter(999999);
        }
        final DeviceEntity d = isBlank(e.getDeviceId()) ? (DeviceEntity) new DeviceEntity().setSorter(0).setId("") : deviceMapper.selectById(e.getDeviceId());
        e.setDeviceSorter(d.getSorter());
        final boolean supplier = isNotBlank(e.getDeviceId()) && Boolean.TRUE.equals(d.getSupplier());
        if (supplier) {
            e.setSurplus(defaultDecimal(e.getMaterialCount()).subtract(defaultDecimal(e.getReceiptCount())).subtract(defaultDecimal(e.getScrapCount())));
        } else {
            e.setSurplus(defaultDecimal(e.getOrderCount()).subtract(defaultDecimal(e.getProcessCount())));
        }
        if (isBlank(e.getPromiseDoneDate())) {
            e.setSupplierDoneDate(null);
        } else {
            final int diff = -10 - ((isNotBlank(e.getNde()) ? 1 : 0) + (isNotBlank(e.getAssemble()) ? 5 : 0) + (isNotBlank(e.getTestPress()) ? 3 : 0) + (isNotBlank(e.getSurfaceTreatment()) ? 3 : 0)) + defaultDecimal(e.getSupplierDoneDateDiff()).intValue();
            e.setSupplierDoneDate(DateUtil.day(cn.hutool.core.date.DateUtil.offsetDay(com.lead.fund.base.common.util.DateUtil.parse(e.getPromiseDoneDate()), diff)));
        }
        if (e.getDeliverCount().equals(BigDecimal.ZERO)) {
            e.setDeliverDate("");
        } else if (e.getDeliverCount().compareTo(defaultDecimal(db.getDeliverCount())) != 0) {
            e.setDeliverDate(DateUtil.day(new Date()));
        }
        if (e.getReceiptCount().equals(BigDecimal.ZERO)) {
            e.setReceiptDate("");
        } else if (e.getReceiptCount().compareTo(defaultDecimal(db.getReceiptCount())) != 0) {
            e.setReceiptDate(DateUtil.day(new Date()));
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
        updateSummaryInfo(e, d);
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
        updateSummaryInfo(e, null);
    }

    private void updateSummaryInfo(TaskEntity e, DeviceEntity device) {
        if (isNotBlank(e.getDeviceId())) {
            final DeviceEntity d = null != device ? device : isBlank(e.getDeviceId()) ? (DeviceEntity) new DeviceEntity().setSorter(0).setId("") : deviceMapper.selectById(e.getDeviceId());
            final List<TaskEntity> taskList = taskMapper.selectList(new LambdaQueryWrapper<TaskEntity>()
                    .eq(TaskEntity::getDeviceId, e.getDeviceId())
                    .orderByAsc(TaskEntity::getSorter).orderByDesc(TaskEntity::getModifyTime)
            );
            boolean supplier = Boolean.TRUE.equals(d.getSupplier());
            final BigDecimal orderCount = defaultDecimal(e.getOrderCount());
            final Predicate<TaskEntity> predicateNoSupplier = t -> defaultIfBlank(e.getDeviceId()).equals(t.getDeviceId()) &&
                    defaultDecimal(e.getOrderCount()).intValue() == (defaultDecimal(t.getOrderCount()).intValue()) &&
                    defaultIfBlank(e.getSaleOrderNo()).equals(t.getSaleOrderNo()) &&
                    defaultIfBlank(e.getOrderProjectNo()).equals(t.getOrderProjectNo()) &&
                    defaultIfBlank(e.getProcessProcedure()).equals(t.getProcessProcedure());
            final Predicate<TaskEntity> predicateSupplier = t -> defaultIfBlank(e.getDeviceId()).equals(t.getDeviceId()) &&
                    defaultDecimal(e.getOrderCount()).intValue() == (defaultDecimal(t.getOrderCount()).intValue()) &&
                    defaultIfBlank(e.getSaleOrderNo()).equals(t.getSaleOrderNo()) &&
                    defaultIfBlank(e.getOrderProjectNo()).equals(t.getOrderProjectNo()) &&
                    defaultIfBlank(defaultIfBlank(e.getSupplierRemark())).equals(defaultIfBlank(t.getSupplierRemark()));
            final BigDecimal sumProcessCount = taskList.stream().filter(predicateNoSupplier).map(t -> defaultDecimal(t.getProcessCount())).reduce(BigDecimal.ZERO, (t, t1) -> BigDecimal.ZERO.add(t).add(t1));
            final BigDecimal surplusNoSupplier = orderCount.subtract(sumProcessCount);
            final BigDecimal sumReceiptCount = taskList.stream().filter(predicateSupplier).map(t -> defaultDecimal(t.getReceiptCount())).reduce(BigDecimal.ZERO, (t, t1) -> BigDecimal.ZERO.add(t).add(t1));
            // final BigDecimal sumScrapCount = taskList.stream().filter(predicateSupplier).map(t -> defaultDecimal(t.getScrapCount())).reduce(BigDecimal.ZERO, (t, t1) -> BigDecimal.ZERO.add(t).add(t1));
            final BigDecimal sumScrapCount = BigDecimal.ZERO;
            final BigDecimal surplusSupplier = orderCount.subtract(sumReceiptCount).subtract(sumScrapCount);
            TaskEntity pdb = new TaskEntity();
            for (int i = 0; i < taskList.size(); i++) {
                final TaskEntity db = taskList.get(i);
                final LambdaUpdateWrapper<TaskEntity> lambda = new LambdaUpdateWrapper<TaskEntity>()
                        .set(TaskEntity::getDeviceId, d.getId())
                        .set(TaskEntity::getDeviceSorter, d.getSorter())
                        .set(TaskEntity::getSorter, isBlank(e.getDeviceId()) ? 999999 : i)
                        .eq(TaskEntity::getId, db.getId());
                if (supplier) {
                    if (predicateSupplier.test(db)) {
                        lambda
                                .set(TaskEntity::getSurplus, surplusSupplier)
                        ;
                        db.setSurplus(surplusSupplier);
                    }
                } else {
                    if (predicateNoSupplier.test(db)) {
                        lambda
                                .set(TaskEntity::getSurplus, surplusNoSupplier)
                        ;
                        db.setSurplus(surplusNoSupplier);
                    }
                }
                if (isNotBlank(pdb.getOfflineDate())) {
                    final String onlineDate = DateUtil.dateTime(cn.hutool.core.date.DateUtil.offsetHour(com.lead.fund.base.common.util.DateUtil.parse(pdb.getOfflineDate()), defaultDecimal(db.getOnlineDateDiff()).intValue()));
                    lambda.set(TaskEntity::getOnlineDate, onlineDate);
                    db.setOnlineDate(onlineDate);
                }
                if (isBlank(db.getOnlineDate()) || null == db.getProcessWorkingHour() || null == db.getPlanReformCount()) {
                    lambda.set(TaskEntity::getOfflineDate, null);
                    db.setOfflineDate(null);
                } else {
                    final int diff = db.getPlanReformCount().multiply(db.getProcessWorkingHour()).divide(new BigDecimal(60 * 18), 2, RoundingMode.HALF_UP).add(new BigDecimal("0.5")).setScale(0, RoundingMode.HALF_UP).intValue();
                    final String offlineDate = DateUtil.dateTime(cn.hutool.core.date.DateUtil.offsetDay(com.lead.fund.base.common.util.DateUtil.parse(db.getOnlineDate()), diff));
                    lambda.set(TaskEntity::getOfflineDate, offlineDate);
                    db.setOfflineDate(offlineDate);
                }
                if (isBlank(db.getOfflineDate()) || isBlank(db.getPromiseDoneDate())) {
                    lambda.set(TaskEntity::getDelay, null);
                    db.setDelay(null);
                } else {
                    final BigDecimal delay = new BigDecimal(cn.hutool.core.date.DateUtil.between(
                            DateUtil.parse(db.getPromiseDoneDate()),
                            DateUtil.parse(db.getOfflineDate()),
                            DateUnit.DAY,
                            false
                    ));
                    lambda.set(TaskEntity::getDelay, delay);
                    db.setDelay(delay);
                }
                taskMapper.update(null, lambda);
                pdb = db;
            }
        }
    }
}
