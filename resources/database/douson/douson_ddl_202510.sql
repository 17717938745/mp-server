alter table douson.MP_INDUSTRY_PARAM
    ADD `PARENT_PARAM_CODE` VARCHAR(128) comment '上级参数编码';
INSERT INTO douson.MP_INDUSTRY_PARAM (PARAM_CATEGORY_ID, PARAM_CODE, PARAM_NAME, SORTER)
VALUES ('commonConfig', 'homePage', 'http://www.douson.cn/', 1);
INSERT INTO douson.MP_INDUSTRY_PARAM (PARAM_CATEGORY_ID, PARAM_CODE, PARAM_NAME, SORTER)
VALUES ('commonConfig', 'adminPage', 'http://192.168.0.21:8081/#/report/dashboard/dashboard2', 2);
INSERT INTO douson.MP_INDUSTRY_PARAM (PARAM_CATEGORY_ID, PARAM_CODE, PARAM_NAME, SORTER)
VALUES ('commonConfig', 'communityName', '阳澄湖道森心声社区', 3);
INSERT INTO douson.MP_INDUSTRY_PARAM (PARAM_CATEGORY_ID, PARAM_CODE, PARAM_NAME, SORTER)
    (SELECT 'organizationalStructure', PARAM_CODE, PARAM_NAME, SORTER
     FROM douson.MP_INDUSTRY_PARAM
     WHERE PARAM_CATEGORY_ID = 'department');
