package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.NumberUtil.defaultInt;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.SchedulingConverter.SCHEDULING_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.model.OptionItem;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.BeanUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.SchedulingDao;
import com.lead.fund.base.server.mp.dao.SchedulingDetailDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.DeviceEntity;
import com.lead.fund.base.server.mp.entity.douson.SchedulingDetailEntity;
import com.lead.fund.base.server.mp.entity.douson.SchedulingEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.SchedulingMapper;
import com.lead.fund.base.server.mp.request.SchedulingDetailRequest;
import com.lead.fund.base.server.mp.request.SchedulingPageRequest;
import com.lead.fund.base.server.mp.request.SchedulingRequest;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.ParamConfigResponse;
import com.lead.fund.base.server.mp.response.SchedulingDetailResponse;
import com.lead.fund.base.server.mp.response.SchedulingDetailWrapperResponse;
import com.lead.fund.base.server.mp.response.SchedulingResponse;
import com.lead.fund.base.server.mp.response.VocationResponse;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
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
@RestController
@RequestMapping("/douson/scheduling")
@Slf4j
@Validated
public class DousonSchedulingController {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private SchedulingMapper schedulingMapper;
    @Resource
    private SchedulingDao schedulingDao;
    @Resource
    private SchedulingDetailDao schedulingDetailDao;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private ParamDao paramDao;

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
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "admin".equals(t.getRoleCode()) || "schedulingManager".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final Date now = new Date();
        final SchedulingEntity e = (SchedulingEntity) SCHEDULING_INSTANCE.scheduling(request)
                .setWeekIndex(DateUtil.parse(request.getDateMonth()).getField(DateField.WEEK_OF_YEAR))
                .setModifier(u.getUserId());
        // copy
        if (isNotBlank(request.getSchedulingCopyId())) {
            schedulingMapper.insert((SchedulingEntity) e
                    .setCreator(u.getUserId())
            );
            schedulingDetailDao.saveBatch(
                    schedulingDetailDao.list(new LambdaQueryWrapper<SchedulingDetailEntity>()
                                    .eq(SchedulingDetailEntity::getSchedulingId, request.getSchedulingCopyId())
                            ).stream()
                            .peek(t -> {
                                t.setSchedulingId(e.getId())
                                        .setId(null)
                                ;
                            }).collect(Collectors.toList())
            );
            // update
        } else if (isNotBlank(e.getId())) {
            if (schedulingMapper.updateById(e) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            // insert
        } else {
            schedulingMapper.insert((SchedulingEntity) e
                    .setCreator(u.getUserId())
            );
            MultitaskUtil.batchInvokeAll(
                    deviceMapper.selectList(new LambdaQueryWrapper<DeviceEntity>()
                                    .select(DeviceEntity::getId, DeviceEntity::getSorter))
                            .stream()
                            .sorted(Comparator.comparing(DeviceEntity::getSorter))
                            .map(t -> new SchedulingDetailEntity()
                                    .setSchedulingId(e.getId())
                                    .setDeviceNumber(t.getId())).collect(Collectors.toList()),
                    l -> {
                        schedulingDetailDao.saveBatch(l);
                        return l;
                    }
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
            lambda.eq(SchedulingDetailEntity::getSchedulingId, d.getSchedulingId());
        }
        if (isNotBlank(d.getDeviceNumber())) {
            lambda.eq(SchedulingDetailEntity::getDeviceNumber, d.getDeviceNumber());
        }
        lambda.orderByAsc(SchedulingDetailEntity::getDeviceNumber);
        return schedulingDetailDao.list(lambda);
    }

    private List<SchedulingResponse> formatSchedulingList(List<SchedulingEntity> list) {
        final List<SchedulingResponse> rl = SCHEDULING_INSTANCE.schedulingList(list);
        for (SchedulingResponse t : rl) {
            final DateTime dateTime = DateUtil.parse(t.getDateMonth());
            final DateTime startDate = DateTime.of(dateTime.getTime());
            final DateTime endDate = DateTime.of(startDate.getTime());
            endDate.setField(DateField.DAY_OF_WEEK, 1);
            endDate.offset(DateField.WEEK_OF_MONTH, 1).offset(DateField.DAY_OF_YEAR, -1);
            t.setDateMonthFormat(dateTime.toString("yyyy-MM ") + "Week " + dateTime.getField(DateField.WEEK_OF_MONTH) + "（" + dateTime.toString("yyyy") + "Week " + t.getWeekIndex() + ", " + DateUtil.day(startDate) + " - " + DateUtil.day(endDate) + "）")
                    .setSchedulingTitle("Bảng chia ca CNC 排班表，Tuần %2d năm %s,(%s đến %s)".formatted(t.getWeekIndex(), dateTime.toString("yyyy"), startDate.toString("dd/MM"), endDate.toString("dd/MM")))
            ;
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
                .filter(StrUtil::isNotBlank)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, MpUserEntity> um = CollUtil.isEmpty(userIdList) ? new HashMap<>(8) : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName, MpUserEntity::getProfession),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        ).stream().collect(Collectors.toMap(AbstractPrimaryKey::getId, t -> t, (t, t1) -> t1, HashMap::new));
        final Map<String, DeviceEntity> dm = deviceMapper.selectList(new LambdaQueryWrapper<DeviceEntity>().orderByAsc(DeviceEntity::getSorter).select(DeviceEntity::getId, DeviceEntity::getDeviceName, DeviceEntity::getSorter))
                .stream().collect(Collectors.toMap(AbstractPrimaryKey::getId, t -> t, (t, t1) -> t1, HashMap::new));
        return rl.stream().peek(t -> {
            t
                    .setDeviceSorter(0)
                    .setScheduleDayTimeFormat(t.getScheduleDayTimeList().stream().filter(StrUtil::isNotBlank).map(t1 -> BeanUtil.wrapperIfNotNull(um.get(t1), MpUserEntity::getName, t1)).distinct().collect(Collectors.joining(",")))
                    .setScheduleMiddleFormat(t.getScheduleMiddleList().stream().filter(StrUtil::isNotBlank).map(t1 -> BeanUtil.wrapperIfNotNull(um.get(t1), MpUserEntity::getName, t1)).collect(Collectors.joining(",")))
                    .setScheduleEveningFormat(t.getScheduleEveningList().stream().filter(StrUtil::isNotBlank).map(t1 -> BeanUtil.wrapperIfNotNull(um.get(t1), MpUserEntity::getName, t1)).collect(Collectors.joining(",")))
                    .setScheduleDayTime12Format(t.getScheduleDayTime12List().stream().filter(StrUtil::isNotBlank).map(t1 -> BeanUtil.wrapperIfNotNull(um.get(t1), MpUserEntity::getName, t1)).collect(Collectors.joining(",")))
                    .setScheduleEvening12Format(t.getScheduleEvening12List().stream().filter(StrUtil::isNotBlank).map(t1 -> BeanUtil.wrapperIfNotNull(um.get(t1), MpUserEntity::getName, t1)).collect(Collectors.joining(",")))
                    .setScheduleDayTimeTechnologyGroupFormat(t.getScheduleDayTimeTechnologyGroupList().stream().filter(StrUtil::isNotBlank).map(t1 -> BeanUtil.wrapperIfNotNull(um.get(t1), MpUserEntity::getName, t1)).collect(Collectors.joining(",")))
                    .setScheduleEveningTechnologyGroupFormat(t.getScheduleEveningTechnologyGroupList().stream().filter(StrUtil::isNotBlank).map(t1 -> BeanUtil.wrapperIfNotNull(um.get(t1), MpUserEntity::getName, t1)).collect(Collectors.joining(",")))
            ;
            final DeviceEntity de = dm.get(t.getDeviceNumber());
            if (null != de) {
                t.setDeviceNumberFormat(de.getDeviceName())
                        .setDeviceSorter(defaultInt(de.getSorter()));
            } else {
                t
                        .setDeviceNumberFormat(t.getDeviceNumber())
                        .setDeviceSorter(0);
            }
        }).sorted(Comparator.comparing(SchedulingDetailResponse::getDeviceSorter)).collect(Collectors.toList());
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
    public DataResult<SchedulingDetailWrapperResponse> detail(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute SchedulingDetailRequest request
    ) {
        final SchedulingDetailWrapperResponse r = new SchedulingDetailWrapperResponse();
        if (isNotBlank(request.getSchedulingId())) {
            r
                    .setScheduling(CollUtil.getFirst(formatSchedulingList(schedulingList(new SchedulingRequest().setSchedulingId(request.getSchedulingId())))))
                    .setSchedulingList(formatSchedulingDetailList(schedulingDetailList(request)))
            ;
            final List<SchedulingDetailResponse> rl = r.getSchedulingList();
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
                    .filter(StrUtil::isNotBlank)
                    .distinct()
                    .collect(Collectors.toList());
            final Map<String, String> pm = paramDao.listByCategoryId("profession")
                    .stream().filter(t -> defaultIfBlank(t.getLabel()).contains("组长")).collect(Collectors.toMap(t -> String.valueOf(t.getValue()), OptionItem::getLabel, (t, t1) -> t1, HashMap::new));
            final Map<String, Boolean> um = CollUtil.isEmpty(userIdList) ? new HashMap<>(8) : userMapper.selectList(
                    DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName, MpUserEntity::getProfession),
                            userIdList,
                            (lam, pl) -> lam.in(MpUserEntity::getId, pl))
            ).stream().collect(Collectors.toMap(AbstractPrimaryKey::getId, t -> pm.containsKey(t.getProfession()), (t, t1) -> t1, HashMap::new));
            r.setHighLightUserIdList(new ArrayList<>(um.entrySet().stream().filter(t -> Boolean.TRUE.equals(t.getValue())).map(Entry::getKey).collect(Collectors.toList())));
            rl.add(0,
                    rl.stream().reduce(
                            new SchedulingDetailResponse()
                                    .setSchedulingDetailId("-1")
                                    .setDeviceNumber("-1")
                                    .setDeviceNumberFormat("Total")
                            ,
                            (t, t1) -> {
                                CollUtil.addAllIfNotContains(t.getScheduleDayTimeList(), t1.getScheduleDayTimeList());
                                CollUtil.addAllIfNotContains(t.getScheduleMiddleList(), t1.getScheduleMiddleList());
                                CollUtil.addAllIfNotContains(t.getScheduleEveningList(), t1.getScheduleEveningList());
                                CollUtil.addAllIfNotContains(t.getScheduleDayTime12List(), t1.getScheduleDayTime12List());
                                CollUtil.addAllIfNotContains(t.getScheduleEvening12List(), t1.getScheduleEvening12List());
                                CollUtil.addAllIfNotContains(t.getScheduleDayTimeTechnologyGroupList(), t1.getScheduleDayTimeTechnologyGroupList());
                                CollUtil.addAllIfNotContains(t.getScheduleEveningTechnologyGroupList(), t1.getScheduleEveningTechnologyGroupList());
                                t.setScheduleDayTimeFormat("%s".formatted(t.getScheduleDayTimeList().stream().distinct().count()));
                                t.setScheduleMiddleFormat("%s".formatted(t.getScheduleMiddleList().stream().distinct().count()));
                                t.setScheduleEveningFormat("%s".formatted(t.getScheduleEveningList().stream().distinct().count()));
                                t.setScheduleDayTime12Format("%s".formatted(t.getScheduleDayTime12List().stream().distinct().count()));
                                t.setScheduleEvening12Format("%s".formatted(t.getScheduleEvening12List().stream().distinct().count()));
                                // t.setScheduleDayTimeTechnologyGroupFormat("%s".formatted(t.getScheduleDayTimeTechnologyGroupList().stream().distinct().count()));
                                // t.setScheduleEveningTechnologyGroupFormat("%s".formatted(t.getScheduleEveningTechnologyGroupList().stream().distinct().count()));
                                t.setScheduleDayTimeTechnologyGroupFormat("Đặng Đình Chiến 邓庭战");
                                t.setScheduleEveningTechnologyGroupFormat("Nguyễn Văn Việt  阮文越");
                                return t;
                            }
                    )
            );
            final AtomicInteger atomicInteger = new AtomicInteger(1);
            for (SchedulingDetailResponse t : rl) {
                t.setIndex(atomicInteger.getAndAdd(1));
            }
        }
        return new DataResult<>(r);
    }

    /**
     * 更新排班明细
     *
     * @param deviceId 设备id
     * @param request  {@link SchedulingDetailRequest}
     * @return {@link Result}
     */
    @PutMapping("detail")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result updateDetail(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody SchedulingDetailRequest request
    ) {
        final Date now = new Date();
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final SchedulingDetailEntity e = (SchedulingDetailEntity) SCHEDULING_INSTANCE.schedulingDetail(request)
                .setModifier(u.getUserId());
        // update
        if (isNotBlank(e.getId())) {
            if (u.getRoleList().stream().noneMatch(t -> "admin".equals(t.getRoleCode()))) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            if (!schedulingDetailDao.updateById(e)) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            // insert
        }
        return new DataResult<>(e);
    }
}
