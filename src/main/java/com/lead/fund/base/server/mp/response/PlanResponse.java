package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.common.basic.model.LinkItem;
import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class PlanResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = 5945042211096726180L;
    /**
     * 精益改善ID
     */
    private String planId;
    /**
     * 序号
     */
    private Integer index;
    /**
     * 创建人用户id
     */
    private String creator;
    /**
     * 创建人用户名
     */
    private String creatorFormat;
    /**
     * 创建日期
     */
    private String createDate;
    /**
     * 部门
     */
    private String department;
    private String departmentFormat;
    /**
     * 精益类型
     */
    private String optimizeType;
    private String optimizeTypeFormat;
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
    private String responsiblePerson;
    private String responsiblePersonFormat;
    /**
     * 完成时间
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
    private List<PhotoImgModel> beforePhotoList = new ArrayList<>();
    /**
     * 图片列表
     */
    private List<PhotoImgModel> afterPhotoList = new ArrayList<>();
    /**
     * 附件列表
     */
    private List<FileModel> attachmentList = new ArrayList<>();
}
