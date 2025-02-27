package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * AssemblyEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_ASSEMBLY")
@NoArgsConstructor
@ClassFunction("整机")
@Accessors(chain = true)
public class AssemblyEntity extends AbstractAdministrator {

    private static final long serialVersionUID = -2958798731405959800L;
    /**
     * 整机序列号
     */
    @FieldRemark(value = "整机序列号")
    @Size(max = 256, message = "整机序列号长度不合法")
    private String serialNumber;
    /**
     * 最大整机序列号索引，维度：采购订单编号、PO项目、销售订单、订单项目
     */
    @FieldRemark(value = "最大整机序列号索引", constraintGroup = "0")
    @Size(max = 8, message = "最大整机序列号索引长度不合法")
    private Integer maxSerialOrderIndex;
    /**
     * 最大整机序列号索引，维度：采购订单编号、PO项目
     */
    @FieldRemark(value = "最大整机序列号索引", constraintGroup = "0")
    @Size(max = 8, message = "最大整机序列号索引长度不合法")
    private Integer maxSerialIndex;
    /**
     * 整机序列号索引
     */
    @FieldRemark(value = "整机序列号索引", constraintGroup = "0")
    @Size(max = 8, message = "整机序列号索引长度不合法")
    private Integer serialIndex;
    /**
     * 采购订单编号
     */
    @FieldRemark(value = "采购订单编号", constraintGroup = "0")
    @Size(max = 64, message = "采购订单编号长度不合法")
    private String purchaseOrderNo;
    /**
     * PO项目
     */
    @FieldRemark(value = "PO项目", constraintGroup = "0")
    @Size(max = 32, message = "PO项目长度不合法")
    private String poProject;
    /**
     * 销售订单
     */
    @FieldRemark(value = "销售订单", constraintGroup = "0")
    @Size(max = 64, message = "销售订单长度不合法")
    private String saleOrderNo;
    /**
     * 订单项目
     */
    @FieldRemark(value = "订单项目", constraintGroup = "0")
    @Size(max = 32, message = "订单项目长度不合法")
    private String orderProject;
    /**
     * 物料号
     */
    @FieldRemark(value = "物料号")
    @Size(max = 64, message = "物料号长度不合法")
    private String materialNo;
    /**
     * 物料描述
     */
    @FieldRemark(value = "物料描述")
    @Size(max = 512, message = "物料描述长度不合法")
    private String materialDescription;
    /**
     * 图号
     */
    @FieldRemark(value = "图号")
    @Size(max = 64, message = "图号长度不合法")
    private String designNumber;
    /**
     * 承诺交期
     */
    @FieldRemark(value = "承诺交期")
    @Size(max = 32, message = "承诺交期长度不合法")
    private String deliveryDate;
    /**
     * 订单数量，维度：采购订单编号、PO项目、销售订单、订单项目
     */
    @FieldRemark(value = "订单数量")
    @Size(max = 8, message = "订单数量长度不合法")
    private Integer count;
    /**
     * 订单数量，维度：采购订单编号、PO项目、销售订单、订单项目
     */
    @FieldRemark(value = "订单数量")
    @Size(max = 8, message = "订单数量长度不合法")
    private Integer orderCount;
    /**
     * 完成数量
     */
    @FieldRemark(value = "完成数量")
    @Size(max = 8, message = "完成数量长度不合法")
    private Integer completedQty;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 512, message = "备注长度不合法")
    private String description;
    /**
     * 阀体
     */
    @FieldRemark(value = "阀体")
    @Size(max = 64, message = "阀体长度不合法")
    private String valveBody;
    /**
     * 阀盖/尾盖
     */
    @FieldRemark(value = "阀盖/尾盖")
    @Size(max = 64, message = "阀盖/尾盖长度不合法")
    private String valveCover;
    /**
     * 闸板
     */
    @FieldRemark(value = "闸板")
    @Size(max = 64, message = "闸板长度不合法")
    private String gate;
    /**
     * 阀座/阀瓣
     */
    @FieldRemark(value = "阀座/阀瓣")
    @Size(max = 64, message = "阀座/阀瓣长度不合法")
    private String valveSeat;
    /**
     * 阀座/阀瓣照片
     */
    @FieldRemark(value = "阀座/阀瓣照片")
    @Size(max = 256, message = "阀座/阀瓣照片长度不合法")
    private String valveSeatPhoto;
    /**
     * 阀杆/尾杆
     */
    @FieldRemark(value = "阀杆/尾杆")
    @Size(max = 256, message = "阀杆/尾杆长度不合法")
    private String valveStem;
    /**
     * 装配人员
     */
    @FieldRemark(value = "装配人员")
    @Size(max = 64, message = "装配人员长度不合法")
    private String assemblyPerson;
    /**
     * 开始装配日期
     */
    @FieldRemark(value = "开始装配日期")
    @Size(max = 32, message = "开始装配日期长度不合法")
    private String assemblyStartDate;
    /**
     * 整机和驱动器试压
     */
    @FieldRemark(value = "整机和驱动器试压")
    @Size(max = 64, message = "整机和驱动器试压长度不合法")
    private String pressureTest;
    /**
     * 闸阀开关扭矩，N.m
     */
    @FieldRemark(value = "闸阀开关扭矩，N.m")
    private BigDecimal torqueNm;
    /**
     * 注油
     */
    @FieldRemark(value = "注油")
    @Size(max = 64, message = "注油长度不合法")
    private String oilInjection;
    /**
     * 试压人员
     */
    @FieldRemark(value = "试压人员")
    @Size(max = 64, message = "试压人员长度不合法")
    private String tester;
    /**
     * 装配完成数量（开始）
     */
    @FieldRemark(value = "装配完成数量")
    @Size(max = 16, message = "装配完成数量长度不合法")
    private Integer assemblyCompleteCount;
    /**
     * 装配完成日期
     */
    @FieldRemark(value = "装配完成日期")
    @Size(max = 32, message = "装配完成日期长度不合法")
    private String assemblyCompleteDate;
}
