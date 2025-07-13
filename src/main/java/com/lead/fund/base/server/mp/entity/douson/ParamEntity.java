package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * IndustryParamEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-11 10:58
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MP_INDUSTRY_PARAM")
@ClassFunction("参数表")
public class ParamEntity implements Serializable {

    private static final long serialVersionUID = 1290544322994587440L;
    /**
     * 参数分类id
     */
    @MppMultiId
    @FieldRemark(value = "参数分类id")
    @Size(max = 64, message = "参数分类id长度最大为64")
    private String paramCategoryId;
    /**
     * 参数编码
     */
    @MppMultiId
    @FieldRemark(value = "参数编码")
    @Size(max = 128, message = "参数编码长度最大为128")
    private String paramCode;
    /**
     * 参数名称
     */
    @FieldRemark(value = "参数名称")
    @Size(max = 256, message = "参数名称长度最大为256")
    @NotNull
    private String paramName;
    /**
     * 扩展内容1
     */
    @FieldRemark(value = "扩展内容1")
    @Size(max = 256, message = "扩展内容1长度最大为256")
    @NotNull
    private String expandFirst;
    /**
     * 排序
     */
    @FieldRemark(value = "排序", defaultValue = "0")
    @Size(max = 8, message = "排序长度最大为8")
    private Integer sorter;
}