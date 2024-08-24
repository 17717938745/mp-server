package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * MpRoleResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:41
 */
@Data
@ToString
public class MpRoleResponse implements Serializable {

    private static final long serialVersionUID = 6886198438973390778L;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色备注
     */
    private String roleRemark;
    /**
     * 标签（分组）
     */
    private String tag;
}
