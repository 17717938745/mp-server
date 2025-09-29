package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.api.frame.AbstractTree;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * DepartRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-06-01 09:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DepartRequest extends AbstractTree<String, DepartRequest> {

    private static final long serialVersionUID = 7127848185721427016L;
    /**
     * 登录历史ID
     */
    private String id;
    private String label;

    @Override
    public String getId() {
        return this.id;
    }
}
