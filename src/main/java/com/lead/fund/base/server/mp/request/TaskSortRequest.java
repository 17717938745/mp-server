package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * TaskSortRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TaskSortRequest implements Serializable {

    private static final long serialVersionUID = 5945041243096001098L;
    /**
     * 生产计划ID
     */
    private List<TaskSortData> taskList;
}
