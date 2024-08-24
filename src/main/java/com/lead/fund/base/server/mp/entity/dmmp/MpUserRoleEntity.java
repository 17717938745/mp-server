package com.lead.fund.base.server.mp.entity.dmmp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用户和角色关联关系
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-09 21:54
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MP_USER_ROLE")
@ClassFunction(value = "用户和角色关联关系表")
@Accessors(chain = true)
public class MpUserRoleEntity implements Serializable, Comparable<MpUserRoleEntity> {

    private static final long serialVersionUID = -909740997157887309L;
    /**
     * 用户id
     */
    @MppMultiId
    @FieldRemark(value = "用户id")
    @Size(max = 64, message = "用户id长度最大为64")
    private String userId;
    /**
     * 角色id
     */
    @MppMultiId
    @FieldRemark(value = "角色id")
    @Size(max = 64, message = "角色id长度最大为64")
    private String roleId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MpUserRoleEntity that = (MpUserRoleEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }

    @Override
    public int compareTo(MpUserRoleEntity o) {
        int r = this.getUserId().compareTo(o.getUserId());
        if (r != 0) {
            return r;
        } else {
            return this.getRoleId().compareTo(o.getRoleId());
        }
    }
}