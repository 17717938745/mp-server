package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * TemplateResponse
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TemplateResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = 5945041243096001052L;

    /**
     * 索引
     */
    private Integer index;
    /**
     * 供应商刀具模板ID
     */
    private String templateId;
    /**
     * 借用方
     */
    private String borrowTemplatePerson;
    private String borrowTemplatePersonFormat;
    /**
     * 借用日期
     */
    private String borrowTemplateDate;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 物料描述
     */
    private String materialDescription;
    /**
     * 数量
     */
    private BigDecimal templateCount;
    /**
     * 承诺归还日期
     */
    private String promiseReturnDate;
    /**
     * 经办人
     */
    private String operatorPerson;
    private String operatorPersonFormat;
    /**
     * 借出照片数量
     */
    private Integer borrowPhotoCount;
    /**
     * 借出照片列表
     */
    private List<PhotoImgModel> borrowPhotoList = new ArrayList<>();
    /**
     * 还回照片数量
     */
    private Integer returnPhotoCount;
    /**
     * 还回照片列表
     */
    private List<PhotoImgModel> returnPhotoList = new ArrayList<>();
    /**
     * 备注
     */
    private String description;
    /**
     * 已还数量
     */
    private BigDecimal returnCount;
    /**
     * 实际归还日期
     */
    private String actualReturnDate;
    /**
     * 是否符合要求
     */
    private Boolean meetRequirement;
    private String meetRequirementFormat;
    /**
     * 单号
     */
    private String templateOrderNo;
}
