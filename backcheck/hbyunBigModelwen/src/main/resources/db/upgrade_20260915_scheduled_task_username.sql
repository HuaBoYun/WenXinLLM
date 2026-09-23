-- 2026-09-15 定时任务改存创建人登录账号
-- 触发时由 hbyunBigModelwen 按账号执行代登录（移植 hbyunSystemSetting /login/loginCheck
-- 完整流程、去除密码校验）现换 token，任务始终以创建人身份、不过期凭证执行。
-- user_token 列保留：旧任务无账号、或代登录失败时回退使用。

ALTER TABLE ai_scheduled_task ADD COLUMN user_name VARCHAR(100);
COMMENT ON COLUMN ai_scheduled_task.user_name IS '创建人登录账号(触发时代登录换token)';

-- 存量任务迁移建议：按创建人补齐 user_name 后旧的 user_token 快照即不再依赖，
-- UPDATE ai_scheduled_task SET user_name = '<登录账号>' WHERE id = '<任务ID>';
