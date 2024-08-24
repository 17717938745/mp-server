package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * MaintainEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_INDUSTRY_MAINTAIN")
@NoArgsConstructor
@ClassFunction("事故记录")
@Accessors(chain = true)
public class MaintainEntity extends AbstractAdministrator {

    private static final long serialVersionUID = -2958798731405959766L;
    /**
     * 日期
     */
    @FieldRemark(value = "日期")
    @Size(max = 32)
    private String date;
    /**
     * 维修ID
     */
    @FieldRemark(value = "维修ID")
    private String equipmentId;
    /**
     * 故障原因
     */
    @FieldRemark(value = "故障原因")
    private String brokenReason;
    /**
     * 故障内容
     */
    @FieldRemark(value = "故障内容")
    private String brokenContent;
    /**
     * 修理内容
     */
    @FieldRemark(value = "修理内容")
    private String repairContent;
    /**
     * 更换配件
     */
    @FieldRemark(value = "更换配件")
    private String replacePair;
    /**
     * 维修类型
     */
    @FieldRemark(value = "维修类型")
    private String repairType;
    /**
     * 停机时长H
     */
    @FieldRemark(value = "停机时长H")
    private BigDecimal stopHour;
    @FieldRemark(value = "停机时长H")
    @TableField(value = "SUM(STOP_HOUR)", updateStrategy = FieldStrategy.NEVER, insertStrategy = FieldStrategy.NEVER)
    private BigDecimal sumStopHour;
    /**
     * 当事人
     */
    @FieldRemark(value = "当事人")
    @Size(max = 64)
    private String partyUser;
    /**
     * 结案
     */
    private Boolean valid;
}
