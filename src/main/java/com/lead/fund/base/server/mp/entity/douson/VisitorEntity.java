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
@TableName("MP_VISITOR")
@NoArgsConstructor
@ClassFunction("访客")
@Accessors(chain = true)
public class VisitorEntity extends AbstractAdmin {

    private static final long serialVersionUID = 5945041243096010700L;
    /**
     * 申请日期
     */
    @FieldRemark(value = "申请日期")
    @Size(max = 32, message = "申请日期长度不合法")
    private String applyDate;
    /**
     * 申请人
     */
    @FieldRemark(value = "申请人")
    @Size(max = 64, message = "申请人长度不合法")
    private String applicant;
    /**
     * 访客姓名
     */
    @FieldRemark(value = "访客姓名")
    @Size(max = 128, message = "访客姓名长度不合法")
    private String visitorName;
    /**
     * 手机号
     */
    @FieldRemark(value = "手机号")
    @Size(max = 32, message = "手机号长度不合法")
    private String phoneNumber;
    /**
     * 公司名称
     */
    @FieldRemark(value = "公司名称")
    @Size(max = 256, message = "公司名称长度不合法")
    private String companyName;
    /**
     * 来访内容
     */
    @FieldRemark(value = "来访内容")
    @Size(max = 512, message = "来访内容长度不合法")
    private String visitContent;
    /**
     * 预计来访时间
     */
    @FieldRemark(value = "预计来访时间")
    @Size(max = 32, message = "预计来访时间长度不合法")
    private String expectedVisitTime;
    /**
     * 预计访问结束时间
     */
    @FieldRemark(value = "预计访问结束时间")
    @Size(max = 32, message = "预计访问结束时间长度不合法")
    private String expectedEndTime;
    /**
     * 联系人
     */
    @FieldRemark(value = "联系人")
    @Size(max = 64, message = "联系人长度不合法")
    private String contactPerson;
    /**
     * 来访部门
     */
    @FieldRemark(value = "来访部门")
    @Size(max = 256, message = "来访部门长度不合法")
    private String visitDepartment;
    /**
     * 批准人
     */
    @FieldRemark(value = "批准人")
    @Size(max = 64, message = "批准人长度不合法")
    private String approver;
    /**
     * 证件及本人照片（5张）
     */
    @FieldRemark(value = "证件及本人照片（5张）")
    @Size(max = 32, message = "证件及本人照片（5张）长度不合法")
    private BigDecimal idAndPhotos;
    /**
     * 备注
     */
    @FieldRemark(value = "备注")
    @Size(max = 512, message = "备注长度不合法")
    private String remarks;
    /**
     * 出厂记录（5张）
     */
    @FieldRemark(value = "出厂记录（5张）")
    @Size(max = 32, message = "出厂记录（5张）长度不合法")
    private BigDecimal factoryRecords;
    /**
     * 访客出厂日期
     */
    @FieldRemark(value = "访客出厂日期")
    @Size(max = 32, message = "访客出厂日期长度不合法")
    private String visitorFactoryDate;
    /**
     * 打印单号
     */
    @FieldRemark(value = "打印单号")
    @Size(max = 64, message = "打印单号长度不合法")
    private String printNumber;
    /**
     * 结案
     */
    private Boolean valid;
}
