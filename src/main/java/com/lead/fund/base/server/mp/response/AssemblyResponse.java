package com.lead.fund.base.server.mp.response;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.common.basic.model.AbstractAdministratorModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * AssemblyResponse
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
public class AssemblyResponse extends AbstractAdministratorModel {

    private static final long serialVersionUID = -2958798731405959803L;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 整机ID
     */
    private String assemblyId;
    /**
     * 整机序列号
     */
    private String serialNumber;
    /**
     * 最大整机序列号索引，维度：采购订单编号、PO项目、销售订单、订单项目
     */
    private Integer maxSerialOrderIndex;
    /**
     * 整机序列号索引
     */
    private Integer serialIndex;
    /**
     * 最大整机序列号索引，维度：采购订单编号、PO项目
     */
    private Integer maxSerialIndex;
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
     * 物料描述
     */
    private String materialDescription;
    /**
     * 图号
     */
    private String designNumber;
    /**
     * 承诺交期
     */
    private String deliveryDate;
    /**
     * 订单数量
     */
    private Integer orderCount;
    /**
     * 完成数量
     */
    private Integer completedQty;
    /**
     * 备注
     */
    private String description;
    /**
     * 阀座/阀瓣照片
     */
    private String valveSeatPhoto;
    /**
     * 阀体
     */
    private String valveBody;
    private Integer valveBodyPhotoCount;
    private List<PhotoImgModel> valveBodyPhotoList = new ArrayList<>();
    /**
     * 阀盖/尾盖
     */
    private String valveCover;
    private Integer valveCoverPhotoCount;
    private List<PhotoImgModel> valveCoverPhotoList = new ArrayList<>();
    /**
     * 闸板
     */
    private String gate;
    private Integer gatePhotoCount;
    private List<PhotoImgModel> gatePhotoList = new ArrayList<>();
    /**
     * 阀座/阀瓣
     */
    private String valveSeat;
    private Integer valveSeatPhotoCount;
    private List<PhotoImgModel> valveSeatPhotoList = new ArrayList<>();
    /**
     * 阀杆/尾杆
     */
    private String valveStem;
    private Integer valveStemPhotoCount;
    ;
    private List<PhotoImgModel> valveStemPhotoList = new ArrayList<>();
    /**
     * 装配人员
     */
    private String assemblyPerson;
    private String assemblyPersonFormat;
    /**
     * 开始装配日期
     */
    private String assemblyStartDate;
    /**
     * 整机和驱动器试压
     */
    private String pressureTest;
    private Integer pressureTestPhotoCount;
    private List<PhotoImgModel> pressureTestPhotoList = new ArrayList<>();
    /**
     * 闸阀开关扭矩，N.m
     */
    private BigDecimal torqueNm;
    private String torqueNmFormat;
    /**
     * 注油
     */
    private String oilInjection;
    private Integer oilInjectionPhotoCount;
    private List<PhotoImgModel> oilInjectionPhotoList = new ArrayList<>();
    /**
     * 试压人员
     */
    private String tester;
    private String testerFormat;
    /**
     * 装配完成日期
     */
    private String assemblyCompleteDate;
    /**
     * 装配完成数量
     */
    private Integer assemblyCompleteCount;
}
