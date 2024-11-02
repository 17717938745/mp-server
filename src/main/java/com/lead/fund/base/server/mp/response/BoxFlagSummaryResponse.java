package com.lead.fund.base.server.mp.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * BoxFlagSummaryResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BoxFlagSummaryResponse implements Serializable {

    private static final long serialVersionUID = -254617081629394076L;
    /**
     * 每箱数量
     */
    private Integer sumEachBoxCount;
}