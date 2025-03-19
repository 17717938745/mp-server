package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.InventoryEntity;
import com.lead.fund.base.server.mp.request.InventoryRequest;
import com.lead.fund.base.server.mp.response.InventoryResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * InventoryConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface InventoryConverter extends Serializable {

    InventoryConverter INVENTORY_INSTANCE = Mappers.getMapper(InventoryConverter.class);

    @Mapping(target = "id", source = "inventoryId")
    @Mapping(target = "remainingQuantity", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(request.getInventoryCount()).subtract(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(request.getMaterialCount())))")
    InventoryEntity inventory(InventoryRequest request);

    @Mapping(target = "inventoryId", source = "id")
    InventoryResponse inventory(InventoryEntity d);

    List<InventoryResponse> inventoryList(List<InventoryEntity> list);

}
