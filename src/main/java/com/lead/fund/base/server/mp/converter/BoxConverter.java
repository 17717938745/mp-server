package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.AssemblyAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.AssemblyEntity;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagEntity;
import com.lead.fund.base.server.mp.entity.douson.BoxFlagPhotoEntity;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.request.AssemblyRequest;
import com.lead.fund.base.server.mp.request.BoxFlagRequest;
import com.lead.fund.base.server.mp.response.AssemblyResponse;
import com.lead.fund.base.server.mp.response.AssemblySummaryResponse;
import com.lead.fund.base.server.mp.response.BoxFlagResponse;
import com.lead.fund.base.server.mp.response.BoxSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;
import java.util.List;

/**
 * BoxConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface BoxConverter extends Serializable {

    BoxConverter BOX_INSTANCE = Mappers.getMapper(BoxConverter.class);


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

//    @Mapping(target = "assemblyCompleteCount", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getAssemblyCompleteCount()))")
//    @Mapping(target = "deliveryDateFormat", expression = "java(com.lead.fund.base.common.util.DateUtil.day(t.getDeliveryDate()))")
    BoxSummaryResponse boxSummary(BoxFlagEntity t);

    List<BoxSummaryResponse> boxSummaryList(List<BoxFlagEntity> list);
}
