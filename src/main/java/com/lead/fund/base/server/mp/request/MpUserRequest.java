package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * MpUserRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:41
 */
@Data
@ToString
public class MpUserRequest implements Serializable {

    private static final long serialVersionUID = 6886198438973390779L;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户
     */
    private List<String> userIdList;
    /**
     * 部门
     */
    private String department;
    /**
     * 职位
     */
    private String profession;
    /**
     * 角色
     */
    private String roleId;
    /**
     * 状态，0-正常 1-删除 {@link com.lead.fund.base.common.basic.cons.frame.AdminState}
     */
    private Integer state;
    /**
     * 姓名
     */
    private String name;
    /**
     * 人员属性
     */
    private String userProperty;
    /**
     * 班次
     */
    private String schedule;
    /**
     * 当班主管
     */
    private String leaderUserId;
    /**
     * 最后一次调薪日期
     */
    private String lastIncreaseSalaryDate;
    /**
     * 计划下次调薪日期
     */
    private String planIncreaseSalaryDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startPlanIncreaseSalaryDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endPlanIncreaseSalaryDate;
    /**
     * 开始时间@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startCreateTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endCreateTime;
    /**
     * 国际
     */
    private String nationality;
    /**
     * 工号
     */
    private String employeeId;
    /**
     * 是否可以外网登录
     */
    private Boolean externalSign;
}
