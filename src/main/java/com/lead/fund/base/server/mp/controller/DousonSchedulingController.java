package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.SchedulingConverter.SCHEDULING_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.SchedulingDao;
import com.lead.fund.base.server.mp.dao.SchedulingDetailDao;
import com.lead.fund.base.server.mp.dao.TaskDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.DeviceEntity;
import com.lead.fund.base.server.mp.entity.douson.SchedulingDetailEntity;
import com.lead.fund.base.server.mp.entity.douson.SchedulingEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.SchedulingMapper;
import com.lead.fund.base.server.mp.request.SchedulingDetailRequest;
import com.lead.fund.base.server.mp.request.SchedulingPageRequest;
import com.lead.fund.base.server.mp.request.SchedulingRequest;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.SchedulingDetailResponse;
import com.lead.fund.base.server.mp.response.SchedulingDetailWrapperResponse;
import com.lead.fund.base.server.mp.response.SchedulingResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
 * DousonSchedulingController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused"})
@RestController
@RequestMapping("/douson/scheduling")
@Slf4j
@Validated
public class DousonSchedulingController {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private TemplatePhotoDao templatePhotoDao;
    @Resource
    private SchedulingMapper schedulingMapper;
    @Resource
    private TaskDao taskDao;
    @Resource
    private SchedulingDao schedulingDao;
    @Resource
    private SchedulingDetailDao schedulingDetailDao;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private ParamDao paramDao;
    @Resource
    private LockHelper lockHelper;
    @Resource
    private UrlHelper urlHelper;
    @Resource
    private DeviceMapper deviceMapper;

    /**
     * 保存、更新排班
     *
     * @param deviceId 设备id
     * @param request  {@link SchedulingRequest}
     * @return {@link Result}
     */
    @PutMapping("merge")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result merge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody SchedulingRequest request
    ) {
        final Date now = new Date();
        final String today = DateUtil.day(now);
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final SchedulingEntity e = (SchedulingEntity) SCHEDULING_INSTANCE.scheduling(request)
                .setModifier(u.getUserId());
        // update
        if (isNotBlank(e.getId())) {
            if (u.getRoleList().stream().noneMatch(t -> "admin".equals(t.getRoleCode()))) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            if (schedulingMapper.updateById(e) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            // insert
        } else {
            if (u.getRoleList().stream().noneMatch(t -> "admin".equals(t.getRoleCode()))) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            schedulingMapper.insert((SchedulingEntity) e
                    .setCreator(u.getUserId())
            );
        }
        return new DataResult<>(e);
    }

    /**
     * 删除排班
     *
     * @param deviceId 设备id
     * @param request  {@link SchedulingRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result delete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute SchedulingRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        if (isNotBlank(request.getSchedulingId())) {
            schedulingDao.removeById(request.getSchedulingId());
            schedulingDetailDao.remove(new LambdaUpdateWrapper<SchedulingDetailEntity>().eq(SchedulingDetailEntity::getSchedulingId, request.getSchedulingId()));
        }
        return new Result();
    }

    private List<SchedulingEntity> schedulingList(SchedulingRequest d) {
        LambdaQueryWrapper<SchedulingEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getSchedulingId())) {
            lambda.eq(SchedulingEntity::getId, d.getSchedulingId());
        }
        if (isNotBlank(d.getDateMonth())) {
            lambda.like(SchedulingEntity::getDateMonth, d.getDateMonth());
        }
        if (null != d.getStartDateMonth()) {
            lambda.ge(SchedulingEntity::getDateMonth, DateUtil.month(cn.hutool.core.date.DateUtil.beginOfDay(d.getStartDateMonth())));
        }
        if (null != d.getEndDateMonth()) {
            lambda.le(SchedulingEntity::getDateMonth, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(d.getEndDateMonth())));
        }
        lambda.orderByDesc(SchedulingEntity::getDateMonth).orderByDesc(SchedulingEntity::getWeekIndex);
        return schedulingDao.list(lambda);
    }

    private List<SchedulingDetailEntity> schedulingDetailList(SchedulingDetailRequest d) {
        LambdaQueryWrapper<SchedulingDetailEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getSchedulingId())) {
            lambda.eq(SchedulingDetailEntity::getId, d.getSchedulingId());
        }
        lambda.orderByAsc(SchedulingDetailEntity::getDeviceNumber);
        return schedulingDetailDao.list(lambda);
    }

    private List<SchedulingResponse> formatSchedulingList(List<SchedulingEntity> list) {
        final List<SchedulingResponse> rl = SCHEDULING_INSTANCE.schedulingList(list);
        for (SchedulingResponse t : rl) {
            final DateTime dateTime = DateUtil.parse(t.getDateMonth());
            final DateTime dateCopy = DateTime.of(dateTime.getTime());
            dateCopy.setField(DateField.WEEK_OF_MONTH, t.getWeekIndex());
            dateCopy.setField(DateField.DAY_OF_WEEK, 1);
            final String startDay = DateUtil.day(dateCopy);
            dateCopy.offset(DateField.WEEK_OF_MONTH, 1);
            final String endDay = DateUtil.day(dateCopy);
            t.setDateFormat(dateTime.toString("yyyy年MM月 ") + "第" + t.getIndex() + "周" + "（" + startDay + " - " + endDay + "）");
        }
        return rl;
    }

    private List<SchedulingDetailResponse> formatSchedulingDetailList(List<SchedulingDetailEntity> list) {
        final List<SchedulingDetailResponse> rl = SCHEDULING_INSTANCE.schedulingDetailList(list);
        final List<String> userIdList = Stream.of(
                        rl.stream().map(SchedulingDetailResponse::getScheduleDayTimeList),
                        rl.stream().map(SchedulingDetailResponse::getScheduleMiddleList),
                        rl.stream().map(SchedulingDetailResponse::getScheduleEveningList),
                        rl.stream().map(SchedulingDetailResponse::getScheduleDayTime12List),
                        rl.stream().map(SchedulingDetailResponse::getScheduleEvening12List),
                        rl.stream().map(SchedulingDetailResponse::getScheduleDayTimeTechnologyGroupList),
                        rl.stream().map(SchedulingDetailResponse::getScheduleEveningTechnologyGroupList)
                )
                .flatMap(t -> t.flatMap(Collection::stream))
                .filter(StrUtil::isNotBlank).distinct()
                .distinct()
                .collect(Collectors.toList());
        final Map<String, MpUserEntity> um = CollUtil.isEmpty(userIdList) ? new HashMap<>(8) : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        ).stream().collect(Collectors.toMap(AbstractPrimaryKey::getId, t -> t, (t, t1) -> t1, HashMap::new));
        final Map<String, String> dm = deviceMapper.selectList(new LambdaQueryWrapper<DeviceEntity>().orderByAsc(DeviceEntity::getSorter).select(DeviceEntity::getId, DeviceEntity::getDeviceName))
                .stream().collect(Collectors.toMap(AbstractPrimaryKey::getId, DeviceEntity::getDeviceName, (t, t1) -> t1, HashMap::new));
        for (SchedulingDetailResponse t : rl) {
            t
                    .setScheduleDayTimeFormat(t.getScheduleDayTimeList().stream().filter(StrUtil::isNotBlank).map(t1 -> dm.getOrDefault(t1, t1)).collect(Collectors.joining(",")))
                    .setScheduleMiddleFormat(t.getScheduleMiddleList().stream().filter(StrUtil::isNotBlank).map(t1 -> dm.getOrDefault(t1, t1)).collect(Collectors.joining(",")))
                    .setScheduleEveningFormat(t.getScheduleEveningList().stream().filter(StrUtil::isNotBlank).map(t1 -> dm.getOrDefault(t1, t1)).collect(Collectors.joining(",")))
                    .setScheduleDayTime12Format(t.getScheduleDayTime12List().stream().filter(StrUtil::isNotBlank).map(t1 -> dm.getOrDefault(t1, t1)).collect(Collectors.joining(",")))
                    .setScheduleEvening12Format(t.getScheduleEvening12List().stream().filter(StrUtil::isNotBlank).map(t1 -> dm.getOrDefault(t1, t1)).collect(Collectors.joining(",")))
                    .setScheduleDayTimeTechnologyGroupFormat(t.getScheduleDayTimeTechnologyGroupList().stream().filter(StrUtil::isNotBlank).map(t1 -> dm.getOrDefault(t1, t1)).collect(Collectors.joining(",")))
                    .setScheduleEveningTechnologyGroupFormat(t.getScheduleEveningTechnologyGroupList().stream().filter(StrUtil::isNotBlank).map(t1 -> dm.getOrDefault(t1, t1)).collect(Collectors.joining(",")))
            ;
        }
        return rl;
    }

    private static String tail(BigDecimal v) {
        return null == v || v.compareTo(BigDecimal.ZERO) <= 0 ? "" : ("-" + v.setScale(0, RoundingMode.HALF_UP));
    }

    /**
     * 排班分页
     *
     * @param deviceId 设备id
     * @param request  {@link SchedulingPageRequest}
     * @return {@link PageResult <SchedulingResponse>}
     */
    @GetMapping("page")
    public PageResult<SchedulingResponse> schedulingAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute SchedulingPageRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "schedulingManager".equals(t.getRoleCode()) || "schedulingRecord".equals(t.getRoleCode()) || "schedulingTesterRecord".equals(t.getRoleCode()) || "schedulingRecordView".equals(t.getRoleCode()) || "admin".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final PageResult<SchedulingEntity> pr = DatabaseUtil.page(request, this::schedulingList);
        final AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(pr.getTotal(), formatSchedulingList(pr.getList())
                .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 排班明细
     *
     * @param deviceId 设备id
     * @param request  {@link SchedulingDetailRequest}
     * @return {@link DataResult<SchedulingDetailWrapperResponse>}
     */
    @GetMapping("detail")
    public DataResult<SchedulingDetailWrapperResponse> detailList(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute SchedulingDetailRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "schedulingManager".equals(t.getRoleCode()) || "schedulingRecord".equals(t.getRoleCode()) || "schedulingTesterRecord".equals(t.getRoleCode()) || "schedulingRecordView".equals(t.getRoleCode()) || "admin".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final SchedulingDetailWrapperResponse r = new SchedulingDetailWrapperResponse();
        if (isNotBlank(request.getSchedulingId())) {
            r
                    .setScheduling(CollUtil.getFirst(formatSchedulingList(schedulingList(new SchedulingRequest().setSchedulingId(request.getSchedulingId())))))
                    .setSchedulingList(formatSchedulingDetailList(schedulingDetailList(request)))
            ;
            final AtomicInteger atomicInteger = new AtomicInteger(1);
            for (SchedulingDetailResponse t : r.getSchedulingList()) {
                t.setIndex(atomicInteger.getAndAdd(1));
            }
        }
        return new DataResult<>(r);
    }

    /**
     * 排班
     *
     * @param deviceId 设备id
     * @param request  {@link SchedulingRequest}
     * @return {@link DataResult<SchedulingResponse>}
     */
    @GetMapping("")
    public DataResult<SchedulingResponse> scheduling(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute SchedulingRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "schedulingManager".equals(t.getRoleCode()) || "schedulingRecord".equals(t.getRoleCode()) || "schedulingTesterRecord".equals(t.getRoleCode()) || "schedulingRecordView".equals(t.getRoleCode()) || "admin".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        return new DataResult<>(CollUtil.getFirst(formatSchedulingList(schedulingList(request))));
    }
}
