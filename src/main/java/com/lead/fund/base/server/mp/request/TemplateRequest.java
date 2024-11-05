package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * TemplateRequest
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
public class TemplateRequest extends AbstractAdministratorModel {

    private static final long serialVersionUID = 5945041243096001051L;
    /**
     * 供应商刀具模板ID
     */
    private String templateId;
    private List<String> templateIdList;
    /**
     * 借用方
     */
    private String borrowTemplatePerson;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startPromiseReturnDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endPromiseReturnDate;
    /**
     * 经办人
     */
    private String operatorPerson;
    /**
     * 借出照片数量
     */
    private Integer borrowPhotoCount;
    /**
     * 借出照片
     */
    private List<PhotoImgModel> borrowPhotoList;
    /**
     * 还回照片数量
     */
    private Integer returnPhotoCount;
    /**
     * 还回照片
     */
    private List<PhotoImgModel> returnPhotoList;
    /**
     * 备注
     */
    private String description;
    /**
     * 已还数量
     */
    private BigDecimal returnCount;
    /**
     * 是否归还
     */
    private Boolean alreadyReturn;
    /**
     * 实际归还日期
     */
    private String actualReturnDate;
    /**
     * 是否符合要求
     */
    private Boolean meetRequirement;
    /**
     * 0:false 1:true -1:--
     */
    private Integer meetRequirementType;
    /**
     * 单号
     */
    private String templateOrderNo;
}
