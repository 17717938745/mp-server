package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.request.AbstractPageRequest;
import com.lead.fund.base.server.mp.entity.douson.ParamEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ParamPageRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ToString
public class ParamPageRequest extends AbstractPageRequest<ParamEntity> {

    private static final long serialVersionUID = 5945041243096726178L;

    public ParamPageRequest() {
        this.setData(new ParamEntity());
    }
}
