package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * TaskRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TaskRequest extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096001164L;
    /**
     * 生产计划ID
     */
    private String taskId;
    /**
     * 设备
     */
    private String deviceId;
    /**
     * 设备排序
     */
    private Integer deviceSorter;
    /**
     * 排序
     */
    private Integer sorter;
    /**
     * 客户简称
     */
    private String customerShortName;
    /**
     * 销售订单
     */
    private String saleOrderNo;
    /**
     * 订单项目
     */
    private String orderProjectNo;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 加工物料描述
     */
    private String improveMaterialDescribe;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 订单数量
     */
    private BigDecimal orderCount;
    /**
     * 毛坯到货日期
     */
    private String roughcastExpireDate;
    /**
     * 领料数量
     */
    private BigDecimal materialCount;
    /**
     * 承诺完成日期
     */
    private String promiseDoneDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startPromiseDoneDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endPromiseDoneDate;
    /**
     * 计划加工数量
     */
    private BigDecimal planReformCount;
    /**
     * 外协工序备注
     */
    private String supplierRemark;
    /**
     * 8H班产量/件
     */
    private BigDecimal productCountHour8;
    /**
     * 12小时班产量/件
     */
    private BigDecimal productCountHour12;
    /**
     * 工序工时/件
     */
    private BigDecimal processWorkingHour;
    /**
     * 上线时间
     */
    private String onlineDate;
    /**
     * 下线时间
     */
    private String offlineDate;
    /**
     * 是否拖期
     */
    private BigDecimal delay;
    /**
     * 已加工数量
     */
    private BigDecimal processCount;
    /**
     * 加工工序
     */
    private List<String> processProcedureList = new ArrayList<>();
    /**
     * NDE
     */
    private String nde;
    /**
     * 装配
     */
    private String assemble;
    /**
     * 试压
     */
    private String testPress;
    /**
     * 表面处理
     */
    private String surfaceTreatment;
    /**
     * 剩余
     */
    private BigDecimal surplus;
    /**
     * 领料单号
     */
    private String materialOrderNo;
    /**
     * 报检单号
     */
    private String checkOrderNo;
    /**
     * 要求外协完成交期
     */
    private String supplierDoneDate;
    /**
     * 发货数量
     */
    private BigDecimal deliverCount;
    /**
     * 发货日期
     */
    private String deliverDate;
    /**
     * 收货数量
     */
    private BigDecimal receiptCount;
    /**
     * 收货日期
     */
    private String receiptDate;
    /**
     * 报废数量
     */
    private BigDecimal scrapCount;
    /**
     * 外协承诺完成时间
     */
    private String supplierPromiseDoneDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startSupplierPromiseDoneDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endSupplierPromiseDoneDate;
    /**
     * 加工状态，1-已完成（加工数量=领料数量）
     */
    private Integer processType;
}