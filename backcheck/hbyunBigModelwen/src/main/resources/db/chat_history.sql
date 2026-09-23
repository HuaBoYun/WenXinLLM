-- AI对话历史记录表（达梦数据库版本）

-- 创建表
CREATE TABLE IF NOT EXISTS ai_chat_history (
    id VARCHAR(64) NOT NULL,
    user_id VARCHAR(64) NOT NULL,
    session_id VARCHAR(64),
    title VARCHAR(255),
    dialogue CLOB,
    has_document NUMBER(1) DEFAULT 0,
    create_time TIMESTAMP,
    update_time TIMESTAMP,
    PRIMARY KEY (id)
);

-- 添加表注释
COMMENT ON TABLE ai_chat_history IS 'AI对话历史记录表';

-- 添加列注释
COMMENT ON COLUMN ai_chat_history.id IS '主键ID';
COMMENT ON COLUMN ai_chat_history.user_id IS '用户ID';
COMMENT ON COLUMN ai_chat_history.session_id IS '会话ID';
COMMENT ON COLUMN ai_chat_history.title IS '对话标题';
COMMENT ON COLUMN ai_chat_history.dialogue IS '对话内容JSON';
COMMENT ON COLUMN ai_chat_history.has_document IS '是否包含文档 0-否 1-是';
COMMENT ON COLUMN ai_chat_history.create_time IS '创建时间';
COMMENT ON COLUMN ai_chat_history.update_time IS '更新时间';

-- 创建索引
CREATE INDEX idx_chat_history_user_id ON ai_chat_history(user_id);
CREATE INDEX idx_chat_history_session_id ON ai_chat_history(session_id);
CREATE INDEX idx_chat_history_update_time ON ai_chat_history(update_time);

