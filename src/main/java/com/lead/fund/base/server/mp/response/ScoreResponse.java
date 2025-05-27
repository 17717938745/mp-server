package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * AssemblyResponse
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
public class ScoreResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = -2958798731405959803L;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 修改时间
     */
    private String modifyTime;
    /**
     * 季度评比报告ID
     */
    private String scoreId;
    /**
     * 工号
     */
    private String employeeId;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 季度
     */
    private String quarter;
    /**
     * 设备编号
     */
    private String deviceNumber;
    /**
     * 质量 50分
     */
    private BigDecimal qualityScore;
    private String qualityScoreFormat;
    /**
     * 全勤/团结  10分
     */
    private BigDecimal attendanceScore;
    private String attendanceScoreFormat;
    /**
     * 环境安全和工艺 20分
     */
    private BigDecimal safetyScore;
    private String safetyScoreFormat;
    /**
     * 月度绩效 (20分)
     */
    private BigDecimal monthlyPerformance;
    private String monthlyPerformanceFormat;
    /**
     * 总上班日数
     */
    private Integer totalWorkDays;
    /**
     * 总计
     */
    private BigDecimal total;
    private String totalFormat;
    /**
     * 评估月数
     */
    private Integer evaluationMonths;
    /**
     * 评比结果
     */
    private String evaluationResult;
    /**
     * 季度奖金
     */
    private BigDecimal quarterlyBonus;
    private String quarterlyBonusFormat;
    /**
     * 备注
     */
    private String description;
    /**
     * 当班主管
     */
    private String leaderUserId;
    /**
     * 照片(最多50)
     */
    private Integer photoCount;
    private List<PhotoImgModel> photoList = new ArrayList<>();
}
