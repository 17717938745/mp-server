package com.lead.fund.base.server.mp.mapper.douson;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lead.fund.base.server.mp.entity.douson.TaskEntity;
import java.io.Serializable;
import org.springframework.stereotype.Repository;

/**
 * TaskMapper
 *
 * @author panchaohui
 * @version 1.0
 * @date 2021-12-05 16:57
 */
@Repository
public interface TaskMapper extends BaseMapper<TaskEntity>, Serializable {

}