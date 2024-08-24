package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.common.database.entity.AbstractAdmin;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * BoxFlagResponse
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
public class BoxFlagResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = -2546170816293944735L;
    /**
     * 装箱标识卡ID
     */
    private String boxFlagId;
    /**
     * 创建人用户名
     */
    private String creatorFormat;
    /**
     * 人员
     */
    private String person;
    /**
     * 日期
     */
    private String date;
    /**
     * 客户简称
     */
    private String customerShortName;
    private String customerShortNameFormat;
    /**
     * 采购订单编号
     */
    private String purchaseOrderNo;
    /**
     * PO项目
     */
    private String poProject;
    /**
     * 销售订单
     */
    private String saleOrderNo;
    /**
     * 订单项目
     */
    private String orderProject;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 每箱数量
     */
    private Integer eachBoxCount;
    /**
     * 箱号
     */
    private Integer boxNumber;
    private String boxNumberFormat;
    /**
     * 序号列表
     */
    private List<String> serialNoList = new ArrayList<>();
    private String serialNoFormat;
    /**
     * 长
     */
    private BigDecimal length;
    /**
     * 宽
     */
    private BigDecimal width;
    /**
     * 高
     */
    private BigDecimal height;
    /**
     * 体积
     */
    private BigDecimal volume;
    private String volumeFormat;
    /**
     * 单件重量
     */
    private BigDecimal unitWeight;
    /**
     * 每箱重量
     */
    private BigDecimal eachBoxWeight;
    /**
     * 单号
     */
    private String orderNo;
    /**
     * 序号
     */
    private String serialNo;
    /**
     * 备注
     */
    private String remark;
    /**
     * 发送数量
     */
    private BigDecimal sendCount;
    /**
     * 发送日期
     */
    private String sendDate;
    /**
     * 图片数量
     */
    private Integer photoCount;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList = new ArrayList<>();
}
