package com.lead.fund.base.server.mp.response;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * UserDeviceResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class UserDeviceResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 9092879800203052038L;
    /**
     * 设备使用率
     */
    private BigDecimal deviceUsePercent = BigDecimal.ZERO;
    /**
     * 开始设备运行时间（小时）
     */
    private Integer deviceRunningStartHour = Integer.MAX_VALUE;
    /**
     * 开始设备运行时间（分钟）
     */
    private Integer deviceRunningStartMinute = 0;
    /**
     * 结束设备运行时间（小时）
     */
    private Integer deviceRunningEndHour = 0;
    /**
     * 结束设备运行时间（分钟）
     */
    private Integer deviceRunningEndMinute = 0;
    /**
     * 设备运行时间（分钟）
     */
    private Integer deviceRunningMinute = 0;
    /**
     * 报告时间
     */
    private Date reportTime;
}
