package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.request.AbstractPageRequest;
import com.lead.fund.base.server.mp.entity.dmmp.MpSignInHistoryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * SignInHistoryPageRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@Accessors(chain = true)
public class SignInHistoryPageRequest extends AbstractPageRequest<SignInHistoryRequest> {

    private static final long serialVersionUID = -8761589579505314112L;

    public SignInHistoryPageRequest() {
        super.setData(new SignInHistoryRequest());
    }
}
