package com.lead.fund.base.server.mp.converter;

import com.lead.fund.base.server.mp.entity.douson.TaskEntity;
import com.lead.fund.base.server.mp.request.TaskRequest;
import com.lead.fund.base.server.mp.response.TaskResponse;
import java.io.Serializable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * AccountConverter
 *
 * @author panchaohui
 * @version 1.0
 * @date 2022-04-28 17:42
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@Mapper
public interface TaskConverter extends Serializable {

    TaskConverter TASK_INSTANCE = Mappers.getMapper(TaskConverter.class);

    @Mapping(target = "id", source = "taskId")
    @Mapping(target = "orderCount", expression = "java(com.lead.fund.base.common.util.NumberUtil.defaultDecimal(request.getOrderCount()))")
    TaskEntity task(TaskRequest request);

    @Mapping(target = "taskId", source = "id")
    @Mapping(target = "processProcedureList", expression = "java(java.util.Arrays.stream(com.lead.fund.base.common.util.StrUtil.defaultIfBlank(d.getProcessProcedure()).split(\",\", -1)).filter(com.lead.fund.base.common.util.StrUtil::isNotBlank).collect(java.util.stream.Collectors.toList()))")
    TaskResponse task(TaskEntity d);

    List<TaskResponse> taskList(List<TaskEntity> list);

}