package com.lead.fund.base.server.mp.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.cons.frame.AdminRole;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.request.Page;
import com.lead.fund.base.common.basic.response.*;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.server.mp.dao.*;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagEntity;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagPhotoEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.AssemblyAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.AssemblyMapper;
import com.lead.fund.base.server.mp.request.AssemblyRequest;
import com.lead.fund.base.server.mp.request.BoxFlagPageRequest;
import com.lead.fund.base.server.mp.request.BoxFlagRequest;
import com.lead.fund.base.server.mp.response.BoxFlagResponse;
import com.lead.fund.base.server.mp.response.BoxFlagSummaryResponse;
import com.lead.fund.base.server.mp.response.BoxSummaryDescribeResponse;
import com.lead.fund.base.server.mp.response.BoxSummaryResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import jakarta.annotation.Resource;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.BeanUtil.defaultIfNull;
import static com.lead.fund.base.common.util.NumberUtil.defaultDecimal;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.BoxConverter.BOX_INSTANCE;

/**
 * DousonBoxController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused"})
@RestController
@RequestMapping("/douson/box")
@Slf4j
@Validated
public class DousonBoxController {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private TemplatePhotoDao templatePhotoDao;
    @Resource
    private AssemblyMapper assemblyMapper;
    @Resource
    private AssemblyAttachmentMapper assemblyAttachmentMapper;
    @Resource
    private AssemblyAttachmentDao assemblyAttachmentDao;
    @Resource
    private TaskDao taskDao;
    @Resource
    private AssemblyDao assemblyDao;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private ParamDao paramDao;
    @Resource
    private LockHelper lockHelper;
    @Resource
    private UrlHelper urlHelper;
    @Resource
    private BoxFlagDao boxFlagDao;
    @Resource
    private BoxFlagSerialNoDao boxFlagSerialNoDao;
    @Resource
    private BoxFlagPhotoDao boxFlagPhotoDao;

    private void mergeRelevance(BoxFlagRequest request, BoxFlagEntity e) {
        boxFlagPhotoDao.remove(new LambdaUpdateWrapper<BoxFlagPhotoEntity>()
                .eq(BoxFlagPhotoEntity::getBoxFlagId, e.getId()));
        boxFlagPhotoDao.saveBatch(
                CollUtil.defaultIfEmpty(request.getPhotoList(), new ArrayList<>())
                        .stream().map(t -> new BoxFlagPhotoEntity()
                                .setBoxFlagId(e.getId())
                                .setPhotoUrl(t.getPhotoUrl())
                                .setPhotoCompressUrl(t.getPhotoCompressUrl())
                        )
                        .collect(Collectors.toList())
        );
    }

    /**
     * 保存装箱标识（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link BoxFlagRequest}
     * @return {@link Result}
     */
    @PostMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result boxFlagAdminSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody BoxFlagRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final List<String> matchRoleCodeList = CollUtil.toList(
                AdminRole.ADMIN.getCode(),
                "box",
                "boxManager"
        );
        if (u.getRoleCodeList().stream().noneMatch(matchRoleCodeList::contains)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }

        BoxFlagEntity e = (BoxFlagEntity) BOX_INSTANCE.boxFlag(request)
                .setOrderNo(boxFlagDao.nextOrderNo())
                .setBoxNumber(boxFlagDao.nextBoxNumber(request.getSaleOrderNo(), request.getOrderProject()))
                .setSendDate(defaultDecimal(request.getSendCount()).compareTo(BigDecimal.ZERO) > 0 ? defaultIfBlank(request.getSendDate(), DateUtil.day(new Date())) : "")
                .setCreator(u.getUserId())
                .setModifier(u.getUserId());
        boxFlagDao.save(e);
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    /**
     * 修改装箱标识（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link BoxFlagRequest}
     * @return {@link Result}
     */
    @PutMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result boxFlagAdminUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody BoxFlagRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final List<String> masterRoleCodeList = CollUtil.toList(
                AdminRole.ADMIN.getCode(),
                "inspector",
                "boxManager"
        );
        final List<String> matchRoleCodeList = CollUtil.toList(
                "box",
                "boxManager"
        );
        CollUtil.addAll(matchRoleCodeList, masterRoleCodeList);
        if (u.getRoleCodeList().stream().noneMatch(matchRoleCodeList::contains)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        BoxFlagEntity e = (BoxFlagEntity) BOX_INSTANCE.boxFlag(request)
                .setSendDate(defaultDecimal(request.getSendCount()).compareTo(BigDecimal.ZERO) > 0 ? defaultIfBlank(request.getSendDate(), DateUtil.day(new Date())) : "")
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<BoxFlagEntity> lambda = new LambdaUpdateWrapper<BoxFlagEntity>()
                .eq(BoxFlagEntity::getId, request.getBoxFlagId());
        if (u.getRoleCodeList().stream().noneMatch(masterRoleCodeList::contains)) {
            lambda.eq(BoxFlagEntity::getCreator, u.getUserId());
        }
        if (!boxFlagDao.update(
                e,
                lambda
        )) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    /**
     * 删除装箱标识（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link BoxFlagRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result boxFlagAdminDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute BoxFlagRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        if (isNotBlank(request.getBoxFlagId())) {
            boxFlagDao.removeById(request.getBoxFlagId());
            boxFlagPhotoDao.removeById(request.getBoxFlagId());
            boxFlagSerialNoDao.removeById(request.getBoxFlagId());
        }
        return new Result();
    }

    private List<BoxFlagEntity> boxFlagList(BoxFlagRequest request) {
        return boxFlagList(request, null);
    }

    private List<BoxFlagEntity> boxFlagList(BoxFlagRequest request, Consumer<LambdaQueryWrapper<BoxFlagEntity>> consumer) {
        LambdaQueryWrapper<BoxFlagEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getCreator())) {
            lambda.eq(BoxFlagEntity::getCreator, request.getCreator());
        }
        if (isNotBlank(request.getOrderNo())) {
            lambda.like(BoxFlagEntity::getOrderNo, request.getOrderNo());
        }
        if (isNotBlank(request.getSaleOrderNo())) {
            lambda.like(BoxFlagEntity::getSaleOrderNo, request.getSaleOrderNo());
        }
        if (isNotBlank(request.getOrderProject())) {
            lambda.like(BoxFlagEntity::getOrderProject, request.getOrderProject());
        }
        if (isNotBlank(request.getCustomerShortName())) {
            lambda.eq(BoxFlagEntity::getCustomerShortName, request.getCustomerShortName());
        }
        if (isNotBlank(request.getPurchaseOrderNo())) {
            lambda.like(BoxFlagEntity::getPurchaseOrderNo, request.getPurchaseOrderNo());
        }
        if (isNotBlank(request.getMaterialNo())) {
            lambda.like(BoxFlagEntity::getMaterialNo, request.getMaterialNo());
        }
        if (isNotBlank(request.getPoProject())) {
            lambda.like(BoxFlagEntity::getPoProject, request.getPoProject());
        }
        if (null != request.getStartDate()) {
            lambda.ge(BoxFlagEntity::getDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(request.getStartDate())));
        }
        if (null != request.getEndDate()) {
            lambda.le(BoxFlagEntity::getDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getEndDate())));
        }
        if (null != request.getAlreadySend()) {
            if (request.getAlreadySend()) {
                lambda.gt(BoxFlagEntity::getSendCount, 0);
            } else {
                lambda.and(
                        lam -> lam.isNull(BoxFlagEntity::getSendCount)
                                .or()
                                .le(BoxFlagEntity::getSendCount, 0)
                );
            }
        }
        lambda.orderByDesc(BoxFlagEntity::getCreateTime);
        if (null != consumer) {
            consumer.accept(lambda);
        }
        return boxFlagDao.list(lambda);
    }

    private List<BoxFlagResponse> formatBoxFlagList(List<BoxFlagEntity> list) {
        List<BoxFlagResponse> rl = BOX_INSTANCE.boxFlagList(list);
        MultitaskUtil.supplementList(
                rl,
                BoxFlagResponse::getBoxFlagId,
                l -> boxFlagPhotoDao.list(new LambdaQueryWrapper<BoxFlagPhotoEntity>().in(BoxFlagPhotoEntity::getBoxFlagId, l)),
                (t, r) -> t.getBoxFlagId().equals(r.getBoxFlagId()),
                (t, r) -> t.getPhotoList().add(BOX_INSTANCE.photo(r, urlHelper.getUrlPrefix()))
        );
        MultitaskUtil.supplementList(
                rl,
                BoxFlagResponse::getCreator,
                l -> userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().in(MpUserEntity::getId, l)),
                (t, r) -> t.getCreator().equals(r.getId()),
                (t, r) -> t.setCreatorFormat(r.getUsername())
        );
        MultitaskUtil.supplementList(
                rl,
                BoxFlagResponse::getCustomerShortName,
                l -> paramDao.listByCategoryId("customerShortName"),
                (t, r) -> t.getCustomerShortName().equals(r.getValue()),
                (t, r) -> t.setCustomerShortNameFormat(r.getLabel())
        );
        for (BoxFlagResponse t : rl) {
            t.setPhotoCount(t.getPhotoList().size());
        }
        return rl;
    }

    /**
     * 装箱标识分页（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link BoxFlagPageRequest}
     * @return {@link PageDataResult}
     */
    @GetMapping("page")
    public PageDataResult<BoxFlagResponse, BoxFlagSummaryResponse> boxFlagAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute BoxFlagPageRequest request
    ) {
        accountHelper.getUser(deviceId);
        final PageResult<BoxFlagEntity> pr = DatabaseUtil.page(request, this::boxFlagList);
        final List<BoxFlagResponse> rl = formatBoxFlagList(pr.getList());
        return new PageDataResult<>(pr.getTotal(), rl, new BoxFlagSummaryResponse().setSumEachBoxCount(rl.stream().map(BoxFlagResponse::getEachBoxCount).reduce(0, Integer::sum)));
    }

    /**
     * 装箱标识上一条（用户新增默认数据填充）
     *
     * @param deviceId 设备id
     * @return {@link DataResult<BoxFlagEntity>}
     */
    @GetMapping("last")
    public DataResult<BoxFlagResponse> boxFlagAdminLast(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        BoxFlagResponse r = defaultIfNull(
                CollUtil.getFirst(
                        formatBoxFlagList(
                                DatabaseUtil.page(
                                        new Page(1, 1),
                                        () -> boxFlagList(new BoxFlagRequest().setCreator(u.getUserId()))
                                ).getList()
                        )
                ),
                new BoxFlagResponse());
        return new DataResult<>(
                (BoxFlagResponse) r
                        .setBoxFlagId("")
                        .setCreatorFormat(u.getUsername())
                        .setPhotoList(CollUtil.defaultIfEmpty(r.getPhotoList(), new ArrayList<>()))
                        .setSerialNoList(CollUtil.defaultIfEmpty(r.getSerialNoList(), new ArrayList<>()))
                        .setSerialNoFormat(String.join("、", CollUtil.defaultIfEmpty(r.getSerialNoList(), new ArrayList<>())))
                        .setOrderNo(boxFlagDao.viewNextOrderNo())
                        .setBoxNumber(-1)
                        .setDate(DateUtil.day(new Date()))
                        .setPhotoList(new ArrayList<>())
                        .setCreator(u.getUserId())
        );
    }


    /**
     * 汇总列表
     *
     * @param deviceId 设备id
     * @param request  {@link AssemblyRequest}
     * @return {@link ListResult <AssemblySummaryResponse>}
     */
    @GetMapping("summary-list")
    public ListResult<BoxSummaryResponse> summaryList(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute BoxFlagRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final List<BoxFlagEntity> l = boxFlagList(request, lambda -> {
            lambda.select(
                    BoxFlagEntity::getCustomerShortName,
                    BoxFlagEntity::getEachBoxCount,
                    BoxFlagEntity::getBoxNumber,
                    BoxFlagEntity::getEachBoxWeight
            );
        });
        final List<BoxSummaryResponse> rl = BOX_INSTANCE.boxSummaryList(l)
                .stream().peek(t -> {
                    t.setSumBoxNumber(t.getBoxNumberList().size());
                })
                .sorted(Comparator.comparing(BoxSummaryResponse::getCustomerShortName))
                .toList();
        final List<BoxSummaryResponse> list = rl.stream().collect(Collectors.groupingBy(
                BoxSummaryResponse::getCustomerShortName,
                Collectors.reducing(
                        (t, t1) -> {
                            final List<Integer> boxNumberList = new ArrayList<>();
                            boxNumberList.addAll(t.getBoxNumberList());
                            boxNumberList.addAll(t1.getBoxNumberList());
                            return new BoxSummaryResponse()
                                    .setCustomerShortName(t.getCustomerShortName())
                                    .setSumEachBoxCount(t.getSumEachBoxCount() + t1.getSumEachBoxCount())
                                    .setSumEachBoxWeight(BigDecimal.ZERO.add(t.getSumEachBoxWeight()).add(t1.getSumEachBoxWeight()))
                                    .setBoxNumberList(boxNumberList)
                                    ;
                        }
                )
        )).values().stream().map(t -> t.orElse(new BoxSummaryResponse())).peek(t -> {
            t.setSumBoxNumber(t.getBoxNumberList().size());
        }).collect(Collectors.toList());
        MultitaskUtil.supplementList(
                list,
                BoxSummaryResponse::getCustomerShortName,
                ll -> paramDao.listByCategoryId("customerShortName"),
                (t, r) -> t.getCustomerShortName().equals(r.getValue()),
                (t, r) -> t.setCustomerShortNameFormat(r.getLabel())
        );
        if (CollUtil.isNotEmpty(list)) {
            final BoxSummaryResponse summaryData = list.stream().reduce(
                    (t, t1) -> new BoxSummaryResponse()
                            .setSumEachBoxCount(t.getSumEachBoxCount() + t1.getSumEachBoxCount())
                            .setSumEachBoxWeight(BigDecimal.ZERO.add(t.getSumEachBoxWeight()).add(t1.getSumEachBoxWeight()))
                            .setSumBoxNumber(t.getSumBoxNumber() + t1.getSumBoxNumber())
                            .setBoxNumberList(null)
            ).orElse(new BoxSummaryResponse());
            list.add(new BoxSummaryResponse().setCustomerShortNameFormat("(blank)"));
            list.add(BOX_INSTANCE.copyBoxSummary(summaryData)
                    .setCustomerShortName("")
                    .setCustomerShortNameFormat("Grand Total")
                    .setBoxNumberList(null)
            );
        }
        return new ListResult<>(
                list.stream()
                        .peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 汇总列表1
     *
     * @param deviceId 设备id
     * @param request  {@link AssemblyRequest}
     * @return {@link ListResult <AssemblySummaryResponse>}
     */
    @GetMapping("summary-list1")
    public DataListResult<BoxSummaryDescribeResponse, BoxSummaryResponse> summaryList1(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute BoxFlagRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        request.setEndDate(cn.hutool.core.date.DateUtil.offsetDay(new Date(), -14));
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final List<BoxFlagEntity> l = boxFlagList(request, lambda -> {
            lambda.select(
                    BoxFlagEntity::getCustomerShortName,
                    BoxFlagEntity::getEachBoxCount,
                    BoxFlagEntity::getBoxNumber,
                    BoxFlagEntity::getEachBoxWeight
            );
        });
        final List<BoxSummaryResponse> rl = BOX_INSTANCE.boxSummaryList(l)
                .stream().peek(t -> {
                    t.setSumBoxNumber(t.getBoxNumberList().size());
                })
                .sorted(Comparator.comparing(BoxSummaryResponse::getCustomerShortName))
                .toList();
        final List<BoxSummaryResponse> list = rl.stream().collect(Collectors.groupingBy(
                BoxSummaryResponse::getCustomerShortName,
                Collectors.reducing(
                        (t, t1) -> {
                            final List<Integer> boxNumberList = new ArrayList<>();
                            boxNumberList.addAll(t.getBoxNumberList());
                            boxNumberList.addAll(t1.getBoxNumberList());
                            return new BoxSummaryResponse()
                                    .setCustomerShortName(t.getCustomerShortName())
                                    .setSumEachBoxCount(t.getSumEachBoxCount() + t1.getSumEachBoxCount())
                                    .setSumEachBoxWeight(BigDecimal.ZERO.add(t.getSumEachBoxWeight()).add(t1.getSumEachBoxWeight()))
                                    .setBoxNumberList(boxNumberList)
                                    ;
                        }
                )
        )).values().stream().map(t -> t.orElse(new BoxSummaryResponse())).peek(t -> {
            t.setSumBoxNumber(t.getBoxNumberList().size());
        }).collect(Collectors.toList());
        MultitaskUtil.supplementList(
                list,
                BoxSummaryResponse::getCustomerShortName,
                ll -> paramDao.listByCategoryId("customerShortName"),
                (t, r) -> t.getCustomerShortName().equals(r.getValue()),
                (t, r) -> t.setCustomerShortNameFormat(r.getLabel())
        );
        if (CollUtil.isNotEmpty(list)) {
            final BoxSummaryResponse summaryData = list.stream().reduce(
                    (t, t1) -> new BoxSummaryResponse()
                            .setSumEachBoxCount(t.getSumEachBoxCount() + t1.getSumEachBoxCount())
                            .setSumEachBoxWeight(BigDecimal.ZERO.add(t.getSumEachBoxWeight()).add(t1.getSumEachBoxWeight()))
                            .setSumBoxNumber(t.getSumBoxNumber() + t1.getSumBoxNumber())
                            .setBoxNumberList(null)
            ).orElse(new BoxSummaryResponse());
            list.add(new BoxSummaryResponse().setCustomerShortNameFormat("(blank)"));
            list.add(BOX_INSTANCE.copyBoxSummary(summaryData)
                    .setCustomerShortName("")
                    .setCustomerShortNameFormat("Grand Total")
                    .setBoxNumberList(null)
            );
        }
        return new DataListResult<>(
                new BoxSummaryDescribeResponse().setTitle("统计<=%s的未发送数据".formatted(DateUtil.day(request.getEndDate()))),
                list.stream()
                        .peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

}
