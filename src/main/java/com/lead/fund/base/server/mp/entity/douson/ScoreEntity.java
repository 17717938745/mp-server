package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ScoreEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_SCORE")
@NoArgsConstructor
@ClassFunction("季度评比报告")
@Accessors(chain = true)
public class ScoreEntity extends AbstractAdministrator {

    @Serial
    private static final long serialVersionUID = 143251638925567903L;
    /**
     * 用户ID
     */
    @FieldRemark(value = "用户ID")
    @Size(max = 64, message = "用户ID长度不合法")
    private String userId;
    /**
     * 季度
     */
    @FieldRemark(value = "季度")
    @Size(max = 16, message = "季度长度不合法")
    private String quarter;
    /**
     * 设备编号
     */
    @FieldRemark(value = "设备编号")
    @Size(max = 64, message = "设备编号长度不合法")
    private String deviceNumber;
    /**
     * 质量 50分
     */
    @FieldRemark(value = "质量 50分")
    private BigDecimal qualityScore;
    /**
     * 全勤/团结  10分
     */
    @FieldRemark(value = "全勤/团结  10分")
    private BigDecimal attendanceScore;
    /**
     * 环境安全和工艺 20分
     */
    @FieldRemark(value = "环境安全和工艺 20分")
    private BigDecimal safetyScore;
    /**
     * 月度绩效 (20分)
     */
    @FieldRemark(value = "月度绩效 (20分)")
    private BigDecimal monthlyPerformance;
    /**
     * 总上班日数
     */
    @FieldRemark(value = "总上班日数")
    private Integer totalWorkDays;
    /**
     * 总计
     */
    @FieldRemark(value = "总计")
    private BigDecimal total;
    /**
     * 评估月数
     */
    @FieldRemark(value = "评估月数")
    private Integer evaluationMonths;
    /**
     * 评比结果
     */
    @FieldRemark(value = "评比结果")
    private String evaluationResult;
    /**
     * 季度奖金
     */
    @FieldRemark(value = "季度奖金")
    private BigDecimal quarterlyBonus;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 512, message = "备注长度不合法")
    private String description;
    /**
     * 当班主管
     */
    @FieldRemark(value = "当班主管")
    @Size(max = 64, message = "当班主管长度不合法")
    private String leaderUserId;
    /**
     * 排序
     */
    @FieldRemark(value = "排序", defaultValue = "0")
    @Size(max = 8, message = "排序长度最大为8")
    private Integer sorter;
}
