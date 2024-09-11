package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ComputerResponse
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
public class VocationResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = 5945041243096001178L;
    /**
     * 请假ID
     */
    private String vocationId;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 请假类型
     */
    private String vocationType;
    private String vocationTypeFormat;
    /**
     * 申请日期
     */
    private String date;
    /**
     * 请假人
     */
    private String user;
    private String userFormat;
    /**
     * 部门
     */
    private String department;
    private String departmentFormat;
    /**
     * 职务
     */
    private String profession;
    private String professionFormat;
    /**
     * 主管领导
     */
    private String chargeUser;
    private String chargeUserFormat;
    /**
     * 请假开始日期
     */
    private String startDate;
    /**
     * 请假结束日期
     */
    private String endDate;
    /**
     * 请假理由
     */
    private String reason;
    /**
     * 请假天数
     */
    private BigDecimal count;
    private String countFormat;
    /**
     * 是否符合请假规定
     */
    private Boolean compliance;
    private String complianceFormat;
    /**
     * 不符合理由
     */
    private String violationReason;
}
