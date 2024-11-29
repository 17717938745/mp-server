package com.lead.fund.base.server.mp.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lead.fund.base.server.mp.entity.douson.TaskEntity;

/**
 * TaskDao
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
public interface TaskDao extends IService<TaskEntity> {

    TaskEntity merge(TaskEntity e);

    TaskEntity merge(String deviceId, TaskEntity e);

    void deleteById(String taskId);
}
