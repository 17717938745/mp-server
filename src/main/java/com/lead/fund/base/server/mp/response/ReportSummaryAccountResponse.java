package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ReportSummaryAccountResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ReportSummaryAccountResponse implements Serializable {

    private static final long serialVersionUID = 5945041243096726180L;
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
     * 平均当班设备完成率
     */
    private BigDecimal deviceCompletePercent;
    private String deviceCompletePercentFormat;
    /**
     * 薪水
     */
    private BigDecimal salary;
    private String salaryFormat;
    /**
     * 用户
     */
    private String userId;
    private String userIdFormat;
    /**
     * 报告日期
     */
    private Set<String> reportDateList = new HashSet<>();
    private int reportDateCount;
    private int totalCount = 1;
}