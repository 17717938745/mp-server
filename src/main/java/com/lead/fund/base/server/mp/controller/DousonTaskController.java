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
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.TaskEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.TaskMapper;
import com.lead.fund.base.server.mp.request.TaskPageRequest;
import com.lead.fund.base.server.mp.request.TaskRequest;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.TaskResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private MpUserMapper userMapper;
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
        final String today = DateUtil.day(new Date());
        final MpUserResponse u = accountHelper.getUser(deviceId);
        TaskEntity e = (TaskEntity) TASK_INSTANCE.task(request);
        lockHelper.lock("task");
        try {
            e
                    .setModifier(u.getUserId());
            // update
            if (isNotBlank(e.getId())) {
                if (u.getRoleList().stream().noneMatch(t -> "task".equals(t.getRoleCode()) || "taskManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
                    throw new BusinessException(AUTHORITY_AUTH_FAIL);
                }
                if (taskMapper.updateById((TaskEntity) e
                        .setCreator(u.getUserId())
                ) <= 0) {
                    throw new BusinessException(AUTHORITY_AUTH_FAIL);
                }
                // insert
            } else {
                if (u.getRoleList().stream().noneMatch(t -> "task".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
                    throw new BusinessException(AUTHORITY_AUTH_FAIL);
                }
                taskMapper.insert(e);
            }
            updateSummaryInfo(e, today);
        } finally {
            lockHelper.unlock("task");
        }
        return new DataResult<>(e);
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
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        taskMapper.deleteById(request.getTaskId());
        return new Result();
    }


    private List<TaskEntity> taskList(TaskRequest d) {
        LambdaQueryWrapper<TaskEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getTaskId())) {
            lambda.eq(TaskEntity::getId, d.getTaskId());
        }
        if (isNotBlank(d.getCustomerShortName())) {
            lambda.like(TaskEntity::getCustomerShortName, d.getCustomerShortName());
        }
        if (isNotBlank(d.getSaleOrderNo())) {
            lambda.like(TaskEntity::getSaleOrderNo, d.getSaleOrderNo());
        }
        if (isNotBlank(d.getDesignNumber())) {
            lambda.like(TaskEntity::getDesignNumber, d.getDesignNumber());
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
        return taskMapper.selectList(lambda);
    }


    private List<TaskResponse> formatTaskList(List<TaskEntity> list) {
        List<TaskResponse> rl = TASK_INSTANCE.taskList(list);
        List<String> userIdList = Stream.of(
                        rl.stream().map(TaskResponse::getCreator).filter(StrUtil::isNotBlank)
                )
                .flatMap(t -> t)
                .distinct()
                .collect(Collectors.toList());
        final List<MpUserEntity> userList = CollUtil.isEmpty(userIdList) ? new ArrayList<>() : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        );
        /*MultitaskUtil.supplementList(
                rl,
                TaskResponse::getOptimizeType,
                l -> paramDao.listByCategoryId("optimizeType"),
                (t, r) -> t.getOptimizeType().equals(r.getValue()),
                (t, r) -> t.setOptimizeTypeFormat(r.getLabel())
        );*/
        for (TaskResponse t : rl) {
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
        if (u.getRoleList().stream().anyMatch(t -> "taskManager".equals(t.getRoleCode()))) {
        }
        PageResult<TaskEntity> pr = DatabaseUtil.page(request, this::taskList);
        AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(pr.getTotal(), formatTaskList(pr.getList())
                .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    private void updateSummaryInfo(TaskEntity e, String today) {
        final List<TaskEntity> el = taskMapper.selectList(
                new LambdaQueryWrapper<TaskEntity>()
                        .eq(TaskEntity::getSaleOrderNo, e.getSaleOrderNo())
        );
        taskMapper.update(null,
                new LambdaUpdateWrapper<TaskEntity>()
                        .set(TaskEntity::getOrderCount, e.getOrderCount())
                        .eq(TaskEntity::getSaleOrderNo, e.getSaleOrderNo())
        );
    }
}
