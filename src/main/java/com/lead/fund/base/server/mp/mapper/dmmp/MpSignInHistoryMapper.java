package com.lead.fund.base.server.mp.mapper.dmmp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lead.fund.base.server.mp.entity.dmmp.MpSignInHistoryEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import java.io.Serializable;
import org.springframework.stereotype.Repository;

/**
 * MpSignInHistoryMapper
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-05 16:57
 */
@Repository
public interface MpSignInHistoryMapper extends BaseMapper<MpSignInHistoryEntity>, Serializable {

}
