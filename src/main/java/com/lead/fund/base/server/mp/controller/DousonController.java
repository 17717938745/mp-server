package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.BasicConst.VARIABLE_PAGE_DEFAULT;
import static com.lead.fund.base.common.basic.cons.BasicConst.VARIABLE_PAGE_MAX;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.BeanUtil.defaultIfNull;
import static com.lead.fund.base.common.util.NumberUtil.defaultDecimal;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.cons.MpConst.STRING_LIST_ENGINEER_USER_ROLE_CODE_LIST;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_OPERATOR_OTHER_NOT_ALLOW;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_ORDER_REPEAT;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_PRODUCT_EXISTS;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_RECORD_DEVICE_TIME_ERROR;
import static com.lead.fund.base.server.mp.converter.IndustryConverter.INDUSTRY_INSTANCE;
import static com.lead.fund.base.server.mp.converter.MpAccountConverter.MP_ACCOUNT_INSTANCE;
import static com.lead.fund.base.server.mp.converter.MpSystemConverter.MP_SYSTEM_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.cons.frame.AdminRole;
import com.lead.fund.base.common.basic.cons.frame.AdminState;
import com.lead.fund.base.common.basic.cons.frame.AdminUser;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.model.OptionItem;
import com.lead.fund.base.common.basic.request.Page;
import com.lead.fund.base.common.basic.response.DataListResult;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.ListResult;
import com.lead.fund.base.common.basic.response.PageDataResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.NumberUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.AccidentAttachmentDao;
import com.lead.fund.base.server.mp.dao.BoxFlagDao;
import com.lead.fund.base.server.mp.dao.BoxFlagPhotoDao;
import com.lead.fund.base.server.mp.dao.BoxFlagSerialNoDao;
import com.lead.fund.base.server.mp.dao.ComputerDao;
import com.lead.fund.base.server.mp.dao.ComputerPhotoDao;
import com.lead.fund.base.server.mp.dao.CrashAttachmentDao;
import com.lead.fund.base.server.mp.dao.DeviceCheckLedgerDao;
import com.lead.fund.base.server.mp.dao.DeviceCheckLedgerPhotoDao;
import com.lead.fund.base.server.mp.dao.DisqualificationOrderDao;
import com.lead.fund.base.server.mp.dao.DisqualificationOrderPhotoDao;
import com.lead.fund.base.server.mp.dao.EquipmentAttachmentDao;
import com.lead.fund.base.server.mp.dao.EventAttachmentDao;
import com.lead.fund.base.server.mp.dao.ImproveAttachmentDao;
import com.lead.fund.base.server.mp.dao.MaintainAttachmentDao;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.PlanAttachmentDao;
import com.lead.fund.base.server.mp.dao.PlanDao;
import com.lead.fund.base.server.mp.dao.PlanPhotoDao;
import com.lead.fund.base.server.mp.dao.ProductPhotoDao;
import com.lead.fund.base.server.mp.dao.QualityAttachmentDao;
import com.lead.fund.base.server.mp.dao.ReportPhotoDao;
import com.lead.fund.base.server.mp.dao.ReportSerialNoDao;
import com.lead.fund.base.server.mp.dao.TroubleAttachmentDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpAccountEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpRoleEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.AccidentAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.AccidentEntity;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagEntity;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagPhotoEntity;
import com.lead.fund.base.server.mp.entity.douson.ComputerEntity;
import com.lead.fund.base.server.mp.entity.douson.ComputerPhotoEntity;
import com.lead.fund.base.server.mp.entity.douson.CrashAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.CrashEntity;
import com.lead.fund.base.server.mp.entity.douson.DeviceCheckLedgerEntity;
import com.lead.fund.base.server.mp.entity.douson.DeviceCheckLedgerPhotoEntity;
import com.lead.fund.base.server.mp.entity.douson.DeviceEntity;
import com.lead.fund.base.server.mp.entity.douson.DisqualificationOrderEntity;
import com.lead.fund.base.server.mp.entity.douson.DisqualificationOrderPhotoEntity;
import com.lead.fund.base.server.mp.entity.douson.EquipmentAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.EquipmentEntity;
import com.lead.fund.base.server.mp.entity.douson.EventAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.EventEntity;
import com.lead.fund.base.server.mp.entity.douson.ImproveAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.ImproveEntity;
import com.lead.fund.base.server.mp.entity.douson.MaintainAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.MaintainEntity;
import com.lead.fund.base.server.mp.entity.douson.OrderEntity;
import com.lead.fund.base.server.mp.entity.douson.ParamEntity;
import com.lead.fund.base.server.mp.entity.douson.PlanAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.PlanEntity;
import com.lead.fund.base.server.mp.entity.douson.PlanPhotoEntity;
import com.lead.fund.base.server.mp.entity.douson.ProductEntity;
import com.lead.fund.base.server.mp.entity.douson.ProductPhotoEntity;
import com.lead.fund.base.server.mp.entity.douson.QualityAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.QualityEntity;
import com.lead.fund.base.server.mp.entity.douson.ReportEntity;
import com.lead.fund.base.server.mp.entity.douson.ReportPhotoEntity;
import com.lead.fund.base.server.mp.entity.douson.ReportSerialNoEntity;
import com.lead.fund.base.server.mp.entity.douson.ReportUserEntity;
import com.lead.fund.base.server.mp.entity.douson.TroubleAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.TroubleEntity;
import com.lead.fund.base.server.mp.entity.douson.VocationEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpAccountMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpFileMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpRoleMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.AccidentAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.AccidentMapper;
import com.lead.fund.base.server.mp.mapper.douson.CrashAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.CrashMapper;
import com.lead.fund.base.server.mp.mapper.douson.DeviceMapper;
import com.lead.fund.base.server.mp.mapper.douson.EquipmentAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.EquipmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.EventAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.EventMapper;
import com.lead.fund.base.server.mp.mapper.douson.ImproveAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.ImproveMapper;
import com.lead.fund.base.server.mp.mapper.douson.MaintainAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.MaintainMapper;
import com.lead.fund.base.server.mp.mapper.douson.OrderMapper;
import com.lead.fund.base.server.mp.mapper.douson.ParamMapper;
import com.lead.fund.base.server.mp.mapper.douson.ProductMapper;
import com.lead.fund.base.server.mp.mapper.douson.ProductPhotoMapper;
import com.lead.fund.base.server.mp.mapper.douson.QualityAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.QualityMapper;
import com.lead.fund.base.server.mp.mapper.douson.ReportMapper;
import com.lead.fund.base.server.mp.mapper.douson.ReportPhotoMapper;
import com.lead.fund.base.server.mp.mapper.douson.ReportSerialNoMapper;
import com.lead.fund.base.server.mp.mapper.douson.ReportUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.TroubleAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.TroubleMapper;
import com.lead.fund.base.server.mp.mapper.douson.VocationMapper;
import com.lead.fund.base.server.mp.request.AccidentPageRequest;
import com.lead.fund.base.server.mp.request.AccidentQueryRequest;
import com.lead.fund.base.server.mp.request.AccidentRequest;
import com.lead.fund.base.server.mp.request.BoxFlagPageRequest;
import com.lead.fund.base.server.mp.request.BoxFlagRequest;
import com.lead.fund.base.server.mp.request.ComputerPageRequest;
import com.lead.fund.base.server.mp.request.ComputerRequest;
import com.lead.fund.base.server.mp.request.CrashPageRequest;
import com.lead.fund.base.server.mp.request.CrashRequest;
import com.lead.fund.base.server.mp.request.DeviceCheckLedgerRequest;
import com.lead.fund.base.server.mp.request.DeviceQueryRequest;
import com.lead.fund.base.server.mp.request.DisqualificationOrderPageRequest;
import com.lead.fund.base.server.mp.request.DisqualificationOrderRequest;
import com.lead.fund.base.server.mp.request.EquipmentPageRequest;
import com.lead.fund.base.server.mp.request.EquipmentRequest;
import com.lead.fund.base.server.mp.request.EventPageRequest;
import com.lead.fund.base.server.mp.request.EventQueryRequest;
import com.lead.fund.base.server.mp.request.EventRequest;
import com.lead.fund.base.server.mp.request.ImprovePageRequest;
import com.lead.fund.base.server.mp.request.ImproveRequest;
import com.lead.fund.base.server.mp.request.MaintainPageRequest;
import com.lead.fund.base.server.mp.request.MaintainRequest;
import com.lead.fund.base.server.mp.request.MpAccountQueryPageRequest;
import com.lead.fund.base.server.mp.request.MpAccountRequest;
import com.lead.fund.base.server.mp.request.OrderPageRequest;
import com.lead.fund.base.server.mp.request.OrderQueryRequest;
import com.lead.fund.base.server.mp.request.OrderRequest;
import com.lead.fund.base.server.mp.request.ParamRequest;
import com.lead.fund.base.server.mp.request.PlanPageRequest;
import com.lead.fund.base.server.mp.request.PlanRequest;
import com.lead.fund.base.server.mp.request.ProductPageRequest;
import com.lead.fund.base.server.mp.request.ProductQueryRequest;
import com.lead.fund.base.server.mp.request.ProductRequest;
import com.lead.fund.base.server.mp.request.QualityPageRequest;
import com.lead.fund.base.server.mp.request.QualityRequest;
import com.lead.fund.base.server.mp.request.ReportQueryRequest;
import com.lead.fund.base.server.mp.request.ReportRequest;
import com.lead.fund.base.server.mp.request.TroublePageRequest;
import com.lead.fund.base.server.mp.request.TroubleRequest;
import com.lead.fund.base.server.mp.request.VocationPageRequest;
import com.lead.fund.base.server.mp.request.VocationRequest;
import com.lead.fund.base.server.mp.response.AccidentResponse;
import com.lead.fund.base.server.mp.response.BoxFlagResponse;
import com.lead.fund.base.server.mp.response.ComputerResponse;
import com.lead.fund.base.server.mp.response.CrashResponse;
import com.lead.fund.base.server.mp.response.DeviceCheckLedgerResponse;
import com.lead.fund.base.server.mp.response.DisqualificationOrderResponse;
import com.lead.fund.base.server.mp.response.EquipmentResponse;
import com.lead.fund.base.server.mp.response.EventResponse;
import com.lead.fund.base.server.mp.response.ImproveResponse;
import com.lead.fund.base.server.mp.response.MaintainResponse;
import com.lead.fund.base.server.mp.response.MaintainSummaryResponse;
import com.lead.fund.base.server.mp.response.MpAccountResponse;
import com.lead.fund.base.server.mp.response.MpRoleResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.OrderResponse;
import com.lead.fund.base.server.mp.response.ParamConfigResponse;
import com.lead.fund.base.server.mp.response.ParamResponse;
import com.lead.fund.base.server.mp.response.ParamResponse.ParamResponseBuilder;
import com.lead.fund.base.server.mp.response.PlanResponse;
import com.lead.fund.base.server.mp.response.ProductResponse;
import com.lead.fund.base.server.mp.response.QualityResponse;
import com.lead.fund.base.server.mp.response.ReportResponse;
import com.lead.fund.base.server.mp.response.ReportSummaryResponse;
import com.lead.fund.base.server.mp.response.TodoData;
import com.lead.fund.base.server.mp.response.TodoResponse;
import com.lead.fund.base.server.mp.response.TroubleResponse;
import com.lead.fund.base.server.mp.response.UserDeviceResponse;
import com.lead.fund.base.server.mp.response.VocationResponse;
import com.lead.fund.base.server.mp.response.VocationSummaryResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DuplicateKeyException;
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

/**
 * IndustryController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused"})
@RestController
@RequestMapping("/douson")
@Slf4j
@Validated
public class DousonController {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductPhotoDao productPhotoDao;
    @Resource
    private ParamMapper paramMapper;
    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private ParamDao paramDao;
    @Resource
    private AccountHelper accountHelper;
    @Resource
    private MpAccountMapper accountMapper;
    @Resource
    private ProductPhotoMapper productPhotoMapper;
    @Resource
    private UrlHelper urlHelper;
    @Resource
    private MpRoleMapper roleMapper;
    @Resource
    private ReportMapper reportMapper;
    @Resource
    private ReportPhotoMapper reportPhotoMapper;
    @Resource
    private ReportPhotoDao reportPhotoDao;
    @Resource
    private ReportSerialNoMapper reportSerialNoMapper;
    @Resource
    private ReportSerialNoDao reportSerialNoDao;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private ReportUserMapper reportUserMapper;
    @Resource
    private AccidentMapper accidentMapper;
    @Resource
    private AccidentAttachmentMapper accidentAttachmentMapper;
    @Resource
    private AccidentAttachmentDao accidentAttachmentDao;
    @Resource
    private BoxFlagDao boxFlagDao;
    @Resource
    private BoxFlagSerialNoDao boxFlagSerialNoDao;
    @Resource
    private BoxFlagPhotoDao boxFlagPhotoDao;
    @Resource
    private DisqualificationOrderDao disqualificationOrderDao;
    @Resource
    private DisqualificationOrderPhotoDao disqualificationOrderPhotoDao;
    @Resource
    private PlanDao planDao;
    @Resource
    private PlanPhotoDao planPhotoDao;
    @Resource
    private PlanAttachmentDao planAttachmentDao;
    @Resource
    private MpFileMapper fileMapper;
    @Resource
    private DeviceCheckLedgerDao deviceCheckLedgerDao;
    @Resource
    private DeviceCheckLedgerPhotoDao deviceCheckLedgerPhotoDao;
    @Resource
    private ComputerDao computerDao;
    @Resource
    private ComputerPhotoDao computerPhotoDao;
    @Resource
    private EventMapper eventMapper;
    @Resource
    private QualityMapper qualityMapper;
    @Resource
    private QualityAttachmentMapper qualityAttachmentMapper;
    @Resource
    private QualityAttachmentDao qualityAttachmentDao;
    @Resource
    private CrashMapper crashMapper;
    @Resource
    private CrashAttachmentMapper crashAttachmentMapper;
    @Resource
    private CrashAttachmentDao crashAttachmentDao;
    @Resource
    private TroubleMapper troubleMapper;
    @Resource
    private TroubleAttachmentMapper troubleAttachmentMapper;
    @Resource
    private TroubleAttachmentDao troubleAttachmentDao;
    @Resource
    private EventAttachmentMapper eventAttachmentMapper;
    @Resource
    private EventAttachmentDao eventAttachmentDao;
    @Resource
    private ImproveMapper improveMapper;
    @Resource
    private ImproveAttachmentMapper improveAttachmentMapper;
    @Resource
    private ImproveAttachmentDao improveAttachmentDao;
    @Resource
    private EquipmentMapper equipmentMapper;
    @Resource
    private EquipmentAttachmentMapper equipmentAttachmentMapper;
    @Resource
    private EquipmentAttachmentDao equipmentAttachmentDao;
    @Resource
    private MaintainMapper maintainMapper;
    @Resource
    private MaintainAttachmentMapper maintainAttachmentMapper;
    @Resource
    private MaintainAttachmentDao maintainAttachmentDao;
    @Resource
    private VocationMapper vocationMapper;

    /**
     * 配置类接口（字典接口）
     *
     * @param request {@link ParamRequest}
     * @return {@link DataResult<ParamResponse>}
     */
    @GetMapping("config")
    public DataResult<ParamResponse> config(@ModelAttribute ParamRequest request) {
        final ParamResponseBuilder builder = ParamResponse.builder();
        for (String categoryId : request.getCategoryIdList()) {
            switch (categoryId) {
                case "device", "testDevice" -> builder.testDeviceList(
                        deviceMapper.selectList(new LambdaQueryWrapper<DeviceEntity>().orderByAsc(DeviceEntity::getSorter))
                                .stream()
                                .map(t -> (ParamConfigResponse) new ParamConfigResponse().setValue(t.getId()).setLabel(t.getDeviceName()))
                                .collect(Collectors.toList())
                );
                case "processProcedure" -> builder.processProcedureList(paramDao.listByCategoryId(categoryId));
                case "schedule" -> builder.scheduleList(paramDao.listByCategoryId(categoryId));
                case "workMinute" -> builder.workMinuteList(
                        paramDao.listByCategoryId(categoryId)
                                .stream()
                                .peek(t -> t.setValue(defaultDecimal(t.getValue())))
                                .collect(Collectors.toList())
                );
                case "processType" -> builder.processTypeList(paramDao.listByCategoryId(categoryId));
                case "stopWorkingContent1" -> builder.stopWorkingContent1List(paramDao.listByCategoryId(categoryId));
                case "stopWorkingContent2" -> builder.stopWorkingContent2List(paramDao.listByCategoryId(categoryId));
                case "stopWorkingContent3" -> builder.stopWorkingContent3List(paramDao.listByCategoryId(categoryId));
                case "customerShortName" -> builder.customerShortNameList(paramDao.listByCategoryId(categoryId));
                case "accidentType" -> builder.accidentTypeList(paramDao.listByCategoryId(categoryId));
                case "process" -> builder.processList(paramDao.listByCategoryId(categoryId));
                case "checkPoint" -> builder.checkPointList(paramDao.listByCategoryId(categoryId));
                case "qualityDealOpinion" -> builder.qualityDealOpinionList(paramDao.listByCategoryId(categoryId));
                case "skillDealOpinion" -> builder.skillDealOpinionList(paramDao.listByCategoryId(categoryId));
                case "defectType" -> builder.defectTypeList(paramDao.listByCategoryId(categoryId));
                case "department" -> builder.departmentList(paramDao.listByCategoryId(categoryId));
                case "profession" -> builder.professionList(paramDao.listByCategoryId(categoryId));
                case "optimizeType" -> builder.optimizeTypeList(paramDao.listByCategoryId(categoryId));
                case "chineseVietnamName" -> builder.chineseVietnamNameList(paramDao.listByCategoryId(categoryId));
                case "calibrationPeriod" -> builder.calibrationPeriodList(
                        paramDao.listByCategoryId(categoryId)
                                .stream()
                                .peek(t -> t.setValue(NumberUtil.defaultInt(t.getValue())))
                                .collect(Collectors.toList())
                );
                case "storage" -> builder.storageList(paramDao.listByCategoryId(categoryId));
                case "deviceCheckLedgerState" -> builder.deviceCheckLedgerStateList(paramDao.listByCategoryId(categoryId));
                case "userProperty" -> builder.userPropertyList(paramDao.listByCategoryId(categoryId));
                case "computerName" -> builder.computerNameList(paramDao.listByCategoryId(categoryId));
                case "companyPosition" -> builder.companyPositionList(paramDao.listByCategoryId(categoryId));
                case "computerState" -> builder.computerStateList(paramDao.listByCategoryId(categoryId));
                case "eventReason" -> builder.eventReasonList(paramDao.listByCategoryId(categoryId));
                case "equipmentNo" -> builder.equipmentNoList(
                        equipmentMapper.selectList(new LambdaQueryWrapper<EquipmentEntity>().orderByAsc(EquipmentEntity::getCreatedTime))
                                .stream()
                                .map(t -> (ParamConfigResponse) new ParamConfigResponse().setValue(t.getId()).setLabel(t.getEquipmentNo()))
                                .collect(Collectors.toList())
                );
                case "equipmentPosition" -> builder.equipmentPositionList(paramDao.listByCategoryId(categoryId));
                case "brokenReason" -> builder.brokenReasonList(paramDao.listByCategoryId(categoryId));
                case "repairType" -> builder.repairTypeList(paramDao.listByCategoryId(categoryId));
                case "improveReason" -> builder.improveReasonList(paramDao.listByCategoryId(categoryId));
                case "qualityReason" -> builder.qualityReasonList(paramDao.listByCategoryId(categoryId));
                case "crashReason" -> builder.crashReasonList(paramDao.listByCategoryId(categoryId));
                case "troubleReason" -> builder.troubleReasonList(paramDao.listByCategoryId(categoryId));
                case "vocationType" -> builder.vocationTypeList(paramDao.listByCategoryId(categoryId));
                default -> {
                }
            }
        }
        return new DataResult<>(
                builder
                        .build()
        );
    }

    /**
     * 待办列表
     *
     * @param deviceId 设备ID
     * @return {@link DataResult<TodoResponse>}
     */
    @GetMapping("todo/list")
    public DataResult<TodoResponse> todoList(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId) {
        final TodoResponse r = new TodoResponse();
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final List<String> disqualificationIdList = u.getRoleCodeList().stream().noneMatch("qualityManager"::equals) ? new ArrayList<>() : disqualificationOrderDao.list(new LambdaQueryWrapper<DisqualificationOrderEntity>()
                        .isNull(DisqualificationOrderEntity::getQualityDealOpinion)
                        .or(true, lam -> lam.eq(DisqualificationOrderEntity::getQualityDealOpinion, ""))
                        .select(DisqualificationOrderEntity::getId)
                ).stream().map(DisqualificationOrderEntity::getId)
                .collect(Collectors.toList());
        final List<String> technologyIdList = u.getRoleCodeList().stream().noneMatch("technologyManager"::equals) ? new ArrayList<>() : disqualificationOrderDao.list(new LambdaQueryWrapper<DisqualificationOrderEntity>()
                        .isNull(DisqualificationOrderEntity::getSkillDealOpinion)
                        .or(true, lam -> lam.eq(DisqualificationOrderEntity::getSkillDealOpinion, ""))
                        .select(DisqualificationOrderEntity::getId)
                ).stream().map(DisqualificationOrderEntity::getId)
                .collect(Collectors.toList());
        final List<String> managerIdList = u.getRoleCodeList().stream().noneMatch("manager"::equals) ? new ArrayList<>() : disqualificationOrderDao.list(new LambdaQueryWrapper<DisqualificationOrderEntity>()
                        .or(true, lam -> lam
                                .isNull(DisqualificationOrderEntity::getFineAmount)
                        )
                        .or(true, lam -> lam
                                .isNull(DisqualificationOrderEntity::getRemark)
                        )
                        .or(true, lam -> lam
                                .eq(DisqualificationOrderEntity::getRemark, "")
                        )
                        .select(DisqualificationOrderEntity::getId)
                ).stream().map(DisqualificationOrderEntity::getId)
                .collect(Collectors.toList());
        final List<String> eventIdList = eventMapper.selectList(new LambdaQueryWrapper<EventEntity>()
                        .and(true, lam ->
                                lam.eq(EventEntity::getUserId, u.getUserId())
                                        .or(true, lam1 -> lam1.eq(EventEntity::getDirectLeader, u.getUserId()))
                        )
                        .eq(EventEntity::getValid, false)
                        .select(EventEntity::getId)
                ).stream().map(EventEntity::getId)
                .collect(Collectors.toList());
        final List<String> qualityIdList = qualityMapper.selectList(new LambdaQueryWrapper<QualityEntity>()
                        .and(true, lam ->
                                lam.eq(QualityEntity::getUserId, u.getUserId())
                                        .or(true, lam1 -> lam1.eq(QualityEntity::getDirectLeader, u.getUserId()))
                        )
                        .eq(QualityEntity::getValid, false)
                        .select(QualityEntity::getId)
                ).stream().map(QualityEntity::getId)
                .collect(Collectors.toList());
        final List<String> crashIdList = crashMapper.selectList(new LambdaQueryWrapper<CrashEntity>()
                        .and(true, lam ->
                                lam.eq(CrashEntity::getUserId, u.getUserId())
                                        .or(true, lam1 -> lam1.eq(CrashEntity::getDirectLeader, u.getUserId()))
                        )
                        .eq(CrashEntity::getValid, false)
                        .select(CrashEntity::getId)
                ).stream().map(CrashEntity::getId)
                .collect(Collectors.toList());
        final List<String> troubleIdList = troubleMapper.selectList(new LambdaQueryWrapper<TroubleEntity>()
                        .and(true, lam ->
                                lam.eq(TroubleEntity::getUserId, u.getUserId())
                                        .or(true, lam1 -> lam1.eq(TroubleEntity::getDirectLeader, u.getUserId()))
                        )
                        .eq(TroubleEntity::getValid, false)
                        .select(TroubleEntity::getId)
                ).stream().map(TroubleEntity::getId)
                .collect(Collectors.toList());
        final List<String> improveIdList = improveMapper.selectList(new LambdaQueryWrapper<ImproveEntity>()
                        .and(true, lam ->
                                lam.eq(ImproveEntity::getUserId, u.getUserId())
                                        .or(true, lam1 -> lam1.eq(ImproveEntity::getDirectLeader, u.getUserId()))
                        )
                        .eq(ImproveEntity::getValid, false)
                        .select(ImproveEntity::getId)
                ).stream().map(ImproveEntity::getId)
                .collect(Collectors.toList());
        final List<String> maintainIdList = maintainMapper.selectList(new LambdaQueryWrapper<MaintainEntity>()
                        .eq(MaintainEntity::getPartyUser, u.getUserId())
                        .eq(MaintainEntity::getValid, false)
                        .select(MaintainEntity::getId)
                ).stream().map(MaintainEntity::getId)
                .collect(Collectors.toList());
        final List<String> idList = Stream.of(disqualificationIdList, technologyIdList, managerIdList, eventIdList, improveIdList, maintainIdList, qualityIdList, crashIdList, troubleIdList)
                .flatMap(Collection::stream)
                .distinct().toList();
        AtomicInteger index = new AtomicInteger(0);
        return new DataResult<>(r
                .setCount(idList.size())
                .setList(idList.stream().map(t -> {
                    final List<String> sceneList = new ArrayList<>();
                    String labelTail = "Null";
                    int type = -1;
                    if (disqualificationIdList.contains(t)) {
                        sceneList.add("disqualification");
                        labelTail = "Order";
                        type = 0;
                    }
                    if (technologyIdList.contains(t)) {
                        sceneList.add("technology");
                        labelTail = "Order";
                        type = 0;
                    }
                    if (managerIdList.contains(t)) {
                        sceneList.add("manager");
                        labelTail = "Order";
                        type = 0;
                    }
                    if (eventIdList.contains(t)) {
                        sceneList.add("EHS");
                        labelTail = "EHS";
                        type = 1;
                    }
                    if (improveIdList.contains(t)) {
                        sceneList.add("improve");
                        labelTail = "Improve";
                        type = 2;
                    }
                    if (maintainIdList.contains(t)) {
                        sceneList.add("maintain");
                        labelTail = "Maintain";
                        type = 3;
                    }
                    if (qualityIdList.contains(t)) {
                        sceneList.add("quality");
                        labelTail = "Quality";
                        type = 4;
                    }
                    if (crashIdList.contains(t)) {
                        sceneList.add("crash");
                        labelTail = "Crash";
                        type = 5;
                    }
                    if (troubleIdList.contains(t)) {
                        sceneList.add("trouble");
                        labelTail = "Trouble";
                        type = 6;
                    }
                    return new TodoData()
                            .setId(t)
                            .setLabel(index.addAndGet(1) + "、" + labelTail)
                            .setType(type)
                            .setSceneList(sceneList);
                }).collect(Collectors.toList()))
        );
    }

    /**
     * 合并订单
     *
     * @param e {@link OrderEntity}
     * @return {@link OrderEntity}
     */
    private OrderEntity mergeRelevance(OrderEntity e) {
        OrderEntity order = orderMapper.selectOne(new LambdaQueryWrapper<OrderEntity>()
                .eq(OrderEntity::getOrderNo, e.getOrderNo())
                .eq(OrderEntity::getProcessProcedure, e.getProcessProcedure())
                .eq(OrderEntity::getTestDevice, e.getTestDevice())
                .eq(OrderEntity::getProjectSequence, e.getProjectSequence())
        );
        // 插入
        if (null == order) {
            orderMapper.insert(order = e);
        }
        return order;
    }

    private void mergeRelevance(ProductRequest request, ProductEntity e) {
        productPhotoMapper.delete(new LambdaQueryWrapper<ProductPhotoEntity>().eq(ProductPhotoEntity::getProductId, e.getId()));
        if (CollUtil.isNotEmpty(request.getPhotoList())) {
            productPhotoDao.saveBatch(request.getPhotoList().stream().map(t -> {
                ProductPhotoEntity p = INDUSTRY_INSTANCE.photo(t);
                p.setProductId(e.getId());
                return p;
            }).collect(Collectors.toList()));
        }
    }

    /**
     * 保存产品（小程序）
     *
     * @param deviceId 设备id
     * @param request  {@link ProductRequest}
     * @return {@link DataResult<ProductEntity>}
     */
    @PostMapping("product")
    public DataResult<ProductEntity> productSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ProductRequest request
    ) {
        final MpAccountEntity account = accountHelper.getAccount(deviceId);
        ProductEntity e = INDUSTRY_INSTANCE.product(request);
        e.setOpenId(account.getOpenId());
        e.setCreator(account.getOpenId());
        e.setModifier(account.getOpenId());
        e.setOrderId(mergeRelevance(INDUSTRY_INSTANCE.productToOrder(request)).getId());
        try {
            productMapper.insert(e);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(MP_PRODUCT_EXISTS);
        }
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    /**
     * 修改产品（小程序）
     *
     * @param deviceId 设备id
     * @param request  {@link ProductRequest}
     * @return {@link DataResult<ProductEntity>}
     */
    @PutMapping("product")
    public DataResult<ProductEntity> productUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ProductRequest request
    ) {
        String modifier;
        try {
            final MpAccountEntity account = accountHelper.getAccount(deviceId);
            modifier = account.getOpenId();
        } catch (Exception e) {
            modifier = AdminUser.SYSTEM.getCode();
        }
        ProductEntity e = (ProductEntity) INDUSTRY_INSTANCE.product(request)
                .setOrderId(mergeRelevance(INDUSTRY_INSTANCE.productToOrder(request)).getId())
                .setModifier(modifier);
        productMapper.updateById(e);
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    private PageResult<ProductResponse> productPage(ProductPageRequest request) {
        if (isNotBlank(request.getData().getTestDevice()) || isNotBlank(request.getData().getProcessProcedure()) || isNotBlank(request.getData().getOrderNo())) {
            LambdaQueryWrapper<OrderEntity> lambda = new LambdaQueryWrapper<>();
            if (isNotBlank(request.getData().getTestDevice())) {
                lambda.eq(OrderEntity::getTestDevice, request.getData().getTestDevice());
            }
            if (isNotBlank(request.getData().getProcessProcedure())) {
                lambda.eq(OrderEntity::getProcessProcedure, request.getData().getProcessProcedure());
            }
            if (isNotBlank(request.getData().getOrderNo())) {
                lambda.eq(OrderEntity::getOrderNo, request.getData().getOrderNo());
            }
            final List<String> orderIdList = orderMapper.selectList(lambda)
                    .stream().map(AbstractPrimaryKey::getId)
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(orderIdList)) {
                request.getData().setOrderIdList(orderIdList);
            } else {
                return new PageResult<>();
            }
        }
        PageResult<ProductEntity> pr = DatabaseUtil.page(request, this::productList);
        return new PageResult<>(pr.getTotal(), formatProductList(pr.getList()));
    }

    /**
     * 查询产品分页（小程序）
     *
     * @param deviceId 设备id
     * @param request  {@link ProductPageRequest}
     * @return {@link PageResult<ProductResponse>}
     */
    @GetMapping("product/page")
    public PageResult<ProductResponse> productPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ProductPageRequest request
    ) {
        final MpAccountEntity account = accountHelper.getAccount(deviceId);
        request.getData().setOpenId(account.getOpenId());
        return productPage(request);
    }

    /**
     * 查询产品明细（小程序）
     *
     * @param deviceId 设备id
     * @param request  {@link ProductQueryRequest}
     * @return {@link PageResult<ProductResponse>}
     */
    @GetMapping("product")
    public DataResult<ProductResponse> product(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ProductQueryRequest request
    ) {
        final MpAccountEntity account = accountHelper.getAccount(deviceId);
        request.setOpenId(account.getOpenId());
        if (isNotBlank(request.getTestDevice())) {
            final List<String> orderIdList = orderMapper.selectList(new LambdaQueryWrapper<OrderEntity>().eq(OrderEntity::getTestDevice, request.getTestDevice()))
                    .stream().map(AbstractPrimaryKey::getId)
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(orderIdList)) {
                request.setOrderIdList(orderIdList);
            } else {
                return new DataResult<>();
            }
        }
        return new DataResult<>(CollUtil.getFirst(formatProductList(productList(request))));
    }

    /**
     * 保存产品（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link ProductRequest}
     * @return {@link DataResult<ProductEntity>}
     */
    @PostMapping("admin/product")
    public DataResult<ProductEntity> productAdminSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ProductRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final List<String> matchRoleCodeList = CollUtil.toList(
                AdminRole.ADMIN.getCode(),
                "engineer"
        );
        if (u.getRoleCodeList().stream().noneMatch(matchRoleCodeList::contains)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        ProductEntity e = (ProductEntity) INDUSTRY_INSTANCE.product(request)
                .setOrderId(mergeRelevance(INDUSTRY_INSTANCE.productToOrder(request)).getId())
                .setCreator(u.getUserId())
                .setModifier(u.getUserId());
        try {
            productMapper.insert(e);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(MP_PRODUCT_EXISTS);
        }
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    /**
     * 修改产品（管理员）
     *
     * @param deviceId 设备id
     * @param request  {@link ProductRequest}
     * @return {@link DataResult<ProductEntity>}
     */
    @PutMapping("admin/product")
    public DataResult<ProductEntity> productAdminUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ProductRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final List<String> masterRoleCodeList = CollUtil.toList(
                AdminRole.ADMIN.getCode()
        );
        final List<String> matchRoleCodeList = CollUtil.toList(
                "engineer"
        );
        CollUtil.addAll(matchRoleCodeList, masterRoleCodeList);
        if (u.getRoleCodeList().stream().noneMatch(matchRoleCodeList::contains)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        String modifier = u.getUserId();
        ProductEntity e = (ProductEntity) INDUSTRY_INSTANCE.product(request)
                .setOrderId(mergeRelevance(INDUSTRY_INSTANCE.productToOrder(request)).getId())
                .setModifier(modifier);
        LambdaUpdateWrapper<ProductEntity> lambda = new LambdaUpdateWrapper<ProductEntity>()
                .eq(ProductEntity::getId, request.getProductId());
        if (u.getRoleCodeList().stream().noneMatch(masterRoleCodeList::contains)) {
            lambda.eq(ProductEntity::getCreator, u.getUserId());
        }
        if (productMapper.update(
                e,
                lambda
        ) <= 0) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    private List<ProductEntity> productList(ProductQueryRequest request) {
        LambdaQueryWrapper<ProductEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getProductId())) {
            lambda.eq(ProductEntity::getId, request.getProductId());
        }
        if (CollUtil.isNotEmpty(request.getOpenIdList())) {
            DatabaseUtil.or(lambda, request.getOpenIdList(), (l, dl) -> l.in(ProductEntity::getOpenId, dl));
        }
        if (isNotBlank(request.getOpenId())) {
            lambda.eq(ProductEntity::getOpenId, request.getOpenId());
        }
        if (CollUtil.isNotEmpty(request.getOrderIdList())) {
            DatabaseUtil.or(lambda,
                    request.getOrderIdList(),
                    (t, l) -> t.in(ProductEntity::getOrderId, l));
        }
        if (isNotBlank(request.getDesignNumber())) {
            lambda.like(ProductEntity::getDesignNumber, request.getDesignNumber());
        }
        if (null != request.getStartReportDate()) {
            lambda.ge(ProductEntity::getReportDate, request.getStartReportDate());
        }
        if (null != request.getEndReportDate()) {
            lambda.le(ProductEntity::getReportDate, cn.hutool.core.date.DateUtil.endOfDay(request.getEndReportDate()).toJdkDate());
        }
        return productMapper.selectList(lambda.orderByDesc(ProductEntity::getReportDate).orderByAsc(ProductEntity::getOpenId).orderByAsc(ProductEntity::getOrderId));
    }

    private List<ProductResponse> formatProductList(List<ProductEntity> l) {
        final List<ProductResponse> list = INDUSTRY_INSTANCE.list(l);
        // open id
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getOpenId())).collect(Collectors.toList()),
                ProductResponse::getOpenId,
                l1 -> accountMapper.selectList(
                        new LambdaQueryWrapper<MpAccountEntity>()
                                .in(MpAccountEntity::getOpenId, l1)
                                .select(
                                        MpAccountEntity::getOpenId,
                                        MpAccountEntity::getNickname,
                                        MpAccountEntity::getMobile
                                )
                ),
                (t, r) -> t.getOpenId().equals(r.getOpenId()),
                INDUSTRY_INSTANCE::account
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getModifier())).collect(Collectors.toList()),
                ProductResponse::getModifier,
                l1 -> userMapper.selectList(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .in(MpUserEntity::getId, l1)
                                .select(
                                        MpUserEntity::getId,
                                        MpUserEntity::getUsername,
                                        MpUserEntity::getName
                                )
                ),
                (t, r) -> t.getModifier().equals(r.getId()),
                (t, r) -> t.setModifierFormat(r.getUsername())
        );
        MultitaskUtil.supplementList(
                list,
                ProductResponse::getProductId,
                l1 -> productPhotoMapper.selectList(
                        new LambdaQueryWrapper<ProductPhotoEntity>()
                                .in(ProductPhotoEntity::getProductId, l1)
                ),
                (r, t) -> r.getProductId().equals(t.getProductId()),
                (r, t) -> INDUSTRY_INSTANCE.photo(r, t, urlHelper.getUrlPrefix())
        );
        MultitaskUtil.supplementList(
                list,
                ProductResponse::getOrderId,
                l1 -> orderMapper.selectList(
                        new LambdaQueryWrapper<OrderEntity>()
                                .in(OrderEntity::getId, l1)
                ),
                (r, t) -> r.getOrderId().equals(t.getId()),
                INDUSTRY_INSTANCE::formatProduct
        );
        MultitaskUtil.supplementList(
                list,
                ProductResponse::getTestDevice,
                l1 -> deviceMapper.selectList(
                        new LambdaQueryWrapper<DeviceEntity>()
                                .in(DeviceEntity::getId, l1)
                ),
                (r, t) -> r.getTestDevice().equals(t.getId()),
                INDUSTRY_INSTANCE::deviceDecorateProduct
        );
        MultitaskUtil.supplementList(
                list,
                ProductResponse::getProcessProcedure,
                l1 -> paramDao.listByCategoryId("processProcedure"),
                (t, r) -> t.getProcessProcedure().equals(r.getValue()),
                (t, r) -> t.setProcessProcedureFormat(r.getLabel())
        );
        for (ProductResponse t : list) {
            if (isNotBlank(t.getOpenIdFormat())) {
                t.setUsernameFormat(t.getOpenIdFormat() + "(Wechat)");
            } else {
                t.setUsernameFormat(t.getModifierFormat() + "(Admin)");
            }
        }
        return list;
    }

    /**
     * 产品分页（管理员）
     *
     * @param deviceId 设备id
     * @param request  {@link ProductPageRequest}
     * @return {@link PageResult<ProductResponse>}
     */
    @GetMapping("admin/product/page")
    public PageResult<ProductResponse> adminProductPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ProductPageRequest request
    ) {
        accountHelper.getUser(deviceId);
        if (isNotBlank(request.getData().getNickname())) {
            request.getData().setOpenIdList(accountMapper.selectList(new LambdaQueryWrapper<MpAccountEntity>().like(MpAccountEntity::getNickname, request.getData().getNickname()))
                    .stream().map(MpAccountEntity::getOpenId)
                    .collect(Collectors.toList()));
            if (CollUtil.isEmpty(request.getData().getOpenIdList())) {
                return new PageResult<>();
            }
        }
        return productPage(request);
    }

    private ProductResponse formatProduct(ProductEntity t) {
        return CollUtil.getFirst(formatProductList(CollUtil.toList(t)));
    }

    /**
     * 产品明细查询
     *
     * @param deviceId  设备id
     * @param productId 产品id
     * @return {@link DataResult<ProductResponse>}
     */
    @GetMapping("admin/product")
    public DataResult<ProductResponse> adminProduct(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestParam String productId
    ) {
        return new DataResult<>(formatProduct(productMapper.selectById(productId)));
    }

    /**
     * 产品下载
     *
     * @param resp {@link HttpServletResponse}
     * @return excel
     */
    @GetMapping("admin/product/export")
    public ResponseEntity<byte[]> productExport(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            HttpServletResponse resp,
            @ModelAttribute ProductQueryRequest request) {
        accountHelper.checkUserAdmin(deviceId);
        List<ProductResponse> l = productPage(
                (ProductPageRequest)
                        new ProductPageRequest()
                                .setData(request)
                                .setPage(new Page(VARIABLE_PAGE_DEFAULT, VARIABLE_PAGE_MAX))

        ).getList();
        byte[] bytes = new byte[0];
        //noinspection ConstantConditions
        try (Workbook workbook = new XSSFWorkbook(this.getClass().getResourceAsStream("/product.xlsx"))) {
            int rowCount;
            CellStyle bodyCellStyle = getBodyCellStyle(workbook);
            CellStyle integerCellStyle = getIntegerCellStyle(workbook);
            CellStyle numberCellStyle = getNumberCellStyle(workbook);
            CellStyle dateCellStyle = getDateCellStyle(workbook);
            Sheet sheet = workbook.getSheetAt(0);
            rowCount = 1;
            for (ProductResponse t : l) {
                int cellCount = 0;
                Row r = sheet.createRow(rowCount++);
                Cell c = r.createCell(cellCount++);
                c.setCellStyle(bodyCellStyle);
                c.setCellValue(t.getOpenIdFormat());
                Cell c7 = r.createCell(cellCount++);
                c7.setCellStyle(dateCellStyle);
                c7.setCellValue(DateUtil.parse(t.getReportDate()));
                Cell c4 = r.createCell(cellCount++);
                c4.setCellStyle(bodyCellStyle);
                c4.setCellValue(t.getOrderNo());
                Cell c5 = r.createCell(cellCount++);
                c5.setCellStyle(bodyCellStyle);
                c5.setCellValue(t.getProjectSequence());
                Cell c6 = r.createCell(cellCount++);
                c6.setCellStyle(bodyCellStyle);
                c6.setCellValue(t.getDesignNumber());
                Cell c1 = r.createCell(cellCount++);
                c1.setCellStyle(dateCellStyle);
                c1.setCellValue(t.getTestDeviceFormat());
                Cell c2 = r.createCell(cellCount++);
                c2.setCellStyle(dateCellStyle);
                c2.setCellValue(t.getProcessProcedureFormat());
                Cell c8 = r.createCell(cellCount++);
                c8.setCellStyle(bodyCellStyle);
                c8.setCellValue(t.getProgramNumber());
                Cell c10 = r.createCell(cellCount++);
                c10.setCellStyle(integerCellStyle);
                c10.setCellValue(t.getDebugMinute());
                Cell c11 = r.createCell(cellCount++);
                c11.setCellStyle(integerCellStyle);
                c11.setCellValue(t.getClampingMinute());
                Cell c12 = r.createCell(cellCount++);
                c12.setCellStyle(integerCellStyle);
                c12.setCellValue(t.getAssistMinute());
                Cell c13 = r.createCell(cellCount++);
                c13.setCellStyle(integerCellStyle);
                c13.setCellValue(t.getRunningMinute());
                Cell c14 = r.createCell(cellCount++);
                c14.setCellStyle(numberCellStyle);
                c14.setCellValue(t.getCountHour8().doubleValue());
                Cell c15 = r.createCell(cellCount++);
                c15.setCellStyle(numberCellStyle);
                c15.setCellValue(t.getCountHour12().doubleValue());
                Cell c9 = r.createCell(cellCount);
                c9.setCellStyle(bodyCellStyle);
                c9.setCellValue(t.getRemark());
            }
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                workbook.write(baos);
                bytes = baos.toByteArray();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            headers.setContentDispositionFormData("attachment", new String(("product" + ".xlsx").getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            log.warn("set attachment error");
        }
        resp.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    /**
     * 保存参数
     *
     * @param deviceId 设备id
     * @param request  {@link ParamEntity}
     * @return {@link Result}
     */
    @PostMapping("admin/param")
    public Result paramSave(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID, required = false) String deviceId,
            @RequestBody ParamEntity request
    ) {
        accountHelper.checkUserAdmin(deviceId);
        paramMapper.insert(request);
        paramDao.clear(request.getParamCategoryId());
        return new Result();
    }

    /**
     * 修改参数
     *
     * @param deviceId 设备id
     * @param request  {@link ParamEntity}
     * @return {@link Result}
     */
    @PutMapping("admin/param")
    public Result paramUpdate(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID, required = false) String deviceId,
            @RequestBody ParamEntity request
    ) {
        accountHelper.checkUserAdmin(deviceId);
        paramMapper.updateByMultiId(request);
        paramDao.clear(request.getParamCategoryId());
        return new Result();
    }

    /**
     * 删除参数
     *
     * @param deviceId 设备id
     * @param request  {@link ParamEntity}
     * @return {@link Result}
     */
    @DeleteMapping("admin/param")
    public Result paramDelete(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID, required = false) String deviceId,
            @ModelAttribute ParamEntity request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        paramMapper.deleteByMultiId(request);
        paramDao.clear(request.getParamCategoryId());
        return new Result();
    }

    private List<ParamEntity> paramList(ParamEntity request) {
        LambdaQueryWrapper<ParamEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getParamCategoryId())) {
            lambda.eq(ParamEntity::getParamCategoryId, request.getParamCategoryId());
        }
        return paramMapper.selectList(lambda.orderByAsc(ParamEntity::getParamCategoryId).orderByAsc(ParamEntity::getSorter).orderByAsc(ParamEntity::getParamCode));
    }

    /**
     * 查询参数列表
     *
     * @param deviceId 设备id
     * @param request  {@link ParamEntity}
     * @return {@link ListResult<ParamEntity>}
     */
    @GetMapping("admin/param/list")
    public ListResult<ParamEntity> paramAdminPage(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID, required = false) String deviceId,
            @ModelAttribute ParamEntity request
    ) {
        log.info("user: {}", accountHelper.getUser(deviceId));
        return new ListResult<>(paramList(request));
    }

    /**
     * 保存设备
     *
     * @param deviceId 设备id
     * @param request  {@link DeviceEntity}
     * @return {@link Result}
     */
    @PostMapping("admin/device")
    public Result deviceSave(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID, required = false) String deviceId,
            @RequestBody DeviceEntity request
    ) {
        accountHelper.checkUserAdmin(deviceId);
        deviceMapper.insert(request);
        return new Result();
    }

    /**
     * 修改设备
     *
     * @param deviceId 设备id
     * @param request  {@link DeviceEntity}
     * @return {@link Result}
     */
    @PutMapping("admin/device")
    public Result deviceUpdate(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID, required = false) String deviceId,
            @RequestBody DeviceEntity request
    ) {
        accountHelper.checkUserAdmin(deviceId);
        deviceMapper.updateById(request);
        return new Result();
    }

    private List<DeviceEntity> deviceList(DeviceQueryRequest request) {
        LambdaQueryWrapper<DeviceEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getDeviceId())) {
            lambda.eq(DeviceEntity::getId, request.getDeviceId());
        }
        if (isNotBlank(request.getDeviceName())) {
            lambda.like(DeviceEntity::getDeviceName, request.getDeviceName());
        }
        return deviceMapper.selectList(lambda.orderByAsc(DeviceEntity::getSorter));
    }

    /**
     * 查询设备列表
     *
     * @param deviceId 设备id
     * @param request  {@link DeviceQueryRequest}
     * @return {@link ListResult<DeviceEntity>}
     */
    @GetMapping("admin/device/list")
    public ListResult<DeviceEntity> deviceAdminList(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID, required = false) String deviceId,
            @ModelAttribute DeviceQueryRequest request
    ) {
        log.info("user: {}", accountHelper.getUser(deviceId));
        return new ListResult<>(deviceList(request));
    }

    /**
     * 保存订单
     *
     * @param deviceId 设备id
     * @param request  {@link OrderRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/order")
    public Result ordrSave(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID, required = false) String deviceId,
            @RequestBody OrderRequest request
    ) {
        accountHelper.checkUserAdmin(deviceId);
        orderMapper.insert(INDUSTRY_INSTANCE.order(request));
        return new Result();
    }

    /**
     * 修改订单
     *
     * @param deviceId 设备id
     * @param request  {@link OrderRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/order")
    public Result orderUpdate(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID, required = false) String deviceId,
            @RequestBody OrderRequest request
    ) {
        accountHelper.checkUserAdmin(deviceId);
        orderMapper.updateById(INDUSTRY_INSTANCE.order(request));
        return new Result();
    }

    private List<OrderEntity> orderList(OrderQueryRequest request) {
        LambdaQueryWrapper<OrderEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getOrderId())) {
            lambda.eq(OrderEntity::getId, request.getOrderId());
        }
        if (CollUtil.isNotEmpty(request.getOrderIdList())) {
            DatabaseUtil.or(
                    lambda, request.getOrderIdList(),
                    (t, l) -> t.in(OrderEntity::getId, l)
            );
        }
        if (isNotBlank(request.getOrderNo())) {
            lambda.like(OrderEntity::getOrderNo, request.getOrderNo());
        }
        if (isNotBlank(request.getProjectSequence())) {
            lambda.like(OrderEntity::getProjectSequence, request.getProjectSequence());
        }
        if (isNotBlank(request.getTestDevice())) {
            lambda.eq(OrderEntity::getTestDevice, request.getTestDevice());
        }
        return orderMapper.selectList(lambda.orderByAsc(OrderEntity::getTestDevice));
    }

    private List<OrderResponse> formatOrderList(List<OrderEntity> l) {
        final List<OrderResponse> list = INDUSTRY_INSTANCE.orderList(l);
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getProcessProcedure())).collect(Collectors.toList()),
                OrderResponse::getProcessProcedure,
                l1 -> paramDao.listByCategoryId("processProcedure"),
                (t, r) -> t.getProcessProcedure().equals(r.getValue()),
                (t, r) -> t.setProcessProcedureFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getTestDevice())).collect(Collectors.toList()),
                OrderResponse::getTestDevice,
                l1 -> deviceMapper.selectList(
                        new LambdaQueryWrapper<DeviceEntity>()
                                .in(DeviceEntity::getId, l1)
                ),
                (r, t) -> r.getTestDevice().equals(t.getId()),
                (r, t) -> r.setTestDeviceFormat(t.getDeviceName())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getOrderId())).collect(Collectors.toList()),
                OrderResponse::getOrderId,
                l1 -> reportMapper.selectList(
                        new LambdaQueryWrapper<ReportEntity>()
                                .in(ReportEntity::getOrderId, l1)
                ),
                (r, t) -> r.getOrderId().equals(t.getOrderId()),
                (r, t) -> r.setProductId(t.getProductId())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isBlank(t.getProductId())).collect(Collectors.toList()),
                OrderResponse::getOrderId,
                l1 -> productMapper.selectList(
                        new LambdaQueryWrapper<ProductEntity>()
                                .in(ProductEntity::getOrderId, l1)
                ),
                (r, t) -> r.getOrderId().equals(t.getOrderId()),
                (r, t) -> r.setProductId(t.getId())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getProductId())).collect(Collectors.toList()),
                OrderResponse::getProductId,
                l1 -> productMapper.selectList(
                        new LambdaQueryWrapper<ProductEntity>()
                                .in(ProductEntity::getId, l1)
                ),
                (r, t) -> r.getProductId().equals(t.getId()),
                (r, t) -> r.setDesignNumber(t.getDesignNumber())
        );
        return list;
    }

    /**
     * 查询订单列表
     *
     * @param deviceId 设备id
     * @param request  {@link OrderQueryRequest}
     * @return {@link ListResult<OrderResponse>}
     */
    @GetMapping("admin/order/list")
    public ListResult<OrderResponse> orderList(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID, required = false) String deviceId,
            @ModelAttribute OrderQueryRequest request
    ) {
        log.info("user: {}", accountHelper.getUser(deviceId));
        return new ListResult<>(formatOrderList(orderList(request)));
    }

    /**
     * 查询订单分页
     *
     * @param deviceId 设备id
     * @param request  {@link OrderPageRequest}
     * @return {@link PageResult<OrderResponse>}
     */
    @GetMapping("admin/order/page")
    public PageResult<OrderResponse> orderPage(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID, required = false) String deviceId,
            @ModelAttribute OrderPageRequest request
    ) {
        log.info("user: {}", accountHelper.getUser(deviceId));
        if (isNotBlank(request.getData().getDesignNumber())) {
            final List<ProductEntity> productList = productMapper.selectList(
                    new LambdaQueryWrapper<ProductEntity>()
                            .like(ProductEntity::getDesignNumber, request.getData().getDesignNumber())
            );
            List<String> orderIdList = Stream.of(
                            productList.stream().map(ProductEntity::getOrderId),
                            reportMapper.selectList(
                                    DatabaseUtil.or(new LambdaQueryWrapper<>(),
                                            productList.stream().map(AbstractPrimaryKey::getId).collect(Collectors.toList()),
                                            (t, l) -> t.in(ReportEntity::getProductId, l))
                            ).stream().map(ReportEntity::getOrderId)
                    ).flatMap(t -> t)
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(orderIdList)) {
                return new PageResult<>();
            }
            request.getData().setOrderIdList(orderIdList);

        }
        PageResult<OrderEntity> page = DatabaseUtil.page(request, this::orderList);
        return new PageResult<>(page.getTotal(), formatOrderList(page.getList()));
    }

    /**
     * 修改账户状态（管理员）
     *
     * @param deviceId 设备id
     * @param request  {@link MpAccountRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/account/state")
    public Result accountAdminState(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody MpAccountRequest request
    ) {
        MpUserResponse user = accountHelper.checkUserAdmin(deviceId);
        String modifier = user.getUserId();
        if (accountMapper.update(
                null,
                new LambdaUpdateWrapper<MpAccountEntity>()
                        .set(MpAccountEntity::getState, request.getState())
                        .eq(MpAccountEntity::getOpenId, request.getOpenId())
        ) > 0) {
            accountHelper.clearByOpenId(request.getOpenId());
        } else {
            log.info("update null, request: {}", JSONUtil.toJsonStr(request));
        }
        return new Result();
    }

    /**
     * 账户分页（管理员）
     *
     * @param deviceId 设备id
     * @param request  {@link MpAccountQueryPageRequest}
     * @return {@link PageResult<MpAccountResponse>}
     */
    @GetMapping("admin/account/page")
    public PageResult<MpAccountResponse> adminAccountPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute MpAccountQueryPageRequest request
    ) {
        PageResult<MpAccountEntity> pr = DatabaseUtil.page(request, d -> {
            LambdaQueryWrapper<MpAccountEntity> lambda = new LambdaQueryWrapper<>();
            if (isNotBlank(d.getNickname())) {
                lambda.eq(MpAccountEntity::getNickname, d.getNickname());
            }
            if (null != d.getOpenId()) {
                lambda.eq(MpAccountEntity::getOpenId, d.getOpenId());
            }
            return accountMapper.selectList(lambda.orderByDesc(MpAccountEntity::getSignTime));
        });
        final List<MpRoleResponse> roleList = roleMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpRoleEntity>()
                                .eq(MpRoleEntity::getState, AdminState.NORMAL.getCode()),
                        STRING_LIST_ENGINEER_USER_ROLE_CODE_LIST,
                        (t, l) -> t.in(MpRoleEntity::getId, l))
        ).stream().map(MP_SYSTEM_INSTANCE::data).toList();
        final List<MpAccountResponse> list = MP_ACCOUNT_INSTANCE.list(pr.getList());
        list.forEach(t -> t.setRoleList(roleList));
        return new PageResult<>(pr.getTotal(), list);
    }

    private ReportEntity mergeRelevance(ReportRequest request, ReportEntity e) {
        reportSerialNoMapper.delete(new LambdaUpdateWrapper<ReportSerialNoEntity>().eq(ReportSerialNoEntity::getReportId, e.getId()));
        try {
            reportSerialNoDao.saveBatch(request.getSerialNoList().stream().map(t -> new ReportSerialNoEntity()
                    .setReportId(e.getId())
                    .setSerialNo(t)
                    .setOrderId(e.getOrderId())
            ).collect(Collectors.toList()));
        } catch (Exception ex) {
            throw new BusinessException(MP_ORDER_REPEAT);
        }
        reportPhotoMapper.delete(new LambdaUpdateWrapper<ReportPhotoEntity>().eq(ReportPhotoEntity::getReportId, e.getId()));
        reportPhotoDao.saveBatch(request.getPhotoList().stream().map(t -> new ReportPhotoEntity().setReportId(e.getId()).setPhotoUrl(t.getPhotoUrl()).setPhotoCompressUrl(t.getPhotoCompressUrl())).collect(Collectors.toList()));
        orderMapper.update(
                null,
                new LambdaUpdateWrapper<OrderEntity>()
                        .set(OrderEntity::getActualCompleteCount, reportMapper.selectList(new LambdaQueryWrapper<ReportEntity>()
                                        .eq(ReportEntity::getOrderId, e.getOrderId())
                                        .select(ReportEntity::getActualCompleteCount)
                                ).stream().map(ReportEntity::getActualCompleteCount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                        .eq(OrderEntity::getId, e.getOrderId())
        );
        final ReportUserEntity ru = new ReportUserEntity().setReportDate(e.getReportDate()).setUserId(e.getUserId()).setWorkMinute(request.getWorkMinute());
        try {
            reportUserMapper.insert(ru);
        } catch (Exception ex) {
            reportUserMapper.updateByMultiId(ru);
        }
        return e;
    }

    /**
     * 保存日报
     *
     * @param deviceId 设备id
     * @param request  {@link ReportRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/report")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result reportSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ReportRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final ReportEntity e = INDUSTRY_INSTANCE.report(request);
        if (u.getRoleList().stream().noneMatch(t -> AdminUser.ADMIN.getCode().equals(t.getRoleCode()))) {
            e.setUserId(u.getUserId());
        } else if (!u.getUserId().equals(request.getUserId())) {
            throw new BusinessException(MP_OPERATOR_OTHER_NOT_ALLOW);
        }
        final ProductEntity product = productMapper.selectById(request.getProductId());
        final OrderEntity order = orderMapper.selectById(product.getOrderId());
        if (deviceMapper.update(
                null,
                new LambdaUpdateWrapper<DeviceEntity>()
                        .set(DeviceEntity::getRunningHour, request.getDeviceRunningEndHour())
                        .set(DeviceEntity::getRunningMinute, request.getDeviceRunningEndMinute())
                        .eq(DeviceEntity::getId, order.getTestDevice())
                        .eq(DeviceEntity::getRunningHour, request.getDeviceRunningStartHour())
                        .eq(DeviceEntity::getRunningMinute, request.getDeviceRunningStartMinute())
        ) <= 0) {
            throw new BusinessException(MP_RECORD_DEVICE_TIME_ERROR);
        }
        reportMapper.insert(
                (ReportEntity) e
                        .setOrderId(mergeRelevance(INDUSTRY_INSTANCE.reportToOrder(request)).getId())
                        .setClampingMinute(product.getClampingMinute()).setAssistMinute(product.getAssistMinute()).setRunningMinute(product.getRunningMinute())
                        .setCreator(u.getUserId())
                        .setModifier(u.getUserId())
        );
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 修改日报
     *
     * @param deviceId 设备id
     * @param request  {@link ProductRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/report")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result reportUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ReportRequest request
    ) {
        final MpUserResponse u = accountHelper.checkUserAdminOrSelf(deviceId, request.getUserId());
        final ReportEntity e = INDUSTRY_INSTANCE.report(request);
        reportMapper.updateById(
                (ReportEntity) e
                        .setOrderId(mergeRelevance(INDUSTRY_INSTANCE.reportToOrder(request)).getId())
                        .setModifier(u.getUserId())
        );
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 删除日报
     *
     * @param deviceId 设备id
     * @param request  {@link ProductRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/report")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result reportDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ReportRequest request
    ) {
        LambdaUpdateWrapper<ReportEntity> lambda = new LambdaUpdateWrapper<ReportEntity>().eq(ReportEntity::getId, request.getReportId());
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            lambda.eq(ReportEntity::getCreator, u.getUserId());
        }
        if (isNotBlank(request.getReportId())) {
            ReportEntity e = reportMapper.selectById(request.getReportId());
            if (reportMapper.delete(lambda) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
            reportPhotoMapper.delete(new LambdaUpdateWrapper<ReportPhotoEntity>().eq(ReportPhotoEntity::getReportId, request.getReportId()));
            reportSerialNoMapper.delete(new LambdaUpdateWrapper<ReportSerialNoEntity>().eq(ReportSerialNoEntity::getReportId, request.getReportId()));
            orderMapper.update(
                    null,
                    new LambdaUpdateWrapper<OrderEntity>()
                            .setSql("ACTUAL_COMPLETE_COUNT = ACTUAL_COMPLETE_COUNT - " + e.getActualCompleteCount())
                            .eq(OrderEntity::getId, e.getOrderId())
            );
        }
        return new Result();
    }

    private List<ReportEntity> reportList(ReportQueryRequest request) {
        LambdaQueryWrapper<ReportEntity> lambda = new LambdaQueryWrapper<>();
        if (null != request.getStartReportDate()) {
            lambda.ge(ReportEntity::getReportDate, DateUtil.day(request.getStartReportDate()));
        }
        if (null != request.getEndReportDate()) {
            lambda.le(ReportEntity::getReportDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getEndReportDate())));
        }
        if (isNotBlank(request.getReportId())) {
            lambda.eq(ReportEntity::getId, request.getReportId());
        }
        if (null != request.getReportDate()) {
            lambda.eq(ReportEntity::getReportDate, request.getReportDate());
        }
        if (isNotBlank(request.getProductId())) {
            lambda.eq(ReportEntity::getProductId, request.getProductId());
        }
        if (isNotBlank(request.getUserId())) {
            lambda.eq(ReportEntity::getUserId, request.getUserId());
        }
        if (CollUtil.isNotEmpty(request.getOrderIdList())) {
            DatabaseUtil.or(lambda, request.getOrderIdList(), (lam, l) -> lam.in(ReportEntity::getOrderId, l));
        }
        if (CollUtil.isNotEmpty(request.getProductIdList())) {
            DatabaseUtil.or(lambda, request.getProductIdList(), (lam, l) -> lam.in(ReportEntity::getProductId, l));
        }
        if (isNotBlank(request.getProcessType())) {
            lambda.eq(ReportEntity::getProcessType, request.getProcessType());
        }
        if (isNotBlank(request.getUsername())) {
            final List<String> userIdList = userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().like(MpUserEntity::getUsername, request.getUsername()))
                    .stream().map(AbstractPrimaryKey::getId)
                    .distinct().toList();
            if (CollUtil.isNotEmpty(userIdList)) {
                DatabaseUtil.or(lambda, userIdList, (lam, l) -> lam.in(ReportEntity::getUserId, l));
            } else {
                return new ArrayList<>();
            }
        }
        if (null != request.getValid()) {
            lambda.eq(ReportEntity::getValid, request.getValid());
        }
        return reportMapper.selectList(lambda
                .orderByDesc(ReportEntity::getReportDate)
                .orderByAsc(ReportEntity::getUserId)
                .orderByAsc(ReportEntity::getOrderId)
                .orderByAsc(ReportEntity::getProductId)
        );
    }

    private List<ReportResponse> formatReportList(List<ReportEntity> l) {
        if (CollUtil.isEmpty(l)) {
            return new ArrayList<>();
        }
        final List<ReportResponse> list = INDUSTRY_INSTANCE.reportList(l);
        for (ReportResponse t : list) {
            t.setCurrentDeviceUseMinute((t.getDeviceRunningEndHour() - t.getDeviceRunningStartHour()) * 60 + (t.getDeviceRunningEndMinute() - t.getDeviceRunningStartMinute()));
        }
        // 用户
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getUserId,
                l1 -> userMapper.selectList(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .in(MpUserEntity::getId, l1)
                ),
                (r, t) -> r.getUserId().equals(t.getId()),
                (r, t) -> r.setUserId(t.getId())
                        .setUsername(t.getUsername())
                        .setUserFormat(t.getName())
        );
        // 工人当日工作情况（工时、...）
        MultitaskUtil.supplementList(
                list,
                t -> CollUtil.toList(t.getReportDate(), t.getUserId()),
                l1 -> reportUserMapper.selectList(
                        DatabaseUtil.singleOr(
                                new LambdaQueryWrapper<>(),
                                l1,
                                (lam, p) -> lam
                                        .eq(ReportUserEntity::getReportDate, p.get(0))
                                        .eq(ReportUserEntity::getUserId, p.get(1))
                        )
                ),
                (t, r) -> t.getReportDate().equals(DateUtil.day(r.getReportDate())) && t.getUserId().equals(r.getUserId()),
                (t, r) -> t.setWorkMinute(r.getWorkMinute())
        );
        // 产品
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getProductId,
                l1 -> productMapper.selectList(
                        new LambdaQueryWrapper<ProductEntity>()
                                .in(ProductEntity::getId, l1)
                ),
                (r, t) -> r.getProductId().equals(t.getId()),
                INDUSTRY_INSTANCE::formatReport
        );
        // 订单
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getOrderId,
                l1 -> orderMapper.selectList(
                        new LambdaQueryWrapper<OrderEntity>()
                                .in(OrderEntity::getId, l1)
                ),
                (r, t) -> r.getOrderId().equals(t.getId()),
                INDUSTRY_INSTANCE::formatReport
        );
        // 设备
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getTestDevice,
                l1 -> deviceMapper.selectList(
                        new LambdaQueryWrapper<DeviceEntity>()
                                .in(DeviceEntity::getId, l1)
                ),
                (r, t) -> defaultIfBlank(r.getTestDevice()).equals(t.getId()),
                INDUSTRY_INSTANCE::formatReport
        );
        // 照片
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getReportId,
                l1 -> reportPhotoMapper.selectList(
                        new LambdaQueryWrapper<ReportPhotoEntity>()
                                .in(ReportPhotoEntity::getReportId, l1)
                ),
                (r, t) -> r.getReportId().equals(t.getReportId()),
                (r, t) -> INDUSTRY_INSTANCE.reportPhoto(r, t, urlHelper.getUrlPrefix())
        );
        // 序列号
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getReportId,
                l1 -> reportSerialNoMapper.selectList(
                        new LambdaQueryWrapper<ReportSerialNoEntity>()
                                .in(ReportSerialNoEntity::getReportId, l1)
                ),
                (t, r) -> t.getReportId().equals(r.getReportId()),
                (t, r) -> t.getSerialNoList().add(r.getSerialNo())
        );
        // 加工工序
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getProcessProcedure,
                l1 -> paramDao.listByCategoryId("processProcedure"),
                (t, r) -> defaultIfBlank(t.getProcessProcedure()).equals(r.getValue()),
                (t, r) -> t.setProcessProcedureFormat(r.getLabel())
        );
        // 加工类型
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getProcessType,
                l1 -> paramDao.listByCategoryId("processType"),
                (t, r) -> defaultIfBlank(t.getProcessType()).equals(r.getValue()),
                (t, r) -> t.setProcessTypeFormat(r.getLabel())
        );
        // 班次
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getSchedule,
                l1 -> paramDao.listByCategoryId("schedule"),
                (t, r) -> defaultIfBlank(t.getSchedule()).equals(r.getValue()),
                (t, r) -> t.setScheduleFormat(r.getLabel())
        );
        // 停机内容
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getStopWorkingContent1,
                l1 -> paramDao.listByCategoryId("stopWorkingContent1"),
                (t, r) -> defaultIfBlank(t.getStopWorkingContent1()).equals(r.getValue()),
                (t, r) -> t.setStopWorkingContent1Format(r.getLabel())
        );
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getStopWorkingContent2,
                l1 -> paramDao.listByCategoryId("stopWorkingContent2"),
                (t, r) -> defaultIfBlank(t.getStopWorkingContent2()).equals(r.getValue()),
                (t, r) -> t.setStopWorkingContent2Format(r.getLabel())
        );
        MultitaskUtil.supplementList(
                list,
                ReportResponse::getStopWorkingContent3,
                l1 -> paramDao.listByCategoryId("stopWorkingContent3"),
                (t, r) -> defaultIfBlank(t.getStopWorkingContent3()).equals(r.getValue()),
                (t, r) -> t.setStopWorkingContent3Format(r.getLabel())
        );
        final Map<List<String>, List<UserDeviceResponse>> dayUserDeviceMap = list.stream()
                .filter(t -> isNotBlank(t.getReportDate()))
                .filter(t -> isNotBlank(t.getUserId()))
                .filter(t -> isNotBlank(t.getTestDevice()))
                .collect(
                        Collectors.groupingBy(
                                t -> CollUtil.toList(t.getReportDate(), t.getUserId(), t.getTestDevice()),
                                Collectors.mapping(
                                        t -> INDUSTRY_INSTANCE.userDevice(new UserDeviceResponse(), t),
                                        Collectors.toList()
                                )
                        )
                );
        final Map<List<String>, Date> dayUserTimeMap = list.stream()
                .filter(t -> isNotBlank(t.getReportDate()))
                .filter(t -> isNotBlank(t.getUserId()))
                .filter(t -> null != t.getReportTime())
                .collect(
                        Collectors.groupingBy(
                                t -> CollUtil.toList(t.getReportDate(), t.getUserId()),
                                Collectors.mapping(
                                        ReportResponse::getReportTime,
                                        Collectors.reducing(
                                                new Date(),
                                                (t, t1) -> t.compareTo(t1) > 0 ? t1 : t
                                        )
                                )
                        )
                );
        final BigDecimal thousand = new BigDecimal("1000");
        return list.stream().peek(t -> {
            final List<UserDeviceResponse> userDeviceList = dayUserDeviceMap.getOrDefault(CollUtil.toList(t.getReportDate(), t.getUserId(), t.getTestDevice()), new ArrayList<>());
            t.setUserDeviceList(userDeviceList.stream().sorted((o2, o1) -> {
                int r = o1.getDeviceRunningStartHour().compareTo(o2.getDeviceRunningStartHour());
                if (r != 0) {
                    return r;
                } else {
                    return o1.getDeviceRunningStartMinute().compareTo(o2.getDeviceRunningStartMinute());
                }
            }).collect(Collectors.toList()));
            t.setDeviceUseMinute(userDeviceList.stream().mapToInt(UserDeviceResponse::getDeviceRunningMinute).sum());
            t.setDeviceUsePercent(new BigDecimal(t.getDeviceUseMinute()).divide(new BigDecimal(t.getWorkMinute()), 4, RoundingMode.HALF_UP));
            t.setDeviceUsePercentFormat(t.getDeviceUsePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%");
            t.setDeviceCompletePercent(t.getCompleteMinute().add(new BigDecimal(t.getLeaderSubsidyMinute())).divide(java.math.BigDecimal.valueOf(t.getWorkMinute()), 4, java.math.RoundingMode.HALF_UP));
            t.setDeviceCompletePercentFormat(t.getDeviceCompletePercent().multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP) + "%");
            t.setSalary((t.getCompleteMinute().add(new BigDecimal(t.getLeaderSubsidyMinute()))).multiply(t.getDeviceUnitPrice()).divide(new BigDecimal("60").multiply(thousand), 0, RoundingMode.HALF_UP).multiply(thousand));
            t.setSalaryFormat(NumberUtil.formatIntTh(t.getSalary()));
        }).sorted((o2, o1) -> {
            int r = o1.getReportDate().compareTo(o2.getReportDate());
            if (r != 0) {
                return r;
            } else {
                Date reportTime1 = dayUserTimeMap.getOrDefault(CollUtil.toList(o1.getReportDate(), o1.getUserId()), new Date());
                Date reportTime2 = dayUserTimeMap.getOrDefault(CollUtil.toList(o2.getReportDate(), o2.getUserId()), new Date());
                int r1 = reportTime1.compareTo(reportTime2);
                if (r1 != 0) {
                    return r1;
                } else {
                    int r2 = o1.getReportTime().compareTo(o2.getReportTime());
                    if (r2 != 0) {
                        return r2;
                    } else {
                        return o1.getTestDevice().compareTo(o2.getTestDevice());
                    }
                }
            }
        }).collect(Collectors.toList());
    }

    /**
     * 日报列表
     *
     * @param deviceId 设备id
     * @param request  {@link ReportQueryRequest}
     * @return {@link DataListResult}
     */
    @GetMapping("admin/report/list")
    public DataListResult<ReportSummaryResponse, ReportResponse> reportAdminList(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ReportQueryRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> AdminUser.ADMIN.getCode().equals(t.getRoleCode()))) {
            request.setUserId(u.getUserId());
        }
        if (isNotBlank(request.getOrderNo()) || isNotBlank(request.getProcessProcedure()) || isNotBlank(request.getProjectSequence()) || isNotBlank(request.getTestDevice())) {
            LambdaQueryWrapper<OrderEntity> lambda = new LambdaQueryWrapper<>();
            if (isNotBlank(request.getOrderNo())) {
                lambda.like(OrderEntity::getOrderNo, request.getOrderNo());
            }
            if (isNotBlank(request.getProjectSequence())) {
                lambda.like(OrderEntity::getProjectSequence, request.getProjectSequence());
            }
            if (isNotBlank(request.getTestDevice())) {
                lambda.eq(OrderEntity::getTestDevice, request.getTestDevice());
            }
            if (isNotBlank(request.getProcessProcedure())) {
                lambda.eq(OrderEntity::getProcessProcedure, request.getProcessProcedure());
            }
            List<String> orderIdList = orderMapper.selectList(lambda).stream().map(OrderEntity::getId).collect(Collectors.toList());
            if (CollUtil.isEmpty(orderIdList)) {
                return new DataListResult<>(new ReportSummaryResponse(), new ArrayList<>());
            }
            request.setOrderIdList(orderIdList);
        }
        if (isNotBlank(request.getDesignNumber())) {
            LambdaQueryWrapper<ProductEntity> lambda = new LambdaQueryWrapper<>();
            if (isNotBlank(request.getDesignNumber())) {
                lambda.like(ProductEntity::getDesignNumber, request.getDesignNumber());
            }
            List<String> productIdList = productMapper.selectList(lambda).stream().map(ProductEntity::getId).collect(Collectors.toList());
            if (CollUtil.isEmpty(productIdList)) {
                return new DataListResult<>(new ReportSummaryResponse(), new ArrayList<>());
            }
            request.setProductIdList(productIdList);
        }
        List<ReportResponse> rl = formatReportList(reportList(request));
        BigDecimal totalSalary = rl.stream().map(ReportResponse::getSalary).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DataListResult<>(
                new ReportSummaryResponse()
                        .setTotalSalary(totalSalary)
                        .setTotalSalaryFormat(NumberUtil.formatIntTh(totalSalary))
                ,
                rl
        );
    }

    /**
     * 查询日报
     *
     * @param deviceId 设备id
     * @param request  {@link ReportQueryRequest}
     * @return {@link DataResult<ReportResponse>}
     */
    @GetMapping("admin/report")
    public DataResult<ReportResponse> reportQuery(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ReportQueryRequest request
    ) {
        final String userId = accountHelper.getUser(deviceId).getUserId();
        request.setUserId(userId);
        return new DataResult<>(Optional.ofNullable(
                CollUtil.getFirst(
                        new ArrayList<ReportResponse>()
                        // formatReportList(reportList(request))
                )
        ).orElseGet(() -> CollUtil.getFirst(
                formatReportList(
                        CollUtil.toList(new ReportEntity().setUserId(userId).setProductId(request.getProductId()).setReportDate(DateUtil.day(new Date())))
                )
        )));
    }

    private void mergeRelevance(AccidentRequest request, AccidentEntity e) {
        accidentAttachmentMapper.delete(new LambdaUpdateWrapper<AccidentAttachmentEntity>()
                .eq(AccidentAttachmentEntity::getAccidentId, e.getId())
        );
        accidentAttachmentDao.saveBatch(
                Stream.of(
                                CollUtil.defaultIfEmpty(request.getDevicePhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.accidentAttachment(e.getId(), "devicePhoto", t)),
                                CollUtil.defaultIfEmpty(request.getDesignNumberPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.accidentAttachment(e.getId(), "designNumberPhoto", t)),
                                CollUtil.defaultIfEmpty(request.getPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.accidentAttachment(e.getId(), "photo", t)),
                                CollUtil.defaultIfEmpty(request.getVideoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.accidentAttachment(e.getId(), "video", t)),
                                CollUtil.defaultIfEmpty(request.getDamagePhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.accidentAttachment(e.getId(), "damagePhoto", t)),
                                CollUtil.defaultIfEmpty(request.getPropertyLossPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.accidentAttachment(e.getId(), "propertyLossPhoto", t)),
                                CollUtil.defaultIfEmpty(request.getImproveEvidencePhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.accidentAttachment(e.getId(), "improveEvidencePhoto", t))
                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 保存事故
     *
     * @param deviceId 设备id
     * @param request  {@link AccidentRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/accident")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result accidentSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody AccidentRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final AccidentEntity e = INDUSTRY_INSTANCE.accident(request);
        e.setCreator(u.getUserId());
        e.setModifier(u.getUserId());
        accidentMapper.insert(e);
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 修改事故
     *
     * @param deviceId 设备id
     * @param request  {@link AccidentRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/accident")
    public Result accidentUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody AccidentRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final AccidentEntity e = INDUSTRY_INSTANCE.accident(request);
        e.setModifier(u.getUserId());
        accidentMapper.updateById(e);
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 删除事故
     *
     * @param deviceId 设备id
     * @param request  {@link AccidentRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/accident")
    public Result accidentDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute AccidentRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        LambdaUpdateWrapper<AccidentEntity> lambda = new LambdaUpdateWrapper<AccidentEntity>().eq(AccidentEntity::getId, request.getAccidentId());
        if (!"admin".equals(u.getUserId())) {
            lambda.eq(AccidentEntity::getCreator, u.getUserId());
        }
        if (isNotBlank(request.getAccidentId())) {
            if (accidentMapper.delete(lambda) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
        }
        return new Result();
    }

    private List<AccidentEntity> accidentList(AccidentQueryRequest request) {
        LambdaQueryWrapper<AccidentEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getAccidentId())) {
            lambda.eq(AccidentEntity::getId, request.getAccidentId());
        }
        if (null != request.getStartReportDate()) {
            lambda.ge(AccidentEntity::getReportDate, DateUtil.day(request.getStartReportDate()));
        }
        if (null != request.getEndReportDate()) {
            lambda.le(AccidentEntity::getReportDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getEndReportDate())));
        }
        if (null != request.getReportDate()) {
            lambda.eq(AccidentEntity::getReportDate, request.getReportDate());
        }
        if (isNotBlank(request.getUserId())) {
            lambda.eq(AccidentEntity::getUserId, request.getUserId());
        }
        if (isNotBlank(request.getQueryUserId())) {
            lambda.or(true, l -> l.eq(AccidentEntity::getDutyPerson, request.getQueryUserId()));
            lambda.or(true, l -> l.eq(AccidentEntity::getGroupLeader, request.getQueryUserId()));
        }
        if (CollUtil.isNotEmpty(request.getUserIdList())) {
            DatabaseUtil.or(lambda, request.getUserIdList(), (lam, l) -> lam.in(AccidentEntity::getUserId, l));
        }
        if (isNotBlank(request.getAccidentType())) {
            lambda.eq(AccidentEntity::getAccidentType, request.getAccidentType());
        }
        if (isNotBlank(request.getDutyPerson())) {
            lambda.or(true, l -> l.eq(AccidentEntity::getDutyPerson, request.getDutyPerson()));
            lambda.or(true, l -> l.eq(AccidentEntity::getDutyPerson1, request.getDutyPerson()));
            lambda.or(true, l -> l.eq(AccidentEntity::getDutyPerson2, request.getDutyPerson()));
            lambda.or(true, l -> l.eq(AccidentEntity::getDutyPerson3, request.getDutyPerson()));
        }
        if (null != request.getValid()) {
            lambda.eq(AccidentEntity::getValid, request.getValid());
        }
        return accidentMapper.selectList(lambda.orderByDesc(AccidentEntity::getReportDate));
    }

    private List<AccidentResponse> formatAccidentList(List<AccidentEntity> l) {
        final List<AccidentResponse> list = INDUSTRY_INSTANCE.accidentList(l);
        List<String> userIdList = Stream.of(
                        list.stream().map(AccidentResponse::getUserId).filter(StrUtil::isNotBlank),
                        list.stream().map(AccidentResponse::getDutyPerson).filter(StrUtil::isNotBlank),
                        list.stream().map(AccidentResponse::getGroupLeader).filter(StrUtil::isNotBlank),
                        list.stream().map(AccidentResponse::getChargePerson).filter(StrUtil::isNotBlank),
                        list.stream().map(AccidentResponse::getManager).filter(StrUtil::isNotBlank)
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
                list.stream().filter(t -> isNotBlank(t.getUserId())).collect(Collectors.toList()),
                AccidentResponse::getUserId,
                l1 -> userList,
                (r, t) -> r.getUserId().equals(t.getId()),
                (r, t) -> r.setUsername(t.getUsername())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getGroupLeader())).collect(Collectors.toList()),
                AccidentResponse::getGroupLeader,
                l1 -> userList,
                (r, t) -> r.getGroupLeader().equals(t.getId()),
                (r, t) -> r.setGroupLeaderFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getChargePerson())).collect(Collectors.toList()),
                AccidentResponse::getChargePerson,
                l1 -> userList,
                (r, t) -> r.getChargePerson().equals(t.getId()),
                (r, t) -> r.setChargePersonFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getManager())).collect(Collectors.toList()),
                AccidentResponse::getManager,
                l1 -> userList,
                (r, t) -> r.getManager().equals(t.getId()),
                (r, t) -> r.setManagerFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getUserId())).collect(Collectors.toList()),
                AccidentResponse::getUserId,
                l1 -> userMapper.selectList(
                        new LambdaQueryWrapper<MpUserEntity>()
                                .in(MpUserEntity::getId, l1)
                ),
                (r, t) -> r.getUserId().equals(t.getId()),
                (r, t) -> r.setUserId(t.getId())
                        .setUsername(t.getUsername())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getAccidentType())).collect(Collectors.toList()),
                AccidentResponse::getAccidentType,
                l1 -> paramDao.listByCategoryId("accidentType"),
                (t, r) -> t.getAccidentType().equals(r.getValue()),
                (t, r) -> t.setAccidentTypeFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                list,
                AccidentResponse::getAccidentId,
                l1 -> accidentAttachmentMapper.selectList(new LambdaQueryWrapper<AccidentAttachmentEntity>()
                        .in(AccidentAttachmentEntity::getAccidentId, l1)),
                (t, r) -> t.getAccidentId().equals(r.getAccidentId()),
                (t, r) -> {
                    switch (r.getAttachmentCategory()) {
                        case "devicePhoto" -> t.getDevicePhotoList().add(
                                INDUSTRY_INSTANCE.accidentPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "designNumberPhoto" -> t.getDesignNumberPhotoList().add(
                                INDUSTRY_INSTANCE.accidentPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "photo" -> t.getPhotoList().add(
                                INDUSTRY_INSTANCE.accidentPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "video" -> t.getVideoList().add(
                                INDUSTRY_INSTANCE.accidentVideo(r, urlHelper.getUrlPrefix())
                        );
                        case "damagePhoto" -> t.getDamagePhotoList().add(
                                INDUSTRY_INSTANCE.accidentPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "propertyLossPhoto" -> t.getPropertyLossPhotoList().add(
                                INDUSTRY_INSTANCE.accidentPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "improveEvidencePhoto" -> t.getImproveEvidencePhotoList().add(
                                INDUSTRY_INSTANCE.accidentPhoto(r, urlHelper.getUrlPrefix())
                        );
                        default -> log.info("accident attachment: {}", JSONUtil.toJsonStr(r));
                    }
                }
        );
        return list;
    }

    /**
     * 事故列表
     *
     * @param deviceId 设备id
     * @param request  {@link MpAccountQueryPageRequest}
     * @return {@link PageResult<AccidentResponse>}
     */
    @GetMapping("admin/accident/page")
    public PageResult<AccidentResponse> accidentAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute AccidentPageRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (isNotBlank(request.getData().getUsername())) {
            final List<String> userIdList = userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().like(MpUserEntity::getUsername, request.getData().getUsername()))
                    .stream().map(AbstractPrimaryKey::getId)
                    .distinct().toList();
            if (CollUtil.isNotEmpty(userIdList)) {
                request.getData().setUserIdList(userIdList);
            } else {
                return new PageResult<>();
            }
        }
        if (u.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "accidentView".equals(t))) {
            request.getData().setQueryUserId(u.getUserId());
        }
        PageResult<AccidentEntity> page = DatabaseUtil.page(request, this::accidentList);
        return new PageResult<>(
                page.getTotal(),
                formatAccidentList(page.getList())
        );
    }

    /**
     * 事故列表（公开）
     *
     * @param request {@link AccidentQueryRequest}
     * @return {@link DataResult<AccidentResponse>}
     */
    @GetMapping("accident")
    public DataResult<AccidentResponse> accident(
            @ModelAttribute AccidentQueryRequest request
    ) {
        return new DataResult<>(
                defaultIfNull(CollUtil.getFirst(formatAccidentList(accidentList(request))), new AccidentResponse())
        );
    }

    private void mergeRelevance(EventRequest request, EventEntity e) {
        eventAttachmentMapper.delete(new LambdaUpdateWrapper<EventAttachmentEntity>()
                .eq(EventAttachmentEntity::getEventId, e.getId())
        );
        eventAttachmentDao.saveBatch(
                Stream.of(
                                CollUtil.defaultIfEmpty(request.getPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.eventAttachment(e.getId(), "0", t)),
                                CollUtil.defaultIfEmpty(request.getFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.eventAttachment(e.getId(), "1", t)),
                                CollUtil.defaultIfEmpty(request.getImprovePhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.eventAttachment(e.getId(), "2", t)),
                                CollUtil.defaultIfEmpty(request.getImproveFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.eventAttachment(e.getId(), "3", t))
                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 保存事故
     *
     * @param deviceId 设备id
     * @param request  {@link EventRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/event")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result eventSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody EventRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        EventEntity e;
        eventMapper.insert(e = (EventEntity) INDUSTRY_INSTANCE.event(request)
                .setReason("," + String.join(",", request.getReasonList()) + ",")
                .setCreator(u.getUserId())
                .setModifier(u.getUserId()));
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 修改事故
     *
     * @param deviceId 设备id
     * @param request  {@link EventRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/event")
    public Result eventUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody EventRequest request
    ) {
        MpUserResponse u;
        try {
            u = accountHelper.getUser(deviceId);
        } catch (Exception e) {
            u = new MpUserResponse();
        }
        EventEntity e = (EventEntity) INDUSTRY_INSTANCE.event(request)
                .setReason("," + String.join(",", request.getReasonList()) + ",")
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<EventEntity> lambda = new LambdaUpdateWrapper<EventEntity>()
                .eq(EventEntity::getId, e.getId());
        if (!"admin".equals(u.getUserId())) {
            lambda.eq(EventEntity::getValid, false);
        }
        if (eventMapper.update(
                e,
                lambda
        ) <= 0) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 删除事故
     *
     * @param deviceId 设备id
     * @param request  {@link EventRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/event")
    public Result eventDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute EventRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        LambdaUpdateWrapper<EventEntity> lambda = new LambdaUpdateWrapper<EventEntity>().eq(EventEntity::getId, request.getEventId());
        if (isNotBlank(request.getEventId())) {
            if (eventMapper.delete(lambda) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
        }
        return new Result();
    }

    private List<EventEntity> eventList(EventQueryRequest request) {
        LambdaQueryWrapper<EventEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getEventId())) {
            lambda.eq(EventEntity::getId, request.getEventId());
        }
        if (null != request.getStartReportDate()) {
            lambda.ge(EventEntity::getReportDate, DateUtil.day(request.getStartReportDate()));
        }
        if (null != request.getEndReportDate()) {
            lambda.le(EventEntity::getReportDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getEndReportDate())));
        }
        if (null != request.getReportDate()) {
            lambda.eq(EventEntity::getReportDate, request.getReportDate());
        }
        if (isNotBlank(request.getReason())) {
            lambda.like(EventEntity::getReason, "," + request.getReason() + ",");
        }
        if (CollUtil.isNotEmpty(request.getReasonList())) {
            DatabaseUtil.or(lambda, request.getReasonList(), (lam, l) -> lam.in(EventEntity::getReason, l));
        }
        if (isNotBlank(request.getUserId())) {
            lambda.eq(EventEntity::getUserId, request.getUserId());
        }
        if (isNotBlank(request.getQueryUserId())) {
            lambda.and(true, lam -> {
                lam.eq(EventEntity::getUserId, request.getQueryUserId())
                        .or(true, lam1 -> lam1.eq(EventEntity::getDirectLeader, request.getQueryUserId()));
            });
        }
        if (CollUtil.isNotEmpty(request.getUserIdList())) {
            DatabaseUtil.or(lambda, request.getUserIdList(), (lam, l) -> lam.in(EventEntity::getUserId, l));
        }
        if (null != request.getValid()) {
            lambda.eq(EventEntity::getValid, request.getValid());
        }
        return eventMapper.selectList(lambda.orderByDesc(EventEntity::getReportDate));
    }

    private List<EventResponse> formatEventList(List<EventEntity> l) {
        final List<EventResponse> list = INDUSTRY_INSTANCE.eventList(l);
        List<String> userIdList = Stream.of(
                        list.stream().map(EventResponse::getUserId).filter(StrUtil::isNotBlank),
                        list.stream().map(EventResponse::getDirectLeader).filter(StrUtil::isNotBlank)
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
                list.stream().filter(t -> isNotBlank(t.getUserId())).collect(Collectors.toList()),
                EventResponse::getUserId,
                l1 -> userList,
                (r, t) -> r.getUserId().equals(t.getId()),
                (r, t) -> r.setUserIdFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getDirectLeader())).collect(Collectors.toList()),
                EventResponse::getDirectLeader,
                l1 -> userList,
                (r, t) -> r.getDirectLeader().equals(t.getId()),
                (r, t) -> r.setDirectLeaderFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list,
                EventResponse::getEventId,
                l1 -> eventAttachmentMapper.selectList(new LambdaQueryWrapper<EventAttachmentEntity>()
                        .in(EventAttachmentEntity::getEventId, l1)),
                (t, r) -> t.getEventId().equals(r.getEventId()),
                (t, r) -> {
                    switch (r.getAttachmentCategory()) {
                        case "0" -> t.getPhotoList().add(
                                INDUSTRY_INSTANCE.eventPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "1" -> t.getFileList().add(
                                INDUSTRY_INSTANCE.eventFile(r, urlHelper.getUrlPrefix())
                        );
                        case "2" -> t.getImprovePhotoList().add(
                                INDUSTRY_INSTANCE.eventPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "3" -> t.getImproveFileList().add(
                                INDUSTRY_INSTANCE.eventFile(r, urlHelper.getUrlPrefix())
                        );
                        default -> log.info("event attachment: {}", JSONUtil.toJsonStr(r));
                    }
                }
        );
        Map<Object, String> eventReasonMap = paramDao.listByCategoryId("eventReason").stream().collect(Collectors.toMap(ParamConfigResponse::getValue, OptionItem::getLabel, (t, t1) -> t1));
        for (EventResponse t : list) {
            t.setReasonList(Arrays.stream(t.getReason().split(",", -1)).filter(StrUtil::isNotBlank).collect(Collectors.toList()));
            t.setReasonFormat(t.getReasonList().stream().filter(StrUtil::isNotBlank).map(t1 -> eventReasonMap.getOrDefault(t1, t1)).collect(Collectors.joining(",")));
        }
        return list;
    }

    /**
     * 事故列表
     *
     * @param deviceId 设备id
     * @param request  {@link MpAccountQueryPageRequest}
     * @return {@link PageResult <EventResponse>}
     */
    @GetMapping("admin/event/page")
    public PageResult<EventResponse> eventAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute EventPageRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (isNotBlank(request.getData().getUsername())) {
            final List<String> userIdList = userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().like(MpUserEntity::getUsername, request.getData().getUsername()))
                    .stream().map(AbstractPrimaryKey::getId)
                    .distinct().toList();
            if (CollUtil.isNotEmpty(userIdList)) {
                request.getData().setUserIdList(userIdList);
            } else {
                return new PageResult<>();
            }
        }
        if (u.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "ehsView".equals(t))) {
            request.getData().setQueryUserId(u.getUserId());
        }
        PageResult<EventEntity> page = DatabaseUtil.page(request, this::eventList);
        AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(
                page.getTotal(),
                formatEventList(page.getList())
                        .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 事故（公开）
     *
     * @param request {@link AccidentQueryRequest}
     * @return {@link DataResult<EventResponse>}
     */
    @GetMapping("event")
    public DataResult<EventResponse> event(
            @ModelAttribute EventQueryRequest request
    ) {
        return new DataResult<>(
                defaultIfNull(CollUtil.getFirst(formatEventList(eventList(request))), new EventResponse())
        );
    }

    private void mergeRelevance(QualityRequest request, QualityEntity e) {
        qualityAttachmentMapper.delete(new LambdaUpdateWrapper<QualityAttachmentEntity>()
                .eq(QualityAttachmentEntity::getQualityId, e.getId())
        );
        qualityAttachmentDao.saveBatch(
                Stream.of(
                                CollUtil.defaultIfEmpty(request.getPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.qualityAttachment(e.getId(), "0", t)),
                                CollUtil.defaultIfEmpty(request.getFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.qualityAttachment(e.getId(), "1", t)),
                                CollUtil.defaultIfEmpty(request.getImprovePhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.qualityAttachment(e.getId(), "2", t)),
                                CollUtil.defaultIfEmpty(request.getImproveFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.qualityAttachment(e.getId(), "3", t))
                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 保存事故
     *
     * @param deviceId 设备id
     * @param request  {@link QualityRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/quality")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result qualitySave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody QualityRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        QualityEntity e;
        qualityMapper.insert(e = (QualityEntity) INDUSTRY_INSTANCE.quality(request)
                .setReason("," + String.join(",", request.getReasonList()) + ",")
                .setCreator(u.getUserId())
                .setModifier(u.getUserId()));
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 修改事故
     *
     * @param deviceId 设备id
     * @param request  {@link QualityRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/quality")
    public Result qualityUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody QualityRequest request
    ) {
        MpUserResponse u;
        try {
            u = accountHelper.getUser(deviceId);
        } catch (Exception e) {
            u = new MpUserResponse();
        }
        QualityEntity e = (QualityEntity) INDUSTRY_INSTANCE.quality(request)
                .setReason("," + String.join(",", request.getReasonList()) + ",")
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<QualityEntity> lambda = new LambdaUpdateWrapper<QualityEntity>()
                .eq(QualityEntity::getId, e.getId());
        if (!"admin".equals(u.getUserId())) {
            lambda.eq(QualityEntity::getValid, false);
        }
        if (qualityMapper.update(
                e,
                lambda
        ) <= 0) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 删除事故
     *
     * @param deviceId 设备id
     * @param request  {@link QualityRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/quality")
    public Result qualityDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute QualityRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        LambdaUpdateWrapper<QualityEntity> lambda = new LambdaUpdateWrapper<QualityEntity>().eq(QualityEntity::getId, request.getQualityId());
        if (isNotBlank(request.getQualityId())) {
            if (qualityMapper.delete(lambda) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
        }
        return new Result();
    }

    private List<QualityEntity> qualityList(QualityRequest request) {
        LambdaQueryWrapper<QualityEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getQualityId())) {
            lambda.eq(QualityEntity::getId, request.getQualityId());
        }
        if (null != request.getStartReportDate()) {
            lambda.ge(QualityEntity::getReportDate, DateUtil.day(request.getStartReportDate()));
        }
        if (null != request.getEndReportDate()) {
            lambda.le(QualityEntity::getReportDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getEndReportDate())));
        }
        if (null != request.getReportDate()) {
            lambda.eq(QualityEntity::getReportDate, request.getReportDate());
        }
        if (isNotBlank(request.getReason())) {
            lambda.like(QualityEntity::getReason, "," + request.getReason() + ",");
        }
        if (CollUtil.isNotEmpty(request.getReasonList())) {
            DatabaseUtil.or(lambda, request.getReasonList(), (lam, l) -> lam.in(QualityEntity::getReason, l));
        }
        if (isNotBlank(request.getUserId())) {
            lambda.eq(QualityEntity::getUserId, request.getUserId());
        }
        if (isNotBlank(request.getQueryUserId())) {
            lambda.and(true, lam -> {
                lam.eq(QualityEntity::getUserId, request.getQueryUserId())
                        .or(true, lam1 -> lam1.eq(QualityEntity::getDirectLeader, request.getQueryUserId()));
            });
        }
        if (CollUtil.isNotEmpty(request.getUserIdList())) {
            DatabaseUtil.or(lambda, request.getUserIdList(), (lam, l) -> lam.in(QualityEntity::getUserId, l));
        }
        if (null != request.getValid()) {
            lambda.eq(QualityEntity::getValid, request.getValid());
        }
        return qualityMapper.selectList(lambda.orderByDesc(QualityEntity::getReportDate));
    }

    private List<QualityResponse> formatQualityList(List<QualityEntity> l) {
        final List<QualityResponse> list = INDUSTRY_INSTANCE.qualityList(l);
        List<String> userIdList = Stream.of(
                        list.stream().map(QualityResponse::getUserId).filter(StrUtil::isNotBlank),
                        list.stream().map(QualityResponse::getDirectLeader).filter(StrUtil::isNotBlank)
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
                list.stream().filter(t -> isNotBlank(t.getUserId())).collect(Collectors.toList()),
                QualityResponse::getUserId,
                l1 -> userList,
                (r, t) -> r.getUserId().equals(t.getId()),
                (r, t) -> r.setUserIdFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getDirectLeader())).collect(Collectors.toList()),
                QualityResponse::getDirectLeader,
                l1 -> userList,
                (r, t) -> r.getDirectLeader().equals(t.getId()),
                (r, t) -> r.setDirectLeaderFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list,
                QualityResponse::getQualityId,
                l1 -> qualityAttachmentMapper.selectList(new LambdaQueryWrapper<QualityAttachmentEntity>()
                        .in(QualityAttachmentEntity::getQualityId, l1)),
                (t, r) -> t.getQualityId().equals(r.getQualityId()),
                (t, r) -> {
                    switch (r.getAttachmentCategory()) {
                        case "0" -> t.getPhotoList().add(
                                INDUSTRY_INSTANCE.qualityPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "1" -> t.getFileList().add(
                                INDUSTRY_INSTANCE.qualityFile(r, urlHelper.getUrlPrefix())
                        );
                        case "2" -> t.getImprovePhotoList().add(
                                INDUSTRY_INSTANCE.qualityPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "3" -> t.getImproveFileList().add(
                                INDUSTRY_INSTANCE.qualityFile(r, urlHelper.getUrlPrefix())
                        );
                        default -> log.info("quality attachment: {}", JSONUtil.toJsonStr(r));
                    }
                }
        );
        Map<Object, String> qualityReasonMap = paramDao.listByCategoryId("qualityReason").stream().collect(Collectors.toMap(ParamConfigResponse::getValue, OptionItem::getLabel, (t, t1) -> t1));
        for (QualityResponse t : list) {
            t.setReasonList(Arrays.stream(t.getReason().split(",", -1)).filter(StrUtil::isNotBlank).collect(Collectors.toList()));
            t.setReasonFormat(t.getReasonList().stream().filter(StrUtil::isNotBlank).map(t1 -> qualityReasonMap.getOrDefault(t1, t1)).collect(Collectors.joining(",")));
        }
        return list;
    }

    /**
     * 事故列表
     *
     * @param deviceId 设备id
     * @param request  {@link MpAccountQueryPageRequest}
     * @return {@link PageResult <QualityResponse>}
     */
    @GetMapping("admin/quality/page")
    public PageResult<QualityResponse> qualityAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute QualityPageRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "qualityView".equals(t))) {
            request.getData().setQueryUserId(u.getUserId());
        }
        PageResult<QualityEntity> page = DatabaseUtil.page(request, this::qualityList);
        AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(
                page.getTotal(),
                formatQualityList(page.getList())
                        .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 事故（公开）
     *
     * @param request {@link AccidentQueryRequest}
     * @return {@link DataResult <QualityResponse>}
     */
    @GetMapping("quality")
    public DataResult<QualityResponse> quality(
            @ModelAttribute QualityRequest request
    ) {
        return new DataResult<>(
                defaultIfNull(CollUtil.getFirst(formatQualityList(qualityList(request))), new QualityResponse())
        );
    }

    private void mergeRelevance(CrashRequest request, CrashEntity e) {
        crashAttachmentMapper.delete(new LambdaUpdateWrapper<CrashAttachmentEntity>()
                .eq(CrashAttachmentEntity::getCrashId, e.getId())
        );
        crashAttachmentDao.saveBatch(
                Stream.of(
                                CollUtil.defaultIfEmpty(request.getPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.crashAttachment(e.getId(), "0", t)),
                                CollUtil.defaultIfEmpty(request.getFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.crashAttachment(e.getId(), "1", t)),
                                CollUtil.defaultIfEmpty(request.getImprovePhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.crashAttachment(e.getId(), "2", t)),
                                CollUtil.defaultIfEmpty(request.getImproveFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.crashAttachment(e.getId(), "3", t))
                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 保存事故
     *
     * @param deviceId 设备id
     * @param request  {@link CrashRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/crash")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result crashSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody CrashRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        CrashEntity e;
        crashMapper.insert(e = (CrashEntity) INDUSTRY_INSTANCE.crash(request)
                .setReason("," + String.join(",", request.getReasonList()) + ",")
                .setCreator(u.getUserId())
                .setModifier(u.getUserId()));
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 修改事故
     *
     * @param deviceId 设备id
     * @param request  {@link CrashRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/crash")
    public Result crashUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody CrashRequest request
    ) {
        MpUserResponse u;
        try {
            u = accountHelper.getUser(deviceId);
        } catch (Exception e) {
            u = new MpUserResponse();
        }
        CrashEntity e = (CrashEntity) INDUSTRY_INSTANCE.crash(request)
                .setReason("," + String.join(",", request.getReasonList()) + ",")
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<CrashEntity> lambda = new LambdaUpdateWrapper<CrashEntity>()
                .eq(CrashEntity::getId, e.getId());
        if (!"admin".equals(u.getUserId())) {
            lambda.eq(CrashEntity::getValid, false);
        }
        if (crashMapper.update(
                e,
                lambda
        ) <= 0) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 删除事故
     *
     * @param deviceId 设备id
     * @param request  {@link CrashRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/crash")
    public Result crashDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute CrashRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        LambdaUpdateWrapper<CrashEntity> lambda = new LambdaUpdateWrapper<CrashEntity>().eq(CrashEntity::getId, request.getCrashId());
        if (isNotBlank(request.getCrashId())) {
            if (crashMapper.delete(lambda) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
        }
        return new Result();
    }

    private List<CrashEntity> crashList(CrashRequest request) {
        LambdaQueryWrapper<CrashEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getCrashId())) {
            lambda.eq(CrashEntity::getId, request.getCrashId());
        }
        if (null != request.getStartReportDate()) {
            lambda.ge(CrashEntity::getReportDate, DateUtil.day(request.getStartReportDate()));
        }
        if (null != request.getEndReportDate()) {
            lambda.le(CrashEntity::getReportDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getEndReportDate())));
        }
        if (null != request.getReportDate()) {
            lambda.eq(CrashEntity::getReportDate, request.getReportDate());
        }
        if (isNotBlank(request.getEquipmentId())) {
            lambda.eq(CrashEntity::getEquipmentId, request.getEquipmentId());
        }
        if (isNotBlank(request.getReason())) {
            lambda.like(CrashEntity::getReason, "," + request.getReason() + ",");
        }
        if (isNotBlank(request.getAccidentDescribe())) {
            lambda.like(CrashEntity::getAccidentDescribe, request.getAccidentDescribe());
        }
        if (CollUtil.isNotEmpty(request.getReasonList())) {
            DatabaseUtil.or(lambda, request.getReasonList(), (lam, l) -> lam.in(CrashEntity::getReason, l));
        }
        if (isNotBlank(request.getUserId())) {
            lambda.eq(CrashEntity::getUserId, request.getUserId());
        }
        if (isNotBlank(request.getQueryUserId())) {
            lambda.and(true, lam -> {
                lam.eq(CrashEntity::getUserId, request.getQueryUserId())
                        .or(true, lam1 -> lam1.eq(CrashEntity::getDirectLeader, request.getQueryUserId()));
            });
        }
        if (CollUtil.isNotEmpty(request.getUserIdList())) {
            DatabaseUtil.or(lambda, request.getUserIdList(), (lam, l) -> lam.in(CrashEntity::getUserId, l));
        }
        if (null != request.getValid()) {
            lambda.eq(CrashEntity::getValid, request.getValid());
        }
        return crashMapper.selectList(lambda.orderByDesc(CrashEntity::getReportDate));
    }

    private List<CrashResponse> formatCrashList(List<CrashEntity> l) {
        final List<CrashResponse> list = INDUSTRY_INSTANCE.crashList(l);
        List<String> userIdList = Stream.of(
                        list.stream().map(CrashResponse::getUserId).filter(StrUtil::isNotBlank),
                        list.stream().map(CrashResponse::getDirectLeader).filter(StrUtil::isNotBlank)
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
                list.stream().filter(t -> isNotBlank(t.getUserId())).collect(Collectors.toList()),
                CrashResponse::getUserId,
                l1 -> userList,
                (r, t) -> r.getUserId().equals(t.getId()),
                (r, t) -> r.setUserIdFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getDirectLeader())).collect(Collectors.toList()),
                CrashResponse::getDirectLeader,
                l1 -> userList,
                (r, t) -> r.getDirectLeader().equals(t.getId()),
                (r, t) -> r.setDirectLeaderFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list,
                CrashResponse::getCrashId,
                l1 -> crashAttachmentMapper.selectList(new LambdaQueryWrapper<CrashAttachmentEntity>()
                        .in(CrashAttachmentEntity::getCrashId, l1)),
                (t, r) -> t.getCrashId().equals(r.getCrashId()),
                (t, r) -> {
                    switch (r.getAttachmentCategory()) {
                        case "0" -> t.getPhotoList().add(
                                INDUSTRY_INSTANCE.crashPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "1" -> t.getFileList().add(
                                INDUSTRY_INSTANCE.crashFile(r, urlHelper.getUrlPrefix())
                        );
                        case "2" -> t.getImprovePhotoList().add(
                                INDUSTRY_INSTANCE.crashPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "3" -> t.getImproveFileList().add(
                                INDUSTRY_INSTANCE.crashFile(r, urlHelper.getUrlPrefix())
                        );
                        default -> log.info("crash attachment: {}", JSONUtil.toJsonStr(r));
                    }
                }
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getEquipmentId())).collect(Collectors.toList()),
                CrashResponse::getEquipmentId,
                l1 -> equipmentMapper.selectList(new LambdaQueryWrapper<EquipmentEntity>()
                        .in(EquipmentEntity::getId, l1)),
                (t, r) -> t.getEquipmentId().equals(r.getId()),
                (t, r) -> {
                    t.setEquipmentNo(r.getEquipmentNo())
                    ;
                }
        );
        Map<Object, String> crashReasonMap = paramDao.listByCategoryId("crashReason").stream().collect(Collectors.toMap(ParamConfigResponse::getValue, OptionItem::getLabel, (t, t1) -> t1));
        for (CrashResponse t : list) {
            t.setReasonList(Arrays.stream(t.getReason().split(",", -1)).filter(StrUtil::isNotBlank).collect(Collectors.toList()));
            t.setReasonFormat(t.getReasonList().stream().filter(StrUtil::isNotBlank).map(t1 -> crashReasonMap.getOrDefault(t1, t1)).collect(Collectors.joining(",")));
        }
        return list;
    }

    /**
     * 事故列表
     *
     * @param deviceId 设备id
     * @param request  {@link MpAccountQueryPageRequest}
     * @return {@link PageResult <CrashResponse>}
     */
    @GetMapping("admin/crash/page")
    public PageResult<CrashResponse> crashAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute CrashPageRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "equipmentManager".equals(t))) {
            request.getData().setQueryUserId(u.getUserId());
        }
        PageResult<CrashEntity> page = DatabaseUtil.page(request, this::crashList);
        AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(
                page.getTotal(),
                formatCrashList(page.getList())
                        .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 事故（公开）
     *
     * @param request {@link AccidentQueryRequest}
     * @return {@link DataResult <CrashResponse>}
     */
    @GetMapping("crash")
    public DataResult<CrashResponse> crash(
            @ModelAttribute CrashRequest request
    ) {
        return new DataResult<>(
                defaultIfNull(CollUtil.getFirst(formatCrashList(crashList(request))), new CrashResponse())
        );
    }


    private void mergeRelevance(TroubleRequest request, TroubleEntity e) {
        troubleAttachmentMapper.delete(new LambdaUpdateWrapper<TroubleAttachmentEntity>()
                .eq(TroubleAttachmentEntity::getTroubleId, e.getId())
        );
        troubleAttachmentDao.saveBatch(
                Stream.of(
                                CollUtil.defaultIfEmpty(request.getPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.troubleAttachment(e.getId(), "0", t)),
                                CollUtil.defaultIfEmpty(request.getFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.troubleAttachment(e.getId(), "1", t)),
                                CollUtil.defaultIfEmpty(request.getImprovePhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.troubleAttachment(e.getId(), "2", t)),
                                CollUtil.defaultIfEmpty(request.getImproveFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.troubleAttachment(e.getId(), "3", t))
                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 保存事故
     *
     * @param deviceId 设备id
     * @param request  {@link TroubleRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/trouble")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result troubleSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody TroubleRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        TroubleEntity e;
        troubleMapper.insert(e = (TroubleEntity) INDUSTRY_INSTANCE.trouble(request)
                .setReason("," + String.join(",", request.getReasonList()) + ",")
                .setCreator(u.getUserId())
                .setModifier(u.getUserId()));
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 修改事故
     *
     * @param deviceId 设备id
     * @param request  {@link TroubleRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/trouble")
    public Result troubleUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody TroubleRequest request
    ) {
        MpUserResponse u;
        try {
            u = accountHelper.getUser(deviceId);
        } catch (Exception e) {
            u = new MpUserResponse();
        }
        TroubleEntity e = (TroubleEntity) INDUSTRY_INSTANCE.trouble(request)
                .setReason("," + String.join(",", request.getReasonList()) + ",")
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<TroubleEntity> lambda = new LambdaUpdateWrapper<TroubleEntity>()
                .eq(TroubleEntity::getId, e.getId());
        if (!"admin".equals(u.getUserId())) {
            lambda.eq(TroubleEntity::getValid, false);
        }
        if (troubleMapper.update(
                e,
                lambda
        ) <= 0) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 删除事故
     *
     * @param deviceId 设备id
     * @param request  {@link TroubleRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/trouble")
    public Result troubleDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute TroubleRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        LambdaUpdateWrapper<TroubleEntity> lambda = new LambdaUpdateWrapper<TroubleEntity>().eq(TroubleEntity::getId, request.getTroubleId());
        if (isNotBlank(request.getTroubleId())) {
            if (troubleMapper.delete(lambda) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
        }
        return new Result();
    }

    private List<TroubleEntity> troubleList(TroubleRequest request) {
        LambdaQueryWrapper<TroubleEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getTroubleId())) {
            lambda.eq(TroubleEntity::getId, request.getTroubleId());
        }
        if (null != request.getStartReportDate()) {
            lambda.ge(TroubleEntity::getReportDate, DateUtil.day(request.getStartReportDate()));
        }
        if (null != request.getEndReportDate()) {
            lambda.le(TroubleEntity::getReportDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getEndReportDate())));
        }
        if (null != request.getReportDate()) {
            lambda.eq(TroubleEntity::getReportDate, request.getReportDate());
        }
        if (isNotBlank(request.getReason())) {
            lambda.like(TroubleEntity::getReason, "," + request.getReason() + ",");
        }
        if (isNotBlank(request.getAccidentDescribe())) {
            lambda.like(TroubleEntity::getAccidentDescribe, request.getAccidentDescribe());
        }
        if (CollUtil.isNotEmpty(request.getReasonList())) {
            DatabaseUtil.or(lambda, request.getReasonList(), (lam, l) -> lam.in(TroubleEntity::getReason, l));
        }
        if (isNotBlank(request.getUserId())) {
            lambda.eq(TroubleEntity::getUserId, request.getUserId());
        }
        if (isNotBlank(request.getQueryUserId())) {
            lambda.and(true, lam -> {
                lam.eq(TroubleEntity::getUserId, request.getQueryUserId())
                        .or(true, lam1 -> lam1.eq(TroubleEntity::getDirectLeader, request.getQueryUserId()));
            });
        }
        if (CollUtil.isNotEmpty(request.getUserIdList())) {
            DatabaseUtil.or(lambda, request.getUserIdList(), (lam, l) -> lam.in(TroubleEntity::getUserId, l));
        }
        if (null != request.getValid()) {
            lambda.eq(TroubleEntity::getValid, request.getValid());
        }
        return troubleMapper.selectList(lambda.orderByDesc(TroubleEntity::getReportDate));
    }

    private List<TroubleResponse> formatTroubleList(List<TroubleEntity> l) {
        final List<TroubleResponse> list = INDUSTRY_INSTANCE.troubleList(l);
        List<String> userIdList = Stream.of(
                        list.stream().map(TroubleResponse::getUserId).filter(StrUtil::isNotBlank),
                        list.stream().map(TroubleResponse::getDirectLeader).filter(StrUtil::isNotBlank)
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
                list.stream().filter(t -> isNotBlank(t.getUserId())).collect(Collectors.toList()),
                TroubleResponse::getUserId,
                l1 -> userList,
                (r, t) -> r.getUserId().equals(t.getId()),
                (r, t) -> r.setUserIdFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getDirectLeader())).collect(Collectors.toList()),
                TroubleResponse::getDirectLeader,
                l1 -> userList,
                (r, t) -> r.getDirectLeader().equals(t.getId()),
                (r, t) -> r.setDirectLeaderFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list,
                TroubleResponse::getTroubleId,
                l1 -> troubleAttachmentMapper.selectList(new LambdaQueryWrapper<TroubleAttachmentEntity>()
                        .in(TroubleAttachmentEntity::getTroubleId, l1)),
                (t, r) -> t.getTroubleId().equals(r.getTroubleId()),
                (t, r) -> {
                    switch (r.getAttachmentCategory()) {
                        case "0" -> t.getPhotoList().add(
                                INDUSTRY_INSTANCE.troublePhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "1" -> t.getFileList().add(
                                INDUSTRY_INSTANCE.troubleFile(r, urlHelper.getUrlPrefix())
                        );
                        case "2" -> t.getImprovePhotoList().add(
                                INDUSTRY_INSTANCE.troublePhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "3" -> t.getImproveFileList().add(
                                INDUSTRY_INSTANCE.troubleFile(r, urlHelper.getUrlPrefix())
                        );
                        default -> log.info("trouble attachment: {}", JSONUtil.toJsonStr(r));
                    }
                }
        );
        Map<Object, String> troubleReasonMap = paramDao.listByCategoryId("troubleReason").stream().collect(Collectors.toMap(ParamConfigResponse::getValue, OptionItem::getLabel, (t, t1) -> t1));
        for (TroubleResponse t : list) {
            t.setReasonList(Arrays.stream(t.getReason().split(",", -1)).filter(StrUtil::isNotBlank).collect(Collectors.toList()));
            t.setReasonFormat(t.getReasonList().stream().filter(StrUtil::isNotBlank).map(t1 -> troubleReasonMap.getOrDefault(t1, t1)).collect(Collectors.joining(",")));
        }
        return list;
    }

    /**
     * 事故列表
     *
     * @param deviceId 设备id
     * @param request  {@link MpAccountQueryPageRequest}
     * @return {@link PageResult <TroubleResponse>}
     */
    @GetMapping("admin/trouble/page")
    public PageResult<TroubleResponse> troubleAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute TroublePageRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "gauger".equals(t))) {
            request.getData().setQueryUserId(u.getUserId());
        }
        PageResult<TroubleEntity> page = DatabaseUtil.page(request, this::troubleList);
        AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(
                page.getTotal(),
                formatTroubleList(page.getList())
                        .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 事故（公开）
     *
     * @param request {@link AccidentQueryRequest}
     * @return {@link DataResult <TroubleResponse>}
     */
    @GetMapping("trouble")
    public DataResult<TroubleResponse> trouble(
            @ModelAttribute TroubleRequest request
    ) {
        return new DataResult<>(
                defaultIfNull(CollUtil.getFirst(formatTroubleList(troubleList(request))), new TroubleResponse())
        );
    }


    private void mergeRelevance(ImproveRequest request, ImproveEntity e) {
        improveAttachmentMapper.delete(new LambdaUpdateWrapper<ImproveAttachmentEntity>()
                .eq(ImproveAttachmentEntity::getImproveId, e.getId())
        );
        improveAttachmentDao.saveBatch(
                Stream.of(
                                CollUtil.defaultIfEmpty(request.getPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.improveAttachment(e.getId(), "0", t)),
                                CollUtil.defaultIfEmpty(request.getFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.improveAttachment(e.getId(), "1", t)),
                                CollUtil.defaultIfEmpty(request.getImprovePhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.improveAttachment(e.getId(), "2", t)),
                                CollUtil.defaultIfEmpty(request.getImproveFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.improveAttachment(e.getId(), "3", t))
                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 保存事故
     *
     * @param deviceId 设备id
     * @param request  {@link ImproveRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/improve")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result improveSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ImproveRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        ImproveEntity e;
        improveMapper.insert(e = (ImproveEntity) INDUSTRY_INSTANCE.improve(request)
                .setReason("," + String.join(",", request.getReasonList()) + ",")
                .setCreator(u.getUserId())
                .setModifier(u.getUserId()));
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 修改事故
     *
     * @param deviceId 设备id
     * @param request  {@link ImproveRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/improve")
    public Result improveUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ImproveRequest request
    ) {
        MpUserResponse u;
        try {
            u = accountHelper.getUser(deviceId);
        } catch (Exception e) {
            u = new MpUserResponse();
        }
        ImproveEntity e = (ImproveEntity) INDUSTRY_INSTANCE.improve(request)
                .setReason("," + String.join(",", request.getReasonList()) + ",")
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<ImproveEntity> lambda = new LambdaUpdateWrapper<ImproveEntity>()
                .eq(ImproveEntity::getId, e.getId());
        if (!"admin".equals(u.getUserId())) {
            lambda.eq(ImproveEntity::getValid, false);
        }
        if (improveMapper.update(
                e,
                lambda
        ) <= 0) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 删除事故
     *
     * @param deviceId 设备id
     * @param request  {@link ImproveRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/improve")
    public Result improveDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ImproveRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        LambdaUpdateWrapper<ImproveEntity> lambda = new LambdaUpdateWrapper<ImproveEntity>().eq(ImproveEntity::getId, request.getImproveId());
        if (isNotBlank(request.getImproveId())) {
            if (improveMapper.delete(lambda) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
        }
        return new Result();
    }

    private List<ImproveEntity> improveList(ImproveRequest request) {
        LambdaQueryWrapper<ImproveEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getImproveId())) {
            lambda.eq(ImproveEntity::getId, request.getImproveId());
        }
        if (null != request.getStartReportDate()) {
            lambda.ge(ImproveEntity::getReportDate, DateUtil.day(request.getStartReportDate()));
        }
        if (null != request.getEndReportDate()) {
            lambda.le(ImproveEntity::getReportDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getEndReportDate())));
        }
        if (null != request.getReportDate()) {
            lambda.eq(ImproveEntity::getReportDate, request.getReportDate());
        }
        if (isNotBlank(request.getReason())) {
            lambda.like(ImproveEntity::getReason, "," + request.getReason() + ",");
        }
        if (CollUtil.isNotEmpty(request.getReasonList())) {
            DatabaseUtil.or(lambda, request.getReasonList(), (lam, l) -> lam.in(ImproveEntity::getReason, l));
        }
        if (isNotBlank(request.getUserId())) {
            lambda.eq(ImproveEntity::getUserId, request.getUserId());
        }
        if (isNotBlank(request.getQueryUserId())) {
            lambda.and(true, lam -> {
                lam.eq(ImproveEntity::getUserId, request.getQueryUserId())
                        .or(true, lam1 -> lam1.eq(ImproveEntity::getDirectLeader, request.getQueryUserId()));
            });
        }
        if (null != request.getValid()) {
            lambda.eq(ImproveEntity::getValid, request.getValid());
        }
        return improveMapper.selectList(lambda.orderByDesc(ImproveEntity::getReportDate));
    }

    private List<ImproveResponse> formatImproveList(List<ImproveEntity> l) {
        final List<ImproveResponse> list = INDUSTRY_INSTANCE.improveList(l);
        List<String> userIdList = Stream.of(
                        list.stream().map(ImproveResponse::getUserId).filter(StrUtil::isNotBlank),
                        list.stream().map(ImproveResponse::getDirectLeader).filter(StrUtil::isNotBlank)
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
                list.stream().filter(t -> isNotBlank(t.getUserId())).collect(Collectors.toList()),
                ImproveResponse::getUserId,
                l1 -> userList,
                (r, t) -> r.getUserId().equals(t.getId()),
                (r, t) -> r.setUserIdFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getDirectLeader())).collect(Collectors.toList()),
                ImproveResponse::getDirectLeader,
                l1 -> userList,
                (r, t) -> r.getDirectLeader().equals(t.getId()),
                (r, t) -> r.setDirectLeaderFormat(t.getName())
        );
        MultitaskUtil.supplementList(
                list,
                ImproveResponse::getImproveId,
                l1 -> improveAttachmentMapper.selectList(new LambdaQueryWrapper<ImproveAttachmentEntity>()
                        .in(ImproveAttachmentEntity::getImproveId, l1)),
                (t, r) -> t.getImproveId().equals(r.getImproveId()),
                (t, r) -> {
                    switch (r.getAttachmentCategory()) {
                        case "0" -> t.getPhotoList().add(
                                INDUSTRY_INSTANCE.improvePhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "1" -> t.getFileList().add(
                                INDUSTRY_INSTANCE.improveFile(r, urlHelper.getUrlPrefix())
                        );
                        case "2" -> t.getImprovePhotoList().add(
                                INDUSTRY_INSTANCE.improvePhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "3" -> t.getImproveFileList().add(
                                INDUSTRY_INSTANCE.improveFile(r, urlHelper.getUrlPrefix())
                        );
                        default -> log.info("improve attachment: {}", JSONUtil.toJsonStr(r));
                    }
                }
        );
        Map<Object, String> improveReasonMap = paramDao.listByCategoryId("improveReason").stream().collect(Collectors.toMap(ParamConfigResponse::getValue, OptionItem::getLabel, (t, t1) -> t1));
        for (ImproveResponse t : list) {
            t.setReasonList(Arrays.stream(t.getReason().split(",", -1)).filter(StrUtil::isNotBlank).collect(Collectors.toList()));
            t.setReasonFormat(t.getReasonList().stream().filter(StrUtil::isNotBlank).map(t1 -> improveReasonMap.getOrDefault(t1, t1)).collect(Collectors.joining(",")));
        }
        return list;
    }

    /**
     * 事故列表
     *
     * @param deviceId 设备id
     * @param request  {@link MpAccountQueryPageRequest}
     * @return {@link PageResult <ImproveResponse>}
     */
    @GetMapping("admin/improve/page")
    public PageResult<ImproveResponse> improveAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ImprovePageRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "engineer".equals(t))) {
            request.getData().setQueryUserId(u.getUserId());
        }
        PageResult<ImproveEntity> page = DatabaseUtil.page(request, this::improveList);
        AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(
                page.getTotal(),
                formatImproveList(page.getList())
                        .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 事故（公开）
     *
     * @param request {@link ImproveRequest}
     * @return {@link DataResult <ImproveResponse>}
     */
    @GetMapping("improve")
    public DataResult<ImproveResponse> improve(
            @ModelAttribute ImproveRequest request
    ) {
        return new DataResult<>(
                defaultIfNull(CollUtil.getFirst(formatImproveList(improveList(request))), new ImproveResponse())
        );
    }

    private void mergeRelevance(EquipmentRequest request, EquipmentEntity e) {
        equipmentAttachmentMapper.delete(new LambdaUpdateWrapper<EquipmentAttachmentEntity>()
                .eq(EquipmentAttachmentEntity::getEquipmentId, e.getId())
        );
        equipmentAttachmentDao.saveBatch(
                Stream.of(
                                CollUtil.defaultIfEmpty(request.getPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.equipmentAttachment(e.getId(), "0", t)),
                                CollUtil.defaultIfEmpty(request.getFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.equipmentAttachment(e.getId(), "1", t))
                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 保存事故
     *
     * @param deviceId 设备id
     * @param request  {@link EquipmentRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/equipment")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result equipmentSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody EquipmentRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch("equipmentManager"::equals)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        EquipmentEntity e;
        equipmentMapper.insert(e = (EquipmentEntity) INDUSTRY_INSTANCE.equipment(request)
                .setUseUser("," + String.join(",", request.getUseUserList()) + ",")
                .setChargeUser("," + String.join(",", request.getChargeUserList()) + ",")
                .setCreator(u.getUserId())
                .setModifier(u.getUserId()));
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 修改事故
     *
     * @param deviceId 设备id
     * @param request  {@link EquipmentRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/equipment")
    public Result equipmentUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody EquipmentRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch("equipmentManager"::equals)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        EquipmentEntity e = (EquipmentEntity) INDUSTRY_INSTANCE.equipment(request)
                .setUseUser("," + String.join(",", request.getUseUserList()) + ",")
                .setChargeUser("," + String.join(",", request.getChargeUserList()) + ",")
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<EquipmentEntity> lambda = new LambdaUpdateWrapper<EquipmentEntity>()
                .eq(EquipmentEntity::getId, e.getId());
        if (equipmentMapper.update(
                e,
                lambda
        ) <= 0) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 删除事故
     *
     * @param deviceId 设备id
     * @param request  {@link EquipmentRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/equipment")
    public Result equipmentDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute EquipmentRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        LambdaUpdateWrapper<EquipmentEntity> lambda = new LambdaUpdateWrapper<EquipmentEntity>().eq(EquipmentEntity::getId, request.getEquipmentId());
        if (isNotBlank(request.getEquipmentId())) {
            if (equipmentMapper.delete(lambda) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
        }
        return new Result();
    }

    private List<EquipmentEntity> equipmentList(EquipmentRequest request) {
        LambdaQueryWrapper<EquipmentEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getEquipmentId())) {
            lambda.eq(EquipmentEntity::getId, request.getEquipmentId());
        }
        if (isNotBlank(request.getEquipmentNo())) {
            lambda.eq(EquipmentEntity::getEquipmentNo, request.getEquipmentNo());
        }
        if (isNotBlank(request.getEquipmentName())) {
            lambda.like(EquipmentEntity::getEquipmentName, request.getEquipmentName());
        }
        if (isNotBlank(request.getSpecification())) {
            lambda.like(EquipmentEntity::getSpecification, request.getSpecification());
        }
        if (isNotBlank(request.getPosition())) {
            lambda.eq(EquipmentEntity::getPosition, request.getPosition());
        }
        if (null != request.getStartDate()) {
            lambda.ge(EquipmentEntity::getDate, DateUtil.day(request.getStartDate()));
        }
        if (null != request.getEndDate()) {
            lambda.le(EquipmentEntity::getDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getEndDate())));
        }
        if (isNotBlank(request.getUseUser())) {
            lambda.like(EquipmentEntity::getUseUser, "," + request.getUseUser() + ",");
        }
        if (isNotBlank(request.getQueryUserId())) {
            lambda.and(true, lam -> {
                lam.eq(EquipmentEntity::getUseUser, request.getQueryUserId())
                        .or(true, lam1 -> lam1.eq(EquipmentEntity::getChargeUser, request.getQueryUserId()));
            });
        }
        return equipmentMapper.selectList(lambda.orderByAsc(EquipmentEntity::getEquipmentNo));
    }

    private List<EquipmentResponse> formatEquipmentList(List<EquipmentEntity> l) {
        final List<EquipmentResponse> list = INDUSTRY_INSTANCE.equipmentList(l);
        List<String> userIdList = Stream.of(
                        list.stream().map(EquipmentResponse::getUseUserList).flatMap(t -> t.stream()).filter(StrUtil::isNotBlank),
                        list.stream().map(EquipmentResponse::getChargeUserList).flatMap(t -> t.stream())
                )
                .flatMap(t -> t)
                .distinct()
                .collect(Collectors.toList());
        final List<MpUserEntity> userList = CollUtil.isEmpty(userIdList) ? new ArrayList<>() : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        );
        final Map<String, String> userMap = userList.stream().collect(Collectors.toMap(MpUserEntity::getId, MpUserEntity::getName, (t, t1) -> t1, HashMap::new));
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getPosition())).collect(Collectors.toList()),
                EquipmentResponse::getPosition,
                l1 -> paramDao.listByCategoryId("equipmentPosition"),
                (r, t) -> r.getPosition().equals(t.getValue()),
                (r, t) -> r.setPositionFormat(t.getLabel())
        );
        MultitaskUtil.supplementList(
                list,
                EquipmentResponse::getEquipmentId,
                l1 -> equipmentAttachmentMapper.selectList(new LambdaQueryWrapper<EquipmentAttachmentEntity>()
                        .in(EquipmentAttachmentEntity::getEquipmentId, l1)),
                (t, r) -> t.getEquipmentId().equals(r.getEquipmentId()),
                (t, r) -> {
                    switch (r.getAttachmentCategory()) {
                        case "0" -> t.getPhotoList().add(
                                INDUSTRY_INSTANCE.equipmentPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "1" -> t.getFileList().add(
                                INDUSTRY_INSTANCE.equipmentFile(r, urlHelper.getUrlPrefix())
                        );
                        default -> log.info("equipment attachment: {}", JSONUtil.toJsonStr(r));
                    }
                }
        );
        final Map<String, String> um = userList.stream().collect(Collectors.toMap(MpUserEntity::getId, MpUserEntity::getName, (t, t1) -> t1));
        for (EquipmentResponse t : list) {
            t.setChargeUserFormat(t.getChargeUserList().stream().filter(StrUtil::isNotBlank).map(t1 -> um.getOrDefault(t1, t1)).collect(Collectors.joining(",")));
            t.setUseUserFormat(t.getUseUserList().stream().filter(StrUtil::isNotBlank).map(t1 -> um.getOrDefault(t1, t1)).collect(Collectors.joining(",")));
        }
        return list;
    }

    /**
     * 事故列表
     *
     * @param deviceId 设备id
     * @param request  {@link EquipmentPageRequest}
     * @return {@link PageResult <EquipmentResponse>}
     */
    @GetMapping("admin/equipment/page")
    public PageResult<EquipmentResponse> equipmentAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute EquipmentPageRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "equipmentView".equals(t))) {
            request.getData().setQueryUserId(u.getUserId());
        }
        PageResult<EquipmentEntity> page = DatabaseUtil.page(request, this::equipmentList);
        AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(
                page.getTotal(),
                formatEquipmentList(page.getList())
                        .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 事故（公开）
     *
     * @param request {@link AccidentQueryRequest}
     * @return {@link DataResult <EquipmentResponse>}
     */
    @GetMapping("equipment")
    public DataResult<EquipmentResponse> equipment(
            @ModelAttribute EquipmentRequest request
    ) {
        return new DataResult<>(
                defaultIfNull(CollUtil.getFirst(formatEquipmentList(equipmentList(request))), new EquipmentResponse())
        );
    }

    private void mergeRelevance(MaintainRequest request, MaintainEntity e) {
        maintainAttachmentMapper.delete(new LambdaUpdateWrapper<MaintainAttachmentEntity>()
                .eq(MaintainAttachmentEntity::getMaintainId, e.getId())
        );
        maintainAttachmentDao.saveBatch(
                Stream.of(
                                CollUtil.defaultIfEmpty(request.getPhotoList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.maintainAttachment(e.getId(), "0", t)),
                                CollUtil.defaultIfEmpty(request.getFileList(), new ArrayList<>())
                                        .stream()
                                        .map(t -> INDUSTRY_INSTANCE.maintainAttachment(e.getId(), "1", t))
                        ).flatMap(t -> t)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 保存维修
     *
     * @param deviceId 设备id
     * @param request  {@link MaintainRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/maintain")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result maintainSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody MaintainRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        MaintainEntity e;
        maintainMapper.insert(e = (MaintainEntity) INDUSTRY_INSTANCE.maintain(request)
                .setBrokenReason("," + String.join(",", request.getBrokenReasonList()) + ",")
                .setPartyUser("," + String.join(",", request.getPartyUserList()) + ",")
                .setCreator(u.getUserId())
                .setModifier(u.getUserId()));
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 修改维修
     *
     * @param deviceId 设备id
     * @param request  {@link MaintainRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/maintain")
    public Result maintainUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody MaintainRequest request
    ) {
        MpUserResponse u;
        try {
            u = accountHelper.getUser(deviceId);
        } catch (Exception e) {
            u = new MpUserResponse();
        }
        MaintainEntity e = (MaintainEntity) INDUSTRY_INSTANCE.maintain(request)
                .setBrokenReason("," + String.join(",", request.getBrokenReasonList()) + ",")
                .setPartyUser("," + String.join(",", request.getPartyUserList()) + ",")
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<MaintainEntity> lambda = new LambdaUpdateWrapper<MaintainEntity>()
                .eq(MaintainEntity::getId, e.getId());
        if (u.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "maintainView".equals(t))) {
            lambda.eq(MaintainEntity::getPartyUser, u.getUserId());
        }
        if (!"admin".equals(u.getUsername())) {
            lambda.eq(MaintainEntity::getValid, false);
        }
        if (maintainMapper.update(
                e,
                lambda
        ) <= 0) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new Result();
    }

    /**
     * 删除维修
     *
     * @param deviceId 设备id
     * @param request  {@link MaintainRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/maintain")
    public Result maintainDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute MaintainRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        LambdaUpdateWrapper<MaintainEntity> lambda = new LambdaUpdateWrapper<MaintainEntity>().eq(MaintainEntity::getId, request.getMaintainId());
        if (isNotBlank(request.getMaintainId())) {
            if (maintainMapper.delete(lambda) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
        }
        return new Result();
    }

    private LambdaQueryWrapper<MaintainEntity> maintainLambda(MaintainRequest request) {
        final LambdaQueryWrapper<MaintainEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getMaintainId())) {
            lambda.eq(MaintainEntity::getId, request.getMaintainId());
        }
        if (null != request.getStartDate()) {
            lambda.ge(MaintainEntity::getDate, DateUtil.day(request.getStartDate()));
        }
        if (null != request.getEndDate()) {
            lambda.le(MaintainEntity::getDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getEndDate())));
        }
        if (isNotBlank(request.getQueryUserId())) {
            lambda.like(MaintainEntity::getPartyUser, "," + request.getQueryUserId() + ",");
        }
        if (isNotBlank(request.getPartyUser())) {
            lambda.like(MaintainEntity::getPartyUser, "," + request.getPartyUser() + ",");
        }
        if (isNotBlank(request.getBrokenReason())) {
            lambda.like(MaintainEntity::getBrokenReason, "," + request.getBrokenReason() + ",");
        }
        if (isNotBlank(request.getEquipmentId())) {
            lambda.eq(MaintainEntity::getEquipmentId, request.getEquipmentId());
        }
        if (CollUtil.isNotEmpty(request.getBrokenReasonList())) {
            DatabaseUtil.or(lambda, request.getBrokenReasonList(), (lam, l) -> lam.in(MaintainEntity::getBrokenReason, l));
        }
        return lambda.orderByDesc(MaintainEntity::getCreatedTime);
    }

    private List<MaintainEntity> maintainList(MaintainRequest request) {
        return maintainMapper.selectList(maintainLambda(request)
                .select(
                        MaintainEntity::getId,
                        MaintainEntity::getCreator,
                        MaintainEntity::getLastModifiedTime,
                        MaintainEntity::getDeletedFlag,
                        MaintainEntity::getCreator,
                        MaintainEntity::getModifier,
                        MaintainEntity::getRemark,
                        MaintainEntity::getDate,
                        MaintainEntity::getEquipmentId,
                        MaintainEntity::getBrokenReason,
                        MaintainEntity::getBrokenContent,
                        MaintainEntity::getRepairContent,
                        MaintainEntity::getReplacePair,
                        MaintainEntity::getRepairType,
                        MaintainEntity::getStopHour,
                        MaintainEntity::getPartyUser,
                        MaintainEntity::getValid
                )
        );
    }

    private MaintainSummaryResponse defaultMaintainSummary() {
        return new MaintainSummaryResponse().setStopHour(BigDecimal.ZERO);
    }

    private MaintainSummaryResponse maintainSummary(MaintainRequest request) {
        return maintainMapper.selectList(
                        maintainLambda(request)
                                .isNotNull(MaintainEntity::getStopHour)
                                .select(MaintainEntity::getSumStopHour)
                ).stream().findFirst()
                .map(t -> new MaintainSummaryResponse().setStopHour(t.getSumStopHour()))
                .orElse(defaultMaintainSummary());
    }

    private List<MaintainResponse> formatMaintainList(List<MaintainEntity> l) {
        final List<MaintainResponse> list = INDUSTRY_INSTANCE.maintainList(l);
        List<String> userIdList = Stream.of(
                        list.stream().map(MaintainResponse::getPartyUserList).flatMap(t -> t.stream()).filter(StrUtil::isNotBlank)
                )
                .flatMap(t -> t)
                .distinct()
                .collect(Collectors.toList());
        final List<MpUserEntity> userList = CollUtil.isEmpty(userIdList) ? new ArrayList<>() : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        );
        final Map<String, String> um = userList.stream().collect(Collectors.toMap(MpUserEntity::getId, MpUserEntity::getName, (t, t1) -> t1));
        MultitaskUtil.supplementList(
                list,
                MaintainResponse::getMaintainId,
                l1 -> maintainAttachmentMapper.selectList(new LambdaQueryWrapper<MaintainAttachmentEntity>()
                        .in(MaintainAttachmentEntity::getMaintainId, l1)),
                (t, r) -> t.getMaintainId().equals(r.getMaintainId()),
                (t, r) -> {
                    switch (r.getAttachmentCategory()) {
                        case "0" -> t.getPhotoList().add(
                                INDUSTRY_INSTANCE.maintainPhoto(r, urlHelper.getUrlPrefix())
                        );
                        case "1" -> t.getFileList().add(
                                INDUSTRY_INSTANCE.maintainFile(r, urlHelper.getUrlPrefix())
                        );
                    }
                }
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getEquipmentId())).collect(Collectors.toList()),
                MaintainResponse::getEquipmentId,
                l1 -> equipmentMapper.selectList(new LambdaQueryWrapper<EquipmentEntity>()
                        .in(EquipmentEntity::getId, l1)),
                (t, r) -> t.getEquipmentId().equals(r.getId()),
                (t, r) -> {
                    t.setEquipmentNo(r.getEquipmentNo())
                            .setPosition(r.getPosition())
                            .setEquipmentName(r.getEquipmentName())
                    ;
                }
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getPosition())).collect(Collectors.toList()),
                MaintainResponse::getPosition,
                l1 -> paramDao.listByCategoryId("equipmentPosition"),
                (r, t) -> r.getPosition().equals(t.getValue()),
                (r, t) -> r.setPositionFormat(t.getLabel())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getRepairType())).collect(Collectors.toList()),
                MaintainResponse::getRepairType,
                l1 -> paramDao.listByCategoryId("repairType"),
                (r, t) -> r.getRepairType().equals(t.getValue()),
                (r, t) -> r.setRepairTypeFormat(t.getLabel())
        );
        final Map<Object, String> m = paramDao.listByCategoryId("brokenReason").stream().collect(Collectors.toMap(ParamConfigResponse::getValue, OptionItem::getLabel, (t, t1) -> t1));
        for (MaintainResponse t : list) {
            t.setBrokenReasonFormat(t.getBrokenReasonList().stream().filter(StrUtil::isNotBlank).map(t1 -> m.getOrDefault(t1, t1)).collect(Collectors.joining(",")));
            t.setPartyUserFormat(t.getPartyUserList().stream().filter(StrUtil::isNotBlank).map(t1 -> um.getOrDefault(t1, t1)).collect(Collectors.joining(",")));
        }
        return list;
    }

    /**
     * 维修列表
     *
     * @param deviceId 设备id
     * @param request  {@link MpAccountQueryPageRequest}
     * @return {@link PageResult <MaintainResponse>}
     */
    @GetMapping("admin/maintain/page")
    public PageDataResult<MaintainResponse, MaintainSummaryResponse> maintainAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute MaintainPageRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "maintainView".equals(t))) {
            request.getData().setQueryUserId(u.getUserId());
        }
        final PageResult<MaintainEntity> page = DatabaseUtil.page(request, this::maintainList);
        final AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageDataResult<>(
                page.getTotal(),
                formatMaintainList(page.getList())
                        .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList()),
                page.getTotal() > 0 ? maintainSummary(request.getData()) : defaultMaintainSummary()
        );
    }

    /**
     * 维修（公开）
     *
     * @param request {@link MaintainRequest}
     * @return {@link DataResult <MaintainResponse>}
     */
    @GetMapping("maintain")
    public DataResult<MaintainResponse> maintain(
            @ModelAttribute MaintainRequest request
    ) {
        return new DataResult<>(
                defaultIfNull(CollUtil.getFirst(formatMaintainList(maintainList(request))), new MaintainResponse())
        );
    }

    /**
     * 保存请假
     *
     * @param deviceId 设备id
     * @param request  {@link VocationRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/vocation")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result vocationSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody VocationRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "vocationManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        VocationEntity e;
        vocationMapper.insert(e = (VocationEntity) INDUSTRY_INSTANCE.vocation(request)
                .setCreator(u.getUserId())
                .setModifier(u.getUserId()));
        return new Result();
    }

    /**
     * 修改请假
     *
     * @param deviceId 设备id
     * @param request  {@link VocationRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/vocation")
    public Result vocationUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody VocationRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "vocationManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        VocationEntity e = (VocationEntity) INDUSTRY_INSTANCE.vocation(request)
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<VocationEntity> lambda = new LambdaUpdateWrapper<VocationEntity>()
                .eq(VocationEntity::getId, e.getId());
        if (vocationMapper.update(
                e,
                lambda
        ) <= 0) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        return new Result();
    }

    /**
     * 删除请假
     *
     * @param deviceId 设备id
     * @param request  {@link VocationRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/vocation")
    public Result vocationDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute VocationRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        LambdaUpdateWrapper<VocationEntity> lambda = new LambdaUpdateWrapper<VocationEntity>().eq(VocationEntity::getId, request.getVocationId());
        if (isNotBlank(request.getVocationId())) {
            if (vocationMapper.delete(lambda) <= 0) {
                throw new BusinessException(AUTHORITY_AUTH_FAIL);
            }
        }
        return new Result();
    }

    private LambdaQueryWrapper<VocationEntity> vocationLambda(VocationRequest request) {
        final LambdaQueryWrapper<VocationEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getVocationId())) {
            lambda.eq(VocationEntity::getId, request.getVocationId());
        }
        if (isNotBlank(request.getQueryUserId())) {
            lambda.and(true,
                    lam -> {
                        lam.eq(VocationEntity::getUser, request.getQueryUserId())
                                .or(true, lam1 -> lam1.eq(VocationEntity::getChargeUser, request.getQueryUserId())
                                );
                    }
            );
        }
        if (isNotBlank(request.getVocationType())) {
            lambda.eq(VocationEntity::getVocationType, request.getVocationType());
        }
        if (isNotBlank(request.getUser())) {
            lambda.eq(VocationEntity::getUser, request.getUser());
        }
        if (null != request.getDate()) {
            lambda.ge(VocationEntity::getDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(request.getDate())));
            lambda.le(VocationEntity::getDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getDate())));
        }
        if (null != request.getStartDate() && null != request.getEndDate()) {
            lambda.ge(VocationEntity::getStartDate, DateUtil.day(cn.hutool.core.date.DateUtil.endOfDay(request.getStartDate())));
            lambda.le(VocationEntity::getEndDate, DateUtil.day(cn.hutool.core.date.DateUtil.beginOfDay(request.getEndDate())));
        }
        if (CollUtil.isNotEmpty(request.getUserIdList())) {
            DatabaseUtil.or(lambda, request.getUserIdList(), (lam, l) -> {
                        lam.in(VocationEntity::getUser, request.getUserIdList());
                    }
            );
        }
        return lambda;
    }

    private List<VocationEntity> vocationList(VocationRequest request) {
        return vocationMapper.selectList(vocationLambda(request)
                .orderByDesc(VocationEntity::getCreatedTime)
                .select(
                        VocationEntity::getId,
                        VocationEntity::getCreator,
                        VocationEntity::getLastModifiedTime,
                        VocationEntity::getDeletedFlag,
                        VocationEntity::getCreator,
                        VocationEntity::getModifier,
                        VocationEntity::getRemark,
                        VocationEntity::getVocationType,
                        VocationEntity::getDate,
                        VocationEntity::getUser,
                        VocationEntity::getChargeUser,
                        VocationEntity::getStartDate,
                        VocationEntity::getEndDate,
                        VocationEntity::getReason,
                        VocationEntity::getCount,
                        VocationEntity::getCompliance,
                        VocationEntity::getViolationReason
                )
        );
    }

    private List<VocationSummaryResponse> vocationSummaryList(VocationRequest request) {
        final List<VocationResponse> rl = formatVocationList(vocationMapper.selectList(
                vocationLambda(request)
                        .select(
                                VocationEntity::getUser,
                                VocationEntity::getCompliance,
                                VocationEntity::getCount
                        )
        ));
        Map<String, List<VocationResponse>> vocationPartitionList = rl.stream().collect(
                Collectors.groupingBy(
                        VocationResponse::getDepartment
                )
        );
        return vocationPartitionList.values()
                .stream().map(l -> {
                    VocationResponse t = CollUtil.getFirst(l);
                    final VocationSummaryResponse r = new VocationSummaryResponse()
                            .setDepartment(t.getDepartment())
                            .setDepartmentFormat(t.getDepartmentFormat())
                            .setSumUserCount(new BigDecimal(l.stream().map(VocationResponse::getUser).distinct().count()))
                            .setSumCount(l.stream().map(VocationResponse::getCount).reduce(BigDecimal.ZERO, BigDecimal::add))
                            .setSumViolationCount(new BigDecimal(l.stream().filter(t1 -> !Boolean.TRUE.equals(t1.getCompliance())).count()))
                            .setSumComplianceCount(new BigDecimal(l.stream().filter(t1 -> Boolean.TRUE.equals(t1.getCompliance())).count()))
                            .setSum(new BigDecimal(l.size()))
                            .setComplianceRate(new BigDecimal(l.stream().filter(t1 -> Boolean.TRUE.equals(t1.getCompliance())).count()).divide(
                                    new BigDecimal(l.size()), 4, RoundingMode.HALF_UP
                            ));
                    return r
                            .setSumUserCountFormat(NumberUtil.formatIntTh(r.getSumUserCount()))
                            .setSumCountFormat(NumberUtil.format(r.getSumCount()))
                            .setSumViolationCountFormat(NumberUtil.formatIntTh(r.getSumViolationCount()))
                            .setSumComplianceCountFormat(NumberUtil.formatIntTh(r.getSumComplianceCount()))
                            .setSumFormat(NumberUtil.formatIntTh(r.getSumCount()))
                            .setComplianceRateFormat(NumberUtil.formatPercent(r.getComplianceRate()))
                            ;
                }).collect(Collectors.toList());
    }

    private List<VocationResponse> formatVocationList(List<VocationEntity> list) {
        final List<VocationResponse> rl = INDUSTRY_INSTANCE.vocationList(list);
        List<String> userIdList = Stream.of(
                        rl.stream().map(VocationResponse::getUser).filter(StrUtil::isNotBlank),
                        rl.stream().map(VocationResponse::getChargeUser).filter(StrUtil::isNotBlank)
                )
                .flatMap(t -> t)
                .distinct()
                .collect(Collectors.toList());
        final List<MpUserEntity> userList = CollUtil.isEmpty(userIdList) ? new ArrayList<>() : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName, MpUserEntity::getDepartment, MpUserEntity::getProfession),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        );
        final Map<String, MpUserEntity> um = userList.stream().collect(Collectors.toMap(MpUserEntity::getId, t -> t, (t, t1) -> t1));
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getVocationType())).collect(Collectors.toList()),
                VocationResponse::getVocationType,
                l -> paramDao.listByCategoryId("vocationType"),
                (t, r) -> t.getVocationType().equals(r.getValue()),
                (t, r) -> t.setVocationTypeFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getUser())).collect(Collectors.toList()),
                VocationResponse::getUser,
                l1 -> userList,
                (t, r) -> t.getUser().equals(r.getId()),
                (t, r) -> {
                    t.setUserFormat(r.getName())
                            .setProfession(r.getProfession())
                            .setDepartment(r.getDepartment())
                    ;
                }
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getDepartment())).collect(Collectors.toList()),
                VocationResponse::getDepartment,
                l -> paramDao.listByCategoryId("department"),
                (t, r) -> t.getDepartment().equals(r.getValue()),
                (t, r) -> t.setDepartmentFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getProfession())).collect(Collectors.toList()),
                VocationResponse::getProfession,
                l -> paramDao.listByCategoryId("profession"),
                (t, r) -> t.getProfession().equals(r.getValue()),
                (t, r) -> t.setProfessionFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getChargeUser())).collect(Collectors.toList()),
                VocationResponse::getChargeUser,
                l1 -> userList,
                (t, r) -> t.getChargeUser().equals(r.getId()),
                (t, r) -> {
                    t.setChargeUserFormat(r.getName());
                }
        );
        for (VocationResponse t : rl) {
            t.setComplianceFormat(Boolean.TRUE.equals(t.getCompliance()) ? "YES" : "NO");
        }
        return rl;
    }

    /**
     * 请假列表
     *
     * @param deviceId 设备id
     * @param request  {@link MpAccountQueryPageRequest}
     * @return {@link PageResult <VocationResponse>}
     */
    @GetMapping("admin/vocation/page")
    public PageResult<VocationResponse> vocationAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute VocationPageRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "vocationView".equals(t.getRoleCode()) || "vocationManager".equals(t.getRoleCode())) && !"admin".equals(u.getUsername())) {
            request.getData().setQueryUserId(u.getUserId());
        }
        if (isNotBlank(request.getData().getDepartment())) {
            List<String> uesrIdList = userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().eq(MpUserEntity::getDepartment, request.getData().getDepartment())
                            .select(MpUserEntity::getId))
                    .stream().map(AbstractPrimaryKey::getId)
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(uesrIdList)) {
                return new PageResult<>();
            } else {
                request.getData().setUserIdList(uesrIdList);
            }
        }
        final PageResult<VocationEntity> page = DatabaseUtil.page(request, this::vocationList);
        final AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(
                page.getTotal(),
                formatVocationList(page.getList())
                        .stream()
                        .peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 请假汇总列表
     *
     * @param deviceId 设备id
     * @param request  {@link VocationRequest}
     * @return {@link ListResult<VocationSummaryResponse>}
     */
    @GetMapping("admin/vocation/summary-list")
    public ListResult<VocationSummaryResponse> vocationAdminSummaryList(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute VocationRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        final List<VocationSummaryResponse> rl = vocationSummaryList(request);
        VocationSummaryResponse r = new VocationSummaryResponse()
                .setDepartment(null)
                .setDepartmentFormat("汇总")
                .setSumUserCount(BigDecimal.ZERO)
                .setSumCount(BigDecimal.ZERO)
                .setSumViolationCount(BigDecimal.ZERO)
                .setSumComplianceCount(BigDecimal.ZERO)
                .setSum(BigDecimal.ZERO)
                .setComplianceRate(BigDecimal.ZERO);
        rl.forEach(t -> {
            r.setSumUserCount(r.getSumUserCount().add(t.getSumUserCount()))
                    .setSumCount(r.getSumCount().add(t.getSumCount()))
                    .setSumViolationCount(r.getSumViolationCount().add(t.getSumViolationCount()))
                    .setSumComplianceCount(r.getSumComplianceCount().add(t.getSumComplianceCount()))
                    .setSum(r.getSum().add(t.getSum()))
                    .setComplianceRate(r.getComplianceRate().add(t.getComplianceRate()))
            ;
        });
        r.setComplianceRate(r.getComplianceRate().divide(new BigDecimal(rl.size()), 4, RoundingMode.HALF_UP));
        rl.add(r
                .setSumUserCountFormat(NumberUtil.formatIntTh(r.getSumUserCount()))
                .setSumCountFormat(NumberUtil.format(r.getSumCount()))
                .setSumViolationCountFormat(NumberUtil.formatIntTh(r.getSumViolationCount()))
                .setSumComplianceCountFormat(NumberUtil.formatIntTh(r.getSumComplianceCount()))
                .setSumFormat(NumberUtil.formatIntTh(r.getSumCount()))
                .setComplianceRateFormat(NumberUtil.formatPercent(r.getComplianceRate())));
        return new ListResult<>(
                rl.stream()
                        .peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 请假（公开）
     *
     * @param request {@link VocationRequest}
     * @return {@link DataResult <VocationResponse>}
     */
    @GetMapping("vocation")
    public DataResult<VocationResponse> vocation(
            @ModelAttribute VocationRequest request
    ) {
        return new DataResult<>(
                defaultIfNull(CollUtil.getFirst(formatVocationList(vocationList(request))), new VocationResponse())
        );
    }

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
    @PostMapping("admin/box-flag")
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

        BoxFlagEntity e = (BoxFlagEntity) INDUSTRY_INSTANCE.boxFlag(request)
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
    @PutMapping("admin/box-flag")
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
        BoxFlagEntity e = (BoxFlagEntity) INDUSTRY_INSTANCE.boxFlag(request)
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
    @DeleteMapping("admin/box-flag")
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
            lambda.like(BoxFlagEntity::getCustomerShortName, request.getCustomerShortName());
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
            lambda.ge(BoxFlagEntity::getDate, request.getStartDate());
        }
        if (null != request.getEndDate()) {
            lambda.le(BoxFlagEntity::getDate, cn.hutool.core.date.DateUtil.endOfDay(request.getEndDate()).toJdkDate());
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
        return boxFlagDao.list(lambda.orderByDesc(BoxFlagEntity::getCreateTime));
    }

    private List<BoxFlagResponse> formatBoxFlagList(List<BoxFlagEntity> list) {
        List<BoxFlagResponse> rl = INDUSTRY_INSTANCE.boxFlagList(list);
        MultitaskUtil.supplementList(
                rl,
                BoxFlagResponse::getBoxFlagId,
                l -> boxFlagPhotoDao.list(new LambdaQueryWrapper<BoxFlagPhotoEntity>().in(BoxFlagPhotoEntity::getBoxFlagId, l)),
                (t, r) -> t.getBoxFlagId().equals(r.getBoxFlagId()),
                (t, r) -> t.getPhotoList().add(INDUSTRY_INSTANCE.photo(r, urlHelper.getUrlPrefix()))
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
     * @return {@link PageResult<BoxFlagResponse>}
     */
    @GetMapping("admin/box-flag/page")
    public PageResult<BoxFlagResponse> boxFlagAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute BoxFlagPageRequest request
    ) {
        accountHelper.getUser(deviceId);
        PageResult<BoxFlagEntity> pr = DatabaseUtil.page(request, this::boxFlagList);
        return new PageResult<>(pr.getTotal(), formatBoxFlagList(pr.getList()));
    }

    /**
     * 装箱标识上一条（用户新增默认数据填充）
     *
     * @param deviceId 设备id
     * @return {@link DataResult<BoxFlagEntity>}
     */
    @GetMapping("admin/box-flag/last")
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

    private DisqualificationOrderEntity mergeRelevance(DisqualificationOrderRequest request, DisqualificationOrderEntity e) {
        disqualificationOrderPhotoDao.remove(new LambdaUpdateWrapper<DisqualificationOrderPhotoEntity>().eq(DisqualificationOrderPhotoEntity::getDisqualificationOrderId, e.getId()));
        disqualificationOrderPhotoDao.saveBatch(request.getPhotoList().stream().map(t -> new DisqualificationOrderPhotoEntity().setDisqualificationOrderId(e.getId()).setPhotoUrl(t.getPhotoUrl()).setPhotoCompressUrl(t.getPhotoCompressUrl())).collect(Collectors.toList()));
        return e;
    }

    /**
     * 保存不合格单（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link DisqualificationOrderRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/disqualification-order")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result disqualificationOrderAdminSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody DisqualificationOrderRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        Integer integer = disqualificationOrderDao.nextOrderNo();
        DisqualificationOrderEntity e = (DisqualificationOrderEntity) INDUSTRY_INSTANCE.disqualificationOrder(request)
                .setDutyPerson("," + String.join(",", request.getDutyPersonList()) + ",")
                .setDisqualificationOrderNo(integer)
                .setDisqualificationOrder("NCR-" + DateUtil.daySimple() + StrUtil.padPre(String.valueOf(integer), 3, "0"))
                .setCreator(u.getUserId())
                .setModifier(u.getUserId());
        disqualificationOrderDao.save(e);
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    /**
     * 修改不合格单（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link DisqualificationOrderRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/disqualification-order")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result disqualificationOrderAdminUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody DisqualificationOrderRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        DisqualificationOrderEntity db = disqualificationOrderDao.getById(request.getDisqualificationOrderId());
        if (null == db) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final DisqualificationOrderEntity e = (DisqualificationOrderEntity) INDUSTRY_INSTANCE.disqualificationOrder(request)
                .setDutyPerson("," + String.join(",", request.getDutyPersonList()) + ",")
                .setDisqualificationOrder("NCR-" + DateUtil.daySimple(db.getCreateTime()) + StrUtil.padPre(String.valueOf(request.getDisqualificationOrderNo()), 3, "0"))
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<DisqualificationOrderEntity> lambda = new LambdaUpdateWrapper<DisqualificationOrderEntity>()
                .eq(DisqualificationOrderEntity::getId, request.getDisqualificationOrderId());
        // 只有经理和自己可以删除
        if (u.getRoleCodeList().stream().noneMatch(t -> "manager".equals(t) || "admin".equals(t))) {
            lambda
                    .eq(DisqualificationOrderEntity::getCreator, u.getUserId());
        }
        if (!disqualificationOrderDao.update(
                e,
                lambda
        )) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    /**
     * 删除不合格单（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link DisqualificationOrderRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/disqualification-order")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result disqualificationOrderAdminDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute DisqualificationOrderRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch("qualityManager"::equals)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        if (isNotBlank(request.getDisqualificationOrderId())) {
            disqualificationOrderDao.removeById(request.getDisqualificationOrderId());
            disqualificationOrderPhotoDao.removeById(request.getDisqualificationOrderId());
        }
        return new Result();
    }

    private List<DisqualificationOrderEntity> disqualificationOrderList(DisqualificationOrderRequest d) {
        LambdaQueryWrapper<DisqualificationOrderEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getDisqualificationOrderId())) {
            lambda.eq(DisqualificationOrderEntity::getId, d.getDisqualificationOrderId());
        }
        if (isNotBlank(d.getCreator())) {
            lambda.eq(DisqualificationOrderEntity::getCreator, d.getCreator());
        }
        if (isNotBlank(d.getOrderNo())) {
            lambda.like(DisqualificationOrderEntity::getOrderNo, d.getOrderNo());
        }
        if (isNotBlank(d.getMaterialNo())) {
            lambda.like(DisqualificationOrderEntity::getMaterialNo, d.getMaterialNo());
        }
        if (isNotBlank(d.getDesignNumber())) {
            lambda.like(DisqualificationOrderEntity::getDesignNumber, d.getDesignNumber());
        }
        if (isNotBlank(d.getDisqualificationOrderNo())) {
            lambda.like(DisqualificationOrderEntity::getDisqualificationOrderNo, d.getDisqualificationOrderNo());
        }
        if (isNotBlank(d.getDisqualificationOrder())) {
            lambda.like(DisqualificationOrderEntity::getDisqualificationOrder, d.getDisqualificationOrder());
        }
        if (isNotBlank(d.getDutyPerson())) {
            lambda.and(true, lam -> {
                lam.eq(DisqualificationOrderEntity::getCreator, d.getDutyPerson())
                        .or(true, lam1 -> {
                            lam1.like(DisqualificationOrderEntity::getDutyPerson, "," + d.getDutyPerson() + ",");
                        });
            });
        }
        if (isNotBlank(d.getDefectType())) {
            lambda.eq(DisqualificationOrderEntity::getDefectType, d.getDefectType());
        }
        if (isNotBlank(d.getQualityDealOpinion())) {
            lambda.like(DisqualificationOrderEntity::getQualityDealOpinion, d.getQualityDealOpinion());
        }
        if (null != d.getStartCreateTime()) {
            lambda.ge(DisqualificationOrderEntity::getCreateTime, d.getStartCreateTime());
        }
        if (null != d.getEndCreateTime()) {
            lambda.le(DisqualificationOrderEntity::getCreateTime, cn.hutool.core.date.DateUtil.endOfDay(d.getEndCreateTime()));
        }
        if (isNotBlank(d.getCheckPoint())) {
            lambda.eq(DisqualificationOrderEntity::getCheckPoint, d.getCheckPoint());
        }
        if (isNotBlank(d.getSkillDealOpinion())) {
            lambda.eq(DisqualificationOrderEntity::getSkillDealOpinion, d.getSkillDealOpinion());
        }
        return disqualificationOrderDao.list(lambda.orderByDesc(DisqualificationOrderEntity::getCreateTime));
    }

    private List<DisqualificationOrderResponse> formatDisqualificationOrderList(List<DisqualificationOrderEntity> list) {
        final List<DisqualificationOrderResponse> rl = INDUSTRY_INSTANCE.disqualificationOrderList(list);
        final List<String> userIdList = Stream.of(
                        rl.stream().map(DisqualificationOrderResponse::getCreator).filter(StrUtil::isNotBlank),
                        rl.stream().map(DisqualificationOrderResponse::getDutyPersonList).flatMap(Collection::stream).filter(StrUtil::isNotBlank)
                )
                .flatMap(t -> t)
                .distinct()
                .collect(Collectors.toList());
        final List<MpUserEntity> userList = CollUtil.isEmpty(userIdList) ? new ArrayList<>() : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        );
        final Map<String, String> userMap = userList.stream().collect(Collectors.toMap(MpUserEntity::getId, MpUserEntity::getName, (t, t1) -> t1, HashMap::new));
        MultitaskUtil.supplementList(
                rl,
                DisqualificationOrderResponse::getDisqualificationOrderId,
                l -> disqualificationOrderPhotoDao.list(new LambdaQueryWrapper<DisqualificationOrderPhotoEntity>().in(DisqualificationOrderPhotoEntity::getDisqualificationOrderId, l)),
                (t, r) -> t.getDisqualificationOrderId().equals(r.getDisqualificationOrderId()),
                (t, r) -> t.getPhotoList().add(INDUSTRY_INSTANCE.photo(r, urlHelper.getUrlPrefix()))
        );
        MultitaskUtil.supplementList(
                rl,
                DisqualificationOrderResponse::getCreator,
                l -> userList,
                (t, r) -> t.getCreator().equals(r.getId()),
                (t, r) -> t.setCreatorFormat(r.getName())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getProcess())).collect(Collectors.toList()),
                DisqualificationOrderResponse::getProcess,
                l -> paramDao.listByCategoryId("process"),
                (t, r) -> t.getProcess().equals(r.getValue()),
                (t, r) -> t.setProcessFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getCheckPoint())).collect(Collectors.toList()),
                DisqualificationOrderResponse::getCheckPoint,
                l -> paramDao.listByCategoryId("checkPoint"),
                (t, r) -> t.getCheckPoint().equals(r.getValue()),
                (t, r) -> t.setCheckPointFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getQualityDealOpinion())).collect(Collectors.toList()),
                DisqualificationOrderResponse::getQualityDealOpinion,
                l -> paramDao.listByCategoryId("qualityDealOpinion"),
                (t, r) -> t.getQualityDealOpinion().equals(r.getValue()),
                (t, r) -> t.setQualityDealOpinionFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getSkillDealOpinion())).collect(Collectors.toList()),
                DisqualificationOrderResponse::getSkillDealOpinion,
                l -> paramDao.listByCategoryId("skillDealOpinion"),
                (t, r) -> t.getSkillDealOpinion().equals(r.getValue()),
                (t, r) -> t.setSkillDealOpinionFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getDefectType())).collect(Collectors.toList()),
                DisqualificationOrderResponse::getDefectType,
                l -> paramDao.listByCategoryId("defectType"),
                (t, r) -> t.getDefectType().equals(r.getValue()),
                (t, r) -> t.setDefectTypeFormat(r.getLabel())
        );
        for (DisqualificationOrderResponse t : rl) {
            t.setDutyPersonFormat(t.getDutyPersonList().stream().filter(StrUtil::isNotBlank).map(t1 -> userMap.getOrDefault(t1, t1)).collect(Collectors.joining(",")));
        }
        return rl;
    }

    /**
     * 不合格单分页（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link DisqualificationOrderPageRequest}
     * @return {@link PageResult<DisqualificationOrderResponse>}
     */
    @GetMapping("admin/disqualification-order/page")
    public PageResult<DisqualificationOrderResponse> disqualificationOrderAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute DisqualificationOrderPageRequest request
    ) {
        MpUserResponse user = accountHelper.getUser(deviceId);
        if (user.getRoleCodeList().stream().noneMatch(t -> "admin".equals(t) || "disqualificationView".equals(t))) {
            request.getData().setDutyPerson(user.getUserId());
        }
        PageResult<DisqualificationOrderEntity> pr = DatabaseUtil.page(request, this::disqualificationOrderList);
        return new PageResult<>(pr.getTotal(), formatDisqualificationOrderList(pr.getList()));
    }

    /**
     * 不合格单
     *
     * @param deviceId 设备id
     * @return {@link Result}
     */
    @GetMapping("admin/disqualification-order/form")
    public Result disqualificationOrderAdminForm(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        DisqualificationOrderResponse r = defaultIfNull(
                CollUtil.getFirst(
                        formatDisqualificationOrderList(
                                DatabaseUtil.page(
                                        new Page(1, 1),
                                        () -> disqualificationOrderList(new DisqualificationOrderRequest().setCreator(u.getUserId()))
                                ).getList()
                        )
                ),
                new DisqualificationOrderResponse()
        );
        return new DataResult<>(
                r
                        .setDisqualificationOrderId("")
                        .setDisqualificationOrderNo(StrUtil.padPre(String.valueOf(disqualificationOrderDao.nextOrderNo()), 3, "0"))
                        .setPhotoList(new ArrayList<>())
                        .setQualityDealOpinion("")
                        .setSkillDealOpinion("")
                        .setFineAmount(null)
                        .setRemark("")
        );
    }

    private PlanEntity mergeRelevance(PlanRequest request, PlanEntity e) {
        planPhotoDao.remove(new LambdaUpdateWrapper<PlanPhotoEntity>().eq(PlanPhotoEntity::getPlanId, e.getId()));
        planPhotoDao.saveBatch(
                Stream.of(
                                request.getBeforePhotoList().stream()
                                        .map(t -> new PlanPhotoEntity().setPlanId(e.getId()).setPhotoUrl(t.getPhotoUrl()).setPhotoCompressUrl(t.getPhotoCompressUrl()).setPhotoType(0)),
                                request.getAfterPhotoList().stream()
                                        .map(t -> new PlanPhotoEntity().setPlanId(e.getId()).setPhotoUrl(t.getPhotoUrl()).setPhotoCompressUrl(t.getPhotoCompressUrl()).setPhotoType(1))
                        )
                        .flatMap(t -> t)
                        .collect(Collectors.toList()));
        planAttachmentDao.remove(new LambdaUpdateWrapper<PlanAttachmentEntity>().eq(PlanAttachmentEntity::getPlanId, e.getId()));
        planAttachmentDao.saveBatch(request.getAttachmentList().stream().map(t -> new PlanAttachmentEntity()
                .setPlanId(e.getId())
                .setFileId(t.getFileId())
                .setFilename(t.getFilename())
                .setUrl(t.getUrl())
        ).collect(Collectors.toList()));
        return e;
    }

    /**
     * 保存精益持续改善（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link PlanRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/plan")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result planAdminSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody PlanRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        PlanEntity e = (PlanEntity) INDUSTRY_INSTANCE.plan(request)
                .setCreator(u.getUserId())
                .setModifier(u.getUserId());
        planDao.save(e);
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    /**
     * 修改精益持续改善（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link PlanRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/plan")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result planAdminUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody PlanRequest request
    ) {
        final MpUserResponse u = accountHelper.checkUserAdminOrSelf(deviceId, request.getCreator());
        PlanEntity e = (PlanEntity) INDUSTRY_INSTANCE.plan(request)
                .setModifier(u.getUserId());
        planDao.updateById(e);
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    /**
     * 删除精益持续改善（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link PlanRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/plan")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result planAdminDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute PlanRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        if (isNotBlank(request.getPlanId())) {
            planDao.removeById(request.getPlanId());
            planPhotoDao.removeById(request.getPlanId());
        }
        return new Result();
    }

    private List<PlanEntity> planList(PlanRequest d) {
        LambdaQueryWrapper<PlanEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getPlanId())) {
            lambda.eq(PlanEntity::getId, d.getPlanId());
        }
        if (isNotBlank(d.getCreator())) {
            lambda.eq(PlanEntity::getCreator, d.getCreator());
        }
        if (isNotBlank(d.getUsername())) {
            lambda.like(PlanEntity::getCreator, d.getUsername());
        }
        if (null != d.getStartCreateDate()) {
            lambda.ge(PlanEntity::getCreateTime, cn.hutool.core.date.DateUtil.beginOfDay(d.getStartCreateDate()));
        }
        if (null != d.getEndCreateDate()) {
            lambda.le(PlanEntity::getCreateTime, cn.hutool.core.date.DateUtil.endOfDay(d.getEndCreateDate()));
        }
        if (isNotBlank(d.getDepartment())) {
            lambda.eq(PlanEntity::getDepartment, d.getDepartment());
        }
        if (isNotBlank(d.getExistsProblem())) {
            lambda.like(PlanEntity::getExistsProblem, d.getExistsProblem());
        }
        if (isNotBlank(d.getOptimizeType())) {
            lambda.eq(PlanEntity::getOptimizeType, d.getOptimizeType());
        }
        if (isNotBlank(d.getResponsiblePerson())) {
            lambda.like(PlanEntity::getResponsiblePerson, d.getResponsiblePerson());
        }
        if (Boolean.TRUE.equals(d.getValid())) {
            lambda.eq(PlanEntity::getValid, d.getValid());
        }
        return planDao.list(lambda.orderByDesc(PlanEntity::getCreateTime));
    }

    private List<PlanResponse> formatPlanList(List<PlanEntity> list) {
        List<PlanResponse> rl = INDUSTRY_INSTANCE.planList(list);
        MultitaskUtil.supplementList(
                rl,
                PlanResponse::getPlanId,
                l -> planPhotoDao.list(new LambdaQueryWrapper<PlanPhotoEntity>().in(PlanPhotoEntity::getPlanId, l)),
                (t, r) -> t.getPlanId().equals(r.getPlanId()),
                (t, r) -> {
                    if (0 == r.getPhotoType()) {
                        t.getBeforePhotoList().add(INDUSTRY_INSTANCE.photo(r, urlHelper.getUrlPrefix()));
                    } else {
                        t.getAfterPhotoList().add(INDUSTRY_INSTANCE.photo(r, urlHelper.getUrlPrefix()));
                    }
                }
        );
        List<String> userIdList = Stream.of(
                        rl.stream().map(PlanResponse::getCreator).filter(StrUtil::isNotBlank),
                        rl.stream().map(PlanResponse::getResponsiblePerson).filter(StrUtil::isNotBlank)
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
                PlanResponse::getCreator,
                l -> userList,
                (t, r) -> t.getCreator().equals(r.getId()),
                (t, r) -> t.setCreatorFormat(r.getName())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getResponsiblePerson())).collect(Collectors.toList()),
                PlanResponse::getResponsiblePerson,
                l -> userList,
                (t, r) -> t.getResponsiblePerson().equals(r.getId()),
                (t, r) -> t.setResponsiblePersonFormat(r.getName())
        );
        MultitaskUtil.supplementList(
                rl,
                PlanResponse::getPlanId,
                l -> planAttachmentDao.list(new LambdaQueryWrapper<PlanAttachmentEntity>().in(PlanAttachmentEntity::getPlanId, l)),
                (t, r) -> t.getPlanId().equals(r.getPlanId()),
                (t, r) -> t.getAttachmentList().add(INDUSTRY_INSTANCE.file(r, urlHelper.getUrlPrefix()))
        );
        MultitaskUtil.supplementList(
                rl,
                PlanResponse::getDepartment,
                l -> paramDao.listByCategoryId("department"),
                (t, r) -> t.getDepartment().equals(r.getValue()),
                (t, r) -> t.setDepartmentFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl,
                PlanResponse::getOptimizeType,
                l -> paramDao.listByCategoryId("optimizeType"),
                (t, r) -> t.getOptimizeType().equals(r.getValue()),
                (t, r) -> t.setOptimizeTypeFormat(r.getLabel())
        );
        return rl;
    }

    /**
     * 精益持续改善分页（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link PlanPageRequest}
     * @return {@link PageResult<PlanResponse>}
     */
    @GetMapping("admin/plan/page")
    public PageResult<PlanResponse> planAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute PlanPageRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleList().stream().noneMatch(t -> "planView".equals(t.getRoleCode()) || AdminUser.ADMIN.getCode().equals(t.getRoleCode()))) {
            request.getData().setResponsiblePerson(u.getUserId());
        }
        PageResult<PlanEntity> pr = DatabaseUtil.page(request, this::planList);
        AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(pr.getTotal(), formatPlanList(pr.getList())
                .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList())
        );
    }

    /**
     * 精益持续改善
     *
     * @param deviceId 设备id
     * @param request  {@link PlanRequest}
     * @return {@link DataResult<PlanResponse>}
     */
    @GetMapping("plan")
    public DataResult<PlanResponse> plan(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute PlanRequest request
    ) {
        return new DataResult<>(CollUtil.getFirst(formatPlanList(planList(request))));
    }

    /**
     * 不合格单（公开）
     *
     * @return {@link Result}
     */
    @GetMapping("disqualification-order")
    public Result disqualificationOrder(
            @ModelAttribute DisqualificationOrderRequest request
    ) {
        DisqualificationOrderResponse r = defaultIfNull(
                CollUtil.getFirst(
                        formatDisqualificationOrderList(
                                disqualificationOrderList(request)
                        )
                ),
                new DisqualificationOrderResponse()
        );
        return new DataResult<>(
                r
        );
    }

    /**
     * 保存检测设备台账（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link DeviceCheckLedgerRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/device-check-ledger")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result deviceCheckLedgerAdminSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody DeviceCheckLedgerRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        // 只有计量员角色可以新增
        if (u.getRoleCodeList().stream().noneMatch("gauger"::equals)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        final DeviceCheckLedgerEntity e = (DeviceCheckLedgerEntity) INDUSTRY_INSTANCE.deviceCheckLedger(request)
                .setDueDate(DateUtil.day(cn.hutool.core.date.DateUtil.offsetDay(DateUtil.parse(request.getCalibrationDate()), request.getCalibrationPeriod())))
                .setBorrower("," + String.join(",", request.getBorrowerList()) + ",")
                .setCreator(u.getUserId())
                .setModifier(u.getUserId());
        deviceCheckLedgerDao.save(e);
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    private DeviceCheckLedgerEntity mergeRelevance(DeviceCheckLedgerRequest request, DeviceCheckLedgerEntity e) {
        deviceCheckLedgerPhotoDao.remove(new LambdaUpdateWrapper<DeviceCheckLedgerPhotoEntity>().eq(DeviceCheckLedgerPhotoEntity::getDeviceCheckLedgerId, e.getId()));
        deviceCheckLedgerPhotoDao.saveBatch(request.getPhotoList().stream().map(t -> new DeviceCheckLedgerPhotoEntity().setDeviceCheckLedgerId(e.getId()).setPhotoUrl(t.getPhotoUrl()).setPhotoCompressUrl(t.getPhotoCompressUrl())).collect(Collectors.toList()));
        return e;
    }

    /**
     * 修改检测设备台账（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link DeviceCheckLedgerRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/device-check-ledger")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result deviceCheckLedgerAdminUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody DeviceCheckLedgerRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        DeviceCheckLedgerEntity e = (DeviceCheckLedgerEntity) INDUSTRY_INSTANCE.deviceCheckLedger(request)
                .setDueDate(DateUtil.day(cn.hutool.core.date.DateUtil.offsetDay(DateUtil.parse(request.getCalibrationDate()), request.getCalibrationPeriod())))
                .setBorrowDate(isBlank(request.getBorrower()) ? "" : defaultIfBlank(request.getBorrowDate(), DateUtil.day(new Date())))
                .setBorrower("," + String.join(",", request.getBorrowerList()) + ",")
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<DeviceCheckLedgerEntity> lambda = new LambdaUpdateWrapper<DeviceCheckLedgerEntity>()
                .eq(DeviceCheckLedgerEntity::getId, request.getDeviceCheckLedgerId());
        // 只有计量员角色可以修改
        if (u.getRoleCodeList().stream().noneMatch("gauger"::equals)) {
            lambda.eq(DeviceCheckLedgerEntity::getCreator, u.getUserId());
        }
        if (!deviceCheckLedgerDao.update(
                e,
                lambda
        )) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    /**
     * 删除检测设备台账（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link DeviceCheckLedgerRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/device-check-ledger")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result deviceCheckLedgerAdminDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute DeviceCheckLedgerRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        if (isNotBlank(request.getDeviceCheckLedgerId())) {
            deviceCheckLedgerDao.removeById(request.getDeviceCheckLedgerId());
            deviceCheckLedgerPhotoDao.removeById(request.getDeviceCheckLedgerId());
        }
        return new Result();
    }


    /**
     * 检测设备台账分页（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link DeviceCheckLedgerRequest}
     * @return {@link ListResult <DeviceCheckLedgerResponse>}
     */
    @GetMapping("admin/device-check-ledger/list")
    public ListResult<DeviceCheckLedgerResponse> deviceCheckLedgerAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute DeviceCheckLedgerRequest request
    ) {
        MpUserResponse user = accountHelper.getUser(deviceId);
        if (user.getRoleList().stream().noneMatch(t -> "gauger".equals(t.getRoleCode()) || "computerView".equals(t.getRoleCode()) || AdminUser.ADMIN.getCode().equals(t.getRoleCode()))) {
            request.setBorrower(user.getUserId());
        }
        return new ListResult<>(formatDeviceCheckLedgerList(deviceCheckLedgerList(request)));
    }

    private List<DeviceCheckLedgerEntity> deviceCheckLedgerList(DeviceCheckLedgerRequest d) {
        LambdaQueryWrapper<DeviceCheckLedgerEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(d.getDeviceCheckLedgerId())) {
            lambda.eq(DeviceCheckLedgerEntity::getId, d.getDeviceCheckLedgerId());
        }
        if (null != d.getStartDueDate()) {
            lambda.like(DeviceCheckLedgerEntity::getDeviceNumber, d.getDeviceNumber());
        }
        if (null != d.getStartDueDate()) {
            lambda.ge(DeviceCheckLedgerEntity::getDueDate, cn.hutool.core.date.DateUtil.beginOfDay(d.getStartDueDate()));
        }
        if (null != d.getEndDueDate()) {
            lambda.le(DeviceCheckLedgerEntity::getDueDate, cn.hutool.core.date.DateUtil.endOfDay(d.getEndDueDate()));
        }
        if (isNotBlank(d.getDeviceNumber())) {
            lambda.like(DeviceCheckLedgerEntity::getDeviceNumber, d.getDeviceNumber());
        }
        if (isNotBlank(d.getSpecification())) {
            lambda.like(DeviceCheckLedgerEntity::getSpecification, d.getSpecification());
        }
        if (isNotBlank(d.getChineseVietnamName())) {
            lambda.eq(DeviceCheckLedgerEntity::getChineseVietnamName, d.getChineseVietnamName());
        }
        if (isNotBlank(d.getDeviceCheckLedgerState())) {
            lambda.eq(DeviceCheckLedgerEntity::getDeviceCheckLedgerState, d.getDeviceCheckLedgerState());
        }
        if (isNotBlank(d.getBorrower())) {
            lambda.like(DeviceCheckLedgerEntity::getBorrower, d.getBorrower());
        }
        if (isNotBlank(d.getStorage())) {
            lambda.eq(DeviceCheckLedgerEntity::getStorage, d.getStorage());
        }
        if (null != d.getOutOfStock()) {
            lambda.eq(DeviceCheckLedgerEntity::getOutOfStock, d.getOutOfStock());
        }
        return deviceCheckLedgerDao.list(lambda.orderByDesc(DeviceCheckLedgerEntity::getCreateTime));
    }

    private List<DeviceCheckLedgerResponse> formatDeviceCheckLedgerList(List<DeviceCheckLedgerEntity> list) {
        List<DeviceCheckLedgerResponse> rl = INDUSTRY_INSTANCE.deviceCheckLedgerList(list);
        final List<String> userIdList = Stream.of(
                        rl.stream().map(DeviceCheckLedgerResponse::getCreator).filter(StrUtil::isNotBlank),
                        rl.stream().map(DeviceCheckLedgerResponse::getBorrowerList).flatMap(t -> t.stream()).filter(StrUtil::isNotBlank)
                )
                .flatMap(t -> t)
                .distinct()
                .collect(Collectors.toList());
        final List<MpUserEntity> userList = CollUtil.isEmpty(userIdList) ? new ArrayList<>() : userMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName),
                        userIdList,
                        (lam, pl) -> lam.in(MpUserEntity::getId, pl))
        );
        final Map<String, String> userMap = userList.stream().collect(Collectors.toMap(MpUserEntity::getId, MpUserEntity::getName, (t, t1) -> t1, HashMap::new));
        MultitaskUtil.supplementList(
                rl,
                DeviceCheckLedgerResponse::getDeviceCheckLedgerId,
                l -> deviceCheckLedgerPhotoDao.list(new LambdaQueryWrapper<DeviceCheckLedgerPhotoEntity>().in(DeviceCheckLedgerPhotoEntity::getDeviceCheckLedgerId, l)),
                (t, r) -> t.getDeviceCheckLedgerId().equals(r.getDeviceCheckLedgerId()),
                (t, r) -> t.getPhotoList().add(INDUSTRY_INSTANCE.photo(r, urlHelper.getUrlPrefix()))
        );
        MultitaskUtil.supplementList(
                rl,
                DeviceCheckLedgerResponse::getCreator,
                l -> userList,
                (t, r) -> t.getCreator().equals(r.getId()),
                (t, r) -> t.setCreatorFormat(r.getUsername())
        );
        final List<ParamConfigResponse> chineseVietnamNameList = paramDao.listByCategoryId("chineseVietnamName");
        final Map<String, Integer> chineseVietnamNameMap = IntStream.range(0, chineseVietnamNameList.size())
                .boxed()
                .collect(Collectors.toMap(t -> String.valueOf(chineseVietnamNameList.get(t).getValue()), t -> t, (t, t1) -> t1));
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getChineseVietnamName())).collect(Collectors.toList()),
                DeviceCheckLedgerResponse::getChineseVietnamName,
                l -> chineseVietnamNameList,
                (t, r) -> t.getChineseVietnamName().equals(r.getValue()),
                (t, r) -> t.setChineseVietnamNameFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getCalibrationPeriod())).collect(Collectors.toList()),
                DeviceCheckLedgerResponse::getCalibrationPeriod,
                l -> paramDao.listByCategoryId("calibrationPeriod"),
                (t, r) -> t.getCalibrationPeriod().equals(NumberUtil.defaultInt(r.getValue())),
                (t, r) -> t.setCalibrationPeriodFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getStorage())).collect(Collectors.toList()),
                DeviceCheckLedgerResponse::getStorage,
                l -> paramDao.listByCategoryId("storage"),
                (t, r) -> t.getStorage().equals(r.getValue()),
                (t, r) -> t.setStorageFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getDeviceCheckLedgerState())).collect(Collectors.toList()),
                DeviceCheckLedgerResponse::getDeviceCheckLedgerState,
                l -> paramDao.listByCategoryId("deviceCheckLedgerState"),
                (t, r) -> t.getDeviceCheckLedgerState().equals(r.getValue()),
                (t, r) -> t.setDeviceCheckLedgerStateFormat(r.getLabel())
        );
        final AtomicInteger index = new AtomicInteger(0);
        DateTime dayAfter15 = cn.hutool.core.date.DateUtil.offsetDay(DateTime.now(), 15);
        return
                rl.stream().sorted((o1, o2) -> {
                    int r = chineseVietnamNameMap.getOrDefault(o1.getChineseVietnamName(), 0) - chineseVietnamNameMap.getOrDefault(o2.getChineseVietnamName(), 0);
                    if (r != 0) {
                        return r;
                    } else {
                        return defaultIfBlank(o1.getCreateTime()).compareTo(defaultIfBlank(o2.getCreateTime()));
                    }
                }).peek(t -> {
                    t.setIndex(index.addAndGet(1))
                            .setBorrowerFormat(t.getBorrowerList().stream().filter(StrUtil::isNotBlank).map(t1 -> userMap.getOrDefault(t1, t1)).collect(Collectors.joining(",")))
                            .setOutOfStockFormat(Boolean.TRUE.equals(t.getOutOfStock()) ? "Yes" : "No")
                            .setDueDateWarning(DateUtil.parse(t.getDueDate()).compareTo(dayAfter15) <= 0)
                    ;
                }).collect(Collectors.toList());
    }


    /**
     * 保存电脑（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link ComputerRequest}
     * @return {@link Result}
     */
    @PostMapping("admin/computer")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result computerAdminSave(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ComputerRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (u.getRoleCodeList().stream().noneMatch("itManager"::equals)) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        ComputerEntity e = (ComputerEntity) INDUSTRY_INSTANCE.computer(request)
                .setCreator(u.getUserId())
                .setModifier(u.getUserId());
        computerDao.save(e);
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    private ComputerEntity mergeRelevance(ComputerRequest request, ComputerEntity e) {
        computerPhotoDao.remove(new LambdaUpdateWrapper<ComputerPhotoEntity>().eq(ComputerPhotoEntity::getComputerId, e.getId()));
        computerPhotoDao.saveBatch(request.getPhotoList().stream().map(t -> new ComputerPhotoEntity().setComputerId(e.getId()).setPhotoUrl(t.getPhotoUrl()).setPhotoCompressUrl(t.getPhotoCompressUrl())).collect(Collectors.toList()));
        return e;
    }

    /**
     * 修改电脑（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link ComputerRequest}
     * @return {@link Result}
     */
    @PutMapping("admin/computer")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result computerAdminUpdate(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ComputerRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        ComputerEntity e = (ComputerEntity) INDUSTRY_INSTANCE.computer(request)
                .setModifier(u.getUserId());
        LambdaUpdateWrapper<ComputerEntity> lambda = new LambdaUpdateWrapper<ComputerEntity>()
                .eq(ComputerEntity::getId, request.getComputerId());
        if (u.getRoleCodeList().stream().noneMatch("itManager"::equals)) {
            lambda.eq(ComputerEntity::getCreator, u.getUserId());
        }
        if (!computerDao.update(
                e,
                lambda
        )) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, e);
        return new DataResult<>(e);
    }

    /**
     * 删除电脑（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link ComputerRequest}
     * @return {@link Result}
     */
    @DeleteMapping("admin/computer")
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result computerAdminDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ComputerRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        if (!"admin".equals(u.getUsername())) {
            throw new BusinessException(AUTHORITY_AUTH_FAIL);
        }
        if (isNotBlank(request.getComputerId())) {
            computerDao.removeById(request.getComputerId());
            computerPhotoDao.removeById(request.getComputerId());
        }
        return new Result();
    }

    private List<ComputerEntity> computerList(ComputerRequest request) {
        LambdaQueryWrapper<ComputerEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getComputerId())) {
            lambda.eq(ComputerEntity::getId, request.getComputerId());
        }
        if (isNotBlank(request.getUserId())) {
            lambda.eq(ComputerEntity::getUserId, request.getUserId());
        }
        if (CollUtil.isNotEmpty(request.getUserIdList())) {
            DatabaseUtil.or(lambda, request.getUserIdList(), (l, dl) -> l.in(ComputerEntity::getUserId, dl));
        }
        if (isNotBlank(request.getBrand())) {
            lambda.like(ComputerEntity::getBrand, request.getBrand());
        }
        if (isNotBlank(request.getComputerName())) {
            lambda.like(ComputerEntity::getName, request.getComputerName());
        }
        if (isNotBlank(request.getMaterialNo())) {
            lambda.like(ComputerEntity::getMaterialNo, request.getMaterialNo());
        }
        if (isNotBlank(request.getModel())) {
            lambda.like(ComputerEntity::getModel, request.getModel());
        }
        if (isNotBlank(request.getPosition())) {
            lambda.eq(ComputerEntity::getPosition, request.getPosition());
        }
        if (null != request.getDetailed()) {
            lambda.eq(ComputerEntity::getDetailed, request.getDetailed());
        }
        return computerDao.list(lambda.orderByDesc(ComputerEntity::getUserId).orderByDesc(ComputerEntity::getCreateTime));
    }

    private List<ComputerResponse> formatComputerList(List<ComputerEntity> list) {
        List<ComputerResponse> rl = INDUSTRY_INSTANCE.computerList(list);
        MultitaskUtil.supplementList(
                rl,
                ComputerResponse::getComputerId,
                l -> computerPhotoDao.list(new LambdaQueryWrapper<ComputerPhotoEntity>().in(ComputerPhotoEntity::getComputerId, l)),
                (t, r) -> t.getComputerId().equals(r.getComputerId()),
                (t, r) -> t.getPhotoList().add(INDUSTRY_INSTANCE.photo(r, urlHelper.getUrlPrefix()))
        );
        MultitaskUtil.supplementList(
                rl,
                ComputerResponse::getCreator,
                l -> userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().in(MpUserEntity::getId, l)),
                (t, r) -> t.getCreator().equals(r.getId()),
                (t, r) -> t.setCreatorFormat(r.getUsername())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getUserId())).collect(Collectors.toList()),
                ComputerResponse::getUserId,
                l -> userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().in(MpUserEntity::getId, l)),
                (t, r) -> t.getUserId().equals(r.getId()),
                (t, r) -> {
                    t.setUsername(r.getUsername());
                    t.setUserChineseName(r.getName());
                    t.setDepartment(r.getDepartment());
                    t.setProfession(r.getProfession());
                }
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getDepartment())).collect(Collectors.toList()),
                ComputerResponse::getDepartment,
                l -> paramDao.listByCategoryId("department"),
                (t, r) -> t.getDepartment().equals(r.getValue()),
                (t, r) -> t.setDepartmentFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getProfession())).collect(Collectors.toList()),
                ComputerResponse::getProfession,
                l -> paramDao.listByCategoryId("profession"),
                (t, r) -> t.getProfession().equals(r.getValue()),
                (t, r) -> t.setProfessionFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getComputerName())).collect(Collectors.toList()),
                ComputerResponse::getComputerName,
                l -> paramDao.listByCategoryId("computerName"),
                (t, r) -> t.getComputerName().equals(r.getValue()),
                (t, r) -> t.setComputerNameFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getPosition())).collect(Collectors.toList()),
                ComputerResponse::getPosition,
                l -> paramDao.listByCategoryId("companyPosition"),
                (t, r) -> t.getPosition().equals(r.getValue()),
                (t, r) -> t.setPositionFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                rl.stream().filter(t -> isNotBlank(t.getComputerState())).collect(Collectors.toList()),
                ComputerResponse::getComputerState,
                l -> paramDao.listByCategoryId("computerState"),
                (t, r) -> t.getComputerState().equals(r.getValue()),
                (t, r) -> t.setComputerStateFormat(r.getLabel())
        );
        for (ComputerResponse t : rl) {
            t.setDetailedFormat(Boolean.TRUE.equals(t.getDetailed()) ? "是" : "否");
        }
        return rl;
    }

    /**
     * 电脑分页（后管）
     *
     * @param deviceId 设备id
     * @param request  {@link ComputerRequest}
     * @return {@link ListResult <ComputerResponse>}
     */
    @GetMapping("admin/computer/page")
    public PageResult<ComputerResponse> computerAdminPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ComputerPageRequest request
    ) {
        MpUserResponse user = accountHelper.getUser(deviceId);
        if (user.getRoleCodeList().stream().noneMatch(t -> AdminRole.ADMIN.getCode().equals(t) || "itManager".equals(t) || "itView".equals(t))) {
            request.getData().setUserId(user.getUserId());
        }
        if (isNotBlank(request.getData().getUsername())) {
            List<String> userIdList = userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().like(MpUserEntity::getUsername, request.getData().getUsername()).select(MpUserEntity::getId))
                    .stream().map(MpUserEntity::getId).collect(Collectors.toList());
            if (CollUtil.isEmpty(userIdList)) {
                return new PageResult<>();
            } else {
                request.getData().setUserIdList(userIdList);
            }
        }
        PageResult<ComputerEntity> pr = DatabaseUtil.page(request, this::computerList);
        AtomicInteger atomicInteger = new AtomicInteger((request.getPage().getPage() - 1) * request.getPage().getLimit());
        return new PageResult<>(pr.getTotal(), formatComputerList(pr.getList())
                .stream().peek(t -> t.setIndex(atomicInteger.addAndGet(1))).collect(Collectors.toList()));
    }

    /*========>>>>>>>>分割线<<<<<<<<========*/

    /**
     * 获取正文单元格样式
     *
     * @param workbook 工作薄对象
     * @return 单元格样式
     */
    private static CellStyle getBodyCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.NO_FILL);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        return cellStyle;
    }

    /**
     * 获取正文单元格样式
     *
     * @param workbook 工作薄对象
     * @return 单元格样式
     */
    private static CellStyle getIntegerCellStyle(Workbook workbook) {
        CellStyle cellStyle = getBodyCellStyle(workbook);
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        return cellStyle;
    }

    /**
     * 获取正文单元格样式
     *
     * @param workbook 工作薄对象
     * @return 单元格样式
     */
    private static CellStyle getNumberCellStyle(Workbook workbook) {
        CellStyle cellStyle = getBodyCellStyle(workbook);
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.0"));
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        return cellStyle;
    }

    /**
     * 获取正文单元格样式
     *
     * @param workbook 工作薄对象
     * @return 单元格样式
     */
    private static CellStyle getNavCellStyle(Workbook workbook) {
        CellStyle cellStyle = getBodyCellStyle(workbook);
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,###.0000"));
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        return cellStyle;
    }

    /**
     * 获取正文单元格样式
     *
     * @param workbook 工作薄对象
     * @return 单元格样式
     */
    private static CellStyle getDateCellStyle(Workbook workbook) {
        CellStyle cellStyle = getBodyCellStyle(workbook);
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        return cellStyle;
    }
}
