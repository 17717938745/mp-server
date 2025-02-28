package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.SchedulingDetailEntity;
import com.lead.fund.base.server.mp.entity.douson.SchedulingEntity;
import com.lead.fund.base.server.mp.request.SchedulingDetailRequest;
import com.lead.fund.base.server.mp.request.SchedulingRequest;
import com.lead.fund.base.server.mp.response.SchedulingDetailResponse;
import com.lead.fund.base.server.mp.response.SchedulingResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * SchedulingConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface SchedulingConverter extends Serializable {

    SchedulingConverter SCHEDULING_INSTANCE = Mappers.getMapper(SchedulingConverter.class);

    @Mapping(target = "id", source = "schedulingId")
    SchedulingEntity scheduling(SchedulingRequest request);

    @Mapping(target = "schedulingId", source = "id")
    SchedulingResponse scheduling(SchedulingEntity t);

    List<SchedulingResponse> schedulingList(List<SchedulingEntity> list);

    @Mapping(target = "id", source = "schedulingDetailId")
    @Mapping(target = "scheduleDayTime", expression = "java(\",\" + request.getScheduleDayTimeList().stream().collect(java.util.stream.Collectors.joining(\",\")) + \",\")")
    @Mapping(target = "scheduleMiddle", expression = "java(\",\" + request.getScheduleMiddleList().stream().collect(java.util.stream.Collectors.joining(\",\")) + \",\")")
    @Mapping(target = "scheduleEvening", expression = "java(\",\" + request.getScheduleEveningList().stream().collect(java.util.stream.Collectors.joining(\",\")) + \",\")")
    @Mapping(target = "scheduleDayTime12", expression = "java(\",\" + request.getScheduleDayTime12List().stream().collect(java.util.stream.Collectors.joining(\",\")) + \",\")")
    @Mapping(target = "scheduleEvening12", expression = "java(\",\" + request.getScheduleEvening12List().stream().collect(java.util.stream.Collectors.joining(\",\")) + \",\")")
    @Mapping(target = "scheduleDayTimeTechnologyGroup", expression = "java(\",\" + request.getScheduleDayTimeTechnologyGroupList().stream().collect(java.util.stream.Collectors.joining(\",\")) + \",\")")
    @Mapping(target = "scheduleEveningTechnologyGroup", expression = "java(\",\" + request.getScheduleEveningTechnologyGroupList().stream().collect(java.util.stream.Collectors.joining(\",\")) + \",\")")
    SchedulingDetailEntity schedulingDetail(SchedulingDetailRequest request);

    @Mapping(target = "schedulingDetailId", source = "id")
    @Mapping(target = "scheduleDayTimeList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getScheduleDayTime()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "scheduleMiddleList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getScheduleMiddle()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "scheduleEveningList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getScheduleEvening()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "scheduleDayTime12List", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getScheduleDayTime12()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "scheduleEvening12List", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getScheduleEvening12()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "scheduleDayTimeTechnologyGroupList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getScheduleDayTimeTechnologyGroup()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "scheduleEveningTechnologyGroupList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(t.getScheduleEveningTechnologyGroup()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    SchedulingDetailResponse schedulingDetail(SchedulingDetailEntity t);

    List<SchedulingDetailResponse> schedulingDetailList(List<SchedulingDetailEntity> list);

}
