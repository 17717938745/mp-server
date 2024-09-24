package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ForumCommentaryRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ForumCommentaryRequest implements Serializable {

    private static final long serialVersionUID = 5945041243096726020L;
    /**
     * 论坛ID
     */
    private String forumId;
    /**
     * 评论ID
     */
    private String commentaryId;
    /**
     * 上级评论ID
     */
    private String parentId;
    /**
     * 内容
     */
    private String content;
}
