package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * VocationSummaryResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class VocationSummaryResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = 5945041243096001178L;
    /**
     * 序号
     */
    private Integer index;
    /**
     * 部门
     */
    private String department;
    private String departmentFormat;
    /**
     * 请假人次
     */
    private BigDecimal sumUserCount;
    private String sumUserCountFormat;
    /**
     * 请假天数
     */
    private BigDecimal sumCount;
    private String sumCountFormat;
    /**
     * 不合格数
     */
    private BigDecimal sumViolationCount;
    private String sumViolationCountFormat;
    /**
     * 合格数
     */
    private BigDecimal sumComplianceCount;
    private String sumComplianceCountFormat;
    /**
     * 总数
     */
    private BigDecimal sum;
    private String sumFormat;
    /**
     * 合格率
     */
    private BigDecimal complianceRate;
    private String complianceRateFormat;
}
