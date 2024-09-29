package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.SERVER_ERROR;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.converter.MpForumConverter.MP_FORUM_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.cons.frame.ExceptionType;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.entity.AbstractPrimaryKey;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.TreeUtil;
import com.lead.fund.base.server.mp.entity.dmmp.MpH5Entity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.douson.ForumCommentaryEntity;
import com.lead.fund.base.server.mp.entity.douson.ForumEntity;
import com.lead.fund.base.server.mp.entity.douson.ForumThumbsUpEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpH5Mapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.douson.ForumCommentaryMapper;
import com.lead.fund.base.server.mp.mapper.douson.ForumMapper;
import com.lead.fund.base.server.mp.mapper.douson.ForumThumbsUpMapper;
import com.lead.fund.base.server.mp.request.ForumCommentaryPageRequest;
import com.lead.fund.base.server.mp.request.ForumCommentaryRequest;
import com.lead.fund.base.server.mp.request.ForumPageRequest;
import com.lead.fund.base.server.mp.request.ForumRequest;
import com.lead.fund.base.server.mp.request.ForumThumbsUpRequest;
import com.lead.fund.base.server.mp.response.ForumCommentaryResponse;
import com.lead.fund.base.server.mp.response.ForumResponse;
import com.lead.fund.base.server.mp.response.ForumThumbsUpResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ForumController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-04-30 16:11
 */
@SuppressWarnings({"SqlResolve", "UnusedReturnValue", "unused"})
@RestController
@RequestMapping("/forum")
@Slf4j
@Validated
public class ForumController {

    @Resource
    private ForumMapper forumMapper;
    @Resource
    private ForumThumbsUpMapper forumThumbsUpMapper;
    @Resource
    private ForumCommentaryMapper forumCommentaryMapper;
    @Resource
    private MpH5Mapper h5Mapper;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private AccountHelper accountHelper;

    /**
     * 合并论坛
     *
     * @param deviceId 设备id
     * @param request  {@link ForumRequest}
     * @return {@link Result}
     */
    @PutMapping("merge")
    public Result forumMerge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ForumRequest request
    ) {
        MpUserResponse u = accountHelper.getUser(deviceId);
        final ForumEntity e = (ForumEntity) MP_FORUM_INSTANCE.entity(request)
                .setModifier(u.getUserId());
        if (isNotBlank(e.getId())) {
            forumMapper.updateById(e.setThumbsUp(null).setCommentary(null));
        } else {
            forumMapper.insert(
                    (ForumEntity) e
                            .setThumbsUp(BigDecimal.ZERO)
                            .setCommentary(BigDecimal.ZERO)
                            .setUserId(u.getUserId())
                            .setCreator(u.getUserId())
            );
        }
        return new DataResult<>(MP_FORUM_INSTANCE.data(e));
    }

    /**
     * 查询论坛分页
     *
     * @param deviceId 设备id
     * @param request  {@link ForumPageRequest}
     * @return {@link PageResult<ForumResponse>}
     */
    @GetMapping("page")
    public PageResult<ForumResponse> forumPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ForumPageRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final String title = request.getData().getTitle();
        final boolean searchTitle = isNotBlank(title);
        if (searchTitle) {
            final List<String> h5IdList = h5Mapper.selectList(new LambdaQueryWrapper<MpH5Entity>().like(MpH5Entity::getTitle, title)
                            .select(MpH5Entity::getId)
                    ).stream().map(AbstractPrimaryKey::getId)
                    .distinct().collect(Collectors.toList());
            if (CollUtil.isEmpty(h5IdList)) {
                return new PageResult<>();
            } else {
                request.getData().setH5IdList(h5IdList);
            }
        }
        final PageResult<ForumEntity> pr = DatabaseUtil.page(request, this::forumList);
        return new PageResult<>(pr.getTotal(), formatForumList(u, pr.getList())
                .stream().peek(t -> {
                    if (searchTitle) {
                        t.setTitle(t.getTitle().replace(title, "<span style=\"color: #EE0000\">" + title + "</span>"));
                    }
                }).collect(Collectors.toList())
        );
    }

    private List<ForumEntity> forumList(ForumRequest request) {
        final LambdaQueryWrapper<ForumEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getForumId())) {
            lambda.eq(ForumEntity::getId, request.getForumId());
        }
        if (CollUtil.isNotEmpty(request.getH5IdList())) {
            DatabaseUtil.or(lambda, request.getH5IdList(), (lam, l) -> lam.in(ForumEntity::getH5Id, l));
        }
        return forumMapper.selectList(lambda.orderByDesc(ForumEntity::getCreatedTime));
    }

    private List<ForumResponse> formatForumList(MpUserResponse u, List<ForumEntity> list) {
        final List<ForumResponse> rl = MP_FORUM_INSTANCE.list(list);
        MultitaskUtil.supplementList(
                rl,
                ForumResponse::getH5Id,
                l -> h5Mapper.selectList(new LambdaQueryWrapper<MpH5Entity>().in(MpH5Entity::getId, l)),
                (t, r) -> t.getH5Id().equals(r.getId()),
                (t, r) -> t
                        .setH5Id(r.getId())
                        .setTitle(r.getTitle())
                        .setContent(r.getContent())
        );
        MultitaskUtil.supplementList(
                rl,
                ForumResponse::getUserId,
                l -> userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().in(MpUserEntity::getId, l)),
                (t, r) -> t.getUserId().equals(r.getId()),
                (t, r) -> t
                        .setUserIdFormat(defaultIfBlank(defaultIfBlank(r.getNickname(), r.getName()), r.getId()))
        );
        MultitaskUtil.supplementList(
                rl,
                ForumResponse::getForumId,
                l -> forumThumbsUpMapper.selectList(new LambdaQueryWrapper<ForumThumbsUpEntity>()
                        .in(ForumThumbsUpEntity::getForumId, l)
                        .eq(ForumThumbsUpEntity::getUserId, u.getUserId())
                ),
                (t, r) -> t.getForumId().equals(r.getForumId()),
                (t, r) -> t
                        .setThumbsUpType(r.getThumbsUp() ? 1 : 2)
        );

        return rl;
    }

    /**
     * 查询论坛
     *
     * @param deviceId 设备id
     * @param request  {@link ForumRequest}
     * @return {@link PageResult<ForumResponse>}
     */
    @GetMapping("")
    public DataResult<ForumResponse> forumGet(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ForumRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        return new DataResult<>(
                CollUtil.getFirst(
                        formatForumList(u, forumList(request))
                )
        );
    }

    /**
     * 论坛点赞
     *
     * @param deviceId 设备id
     * @param request  {@link ForumRequest}
     * @return {@link Result}
     */
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @PutMapping("thumbs-up/merge")
    public Result forumThumbsUpMerge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ForumThumbsUpRequest request
    ) {
        final Boolean thumbsUp = Boolean.TRUE.equals(request.getThumbsUp());
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final ForumThumbsUpResponse r = new ForumThumbsUpResponse();
        final ForumThumbsUpEntity e = new ForumThumbsUpEntity()
                .setForumId(request.getForumId())
                .setUserId(u.getUserId())
                .setThumbsUp(thumbsUp);
        final ForumThumbsUpEntity db = forumThumbsUpMapper.selectOne(new LambdaQueryWrapper<ForumThumbsUpEntity>()
                .eq(ForumThumbsUpEntity::getForumId, request.getForumId())
                .eq(ForumThumbsUpEntity::getUserId, u.getUserId())
        );
        if (null == db) {
            forumThumbsUpMapper.insert(e);
            r.setThumbsUpChange(thumbsUp ? 1 : -1)
                    .setThumbsUpType(thumbsUp ? 1 : 2);
        } else if (e.getThumbsUp().equals(db.getThumbsUp())) {
            forumThumbsUpMapper.deleteByMultiId(e);
            r.setThumbsUpChange(thumbsUp ? -1 : 1)
                    .setThumbsUpType(0);
        } else {
            forumThumbsUpMapper.updateByMultiId(e);
            r.setThumbsUpChange(thumbsUp ? 2 : -2)
                    .setThumbsUpType(thumbsUp ? 1 : 2);
        }
        final LambdaUpdateWrapper<ForumEntity> lambda = new LambdaUpdateWrapper<ForumEntity>()
                .setSql(true, "THUMBS_UP = THUMBS_UP + " + r.getThumbsUpChange())
                .eq(ForumEntity::getId, request.getForumId());
        forumMapper.update(null, lambda);
        return new DataResult<>(r);
    }

    /**
     * 评论
     *
     * @param deviceId 设备id
     * @param request  {@link ForumCommentaryRequest}
     * @return {@link Result}
     */
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @PutMapping("commentary")
    public Result commentaryMerge(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @RequestBody ForumCommentaryRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final ForumCommentaryEntity e = (ForumCommentaryEntity) MP_FORUM_INSTANCE.commentary(request)
                .setModifier(u.getUserId());
        if (isNotBlank(e.getId())) {
            forumCommentaryMapper.updateById(e);
        } else {
            forumCommentaryMapper.insert(
                    (ForumCommentaryEntity) e
                            .setUserId(u.getUserId())
                            .setCreator(u.getUserId())
            );
            forumMapper.update(
                    null,
                    new LambdaUpdateWrapper<ForumEntity>()
                            .eq(ForumEntity::getId, request.getForumId())
                            .setSql(true, "COMMENTARY = COMMENTARY + 1")
            );
        }
        return new DataResult<>(MP_FORUM_INSTANCE.commentary(e));
    }

    /**
     * 删除文章
     *
     * @param deviceId 设备id
     * @param request  {@link ForumCommentaryRequest}
     * @return {@link Result}
     */
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @DeleteMapping("")
    public Result delete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ForumRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        if (isNotBlank(request.getForumId())) {
            final LambdaUpdateWrapper<ForumEntity> lambda = new LambdaUpdateWrapper<ForumEntity>()
                    .eq(ForumEntity::getId, request.getForumId());
            if (!"admin".equals(u.getUsername())) {
                lambda.eq(ForumEntity::getUserId, u.getUserId());
            }
            if (forumMapper.delete(lambda) <= 0) {
                throw new BusinessException(ExceptionType.AUTHORITY_AUTH_FAIL);
            }
            forumCommentaryMapper.delete(new LambdaUpdateWrapper<ForumCommentaryEntity>().eq(ForumCommentaryEntity::getForumId, request.getForumId()));
        }
        return new Result();
    }

    /**
     * 删除评论
     *
     * @param deviceId 设备id
     * @param request  {@link ForumCommentaryRequest}
     * @return {@link Result}
     */
    @Transactional(value = "dousonDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    @DeleteMapping("commentary")
    public Result commentaryDelete(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ForumCommentaryRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final LambdaUpdateWrapper<ForumCommentaryEntity> lambda = new LambdaUpdateWrapper<ForumCommentaryEntity>()
                .eq(ForumCommentaryEntity::getId, request.getCommentaryId());
        if (!"admin".equals(u.getUsername())) {
            lambda.eq(ForumCommentaryEntity::getUserId, u.getUserId());
        }
        if (forumCommentaryMapper.selectCount(new LambdaQueryWrapper<ForumCommentaryEntity>().eq(ForumCommentaryEntity::getParentId, request.getCommentaryId())) > 0) {
            throw new BusinessException(SERVER_ERROR.getCode(), "Some one comment you");
        }
        if (isBlank(request.getForumId()) || forumCommentaryMapper.delete(lambda) <= 0) {
            throw new BusinessException(ExceptionType.AUTHORITY_AUTH_FAIL);
        }
        forumMapper.update(
                null,
                new LambdaUpdateWrapper<ForumEntity>()
                        .eq(ForumEntity::getId, request.getForumId())
                        .setSql(true, "COMMENTARY = COMMENTARY - 1")
        );
        return new Result();
    }

    /**
     * 查询评论分页
     *
     * @param deviceId 设备id
     * @param request  {@link ForumCommentaryPageRequest}
     * @return {@link PageResult<ForumCommentaryResponse>}
     */
    @GetMapping("commentary/tree")
    public PageResult<ForumCommentaryResponse> forumCommentaryTree(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute ForumCommentaryPageRequest request
    ) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final PageResult<ForumCommentaryEntity> pr = DatabaseUtil.page(request, this::forumCommentaryList);
        final List<ForumCommentaryResponse> list = TreeUtil.tree(
                formatForumCommentaryList(u, pr.getList()),
                t -> t
        );
        return new PageResult<>(pr.getTotal(), list);
    }

    private List<ForumCommentaryEntity> forumCommentaryList(ForumCommentaryRequest request) {
        final LambdaQueryWrapper<ForumCommentaryEntity> lambda = new LambdaQueryWrapper<>();
        if (isNotBlank(request.getForumId())) {
            lambda.eq(ForumCommentaryEntity::getForumId, request.getForumId());
        }
        return forumCommentaryMapper.selectList(lambda);
    }

    private List<ForumCommentaryResponse> formatForumCommentaryList(MpUserResponse u, List<ForumCommentaryEntity> list) {
        final List<ForumCommentaryResponse> rl = MP_FORUM_INSTANCE.commentaryList(list);
        boolean admin = "admin".equals(u.getUsername());
        MultitaskUtil.supplementList(
                rl,
                ForumCommentaryResponse::getUserId,
                l -> userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().in(MpUserEntity::getId, l)),
                (t, r) -> t.getUserId().equals(r.getId()),
                (t, r) -> t
                        .setUserIdFormat(!admin ? ("匿名用户" + r.getId().substring(r.getId().length() - 4)) : defaultIfBlank(defaultIfBlank(r.getNickname(), r.getName()), r.getId()))
        );
        return rl;
    }
}
