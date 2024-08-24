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
import lombok.experimental.Accessors;

/**
 * OrderEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-11 10:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@NoArgsConstructor
@TableName("MP_INDUSTRY_ORDER")
@ClassFunction("订单")
@Accessors(chain = true)
public class OrderEntity extends AbstractPrimaryKey {

    private static final long serialVersionUID = 1290544322994587440L;
    /**
     * 订单
     */
    @FieldRemark(value = "订单", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "订单长度不合法")
    private String orderNo;
    /**
     * 项次
     */
    @FieldRemark(value = "项次", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "项次长度不合法")
    private String projectSequence;
    /**
     * 调试设备
     */
    @FieldRemark(value = "调试设备", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "调试设备长度不合法")
    private String testDevice;
    /**
     * 加工工序
     */
    @FieldRemark(value = "加工工序", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "加工工序长度不合法")
    private String processProcedure;
    /**
     * 产品数量
     */
    @FieldRemark(value = "产品数量")
    private BigDecimal orderCount;
    /**
     * 完成数量
     */
    private BigDecimal actualCompleteCount;
}