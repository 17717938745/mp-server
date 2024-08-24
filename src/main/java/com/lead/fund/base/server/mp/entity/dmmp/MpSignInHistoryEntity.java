package com.lead.fund.base.server.mp.entity.dmmp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * MpSignInHistoryEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-06-01 09:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("MP_SIGN_IN_HISTORY")
@NoArgsConstructor
@ClassFunction("登录历史")
@Accessors(chain = true)
public class MpSignInHistoryEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = 7127848185721427016L;
    /**
     * 设备ID
     */
    @FieldRemark("设备ID")
    @Size(max = 64, message = "设备ID长度最大为64")
    private String deviceId;
    /**
     * 用户ID
     */
    @FieldRemark("用户ID")
    @Size(max = 64, message = "用户ID长度最大为64")
    private String userId;
    /**
     * 用户名
     */
    @FieldRemark("用户名")
    @Size(max = 64, message = "用户名长度最大为64")
    private String username;
    /**
     * 登录时间
     */
    @FieldRemark("登录时间")
    @Size(max = 64, message = "登录时间长度最大为64")
    private String signInTime;
    /**
     * 客户端IP
     */
    @FieldRemark("客户端IP")
    @Size(max = 64, message = "客户端IP长度最大为64")
    private String clientIp;
    /**
     * 请求头
     */
    @FieldRemark("客户端IP")
    @Size(max = 1024, message = "客户端IP长度最大为64")
    private String userAgent;
    /**
     * 是否成功
     */
    @FieldRemark("是否成功")
    private Boolean success;
    /**
     * 异常信息
     */
    @FieldRemark("异常信息")
    private String errorMessage;
}
