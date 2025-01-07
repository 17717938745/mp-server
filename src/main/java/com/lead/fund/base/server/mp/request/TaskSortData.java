package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.database.entity.AbstractAdmin;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * TaskSortData
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TaskSortData implements Serializable {

    private static final long serialVersionUID = 5945041243096001099L;
    /**
     * 生产计划ID
     */
    private String taskId;
}
