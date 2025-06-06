package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * AssemblySummaryResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AssemblySummaryResponse implements Serializable {

    private static final long serialVersionUID = -2958798731405959805L;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 采购订单编号
     */
    private String purchaseOrderNo;
    /**
     * PO项目
     */
    private String poProject;
    /**
     * 销售订单
     */
    private String saleOrderNo;
    /**
     * 订单项目
     */
    private String orderProject;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 物料描述
     */
    private String materialDescription;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 承诺交期
     */
    private String deliveryDate;
    private String deliveryDateFormat;
    /**
     * 完成数量
     */
    private Integer completedQty;
    /**
     * 注油完成数量
     */
    private Integer oilInjectionCompleteCount;
    /**
     * 注油完成日期
     */
    private String oilInjectionCompleteDate;
    private String oilInjectionCompleteDateFormat;
    /**
     * 装配完成日期
     */
    private String assemblyCompleteDate;
    private String assemblyCompleteDateFormat;
    /**
     * 装配完成数量
     */
    private Integer assemblyCompleteCount;
    /**
     * 订单数量，维度：采购订单编号、PO项目、销售订单、订单项目
     */
    private Integer orderCount;
}
