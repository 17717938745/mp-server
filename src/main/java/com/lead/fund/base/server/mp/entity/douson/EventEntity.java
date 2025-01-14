package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * EventEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_INDUSTRY_EVENT")
@NoArgsConstructor
@ClassFunction("事故记录")
@Accessors(chain = true)
public class EventEntity extends AbstractAdministrator {

    private static final long serialVersionUID = -2958798731405959766L;
    /**
     * 活动时间
     */
    @FieldRemark(value = "日期")
    private String reportDate;
    /**
     * 序号（单号）
     */
    @FieldRemark(value = "序号")
    private String serialNo;
    /**
     * 当事人
     */
    @FieldRemark(value = "当事人")
    @Size(max = 64, message = "当事人")
    private String userId;
    /**
     * 上级领导
     */
    @FieldRemark(value = "上级领导")
    @Size(max = 64, message = "上级领导")
    private String directLeader;
    /**
     * 问题描述
     */
    @FieldRemark(value = "问题描述")
    @Size(max = 256, message = "问题描述")
    private String accidentDescribe;
    /**
     * 原因
     */
    @FieldRemark(value = "原因")
    @Size(max = 256, message = "原因")
    private String reason;
    /**
     * 解决方法
     */
    @FieldRemark(value = "解决方法")
    @Size(max = 256, message = "解决方法")
    private String solve;
    /**
     * 改善后的证据描述
     */
    @FieldRemark(value = "改善后的证据描述")
    @Size(max = 64, message = "改善后的证据描述")
    private String improveDescribe;
    /**
     * 奖惩意见
     */
    @FieldRemark(value = "奖惩意见")
    @Size(max = 64, message = "奖惩意见")
    private String opinion;
    /**
     * 是否有效
     */
    @FieldRemark(value = "是否有效", defaultValue = "0")
    private Boolean valid;
}
