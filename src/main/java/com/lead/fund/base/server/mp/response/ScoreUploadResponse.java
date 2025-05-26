package com.lead.fund.base.server.mp.response;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ScoreUploadResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@Accessors(chain = true)
public class ScoreUploadResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 4083223105492336504L;
    private BigDecimal uploadCount;
    private BigDecimal uploadDetailCount;
    private BigDecimal afterCount;
    private BigDecimal afterDetailCount;
}
