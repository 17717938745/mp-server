package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.DressEntity;
import com.lead.fund.base.server.mp.request.DressRequest;
import com.lead.fund.base.server.mp.response.DressResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * DressConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface DressConverter extends Serializable {

    DressConverter DRESS_INSTANCE = Mappers.getMapper(DressConverter.class);

    @Mapping(target = "id", source = "dressId")
    @Mapping(target = "applyDate", expression = "java(com.lead.fund.base.common.util.DateUtil.day(request.getApplyDate()))")
    DressEntity dress(DressRequest request);

    @Mapping(target = "dressId", source = "id")
    @Mapping(target = "applyDate", expression = "java(com.lead.fund.base.common.util.DateUtil.day(d.getApplyDate()))")
    DressResponse dress(DressEntity d);

    List<DressResponse> dressList(List<DressEntity> list);

}
