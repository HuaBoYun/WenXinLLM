-- ============================================================================
-- AI 数据治理 - 元数据存储建表脚本（达梦 DM8）
-- 位置：hbyunBigModelwen/src/main/resources/db/gov_meta_ddl.sql
-- 配套代码：GovernanceMetaController（/v1/ai/governance/meta/*）
--
-- 【重要】为什么与旧版不同：
--   旧版把主题域列命名为 DOMAIN —— DOMAIN 是达梦数据库的保留字，
--   建表/增删改查都会在 [DOMAIN] 附近报「语法分析出错」，
--   且服务启动时 @PostConstruct 自动建表第一条失败即中断，导致三张表全都没有创建。
--   本版统一改名为 DOMAIN_NAME，代码（GovernanceMetaController）已同步修改，
--   前端入参/出参字段名（domain / domainStats）不受影响。
--
-- 【执行说明】
--   1. 直接在达梦客户端整脚本执行一次即可；均为新建表，可重复执行前先手工 DROP。
--   2. 若此前手工建过带 DOMAIN 列的旧表，请先执行下面的 DROP 再建。
--   3. MySQL 环境执行时：CLOB 改 LONGTEXT，其余不变。
-- ============================================================================

-- 如需重建旧表，先取消注释执行：
-- DROP TABLE GOV_CONTEXT;
-- DROP TABLE GOV_TABLE_META;
-- DROP TABLE GOV_DATA_SOURCE;

-- ----------------------------------------------------------------------------
-- 1. GOV_DATA_SOURCE 数据源连接配置（数据中台「连接数据源」保存的连接）
-- ----------------------------------------------------------------------------
CREATE TABLE GOV_DATA_SOURCE (
  ID             VARCHAR(64)   NOT NULL,
  USER_ID        VARCHAR(64),
  NAME           VARCHAR(200),
  DB_TYPE        VARCHAR(20),
  HOST           VARCHAR(200),
  PORT           INT,
  DB_NAME        VARCHAR(200),
  USERNAME       VARCHAR(200),
  PASSWORD_B64   VARCHAR(800),
  DOMAIN_NAME    VARCHAR(100),
  TABLE_COUNT    INT           DEFAULT 0,
  DOMAIN_STATS   CLOB,
  LAST_SCAN_TIME VARCHAR(40),
  CREATE_TIME    VARCHAR(40),
  UPDATE_TIME    VARCHAR(40),
  CONSTRAINT PK_GOV_DATA_SOURCE PRIMARY KEY (ID)
);

COMMENT ON TABLE GOV_DATA_SOURCE IS 'AI数据治理-数据源连接配置（密码Base64存储，生产建议替换为AES）';
COMMENT ON COLUMN GOV_DATA_SOURCE.ID IS '主键（ds_前缀）';
COMMENT ON COLUMN GOV_DATA_SOURCE.USER_ID IS '所属用户ID（staffid）';
COMMENT ON COLUMN GOV_DATA_SOURCE.NAME IS '数据源名称';
COMMENT ON COLUMN GOV_DATA_SOURCE.DB_TYPE IS '数据库类型 mysql/dm 等';
COMMENT ON COLUMN GOV_DATA_SOURCE.HOST IS '主机地址';
COMMENT ON COLUMN GOV_DATA_SOURCE.PORT IS '端口';
COMMENT ON COLUMN GOV_DATA_SOURCE.DB_NAME IS '库名（达梦为用户名大写）';
COMMENT ON COLUMN GOV_DATA_SOURCE.USERNAME IS '数据库用户名';
COMMENT ON COLUMN GOV_DATA_SOURCE.PASSWORD_B64 IS '密码（Base64）';
COMMENT ON COLUMN GOV_DATA_SOURCE.DOMAIN_NAME IS '主主题域';
COMMENT ON COLUMN GOV_DATA_SOURCE.TABLE_COUNT IS '上次采集的表数量';
COMMENT ON COLUMN GOV_DATA_SOURCE.DOMAIN_STATS IS '主题域分布统计 JSON';
COMMENT ON COLUMN GOV_DATA_SOURCE.LAST_SCAN_TIME IS '上次元数据采集时间';
COMMENT ON COLUMN GOV_DATA_SOURCE.CREATE_TIME IS '创建时间';
COMMENT ON COLUMN GOV_DATA_SOURCE.UPDATE_TIME IS '更新时间';

-- ----------------------------------------------------------------------------
-- 2. GOV_TABLE_META 表元数据（元数据采集任务固化：表 + 字段 JSON）
--    SUB_DOMAIN / BIZ_OBJECT：AI 资产分层产出（L2 业务子主题域 / L3 业务对象）
-- ----------------------------------------------------------------------------
CREATE TABLE GOV_TABLE_META (
  SOURCE_ID     VARCHAR(64)  NOT NULL,
  TABLE_NAME    VARCHAR(200) NOT NULL,
  TABLE_COMMENT VARCHAR(1000),
  DOMAIN_NAME   VARCHAR(100),
  SUB_DOMAIN    VARCHAR(100),
  BIZ_OBJECT    VARCHAR(100),
  TABLE_ROWS    BIGINT       DEFAULT 0,
  COLUMN_COUNT  INT          DEFAULT 0,
  COLUMNS_JSON  CLOB,
  UPDATE_TIME   VARCHAR(40),
  CONSTRAINT PK_GOV_TABLE_META PRIMARY KEY (SOURCE_ID, TABLE_NAME)
);

COMMENT ON TABLE GOV_TABLE_META IS 'AI数据治理-表元数据（万表规模分页查询/目录分级底座）';
COMMENT ON COLUMN GOV_TABLE_META.SOURCE_ID IS '数据源ID（GOV_DATA_SOURCE.ID）';
COMMENT ON COLUMN GOV_TABLE_META.TABLE_NAME IS '物理表名';
COMMENT ON COLUMN GOV_TABLE_META.TABLE_COMMENT IS '表注释';
COMMENT ON COLUMN GOV_TABLE_META.DOMAIN_NAME IS 'L1 业务域';
COMMENT ON COLUMN GOV_TABLE_META.SUB_DOMAIN IS 'L2 业务子主题域';
COMMENT ON COLUMN GOV_TABLE_META.BIZ_OBJECT IS 'L3 业务对象';
COMMENT ON COLUMN GOV_TABLE_META.TABLE_ROWS IS '行数估算';
COMMENT ON COLUMN GOV_TABLE_META.COLUMN_COUNT IS '字段数量（L5 业务属性数）';
COMMENT ON COLUMN GOV_TABLE_META.COLUMNS_JSON IS '字段明细 JSON 数组（name/type/nullable/comment）';
COMMENT ON COLUMN GOV_TABLE_META.UPDATE_TIME IS '采集时间';

-- 存量库补列（服务启动时也会自动尝试，失败可手工执行）：
-- ALTER TABLE GOV_TABLE_META ADD SUB_DOMAIN VARCHAR(100);
-- ALTER TABLE GOV_TABLE_META ADD BIZ_OBJECT VARCHAR(100);

-- ----------------------------------------------------------------------------
-- 3. GOV_CONTEXT 治理上下文（前端 govCtx 整体固化，替代 localStorage 单端存储）
-- ----------------------------------------------------------------------------
CREATE TABLE GOV_CONTEXT (
  USER_ID     VARCHAR(64) NOT NULL,
  CTX_JSON    CLOB,
  UPDATE_TIME VARCHAR(40),
  CONSTRAINT PK_GOV_CONTEXT PRIMARY KEY (USER_ID)
);

COMMENT ON TABLE GOV_CONTEXT IS 'AI数据治理-治理上下文（按用户整体 upsert）';
COMMENT ON COLUMN GOV_CONTEXT.USER_ID IS '用户ID（staffid），主键';
COMMENT ON COLUMN GOV_CONTEXT.CTX_JSON IS '治理上下文 JSON（步骤进度/工件/数据源/aiCatalog 等）';
COMMENT ON COLUMN GOV_CONTEXT.UPDATE_TIME IS '更新时间';
