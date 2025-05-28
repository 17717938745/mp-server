package com.lead.fund.base.server.mp.response;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ScoreSummaryResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ScoreSummaryResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 8615319736399784093L;
    /**
     * 评比结果
     */
    private String evaluationResult;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 百分比
     */
    private BigDecimal percent;
    private String percentFormat;
}
