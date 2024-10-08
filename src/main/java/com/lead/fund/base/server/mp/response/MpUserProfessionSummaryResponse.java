package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * MpUserProfessionSummaryResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-03-28 20:41
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@Accessors(chain = true)
public class MpUserProfessionSummaryResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = 6886198438973390041L;
    private Integer index;
    private String department;
    private String departmentFormat;
    private String profession;
    private String professionFormat;
    private BigDecimal workShop;
    private BigDecimal office;
    private BigDecimal total;
}
