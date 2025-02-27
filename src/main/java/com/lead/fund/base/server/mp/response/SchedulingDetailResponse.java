package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import java.util.List;
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
@NoArgsConstructor
@Accessors(chain = true)
public class SchedulingDetailResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = -2958798731405959906L;
    /**
     * 序号
     */
    private Integer index;
    /**
     * 排班ID
     */
    private String schedulingId;
    /**
     * 设备编号
     */
    private String deviceNumber;
    private String deviceNumberFormat;
    /**
     * 白班
     */
    private String scheduleDayTimeFormat;
    private List<String> scheduleDayTimeList;
    /**
     * 中班
     */
    private String scheduleMiddleFormat;
    private List<String> scheduleMiddleList;
    /**
     * 夜班
     */
    private String scheduleEveningFormat;
    private List<String> scheduleEveningList;
    /**
     * 白班12H
     */
    private String scheduleDayTime12Format;
    private List<String> scheduleDayTime12List;
    /**
     * 夜班12H
     */
    private String scheduleEvening12Format;
    private List<String> scheduleEvening12List;
    /**
     * 白班技术组
     */
    private String scheduleDayTimeTechnologyGroupFormat;
    private List<String> scheduleDayTimeTechnologyGroupList;
    /**
     * 夜班技术组
     */
    private String scheduleEveningTechnologyGroupFormat;
    private List<String> scheduleEveningTechnologyGroupList;
}
