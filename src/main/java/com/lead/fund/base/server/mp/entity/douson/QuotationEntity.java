package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.Bool;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * QuotationEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_QUOTATION")
@NoArgsConstructor
@ClassFunction("加工报价")
@Accessors(chain = true)
public class QuotationEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096010700L;
    /**
     * 客户
     */
    @FieldRemark(value = "客户")
    @Size(max = 64, message = "客户长度不合法")
    private String customer;
    /**
     * 图号
     */
    @FieldRemark(value = "图号")
    @Size(max = 64, message = "图号长度不合法")
    private String designNumber;
    /**
     * 名称
     */
    @FieldRemark(value = "名称")
    @Size(max = 128, message = "名称长度不合法")
    private String name;
    /**
     * 材质
     */
    @FieldRemark(value = "材质")
    @Size(max = 32, message = "材质长度不合法")
    private String materialQuality;
    /**
     * 数量
     */
    @FieldRemark(value = "数量")
    private BigDecimal count;
    /**
     * 报价日期
     */
    @FieldRemark(value = "报价日期")
    @Size(max = 32, message = "报价日期长度不合法")
    private String quotationDate;
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
    private BigDecimal remarks;
    /**
     * 报价人
     */
    @FieldRemark(value = "报价人")
    @Size(max = 64, message = "报价人长度不合法")
    private String bidder;
    /**
     * 是否成功接单
     */
    @FieldRemark(value = "是否成功接单")
    private Boolean acceptOrder;
}
