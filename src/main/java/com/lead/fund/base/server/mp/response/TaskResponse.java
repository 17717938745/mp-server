package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.database.entity.AbstractAdmin;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * TaskResponse
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
public class TaskResponse extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096001165L;
    /**
     * 序号
     */
    private Integer index;
    /**
     * 序号
     */
    private Integer deviceIndex;
    /**
     * 生产计划ID
     */
    private String taskId;
    /**
     * 设备
     */
    private String deviceId;
    private String deviceIdFormat;
    /**
     * 设备排序
     */
    private Integer deviceSorter;
    /**
     * 是否可以往上
     */
    private boolean up;
    /**
     * 排序
     */
    private Integer sorter;
    /**
     * 是否可以往下
     */
    private boolean down;
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
     * 上线时间偏移
     */
    private BigDecimal onlineDateDiff;
    /**
     * 上线时间
     */
    private String onlineDate;
    /**
     * 下线时间
     */
    private String offlineDate;
    /**
     * 是否拖期（天）
     */
    private BigDecimal delay;
    /**
     * 0-是，1-否
     */
    private Integer delayType;
    private String delayTypeFormat;
    /**
     * 已加工数量
     */
    private BigDecimal processCount;
    /**
     * 加工工序
     */
    private String processProcedure;
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
    private String materialOrderNoFormat;
    /**
     * 报检单号
     */
    private String checkOrderNo;
    private String checkOrderNoFormat;
    /**
     * 要求外协完成交期偏移
     */
    private BigDecimal supplierDoneDateDiff;
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
     * 发货日期备注
     */
    private String deliverDateRemark;
    /**
     * 收货数量
     */
    private BigDecimal receiptCount;
    /**
     * 收货日期
     */
    private String receiptDate;
    /**
     * 收货日期备注
     */
    private String receiptDateRemark;
    /**
     * 报废数量
     */
    private BigDecimal scrapCount;
    /**
     * 外协承诺完成时间
     */
    private String supplierPromiseDoneDate;
    /**
     * 是否准时交付
     */
    private Boolean timelyDeliver;
    private String timelyDeliverFormat;
}
