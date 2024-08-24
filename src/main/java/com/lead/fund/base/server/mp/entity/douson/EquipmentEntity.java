package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdministrator;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * EquipmentEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_INDUSTRY_EQUIPMENT")
@NoArgsConstructor
@ClassFunction("事故记录")
@Accessors(chain = true)
public class EquipmentEntity extends AbstractAdministrator {

    private static final long serialVersionUID = -2958798731405959766L;
    /**
     * 设备编号
     */
    @FieldRemark(value = "设备编号")
    private String equipmentNo;
    /**
     * 设备名称
     */
    @FieldRemark(value = "设备名称")
    private String equipmentName;
    /**
     * 规格
     */
    @FieldRemark(value = "规格")
    private String specification;
    /**
     * 日期
     */
    @FieldRemark(value = "日期")
    @Size(max = 32)
    private String date;
    /**
     * 使用人员
     */
    @FieldRemark(value = "使用人员")
    @Size(max = 64)
    private String userId;
    /**
     * 设备细节描述
     */
    @FieldRemark(value = "设备细节描述")
    private String detailDescribe;
    /**
     * 设备细节描述
     */
    @FieldRemark(value = "设备细节描述")
    private String gasolineType;
    /**
     * 负责人
     */
    @FieldRemark(value = "负责人")
    @Size(max = 64)
    private String chargeUser;
    /**
     * 位置
     */
    @FieldRemark(value = "位置")
    private String position;
}
