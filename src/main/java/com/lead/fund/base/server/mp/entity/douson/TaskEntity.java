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
 * TaskEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_TASK")
@NoArgsConstructor
@ClassFunction("生产计划")
@Accessors(chain = true)
public class TaskEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096001163L;
    /**
     * 设备
     */
    @FieldRemark(value = "设备")
    @Size(max = 256, message = "设备长度不合法")
    private String deviceId;
    /**
     * 设备排序
     */
    @FieldRemark(value = "设备排序")
    @Size(max = 16, message = "设备排序长度不合法")
    private Integer deviceSorter;
    /**
     * 排序
     */
    @FieldRemark(value = "排序")
    @Size(max = 16, message = "排序长度不合法")
    private Integer sorter;
    /**
     * 客户简称
     */
    @FieldRemark(value = "客户简称")
    @Size(max = 256, message = "客户简称长度不合法")
    private String customerShortName;
    /**
     * 销售订单
     */
    @FieldRemark(value = "销售订单")
    @Size(max = 64, message = "销售订单长度不合法")
    private String saleOrderNo;
    /**
     * 订单项目
     */
    @FieldRemark(value = "订单项目")
    @Size(max = 32, message = "订单项目长度不合法")
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
     * 毛坯到货日期
     */
    @FieldRemark(value = "毛坯到货日期")
    @Size(max = 32, message = "毛坯到货日期长度不合法")
    private String roughcastExpireDate;
    /**
     * 领料数量
     */
    @FieldRemark(value = "领料数量")
    @Size(max = 16, message = "领料数量长度不合法")
    private BigDecimal materialCount;
    /**
     * 承诺完成日期
     */
    @FieldRemark(value = "承诺完成日期")
    @Size(max = 32, message = "承诺完成日期长度不合法")
    private String promiseDoneDate;
    /**
     * 计划加工数量
     */
    @FieldRemark(value = "计划加工数量")
    @Size(max = 16, message = "计划加工数量长度不合法")
    private BigDecimal planReformCount;
    /**
     * 外协工序备注
     */
    @FieldRemark(value = "外协工序备注")
    @Size(max = 256, message = "外协工序备注长度不合法")
    private String supplierRemark;
    /**
     * 8H班产量/件
     */
    @FieldRemark(value = "8H班产量/件")
    @Size(max = 16, message = "8H班产量/件长度不合法")
    private BigDecimal productCountHour8;
    /**
     * 12小时班产量/件
     */
    @FieldRemark(value = "12小时班产量/件")
    @Size(max = 16, message = "12小时班产量/件长度不合法")
    private BigDecimal productCountHour12;
    /**
     * 工序工时/件
     */
    @FieldRemark(value = "工序工时/件")
    @Size(max = 16, message = "工序工时/件长度不合法")
    private BigDecimal processWorkingHour;
    /**
     * 上线时间偏移
     */
    @FieldRemark(value = "上线时间偏移")
    @Size(max = 16, message = "上线时间偏移长度不合法")
    private BigDecimal onlineDateDiff;
    /**
     * 上线时间
     */
    @FieldRemark(value = "上线时间")
    @Size(max = 32, message = "上线时间长度不合法")
    private String onlineDate;
    /**
     * 下线时间
     */
    @FieldRemark(value = "下线时间")
    @Size(max = 32, message = "下线时间长度不合法")
    private String offlineDate;
    /**
     * 是否拖期
     */
    @FieldRemark(value = "是否拖期")
    @Size(max = 2, message = "是否拖期长度不合法")
    private BigDecimal delay;
    /**
     * 已加工数量
     */
    @FieldRemark(value = "已加工数量")
    @Size(max = 16, message = "已加工数量长度不合法")
    private BigDecimal processCount;
    /**
     * 加工工序
     */
    @FieldRemark(value = "加工工序")
    @Size(max = 64, message = "加工工序长度不合法")
    private String processProcedure;
    /**
     * NDE
     */
    @FieldRemark(value = "NDE")
    @Size(max = 16, message = "NDE长度不合法")
    private String nde;
    /**
     * 装配
     */
    @FieldRemark(value = "装配")
    @Size(max = 16, message = "装配长度不合法")
    private String assemble;
    /**
     * 试压
     */
    @FieldRemark(value = "试压")
    @Size(max = 16, message = "试压长度不合法")
    private String testPress;
    /**
     * 表面处理
     */
    @FieldRemark(value = "表面处理")
    @Size(max = 16, message = "表面处理长度不合法")
    private String surfaceTreatment;
    /**
     * 剩余
     */
    @FieldRemark(value = "剩余")
    @Size(max = 16, message = "剩余长度不合法")
    private BigDecimal surplus;
    /**
     * 领料单号
     */
    @FieldRemark(value = "领料单号")
    @Size(max = 64, message = "领料单号长度不合法")
    private String materialOrderNo;
    /**
     * 报检单号
     */
    @FieldRemark(value = "报检单号")
    @Size(max = 64, message = "报检单号长度不合法")
    private String checkOrderNo;
    /**
     * 要求外协完成交期
     */
    @FieldRemark(value = "要求外协完成交期")
    @Size(max = 32, message = "要求外协完成交期长度不合法")
    private String supplierDoneDate;
    /**
     * 发货数量
     */
    @FieldRemark(value = "发货数量")
    @Size(max = 16, message = "发货数量长度不合法")
    private BigDecimal deliverCount;
    /**
     * 发货日期
     */
    @FieldRemark(value = "发货日期")
    @Size(max = 32, message = "发货日期长度不合法")
    private String deliverDate;
    /**
     * 发货日期备注
     */
    @FieldRemark(value = "发货日期备注")
    @Size(max = 512, message = "发货日期备注长度不合法")
    private String deliverDateRemark;
    /**
     * 收货数量
     */
    @FieldRemark(value = "收货数量")
    @Size(max = 16, message = "收货数量长度不合法")
    private BigDecimal receiptCount;
    /**
     * 收货日期
     */
    @FieldRemark(value = "收货日期")
    @Size(max = 32, message = "收货日期长度不合法")
    private String receiptDate;
    /**
     * 收货日期备注
     */
    @FieldRemark(value = "收货日期备注")
    @Size(max = 512, message = "收货日期备注长度不合法")
    private String receiptDateRemark;
    /**
     * 报废数量
     */
    @FieldRemark(value = "报废数量")
    @Size(max = 16, message = "报废数量长度不合法")
    private BigDecimal scrapCount;
    /**
     * 外协承诺完成时间
     */
    @FieldRemark(value = "外协承诺完成时间")
    @Size(max = 512, message = "外协承诺完成时间长度不合法")
    private String supplierPromiseDoneDate;
}
