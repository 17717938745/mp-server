package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.DateUtil.parse;
import static com.lead.fund.base.common.util.NumberUtil.defaultDecimal;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.QuotationConverter.QUOTATION_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.model.OptionItem;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.QuotationAttachmentDao;
import com.lead.fund.base.server.mp.dao.QuotationDao;
import com.lead.fund.base.server.mp.dao.QuotationItemDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.QuotationAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.QuotationEntity;
import com.lead.fund.base.server.mp.entity.douson.QuotationItemEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.MaterialMapper;
import com.lead.fund.base.server.mp.mapper.douson.QuotationMapper;
import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.request.QuotationPageRequest;
import com.lead.fund.base.server.mp.request.QuotationRequest;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.QuotationResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
 * DousonQuotationController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue"})
@RestController
@RequestMapping("/douson/quotation")
@Slf4j
@Validated
public class DousonQuotationController {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private UrlHelper urlHelper;
    @Resource
    private TemplatePhotoDao templatePhotoDao;
    @Resource
    private QuotationMapper quotationMapper;
    @Resource
    private QuotationDao quotationDao;
    @Resource
    private QuotationItemDao quotationItemDao;
    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private QuotationAttachmentDao quotationAttachmentDao;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private ParamDao paramDao;
    @Resource
    private LockHelper lockHelper;

    /**
     * 保存、更新访客
     *
     * @param deviceId 设备id
     * @param request  {@link QuotationRequest}
     * @return {@link Result}
     */
    @PostMapping("item")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result addItem(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody QuotationRequest request
    ) {

        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch("quotationManager"::equals)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final QuotationEntity e = QUOTATION_INSTANCE.quotation(request);
        quotationItemDao.saveBatch(
                IntStream.range(0, 1).mapToObj(i -> QUOTATION_INSTANCE.blankQuotationItem(e, request, u.getUserId())).collect(Collectors.toList())
        );
        return new Result();
    }

    /**
     * 保存、更新访客
     *
     * @param deviceId 设备id
     * @param request  {@link QuotationRequest}
     * @return {@link Result}
     */
    @PutMapping("merge")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result merge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody QuotationRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch("quotationManager"::equals)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final DateTime now = DateTime.now();
        final QuotationEntity db = isBlank(request.getQuotationId()) ? null : quotationDao.getById(request.getQuotationId());
        final QuotationEntity e = QUOTATION_INSTANCE.quotation(request);
        e
                .setDesignNumberCount(new BigDecimal(request.getDesignNumberList().size()))
                .setBidder(u.getUserId())
        ;
        if (isBlank(e.getId())) {
            quotationDao.save(e
                    .setQuotationDate(DateUtil.dateTime(now))
            );
            quotationItemDao.saveBatch(
                    IntStream.range(0, 3).mapToObj(i -> QUOTATION_INSTANCE.blankQuotationItem(e, request, u.getUserId())).collect(Collectors.toList())
            );
        } else if (null == db) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        } else {
            quotationDao.updateById(e);
            quotationItemDao.updateById(QUOTATION_INSTANCE.quotationItem(request)
                    .setProcessUnitPrice(paramDao.listByCategoryId("quotationProcessDevice").stream().collect(Collectors.toMap(OptionItem::getValue, t -> defaultDecimal(t.getExpandFirst()))).getOrDefault(request.getProcessDevice(), BigDecimal.ZERO))
            );
        }
        final QuotationEntity quotationEntity = quotationDao.getById(e.getId());
        final List<QuotationItemEntity> list = quotationItemDao.list(new LambdaQueryWrapper<QuotationItemEntity>().eq(QuotationItemEntity::getQuotationId, e.getId()));
        for (QuotationItemEntity t : list) {
            t
                    .setProcessUnitPrice(defaultDecimal(t.getProcessUnitPrice()))
                    .setProcessTime(defaultDecimal(t.getProcessTime()))
                    .setSummaryPrice(
                            t.getProcessUnitPrice().multiply(t.getProcessTime())
                                    .divide(new BigDecimal("60"), 8, RoundingMode.HALF_UP)
                    )
            ;
            quotationItemDao.updateById(t);
        }
        quotationEntity.setSummaryPrice(list.stream().map(QuotationItemEntity::getSummaryPrice).reduce(BigDecimal.ZERO, BigDecimal::add).multiply(quotationEntity.getProcessTime()));
        quotationDao.updateById(quotationEntity);
        quotationAttachmentDao.remove(new LambdaUpdateWrapper<QuotationAttachmentEntity>().eq(QuotationAttachmentEntity::getQuotationId, e.getId()));
        quotationAttachmentDao.saveBatch(
                Stream.of(
                                request.getDesignNumberList().stream().map(t -> new QuotationAttachmentEntity()
                                        .setQuotationId(e.getId())
                                        .setAttachmentCategory("1")
                                        .setAttachmentType("designNumber")
                                        .setUrl(t.getUrl())
                                        .setFileId(t.getFileId())
                                        .setFilename(t.getFilename())
                                        .setCompressUrl(t.getUrl())
                                )
                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );
        return new DataResult<>(e);
    }

    /**
     * 删除访客
     *
     * @param deviceId 设备id
     * @param request  {@link QuotationRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result delete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute QuotationRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "quotationManager".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        if (isBlank(request.getQuotationItemId())) {
            quotationDao.removeById(request.getQuotationId());
            quotationAttachmentDao.remove(new LambdaUpdateWrapper<QuotationAttachmentEntity>()
                    .eq(QuotationAttachmentEntity::getQuotationId, request.getQuotationId())
            );
        } else {
            quotationItemDao.removeById(request.getQuotationItemId());
        }
        return new Result();
    }


    private List<QuotationEntity> quotationList(QuotationRequest d) {
        LambdaQueryWrapper<QuotationEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getQuotationId())) {
            lambda.eq(QuotationEntity::getId, d.getQuotationId());
        }
        if (isNotBlank(d.getCustomer())) {
            lambda.like(QuotationEntity::getCustomer, d.getCustomer());
        }
        if (isNotBlank(d.getDesignNumber())) {
            lambda.like(QuotationEntity::getDesignNumber, d.getDesignNumber());
        }
        if (isNotBlank(d.getName())) {
            lambda.like(QuotationEntity::getName, d.getName());
        }
        if (isNotBlank(d.getMaterialQuality())) {
            lambda.like(QuotationEntity::getMaterialQuality, d.getMaterialQuality());
        }
        if (isNotBlank(d.getStartQuotationDate())) {
            lambda.ge(QuotationEntity::getQuotationDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(parse(d.getStartQuotationDate()))));
        }
        if (isNotBlank(d.getEndQuotationDate())) {
            lambda.le(QuotationEntity::getQuotationDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(parse(d.getEndQuotationDate()))));
        }
        if (isNotBlank(d.getRemarks())) {
            lambda.like(QuotationEntity::getRemarks, d.getRemarks());
        }
        if (isNotBlank(d.getBidder())) {
            lambda.eq(QuotationEntity::getBidder, d.getBidder());
        }
        if (null != d.getAcceptOrder()) {
            lambda.eq(QuotationEntity::getAcceptOrder, d.getAcceptOrder());
        }
        return quotationMapper.selectList(lambda.orderByDesc(QuotationEntity::getCreateTime));
    }


    private List<QuotationResponse> formatQuotationList(AtomicInteger startIndex, List<QuotationEntity> list) {
        final List<QuotationResponse> rl = QUOTATION_INSTANCE.quotationList(list);
        if (CollUtil.isEmpty(rl)) {
            return new ArrayList<>();
        }
        final Map<String, List<QuotationAttachmentEntity>> am = quotationAttachmentDao.list(
                        DatabaseUtil.or(new LambdaQueryWrapper<>(), rl.stream().map(QuotationResponse::getQuotationId).collect(Collectors.toList()), (lam, l) -> lam.in(QuotationAttachmentEntity::getQuotationId, l))
                ).stream()
                .collect(Collectors.groupingBy(QuotationAttachmentEntity::getQuotationId));
        final Map<String, List<QuotationItemEntity>> itemListMap = quotationItemDao.list(
                DatabaseUtil.or(
                        new LambdaQueryWrapper<QuotationItemEntity>().orderByAsc(QuotationItemEntity::getCreateTime),
                        rl.stream().map(QuotationResponse::getQuotationId).collect(Collectors.toList()),
                        (lam, l) -> {
                            lam.in(QuotationItemEntity::getQuotationId, l);
                        }
                )
        ).stream().collect(Collectors.groupingBy(QuotationItemEntity::getQuotationId));
        final AtomicInteger index = new AtomicInteger(null == startIndex ? 0 : startIndex.get());
        final List<QuotationResponse> resultList = rl.stream().map(t -> {
            t
                    .setIndex(index.addAndGet(1))
                    .setDesignNumberList(am.getOrDefault(t.getQuotationId(), new ArrayList<>())
                            .stream().filter(tt -> "1".equals(tt.getAttachmentCategory()) && "designNumber".equals(tt.getAttachmentType()))
                            .map(tt -> new FileModel()
                                    .setUrl(tt.getUrl())
                                    .setFullUrl(urlHelper.fullUrl(tt.getUrl()))
                                    .setFileId(tt.getFileId())
                                    .setFilename(tt.getFilename())
                            )
                            .collect(Collectors.toList()))
                    .setDesignNumberCount(new BigDecimal(t.getDesignNumberList().size()))
            ;
            return Stream.of(
                    itemListMap.getOrDefault(t.getQuotationId(), new ArrayList<>())
                            .stream().map(tt ->
                                    QUOTATION_INSTANCE.quotation(t, tt)
                            ),
                    Stream.of(t)
            ).flatMap(tt -> tt).collect(Collectors.toList());
        }).flatMap(Collection::stream).collect(Collectors.toList());
        MultitaskUtil.supplementList(
                resultList.stream().filter(t -> isNotBlank(t.getBidder())).collect(Collectors.toList()),
                QuotationResponse::getBidder,
                l1 -> userMapper.selectList(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .in(MpUserEntity::getId, l1)
                                .select(
                                        MpUserEntity::getId,
                                        MpUserEntity::getUsername,
                                        MpUserEntity::getName
                                )
                ),
                (t, r) -> t.getBidder().equals(r.getId()),
                (t, r) -> t.setBidderFormat(r.getName())
        );
        MultitaskUtil.supplementList(
                resultList.stream().filter(t -> isNotBlank(t.getProcessProcedure())).collect(Collectors.toList()),
                QuotationResponse::getProcessProcedure,
                l1 -> paramDao.listByCategoryId("quotationProcessProcedure"),
                (t, r) -> t.getProcessProcedure().equals(r.getValue()),
                (t, r) -> t.setProcessProcedureFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                resultList.stream().filter(t -> isNotBlank(t.getProcessDevice())).collect(Collectors.toList()),
                QuotationResponse::getProcessDevice,
                l1 -> paramDao.listByCategoryId("quotationProcessDevice"),
                (t, r) -> t.getProcessDevice().equals(r.getValue()),
                (t, r) -> t.setProcessDeviceFormat(r.getLabel())
        );
        return resultList;
    }

    /**
     * 访客分页
     *
     * @param deviceId 设备id
     * @param request  {@link QuotationPageRequest}
     * @return {@link PageResult <QuotationResponse>}
     */
    @GetMapping("page")
    public PageResult<QuotationResponse> quotationAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute QuotationPageRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(
                t -> "quotationManager".equals(t.getRoleCode()) || "quotationView".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final PageResult<QuotationEntity> pr = DatabaseUtil.page(request, this::quotationList);
        final AtomicInteger index = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(
                pr.getTotal(),
                formatQuotationList(index, pr.getList())
        );
    }
}
