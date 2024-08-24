package com.lead.fund.base.server.mp.request;

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
 * ComputerRequest
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
public class ComputerRequest extends AbstractAdministratorModel {

    private static final long serialVersionUID = 5945041243096001178L;
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
    /**
     * 设备型号
     */
    private String model;
    /**
     * 使用人
     */
    private String userId;
    private String username;
    private List<String> userIdList;
    /**
     * 位置
     */
    private String position;
    /**
     * 入库日期
     */
    private String storageDate;
    /**
     * 状态
     */
    private String computerState;
    /**
     * 是否有账
     */
    private Boolean detailed;
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
