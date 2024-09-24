package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ForumThumbsUpEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@TableName("MP_FORUM_THUMBS_UP")
@NoArgsConstructor
@ClassFunction("论坛点赞")
@Accessors(chain = true)
public class ForumThumbsUpEntity implements Serializable {

    private static final long serialVersionUID = -8361023609419505017L;
    /**
     * 论坛ID
     */
    @MppMultiId
    @FieldRemark(value = "论坛ID")
    @Size(max = 64, message = "论坛ID长度不合法")
    private String forumId;
    /**
     * 用户ID
     */
    @MppMultiId
    @FieldRemark(value = "用户ID")
    @Size(max = 64, message = "用户ID长度不合法")
    private String userId;
    /**
     * 是否点赞
     */
    @FieldRemark(value = "是否点赞")
    private Boolean thumbsUp;
}
