package com.lead.fund.base.server.mp.controller;

import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.BasicConst.REQUEST_METHOD_KEY_USER_AGENT;
import static com.lead.fund.base.common.basic.cons.BasicConst.STRING_ADMIN_DEVICE_ID;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_AUTH_ERROR;
import static com.lead.fund.base.common.basic.cons.frame.ExceptionType.AUTHORITY_SIGN_LOCKED;
import static com.lead.fund.base.common.util.StrUtil.defaultIfBlank;
import static com.lead.fund.base.common.util.StrUtil.isBlank;
import static com.lead.fund.base.common.util.StrUtil.isNotBlank;
import static com.lead.fund.base.server.mp.cons.MpExceptionType.MP_PASSWORD_ERROR;
import static com.lead.fund.base.server.mp.converter.IndustryConverter.INDUSTRY_INSTANCE;
import static com.lead.fund.base.server.mp.converter.MpSystemConverter.MP_SYSTEM_INSTANCE;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lead.fund.base.common.basic.cons.frame.AdminState;
import com.lead.fund.base.common.basic.cons.frame.ExceptionType;
import com.lead.fund.base.common.basic.exec.BusinessException;
import com.lead.fund.base.common.basic.response.DataResult;
import com.lead.fund.base.common.basic.response.ListResult;
import com.lead.fund.base.common.basic.response.PageResult;
import com.lead.fund.base.common.basic.response.Result;
import com.lead.fund.base.common.database.util.DatabaseUtil;
import com.lead.fund.base.common.util.DateUtil;
import com.lead.fund.base.common.util.MultitaskUtil;
import com.lead.fund.base.common.util.SecurityUtil;
import com.lead.fund.base.common.util.StrUtil;
import com.lead.fund.base.common.web.util.HttpUtil;
import com.lead.fund.base.server.mp.dao.MpDeviceDao;
import com.lead.fund.base.server.mp.dao.MpUserRoleDao;
import com.lead.fund.base.server.mp.dao.ParamDao;
import com.lead.fund.base.server.mp.dao.UserPhotoDao;
import com.lead.fund.base.server.mp.entity.dmmp.MpDeviceEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpRoleEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpSignInHistoryEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserPhotoEntity;
import com.lead.fund.base.server.mp.entity.dmmp.MpUserRoleEntity;
import com.lead.fund.base.server.mp.helper.AccountHelper;
import com.lead.fund.base.server.mp.helper.UrlHelper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpRoleMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpSignInHistoryMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserMapper;
import com.lead.fund.base.server.mp.mapper.dmmp.MpUserRoleMapper;
import com.lead.fund.base.server.mp.request.MpUserMergeRequest;
import com.lead.fund.base.server.mp.request.MpUserPasswordRequest;
import com.lead.fund.base.server.mp.request.MpUserRequest;
import com.lead.fund.base.server.mp.request.SignInHistoryPageRequest;
import com.lead.fund.base.server.mp.request.SignInRequest;
import com.lead.fund.base.server.mp.response.MpRoleGroupResponse;
import com.lead.fund.base.server.mp.response.MpRoleResponse;
import com.lead.fund.base.server.mp.response.MpSignInHistoryResponse;
import com.lead.fund.base.server.mp.response.MpUserConfigResponse;
import com.lead.fund.base.server.mp.response.MpUserDepartmentSummaryResponse;
import com.lead.fund.base.server.mp.response.MpUserProfessionSummaryResponse;
import com.lead.fund.base.server.mp.response.MpUserResponse;
import com.lead.fund.base.server.mp.response.ParamConfigResponse;
import com.lead.fund.base.server.mp.response.SignInResponse;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemController
 *
 * @author panchaohui
 * @version 1.0
 * @date 2024-05-14 10:52
 */
@SuppressWarnings("SqlResolve")
@RestController
@RequestMapping("/system")
@Slf4j
@Validated
public class SystemController {

    @Resource
    private MpDeviceDao deviceDao;
    @Resource
    private MpUserMapper userMapper;
    @Resource
    private MpUserRoleMapper userRoleMapper;
    @Resource
    private MpUserRoleDao userRoleDao;
    @Resource
    private MpRoleMapper roleMapper;
    @Resource
    private AccountHelper accountHelper;
    @Resource
    private ParamDao paramDao;
    @Resource
    private UserPhotoDao userPhotoDao;
    @Resource
    private UrlHelper urlHelper;
    @Resource
    private MpSignInHistoryMapper signInHistoryMapper;
    @Resource
    private MpUserMapper mpUserMapper;

    /**
     * 注册设备
     *
     * @param req {@link HttpServletRequest}
     * @return {@link DataResult<MpDeviceEntity>}
     */
    @GetMapping("device")
    public DataResult<MpDeviceEntity> device(HttpServletRequest req) {
        return new DataResult<>(deviceDao.defaultDevice(null, null, HttpUtil.ip(req)));
    }

    /**
     * 用户配置列表
     *
     * @return {@link ListResult<MpUserConfigResponse>}
     */
    @GetMapping("user/config/list")
    public ListResult<MpUserConfigResponse> userConfigList(@ModelAttribute MpUserRequest request) {
        return new ListResult<>(userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().select(MpUserEntity::getId, MpUserEntity::getUsername, MpUserEntity::getName))
                .stream()
                .map(t -> new MpUserConfigResponse().setUserId(t.getId()).setUsername(t.getUsername()).setName(t.getName()))
                .collect(Collectors.toList()));
    }

    /**
     * 用户部门汇总
     *
     * @return {@link ListResult<MpUserDepartmentSummaryResponse>}
     */
    @GetMapping("user/department-summary")
    public ListResult<MpUserDepartmentSummaryResponse> departmentSummary() {
        List<MpUserDepartmentSummaryResponse> l = mpUserMapper.userDepartmentSummary();
        final List<ParamConfigResponse> pl = paramDao.listByCategoryId("department");
        final Map<Object, String> m = pl
                .stream().collect(Collectors.toMap(
                        ParamConfigResponse::getValue, ParamConfigResponse::getLabel, (t, t1) -> t1
                ));
        final AtomicInteger ps = new AtomicInteger(0);
        final Map<Object, Integer> m1 = pl
                .stream().collect(Collectors.toMap(
                        ParamConfigResponse::getValue, t -> ps.addAndGet(1), (t, t1) -> t1
                ));
        final AtomicInteger index = new AtomicInteger(0);
        final BigDecimal[] bl = new BigDecimal[]{
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        };
        l = l.stream().peek(t -> {
                    t.setIndex(index.addAndGet(1))
                            .setDepartmentFormat(m.getOrDefault(t.getDepartment(), t.getDepartment()))
                    ;
                    bl[0] = bl[0].add(t.getWorkShop());
                    bl[1] = bl[1].add(t.getOffice());
                    bl[2] = bl[2].add(t.getTotal());
                    bl[3] = bl[3].add(t.getScheduleNull());
                    bl[4] = bl[4].add(t.getScheduleDayTime());
                    bl[5] = bl[5].add(t.getScheduleDayTime12());
                    bl[6] = bl[6].add(t.getScheduleEvening());
                    bl[7] = bl[7].add(t.getScheduleEvening12());
                    bl[8] = bl[8].add(t.getScheduleMiddle());
                }).sorted(Comparator.comparingInt(t -> m1.getOrDefault(t.getDepartment(), 0)))
                .collect(Collectors.toList());
        l.add(
                new MpUserDepartmentSummaryResponse()
                        .setIndex(index.addAndGet(1))
                        .setDepartment("-1")
                        .setDepartmentFormat("总计")
                        .setWorkShop(bl[0])
                        .setOffice(bl[1])
                        .setTotal(bl[2])
                        .setScheduleNull(bl[3])
                        .setScheduleDayTime(bl[4])
                        .setScheduleDayTime12(bl[5])
                        .setScheduleEvening(bl[6])
                        .setScheduleEvening12(bl[7])
                        .setScheduleMiddle(bl[8])
        );
        return new ListResult<>(l);
    }

    /**
     * 用户职业汇总
     *
     * @return {@link ListResult<MpUserDepartmentSummaryResponse>}
     */
    @GetMapping("user/profession-summary")
    public ListResult<MpUserProfessionSummaryResponse> professionSummary() {
        List<MpUserProfessionSummaryResponse> l = mpUserMapper.userProfessionSummary();
        final List<ParamConfigResponse> pl = paramDao.listByCategoryId("department");
        final Map<Object, String> dm = pl
                .stream().collect(Collectors.toMap(
                        ParamConfigResponse::getValue, ParamConfigResponse::getLabel, (t, t1) -> t1
                ));
        final AtomicInteger dps = new AtomicInteger(0);
        final Map<Object, Integer> dm1 = pl
                .stream().collect(Collectors.toMap(
                        ParamConfigResponse::getValue, t -> dps.addAndGet(1), (t, t1) -> t1
                ));
        final List<ParamConfigResponse> ppl = paramDao.listByCategoryId("profession");
        final Map<Object, String> pm = ppl
                .stream().collect(Collectors.toMap(
                        ParamConfigResponse::getValue, ParamConfigResponse::getLabel, (t, t1) -> t1
                ));
        final AtomicInteger pps = new AtomicInteger(0);
        final Map<Object, Integer> pm1 = ppl
                .stream().collect(Collectors.toMap(
                        ParamConfigResponse::getValue, t -> pps.addAndGet(1), (t, t1) -> t1
                ));
        final AtomicInteger index = new AtomicInteger(0);
        final BigDecimal[] bl = new BigDecimal[]{
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        };
        l = l.stream().peek(t -> {
                    t.setIndex(index.addAndGet(1))
                            .setDepartmentFormat(dm.getOrDefault(t.getDepartment(), t.getDepartment()))
                            .setProfessionFormat(pm.getOrDefault(t.getProfession(), t.getProfession()))
                    ;
                    bl[0] = bl[0].add(t.getWorkShop());
                    bl[1] = bl[1].add(t.getOffice());
                    bl[2] = bl[2].add(t.getTotal());
                }).sorted((t, t1) -> {
                    int i = dm1.getOrDefault(t.getDepartment(), 0) - dm1.getOrDefault(t1.getDepartment(), 0);
                    int i1 = pm1.getOrDefault(t.getProfession(), 0) - pm1.getOrDefault(t1.getProfession(), 0);
                    if (i1 == 0) {
                        return i;
                    } else {
                        return i1;
                    }
                })
                .collect(Collectors.toList());
        l.add(
                new MpUserProfessionSummaryResponse()
                        .setIndex(index.addAndGet(1))
                        .setDepartment("-1")
                        .setDepartmentFormat("总计")
                        .setWorkShop(bl[0])
                        .setOffice(bl[1])
                        .setTotal(bl[2])
        );
        return new ListResult<>(l);
    }

    /**
     * 用户列表
     *
     * @return {@link ListResult<MpUserResponse>}
     */
    @GetMapping("user/list")
    public ListResult<MpUserResponse> userList(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId, @ModelAttribute MpUserRequest request) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        final LambdaQueryWrapper<MpUserEntity> lambda = new LambdaQueryWrapper<>();
        final boolean notAdmin = u.getRoleCodeList().stream().noneMatch("admin"::equals);
        if (notAdmin) {
            lambda.eq(MpUserEntity::getState, AdminState.NORMAL.getCode());
        }
        if (null != request) {
            if (null != request.getState()) {
                lambda.eq(MpUserEntity::getState, request.getState());
            }
            if (isNotBlank(request.getRoleId())) {
                List<String> userIdList = userRoleMapper.selectList(new LambdaQueryWrapper<MpUserRoleEntity>().eq(MpUserRoleEntity::getRoleId, request.getRoleId()))
                        .stream().map(MpUserRoleEntity::getUserId).collect(Collectors.toList());
                if (CollUtil.isEmpty(userIdList)) {
                    return new ListResult<>();
                } else {
                    request.setUserIdList(userIdList);
                }
            }
            if (isNotBlank(request.getUsername())) {
                lambda.like(MpUserEntity::getUsername, request.getUsername());
            }
            if (CollUtil.isNotEmpty(request.getUserIdList())) {
                lambda.in(MpUserEntity::getId, request.getUserIdList());
            }
            if (isNotBlank(request.getUsername())) {
                lambda.like(MpUserEntity::getUsername, request.getUsername());
            }
            if (isNotBlank(request.getDepartment())) {
                lambda.eq(MpUserEntity::getDepartment, request.getDepartment());
            }
            if (isNotBlank(request.getProfession())) {
                lambda.eq(MpUserEntity::getProfession, request.getProfession());
            }
            if (isNotBlank(request.getName())) {
                lambda.like(MpUserEntity::getName, request.getName());
            }
            if (isNotBlank(request.getUserProperty())) {
                lambda.eq(MpUserEntity::getUserProperty, request.getUserProperty());
            }
            if (isNotBlank(request.getSchedule())) {
                lambda.eq(MpUserEntity::getSchedule, request.getSchedule());
            }
            if (isNotBlank(request.getState())) {
                lambda.eq(MpUserEntity::getState, request.getState());
            }
        }
        final List<MpUserResponse> list = userMapper.selectList(lambda
                        .orderByAsc(MpUserEntity::getState)
                        .orderByDesc(MpUserEntity::getCreateTime))
                .stream().map(MP_SYSTEM_INSTANCE::data).toList();
        final List<String> userIdList = list.stream().map(MpUserResponse::getUserId).distinct().collect(Collectors.toList());
        final List<MpUserRoleEntity> userRoleList = CollUtil.isEmpty(userIdList) ? new ArrayList<>() : userRoleMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<>(), userIdList, (t, l) -> t.in(MpUserRoleEntity::getUserId, l))
        );
        final List<String> roleIdList = userRoleList.stream().map(MpUserRoleEntity::getRoleId).distinct().collect(Collectors.toList());
        final List<MpRoleEntity> roleList = CollUtil.isEmpty(roleIdList) ? new ArrayList<>() : roleMapper.selectList(
                DatabaseUtil.or(new LambdaQueryWrapper<MpRoleEntity>().eq(MpRoleEntity::getState, AdminState.NORMAL.getCode()),
                        roleIdList,
                        (t, l) -> t.in(MpRoleEntity::getId, l))
        );
        final Map<String, MpRoleEntity> roleMap = roleList.stream().collect(Collectors.toMap(MpRoleEntity::getId, t -> t, (t, t1) -> t1, HashMap::new));
        final Map<String, List<MpRoleResponse>> userRoleListMap = userRoleList.stream().collect(Collectors.groupingBy(
                MpUserRoleEntity::getUserId,
                Collectors.mapping(
                        t -> MP_SYSTEM_INSTANCE.data(roleMap.get(t.getRoleId())),
                        Collectors.filtering(Objects::nonNull, Collectors.toList())
                )
        ));
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getDepartment())).collect(Collectors.toList()),
                MpUserResponse::getDepartment,
                l -> paramDao.listByCategoryId("department"),
                (t, r) -> t.getDepartment().equals(r.getValue()),
                (t, r) -> t.setDepartmentFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getUserProperty())).collect(Collectors.toList()),
                MpUserResponse::getUserProperty,
                l -> paramDao.listByCategoryId("userProperty"),
                (t, r) -> t.getUserProperty().equals(r.getValue()),
                (t, r) -> t.setUserPropertyFormat(r.getLabel())
        );
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getSchedule())).collect(Collectors.toList()),
                MpUserResponse::getSchedule,
                l -> paramDao.listByCategoryId("schedule"),
                (t, r) -> t.getSchedule().equals(r.getValue()),
                (t, r) -> t.setScheduleFormat(r.getLabel())
        );
        final List<ParamConfigResponse> professionList = paramDao.listByCategoryId("profession");
        MultitaskUtil.supplementList(
                list.stream().filter(t -> isNotBlank(t.getUserId())).collect(Collectors.toList()),
                MpUserResponse::getUserId,
                l -> userPhotoDao.list(new LambdaQueryWrapper<MpUserPhotoEntity>().in(MpUserPhotoEntity::getUserId, l)),
                (t, r) -> t.getUserId().equals(r.getUserId()),
                (t, r) -> t.getPhotoList().add(MP_SYSTEM_INSTANCE.photo(r, urlHelper.getUrlPrefix()))
        );
        AtomicInteger index = new AtomicInteger(0);
        boolean notUserManager = u.getRoleCodeList().stream().noneMatch("userManager"::equals);
        return new ListResult<>(
                list.stream()
                        .peek(t -> {
                            t.setProfessionIndex(0);
                            if (isNotBlank(t.getProfession())) {
                                for (int i = 0; i < professionList.size(); i++) {
                                    ParamConfigResponse p = professionList.get(i);
                                    if (t.getProfession().equals(p.getValue())) {
                                        t.setProfessionFormat(p.getLabel());
                                        t.setProfessionIndex(i);
                                        break;
                                    }
                                }
                            }
                        })
                        .sorted(Comparator.comparing(MpUserResponse::getProfessionIndex))
                        .peek(t -> {
                            if (notUserManager) {
                                t.setInterviewResume("");
                            }
                            if (notAdmin) {
                                t.setMobile("");
                            }
                            t.setIndex(index.addAndGet(1));
                            t.setRoleList(userRoleListMap.getOrDefault(t.getUserId(), new ArrayList<>()));
                            t.setRoleIdList(t.getRoleList().stream().map(MpRoleResponse::getRoleId).collect(Collectors.toList()));
                        }).collect(Collectors.toList())
        );
    }

    /**
     * 角色列表
     *
     * @return {@link ListResult<MpRoleResponse>}
     */
    @GetMapping("role/list")
    public ListResult<MpRoleResponse> roleList() {
        final List<MpRoleResponse> list = roleMapper.selectList(
                        new LambdaQueryWrapper<>()
                )
                .stream().map(MP_SYSTEM_INSTANCE::data)
                .collect(Collectors.toList());
        return new ListResult<>(
                list
        );
    }

    /**
     * 角色列表（分组）
     *
     * @return {@link ListResult<MpRoleResponse>}
     */
    @GetMapping("role/group/list")
    public ListResult<MpRoleGroupResponse> roleGroupList() {
        return new ListResult<>(
                roleMapper.selectList(
                                new LambdaQueryWrapper<>()
                        )
                        .stream().map(MP_SYSTEM_INSTANCE::data)
                        .collect(Collectors.groupingBy(
                                MpRoleResponse::getTag
                        )).entrySet().stream().map(e -> new MpRoleGroupResponse().setTag(e.getKey())
                                .setLabel(e.getKey())
                                .setList(e.getValue()))
                        .collect(Collectors.toList())
        );
    }

    /**
     * 角色保存
     *
     * @param request {@link MpUserMergeRequest}
     * @return {@link Result}
     */
    @PostMapping("user")
    @Transactional(value = "dmmpDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result userSave(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId, @RequestBody MpUserMergeRequest request) {
        final MpUserResponse u = accountHelper.checkUserAdmin(deviceId);
        final MpUserEntity user = MP_SYSTEM_INSTANCE.entity(request);
        if (u.getRoleCodeList().stream().noneMatch("userManager"::equals)) {
            user.setInterviewResume(null);
        }
        user.setSalt(SecurityUtil.randomSalt())
                .setPasswordEncrypt(SecurityUtil.sign(request.getPassword(), user.getSalt()))
                .setCreator(u.getUserId())
                .setModifier(u.getUserId())
        ;
        userMapper.insert(user);
        mergeRelevance(request, user);
        return new Result();
    }

    /**
     * 用户修改
     *
     * @param request {@link MpUserMergeRequest}
     * @return {@link Result}
     */
    @PutMapping("user")
    @Transactional(value = "dmmpDataSourceTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Result userUpdate(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId, @RequestBody MpUserMergeRequest request) {
        final MpUserResponse u = accountHelper.checkUserAdmin(deviceId);
        final MpUserEntity user = MP_SYSTEM_INSTANCE.entity(request);
        if (u.getRoleCodeList().stream().noneMatch("userManager"::equals)) {
            user.setInterviewResume(null);
        }
        user.setSalt(isBlank(request.getPassword()) ? null : defaultIfBlank(user.getSalt(), SecurityUtil.randomSalt()))
                .setPasswordEncrypt(isBlank(request.getPassword()) ? null : SecurityUtil.sign(request.getPassword(), user.getSalt()))
                .setModifier(u.getUserId())
        ;
        if (userMapper.update(
                user,
                new LambdaUpdateWrapper<MpUserEntity>()
                        .eq(MpUserEntity::getId, u.getRoleCodeList().stream().anyMatch("admin"::equals) ? user.getId() : u.getUserId())
        ) <= 0) {
            throw new BusinessException(ExceptionType.AUTHORITY_AUTH_FAIL);
        }
        mergeRelevance(request, user);
        return new Result();
    }

    private void mergeRelevance(MpUserMergeRequest request, MpUserEntity user) {
        userRoleMapper.delete(new LambdaUpdateWrapper<MpUserRoleEntity>().eq(MpUserRoleEntity::getUserId, user.getId()));
        userRoleDao.saveBatch(request.getRoleIdList().stream().map(t -> new MpUserRoleEntity().setUserId(user.getId()).setRoleId(t)).collect(Collectors.toList()));
        userPhotoDao.remove(new LambdaUpdateWrapper<MpUserPhotoEntity>().eq(MpUserPhotoEntity::getUserId, request.getUserId()));
        userPhotoDao.saveBatch(request.getPhotoList().stream().map(t -> new MpUserPhotoEntity().setUserId(request.getUserId()).setPhotoUrl(t.getPhotoUrl()).setPhotoCompressUrl(t.getPhotoCompressUrl())).collect(Collectors.toList()));
    }

    /**
     * 修改密码
     *
     * @param request {@link MpUserPasswordRequest}
     * @return {@link Result}
     */
    @PutMapping("user/password")
    public Result userUpdatePassword(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId, @RequestBody MpUserPasswordRequest request) {
        final MpUserResponse u = accountHelper.getUser(deviceId);
        MpUserEntity db = userMapper.selectById(u.getUserId());
        final String salt = SecurityUtil.randomSalt();
        if (userMapper.update(
                null,
                new LambdaUpdateWrapper<MpUserEntity>()
                        .set(MpUserEntity::getPasswordEncrypt, SecurityUtil.sign(SecurityUtil.decrypt(request.getPassword()), salt))
                        .set(MpUserEntity::getSalt, salt)
                        .eq(MpUserEntity::getPasswordEncrypt, SecurityUtil.sign(SecurityUtil.decrypt(request.getOldPassword()), db.getSalt()))
                        .eq(MpUserEntity::getId, u.getUserId())
        ) <= 0) {
            throw new BusinessException(MP_PASSWORD_ERROR);
        }
        return new Result();
    }

    /**
     * 登录历史
     *
     * @param deviceId 登录历史
     * @param request  {@link SignInHistoryPageRequest}
     * @return {@link PageResult<MpSignInHistoryResponse>}
     */
    @GetMapping("sign-in-history/page")
    public PageResult<MpSignInHistoryResponse> signInHistoryPage(
            @RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId,
            @ModelAttribute SignInHistoryPageRequest request
    ) {
        accountHelper.getUser(deviceId);
        LambdaQueryWrapper<MpSignInHistoryEntity> lambda = new LambdaQueryWrapper<MpSignInHistoryEntity>().orderByDesc(MpSignInHistoryEntity::getSignInTime);
        PageResult<MpSignInHistoryEntity> pr = DatabaseUtil.page(request, d -> {
            if (isNotBlank(d.getUsername())) {
                lambda.like(MpSignInHistoryEntity::getUsername, d.getUsername());
            }
            if (null != d.getPublicIp()) {
                if (d.getPublicIp()) {
                    lambda.notLike(MpSignInHistoryEntity::getClientIp, "192.168.");
                } else {
                    lambda.like(MpSignInHistoryEntity::getClientIp, "192.168.");
                }
            }
            return signInHistoryMapper.selectList(lambda);
        });
        return new PageResult<>(pr.getTotal(), INDUSTRY_INSTANCE.signInHistoryList(pr.getList()));
    }

    /**
     * 登录
     *
     * @param deviceId 设备id
     * @param request  {@link SignInRequest}
     * @return {@link DataResult<SignInResponse>}
     */
    @PutMapping("sign-in")
    public DataResult<SignInResponse> signIn(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId, HttpServletRequest req, @RequestBody SignInRequest request) {
        MpDeviceEntity device = deviceDao.defaultDevice(deviceId, null, HttpUtil.ip(req));
        MpUserEntity e = CollUtil.getFirst(userMapper.selectList(new LambdaQueryWrapper<MpUserEntity>().eq(MpUserEntity::getUsername, request.getUsername()).or(true, l -> l.eq(MpUserEntity::getMobile, request.getUsername()))));
        DateTime now = DateTime.now();
        String inputPasswordEncrypt;
        String inputPassword;
        final MpSignInHistoryEntity history = new MpSignInHistoryEntity()
                .setDeviceId(deviceId)
                .setUsername(request.getUsername())
                .setUserId(null != e ? e.getId() : null)
                .setSignInTime(DateUtil.dateTime(now))
                .setClientIp(HttpUtil.ip(req))
                .setUserAgent(StrUtil.sub(req.getHeader(REQUEST_METHOD_KEY_USER_AGENT), 0, 1024 / 2));
        try {
            if (null == e) {
                // 可能是伪造的数据
                throw new BusinessException(AUTHORITY_AUTH_ERROR);
            } else if (e.getSignInLock()) {
                // 账号被管理员锁定
                throw new BusinessException(AUTHORITY_SIGN_LOCKED);
            } else if (null != e.getSignInLockTime() && now.compareTo(e.getSignInLockTime()) <= 0) {
                // 锁定中
                throw new BusinessException(AUTHORITY_SIGN_LOCKED);
            } else if (
                    !STRING_ADMIN_DEVICE_ID.equals(deviceId) &&
                            !(inputPasswordEncrypt = SecurityUtil.sign(inputPassword = SecurityUtil.decrypt(request.getPasswordEncrypt()), e.getSalt())).equals(e.getPasswordEncrypt())) {
                log.info("password error, input encrypt: {}, input: {}, db: {}", inputPasswordEncrypt, inputPassword, e.getPasswordEncrypt());
                // 密码错误
                userMapper.update(
                        null,
                        new LambdaUpdateWrapper<MpUserEntity>()
                                .set(MpUserEntity::getSignInFailCount, e.getSignInFailCount() + 1)
                                .eq(MpUserEntity::getId, e.getId())
                );
                throw new BusinessException(AUTHORITY_AUTH_ERROR);
            } else {
                // 密码正确，重置锁定状态
                userMapper.update(
                        null,
                        new LambdaUpdateWrapper<MpUserEntity>()
                                .set(MpUserEntity::getSignInFailCount, 0)
                                .set(MpUserEntity::getSignInLockTime, DateTime.of(0L))
                                .eq(MpUserEntity::getId, e.getId())
                );
                device.setUserId(e.getId());
                deviceDao.update(new LambdaUpdateWrapper<MpDeviceEntity>()
                        .set(MpDeviceEntity::getUserId, e.getId())
                        .eq(MpDeviceEntity::getId, device.getId())
                );
            }
            accountHelper.clear(deviceId);
            history.setSuccess(true);
        } catch (BusinessException e1) {
            history.setSuccess(false)
                    .setErrorMessage(e1.getMessage());
            throw e1;
        }
        if (!STRING_ADMIN_DEVICE_ID.equals(deviceId)) {
            signInHistoryMapper.insert(history);
        }
        return new DataResult<>(SignInResponse.builder()
                .user(accountHelper.getUser(deviceId, true))
                .build()
        );
    }

    /**
     * 登出
     *
     * @param deviceId 设备id
     * @return {@link Result}
     */
    @DeleteMapping("sign-out")
    public Result signOut(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId) {
        accountHelper.clear(deviceId);
        return new Result();
    }

    /**
     * 清除缓存
     *
     * @param deviceId 设备id
     * @return {@link Result}
     */
    @DeleteMapping("/cache")
    public Result clearAll(@RequestHeader(value = REQUEST_METHOD_KEY_DEVICE_ID) String deviceId) {
        accountHelper.checkUserAdmin(deviceId);
        accountHelper.clearAll();
        return new Result();
    }
}
