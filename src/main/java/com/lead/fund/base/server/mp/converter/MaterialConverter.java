package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.ExamineEntity;
import com.lead.fund.base.server.mp.entity.douson.MaterialDetailEntity;
import com.lead.fund.base.server.mp.entity.douson.MaterialEntity;
import com.lead.fund.base.server.mp.entity.douson.TaskEntity;
import com.lead.fund.base.server.mp.request.MaterialRequest;
import com.lead.fund.base.server.mp.response.MaterialResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MaterialConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface MaterialConverter extends Serializable {

    MaterialConverter MATERIAL_INSTANCE = Mappers.getMapper(MaterialConverter.class);

    @Mapping(target = "id", source = "materialId")
    @Mapping(target = "orderCount", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(request.getOrderCount()))")
    @Mapping(target = "materialCount", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(request.getMaterialCount()))")
    @Mapping(target = "productionCount", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(request.getProductionCount()))")
    MaterialEntity material(MaterialRequest request);

    @Mapping(target = "id", source = "materialDetailId")
    MaterialDetailEntity materialDetail(MaterialRequest request);

    @Mapping(target = "materialId", source = "id")
    @Mapping(target = "generateTaskFormat", expression = "java(Boolean.TRUE.equals(d.getGenerateTask()) ? \"Yes\" : \"No\")")
    @Mapping(target = "generateExamineFormat", expression = "java(Boolean.TRUE.equals(d.getGenerateExamine()) ? \"Yes\" : \"No\")")
    MaterialResponse material(MaterialEntity d);

    List<MaterialResponse> materialList(List<MaterialEntity> list);

    @Mapping(target = "roughcastExpireDate", source = "productionDate")
    @Mapping(target = "materialCount", source = "sumMaterialCount")
    TaskEntity generateTask(MaterialEntity e);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "modifyTime", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "modifier", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "orderQuantity", source = "orderCount")
    @Mapping(target = "orderTotalQuantity", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(t.getMaterialCount()))")
    ExamineEntity examine(MaterialEntity t);
}
