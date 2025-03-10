package com.lead.fund.base.server.mp.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.ListResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.*;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.AssemblyAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.AssemblyEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.AssemblyAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.AssemblyMapper;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.request.AssemblyPageRequest;
import com.lead.fund.base.server.mp.request.AssemblyRequest;
import com.lead.fund.base.server.mp.response.AssemblyResponse;
import com.lead.fund.base.server.mp.response.AssemblySummaryResponse;
import com.lead.fund.base.server.mp.response.AssemblyUploadResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.NumberUtil.defaultDecimal;
import static com.lead.fund.base.common.util.NumberUtil.defaultInt;
import static com.lead.fund.base.common.util.StrUtil.*;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_UPLOAD_EXCEL_ERROR;
import static com.lead.fund.base.server.mp.converter.AssemblyConverter.ASSEMBLY_INSTANCE;
import static com.lead.fund.base.server.mp.util.ExcelUtil.getCellValue;
import static org.checkerframework.checker.nullness.Opt.orElse;

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
     * 模板下载
     *
     * @param resp {@link HttpServletResponse}
     * @return excel
     */
    @GetMapping("template")
    public ResponseEntity<byte[]> template(HttpServletResponse resp) {
        byte[] assemblyTemplateBytes = IoUtil.readBytes(this.getClass().getResourceAsStream("/assembly_template.xlsx"));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            headers.setContentDispositionFormData("attachment", new String(("assembly_template" + ".xlsx").getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            log.warn("set attachment error");
        }
        resp.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        return new ResponseEntity<>(assemblyTemplateBytes, headers, HttpStatus.OK);
    }

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
                            .setOrderCount(defaultDecimal(getCellValue(row.getCell(ci++))).setScale(0, RoundingMode.HALF_UP).intValue())
                            .setDeliveryDate(DateUtil.day(defaultIfBlank(getCellValue(row.getCell(ci++)), today)))
                            .setCompletedQty(0)
                            .setAssemblyCompleteCount(0)
                            .setOilInjectionCompleteCount(0)
                            .setAssemblyIndex(assemblyDao.assemblyIndex(new AssemblyEntity()))
                            .setDescription(defaultIfBlank(getCellValue(row.getCell(ci++))))
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
                        .setCompletedQty(0)
                        .setAssemblyCompleteCount(0)
                        .setSerialNumber(t.getPurchaseOrderNo() + t.getPoProject() + StrUtil.padPre("0", 3, "0"))
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
                .setCompletedQty(defaultInt(request.getAssemblyCompleteCount()))
                .setAssemblyCompleteCount(defaultInt(request.getAssemblyCompleteCount()))
                .setOilInjectionCompleteCount(defaultInt(request.getOilInjectionCompleteCount()))
                .setModifier(u.getUserId());
        e.setSerialNumber(e.getPurchaseOrderNo() + e.getPoProject() + StrUtil.padPre(String.valueOf(e.getSerialIndex()), 3, "0"))
                .setAssemblyIndex(assemblyDao.assemblyIndex(e));
        // update
        if (isNotBlank(e.getId())) {
            if (u.getRoleList().stream().noneMatch(t -> "assemblyManager".equals(t.getRoleCode()) || "assemblyRecord".equals(t.getRoleCode()) || "assemblyTesterRecord".equals(t.getRoleCode()))) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            if (assemblyMapper.update(
                    e,
                    new LambdaUpdateWrapper<AssemblyEntity>()
                            .eq(AssemblyEntity::getId, e.getId())
                            .le(AssemblyEntity::getLastModifiedTime, DateUtil.parse(request.getModifyTime()))
            ) <= 0) {
                if (assemblyMapper.selectById(e.getId()).getLastModifiedTime().compareTo(DateUtil.parse(request.getModifyTime())) > 0) {
                    throw new BusinessException(AUTHORITY_AUTH_FAIL.getCode(), "数据已被修改，请重新获取数据。（Please reload data）");
                }
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
                    .setSerialNumber(e.getPurchaseOrderNo() + e.getPoProject() + StrUtil.padPre(String.valueOf(e.getSerialIndex()), 3, "0"))
                    .setOrderCount(1)
                    .setCompletedQty(0)
                    .setAssemblyIndex(request.getAssemblyIndex())
                    .setAssemblyCompleteCount(0)
                    .setOilInjectionCompleteCount(0)
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
            final AssemblyEntity db = assemblyDao.getById(request.getAssemblyId());
            if (null != db) {
                /*if (db.getSerialIndex() == 0) {
                    assemblyDao.list(new LambdaQueryWrapper<AssemblyEntity>()
                            .eq(AssemblyEntity::getPurchaseOrderNo, db.getPurchaseOrderNo())
                            .eq(AssemblyEntity::getPoProject, db.getPoProject())
                            .eq(AssemblyEntity::getSaleOrderNo, db.getSaleOrderNo())
                            .eq(AssemblyEntity::getOrderProject, db.getOrderProject())
                            .select(AssemblyEntity::getId)
                    ).forEach(t -> {
                        assemblyAttachmentDao.remove(new LambdaUpdateWrapper<AssemblyAttachmentEntity>()
                                .eq(AssemblyAttachmentEntity::getAssemblyId, t.getId())
                        );
                    });
                    assemblyDao.remove(new LambdaUpdateWrapper<AssemblyEntity>()
                            .eq(AssemblyEntity::getPurchaseOrderNo, db.getPurchaseOrderNo())
                            .eq(AssemblyEntity::getPoProject, db.getPoProject())
                            .eq(AssemblyEntity::getSaleOrderNo, db.getSaleOrderNo())
                            .eq(AssemblyEntity::getOrderProject, db.getOrderProject())
                    );
                } else {
                }*/
                assemblyDao.removeById(request.getAssemblyId());
                assemblyAttachmentDao.remove(new LambdaUpdateWrapper<AssemblyAttachmentEntity>()
                        .eq(AssemblyAttachmentEntity::getAssemblyId, db.getId())
                );
            }
        }
        return new Result();
    }

    private List<AssemblyEntity> assemblyList(AssemblyRequest d) {
        return assemblyList(d, null);
    }

    private List<AssemblyEntity> assemblyList(AssemblyRequest d, Consumer<LambdaQueryWrapper<AssemblyEntity>> consumer) {
        LambdaQueryWrapper<AssemblyEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getAssemblyId())) {
            lambda.eq(AssemblyEntity::getId, d.getAssemblyId());
        }
        if (isNotBlank(d.getSerialNumber())) {
            lambda.like(AssemblyEntity::getSerialNumber, d.getSerialNumber().replace(" ", ""));
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
        if (null != d.getStartOilInjectionCompleteDate()) {
            lambda.ge(AssemblyEntity::getOilInjectionCompleteDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(d.getStartOilInjectionCompleteDate())));
        }
        if (null != d.getEndOilInjectionCompleteDate()) {
            lambda.le(AssemblyEntity::getOilInjectionCompleteDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(d.getEndOilInjectionCompleteDate())));
        }
        if (null != d.getStartAssemblyCompleteDate()) {
            lambda.ge(AssemblyEntity::getAssemblyCompleteDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(d.getStartAssemblyCompleteDate())) + " 08:00:00");
        }
        if (null != d.getEndAssemblyCompleteDate()) {
            lambda.le(AssemblyEntity::getAssemblyCompleteDate, DateUtil.day(cn.hutool.core.date.DateUtil.offsetDay(DateTime.of(d.getEndAssemblyCompleteDate()), 1)) + " 08:00:00");
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
        if (isNotBlank(d.getValveBody())) {
            lambda.like(AssemblyEntity::getValveBody, d.getValveBody());
        }
        if (null != d.getAssemblyCompleteType()) {
            if (0 == d.getAssemblyCompleteType()) {
                lambda.apply("ASSEMBLY_COMPLETE_COUNT >= ORDER_COUNT");
            } else if (1 == d.getAssemblyCompleteType()) {
                lambda.apply("(ASSEMBLY_COMPLETE_COUNT < ORDER_COUNT)");
            }
        }
        if (null != d.getOilInjectionCompleteType()) {
            if (0 == d.getOilInjectionCompleteType()) {
                lambda.apply("OIL_INJECTION_COMPLETE_COUNT >= ORDER_COUNT");
            } else if (1 == d.getOilInjectionCompleteType()) {
                lambda.apply("(OIL_INJECTION_COMPLETE_COUNT < ORDER_COUNT)");
            }
        }
        lambda.orderByDesc(AssemblyEntity::getAssemblyIndex).orderByAsc(AssemblyEntity::getSerialIndex);
        if (null != consumer) {
            consumer.accept(lambda);
        }
        return assemblyDao.list(lambda);
    }

    private List<AssemblyResponse> formatAssemblyList(List<AssemblyEntity> list) {
        List<AssemblyResponse> rl = ASSEMBLY_INSTANCE.assemblyList(list);
        List<String> userIdList = Stream.of(
                        rl.stream().map(AssemblyResponse::getCreator).filter(StrUtil::isNotBlank),
                        rl.stream().map(AssemblyResponse::getTester).filter(StrUtil::isNotBlank),
                        rl.stream().map(AssemblyResponse::getAssemblyPerson).filter(StrUtil::isNotBlank)
                )
                .flatMap(t -> t)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, String> um = CollUtil.isEmpty(userIdList) ? new HashMap<>(8) : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        ).stream().collect(Collectors.toMap(AbstractPrimaryKey::getId, MpUserEntity::getName, (t, t1) -> t1));
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
                    .setAssemblyPersonFormat(um.getOrDefault(t.getAssemblyPerson(), t.getAssemblyPerson()))
                    .setTesterFormat(um.getOrDefault(t.getTester(), t.getTester()))
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
        if (u.getRoleList().stream().noneMatch(t -> "assemblyManager".equals(t.getRoleCode()) || "assemblyRecord".equals(t.getRoleCode()) || "assemblyTesterRecord".equals(t.getRoleCode()) || "assemblyRecordView".equals(t.getRoleCode()) || "admin".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final PageResult<AssemblyEntity> pr = DatabaseUtil.page(request, this::assemblyList);
        final AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(pr.getTotal(), formatAssemblyList(pr.getList())
                .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 整机装配
     *
     * @param deviceId 设备id
     * @param request  {@link AssemblyRequest}
     * @return {@link DataResult<AssemblyResponse>}
     */
    @GetMapping("")
    public DataResult<AssemblyResponse> assembly(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute AssemblyRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "assemblyManager".equals(t.getRoleCode()) || "assemblyRecord".equals(t.getRoleCode()) || "assemblyTesterRecord".equals(t.getRoleCode()) || "assemblyRecordView".equals(t.getRoleCode()) || "admin".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        return new DataResult<>(CollUtil.getFirst(formatAssemblyList(assemblyList(request))));
    }

    /**
     * 汇总列表
     *
     * @param deviceId 设备id
     * @param request  {@link AssemblyRequest}
     * @return {@link ListResult <AssemblySummaryResponse>}
     */
    @GetMapping("summary-list")
    public ListResult<AssemblySummaryResponse> summaryList(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute AssemblyRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "assemblyManager".equals(t.getRoleCode()) || "assemblyRecord".equals(t.getRoleCode()) || "assemblyTesterRecord".equals(t.getRoleCode()) || "assemblyRecordView".equals(t.getRoleCode()) || "admin".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final List<AssemblyEntity> l = assemblyList(request, lambda -> {
            lambda.eq(AssemblyEntity::getSerialIndex, 0)
                    .select(
                            AssemblyEntity::getPurchaseOrderNo,
                            AssemblyEntity::getPoProject,
                            AssemblyEntity::getSaleOrderNo,
                            AssemblyEntity::getOrderProject,
                            AssemblyEntity::getMaterialNo,
                            AssemblyEntity::getOrderCount,
                            AssemblyEntity::getMaterialDescription,
                            AssemblyEntity::getDesignNumber,
                            AssemblyEntity::getDeliveryDate,
                            AssemblyEntity::getCompleteDate,
                            AssemblyEntity::getCompletedQty,
                            AssemblyEntity::getAssemblyCompleteDate,
                            AssemblyEntity::getAssemblyCompleteCount,
                            AssemblyEntity::getOilInjectionCompleteCount,
                            AssemblyEntity::getOilInjectionCompleteDate
                    );
        });
        final List<AssemblySummaryResponse> rl = ASSEMBLY_INSTANCE.assemblySummaryList(l)
                .stream().peek(t -> {
                    t.setDeliveryDate(DateUtil.day(t.getDeliveryDate()));
                    if (isNotBlank(t.getAssemblyCompleteDate()) && t.getCompletedQty() > 0) {
                        t.setAssemblyCompleteDate(DateUtil.day(t.getAssemblyCompleteDate()));
                    } else {
                        t.setAssemblyCompleteDate("--")
                                .setCompletedQty(0);
                    }
                })
                .sorted(Comparator.comparing(AssemblySummaryResponse::getAssemblyCompleteDate))
                .collect(Collectors.toList());
        rl.add(
                rl.stream().reduce(
                        new AssemblySummaryResponse()
                                .setPurchaseOrderNo("")
                                .setPoProject("")
                                .setSaleOrderNo("")
                                .setOrderProject("")
                                .setMaterialNo("")
                                .setMaterialDescription("")
                                .setDesignNumber("")
                                .setDeliveryDate("--")
                                .setAssemblyCompleteDate("合计")
                                .setAssemblyCompleteCount(0)
                                .setCompletedQty(0)
                                .setOilInjectionCompleteCount(0)
                                .setOrderCount(0)
                        ,
                        (t, t1) -> {
                            t
                                    .setAssemblyCompleteCount(t.getAssemblyCompleteCount() + t1.getAssemblyCompleteCount())
                                    .setOilInjectionCompleteCount(t.getOilInjectionCompleteCount() + t1.getOilInjectionCompleteCount())
                                    .setCompletedQty(t.getCompletedQty() + t1.getCompletedQty())
                                    .setOrderCount(t.getOrderCount() + t1.getOrderCount())
                            ;
                            return t;
                        }
                )
        );
        return new ListResult<>(
                rl.stream()
                        .peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 汇总列表2
     *
     * @param deviceId 设备id
     * @param request  {@link AssemblyRequest}
     * @return {@link ListResult <AssemblySummaryResponse>}
     */
    @GetMapping("summary-list2")
    public ListResult<AssemblySummaryResponse> summaryList2(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute AssemblyRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "assemblyManager".equals(t.getRoleCode()) || "assemblyRecord".equals(t.getRoleCode()) || "assemblyTesterRecord".equals(t.getRoleCode()) || "assemblyRecordView".equals(t.getRoleCode()) || "admin".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final List<AssemblyEntity> l = assemblyList(request, lambda -> {
            lambda
                    .ne(AssemblyEntity::getSerialIndex, 0)
                    .gt(AssemblyEntity::getCompletedQty, 0)
                    .isNotNull(AssemblyEntity::getCompleteDate)
                    .select(
                            AssemblyEntity::getPurchaseOrderNo,
                            AssemblyEntity::getPoProject,
                            AssemblyEntity::getSaleOrderNo,
                            AssemblyEntity::getOrderProject,
                            AssemblyEntity::getMaterialNo,
                            AssemblyEntity::getOrderCount,
                            AssemblyEntity::getMaterialDescription,
                            AssemblyEntity::getDesignNumber,
                            AssemblyEntity::getDeliveryDate,
                            AssemblyEntity::getCompleteDate,
                            AssemblyEntity::getCompletedQty,
                            AssemblyEntity::getAssemblyCompleteDate,
                            AssemblyEntity::getAssemblyCompleteCount,
                            AssemblyEntity::getOilInjectionCompleteCount,
                            AssemblyEntity::getOilInjectionCompleteDate
                    );
        });
        final DateTime now = DateTime.now();
        final String startTime = DateUtil.day(now) + " 08:00:00";
        final String endTime = DateUtil.day(cn.hutool.core.date.DateUtil.offsetDay(now, 1)) + " 08:00:00";
        final Map<String, Map<List<String>, AssemblySummaryResponse>> completeListMap = ASSEMBLY_INSTANCE.assemblySummaryList(l)
                .stream().peek(t -> {
                    final DateTime completeDate = DateUtil.parse(t.getAssemblyCompleteDate());
                    t.setAssemblyCompleteDateFormat(
                                    DateUtil.daySimple(DateUtil.hourSecond(completeDate).compareTo("08:00:00") < 0 ? cn.hutool.core.date.DateUtil.offsetDay(completeDate, -1) : completeDate)
                            )
                            .setDeliveryDateFormat(DateUtil.daySimple(t.getDeliveryDate()))
                    ;
                })
                .sorted(Comparator.comparing(AssemblySummaryResponse::getAssemblyCompleteDate))
                .collect(Collectors.groupingBy(
                        AssemblySummaryResponse::getAssemblyCompleteDateFormat,
                        Collectors.groupingBy(
                                t -> CollUtil.toList(t.getPurchaseOrderNo(), t.getPoProject(), t.getSaleOrderNo(), t.getOrderProject()),
                                Collectors.reducing(defaultAssemblySummary(), (t1, t2) -> {
                                    return defaultAssemblySummary()
                                            .setPurchaseOrderNo(t2.getPurchaseOrderNo())
                                            .setPoProject(t2.getPoProject())
                                            .setSaleOrderNo(t2.getSaleOrderNo())
                                            .setOrderProject(t2.getOrderProject())
                                            .setMaterialNo(t2.getMaterialNo())
                                            .setMaterialDescription(t2.getMaterialDescription())
                                            .setDesignNumber(t2.getDesignNumber())
                                            .setDeliveryDate(t2.getDeliveryDate())
                                            .setDeliveryDateFormat(t2.getDeliveryDateFormat())
                                            .setAssemblyCompleteDate(t2.getAssemblyCompleteDate())
                                            .setAssemblyCompleteDateFormat(t2.getAssemblyCompleteDateFormat())
                                            .setAssemblyCompleteCount(t1.getAssemblyCompleteCount() + t2.getAssemblyCompleteCount())
                                            .setCompletedQty(t1.getCompletedQty() + t2.getCompletedQty())
                                            .setOilInjectionCompleteCount(t1.getOilInjectionCompleteCount() + t2.getOilInjectionCompleteCount())
                                            .setOrderCount(t1.getOrderCount() + t2.getOrderCount())
                                            ;
                                })
                        )
                ));
        final List<AssemblySummaryResponse> rl = completeListMap.entrySet().stream().sorted(Comparator.comparing(Entry::getKey)).flatMap(t -> t.getValue().entrySet().stream().sorted(Comparator.comparing(o -> o.getKey().toString())).map(Entry::getValue)).collect(Collectors.toList());
        rl.add(
                rl.stream().reduce(
                        defaultAssemblySummary().setAssemblyCompleteDateFormat("合计")
                        ,
                        (t, t1) -> {
                            return defaultAssemblySummary().setAssemblyCompleteDateFormat("合计")
                                    .setAssemblyCompleteCount(t.getAssemblyCompleteCount() + t1.getAssemblyCompleteCount())
                                    .setOilInjectionCompleteCount(t.getOilInjectionCompleteCount() + t1.getOilInjectionCompleteCount())
                                    .setCompletedQty(t.getCompletedQty() + t1.getCompletedQty())
                                    .setOrderCount(t.getOrderCount() + t1.getOrderCount())
                                    ;
                        }
                )
        );
        return new ListResult<>(
                rl.stream()
                        .peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    private void updateSummaryInfo(AssemblyEntity e, AssemblyRequest request) {
        final List<AssemblyAttachmentEntity> attachList = assemblyAttachmentMapper.selectList(new LambdaQueryWrapper<AssemblyAttachmentEntity>()
                .eq(AssemblyAttachmentEntity::getAssemblyId, e.getId())
                .in(AssemblyAttachmentEntity::getAttachmentCategory, "valveBody", "oilInjection")
        );
        final String valveBodyString = attachList.stream().filter(t -> "valveBody".equals(t.getAttachmentCategory())).map(AssemblyAttachmentEntity::getUrl).sorted(String::compareTo).collect(Collectors.joining(","));
        final String oilInjectionString = attachList.stream().filter(t -> "oilInjection".equals(t.getAttachmentCategory())).map(AssemblyAttachmentEntity::getUrl).sorted(String::compareTo).collect(Collectors.joining(","));
        final String valveBodyRequestString = request.getValveBodyPhotoList().stream().map(PhotoImgModel::getPhotoUrl).sorted(String::compareTo).collect(Collectors.joining(","));
        final String oilInjectionRequestString = request.getOilInjectionPhotoList().stream().map(PhotoImgModel::getPhotoUrl).sorted(String::compareTo).collect(Collectors.joining(","));
        final boolean valveBodyChange = isNotBlank(valveBodyRequestString) && !valveBodyString.equals(valveBodyRequestString);
        final boolean oilInjectionChange = isNotBlank(oilInjectionRequestString) && !oilInjectionString.equals(oilInjectionRequestString);
        final String dateTime = DateUtil.dateTime(new Date());
        // 更新各种完成数量、及日期
        final LambdaUpdateWrapper<AssemblyEntity> updateLambda = new LambdaUpdateWrapper<AssemblyEntity>()
                .set(AssemblyEntity::getLastModifiedTime, DateTime.now())
                .eq(AssemblyEntity::getId, e.getId());
        if (valveBodyChange) {
            updateLambda
                    .set(AssemblyEntity::getAssemblyCompleteDate, dateTime)
                    .set(AssemblyEntity::getAssemblyCompleteCount, 1)
                    .set(AssemblyEntity::getAssemblyPerson, e.getModifier())
            ;
        } else if (isBlank(valveBodyRequestString)) {
            updateLambda
                    .set(AssemblyEntity::getAssemblyCompleteDate, "")
                    .set(AssemblyEntity::getAssemblyCompleteCount, 0)
                    .set(AssemblyEntity::getAssemblyPerson, "")
                    .set(AssemblyEntity::getCompleteDate, "")
                    .set(AssemblyEntity::getCompletedQty, 0)
            ;
        }
        if (oilInjectionChange) {
            updateLambda
                    .set(AssemblyEntity::getOilInjectionCompleteDate, dateTime)
                    .set(AssemblyEntity::getOilInjectionCompleteCount, 1)
                    .set(AssemblyEntity::getTester, e.getModifier())
            ;
        } else if (isBlank(oilInjectionRequestString)) {
            updateLambda
                    .set(AssemblyEntity::getOilInjectionCompleteDate, "")
                    .set(AssemblyEntity::getOilInjectionCompleteCount, 0)
                    .set(AssemblyEntity::getCompleteDate, "")
                    .set(AssemblyEntity::getCompletedQty, 0)
                    .set(AssemblyEntity::getTester, "")
            ;
        }
        assemblyMapper.update(null, updateLambda);
        // 更新附件
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
        // 更新完成数量及主数量
        final List<AssemblyEntity> assemblyList = assemblyMapper.selectList(new LambdaQueryWrapper<AssemblyEntity>()
                .ne(AssemblyEntity::getSerialIndex, 0)
                .eq(AssemblyEntity::getPurchaseOrderNo, e.getPurchaseOrderNo())
                .eq(AssemblyEntity::getPoProject, e.getPoProject())
                .eq(AssemblyEntity::getSaleOrderNo, e.getSaleOrderNo())
                .eq(AssemblyEntity::getOrderProject, e.getOrderProject())
                .select(AssemblyEntity::getId, AssemblyEntity::getCompletedQty, AssemblyEntity::getAssemblyCompleteCount, AssemblyEntity::getOilInjectionCompleteCount, AssemblyEntity::getAssemblyCompleteDate, AssemblyEntity::getOilInjectionCompleteDate, AssemblyEntity::getAssemblyPerson, AssemblyEntity::getTester)
        );
        for (AssemblyEntity t : assemblyList) {
            final int completeCount = Math.min(defaultInt(t.getAssemblyCompleteCount()), defaultInt(t.getOilInjectionCompleteCount()));
            final String completeDate = defaultIfBlank(defaultIfBlank(t.getAssemblyCompleteDate()).compareTo(defaultIfBlank(t.getOilInjectionCompleteDate())) >= 0 ? t.getAssemblyCompleteDate() : t.getOilInjectionCompleteDate());
            if (completeCount != defaultInt(t.getCompletedQty()) || !completeDate.equals(t.getCompleteDate())) {
                t.setCompletedQty(completeCount);
                final LambdaUpdateWrapper<AssemblyEntity> lam = new LambdaUpdateWrapper<AssemblyEntity>()
                        .set(AssemblyEntity::getCompletedQty, completeCount)
                        .eq(AssemblyEntity::getId, t.getId());
                if (completeCount > 0) {
                    if (defaultInt(t.getAssemblyCompleteCount()) > 0 && defaultInt(t.getOilInjectionCompleteCount()) > 0) {
                        t.setCompleteDate(completeDate);
                    }
                    lam.set(AssemblyEntity::getCompleteDate, t.getCompleteDate());
                } else {
                    lam.set(AssemblyEntity::getCompleteDate, "");
                }
                assemblyMapper.update(null, lam);
            }
        }
        final Integer completeCount = assemblyList.stream().map(t -> defaultInt(t.getCompletedQty())).reduce(0, Integer::sum);
        final Integer assemblyCompleteCount = assemblyList.stream().map(t -> defaultInt(t.getAssemblyCompleteCount())).reduce(0, Integer::sum);
        final Integer oilInjectionCompleteCount = assemblyList.stream().map(t -> defaultInt(t.getOilInjectionCompleteCount())).reduce(0, Integer::sum);
        final String assemblyCompleteDate = assemblyList.stream().map(AssemblyEntity::getAssemblyCompleteDate).filter(StrUtil::isNotBlank).max(String::compareTo).orElse("");
        final LambdaUpdateWrapper<AssemblyEntity> lam = new LambdaUpdateWrapper<AssemblyEntity>()
                .set(AssemblyEntity::getCompletedQty, completeCount)
                .set(AssemblyEntity::getAssemblyCompleteCount, assemblyCompleteCount)
                .set(AssemblyEntity::getOilInjectionCompleteCount, oilInjectionCompleteCount)
                .set(AssemblyEntity::getAssemblyCompleteDate, assemblyCompleteDate)
                .eq(AssemblyEntity::getSerialIndex, 0)
                .eq(AssemblyEntity::getPurchaseOrderNo, e.getPurchaseOrderNo())
                .eq(AssemblyEntity::getPoProject, e.getPoProject())
                .eq(AssemblyEntity::getSaleOrderNo, e.getSaleOrderNo())
                .eq(AssemblyEntity::getOrderProject, e.getOrderProject());
        assemblyList.stream().filter(t -> StrUtil.isNotBlank(t.getOilInjectionCompleteDate())).min((o1, o2) -> o2.getOilInjectionCompleteDate().compareTo(o1.getOilInjectionCompleteDate())).ifPresent(oilInjectionCompleteEntity -> lam
                .set(AssemblyEntity::getOilInjectionCompleteDate, oilInjectionCompleteEntity.getOilInjectionCompleteDate())
                .set(AssemblyEntity::getTester, oilInjectionCompleteEntity.getTester()));
        assemblyList.stream().filter(t -> StrUtil.isNotBlank(t.getAssemblyCompleteDate())).min((o1, o2) -> o2.getAssemblyCompleteDate().compareTo(o1.getAssemblyCompleteDate())).ifPresent(oilInjectionCompleteEntity -> lam
                .set(AssemblyEntity::getAssemblyCompleteDate, oilInjectionCompleteEntity.getAssemblyCompleteDate())
                .set(AssemblyEntity::getAssemblyPerson, oilInjectionCompleteEntity.getAssemblyPerson()));
        assemblyMapper.update(null, lam);
    }

    public static AssemblySummaryResponse defaultAssemblySummary() {
        return new AssemblySummaryResponse()
                .setPurchaseOrderNo("")
                .setPoProject("")
                .setSaleOrderNo("")
                .setOrderProject("")
                .setMaterialNo("")
                .setMaterialDescription("")
                .setDesignNumber("")
                .setDeliveryDate("--")
                .setDeliveryDateFormat("--")
                .setAssemblyCompleteDate("--")
                .setAssemblyCompleteDateFormat("--")
                .setAssemblyCompleteCount(0)
                .setCompletedQty(0)
                .setOilInjectionCompleteCount(0)
                .setOrderCount(0)
                ;
    }
}
