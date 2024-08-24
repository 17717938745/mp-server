package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.request.AbstractPageRequest;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * BoxFlagPageRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@Accessors(chain = true)
public class BoxFlagPageRequest extends AbstractPageRequest<BoxFlagRequest> {

    private static final long serialVersionUID = -8273199494589663967L;

    public BoxFlagPageRequest() {
        super.setData(new BoxFlagRequest());
    }
}
