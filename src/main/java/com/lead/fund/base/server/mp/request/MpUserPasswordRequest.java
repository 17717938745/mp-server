package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

/**
 * MpUserPasswordRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:41
 */
@Data
@ToString
public class MpUserPasswordRequest implements Serializable {

    private static final long serialVersionUID = 4288951200576811128L;
    /**
     * 旧密码
     */
    private String oldPassword;
    /**
     * 密码
     */
    private String password;
}
