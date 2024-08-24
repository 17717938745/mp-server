package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * MpUserConfigResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:41
 */
@Data
@ToString
@Accessors(chain = true)
public class MpUserConfigResponse implements Serializable {

    private static final long serialVersionUID = -9010755393080733977L;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
}
