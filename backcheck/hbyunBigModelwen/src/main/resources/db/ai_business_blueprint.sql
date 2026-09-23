-- =============================================================
-- AI 编程模块 —— 业务梳理 & 业务蓝图 数据库脚本（达梦 DM8）
-- 来源：《AI编程-业务梳理与业务蓝图-数据库设计文档》
-- 日期：2026-06-24
-- 说明：
--   Part A 已有表加列（ai_business_template / ai_business_doc_version）
--   Part B 新建表（confirm / blueprint / requirement / bug / draft）
--   Part C 索引
--   Part D 存量数据迁移
-- 注意：达梦语法，大文本用 CLOB，时间用 TIMESTAMP，主键由应用层雪花生成。
--      ALTER/CREATE 非幂等，重复执行前请确认；建议先在测试库验证。
-- =============================================================


-- =============================================================
-- Part A：已有表加列
-- =============================================================

-- A.1 ai_business_template（业务梳理节点：需求/流程模版）
ALTER TABLE ai_business_template ADD node_type INT DEFAULT 0;
ALTER TABLE ai_business_template ADD flow_source VARCHAR(20);
ALTER TABLE ai_business_template ADD source_flow_id VARCHAR(64);
ALTER TABLE ai_business_template ADD confirm_status INT DEFAULT 0;
ALTER TABLE ai_business_template ADD confirmed_version_id VARCHAR(64);
ALTER TABLE ai_business_template ADD description VARCHAR(2000);

COMMENT ON COLUMN ai_business_template.node_type IS '节点类型:0=流程模版,1=需求';
COMMENT ON COLUMN ai_business_template.flow_source IS '需求来源:custom/builtin';
COMMENT ON COLUMN ai_business_template.source_flow_id IS 'builtin引用的流程模版id';
COMMENT ON COLUMN ai_business_template.confirm_status IS '确认状态:0待确认,1已确认';
COMMENT ON COLUMN ai_business_template.confirmed_version_id IS '已确认指向的版本id';
COMMENT ON COLUMN ai_business_template.description IS '需求描述';

-- A.2 ai_business_doc_version（文档版本/留痕）
ALTER TABLE ai_business_doc_version ADD edit_type VARCHAR(20) DEFAULT 'manual';
COMMENT ON COLUMN ai_business_doc_version.edit_type IS '编辑类型:manual/ai/restore/confirm';


-- =============================================================
-- Part B：新建表
-- =============================================================

-- B.1 ai_business_doc_confirm（签字确认记录）
CREATE TABLE ai_business_doc_confirm (
  id              VARCHAR(64)   NOT NULL,
  template_id     VARCHAR(64)   NOT NULL,
  version_id      VARCHAR(64)   NOT NULL,
  version_no      INT,
  signature_img   CLOB,
  confirmer_names VARCHAR(500),
  confirm_opinion VARCHAR(1000),
  operator_id     VARCHAR(64),
  operator_name   VARCHAR(100),
  confirm_time    TIMESTAMP,
  PRIMARY KEY (id)
);
COMMENT ON TABLE  ai_business_doc_confirm IS '业务梳理-签字确认记录';
COMMENT ON COLUMN ai_business_doc_confirm.id IS '主键';
COMMENT ON COLUMN ai_business_doc_confirm.template_id IS '所属需求ID';
COMMENT ON COLUMN ai_business_doc_confirm.version_id IS '确认时的版本ID';
COMMENT ON COLUMN ai_business_doc_confirm.version_no IS '确认时的版本号';
COMMENT ON COLUMN ai_business_doc_confirm.signature_img IS '签名图base64(canvas导出)';
COMMENT ON COLUMN ai_business_doc_confirm.confirmer_names IS '确认人姓名,可多个逗号分隔';
COMMENT ON COLUMN ai_business_doc_confirm.confirm_opinion IS '确认意见';
COMMENT ON COLUMN ai_business_doc_confirm.operator_id IS '操作人ID';
COMMENT ON COLUMN ai_business_doc_confirm.operator_name IS '操作人姓名';
COMMENT ON COLUMN ai_business_doc_confirm.confirm_time IS '确认时间';

-- B.2 ai_blueprint（业务蓝图主表）
CREATE TABLE ai_blueprint (
  id                 VARCHAR(64) NOT NULL,
  source_template_id VARCHAR(64),
  customer_name      VARCHAR(200),
  project_name       VARCHAR(200),
  contract_no        VARCHAR(100),
  department         VARCHAR(200),
  proposer           VARCHAR(100),
  plan               CLOB,
  estimated_days     INT,
  priority           VARCHAR(20),
  creator_id         VARCHAR(64),
  creator_name       VARCHAR(100),
  create_time        TIMESTAMP,
  update_time        TIMESTAMP,
  PRIMARY KEY (id)
);
COMMENT ON TABLE  ai_blueprint IS '业务蓝图主表';
COMMENT ON COLUMN ai_blueprint.id IS '主键';
COMMENT ON COLUMN ai_blueprint.source_template_id IS '来源需求ID';
COMMENT ON COLUMN ai_blueprint.customer_name IS '客户名称';
COMMENT ON COLUMN ai_blueprint.project_name IS '项目名称';
COMMENT ON COLUMN ai_blueprint.contract_no IS '合同编号';
COMMENT ON COLUMN ai_blueprint.department IS '提出单位';
COMMENT ON COLUMN ai_blueprint.proposer IS '提出人';
COMMENT ON COLUMN ai_blueprint.plan IS '实施方案';
COMMENT ON COLUMN ai_blueprint.estimated_days IS '预估人天';
COMMENT ON COLUMN ai_blueprint.priority IS '优先级:紧急/高/中/低';
COMMENT ON COLUMN ai_blueprint.creator_id IS '创建人ID';
COMMENT ON COLUMN ai_blueprint.creator_name IS '创建人姓名';
COMMENT ON COLUMN ai_blueprint.create_time IS '创建时间';
COMMENT ON COLUMN ai_blueprint.update_time IS '更新时间';

-- B.3 ai_blueprint_requirement（蓝图需求点，字段对齐现有需求行）
CREATE TABLE ai_blueprint_requirement (
  id            VARCHAR(64) NOT NULL,
  blueprint_id  VARCHAR(64) NOT NULL,
  seq_no        INT,
  status        VARCHAR(20),
  submitter     VARCHAR(100),
  submit_time   VARCHAR(20),
  req_domain    VARCHAR(50),
  module        VARCHAR(200),
  description   CLOB,
  image_list    CLOB,
  urgency       VARCHAR(20),
  resolve_date  VARCHAR(20),
  handler       VARCHAR(100),
  solution      CLOB,
  resolved      VARCHAR(10),
  create_time   TIMESTAMP,
  PRIMARY KEY (id)
);
COMMENT ON TABLE  ai_blueprint_requirement IS '业务蓝图-需求点';
COMMENT ON COLUMN ai_blueprint_requirement.id IS '主键';
COMMENT ON COLUMN ai_blueprint_requirement.blueprint_id IS '所属蓝图ID';
COMMENT ON COLUMN ai_blueprint_requirement.seq_no IS '序号';
COMMENT ON COLUMN ai_blueprint_requirement.status IS '问题状态:待处理/已完成';
COMMENT ON COLUMN ai_blueprint_requirement.submitter IS '提交人';
COMMENT ON COLUMN ai_blueprint_requirement.submit_time IS '提交时间';
COMMENT ON COLUMN ai_blueprint_requirement.req_domain IS '问题领域:需求调整/新增需求/BUG修复/功能优化';
COMMENT ON COLUMN ai_blueprint_requirement.module IS '问题模块';
COMMENT ON COLUMN ai_blueprint_requirement.description IS '问题/功能点描述';
COMMENT ON COLUMN ai_blueprint_requirement.image_list IS '问题截图base64 JSON数组';
COMMENT ON COLUMN ai_blueprint_requirement.urgency IS '紧急程度:高/中/低';
COMMENT ON COLUMN ai_blueprint_requirement.resolve_date IS '预计解决时间';
COMMENT ON COLUMN ai_blueprint_requirement.handler IS '处理人';
COMMENT ON COLUMN ai_blueprint_requirement.solution IS '解决方案';
COMMENT ON COLUMN ai_blueprint_requirement.resolved IS '是否解决:是/否';
COMMENT ON COLUMN ai_blueprint_requirement.create_time IS '创建时间';

-- B.4 ai_blueprint_bug（蓝图 Bug 列表）
CREATE TABLE ai_blueprint_bug (
  id            VARCHAR(64) NOT NULL,
  blueprint_id  VARCHAR(64) NOT NULL,
  seq_no        INT,
  status        VARCHAR(20),
  submitter     VARCHAR(100),
  submit_time   VARCHAR(20),
  module        VARCHAR(200),
  description   CLOB,
  image_list    CLOB,
  urgency       VARCHAR(20),
  resolve_date  VARCHAR(20),
  handler       VARCHAR(100),
  solution      CLOB,
  resolved      VARCHAR(10),
  create_time   TIMESTAMP,
  PRIMARY KEY (id)
);
COMMENT ON TABLE  ai_blueprint_bug IS '业务蓝图-Bug列表';
COMMENT ON COLUMN ai_blueprint_bug.id IS '主键';
COMMENT ON COLUMN ai_blueprint_bug.blueprint_id IS '所属蓝图ID';
COMMENT ON COLUMN ai_blueprint_bug.seq_no IS '序号';
COMMENT ON COLUMN ai_blueprint_bug.status IS '状态:待处理/已完成';
COMMENT ON COLUMN ai_blueprint_bug.submitter IS '提交人';
COMMENT ON COLUMN ai_blueprint_bug.submit_time IS '提交时间';
COMMENT ON COLUMN ai_blueprint_bug.module IS '所属模块';
COMMENT ON COLUMN ai_blueprint_bug.description IS '问题描述';
COMMENT ON COLUMN ai_blueprint_bug.image_list IS '截图base64 JSON数组';
COMMENT ON COLUMN ai_blueprint_bug.urgency IS '紧急程度:高/中/低';
COMMENT ON COLUMN ai_blueprint_bug.resolve_date IS '预计解决时间';
COMMENT ON COLUMN ai_blueprint_bug.handler IS '处理人';
COMMENT ON COLUMN ai_blueprint_bug.solution IS '解决方案';
COMMENT ON COLUMN ai_blueprint_bug.resolved IS '是否解决:是/否';
COMMENT ON COLUMN ai_blueprint_bug.create_time IS '创建时间';

-- B.5 ai_blueprint_draft（业务蓝图草稿）
CREATE TABLE ai_blueprint_draft (
  id          VARCHAR(64) NOT NULL,
  user_id     VARCHAR(64) NOT NULL,
  user_name   VARCHAR(100),
  title       VARCHAR(255),
  form_data   CLOB,
  create_time TIMESTAMP,
  update_time TIMESTAMP,
  PRIMARY KEY (id)
);
COMMENT ON TABLE  ai_blueprint_draft IS '业务蓝图-草稿';
COMMENT ON COLUMN ai_blueprint_draft.id IS '主键';
COMMENT ON COLUMN ai_blueprint_draft.user_id IS '用户ID';
COMMENT ON COLUMN ai_blueprint_draft.user_name IS '用户姓名';
COMMENT ON COLUMN ai_blueprint_draft.title IS '草稿标题(默认项目名)';
COMMENT ON COLUMN ai_blueprint_draft.form_data IS '表单整体JSON(requirements+bugs+项目信息)';
COMMENT ON COLUMN ai_blueprint_draft.create_time IS '创建时间';
COMMENT ON COLUMN ai_blueprint_draft.update_time IS '更新时间';


-- =============================================================
-- Part C：索引
-- =============================================================
CREATE INDEX idx_docver_tpl    ON ai_business_doc_version(template_id);
CREATE INDEX idx_draft_user    ON ai_business_user_draft(user_id);
CREATE INDEX idx_confirm_tpl   ON ai_business_doc_confirm(template_id);
CREATE INDEX idx_bp_req_bp     ON ai_blueprint_requirement(blueprint_id);
CREATE INDEX idx_bp_bug_bp     ON ai_blueprint_bug(blueprint_id);
CREATE INDEX idx_bp_draft_user ON ai_blueprint_draft(user_id);


-- =============================================================
-- Part D：存量数据迁移
-- =============================================================
-- 将存量系统流程模版显式置为流程模版类型（node_type=0）
UPDATE ai_business_template SET node_type = 0 WHERE is_system = 1;
COMMIT;

