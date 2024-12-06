package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.NumberUtil.defaultDecimal;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_OPERATOR_OTHER_NOT_ALLOW;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_ORDER_REPEAT;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_RECORD_DEVICE_TIME_ERROR;
import static com.lead.fund.base.server.mp.converter.IndustryConverter.INDUSTRY_INSTANCE;
import static com.lead.fund.base.server.mp.converter.ReportConverter.REPORT_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.cons.frame.AdminUser;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.response.DataListResult;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.ListResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.NumberUtil;
import com.lead.fund.base.server.mp.dao.MaterialDao;
import com.lead.fund.base.server.mp.dao.MaterialDetailDao;
import com.lead.fund.base.server.mp.dao.OrderDao;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.ReportPhotoDao;
import com.lead.fund.base.server.mp.dao.ReportSerialNoDao;
import com.lead.fund.base.server.mp.dao.TaskDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.DeviceEntity;
import com.lead.fund.base.server.mp.entity.douson.OrderEntity;
import com.lead.fund.base.server.mp.entity.douson.ProductEntity;
import com.lead.fund.base.server.mp.entity.douson.ReportEntity;
import com.lead.fund.base.server.mp.entity.douson.ReportPhotoEntity;
import com.lead.fund.base.server.mp.entity.douson.ReportSerialNoEntity;
import com.lead.fund.base.server.mp.entity.douson.ReportUserEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.MaterialDetailMapper;
import com.lead.fund.base.server.mp.mapper.douson.MaterialMapper;
import com.lead.fund.base.server.mp.mapper.douson.OrderMapper;
import com.lead.fund.base.server.mp.mapper.douson.ProductMapper;
import com.lead.fund.base.server.mp.mapper.douson.ReportMapper;
import com.lead.fund.base.server.mp.mapper.douson.ReportPhotoMapper;
import com.lead.fund.base.server.mp.mapper.douson.ReportSerialNoMapper;
import com.lead.fund.base.server.mp.request.ProductRequest;
import com.lead.fund.base.server.mp.request.ReportQueryRequest;
import com.lead.fund.base.server.mp.request.ReportRequest;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.ReportResponse;
import com.lead.fund.base.server.mp.response.ReportSummaryAccountResponse;
import com.lead.fund.base.server.mp.response.ReportSummaryDeviceResponse;
import com.lead.fund.base.server.mp.response.ReportSummaryResponse;
import com.lead.fund.base.server.mp.response.UserDeviceResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BinaryOperator;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DousonReportController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused", "ResultOfMethodCallIgnored"})
@RestController
@RequestMapping("/douson/report")
@Slf4j
@Validated
public class DousonReportController {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private TemplatePhotoDao templatePhotoDao;
    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private TaskDao taskDao;
    @Resource
    private MaterialDao materialDao;
    @Resource
    private MaterialDetailMapper materialDetailMapper;
    @Resource
    private MaterialDetailDao materialDetailDao;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private ParamDao paramDao;
    @Resource
    private LockHelper lockHelper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDao orderDao;
    @Resource
    private ReportMapper reportMapper;
    @Resource
    private ReportPhotoMapper reportPhotoMapper;
    @Resource
    private ReportPhotoDao reportPhotoDao;
    @Resource
    private ReportSerialNoMapper reportSerialNoMapper;
    @Resource
    private ReportSerialNoDao reportSerialNoDao;
    @Resource
    private UrlHelper urlHelper;

    private ReportEntity mergeRelevance(ReportRequest request, ReportEntity e) {
        reportSerialNoMapper.delete(new LambdaUpdateWrapper<ReportSerialNoEntity>().eq(ReportSerialNoEntity::getReportId, e.getId()));
        try {
            reportSerialNoDao.saveBatch(request.getSerialNoList().stream().map(t -> new ReportSerialNoEntity()
                    .setReportId(e.getId())
                    .setSerialNo(t)
                    .setOrderId(e.getOrderId())
            ).collect(Collectors.toList()));
        } catch (Exception ex) {
            throw new BusinessException(MP_ORDER_REPEAT);
        }
        reportPhotoMapper.delete(new LambdaUpdateWrapper<ReportPhotoEntity>().eq(ReportPhotoEntity::getReportId, e.getId()));
        reportPhotoDao.saveBatch(request.getPhotoList().stream().map(t -> new ReportPhotoEntity().setReportId(e.getId()).setPhotoUrl(t.getPhotoUrl()).setPhotoCompressUrl(t.getPhotoCompressUrl())).collect(Collectors.toList()));
        orderMapper.update(
                null,
                new LambdaUpdateWrapper<OrderEntity>()
                        .set(OrderEntity::getActualCompleteCount, reportMapper.selectList(new LambdaQueryWrapper<ReportEntity>()
                                        .eq(ReportEntity::getOrderId, e.getOrderId())
                                        .select(ReportEntity::getActualCompleteCount)
                                ).stream().map(ReportEntity::getActualCompleteCount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                        .eq(OrderEntity::getId, e.getOrderId())
        );
        reportMapper.update(
                null,
                new LambdaUpdateWrapper<ReportEntity>()
                        .set(ReportEntity::getWorkMinute, defaultDecimal(request.getWorkMinute(), new BigDecimal("435")))
                        .eq(ReportEntity::getReportDate, e.getReportDate())
                        .eq(ReportEntity::getUserId, e.getUserId())
        );
        final ReportUserEntity ru = new ReportUserEntity().setReportDate(e.getReportDate()).setUserId(e.getUserId()).setWorkMinute(request.getWorkMinute());
        return e;
    }

    /**
     * 保存日报
     *
     * @param deviceId 设备id
     * @param request  {@link ReportRequest}
     * @return {@link Result}
     */
    @PostMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result reportSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ReportRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final ReportEntity e = INDUSTRY_INSTANCE.report(request);
        if (u.getRoleList().stream().noneMatch(t -> AdminUser.ADMIN.getCode().equals(t.getRoleCode()))) {
            e.setUserId(u.getUserId());
        } else if (!u.getUserId().equals(request.getUserId())) {
            throw new BusinessException(MP_OPERATOR_OTHER_NOT_ALLOW);
        }
        final ProductEntity product = productMapper.selectById(request.getProductId());
        final OrderEntity order = orderMapper.selectById(product.getOrderId());
        if (deviceMapper.update(
                null,
                new LambdaUpdateWrapper<DeviceEntity>()
                        .set(DeviceEntity::getRunningHour, request.getDeviceRunningEndHour())
                        .set(DeviceEntity::getRunningMinute, request.getDeviceRunningEndMinute())
                        .eq(DeviceEntity::getId, order.getTestDevice())
                        .eq(DeviceEntity::getRunningHour, request.getDeviceRunningStartHour())
                        .eq(DeviceEntity::getRunningMinute, request.getDeviceRunningStartMinute())
        ) <= 0) {
            throw new BusinessException(MP_RECORD_DEVICE_TIME_ERROR);
        }
        reportMapper.insert(
                (ReportEntity) e
                        .setOrderId(orderDao.mergeRelevance(INDUSTRY_INSTANCE.reportToOrder(request)).getId())
                        .setClampingMinute(product.getClampingMinute()).setAssistMinute(product.getAssistMinute()).setRunningMinute(product.getRunningMinute())
                        .setCreator(u.getUserId())
                        .setModifier(u.getUserId())
        );
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 修改日报
     *
     * @param deviceId 设备id
     * @param request  {@link ProductRequest}
     * @return {@link Result}
     */
    @PutMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result reportUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ReportRequest request
    ) {
        final MpUserResponse u = accountHelper.checkUserAdminOrSelf(deviceId, request.getUserId());
        final ReportEntity e = INDUSTRY_INSTANCE.report(request);
        reportMapper.updateById(
                (ReportEntity) e
                        .setOrderId(orderDao.mergeRelevance(INDUSTRY_INSTANCE.reportToOrder(request)).getId())
                        .setModifier(u.getUserId())
        );
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 删除日报
     *
     * @param deviceId 设备id
     * @param request  {@link ProductRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result reportDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ReportRequest request
    ) {
        LambdaUpdateWrapper<ReportEntity> lambda = new LambdaUpdateWrapper<ReportEntity>().eq(ReportEntity::getId, request.getReportId());
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            lambda.eq(ReportEntity::getCreator, u.getUserId());
        }
        if (isNotBlank(request.getReportId())) {
            ReportEntity e = reportMapper.selectById(request.getReportId());
            if (reportMapper.delete(lambda) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            reportPhotoMapper.delete(new LambdaUpdateWrapper<ReportPhotoEntity>().eq(ReportPhotoEntity::getReportId, request.getReportId()));
            reportSerialNoMapper.delete(new LambdaUpdateWrapper<ReportSerialNoEntity>().eq(ReportSerialNoEntity::getReportId, request.getReportId()));
            orderMapper.update(
                    null,
                    new LambdaUpdateWrapper<OrderEntity>()
                            .setSql("ACTUAL_COMPLETE_COUNT = ACTUAL_COMPLETE_COUNT - " + e.getActualCompleteCount())
                            .eq(OrderEntity::getId, e.getOrderId())
            );
        }
        return new Result();
    }

    private List<ReportEntity> reportList(ReportQueryRequest request) {
        LambdaQueryWrapper<ReportEntity> lambda = new LambdaQueryWrapper<>();
        if (null != request.getStartReportDate()) {
            lambda.ge(ReportEntity::getReportDate, DateUtil.day(request.getStartReportDate()));
        }
        if (null != request.getEndReportDate()) {
            lambda.le(ReportEntity::getReportDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getEndReportDate())));
        }
        if (isNotBlank(request.getReportId())) {
            lambda.eq(ReportEntity::getId, request.getReportId());
        }
        if (null != request.getReportDate()) {
            lambda.eq(ReportEntity::getReportDate, request.getReportDate());
        }
        if (isNotBlank(request.getProductId())) {
            lambda.eq(ReportEntity::getProductId, request.getProductId());
        }
        if (isNotBlank(request.getUserId())) {
            lambda.eq(ReportEntity::getUserId, request.getUserId());
        }
        if (CollUtil.isNotEmpty(request.getOrderIdList())) {
            DatabaseUtil.or(lambda, request.getOrderIdList(), (lam, l) -> lam.in(ReportEntity::getOrderId, l));
        }
        if (CollUtil.isNotEmpty(request.getProductIdList())) {
            DatabaseUtil.or(lambda, request.getProductIdList(), (lam, l) -> lam.in(ReportEntity::getProductId, l));
        }
        if (isNotBlank(request.getProcessType())) {
            lambda.eq(ReportEntity::getProcessType, request.getProcessType());
        }
        if (isNotBlank(request.getUsername())) {
            final List<String> userIdList = userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().like(MpUserEntity::getUsername, request.getUsername()))
                    .stream().map(AbstractPrimaryKey::getId)
                    .distinct().toList();
            if (CollUtil.isNotEmpty(userIdList)) {
                DatabaseUtil.or(lambda, userIdList, (lam, l) -> lam.in(ReportEntity::getUserId, l));
            } else {
                return new ArrayList<>();
            }
        }
        if (null != request.getValid()) {
            lambda.eq(ReportEntity::getValid, request.getValid());
        }
        return reportMapper.selectList(lambda
                .orderByDesc(ReportEntity::getReportDate)
                .orderByAsc(ReportEntity::getUserId)
                .orderByAsc(ReportEntity::getOrderId)
                .orderByAsc(ReportEntity::getProductId)
        );
    }

    private List<ReportResponse> formatReportList(List<ReportEntity> l) {
        return formatReportList(l, false);
    }

    private List<ReportResponse> formatReportList(List<ReportEntity> l, boolean pure) {
        if (CollUtil.isEmpty(l)) {
            return new ArrayList<>();
        }
        final List<ReportResponse> list = INDUSTRY_INSTANCE.reportList(l).stream().peek(t -> {
            t.setCurrentDeviceUseMinute((t.getDeviceRunningEndHour() - t.getDeviceRunningStartHour()) * 60 + (t.getDeviceRunningEndMinute() - t.getDeviceRunningStartMinute()));
        }).collect(Collectors.toList());
        // 用户
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getUserId,
                l1 -> userMapper.selectList(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .in(MpUserEntity::getId, l1)
                ),
                (r, t) -> r.getUserId().equals(t.getId()),
                (r, t) -> r.setUserId(t.getId())
                        .setUsername(t.getUsername())
                        .setUserFormat(t.getName())
        );
        // 产品
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getProductId,
                l1 -> productMapper.selectList(
                        new LambdaQueryWrapper<ProductEntity>()
                                .in(ProductEntity::getId, l1)
                ),
                (r, t) -> r.getProductId().equals(t.getId()),
                INDUSTRY_INSTANCE::formatReport
        );
        // 订单
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getOrderId,
                l1 -> orderMapper.selectList(
                        new LambdaQueryWrapper<OrderEntity>()
                                .in(OrderEntity::getId, l1)
                ),
                (r, t) -> r.getOrderId().equals(t.getId()),
                INDUSTRY_INSTANCE::formatReport
        );
        // 设备
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getTestDevice,
                l1 -> deviceMapper.selectList(
                        new LambdaQueryWrapper<DeviceEntity>()
                                .in(DeviceEntity::getId, l1)
                ),
                (r, t) -> defaultIfBlank(r.getTestDevice()).equals(t.getId()),
                INDUSTRY_INSTANCE::formatReport
        );
        if (!pure) {
            // 照片
            MultitaskUtil.supplementList(
                    list,
                    ReportResponse::getReportId,
                    l1 -> reportPhotoMapper.selectList(
                            new LambdaQueryWrapper<ReportPhotoEntity>()
                                    .in(ReportPhotoEntity::getReportId, l1)
                    ),
                    (r, t) -> r.getReportId().equals(t.getReportId()),
                    (r, t) -> INDUSTRY_INSTANCE.reportPhoto(r, t, urlHelper.getUrlPrefix())
            );
            // 序列号
            MultitaskUtil.supplementList(
                    list,
                    ReportResponse::getReportId,
                    l1 -> reportSerialNoMapper.selectList(
                            new LambdaQueryWrapper<ReportSerialNoEntity>()
                                    .in(ReportSerialNoEntity::getReportId, l1)
                    ),
                    (t, r) -> t.getReportId().equals(r.getReportId()),
                    (t, r) -> t.getSerialNoList().add(r.getSerialNo())
            );
            // 加工工序
            MultitaskUtil.supplementList(
                    list,
                    ReportResponse::getProcessProcedure,
                    l1 -> paramDao.listByCategoryId("processProcedure"),
                    (t, r) -> defaultIfBlank(t.getProcessProcedure()).equals(r.getValue()),
                    (t, r) -> t.setProcessProcedureFormat(r.getLabel())
            );
            // 加工类型
            MultitaskUtil.supplementList(
                    list,
                    ReportResponse::getProcessType,
                    l1 -> paramDao.listByCategoryId("processType"),
                    (t, r) -> defaultIfBlank(t.getProcessType()).equals(r.getValue()),
                    (t, r) -> t.setProcessTypeFormat(r.getLabel())
            );
            // 班次
            MultitaskUtil.supplementList(
                    list,
                    ReportResponse::getSchedule,
                    l1 -> paramDao.listByCategoryId("schedule"),
                    (t, r) -> defaultIfBlank(t.getSchedule()).equals(r.getValue()),
                    (t, r) -> t.setScheduleFormat(r.getLabel())
            );
            // 停机内容
            MultitaskUtil.supplementList(
                    list,
                    ReportResponse::getStopWorkingContent1,
                    l1 -> paramDao.listByCategoryId("stopWorkingContent1"),
                    (t, r) -> defaultIfBlank(t.getStopWorkingContent1()).equals(r.getValue()),
                    (t, r) -> t.setStopWorkingContent1Format(r.getLabel())
            );
            MultitaskUtil.supplementList(
                    list,
                    ReportResponse::getStopWorkingContent2,
                    l1 -> paramDao.listByCategoryId("stopWorkingContent2"),
                    (t, r) -> defaultIfBlank(t.getStopWorkingContent2()).equals(r.getValue()),
                    (t, r) -> t.setStopWorkingContent2Format(r.getLabel())
            );
            MultitaskUtil.supplementList(
                    list,
                    ReportResponse::getStopWorkingContent3,
                    l1 -> paramDao.listByCategoryId("stopWorkingContent3"),
                    (t, r) -> defaultIfBlank(t.getStopWorkingContent3()).equals(r.getValue()),
                    (t, r) -> t.setStopWorkingContent3Format(r.getLabel())
            );
        }
        final Map<List<String>, List<UserDeviceResponse>> dayUserDeviceMap = list.stream()
                .filter(t -> isNotBlank(t.getReportDate()))
                .filter(t -> isNotBlank(t.getUserId()))
                .filter(t -> isNotBlank(t.getTestDevice()))
                .collect(
                        Collectors.groupingBy(
                                t -> CollUtil.toList(t.getReportDate(), t.getUserId(), t.getTestDevice()),
                                Collectors.mapping(
                                        t -> INDUSTRY_INSTANCE.userDevice(new UserDeviceResponse(), t),
                                        Collectors.toList()
                                )
                        )
                );
        final Map<List<String>, Date> dayUserTimeMap = list.stream()
                .filter(t -> isNotBlank(t.getReportDate()))
                .filter(t -> isNotBlank(t.getUserId()))
                .filter(t -> null != t.getReportTime())
                .collect(
                        Collectors.groupingBy(
                                t -> CollUtil.toList(t.getReportDate(), t.getUserId()),
                                Collectors.mapping(
                                        ReportResponse::getReportTime,
                                        Collectors.reducing(
                                                new Date(),
                                                (t, t1) -> t.compareTo(t1) > 0 ? t1 : t
                                        )
                                )
                        )
                );
        final BigDecimal thousand = new BigDecimal("1000");
        return list.stream().peek(t -> {
            final List<UserDeviceResponse> userDeviceList = dayUserDeviceMap.getOrDefault(CollUtil.toList(t.getReportDate(), t.getUserId(), t.getTestDevice()), new ArrayList<>());
            t.setUserDeviceList(userDeviceList.stream().sorted((o2, o1) -> {
                int r = o1.getDeviceRunningStartHour().compareTo(o2.getDeviceRunningStartHour());
                if (r != 0) {
                    return r;
                } else {
                    return o1.getDeviceRunningStartMinute().compareTo(o2.getDeviceRunningStartMinute());
                }
            }).collect(Collectors.toList()));
            t.setDeviceUseMinute(userDeviceList.stream().mapToInt(UserDeviceResponse::getDeviceRunningMinute).sum());
            t.setDeviceUsePercent(new BigDecimal(t.getDeviceUseMinute()).divide(new BigDecimal(t.getWorkMinute()), 4, RoundingMode.HALF_UP));
            t.setDeviceUsePercentFormat(t.getDeviceUsePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%");
            t.setDeviceCompletePercent(t.getCompleteMinute().add(new BigDecimal(t.getLeaderSubsidyMinute())).divide(java.math.BigDecimal.valueOf(t.getWorkMinute()), 4, java.math.RoundingMode.HALF_UP));
            t.setDeviceCompletePercentFormat(t.getDeviceCompletePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%");
            t.setSalary((t.getCompleteMinute().add(new BigDecimal(t.getLeaderSubsidyMinute()))).multiply(t.getDeviceUnitPrice()).divide(new BigDecimal("60").multiply(thousand), 0, RoundingMode.HALF_UP).multiply(thousand));
            t.setSalaryFormat(NumberUtil.formatIntTh(t.getSalary()));
        }).sorted((o2, o1) -> {
            int r = o1.getReportDate().compareTo(o2.getReportDate());
            if (r != 0) {
                return r;
            } else {
                Date reportTime1 = dayUserTimeMap.getOrDefault(CollUtil.toList(o1.getReportDate(), o1.getUserId()), new Date());
                Date reportTime2 = dayUserTimeMap.getOrDefault(CollUtil.toList(o2.getReportDate(), o2.getUserId()), new Date());
                int r1 = reportTime1.compareTo(reportTime2);
                if (r1 != 0) {
                    return r1;
                } else {
                    int r2 = o1.getReportTime().compareTo(o2.getReportTime());
                    if (r2 != 0) {
                        return r2;
                    } else {
                        return o1.getTestDevice().compareTo(o2.getTestDevice());
                    }
                }
            }
        }).collect(Collectors.toList());
    }

    /**
     * 日报列表
     *
     * @param deviceId 设备id
     * @param request  {@link ReportQueryRequest}
     * @return {@link DataListResult}
     */
    @GetMapping("list")
    public DataListResult<ReportSummaryResponse, ReportResponse> reportAdminList(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ReportQueryRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> AdminUser.ADMIN.getCode().equals(t.getRoleCode()))) {
            request.setUserId(u.getUserId());
        }
        if (isNotBlank(request.getOrderNo()) || isNotBlank(request.getProcessProcedure()) || isNotBlank(request.getProjectSequence()) || isNotBlank(request.getTestDevice())) {
            LambdaQueryWrapper<OrderEntity> lambda = new LambdaQueryWrapper<>();
            if (isNotBlank(request.getOrderNo())) {
                lambda.like(OrderEntity::getOrderNo, request.getOrderNo());
            }
            if (isNotBlank(request.getProjectSequence())) {
                lambda.like(OrderEntity::getProjectSequence, request.getProjectSequence());
            }
            if (isNotBlank(request.getTestDevice())) {
                lambda.eq(OrderEntity::getTestDevice, request.getTestDevice());
            }
            if (isNotBlank(request.getProcessProcedure())) {
                lambda.eq(OrderEntity::getProcessProcedure, request.getProcessProcedure());
            }
            List<String> orderIdList = orderMapper.selectList(lambda).stream().map(OrderEntity::getId).collect(Collectors.toList());
            if (CollUtil.isEmpty(orderIdList)) {
                return new DataListResult<>(new ReportSummaryResponse(), new ArrayList<>());
            }
            request.setOrderIdList(orderIdList);
        }
        if (isNotBlank(request.getDesignNumber())) {
            LambdaQueryWrapper<ProductEntity> lambda = new LambdaQueryWrapper<>();
            if (isNotBlank(request.getDesignNumber())) {
                lambda.like(ProductEntity::getDesignNumber, request.getDesignNumber());
            }
            List<String> productIdList = productMapper.selectList(lambda).stream().map(ProductEntity::getId).collect(Collectors.toList());
            if (CollUtil.isEmpty(productIdList)) {
                return new DataListResult<>(new ReportSummaryResponse(), new ArrayList<>());
            }
            request.setProductIdList(productIdList);
        }
        List<ReportResponse> rl = formatReportList(reportList(request))
                .stream().filter(t -> {
                    if (null != request.getSurplusCountType()) {
                        if (request.getSurplusCountType() == 0) {
                            return t.getSurplusCount().compareTo(BigDecimal.ZERO) == 0;
                        } else if (request.getSurplusCountType() == 2) {
                            return t.getSurplusCount().compareTo(BigDecimal.ZERO) != 0;
                        } else if (request.getSurplusCountType() < 0) {
                            return t.getSurplusCount().compareTo(BigDecimal.ZERO) < 0;
                        } else {
                            return t.getSurplusCount().compareTo(BigDecimal.ZERO) > 0;
                        }
                    }
                    return true;
                }).collect(Collectors.toList());
        BigDecimal totalSalary = rl.stream().map(ReportResponse::getSalary).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DataListResult<>(
                new ReportSummaryResponse()
                        .setTotalSalary(totalSalary)
                        .setTotalSalaryFormat(NumberUtil.formatIntTh(totalSalary))
                ,
                rl
        );
    }

    /**
     * 用户汇总列表
     *
     * @param deviceId 设备id
     * @param request  {@link ReportQueryRequest}
     * @return {@link ReportSummaryAccountResponse}
     */
    @SuppressWarnings({"OptionalGetWithoutIsPresent", "SimplifyStreamApiCallChains"})
    @GetMapping("summary/account")
    public ListResult<ReportSummaryAccountResponse> reportSummaryAccount(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ReportQueryRequest request
    ) {
        final List<ReportResponse> rl = formatReportList(reportList(request), true);
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final BinaryOperator<ReportSummaryAccountResponse> bi = (t, t1) -> new ReportSummaryAccountResponse()
                .setReportDateList((java.util.Set<String>) CollUtil.addAll(CollUtil.newHashSet(t.getReportDateList()), t1.getReportDateList()))
                .setReportDateCount(CollUtil.addAll(CollUtil.newHashSet(t.getReportDateList()), t1.getReportDateList()).size())
                .setUserId(defaultIfBlank(t.getUserId(), t1.getUserId()))
                .setUserIdFormat(defaultIfBlank(t.getUserIdFormat(), t1.getUserIdFormat()))
                .setDeviceId(defaultIfBlank(t.getDeviceId(), t1.getDeviceId()))
                .setDeviceIdFormat(defaultIfBlank(t.getDeviceIdFormat(), t1.getDeviceIdFormat()))
                .setTotalCount(t.getTotalCount() + t1.getTotalCount())
                .setSumDeviceCompletePercent(t.getSumDeviceCompletePercent().add(t1.getSumDeviceCompletePercent()))
                .setDeviceCompletePercent(t.getSumDeviceCompletePercent().add(t1.getSumDeviceCompletePercent()).divide(new BigDecimal(CollUtil.addAll(CollUtil.newHashSet(t.getReportDateList()), t1.getReportDateList()).size()), 8, RoundingMode.HALF_UP))
                .setSalary(t.getSalary().add(t1.getSalary()));
        final Map<String, List<ReportSummaryAccountResponse>> userDeviceListMap = rl.stream()
                .map(REPORT_INSTANCE::summaryAccount)
                .collect(Collectors.groupingBy(
                        t -> CollUtil.toList(t.getUserId(), t.getDeviceId()),
                        Collectors.reducing(bi)
                ))
                .values()
                .stream()
                .map(Optional::get)
                .collect(Collectors.groupingBy(ReportSummaryAccountResponse::getUserId));
        final List<ReportSummaryAccountResponse> list = rl.stream()
                .map(REPORT_INSTANCE::summaryAccount)
                .collect(Collectors.groupingBy(
                        ReportSummaryAccountResponse::getUserId,
                        Collectors.reducing(bi)
                ))
                .values()
                .stream()
                .map(Optional::get)
                .sorted((o1, o2) -> o2.getDeviceCompletePercent().compareTo(o1.getDeviceCompletePercent()))
                .peek(t -> t
                        .setIndex(atomicInteger.addAndGet(1))
                )
                .collect(Collectors.toList());
        final List<ReportSummaryAccountResponse> userDeviceList = list
                .stream()
                .flatMap(t -> isBlank(t.getUserId()) ? Stream.of(t) : userDeviceListMap.getOrDefault(t.getUserId(), new ArrayList<>()).stream().peek(t1 -> {
                                    t1
                                            .setDeviceSumDeviceCompletePercent(t1.getSumDeviceCompletePercent())
                                            .setDeviceDeviceCompletePercent(t1.getDeviceCompletePercent())
                                            .setDeviceSalary(t1.getSalary())
                                            .setDeviceReportDateList(t1.getReportDateList())
                                            .setDeviceReportDateCount(t1.getReportDateList().size())
                                            .setDeviceTotalCount(t1.getTotalCount())

                                            .setSumDeviceCompletePercent(t.getSumDeviceCompletePercent())
                                            .setDeviceCompletePercent(t.getDeviceCompletePercent())
                                            .setSalary(t.getSalary())
                                            .setReportDateList(t.getReportDateList())
                                            .setReportDateCount(t.getReportDateList().size())
                                            .setTotalCount(t.getTotalCount())
                                            .setIndex(t.getIndex())
                                    ;
                                })
                                .sorted((o1, o2) -> o2.getDeviceDeviceCompletePercent().compareTo(o1.getDeviceDeviceCompletePercent()))
                )
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(list) && CollUtil.isNotEmpty(userDeviceList)) {
            final ReportSummaryAccountResponse summary = new ReportSummaryAccountResponse()
                    .setUserId("")
                    .setUserIdFormat("总计");
            list.stream().reduce(
                    (t, t1) -> summary
                            .setSumDeviceCompletePercent(t.getSumDeviceCompletePercent().add(t1.getSumDeviceCompletePercent()))
                            .setReportDateList((java.util.Set<String>) CollUtil.addAll(CollUtil.newHashSet(t.getReportDateList()), t1.getReportDateList()))
                            .setReportDateCount(summary.getReportDateList().size())
                            .setTotalCount(t.getTotalCount() + t1.getTotalCount())
                            .setSalary(t.getSalary().add(t1.getSalary()))
                            // 先累计
                            .setDeviceCompletePercent(t.getDeviceCompletePercent().add(t1.getDeviceCompletePercent()))
            );
            userDeviceList.stream()
                    .reduce(
                            (t, t1) -> summary
                                    .setDeviceSumDeviceCompletePercent(t.getSumDeviceCompletePercent().add(t1.getSumDeviceCompletePercent()))
                                    .setDeviceReportDateList((java.util.Set<String>) CollUtil.addAll(CollUtil.newHashSet(t.getDeviceReportDateList()), t1.getDeviceReportDateList()))
                                    .setDeviceReportDateCount(summary.getDeviceReportDateList().size())
                                    .setDeviceTotalCount(t.getDeviceTotalCount() + t1.getDeviceTotalCount())
                                    .setDeviceSalary(t.getDeviceSalary().add(t1.getDeviceSalary()))
                                    // 先累计
                                    .setDeviceDeviceCompletePercent(t.getDeviceDeviceCompletePercent().add(t1.getDeviceDeviceCompletePercent()))
                    );
            summary
                    // 在平均
                    .setDeviceCompletePercent(summary.getDeviceCompletePercent().divide(new BigDecimal(list.size()), 8, RoundingMode.HALF_UP))
                    .setDeviceDeviceCompletePercent(summary.getDeviceDeviceCompletePercent().divide(new BigDecimal(userDeviceList.size()), 8, RoundingMode.HALF_UP))
                    .setIndex(atomicInteger.addAndGet(1));
            userDeviceList.add(summary);
        }
        return new ListResult<>(
                userDeviceList
                        .stream()
                        .peek(t -> t
                                .setSalaryFormat(NumberUtil.formatIntTh(t.getSalary()))
                                .setSumDeviceCompletePercentFormat(t.getSumDeviceCompletePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                                .setDeviceCompletePercentFormat(t.getDeviceCompletePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                                .setDeviceSalaryFormat(NumberUtil.formatIntTh(t.getDeviceSalary()))
                                .setDeviceSumDeviceCompletePercentFormat(t.getDeviceSumDeviceCompletePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                                .setDeviceDeviceCompletePercentFormat(t.getDeviceDeviceCompletePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                        )
                        .collect(Collectors.toList())
        );
    }

    /**
     * 设备汇总列表
     *
     * @param deviceId 设备id
     * @param request  {@link ReportQueryRequest}
     * @return {@link ReportSummaryDeviceResponse}
     */
    @SuppressWarnings({"OptionalGetWithoutIsPresent", "SimplifyStreamApiCallChains"})
    @GetMapping("summary/device")
    public ListResult<ReportSummaryDeviceResponse> reportSummaryDevice(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ReportQueryRequest request
    ) {
        final List<ReportResponse> rl = formatReportList(reportList(request), true)
                .stream().filter(t -> isBlank(request.getTestDevice()) || request.getTestDevice().equals(t.getDeviceId())).collect(Collectors.toList());
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final BinaryOperator<ReportSummaryDeviceResponse> bi = (t, t1) -> {
            final ReportSummaryDeviceResponse r = new ReportSummaryDeviceResponse()
                    .setReportDateList((java.util.Set<String>) CollUtil.addAll(CollUtil.newHashSet(t.getReportDateList()), t1.getReportDateList()))
                    .setUserIdList((java.util.Set<String>) CollUtil.addAll(CollUtil.newHashSet(t.getUserIdList()), t1.getUserIdList()))
                    .setDesignNumberList((java.util.Set<String>) CollUtil.addAll(CollUtil.newHashSet(t.getDesignNumberList()), t1.getDesignNumberList()))
                    .setDeviceId(defaultIfBlank(t.getDeviceId(), t1.getDeviceId()))
                    .setDeviceIdFormat(defaultIfBlank(t.getDeviceIdFormat(), t1.getDeviceIdFormat()))
                    .setUserId(defaultIfBlank(t.getUserId(), t1.getUserId()))
                    .setUserIdFormat(defaultIfBlank(t.getUserIdFormat(), t1.getUserIdFormat()))
                    .setTotalCount(t.getTotalCount() + t1.getTotalCount())
                    .setSumDeviceCompletePercent(t.getSumDeviceCompletePercent().add(t1.getSumDeviceCompletePercent()))
                    .setSumDeviceUsePercent(t.getReportDateList().containsAll(t1.getReportDateList()) && t.getUserIdList().containsAll(t1.getUserIdList()) ? t.getSumDeviceUsePercent() : t.getSumDeviceUsePercent().add(t1.getSumDeviceUsePercent()));
            r
                    .setReportDateCount(r.getReportDateList().size())
                    .setUserIdCount(r.getUserIdList().size())
                    .setDesignNumberCount(r.getDesignNumberList().size())
                    .setDeviceCompletePercent(r.getSumDeviceCompletePercent().divide(new BigDecimal(r.getReportDateCount()), 8, RoundingMode.HALF_UP))
                    .setDeviceUsePercent(r.getSumDeviceUsePercent().divide(new BigDecimal(r.getReportDateCount()), 8, RoundingMode.HALF_UP))
            ;
            return r;
        };
        final Map<String, List<ReportSummaryDeviceResponse>> deviceUserListMap = rl.stream()
                .map(REPORT_INSTANCE::summaryDevice)
                .collect(Collectors.groupingBy(
                        t -> CollUtil.toList(t.getUserId(), t.getDeviceId()),
                        Collectors.reducing(bi)
                ))
                .values()
                .stream()
                .map(Optional::get)
                .collect(Collectors.groupingBy(ReportSummaryDeviceResponse::getDeviceId));
        final List<ReportSummaryDeviceResponse> list = rl.stream()
                .map(REPORT_INSTANCE::summaryDevice)
                .collect(Collectors.groupingBy(
                        ReportSummaryDeviceResponse::getDeviceId,
                        Collectors.reducing(bi)
                ))
                .values()
                .stream()
                .map(Optional::get)
                .sorted((o1, o2) -> o2.getDeviceCompletePercent().compareTo(o1.getDeviceCompletePercent()))
                .peek(t -> t
                        .setIndex(atomicInteger.addAndGet(1))
                )
                .collect(Collectors.toList());
        final List<ReportSummaryDeviceResponse> deviceUserList = list
                .stream()
                .flatMap(t -> {
                            if (isBlank(t.getDeviceId())) {
                                return Stream.of(t);
                            }
                            final List<ReportSummaryDeviceResponse> dl = deviceUserListMap.getOrDefault(t.getDeviceId(), new ArrayList<>());
                            dl.stream().map(tt -> new BigDecimal[]{
                                    tt.getDeviceCompletePercent(),
                                    tt.getDeviceUsePercent()
                            }).reduce((tt, tt1) -> new BigDecimal[]{
                                    BigDecimal.ZERO.add(tt[0]).add(tt1[0]),
                                    BigDecimal.ZERO.add(tt[1]).add(tt1[1])
                            }).ifPresent(tt -> {
                                t
                                        .setDeviceCompletePercent(tt[0].divide(new BigDecimal(dl.size()), 8, RoundingMode.HALF_UP))
                                        .setDeviceUsePercent(tt[1].divide(new BigDecimal(dl.size()), 8, RoundingMode.HALF_UP));
                            });
                            return dl.stream().peek(t1 -> {
                                        t1
                                                .setDeviceReportDateList(t1.getReportDateList())
                                                .setDeviceReportDateCount(t1.getReportDateCount())
                                                .setDeviceDeviceCompletePercent(t1.getDeviceCompletePercent())
                                                .setDeviceSumDeviceCompletePercent(t1.getSumDeviceCompletePercent())
                                                .setDeviceSumDeviceUsePercent(t1.getSumDeviceUsePercent())
                                                .setDeviceDeviceUsePercent(t1.getDeviceUsePercent())
                                                .setDevicePercentDiff(t1.getPercentDiff())
                                                .setIndex(t.getIndex())
                                                .setReportDateList(t.getReportDateList())
                                                .setReportDateCount(t.getReportDateCount())
                                                .setUserIdList(t.getUserIdList())
                                                .setUserIdCount(t.getUserIdCount())
                                                .setTotalCount(t.getTotalCount())
                                                .setSumDeviceCompletePercent(t.getSumDeviceCompletePercent())
                                                .setSumDeviceUsePercent(t.getSumDeviceUsePercent())
                                                .setDeviceCompletePercent(t.getDeviceCompletePercent())
                                                .setDeviceUsePercent(t.getDeviceUsePercent())
                                                .setDesignNumberList(t.getDesignNumberList())
                                                .setDesignNumberCount(t.getDesignNumberCount())
                                        ;
                                    })
                                    .sorted((o1, o2) -> o2.getDeviceDeviceCompletePercent().compareTo(o1.getDeviceDeviceCompletePercent()))
                                    ;
                        }
                )
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(list) && CollUtil.isNotEmpty(deviceUserList)) {
            final ReportSummaryDeviceResponse summary = new ReportSummaryDeviceResponse()
                    .setDeviceId("")
                    .setDeviceIdFormat("总计");
            list.stream()
                    .reduce(
                            (t, t1) -> summary
                                    .setReportDateList((java.util.Set<String>) CollUtil.addAll(CollUtil.newHashSet(t.getReportDateList()), t1.getReportDateList()))
                                    .setReportDateCount(summary.getReportDateList().size())
                                    .setTotalCount(t.getTotalCount() + t1.getTotalCount())
                                    .setUserIdCount(t.getUserIdCount() + t1.getUserIdCount())
                                    .setSumDeviceUsePercent(t.getSumDeviceUsePercent().add(t1.getSumDeviceUsePercent()))
                                    .setSumDeviceCompletePercent(t.getSumDeviceCompletePercent().add(t1.getSumDeviceCompletePercent()))
                                    // 先累计
                                    .setDeviceUsePercent(t.getDeviceUsePercent().add(t1.getDeviceUsePercent()))
                                    .setDeviceCompletePercent(t.getDeviceCompletePercent().add(t1.getDeviceCompletePercent()))
                    );
            deviceUserList.stream()
                    .reduce(
                            (t, t1) -> summary
                                    .setDeviceReportDateList((java.util.Set<String>) CollUtil.addAll(CollUtil.newHashSet(t.getDeviceReportDateList()), t1.getDeviceReportDateList()))
                                    .setDeviceReportDateCount(summary.getDeviceReportDateList().size())
                                    .setDeviceTotalCount(t.getDeviceTotalCount() + t1.getDeviceTotalCount())
                                    .setUserIdCount(t.getUserIdCount() + t1.getUserIdCount())
                                    .setDeviceSumDeviceUsePercent(t.getDeviceSumDeviceUsePercent().add(t1.getDeviceSumDeviceUsePercent()))
                                    .setDeviceSumDeviceCompletePercent(t.getDeviceSumDeviceCompletePercent().add(t1.getDeviceSumDeviceCompletePercent()))
                                    // 先累计
                                    .setDeviceDeviceUsePercent(t.getDeviceDeviceUsePercent().add(t1.getDeviceDeviceUsePercent()))
                                    .setDeviceDeviceCompletePercent(t.getDeviceDeviceCompletePercent().add(t1.getDeviceDeviceCompletePercent()))
                    );
            deviceUserList.add(summary
                    .setDeviceUsePercent(summary.getDeviceUsePercent().divide(new BigDecimal(atomicInteger.get()), 8, RoundingMode.HALF_UP))
                    .setDeviceCompletePercent(summary.getDeviceCompletePercent().divide(new BigDecimal(atomicInteger.get()), 8, RoundingMode.HALF_UP))
                    .setDeviceDeviceUsePercent(summary.getDeviceDeviceUsePercent().divide(new BigDecimal(atomicInteger.get()), 8, RoundingMode.HALF_UP))
                    .setDeviceDeviceCompletePercent(summary.getDeviceDeviceCompletePercent().divide(new BigDecimal(atomicInteger.get()), 8, RoundingMode.HALF_UP))
                    .setIndex(atomicInteger.addAndGet(1)));
        }
        return new ListResult<>(deviceUserList.stream()
                .peek(t -> t
                        .setSumDeviceCompletePercentFormat(t.getSumDeviceCompletePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                        .setDeviceCompletePercentFormat(t.getDeviceCompletePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                        .setSumDeviceUsePercentFormat(t.getSumDeviceUsePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                        .setDeviceUsePercentFormat(t.getDeviceUsePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                        .setPercentDiff(t.getDeviceCompletePercent().subtract(t.getDeviceUsePercent()))
                        .setPercentDiffFormat(t.getPercentDiff().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                        .setDeviceSumDeviceCompletePercentFormat(t.getDeviceSumDeviceCompletePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                        .setDeviceDeviceCompletePercentFormat(t.getDeviceDeviceCompletePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                        .setDeviceSumDeviceUsePercentFormat(t.getDeviceSumDeviceUsePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                        .setDeviceDeviceUsePercentFormat(t.getDeviceDeviceUsePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                        .setDevicePercentDiff(t.getDeviceDeviceCompletePercent().subtract(t.getDeviceDeviceUsePercent()))
                        .setDevicePercentDiffFormat(t.getDevicePercentDiff().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%")
                )
                .collect(Collectors.toList()));
    }

    /**
     * 查询日报
     *
     * @param deviceId 设备id
     * @param request  {@link ReportQueryRequest}
     * @return {@link DataResult<ReportResponse>}
     */
    @GetMapping("")
    public DataResult<ReportResponse> reportQuery(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ReportQueryRequest request
    ) {
        final String userId = accountHelper.getUser(deviceId).getUserId();
        request.setUserId(userId);
        return new DataResult<>(Optional.ofNullable(
                CollUtil.getFirst(
                        new ArrayList<ReportResponse>()
                        // formatReportList(reportList(request))
                )
        ).orElseGet(() -> CollUtil.getFirst(
                formatReportList(
                        CollUtil.toList(new ReportEntity().setUserId(userId).setProductId(request.getProductId()).setReportDate(DateUtil.day(new Date())))
                )
        )));
    }

}
