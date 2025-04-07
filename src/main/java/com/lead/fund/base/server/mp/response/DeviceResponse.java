package com.lead.fund.base.server.mp.response;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * DeviceResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-11 10:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class DeviceResponse extends AbstractPrimaryKey {

    private static final long serialVersionUID = 1290544322994587150L;
    /**
     * 设备ID
     */
    private String id;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    /**
     * 运行小时
     */
    private BigDecimal runningHour;
    /**
     * 运行分钟
     */
    private BigDecimal runningMinute;
    /**
     * 是否归属供应商，供应商和自己公司计算逻辑不一样
     */
    private Boolean supplier;
    private String supplierFormat;
    /**
     * 排序
     */
    private Integer sorter;
    /**
     * 管理人
     */
    private String manager;
    private String managerFormat;
}