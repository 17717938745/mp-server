package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * ComputerResponse
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
public class ComputerResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = 5945041243096001178L;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 图片数量
     */
    private Integer photoCount;
    /**
     * 创建人用户名
     */
    private String creatorFormat;
    /**
     * 电脑ID
     */
    private String computerId;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 设备名称
     */
    private String computerName;
    private String computerNameFormat;
    /**
     * 设备型号
     */
    private String model;
    /**
     * 使用人
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户中文名称
     */
    private String userChineseName;
    /**
     * 部门
     */
    private String department;
    private String departmentFormat;
    /**
     * 职位
     */
    private String profession;
    private String professionFormat;
    /**
     * 位置
     */
    private String position;
    private String positionFormat;
    /**
     * 入库日期
     */
    private String storageDate;
    /**
     * 状态
     */
    private String computerState;
    private String computerStateFormat;
    /**
     * 是否有账
     */
    private Boolean detailed;
    private String detailedFormat;
    /**
     * 设备原产地
     */
    private String productPlace;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 备注
     */
    private String remark;
    /**
     * 图片列表
     */
    private List<PhotoImgModel> photoList = new ArrayList<>();
}
