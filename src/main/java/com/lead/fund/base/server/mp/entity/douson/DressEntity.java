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
 * DressEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_DRESS")
@NoArgsConstructor
@ClassFunction("工装管理")
@Accessors(chain = true)
public class DressEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096010700L;
    /**
     * 工装类型
     */
    @FieldRemark(value = "工装类型")
    @Size(max = 64, message = "工装类型长度不合法")
    private String workDressType;
    /**
     * 物料号
     */
    @FieldRemark(value = "物料号")
    @Size(max = 64, message = "物料号长度不合法")
    private String materialNo;
    /**
     * 描述
     */
    @FieldRemark(value = "描述")
    @Size(max = 512, message = "描述长度不合法")
    private String remark;
    /**
     * 图号
     */
    @FieldRemark(value = "图号")
    @Size(max = 128, message = "图号长度不合法")
    private String designNumber;
    /**
     * 申请数量
     */
    @FieldRemark(value = "申请数量")
    private BigDecimal applyCount;
    /**
     * 申请日期
     */
    @FieldRemark(value = "申请日期")
    @Size(max = 32, message = "申请日期长度不合法")
    private String applyDate;
    /**
     * 库存位置
     */
    @FieldRemark(value = "库存位置")
    @Size(max = 128, message = "库存位置长度不合法")
    private String storePosition;
    /**
     * 验收
     */
    @FieldRemark(value = "验收")
    @Size(max = 128, message = "验收长度不合法")
    private String checkAcceptUser;
    /**
     * 备注（注明为哪个订单服务）
     */
    @FieldRemark(value = "备注（注明为哪个订单服务）")
    @Size(max = 512, message = "备注（注明为哪个订单服务）长度不合法")
    private String descriptionOfOrder;
    /**
     * 入库数量
     */
    @FieldRemark(value = "入库数量")
    private BigDecimal storeCount;
    /**
     * 入库日期备注
     */
    @FieldRemark(value = "入库日期备注")
    @Size(max = 512, message = "入库日期备注长度不合法")
    private String storeDateDescription;
}
