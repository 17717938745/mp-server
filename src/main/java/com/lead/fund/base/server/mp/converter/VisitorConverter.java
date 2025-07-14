package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.VisitorEntity;
import com.lead.fund.base.server.mp.request.VisitorRequest;
import com.lead.fund.base.server.mp.response.VisitorResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * VisitorConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface VisitorConverter extends Serializable {

    VisitorConverter VISITOR_INSTANCE = Mappers.getMapper(VisitorConverter.class);

    @Mapping(target = "id", source = "visitorId")
    @Mapping(target = "applyDate", expression = "java(com.lead.fund.base.common.util.StrUtil.wrapperIfNotBlank(request.getApplyDate(), com.lead.fund.base.common.util.DateUtil::day))")
    VisitorEntity visitor(VisitorRequest request);

    @Mapping(target = "visitorId", source = "id")
    @Mapping(target = "applyDate", expression = "java(com.lead.fund.base.common.util.StrUtil.wrapperIfNotBlank(d.getApplyDate(), com.lead.fund.base.common.util.DateUtil::day))")
    @Mapping(target = "expectedVisitTime", expression = "java(com.lead.fund.base.common.util.StrUtil.wrapperIfNotBlank(d.getExpectedVisitTime(), com.lead.fund.base.common.util.DateUtil::tradeDateTime))")
    @Mapping(target = "expectedEndTime", expression = "java(com.lead.fund.base.common.util.StrUtil.wrapperIfNotBlank(d.getExpectedEndTime(), com.lead.fund.base.common.util.DateUtil::tradeDateTime))")
    @Mapping(target = "visitorFactoryDate", expression = "java(com.lead.fund.base.common.util.StrUtil.wrapperIfNotBlank(d.getVisitorFactoryDate(), com.lead.fund.base.common.util.DateUtil::tradeDateTime))")
    @Mapping(target = "visitContentFormat", source = "visitContent")
    VisitorResponse visitor(VisitorEntity d);

    List<VisitorResponse> visitorList(List<VisitorEntity> list);

}
