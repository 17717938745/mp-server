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
     * 总当班设备完成率
     */
    private BigDecimal sumDeviceCompletePercent = BigDecimal.ZERO;
    private String sumDeviceCompletePercentFormat;
    /**
     * 当班设备完成率
     */
    private BigDecimal deviceCompletePercent;
    private String deviceCompletePercentFormat;
    /**
     * 总设备使用率
     */
    private BigDecimal sumDeviceUsePercent;
    private String sumDeviceUsePercentFormat;
    /**
     * 设备使用率
     */
    private BigDecimal deviceUsePercent;
    private String deviceUsePercentFormat;
    /**
     * 使用率差值（绝对值）
     */
    private BigDecimal percentDiff;
    private String percentDiffFormat;
    /**
     * 用户ID列表
     */
    private Set<String> userIdList = new HashSet<>();
    private int userIdCount;
    /**
     * 用户
     */
    private String deviceId;
    private String deviceIdFormat;
    /**
     * 报告日期
     */
    private Set<String> reportDateList;
    private int reportDateCount = 1;
    private int totalCount = 1;
}
