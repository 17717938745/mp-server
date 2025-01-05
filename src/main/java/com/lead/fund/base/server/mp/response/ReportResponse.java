package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ReportResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ReportResponse implements Serializable {

    private static final long serialVersionUID = 5945041243096726178L;
    /**
     * ID
     */
    private String id;
    /**
     * 日报id
     */
    private String reportId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 活动时间
     */
    private String reportDate;
    /**
     * 操作人
     */
    private String userId;
    private String username;
    private String userFormat;
    /**
     * 订单数量
     */
    private BigDecimal orderCount;
    /**
     * 加工类型
     */
    private String processType;
    private String processTypeFormat;
    /**
     * 班次
     */
    private String schedule;
    private String scheduleFormat;
    /**
     * 应完成，默认取8小时
     */
    private BigDecimal shouldCompleteCount;
    /**
     * 当班完成数量
     */
    private BigDecimal actualCompleteCount;
    /**
     * 工作时间
     */
    private Integer workMinute;
    /**
     * 完成工时
     */
    private BigDecimal completeMinute;
    /**
     * 设备完成效率（应）
     */
    private BigDecimal deviceCompletePercent;
    private String deviceCompletePercentFormat;
    /**
     * 设备使用时间（某天、某人、某设备）
     */
    private Integer deviceUseMinute;
    /**
     * 当前报告设备使用时间
     */
    private Integer currentDeviceUseMinute;
    /**
     * 设备使用率
     */
    private BigDecimal deviceUsePercent;
    private String deviceUsePercentFormat;
    /**
     * 主管补贴工时
     */
    private Integer leaderSubsidyMinute;
    /**
     * 欠交数量
     */
    private BigDecimal surplusCount;
    /**
     * 设备单价
     */
    private BigDecimal deviceUnitPrice;
    private String deviceUnitPriceFormat;
    /**
     * 薪水
     */
    private BigDecimal salary;
    private String salaryFormat;
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
     * 设备信息汇总（按天分组）
     */
    private List<UserDeviceResponse> userDeviceList;
    /**
     * 备注
     */
    private String remark;
    /**
     * 停机内容1
     */
    private String stopWorkingContent1;
    private String stopWorkingContent1Format;
    /**
     * 停机时间1
     */
    private Integer stopWorkingMinute1;
    /**
     * 停机内容2
     */
    private String stopWorkingContent2;
    private String stopWorkingContent2Format;
    /**
     * 停机时间2
     */
    private Integer stopWorkingMinute2;
    /**
     * 停机内容3
     */
    private String stopWorkingContent3;
    private String stopWorkingContent3Format;
    /**
     * 停机时间3
     */
    private Integer stopWorkingMinute3;
    /**
     * 改善意见
     */
    private String improveSuggestion;

    /**
     * 订单
     */
    private String orderNo;
    /**
     * 项次
     */
    private String projectSequence;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 调试设备
     */
    private String testDevice;
    private String testDeviceFormat;
    private String deviceId;
    private String deviceIdFormat;
    /**
     * 加工工序
     */
    private String processProcedure;
    private String processProcedureFormat;
    /**
     * 程序号
     */
    private String programNumber;
    /**
     * 调试时间/分钟
     */
    private Integer debugMinute;
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
     * 产品标准工时/分钟
     */
    private BigDecimal productStandMinute;
    /**
     * 序号列表
     */
    private List<String> serialNoList = new ArrayList<>();
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList;
    /**
     * 是否有效
     */
    private Boolean valid;
    /**
     * 报告时间
     */
    private Date reportTime;
}
