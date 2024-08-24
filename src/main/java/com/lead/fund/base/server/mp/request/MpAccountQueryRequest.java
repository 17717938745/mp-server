package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * AccountQueryRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 19:28
 */
@Data
@Accessors(chain = true)
@ToString
public class MpAccountQueryRequest implements Serializable {

    private static final long serialVersionUID = -7096126141894413212L;
    /**
     * 小程序id
     */
    private String openId;
    /**
     * 昵称
     */
    private String nickname;
}
