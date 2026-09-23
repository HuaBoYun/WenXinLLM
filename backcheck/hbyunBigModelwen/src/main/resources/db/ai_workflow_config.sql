-- =============================================================
-- AI 办公工作流配置（达梦 DM8）
-- 说明：业务人员在 AI 办公页可视化编排的工作流（触发关键字 + 步骤链），
--       存后端实现跨终端持久化；keywords/steps 为前端 JSON 序列化的 CLOB
-- =============================================================

CREATE TABLE ai_workflow_config (
  id          VARCHAR(64)  NOT NULL,
  user_id     VARCHAR(64),
  user_name   VARCHAR(128),
  name        VARCHAR(200) NOT NULL,
  enabled     INT DEFAULT 1,
  keywords    CLOB,
  steps       CLOB,
  create_time TIMESTAMP,
  update_time TIMESTAMP,
  PRIMARY KEY (id)
);
COMMENT ON TABLE  ai_workflow_config IS 'AI办公工作流配置(触发关键字+步骤链)';
COMMENT ON COLUMN ai_workflow_config.id IS '主键(雪花/前端生成)';
COMMENT ON COLUMN ai_workflow_config.user_id IS '归属用户ID(staffid)';
COMMENT ON COLUMN ai_workflow_config.user_name IS '创建人登录账号';
COMMENT ON COLUMN ai_workflow_config.name IS '工作流名称';
COMMENT ON COLUMN ai_workflow_config.enabled IS '状态 1启用 0停用';
COMMENT ON COLUMN ai_workflow_config.keywords IS '触发关键字JSON数组 ["季度总结",...],消息包含任一关键字即触发';
COMMENT ON COLUMN ai_workflow_config.steps IS '步骤JSON数组 [{id,name,prompt,artifact,api,rules,x,y},...] 按数组顺序串行执行,x/y为画布坐标';
COMMENT ON COLUMN ai_workflow_config.create_time IS '创建时间';
COMMENT ON COLUMN ai_workflow_config.update_time IS '更新时间';
CREATE INDEX idx_ai_wf_user ON ai_workflow_config(user_id);

COMMIT;
