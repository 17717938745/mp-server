package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ReportSummaryResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ReportSummaryResponse implements Serializable {

    private static final long serialVersionUID = 5945041243096726179L;
    /**
     * 总薪水
     */
    private BigDecimal totalSalary;
    private String totalSalaryFormat;
}
