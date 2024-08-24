package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DeviceCheckLedgerResponse
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
public class DeviceCheckLedgerResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = -2546170816293001735L;
    /**
     * 检查设备台账ID
     */
    private String deviceCheckLedgerId;
    /**
     * 创建人用户名
     */
    private String creatorFormat;
    /**
     * 序号
     */
    private Integer index;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 设备编号
     */
    private String deviceNumber;
    /**
     * 中越文名称
     */
    private String chineseVietnamName;
    private String chineseVietnamNameFormat;
    /**
     * 英文名称
     */
    private String englishName;
    /**
     * 规格
     */
    private String specification;
    /**
     * 校准单位
     */
    private String calibrationUnit;
    /**
     * 校准日期
     */
    private String calibrationDate;
    /**
     * 有效期(下次校验日期)
     */
    private String dueDate;
    /**
     * 校验周期（天）
     */
    private Integer calibrationPeriod;
    private String calibrationPeriodFormat;
    /**
     * 有效期快国旗
     */
    private Boolean dueDateWarning;
    /**
     * 厂家
     */
    private String manufacturers;
    /**
     * 验收标准
     */
    private String acceptanceStandard;
    /**
     * 库位
     */
    private String storage;
    private String storageFormat;
    /**
     * 是否出库
     */
    private Boolean outOfStock;
    private String outOfStockFormat;
    /**
     * 借用人
     */
    private String borrower;
    private List<String> borrowerList = new ArrayList<>();
    private String borrowerFormat;
    /**
     * 借用日期
     */
    private String borrowDate;
    /**
     * 状态
     */
    private String deviceCheckLedgerState;
    private String deviceCheckLedgerStateFormat;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList = new ArrayList<>();
}
