-- =============================================================
-- AI 办公定时任务 后端调度（达梦 DM8）
-- 说明：定时任务与执行日志由 hbyunBigModelwen 调度线程直接驱动，
--       到点后由后端直连 xingguang wenxinclaw 网关发送消息
-- =============================================================

-- 定时任务表
CREATE TABLE ai_scheduled_task (
  id                   VARCHAR(64)  NOT NULL,
  task_name            VARCHAR(2000) NOT NULL,
  mode                 VARCHAR(20)  NOT NULL,
  exec_time            VARCHAR(10),
  exec_datetime        TIMESTAMP,
  interval_minutes     INT,
  module_name          VARCHAR(200),
  module_identification VARCHAR(100),
  user_id              VARCHAR(64),
  user_token           VARCHAR(2000),
  status               INT DEFAULT 1,
  done                 INT DEFAULT 0,
  last_fire_time       TIMESTAMP,
  last_fire_date       VARCHAR(20),
  creator_name         VARCHAR(100),
  create_time          TIMESTAMP,
  update_time          TIMESTAMP,
  PRIMARY KEY (id)
);
COMMENT ON TABLE  ai_scheduled_task IS 'AI办公定时任务表(后端调度)';
COMMENT ON COLUMN ai_scheduled_task.id IS '主键(雪花)';
COMMENT ON COLUMN ai_scheduled_task.task_name IS '任务描述(到点发送的消息内容)';
COMMENT ON COLUMN ai_scheduled_task.mode IS '执行方式 daily每天定时/once执行一次/interval循环间隔';
COMMENT ON COLUMN ai_scheduled_task.exec_time IS 'daily模式执行时间 HH:mm';
COMMENT ON COLUMN ai_scheduled_task.exec_datetime IS 'once模式执行时间点';
COMMENT ON COLUMN ai_scheduled_task.interval_minutes IS 'interval模式间隔分钟数';
COMMENT ON COLUMN ai_scheduled_task.module_name IS '创建时选中的模块名称(上下文快照)';
COMMENT ON COLUMN ai_scheduled_task.module_identification IS '创建时选中的模块唯一标识';
COMMENT ON COLUMN ai_scheduled_task.user_id IS '创建人ID';
COMMENT ON COLUMN ai_scheduled_task.user_token IS '创建人token快照(发送时拼USER_TOKEN前缀)';
COMMENT ON COLUMN ai_scheduled_task.status IS '状态 1启用 0停用';
COMMENT ON COLUMN ai_scheduled_task.done IS 'once模式是否已执行 0否 1是';
COMMENT ON COLUMN ai_scheduled_task.last_fire_time IS '上次触发时间';
COMMENT ON COLUMN ai_scheduled_task.last_fire_date IS 'daily模式上次触发日期 yyyy-M-d';
CREATE INDEX idx_sch_task_status ON ai_scheduled_task(status);

-- 执行日志表
CREATE TABLE ai_scheduled_task_log (
  id                   VARCHAR(64)  NOT NULL,
  task_id              VARCHAR(64)  NOT NULL,
  task_desc            VARCHAR(2000),
  fire_time            TIMESTAMP,
  status               VARCHAR(20),
  response_excerpt     VARCHAR(2000),
  error_msg            VARCHAR(2000),
  create_time          TIMESTAMP,
  PRIMARY KEY (id)
);
COMMENT ON TABLE  ai_scheduled_task_log IS 'AI办公定时任务执行日志';
COMMENT ON COLUMN ai_scheduled_task_log.task_id IS '任务ID';
COMMENT ON COLUMN ai_scheduled_task_log.task_desc IS '任务描述快照';
COMMENT ON COLUMN ai_scheduled_task_log.fire_time IS '触发时间';
COMMENT ON COLUMN ai_scheduled_task_log.status IS '状态 sent已发送/responded已回复/failed发送失败/error回复异常';
COMMENT ON COLUMN ai_scheduled_task_log.response_excerpt IS 'AI回复摘要(前2000字符)';
COMMENT ON COLUMN ai_scheduled_task_log.error_msg IS '错误信息';
CREATE INDEX idx_sch_task_log_task ON ai_scheduled_task_log(task_id);

-- wenxinclaw 网关设备身份表（后端直连用的 Ed25519 设备密钥，单行）
CREATE TABLE ai_wenxinclaw_device_identity (
  id                   VARCHAR(64)  NOT NULL,
  device_id            VARCHAR(128) NOT NULL,
  public_key           VARCHAR(256) NOT NULL,
  private_key          VARCHAR(512) NOT NULL,
  create_time          TIMESTAMP,
  PRIMARY KEY (id)
);
COMMENT ON TABLE  ai_wenxinclaw_device_identity IS 'wenxinclaw网关后端设备身份(Ed25519)';
COMMENT ON COLUMN ai_wenxinclaw_device_identity.device_id IS '设备ID(SHA256指纹hex)';

COMMIT;

-- =============================================================
-- 升级：20260902 执行记录支持完整回复展示与按用户隔离查询
-- =============================================================
ALTER TABLE ai_scheduled_task_log ADD COLUMN RESPONSE_CONTENT CLOB;
COMMENT ON COLUMN ai_scheduled_task_log.response_content IS 'AI完整回复(不截断)';
