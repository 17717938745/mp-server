package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.request.AbstractPageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * InventoryPageRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@Accessors(chain = true)
public class InventoryPageRequest extends AbstractPageRequest<InventoryRequest> {

    private static final long serialVersionUID = 5945041243096010703L;

    public InventoryPageRequest() {
        setData(new InventoryRequest());
    }
}