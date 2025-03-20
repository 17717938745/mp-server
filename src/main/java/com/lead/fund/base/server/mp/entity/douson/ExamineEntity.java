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
 * ExamineEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_EXAMINE")
@NoArgsConstructor
@ClassFunction("订单检验记录")
@Accessors(chain = true)
public class ExamineEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096010700L;
    /**
     * 生产工单ID
     */
    @FieldRemark(value = "报检单号", unique = true, indexGroup = "0")
    private String materialId;
    /**
     * 报检单号
     */
    @FieldRemark(value = "报检单号")
    @Size(max = 128, message = "报检单号长度不合法")
    private String checkOrderNo;
    /**
     * 报检单合计数量
     */
    @FieldRemark(value = "报检单合计数量")
    private BigDecimal orderTotalQuantity;

    /**
     * 标识/硬度备注
     */
    @FieldRemark(value = "标识/硬度备注")
    @Size(max = 512, message = "标识/硬度备注长度不合法")
    private String identificationHardnessRemark;
    /**
     * NDE/尺寸备注
     */
    @FieldRemark(value = "NDE/尺寸备注")
    @Size(max = 512, message = "NDE/尺寸备注长度不合法")
    private String ndeDimensionRemark;
    /**
     * 检验完成数量
     */
    @FieldRemark(value = "检验完成数量")
    private BigDecimal inspectionCompletedQuantity;
    /**
     * 客户简称
     */
    @FieldRemark(value = "客户简称")
    @Size(max = 64, message = "客户简称长度不合法")
    private String customerShortName;
    /**
     * 销售订单
     */
    @FieldRemark(value = "销售订单")
    @Size(max = 64, message = "销售订单长度不合法")
    private String saleOrderNo;
    /**
     * 订单项目号
     */
    @FieldRemark(value = "订单项目号")
    @Size(max = 64, message = "订单项目号长度不合法")
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
    @Size(max = 512, message = "加工物料描述长度不合法")
    private String improveMaterialDescribe;
    /**
     * 图号
     */
    @FieldRemark(value = "图号")
    @Size(max = 64, message = "图号长度不合法")
    private String designNumber;
    /**
     * 订单数量
     */
    @FieldRemark(value = "订单数量")
    private BigDecimal orderQuantity;
    /**
     * 承诺完成日期
     */
    @FieldRemark(value = "承诺完成日期")
    @Size(max = 32, message = "承诺完成日期长度不合法")
    private String promiseDoneDate;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 512, message = "备注长度不合法")
    private String description;
    /**
     * 标识人员
     */
    @FieldRemark(value = "标识人员")
    @Size(max = 64, message = "标识人员长度不合法")
    private String identificationPerson;
    /**
     * 标识日期
     */
    @FieldRemark(value = "标识日期")
    @Size(max = 32, message = "标识日期长度不合法")
    private String identificationDate;
    /**
     * 检验人员
     */
    @FieldRemark(value = "检验人员")
    @Size(max = 64, message = "检验人员长度不合法")
    private String inspectionPerson;
    /**
     * 检验完成日期
     */
    @FieldRemark(value = "检验完成日期")
    @Size(max = 32, message = "检验完成日期长度不合法")
    private String inspectionCompletedDate;
}
