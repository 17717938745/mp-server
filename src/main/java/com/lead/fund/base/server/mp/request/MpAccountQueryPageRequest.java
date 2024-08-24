package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.request.AbstractPageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * AccountQueryPageRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 19:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@ToString
public class MpAccountQueryPageRequest extends AbstractPageRequest<MpAccountQueryRequest> {

    private static final long serialVersionUID = -993207613393705047L;

    public MpAccountQueryPageRequest() {
        setData(new MpAccountQueryRequest());
    }
}
