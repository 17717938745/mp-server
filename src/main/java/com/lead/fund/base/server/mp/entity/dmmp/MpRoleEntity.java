package com.lead.fund.base.server.mp.entity.dmmp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 角色
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-09 21:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName("MP_ROLE")
@ClassFunction("角色表")
public class MpRoleEntity extends AbstractAdmin {

    private static final long serialVersionUID = -3212788055198804308L;
    /**
     * 角色编码
     */
    @FieldRemark(value = "角色编码", unique = true, constraintType = "UNIQUE")
    @Size(max = 32, message = "角色编码长度最大为32")
    @NotNull
    private String roleCode;
    /**
     * 角色名称
     */
    @FieldRemark(value = "角色名称")
    @Size(max = 32, message = "角色名称长度最大为32")
    private String roleName;
    /**
     * 角色备注
     */
    @FieldRemark(value = "角色备注")
    @Size(max = 256, message = "角色备注长度最大为256")
    private String roleRemark;
    /**
     * 标签
     */
    @FieldRemark(value = "标签")
    @Size(max = 256, message = "标签长度最大为256")
    private String tag;
}