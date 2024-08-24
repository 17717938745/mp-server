package com.lead.fund.base.server.mp.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lead.fund.base.server.mp.entity.douson.ParamEntity;
import com.lead.fund.base.server.mp.response.ParamConfigResponse;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;

/**
 * ParamDao
 *
 * @author panchaohui
 * @version 1.0
 * @date 2023-08-17 12:08
 */
public interface ParamDao extends IService<ParamEntity> {

    List<ParamConfigResponse> listByCategoryId(String paramCategoryId);

    void clear(String paramCategoryId);

    void clearAll();
}
