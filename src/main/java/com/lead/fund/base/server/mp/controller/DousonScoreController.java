package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.NumberUtil.defaultDecimal;
import static com.lead.fund.base.common.util.NumberUtil.defaultInt;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_UPLOAD_EXCEL_ERROR;
import static com.lead.fund.base.server.mp.converter.ScoreConverter.SCORE_INSTANCE;
import static com.lead.fund.base.server.mp.util.ExcelUtil.getCellValue;

import cn.hutool.core.collection.CollUtil;
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
import com.lead.fund.base.common.util.BeanUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.NumberUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.ScoreAttachmentDao;
import com.lead.fund.base.server.mp.dao.ScoreDao;
import com.lead.fund.base.server.mp.dao.TaskDao;
import com.lead.fund.base.server.mp.dao.TemplatePhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.ScoreAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.ScoreEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.LockHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.ScoreAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.ScoreMapper;
import com.lead.fund.base.server.mp.request.ScoreRequest;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.ScoreResponse;
import com.lead.fund.base.server.mp.response.ScoreSummaryResponse;
import com.lead.fund.base.server.mp.response.ScoreUploadResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
 * DousonScoreController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue"})
@RestController
@RequestMapping("/douson/score")
@Slf4j
@Validated
public class DousonScoreController {

    @Resource
    private AccountHelper accountHelper;
    @Resource
    private TemplatePhotoDao templatePhotoDao;
    @Resource
    private ScoreMapper scoreMapper;
    @Resource
    private ScoreAttachmentMapper scoreAttachmentMapper;
    @Resource
    private ScoreAttachmentDao scoreAttachmentDao;
    @Resource
    private TaskDao taskDao;
    @Resource
    private ScoreDao scoreDao;
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
        byte[] scoreTemplateBytes = IoUtil.readBytes(this.getClass().getResourceAsStream("/score_template.xlsx"));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            headers.setContentDispositionFormData("attachment", new String(("score_template" + ".xlsx").getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            log.warn("set attachment error");
        }
        resp.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        return new ResponseEntity<>(scoreTemplateBytes, headers, HttpStatus.OK);
    }

    /**
     * 上传季度评比报告
     *
     * @param file 单个图片 {@link MultipartFile}
     * @return {@link DataResult<ScoreUploadResponse>}
     */
    @PostMapping("upload")
    public DataResult<ScoreUploadResponse> upload(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "quarter") String quarter
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "scoreManager".equals(t.getRoleCode()) || "scoreRecord".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final ScoreUploadResponse res = new ScoreUploadResponse();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            final List<ScoreRequest> el = new ArrayList<>();
            final XSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
                final Row row = sheet.getRow(i);
                if (null != row) {
                    int ci = 0;
                    final ScoreRequest r = new ScoreRequest()
                            .setEmployeeId(getCellValue(row.getCell(ci++)))
                            .setUserName(getCellValue(row.getCell(ci++)))
                            .setQuarter(quarter)
                            .setDeviceNumber(getCellValue(row.getCell(ci++)))
                            .setQualityScore(defaultDecimal(getCellValue(row.getCell(ci++))))
                            .setAttendanceScore(defaultDecimal(getCellValue(row.getCell(ci++))))
                            .setSafetyScore(defaultDecimal(getCellValue(row.getCell(ci++))))
                            .setMonthlyPerformance(defaultDecimal(getCellValue(row.getCell(ci++))))
                            .setTotalWorkDays(defaultInt(getCellValue(row.getCell(ci++))))
                            .setEvaluationMonths(defaultInt(getCellValue(row.getCell(ci++))))
                            .setEvaluationResult(getCellValue(row.getCell(ci++)))
                            .setQuarterlyBonus(defaultDecimal(getCellValue(row.getCell(ci++))))
                            .setDescription(getCellValue(row.getCell(ci++)))
                            .setLeaderUserId(getCellValue(row.getCell(ci++)));
                    el.add(r);
                }
            }
            final Map<List<String>, String> userIdMap = new HashMap<>(8);
            for (int i = 0; i < el.size(); i++) {
                final ScoreRequest t = el.get(i);
                t.setTotal(
                        BigDecimal.ZERO.add(t.getQualityScore().add(t.getSafetyScore()).add(t.getAttendanceScore()).add(t.getMonthlyPerformance()))
                );
                final String userId = userIdMap.computeIfAbsent(CollUtil.toList(t.getEmployeeId(), t.getUserName()), k -> BeanUtil.wrapperIfNotNull(userMapper.selectOne(new LambdaQueryWrapper<MpUserEntity>()
                        .eq(MpUserEntity::getEmployeeId, k.get(0))
                        .eq(MpUserEntity::getName, k.get(1))
                ), MpUserEntity::getId, ""));
                if (isBlank(userId)) {
                    res.getNotMatchUserList().add("%s（%s）".formatted(t.getUserName(), t.getEmployeeId()));
                } else {
                    final ScoreEntity e = (ScoreEntity) SCORE_INSTANCE.score(t)
                            .setSorter(i)
                            .setUserId(userId)
                            .setCreator(u.getUserId())
                            .setModifier(u.getUserId());
                    if (!scoreDao.update(e,
                            new LambdaUpdateWrapper<ScoreEntity>()
                                    .eq(ScoreEntity::getQuarter, quarter)
                                    .eq(ScoreEntity::getUserId, e.getUserId())
                    )) {
                        scoreDao.save(e);
                        res.getInsertUserList().add("%s（%s）".formatted(t.getUserName(), t.getEmployeeId()));
                    } else {
                        res.getUpdateUserList().add("%s（%s）".formatted(t.getUserName(), t.getEmployeeId()));
                    }
                }
            }
            res
                    .setTotalCount(el.size())
                    .setErrorCount(res.getNotMatchUserList().size())
                    .setInsertUserCount(res.getInsertUserList().size())
                    .setUpdateUserCount(res.getUpdateUserList().size())
            ;
        } catch (Exception e) {
            throw new BusinessException(MP_UPLOAD_EXCEL_ERROR).setOriginException(e);
        }
        return new DataResult<>(res);
    }

    /**
     * 保存、更新季度评比报告
     *
     * @param deviceId 设备id
     * @param request  {@link ScoreRequest}
     * @return {@link Result}
     */
    @PutMapping("merge")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result merge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ScoreRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final ScoreEntity e = (ScoreEntity) SCORE_INSTANCE.score(request)
                .setModifier(u.getUserId());
        e.setTotal(
                BigDecimal.ZERO.add(e.getQualityScore().add(e.getSafetyScore()).add(e.getAttendanceScore()).add(e.getMonthlyPerformance()))
        );
        // update
        if (isNotBlank(e.getId())) {
            if (u.getRoleList().stream().noneMatch(t -> "scoreManager".equals(t.getRoleCode()))) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            if (scoreMapper.update(
                    e,
                    new LambdaUpdateWrapper<ScoreEntity>()
                            .eq(ScoreEntity::getId, e.getId())
                            .le(ScoreEntity::getLastModifiedTime, DateUtil.parse(request.getModifyTime()))
            ) <= 0) {
                if (scoreMapper.selectById(e.getId()).getLastModifiedTime().compareTo(DateUtil.parse(request.getModifyTime())) > 0) {
                    throw new BusinessException(AUTHORITY_AUTH_FAIL.getCode(), "数据已被修改，请重新获取数据。（Please reload data）");
                }
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            // insert
        } else {
            if (u.getRoleList().stream().noneMatch(t -> "scoreManager".equals(t.getRoleCode()))) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            scoreMapper.insert((ScoreEntity) e
                    .setCreator(u.getUserId())
            );
        }
        scoreAttachmentDao.remove(new LambdaUpdateWrapper<ScoreAttachmentEntity>().eq(ScoreAttachmentEntity::getScoreId, e.getId()));
        scoreAttachmentDao.saveBatch(
                request.getPhotoList().stream().map(t -> SCORE_INSTANCE.attachment(e.getId(), "photo", t)).collect(Collectors.toList())
        );
        return new DataResult<>(e);
    }

    /**
     * 删除季度评比报告
     *
     * @param deviceId 设备id
     * @param request  {@link ScoreRequest}
     * @return {@link Result}
     */
    @DeleteMapping("")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result delete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ScoreRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername()) && u.getRoleCodeList().stream().noneMatch("scoreManager"::equals)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        if (isNotBlank(request.getScoreId())) {
            final ScoreEntity db = scoreDao.getById(request.getScoreId());
            if (null != db) {
                scoreDao.removeById(request.getScoreId());
                scoreAttachmentDao.remove(new LambdaUpdateWrapper<ScoreAttachmentEntity>()
                        .eq(ScoreAttachmentEntity::getScoreId, db.getId())
                );
            }
        }
        return new Result();
    }

    private List<ScoreEntity> scoreList(ScoreRequest d) {
        return scoreList(d, null);
    }

    private List<ScoreEntity> scoreList(ScoreRequest d, Consumer<LambdaQueryWrapper<ScoreEntity>> consumer) {
        LambdaQueryWrapper<ScoreEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getScoreId())) {
            lambda.eq(ScoreEntity::getId, d.getScoreId());
        }
        if (isNotBlank(d.getQuarter())) {
            lambda.eq(ScoreEntity::getQuarter, d.getQuarter());
        }
        if (isNotBlank(d.getUserId())) {
            lambda.eq(ScoreEntity::getUserId, d.getUserId());
        }
        if (isNotBlank(d.getEvaluationResult())) {
            lambda.eq(ScoreEntity::getEvaluationResult, d.getEvaluationResult());
        }
        if (isNotBlank(d.getDescription())) {
            lambda.like(ScoreEntity::getDescription, d.getDescription());
        }
        if (isNotBlank(d.getLeaderUserId())) {
            lambda.like(ScoreEntity::getLeaderUserId, d.getLeaderUserId());
        }
        lambda.orderByDesc(ScoreEntity::getQuarter).orderByAsc(ScoreEntity::getSorter);
        if (null != consumer) {
            consumer.accept(lambda);
        }
        return scoreDao.list(lambda);
    }

    private List<ScoreResponse> formatScoreList(List<ScoreEntity> list) {
        List<ScoreResponse> rl = SCORE_INSTANCE.scoreList(list);
        List<String> userIdList = Stream.of(
                        rl.stream().map(ScoreResponse::getUserId).filter(StrUtil::isNotBlank),
                        rl.stream().map(ScoreResponse::getLeaderUserId).filter(StrUtil::isNotBlank)
                )
                .flatMap(t -> t)
                .distinct()
                .collect(Collectors.toList());
        final Map<String, MpUserEntity> um = CollUtil.isEmpty(userIdList) ? new HashMap<>(8) : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getEmployeeId, MpUserEntity::getName),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        ).stream().collect(Collectors.toMap(AbstractPrimaryKey::getId, t -> t, (t, t1) -> t1));
        MultitaskUtil.supplementList(
                rl,
                ScoreResponse::getScoreId,
                l1 -> scoreAttachmentMapper.selectList(new LambdaQueryWrapper<ScoreAttachmentEntity>()
                        .in(ScoreAttachmentEntity::getScoreId, l1)),
                (t, r) -> t.getScoreId().equals(r.getScoreId()),
                (t, r) -> {
                    switch (r.getAttachmentCategory()) {
                        case "photo" -> t.getPhotoList().add(
                                SCORE_INSTANCE.photo(r, urlHelper.getUrlPrefix())
                        );
                        default -> log.info("accident attachment: {}", JSONUtil.toJsonStr(r));
                    }
                }
        );
        for (ScoreResponse t : rl) {
            final MpUserEntity u = um.get(t.getUserId());
            t
                    .setPhotoCount(t.getPhotoList().size())
                    .setEmployeeId(BeanUtil.wrapperIfNotNull(u, MpUserEntity::getEmployeeId, t.getUserId()))
                    .setUserName(BeanUtil.wrapperIfNotNull(u, MpUserEntity::getName, t.getUserId()))
            ;
        }
        return rl;
    }

    /**
     * 季度评比报告列表
     *
     * @param deviceId 设备id
     * @param request  {@link ScoreRequest}
     * @return {@link PageResult <ScoreResponse>}
     */
    @GetMapping("list")
    public ListResult<ScoreResponse> scoreList(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ScoreRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername()) && u.getRoleList().stream().noneMatch(t -> "scoreManager".equals(t.getRoleCode()) || "scoreView".equals(t.getRoleCode()))) {
            u.setUserId(u.getUserId());
        }
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final List<ScoreResponse> rl = formatScoreList(scoreList(request));
        if (CollUtil.isNotEmpty(rl)) {
            rl.add(rl.stream().reduce(new ScoreResponse().setUserName("总计")
                            .setQualityScore(BigDecimal.ZERO)
                            .setAttendanceScore(BigDecimal.ZERO)
                            .setSafetyScore(BigDecimal.ZERO)
                            .setMonthlyPerformance(BigDecimal.ZERO)
                            .setTotalWorkDays(0)
                            .setTotal(BigDecimal.ZERO)
                            .setEvaluationMonths(0)
                            .setQuarterlyBonus(BigDecimal.ZERO)
                            .setEvaluationResult("--")
                    , (t, t1) -> {
                        t
                                .setQualityScore(t.getQualityScore().add(t1.getQualityScore()))
                                .setQualityScoreFormat(NumberUtil.format(t.getQualityScore()))
                                .setAttendanceScore(t.getAttendanceScore().add(t1.getAttendanceScore()))
                                .setAttendanceScoreFormat(NumberUtil.format(t.getAttendanceScore()))
                                .setSafetyScore(t.getSafetyScore().add(t1.getSafetyScore()))
                                .setSafetyScoreFormat(NumberUtil.format(t.getSafetyScore()))
                                .setMonthlyPerformance(t.getMonthlyPerformance().add(t1.getMonthlyPerformance()))
                                .setMonthlyPerformanceFormat(NumberUtil.format(t.getMonthlyPerformance()))
                                .setTotalWorkDays(t.getTotalWorkDays() + t1.getTotalWorkDays())
                                .setTotal(t.getTotal().add(t1.getTotal()))
                                .setTotalFormat(NumberUtil.format(t.getTotal()))
                                .setEvaluationMonths(t.getEvaluationMonths() + t1.getEvaluationMonths())
                                .setQuarterlyBonus(t.getQuarterlyBonus().add(t1.getQuarterlyBonus()))
                                .setQuarterlyBonusFormat(NumberUtil.formatDecimal0(t.getQuarterlyBonus()))
                        ;
                        return t;
                    }));
        }
        return new ListResult<>(rl
                .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 季度评比报告
     *
     * @param deviceId 设备id
     * @param request  {@link ScoreRequest}
     * @return {@link DataResult<ScoreResponse>}
     */
    @GetMapping("")
    public DataResult<ScoreResponse> score(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ScoreRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "scoreManager".equals(t.getRoleCode()) || "scoreRecord".equals(t.getRoleCode()) || "scoreTesterRecord".equals(t.getRoleCode()) || "scoreRecordView".equals(t.getRoleCode()) || "admin".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        return new DataResult<>(CollUtil.getFirst(formatScoreList(scoreList(request))));
    }

    /**
     * 汇总列表
     *
     * @param deviceId 设备id
     * @param request  {@link ScoreRequest}
     * @return {@link ListResult <ScoreSummaryResponse>}
     */
    @GetMapping("summary-list")
    public ListResult<ScoreSummaryResponse> summaryList(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ScoreRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "scoreManager".equals(t.getRoleCode()) || "scoreRecord".equals(t.getRoleCode()) || "scoreTesterRecord".equals(t.getRoleCode()) || "scoreRecordView".equals(t.getRoleCode()) || "admin".equals(t.getRoleCode()))) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final List<ScoreEntity> l = scoreList(request, lambda -> {
            lambda
                    .select(
                            ScoreEntity::getEvaluationResult,
                            ScoreEntity::getUserId
                    )
                    .orderByAsc(ScoreEntity::getEvaluationResult);
        });
        final int total = l.size();
        final List<ScoreSummaryResponse> rl = SCORE_INSTANCE.scoreSummaryList(l)
                .stream()
                .collect(Collectors.groupingBy(
                        ScoreSummaryResponse::getEvaluationResult,
                        LinkedHashMap::new,
                        Collectors.counting()
                )).entrySet()
                .stream()
                .map(t -> {
                    final ScoreSummaryResponse r = new ScoreSummaryResponse()
                            .setEvaluationResult(t.getKey())
                            .setCount(t.getValue().intValue())
                            .setPercent(new BigDecimal(t.getValue())
                                    .divide(new BigDecimal(total), 4, RoundingMode.HALF_UP)
                                    .setScale(4, RoundingMode.HALF_UP)
                            );
                    return r.setPercentFormat(NumberUtil.formatPercent(r.getPercent()));
                }).collect(Collectors.toList());
        rl.add(new ScoreSummaryResponse()
                .setEvaluationResult("总计")
                .setCount(total)
                .setPercent(new BigDecimal(1))
                .setPercentFormat(NumberUtil.formatPercent(new BigDecimal(1))));
        return new ListResult<>(
                rl
        );
    }

}
