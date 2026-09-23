-- AI提示词配置表（达梦数据库版本）
-- 用于保存用户自定义的系统提示词，按 user_id + prompt_type 唯一
-- prompt_type 示例：consult-AI咨询，writing-AI建模（预留扩展）

-- 创建表
CREATE TABLE IF NOT EXISTS ai_prompt_config (
    id VARCHAR(64) NOT NULL,
    user_id VARCHAR(64) NOT NULL,
    prompt_type VARCHAR(64) NOT NULL,
    prompt_content CLOB,
    create_time TIMESTAMP,
    update_time TIMESTAMP,
    PRIMARY KEY (id)
);

-- 添加表注释
COMMENT ON TABLE ai_prompt_config IS 'AI提示词配置表';

-- 添加列注释
COMMENT ON COLUMN ai_prompt_config.id IS '主键ID';
COMMENT ON COLUMN ai_prompt_config.user_id IS '用户ID';
COMMENT ON COLUMN ai_prompt_config.prompt_type IS '提示词类型 consult-AI咨询 writing-AI建模';
COMMENT ON COLUMN ai_prompt_config.prompt_content IS '提示词内容';
COMMENT ON COLUMN ai_prompt_config.create_time IS '创建时间';
COMMENT ON COLUMN ai_prompt_config.update_time IS '更新时间';

-- 创建唯一索引：同一用户同一类型只保留一条配置
CREATE UNIQUE INDEX idx_prompt_config_user_type ON ai_prompt_config(user_id, prompt_type);
