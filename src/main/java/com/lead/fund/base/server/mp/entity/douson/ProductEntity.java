package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * IndustryProductEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 18:30
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_INDUSTRY_PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
@ClassFunction("零件")
@Accessors(chain = true)
public class ProductEntity extends AbstractAdministrator {

    private static final long serialVersionUID = 5945041243096726178L;
    /**
     * 小程序id
     */
    @FieldRemark(value = "小程序open-id", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "小程序open-id长度不合法")
    private String openId;
    /**
     * 日期
     */
    @FieldRemark(value = "日期", indexType = "UNIQUE", indexGroup = "0")
    private Date reportDate;
    /**
     * 图号
     */
    @FieldRemark(value = "图号", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "图号长度不合法")
    private String designNumber;
    /**
     * 订单编号
     */
    @FieldRemark(value = "订单编号", indexType = "UNIQUE", indexGroup = "0")
    @Size(max = 64, message = "订单编号长度不合法")
    private String orderId;
    /**
     * 程序号
     */
    @FieldRemark(value = "程序号")
    private String programNumber;
    /**
     * 调试时间/分钟
     */
    private Integer debugMinute;
    /**
     * 装夹时间/分钟
     */
    private Integer clampingMinute;
    /**
     * 辅助时间/分钟
     */
    private Integer assistMinute;
    /**
     * 程式运行时间/分钟
     */
    private Integer runningMinute;
}
