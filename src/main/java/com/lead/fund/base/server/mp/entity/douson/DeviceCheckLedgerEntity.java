package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * DeviceCheckLedgerEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_DEVICE_CHECK_LEDGER")
@NoArgsConstructor
@ClassFunction("检查设备台账")
@Accessors(chain = true)
public class DeviceCheckLedgerEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096001178L;
    /**
     * 物料号
     */
    @FieldRemark(value = "物料号")
    @Size(max = 256, message = "物料号长度不合法")
    private String materialNo;
    /**
     * 设备编号
     */
    @FieldRemark(value = "设备编号")
    @Size(max = 256, message = "设备编号长度不合法")
    private String deviceNumber;
    /**
     * 中越文名称
     */
    @FieldRemark(value = "中越文名称")
    @Size(max = 256, message = "中越文名称长度不合法")
    private String chineseVietnamName;
    /**
     * 英文名称
     */
    @FieldRemark(value = "英文名称")
    @Size(max = 256, message = "英文名称长度不合法")
    private String englishName;
    /**
     * 规格
     */
    @FieldRemark(value = "规格")
    @Size(max = 256, message = "规格长度不合法")
    private String specification;
    /**
     * 校准单位
     */
    @FieldRemark(value = "校准单位")
    @Size(max = 256, message = "校准单位长度不合法")
    private String calibrationUnit;
    /**
     * 校准日期
     */
    @FieldRemark(value = "校准日期")
    @Size(max = 256, message = "校准日期长度不合法")
    private String calibrationDate;
    /**
     * 有效期(下次校验日期)
     */
    @FieldRemark(value = "有效期(下次校验日期)")
    @Size(max = 256, message = "有效期(下次校验日期)长度不合法")
    private String dueDate;
    /**
     * 校验周期（天）
     */
    @FieldRemark(value = "校验周期（天）")
    @Size(max = 16, message = "校验周期（天）长度不合法")
    private Integer calibrationPeriod;
    /**
     * 厂家
     */
    @FieldRemark(value = "厂家")
    @Size(max = 256, message = "厂家长度不合法")
    private String manufacturers;
    /**
     * 验收标准
     */
    @FieldRemark(value = "验收标准")
    @Size(max = 256, message = "验收标准长度不合法")
    private String acceptanceStandard;
    /**
     * 库位
     */
    @FieldRemark(value = "库位")
    @Size(max = 256, message = "库位长度不合法")
    private String storage;
    /**
     * 是否出库
     */
    @FieldRemark(value = "是否出库")
    private Boolean outOfStock;
    /**
     * 借用人
     */
    @FieldRemark(value = "借用人")
    @Size(max = 256, message = "借用人长度不合法")
    private String borrower;
    /**
     * 借用日期
     */
    @FieldRemark(value = "借用日期")
    @Size(max = 256, message = "借用日期长度不合法")
    private String borrowDate;
    /**
     * 状态
     */
    @FieldRemark(value = "状态")
    private String deviceCheckLedgerState;
}
