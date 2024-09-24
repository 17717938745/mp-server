package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.api.frame.TreeItemApi;
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
 * ForumCommentaryEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@TableName("MP_FORUM_COMMENTARY")
@NoArgsConstructor
@ClassFunction("论坛评论")
@Accessors(chain = true)
public class ForumCommentaryEntity extends AbstractAdministrator implements TreeItemApi<String> {

    private static final long serialVersionUID = -8361023609419505016L;
    /**
     * 论坛ID
     */
    @FieldRemark(value = "论坛ID")
    @Size(max = 64, message = "论坛ID长度不合法")
    private String forumId;
    /**
     * 上级评论ID
     */
    @FieldRemark(value = "上级评论ID")
    @Size(max = 64, message = "上级评论ID长度不合法")
    private String parentId;
    /**
     * 用户ID
     */
    @FieldRemark(value = "用户ID")
    @Size(max = 64, message = "用户ID长度不合法")
    private String userId;
    /**
     * 内容
     */
    @FieldRemark(value = "内容")
    private String content;
}
