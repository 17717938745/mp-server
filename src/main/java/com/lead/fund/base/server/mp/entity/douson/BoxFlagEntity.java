package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * BoxFlagEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_BOX_FLAG")
@NoArgsConstructor
@ClassFunction("装箱标识卡")
@Accessors(chain = true)
public class BoxFlagEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096726178L;
    /**
     * 日期
     */
    @FieldRemark(value = "日期")
    @Size(max = 32, message = "日期")
    private String date;
    /**
     * 客户简称
     */
    @FieldRemark(value = "客户简称")
    @Size(max = 64, message = "客户简称")
    private String customerShortName;
    /**
     * 采购订单编号
     */
    @FieldRemark(value = "采购订单编号")
    @Size(max = 256, message = "采购订单编号")
    private String purchaseOrderNo;
    /**
     * PO项目
     */
    @FieldRemark(value = "PO项目", unique = true, constraintGroup = "1", constraintType = "UNIQUE")
    @Size(max = 128, message = "PO项目")
    private String poProject;
    /**
     * 销售订单
     */
    @FieldRemark(value = "销售订单", unique = true, constraintGroup = "1", constraintType = "UNIQUE")
    @Size(max = 128, message = "销售订单")
    private String saleOrderNo;
    /**
     * 订单项目
     */
    @FieldRemark(value = "订单项目")
    @Size(max = 128, message = "订单项目")
    private String orderProject;
    /**
     * 物料号
     */
    @FieldRemark(value = "物料号")
    @Size(max = 256, message = "物料号长度不合法")
    private String materialNo;
    /**
     * 每箱数量
     */
    @FieldRemark(value = "每箱数量")
    private Integer eachBoxCount;
    /**
     * 箱号
     */
    @FieldRemark(value = "箱号", unique = true, constraintGroup = "1", constraintType = "UNIQUE")
    @Size(max = 256, message = "箱号")
    private Integer boxNumber;
    /**
     * 长
     */
    @FieldRemark(value = "长")
    private BigDecimal length;
    /**
     * 宽
     */
    @FieldRemark(value = "宽")
    private BigDecimal width;
    /**
     * 高
     */
    @FieldRemark(value = "高")
    private BigDecimal height;
    /**
     * 单件重量
     */
    @FieldRemark(value = "单件重量")
    private BigDecimal unitWeight;
    /**
     * 每箱重量
     */
    @FieldRemark(value = "每箱重量")
    private BigDecimal eachBoxWeight;
    /**
     * 单号
     */
    @FieldRemark(value = "单号", unique = true, constraintGroup = "0", constraintType = "UNIQUE")
    @Size(max = 256, message = "单号")
    private String orderNo;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 1024, message = "备注")
    private String remark;
    /**
     * 序号
     */
    @FieldRemark(value = "序号")
    @Size(max = 1024, message = "序号")
    private String serialNo;
    /**
     * 发送数量
     */
    @FieldRemark(value = "发送数量")
    private BigDecimal sendCount;
    /**
     * 发送日期
     */
    @FieldRemark(value = "发送日期")
    private String sendDate;
}
