package com.lead.fund.base.server.mp.request;

import cn.hutool.core.collection.CollUtil;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ParamRequest
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-23 17:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParamRequest implements Serializable {

    private static final long serialVersionUID = -7853387184219163787L;
    /**
     * 分类ID列表
     */
    private List<String> categoryIdList = CollUtil.toList(
            "processProcedure",
            "device",
            "schedule",
            "workMinute",
            "processType",
            "stopWorkingContent1",
            "stopWorkingContent2",
            "stopWorkingContent3",
            "customerShortName",
            "accidentType",
            "process",
            "checkPoint",
            "qualityDealOpinion",
            "skillDealOpinion",
            "defectType",
            "department",
            "profession",
            "optimizeType",
            "chineseVietnamName",
            "calibrationPeriod",
            "storage",
            "deviceCheckLedgerState",
            "userProperty",
            "computerName",
            "companyPosition",
            "computerState"
    );
}
