create database if not exists douson;
use douson;
create table douson.MP_ASSEMBLY
(
    SERIAL_NUMBER                varchar(256)    null comment '整机序列号',
    MAX_SERIAL_INDEX             int             null comment '最大整机序列号索引',
    SERIAL_INDEX                 int             null comment '整机序列号索引',
    PURCHASE_ORDER_NO            varchar(64)     null comment '采购订单编号',
    PO_PROJECT                   varchar(32)     null comment 'PO项目',
    SALE_ORDER_NO                varchar(64)     null comment '销售订单',
    ORDER_PROJECT                varchar(32)     null comment '订单项目',
    MATERIAL_NO                  varchar(64)     null comment '物料号',
    MATERIAL_DESCRIPTION         varchar(512)    null comment '物料描述',
    DESIGN_NUMBER                varchar(64)     null comment '图号',
    DELIVERY_DATE                varchar(32)     null comment '承诺交期',
    ORDER_COUNT                  int             null comment '订单数量',
    COMPLETED_QTY                int             null comment '完成数量',
    DESCRIPTION                  varchar(512)    null comment '备注',
    VALVE_BODY                   varchar(64)     null comment '阀体',
    VALVE_COVER                  varchar(64)     null comment '阀盖/尾盖',
    GATE                         varchar(64)     null comment '闸板',
    VALVE_SEAT                   varchar(64)     null comment '阀座/阀瓣',
    VALVE_SEAT_PHOTO             varchar(256)    null comment '阀座/阀瓣照片',
    VALVE_STEM                   varchar(256)    null comment '阀杆/尾杆',
    ASSEMBLY_PERSON              varchar(64)     null comment '装配人员',
    PRESSURE_TEST                varchar(64)     null comment '整机和驱动器试压',
    TORQUE_NM                    decimal(38, 10) null comment '闸阀开关扭矩，N.m',
    OIL_INJECTION                varchar(64)     null comment '注油',
    TESTER                       varchar(64)     null comment '试压人员',
    ASSEMBLY_COMPLETE_DATE       varchar(32)     null comment '装配完成日期',
    ASSEMBLY_COMPLETE_COUNT      int             null comment '装配完成数量',
    CREATOR                      varchar(64)     null comment '创建人用户id',
    MODIFIER                     varchar(64)     null comment '修改人用户id',
    REMARK                       varchar(512)    null comment '备注',
    CREATED_TIME                 datetime        not null comment '创建时间',
    LAST_MODIFIED_TIME           datetime        not null comment '修改时间',
    DELETED_FLAG                 int             null comment '删除标志',
    ID                           varchar(64)     not null comment '唯一标志'
        primary key,
    MAX_SERIAL_ORDER_INDEX       decimal(8)      null,
    COUNT                        decimal(8)      null,
    OIL_INJECTION_COMPLETE_DATE  varchar(32)     null comment '注油完成日期',
    OIL_INJECTION_COMPLETE_COUNT int             null comment '注油完成数量',
    COMPLETE_DATE                varchar(32)     null comment '完成日期',
    ASSEMBLY_INDEX               int             null comment '注油完成索引'
)
    comment '整机';

create index IDX_ASSEMBLY_UNQ_0
    on douson.MP_ASSEMBLY (MAX_SERIAL_INDEX, SERIAL_INDEX, PURCHASE_ORDER_NO, PO_PROJECT, SALE_ORDER_NO, ORDER_PROJECT);

create index IDX_M_A_A_I
    on douson.MP_ASSEMBLY (ASSEMBLY_INDEX);

create table douson.MP_ASSEMBLY_ATTACHMENT
(
    ASSEMBLY_ID         varchar(64)  null comment '整机ID',
    ATTACHMENT_CATEGORY varchar(64)  null comment '附件分类',
    ATTACHMENT_TYPE     varchar(32)  null comment '附件类型',
    URL                 varchar(256) null comment '链接',
    FILE_ID             varchar(256) null comment '文件ID',
    FILENAME            varchar(256) null comment '文件名',
    COMPRESS_URL        varchar(256) null comment '压缩链接',
    ID                  varchar(64)  not null comment '唯一标志'
        primary key
)
    comment '整机附件';

create index IDX_ASSEMBLY_ATT_UNQ_0
    on douson.MP_ASSEMBLY_ATTACHMENT (ASSEMBLY_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL, FILE_ID);

create table douson.MP_BOX_FLAG
(
    DATE                varchar(32)     null comment '日期',
    CUSTOMER_SHORT_NAME varchar(64)     null comment '客户简称',
    PURCHASE_ORDER_NO   varchar(256)    null comment '采购订单编号',
    PO_PROJECT          varchar(128)    null comment 'PO项目',
    SALE_ORDER_NO       varchar(128)    null comment '销售订单',
    ORDER_PROJECT       varchar(128)    null comment '订单项目',
    EACH_BOX_COUNT      int             null comment '每箱数量',
    BOX_NUMBER          int             null comment '箱号',
    LENGTH              decimal(38, 10) null comment '长',
    WIDTH               decimal(38, 10) null comment '宽',
    HEIGHT              decimal(38, 10) null comment '高',
    UNIT_WEIGHT         decimal(38, 10) null comment '单件重量',
    EACH_BOX_WEIGHT     decimal(38, 10) null comment '每箱重量',
    ORDER_NO            varchar(256)    null comment '单号',
    REMARK              varchar(1024)   null comment '备注',
    CREATOR             varchar(64)     null comment '创建人用户id',
    MODIFIER            varchar(64)     null comment '修改人用户id',
    STATE               int default 0   null comment '状态，0-正常 1-删除',
    CREATE_TIME         datetime        null comment '创建时间',
    MODIFY_TIME         datetime        null comment '修改时间',
    ID                  varchar(64)     not null comment '唯一标志'
        primary key,
    SERIAL_NO           varchar(1024)   null,
    MATERIAL_NO         varchar(256)    null,
    SEND_COUNT          decimal(18, 2)  null,
    SEND_DATE           varchar(32)     null,
    constraint UNQ_15110_BoxFlag_0
        unique (ORDER_NO),
    constraint UNQ_15110_BoxFlag_1
        unique (SALE_ORDER_NO, ORDER_PROJECT, BOX_NUMBER)
)
    comment '装箱标识卡';

create table douson.MP_BOX_FLAG_PHOTO
(
    BOX_FLAG_ID        varchar(64)  null comment '装箱标识卡ID',
    PHOTO_URL          varchar(256) null comment '照片链接',
    PHOTO_COMPRESS_URL varchar(256) null comment '照片压缩链接',
    ID                 varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_BoxFlagPhoto_0
        unique (BOX_FLAG_ID, PHOTO_URL)
)
    comment '零件';

create table douson.MP_BOX_FLAG_SERIAL_NO
(
    BOX_FLAG_ID varchar(64) null comment '装箱标识卡ID',
    SERIAL_NO   varchar(64) null comment '序列号',
    ID          varchar(64) not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_BoxFlagSerialNo_0
        unique (SERIAL_NO)
)
    comment '序列号';

create table douson.MP_COMPUTER
(
    MATERIAL_NO    varchar(256)  null comment '物料号',
    BRAND          varchar(256)  null comment '品牌',
    NAME           varchar(256)  null comment '设备名称',
    MODEL          varchar(256)  null comment '设备型号',
    USER_ID        varchar(256)  null comment '使用人',
    POSITION       varchar(256)  null comment '位置',
    STORAGE_DATE   varchar(256)  null comment '入库日期',
    COMPUTER_STATE varchar(256)  null comment '状态',
    DETAILED       bit           null comment '是否有账',
    PRODUCT_PLACE  varchar(256)  null comment '设备原产地',
    SUPPLIER       varchar(256)  null comment '供应商',
    REMARK         varchar(256)  null comment '备注',
    CREATOR        varchar(64)   null comment '创建人用户id',
    MODIFIER       varchar(64)   null comment '修改人用户id',
    STATE          int default 0 null comment '状态，0-正常 1-删除',
    CREATE_TIME    datetime      null comment '创建时间',
    MODIFY_TIME    datetime      null comment '修改时间',
    ID             varchar(64)   not null comment '唯一标志'
        primary key
)
    comment '检查设备台账';

create table douson.MP_COMPUTER_PHOTO
(
    COMPUTER_ID        varchar(64)  null comment '电脑ID',
    PHOTO_URL          varchar(256) null comment '照片链接',
    PHOTO_COMPRESS_URL varchar(256) null comment '照片压缩链接',
    ID                 varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_ComputerPhoto_0
        unique (COMPUTER_ID, PHOTO_URL)
)
    comment '零件';

create table douson.MP_DEVICE_CHECK_LEDGER
(
    MATERIAL_NO               varchar(256)  null comment '物料号',
    DEVICE_NUMBER             varchar(256)  null comment '设备编号',
    CHINESE_VIETNAM_NAME      varchar(256)  null comment '中越文名称',
    ENGLISH_NAME              varchar(256)  null comment '英文名称',
    SPECIFICATION             varchar(256)  null comment '规格',
    CALIBRATION_UNIT          varchar(256)  null comment '校准单位',
    CALIBRATION_DATE          varchar(256)  null comment '校准日期',
    DUE_DATE                  varchar(256)  null comment '有效期(下次校验日期)',
    CALIBRATION_PERIOD        int           null comment '校验周期（天）',
    MANUFACTURERS             varchar(256)  null comment '厂家',
    ACCEPTANCE_STANDARD       varchar(256)  null comment '验收标准',
    STORAGE                   varchar(256)  null comment '库位',
    OUT_OF_STOCK              bit(2)        null comment '是否出库',
    BORROWER                  varchar(256)  null comment '借用人',
    BORROW_DATE               varchar(256)  null comment '借用日期',
    CREATOR                   varchar(64)   null comment '创建人用户id',
    MODIFIER                  varchar(64)   null comment '修改人用户id',
    DEVICE_CHECK_LEDGER_STATE int default 0 null comment '状态，参考参数',
    STATE                     int default 0 null comment '状态，0-正常 1-删除',
    CREATE_TIME               datetime      null comment '创建时间',
    MODIFY_TIME               datetime      null comment '修改时间',
    ID                        varchar(64)   not null comment '唯一标志'
        primary key
);

create table douson.MP_DEVICE_CHECK_LEDGER_PHOTO
(
    DEVICE_CHECK_LEDGER_ID varchar(64)  null comment '检查设备台账ID',
    PHOTO_URL              varchar(256) null comment '照片链接',
    PHOTO_COMPRESS_URL     varchar(256) null comment '照片压缩链接',
    ID                     varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_DeviceCheckLedgerPhoto515_0
        unique (DEVICE_CHECK_LEDGER_ID, PHOTO_URL)
)
    comment '零件';

create table douson.MP_DISQUALIFICATION_ORDER
(
    DISQUALIFICATION_ORDER_NO varchar(256)  null comment '不合格单号',
    INSPECTOR                 varchar(256)  null comment '检验人员',
    ORDER_NO                  varchar(256)  null comment '单号',
    PROJECT_SEQUENCE          varchar(256)  null comment '项次',
    MATERIAL_NO               varchar(256)  null comment '物料号',
    DESIGN_NUMBER             varchar(256)  null comment '图号',
    PROCESS                   varchar(256)  null comment '流程',
    DISQUALIFICATION_CONTENT  varchar(256)  null comment '不合格内容',
    COUNT                     varchar(256)  null comment '数量',
    CHECK_POINT               varchar(256)  null comment '检验节点',
    DUTY_PERSON               varchar(256)  null comment '责任人员',
    QUALITY_DEAL_OPINION      varchar(256)  null comment '质量处理意见',
    SKILL_DEAL_OPINION        varchar(256)  null comment '技术处理意见',
    FINE_AMOUNT               varchar(256)  null comment '扣款',
    REMARK                    varchar(1024) null comment '备注',
    STOVE_NO                  varchar(256)  null comment '炉号',
    HOT_BATCH_NO              varchar(256)  null comment '热批号',
    SERIAL_NO                 varchar(256)  null comment '序列号',
    DEFECT_TYPE               varchar(256)  null comment '缺陷类型',
    CREATOR                   varchar(64)   null comment '创建人用户id',
    MODIFIER                  varchar(64)   null comment '修改人用户id',
    STATE                     int default 0 null comment '状态，0-正常 1-删除',
    CREATE_TIME               datetime      null comment '创建时间',
    MODIFY_TIME               datetime      null comment '修改时间',
    ID                        varchar(64)   not null comment '唯一标志'
        primary key,
    DISQUALIFICATION_ORDER    varchar(256)  null
)
    comment '不合格单';

create table douson.MP_DISQUALIFICATION_ORDER_PHOTO
(
    DISQUALIFICATION_ORDER_ID varchar(64)  null comment '不合格单ID',
    PHOTO_URL                 varchar(256) null comment '照片链接',
    PHOTO_COMPRESS_URL        varchar(256) null comment '照片压缩链接',
    ID                        varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_DisqualificationOrderPhoto409_0
        unique (DISQUALIFICATION_ORDER_ID, PHOTO_URL)
)
    comment '零件';

create table douson.MP_FORUM
(
    USER_ID            varchar(64)     null comment '用户ID',
    H5_ID              varchar(64)     null comment 'H5的ID',
    THUMBS_UP          decimal(38, 10) null comment '点赞',
    COMMENTARY         decimal(38, 10) null comment '评论',
    CREATOR            varchar(256)    null comment 'null',
    MODIFIER           varchar(256)    null comment 'null',
    REMARK             varchar(256)    null comment 'null',
    CREATED_TIME       datetime        not null comment '创建时间',
    LAST_MODIFIED_TIME datetime        not null comment '修改时间',
    DELETED_FLAG       int             null comment '删除标志',
    ID                 varchar(64)     not null comment '唯一标志'
        primary key
);

create table douson.MP_FORUM_COMMENTARY
(
    FORUM_ID           varchar(64)  null comment '论坛ID',
    PARENT_ID          varchar(64)  null comment '上级论坛ID',
    USER_ID            varchar(64)  null comment '用户ID',
    CONTENT            mediumtext   null comment '内容',
    CREATOR            varchar(256) null comment 'null',
    MODIFIER           varchar(256) null comment 'null',
    REMARK             varchar(256) null comment 'null',
    CREATED_TIME       datetime     not null comment '创建时间',
    LAST_MODIFIED_TIME datetime     not null comment '修改时间',
    DELETED_FLAG       int          null comment '删除标志',
    ID                 varchar(64)  not null comment '唯一标志'
        primary key
);

create table douson.MP_FORUM_THUMBS_UP
(
    FORUM_ID  varchar(64) not null comment '论坛ID',
    USER_ID   varchar(64) not null comment '用户ID',
    THUMBS_UP int         null comment '是否点赞',
    primary key (FORUM_ID, USER_ID)
);

create table douson.MP_INDUSTRY_ACCIDENT
(
    REPORT_DATE               varchar(256)     null comment '日期',
    USER_ID                   varchar(64)      null comment '操作人',
    ACCIDENT_TYPE             varchar(64)      null comment '事故类型',
    DUTY_PERSON               varchar(128)     null comment '责任人',
    GROUP_LEADER              varchar(128)     null comment '组长',
    CHARGE_PERSON             varchar(128)     null comment '主管',
    MANAGER                   varchar(128)     null comment '经理',
    DEVICE_DESCRIBE           varchar(256)     null comment '设备描述',
    DESIGN_NUMBER_DESCRIBE    varchar(256)     null comment '图号描述',
    PRODUCT_WEIGHT            decimal(38, 10)  null comment 'null',
    ACCIDENT_DESCRIBE         varchar(256)     null comment '问题描述',
    DAMAGE_DESCRIBE           varchar(256)     null comment '伤害描述',
    PROPERTY_LOSS_DESCRIBE    varchar(256)     null comment '财产损失描述',
    HUMAN_FACTOR_REASON       varchar(256)     null comment '人的因素原因',
    DEVICE_REASON             varchar(256)     null comment '设备原因',
    MATERIAL_REASON           varchar(256)     null comment '材料原因',
    WORK_METHOD_REASON        varchar(256)     null comment '工作方法原因',
    ENVIRONMENT_REASON        varchar(256)     null comment '环境原因',
    HUMAN_FACTOR_SOLVE        varchar(256)     null comment '人的因素解决方法',
    DEVICE_SOLVE              varchar(256)     null comment '设备解决方法',
    MATERIAL_SOLVE            varchar(256)     null comment '材料解决方法',
    WORK_METHOD_SOLVE         varchar(256)     null comment '工作方法解决方法',
    ENVIRONMENT_SOLVE         varchar(256)     null comment '环境解决方法',
    DUTY_PERSON1              varchar(128)     null comment '责任人1',
    FINE_AMOUNT1              decimal(38, 10)  null comment '罚款金额1',
    DUTY_PERSON2              varchar(128)     null comment '责任人2',
    FINE_AMOUNT2              decimal(38, 10)  null comment '罚款金额2',
    DUTY_PERSON3              varchar(128)     null comment '责任人3',
    FINE_AMOUNT3              decimal(38, 10)  null comment '罚款金额3',
    IMPROVE_EVIDENCE_DESCRIBE varchar(64)      null comment '改善后的证据描述',
    VALID                     bit default b'0' null comment '是否有效',
    CREATOR                   varchar(256)     null comment 'null',
    MODIFIER                  varchar(256)     null comment 'null',
    REMARK                    varchar(256)     null comment 'null',
    CREATED_TIME              datetime         not null comment '创建时间',
    LAST_MODIFIED_TIME        datetime         not null comment '修改时间',
    DELETED_FLAG              int              null comment '删除标志',
    ID                        varchar(64)      not null comment '唯一标志'
        primary key
)
    comment '事故记录';

create table douson.MP_INDUSTRY_ACCIDENT_ATTACHMENT
(
    ACCIDENT_ID         varchar(64)  null comment '事故id',
    ATTACHMENT_CATEGORY varchar(64)  null comment '附件分类',
    ATTACHMENT_TYPE     varchar(32)  null comment '附件类型',
    URL                 varchar(256) null comment '链接',
    COMPRESS_URL        varchar(256) null comment '压缩链接',
    ID                  varchar(64)  not null comment '唯一标志'
        primary key
)
    comment '事故记录';

create table douson.MP_INDUSTRY_CRASH
(
    REPORT_DATE        varchar(256)     null comment '日期',
    USER_ID            varchar(64)      null comment '当事人',
    DIRECT_LEADER      varchar(64)      null comment '上级领导',
    ACCIDENT_DESCRIBE  varchar(256)     null comment '问题描述',
    REASON             varchar(256)     null comment '原因',
    SOLVE              varchar(256)     null comment '解决方法',
    IMPROVE_DESCRIBE   varchar(256)     null comment '改善后的证据描述',
    OPINION            varchar(64)      null comment '奖惩意见',
    VALID              bit default b'0' null comment '是否有效',
    CREATOR            varchar(256)     null comment 'null',
    MODIFIER           varchar(256)     null comment 'null',
    REMARK             varchar(256)     null comment 'null',
    CREATED_TIME       datetime         not null comment '创建时间',
    LAST_MODIFIED_TIME datetime         not null comment '修改时间',
    DELETED_FLAG       int              null comment '删除标志',
    ID                 varchar(64)      not null comment '唯一标志'
        primary key,
    EQUIPMENT_ID       varchar(64)      null,
    SERIAL_NO          varchar(16)      null
)
    comment '事故记录';

create table douson.MP_INDUSTRY_CRASH_ATTACHMENT
(
    CRASH_ID            varchar(64)  null,
    ATTACHMENT_CATEGORY varchar(64)  null comment '附件分类',
    ATTACHMENT_TYPE     varchar(32)  null comment '附件类型',
    URL                 varchar(256) null comment '链接',
    FILE_ID             varchar(256) null comment '文件ID',
    FILENAME            varchar(256) null comment '文件名',
    COMPRESS_URL        varchar(256) null comment '压缩链接',
    ID                  varchar(64)  not null comment '唯一标志'
        primary key
)
    comment '事故记录';

create index UNQ_15110_EventAttachment_0
    on douson.MP_INDUSTRY_CRASH_ATTACHMENT (CRASH_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL);

create table douson.MP_INDUSTRY_DEVICE
(
    DEVICE_NAME    varchar(256)                         null comment '设备名称',
    UNIT_PRICE     decimal(38, 10) default 0.0000000000 null comment '单价',
    RUNNING_HOUR   decimal(38, 10) default 0.0000000000 null comment '运行小时',
    RUNNING_MINUTE decimal(38, 10) default 0.0000000000 null comment '运行分钟',
    SORTER         int             default 0            null comment '排序',
    ID             varchar(64)                          not null comment '唯一标志'
        primary key,
    SUPPLIER       int                                  null
)
    comment '设备';

create table douson.MP_INDUSTRY_EQUIPMENT
(
    EQUIPMENT_NO       varchar(256) null comment '设备编号',
    EQUIPMENT_NAME     varchar(256) null comment '设备名称',
    SPECIFICATION      varchar(256) null comment '规格',
    DATE               varchar(32)  null comment '日期',
    USER_ID            varchar(64)  null comment '使用人员',
    REMARK             varchar(256) null comment '备注',
    DETAIL_DESCRIBE    varchar(256) null comment '设备细节描述',
    GASOLINE_TYPE      varchar(256) null comment '设备细节描述',
    CHARGE_USER        varchar(64)  null comment '负责人',
    POSITION           varchar(256) null comment '位置',
    CREATOR            varchar(256) null comment 'null',
    MODIFIER           varchar(256) null comment 'null',
    CREATED_TIME       datetime     not null comment '创建时间',
    LAST_MODIFIED_TIME datetime     not null comment '修改时间',
    DELETED_FLAG       int          null comment '删除标志',
    ID                 varchar(64)  not null comment '唯一标志'
        primary key,
    USE_USER           varchar(256) null,
    constraint UNQ_15110_Equipment732_0
        unique (EQUIPMENT_NO)
)
    comment '事故记录';

create table douson.MP_INDUSTRY_EQUIPMENT_ATTACHMENT
(
    EQUIPMENT_ID        varchar(64)  null comment '设备编号',
    ATTACHMENT_CATEGORY varchar(64)  null comment '附件分类',
    ATTACHMENT_TYPE     varchar(32)  null comment '附件类型',
    URL                 varchar(256) null comment '链接',
    FILE_ID             varchar(256) null comment '文件ID',
    FILENAME            varchar(256) null comment '文件名',
    COMPRESS_URL        varchar(256) null comment '压缩链接',
    ID                  varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_EquipmentAttachment732_0
        unique (EQUIPMENT_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL, FILE_ID)
)
    comment '事故记录';

create table douson.MP_INDUSTRY_EVENT
(
    REPORT_DATE        varchar(256)     null comment '日期',
    USER_ID            varchar(64)      null comment '当事人',
    DIRECT_LEADER      varchar(64)      null comment '上级领导',
    ACCIDENT_DESCRIBE  varchar(256)     null comment '问题描述',
    REASON             varchar(256)     null comment '原因',
    SOLVE              varchar(256)     null comment '解决方法',
    IMPROVE_DESCRIBE   varchar(256)     null comment '改善后的证据描述',
    OPINION            varchar(64)      null comment '奖惩意见',
    VALID              bit default b'0' null comment '是否有效',
    CREATOR            varchar(256)     null comment 'null',
    MODIFIER           varchar(256)     null comment 'null',
    REMARK             varchar(256)     null comment 'null',
    CREATED_TIME       datetime         not null comment '创建时间',
    LAST_MODIFIED_TIME datetime         not null comment '修改时间',
    DELETED_FLAG       int              null comment '删除标志',
    ID                 varchar(64)      not null comment '唯一标志'
        primary key,
    SERIAL_NO          varchar(16)      null,
    constraint UNQ_15110_MP_INDUSTRY_EVENT_0
        unique (SERIAL_NO)
)
    comment '事故记录';

create table douson.MP_INDUSTRY_EVENT_ATTACHMENT
(
    EVENT_ID            varchar(64)  null comment '事故id',
    ATTACHMENT_CATEGORY varchar(64)  null comment '附件分类',
    ATTACHMENT_TYPE     varchar(32)  null comment '附件类型',
    URL                 varchar(256) null comment '链接',
    FILE_ID             varchar(256) null comment '文件ID',
    FILENAME            varchar(256) null comment '文件名',
    COMPRESS_URL        varchar(256) null comment '压缩链接',
    ID                  varchar(64)  not null comment '唯一标志'
        primary key
)
    comment '事故记录';

create index UNQ_15110_EventAttachment_0
    on douson.MP_INDUSTRY_EVENT_ATTACHMENT (EVENT_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL);

create table douson.MP_INDUSTRY_IMPROVE
(
    REPORT_DATE        varchar(256)     null comment '日期',
    USER_ID            varchar(1024)    null,
    DIRECT_LEADER      varchar(64)      null comment '上级领导',
    ACCIDENT_DESCRIBE  varchar(1024)    null comment '问题描述',
    REASON             varchar(256)     null comment '原因',
    SOLVE              varchar(256)     null comment '解决方法',
    IMPROVE_DESCRIBE   varchar(64)      null comment '改善后的证据描述',
    OPINION            varchar(64)      null comment '奖惩意见',
    VALID              bit default b'0' null comment '是否有效',
    CREATOR            varchar(256)     null comment 'null',
    MODIFIER           varchar(256)     null comment 'null',
    REMARK             varchar(256)     null comment 'null',
    CREATED_TIME       datetime         not null comment '创建时间',
    LAST_MODIFIED_TIME datetime         not null comment '修改时间',
    DELETED_FLAG       int              null comment '删除标志',
    ID                 varchar(64)      not null comment '唯一标志'
        primary key,
    EQUIPMENT_ID       varchar(64)      null,
    TOOL_DESCRIBE      varchar(512)     null,
    SERIAL_NO          varchar(16)      null
)
    comment '改善记录';

create table douson.MP_INDUSTRY_IMPROVE_ATTACHMENT
(
    IMPROVE_ID          varchar(64)  null comment '改善id',
    ATTACHMENT_CATEGORY varchar(64)  null comment '附件分类',
    ATTACHMENT_TYPE     varchar(32)  null comment '附件类型',
    URL                 varchar(256) null comment '链接',
    FILE_ID             varchar(256) null comment '文件ID',
    FILENAME            varchar(256) null comment '文件名',
    COMPRESS_URL        varchar(256) null comment '压缩链接',
    ID                  varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_ImproveAttachment_0
        unique (IMPROVE_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL, FILE_ID)
)
    comment '事故记录';

create table douson.MP_INDUSTRY_MAINTAIN
(
    DATE               varchar(32)      null comment '日期',
    EQUIPMENT_ID       varchar(256)     null comment '维修ID',
    BROKEN_REASON      varchar(256)     null comment '故障原因',
    BROKEN_CONTENT     varchar(256)     null comment '故障内容',
    REPAIR_CONTENT     varchar(256)     null comment '修理内容',
    REPLACE_PAIR       varchar(256)     null comment '更换配件',
    REPAIR_TYPE        varchar(256)     null comment '维修类型',
    STOP_HOUR          decimal(38, 10)  null comment '停机时长H',
    PARTY_USER         varchar(64)      null comment '当事人',
    CREATOR            varchar(256)     null comment 'null',
    MODIFIER           varchar(256)     null comment 'null',
    REMARK             varchar(256)     null comment 'null',
    CREATED_TIME       datetime         not null comment '创建时间',
    LAST_MODIFIED_TIME datetime         not null comment '修改时间',
    DELETED_FLAG       int              null comment '删除标志',
    VALID              bit default b'0' null comment '是否有效',
    ID                 varchar(64)      not null comment '唯一标志'
        primary key
)
    comment '事故记录';

create table douson.MP_INDUSTRY_MAINTAIN_ATTACHMENT
(
    MAINTAIN_ID         varchar(64)  null comment '维修ID',
    ATTACHMENT_CATEGORY varchar(64)  null comment '附件分类',
    ATTACHMENT_TYPE     varchar(32)  null comment '附件类型',
    URL                 varchar(256) null comment '链接',
    FILE_ID             varchar(256) null comment '文件ID',
    FILENAME            varchar(256) null comment '文件名',
    COMPRESS_URL        varchar(256) null comment '压缩链接',
    ID                  varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_MaintainAttachment_0
        unique (MAINTAIN_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL, FILE_ID)
)
    comment '事故记录';

create table douson.MP_INDUSTRY_ORDER
(
    ORDER_NO              varchar(64)                       null comment '订单',
    PROJECT_SEQUENCE      varchar(64)                       null comment '项次',
    TEST_DEVICE           varchar(64)                       null comment '调试设备',
    PROCESS_PROCEDURE     varchar(64)                       null comment '加工工序',
    ORDER_COUNT           decimal(24, 6) default 0.000000   null,
    ID                    varchar(64)                       not null comment '唯一标志'
        primary key,
    ACTUAL_COMPLETE_COUNT decimal(32, 8) default 0.00000000 null
)
    comment '订单';

create index UNQ_15110_Order_0
    on douson.MP_INDUSTRY_ORDER (ORDER_NO, PROJECT_SEQUENCE, TEST_DEVICE, PROCESS_PROCEDURE);

create table douson.MP_INDUSTRY_PARAM
(
    PARAM_CATEGORY_ID varchar(64)  default '' not null comment '参数分类id',
    PARAM_CODE        varchar(128) default '' not null comment '参数编码',
    PARAM_NAME        varchar(256)            not null comment '参数名称',
    SORTER            int          default 0  not null,
    primary key (PARAM_CATEGORY_ID, PARAM_CODE)
)
    comment '参数表';

create index IND_15110_IndustryParam_0
    on douson.MP_INDUSTRY_PARAM (PARAM_CATEGORY_ID);

create table douson.MP_INDUSTRY_PRODUCT
(
    OPEN_ID            varchar(64)            null comment '小程序open-id',
    REPORT_DATE        datetime               null comment '日期',
    DESIGN_NUMBER      varchar(64)            null comment '图号',
    DEBUG_MINUTE       int                    null comment 'null',
    CLAMPING_MINUTE    int                    null comment 'null',
    ASSIST_MINUTE      int                    null comment 'null',
    RUNNING_MINUTE     int                    null comment 'null',
    PROGRAM_NUMBER     varchar(256)           null comment '0',
    CREATOR            varchar(256)           null comment 'null',
    MODIFIER           varchar(256)           null comment 'null',
    REMARK             varchar(256)           null comment 'null',
    CREATED_TIME       datetime               not null comment '创建时间',
    LAST_MODIFIED_TIME datetime               not null comment '修改时间',
    DELETED_FLAG       int                    null comment '删除标志',
    ID                 varchar(64) default '' not null comment '唯一标志'
        primary key,
    ORDER_ID           varchar(64)            null,
    constraint UNQ_15110_IndustryProduct_0
        unique (OPEN_ID, REPORT_DATE, ORDER_ID, DESIGN_NUMBER)
)
    comment '零件';

create table douson.MP_INDUSTRY_PRODUCT_PHOTO
(
    PRODUCT_ID         varchar(64) default '' not null comment '产品id',
    PHOTO_COMPRESS_URL varchar(256)           null comment '照片压缩链接',
    PHOTO_URL          varchar(180)           null comment '照片链接',
    ID                 varchar(64)            not null
        primary key,
    constraint mp_industry_product_photo_PRODUCT_ID_PHOTO_URL_uindex
        unique (PRODUCT_ID, PHOTO_URL)
)
    comment '零件';

create table douson.MP_INDUSTRY_QUALITY
(
    REPORT_DATE        varchar(256)     null comment '日期',
    USER_ID            varchar(64)      null comment '当事人',
    DIRECT_LEADER      varchar(64)      null comment '上级领导',
    ACCIDENT_DESCRIBE  varchar(256)     null comment '问题描述',
    REASON             varchar(256)     null comment '原因',
    SOLVE              varchar(256)     null comment '解决方法',
    IMPROVE_DESCRIBE   varchar(256)     null comment '改善后的证据描述',
    OPINION            varchar(64)      null comment '奖惩意见',
    VALID              bit default b'0' null comment '是否有效',
    CREATOR            varchar(256)     null comment 'null',
    MODIFIER           varchar(256)     null comment 'null',
    REMARK             varchar(256)     null comment 'null',
    CREATED_TIME       datetime         not null comment '创建时间',
    LAST_MODIFIED_TIME datetime         not null comment '修改时间',
    DELETED_FLAG       int              null comment '删除标志',
    ID                 varchar(64)      not null comment '唯一标志'
        primary key,
    SERIAL_NO          varchar(16)      null
)
    comment '事故记录';

create table douson.MP_INDUSTRY_QUALITY_ATTACHMENT
(
    QUALITY_ID          varchar(64)  null comment '事故id',
    ATTACHMENT_CATEGORY varchar(64)  null comment '附件分类',
    ATTACHMENT_TYPE     varchar(32)  null comment '附件类型',
    URL                 varchar(256) null comment '链接',
    FILE_ID             varchar(256) null comment '文件ID',
    FILENAME            varchar(256) null comment '文件名',
    COMPRESS_URL        varchar(256) null comment '压缩链接',
    ID                  varchar(64)  not null comment '唯一标志'
        primary key
)
    comment '事故记录';

create index UNQ_15110_EventAttachment_0
    on douson.MP_INDUSTRY_QUALITY_ATTACHMENT (QUALITY_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL);

create table douson.MP_INDUSTRY_REPORT
(
    PRODUCT_ID                  varchar(64)             null comment '产品id',
    REPORT_DATE                 varchar(18)             null comment '日期',
    USER_ID                     varchar(64)             null comment '操作人',
    PROCESS_TYPE                varchar(64)             null comment '加工类型',
    SCHEDULE                    varchar(64)             null comment '班次',
    ACTUAL_COMPLETE_COUNT       decimal(38, 10)         null,
    LEADER_SUBSIDY_MINUTE       int                     null,
    DEVICE_RUNNING_START_HOUR   int                     null,
    DEVICE_RUNNING_START_MINUTE int                     null,
    DEVICE_RUNNING_END_HOUR     int                     null,
    DEVICE_RUNNING_END_MINUTE   int                     null,
    STOP_WORKING_CONTENT1       varchar(256)            null,
    STOP_WORKING_MINUTE1        int                     null,
    STOP_WORKING_CONTENT2       varchar(256)            null,
    STOP_WORKING_MINUTE2        int                     null,
    STOP_WORKING_CONTENT3       varchar(256)            null,
    STOP_WORKING_MINUTE3        int                     null,
    IMPROVE_SUGGESTION          varchar(1024)           null,
    CREATOR                     varchar(256)            null,
    MODIFIER                    varchar(256)            null,
    REMARK                      varchar(256)            null,
    CREATED_TIME                datetime                not null comment '创建时间',
    LAST_MODIFIED_TIME          datetime                not null comment '修改时间',
    DELETED_FLAG                int                     null comment '删除标志',
    ID                          varchar(64)             not null comment '唯一标志'
        primary key,
    ORDER_ID                    varchar(64) default '0' null,
    VALID                       int         default 0   null,
    CLAMPING_MINUTE             int                     null comment '装夹时间/分钟',
    ASSIST_MINUTE               int                     null comment '辅助时间/分钟',
    RUNNING_MINUTE              int                     null comment '程式运行时间/分钟',
    WORK_MINUTE                 decimal(38, 10)         null
)
    comment '日报';

create index UNQ_15110_IndustryReport_0
    on douson.MP_INDUSTRY_REPORT (PRODUCT_ID, REPORT_DATE, USER_ID);

create index UNQ_15110_IndustryReport_1
    on douson.MP_INDUSTRY_REPORT (ORDER_ID);

create table douson.MP_INDUSTRY_REPORT_PHOTO
(
    REPORT_ID          varchar(64)  null comment '日报id',
    PHOTO_URL          varchar(128) null comment '照片链接',
    PHOTO_COMPRESS_URL varchar(256) null comment '照片压缩链接',
    ID                 varchar(64)  not null comment '唯一标志'
        primary key
)
    comment '零件';

create index UNQ_15110_IndustryReportPhoto729_0
    on douson.MP_INDUSTRY_REPORT_PHOTO (REPORT_ID, PHOTO_URL);

create table douson.MP_INDUSTRY_REPORT_SERIAL_NO
(
    REPORT_ID varchar(64) null comment '日报id',
    ORDER_ID  varchar(64) null comment '订单ID',
    SERIAL_NO varchar(64) null comment '序列号',
    ID        varchar(64) not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_IndustryReportSerialNo73_0
        unique (ORDER_ID, SERIAL_NO)
)
    comment '零件';

create table douson.MP_INDUSTRY_REPORT_USER
(
    REPORT_DATE varchar(18) not null comment '日期',
    USER_ID     varchar(64) not null comment '操作人',
    WORK_MINUTE int         null comment 'null',
    primary key (REPORT_DATE, USER_ID)
)
    comment '日报';

create table douson.MP_INDUSTRY_TROUBLE
(
    REPORT_DATE        varchar(256)     null comment '日期',
    USER_ID            varchar(64)      null comment '当事人',
    DIRECT_LEADER      varchar(64)      null comment '上级领导',
    ACCIDENT_DESCRIBE  varchar(256)     null comment '问题描述',
    REASON             varchar(256)     null comment '原因',
    SOLVE              varchar(256)     null comment '解决方法',
    IMPROVE_DESCRIBE   varchar(256)     null comment '改善后的证据描述',
    OPINION            varchar(64)      null comment '奖惩意见',
    VALID              bit default b'0' null comment '是否有效',
    CREATOR            varchar(256)     null comment 'null',
    MODIFIER           varchar(256)     null comment 'null',
    REMARK             varchar(256)     null comment 'null',
    CREATED_TIME       datetime         not null comment '创建时间',
    LAST_MODIFIED_TIME datetime         not null comment '修改时间',
    DELETED_FLAG       int              null comment '删除标志',
    ID                 varchar(64)      not null comment '唯一标志'
        primary key,
    EQUIPMENT_ID       varchar(64)      null,
    TOOL_DESCRIBE      varchar(512)     null,
    SERIAL_NO          varchar(16)      null
)
    comment '事故记录';

create table douson.MP_INDUSTRY_TROUBLE_ATTACHMENT
(
    TROUBLE_ID          varchar(64)  null,
    ATTACHMENT_CATEGORY varchar(64)  null comment '附件分类',
    ATTACHMENT_TYPE     varchar(32)  null comment '附件类型',
    URL                 varchar(256) null comment '链接',
    FILE_ID             varchar(256) null comment '文件ID',
    FILENAME            varchar(256) null comment '文件名',
    COMPRESS_URL        varchar(256) null comment '压缩链接',
    ID                  varchar(64)  not null comment '唯一标志'
        primary key
)
    comment '事故记录';

create index UNQ_15110_EventAttachment_0
    on douson.MP_INDUSTRY_TROUBLE_ATTACHMENT (TROUBLE_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL);

create table douson.MP_INDUSTRY_VOCATION
(
    VOCATION_TYPE      varchar(256)    null comment '请假类型',
    DATE               varchar(256)    null comment '申请日期',
    USER               varchar(256)    null comment '请假人',
    CHARGE_USER        varchar(256)    null comment '主管领导',
    START_DATE         varchar(256)    null comment '请假开始日期',
    END_DATE           varchar(256)    null comment '请假结束日期',
    REASON             varchar(256)    null comment '请假理由',
    COUNT              decimal(38, 10) null comment '请假天数',
    COMPLIANCE         bit             null comment '是否符合请假规定',
    VIOLATION_REASON   varchar(256)    null comment '不符合理由',
    CREATOR            varchar(256)    null comment 'null',
    MODIFIER           varchar(256)    null comment 'null',
    REMARK             varchar(256)    null comment 'null',
    CREATED_TIME       datetime        not null comment '创建时间',
    LAST_MODIFIED_TIME datetime        not null comment '修改时间',
    DELETED_FLAG       int             null comment '删除标志',
    ID                 varchar(64)     not null comment '唯一标志'
        primary key
)
    comment '请假记录';

create table douson.MP_INVENTORY
(
    MATERIAL_NO          varchar(64)     null comment '物料号',
    MATERIAL_DESCRIPTION varchar(256)    null comment '物料描述',
    DESIGN_NUMBER        varchar(128)    null comment '图号',
    INVENTORY_COUNT      decimal(38, 10) null comment '库存数量',
    INVENTORY_DATE       varchar(32)     null comment '库存日期',
    TYPE                 varchar(64)     null comment '类型',
    DESCRIPTION          varchar(256)    null comment '备注',
    MATERIAL_COUNT       decimal(38, 10) null comment '领料数量',
    MATERIAL_DATE        varchar(32)     null comment '领料日期',
    CREATOR              varchar(64)     null comment '创建人用户id',
    MODIFIER             varchar(64)     null comment '修改人用户id',
    STATE                int default 0   null comment '状态，0-正常 1-删除',
    CREATE_TIME          datetime        null comment '创建时间',
    MODIFY_TIME          datetime        null comment '修改时间',
    ID                   varchar(64)     not null comment '唯一标志'
        primary key
)
    comment '计划外库存';

create table douson.MP_INVENTORY_ATTACHMENT
(
    INVENTORY_ID        varchar(64)   null comment '库存id',
    ATTACHMENT_CATEGORY varchar(64)   null comment '附件分类',
    ATTACHMENT_TYPE     varchar(32)   null comment '附件类型',
    URL                 varchar(256)  null comment '链接',
    FILE_ID             varchar(256)  null comment '文件ID',
    FILENAME            varchar(256)  null comment '文件名',
    COMPRESS_URL        varchar(256)  null comment '压缩链接',
    CREATOR             varchar(64)   null comment '创建人用户id',
    MODIFIER            varchar(64)   null comment '修改人用户id',
    STATE               int default 0 null comment '状态，0-正常 1-删除',
    CREATE_TIME         datetime      null comment '创建时间',
    MODIFY_TIME         datetime      null comment '修改时间',
    ID                  varchar(64)   not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_InventoryAttachment328_0
        unique (INVENTORY_ID, FILE_ID)
)
    comment '生产计划';

create table douson.MP_MATERIAL
(
    CUSTOMER_SHORT_NAME       varchar(256)                         null comment '客户简称',
    CUSTOMER_ORDER_NO         varchar(64)                          null comment '客户订单号',
    CUSTOMER_PROJECT_SEQUENCE varchar(32)                          null comment '客户项次号',
    SALE_ORDER_NO             varchar(64)                          null comment '销售订单号',
    ORDER_PROJECT_NO          varchar(32)                          null comment '订单项目号',
    MATERIAL_NO               varchar(64)                          null comment '物料号',
    IMPROVE_MATERIAL_DESCRIBE varchar(256)                         null comment '加工物料描述',
    DESIGN_NUMBER             varchar(128)                         null comment '图号',
    ORDER_COUNT               decimal(39, 10)                      null comment '订单数量',
    PRODUCTION_DATE           varchar(32)                          null comment '投产日期',
    PROMISE_DONE_DATE         varchar(32)                          null comment '承诺交期',
    BLANK_MATERIAL_NO         varchar(128)                         null comment '毛坯物料号',
    BLANK_MATERIAL_DESCRIBE   varchar(256)                         null comment '毛坯物料描述',
    ROUGHCAST_DESIGN_NUMBER   varchar(64)                          null comment '毛坯图号',
    MATERIAL_COUNT            decimal(39, 10)                      null comment '领料数量',
    STOVE_NO                  varchar(64)                          null comment '炉号',
    HOT_BATCH_NO              varchar(64)                          null comment '热批号',
    SERIAL_NO                 varchar(256)                         null comment '序列号',
    SURPLUS_COUNT             decimal(39, 10)                      null comment '欠交数量',
    NDE                       varchar(256)                         null comment '`NDE`',
    ASSEMBLE                  varchar(256)                         null comment '装配',
    TEST_PRESS                varchar(256)                         null comment '试压',
    SURFACE_TREATMENT         varchar(256)                         null comment '表面处理',
    CHARGE_COMPANY            varchar(256)                         null comment '负责单位',
    DESCRIPTION               varchar(512)                         null comment '备注',
    PRODUCTION_COUNT          decimal(39, 10)                      null comment '排产数量',
    ARRANGE_PRODUCTION_DATE   varchar(32)                          null comment '排产日期',
    MATERIAL_ORDER_NO         varchar(64)                          null comment '领料单号',
    MATERIAL_PRINT_COUNT      decimal(39, 10) default 0.0000000000 null comment '领料单打印次数',
    CHECK_ORDER_NO            varchar(64)                          null comment '报检单号',
    CHECK_PRINT_COUNT         decimal(39, 10) default 0.0000000000 null comment '报检单打印次数',
    CREATOR                   varchar(64)                          null comment '创建人用户id',
    MODIFIER                  varchar(64)                          null comment '修改人用户id',
    STATE                     int             default 0            null comment '状态，0-正常 1-删除',
    CREATE_TIME               datetime                             null comment '创建时间',
    MODIFY_TIME               datetime                             null comment '修改时间',
    ID                        varchar(64)                          not null comment '唯一标志'
        primary key,
    REMAIN_COUNT              decimal(39, 10)                      null comment '剩余数量',
    GENERATE_TASK             int             default 0            null,
    SUM_MATERIAL_COUNT        decimal(26, 10)                      null
)
    comment '生产工单';

create index null_15110_Material_0
    on douson.MP_MATERIAL (SALE_ORDER_NO, ORDER_PROJECT_NO, PRODUCTION_DATE);

create table douson.MP_PLAN
(
    CREATE_DATE        varchar(256)    null comment '创建日期',
    DEPARTMENT         varchar(256)    null comment '部门',
    OPTIMIZE_TYPE      varchar(256)    null comment '精益类型',
    EXISTS_PROBLEM     varchar(1024)   null,
    SOLVE_SCHEME       varchar(1024)   null,
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
        primary key,
    SERIAL_NO          varchar(16)     null,
    RESPONSIBLE_TEAM   varchar(128)    null
)
    comment '精益持续改善';

create table douson.MP_PLAN_ATTACHMENT
(
    PLAN_ID  varchar(64)  null comment '精益持续改善ID',
    FILE_ID  varchar(256) null comment '文件ID',
    FILENAME varchar(256) null comment '文件名',
    URL      varchar(256) null comment 'null',
    ID       varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_PlanAttachment_0
        unique (PLAN_ID, FILE_ID)
)
    comment '精益持续改善附件';

create table douson.MP_PLAN_PHOTO
(
    PLAN_ID            varchar(64)  null comment '精益持续改善ID',
    PHOTO_URL          varchar(256) null comment '照片链接',
    PHOTO_COMPRESS_URL varchar(256) null comment '照片压缩链接',
    PHOTO_TYPE         int          null comment '照片压缩链接',
    ID                 varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_PlanPhoto_0
        unique (PLAN_ID, PHOTO_URL)
)
    comment '精益持续改善';

create table douson.MP_SCHEDULING
(
    DATE_MONTH                               varchar(16)  null comment '某一月',
    WEEK_INDEX                               int          null comment '第几周',
    SCHEDULE_DAY_TIME_LABEL                  varchar(512) null comment '白班',
    SCHEDULE_MIDDLE_LABEL                    varchar(512) null comment '中班',
    SCHEDULE_EVENING_LABEL                   varchar(512) null comment '夜班',
    SCHEDULE_DAY_TIME12_LABEL                varchar(512) null comment '白班12H',
    SCHEDULE_EVENING12_LABEL                 varchar(512) null comment '夜班12H',
    SCHEDULE_DAY_TIME_TECHNOLOGY_GROUP_LABEL varchar(512) null comment '白班技术组',
    SCHEDULE_EVENING_TECHNOLOGY_GROUP_LABEL  varchar(512) null comment '夜班技术组',
    CREATOR                                  varchar(64)  null comment '创建人用户id',
    MODIFIER                                 varchar(64)  null comment '修改人用户id',
    REMARK                                   varchar(512) null comment '备注',
    CREATED_TIME                             datetime     not null comment '创建时间',
    LAST_MODIFIED_TIME                       datetime     not null comment '修改时间',
    DELETED_FLAG                             int          null comment '删除标志',
    ID                                       varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_SCH_D_M
        unique (DATE_MONTH),
    constraint UNQ_SCH_D_M_W_I
        unique (DATE_MONTH, WEEK_INDEX)
)
    comment '排班';

create table douson.MP_SCHEDULING_DETAIL
(
    SCHEDULING_ID                      varchar(64)  null comment '排班ID',
    DEVICE_NUMBER                      varchar(64)  null comment '设备编号',
    SCHEDULE_DAY_TIME                  varchar(512) null comment '白班',
    SCHEDULE_MIDDLE                    varchar(512) null comment '中班',
    SCHEDULE_EVENING                   varchar(512) null comment '夜班',
    SCHEDULE_DAY_TIME12                varchar(512) null comment '白班12H',
    SCHEDULE_EVENING12                 varchar(512) null comment '夜班12H',
    SCHEDULE_DAY_TIME_TECHNOLOGY_GROUP varchar(512) null comment '白班技术组',
    SCHEDULE_EVENING_TECHNOLOGY_GROUP  varchar(512) null comment '夜班技术组',
    CREATOR                            varchar(64)  null comment '创建人用户id',
    MODIFIER                           varchar(64)  null comment '修改人用户id',
    REMARK                             varchar(512) null comment '备注',
    CREATED_TIME                       datetime     not null comment '创建时间',
    LAST_MODIFIED_TIME                 datetime     not null comment '修改时间',
    DELETED_FLAG                       int          null comment '删除标志',
    ID                                 varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_SCH_D_D_M_W_I
        unique (SCHEDULING_ID, DEVICE_NUMBER)
)
    comment '排班明细';

create table douson.MP_TASK
(
    DEVICE_ID                  varchar(256)                         null comment '设备',
    CUSTOMER_SHORT_NAME        varchar(256)                         null comment '客户简称',
    SALE_ORDER_NO              varchar(64)                          null comment '销售订单',
    ORDER_PROJECT_NO           varchar(32)                          null comment '订单项目',
    MATERIAL_NO                varchar(64)                          null comment '物料号',
    IMPROVE_MATERIAL_DESCRIBE  varchar(256)                         null comment '加工物料描述',
    DESIGN_NUMBER              varchar(128)                         null comment '图号',
    ORDER_COUNT                decimal(38, 10)                      null comment '订单数量',
    ROUGHCAST_EXPIRE_DATE      varchar(32)                          null comment '毛坯到货日期',
    MATERIAL_COUNT             decimal(38, 10)                      null comment '领料数量',
    PROMISE_DONE_DATE          varchar(32)                          null comment '承诺完成日期',
    PLAN_REFORM_COUNT          decimal(38, 10)                      null comment '计划加工数量',
    SUPPLIER_REMARK            varchar(256)                         null comment '外协工序备注',
    PRODUCT_COUNT_HOUR8        decimal(38, 10)                      null comment '8H班产量/件',
    PRODUCT_COUNT_HOUR12       decimal(38, 10)                      null comment '12小时班产量/件',
    PROCESS_WORKING_HOUR       decimal(38, 10)                      null comment '工序工时/件',
    OFFLINE_DATE               varchar(32)                          null comment '下线时间',
    DELAY                      decimal(32, 10)                      null,
    PROCESS_COUNT              decimal(38, 10)                      null comment '已加工数量',
    PROCESS_PROCEDURE          varchar(64)                          null comment '加工工序',
    NDE                        varchar(16)                          null comment '`NDE`',
    ASSEMBLE                   varchar(16)                          null comment '装配',
    TEST_PRESS                 varchar(16)                          null comment '试压',
    SURFACE_TREATMENT          varchar(16)                          null comment '表面处理',
    SURPLUS                    decimal(38, 10)                      null comment '剩余',
    MATERIAL_ORDER_NO          varchar(64)                          null comment '领料单号',
    CHECK_ORDER_NO             varchar(64)                          null comment '报检单号',
    SUPPLIER_DONE_DATE         varchar(32)                          null comment '要求外协完成交期',
    DELIVER_COUNT              decimal(38, 10)                      null comment '发货数量',
    DELIVER_DATE               varchar(32)                          null comment '发货日期',
    RECEIPT_COUNT              decimal(38, 10)                      null comment '收货数量',
    RECEIPT_DATE               varchar(32)                          null comment '收货日期',
    SCRAP_COUNT                decimal(38, 10)                      null comment '报废数量',
    SUPPLIER_PROMISE_DONE_DATE varchar(512)                         null,
    CREATOR                    varchar(64)                          null comment '创建人用户id',
    MODIFIER                   varchar(64)                          null comment '修改人用户id',
    STATE                      int             default 0            null comment '状态，0-正常 1-删除',
    CREATE_TIME                datetime                             null comment '创建时间',
    MODIFY_TIME                datetime                             null comment '修改时间',
    ID                         varchar(64)                          not null comment '唯一标志'
        primary key,
    ONLINE_DATE                varchar(32)                          null comment '上线时间',
    SORTER                     int                                  null,
    DEVICE_SORTER              int                                  null,
    ONLINE_DATE_DIFF           decimal(26, 10)                      null,
    DELIVER_DATE_REMARK        varchar(512)                         null,
    RECEIPT_DATE_REMARK        varchar(512)                         null,
    SUPPLIER_DONE_DATE_DIFF    decimal(38, 10) default 0.0000000000 null
)
    comment '生产计划';

create table douson.MP_TEMPLATE
(
    BORROW_TEMPLATE_PERSON varchar(64)     null comment '借用方',
    BORROW_TEMPLATE_DATE   varchar(32)     null comment '借用日期',
    MATERIAL_NO            varchar(256)    null comment '物料号',
    MATERIAL_DESCRIPTION   varchar(512)    null comment '物料描述',
    TEMPLATE_COUNT         decimal(38, 10) null comment '数量',
    PROMISE_RETURN_DATE    varchar(32)     null comment '承诺归还日期',
    OPERATOR_PERSON        varchar(64)     null comment '经办人',
    BORROW_PHOTO_COUNT     decimal(38, 10) null comment '借出照片数量',
    RETURN_PHOTO_COUNT     decimal(38, 10) null comment '还回照片数量',
    DESCRIPTION            varchar(512)    null comment '备注',
    RETURN_COUNT           decimal(38, 10) null comment '已还数量',
    ACTUAL_RETURN_DATE     varchar(32)     null comment '实际归还日期',
    MEET_REQUIREMENT       bit             null comment '是否符合要求',
    TEMPLATE_ORDER_NO      varchar(256)    null comment '单号',
    CREATOR                varchar(64)     null comment '创建人用户id',
    MODIFIER               varchar(64)     null comment '修改人用户id',
    STATE                  int default 0   null comment '状态，0-正常 1-删除',
    CREATE_TIME            datetime        null comment '创建时间',
    MODIFY_TIME            datetime        null comment '修改时间',
    ID                     varchar(64)     not null comment '唯一标志'
        primary key,
    `INDEX`                decimal(16, 8)  null
)
    comment '副本供应商刀具';

create table douson.MP_TEMPLATE_PHOTO
(
    TEMPLATE_ID        varchar(64)  null comment '供应商刀具模板ID',
    PHOTO_URL          varchar(256) null comment '照片链接',
    PHOTO_COMPRESS_URL varchar(256) null comment '照片压缩链接',
    PHOTO_TYPE         int          null comment '照片压缩链接',
    ID                 varchar(64)  not null comment '唯一标志'
        primary key,
    constraint UNQ_15110_TemplatePhoto_0
        unique (TEMPLATE_ID, PHOTO_URL)
)
    comment '副本供应商刀具图片';
ALTER TABLE douson.`MP_INVENTORY`
    ADD COLUMN `PLAN_QUANTITY`        DECIMAL(16, 2) COMMENT '计划数量',
    ADD COLUMN `REMAINING_QUANTITY`   DECIMAL(16, 2) COMMENT '剩余数量',
    ADD COLUMN `OUT_OF_PLAN_ORDER_NO` VARCHAR(64) COMMENT '计划外单号';
ALTER TABLE douson.`MP_MATERIAL`
    ADD `GENERATE_EXAMINE` INT(1) DEFAULT 0 COMMENT '是否生成检验单，0-否 1-是';
CREATE TABLE douson.MP_EXAMINE
(
    `CHECK_ORDER_NO`                 VARCHAR(512) COMMENT '报检单号',
    `ORDER_TOTAL_QUANTITY`           DECIMAL(38, 10) COMMENT '报检单合计数量',
    `IDENTIFICATION_HARDNESS_REMARK` VARCHAR(512) COMMENT '标识/硬度备注',
    `NDE_DIMENSION_REMARK`           VARCHAR(512) COMMENT 'NDE/尺寸备注',
    `INSPECTION_COMPLETED_QUANTITY`  DECIMAL(38, 10) COMMENT '检验完成数量',
    `CUSTOMER_SHORT_NAME`            VARCHAR(64) COMMENT '客户简称',
    `SALE_ORDER_NO`                  VARCHAR(64) COMMENT '销售订单',
    `ORDER_PROJECT_NO`               VARCHAR(64) COMMENT '订单项目号',
    `MATERIAL_NO`                    VARCHAR(64) COMMENT '物料号',
    `IMPROVE_MATERIAL_DESCRIBE`      VARCHAR(512) COMMENT '加工物料描述',
    `DESIGN_NUMBER`                  VARCHAR(64) COMMENT '图号',
    `ORDER_QUANTITY`                 DECIMAL(38, 10) COMMENT '订单数量',
    `PROMISE_DONE_DATE`              VARCHAR(32) COMMENT '承诺完成日期',
    `DESCRIPTION`                    VARCHAR(512) COMMENT '备注',
    `IDENTIFICATION_PERSON`          VARCHAR(64) COMMENT '标识人员',
    `IDENTIFICATION_DATE`            VARCHAR(32) COMMENT '标识日期',
    `INSPECTION_PERSON`              VARCHAR(64) COMMENT '检验人员',
    `INSPECTION_COMPLETED_DATE`      VARCHAR(32) COMMENT '检验完成日期',
    `CREATOR`                        VARCHAR(64) COMMENT '创建人用户id',
    `MODIFIER`                       VARCHAR(64) COMMENT '修改人用户id',
    `STATE`                          INT(2) DEFAULT 0 COMMENT '状态，0-正常 1-删除',
    `CREATE_TIME`                    DATETIME COMMENT '创建时间',
    `MODIFY_TIME`                    DATETIME COMMENT '修改时间',
    `ID`                             VARCHAR(64) COMMENT '唯一标志',
    CONSTRAINT PK_15110_Examine_0 PRIMARY KEY (ID)
);
ALTER TABLE douson.MP_EXAMINE
    COMMENT '订单检验记录';
CREATE INDEX UNQ_15110_Examine_1 ON MP_EXAMINE (SALE_ORDER_NO, ORDER_PROJECT_NO);
CREATE TABLE douson.MP_EXAMINE_ATTACHMENT
(
    `EXAMINE_ID`          VARCHAR(64) COMMENT '检验记录id',
    `ATTACHMENT_CATEGORY` VARCHAR(64) COMMENT '附件分类',
    `ATTACHMENT_TYPE`     VARCHAR(32) COMMENT '附件类型',
    `URL`                 VARCHAR(256) COMMENT '链接',
    `FILE_ID`             VARCHAR(256) COMMENT '文件ID',
    `FILENAME`            VARCHAR(256) COMMENT '文件名',
    `COMPRESS_URL`        VARCHAR(256) COMMENT '压缩链接',
    `CREATOR`             VARCHAR(64) COMMENT '创建人用户id',
    `MODIFIER`            VARCHAR(64) COMMENT '修改人用户id',
    `STATE`               INT(2) DEFAULT 0 COMMENT '状态，0-正常 1-删除',
    `CREATE_TIME`         DATETIME COMMENT '创建时间',
    `MODIFY_TIME`         DATETIME COMMENT '修改时间',
    `ID`                  VARCHAR(64) COMMENT '唯一标志',
    CONSTRAINT PK_15110_ExamineAttachment_0 PRIMARY KEY (ID)
);
ALTER TABLE douson.MP_EXAMINE_ATTACHMENT
    COMMENT '订单检验记录附件';
CREATE UNIQUE INDEX UNQ_15110_ExamineAttachment_0 ON MP_EXAMINE_ATTACHMENT (EXAMINE_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL, FILE_ID);
ALTER TABLE douson.MP_EXAMINE
    ADD COLUMN `NDE` VARCHAR(256);
ALTER TABLE douson.MP_EXAMINE
    ADD COLUMN `ASSEMBLE` VARCHAR(256);
ALTER TABLE douson.MP_EXAMINE
    ADD COLUMN `TEST_PRESS` VARCHAR(256);
ALTER TABLE douson.MP_EXAMINE
    ADD COLUMN `SURFACE_TREATMENT` VARCHAR(256);
ALTER TABLE douson.MP_FORUM
    ADD COLUMN `CATEGORY`             VARCHAR(32),
    ADD COLUMN `COMMENTARY_SHOW_TYPE` INT;
CREATE INDEX IND_MP_FORUM_CATEGORY ON douson.MP_FORUM (CATEGORY);
ALTER TABLE douson.MP_INDUSTRY_EQUIPMENT
    ADD COLUMN `API_DEVICE` VARCHAR(64);
ALTER TABLE dmmp.MP_USER
    ADD COLUMN `NATIONALITY` VARCHAR(255);
ALTER TABLE dmmp.MP_USER
    ADD COLUMN `EXTERNAL_SIGN` INT(2) DEFAULT 0;
ALTER TABLE douson.MP_MATERIAL
    ADD COLUMN `CIRCULATED_DOCUMENT` INT(2) DEFAULT 0 NOT NULL COMMENT '是否流转文档 0:否 1:是';
ALTER TABLE douson.MP_INDUSTRY_DEVICE
    ADD COLUMN MANAGER VARCHAR(255);
ALTER TABLE douson.MP_PLAN
    MODIFY COLUMN `SOLVE_SCHEME` VARCHAR(4096);
CREATE TABLE douson.MP_DRESS
(
    WORK_DRESS_TYPE        VARCHAR(64) COMMENT '工装类型',
    MATERIAL_NO            VARCHAR(64) COMMENT '物料号',
    REMARK                 VARCHAR(512) COMMENT '描述',
    DESIGN_NUMBER          VARCHAR(128) COMMENT '图号',
    APPLY_COUNT            DECIMAL(19, 2) COMMENT '申请数量',
    APPLY_DATE             VARCHAR(32) COMMENT '申请日期',
    STORE_POSITION         VARCHAR(128) COMMENT '库存位置',
    CHECK_ACCEPT_USER      VARCHAR(128) COMMENT '验收',
    DESCRIPTION_OF_ORDER   VARCHAR(512) COMMENT '备注（注明为哪个订单服务）',
    STORE_COUNT            DECIMAL(19, 2) COMMENT '入库数量',
    STORE_DATE_DESCRIPTION VARCHAR(512) COMMENT '入库日期备注',
    CREATOR                VARCHAR(64)   NULL COMMENT '创建人用户ID',
    MODIFIER               VARCHAR(64)   NULL COMMENT '修改人用户ID',
    STATE                  INT DEFAULT 0 NULL COMMENT '状态，0-正常 1-删除',
    CREATE_TIME            DATETIME      NULL COMMENT '创建时间',
    MODIFY_TIME            DATETIME      NULL COMMENT '修改时间',
    ID                     VARCHAR(64)   NOT NULL COMMENT '唯一标志',
    PRIMARY KEY (ID)
) COMMENT = '工装管理';

CREATE TABLE douson.MP_DRESS_ATTACHMENT
(
    DRESS_ID            VARCHAR(64)   NULL COMMENT '工装管理ID',
    ATTACHMENT_CATEGORY VARCHAR(64)   NULL COMMENT '附件分类',
    ATTACHMENT_TYPE     VARCHAR(32)   NULL COMMENT '附件类型',
    URL                 VARCHAR(256)  NULL COMMENT '链接',
    FILE_ID             VARCHAR(256)  NULL COMMENT '文件ID',
    FILENAME            VARCHAR(256)  NULL COMMENT '文件名',
    COMPRESS_URL        VARCHAR(256)  NULL COMMENT '压缩链接',
    CREATOR             VARCHAR(64)   NULL COMMENT '创建人用户ID',
    MODIFIER            VARCHAR(64)   NULL COMMENT '修改人用户ID',
    STATE               INT DEFAULT 0 NULL COMMENT '状态，0-正常 1-删除',
    CREATE_TIME         DATETIME      NULL COMMENT '创建时间',
    MODIFY_TIME         DATETIME      NULL COMMENT '修改时间',
    ID                  VARCHAR(64)   NOT NULL COMMENT '唯一标志'
        PRIMARY KEY,
    CONSTRAINT UNQ_15110_IMPROVEATTACHMENT_0
        UNIQUE (DRESS_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL, FILE_ID)
)
    COMMENT '工装管理附件';

# DROP TABLE douson.MP_SCORE;
CREATE TABLE douson.MP_SCORE
(
    USER_ID             VARCHAR(64)     NULL COMMENT '用户ID',
    QUARTER             VARCHAR(16)     NULL COMMENT '季度',
    DEVICE_NUMBER       VARCHAR(64)     NULL COMMENT '设备编号',
    QUALITY_SCORE       DECIMAL(38, 10) NULL COMMENT '质量 50分',
    ATTENDANCE_SCORE    DECIMAL(38, 10) NULL COMMENT '全勤/团结  10分',
    SAFETY_SCORE        DECIMAL(38, 10) NULL COMMENT '环境安全和工艺 20分',
    MONTHLY_PERFORMANCE DECIMAL(38, 10) NULL COMMENT '月度绩效 (20分)',
    TOTAL_WORK_DAYS     INT             NULL COMMENT '总上班日数',
    TOTAL               DECIMAL(38, 10) NULL COMMENT '总计',
    EVALUATION_MONTHS   INT             NULL COMMENT '评估月数',
    EVALUATION_RESULT   VARCHAR(255)    NULL COMMENT '评比结果',
    QUARTERLY_BONUS     DECIMAL(38, 10) NULL COMMENT '季度奖金',
    DESCRIPTION         VARCHAR(512)    NULL COMMENT '备注',
    LEADER_USER_ID      VARCHAR(64)     NULL COMMENT '当班主管',
    SORTER              int default 0   null comment '排序',
    CREATOR             varchar(64)     null comment '创建人用户id',
    MODIFIER            varchar(64)     null comment '修改人用户id',
    REMARK              varchar(512)    null comment '备注',
    CREATED_TIME        datetime        not null comment '创建时间',
    LAST_MODIFIED_TIME  datetime        not null comment '修改时间',
    DELETED_FLAG        int             null comment '删除标志',
    ID                  VARCHAR(64)     NOT NULL COMMENT '唯一标志',
    PRIMARY KEY (ID)
) COMMENT '季度评比报告';

# DROP TABLE douson.MP_SCORE_ATTACHMENT;
CREATE TABLE douson.MP_SCORE_ATTACHMENT
(
    SCORE_ID            VARCHAR(64)  NOT NULL COMMENT '季度评比报告ID',
    ATTACHMENT_CATEGORY VARCHAR(64)  NULL COMMENT '附件分类，photo-阀体照片',
    ATTACHMENT_TYPE     VARCHAR(4)   NULL COMMENT '附件类型，0：照片；1-视频',
    URL                 VARCHAR(256) NULL COMMENT '链接',
    FILE_ID             VARCHAR(64)  NULL COMMENT '文件ID',
    FILENAME            VARCHAR(255) NULL COMMENT '文件名',
    COMPRESS_URL        TEXT         NULL COMMENT '压缩链接',
    ID                  VARCHAR(64)  NOT NULL COMMENT '唯一标志',
    PRIMARY KEY (ID)
) COMMENT '季度评比报告附件';
create unique index UNQ_15110_SA_0
    on douson.MP_SCORE_ATTACHMENT (SCORE_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL, FILE_ID);
CREATE TABLE MP_VISITOR
(
    `APPLY_DATE`           VARCHAR(32) COMMENT '申请日期',
    `APPLICANT`            VARCHAR(64) COMMENT '申请人',
    `VISITOR_NAME`         VARCHAR(128) COMMENT '访客姓名',
    `PHONE_NUMBER`         VARCHAR(32) COMMENT '手机号',
    `COMPANY_NAME`         VARCHAR(256) COMMENT '公司名称',
    `VISIT_CONTENT`        VARCHAR(512) COMMENT '来访内容',
    `EXPECTED_VISIT_TIME`  VARCHAR(32) COMMENT '预计来访时间',
    `EXPECTED_END_TIME`    VARCHAR(32) COMMENT '预计访问结束时间',
    `CONTACT_PERSON`       VARCHAR(64) COMMENT '联系人',
    `VISIT_DEPARTMENT`     VARCHAR(256) COMMENT '来访部门',
    `APPROVER`             VARCHAR(64) COMMENT '批准人',
    `ID_AND_PHOTOS`        DECIMAL(38, 10) COMMENT '证件及本人照片（5张）',
    `REMARKS`              VARCHAR(512) COMMENT '备注',
    `FACTORY_RECORDS`      DECIMAL(38, 10) COMMENT '出厂记录（5张）',
    `VISITOR_FACTORY_DATE` VARCHAR(32) COMMENT '访客出厂日期',
    `PRINT_NUMBER`         VARCHAR(64) COMMENT '打印单号',
    `VALID`                BIT(1) COMMENT 'null',
    `CREATOR`              VARCHAR(64) COMMENT '创建人用户id',
    `MODIFIER`             VARCHAR(64) COMMENT '修改人用户id',
    `STATE`                INT(2) DEFAULT 0 COMMENT '状态，0-正常 1-删除',
    `CREATE_TIME`          DATETIME COMMENT '创建时间',
    `MODIFY_TIME`          DATETIME COMMENT '修改时间',
    `ID`                   VARCHAR(64) COMMENT '唯一标志',
    CONSTRAINT PK_15110_Visitor_0 PRIMARY KEY (ID)
);

ALTER TABLE MP_VISITOR
    COMMENT '访客';
CREATE TABLE douson.MP_VISITOR_ATTACHMENT
(
    VISITOR_ID          VARCHAR(64)   NULL COMMENT '访客ID',
    ATTACHMENT_CATEGORY VARCHAR(64)   NULL COMMENT '附件分类',
    ATTACHMENT_TYPE     VARCHAR(32)   NULL COMMENT '附件类型',
    URL                 VARCHAR(256)  NULL COMMENT '链接',
    FILE_ID             VARCHAR(256)  NULL COMMENT '文件ID',
    FILENAME            VARCHAR(256)  NULL COMMENT '文件名',
    COMPRESS_URL        VARCHAR(256)  NULL COMMENT '压缩链接',
    CREATOR             VARCHAR(64)   NULL COMMENT '创建人用户ID',
    MODIFIER            VARCHAR(64)   NULL COMMENT '修改人用户ID',
    STATE               INT DEFAULT 0 NULL COMMENT '状态，0-正常 1-删除',
    CREATE_TIME         DATETIME      NULL COMMENT '创建时间',
    MODIFY_TIME         DATETIME      NULL COMMENT '修改时间',
    ID                  VARCHAR(64)   NOT NULL COMMENT '唯一标志'
        PRIMARY KEY,
    CONSTRAINT UNQ_15110_VISITOR_0
        UNIQUE (VISITOR_ID, ATTACHMENT_CATEGORY, ATTACHMENT_TYPE, URL, FILE_ID)
)
    COMMENT '访客附件';

ALTER TABLE DOUSON.MP_INDUSTRY_PARAM
    ADD `EXPAND_FIRST` VARCHAR(256) DEFAULT NULL COMMENT '扩展内容1';

CREATE TABLE MP_QUOTATION
(
    `CUSTOMER`            VARCHAR(64) COMMENT '客户',
    `DESIGN_NUMBER`       VARCHAR(64) COMMENT '图号',
    `DESIGN_NUMBER_COUNT` DECIMAL(38, 10) COMMENT 'null',
    `NAME`                VARCHAR(128) COMMENT '名称',
    `MATERIAL_QUALITY`    VARCHAR(32) COMMENT '材质',
    `COUNT`               VARCHAR(128) COMMENT '数量',
    `QUOTATION_DATE`      VARCHAR(32) COMMENT '报价日期',
    `PROCESS_PROCEDURE`   VARCHAR(32) COMMENT '加工工序',
    `PROCESS_DEVICE`      VARCHAR(32) COMMENT '加工设备',
    `PROCESS_UNIT_PRICE`  DECIMAL(38, 10) COMMENT '加工单价',
    `PROCESS_TIME`        VARCHAR(32) COMMENT '加工时间',
    `SUMMARY_PRICE`       DECIMAL(38, 10) COMMENT '汇总价格/元',
    `REMARKS`             VARCHAR(512) COMMENT '备注',
    `BIDDER`              VARCHAR(64) COMMENT '报价人',
    `ACCEPT_ORDER`        BIT(1) COMMENT '是否成功接单',
    `CREATOR`             VARCHAR(64) COMMENT '创建人用户id',
    `MODIFIER`            VARCHAR(64) COMMENT '修改人用户id',
    `STATE`               INT(2) DEFAULT 0 COMMENT '状态，0-正常 1-删除',
    `CREATE_TIME`         DATETIME COMMENT '创建时间',
    `MODIFY_TIME`         DATETIME COMMENT '修改时间',
    `ID`                  VARCHAR(64) COMMENT '唯一标志',
    CONSTRAINT PK_15110_Quotation_0 PRIMARY KEY (ID)
);

ALTER TABLE MP_QUOTATION
    COMMENT '加工报价';

CREATE TABLE MP_QUOTATION_ATTACHMENT
(
    `QUOTATION_ID`        VARCHAR(64) COMMENT '加工报价id',
    `ATTACHMENT_CATEGORY` VARCHAR(64) COMMENT '附件分类',
    `ATTACHMENT_TYPE`     VARCHAR(32) COMMENT '附件类型',
    `URL`                 VARCHAR(256) COMMENT '链接',
    `FILE_ID`             VARCHAR(256) COMMENT '文件ID',
    `FILENAME`            VARCHAR(256) COMMENT '文件名',
    `COMPRESS_URL`        VARCHAR(256) COMMENT '压缩链接',
    `CREATOR`             VARCHAR(64) COMMENT '创建人用户id',
    `MODIFIER`            VARCHAR(64) COMMENT '修改人用户id',
    `STATE`               INT(2) DEFAULT 0 COMMENT '状态，0-正常 1-删除',
    `CREATE_TIME`         DATETIME COMMENT '创建时间',
    `MODIFY_TIME`         DATETIME COMMENT '修改时间',
    `ID`                  VARCHAR(64) COMMENT '唯一标志',
    CONSTRAINT PK_15110_QuotationAttachment_0 PRIMARY KEY (ID)
);
ALTER TABLE MP_QUOTATION_ATTACHMENT
    COMMENT '加工报价附件';
CREATE INDEX UNQ_15110_QuotationAttachment729_0 ON MP_QUOTATION_ATTACHMENT (QUOTATION_ID, FILE_ID);
CREATE TABLE MP_QUOTATION_ITEM
(
    `QUOTATION_ID`       VARCHAR(256) COMMENT 'null',
    `PROCESS_PROCEDURE`  VARCHAR(32) COMMENT '加工工序',
    `PROCESS_DEVICE`     VARCHAR(32) COMMENT '加工设备',
    `PROCESS_UNIT_PRICE` DECIMAL(38, 10) COMMENT '加工单价',
    `PROCESS_TIME`       DECIMAL(38, 10) COMMENT '加工时间',
    `SUMMARY_PRICE`      DECIMAL(38, 10) COMMENT '汇总价格/元',
    `REMARKS`            VARCHAR(512) COMMENT '备注',
    `CREATOR`            VARCHAR(64) COMMENT '创建人用户id',
    `MODIFIER`           VARCHAR(64) COMMENT '修改人用户id',
    `STATE`              INT(2) DEFAULT 0 COMMENT '状态，0-正常 1-删除',
    `CREATE_TIME`        DATETIME COMMENT '创建时间',
    `MODIFY_TIME`        DATETIME COMMENT '修改时间',
    `ID`                 VARCHAR(64) COMMENT '唯一标志',
    CONSTRAINT PK_15110_QuotationItem_0 PRIMARY KEY (ID)
);
ALTER TABLE MP_QUOTATION_ITEM
    COMMENT '加工报价明细';