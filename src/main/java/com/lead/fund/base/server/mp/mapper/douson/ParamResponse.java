package com.lead.fund.base.server.mp.mapper.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class ParamResponse implements Serializable {

    private static final long serialVersionUID = 5605163073029135135L;
    /**
     * 参数分类id
     */
    private String paramCategoryId;
    private String paramCategoryIdFormat;
    /**
     * 参数编码
     */
    private String paramCode;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 排序
     */
    private Integer sorter;
}