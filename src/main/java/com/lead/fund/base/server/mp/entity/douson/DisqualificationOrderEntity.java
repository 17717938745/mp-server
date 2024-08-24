package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * DisqualificationOrderEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_DISQUALIFICATION_ORDER")
@NoArgsConstructor
@ClassFunction("不合格单")
@Accessors(chain = true)
public class DisqualificationOrderEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096726179L;
    /**
     * 不合格单号
     */
    @FieldRemark(value = "不合格单号")
    @Size(max = 256, message = "不合格单号")
    private Integer disqualificationOrderNo;
    /**
     * 不合格单号
     */
    @FieldRemark(value = "不合格单号")
    @Size(max = 256, message = "不合格单号")
    private String disqualificationOrder;
    /**
     * 单号
     */
    @FieldRemark(value = "单号")
    @Size(max = 256, message = "单号")
    private String orderNo;
    /**
     * 项次
     */
    @FieldRemark(value = "项次")
    @Size(max = 256, message = "项次")
    private String projectSequence;
    /**
     * 物料号
     */
    @FieldRemark(value = "物料号")
    @Size(max = 256, message = "物料号")
    private String materialNo;
    /**
     * 图号
     */
    @FieldRemark(value = "图号")
    @Size(max = 256, message = "图号")
    private String designNumber;
    /**
     * 流程
     */
    @FieldRemark(value = "流程")
    @Size(max = 256, message = "流程")
    private String process;
    /**
     * 不合格内容
     */
    @FieldRemark(value = "不合格内容")
    @Size(max = 256, message = "不合格内容")
    private String disqualificationContent;
    /**
     * 数量
     */
    @FieldRemark(value = "数量")
    @Size(max = 256, message = "数量")
    private String count;
    /**
     * 检验节点
     */
    @FieldRemark(value = "检验节点")
    @Size(max = 256, message = "检验节点")
    private String checkPoint;
    /**
     * 责任人员
     */
    @FieldRemark(value = "责任人员")
    @Size(max = 256, message = "责任人员")
    private String dutyPerson;
    /**
     * 质量处理意见
     */
    @FieldRemark(value = "质量处理意见")
    @Size(max = 256, message = "质量处理意见")
    private String qualityDealOpinion;
    /**
     * 技术处理意见
     */
    @FieldRemark(value = "技术处理意见")
    @Size(max = 256, message = "技术处理意见")
    private String skillDealOpinion;
    /**
     * 扣款
     */
    @FieldRemark(value = "扣款")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigDecimal fineAmount;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 1024, message = "备注")
    private String remark;
    /**
     * 炉号
     */
    @FieldRemark(value = "炉号")
    @Size(max = 256, message = "炉号")
    private String stoveNo;
    /**
     * 热批号
     */
    @FieldRemark(value = "热批号")
    @Size(max = 256, message = "热批号")
    private String hotBatchNo;
    /**
     * 序列号
     */
    @FieldRemark(value = "序列号")
    @Size(max = 256, message = "序列号")
    private String serialNo;
    /**
     * 缺陷类型
     */
    @FieldRemark(value = "缺陷类型")
    @Size(max = 256, message = "缺陷类型")
    private String defectType;
}
