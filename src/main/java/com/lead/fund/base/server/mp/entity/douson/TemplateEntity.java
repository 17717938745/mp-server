package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * TemplateEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_TEMPLATE")
@NoArgsConstructor
@ClassFunction("供应商刀具模板")
@Accessors(chain = true)
public class TemplateEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096001050L;
    /**
     * 序号
     */
    @FieldRemark(value = "序号")
    @Size(max = 16, message = "序号")
    @TableField(value = "`INDEX`")
    private Integer index;
    /**
     * 借用方
     */
    @FieldRemark(value = "借用方")
    @Size(max = 64, message = "借用方长度不合法")
    private String borrowTemplatePerson;
    /**
     * 借用日期
     */
    @FieldRemark(value = "借用日期")
    @Size(max = 32, message = "借用日期长度不合法")
    private String borrowTemplateDate;
    /**
     * 物料号
     */
    @FieldRemark(value = "物料号")
    @Size(max = 256, message = "物料号长度不合法")
    private String materialNo;
    /**
     * 物料描述
     */
    @FieldRemark(value = "物料描述")
    @Size(max = 512, message = "物料描述长度不合法")
    private String materialDescription;
    /**
     * 数量
     */
    @FieldRemark(value = "数量")
    @Size(max = 32, message = "数量长度不合法")
    private BigDecimal templateCount;
    /**
     * 承诺归还日期
     */
    @FieldRemark(value = "承诺归还日期")
    @Size(max = 32, message = "承诺归还日期长度不合法")
    private String promiseReturnDate;
    /**
     * 经办人
     */
    @FieldRemark(value = "经办人")
    @Size(max = 64, message = "经办人长度不合法")
    private String operatorPerson;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 512, message = "备注长度不合法")
    private String description;
    /**
     * 已还数量
     */
    @FieldRemark(value = "已还数量")
    @Size(max = 32, message = "已还数量长度不合法")
    private BigDecimal returnCount;
    /**
     * 实际归还日期
     */
    @FieldRemark(value = "实际归还日期")
    @Size(max = 32, message = "实际归还日期长度不合法")
    private String actualReturnDate;
    /**
     * 是否符合要求
     */
    @FieldRemark(value = "是否符合要求")
    @Size(max = 1, message = "是否符合要求长度不合法")
    private Boolean meetRequirement;
    /**
     * 单号
     */
    @FieldRemark(value = "单号")
    @Size(max = 256, message = "单号长度不合法")
    private String templateOrderNo;
}
