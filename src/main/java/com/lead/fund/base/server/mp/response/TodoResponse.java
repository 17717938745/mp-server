package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * TodoResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-23 17:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TodoResponse implements Serializable {

    private static final long serialVersionUID = 6855908741334479521L;
    /**
     * 数量
     */
    private long count;
    /**
     * 明细
     */
    private List<TodoData> list;
}
