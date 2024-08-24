package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * AccidentEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@TableName("MP_INDUSTRY_ACCIDENT")
@NoArgsConstructor
@ClassFunction("事故记录")
@Accessors(chain = true)
public class AccidentEntity extends AbstractAdministrator {

    private static final long serialVersionUID = -8361023609419505388L;
    /**
     * 活动时间
     */
    @FieldRemark(value = "日期")
    private String reportDate;
    /**
     * 操作人
     */
    @FieldRemark(value = "操作人")
    @Size(max = 64, message = "操作人")
    private String userId;
    /**
     * 事故类型
     */
    @FieldRemark(value = "事故类型")
    @Size(max = 64, message = "事故类型")
    private String accidentType;
    /**
     * 责任人
     */
    @FieldRemark(value = "责任人")
    @Size(max = 128, message = "责任人")
    private String dutyPerson;
    /**
     * 组长
     */
    @FieldRemark(value = "组长")
    @Size(max = 128, message = "组长")
    private String groupLeader;
    /**
     * 主管
     */
    @FieldRemark(value = "主管")
    @Size(max = 128, message = "主管")
    private String chargePerson;
    /**
     * 经理
     */
    @FieldRemark(value = "经理")
    @Size(max = 128, message = "经理")
    private String manager;
    /**
     * 设备描述
     */
    @FieldRemark(value = "设备描述")
    @Size(max = 256, message = "设备描述")
    private String deviceDescribe;
    /**
     * 图号描述
     */
    @FieldRemark(value = "图号描述")
    @Size(max = 256, message = "图号描述")
    private String designNumberDescribe;
    /**
     * 产品重量（KG）
     */
    private BigDecimal productWeight;
    /**
     * 问题描述
     */
    @FieldRemark(value = "问题描述")
    @Size(max = 256, message = "问题描述")
    private String accidentDescribe;
    /**
     * 伤害描述
     */
    @FieldRemark(value = "伤害描述")
    @Size(max = 256, message = "伤害描述")
    private String damageDescribe;
    /**
     * 财产损失描述
     */
    @FieldRemark(value = "财产损失描述")
    @Size(max = 256, message = "财产损失描述")
    private String propertyLossDescribe;
    /**
     * 人的因素原因
     */
    @FieldRemark(value = "人的因素原因")
    @Size(max = 256, message = "人的因素原因")
    private String humanFactorReason;
    /**
     * 设备原因
     */
    @FieldRemark(value = "设备原因")
    @Size(max = 256, message = "设备原因")
    private String deviceReason;
    /**
     * 材料原因
     */
    @FieldRemark(value = "材料原因")
    @Size(max = 256, message = "材料原因")
    private String materialReason;
    /**
     * 工作方法原因
     */
    @FieldRemark(value = "工作方法原因")
    @Size(max = 256, message = "工作方法原因")
    private String workMethodReason;
    /**
     * 环境原因
     */
    @FieldRemark(value = "环境原因")
    @Size(max = 256, message = "环境原因")
    private String environmentReason;
    /**
     * 人的因素解决方法
     */
    @FieldRemark(value = "人的因素解决方法")
    @Size(max = 256, message = "人的因素解决方法")
    private String humanFactorSolve;
    /**
     * 设备解决方法
     */
    @FieldRemark(value = "设备解决方法")
    @Size(max = 256, message = "设备解决方法")
    private String deviceSolve;
    /**
     * 材料解决方法
     */
    @FieldRemark(value = "材料解决方法")
    @Size(max = 256, message = "材料解决方法")
    private String materialSolve;
    /**
     * 工作方法解决方法
     */
    @FieldRemark(value = "工作方法解决方法")
    @Size(max = 256, message = "工作方法解决方法")
    private String workMethodSolve;
    /**
     * 环境解决方法
     */
    @FieldRemark(value = "环境解决方法")
    @Size(max = 256, message = "环境解决方法")
    private String environmentSolve;
    /**
     * 责任人1
     */
    @FieldRemark(value = "责任人1")
    @Size(max = 128, message = "责任人1")
    private String dutyPerson1;
    /**
     * 罚款金额1
     */
    @FieldRemark(value = "罚款金额1")
    private BigDecimal fineAmount1;
    /**
     * 责任人2
     */
    @FieldRemark(value = "责任人2")
    @Size(max = 128, message = "责任人2")
    private String dutyPerson2;
    /**
     * 罚款金额2
     */
    @FieldRemark(value = "罚款金额2")
    private BigDecimal fineAmount2;
    /**
     * 责任人3
     */
    @FieldRemark(value = "责任人3")
    @Size(max = 128, message = "责任人3")
    private String dutyPerson3;
    /**
     * 罚款金额3
     */
    @FieldRemark(value = "罚款金额3")
    private BigDecimal fineAmount3;
    /**
     * 改善后的证据描述
     */
    @FieldRemark(value = "改善后的证据描述")
    @Size(max = 64, message = "改善后的证据描述")
    private String improveEvidenceDescribe;
    /**
     * 是否有效
     */
    @FieldRemark(value = "是否有效", defaultValue = "0")
    private Boolean valid;
}
