package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * AssemblyRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ScoreRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -9124162949342382879L;
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
    private String qualityScore;
    /**
     * 全勤/团结  10分
     */
    private String attendanceScore;
    /**
     * 环境安全和工艺 20分
     */
    private String safetyScore;
    /**
     * 月度绩效 (20分)
     */
    private String monthlyPerformance;
    /**
     * 总上班日数
     */
    private String totalWorkDays;
    /**
     * 总计
     */
    private String total;
    /**
     * 评估月数
     */
    private String evaluationMonths;
    /**
     * 评比结果
     */
    private String evaluationResult;
    /**
     * 季度奖金
     */
    private String quarterlyBonus;
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
    private List<PhotoImgModel> photoList = new ArrayList<>();
    /**
     * 装配完成日期
     */
    private String assemblyCompleteDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startAssemblyCompleteDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endAssemblyCompleteDate;
}
