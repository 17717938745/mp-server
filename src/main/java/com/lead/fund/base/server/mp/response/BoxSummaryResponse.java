package com.lead.fund.base.server.mp.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * BoxSummaryResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BoxSummaryResponse implements Serializable {

    private static final long serialVersionUID = -2958798731405959806L;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 客户简称
     */
    private String customerShortName;
    private String customerShortNameFormat;
    /**
     * 每箱数量
     */
    private Integer sumEachBoxCount;
    /**
     * 箱号
     */
    private List<Integer> boxNumberList;
    private Integer sumBoxNumber;
    /**
     * 每箱重量
     */
    private BigDecimal sumEachBoxWeight;
}
