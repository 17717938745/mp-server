package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.QuotationEntity;
import com.lead.fund.base.server.mp.entity.douson.QuotationItemEntity;
import com.lead.fund.base.server.mp.request.QuotationRequest;
import com.lead.fund.base.server.mp.response.QuotationResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * QuotationConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface QuotationConverter extends Serializable {

    QuotationConverter QUOTATION_INSTANCE = Mappers.getMapper(QuotationConverter.class);

    @Mapping(target = "id", source = "quotationId")
    @Mapping(target = "processProcedure", constant = "加工成本汇总/元")
    @Mapping(target = "processDevice", ignore = true)
    @Mapping(target = "processUnitPrice", ignore = true)
    @Mapping(target = "processTime", expression = "java(com.lead.fund.base.common.util.StrUtil.isBlank(request.getQuotationItemId()) ? com.lead.fund.base.common.util.NumberUtil.defaultDecimal(request.getProcessTime(), new java.math.BigDecimal(\"1\")) : null)")
    QuotationEntity quotation(QuotationRequest request);

    @Mapping(target = "quotationId", source = "id")
    @Mapping(target = "acceptOrderFormat", expression = "java(Boolean.TRUE.equals(d.getAcceptOrder()) ? \"Yes\" : \"No\")")
    @Mapping(target = "processProcedureFormat", source = "d.processProcedure")
    QuotationResponse quotation(QuotationEntity d);

    List<QuotationResponse> quotationList(List<QuotationEntity> list);

    @Mapping(target = "quotationId", source = "quotationId")
    @Mapping(target = "id", source = "quotationItemId")
    QuotationItemEntity quotationItem(QuotationRequest request);

    @Mapping(target = "quotationId", source = "t.quotationId")
    @Mapping(target = "id", source = "tt.id")
    @Mapping(target = "createTime", source = "tt.createTime")
    @Mapping(target = "modifyTime", source = "tt.modifyTime")
    @Mapping(target = "creator", source = "tt.creator")
    @Mapping(target = "modifier", source = "tt.modifier")
    @Mapping(target = "state", source = "tt.state")
    @Mapping(target = "processProcedure", source = "tt.processProcedure")
    @Mapping(target = "processDevice", source = "tt.processDevice")
    @Mapping(target = "processUnitPrice", source = "tt.processUnitPrice")
    @Mapping(target = "processTime", source = "tt.processTime")
    @Mapping(target = "summaryPrice", source = "tt.summaryPrice")
    @Mapping(target = "remarks", source = "tt.remarks")
    @Mapping(target = "quotationItemId", source = "tt.id")
    QuotationResponse quotation(QuotationResponse t, QuotationItemEntity tt);

    /*@Mapping(target = "index", source = "tt.index")
    @Mapping(target = "quotationId", source = "tt.quotationId")
    @Mapping(target = "quotationItemId", source = "tt.quotationItemId")
    @Mapping(target = "designNumberCount", source = "tt.designNumberCount")
    @Mapping(target = "designNumberList", source = "tt.designNumberList")
    @Mapping(target = "customer", source = "tt.customer")
    @Mapping(target = "name", source = "tt.name")
    @Mapping(target = "materialQuality", source = "tt.materialQuality")
    @Mapping(target = "designNumber", source = "tt.designNumber")
    @Mapping(target = "id", source = "tt.id")
    @Mapping(target = "createTime", source = "tt.createTime")
    @Mapping(target = "modifyTime", source = "tt.modifyTime")
    @Mapping(target = "creator", source = "tt.creator")
    @Mapping(target = "modifier", source = "tt.modifier")
    @Mapping(target = "state", source = "tt.state")
    @Mapping(target = "processProcedure", source = "tt.processProcedure")
    @Mapping(target = "processProcedureFormat", source = "tt.processProcedureFormat")
    @Mapping(target = "processDevice", source = "tt.processDevice")
    @Mapping(target = "processDeviceFormat", source = "tt.processDeviceFormat")
    @Mapping(target = "processUnitPrice", source = "tt.processUnitPrice")
    @Mapping(target = "processTime", source = "tt.processTime")
    @Mapping(target = "summaryPrice", source = "tt.summaryPrice")
    @Mapping(target = "remarks", source = "tt.remarks")
    @Mapping(target = "count", source = "tt.count")
    @Mapping(target = "bidder", source = "tt.bidder")
    @Mapping(target = "bidderFormat", source = "tt.bidderFormat")
    @Mapping(target = "acceptOrder", source = "tt.acceptOrder")
    @Mapping(target = "quotationDate", source = "tt.quotationDate")
    QuotationResponse summary(QuotationResponse tt, QuotationResponse tt1);*/

    @Mapping(target = "id", expression = "java(com.lead.fund.base.common.util.IdUtil.nextIdStr())")
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "modifyTime", ignore = true)
    @Mapping(target = "creator", source = "userId")
    @Mapping(target = "modifier", source = "userId")
    @Mapping(target = "state", source = "e.state")
    @Mapping(target = "quotationId", source = "e.id")
    @Mapping(target = "processProcedure", ignore = true)
    @Mapping(target = "processDevice", ignore = true)
    @Mapping(target = "processUnitPrice", ignore = true)
    @Mapping(target = "processTime", ignore = true)
    @Mapping(target = "summaryPrice", ignore = true)
    @Mapping(target = "remarks", ignore = true)
    QuotationItemEntity blankQuotationItem(QuotationEntity e, QuotationRequest request, String userId);
}
