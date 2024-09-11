package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * VocationEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_INDUSTRY_VOCATION")
@NoArgsConstructor
@ClassFunction("请假记录")
@Accessors(chain = true)
public class VocationEntity extends AbstractAdministrator {

    private static final long serialVersionUID = -2958798731405959766L;
    /**
     * 请假类型
     */
    @FieldRemark(value = "请假类型")
    private String vocationType;
    /**
     * 申请日期
     */
    @FieldRemark(value = "申请日期")
    private String date;
    /**
     * 请假人
     */
    @FieldRemark(value = "请假人")
    private String user;
    /**
     * 主管领导
     */
    @FieldRemark(value = "主管领导")
    private String chargeUser;
    /**
     * 请假开始日期
     */
    @FieldRemark(value = "请假开始日期")
    private String startDate;
    /**
     * 请假结束日期
     */
    @FieldRemark(value = "请假结束日期")
    private String endDate;
    /**
     * 请假理由
     */
    @FieldRemark(value = "请假理由")
    private String reason;
    /**
     * 请假天数
     */
    @FieldRemark(value = "请假天数")
    private BigDecimal count;
    /**
     * 是否符合请假规定
     */
    @FieldRemark(value = "是否符合请假规定")
    private Boolean compliance;
    /**
     * 不符合理由
     */
    @FieldRemark(value = "不符合理由")
    private String violationReason;
    /**
     * 请假人次
     */
    @FieldRemark(value = "请假人次")
    @TableField(value = "SUM(DISTINCT USER)", updateStrategy = FieldStrategy.NEVER, insertStrategy = FieldStrategy.NEVER)
    private BigDecimal sumUserCount;
    /**
     * 请假天数
     */
    @FieldRemark(value = "请假天数")
    @TableField(value = "SUM(COUNT)", updateStrategy = FieldStrategy.NEVER, insertStrategy = FieldStrategy.NEVER)
    private BigDecimal sumCount;
    /**
     * 不合格数
     */
    @FieldRemark(value = "请假天数")
    @TableField(value = "SUM(CASE WHEN COMPLIANCE = 1 THEN 0 ELSE 1 END)", updateStrategy = FieldStrategy.NEVER, insertStrategy = FieldStrategy.NEVER)
    private BigDecimal sumViolationCount;
    /**
     * 合格数
     */
    @FieldRemark(value = "请假天数")
    @TableField(value = "SUM(CASE WHEN COMPLIANCE = 1 THEN 1 ELSE 0 END)", updateStrategy = FieldStrategy.NEVER, insertStrategy = FieldStrategy.NEVER)
    private BigDecimal sumComplianceCount;
    /**
     * 总数
     */
    @FieldRemark(value = "请假天数")
    @TableField(value = "COUNT(1)", updateStrategy = FieldStrategy.NEVER, insertStrategy = FieldStrategy.NEVER)
    private BigDecimal sum;
    /**
     * 部门
     */
    @FieldRemark(value = "部门")
    @TableField(value = "COUNT(1)", updateStrategy = FieldStrategy.NEVER, insertStrategy = FieldStrategy.NEVER)
    private BigDecimal department;
}
