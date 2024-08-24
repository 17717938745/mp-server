package com.lead.fund.base.server.mp.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * EventQueryRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ImproveQueryRequest implements Serializable {

    private static final long serialVersionUID = -2834989195650436909L;
    /**
     * 事故ID
     */
    private String improveId;
    /**
     * 活动时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startReportDate;
    /**
     * 活动时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endReportDate;
    /**
     * 日报id
     */
    private String reportId;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 活动时间 @DateTimeFormat(pattern = "yyyy-MM-dd")
     */
    private String reportDate;
    /**
     * 查询用户ID
     */
    private String queryUserId;
    /**
     * 操作人
     */
    private String userId;
    private List<String> userIdList;
    /**
     * 用户名
     */
    private String username;
    /**
     * 原因
     */
    private String reason;
    /**
     * 原因
     */
    private List<String> reasonList;
    /**
     * 是否有效
     */
    private Boolean valid;
}
