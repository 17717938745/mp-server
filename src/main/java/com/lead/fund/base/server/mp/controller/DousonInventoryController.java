package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.DataFormatUtil.defaultDecimal;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.InventoryConverter.INVENTORY_INSTANCE;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.server.mp.dao.InventoryAttachmentDao;
import com.lead.fund.base.server.mp.dao.InventoryDao;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.douson.InventoryAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.InventoryEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.InventoryMapper;
import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.request.InventoryPageRequest;
import com.lead.fund.base.server.mp.request.InventoryRequest;
import com.lead.fund.base.server.mp.response.InventoryResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
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
 * DousonInventoryController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused"})
@RestController
@RequestMapping("/douson/inventory")
@Slf4j
@Validated
public class DousonInventoryController {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private UrlHelper urlHelper;
    @Resource
    private TemplatePhotoDao templatePhotoDao;
    @Resource
    private InventoryMapper inventoryMapper;
    @Resource
    private InventoryDao inventoryDao;
    @Resource
    private InventoryAttachmentDao inventoryAttachmentDao;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private ParamDao paramDao;
    @Resource
    private LockHelper lockHelper;

    /**
     * 保存、更新计划外库存
     *
     * @param deviceId 设备id
     * @param request  {@link InventoryRequest}
     * @return {@link Result}
     */
    @PutMapping("merge")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result merge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody InventoryRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "inventoryManager".equals(t) || "inventoryView".equals(t))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final InventoryEntity db = isBlank(request.getInventoryId()) ? null : inventoryDao.getById(request.getInventoryId());
        final InventoryEntity e = INVENTORY_INSTANCE.inventory(request);
        e.setMaterialCount(defaultDecimal(e.getMaterialCount())).setInventoryCount(defaultDecimal(e.getInventoryCount()));
        if (BigDecimal.ZERO.equals(e.getMaterialCount())) {
            e.setMaterialDate("");
        } else {
            e.setMaterialDate(null == db || !defaultDecimal(db.getMaterialCount()).equals(e.getMaterialCount()) ? DateUtil.day(new Date()) : db.getMaterialDate());
        }
        if (isBlank(e.getId())) {
            inventoryDao.save(e);
        } else {
            inventoryDao.updateById(e);
        }
        inventoryAttachmentDao.remove(new LambdaUpdateWrapper<InventoryAttachmentEntity>().eq(InventoryAttachmentEntity::getInventoryId, e.getId()));
        inventoryAttachmentDao.saveBatch(
                Stream.of(
                                request.getPhotoList().stream().map(t -> new InventoryAttachmentEntity()
                                        .setInventoryId(e.getId())
                                        .setAttachmentCategory("0")
                                        .setAttachmentType("0")
                                        .setUrl(t.getPhotoUrl())
                                        .setCompressUrl(t.getPhotoCompressUrl())),
                                request.getAttachmentList().stream().map(t -> new InventoryAttachmentEntity()
                                        .setInventoryId(e.getId())
                                        .setAttachmentCategory("1")
                                        .setAttachmentType("1")
                                        .setUrl(t.getUrl())
                                        .setFileId(t.getFileId())
                                        .setFilename(t.getFilename())
                                        .setCompressUrl(t.getUrl()))
                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );
        return new DataResult<>(e);
    }

    /**
     * 删除计划外库存
     *
     * @param deviceId 设备id
     * @param request  {@link InventoryRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result delete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute InventoryRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "inventoryManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        inventoryDao.removeById(request.getInventoryId());
        return new Result();
    }


    private List<InventoryEntity> inventoryList(InventoryRequest d) {
        LambdaQueryWrapper<InventoryEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getInventoryId())) {
            lambda.eq(InventoryEntity::getId, d.getInventoryId());
        }
        if (isNotBlank(d.getMaterialNo())) {
            lambda.like(InventoryEntity::getMaterialNo, d.getMaterialNo());
        }
        if (isNotBlank(d.getMaterialDescription())) {
            lambda.like(InventoryEntity::getMaterialDescription, d.getMaterialDescription());
        }
        if (isNotBlank(d.getDesignNumber())) {
            lambda.like(InventoryEntity::getDesignNumber, d.getDesignNumber());
        }
        if (isNotBlank(d.getType())) {
            lambda.like(InventoryEntity::getType, d.getType());
        }
        if (isNotBlank(d.getMaterialDate())) {
            lambda.eq(InventoryEntity::getMaterialDate, d.getMaterialDate());
        }
        if (isNotBlank(d.getStartMaterialDate())) {
            lambda.ge(InventoryEntity::getMaterialDate, d.getStartMaterialDate());
        }
        if (isNotBlank(d.getEndMaterialDate())) {
            lambda.le(InventoryEntity::getMaterialDate, d.getEndMaterialDate());
        }
        if (isNotBlank(d.getDescription())) {
            lambda.like(InventoryEntity::getDescription, d.getDescription());
        }
        if (null != d.getInventoryCountType()) {
            lambda.apply("(INVENTORY_COUNT = 0 OR INVENTORY_COUNT != MATERIAL_COUNT)");
        }
        return inventoryMapper.selectList(lambda.orderByDesc(InventoryEntity::getInventoryDate));
    }


    private List<InventoryResponse> formatInventoryList(List<InventoryEntity> list) {
        List<InventoryResponse> rl = INVENTORY_INSTANCE.inventoryList(list);
        final Map<String, List<InventoryAttachmentEntity>> am = inventoryAttachmentDao.list(
                        DatabaseUtil.or(new LambdaQueryWrapper<>(), rl.stream().map(InventoryResponse::getInventoryId).collect(Collectors.toList()), (lam, l) -> lam.in(InventoryAttachmentEntity::getInventoryId, l))
                ).stream()
                .collect(Collectors.groupingBy(InventoryAttachmentEntity::getInventoryId));
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getType())).collect(Collectors.toList()),
                InventoryResponse::getType,
                l -> paramDao.listByCategoryId("inventoryOutOfPlanType"),
                (t, r) -> t.getType().equals(r.getValue()),
                (t, r) -> t.setTypeFormat(r.getLabel())
        );
        for (InventoryResponse t : rl) {
            t.setPhotoList(am.getOrDefault(t.getInventoryId(), new ArrayList<>())
                    .stream().filter(tt -> "0".equals(tt.getAttachmentCategory()) && "0".equals(tt.getAttachmentType()))
                    .map(tt -> new PhotoImgModel()
                            .setPhotoUrl(tt.getUrl())
                            .setFullPhotoUrl(urlHelper.fullUrl(tt.getUrl()))
                            .setPhotoCompressUrl(tt.getCompressUrl())
                            .setFullPhotoCompressUrl(urlHelper.fullUrl(tt.getCompressUrl()))
                    )
                    .collect(Collectors.toList()));
            t.setAttachmentList(am.getOrDefault(t.getInventoryId(), new ArrayList<>())
                    .stream().filter(tt -> "1".equals(tt.getAttachmentCategory()) && "1".equals(tt.getAttachmentType()))
                    .map(tt -> new FileModel()
                            .setUrl(tt.getUrl())
                            .setFullUrl(urlHelper.fullUrl(tt.getUrl()))
                            .setFileId(tt.getFileId())
                            .setFilename(tt.getFilename())
                    )
                    .collect(Collectors.toList()));
            t.setPhotoCount(new BigDecimal(t.getPhotoList().size()));
        }
        return rl;
    }

    /**
     * 计划外库存分页
     *
     * @param deviceId 设备id
     * @param request  {@link InventoryPageRequest}
     * @return {@link PageResult <InventoryResponse>}
     */
    @GetMapping("page")
    public PageResult<InventoryResponse> inventoryAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute InventoryPageRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "admin".equals(t.getRoleCode()) || "inventoryManager".equals(t.getRoleCode()) || "inventoryView".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
            return new PageResult<>(0, new ArrayList<>());
        }
        final PageResult<InventoryEntity> pr = DatabaseUtil.page(request, this::inventoryList);
        final AtomicInteger index = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        final AtomicInteger deviceIndex = new AtomicInteger(0);
        final List<InventoryResponse> l = formatInventoryList(pr.getList());
        return new PageResult<>(
                pr.getTotal(),
                l.stream().peek(t -> {
                    t.setIndex(index.addAndGet(1));
                }).collect(Collectors.toList())
        );
    }

}
