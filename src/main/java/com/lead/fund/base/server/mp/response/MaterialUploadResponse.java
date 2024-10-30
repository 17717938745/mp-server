package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.database.entity.AbstractAdmin;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * MaterialUploadResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@Accessors(chain = true)
public class MaterialUploadResponse implements Serializable {

    private static final long serialVersionUID = 5945041243096001065L;
    private BigDecimal uploadCount;
    private BigDecimal uploadDetailCount;
    private BigDecimal afterCount;
    private BigDecimal afterDetailCount;
}
