package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * SignInRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:41
 */
@Data
@ToString
public class SignInRequest implements Serializable {

    private static final long serialVersionUID = -7478952409966989768L;
    /**
     * 手机号或者用户名
     */
    private String username;
    /**
     * 密码密文，取默认的的salt，对密码明文进行sm4加密
     */
    private String passwordEncrypt;
}
