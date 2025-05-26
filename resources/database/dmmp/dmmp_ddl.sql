create database if not exists dmmp;
use dmmp;
create table dmmp.MP_ACCOUNT
(
    OPEN_ID     varchar(128) default '' not null comment '小程序open-id'
        primary key,
    NICKNAME    varchar(128)            null comment '昵称',
    AVATAR_URL  varchar(128)            null comment '头像链接',
    GENDER      int                     null comment '性别',
    SESSION_KEY varchar(256)            null comment 'session-key',
    UNION_ID    varchar(256)            null comment 'union-id',
    SIGN_TIME   datetime                null comment '登录、注册时间',
    MOBILE      varchar(256)            null comment '手机号',
    STATE       int          default 0  null
)
    comment '小程序用户';

create table dmmp.MP_DEVICE
(
    SALT        varchar(32)            not null comment '密钥',
    USER_ID     varchar(64)            null comment '用户id',
    ACCOUNT_ID  varchar(64)            null comment '账户id',
    IP          varchar(128)           null comment '客户端ip',
    CREATOR     varchar(64)            null comment '创建人用户id',
    MODIFIER    varchar(64)            null comment '修改人用户id',
    STATE       int         default 0  null comment '状态，0-正常 1-删除',
    CREATE_TIME datetime               null comment '创建时间',
    MODIFY_TIME datetime               null comment '修改时间',
    ID          varchar(64) default '' not null comment '唯一标志'
        primary key
)
    comment '设备';

create table dmmp.MP_FILE
(
    ID        varchar(64)   not null
        primary key,
    DATA      longblob      not null,
    FILENAME  varchar(1024) not null,
    EXTENSION varchar(64)   not null
);

create table dmmp.MP_H5
(
    TITLE       varchar(512)  null comment '标题',
    CONTENT     mediumtext    not null comment '内容',
    REMARK      varchar(1024) null comment '备注',
    CREATOR     varchar(64)   null comment '创建人用户id',
    MODIFIER    varchar(64)   null comment '修改人用户id',
    STATE       int default 0 null comment '状态，0-正常 1-删除',
    CREATE_TIME datetime      null comment '创建时间',
    MODIFY_TIME datetime      null comment '修改时间',
    ID          varchar(64)   not null comment '唯一标志'
        primary key
);

create table dmmp.MP_IMG
(
    ID            varchar(64)   not null
        primary key,
    DATA          mediumblob    not null,
    FILENAME      varchar(1024) not null,
    EXTENSION     varchar(64)   not null,
    COMPRESS_DATA mediumblob    not null
);

create table dmmp.MP_PLAN
(
    CREATE_DATE        varchar(256)    null comment '创建日期',
    DEPARTMENT         varchar(256)    null comment '部门',
    OPTIMIZE_TYPE      varchar(256)    null comment '精益类型',
    EXISTS_PROBLEM     varchar(256)    null comment '存在问题',
    SOLVE_SCHEME       varchar(256)    null comment '解决方案',
    RESPONSIBLE_PERSON varchar(256)    null comment '负责人',
    PLAN_COMPLETE_TIME varchar(256)    null comment '负责人',
    AWARD_AMOUNT       decimal(38, 10) null comment '奖励金额',
    VALID              bit             null comment '结案',
    CREATOR            varchar(64)     null comment '创建人用户id',
    MODIFIER           varchar(64)     null comment '修改人用户id',
    STATE              int default 0   null comment '状态，0-正常 1-删除',
    CREATE_TIME        datetime        null comment '创建时间',
    MODIFY_TIME        datetime        null comment '修改时间',
    ID                 varchar(64)     not null comment '唯一标志'
        primary key
)
    comment '精益持续改善';

create table dmmp.MP_ROLE
(
    ROLE_CODE   varchar(32)   not null comment '角色编码',
    ROLE_NAME   varchar(256)  null,
    ROLE_REMARK varchar(256)  null comment '角色备注',
    TAG         varchar(256)  null comment '标签',
    CREATOR     varchar(64)   null comment '创建人用户id',
    MODIFIER    varchar(64)   null comment '修改人用户id',
    STATE       int default 0 null comment '状态，0-正常 1-删除',
    CREATE_TIME datetime      null comment '创建时间',
    MODIFY_TIME datetime      null comment '修改时间',
    ID          varchar(64)   not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_MpRole_1
        unique (ROLE_CODE)
)
    comment '角色表';

create table dmmp.MP_SIGN_IN_HISTORY
(
    DEVICE_ID     varchar(64)   null comment '设备ID',
    USER_ID       varchar(64)   null comment '用户ID',
    SIGN_IN_TIME  varchar(64)   null comment '登录时间',
    CLIENT_IP     varchar(64)   null comment '客户端IP',
    USER_AGENT    varchar(1024) null comment '客户端IP',
    SUCCESS       bit           null comment '是否成功',
    ERROR_MESSAGE varchar(256)  null comment '异常信息',
    ID            varchar(64)   not null comment '唯一标志'
        primary key,
    username      varchar(64)   null
)
    comment '登录历史';

create index IND_MSIH_SIT_0
    on dmmp.MP_SIGN_IN_HISTORY (SIGN_IN_TIME);

create table dmmp.MP_USER
(
    USERNAME                    varchar(32)             null comment '用户名',
    NAME                        varchar(32)             null comment '姓名',
    NICKNAME                    varchar(128)            null comment '昵称',
    PASSWORD_ENCRYPT            varchar(128)            null comment '密码密文',
    SALT                        varchar(32)             null comment '密码密钥',
    MOBILE                      varchar(64) default '1' null comment '手机',
    MAIL                        varchar(256)            null comment '邮箱',
    PASSWORD_EXPIRE_TIME        datetime                null comment '密码过期时间',
    SIGN_IN_LOCK                int         default 0   null comment '登录是否锁定',
    SIGN_IN_FAIL_COUNT          int         default 0   null comment '登录错误次数',
    SIGN_IN_LOCK_TIME           datetime                null comment '登录锁定时间',
    CREATOR                     varchar(64)             null comment '创建人用户id',
    MODIFIER                    varchar(64)             null comment '修改人用户id',
    STATE                       int         default 0   null comment '状态，0-正常 1-删除',
    CREATE_TIME                 datetime                null comment '创建时间',
    MODIFY_TIME                 datetime                null comment '修改时间',
    ID                          varchar(64)             not null comment '唯一标志'
        primary key,
    DEPARTMENT                  varchar(256)            null,
    PROFESSION                  varchar(256)            null,
    INTERVIEW_RESUME            mediumtext              null,
    SCHEDULE                    varchar(256)            null comment '班次',
    USER_PROPERTY               varchar(256)            null comment '人员属性',
    LEADER_USER_ID              varchar(64)             null,
    `LAST_INCREASE_SALARY_DATE` varchar(32),
    `PLAN_INCREASE_SALARY_DATE` varchar(32),
    `EMPLOYEE_ID`               varchar(64) default '' default null comment '员工工号'
)
    comment '用户表';

create index UNQ_15110_MpUser_1
    on dmmp.MP_USER (USERNAME);
create index UNQ_15110_MpUser_2
    on dmmp.MP_USER (MOBILE);
create unique index UNQ_15110_MpUser_3
    on dmmp.MP_USER (`EMPLOYEE_ID`);



create table dmmp.MP_USER_PHOTO
(
    USER_ID            varchar(64)  null comment '用户ID',
    PHOTO_URL          varchar(256) null comment '照片链接',
    PHOTO_COMPRESS_URL varchar(256) null comment '照片压缩链接',
    ID                 varchar(64)  not null comment '唯一标志'
        primary key
)
    comment '用户头像';

create table dmmp.MP_USER_ROLE
(
    USER_ID varchar(64) not null comment '用户id',
    ROLE_ID varchar(64) not null comment '角色id',
    primary key (USER_ID, ROLE_ID)
)
    comment '用户和角色关联关系表';

create table dmmp.MP_VIDEO
(
    ID            varchar(64)   not null
        primary key,
    DATA          mediumblob    not null,
    FILENAME      varchar(1024) not null,
    EXTENSION     varchar(64)   not null,
    COMPRESS_DATA mediumblob    not null
);

