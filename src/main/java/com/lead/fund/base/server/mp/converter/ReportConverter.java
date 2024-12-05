package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.response.ReportResponse;
import com.lead.fund.base.server.mp.response.ReportSummaryAccountResponse;
import com.lead.fund.base.server.mp.response.ReportSummaryDeviceResponse;
import java.io.Serializable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * ReportConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface ReportConverter extends Serializable {

    ReportConverter REPORT_INSTANCE = Mappers.getMapper(ReportConverter.class);

    @Mapping(target = "reportDateList", expression = "java(cn.hutool.core.collection.CollUtil.newHashSet(t.getReportDate()))")
    @Mapping(target = "userIdFormat", source = "userFormat")
    @Mapping(target = "sumDeviceCompletePercent", source = "deviceCompletePercent")
    ReportSummaryAccountResponse summaryAccount(ReportResponse t);

    @Mapping(target = "reportDateList", expression = "java(cn.hutool.core.collection.CollUtil.newHashSet(t.getReportDate()))")
    @Mapping(target = "userIdList", expression = "java(cn.hutool.core.collection.CollUtil.newHashSet(t.getUserId()))")
    @Mapping(target = "userIdFormat", source = "userFormat")
    @Mapping(target = "sumDeviceCompletePercent", source = "deviceCompletePercent")
    @Mapping(target = "sumDeviceUsePercent", source = "deviceUsePercent")
    ReportSummaryDeviceResponse summaryDevice(ReportResponse t);
}
