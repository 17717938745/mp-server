package com.lead.fund.base.server.mp.response;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * MpSignInHistoryResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-06-01 09:57
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MpSignInHistoryResponse implements Serializable {

    private static final long serialVersionUID = 7127848185721427016L;
    /**
     * 登录历史ID
     */
    private String signInHistoryId;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 登录时间
     */
    private String signInTime;
    /**
     * 客户端IP
     */
    private String clientIp;
    /**
     * 请求头
     */
    private String userAgent;
    /**
     * 是否成功
     */
    private Boolean success;
    private String successFormat;
    /**
     * 异常信息
     */
    private String errorMessage;
}
