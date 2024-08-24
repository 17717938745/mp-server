package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MpAccountResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 10:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MpAccountResponse implements Serializable {

    private static final long serialVersionUID = 5604860321716245689L;
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
     * 小程序id
     */
    private String openId;
    /**
     * 角色列表
     */
    private List<MpRoleResponse> roleList;
    /**
     * 状态，0-正常 1-删除 {@link com.lead.fund.base.common.basic.cons.frame.AdminState}
     */
    private Integer state;
}
