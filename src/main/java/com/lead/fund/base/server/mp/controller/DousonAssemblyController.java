package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.NumberUtil.defaultDecimal;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_UPLOAD_EXCEL_ERROR;
import static com.lead.fund.base.server.mp.converter.AssemblyConverter.ASSEMBLY_INSTANCE;
import static com.lead.fund.base.server.mp.util.ExcelUtil.getCellValue;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.AssemblyAttachmentDao;
import com.lead.fund.base.server.mp.dao.AssemblyDao;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.TaskDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.AssemblyAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.AssemblyEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.AssemblyAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.AssemblyMapper;
import com.lead.fund.base.server.mp.request.AssemblyPageRequest;
import com.lead.fund.base.server.mp.request.AssemblyRequest;
import com.lead.fund.base.server.mp.response.AssemblyResponse;
import com.lead.fund.base.server.mp.response.AssemblyUploadResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * DousonAssemblyController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused"})
@RestController
@RequestMapping("/douson/assembly")
@Slf4j
@Validated
public class DousonAssemblyController {

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

    /**
     * 上传整机装配
     *
     * @param file 单个图片 {@link MultipartFile}
     * @return {@link DataResult<AssemblyUploadResponse>}
     */
    @PostMapping("upload")
    public DataResult<AssemblyUploadResponse> upload(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        boolean assembly = u.getRoleList().stream().noneMatch(t -> "assembly".equals(t.getRoleCode()));
        final AssemblyUploadResponse res = new AssemblyUploadResponse();
        final String today = DateUtil.day(new Date());
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            final List<AssemblyRequest> el = new ArrayList<>();
            final XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                final Row row = sheet.getRow(i);
                if (null != row) {
                    int ci = 0;
                    final AssemblyRequest r = new AssemblyRequest()
                            .setPurchaseOrderNo(getCellValue(row.getCell(ci++)))
                            .setPoProject(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setSaleOrderNo(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setOrderProject(getCellValue(row.getCell(ci++)))
                            .setMaterialNo(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setMaterialDescription(getCellValue(row.getCell(ci++)))
                            .setDesignNumber(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setDeliveryDate(DateUtil.day(defaultIfBlank(getCellValue(row.getCell(ci++)), today)))
                            .setOrderCount(defaultDecimal(getCellValue(row.getCell(ci++))).setScale(0, RoundingMode.HALF_UP).intValue())
                            .setCompletedQty(defaultDecimal(getCellValue(row.getCell(ci++))).setScale(0, RoundingMode.HALF_UP).intValue())
                            .setValveBody(getCellValue(row.getCell(ci++)))
                            .setValveCover(getCellValue(row.getCell(ci++)))
                            .setGate(getCellValue(row.getCell(ci++)))
                            .setValveSeat(getCellValue(row.getCell(ci++)))
                            .setValveStem(getCellValue(row.getCell(ci++)));
                    ;
                    el.add(r);
                }
            }
            final List<AssemblyEntity> il = new ArrayList<>();
            final Map<List<String>, String> orderNoMap = new HashMap<>(8);
            final Map<List<String>, String> indexMap = new HashMap<>(8);
            for (AssemblyRequest t : el) {
                final AssemblyEntity e = (AssemblyEntity) ASSEMBLY_INSTANCE.assembly(t)
                        .setSerialIndex(0)
                        .setMaxSerialIndex(0)
                        .setMaxSerialOrderIndex(0)
                        .setSerialNumber(t.getPurchaseOrderNo() + " " + t.getPoProject() + " " + StrUtil.padPre("0", 3, "0"))
                        .setCreator(u.getUserId())
                        .setModifier(u.getUserId());
                il.add(e);
            }
            assemblyDao.saveBatch(
                    Stream.of(
                                    il
                            ).flatMap(Collection::stream)
                            .toList()
            );
        } catch (Exception e) {
            throw new BusinessException(MP_UPLOAD_EXCEL_ERROR).setOriginException(e);
        }
        return new DataResult<>(res);
    }

    /**
     * 保存、更新整机装配
     *
     * @param deviceId 设备id
     * @param request  {@link AssemblyRequest}
     * @return {@link Result}
     */
    @PutMapping("merge")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result merge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody AssemblyRequest request
    ) {
        final Date now = new Date();
        final String today = DateUtil.day(now);
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final AssemblyEntity e = (AssemblyEntity) ASSEMBLY_INSTANCE.assembly(request)
                .setModifier(u.getUserId());
        // update
        if (isNotBlank(e.getId())) {
            if (u.getRoleList().stream().noneMatch(t -> "assemblyManager".equals(t.getRoleCode()) || "assemblyRecord".equals(t.getRoleCode()) || "assemblyTesterRecord".equals(t.getRoleCode()))) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            if (assemblyMapper.updateById((AssemblyEntity) e
            ) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            // insert
        } else {
            if (u.getRoleList().stream().noneMatch(t -> "assemblyManager".equals(t.getRoleCode()))) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            assemblyMapper.insert((AssemblyEntity) e
                    .setCreator(u.getUserId())
            );
        }
        updateSummaryInfo(e, request);
        return new DataResult<>(e);
    }

    /**
     * 生成整机装配列表
     *
     * @param deviceId 设备id
     * @param request  {@link AssemblyRequest}
     * @return {@link Result}
     */
    @PostMapping("generate-list")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result generateList(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody AssemblyRequest request
    ) {
        final Date now = new Date();
        final String today = DateUtil.day(now);
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final AssemblyEntity db = assemblyMapper.selectById(request.getAssemblyId());
        final List<AssemblyEntity> l = new ArrayList<>();
        for (int i = 0; i < request.getCreateOrderCount(); i++) {
            final AssemblyEntity e = ASSEMBLY_INSTANCE.assembly(request);
            e
                    .setMaxSerialOrderIndex(db.getMaxSerialOrderIndex() + (i + 1))
                    .setMaxSerialIndex(db.getMaxSerialIndex() + (i + 1))
                    .setSerialIndex(db.getMaxSerialIndex() + (i + 1))
                    .setSerialNumber(e.getPurchaseOrderNo() + " " + e.getPoProject() + " " + StrUtil.padPre(String.valueOf(e.getSerialIndex()), 3, "0"))
                    .setCreator(u.getUserId())
                    .setModifier(u.getUserId())
                    .setId(null)
            ;
            l.add(e);
        }
        assemblyDao.saveBatch(l);
        assemblyMapper.update(null,
                new LambdaUpdateWrapper<AssemblyEntity>()
                        .setSql(true, "MAX_SERIAL_INDEX = MAX_SERIAL_INDEX + " + request.getCreateOrderCount())
                        .eq(AssemblyEntity::getPurchaseOrderNo, db.getPurchaseOrderNo())
                        .eq(AssemblyEntity::getPoProject, db.getPoProject())
        );
        assemblyMapper.update(null,
                new LambdaUpdateWrapper<AssemblyEntity>()
                        .setSql(true, "MAX_SERIAL_ORDER_INDEX = MAX_SERIAL_ORDER_INDEX + " + request.getCreateOrderCount())
                        .eq(AssemblyEntity::getPurchaseOrderNo, db.getPurchaseOrderNo())
                        .eq(AssemblyEntity::getPoProject, db.getPoProject())
                        .eq(AssemblyEntity::getSaleOrderNo, db.getSaleOrderNo())
                        .eq(AssemblyEntity::getOrderProject, db.getOrderProject())
        );
        return new DataResult<>(db);
    }

    /**
     * 删除整机装配
     *
     * @param deviceId 设备id
     * @param request  {@link AssemblyRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result delete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute AssemblyRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername()) && u.getRoleCodeList().stream().noneMatch("assemblyManager"::equals)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        if (isNotBlank(request.getAssemblyId())) {
            assemblyDao.removeById(request.getAssemblyId());
        }
        return new Result();
    }

    private List<AssemblyEntity> assemblyList(AssemblyRequest d) {
        LambdaQueryWrapper<AssemblyEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getAssemblyId())) {
            lambda.eq(AssemblyEntity::getId, d.getAssemblyId());
        }
        if (isNotBlank(d.getSerialNumber())) {
            lambda.like(AssemblyEntity::getSerialNumber, d.getSerialNumber());
        }
        if (isNotBlank(d.getPurchaseOrderNo())) {
            lambda.like(AssemblyEntity::getPurchaseOrderNo, d.getPurchaseOrderNo());
        }
        if (isNotBlank(d.getPoProject())) {
            lambda.like(AssemblyEntity::getPoProject, d.getPoProject());
        }
        if (isNotBlank(d.getSaleOrderNo())) {
            lambda.like(AssemblyEntity::getSaleOrderNo, d.getSaleOrderNo());
        }
        if (isNotBlank(d.getOrderProject())) {
            lambda.like(AssemblyEntity::getOrderProject, d.getOrderProject());
        }
        if (isNotBlank(d.getMaterialNo())) {
            lambda.like(AssemblyEntity::getMaterialNo, d.getMaterialNo());
        }
        if (isNotBlank(d.getMaterialDescription())) {
            lambda.like(AssemblyEntity::getMaterialDescription, d.getMaterialDescription());
        }
        if (isNotBlank(d.getDesignNumber())) {
            lambda.like(AssemblyEntity::getDesignNumber, d.getDesignNumber());
        }
        if (null != d.getStartDeliveryDate()) {
            lambda.ge(AssemblyEntity::getDeliveryDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(d.getStartDeliveryDate())));
        }
        if (null != d.getEndDeliveryDate()) {
            lambda.le(AssemblyEntity::getDeliveryDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(d.getEndDeliveryDate())));
        }
        if (isNotBlank(d.getOrderCount())) {
            lambda.eq(AssemblyEntity::getOrderCount, d.getOrderCount());
        }
        if (isNotBlank(d.getCompletedQty())) {
            lambda.eq(AssemblyEntity::getCompletedQty, d.getCompletedQty());
        }
        if (isNotBlank(d.getDescription())) {
            lambda.like(AssemblyEntity::getDescription, d.getDescription());
        }
        return assemblyDao.list(lambda.orderByDesc(AssemblyEntity::getPurchaseOrderNo).orderByDesc(AssemblyEntity::getPoProject).orderByDesc(AssemblyEntity::getSaleOrderNo).orderByDesc(AssemblyEntity::getOrderProject).orderByAsc(AssemblyEntity::getSerialIndex));
    }

    private List<AssemblyResponse> formatAssemblyList(List<AssemblyEntity> list) {
        List<AssemblyResponse> rl = ASSEMBLY_INSTANCE.assemblyList(list);
        List<String> userIdList = Stream.of(
                        rl.stream().map(AssemblyResponse::getCreator).filter(StrUtil::isNotBlank)
                )
                .flatMap(t -> t)
                .distinct()
                .collect(Collectors.toList());
        final List<MpUserEntity> userList = CollUtil.isEmpty(userIdList) ? new ArrayList<>() : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        );
        MultitaskUtil.supplementList(
                rl,
                AssemblyResponse::getAssemblyId,
                l1 -> assemblyAttachmentMapper.selectList(new LambdaQueryWrapper<AssemblyAttachmentEntity>()
                        .in(AssemblyAttachmentEntity::getAssemblyId, l1)),
                (t, r) -> t.getAssemblyId().equals(r.getAssemblyId()),
                (t, r) -> {
                    switch (r.getAttachmentCategory()) {
                        case "valveBody" -> t.getValveBodyPhotoList().add(
                                ASSEMBLY_INSTANCE.photo(r, urlHelper.getUrlPrefix())
                        );
                        case "valveCover" -> t.getValveCoverPhotoList().add(
                                ASSEMBLY_INSTANCE.photo(r, urlHelper.getUrlPrefix())
                        );
                        case "gate" -> t.getGatePhotoList().add(
                                ASSEMBLY_INSTANCE.photo(r, urlHelper.getUrlPrefix())
                        );
                        case "valveSeat" -> t.getValveSeatPhotoList().add(
                                ASSEMBLY_INSTANCE.photo(r, urlHelper.getUrlPrefix())
                        );
                        case "valveStem" -> t.getValveStemPhotoList().add(
                                ASSEMBLY_INSTANCE.photo(r, urlHelper.getUrlPrefix())
                        );
                        case "pressureTest" -> t.getPressureTestPhotoList().add(
                                ASSEMBLY_INSTANCE.photo(r, urlHelper.getUrlPrefix())
                        );
                        case "oilInjection" -> t.getOilInjectionPhotoList().add(
                                ASSEMBLY_INSTANCE.photo(r, urlHelper.getUrlPrefix())
                        );
                        default -> log.info("accident attachment: {}", JSONUtil.toJsonStr(r));
                    }
                }
        );
        for (AssemblyResponse t : rl) {
            t
                    .setGatePhotoCount(t.getGatePhotoList().size())
                    .setValveSeatPhotoCount(t.getValveSeatPhotoList().size())
                    .setValveStemPhotoCount(t.getValveStemPhotoList().size())
                    .setPressureTestPhotoCount(t.getPressureTestPhotoList().size())
                    .setValveBodyPhotoCount(t.getValveBodyPhotoList().size())
                    .setValveCoverPhotoCount(t.getValveCoverPhotoList().size())
                    .setOilInjectionPhotoCount(t.getOilInjectionPhotoList().size())
                    ;
        }
        return rl;
    }

    private static String tail(BigDecimal v) {
        return null == v || v.compareTo(BigDecimal.ZERO) <= 0 ? "" : ("-" + v.setScale(0, RoundingMode.HALF_UP));
    }

    /**
     * 整机装配分页
     *
     * @param deviceId 设备id
     * @param request  {@link AssemblyPageRequest}
     * @return {@link PageResult <AssemblyResponse>}
     */
    @GetMapping("page")
    public PageResult<AssemblyResponse> assemblyAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute AssemblyPageRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().anyMatch(t -> "assemblyManager".equals(t.getRoleCode()))) {
        }
        final PageResult<AssemblyEntity> pr = DatabaseUtil.page(request, this::assemblyList);
        final AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(pr.getTotal(), formatAssemblyList(pr.getList())
                .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    private void updateSummaryInfo(AssemblyEntity e, AssemblyRequest request) {
        assemblyAttachmentMapper.delete(new LambdaUpdateWrapper<AssemblyAttachmentEntity>()
                .eq(AssemblyAttachmentEntity::getAssemblyId, e.getId())
        );
        assemblyAttachmentDao.saveBatch(
                Stream.of(
                                CollUtil.defaultIfEmpty(request.getValveBodyPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> ASSEMBLY_INSTANCE.attachment(e.getId(), "valveBody", t)),
                                CollUtil.defaultIfEmpty(request.getValveCoverPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> ASSEMBLY_INSTANCE.attachment(e.getId(), "valveCover", t)),
                                CollUtil.defaultIfEmpty(request.getGatePhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> ASSEMBLY_INSTANCE.attachment(e.getId(), "gate", t)),
                                CollUtil.defaultIfEmpty(request.getValveSeatPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> ASSEMBLY_INSTANCE.attachment(e.getId(), "valveSeat", t)),
                                CollUtil.defaultIfEmpty(request.getValveStemPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> ASSEMBLY_INSTANCE.attachment(e.getId(), "valveStem", t)),
                                CollUtil.defaultIfEmpty(request.getPressureTestPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> ASSEMBLY_INSTANCE.attachment(e.getId(), "pressureTest", t)),
                                CollUtil.defaultIfEmpty(request.getOilInjectionPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> ASSEMBLY_INSTANCE.attachment(e.getId(), "oilInjection", t))
                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );

//        final List<AssemblyEntity> el = assemblyMapper.selectList(
//                new LambdaQueryWrapper<AssemblyEntity>()
//                        .eq(AssemblyEntity::getSaleOrderNo, e.getSaleOrderNo())
//        );
//        assemblyMapper.update(null,
//                new LambdaUpdateWrapper<AssemblyEntity>()
//                        .eq(AssemblyEntity::getSaleOrderNo, e.getSaleOrderNo())
//        );
    }
}
