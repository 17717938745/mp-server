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

/**
 * ComputerEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_COMPUTER")
@NoArgsConstructor
@ClassFunction("检查设备台账")
@Accessors(chain = true)
public class ComputerEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096001178L;
    /**
     * 物料号
     */
    @FieldRemark(value = "物料号")
    @Size(max = 256, message = "物料号长度不合法")
    private String materialNo;
    /**
     * 品牌
     */
    @FieldRemark(value = "品牌")
    @Size(max = 256, message = "品牌长度不合法")
    private String brand;
    /**
     * 设备名称
     */
    @FieldRemark(value = "设备名称")
    @Size(max = 256, message = "设备名称长度不合法")
    private String name;
    /**
     * 设备型号
     */
    @FieldRemark(value = "设备型号")
    @Size(max = 256, message = "设备型号长度不合法")
    private String model;
    /**
     * 使用人
     */
    @FieldRemark(value = "使用人")
    @Size(max = 256, message = "使用人长度不合法")
    private String userId;
    /**
     * 位置
     */
    @FieldRemark(value = "位置")
    @Size(max = 256, message = "位置长度不合法")
    private String position;
    /**
     * 入库日期
     */
    @FieldRemark(value = "入库日期")
    @Size(max = 256, message = "入库日期长度不合法")
    private String storageDate;
    /**
     * 状态
     */
    @FieldRemark(value = "状态")
    @Size(max = 256, message = "状态长度不合法")
    private String computerState;
    /**
     * 是否有账
     */
    @FieldRemark(value = "是否有账")
    private Boolean detailed;
    /**
     * 设备原产地
     */
    @FieldRemark(value = "设备原产地")
    @Size(max = 256, message = "设备原产地长度不合法")
    private String productPlace;
    /**
     * 供应商
     */
    @FieldRemark(value = "供应商")
    @Size(max = 256, message = "供应商长度不合法")
    private String supplier;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 256, message = "备注长度不合法")
    private String remark;
}
