package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.MaterialDetailEntity;
import com.lead.fund.base.server.mp.entity.douson.MaterialEntity;
import com.lead.fund.base.server.mp.request.MaterialRequest;
import com.lead.fund.base.server.mp.response.MaterialResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
    MaterialResponse material(MaterialEntity d);

    List<MaterialResponse> materialList(List<MaterialEntity> list);

}