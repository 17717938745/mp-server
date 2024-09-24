package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ForumResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ForumResponse implements Serializable {

    private static final long serialVersionUID = 5945041243096726012L;
    /**
     * 论坛ID
     */
    private String forumId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 用户ID
     */
    private String userId;
    private String userIdFormat;
    /**
     * H5的ID
     */
    private String h5Id;
    /**
     * 点赞
     */
    private BigDecimal thumbsUp;
    /**
     * 当前登录用户点赞情况，0-未操作，1-点赞，2-踩
     */
    private int thumbsUpType;
    /**
     * 评论
     */
    private BigDecimal commentary;
}
