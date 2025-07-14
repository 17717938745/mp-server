package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * QuotationItemEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_QUOTATION_ITEM")
@NoArgsConstructor
@ClassFunction("加工报价明细")
@Accessors(chain = true)
public class QuotationItemEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096010700L;
    /**
     * 报价ID
     */
    private String quotationId;
    /**
     * 加工工序
     */
    @FieldRemark(value = "加工工序")
    @Size(max = 32, message = "加工工序长度不合法")
    private String processProcedure;
    /**
     * 加工设备
     */
    @FieldRemark(value = "加工设备")
    @Size(max = 32, message = "加工设备长度不合法")
    private String processDevice;
    /**
     * 加工单价
     */
    @FieldRemark(value = "加工单价")
    private BigDecimal processUnitPrice;
    /**
     * 加工时间
     */
    @FieldRemark(value = "加工时间")
    @Size(max = 32, message = "加工时间长度不合法")
    private String processTime;
    /**
     * 汇总价格/元
     */
    @FieldRemark(value = "汇总价格/元")
    private BigDecimal summaryPrice;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 512, message = "备注长度不合法")
    private String remarks;
}
