package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MpAccountRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 10:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MpAccountRequest implements Serializable {

    private static final long serialVersionUID = -3980136865057151271L;
    /**
     * 小程序id
     */
    private String openId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像链接
     */
    private String avatarUrl;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 状态，0-正常 1-删除 {@link com.lead.fund.base.common.basic.cons.frame.AdminState}
     */
    private Integer state;
}
