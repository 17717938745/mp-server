package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * SchedulingResponse
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
public class SchedulingResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = -2958798731405959004L;
    /**
     * 序号
     */
    private Integer index;
    /**
     * 排班ID
     */
    private String schedulingId;
    /**
     * 某一月
     */
    private String dateMonth;
    /**
     * 第几周
     */
    private Integer weekIndex;
    /**
     * 日期
     */
    private String dateFormat;
    /**
     * 白班
     */
    private String scheduleDayTimeLabel;
    /**
     * 中班
     */
    private String scheduleMiddleLabel;
    /**
     * 夜班
     */
    private String scheduleEveningLabel;
    /**
     * 白班12H
     */
    private String scheduleDayTime12Label;
    /**
     * 夜班12H
     */
    private String scheduleEvening12Label;
    /**
     * 白班技术组
     */
    private String scheduleDayTimeTechnologyGroupLabel;
    /**
     * 夜班技术组
     */
    private String scheduleEveningTechnologyGroupLabel;
}
