package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * SchedulingDetailWrapperResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class SchedulingDetailWrapperResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = -2958798731405959907L;
    /**
     * 基本描述
     */
    private SchedulingResponse scheduling;
    /**
     * 明细列表
     */
    private List<SchedulingDetailResponse> schedulingList;
    /**
     * 高亮用户列表
     */
    private List<String> highLightUserIdList;
}
