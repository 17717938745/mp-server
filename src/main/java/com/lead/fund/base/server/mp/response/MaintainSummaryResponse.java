package com.lead.fund.base.server.mp.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * MaintainSummaryResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MaintainSummaryResponse implements Serializable {

    private static final long serialVersionUID = 5658438762619500630L;
    /**
     * 停机时长H
     */
    private BigDecimal stopHour;
    private String stopHourFormat;
}
