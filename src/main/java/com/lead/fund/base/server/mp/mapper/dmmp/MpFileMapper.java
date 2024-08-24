package com.lead.fund.base.server.mp.mapper.dmmp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lead.fund.base.server.mp.entity.dmmp.MpFileEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpImgEntity;
import java.io.Serializable;
import org.springframework.stereotype.Repository;

/**
 * MpFileMapper
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-05 16:57
 */
@Repository
public interface MpFileMapper extends BaseMapper<MpFileEntity>, Serializable {

}
