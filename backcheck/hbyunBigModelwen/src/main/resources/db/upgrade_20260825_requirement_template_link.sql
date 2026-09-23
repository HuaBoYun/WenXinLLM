-- =============================================================
-- 【2026-08-25】需求管理页关联改造 存量库迁移脚本（达梦 DM8）
-- 目的：建立 监管模型需求(ai_regulatory_requirement)
--       与 业务梳理需求模板(ai_business_template) 的一对一关联
-- 使用：对已有 ai_regulatory_requirement 表的存量库执行本脚本；
--       全新建库直接执行 ai_regulatory_requirement.sql 即可，无需本脚本
-- =============================================================

ALTER TABLE ai_regulatory_requirement ADD COLUMN template_id VARCHAR(64);
COMMENT ON COLUMN ai_regulatory_requirement.template_id IS '关联的业务梳理需求模板ID(ai_business_template.id,一对一)';
CREATE INDEX idx_reg_req_tplid ON ai_regulatory_requirement(template_id);
COMMIT;
