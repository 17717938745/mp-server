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
 * SchedulingEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_SCHEDULING")
@NoArgsConstructor
@ClassFunction("排班")
@Accessors(chain = true)
public class SchedulingEntity extends AbstractAdministrator {

    private static final long serialVersionUID = -2958798731405959001L;
    /**
     * 某一月
     */
    @FieldRemark(value = "某一月", constraintGroup = "0")
    @Size(max = 16, message = "某一月长度不合法")
    private String dateMonth;
    /**
     * 第几周
     */
    @FieldRemark(value = "第几周", constraintGroup = "0")
    @Size(max = 8, message = "第几周长度不合法")
    private Integer weekIndex;
    /**
     * 白班
     */
    @FieldRemark(value = "白班")
    @Size(max = 512, message = "白班长度不合法")
    private String scheduleDayTimeLabel;
    /**
     * 中班
     */
    @FieldRemark(value = "中班")
    @Size(max = 512, message = "中班长度不合法")
    private String scheduleMiddleLabel;
    /**
     * 夜班
     */
    @FieldRemark(value = "夜班")
    @Size(max = 512, message = "夜班长度不合法")
    private String scheduleEveningLabel;
    /**
     * 白班12H
     */
    @FieldRemark(value = "白班12H")
    @Size(max = 512, message = "白班12H长度不合法")
    private String scheduleDayTime12Label;
    /**
     * 夜班12H
     */
    @FieldRemark(value = "夜班12H")
    @Size(max = 512, message = "夜班12H长度不合法")
    private String scheduleEvening12Label;
    /**
     * 白班技术组
     */
    @FieldRemark(value = "白班技术组")
    @Size(max = 512, message = "白班技术组长度不合法")
    private String scheduleDayTimeTechnologyGroupLabel;
    /**
     * 夜班技术组
     */
    @FieldRemark(value = "夜班技术组")
    @Size(max = 512, message = "夜班技术组长度不合法")
    private String scheduleEveningTechnologyGroupLabel;
}
