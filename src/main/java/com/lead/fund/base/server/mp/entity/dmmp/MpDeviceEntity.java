package com.lead.fund.base.server.mp.entity.dmmp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 设备（客户端唯一标志）
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-29 09:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName("MP_DEVICE")
@ClassFunction("设备")
@Accessors(chain = true)
public class MpDeviceEntity extends AbstractAdmin {

    private static final long serialVersionUID = -478723606577079640L;
    /**
     * 密钥
     */
    @FieldRemark("密钥")
    @Size(max = 32, message = "密钥长度不合法")
    @NotNull
    private String salt;
    /**
     * 用户id
     */
    @FieldRemark("用户id")
    @Size(max = 64, message = "用户id长度不合法")
    private String userId;
    /**
     * 账户id
     */
    @FieldRemark("账户id")
    @Size(max = 64, message = "账户id长度不合法")
    private String accountId;
    /**
     * 客户端ip
     */
    @FieldRemark("客户端ip")
    @Size(max = 128, message = "客户端ip长度不合法")
    private String ip;
}
