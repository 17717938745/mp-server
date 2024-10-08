package com.lead.fund.base.server.mp.mapper.dmmp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.response.MpUserDepartmentSummaryResponse;
import com.lead.fund.base.server.mp.response.MpUserProfessionSummaryResponse;
import java.io.Serializable;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * MpUserMapper
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-05 16:57
 */
@Repository
public interface MpUserMapper extends BaseMapper<MpUserEntity>, Serializable {

    List<MpUserDepartmentSummaryResponse> userDepartmentSummary();

    List<MpUserProfessionSummaryResponse> userProfessionSummary();
}
