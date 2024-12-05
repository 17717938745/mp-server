package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ReportSummaryDeviceResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ReportSummaryDeviceResponse implements Serializable {

    private static final long serialVersionUID = 5945041243096726181L;
    /**
     * 序号
     */
    private Integer index;
    /**
     * 报告日期
     */
    private Set<String> reportDateList = new HashSet<>();
    private Integer reportDateCount = 1;
    /**
     * 用户
     */
    private String userId;
    private String userIdFormat;
    /**
     * 用户ID列表
     */
    private Set<String> userIdList = new HashSet<>();
    private Integer userIdCount;
    /**
     * 用户
     */
    private String deviceId;
    private String deviceIdFormat;
    private Integer totalCount = 1;
    /**
     * 总条数
     */
    private Integer deviceTotalCount = 1;
    /**
     * 总当班设备完成率
     */
    private BigDecimal sumDeviceCompletePercent = BigDecimal.ZERO;
    private String sumDeviceCompletePercentFormat;
    /**
     * 当班设备完成率
     */
    private BigDecimal deviceCompletePercent = BigDecimal.ZERO;
    private String deviceCompletePercentFormat;
    /**
     * 总设备使用率
     */
    private BigDecimal sumDeviceUsePercent = BigDecimal.ZERO;
    private String sumDeviceUsePercentFormat;
    /**
     * 设备使用率
     */
    private BigDecimal deviceUsePercent = BigDecimal.ZERO;
    private String deviceUsePercentFormat;
    /**
     * 使用率差值（绝对值）
     */
    private BigDecimal percentDiff = BigDecimal.ZERO;
    private String percentDiffFormat;
    /**
     * 报告日期，start device ========
     */
    private Set<String> deviceReportDateList = new HashSet<>();
    /**
     * 记录数
     */
    private Integer deviceReportDateCount = 1;
    /**
     * 当班设备完成率
     */
    private BigDecimal deviceDeviceCompletePercent = BigDecimal.ZERO;
    private String deviceDeviceCompletePercentFormat;
    /**
     * 总当班设备完成率
     */
    private BigDecimal deviceSumDeviceCompletePercent = BigDecimal.ZERO;
    private String deviceSumDeviceCompletePercentFormat;
    /**
     * 总设备使用率
     */
    private BigDecimal deviceSumDeviceUsePercent = BigDecimal.ZERO;
    private String deviceSumDeviceUsePercentFormat;
    /**
     * 设备使用率
     */
    private BigDecimal deviceDeviceUsePercent = BigDecimal.ZERO;
    private String deviceDeviceUsePercentFormat;
    /**
     * 使用率差值（绝对值）
     */
    private BigDecimal devicePercentDiff = BigDecimal.ZERO;
    private String devicePercentDiffFormat;
}
