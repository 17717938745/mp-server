package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * MpUserDepartmentSummaryResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@Accessors(chain = true)
public class MpUserDepartmentSummaryResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = 6886198438973390040L;
    private Integer index;
    private String department;
    private String departmentFormat;
    private BigDecimal workShop;
    private BigDecimal office;
    private BigDecimal total;
    private BigDecimal scheduleNull;
    private BigDecimal scheduleDayTime;
    private BigDecimal scheduleDayTime12;
    private BigDecimal scheduleEvening;
    private BigDecimal scheduleEvening12;
    private BigDecimal scheduleMiddle;
    private BigDecimal vietnamCount;
    private BigDecimal chinaCount;
    private String manager;
    private String managerFormat;
}
