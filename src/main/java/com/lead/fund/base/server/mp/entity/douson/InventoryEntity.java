package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * InventoryEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_INVENTORY")
@NoArgsConstructor
@ClassFunction("计划外库存")
@Accessors(chain = true)
public class InventoryEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096010700L;
    /**
     * 物料号
     */
    @FieldRemark(value = "物料号")
    @Size(max = 64, message = "物料号长度不合法")
    private String materialNo;
    /**
     * 物料描述
     */
    @FieldRemark(value = "物料描述")
    @Size(max = 256, message = "物料描述长度不合法")
    private String materialDescription;
    /**
     * 图号
     */
    @FieldRemark(value = "图号")
    @Size(max = 128, message = "图号长度不合法")
    private String designNumber;
    /**
     * 库存数量
     */
    @FieldRemark(value = "库存数量")
    @Size(max = 16, message = "库存数量长度不合法")
    private BigDecimal inventoryCount;
    /**
     * 库存日期
     */
    @FieldRemark(value = "库存日期")
    @Size(max = 32, message = "库存日期长度不合法")
    private String inventoryDate;
    /**
     * 类型
     */
    @FieldRemark(value = "类型")
    @Size(max = 64, message = "类型长度不合法")
    private String type;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 256, message = "备注长度不合法")
    private String description;
    /**
     * 领料数量
     */
    @FieldRemark(value = "领料数量")
    @Size(max = 16, message = "领料数量长度不合法")
    private BigDecimal materialCount;
    /**
     * 领料日期
     */
    @FieldRemark(value = "领料日期")
    @Size(max = 32, message = "领料日期长度不合法")
    private String materialDate;
}
