-- =============================================================
-- 监管模型需求管理  数据库脚本（达梦 DM8）
-- 依赖：ai_regulatory_requirement 主表 + ai_regulatory_kpi 关键指标子表
-- 说明：达梦语法，大文本用 CLOB，时间用 TIMESTAMP，主键由应用层雪花生成
-- =============================================================

-- =============================================================
-- 主表：ai_regulatory_requirement
-- =============================================================
CREATE TABLE ai_regulatory_requirement (
  id               VARCHAR(64)  NOT NULL,
  req_no           VARCHAR(64),
  template_id      VARCHAR(64),
  department       VARCHAR(200),
  proposer         VARCHAR(100),
  proposer_id      VARCHAR(64),
  plan             CLOB,
  estimated_days   INT,
  priority         VARCHAR(20),
  value_score      INT,
  difficulty_score INT,
  invest_score     INT,
  priority_score   INT,
  submit_status    INT DEFAULT 0,
  submit_time      TIMESTAMP,
  creator_id       VARCHAR(64),
  creator_name     VARCHAR(100),
  create_time      TIMESTAMP,
  update_time      TIMESTAMP,
  PRIMARY KEY (id)
);
COMMENT ON TABLE  ai_regulatory_requirement IS '监管模型需求主表';
COMMENT ON COLUMN ai_regulatory_requirement.id IS '主键(雪花)';
COMMENT ON COLUMN ai_regulatory_requirement.req_no IS '需求编号 REQ-yyyyMMdd-xxxx';
COMMENT ON COLUMN ai_regulatory_requirement.template_id IS '关联的业务梳理需求模板ID(ai_business_template.id,一对一)';
COMMENT ON COLUMN ai_regulatory_requirement.department IS '提出单位';
COMMENT ON COLUMN ai_regulatory_requirement.proposer IS '提出人姓名';
COMMENT ON COLUMN ai_regulatory_requirement.proposer_id IS '提出人ID';
COMMENT ON COLUMN ai_regulatory_requirement.plan IS '实施方案(纯文本)';
COMMENT ON COLUMN ai_regulatory_requirement.estimated_days IS '预估人天';
COMMENT ON COLUMN ai_regulatory_requirement.priority IS '优先级 高/中/低';
COMMENT ON COLUMN ai_regulatory_requirement.value_score IS '需求价值评分 1-5';
COMMENT ON COLUMN ai_regulatory_requirement.difficulty_score IS '技术难度评分 1-5';
COMMENT ON COLUMN ai_regulatory_requirement.invest_score IS '资源投入评分 1-5';
COMMENT ON COLUMN ai_regulatory_requirement.priority_score IS '优先级得分 value*2-difficulty-invest';
COMMENT ON COLUMN ai_regulatory_requirement.submit_status IS '审批状态 0草稿 1待审批';
COMMENT ON COLUMN ai_regulatory_requirement.submit_time IS '提交审批时间';
COMMENT ON COLUMN ai_regulatory_requirement.creator_id IS '创建人ID';
COMMENT ON COLUMN ai_regulatory_requirement.creator_name IS '创建人姓名';
COMMENT ON COLUMN ai_regulatory_requirement.create_time IS '创建时间';
COMMENT ON COLUMN ai_regulatory_requirement.update_time IS '更新时间';


-- =============================================================
-- 关键指标子表：ai_regulatory_kpi
-- 每行监管需求可挂多个关键指标
-- =============================================================
CREATE TABLE ai_regulatory_kpi (
  id               VARCHAR(64) NOT NULL,
  requirement_id   VARCHAR(64) NOT NULL,
  seq_no           INT,
  req_name         VARCHAR(500),
  business_domain  VARCHAR(100),
  reg_goal         CLOB,
  kpi_list         CLOB,
  expected_effect  CLOB,
  attach_list      CLOB,
  create_time      TIMESTAMP,
  PRIMARY KEY (id)
);
COMMENT ON TABLE  ai_regulatory_kpi IS '监管模型需求-需求点/关键指标子表';
COMMENT ON COLUMN ai_regulatory_kpi.id IS '主键(雪花)';
COMMENT ON COLUMN ai_regulatory_kpi.requirement_id IS '所属需求主键';
COMMENT ON COLUMN ai_regulatory_kpi.seq_no IS '序号';
COMMENT ON COLUMN ai_regulatory_kpi.req_name IS '需求名称';
COMMENT ON COLUMN ai_regulatory_kpi.business_domain IS '业务域';
COMMENT ON COLUMN ai_regulatory_kpi.reg_goal IS '监管目标';
COMMENT ON COLUMN ai_regulatory_kpi.kpi_list IS '关键指标列表(JSON数组或换行文本)';
COMMENT ON COLUMN ai_regulatory_kpi.expected_effect IS '预期效果';
COMMENT ON COLUMN ai_regulatory_kpi.attach_list IS '附件列表(JSON数组:name/type/base64)';
COMMENT ON COLUMN ai_regulatory_kpi.create_time IS '创建时间';


-- =============================================================
-- 索引
-- =============================================================
CREATE INDEX idx_reg_req_creator ON ai_regulatory_requirement(creator_id);
CREATE INDEX idx_reg_req_reqno   ON ai_regulatory_requirement(req_no);
CREATE INDEX idx_reg_req_tplid   ON ai_regulatory_requirement(template_id);
CREATE INDEX idx_reg_kpi_reqid   ON ai_regulatory_kpi(requirement_id);
COMMIT;
