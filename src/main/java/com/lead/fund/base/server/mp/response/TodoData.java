package com.lead.fund.base.server.mp.response;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * TodoData
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-23 17:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TodoData implements Serializable {

    private static final long serialVersionUID = -5438787515711380636L;
    /**
     * ID
     */
    private String id;
    /**
     * 类型，0-不合格单，1-EHS，2-改善，3-设备维修记录，4-质量事故报告
     */
    private Integer type;
    /**
     * 文案
     */
    private String label;
    /**
     * 场景列表
     */
    private List<String> sceneList;
}
