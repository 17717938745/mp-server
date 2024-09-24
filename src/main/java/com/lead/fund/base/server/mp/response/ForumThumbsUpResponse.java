package com.lead.fund.base.server.mp.response;

import java.io.Serializable;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ForumThumbsUpResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ForumThumbsUpResponse implements Serializable {

    private static final long serialVersionUID = 5945041243096726018L;
    /**
     * 赞同变化数量
     */
    private int thumbsUpChange;
    /**
     * 当前登录用户点赞情况，0-未操作，1-点赞，2-踩
     */
    private int thumbsUpType;
}
