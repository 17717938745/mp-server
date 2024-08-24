package com.lead.fund.base.server.mp.entity.douson;

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

/**
 * DeviceEntity
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
@TableName("MP_INDUSTRY_DEVICE")
@ClassFunction("设备")
public class DeviceEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = 1290544322994587440L;
    /**
     * 设备名称
     */
    @FieldRemark(value = "设备名称")
    private String deviceName;
    /**
     * 单价
     */
    @FieldRemark(value = "单价", defaultValue = "0")
    private BigDecimal unitPrice;
    /**
     * 运行小时
     */
    @FieldRemark(value = "运行小时", defaultValue = "0")
    private BigDecimal runningHour;
    /**
     * 运行分钟
     */
    @FieldRemark(value = "运行分钟", defaultValue = "0")
    private BigDecimal runningMinute;
    /**
     * 排序
     */
    @FieldRemark(value = "排序", defaultValue = "0")
    @Size(max = 8, message = "排序长度最大为8")
    private Integer sorter;
}