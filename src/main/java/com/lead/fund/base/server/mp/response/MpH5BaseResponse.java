package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MpH5BaseResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-23 17:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MpH5BaseResponse implements Serializable {

    private static final long serialVersionUID = -5102590282487063007L;
    /**
     * 页面ID
     */
    private String h5Id;
}
