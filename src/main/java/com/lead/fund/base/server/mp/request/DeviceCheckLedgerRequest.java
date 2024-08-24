package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.util.ArrayList;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * DeviceCheckLedgerRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DeviceCheckLedgerRequest implements Serializable {

    private static final long serialVersionUID = -8191529848485002186L;
    /**
     * 检查设备台账ID
     */
    private String deviceCheckLedgerId;
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
     * 开始有效期(下次校验日期)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDueDate;
    /**
     * 结束有效期(下次校验日期)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDueDate;
    /**
     * 校验周期（天）
     */
    private Integer calibrationPeriod;
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
    /**
     * 是否出库
     */
    private Boolean outOfStock;
    /**
     * 借用人
     */
    private String borrower;
    private List<String> borrowerList = new ArrayList<>();
    /**
     * 借用日期
     */
    private String borrowDate;
    /**
     * 状态
     */
    private String deviceCheckLedgerState;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList;
}
