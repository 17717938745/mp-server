package com.lead.fund.base.server.mp.entity.dmmp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * 用户
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-09 21:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName("MP_USER")
@ClassFunction(value = "用户表")
@Accessors(chain = true)
public class MpUserEntity extends AbstractAdmin {

    private static final long serialVersionUID = -3212788055198804308L;
    /**
     * 用户名
     */
    @FieldRemark(value = "用户名", unique = true, constraintType = "UNIQUE")
    @Size(max = 32, message = "用户名长度最大为32")
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 姓名
     */
    @FieldRemark(value = "姓名")
    @Size(max = 32, message = "姓名长度最大为32")
    private String name;
    /**
     * 昵称
     */
    @FieldRemark(value = "昵称")
    @Size(max = 128, message = "昵称长度最大为128")
    private String nickname;
    /**
     * 密码密文
     */
    @FieldRemark(value = "密码密文")
    @Size(max = 128, message = "密码密文长度最大为128")
    private String passwordEncrypt;
    /**
     * 密码密钥，为了做密码历史记录功能，同一个用户只生成唯一的密钥，不可被修改
     */
    @FieldRemark(value = "密码密钥")
    @Size(max = 32, message = "密码密钥长度最大为32")
    @Length
    private String salt;
    /**
     * 手机
     */
    @FieldRemark(value = "手机", defaultValue = "1", unique = true, constraintType = "UNIQUE")
    @Size(max = 64, message = "手机长度最大为64")
    private String mobile;
    /**
     * 邮箱
     */
    @FieldRemark(value = "邮箱")
    @Size(max = 256, message = "邮箱长度最大为256")
    private String mail;
    /**
     * 密码过期时间
     */
    @FieldRemark(value = "密码过期时间")
    private Date passwordExpireTime;
    /**
     * 登录是否锁定
     */
    @FieldRemark(value = "登录是否锁定", defaultValue = "0")
    @Size(max = 2, message = "登录是否锁定长度最大为2")
    private Boolean signInLock;
    /**
     * 登录错误次数
     */
    @FieldRemark(value = "登录错误次数", defaultValue = "0")
    @Size(max = 4, message = "登录错误次数长度最大为4")
    private Integer signInFailCount;
    /**
     * 登录锁定时间
     */
    @FieldRemark(value = "登录锁定时间")
    private Date signInLockTime;
    /**
     * 部门
     */
    @FieldRemark(value = "部门")
    @Size(max = 256, message = "部门")
    private String department;
    /**
     * 职位
     */
    @FieldRemark(value = "职位")
    @Size(max = 256, message = "职位")
    private String profession;
    /**
     * interview resume
     */
    @FieldRemark(value = "interview resume")
    private String interviewResume;
    /**
     * 人员属性
     */
    @FieldRemark(value = "人员属性")
    private String userProperty;
    /**
     * 班次
     */
    @FieldRemark(value = "班次")
    private String schedule;
    /**
     * 当班主管
     */
    @FieldRemark(value = "当班主管")
    private String leaderUserId;
    /**
     * 最后一次调薪日期
     */
    private String lastIncreaseSalaryDate;
    /**
     * 计划下次调薪日期
     */
    private String planIncreaseSalaryDate;
    /**
     * 国籍
     */
    @FieldRemark(value = "国籍")
    private String nationality;
}