package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.TaskConverter.TASK_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.TaskDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.douson.DeviceEntity;
import com.lead.fund.base.server.mp.entity.douson.TaskEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.TaskMapper;
import com.lead.fund.base.server.mp.request.TaskPageRequest;
import com.lead.fund.base.server.mp.request.TaskRequest;
import com.lead.fund.base.server.mp.request.TaskSortData;
import com.lead.fund.base.server.mp.request.TaskSortRequest;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.TaskResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DousonTaskController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused"})
@RestController
@RequestMapping("/douson/task")
@Slf4j
@Validated
public class DousonTaskController {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private TemplatePhotoDao templatePhotoDao;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private TaskDao taskDao;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private ParamDao paramDao;
    @Resource
    private LockHelper lockHelper;


    /**
     * 保存、更新生产计划
     *
     * @param deviceId 设备id
     * @param request  {@link TaskRequest}
     * @return {@link Result}
     */
    @PutMapping("merge")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result merge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody TaskRequest request
    ) {
        return new DataResult<>(
                taskDao.merge(
                        deviceId,
                        TASK_INSTANCE.task(request)
                )
        );
    }

    /**
     * 生产计划排序
     *
     * @param deviceId 设备id
     * @param request  {@link TaskRequest}
     * @return {@link Result}
     */
    @PutMapping("sort")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result sort(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody TaskSortRequest request
    ) {
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        for (TaskSortData t : request.getTaskList()) {
            taskDao.update(new LambdaUpdateWrapper<TaskEntity>()
                    .set(TaskEntity::getSorter, atomicInteger.getAndAdd(1))
                    .eq(TaskEntity::getId, t.getTaskId())
            );
        }
        return new Result();
    }

    /**
     * 删除生产工单
     *
     * @param deviceId 设备id
     * @param request  {@link TaskRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result delete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute TaskRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "taskManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        taskDao.deleteById(request.getTaskId());
        return new Result();
    }


    private List<TaskEntity> taskList(TaskRequest d) {
        LambdaQueryWrapper<TaskEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getTaskId())) {
            lambda.eq(TaskEntity::getId, d.getTaskId());
        }
        if (isNotBlank(d.getCreator())) {
            lambda.eq(TaskEntity::getCreator, d.getCreator());
        }
        if (isNotBlank(d.getDeviceId())) {
            lambda.eq(TaskEntity::getDeviceId, d.getDeviceId());
        }
        if (isNotBlank(d.getCustomerShortName())) {
            lambda.like(TaskEntity::getCustomerShortName, d.getCustomerShortName());
        }
        if (isNotBlank(d.getSaleOrderNo())) {
            lambda.like(TaskEntity::getSaleOrderNo, d.getSaleOrderNo());
        }
        if (isNotBlank(d.getOrderProjectNo())) {
            lambda.like(TaskEntity::getOrderProjectNo, d.getOrderProjectNo());
        }
        if (isNotBlank(d.getMaterialNo())) {
            lambda.like(TaskEntity::getMaterialNo, d.getMaterialNo());
        }
        if (isNotBlank(d.getImproveMaterialDescribe())) {
            lambda.like(TaskEntity::getImproveMaterialDescribe, d.getImproveMaterialDescribe());
        }
        if (isNotBlank(d.getDesignNumber())) {
            lambda.like(TaskEntity::getDesignNumber, d.getDesignNumber());
        }
        if (null != d.getOrderCount()) {
            lambda.eq(TaskEntity::getOrderCount, d.getOrderCount());
        }
        if (null != d.getStartPromiseDoneDate()) {
            lambda.ge(TaskEntity::getPromiseDoneDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(d.getStartPromiseDoneDate()).toJdkDate()));
        }
        if (null != d.getEndPromiseDoneDate()) {
            lambda.le(TaskEntity::getPromiseDoneDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(d.getEndPromiseDoneDate()).toJdkDate()));
        }
        if (null != d.getDelay()) {
            lambda.eq(TaskEntity::getDelay, d.getDelay());
        }
        if (null != d.getScrapCount()) {
            lambda.eq(TaskEntity::getScrapCount, d.getScrapCount());
        }
        if (null != d.getStartSupplierPromiseDoneDate()) {
            lambda.ge(TaskEntity::getSupplierPromiseDoneDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(d.getStartSupplierPromiseDoneDate()).toJdkDate()));
        }
        if (null != d.getEndSupplierPromiseDoneDate()) {
            lambda.le(TaskEntity::getSupplierPromiseDoneDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(d.getEndSupplierPromiseDoneDate()).toJdkDate()));
        }
        if (isNotBlank(d.getNde())) {
            lambda.eq(TaskEntity::getNde, d.getNde());
        }
        if (isNotBlank(d.getAssemble())) {
            lambda.eq(TaskEntity::getAssemble, d.getAssemble());
        }
        if (isNotBlank(d.getTestPress())) {
            lambda.eq(TaskEntity::getTestPress, d.getTestPress());
        }
        if (isNotBlank(d.getSurfaceTreatment())) {
            lambda.eq(TaskEntity::getSurfaceTreatment, d.getSurfaceTreatment());
        }
        if (null != d.getSurplus()) {
            lambda.eq(TaskEntity::getSurplus, d.getSurplus());
        }
        if (null != d.getSurplusCountType()) {
            if (1 == d.getSurplusCountType()) {
                lambda.eq(TaskEntity::getSurplus, 0);
            } else {
                lambda.and(true, lambdaTemp -> lambdaTemp.ne(TaskEntity::getSurplus, 0).or(true, lam -> lam.isNull(TaskEntity::getSurplus)));
            }
        }
        /*if (Boolean.TRUE.equals(d.getSupplier())) {
            DatabaseUtil.or(lambda, deviceMapper.selectList(new LambdaQueryWrapper<DeviceEntity>().eq(DeviceEntity::getSupplier, true))
                    .stream().map(AbstractPrimaryKey::getId)
                    .collect(Collectors.toList()), (lam, l) -> {
                lam.in(TaskEntity::getDeviceId, l);
            })
            ;
        }*/
        if (null != d.getDelayType()) {
            if (1 == d.getDelayType()) {
                lambda.eq(TaskEntity::getSurplus, 0)
                        .isNotNull(TaskEntity::getReceiptDate)
                        .isNotNull(TaskEntity::getSupplierDoneDate)
                        .apply("RECEIPT_DATE <= SUPPLIER_DONE_DATE")
                ;
            } else {
                lambda.ne(TaskEntity::getSurplus, 0)
                        .or(true, lam -> {
                            lam.apply("RECEIPT_DATE IS NULL OR SUPPLIER_DONE_DATE IS NULL OR RECEIPT_DATE > SUPPLIER_DONE_DATE");
                        });
            }
        }
        if (null != d.getProcessType()) {
            if (1 == d.getProcessType()) {
                lambda.apply("(PROCESS_COUNT !=0 AND MATERIAL_COUNT != 0 AND PROCESS_COUNT = MATERIAL_COUNT)");
            } else if (0 == d.getProcessType()) {
                lambda.apply("(PROCESS_COUNT =0 OR MATERIAL_COUNT = 0 OR PROCESS_COUNT != MATERIAL_COUNT)");
            }
        }
        if (null != d.getProcessCountType()) {
            if (1 == d.getProcessCountType()) {
                lambda.apply("(PROCESS_COUNT != 0 AND PLAN_REFORM_COUNT != 0 AND PROCESS_COUNT = PLAN_REFORM_COUNT)");
            } else if (0 == d.getProcessCountType()) {
                lambda.apply("(PROCESS_COUNT = 0 OR PLAN_REFORM_COUNT = 0 OR PROCESS_COUNT != PLAN_REFORM_COUNT)");
            }
        }
        if (null != d.getReceiptCountType()) {
            if (1 == d.getReceiptCountType()) {
                lambda.apply("(RECEIPT_COUNT != 0 AND PLAN_REFORM_COUNT != 0 AND RECEIPT_COUNT = PLAN_REFORM_COUNT)");
            } else if (0 == d.getReceiptCountType()) {
                lambda.apply("(RECEIPT_COUNT = 0 OR PLAN_REFORM_COUNT = 0 OR RECEIPT_COUNT != PLAN_REFORM_COUNT)");
            }
        }
        if (isNotBlank(d.getMaterialOrderNo())) {
            lambda.like(TaskEntity::getMaterialOrderNo, d.getMaterialOrderNo());
        }
        if (isNotBlank(d.getCheckOrderNo())) {
            lambda.like(TaskEntity::getCheckOrderNo, d.getCheckOrderNo());
        }
        return taskMapper.selectList(lambda.orderByAsc(TaskEntity::getDeviceSorter).orderByAsc(TaskEntity::getSorter).orderByAsc(TaskEntity::getPromiseDoneDate));
    }


    private List<TaskResponse> formatTaskList(List<TaskEntity> list) {
        List<TaskResponse> rl = TASK_INSTANCE.taskList(list);
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getDeviceId())).collect(Collectors.toList()),
                TaskResponse::getDeviceId,
                l -> deviceMapper.selectList(DatabaseUtil.or(new LambdaQueryWrapper<>(), l, (lam, ll) -> lam.in(DeviceEntity::getId, ll))),
                (t, r) -> t.getDeviceId().equals(r.getId()),
                (t, r) -> t.setDeviceIdFormat(r.getDeviceName())
        );
        for (TaskResponse t : rl) {
            t.setCheckOrderNoFormat(t.getCheckOrderNo());
            t.setMaterialOrderNoFormat(t.getMaterialOrderNo());
            if (null != t.getSurplus() && BigDecimal.ZERO.equals(t.getSurplus()) && isNotBlank(t.getReceiptDate()) && isNotBlank(t.getSupplierDoneDate()) && DateUtil.future(t.getReceiptDate(), t.getSupplierDoneDate())) {
                t.setDelayType(1)
                        .setDelayTypeFormat("No");
            } else {
                t.setDelayType(0)
                        .setDelayTypeFormat("Yes");
            }
            t.setTimelyDeliver(t.getPlanReformCount().compareTo(BigDecimal.ZERO) != 0 && t.getPlanReformCount().equals(t.getReceiptCount()) && isNotBlank(t.getSupplierDoneDate()) && isNotBlank(t.getReceiptDate()) && t.getSupplierDoneDate().compareTo(t.getReceiptDate()) >= 0);
            t.setTimelyDeliverFormat(Boolean.TRUE.equals(t.getTimelyDeliver()) ? "Yes" : "No");
        }
        return rl;
    }

    private static String tail(BigDecimal v) {
        return null == v || v.compareTo(BigDecimal.ZERO) <= 0 ? "" : ("-" + v.setScale(0, RoundingMode.HALF_UP));
    }

    /**
     * 生产工单分页
     *
     * @param deviceId 设备id
     * @param request  {@link TaskPageRequest}
     * @return {@link PageResult <TaskResponse>}
     */
    @GetMapping("page")
    public PageResult<TaskResponse> taskAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute TaskPageRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "admin".equals(t.getRoleCode()) || "taskManager".equals(t.getRoleCode()) || "taskView".equals(t.getRoleCode()) || "supplierManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername()) && !"3".equals(u.getUserProperty())) {
            return new PageResult<>(0, new ArrayList<>());
        }
        // 供应商只能看自己
        if ("3".equals(u.getUserProperty())) {
            request.getData().setDeviceId(deviceMapper.selectList(new LambdaQueryWrapper<DeviceEntity>().eq(DeviceEntity::getManager, u.getUserId()))
                    .stream().map(AbstractPrimaryKey::getId).findFirst().orElse("null"));
        } else if (u.getRoleList().stream().anyMatch(t -> !"admin".equals(t.getRoleCode()) && !"taskManager".equals(t.getRoleCode()) && !"taskView".equals(t.getRoleCode()) && "supplierManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
            request.getData().setSupplier(true);
        }
        final PageResult<TaskEntity> pr = DatabaseUtil.page(request, this::taskList);
        final AtomicInteger index = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        final AtomicInteger deviceIndex = new AtomicInteger(0);
        final TaskResponse[] prev = {new TaskResponse().setDeviceId("")};
        final List<TaskResponse> l = formatTaskList(pr.getList());
        return new PageResult<>(
                pr.getTotal(),
                l.stream().map(t -> {
                    t.setIndex(index.addAndGet(1))
                            .setUp(index.get() > 1)
                            .setDown(index.get() < pr.getTotal());
                    List<TaskResponse> rl;
                    if (!t.getDeviceId().equals(prev[0].getDeviceId())) {
                        prev[0].setDown(false);
                        t.setUp(false);
                        rl = CollUtil.toList(new TaskResponse().setTaskId("auto-" + index.get()), t);
                        deviceIndex.set(0);
                    } else {
                        rl = CollUtil.toList(t);
                    }
                    prev[0] = t;
                    t
                            .setDeviceIndex(deviceIndex.addAndGet(1));
                    return rl;
                }).flatMap(Collection::stream).collect(Collectors.toList())
        );
    }

}
