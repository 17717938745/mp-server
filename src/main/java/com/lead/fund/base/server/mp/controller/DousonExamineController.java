package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.ExamineConverter.EXAMINE_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.server.mp.dao.ExamineAttachmentDao;
import com.lead.fund.base.server.mp.dao.ExamineDao;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.ExamineAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.ExamineEntity;
import com.lead.fund.base.server.mp.entity.douson.MaterialEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.ExamineMapper;
import com.lead.fund.base.server.mp.mapper.douson.MaterialMapper;
import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.request.ExaminePageRequest;
import com.lead.fund.base.server.mp.request.ExamineRequest;
import com.lead.fund.base.server.mp.response.ExamineResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import jakarta.annotation.Resource;

import java.math.BigDecimal;
import java.util.ArrayList;
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
 * DousonExamineController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused"})
@RestController
@RequestMapping("/douson/examine")
@Slf4j
@Validated
public class DousonExamineController {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private UrlHelper urlHelper;
    @Resource
    private TemplatePhotoDao templatePhotoDao;
    @Resource
    private ExamineMapper examineMapper;
    @Resource
    private ExamineDao examineDao;
    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private ExamineAttachmentDao examineAttachmentDao;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private ParamDao paramDao;
    @Resource
    private LockHelper lockHelper;

    /**
     * 保存、更新订单检验记录
     *
     * @param deviceId 设备id
     * @param request  {@link ExamineRequest}
     * @return {@link Result}
     */
    @PutMapping("merge")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result merge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ExamineRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "examineManager".equals(t) || "identificationRecord".equals(t) || "hardnessRecord".equals(t) || "ndeRecord".equals(t) || "dimensionRecord".equals(t))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final DateTime now = DateTime.now();
        final ExamineEntity db = isBlank(request.getExamineId()) ? null : examineDao.getById(request.getExamineId());
        final BigDecimal inspectionCompletedQuantity = null == db ? BigDecimal.ZERO : db.getInspectionCompletedQuantity();
        final ExamineEntity e = EXAMINE_INSTANCE.examine(request);
        final List<ExamineAttachmentEntity> atl = examineAttachmentDao.list(new LambdaQueryWrapper<ExamineAttachmentEntity>().eq(ExamineAttachmentEntity::getExamineId, e.getId()));
        final List<String> identificationUrlList = atl.stream().filter(t -> "identification".equals(t.getAttachmentType())).map(ExamineAttachmentEntity::getUrl)
                .sorted(String::compareTo).toList();
        final List<String> identificationRequestUrlList = request.getIdentificationList().stream().map(PhotoImgModel::getPhotoUrl)
                .sorted(String::compareTo).toList();
        if (!identificationUrlList.equals(identificationRequestUrlList)) {
            if (CollUtil.isNotEmpty(identificationRequestUrlList)) {
                e.setIdentificationPerson(u.getUserId())
                        .setIdentificationDate(DateUtil.dateTime(now))
                ;
            } else {
                e.setIdentificationPerson("")
                        .setIdentificationDate("")
                ;

            }
        }
        if (!e.getInspectionCompletedQuantity().equals(inspectionCompletedQuantity)) {
            if (e.getInspectionCompletedQuantity().compareTo(BigDecimal.ZERO) != 0) {
                e.setInspectionPerson(u.getUserId())
                        .setInspectionCompletedDate(DateUtil.dateTime(now))
                ;
            } else {
                e.setInspectionPerson("")
                        .setInspectionCompletedDate("")
                ;
            }
        }
        if (isBlank(e.getId())) {
            examineDao.save(e);
        } else {
            examineDao.updateById(e);
        }
        examineAttachmentDao.remove(new LambdaUpdateWrapper<ExamineAttachmentEntity>().eq(ExamineAttachmentEntity::getExamineId, e.getId()));
        examineAttachmentDao.saveBatch(
                Stream.of(
                                request.getIdentificationList().stream().map(t -> new ExamineAttachmentEntity()
                                        .setExamineId(e.getId())
                                        .setAttachmentCategory("0")
                                        .setAttachmentType("identification")
                                        .setUrl(t.getPhotoUrl())
                                        .setCompressUrl(t.getPhotoCompressUrl())),
                                request.getHardnessList().stream().map(t -> new ExamineAttachmentEntity()
                                        .setExamineId(e.getId())
                                        .setAttachmentCategory("1")
                                        .setAttachmentType("hardness")
                                        .setUrl(t.getUrl())
                                        .setFileId(t.getFileId())
                                        .setFilename(t.getFilename())
                                        .setCompressUrl(t.getUrl())),
                                request.getNdeList().stream().map(t -> new ExamineAttachmentEntity()
                                        .setExamineId(e.getId())
                                        .setAttachmentCategory("0")
                                        .setAttachmentType("nde")
                                        .setUrl(t.getPhotoUrl())
                                        .setCompressUrl(t.getPhotoCompressUrl())),
                                request.getDimensionList().stream().map(t -> new ExamineAttachmentEntity()
                                        .setExamineId(e.getId())
                                        .setAttachmentCategory("0")
                                        .setAttachmentType("dimension")
                                        .setUrl(t.getPhotoUrl())
                                        .setCompressUrl(t.getPhotoCompressUrl()))

                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );
        return new DataResult<>(e);
    }

    /**
     * 删除订单检验记录
     *
     * @param deviceId 设备id
     * @param request  {@link ExamineRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result delete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ExamineRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "examineManager".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final ExamineEntity db = examineDao.getById(request.getExamineId());
        if (null != db) {
            examineDao.removeById(request.getExamineId());
            examineAttachmentDao.remove(new LambdaUpdateWrapper<ExamineAttachmentEntity>()
                    .eq(ExamineAttachmentEntity::getExamineId, db.getId())
            );
            if (isNotBlank(db.getSaleOrderNo()) && isNotBlank(db.getOrderProjectNo())) {
                materialMapper.update(
                        null,
                        new LambdaUpdateWrapper<MaterialEntity>()
                                .set(MaterialEntity::getGenerateExamine, false)
                                .eq(MaterialEntity::getId, db.getSaleOrderNo())
                                .eq(MaterialEntity::getId, db.getOrderProjectNo())
                );
            }
        }
        return new Result();
    }


    private List<ExamineEntity> examineList(ExamineRequest d) {
        LambdaQueryWrapper<ExamineEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getExamineId())) {
            lambda.eq(ExamineEntity::getId, d.getExamineId());
        }
        if (isNotBlank(d.getCheckOrderNo())) {
            lambda.like(ExamineEntity::getCheckOrderNo, d.getCheckOrderNo());
        }
        if (isNotBlank(d.getCustomerShortName())) {
            lambda.like(ExamineEntity::getCustomerShortName, d.getCustomerShortName());
        }
        if (isNotBlank(d.getSaleOrderNo())) {
            lambda.like(ExamineEntity::getSaleOrderNo, d.getSaleOrderNo());
        }
        if (isNotBlank(d.getOrderProjectNo())) {
            lambda.like(ExamineEntity::getOrderProjectNo, d.getOrderProjectNo());
        }
        if (isNotBlank(d.getMaterialNo())) {
            lambda.like(ExamineEntity::getMaterialNo, d.getMaterialNo());
        }
        if (isNotBlank(d.getDesignNumber())) {
            lambda.like(ExamineEntity::getDesignNumber, d.getDesignNumber());
        }
        if (isNotBlank(d.getImproveMaterialDescribe())) {
            lambda.like(ExamineEntity::getImproveMaterialDescribe, d.getImproveMaterialDescribe());
        }
        if (isNotBlank(d.getDescription())) {
            lambda.like(ExamineEntity::getDescription, d.getDescription());
        }
        if (null != d.getStartPromiseDoneDate()) {
            lambda.ge(ExamineEntity::getPromiseDoneDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(d.getStartPromiseDoneDate())));
        }
        if (null != d.getEndPromiseDoneDate()) {
            lambda.le(ExamineEntity::getPromiseDoneDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(d.getEndPromiseDoneDate())));
        }
        if (null != d.getStartIdentificationDate()) {
            lambda.ge(ExamineEntity::getIdentificationDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(d.getStartIdentificationDate())));
        }
        if (null != d.getEndIdentificationDate()) {
            lambda.le(ExamineEntity::getIdentificationDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(d.getEndIdentificationDate())));
        }
        if (null != d.getStartInspectionCompletedDate()) {
            lambda.ge(ExamineEntity::getInspectionCompletedDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(d.getStartInspectionCompletedDate())));
        }
        if (null != d.getEndInspectionCompletedDate()) {
            lambda.le(ExamineEntity::getInspectionCompletedDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(d.getEndInspectionCompletedDate())));
        }
        return examineMapper.selectList(lambda.orderByDesc(ExamineEntity::getCreateTime));
    }


    private List<ExamineResponse> formatExamineList(List<ExamineEntity> list) {
        List<ExamineResponse> rl = EXAMINE_INSTANCE.examineList(list);
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getInspectionPerson())).collect(Collectors.toList()),
                ExamineResponse::getInspectionPerson,
                l1 -> userMapper.selectList(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .in(MpUserEntity::getId, l1)
                                .select(
                                        MpUserEntity::getId,
                                        MpUserEntity::getUsername,
                                        MpUserEntity::getName
                                )
                ),
                (t, r) -> t.getInspectionPerson().equals(r.getId()),
                (t, r) -> t.setInspectionPersonFormat(r.getName())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getIdentificationPerson())).collect(Collectors.toList()),
                ExamineResponse::getIdentificationPerson,
                l1 -> userMapper.selectList(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .in(MpUserEntity::getId, l1)
                                .select(
                                        MpUserEntity::getId,
                                        MpUserEntity::getUsername,
                                        MpUserEntity::getName
                                )
                ),
                (t, r) -> t.getIdentificationPerson().equals(r.getId()),
                (t, r) -> t.setIdentificationPersonFormat(r.getName())
        );
        final Map<String, List<ExamineAttachmentEntity>> am = examineAttachmentDao.list(
                        DatabaseUtil.or(new LambdaQueryWrapper<>(), rl.stream().map(ExamineResponse::getExamineId).collect(Collectors.toList()), (lam, l) -> lam.in(ExamineAttachmentEntity::getExamineId, l))
                ).stream()
                .collect(Collectors.groupingBy(ExamineAttachmentEntity::getExamineId));
        for (ExamineResponse t : rl) {
            t.setIdentificationList(am.getOrDefault(t.getExamineId(), new ArrayList<>())
                    .stream().filter(tt -> "identification".equals(tt.getAttachmentType()))
                    .map(tt -> new PhotoImgModel()
                            .setPhotoUrl(tt.getUrl())
                            .setFullPhotoUrl(urlHelper.fullUrl(tt.getUrl()))
                            .setPhotoCompressUrl(tt.getCompressUrl())
                            .setFullPhotoCompressUrl(urlHelper.fullUrl(tt.getCompressUrl()))
                    )
                    .collect(Collectors.toList()));
            t.setHardnessList(am.getOrDefault(t.getExamineId(), new ArrayList<>())
                    .stream().filter(tt -> "hardness".equals(tt.getAttachmentType()))
                    .map(tt -> new FileModel()
                            .setUrl(tt.getUrl())
                            .setFullUrl(urlHelper.fullUrl(tt.getUrl()))
                            .setFileId(tt.getFileId())
                            .setFilename(tt.getFilename())
                    )
                    .collect(Collectors.toList()));
            t.setNdeList(am.getOrDefault(t.getExamineId(), new ArrayList<>())
                    .stream().filter(tt -> "nde".equals(tt.getAttachmentType()))
                    .map(tt -> new PhotoImgModel()
                            .setPhotoUrl(tt.getUrl())
                            .setFullPhotoUrl(urlHelper.fullUrl(tt.getUrl()))
                            .setPhotoCompressUrl(tt.getCompressUrl())
                            .setFullPhotoCompressUrl(urlHelper.fullUrl(tt.getCompressUrl()))
                    )
                    .collect(Collectors.toList()));
            t.setDimensionList(am.getOrDefault(t.getExamineId(), new ArrayList<>())
                    .stream().filter(tt -> "dimension".equals(tt.getAttachmentType()))
                    .map(tt -> new PhotoImgModel()
                            .setPhotoUrl(tt.getUrl())
                            .setFullPhotoUrl(urlHelper.fullUrl(tt.getUrl()))
                            .setPhotoCompressUrl(tt.getCompressUrl())
                            .setFullPhotoCompressUrl(urlHelper.fullUrl(tt.getCompressUrl()))
                    )
                    .collect(Collectors.toList()));
            t.setIdentificationCount(t.getIdentificationList().size())
                    .setHardnessCount(t.getHardnessList().size())
                    .setNdeCount(t.getNdeList().size())
                    .setDimensionCount(t.getDimensionList().size())
            ;
        }
        return rl;
    }

    /**
     * 订单检验记录分页
     *
     * @param deviceId 设备id
     * @param request  {@link ExaminePageRequest}
     * @return {@link PageResult <ExamineResponse>}
     */
    @GetMapping("page")
    public PageResult<ExamineResponse> examineAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ExaminePageRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(
                t -> "admin".equals(t.getRoleCode()) || "identificationRecord".equals(t.getRoleCode()) || "hardnessRecord".equals(t.getRoleCode()) || "ndeRecord".equals(t.getRoleCode()) || "dimensionRecord".equals(t.getRoleCode()) || "examineManager".equals(t.getRoleCode()) || "examineView".equals(t.getRoleCode()))
                && !"admin".equals(u.getUsername())) {
            return new PageResult<>(0, new ArrayList<>());
        }
        final PageResult<ExamineEntity> pr = DatabaseUtil.page(request, this::examineList);
        final AtomicInteger index = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        final AtomicInteger deviceIndex = new AtomicInteger(0);
        final List<ExamineResponse> l = formatExamineList(pr.getList());
        return new PageResult<>(
                pr.getTotal(),
                l.stream().peek(t -> {
                    t.setIndex(index.addAndGet(1));
                }).collect(Collectors.toList())
        );
    }

}
