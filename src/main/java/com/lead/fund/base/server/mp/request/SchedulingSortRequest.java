package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * SchedulingSortRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class SchedulingSortRequest implements Serializable {

    private static final long serialVersionUID = -2958798731405959906L;
    /**
     * label列表
     */
    private List<String> labelList;
    /**
     * 排班ID
     */
    private String schedulingId;
    /**
     * 某一月
     */
    private String dateMonth;
}
