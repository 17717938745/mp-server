package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.request.AbstractPageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * OrderPageRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 19:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ToString
public class OrderPageRequest extends AbstractPageRequest<OrderQueryRequest> {

    private static final long serialVersionUID = -628976701158203209L;

    public OrderPageRequest() {
        setData(new OrderQueryRequest());
    }
}
