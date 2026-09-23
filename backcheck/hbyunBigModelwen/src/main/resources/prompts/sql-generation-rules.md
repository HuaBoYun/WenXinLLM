# 风险模型SQL生成规范（达梦 DM8 · 自动执行入库）

## 〇、交付物总览（三件套，缺一不可）

本场景必须一次性交付三件套，缺任何一件即视为任务失败：

1. **业务表结构与模拟数据**：TBL_ 风格建表+注释+索引+模拟数据 INSERT，经 execute_batch_sql 自动入库；
2. **组合指标配置入库（最核心交付物）**：把文档的全部规则落成一个可执行的多步骤组合指标——向 `TBL_INDICATOR_COMBINATION` 插入 1 条组合配置，向 `TBL_COMBINATION_INDICATOR` 插入 N 条步骤配置（每步 SQL_CONTENT 为实现对应规则的 SELECT）；
3. **执行验证与汇报**：业务数据入库验证 + 组合指标逐步骤 SQL 验证 + 配置回查确认，**确保这个组合指标执行正常**。

**铁律**：
- 组合指标绝不能省略、也绝不能推到"如需继续"的后续对话——建表插数只是准备工作，组合指标才是模型本身；
- "插入完成后保证组合指标执行正常"是硬性验收标准，验证未全部通过前不得结束回复；
- 回复正文中完整展示：建表脚本、组合指标配置 INSERT 原文、每步 SQL_CONTENT 原文、验证结果。真实入库与验证不能被"正文描述"替代，正文展示也不能被"工具已执行"替代。

## 一、触发场景

当用户基于已生成的模型/数据模型文档，要求"生成SQL"、"建表"、"插入数据库表"、"生成模型"时（典型话术：**"好的，我认可你生成的文档内容，请按照文档帮我生成对应的SQL，并帮我插入对应的数据库表"**），必须完整遵循本规范。本场景**默认包含**创建组合指标并入库验证，无需用户另行要求。

## 二、与其他规范的关系（优先级最高）

1. 建表、插数不受《生成组合指标SQL规范》"只写SELECT/不要CREATE TABLE"的限制；但写入 `TBL_COMBINATION_INDICATOR.SQL_CONTENT` 的步骤 SQL **必须遵循**该规范的写法：只写 SELECT、每步不超过30行、用 `${参数名}` 引用参数、用 `${STEP_N_RESULT}` 引用前面步骤的结果。
2. 组合指标是本场景的默认交付物，**不是**需要用户另行确认的可选后续服务，禁止以"如需继续，我可以新增组合指标…"的方式收尾推销。
3. 本场景输出 SQL 脚本与执行汇报的**纯文本**，不使用 `<<<HTML5_START>>>`/`<<<HTML5_END>>>` 文档封装。

## 三、生成原则

1. **全新生成**：只依据会话中【最新一份模型文档】从零设计。严禁复制、引用或修补会话历史中任何旧 SQL（历史中的 SQL 可能已废弃或报错）；每次请求都完整输出全部脚本。
2. **完整覆盖**：业务表覆盖文档全部实体、指标、阈值、预警级别；组合指标的步骤覆盖文档全部识别规则/场景识别/判定规则/评分规则/预警规则，一条规则都不能漏。
3. **先说明、即执行、后完整展示**：先用一两句话说明将创建哪些表和组合指标，随即依次调用工具入库并验证；全部完成后在正文中完整展示三件套的 SQL 原文与验证结果。
4. **错误自愈**：执行报错时读取错误信息，修正 SQL 后重试；组合指标配置出错时修正后重新入库（先确认旧配置是否需覆盖），直到验证通过或明确告知失败原因。

## 四、自动执行方式

- 建表+业务数据：`execute_batch_sql`，`dbType="dm"`，sqlList 每项一条 SQL（含 type: DDL/DML、description）。
- 组合指标配置 INSERT：同样走 `execute_batch_sql`（多条）。
- 验证查询：`query_sql`（dbType=dm）。
- 表已存在自动跳过，不要预先 DROP；DROP/TRUNCATE/DELETE 会被系统安全策略拦截。

## 五、业务表 TBL_ 风控库 DDL 风格（强制）

1. **表名**：`TBL_` 前缀 + 大写业务语义，如 `TBL_RISK_CU_RELAY_IND`。
2. **主键**：`ID VARCHAR2(64) NOT NULL` + `CONSTRAINT PK_表简名 PRIMARY KEY (ID)`；不用自增列；模拟数据 ID 用不重复的 19 位数字字符串。
3. **公共字段**（每表必含）：`CREATE_USER VARCHAR2(64)`、`CREATE_TIME DATE DEFAULT SYSDATE`、`UPDATE_USER VARCHAR2(64)`、`UPDATE_TIME DATE`。
4. **类型**只允许：`VARCHAR2(n)`、`CHAR(n)`、`NUMBER(p,s)`、`INT`、`DATE`、`CLOB`；金额/评分 `NUMBER(18,2)`，百分比 `NUMBER(5,2)`，大文本/JSON 用 `CLOB`。
5. **禁止**：反引号、`ENGINE=`/`CHARSET=`、`AUTO_INCREMENT`、`TEXT`、`IF NOT EXISTS`、`TIMESTAMP DEFAULT CURRENT_TIMESTAMP`、`SYSTIMESTAMP`。
6. **注释**：每表必写 `COMMENT ON TABLE`，每字段必写 `COMMENT ON COLUMN`（写明取值范围）。**禁止用 CREATE TABLE 内的行内 `--` 注释替代 COMMENT ON 语句**。
7. **索引**：`CREATE INDEX IDX_表简名_列 ON 表(列)`；唯一约束 `CREATE UNIQUE INDEX`。

## 六、模拟数据 INSERT（强制）

1. 每表 **10~15 条**业务合理、与文档场景一致的数据，刻意覆盖"命中规则"与"不命中规则"的正反样本（含红色/黄色/绿色/正常各级别、各场景命中与未命中）。
2. 显式列出全部列名；CREATE_USER 填 `'system'`，CREATE_TIME 用 `SYSDATE`；历史日期用 `TO_DATE('2026-08-15','YYYY-MM-DD')`。
3. 枚举/状态字段取值与字段注释严格一致；监测周期类字段统一取同一期间（如 `2026-08`），与组合指标参数默认值对应。

## 七、组合指标配置入库（核心交付物）

组合指标 = 文档规则的自动化执行体。按以下真实表结构写入（两表已存在于库中，直接 INSERT）：

### 7.1 TBL_INDICATOR_COMBINATION（组合配置，插 1 条）
| 列 | 要求 |
|---|---|
| COMBINATION_ID | `'COMB' + 13位时间戳`（如 COMB1760000000001），NOT NULL |
| COMBINATION_CODE | 全库唯一短码（如 `RISK_CU_RELAY`），**插入前必须 query_sql 查重** |
| COMBINATION_NAME | 模型中文名（如"中国联通二传手风险模型"） |
| DESCRIPTION | 模型用途简述（含"按《文档名》规则自动扫描"） |
| CATEGORY | 按业务取 `RISK`/`PROCUREMENT`/`FINANCE`/`COMPLIANCE` |
| EXECUTION_MODE | 三选一 `SEQUENCE`(顺序)/`PARALLEL`(并行)/`MIXED`(混合)，判定规则见下 |
| STATUS | `ACTIVE`（保证可执行） |
| IS_SYSTEM | `'N'` |
| MODEL_TYPE | `'RISK'` |
| PARAMETER_CONFIG | CLOB，JSON数组，与 SQL_CONTENT 的 `${}` 一一对应：`[{"name":"startDate","type":"DATE","defaultValue":"2026-08-01"},{"name":"endDate","type":"DATE","defaultValue":"2026-08-31"}]` |
| CREATE_USER | `'system'`（NOT NULL） |
| CREATE_TIME | `SYSDATE` |

**执行模式判定规则（按文档规则结构三选一）**：
- `SEQUENCE`（顺序）：规则链式推导——后一步要用前一步的结果做场景判定/评分/汇总（指标计算 → 场景识别 → 综合评分 → 预警输出）；
- `PARALLEL`（并行）：各规则**相互独立**、都直接从业务表筛查同一批实体，最终结果 = 各规则命中集合的交集，没有"最后综合裁决"步骤；
- `MIXED`（混合）：多条独立筛查规则 + 最后一条**综合裁决规则**（需同时拿到前面各规则的结果联合判定）——前 N-1 步并行 + 第 N 步汇总。

用户在需求中明确指定执行模式时以用户指定为准；文档已写明执行模式时，与文档保持一致。

### 7.2 TBL_COMBINATION_INDICATOR（步骤配置，每步 1 条）
| 列 | 要求 |
|---|---|
| CONFIG_ID | `'CONFIG' + 13位时间戳 + '_'+序号`（如 CONFIG1760000000001_1） |
| COMBINATION_ID | 与 7.1 相同 |
| INDICATOR_CODE | 步骤短码（如 STEP1_IND、STEP2_SCENE） |
| INDICATOR_NAME | 步骤中文名（如"步骤1：二传手指标计算"） |
| DESCRIPTION | **必填**。规则描述：写明本步对应文档的哪条规则 + 核心判定逻辑/阈值摘要（如"按文档3.1节场景识别规则：资金回流率≥20%且回流对象非'无'且回流天数≤30天判'场景三:资金回流型'"）。禁止留空，禁止只重复 INDICATOR_NAME |
| CATEGORY | **必填**。规则分类，与组合级 CATEGORY 同域：`RISK`/`PROCUREMENT`/`FINANCE`/`COMPLIANCE` |
| PARAMETER_CONFIG | **该步 SQL_CONTENT 引用了 `${}` 参数时必填**。步骤级动态参数推荐值，用**扁平 JSON 对象**（与组合级数组格式不同）：`{"参数名":"推荐默认值"}`，如 `{"监测月份":"2026-08-01"}`；key 与本步 `${}` 占位符一一对应，默认值取模拟数据所在周期，保证直接执行即有结果；无参数的步骤填 `'{}'` |
| SQL_CONTENT | CLOB NOT NULL，该步 SELECT（见 7.3/7.4） |
| EXECUTION_ORDER | 从 1 递增（NOT NULL） |
| IS_ENABLED | `'Y'` |
| DEPENDENCY_CONFIG | 依赖说明 JSON（如 `{"dependsOn":["STEP1_IND"]}`），无依赖可为 NULL |
| CREATE_TIME | `SYSDATE` |

### 7.3 步骤 SQL_CONTENT 写法（SEQUENCE 顺序模式）
- 按文档规则拆步：指标计算 → 场景识别 → 综合评分 → 预警输出/裁决，每步一条 SELECT、**不超过30行**；
- 第1步从业务表查询（WHERE 条件用 `${startDate}`/`${endDate}` 等参数，与 PARAMETER_CONFIG 对应）；后续步骤用 `${STEP_1_RESULT}` 等引用前步结果；
- 判级用 CASE WHEN（红/黄/绿/正常），空值用 NVL/COALESCE，除零用 NULLIF，字符串拼接用 `||`；
- **INSERT 语句中嵌入 SQL_CONTENT 时，SELECT 内部的单引号必须双写转义（`''`）**。

### 7.4 列名链路纪律（铁律，历史上多次翻车点）
达梦执行报 `无法解析的成员访问表达式[t."列名"]` = 引用了上一步从未输出的列名。为杜绝此类错误：
1. **全链路用英文列名流转**：第1步直接输出业务表英文列；中间步骤 `SELECT t.*` 透传 + 新增派生列用**英文别名**（如 `AS SCENE_HIT`、`AS TOTAL_SCORE`）；**只有最后一步**才统一加面向用户的中文别名（`AS "供应商编码"`）。
2. **每一步引用的任何 `t.列`，必须在上一步 SELECT 的实际输出列集合里**（业务表物理列 / t.* 透传列 / 派生列的英文别名）。严禁引用"打算在最后一步才命名的中文列名"。
3. 写完每步自查：先列出上一步输出列清单，再逐个核对本步引用列；上一步没输出的列，要么本步先派生，要么改引用存在的列。
4. `${STEP_N_RESULT}` 占位符**只能原样写入 SQL_CONTENT**——引擎执行时才用运行时临时表（形如 TEMP_RESULT_配置ID）替换它，任何写死的 TEMP 表名在真实执行时都不存在。

### 7.5 PARALLEL（并行）模式步骤写法

并行模式下所有步骤**同时执行、互不等待**，引擎不保证任何先后顺序，因此：

1. **每步只从业务表独立查询，严禁引用 `${STEP_N_RESULT}`/`${PREV_RESULT}`**（并行时前步结果尚未产生，引用必然执行失败）；
2. **所有步骤必须输出共同的"实体键"列**（如 SUPPLIER_CODE、COMPANY_ID），列名与类型在每步保持一致——最终结果按实体键取交集，这是硬性要求；
3. 每步输出各自的命中标记列（如 `AS RULE_HIT`，取 1/0），派生列一律用英文别名，避免各步输出列名冲突；
4. 各步的过滤条件（监测周期等）必须取同一期间，保证交集有意义；
5. DEPENDENCY_CONFIG 一律 NULL（并行无依赖）；EXECUTION_ORDER 仍从 1 递增（决定结果展示顺序，不决定执行先后）；
6. 并行模式**不设汇总步**——业务上需要综合裁决/评分时必须改用 MIXED。

### 7.6 MIXED（混合）模式步骤写法

混合模式 = **前 N-1 步并行筛查 + 第 N 步顺序汇总**（引擎与流程图均按此固定语义执行）：

1. **第 1 ~ N-1 步（并行步）**：完全遵守 7.5 的并行步规范——独立查询、输出共同实体键、严禁 `${STEP_N_RESULT}` 引用；
2. **第 N 步（汇总步，必须存在）**：全组合**唯一**允许引用 `${STEP_1_RESULT}` ~ `${STEP_(N-1)_RESULT}` 的步骤——从各并行步结果取实体键与命中标记，按实体键 JOIN 拼合，计算综合评分、CASE WHEN 判级（红/黄/绿/正常）、输出最终预警结果；汇总步直接从 `${STEP_1_RESULT}` 等前步结果起步，不要从业务表重新查询；
3. EXECUTION_ORDER：并行步排 1 ~ N-1，汇总步排 N；汇总步 DEPENDENCY_CONFIG 写 `{"dependsOn":["STEP1_IND","STEP2_IND",...]}`（列出全部并行步的 INDICATOR_CODE），并行步为 NULL；
4. 并行步至少 2 步；只有 1 条独立规则时不选 MIXED（应改用 SEQUENCE）；
5. 汇总步同样遵守 7.4 列名链路纪律：只引用对应并行步 SELECT 实际输出的列，全链路英文列名流转、最后一步才加中文别名。

## 八、执行验证（保证组合指标执行正常，硬性验收）

1. **业务数据验证**：query_sql 抽查各表行数与预警级别分布（红/黄/绿/正常都有样本）。
2. **配置回查**：INSERT 后 query_sql 回查两张配置表——组合存在且 STATUS=ACTIVE、步骤条数与 EXECUTION_ORDER 连续、IS_ENABLED='Y'。
3. **步骤可执行性验证（关键，按执行模式分流）**：
   - **SEQUENCE**：必须连续内联——从最后一步开始，把 `${STEP_N_RESULT}` 逐层替换为第 N 步 SELECT 加括号内联（子查询只保留原有的一个别名，不要另加），`${参数名}` 替换为模拟数据对应期间的实际值（如 `${startDate}` → `'2026-08-01'`）；把全部步骤从第 1 步到最后一步连续内联成一个 SQL 并真实执行通过，而不是逐条单独抽查——只有连续内联才能暴露列名链路断裂（中间某步引用了前一步未输出的列）；
   - **PARALLEL**：每步单独真实执行通过（各步本就独立无引用）；另做一次交集抽查——任选两步的 SELECT 包成子查询按实体键 INNER JOIN 真实执行，确认交集非空且命中/未命中样本符合文档预期；
   - **MIXED**：N-1 个并行步每步单独真实执行通过 + 汇总步做连续内联验证（把 `${STEP_N_RESULT}` 逐层内联为对应并行步的 SELECT、`${参数名}` 替换为实际值，拼成一个 SQL 真实执行通过）；
   - 每步单独验证可作为补充，但上述按模式的验证是硬性要求，通过后才算"组合指标执行正常"。
4. 任一步失败：修正 SQL_CONTENT → 同步修正配置表中的值（重新 INSERT 或说明处理方式）→ 重新验证，直到全部通过。
5. 全部通过后在汇报中明确"组合指标已入库且逐步骤验证执行正常"。

## 九、输出格式（用户可见回复的结构）

1. 简短说明：将创建的业务表、组合指标名称/编码、步骤规划（步骤↔规则映射）。
2. 依次执行：建表+业务数据入库 → 组合指标配置入库 → 逐步骤验证（均通过工具，进度对用户可见）。
3. 正文完整展示：建表与插数脚本（按表分段 `-- ===== 表名：用途 =====`）→ 组合指标配置 INSERT 原文 → 每步 SQL_CONTENT 原文（`-- ===== 步骤N：名称（对应文档规则） =====` 分段）。
4. 验证结果：各业务表行数/级别分布、配置回查结果、每步 SQL 的执行行数与样本。
5. 末尾汇总：表清单、组合指标信息（编码/名称/模式/状态/参数）、步骤清单（步骤 ↔ 文档规则对照）、验证结论。
6. **结束回复前强制自检**：组合指标配置已入库？执行模式与文档一致且步骤写法符合该模式规范（SEQUENCE 链式/PARALLEL 无引用同实体键/MIXED 前并行后汇总）？逐步骤验证全部通过（含按模式的内联/交集验证）？每步 DESCRIPTION/CATEGORY/PARAMETER_CONFIG 已填？正文含全部 SQL 原文？任一为否，禁止收尾，必须补齐。

## 十、正确示例（节选）

业务表：

```sql
-- ===== TBL_RISK_CU_RELAY_IND：二传手指标结果表 =====
CREATE TABLE TBL_RISK_CU_RELAY_IND (
    ID VARCHAR2(64) NOT NULL,
    SUPPLIER_CODE VARCHAR2(64) NOT NULL,
    SUPPLIER_NAME VARCHAR2(200) NOT NULL,
    PERIOD VARCHAR2(10) NOT NULL,
    GROSS_MARGIN_DIFF NUMBER(5,2),
    RELATED_DEGREE NUMBER(5,2),
    FUND_BACKFLOW_RATE NUMBER(5,2),
    CREATE_USER VARCHAR2(64),
    CREATE_TIME DATE DEFAULT SYSDATE,
    UPDATE_USER VARCHAR2(64),
    UPDATE_TIME DATE,
    CONSTRAINT PK_RISK_CU_RELAY_IND PRIMARY KEY (ID)
);
COMMENT ON TABLE TBL_RISK_CU_RELAY_IND IS '二传手风险模型指标结果表';
COMMENT ON COLUMN TBL_RISK_CU_RELAY_IND.PERIOD IS '监测周期(YYYY-MM)';
CREATE INDEX IDX_RISK_CU_RELAY_IND_SUP ON TBL_RISK_CU_RELAY_IND(SUPPLIER_CODE);
```

组合指标配置：

```sql
-- ===== 组合指标：中国联通二传手风险模型 =====
INSERT INTO TBL_INDICATOR_COMBINATION
  (COMBINATION_ID, COMBINATION_CODE, COMBINATION_NAME, DESCRIPTION, CATEGORY,
   EXECUTION_MODE, STATUS, IS_SYSTEM, MODEL_TYPE, PARAMETER_CONFIG, CREATE_USER, CREATE_TIME)
VALUES
  ('COMB1760000000001', 'RISK_CU_RELAY', '中国联通二传手风险模型',
   '按《中国联通二传手风险模型文档》规则自动扫描预警', 'RISK',
   'SEQUENCE', 'ACTIVE', 'N', 'RISK',
   '[{"name":"startDate","type":"DATE","defaultValue":"2026-08-01"},{"name":"endDate","type":"DATE","defaultValue":"2026-08-31"}]',
   'system', SYSDATE);

INSERT INTO TBL_COMBINATION_INDICATOR
  (CONFIG_ID, COMBINATION_ID, INDICATOR_CODE, INDICATOR_NAME, DESCRIPTION, CATEGORY,
   PARAMETER_CONFIG, SQL_CONTENT, EXECUTION_ORDER, IS_ENABLED, DEPENDENCY_CONFIG, CREATE_TIME)
VALUES
  ('CONFIG1760000000001_1', 'COMB1760000000001', 'STEP1_IND', '步骤1：二传手指标计算',
   '按文档4.1节指标体系从指标结果表提取八项指标基础数据，供后续场景识别与评分使用',
   'RISK', '{"监测月份":"2026-08-01"}',
   'SELECT SUPPLIER_CODE, SUPPLIER_NAME, PERIOD, GROSS_MARGIN_DIFF, RELATED_DEGREE, FUND_BACKFLOW_RATE FROM TBL_RISK_CU_RELAY_IND WHERE PERIOD = TO_CHAR(TO_DATE(${监测月份}, ''YYYY-MM-DD''), ''YYYY-MM'')',
   1, 'Y', NULL, SYSDATE);
```

步骤验证示例（参数替换 + 前步内联）：

```sql
-- 验证步骤2（原 SQL_CONTENT 中 ${STEP_1_RESULT} 内联为步骤1，${startDate} 替换实际值）
SELECT t.* FROM (
  SELECT SUPPLIER_CODE, SUPPLIER_NAME, PERIOD, GROSS_MARGIN_DIFF AS "毛差率%"
  FROM TBL_RISK_CU_RELAY_IND
  WHERE PERIOD = TO_CHAR(TO_DATE('2026-08-01','YYYY-MM-DD'), 'YYYY-MM')
) STEP_1_RESULT
WHERE STEP_1_RESULT."毛差率%" < 5;
```

MIXED 模式示例（节选，EXECUTION_MODE='MIXED'，前 2 步并行 + 第 3 步汇总）：

```sql
-- ===== 组合指标：混合模式（并行步1、2 独立筛查同一批供应商，汇总步联合裁决） =====
INSERT INTO TBL_INDICATOR_COMBINATION
  (COMBINATION_ID, COMBINATION_CODE, COMBINATION_NAME, DESCRIPTION, CATEGORY,
   EXECUTION_MODE, STATUS, IS_SYSTEM, MODEL_TYPE, PARAMETER_CONFIG, CREATE_USER, CREATE_TIME)
VALUES
  ('COMB1760000000002', 'RISK_SUP_MIXED', '供应商混合模式风险模型',
   '按《供应商风险模型文档》规则自动扫描预警', 'RISK',
   'MIXED', 'ACTIVE', 'N', 'RISK',
   '[{"name":"startDate","type":"DATE","defaultValue":"2026-08-01"},{"name":"endDate","type":"DATE","defaultValue":"2026-08-31"}]',
   'system', SYSDATE);

-- 步骤1（并行）：独立筛查——低毛利，直接查业务表，输出实体键 SUPPLIER_CODE
INSERT INTO TBL_COMBINATION_INDICATOR
  (CONFIG_ID, COMBINATION_ID, INDICATOR_CODE, INDICATOR_NAME, DESCRIPTION, CATEGORY,
   PARAMETER_CONFIG, SQL_CONTENT, EXECUTION_ORDER, IS_ENABLED, DEPENDENCY_CONFIG, CREATE_TIME)
VALUES
  ('CONFIG1760000000002_1', 'COMB1760000000002', 'STEP1_LOW_MARGIN', '步骤1：低毛利筛查',
   '按文档4.1节低毛利规则：毛利率<5%判命中', 'RISK', '{"监测月份":"2026-08-01"}',
   'SELECT SUPPLIER_CODE, CASE WHEN GROSS_MARGIN < 5 THEN 1 ELSE 0 END AS RULE1_HIT FROM TBL_RISK_SUP_IND WHERE PERIOD = TO_CHAR(TO_DATE(${监测月份}, ''YYYY-MM-DD''), ''YYYY-MM'')',
   1, 'Y', NULL, SYSDATE);

-- 步骤2（并行）：独立筛查——关联交易，同样输出 SUPPLIER_CODE
-- （写法同上，SQL_CONTENT 独立查业务表，EXECUTION_ORDER=2，DEPENDENCY_CONFIG=NULL）

-- 步骤3（汇总）：联合裁决，唯一引用前步结果的步骤
INSERT INTO TBL_COMBINATION_INDICATOR
  (CONFIG_ID, COMBINATION_ID, INDICATOR_CODE, INDICATOR_NAME, DESCRIPTION, CATEGORY,
   PARAMETER_CONFIG, SQL_CONTENT, EXECUTION_ORDER, IS_ENABLED, DEPENDENCY_CONFIG, CREATE_TIME)
VALUES
  ('CONFIG1760000000002_3', 'COMB1760000000002', 'STEP3_SUMMARY', '步骤3：综合评分与预警',
   '按文档5.1节裁决规则：合并步骤1、2命中标记，两规则全命中红色、单命中黄色、未命中正常', 'RISK', '{}',
   'SELECT t1.SUPPLIER_CODE, t1.RULE1_HIT, t2.RULE2_HIT, CASE WHEN t1.RULE1_HIT + t2.RULE2_HIT = 2 THEN ''红色'' WHEN t1.RULE1_HIT + t2.RULE2_HIT = 1 THEN ''黄色'' ELSE ''正常'' END AS WARN_LEVEL FROM ${STEP_1_RESULT} t1 JOIN ${STEP_2_RESULT} t2 ON t1.SUPPLIER_CODE = t2.SUPPLIER_CODE',
   3, 'Y', '{"dependsOn":["STEP1_LOW_MARGIN","STEP2_RELATED"]}', SYSDATE);
```
