package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * IndustryReportSerialNoEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_INDUSTRY_REPORT_SERIAL_NO")
@NoArgsConstructor
@AllArgsConstructor
@ClassFunction("零件")
@Accessors(chain = true)
public class ReportSerialNoEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = 5945041243096726178L;
    /**
     * 日报id
     */
    @FieldRemark(value = "日报id", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "日报id长度不合法")
    private String reportId;
    /**
     * 订单ID
     */
    @FieldRemark(value = "订单ID", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "长度不对")
    private String orderId;
    /**
     * 序列号
     */
    @FieldRemark(value = "序列号", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "长度不对")
    private String serialNo;
}
