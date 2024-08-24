package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * IndustryRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ReportRequest implements Serializable {

    private static final long serialVersionUID = 5945041243096726177L;
    /**
     * 日报id
     */
    private String reportId;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 活动时间 @DateTimeFormat(pattern = "yyyy-MM-dd")
     */

    private String reportDate;
    /**
     * 订单
     */
    private String orderNo;
    /**
     * 调试设备
     */
    private String testDevice;
    /**
     * 项次
     */
    private String projectSequence;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 加工工序
     */
    private String processProcedure;
    /**
     * 操作人
     */
    private String userId;
    /**
     * 订单数量
     */
    private BigDecimal orderCount;
    /**
     * 加工类型
     */
    private String processType;
    /**
     * 工作时间
     */
    private Integer workMinute;
    /**
     * 班次
     */
    private String schedule;
    /**
     * 当班完成数量
     */
    private BigDecimal actualCompleteCount;
    /**
     * 主管补贴工时
     */
    private Integer leaderSubsidyMinute = 0;
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
     * 备注
     */
    private String remark;
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
    private String improveSuggestion;
    /**
     * 序号列表
     */
    private List<String> serialNoList;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList;
    /**
     * 是否有效
     */
    private Boolean valid;
}
