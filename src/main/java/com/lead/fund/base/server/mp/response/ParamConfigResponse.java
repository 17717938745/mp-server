package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.OptionItem;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * ParamConfigResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-23 17:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ParamConfigResponse extends OptionItem<Object> {

    private static final long serialVersionUID = -2424070101315497078L;
}
