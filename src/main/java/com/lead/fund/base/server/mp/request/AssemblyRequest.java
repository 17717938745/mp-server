package com.lead.fund.base.server.mp.request;

import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * AssemblyRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AssemblyRequest implements Serializable {

    private static final long serialVersionUID = -2958798731405959802L;
    /**
     * 整机ID
     */
    private String assemblyId;
    /**
     * 索引，维度：采购订单编号、PO项目、销售订单、订单项目
     */
    private Integer assemblyIndex;
    /**
     * 修改时间
     */
    private String modifyTime;
    /**
     * 整机序列号
     */
    private String serialNumber;
    /**
     * 最大整机序列号索引，维度：采购订单编号、PO项目、销售订单、订单项目
     */
    private Integer maxSerialOrderIndex;
    /**
     * 最大整机序列号索引，维度：采购订单编号、PO项目
     */
    private Integer maxSerialIndex;
    /**
     * 整机序列号索引
     */
    private Integer serialIndex;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDeliveryDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDeliveryDate;
    /**
     * 订单数量
     */
    private Integer orderCount;
    /**
     * 生成订单数量
     */
    private Integer createOrderCount;
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
    private List<PhotoImgModel> valveBodyPhotoList = new ArrayList<>();
    /**
     * 阀盖/尾盖
     */
    private String valveCover;
    private List<PhotoImgModel> valveCoverPhotoList = new ArrayList<>();
    /**
     * 闸板
     */
    private String gate;
    private List<PhotoImgModel> gatePhotoList = new ArrayList<>();
    /**
     * 阀座/阀瓣
     */
    private String valveSeat;
    private List<PhotoImgModel> valveSeatPhotoList = new ArrayList<>();
    /**
     * 阀杆/尾杆
     */
    private String valveStem;
    private List<PhotoImgModel> valveStemPhotoList = new ArrayList<>();
    /**
     * 装配人员
     */
    private String assemblyPerson;
    /**
     * 注油完成数量
     */
    private Integer oilInjectionCompleteCount;
    /**
     * 注油完成日期
     */
    private String oilInjectionCompleteDate;
    /**
     * 整机和驱动器试压
     */
    private String pressureTest;
    private List<PhotoImgModel> pressureTestPhotoList = new ArrayList<>();
    /**
     * 闸阀开关扭矩，N.m
     */
    private BigDecimal torqueNm;
    /**
     * 注油
     */
    private String oilInjection;
    private List<PhotoImgModel> oilInjectionPhotoList = new ArrayList<>();
    /**
     * 试压人员
     */
    private String tester;
    /**
     * 装配完成日期
     */
    private String assemblyCompleteDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startAssemblyCompleteDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endAssemblyCompleteDate;
    /**
     * 装配完成数量
     */
    private Integer assemblyCompleteCount;
    /**
     * 装配完成情况，-1-全部，0-yes，1-no
     */
    private Integer assemblyCompleteType;
    /**
     * 装配完成情况，-1-全部，0-yes，1-no
     */
    private Integer oilInjectionCompleteType;
}
