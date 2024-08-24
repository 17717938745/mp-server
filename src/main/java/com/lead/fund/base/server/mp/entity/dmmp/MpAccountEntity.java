package com.lead.fund.base.server.mp.entity.dmmp;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;

/**
 * MpAccountEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-06-01 09:57
 */
@Data
@TableName("MP_ACCOUNT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ClassFunction("小程序用户")
public class MpAccountEntity implements Serializable {

    private static final long serialVersionUID = 8061641274431967698L;
    /**
     * 状态，0-正常 1-删除 {@link com.lead.fund.base.common.basic.cons.frame.AdminState}
     */
    @FieldRemark(value = "状态，0-正常 1-删除", defaultValue = "0")
    @TableField(fill = FieldFill.INSERT)
    @Size(max = 2, message = "状态长度最大为2")
    private Integer state;
    /**
     * 小程序open-id
     */
    @TableId
    @FieldRemark(value = "小程序open-id")
    @Size(max = 128, message = "小程序open-id长度不合法")
    private String openId;
    /**
     * 昵称
     */
    @FieldRemark(value = "昵称")
    @Size(max = 128, message = "昵称长度不合法")
    private String nickname;
    /**
     * 头像链接
     */
    @FieldRemark(value = "头像链接")
    @Size(max = 128, message = "昵称长度不合法")
    private String avatarUrl;
    /**
     * 性别 {@link com.lead.fund.base.common.basic.cons.account.GenderType}
     */
    @FieldRemark(value = "性别")
    @Size(max = 4, message = "头像链接内容长度不合法")
    private Integer gender;
    /**
     * session-key
     */
    @FieldRemark(value = "session-key")
    @Size(max = 256, message = "session-key长度不合法")
    private String sessionKey;
    /**
     * union-id
     */
    @FieldRemark(value = "union-id")
    @Size(max = 256, message = "union-id长度不合法")
    private String unionId;
    /**
     * 登录、注册时间
     */
    @FieldRemark(value = "登录、注册时间")
    private Date signTime;
    /**
     * 手机号
     */
    @FieldRemark(value = "手机号")
    private String mobile;
}
