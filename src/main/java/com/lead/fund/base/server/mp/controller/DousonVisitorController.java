package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.DateUtil.parse;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.VisitorConverter.VISITOR_INSTANCE;

import cn.hutool.core.collection.CollUtil;
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
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.dao.VisitorAttachmentDao;
import com.lead.fund.base.server.mp.dao.VisitorDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.VisitorAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.VisitorEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.MaterialMapper;
import com.lead.fund.base.server.mp.mapper.douson.VisitorMapper;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.request.VisitorPageRequest;
import com.lead.fund.base.server.mp.request.VisitorRequest;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.ParamConfigResponse;
import com.lead.fund.base.server.mp.response.VisitorResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
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
 * DousonVisitorController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused"})
@RestController
@RequestMapping("/douson/visitor")
@Slf4j
@Validated
public class DousonVisitorController {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private UrlHelper urlHelper;
    @Resource
    private TemplatePhotoDao templatePhotoDao;
    @Resource
    private VisitorMapper visitorMapper;
    @Resource
    private VisitorDao visitorDao;
    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private VisitorAttachmentDao visitorAttachmentDao;
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
     * @param request  {@link VisitorRequest}
     * @return {@link Result}
     */
    @PutMapping("merge")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result merge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody VisitorRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final DateTime now = DateTime.now();
        final VisitorEntity db = isBlank(request.getVisitorId()) ? null : visitorDao.getById(request.getVisitorId());
        final VisitorEntity e = VISITOR_INSTANCE.visitor(request);
        final Map<Object, String> dm = paramDao.listByCategoryId("department")
                .stream().collect(Collectors.toMap(ParamConfigResponse::getValue, t -> defaultIfBlank(t.getExpandFirst())));
        e.setApprover(dm.getOrDefault(userMapper.selectById(e.getContactPerson()).getDepartment(), ""))
                .setIdAndPhotos(new BigDecimal(request.getIdAndPhotosList().size()))
                .setFactoryRecords(new BigDecimal(request.getFactoryRecordsList().size()))
        ;
        if (isBlank(e.getId())) {
            visitorDao.save(e
                    .setApplyDate(DateUtil.dateTime())
                    .setApplicant(u.getUserId())
                    .setValid(false)
                    .setPrintNumber("")
            );
        } else if (null == db) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        } else {
            if (u.getRoleCodeList().stream().noneMatch(t -> "visitorManager".equals(t) || "visitorSecurity".equals(t)) && !u.getUserId().equals(db.getApplicant())) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            visitorDao.updateById(e
                    .setPrintNumber(defaultIfBlank(db.getPrintNumber(), () -> CollUtil.isEmpty(request.getIdAndPhotosList()) ? "" : DateUtil.dateTimeSimple().substring(2, 12)))
                    .setVisitorFactoryDate(defaultIfBlank(db.getVisitorFactoryDate(), () -> CollUtil.isEmpty(request.getFactoryRecordsList()) ? "" : DateUtil.dateTime()))
            );
        }
        visitorAttachmentDao.remove(new LambdaUpdateWrapper<VisitorAttachmentEntity>().eq(VisitorAttachmentEntity::getVisitorId, e.getId()));
        visitorAttachmentDao.saveBatch(
                Stream.of(
                                request.getFactoryRecordsList().stream().map(t -> new VisitorAttachmentEntity()
                                        .setVisitorId(e.getId())
                                        .setAttachmentCategory("0")
                                        .setAttachmentType("factoryRecords")
                                        .setUrl(t.getPhotoUrl())
                                        .setCompressUrl(t.getPhotoCompressUrl())
                                ),
                                request.getIdAndPhotosList().stream().map(t -> new VisitorAttachmentEntity()
                                        .setVisitorId(e.getId())
                                        .setAttachmentCategory("0")
                                        .setAttachmentType("idAndPhotos")
                                        .setUrl(t.getPhotoUrl())
                                        .setCompressUrl(t.getPhotoCompressUrl())
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
     * @param request  {@link VisitorRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result delete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute VisitorRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "visitorManager".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final VisitorEntity db = visitorDao.getById(request.getVisitorId());
        if (null != db) {
            visitorDao.removeById(request.getVisitorId());
            visitorAttachmentDao.remove(new LambdaUpdateWrapper<VisitorAttachmentEntity>()
                    .eq(VisitorAttachmentEntity::getVisitorId, db.getId())
            );
        }
        return new Result();
    }


    private List<VisitorEntity> visitorList(VisitorRequest d) {
        LambdaQueryWrapper<VisitorEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getVisitorId())) {
            lambda.eq(VisitorEntity::getId, d.getVisitorId());
        }
        if (isNotBlank(d.getApplicant())) {
            lambda.eq(VisitorEntity::getApplicant, d.getApplicant());
        }
        if (isNotBlank(d.getVisitorName())) {
            lambda.like(VisitorEntity::getVisitorName, d.getVisitorName());
        }
        if (isNotBlank(d.getCompanyName())) {
            lambda.like(VisitorEntity::getCompanyName, d.getCompanyName());
        }
        if (isNotBlank(d.getVisitContent())) {
            lambda.like(VisitorEntity::getVisitContent, d.getVisitContent());
        }
        if (isNotBlank(d.getStartApplyDate())) {
            lambda.ge(VisitorEntity::getApplyDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(parse(d.getStartApplyDate()))));
        }
        if (isNotBlank(d.getEndApplyDate())) {
            lambda.le(VisitorEntity::getApplyDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(parse(d.getEndApplyDate()))));
        }
        if (isNotBlank(d.getStartExpectedVisitTime())) {
            lambda.ge(VisitorEntity::getExpectedVisitTime, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(parse(d.getStartExpectedVisitTime()))));
        }
        if (isNotBlank(d.getEndExpectedVisitTime())) {
            lambda.le(VisitorEntity::getExpectedVisitTime, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(parse(d.getEndExpectedVisitTime()))));
        }
        if (isNotBlank(d.getStartExpectedEndTime())) {
            lambda.ge(VisitorEntity::getExpectedEndTime, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(parse(d.getStartExpectedEndTime()))));
        }
        if (isNotBlank(d.getEndExpectedEndTime())) {
            lambda.le(VisitorEntity::getExpectedEndTime, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(parse(d.getEndExpectedEndTime()))));
        }
        if (isNotBlank(d.getContactPerson())) {
            lambda.eq(VisitorEntity::getContactPerson, d.getContactPerson());
        }
        if (CollUtil.isNotEmpty(d.getContactPersonList())) {
            DatabaseUtil.or(lambda, d.getContactPersonList(), (l, t) -> l.in(VisitorEntity::getContactPerson, t));
        }
        if (isNotBlank(d.getPrintNumber())) {
            lambda.like(VisitorEntity::getPrintNumber, d.getPrintNumber());
        }
        return visitorMapper.selectList(lambda.orderByDesc(VisitorEntity::getCreateTime));
    }


    private List<VisitorResponse> formatVisitorList(List<VisitorEntity> list) {
        final List<VisitorResponse> rl = VISITOR_INSTANCE.visitorList(list);
        final List<String> contactPersonList = rl.stream().map(VisitorResponse::getContactPerson).filter(StrUtil::isNotBlank).collect(Collectors.toList());
        final Map<String, String> userDepartMap = CollUtil.isEmpty(contactPersonList) ? new HashMap<>(8) : userMapper.selectList(
                DatabaseUtil.or(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .select(MpUserEntity::getId, MpUserEntity::getName, MpUserEntity::getUsername, MpUserEntity::getDepartment),
                        contactPersonList,
                        (lam, l) -> {
                            lam.in(MpUserEntity::getId, l);
                        }
                )
        ).stream().collect(Collectors.toMap(MpUserEntity::getId, t -> defaultIfBlank(t.getDepartment())));
        final Map<Object, String> dm = paramDao.listByCategoryId("department")
                .stream().collect(Collectors.toMap(ParamConfigResponse::getValue, ParamConfigResponse::getLabel));
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getApplicant())).collect(Collectors.toList()),
                VisitorResponse::getApplicant,
                l1 -> userMapper.selectList(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .in(MpUserEntity::getId, l1)
                                .select(
                                        MpUserEntity::getId,
                                        MpUserEntity::getUsername,
                                        MpUserEntity::getName
                                )
                ),
                (t, r) -> t.getApplicant().equals(r.getId()),
                (t, r) -> t.setApplicantFormat(r.getName())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getApprover())).collect(Collectors.toList()),
                VisitorResponse::getApprover,
                l1 -> userMapper.selectList(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .in(MpUserEntity::getId, l1)
                                .select(
                                        MpUserEntity::getId,
                                        MpUserEntity::getUsername,
                                        MpUserEntity::getName
                                )
                ),
                (t, r) -> t.getApprover().equals(r.getId()),
                (t, r) -> t.setApproverFormat(r.getName())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getVisitContent())).collect(Collectors.toList()),
                VisitorResponse::getVisitContent,
                l1 -> paramDao.listByCategoryId("visitContent"),
                (t, r) -> t.getVisitContent().equals(r.getValue()),
                (t, r) -> t.setVisitContentFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getContactPerson())).collect(Collectors.toList()),
                VisitorResponse::getContactPerson,
                l1 -> userMapper.selectList(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .in(MpUserEntity::getId, l1)
                                .select(
                                        MpUserEntity::getId,
                                        MpUserEntity::getUsername,
                                        MpUserEntity::getName
                                )
                ),
                (t, r) -> t.getContactPerson().equals(r.getId()),
                (t, r) -> t.setContactPersonFormat(r.getName())
        );
        final Map<String, List<VisitorAttachmentEntity>> am = visitorAttachmentDao.list(
                        DatabaseUtil.or(new LambdaQueryWrapper<>(), rl.stream().map(VisitorResponse::getVisitorId).collect(Collectors.toList()), (lam, l) -> lam.in(VisitorAttachmentEntity::getVisitorId, l))
                ).stream()
                .collect(Collectors.groupingBy(VisitorAttachmentEntity::getVisitorId));
        for (VisitorResponse t : rl) {
            t
                    .setIdAndPhotosList(am.getOrDefault(t.getVisitorId(), new ArrayList<>())
                            .stream().filter(tt -> "0".equals(tt.getAttachmentCategory()) && "idAndPhotos".equals(tt.getAttachmentType()))
                            .map(tt -> new PhotoImgModel()
                                    .setPhotoUrl(tt.getUrl())
                                    .setFullPhotoUrl(urlHelper.fullUrl(tt.getUrl()))
                                    .setPhotoCompressUrl(tt.getCompressUrl())
                                    .setFullPhotoCompressUrl(urlHelper.fullUrl(tt.getCompressUrl()))
                            )
                            .collect(Collectors.toList()))
                    .setFactoryRecordsList(am.getOrDefault(t.getVisitorId(), new ArrayList<>())
                            .stream().filter(tt -> "0".equals(tt.getAttachmentCategory()) && "factoryRecords".equals(tt.getAttachmentType()))
                            .map(tt -> new PhotoImgModel()
                                    .setPhotoUrl(tt.getUrl())
                                    .setFullPhotoUrl(urlHelper.fullUrl(tt.getUrl()))
                                    .setPhotoCompressUrl(tt.getCompressUrl())
                                    .setFullPhotoCompressUrl(urlHelper.fullUrl(tt.getCompressUrl()))
                            )
                            .collect(Collectors.toList()))
                    .setVisitDepartment(userDepartMap.getOrDefault(t.getContactPerson(), ""))
                    .setVisitDepartmentFormat(dm.getOrDefault(t.getVisitDepartment(), t.getVisitDepartment()))
            ;
        }
        return rl;
    }

    /**
     * 访客分页
     *
     * @param deviceId 设备id
     * @param request  {@link VisitorPageRequest}
     * @return {@link PageResult <VisitorResponse>}
     */
    @GetMapping("page")
    public PageResult<VisitorResponse> visitorAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute VisitorPageRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(
                t -> "visitorManager".equals(t.getRoleCode()) || "visitorView".equals(t.getRoleCode()) || "visitorSecurity".equals(t.getRoleCode()))) {
            request.getData().setApplicant(u.getUserId());
        }
        if (isNotBlank(request.getData().getVisitDepartment())) {
            final List<String> userIdList = userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().eq(MpUserEntity::getDepartment, request.getData().getVisitDepartment())
                    .select(MpUserEntity::getId)).stream().map(AbstractPrimaryKey::getId).collect(Collectors.toList());
            if (CollUtil.isEmpty(userIdList)) {
                return new PageResult<>();
            }
            request.getData().setContactPersonList(userIdList);
        }
        final PageResult<VisitorEntity> pr = DatabaseUtil.page(request, this::visitorList);
        final AtomicInteger index = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        final AtomicInteger deviceIndex = new AtomicInteger(0);
        final List<VisitorResponse> l = formatVisitorList(pr.getList());
        return new PageResult<>(
                pr.getTotal(),
                l.stream().peek(t -> {
                    t.setIndex(index.addAndGet(1));
                }).collect(Collectors.toList())
        );
    }

}
