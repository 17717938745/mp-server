package com.lead.fund.base.server.mp.response;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ForumCommentaryResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ForumCommentaryResponse extends com.lead.fund.base.common.basic.api.frame.AbstractTree<String, ForumCommentaryResponse> implements com.lead.fund.base.common.basic.api.frame.TreeItemApi<String> {

    private static final long serialVersionUID = 5945041243096726019L;
    /**
     * 论坛ID
     */
    private String forumId;
    private String createdTimeFormat;
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
    /**
     * 用户ID
     */
    private String userId;
    private String userIdFormat;

    @Override
    public String getId() {
        return this.commentaryId;
    }
}
