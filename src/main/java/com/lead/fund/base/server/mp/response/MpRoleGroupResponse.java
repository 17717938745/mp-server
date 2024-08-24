package com.lead.fund.base.server.mp.response;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * MpRoleGroupResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:41
 */
@Data
@ToString
@Accessors(chain = true)
public class MpRoleGroupResponse implements Serializable {

    private static final long serialVersionUID = 6886198438973390110L;
    /**
     * 标签
     */
    private String tag;
    /**
     * 文字
     */
    private String label;
    /**
     * 列表
     */
    private List<MpRoleResponse> list;

}
