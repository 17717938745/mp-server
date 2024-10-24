package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.dmmp.MpAccountEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpSignInHistoryEntity;
import com.lead.fund.base.server.mp.entity.douson.*;
import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.model.VideoModel;
import com.lead.fund.base.server.mp.request.*;
import com.lead.fund.base.server.mp.response.*;

import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * AccountConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface IndustryConverter extends Serializable {

    IndustryConverter INDUSTRY_INSTANCE = Mappers.getMapper(IndustryConverter.class);

    @Mapping(target = "value", source = "paramCode")
    @Mapping(target = "label", source = "paramName")
    ParamConfigResponse param(ParamEntity e);

    @Mapping(target = "id", source = "productId")
    ProductEntity product(ProductRequest data);

    @Mapping(target = "openId", source = "openId")
    @Mapping(target = "openIdFormat", expression = "java(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getNickname(), com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getMobile(), t.getOpenId())))")
    ProductResponse account(@MappingTarget ProductResponse r, MpAccountEntity t);

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "reportDate", expression = "java(com.lead.fund.base.common.util.DateUtil.day(t.getReportDate()))")
    @Mapping(target = "debugMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getDebugMinute()))")
    @Mapping(target = "clampingMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getClampingMinute()))")
    @Mapping(target = "assistMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getAssistMinute()))")
    @Mapping(target = "runningMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getRunningMinute()))")
    @Mapping(target = "countHour8", expression = "java(java.math.BigDecimal.valueOf(435.0).divide(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getClampingMinute()).add(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getAssistMinute()).add(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getRunningMinute())).max(java.math.BigDecimal.valueOf(1.0))), 1, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "productStandMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getClampingMinute()).add(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getAssistMinute()).add(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getRunningMinute()))))")
    @Mapping(target = "countHour12", expression = "java(java.math.BigDecimal.valueOf(585.0).divide(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getClampingMinute()).add(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getAssistMinute()).add(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getRunningMinute())).max(java.math.BigDecimal.valueOf(1.0))), 1, java.math.RoundingMode.HALF_UP))")
    ProductResponse data(ProductEntity t);

    List<ProductResponse> list(List<ProductEntity> list);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getPhotoCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getPhotoCompressUrl().startsWith(\"http:\") || e.getPhotoCompressUrl().startsWith(\"https:\") ? e.getPhotoCompressUrl() : (urlPrefix + e.getPhotoCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getPhotoUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getPhotoUrl().startsWith(\"http:\") || e.getPhotoUrl().startsWith(\"https:\") ? e.getPhotoUrl() : (urlPrefix + e.getPhotoUrl()))")
    PhotoImgModel photo(ProductPhotoEntity e, String urlPrefix);

    @Mapping(target = "photoList", expression = "java((List<com.lead.fund.base.server.mp.model.PhotoImgModel>)cn.hutool.core.collection.CollUtil.addAll(r.getPhotoList(), photo(t, urlPrefix)))")
    ProductResponse photo(@MappingTarget ProductResponse r, ProductPhotoEntity t, String urlPrefix);

    ProductPhotoEntity photo(PhotoImgModel t);

    List<ProductPhotoEntity> photoList(List<PhotoImgModel> l);


    @Mapping(target = "reportTime", source = "createdTime")
    @Mapping(target = "reportId", source = "id")
    @Mapping(target = "reportDate", expression = "java(com.lead.fund.base.common.util.DateUtil.day(t.getReportDate()))")
    @Mapping(target = "photoList", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "workMinute", constant = "435")
    @Mapping(target = "leaderSubsidyMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getLeaderSubsidyMinute()))")
    @Mapping(target = "deviceRunningStartHour", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getDeviceRunningStartHour()))")
    @Mapping(target = "deviceRunningStartMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getDeviceRunningStartMinute()))")
    @Mapping(target = "deviceRunningEndHour", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getDeviceRunningEndHour()))")
    @Mapping(target = "deviceRunningEndMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getDeviceRunningEndMinute()))")
    @Mapping(target = "actualCompleteCount", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getActualCompleteCount()))")
    ReportResponse data(ReportEntity t);

    List<ReportResponse> reportList(List<ReportEntity> list);

    @Mapping(target = "id", source = "reportId")
    ReportEntity report(ReportRequest t);

    @Mapping(target = "id", source = "accidentId")
    AccidentEntity accident(AccidentRequest t);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getPhotoCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getPhotoCompressUrl().startsWith(\"http:\") || e.getPhotoCompressUrl().startsWith(\"https:\") ? e.getPhotoCompressUrl() : (urlPrefix + e.getPhotoCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getPhotoUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getPhotoUrl().startsWith(\"http:\") || e.getPhotoUrl().startsWith(\"https:\") ? e.getPhotoUrl() : (urlPrefix + e.getPhotoUrl()))")
    PhotoImgModel reportPhoto(ReportPhotoEntity e, String urlPrefix);

    @Mapping(target = "photoList", expression = "java((List<com.lead.fund.base.server.mp.model.PhotoImgModel>)cn.hutool.core.collection.CollUtil.addAll(r.getPhotoList(), reportPhoto(t, urlPrefix)))")
    ReportResponse reportPhoto(@MappingTarget ReportResponse r, ReportPhotoEntity t, String urlPrefix);

    @Mapping(target = "reportDate", ignore = true)
    @Mapping(target = "photoList", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "orderId", expression = "java(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(r.getOrderId(), t.getOrderId()))")
    @Mapping(target = "actualCompleteCount", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(r.getActualCompleteCount()))")
    @Mapping(target = "workMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(r.getWorkMinute(), 435))")
    @Mapping(target = "clampingMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(r.getClampingMinute(), t.getClampingMinute()))")
    @Mapping(target = "assistMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(r.getAssistMinute(), t.getAssistMinute()))")
    @Mapping(target = "runningMinute", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(r.getRunningMinute(), t.getRunningMinute()))")
    @Mapping(target = "productStandMinute", expression = "java(java.math.BigDecimal.valueOf(r.getClampingMinute() + r.getAssistMinute() + r.getRunningMinute()))")
    @Mapping(target = "shouldCompleteCount", expression = "java(java.math.BigDecimal.valueOf(r.getWorkMinute()).divide(r.getProductStandMinute().max(java.math.BigDecimal.valueOf(1.0)), 1, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "completeMinute", expression = "java(r.getActualCompleteCount().multiply(r.getProductStandMinute()))")
    ReportResponse formatReport(@MappingTarget ReportResponse r, ProductEntity t);

    @Mapping(target = "deviceRunningMinute", expression = "java((t.getDeviceRunningEndHour() - t.getDeviceRunningStartHour()) * 60 + (t.getDeviceRunningEndMinute() - t.getDeviceRunningStartMinute()))")
    @Mapping(target = "deviceUsePercent", expression = "java(new java.math.BigDecimal(r.getDeviceRunningMinute()).divide(new java.math.BigDecimal(t.getWorkMinute()), 4, java.math.RoundingMode.HALF_UP))")
    @Mapping(target = "reportTime", expression = "java(null == t.getReportTime() ? new java.util.Date() : t.getReportTime())")
    UserDeviceResponse userDevice(@MappingTarget UserDeviceResponse r, ReportResponse t);

    @Mapping(target = "testDeviceFormat", source = "deviceName")
    @Mapping(target = "deviceUnitPrice", source = "unitPrice")
    @Mapping(target = "deviceRunningStartHour", source = "runningHour")
    @Mapping(target = "deviceRunningStartMinute", source = "runningMinute")
    @Mapping(target = "runningMinute", ignore = true)
    ProductResponse deviceDecorateProduct(@MappingTarget ProductResponse t, DeviceEntity t1);

    @Mapping(target = "testDeviceFormat", source = "t.deviceName")
    @Mapping(target = "deviceUnitPrice", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getUnitPrice()))")
    @Mapping(target = "deviceUnitPriceFormat", expression = "java(com.lead.fund.base.common.util.NumberUtil.formatIntTh(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getUnitPrice())))")
    @Mapping(target = "deviceRunningStartHour", expression = "java(com.lead.fund.base.common.util.StrUtil.isNotBlank(r.getReportId()) ? r.getDeviceRunningStartHour() : com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getRunningHour()).intValue())")
    @Mapping(target = "deviceRunningStartMinute", expression = "java(com.lead.fund.base.common.util.StrUtil.isNotBlank(r.getReportId()) ? r.getDeviceRunningStartMinute() : com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getRunningMinute()).intValue())")
    @Mapping(target = "runningMinute", ignore = true)
    ReportResponse formatReport(@MappingTarget ReportResponse r, DeviceEntity t);

    @Mapping(target = "orderId", source = "id")
    OrderResponse order(OrderEntity t);

    List<OrderResponse> orderList(List<OrderEntity> orderList);

    @Mapping(target = "id", source = "orderId")
    OrderEntity order(OrderRequest request);

    ProductResponse formatProduct(@MappingTarget ProductResponse r, OrderEntity t);

    @Mapping(target = "orderCount", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getOrderCount()))")
    @Mapping(target = "actualCompleteCount", ignore = true)
    @Mapping(target = "surplusCount", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getOrderCount()).subtract(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getActualCompleteCount())))")
    ReportResponse formatReport(@MappingTarget ReportResponse r, OrderEntity t);

    @Mapping(target = "orderCount", ignore = true)
    OrderEntity reportToOrder(ReportRequest request);

    @Mapping(target = "actualCompleteCount", constant = "0")
    OrderEntity productToOrder(ProductRequest request);

    @Mapping(target = "accidentId", source = "accidentId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "0")
    @Mapping(target = "url", source = "t.photoUrl")
    @Mapping(target = "compressUrl", source = "t.photoCompressUrl")
    AccidentAttachmentEntity accidentAttachment(String accidentId, String attachmentCategory, PhotoImgModel t);

    @Mapping(target = "accidentId", source = "accidentId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "1")
    @Mapping(target = "url", source = "t.videoUrl")
    @Mapping(target = "compressUrl", source = "t.videoCompressUrl")
    AccidentAttachmentEntity accidentAttachment(String accidentId, String attachmentCategory, VideoModel t);

    @Mapping(target = "accidentId", source = "id")
    @Mapping(target = "fineAmount1Format", expression = "java(com.lead.fund.base.common.util.NumberUtil.formatIntTh(t.getFineAmount1()))")
    @Mapping(target = "fineAmount2Format", expression = "java(com.lead.fund.base.common.util.NumberUtil.formatIntTh(t.getFineAmount2()))")
    @Mapping(target = "fineAmount3Format", expression = "java(com.lead.fund.base.common.util.NumberUtil.formatIntTh(t.getFineAmount3()))")
    AccidentResponse accident(AccidentEntity t);

    List<AccidentResponse> accidentList(List<AccidentEntity> l);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getCompressUrl().startsWith(\"http:\") || e.getCompressUrl().startsWith(\"https:\") ? e.getCompressUrl() : (urlPrefix + e.getCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    PhotoImgModel accidentPhoto(AccidentAttachmentEntity e, String urlPrefix);

    @Mapping(target = "videoCompressUrl", expression = "java(e.getCompressUrl())")
    @Mapping(target = "fullVideoCompressUrl", expression = "java(e.getCompressUrl().startsWith(\"http:\") || e.getCompressUrl().startsWith(\"https:\") ? e.getCompressUrl() : (urlPrefix + e.getCompressUrl()))")
    @Mapping(target = "videoUrl", expression = "java(e.getUrl())")
    @Mapping(target = "fullVideoUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    VideoModel accidentVideo(AccidentAttachmentEntity e, String urlPrefix);

    @Mapping(target = "id", source = "boxFlagId")
    BoxFlagEntity boxFlag(BoxFlagRequest request);

    @Mapping(target = "boxFlagId", source = "id")
    @Mapping(target = "stateFormat", expression = "java(com.lead.fund.base.common.basic.cons.frame.AdminState.label(d.getState()))")
    @Mapping(target = "boxNumberFormat", expression = "java(\"T\" + d.getBoxNumber())")
    @Mapping(target = "createTime", expression = "java(com.lead.fund.base.common.util.DateUtil.tradeDateTime(d.getCreateTime()))")
    @Mapping(target = "modifyTime", expression = "java(com.lead.fund.base.common.util.DateUtil.tradeDateTime(d.getModifyTime()))")
    @Mapping(target = "length", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(d.getLength()))")
    @Mapping(target = "width", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(d.getWidth()))")
    @Mapping(target = "height", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(d.getHeight()))")
    @Mapping(target = "volume", expression = "java(d.getLength().multiply(d.getWidth()).multiply(d.getHeight()))")
    @Mapping(target = "volumeFormat", expression = "java(d.getLength().intValue() + \"*\" + d.getWidth().intValue()+ \"*\" + d.getHeight().intValue())")
    BoxFlagResponse boxFlag(BoxFlagEntity d);

    List<BoxFlagResponse> boxFlagList(List<BoxFlagEntity> list);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getPhotoCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getPhotoCompressUrl().startsWith(\"http:\") || e.getPhotoCompressUrl().startsWith(\"https:\") ? e.getPhotoCompressUrl() : (urlPrefix + e.getPhotoCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getPhotoUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getPhotoUrl().startsWith(\"http:\") || e.getPhotoUrl().startsWith(\"https:\") ? e.getPhotoUrl() : (urlPrefix + e.getPhotoUrl()))")
    PhotoImgModel photo(BoxFlagPhotoEntity e, String urlPrefix);

    @Mapping(target = "id", source = "disqualificationOrderId")
    DisqualificationOrderEntity disqualificationOrder(DisqualificationOrderRequest request);

    @Mapping(target = "disqualificationOrderId", source = "id")
    @Mapping(target = "disqualificationOrderNo", expression = "java(java.lang.String.valueOf(d.getDisqualificationOrderNo()))")
    @Mapping(target = "disqualificationOrderNoFormat", expression = "java(\"NCR-\" + com.lead.fund.base.common.util.DateUtil.daySimple(d.getCreateTime()) + com.lead.fund.base.common.util.StrUtil.padPre(java.lang.String.valueOf(d.getDisqualificationOrderNo()), 3, \"0\"))")
    @Mapping(target = "stateFormat", expression = "java(com.lead.fund.base.common.basic.cons.frame.AdminState.label(d.getState()))")
    @Mapping(target = "createTime", expression = "java(com.lead.fund.base.common.util.DateUtil.tradeDateTime(d.getCreateTime()))")
    @Mapping(target = "modifyTime", expression = "java(com.lead.fund.base.common.util.DateUtil.tradeDateTime(d.getModifyTime()))")
    @Mapping(target = "fineAmountFormat", expression = "java(com.lead.fund.base.common.util.NumberUtil.formatIntTh(d.getFineAmount()))")
    @Mapping(target = "dutyPersonList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(d.getDutyPerson()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    DisqualificationOrderResponse disqualificationOrder(DisqualificationOrderEntity d);

    List<DisqualificationOrderResponse> disqualificationOrderList(List<DisqualificationOrderEntity> list);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getPhotoCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getPhotoCompressUrl().startsWith(\"http:\") || e.getPhotoCompressUrl().startsWith(\"https:\") ? e.getPhotoCompressUrl() : (urlPrefix + e.getPhotoCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getPhotoUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getPhotoUrl().startsWith(\"http:\") || e.getPhotoUrl().startsWith(\"https:\") ? e.getPhotoUrl() : (urlPrefix + e.getPhotoUrl()))")
    PhotoImgModel photo(DisqualificationOrderPhotoEntity e, String urlPrefix);

    @Mapping(target = "id", source = "planId")
    PlanEntity plan(PlanRequest request);

    @Mapping(target = "planId", source = "id")
    @Mapping(target = "stateFormat", expression = "java(com.lead.fund.base.common.basic.cons.frame.AdminState.label(d.getState()))")
    @Mapping(target = "createTime", expression = "java(com.lead.fund.base.common.util.DateUtil.tradeDateTime(d.getCreateTime()))")
    @Mapping(target = "modifyTime", expression = "java(com.lead.fund.base.common.util.DateUtil.tradeDateTime(d.getModifyTime()))")
    @Mapping(target = "awardAmountFormat", expression = "java(com.lead.fund.base.common.util.NumberUtil.formatIntTh(d.getAwardAmount()))")
    PlanResponse plan(PlanEntity d);

    List<PlanResponse> planList(List<PlanEntity> list);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getPhotoCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getPhotoCompressUrl().startsWith(\"http:\") || e.getPhotoCompressUrl().startsWith(\"https:\") ? e.getPhotoCompressUrl() : (urlPrefix + e.getPhotoCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getPhotoUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getPhotoUrl().startsWith(\"http:\") || e.getPhotoUrl().startsWith(\"https:\") ? e.getPhotoUrl() : (urlPrefix + e.getPhotoUrl()))")
    PhotoImgModel photo(PlanPhotoEntity e, String urlPrefix);


    @Mapping(target = "fullUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    FileModel file(PlanAttachmentEntity e, String urlPrefix);


    @Mapping(target = "id", source = "deviceCheckLedgerId")
    DeviceCheckLedgerEntity deviceCheckLedger(DeviceCheckLedgerRequest t);

    @Mapping(target = "deviceCheckLedgerId", source = "id")
    @Mapping(target = "borrowerList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getBorrower()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    DeviceCheckLedgerResponse deviceCheckLedgerList(DeviceCheckLedgerEntity t);

    List<DeviceCheckLedgerResponse> deviceCheckLedgerList(List<DeviceCheckLedgerEntity> list);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getPhotoCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getPhotoCompressUrl().startsWith(\"http:\") || e.getPhotoCompressUrl().startsWith(\"https:\") ? e.getPhotoCompressUrl() : (urlPrefix + e.getPhotoCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getPhotoUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getPhotoUrl().startsWith(\"http:\") || e.getPhotoUrl().startsWith(\"https:\") ? e.getPhotoUrl() : (urlPrefix + e.getPhotoUrl()))")
    PhotoImgModel photo(DeviceCheckLedgerPhotoEntity e, String urlPrefix);

    @Mapping(target = "id", source = "computerId")
    @Mapping(target = "name", source = "computerName")
    ComputerEntity computer(ComputerRequest request);

    @Mapping(target = "computerName", source = "name")
    @Mapping(target = "computerId", source = "id")
    ComputerResponse computer(ComputerEntity d);

    List<ComputerResponse> computerList(List<ComputerEntity> list);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getPhotoCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getPhotoCompressUrl().startsWith(\"http:\") || e.getPhotoCompressUrl().startsWith(\"https:\") ? e.getPhotoCompressUrl() : (urlPrefix + e.getPhotoCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getPhotoUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getPhotoUrl().startsWith(\"http:\") || e.getPhotoUrl().startsWith(\"https:\") ? e.getPhotoUrl() : (urlPrefix + e.getPhotoUrl()))")
    PhotoImgModel photo(ComputerPhotoEntity e, String urlPrefix);

    @Mapping(target = "signInHistoryId", source = "id")
    @Mapping(target = "successFormat", expression = "java(Boolean.TRUE.equals(t.getSuccess()) ? \"Yes\" : \"No\")")
    MpSignInHistoryResponse signInHistory(MpSignInHistoryEntity t);

    List<MpSignInHistoryResponse> signInHistoryList(List<MpSignInHistoryEntity> list);

    @Mapping(target = "eventId", source = "eventId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "0")
    @Mapping(target = "url", source = "t.photoUrl")
    @Mapping(target = "compressUrl", source = "t.photoCompressUrl")
    EventAttachmentEntity eventAttachment(String eventId, String attachmentCategory, PhotoImgModel t);

    @Mapping(target = "eventId", source = "eventId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "1")
    @Mapping(target = "url", source = "t.url")
    @Mapping(target = "fileId", source = "t.fileId")
    @Mapping(target = "filename", source = "t.filename")
    @Mapping(target = "compressUrl", source = "t.url")
    EventAttachmentEntity eventAttachment(String eventId, String attachmentCategory, FileModel t);

    @Mapping(target = "id", source = "eventId")
    EventEntity event(EventRequest t);

    List<EventResponse> eventList(List<EventEntity> l);

    @Mapping(target = "eventId", source = "id")
    EventResponse event(EventEntity t);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getCompressUrl().startsWith(\"http:\") || e.getCompressUrl().startsWith(\"https:\") ? e.getCompressUrl() : (urlPrefix + e.getCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    PhotoImgModel eventPhoto(EventAttachmentEntity e, String urlPrefix);

    @Mapping(target = "fullUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    FileModel eventFile(EventAttachmentEntity e, String urlPrefix);

    @org.mapstruct.Mapping(target = "qualityId", source = "qualityId")
    @org.mapstruct.Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @org.mapstruct.Mapping(target = "attachmentType", constant = "0")
    @org.mapstruct.Mapping(target = "url", source = "t.photoUrl")
    @org.mapstruct.Mapping(target = "compressUrl", source = "t.photoCompressUrl")
    QualityAttachmentEntity qualityAttachment(String qualityId, String attachmentCategory, PhotoImgModel t);

    @org.mapstruct.Mapping(target = "qualityId", source = "qualityId")
    @org.mapstruct.Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @org.mapstruct.Mapping(target = "attachmentType", constant = "1")
    @org.mapstruct.Mapping(target = "url", source = "t.url")
    @org.mapstruct.Mapping(target = "fileId", source = "t.fileId")
    @org.mapstruct.Mapping(target = "filename", source = "t.filename")
    @org.mapstruct.Mapping(target = "compressUrl", source = "t.url")
    QualityAttachmentEntity qualityAttachment(String qualityId, String attachmentCategory, FileModel t);

    @org.mapstruct.Mapping(target = "id", source = "qualityId")
    QualityEntity quality(QualityRequest t);

    List<QualityResponse> qualityList(List<QualityEntity> l);

    @org.mapstruct.Mapping(target = "qualityId", source = "id")
    QualityResponse quality(QualityEntity t);

    @org.mapstruct.Mapping(target = "photoCompressUrl", expression = "java(e.getCompressUrl())")
    @org.mapstruct.Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getCompressUrl().startsWith(\"http:\") || e.getCompressUrl().startsWith(\"https:\") ? e.getCompressUrl() : (urlPrefix + e.getCompressUrl()))")
    @org.mapstruct.Mapping(target = "photoUrl", expression = "java(e.getUrl())")
    @org.mapstruct.Mapping(target = "fullPhotoUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    PhotoImgModel qualityPhoto(QualityAttachmentEntity e, String urlPrefix);

    @Mapping(target = "fullUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    FileModel qualityFile(QualityAttachmentEntity e, String urlPrefix);

    @org.mapstruct.Mapping(target = "crashId", source = "crashId")
    @org.mapstruct.Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @org.mapstruct.Mapping(target = "attachmentType", constant = "0")
    @org.mapstruct.Mapping(target = "url", source = "t.photoUrl")
    @org.mapstruct.Mapping(target = "compressUrl", source = "t.photoCompressUrl")
    CrashAttachmentEntity crashAttachment(String crashId, String attachmentCategory, PhotoImgModel t);

    @org.mapstruct.Mapping(target = "crashId", source = "crashId")
    @org.mapstruct.Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @org.mapstruct.Mapping(target = "attachmentType", constant = "1")
    @org.mapstruct.Mapping(target = "url", source = "t.url")
    @org.mapstruct.Mapping(target = "fileId", source = "t.fileId")
    @org.mapstruct.Mapping(target = "filename", source = "t.filename")
    @org.mapstruct.Mapping(target = "compressUrl", source = "t.url")
    CrashAttachmentEntity crashAttachment(String crashId, String attachmentCategory, FileModel t);

    @org.mapstruct.Mapping(target = "id", source = "crashId")
    CrashEntity crash(CrashRequest t);

    List<CrashResponse> crashList(List<CrashEntity> l);

    @org.mapstruct.Mapping(target = "crashId", source = "id")
    @Mapping(target = "userIdList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getUserId()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    CrashResponse crash(CrashEntity t);

    @org.mapstruct.Mapping(target = "photoCompressUrl", expression = "java(e.getCompressUrl())")
    @org.mapstruct.Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getCompressUrl().startsWith(\"http:\") || e.getCompressUrl().startsWith(\"https:\") ? e.getCompressUrl() : (urlPrefix + e.getCompressUrl()))")
    @org.mapstruct.Mapping(target = "photoUrl", expression = "java(e.getUrl())")
    @org.mapstruct.Mapping(target = "fullPhotoUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    PhotoImgModel crashPhoto(CrashAttachmentEntity e, String urlPrefix);

    @Mapping(target = "fullUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    FileModel crashFile(CrashAttachmentEntity e, String urlPrefix);


    @org.mapstruct.Mapping(target = "troubleId", source = "troubleId")
    @org.mapstruct.Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @org.mapstruct.Mapping(target = "attachmentType", constant = "0")
    @org.mapstruct.Mapping(target = "url", source = "t.photoUrl")
    @org.mapstruct.Mapping(target = "compressUrl", source = "t.photoCompressUrl")
    TroubleAttachmentEntity troubleAttachment(String troubleId, String attachmentCategory, PhotoImgModel t);

    @org.mapstruct.Mapping(target = "troubleId", source = "troubleId")
    @org.mapstruct.Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @org.mapstruct.Mapping(target = "attachmentType", constant = "1")
    @org.mapstruct.Mapping(target = "url", source = "t.url")
    @org.mapstruct.Mapping(target = "fileId", source = "t.fileId")
    @org.mapstruct.Mapping(target = "filename", source = "t.filename")
    @org.mapstruct.Mapping(target = "compressUrl", source = "t.url")
    TroubleAttachmentEntity troubleAttachment(String troubleId, String attachmentCategory, FileModel t);

    @org.mapstruct.Mapping(target = "id", source = "troubleId")
    TroubleEntity trouble(TroubleRequest t);

    List<TroubleResponse> troubleList(List<TroubleEntity> l);

    @org.mapstruct.Mapping(target = "troubleId", source = "id")
    TroubleResponse trouble(TroubleEntity t);

    @org.mapstruct.Mapping(target = "photoCompressUrl", expression = "java(e.getCompressUrl())")
    @org.mapstruct.Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getCompressUrl().startsWith(\"http:\") || e.getCompressUrl().startsWith(\"https:\") ? e.getCompressUrl() : (urlPrefix + e.getCompressUrl()))")
    @org.mapstruct.Mapping(target = "photoUrl", expression = "java(e.getUrl())")
    @org.mapstruct.Mapping(target = "fullPhotoUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    PhotoImgModel troublePhoto(TroubleAttachmentEntity e, String urlPrefix);

    @Mapping(target = "fullUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    FileModel troubleFile(TroubleAttachmentEntity e, String urlPrefix);

    @Mapping(target = "improveId", source = "improveId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "0")
    @Mapping(target = "url", source = "t.photoUrl")
    @Mapping(target = "compressUrl", source = "t.photoCompressUrl")
    ImproveAttachmentEntity improveAttachment(String improveId, String attachmentCategory, PhotoImgModel t);

    @Mapping(target = "improveId", source = "improveId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "1")
    @Mapping(target = "url", source = "t.url")
    @Mapping(target = "fileId", source = "t.fileId")
    @Mapping(target = "filename", source = "t.filename")
    @Mapping(target = "compressUrl", source = "t.url")
    ImproveAttachmentEntity improveAttachment(String improveId, String attachmentCategory, FileModel t);

    @Mapping(target = "id", source = "improveId")
    ImproveEntity improve(ImproveRequest t);

    List<ImproveResponse> improveList(List<ImproveEntity> l);

    @Mapping(target = "improveId", source = "id")
    @Mapping(target = "userIdList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getUserId()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    ImproveResponse improve(ImproveEntity t);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getCompressUrl().startsWith(\"http:\") || e.getCompressUrl().startsWith(\"https:\") ? e.getCompressUrl() : (urlPrefix + e.getCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    PhotoImgModel improvePhoto(ImproveAttachmentEntity e, String urlPrefix);

    @Mapping(target = "fullUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    FileModel improveFile(ImproveAttachmentEntity e, String urlPrefix);


    @Mapping(target = "equipmentId", source = "equipmentId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "0")
    @Mapping(target = "url", source = "t.photoUrl")
    @Mapping(target = "compressUrl", source = "t.photoCompressUrl")
    EquipmentAttachmentEntity equipmentAttachment(String equipmentId, String attachmentCategory, PhotoImgModel t);

    @Mapping(target = "equipmentId", source = "equipmentId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "1")
    @Mapping(target = "url", source = "t.url")
    @Mapping(target = "fileId", source = "t.fileId")
    @Mapping(target = "filename", source = "t.filename")
    @Mapping(target = "compressUrl", source = "t.url")
    EquipmentAttachmentEntity equipmentAttachment(String equipmentId, String attachmentCategory, FileModel t);

    @Mapping(target = "id", source = "equipmentId")
    EquipmentEntity equipment(EquipmentRequest t);

    List<EquipmentResponse> equipmentList(List<EquipmentEntity> l);

    @Mapping(target = "equipmentId", source = "id")
    @Mapping(target = "chargeUserList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getChargeUser()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "useUserList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getUseUser()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    EquipmentResponse equipment(EquipmentEntity t);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getCompressUrl().startsWith(\"http:\") || e.getCompressUrl().startsWith(\"https:\") ? e.getCompressUrl() : (urlPrefix + e.getCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    PhotoImgModel equipmentPhoto(EquipmentAttachmentEntity e, String urlPrefix);

    @Mapping(target = "fullUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    FileModel equipmentFile(EquipmentAttachmentEntity e, String urlPrefix);

    @Mapping(target = "maintainId", source = "maintainId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "0")
    @Mapping(target = "url", source = "t.photoUrl")
    @Mapping(target = "compressUrl", source = "t.photoCompressUrl")
    MaintainAttachmentEntity maintainAttachment(String maintainId, String attachmentCategory, PhotoImgModel t);

    @Mapping(target = "maintainId", source = "maintainId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "1")
    @Mapping(target = "url", source = "t.url")
    @Mapping(target = "fileId", source = "t.fileId")
    @Mapping(target = "filename", source = "t.filename")
    @Mapping(target = "compressUrl", source = "t.url")
    MaintainAttachmentEntity maintainAttachment(String maintainId, String attachmentCategory, FileModel t);

    @Mapping(target = "id", source = "maintainId")
    MaintainEntity maintain(MaintainRequest t);

    List<MaintainResponse> maintainList(List<MaintainEntity> l);

    @Mapping(target = "maintainId", source = "id")
    @Mapping(target = "partyUserList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getPartyUser()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "brokenReasonList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getBrokenReason()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    MaintainResponse maintain(MaintainEntity t);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getCompressUrl().startsWith(\"http:\") || e.getCompressUrl().startsWith(\"https:\") ? e.getCompressUrl() : (urlPrefix + e.getCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    PhotoImgModel maintainPhoto(MaintainAttachmentEntity e, String urlPrefix);

    @Mapping(target = "fullUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    FileModel maintainFile(MaintainAttachmentEntity e, String urlPrefix);

    @Mapping(target = "id", source = "vocationId")
    @Mapping(target = "date", expression = "java(com.lead.fund.base.common.util.DateUtil.day(t.getDate()))")
    @Mapping(target = "startDate", expression = "java(com.lead.fund.base.common.util.DateUtil.day(t.getStartDate()))")
    @Mapping(target = "endDate", expression = "java(com.lead.fund.base.common.util.DateUtil.day(t.getEndDate()))")
    VocationEntity vocation(VocationRequest t);

    List<VocationResponse> vocationList(List<VocationEntity> l);

    @Mapping(target = "vocationId", source = "id")
    @Mapping(target = "countFormat", expression = "java(com.lead.fund.base.common.util.NumberUtil.format(t.getCount()))")
    VocationResponse vocation(VocationEntity t);

    @Mapping(target = "templateId", source = "templateId")
    @Mapping(target = "photoType", source = "photoType")
    @Mapping(target = "photoUrl", source = "t.photoUrl")
    @Mapping(target = "photoCompressUrl", source = "t.photoCompressUrl")
    TemplatePhotoEntity templatePhoto(String templateId, String photoType, PhotoImgModel t);


    @Mapping(target = "photoCompressUrl", expression = "java(e.getPhotoCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getPhotoCompressUrl().startsWith(\"http:\") || e.getPhotoCompressUrl().startsWith(\"https:\") ? e.getPhotoCompressUrl() : (urlPrefix + e.getPhotoCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getPhotoUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getPhotoUrl().startsWith(\"http:\") || e.getPhotoUrl().startsWith(\"https:\") ? e.getPhotoUrl() : (urlPrefix + e.getPhotoUrl()))")
    PhotoImgModel templatePhoto(TemplatePhotoEntity e, String urlPrefix);

    @Mapping(target = "id", source = "templateId")
    TemplateEntity template(TemplateRequest t);

    @Mapping(target = "templateId", source = "id")
    TemplateResponse template(TemplateEntity t);

    List<TemplateResponse> templateList(List<TemplateEntity> t);
}
