package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * PlanEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_PLAN")
@NoArgsConstructor
@ClassFunction("精益持续改善")
@Accessors(chain = true)
public class PlanEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096726180L;
    /**
     * 创建日期
     */
    @FieldRemark(value = "创建日期")
    @Size(max = 256, message = "创建日期")
    private String createDate;
    /**
     * 部门
     */
    @FieldRemark(value = "部门")
    @Size(max = 256, message = "部门")
    private String department;
    /**
     * 精益类型
     */
    @FieldRemark(value = "精益类型")
    @Size(max = 256, message = "精益类型")
    private String optimizeType;
    /**
     * 存在问题
     */
    @FieldRemark(value = "存在问题")
    @Size(max = 256, message = "存在问题")
    private String existsProblem;
    /**
     * 解决方案
     */
    @FieldRemark(value = "解决方案")
    @Size(max = 2048, message = "解决方案")
    private String solveScheme;
    /**
     * 负责人
     */
    @FieldRemark(value = "负责人")
    @Size(max = 256, message = "负责人")
    private String responsiblePerson;
    /**
     * 计划完成时间
     */
    @FieldRemark(value = "计划完成时间")
    @Size(max = 256, message = "计划完成时间")
    private String planCompleteTime;
    /**
     * 奖励金额
     */
    @FieldRemark(value = "奖励金额")
    @Size(max = 256, message = "奖励金额")
    private BigDecimal awardAmount;
    /**
     * 结案
     */
    @FieldRemark(value = "结案")
    private Boolean valid;
    /**
     * 序号（单号）
     */
    private String serialNo;
    /**
     * 负责小组
     */
    private String responsibleTeam;
}
