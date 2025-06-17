package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * UserResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@Accessors(chain = true)
public class MpUserResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = 6886198438973390778L;
    private Integer index;
    private Integer professionIndex;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码密文
     */
    private String passwordEncrypt;
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
     * 密码过期时间
     */
    private Date passwordExpireTime;
    private boolean passwordExpire;
    /**
     * 登录是否锁定
     */
    private boolean signInLock;
    /**
     * 登录错误次数
     */
    private int signInFailCount;
    /**
     * 登录锁定时间
     */
    private Date signInLockTime;
    /**
     * 角色列表
     */
    private List<MpRoleResponse> roleList;
    private List<String> roleIdList;
    private List<String> roleCodeList;
    /**
     * 创建人
     */
    private String creatorFormat;
    /**
     * 修改人
     */
    private String modifierFormat;
    /**
     * 密码密钥，为了做密码历史记录功能，同一个用户只生成唯一的密钥，不可被修改
     */
    private String salt;
    /**
     * 活跃时间
     */
    private long aliveMillis;
    /**
     * 部门
     */
    private String department;
    private String departmentFormat;
    /**
     * 职位
     */
    private String profession;
    private String professionFormat;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList = new ArrayList<>();
    /**
     * interview resume
     */
    private String interviewResume;
    /**
     * 人员属性
     */
    private String userProperty;
    private String userPropertyFormat;
    /**
     * 班次
     */
    private String schedule;
    private String scheduleFormat;
    /**
     * 当班主管
     */
    private String leaderUserId;
    private String leaderUserIdFormat;
    /**
     * 最后一次调薪日期
     */
    private String lastIncreaseSalaryDate;
    /**
     * 计划下次调薪日期
     */
    private String planIncreaseSalaryDate;
    /**
     * 计划下次调薪日期剩余天数
     */
    private Integer planIncreaseSalaryDateCount;
    /**
     * 国际
     */
    private String nationality;
    private String nationalityFormat;
    /**
     * 工号
     */
    private String employeeId;
    /**
     * 是否可以外网登录
     */
    private Boolean externalSign;
    private String externalSignFormat;
}
