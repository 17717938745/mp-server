package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ForumRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class ForumRequest implements Serializable {

    private static final long serialVersionUID = 5945041243096726010L;
    /**
     * 论坛ID
     */
    private String forumId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * H5的ID
     */
    private String h5Id;
    /**
     * H5的ID列表
     */
    private List<String> h5IdList;
    /**
     * 标题
     */
    private String title;
    /**
     * 点赞
     */
    private BigDecimal thumbsUp;
    /**
     * 评论
     */
    private BigDecimal commentary;
    /**
     * 分类，processGroupCheck：工艺组检查；ehsSafetyGroupCheck：EHS安全组检查；qualityDepartmentCheck：质量部检查；其他：首页；
     */
    private String category;

}
