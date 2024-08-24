package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * SignInResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SignInResponse implements Serializable {

    private static final long serialVersionUID = -6346202654210300171L;
    /**
     * 用户id
     */
    private MpUserResponse user;
}
