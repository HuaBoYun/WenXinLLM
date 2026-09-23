-- 成本中心相关数据表
-- 创建时间：2024-12-19
-- 描述：华博云财务共享平台 - 成本中心管理模块数据表

-- ==================== 成本中心主表 ====================
-- 成本中心基础信息表
CREATE TABLE T_COST_CENTER (
    center_id BIGINT PRIMARY KEY,
    center_code VARCHAR(50) NOT NULL UNIQUE COMMENT '成本中心编码',
    center_name VARCHAR(200) NOT NULL COMMENT '成本中心名称',
    center_type INT DEFAULT 1 COMMENT '成本中心类型：1-生产成本中心，2-管理成本中心，3-销售成本中心，4-辅助成本中心',
    parent_center_id BIGINT COMMENT '上级成本中心ID',
    center_level INT DEFAULT 1 COMMENT '成本中心层级',
    book_id BIGINT NOT NULL COMMENT '账簿ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    responsible_person VARCHAR(100) COMMENT '负责人',
    contact_phone VARCHAR(50) COMMENT '联系电话',
    contact_email VARCHAR(100) COMMENT '联系邮箱',
    center_description TEXT COMMENT '成本中心描述',
    is_enabled TINYINT DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    version INT DEFAULT 1 COMMENT '版本号',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    INDEX idx_center_code (center_code),
    INDEX idx_book_tenant (book_id, tenant_id),
    INDEX idx_parent_center (parent_center_id),
    INDEX idx_center_type (center_type),
    INDEX idx_is_enabled (is_enabled),
    INDEX idx_is_deleted (is_deleted)
) COMMENT='成本中心基础信息表';

-- ==================== 成本归集相关表 ====================
-- 成本归集主表
CREATE TABLE T_COST_COLLECTION (
    collection_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '归集ID',
    collection_no VARCHAR(50) NOT NULL UNIQUE COMMENT '归集单号',
    collection_period VARCHAR(20) NOT NULL COMMENT '归集期间，格式：YYYY-MM',
    cost_center_id BIGINT NOT NULL COMMENT '成本中心ID',
    collection_type INT NOT NULL COMMENT '归集类型：1-直接成本，2-间接成本，3-制造费用',
    collection_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '归集金额',
    collection_status INT DEFAULT 1 COMMENT '归集状态：1-待归集，2-归集中，3-已归集，4-已审核',
    collection_method INT DEFAULT 1 COMMENT '归集方式：1-手工归集，2-自动归集',
    collection_date DATE COMMENT '归集日期',
    auditor_id BIGINT COMMENT '审核人ID',
    auditor_name VARCHAR(100) COMMENT '审核人姓名',
    audit_time DATETIME COMMENT '审核时间',
    audit_remark TEXT COMMENT '审核备注',
    book_id BIGINT NOT NULL COMMENT '账簿ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    remark TEXT COMMENT '备注',
    version INT DEFAULT 1 COMMENT '版本号',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    FOREIGN KEY (cost_center_id) REFERENCES T_COST_CENTER(center_id),
    INDEX idx_collection_no (collection_no),
    INDEX idx_collection_period (collection_period),
    INDEX idx_cost_center_id (cost_center_id),
    INDEX idx_collection_type (collection_type),
    INDEX idx_collection_status (collection_status),
    INDEX idx_book_tenant (book_id, tenant_id),
    INDEX idx_collection_date (collection_date),
    INDEX idx_is_deleted (is_deleted)
) COMMENT='成本归集主表';

-- 成本归集明细表
CREATE TABLE T_COST_COLLECTION_DETAIL (
    detail_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    collection_id BIGINT NOT NULL COMMENT '归集ID',
    cost_element_code VARCHAR(50) COMMENT '成本要素编码',
    cost_element_name VARCHAR(200) COMMENT '成本要素名称',
    voucher_id BIGINT COMMENT '凭证ID',
    voucher_no VARCHAR(50) COMMENT '凭证号',
    account_code VARCHAR(50) COMMENT '会计科目编码',
    account_name VARCHAR(200) COMMENT '会计科目名称',
    original_amount DECIMAL(18,2) DEFAULT 0.00 COMMENT '原始金额',
    allocation_rate DECIMAL(10,6) DEFAULT 1.000000 COMMENT '分摊率',
    allocated_amount DECIMAL(18,2) DEFAULT 0.00 COMMENT '分摊金额',
    department_name VARCHAR(200) COMMENT '部门名称',
    project_name VARCHAR(200) COMMENT '项目名称',
    business_type VARCHAR(50) COMMENT '业务类型',
    remark TEXT COMMENT '备注',
    book_id BIGINT NOT NULL COMMENT '账簿ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    version INT DEFAULT 1 COMMENT '版本号',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (collection_id) REFERENCES T_COST_COLLECTION(collection_id),
    INDEX idx_collection_id (collection_id),
    INDEX idx_cost_element_code (cost_element_code),
    INDEX idx_voucher_id (voucher_id),
    INDEX idx_account_code (account_code),
    INDEX idx_book_tenant (book_id, tenant_id),
    INDEX idx_is_deleted (is_deleted)
) COMMENT='成本归集明细表';

-- ==================== 成本预算相关表 ====================
-- 成本预算表
CREATE TABLE T_COST_BUDGET (
    budget_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '预算ID',
    budget_no VARCHAR(50) NOT NULL UNIQUE COMMENT '预算单号',
    budget_period VARCHAR(20) NOT NULL COMMENT '预算期间，格式：YYYY或YYYY-MM',
    budget_type INT DEFAULT 1 COMMENT '预算类型：1-年度预算，2-季度预算，3-月度预算',
    cost_center_id BIGINT NOT NULL COMMENT '成本中心ID',
    budget_amount DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '预算金额',
    actual_amount DECIMAL(18,2) DEFAULT 0.00 COMMENT '实际金额',
    variance_amount DECIMAL(18,2) DEFAULT 0.00 COMMENT '差异金额',
    variance_rate DECIMAL(8,4) DEFAULT 0.0000 COMMENT '差异率(%)',
    execution_rate DECIMAL(8,4) DEFAULT 0.0000 COMMENT '执行率(%)',
    budget_status INT DEFAULT 1 COMMENT '预算状态：1-草稿，2-执行中，3-已完成，4-已关闭',
    approve_status INT DEFAULT 0 COMMENT '审批状态：0-未提交，1-审批中，2-已审批，3-已驳回',
    approver_id BIGINT COMMENT '审批人ID',
    approver_name VARCHAR(100) COMMENT '审批人姓名',
    approve_time DATETIME COMMENT '审批时间',
    approve_remark TEXT COMMENT '审批备注',
    book_id BIGINT NOT NULL COMMENT '账簿ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    remark TEXT COMMENT '备注',
    version INT DEFAULT 1 COMMENT '版本号',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    FOREIGN KEY (cost_center_id) REFERENCES T_COST_CENTER(center_id),
    INDEX idx_budget_no (budget_no),
    INDEX idx_budget_period (budget_period),
    INDEX idx_cost_center_id (cost_center_id),
    INDEX idx_budget_type (budget_type),
    INDEX idx_budget_status (budget_status),
    INDEX idx_book_tenant (book_id, tenant_id),
    INDEX idx_is_deleted (is_deleted)
) COMMENT='成本预算表';

-- ==================== 成本控制相关表 ====================
-- 成本控制表
CREATE TABLE T_COST_CONTROL (
    control_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '控制ID',
    control_no VARCHAR(50) NOT NULL UNIQUE COMMENT '控制规则编号',
    control_name VARCHAR(200) NOT NULL COMMENT '控制规则名称',
    cost_center_id BIGINT NOT NULL COMMENT '成本中心ID',
    control_type INT DEFAULT 1 COMMENT '控制类型：1-预算控制，2-阈值控制，3-比率控制',
    control_limit DECIMAL(18,2) DEFAULT 0.00 COMMENT '控制限额',
    warning_threshold DECIMAL(8,4) DEFAULT 80.0000 COMMENT '预警阈值(%)',
    error_threshold DECIMAL(8,4) DEFAULT 100.0000 COMMENT '错误阈值(%)',
    control_status INT DEFAULT 1 COMMENT '控制状态：1-正常，2-预警，3-超限',
    is_enabled TINYINT DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
    effective_date DATE COMMENT '生效日期',
    expire_date DATE COMMENT '失效日期',
    check_frequency INT DEFAULT 1 COMMENT '检查频率：1-实时，2-每日，3-每周，4-每月',
    book_id BIGINT NOT NULL COMMENT '账簿ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    control_rule TEXT COMMENT '控制规则配置',
    remark TEXT COMMENT '备注',
    version INT DEFAULT 1 COMMENT '版本号',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    FOREIGN KEY (cost_center_id) REFERENCES T_COST_CENTER(center_id),
    INDEX idx_control_no (control_no),
    INDEX idx_cost_center_id (cost_center_id),
    INDEX idx_control_type (control_type),
    INDEX idx_control_status (control_status),
    INDEX idx_is_enabled (is_enabled),
    INDEX idx_book_tenant (book_id, tenant_id),
    INDEX idx_effective_expire (effective_date, expire_date),
    INDEX idx_is_deleted (is_deleted)
) COMMENT='成本控制表';

-- ==================== 成本分析相关表 ====================
-- 成本分析结果表
CREATE TABLE T_COST_ANALYSIS (
    analysis_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分析ID',
    analysis_no VARCHAR(50) NOT NULL UNIQUE COMMENT '分析编号',
    analysis_period VARCHAR(20) NOT NULL COMMENT '分析期间',
    analysis_type INT DEFAULT 1 COMMENT '分析类型：1-结构分析，2-趋势分析，3-对比分析，4-差异分析',
    cost_center_id BIGINT COMMENT '成本中心ID（为空表示全部分析）',
    total_cost DECIMAL(18,2) DEFAULT 0.00 COMMENT '总成本',
    direct_cost DECIMAL(18,2) DEFAULT 0.00 COMMENT '直接成本',
    indirect_cost DECIMAL(18,2) DEFAULT 0.00 COMMENT '间接成本',
    manufacturing_cost DECIMAL(18,2) DEFAULT 0.00 COMMENT '制造费用',
    budget_cost DECIMAL(18,2) DEFAULT 0.00 COMMENT '预算成本',
    variance_amount DECIMAL(18,2) DEFAULT 0.00 COMMENT '差异金额',
    variance_rate DECIMAL(8,4) DEFAULT 0.0000 COMMENT '差异率(%)',
    efficiency_index DECIMAL(8,4) DEFAULT 0.0000 COMMENT '效率指数',
    analysis_status INT DEFAULT 1 COMMENT '分析状态：1-草稿，2-已完成',
    book_id BIGINT NOT NULL COMMENT '账簿ID',
    tenant_id BIGINT NOT NULL COMMENT '租户ID',
    analysis_data TEXT COMMENT '分析结果数据（JSON格式）',
    chart_data TEXT COMMENT '图表数据（JSON格式）',
    remark TEXT COMMENT '备注',
    version INT DEFAULT 1 COMMENT '版本号',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    FOREIGN KEY (cost_center_id) REFERENCES T_COST_CENTER(center_id),
    INDEX idx_analysis_no (analysis_no),
    INDEX idx_analysis_period (analysis_period),
    INDEX idx_cost_center_id (cost_center_id),
    INDEX idx_analysis_type (analysis_type),
    INDEX idx_book_tenant (book_id, tenant_id),
    INDEX idx_is_deleted (is_deleted)
) COMMENT='成本分析结果表';

-- ==================== 初始化数据 ====================
-- 插入成本中心基础数据
INSERT INTO T_COST_CENTER (center_id, center_code, center_name, center_type, parent_center_id, center_level, book_id, tenant_id, responsible_person, is_enabled) VALUES
(10001, 'CC001', '生产部成本中心', 1, NULL, 1, 1, 1000, '张三', 1),
(10002, 'CC002', '管理部成本中心', 2, NULL, 1, 1, 1000, '李四', 1),
(10003, 'CC003', '销售部成本中心', 3, NULL, 1, 1, 1000, '王五', 1),
(10004, 'CC004', '辅助生产部成本中心', 4, NULL, 1, 1, 1000, '赵六', 1),
(10005, 'CC005', '生产一车间', 1, 10001, 2, 1, 1000, '钱七', 1),
(10006, 'CC006', '生产二车间', 1, 10001, 2, 1, 1000, '孙八', 1),
(10007, 'CC007', '质量部', 4, 10001, 2, 1, 1000, '周九', 1),
(10008, 'CC008', '财务部', 2, 10002, 2, 1, 1000, '吴十', 1),
(10009, 'CC009', '人力资源部', 2, 10002, 2, 1, 1000, '郑十一', 1),
(10010, 'CC010', '采购部', 2, 10002, 2, 1, 1000, '王十二', 1);

-- 插入成本归集测试数据
INSERT INTO T_COST_COLLECTION (collection_no, collection_period, cost_center_id, collection_type, collection_amount, collection_status, collection_date, book_id, tenant_id) VALUES
('COL202401001', '2024-01', 10001, 1, 150000.00, 3, '2024-01-15', 1, 1000),
('COL202401002', '2024-01', 10002, 2, 80000.00, 3, '2024-01-16', 1, 1000),
('COL202401003', '2024-01', 10003, 1, 120000.00, 2, '2024-01-17', 1, 1000),
('COL202402001', '2024-02', 10001, 3, 60000.00, 1, NULL, 1, 1000),
('COL202402002', '2024-02', 10005, 1, 200000.00, 3, '2024-02-15', 1, 1000);

-- 插入成本预算测试数据
INSERT INTO T_COST_BUDGET (budget_no, budget_period, budget_type, cost_center_id, budget_amount, actual_amount, budget_status, book_id, tenant_id) VALUES
('BUD2024001', '2024', 1, 10001, 1000000.00, 850000.00, 2, 1, 1000),
('BUD2024002', '2024', 1, 10002, 500000.00, 480000.00, 2, 1, 1000),
('BUD2024003', '2024', 1, 10003, 800000.00, 750000.00, 2, 1, 1000),
('BUD2024004', '2024', 1, 10005, 1200000.00, 1100000.00, 2, 1, 1000);

-- 插入成本控制测试数据
INSERT INTO T_COST_CONTROL (control_no, control_name, cost_center_id, control_type, control_limit, warning_threshold, control_status, book_id, tenant_id) VALUES
('CTL2024001', '生产成本控制规则', 10001, 1, 100000.00, 80.0000, 1, 1, 1000),
('CTL2024002', '管理费用控制规则', 10002, 1, 50000.00, 85.0000, 1, 1, 1000),
('CTL2024003', '销售费用控制规则', 10003, 1, 80000.00, 90.0000, 2, 1, 1000);

-- 插入成本分析测试数据
INSERT INTO T_COST_ANALYSIS (analysis_no, analysis_period, analysis_type, cost_center_id, total_cost, direct_cost, indirect_cost, manufacturing_cost, analysis_status, book_id, tenant_id) VALUES
('ANA2024001', '2024-01', 1, NULL, 15800000.00, 5000000.00, 8000000.00, 2800000.00, 2, 1, 1000),
('ANA2024002', '2024-01', 1, 10001, 6500000.00, 3000000.00, 2000000.00, 1500000.00, 2, 1, 1000),
('ANA2024003', '2024-02', 2, 10001, 7200000.00, 3500000.00, 2200000.00, 1500000.00, 2, 1, 1000);

-- ==================== 索引优化建议 ====================
-- 如果数据量大，可以考虑添加以下复合索引
-- ALTER TABLE T_COST_COLLECTION ADD INDEX idx_period_status_center (collection_period, collection_status, cost_center_id);
-- ALTER TABLE T_COST_BUDGET ADD INDEX idx_period_type_center (budget_period, budget_type, cost_center_id);
-- ALTER TABLE T_COST_ANALYSIS ADD INDEX idx_period_type_center (analysis_period, analysis_type, cost_center_id);

-- ==================== 数据字典说明 ====================
-- 成本中心类型：1-生产成本中心，2-管理成本中心，3-销售成本中心，4-辅助成本中心
-- 归集类型：1-直接成本，2-间接成本，3-制造费用
-- 归集状态：1-待归集，2-归集中，3-已归集，4-已审核
-- 归集方式：1-手工归集，2-自动归集
-- 预算类型：1-年度预算，2-季度预算，3-月度预算
-- 预算状态：1-草稿，2-执行中，3-已完成，4-已关闭
-- 审批状态：0-未提交，1-审批中，2-已审批，3-已驳回
-- 控制类型：1-预算控制，2-阈值控制，3-比率控制
-- 控制状态：1-正常，2-预警，3-超限
-- 检查频率：1-实时，2-每日，3-每周，4-每月
-- 分析类型：1-结构分析，2-趋势分析，3-对比分析，4-差异分析
-- 分析状态：1-草稿，2-已完成