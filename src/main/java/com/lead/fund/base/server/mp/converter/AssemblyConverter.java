package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.AssemblyAttachmentEntity;
import com.lead.fund.base.server.mp.entity.douson.AssemblyEntity;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.request.AssemblyRequest;
import com.lead.fund.base.server.mp.response.AssemblyResponse;
import com.lead.fund.base.server.mp.response.AssemblySummaryResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * AssemblyConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface AssemblyConverter extends Serializable {

    AssemblyConverter ASSEMBLY_INSTANCE = Mappers.getMapper(AssemblyConverter.class);

    @Mapping(target = "id", source = "assemblyId")
    AssemblyEntity assembly(AssemblyRequest request);

    @Mapping(target = "assemblyId", source = "id")
    @Mapping(target = "serialNumber", expression = "java(t.getPurchaseOrderNo() + \" \" + t.getPoProject() + \" \" + com.lead.fund.base.common.util.StrUtil.padPre(java.lang.String.valueOf(t.getSerialIndex()), 3, \"0\"))")
    @Mapping(target = "torqueNmFormat", expression = "java(null == t.getTorqueNm() ? \"--\" : (com.lead.fund.base.common.util.NumberUtil.formatIntTh(t.getTorqueNm()) + \"N.m\"))")
    @Mapping(target = "modifyTime", expression = "java(com.lead.fund.base.common.util.DateUtil.dateTime(t.getLastModifiedTime(), com.lead.fund.base.common.basic.cons.util.date.DateFormat.DOT_MILLIS.getCode(), \"--\"))")
    AssemblyResponse assembly(AssemblyEntity t);

    List<AssemblyResponse> assemblyList(List<AssemblyEntity> list);

    @Mapping(target = "assemblyId", source = "assemblyId")
    @Mapping(target = "attachmentCategory", source = "attachmentCategory")
    @Mapping(target = "attachmentType", constant = "0")
    @Mapping(target = "url", source = "t.photoUrl")
    @Mapping(target = "compressUrl", source = "t.photoCompressUrl")
    AssemblyAttachmentEntity attachment(String assemblyId, String attachmentCategory, PhotoImgModel t);

    @Mapping(target = "photoCompressUrl", expression = "java(e.getCompressUrl())")
    @Mapping(target = "fullPhotoCompressUrl", expression = "java(e.getCompressUrl().startsWith(\"http:\") || e.getCompressUrl().startsWith(\"https:\") ? e.getCompressUrl() : (urlPrefix + e.getCompressUrl()))")
    @Mapping(target = "photoUrl", expression = "java(e.getUrl())")
    @Mapping(target = "fullPhotoUrl", expression = "java(e.getUrl().startsWith(\"http:\") || e.getUrl().startsWith(\"https:\") ? e.getUrl() : (urlPrefix + e.getUrl()))")
    PhotoImgModel photo(AssemblyAttachmentEntity e, String urlPrefix);

    @Mapping(target = "completedQty", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getCompletedQty()))")
    @Mapping(target = "assemblyCompleteCount", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultInteger(t.getAssemblyCompleteCount()))")
    AssemblySummaryResponse assemblySummary(AssemblyEntity t);

    List<AssemblySummaryResponse> assemblySummaryList(List<AssemblyEntity> list);
}
