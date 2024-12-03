package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * IndustryReportEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_INDUSTRY_REPORT")
@NoArgsConstructor
@ClassFunction("日报")
@Accessors(chain = true)
public class ReportEntity extends AbstractAdministrator {

    private static final long serialVersionUID = 5945041243096726177L;
    /**
     * 产品id
     */
    @FieldRemark(value = "产品id", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "产品id")
    private String productId;
    /**
     * 活动时间
     */
    @FieldRemark(value = "日期", indexType = "UNIQUE", indexGroup = "0")
    private String reportDate;
    /**
     * 操作人
     */
    @FieldRemark(value = "操作人", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "操作人")
    private String userId;
    /**
     * 加工类型
     */
    @FieldRemark(value = "加工类型")
    @Size(max = 64, message = "加工类型")
    private String processType;
    /**
     * 班次
     */
    @FieldRemark(value = "班次")
    @Size(max = 64, message = "班次")
    private String schedule;
    /**
     * 当班完成数量
     */
    private BigDecimal actualCompleteCount;
    /**
     * 主管补贴工时
     */
    private Integer leaderSubsidyMinute;
    /**
     * 开始设备运行时间（小时）
     */
    private Integer deviceRunningStartHour;
    /**
     * 开始设备运行时间（分钟）
     */
    private Integer deviceRunningStartMinute;
    /**
     * 结束设备运行时间（小时）
     */
    private Integer deviceRunningEndHour;
    /**
     * 结束设备运行时间（分钟）
     */
    private Integer deviceRunningEndMinute;
    /**
     * 停机内容1
     */
    private String stopWorkingContent1;
    /**
     * 停机时间1
     */
    private Integer stopWorkingMinute1;
    /**
     * 停机内容2
     */
    private String stopWorkingContent2;
    /**
     * 停机时间2
     */
    private Integer stopWorkingMinute2;
    /**
     * 停机内容3
     */
    private String stopWorkingContent3;
    /**
     * 停机时间3
     */
    private Integer stopWorkingMinute3;
    /**
     * 改善意见
     */
    @Size(max = 1024, message = "改善意见")
    private String improveSuggestion;
    /**
     * 订单编号
     */
    @FieldRemark(value = "订单编号")
    @Size(max = 64, message = "订单编号长度不合法")
    private String orderId;
    /**
     * 是否有效
     */
    @FieldRemark(value = "是否有效", defaultValue = "0")
    private Boolean valid;
    /**
     * 装夹时间/分钟
     */
    private Integer clampingMinute;
    /**
     * 辅助时间/分钟
     */
    private Integer assistMinute;
    /**
     * 程式运行时间/分钟
     */
    private Integer runningMinute;
    /**
     * 工作时间
     */
    private Integer workMinute;
}
