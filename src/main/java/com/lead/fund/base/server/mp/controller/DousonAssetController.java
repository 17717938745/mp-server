package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.DressConverter.DRESS_INSTANCE;

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
import com.lead.fund.base.server.mp.dao.DressAttachmentDao;
import com.lead.fund.base.server.mp.dao.DressDao;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.DressAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.DressEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.DressMapper;
import com.lead.fund.base.server.mp.mapper.douson.MaterialMapper;
import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.request.DressPageRequest;
import com.lead.fund.base.server.mp.request.DressRequest;
import com.lead.fund.base.server.mp.response.DressResponse;
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
 * DousonDressController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused"})
@RestController
@RequestMapping("/douson/dress")
@Slf4j
@Validated
public class DousonAssetController {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private UrlHelper urlHelper;
    @Resource
    private TemplatePhotoDao templatePhotoDao;
    @Resource
    private DressMapper dressMapper;
    @Resource
    private DressDao dressDao;
    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private DressAttachmentDao dressAttachmentDao;
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
     * @param request  {@link DressRequest}
     * @return {@link Result}
     */
    @PutMapping("merge")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result merge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody DressRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final DateTime now = DateTime.now();
        final DressEntity db = isBlank(request.getDressId()) ? null : dressDao.getById(request.getDressId());
        final DressEntity e = DRESS_INSTANCE.dress(request);
        if (isBlank(e.getId())) {
            if (u.getRoleCodeList().stream().noneMatch("dressManager"::equals)) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            dressDao.save(e);
        } else {
            if (u.getRoleCodeList().stream().noneMatch(t -> "dressManager".equals(t) || "storeDressManager".equals(t))) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            dressDao.updateById(e);
        }
        dressAttachmentDao.remove(new LambdaUpdateWrapper<DressAttachmentEntity>().eq(DressAttachmentEntity::getDressId, e.getId()));
        dressAttachmentDao.saveBatch(
                Stream.of(
                                request.getDesignNumberList().stream().map(t -> new DressAttachmentEntity()
                                        .setDressId(e.getId())
                                        .setAttachmentCategory("1")
                                        .setAttachmentType("designNumber")
                                        .setUrl(t.getUrl())
                                        .setFileId(t.getFileId())
                                        .setFilename(t.getFilename())
                                        .setCompressUrl(t.getUrl())),
                                request.getStorePictureList().stream().map(t -> new DressAttachmentEntity()
                                        .setDressId(e.getId())
                                        .setAttachmentCategory("0")
                                        .setAttachmentType("storePicture")
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
     * @param request  {@link DressRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result delete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute DressRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "dressManager".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final DressEntity db = dressDao.getById(request.getDressId());
        if (null != db) {
            dressDao.removeById(request.getDressId());
            dressAttachmentDao.remove(new LambdaUpdateWrapper<DressAttachmentEntity>()
                    .eq(DressAttachmentEntity::getDressId, db.getId())
            );
        }
        return new Result();
    }


    private List<DressEntity> dressList(DressRequest d) {
        LambdaQueryWrapper<DressEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getDressId())) {
            lambda.eq(DressEntity::getId, d.getDressId());
        }
        if (isNotBlank(d.getWorkDressType())) {
            lambda.like(DressEntity::getWorkDressType, d.getWorkDressType());
        }
        if (isNotBlank(d.getMaterialNo())) {
            lambda.like(DressEntity::getMaterialNo, d.getMaterialNo());
        }
        if (isNotBlank(d.getRemark())) {
            lambda.like(DressEntity::getRemark, d.getRemark());
        }
        if (isNotBlank(d.getDesignNumber())) {
            lambda.like(DressEntity::getDesignNumber, d.getDesignNumber());
        }
        if (null != d.getStartApplyDate()) {
            lambda.ge(DressEntity::getApplyDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(d.getStartApplyDate())));
        }
        if (null != d.getEndApplyDate()) {
            lambda.le(DressEntity::getApplyDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(d.getEndApplyDate())));
        }
        if (isNotBlank(d.getDescriptionOfOrder())) {
            lambda.like(DressEntity::getDescriptionOfOrder, d.getDescriptionOfOrder());
        }
        if (isNotBlank(d.getStoreDateDescription())) {
            lambda.like(DressEntity::getStoreDateDescription, d.getStoreDateDescription());
        }
        return dressMapper.selectList(lambda.orderByDesc(DressEntity::getCreateTime));
    }


    private List<DressResponse> formatDressList(List<DressEntity> list) {
        List<DressResponse> rl = DRESS_INSTANCE.dressList(list);
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getCheckAcceptUser())).collect(Collectors.toList()),
                DressResponse::getCheckAcceptUser,
                l1 -> userMapper.selectList(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .in(MpUserEntity::getId, l1)
                                .select(
                                        MpUserEntity::getId,
                                        MpUserEntity::getUsername,
                                        MpUserEntity::getName
                                )
                ),
                (t, r) -> t.getCheckAcceptUser().equals(r.getId()),
                (t, r) -> t.setCheckAcceptUserFormat(r.getName())
        );
        MultitaskUtil.supplementList(
                rl,
                DressResponse::getWorkDressType,
                l1 -> paramDao.listByCategoryId("workDressType"),
                (t, r) -> defaultIfBlank(t.getWorkDressType()).equals(r.getValue()),
                (t, r) -> t.setWorkDressTypeFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl,
                DressResponse::getStorePosition,
                l1 -> paramDao.listByCategoryId("storePosition"),
                (t, r) -> defaultIfBlank(t.getStorePosition()).equals(r.getValue()),
                (t, r) -> t.setStorePositionFormat(r.getLabel())
        );
        final Map<String, List<DressAttachmentEntity>> am = dressAttachmentDao.list(
                        DatabaseUtil.or(new LambdaQueryWrapper<>(), rl.stream().map(DressResponse::getDressId).collect(Collectors.toList()), (lam, l) -> lam.in(DressAttachmentEntity::getDressId, l))
                ).stream()
                .collect(Collectors.groupingBy(DressAttachmentEntity::getDressId));
        for (DressResponse t : rl) {
            t.setDesignNumberList(am.getOrDefault(t.getDressId(), new ArrayList<>())
                    .stream().filter(tt -> "designNumber".equals(tt.getAttachmentType()))
                    .map(tt -> new FileModel()
                            .setUrl(tt.getUrl())
                            .setFullUrl(urlHelper.fullUrl(tt.getUrl()))
                            .setFileId(tt.getFileId())
                            .setFilename(tt.getFilename())
                    )
                    .collect(Collectors.toList()));
            t.setStorePictureList(am.getOrDefault(t.getDressId(), new ArrayList<>())
                    .stream().filter(tt -> "storePicture".equals(tt.getAttachmentType()))
                    .map(tt -> new PhotoImgModel()
                            .setPhotoUrl(tt.getUrl())
                            .setFullPhotoUrl(urlHelper.fullUrl(tt.getUrl()))
                            .setPhotoCompressUrl(tt.getCompressUrl())
                            .setFullPhotoCompressUrl(urlHelper.fullUrl(tt.getCompressUrl()))
                    )
                    .collect(Collectors.toList()));
            t
                    .setDesignNumberCount(t.getDesignNumberList().size())
                    .setStorePictureCount(t.getStorePictureList().size())
            ;
        }
        return rl;
    }

    /**
     * 订单检验记录分页
     *
     * @param deviceId 设备id
     * @param request  {@link DressPageRequest}
     * @return {@link PageResult <DressResponse>}
     */
    @GetMapping("page")
    public PageResult<DressResponse> dressAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute DressPageRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(
                t -> "admin".equals(t.getRoleCode()) || "dressManager".equals(t.getRoleCode()) || "storeDressManager".equals(t.getRoleCode()) || "dressView".equals(t.getRoleCode()))
                && !"admin".equals(u.getUsername())) {
            return new PageResult<>(0, new ArrayList<>());
        }
        final PageResult<DressEntity> pr = DatabaseUtil.page(request, this::dressList);
        final AtomicInteger index = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        final AtomicInteger deviceIndex = new AtomicInteger(0);
        final List<DressResponse> l = formatDressList(pr.getList());
        return new PageResult<>(
                pr.getTotal(),
                l.stream().peek(t -> {
                    t.setIndex(index.addAndGet(1));
                }).collect(Collectors.toList())
        );
    }

}
