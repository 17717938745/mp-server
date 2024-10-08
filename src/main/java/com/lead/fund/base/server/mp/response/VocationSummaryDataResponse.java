package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * VocationSummaryDataResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class VocationSummaryDataResponse implements Serializable {

    private static final long serialVersionUID = 5945041243096001042L;
    /**
     * 请假天数
     */
    private BigDecimal vocationDays;
    private String vocationDaysFormat;
}
