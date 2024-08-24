package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * MpSignRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:24
 */
@Data
@ToString
public class MpSignRequest implements Serializable {

    private static final long serialVersionUID = 961501704515745009L;
    /**
     * 小程序登录凭证
     */
    private String code;
}
