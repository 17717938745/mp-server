package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.NumberUtil.defaultDecimal;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_DATA_QUERY_ERROR;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_UPLOAD_EXCEL_ERROR;
import static com.lead.fund.base.server.mp.converter.MaterialConverter.MATERIAL_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.cons.frame.AdminState;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.ListResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.MaterialDao;
import com.lead.fund.base.server.mp.dao.MaterialDetailDao;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.TaskDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.MaterialEntity;
import com.lead.fund.base.server.mp.entity.douson.TaskEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.MaterialDetailMapper;
import com.lead.fund.base.server.mp.mapper.douson.MaterialMapper;
import com.lead.fund.base.server.mp.request.MaterialPageRequest;
import com.lead.fund.base.server.mp.request.MaterialRequest;
import com.lead.fund.base.server.mp.response.MaterialResponse;
import com.lead.fund.base.server.mp.response.MaterialUploadResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
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
 * DousonMaterialController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused"})
@RestController
@RequestMapping("/douson/material")
@Slf4j
@Validated
public class DousonMaterialController {

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


    /**
     * 保存、更新生产工单
     *
     * @param deviceId 设备id
     * @param request  {@link MaterialRequest}
     * @return {@link Result}
     */
    @PutMapping("merge")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result merge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody MaterialRequest request
    ) {
        final Date now = new Date();
        final String today = DateUtil.day(now);
        final MpUserResponse u = accountHelper.getUser(deviceId);
        MaterialEntity e = (MaterialEntity) MATERIAL_INSTANCE.material(request);
        e.setRemainCount(e.getMaterialCount().subtract(e.getProductionCount()));
        lockHelper.lock("material");
        try {
            final List<String> key = CollUtil.toList(request.getSaleOrderNo(), request.getOrderProjectNo());
            e
                    .setArrangeProductionDate(defaultDecimal(e.getProductionCount()).compareTo(BigDecimal.ZERO) > 0 ? today : null)
                    .setGenerateTask(defaultDecimal(e.getProductionCount()).compareTo(BigDecimal.ZERO) > 0 ? true : null)
                    .setModifier(u.getUserId());
            final boolean alreadyGenerateTask = isNotBlank(e.getId()) && Boolean.TRUE.equals(materialMapper.selectById(e.getId()).getGenerateTask());
            // update
            if (isNotBlank(e.getId())) {
                if (u.getRoleList().stream().noneMatch(t -> "material".equals(t.getRoleCode()) || "materialManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
                    throw new BusinessException(AUTHORITY_AUTH_FAIL);
                }
                if (materialMapper.updateById((MaterialEntity) e
                        .setCreator(u.getUserId())
                ) <= 0) {
                    throw new BusinessException(AUTHORITY_AUTH_FAIL);
                }
                // insert
            } else {
                if (u.getRoleList().stream().noneMatch(t -> "material".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
                    throw new BusinessException(AUTHORITY_AUTH_FAIL);
                }
                e
                        .setMaterialOrderNo(materialDao.nextMaterialOrderNo())
                        .setCheckOrderNo(materialDao.nextCheckOrderNo());
                materialMapper.insert(e);
            }
            updateSummaryInfo(e, today);
            if (!alreadyGenerateTask && Boolean.TRUE.equals(e.getGenerateTask())) {
                taskDao.merge((TaskEntity) MATERIAL_INSTANCE.generateTask(e)
                        .setCreator(u.getUserId())
                        .setModifier(u.getUserId())
                        .setState(AdminState.NORMAL.getCode())
                        .setCreateTime(now)
                        .setModifyTime(now)
                        .setId(null));
            }
        } finally {
            lockHelper.unlock("material");
        }
        return new DataResult<>(e);
    }

    /**
     * 上传生产工单
     *
     * @param file 单个图片 {@link MultipartFile}
     * @return {@link DataResult<MaterialUploadResponse>}
     */
    @PostMapping("upload")
    public DataResult<MaterialUploadResponse> upload(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        boolean material = u.getRoleList().stream().noneMatch(t -> "material".equals(t.getRoleCode()));
        final MaterialUploadResponse res = new MaterialUploadResponse();
        final String today = DateUtil.day(new Date());
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            final List<MaterialRequest> el = new ArrayList<>();
            final XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                final Row row = sheet.getRow(i);
                if (null != row) {
                    int ci = 0;
                    final MaterialRequest r = new MaterialRequest()
                            .setCustomerShortName(getCellValue(row.getCell(ci++)))
                            .setCustomerOrderNo(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setCustomerProjectSequence(getCellValue(row.getCell(ci++)))
                            .setSaleOrderNo(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setOrderProjectNo(getCellValue(row.getCell(ci++)))
                            .setMaterialNo(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setImproveMaterialDescribe(getCellValue(row.getCell(ci++)))
                            .setDesignNumber(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setOrderCount(defaultDecimal(getCellValue(row.getCell(ci++))).setScale(0, RoundingMode.HALF_UP))
                            .setProductionDate(DateUtil.day(defaultIfBlank(getCellValue(row.getCell(ci++)), today)))
                            .setPromiseDoneDate(DateUtil.day(getCellValue(row.getCell(ci++))))
                            .setBlankMaterialNo(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setBlankMaterialDescribe(getCellValue(row.getCell(ci++)))
                            .setRoughcastDesignNumber(getCellValue(row.getCell(ci++)))
                            .setMaterialCount(defaultDecimal(getCellValue(row.getCell(ci++))).setScale(0, RoundingMode.HALF_UP))
                            .setStoveNo(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setHotBatchNo(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setSerialNo(defaultIfBlank(getCellValue(row.getCell(ci++))).toUpperCase())
                            .setSurplusCount(defaultDecimal(getCellValue(row.getCell(ci++))))
                            .setNde(getCellValue(row.getCell(ci++)))
                            .setAssemble(getCellValue(row.getCell(ci++)))
                            .setTestPress(getCellValue(row.getCell(ci++)))
                            .setSurfaceTreatment(getCellValue(row.getCell(ci++)))
                            .setDescription(getCellValue(row.getCell(ci++)))
                            .setChargeCompany(getCellValue(row.getCell(ci++)))
                            .setProductionCount(defaultDecimal(getCellValue(row.getCell(ci++))).setScale(0, RoundingMode.HALF_UP))
                            .setArrangeProductionDate(DateUtil.day(defaultIfBlank(getCellValue(row.getCell(ci++)), today)))
                            .setMaterialOrderNo(getCellValue(row.getCell(ci++)))
                            .setCheckOrderNo(getCellValue(row.getCell(ci++)));
                    r.setRemainCount(r.getMaterialCount().subtract(r.getProductionCount()))
                            .setProductionCount(null)
                            .setArrangeProductionDate(null)
                    ;
                    el.add(r);
                }
            }
            res
                    .setUploadDetailCount(BigDecimal.valueOf(el.size()))
                    .setUploadCount(BigDecimal.valueOf(el.stream().map(t -> CollUtil.toList(t.getSaleOrderNo(), t.getOrderProjectNo())).distinct().count()))
            ;
            lockHelper.lock("material");
            try {
                /*materialMapper.delete(
                        new LambdaUpdateWrapper<MaterialEntity>()
                                .eq(MaterialEntity::getProductionDate, today)
                );
                materialDao.init();*/
                final List<MaterialEntity> il = new ArrayList<>();
                final Map<List<String>, String> orderNoMap = new HashMap<>(8);
                final Map<List<String>, String> indexMap = new HashMap<>(8);
                for (MaterialRequest t : el) {
                    final List<String> key = CollUtil.toList(t.getSaleOrderNo(), t.getOrderProjectNo());
                    final MaterialEntity e = (MaterialEntity) MATERIAL_INSTANCE.material(t)
                            .setMaterialOrderNo(orderNoMap.computeIfAbsent(key, k -> materialDao.nextMaterialOrderNo()))
                            .setCheckOrderNo(indexMap.computeIfAbsent(key, k -> materialDao.nextCheckOrderNo()))
                            .setCreator(u.getUserId())
                            .setModifier(u.getUserId());
                    il.add(e);
                }
                materialDao.saveBatch(
                        Stream.of(
                                        il
                                ).flatMap(Collection::stream)
                                .toList()
                );
                final List<MaterialEntity> afterList = materialMapper.selectList(
                        new LambdaQueryWrapper<MaterialEntity>()
                                .eq(MaterialEntity::getProductionDate, today)
                                .select(MaterialEntity::getSaleOrderNo,
                                        MaterialEntity::getOrderProjectNo,
                                        MaterialEntity::getProductionDate
                                )
                );
                final Map<List<String>, Long> afterKm = afterList.stream()
                        .collect(Collectors.groupingBy(
                                t -> CollUtil.toList(t.getSaleOrderNo(), t.getOrderProjectNo()),
                                Collectors.counting()
                        ));
                res
                        .setAfterDetailCount(BigDecimal.valueOf(afterList.size()))
                        .setAfterCount(BigDecimal.valueOf(afterKm.keySet().size()))
                ;
                final Map<List<String>, BigDecimal> materialCountMap =
                        materialMapper.selectList(
                                DatabaseUtil.singleOr(new LambdaQueryWrapper<MaterialEntity>().select(MaterialEntity::getSaleOrderNo,
                                        MaterialEntity::getOrderProjectNo,
                                        MaterialEntity::getProductionDate,
                                        MaterialEntity::getMaterialCount
                                ), el, (lam, t) -> {
                                    lam.eq(MaterialEntity::getSaleOrderNo, t.getSaleOrderNo())
                                            .eq(MaterialEntity::getOrderProjectNo, t.getOrderProjectNo());
                                })
                        ).stream().collect(Collectors.groupingBy(
                                t -> CollUtil.toList(t.getSaleOrderNo(), t.getOrderProjectNo()),
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        t -> defaultDecimal(t.getMaterialCount()),
                                        BigDecimal::add
                                )
                        ));
                Map<ArrayList<String>, BigDecimal> materialOrderCountMap = el.stream().collect(Collectors.toMap(t -> CollUtil.toList(t.getSaleOrderNo(), t.getOrderProjectNo()), MaterialRequest::getOrderCount, (t, t1) -> t1));
                for (Map.Entry<ArrayList<String>, BigDecimal> e : materialOrderCountMap.entrySet()) {
                    List<String> key = e.getKey();
                    BigDecimal orderCount = e.getValue();
                    BigDecimal materialCount = materialCountMap.getOrDefault(key, BigDecimal.ZERO);
                    BigDecimal surplusCount = orderCount.subtract(materialCount);
                    materialMapper.update(
                            null,
                            new LambdaUpdateWrapper<MaterialEntity>()
                                    .set(MaterialEntity::getOrderCount, orderCount)
                                    .set(MaterialEntity::getSurplusCount, surplusCount)
                                    .eq(MaterialEntity::getSaleOrderNo, key.get(0))
                                    .eq(MaterialEntity::getOrderProjectNo, key.get(1))
                    );
                }
            } finally {
                lockHelper.unlock("material");
            }
        } catch (Exception e) {
            throw new BusinessException(MP_UPLOAD_EXCEL_ERROR).setOriginException(e);
        }
        return new DataResult<>(res);
    }

    /**
     * 删除生产工单
     *
     * @param deviceId 设备id
     * @param request  {@link MaterialRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result delete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute MaterialRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final MaterialEntity e = materialDao.getById(request.getMaterialId());
        if (isNotBlank(request.getMaterialId())) {
            materialDao.removeById(request.getMaterialId());
        }
        updateSummaryInfo(e, DateUtil.day(new Date()));
        return new Result();
    }

    private List<MaterialEntity> materialList(MaterialRequest d) {
        LambdaQueryWrapper<MaterialEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getMaterialId())) {
            lambda.eq(MaterialEntity::getId, d.getMaterialId());
        }
        if (CollUtil.isNotEmpty(d.getMaterialIdList())) {
            DatabaseUtil.or(lambda, d.getMaterialIdList(), (lam, l) -> lam.in(MaterialEntity::getId, d.getMaterialIdList()));
        }
        if (isNotBlank(d.getCustomerShortName())) {
            lambda.like(MaterialEntity::getCustomerShortName, d.getCustomerShortName());
        }
        if (isNotBlank(d.getCustomerOrderNo())) {
            lambda.like(MaterialEntity::getCustomerOrderNo, d.getCustomerOrderNo());
        }
        if (isNotBlank(d.getCustomerProjectSequence())) {
            lambda.like(MaterialEntity::getCustomerProjectSequence, d.getCustomerProjectSequence());
        }
        if (isNotBlank(d.getSaleOrderNo())) {
            lambda.like(MaterialEntity::getSaleOrderNo, d.getSaleOrderNo());
        }
        if (isNotBlank(d.getOrderProjectNo())) {
            lambda.like(MaterialEntity::getOrderProjectNo, d.getOrderProjectNo());
        }
        if (isNotBlank(d.getMaterialNo())) {
            lambda.like(MaterialEntity::getMaterialNo, d.getMaterialNo());
        }
        if (isNotBlank(d.getDesignNumber())) {
            lambda.like(MaterialEntity::getDesignNumber, d.getDesignNumber());
        }
        if (null != d.getStartPromiseDoneDate()) {
            lambda.ge(MaterialEntity::getPromiseDoneDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(d.getStartPromiseDoneDate())));
        }
        if (null != d.getEndPromiseDoneDate()) {
            lambda.le(MaterialEntity::getPromiseDoneDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(d.getEndPromiseDoneDate()).toJdkDate()));
        }
        if (null != d.getSurplusCountType()) {
            if (d.getSurplusCountType() == 0) {
                lambda.eq(MaterialEntity::getSurplusCount, 0);
            } else if (d.getSurplusCountType() == 2) {
                lambda.ne(MaterialEntity::getSurplusCount, 0);
            } else if (d.getSurplusCountType() < 0) {
                lambda.lt(MaterialEntity::getSurplusCount, 0);
            } else {
                lambda.gt(MaterialEntity::getSurplusCount, 0);
            }
        }
        if (null != d.getRemainCountType()) {
            if (d.getRemainCountType() == 0) {
                lambda.eq(MaterialEntity::getRemainCount, 0);
            } else if (d.getRemainCountType() == 2) {
                lambda.ne(MaterialEntity::getRemainCount, 0);
            } else if (d.getRemainCountType() < 0) {
                lambda.lt(MaterialEntity::getRemainCount, 0);
            } else {
                lambda.gt(MaterialEntity::getRemainCount, 0);
            }
        }
        if (isNotBlank(d.getChargeCompany())) {
            lambda.like(MaterialEntity::getChargeCompany, d.getChargeCompany());
        }
        if (isNotBlank(d.getNde())) {
            lambda.eq(MaterialEntity::getNde, d.getNde());
        }
        if (isNotBlank(d.getAssemble())) {
            lambda.eq(MaterialEntity::getAssemble, d.getAssemble());
        }
        if (isNotBlank(d.getTestPress())) {
            lambda.eq(MaterialEntity::getTestPress, d.getTestPress());
        }
        if (isNotBlank(d.getSurfaceTreatment())) {
            lambda.eq(MaterialEntity::getSurfaceTreatment, d.getSurfaceTreatment());
        }
        if (isNotBlank(d.getMaterialOrderNo())) {
            if (Boolean.TRUE.equals(d.getAccurateMatch())) {
                lambda.eq(MaterialEntity::getMaterialOrderNo, d.getMaterialOrderNo());
            } else {
                lambda.like(MaterialEntity::getMaterialOrderNo, d.getMaterialOrderNo());
            }
        }
        if (isNotBlank(d.getCheckOrderNo())) {
            if (Boolean.TRUE.equals(d.getAccurateMatch())) {
                lambda.eq(MaterialEntity::getCheckOrderNo, d.getCheckOrderNo());
            } else {
                lambda.like(MaterialEntity::getCheckOrderNo, d.getCheckOrderNo());
            }
        }
        if (0 == d.getOrderByPromiseDoneDate()) {
            lambda.orderByDesc(MaterialEntity::getCreateTime);
        } else if (1 == d.getOrderByPromiseDoneDate()) {
            lambda.orderByAsc(MaterialEntity::getPromiseDoneDate);
        } else {
            lambda.orderByDesc(MaterialEntity::getPromiseDoneDate);
        }
        return materialDao.list(lambda);
    }

    private List<MaterialResponse> formatMaterialList(List<MaterialEntity> list) {
        List<MaterialResponse> rl = MATERIAL_INSTANCE.materialList(list);
        List<String> userIdList = Stream.of(
                        rl.stream().map(MaterialResponse::getCreator).filter(StrUtil::isNotBlank)
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
                rl.stream().filter(t -> isNotBlank(t.getCreator())).collect(Collectors.toList()),
                MaterialResponse::getCreator,
                l -> userList,
                (t, r) -> t.getCreator().equals(r.getId()),
                (t, r) -> t.setCreatorFormat(r.getName())
        );
        /*MultitaskUtil.supplementList(
                rl,
                MaterialResponse::getOptimizeType,
                l -> paramDao.listByCategoryId("optimizeType"),
                (t, r) -> t.getOptimizeType().equals(r.getValue()),
                (t, r) -> t.setOptimizeTypeFormat(r.getLabel())
        );*/
        for (MaterialResponse t : rl) {
            t.setMaterialOrderNoFormat(t.getMaterialOrderNo() + tail(t.getMaterialPrintCount()));
            t.setCheckOrderNoFormat(t.getCheckOrderNo() + tail(t.getCheckPrintCount()));
        }
        return rl;
    }

    private static String tail(BigDecimal v) {
        return null == v || v.compareTo(BigDecimal.ZERO) <= 0 ? "" : ("-" + v.setScale(0, RoundingMode.HALF_UP));
    }

    /**
     * 生产工单分页
     *
     * @param deviceId 设备id
     * @param request  {@link MaterialPageRequest}
     * @return {@link PageResult <MaterialResponse>}
     */
    @GetMapping("page")
    public PageResult<MaterialResponse> materialAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute MaterialPageRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().anyMatch(t -> "materialManager".equals(t.getRoleCode()))) {
            request.getData().setOrderByPromiseDoneDate(1);
        }
        final PageResult<MaterialEntity> pr = DatabaseUtil.page(request, this::materialList);
        final AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(pr.getTotal(), formatMaterialList(pr.getList())
                .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 报检单打印
     *
     * @param deviceId 设备id
     * @param request  {@link MaterialRequest}
     * @return {@link ListResult<MaterialResponse>}
     */
    @GetMapping("check")
    public ListResult<MaterialResponse> check(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute MaterialRequest request
    ) {
        final ListResult<MaterialResponse> r = new ListResult<>(formatMaterialList(materialList(request.setAccurateMatch(true))));
        if (!r.getList().isEmpty()) {
            /*if (r.getList().stream().map(MaterialResponse::getCheckOrderNo).distinct().count() != 1) {
                throw new BusinessException(MP_DATA_QUERY_ERROR);
            }*/
            materialMapper.update(
                    null,
                    new LambdaUpdateWrapper<MaterialEntity>()
                            .setSql("CHECK_PRINT_COUNT = CHECK_PRINT_COUNT + 1")
                            .eq(MaterialEntity::getCheckOrderNo, CollUtil.getFirst(r.getList()).getCheckOrderNo())
            );
        }
        return r;
    }

    /**
     * 领料单打印
     *
     * @param deviceId 设备id
     * @param request  {@link MaterialRequest}
     * @return {@link ListResult<MaterialResponse>}
     */
    @GetMapping("index")
    public ListResult<MaterialResponse> index(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute MaterialRequest request
    ) {
        final ListResult<MaterialResponse> r = new ListResult<>(formatMaterialList(materialList(request.setAccurateMatch(true))));
        if (!r.getList().isEmpty()) {
            /*if (r.getList().stream().map(MaterialResponse::getMaterialOrderNo).distinct().count() != 1) {
                throw new BusinessException(MP_DATA_QUERY_ERROR);
            }*/
            materialMapper.update(
                    null,
                    new LambdaUpdateWrapper<MaterialEntity>()
                            .setSql("MATERIAL_PRINT_COUNT = MATERIAL_PRINT_COUNT + 1")
                            .eq(MaterialEntity::getMaterialOrderNo, CollUtil.getFirst(r.getList()).getMaterialOrderNo())
            );
        }
        return r;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        // 以下是判断数据的类型
        switch (cell.getCellType()) {
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellValue = sdf.format(org.apache.poi.ss.usermodel.DateUtil.getJavaDate(cell.getNumericCellValue()));
                } else {
                    DataFormatter dataFormatter = new DataFormatter();
                    cellValue = dataFormatter.formatCellValue(cell);
                }
                break;
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:
                cellValue = cell.getBooleanCellValue() + "";
                break;
            case FORMULA:
                cellValue = cell.getCellFormula();
                break;
            case BLANK:
                cellValue = "";
                break;
            case ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    private void updateSummaryInfo(MaterialEntity e, String today) {
        final List<MaterialEntity> el = materialMapper.selectList(
                new LambdaQueryWrapper<MaterialEntity>()
                        .eq(MaterialEntity::getSaleOrderNo, e.getSaleOrderNo())
                        .eq(MaterialEntity::getOrderProjectNo, e.getOrderProjectNo())
        );
        BigDecimal sumMaterialCount = defaultDecimal(el.stream().map(MaterialEntity::getMaterialCount).reduce(BigDecimal.ZERO, BigDecimal::add));
        final BigDecimal surplusCount = e.getOrderCount().subtract(sumMaterialCount);
        materialMapper.update(null,
                new LambdaUpdateWrapper<MaterialEntity>()
                        .set(MaterialEntity::getNde, e.getNde())
                        .set(MaterialEntity::getAssemble, e.getAssemble())
                        .set(MaterialEntity::getTestPress, e.getTestPress())
                        .set(MaterialEntity::getSurfaceTreatment, e.getSurfaceTreatment())
                        .set(MaterialEntity::getOrderCount, e.getOrderCount())
                        .set(MaterialEntity::getGenerateTask, e.getGenerateTask())
                        .set(MaterialEntity::getSurplusCount, surplusCount)
                        .eq(MaterialEntity::getSaleOrderNo, e.getSaleOrderNo())
                        .eq(MaterialEntity::getOrderProjectNo, e.getOrderProjectNo())
        );
    }
}
