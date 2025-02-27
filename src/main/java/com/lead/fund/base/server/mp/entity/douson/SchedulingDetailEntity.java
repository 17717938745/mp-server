package com.lead.fund.base.server.mp.entity.douson;

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
 * SchedulingDetailEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_SCHEDULING_DETAIL")
@NoArgsConstructor
@ClassFunction("排班明细")
@Accessors(chain = true)
public class SchedulingDetailEntity extends AbstractAdministrator {

    private static final long serialVersionUID = -2958798731405959900L;
    /**
     * 排班ID
     */
    @FieldRemark(value = "排班ID", constraintGroup = "0")
    @Size(max = 64, message = "排班ID长度不合法")
    private String schedulingId;
    /**
     * 设备编号
     */
    @FieldRemark(value = "设备编号", constraintGroup = "0")
    @Size(max = 64, message = "设备编号长度不合法")
    private String deviceNumber;
    /**
     * 白班
     */
    @FieldRemark(value = "白班")
    @Size(max = 512, message = "白班长度不合法")
    private String scheduleDayTime;
    /**
     * 中班
     */
    @FieldRemark(value = "中班")
    @Size(max = 512, message = "中班长度不合法")
    private String scheduleMiddle;
    /**
     * 夜班
     */
    @FieldRemark(value = "夜班")
    @Size(max = 512, message = "夜班长度不合法")
    private String scheduleEvening;
    /**
     * 白班12H
     */
    @FieldRemark(value = "白班12H")
    @Size(max = 512, message = "白班12H长度不合法")
    private String scheduleDayTime12;
    /**
     * 夜班12H
     */
    @FieldRemark(value = "夜班12H")
    @Size(max = 512, message = "夜班12H长度不合法")
    private String scheduleEvening12;
    /**
     * 白班技术组
     */
    @FieldRemark(value = "白班技术组")
    @Size(max = 512, message = "白班技术组长度不合法")
    private String scheduleDayTimeTechnologyGroup;
    /**
     * 夜班技术组
     */
    @FieldRemark(value = "夜班技术组")
    @Size(max = 512, message = "夜班技术组长度不合法")
    private String scheduleEveningTechnologyGroup;
}
