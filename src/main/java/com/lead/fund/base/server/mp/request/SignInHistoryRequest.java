package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.basic.request.AbstractPageRequest;
import com.lead.fund.base.server.mp.entity.dmmp.MpSignInHistoryEntity;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * SignInHistoryPageRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@Accessors(chain = true)
public class SignInHistoryRequest implements Serializable {

    private static final long serialVersionUID = -8761589579505314112L;

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
     * 公网IP
     */
    private Boolean publicIp;
}
