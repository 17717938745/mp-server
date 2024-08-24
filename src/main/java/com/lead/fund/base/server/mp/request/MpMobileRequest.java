package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MpMobileRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 10:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MpMobileRequest implements Serializable {

    private static final long serialVersionUID = 8344427245095563684L;
    private String encryptedData;
    private String iv;
}
