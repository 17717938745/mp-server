package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * BoxSummaryDescribeResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BoxSummaryDescribeResponse implements Serializable {

    private static final long serialVersionUID = -2958798731405959806L;
    /**
     * 标题
     */
    private String title;
}
