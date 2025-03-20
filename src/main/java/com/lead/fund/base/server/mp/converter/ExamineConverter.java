package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.ExamineEntity;
import com.lead.fund.base.server.mp.request.ExamineRequest;
import com.lead.fund.base.server.mp.response.ExamineResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * ExamineConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface ExamineConverter extends Serializable {

    ExamineConverter EXAMINE_INSTANCE = Mappers.getMapper(ExamineConverter.class);

    @Mapping(target = "id", source = "examineId")
    @Mapping(target = "inspectionCompletedQuantity", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(request.getInspectionCompletedQuantity()))")
    ExamineEntity examine(ExamineRequest request);

    @Mapping(target = "examineId", source = "id")
    ExamineResponse examine(ExamineEntity d);

    List<ExamineResponse> examineList(List<ExamineEntity> list);

}
