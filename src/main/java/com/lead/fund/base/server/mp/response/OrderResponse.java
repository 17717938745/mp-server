package com.lead.fund.base.server.mp.response;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * OrderEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-11 10:58
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse implements Serializable {

    private static final long serialVersionUID = 1290544322994587440L;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 订单
     */
    private String orderNo;
    /**
     * 项次
     */
    private String projectSequence;
    /**
     * 调试设备
     */
    private String testDevice;
    private String testDeviceFormat;
    /**
     * 加工工序
     */
    private String processProcedure;
    private String processProcedureFormat;
    /**
     * 产品数量
     */
    private BigDecimal orderCount;
    /**
     * 排序
     */
    private Integer sorter;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 日报id
     */
    private String reportId;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 完成数量
     */
    private BigDecimal actualCompleteCount;
}