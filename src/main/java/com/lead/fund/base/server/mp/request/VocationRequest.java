package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * IndustryRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@Data
@Accessors(chain = true)
@ToString
public class VocationRequest implements Serializable {

    private static final long serialVersionUID = 5945041243096726177L;
    /**
     * 请假ID
     */
    private String vocationId;
    /**
     * 请假类型
     */
    private String vocationType;
    /**
     * 部门
     */
    private String department;
    /**
     * 申请日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    /**
     * 查询用户ID
     */
    private String queryUserId;
    /**
     * 请假人
     */
    private String user;
    private List<String> userIdList;
    /**
     * 主管领导
     */
    private String chargeUser;
    /**
     * 请假开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 请假结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 请假理由
     */
    private String reason;
    /**
     * 请假天数
     */
    private BigDecimal count;
    /**
     * 是否符合请假规定
     */
    private Boolean compliance;
    /**
     * 不符合理由
     */
    private String violationReason;
}
