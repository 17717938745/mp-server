package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.TaskConverter.TASK_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.model.OptionItem;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.TaskDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.DeviceEntity;
import com.lead.fund.base.server.mp.entity.douson.TaskEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.TaskMapper;
import com.lead.fund.base.server.mp.request.TaskPageRequest;
import com.lead.fund.base.server.mp.request.TaskRequest;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.TaskResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
                                .setProcessProcedure("," + String.join(",", request.getProcessProcedureList()) + ",")
                )
        );
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
        if (null != d.getProcessType()) {
            if (1 == d.getProcessType()) {
                lambda.apply("PROCESS_COUNT = MATERIAL_COUNT");
            } else if (0 == d.getProcessType()) {
                lambda.apply("(PROCESS_COUNT IS NULL OR MATERIAL_COUNT IS NULL OR PROCESS_COUNT != MATERIAL_COUNT)");
            }
        }
        if (isNotBlank(d.getMaterialOrderNo())) {
            lambda.like(TaskEntity::getMaterialOrderNo, d.getMaterialOrderNo());
        }
        if (isNotBlank(d.getCheckOrderNo())) {
            lambda.like(TaskEntity::getCheckOrderNo, d.getCheckOrderNo());
        }
        return taskMapper.selectList(lambda.orderByAsc(TaskEntity::getDeviceSorter).orderByAsc(TaskEntity::getSorter));
    }


    private List<TaskResponse> formatTaskList(List<TaskEntity> list) {
        List<TaskResponse> rl = TASK_INSTANCE.taskList(list);
        List<String> userIdList = Stream.of(
                        rl.stream().map(TaskResponse::getCreator).filter(StrUtil::isNotBlank)
                )
                .flatMap(t -> t)
                .distinct()
                .collect(Collectors.toList());
        final List<MpUserEntity> userList = CollUtil.isEmpty(userIdList) ? new ArrayList<MpUserEntity>() : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getDeviceId())).collect(Collectors.toList()),
                TaskResponse::getDeviceId,
                l -> deviceMapper.selectList(DatabaseUtil.or(new LambdaQueryWrapper<>(), l, (lam, ll) -> lam.in(DeviceEntity::getId, ll))),
                (t, r) -> t.getDeviceId().equals(r.getId()),
                (t, r) -> t.setDeviceIdFormat(r.getDeviceName())
        );
        final List<String> ppl = rl.stream().map(TaskResponse::getProcessProcedureList).filter(CollUtil::isNotEmpty).flatMap(Collection::stream).toList();
        final Map<Object, String> ppm = paramDao.listByCategoryId("processProcedure").stream().collect(Collectors.toMap(OptionItem::getValue, OptionItem::getLabel, (t, t1) -> t1));
        for (TaskResponse t : rl) {
            t.setCheckOrderNoFormat(t.getCheckOrderNo());
            t.setMaterialOrderNoFormat(t.getMaterialOrderNo());
            t.setProcessProcedureFormat(t.getProcessProcedureList().stream().map(t1 -> ppm.getOrDefault(t1, t1)).collect(Collectors.joining(",")));
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
        if (u.getRoleList().stream().noneMatch(t -> "admin".equals(t.getRoleCode()) || "taskManager".equals(t.getRoleCode()) || "taskView".equals(t.getRoleCode()) || "supplierManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
            return new PageResult<>(0, new ArrayList<>());
        }
        final PageResult<TaskEntity> pr = DatabaseUtil.page(request, this::taskList);
        final AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        final TaskResponse[] prev = {new TaskResponse().setDeviceId("")};
        final List<TaskResponse> l = formatTaskList(pr.getList());
        return new PageResult<>(
                pr.getTotal(),
                l.stream().map(t -> {
                    t.setIndex(atomicInteger.addAndGet(1))
                            .setUp(isNotBlank(t.getDeviceId()) && t.getIndex() > 0)
                            .setDown(isNotBlank(t.getDeviceId()) && t.getIndex() < pr.getTotal());
                    List<TaskResponse> rl;
                    if (!t.getDeviceId().equals(prev[0].getDeviceId())) {
                        prev[0].setDown(false);
                        t.setUp(false);
                        rl = CollUtil.toList(new TaskResponse(), t);
                    } else {
                        rl = CollUtil.toList(t);
                    }
                    prev[0] = t;
                    return rl;
                }).flatMap(Collection::stream).collect(Collectors.toList())
        );
    }

}
