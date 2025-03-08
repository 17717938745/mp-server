package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * MpUserSaveRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:41
 */
@Data
@ToString
public class MpUserMergeRequest implements Serializable {

    private static final long serialVersionUID = 4288951200576827628L;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 密码
     */
    private String password;
    /**
     * 状态，0-正常 1-删除 {@link com.lead.fund.base.common.basic.cons.frame.AdminState}
     */
    private Integer state;
    /**
     * 角色id列表
     */
    private List<String> roleIdList;
    /**
     * 部门
     */
    private String department;
    /**
     * 职位
     */
    private String profession;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList;
    /**
     * interview resume
     */
    private String interviewResume;
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
     * 计划下次调薪日期
     */
    private String lastIncreaseSalaryDate;
    /**
     * 最后一次调薪日期
     */
    private String planIncreaseSalaryDate;
}
