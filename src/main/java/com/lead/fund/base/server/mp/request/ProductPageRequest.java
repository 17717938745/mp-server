package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.request.AbstractPageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ProductPageRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ToString
public class ProductPageRequest extends AbstractPageRequest<ProductQueryRequest> {

    private static final long serialVersionUID = 5945041243096726179L;

    public ProductPageRequest() {
        this.setData(new ProductQueryRequest());
    }
}
