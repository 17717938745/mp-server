package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MpMobileResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 10:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MpMobileResponse implements Serializable {

    private static final long serialVersionUID = 5604860321716245689L;
    private String mobile;
    private String pureMobile;
    private String countryCode;
}
