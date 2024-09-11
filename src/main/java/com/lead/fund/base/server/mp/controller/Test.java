package com.lead.fund.base.server.mp.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.model.OptionItem;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.PageDataResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.server.mp.dao.TroubleAttachmentDao;
import com.lead.fund.base.server.mp.dao.TroubleAttachmentDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.*;
import com.lead.fund.base.server.mp.mapper.douson.TroubleAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.TroubleMapper;
import com.lead.fund.base.server.mp.mapper.douson.TroubleAttachmentMapper;
import com.lead.fund.base.server.mp.mapper.douson.TroubleMapper;
import com.lead.fund.base.server.mp.model.FileModel;
import com.lead.fund.base.server.mp.model.PhotoImgModel;
import com.lead.fund.base.server.mp.request.*;
import com.lead.fund.base.server.mp.response.VocationResponse;
import com.lead.fund.base.server.mp.response.VocationResponse;
import com.lead.fund.base.server.mp.response.VocationSummaryResponse;
import com.lead.fund.base.server.mp.response.TroubleResponse;
import com.lead.fund.base.server.mp.response.TroubleResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.ParamConfigResponse;
import com.lead.fund.base.server.mp.response.TroubleResponse;
import com.lead.fund.base.server.mp.response.TroubleResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import org.mapstruct.Mapping;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_FAIL;
import static com.lead.fund.base.common.util.BeanUtil.defaultIfNull;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.IndustryConverter.INDUSTRY_INSTANCE;

public class Test {


}
