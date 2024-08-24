package com.lead.fund.base.server.mp.entity.douson;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.lead.fund.base.common.basic.cons.frame.ClassFunction;
import com.lead.fund.base.common.basic.cons.frame.FieldRemark;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * ReportUserEntity
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-14 10:53
 */
@ToString
@Data
@TableName("MP_INDUSTRY_REPORT_USER")
@NoArgsConstructor
@ClassFunction("日报")
@Accessors(chain = true)
public class ReportUserEntity implements Serializable {

    private static final long serialVersionUID = 6871891074351523210L;
    /**
     * 活动时间
     */
    @MppMultiId
    @FieldRemark(value = "日期")
    private String reportDate;
    /**
     * 操作人
     */
    @MppMultiId
    @FieldRemark(value = "操作人")
    @Size(max = 64, message = "操作人")
    private String userId;
    /**
     * 工作时间
     */
    private Integer workMinute;
}
