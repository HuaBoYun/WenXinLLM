-- 业务蓝图草稿表（达梦数据库兼容）
CREATE TABLE IF NOT EXISTS ai_blueprint_draft (
    id            VARCHAR(64)   NOT NULL,
    user_id       VARCHAR(64)   NOT NULL,
    user_name     VARCHAR(128),
    title         VARCHAR(255),
    form_data     CLOB,
    create_time   TIMESTAMP,
    update_time   TIMESTAMP,
    PRIMARY KEY (id)
);

COMMENT ON TABLE  ai_blueprint_draft              IS '业务蓝图草稿表';
COMMENT ON COLUMN ai_blueprint_draft.id           IS '主键ID';
COMMENT ON COLUMN ai_blueprint_draft.user_id      IS '用户ID';
COMMENT ON COLUMN ai_blueprint_draft.user_name    IS '用户姓名';
COMMENT ON COLUMN ai_blueprint_draft.title        IS '草稿标题（项目名称）';
COMMENT ON COLUMN ai_blueprint_draft.form_data    IS '表单完整数据 JSON（含需求条目+图片base64）';
COMMENT ON COLUMN ai_blueprint_draft.create_time  IS '创建时间';
COMMENT ON COLUMN ai_blueprint_draft.update_time  IS '更新时间';

CREATE INDEX idx_blueprint_draft_user_id ON ai_blueprint_draft(user_id);
