package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * MaterialEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_MATERIAL")
@NoArgsConstructor
@ClassFunction("生产工单")
@Accessors(chain = true)
public class MaterialEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096001061L;
    /**
     * 客户简称
     */
    @FieldRemark(value = "客户简称")
    @Size(max = 256, message = "客户简称长度不合法")
    private String customerShortName;
    /**
     * 客户订单号
     */
    @FieldRemark(value = "客户订单号")
    @Size(max = 64, message = "客户订单号长度不合法")
    private String customerOrderNo;
    /**
     * 客户项次号
     */
    @FieldRemark(value = "客户项次号")
    @Size(max = 32, message = "客户项次号长度不合法")
    private String customerProjectSequence;
    /**
     * 销售订单号
     */
    @FieldRemark(value = "销售订单号", indexType = "NORMAL", indexGroup = "0")
    @Size(max = 64, message = "销售订单号长度不合法")
    private String saleOrderNo;
    /**
     * 订单项目号
     */
    @FieldRemark(value = "订单项目号", indexType = "NORMAL", indexGroup = "0")
    @Size(max = 32, message = "订单项目号长度不合法")
    private String orderProjectNo;
    /**
     * 物料号
     */
    @FieldRemark(value = "物料号")
    @Size(max = 64, message = "物料号长度不合法")
    private String materialNo;
    /**
     * 加工物料描述
     */
    @FieldRemark(value = "加工物料描述")
    @Size(max = 256, message = "加工物料描述长度不合法")
    private String improveMaterialDescribe;
    /**
     * 图号
     */
    @FieldRemark(value = "图号")
    @Size(max = 128, message = "图号长度不合法")
    private String designNumber;
    /**
     * 订单数量
     */
    @FieldRemark(value = "订单数量")
    @Size(max = 16, message = "订单数量长度不合法")
    private BigDecimal orderCount;
    /**
     * 投产日期
     */
    @FieldRemark(value = "投产日期", indexType = "NORMAL", indexGroup = "0")
    @Size(max = 32, message = "投产日期长度不合法")
    private String productionDate;
    /**
     * 承诺交期
     */
    @FieldRemark(value = "承诺交期")
    @Size(max = 32, message = "承诺交期长度不合法")
    private String promiseDoneDate;
    /**
     * 毛坯物料号
     */
    @FieldRemark(value = "毛坯物料号")
    @Size(max = 128, message = "毛坯物料号长度不合法")
    private String blankMaterialNo;
    /**
     * 毛坯物料描述
     */
    @FieldRemark(value = "毛坯物料描述")
    @Size(max = 256, message = "毛坯物料描述长度不合法")
    private String blankMaterialDescribe;
    /**
     * 毛坯图号
     */
    @FieldRemark(value = "毛坯图号")
    @Size(max = 64, message = "毛坯图号长度不合法")
    private String roughcastDesignNumber;
    /**
     * 领料数量
     */
    @FieldRemark(value = "领料数量")
    @Size(max = 16, message = "领料数量长度不合法")
    private BigDecimal materialCount;
    @FieldRemark(value = "累计领料数量")
    @Size(max = 16, message = "累计领料数量长度不合法")
    private BigDecimal sumMaterialCount;
    /**
     * 炉号
     */
    @FieldRemark(value = "炉号")
    @Size(max = 64, message = "炉号长度不合法")
    private String stoveNo;
    /**
     * 热批号
     */
    @FieldRemark(value = "热批号")
    @Size(max = 64, message = "热批号长度不合法")
    private String hotBatchNo;
    /**
     * 序列号
     */
    @FieldRemark(value = "序列号")
    @Size(max = 256, message = "序列号长度不合法")
    private String serialNo;
    /**
     * 欠交数量
     */
    @FieldRemark(value = "欠交数量")
    @Size(max = 16, message = "欠交数量长度不合法")
    private BigDecimal surplusCount;
    /**
     * NDE
     */
    @FieldRemark(value = "NDE")
    @Size(max = 256, message = "NDE长度不合法")
    private String nde;
    /**
     * 装配
     */
    @FieldRemark(value = "装配")
    @Size(max = 256, message = "装配长度不合法")
    private String assemble;
    /**
     * 试压
     */
    @FieldRemark(value = "试压")
    @Size(max = 256, message = "试压长度不合法")
    private String testPress;
    /**
     * 表面处理
     */
    @FieldRemark(value = "表面处理")
    @Size(max = 256, message = "表面处理长度不合法")
    private String surfaceTreatment;
    /**
     * 负责单位
     */
    @FieldRemark(value = "负责单位")
    @Size(max = 256, message = "负责单位长度不合法")
    private String chargeCompany;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 512, message = "备注长度不合法")
    private String description;
    /**
     * 排产数量
     */
    @FieldRemark(value = "排产数量")
    @Size(max = 16, message = "排产数量长度不合法")
    private BigDecimal productionCount;
    /**
     * 领料数量-排产数量
     */
    @FieldRemark(value = "领料数量-排产数量")
    @Size(max = 16, message = "领料数量-排产数量长度不合法")
    private BigDecimal remainCount;
    /**
     * 排产日期
     */
    @FieldRemark(value = "排产日期")
    @Size(max = 32, message = "排产日期长度不合法")
    private String arrangeProductionDate;
    /**
     * 领料单号
     */
    @FieldRemark(value = "领料单号")
    @Size(max = 64, message = "领料单号长度不合法")
    private String materialOrderNo;
    private BigDecimal materialPrintCount;
    /**
     * 报检单号
     */
    @FieldRemark(value = "报检单号")
    @Size(max = 64, message = "报检单号长度不合法")
    private String checkOrderNo;
    private BigDecimal checkPrintCount;
    /**
     * 是否生成计划
     */
    @FieldRemark(value = "是否生成计划")
    @Size(max = 2, message = "是否生成计划长度不合法")
    private Boolean generateTask;
    /**
     * 是否生成订单检验记录
     */
    @FieldRemark(value = "是否生成订单检验记录")
    @Size(max = 2, message = "是否生成订单检验记录")
    private Boolean generateExamine;
}
