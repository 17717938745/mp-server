package com.lead.fund.base.server.mp.request;

import com.baomidou.mybatisplus.annotation.TableName;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.basic.model.LinkItem;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * PlanEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class PlanRequest implements Serializable {

    private static final long serialVersionUID = 5945042211096726180L;
    /**
     * 精益改善ID
     */
    private String planId;
    /**
     * 开始时间@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startCreateDate;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endCreateDate;
    /**
     * 创建人用户id
     */
    private String creator;
    /**
     * 创建人用户名称
     */
    private String username;
    /**
     * 创建日期
     */
    private String createDate;
    /**
     * 部门
     */
    private String department;
    /**
     * 精益类型
     */
    private String optimizeType;
    /**
     * 存在问题
     */
    private String existsProblem;
    /**
     * 解决方案
     */
    private String solveScheme;
    /**
     * 负责人
     */
    private List<String> responsiblePersonList;
    private String responsiblePerson;
    /**
     * 负责人
     */
    private String planCompleteTime;
    /**
     * 奖励金额
     */
    private BigDecimal awardAmount;
    private String awardAmountFormat;
    /**
     * 结案
     */
    private Boolean valid;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> beforePhotoList;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> afterPhotoList;
    /**
     * 附件列表
     */
    private List<FileModel> attachmentList;
    /**
     * 序号（单号）
     */
    private String serialNo;
}
