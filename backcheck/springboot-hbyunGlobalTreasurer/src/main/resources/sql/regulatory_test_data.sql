-- =====================================================
-- 监管报送模块测试数据
-- 生成时间: 2026-01-22
-- 每个表10条测试数据
-- =====================================================

-- 1. 监管机构表 TBL_REGULATORY_AUTHORITY
INSERT INTO TBL_REGULATORY_AUTHORITY (AUTHORITY_ID, AUTHORITY_CODE, AUTHORITY_NAME, AUTHORITY_TYPE, CONTACT_PERSON, CONTACT_PHONE, CONTACT_EMAIL, ADDRESS, WEBSITE, DESCRIPTION, IS_ACTIVE, IS_IMPORTANT, SORT_ORDER, COMPANY_ID, DELETE_FLAG, CREATED_BY, CREATED_TIME, REMARK) VALUES
('AUTH001', 'PBOC', '中国人民银行', 'CENTRAL_BANK', '张三', '010-66194114', 'dev@example.com', '北京市西城区成方街32号', 'http://www.pbc.gov.cn', '中央银行，负责货币政策', 1, 1, 1, NULL, 0, 'admin', SYSDATE, '央行'),
('AUTH002', 'CBIRC', '中国银保监会', 'REGULATOR', '李四', '010-66279113', 'dev@example.com', '北京市西城区金融大街甲15号', 'http://www.cbirc.gov.cn', '银行保险监管机构', 1, 1, 2, NULL, 0, 'admin', SYSDATE, '银保监'),
('AUTH003', 'CSRC', '中国证监会', 'REGULATOR', '王五', '010-88061000', 'dev@example.com', '北京市西城区金融大街19号', 'http://www.csrc.gov.cn', '证券期货监管机构', 1, 1, 3, NULL, 0, 'admin', SYSDATE, '证监会'),
('AUTH004', 'SAFE', '国家外汇管理局', 'REGULATOR', '赵六', '010-68402265', 'dev@example.com', '北京市海淀区阜成路18号', 'http://www.safe.gov.cn', '外汇管理机构', 1, 1, 4, NULL, 0, 'admin', SYSDATE, '外管局'),
('AUTH005', 'MOF', '财政部', 'GOVERNMENT', '钱七', '010-68551114', 'dev@example.com', '北京市西城区三里河南三巷3号', 'http://www.mof.gov.cn', '财政管理部门', 1, 0, 5, NULL, 0, 'admin', SYSDATE, '财政部'),
('AUTH006', 'SAT', '国家税务总局', 'GOVERNMENT', '孙八', '010-63417114', 'dev@example.com', '北京市海淀区羊坊店西路5号', 'http://www.chinatax.gov.cn', '税务管理机构', 1, 0, 6, NULL, 0, 'admin', SYSDATE, '税务局'),
('AUTH007', 'NDRC', '国家发改委', 'GOVERNMENT', '周九', '010-68502000', 'dev@example.com', '北京市西城区月坛南街38号', 'http://www.ndrc.gov.cn', '发展改革部门', 1, 0, 7, NULL, 0, 'admin', SYSDATE, '发改委'),
('AUTH008', 'SAMR', '市场监管总局', 'GOVERNMENT', '吴十', '010-88650000', 'dev@example.com', '北京市西城区三里河东路8号', 'http://www.samr.gov.cn', '市场监管机构', 1, 0, 8, NULL, 0, 'admin', SYSDATE, '市监局'),
('AUTH009', 'LOCAL_PBOC', '人民银行上海分行', 'LOCAL_BRANCH', '郑一', '021-63261000', 'dev@example.com', '上海市浦东新区陆家嘴东路181号', NULL, '央行上海分行', 1, 0, 9, NULL, 0, 'admin', SYSDATE, '上海分行'),
('AUTH010', 'LOCAL_CBIRC', '银保监会上海局', 'LOCAL_BRANCH', '冯二', '021-58768888', 'dev@example.com', '上海市浦东新区银城中路8号', NULL, '银保监上海局', 1, 0, 10, NULL, 0, 'admin', SYSDATE, '上海局');

-- 2. 报告模板表 TBL_REPORT_TEMPLATE
INSERT INTO TBL_REPORT_TEMPLATE (TEMPLATE_ID, TEMPLATE_CODE, TEMPLATE_NAME, AUTHORITY_ID, TEMPLATE_TYPE, TEMPLATE_VERSION, TEMPLATE_CONTENT, TEMPLATE_FILE_PATH, EFFECTIVE_DATE, EXPIRY_DATE, IS_ENABLED, REPORT_FREQUENCY, DESCRIPTION, COMPANY_ID, DELETE_FLAG, CREATED_BY, CREATED_TIME, REMARK) VALUES
('TPL001', 'PBOC_CASH_DAILY', '现金流量日报', 'AUTH001', 'CASH_FLOW', 'V1.0', NULL, '/templates/cash_daily.xlsx', SYSDATE-365, SYSDATE+365, 1, 'DAILY', '每日现金流量报表', NULL, 0, 'admin', SYSDATE, '日报'),
('TPL002', 'PBOC_CASH_MONTHLY', '现金流量月报', 'AUTH001', 'CASH_FLOW', 'V1.0', NULL, '/templates/cash_monthly.xlsx', SYSDATE-365, SYSDATE+365, 1, 'MONTHLY', '每月现金流量报表', NULL, 0, 'admin', SYSDATE, '月报'),
('TPL003', 'CBIRC_RISK_QUARTERLY', '风险监测季报', 'AUTH002', 'RISK', 'V2.0', NULL, '/templates/risk_quarterly.xlsx', SYSDATE-365, SYSDATE+365, 1, 'QUARTERLY', '季度风险监测报表', NULL, 0, 'admin', SYSDATE, '季报'),
('TPL004', 'SAFE_FX_MONTHLY', '外汇收支月报', 'AUTH004', 'FOREX', 'V1.5', NULL, '/templates/fx_monthly.xlsx', SYSDATE-365, SYSDATE+365, 1, 'MONTHLY', '每月外汇收支报表', NULL, 0, 'admin', SYSDATE, '外汇月报'),
('TPL005', 'PBOC_LOAN_QUARTERLY', '贷款统计季报', 'AUTH001', 'LOAN', 'V1.0', NULL, '/templates/loan_quarterly.xlsx', SYSDATE-365, SYSDATE+365, 1, 'QUARTERLY', '季度贷款统计报表', NULL, 0, 'admin', SYSDATE, '贷款季报'),
('TPL006', 'CBIRC_CAPITAL_MONTHLY', '资本充足率月报', 'AUTH002', 'CAPITAL', 'V3.0', NULL, '/templates/capital_monthly.xlsx', SYSDATE-365, SYSDATE+365, 1, 'MONTHLY', '每月资本充足率报表', NULL, 0, 'admin', SYSDATE, '资本月报'),
('TPL007', 'SAT_TAX_MONTHLY', '税务申报月报', 'AUTH006', 'TAX', 'V1.0', NULL, '/templates/tax_monthly.xlsx', SYSDATE-365, SYSDATE+365, 1, 'MONTHLY', '每月税务申报报表', NULL, 0, 'admin', SYSDATE, '税务月报'),
('TPL008', 'MOF_FINANCE_ANNUAL', '财务年报', 'AUTH005', 'FINANCE', 'V2.0', NULL, '/templates/finance_annual.xlsx', SYSDATE-365, SYSDATE+365, 1, 'ANNUAL', '年度财务报表', NULL, 0, 'admin', SYSDATE, '财务年报'),
('TPL009', 'PBOC_PAYMENT_DAILY', '支付结算日报', 'AUTH001', 'PAYMENT', 'V1.0', NULL, '/templates/payment_daily.xlsx', SYSDATE-365, SYSDATE+365, 1, 'DAILY', '每日支付结算报表', NULL, 0, 'admin', SYSDATE, '支付日报'),
('TPL010', 'CSRC_INVEST_QUARTERLY', '投资情况季报', 'AUTH003', 'INVESTMENT', 'V1.0', NULL, '/templates/invest_quarterly.xlsx', SYSDATE-365, SYSDATE+365, 1, 'QUARTERLY', '季度投资情况报表', NULL, 0, 'admin', SYSDATE, '投资季报');

-- 3. 监管报告表 TBL_REGULATORY_REPORT
INSERT INTO TBL_REGULATORY_REPORT (REPORT_ID, REPORT_NO, REPORT_NAME, TEMPLATE_ID, AUTHORITY_ID, REPORT_PERIOD, REPORT_DATE, DUE_DATE, SUBMIT_DATE, REPORT_STATUS, REPORT_CONTENT, REPORT_FILE_PATH, SUBMISSION_METHOD, ACKNOWLEDGMENT_NO, REJECT_REASON, GENERATED_TIME, VALIDATED_TIME, COMPANY_ID, DELETE_FLAG, CREATED_BY, CREATED_TIME, REMARK) VALUES
('RPT001', 'RPT-2026-001', '2026年1月现金流量日报', 'TPL001', 'AUTH001', '2026-01', SYSDATE, SYSDATE+7, NULL, 'DRAFT', NULL, NULL, 'ONLINE', NULL, NULL, NULL, NULL, NULL, 0, 'admin', SYSDATE, '待提交'),
('RPT002', 'RPT-2026-002', '2025年12月现金流量月报', 'TPL002', 'AUTH001', '2025-12', SYSDATE-30, SYSDATE-15, SYSDATE-20, 'SUBMITTED', NULL, '/reports/cash_202512.pdf', 'ONLINE', 'ACK-20260101-001', NULL, SYSDATE-25, SYSDATE-22, NULL, 0, 'admin', SYSDATE, '已提交'),
('RPT003', 'RPT-2026-003', '2025年Q4风险监测季报', 'TPL003', 'AUTH002', '2025-Q4', SYSDATE-45, SYSDATE-30, SYSDATE-35, 'ACCEPTED', NULL, '/reports/risk_2025Q4.pdf', 'ONLINE', 'ACK-20260105-002', NULL, SYSDATE-50, SYSDATE-48, NULL, 0, 'admin', SYSDATE, '已通过'),
('RPT004', 'RPT-2026-004', '2025年12月外汇收支月报', 'TPL004', 'AUTH004', '2025-12', SYSDATE-30, SYSDATE-10, SYSDATE-15, 'REJECTED', NULL, '/reports/fx_202512.pdf', 'ONLINE', NULL, '数据格式不符合要求', SYSDATE-20, SYSDATE-18, NULL, 0, 'admin', SYSDATE, '被退回'),
('RPT005', 'RPT-2026-005', '2025年Q4贷款统计季报', 'TPL005', 'AUTH001', '2025-Q4', SYSDATE-45, SYSDATE-20, NULL, 'VALIDATED', NULL, '/reports/loan_2025Q4.pdf', 'ONLINE', NULL, NULL, SYSDATE-30, SYSDATE-25, NULL, 0, 'admin', SYSDATE, '已验证'),
('RPT006', 'RPT-2026-006', '2026年1月资本充足率月报', 'TPL006', 'AUTH002', '2026-01', SYSDATE, SYSDATE+15, NULL, 'GENERATED', NULL, '/reports/capital_202601.pdf', 'ONLINE', NULL, NULL, SYSDATE-2, NULL, NULL, 0, 'admin', SYSDATE, '已生成'),
('RPT007', 'RPT-2026-007', '2026年1月税务申报月报', 'TPL007', 'AUTH006', '2026-01', SYSDATE, SYSDATE+10, NULL, 'DRAFT', NULL, NULL, 'OFFLINE', NULL, NULL, NULL, NULL, NULL, 0, 'admin', SYSDATE, '草稿'),
('RPT008', 'RPT-2026-008', '2025年度财务年报', 'TPL008', 'AUTH005', '2025', SYSDATE-30, SYSDATE+60, NULL, 'DRAFT', NULL, NULL, 'ONLINE', NULL, NULL, NULL, NULL, NULL, 0, 'admin', SYSDATE, '年报草稿'),
('RPT009', 'RPT-2026-009', '2026年1月支付结算日报', 'TPL009', 'AUTH001', '2026-01-22', SYSDATE, SYSDATE+1, NULL, 'GENERATED', NULL, '/reports/payment_20260122.pdf', 'ONLINE', NULL, NULL, SYSDATE, NULL, NULL, 0, 'admin', SYSDATE, '今日日报'),
('RPT010', 'RPT-2026-010', '2025年Q4投资情况季报', 'TPL010', 'AUTH003', '2025-Q4', SYSDATE-45, SYSDATE-25, SYSDATE-28, 'ACCEPTED', NULL, '/reports/invest_2025Q4.pdf', 'ONLINE', 'ACK-20260110-003', NULL, SYSDATE-35, SYSDATE-32, NULL, 0, 'admin', SYSDATE, '已通过');

-- 4. 合规检查规则表 TBL_COMPLIANCE_RULE
INSERT INTO TBL_COMPLIANCE_RULE (RULE_ID, RULE_CODE, RULE_NAME, RULE_TYPE, AUTHORITY_ID, RULE_EXPRESSION, SEVERITY_LEVEL, IS_ENABLED, EFFECTIVE_DATE, EXPIRY_DATE, DESCRIPTION, COMPANY_ID, DELETE_FLAG, CREATED_BY, CREATED_TIME, REMARK) VALUES
('RULE001', 'RULE_CASH_BALANCE', '现金余额校验', 'DATA_VALIDATION', 'AUTH001', 'cash_balance >= 0', 'HIGH', 1, SYSDATE-365, SYSDATE+365, '现金余额不能为负数', NULL, 0, 'admin', SYSDATE, '基础校验'),
('RULE002', 'RULE_AMOUNT_MATCH', '金额勾稽校验', 'DATA_VALIDATION', 'AUTH001', 'total = sum(details)', 'HIGH', 1, SYSDATE-365, SYSDATE+365, '汇总金额与明细合计必须一致', NULL, 0, 'admin', SYSDATE, '勾稽校验'),
('RULE003', 'RULE_DATE_RANGE', '日期范围校验', 'FORMAT_CHECK', 'AUTH001', 'report_date within period', 'MEDIUM', 1, SYSDATE-365, SYSDATE+365, '报告日期必须在报告期间内', NULL, 0, 'admin', SYSDATE, '日期校验'),
('RULE004', 'RULE_REQUIRED_FIELD', '必填字段校验', 'FORMAT_CHECK', 'AUTH002', 'required_fields not null', 'HIGH', 1, SYSDATE-365, SYSDATE+365, '必填字段不能为空', NULL, 0, 'admin', SYSDATE, '必填校验'),
('RULE005', 'RULE_FX_RATE', '汇率合理性校验', 'BUSINESS_RULE', 'AUTH004', 'fx_rate within 10% of market', 'MEDIUM', 1, SYSDATE-365, SYSDATE+365, '汇率偏离市场价不超过10%', NULL, 0, 'admin', SYSDATE, '汇率校验'),
('RULE006', 'RULE_CAPITAL_RATIO', '资本充足率校验', 'BUSINESS_RULE', 'AUTH002', 'capital_ratio >= 8%', 'HIGH', 1, SYSDATE-365, SYSDATE+365, '资本充足率不低于8%', NULL, 0, 'admin', SYSDATE, '资本校验'),
('RULE007', 'RULE_LOAN_LIMIT', '贷款集中度校验', 'BUSINESS_RULE', 'AUTH002', 'single_loan <= 10% of capital', 'HIGH', 1, SYSDATE-365, SYSDATE+365, '单一贷款不超过资本10%', NULL, 0, 'admin', SYSDATE, '集中度校验'),
('RULE008', 'RULE_TAX_CALC', '税额计算校验', 'CALCULATION', 'AUTH006', 'tax = taxable_amount * rate', 'MEDIUM', 1, SYSDATE-365, SYSDATE+365, '税额计算正确性校验', NULL, 0, 'admin', SYSDATE, '税额校验'),
('RULE009', 'RULE_DUPLICATE_CHECK', '重复数据校验', 'DATA_VALIDATION', 'AUTH001', 'no duplicate records', 'LOW', 1, SYSDATE-365, SYSDATE+365, '不允许重复提交数据', NULL, 0, 'admin', SYSDATE, '去重校验'),
('RULE010', 'RULE_SIGN_CHECK', '数据签名校验', 'SECURITY', 'AUTH001', 'valid digital signature', 'HIGH', 1, SYSDATE-365, SYSDATE+365, '数据必须有有效数字签名', NULL, 0, 'admin', SYSDATE, '签名校验');

-- 5. 合规检查结果表 TBL_COMPLIANCE_RESULT
INSERT INTO TBL_COMPLIANCE_RESULT (RESULT_ID, RULE_ID, REPORT_ID, CHECK_TIME, CHECK_STATUS, IS_PASSED, VIOLATION_DETAILS, SEVERITY_LEVEL, ACTION_TAKEN, RESOLVED_TIME, IS_ESCALATED, COMPANY_ID, DELETE_FLAG, CREATED_BY, CREATED_TIME, REMARK) VALUES
('RES001', 'RULE001', 'RPT001', SYSDATE-1, 'COMPLETED', 1, NULL, 'HIGH', NULL, NULL, 0, NULL, 0, 'system', SYSDATE, '校验通过'),
('RES002', 'RULE002', 'RPT001', SYSDATE-1, 'COMPLETED', 1, NULL, 'HIGH', NULL, NULL, 0, NULL, 0, 'system', SYSDATE, '校验通过'),
('RES003', 'RULE003', 'RPT002', SYSDATE-20, 'COMPLETED', 1, NULL, 'MEDIUM', NULL, NULL, 0, NULL, 0, 'system', SYSDATE, '校验通过'),
('RES004', 'RULE004', 'RPT004', SYSDATE-15, 'COMPLETED', 0, '字段"交易对手"为空', 'HIGH', '补充完善数据', NULL, 1, NULL, 0, 'system', SYSDATE, '校验失败'),
('RES005', 'RULE005', 'RPT004', SYSDATE-15, 'COMPLETED', 0, '汇率偏离市场价15%', 'MEDIUM', '核实汇率数据', SYSDATE-10, 0, NULL, 0, 'system', SYSDATE, '已修正'),
('RES006', 'RULE006', 'RPT003', SYSDATE-48, 'COMPLETED', 1, NULL, 'HIGH', NULL, NULL, 0, NULL, 0, 'system', SYSDATE, '校验通过'),
('RES007', 'RULE007', 'RPT005', SYSDATE-25, 'COMPLETED', 1, NULL, 'HIGH', NULL, NULL, 0, NULL, 0, 'system', SYSDATE, '校验通过'),
('RES008', 'RULE008', 'RPT007', SYSDATE, 'PENDING', NULL, NULL, 'MEDIUM', NULL, NULL, 0, NULL, 0, 'system', SYSDATE, '待校验'),
('RES009', 'RULE001', 'RPT006', SYSDATE-2, 'COMPLETED', 1, NULL, 'HIGH', NULL, NULL, 0, NULL, 0, 'system', SYSDATE, '校验通过'),
('RES010', 'RULE009', 'RPT009', SYSDATE, 'COMPLETED', 1, NULL, 'LOW', NULL, NULL, 0, NULL, 0, 'system', SYSDATE, '校验通过');

-- 6. 报送任务表 TBL_SUBMISSION_TASK
INSERT INTO TBL_SUBMISSION_TASK (TASK_ID, TASK_CODE, TASK_NAME, REPORT_ID, AUTHORITY_ID, TASK_TYPE, TASK_STATUS, SCHEDULED_TIME, EXECUTED_TIME, COMPLETED_TIME, RETRY_COUNT, MAX_RETRIES, ERROR_MESSAGE, COMPANY_ID, DELETE_FLAG, CREATED_BY, CREATED_TIME, REMARK) VALUES
('TASK001', 'TASK-2026-001', '提交现金流量日报', 'RPT001', 'AUTH001', 'SUBMIT', 'PENDING', SYSDATE+1, NULL, NULL, 0, 3, NULL, NULL, 0, 'admin', SYSDATE, '待执行'),
('TASK002', 'TASK-2026-002', '提交现金流量月报', 'RPT002', 'AUTH001', 'SUBMIT', 'COMPLETED', SYSDATE-20, SYSDATE-20, SYSDATE-20, 0, 3, NULL, NULL, 0, 'admin', SYSDATE, '已完成'),
('TASK003', 'TASK-2026-003', '提交风险监测季报', 'RPT003', 'AUTH002', 'SUBMIT', 'COMPLETED', SYSDATE-35, SYSDATE-35, SYSDATE-35, 0, 3, NULL, NULL, 0, 'admin', SYSDATE, '已完成'),
('TASK004', 'TASK-2026-004', '重新提交外汇月报', 'RPT004', 'AUTH004', 'RESUBMIT', 'PENDING', SYSDATE+2, NULL, NULL, 1, 3, '首次提交被退回', NULL, 0, 'admin', SYSDATE, '重新提交'),
('TASK005', 'TASK-2026-005', '提交贷款统计季报', 'RPT005', 'AUTH001', 'SUBMIT', 'RUNNING', SYSDATE, SYSDATE, NULL, 0, 3, NULL, NULL, 0, 'admin', SYSDATE, '执行中'),
('TASK006', 'TASK-2026-006', '验证资本充足率月报', 'RPT006', 'AUTH002', 'VALIDATE', 'PENDING', SYSDATE+1, NULL, NULL, 0, 3, NULL, NULL, 0, 'admin', SYSDATE, '待验证'),
('TASK007', 'TASK-2026-007', '生成税务申报月报', 'RPT007', 'AUTH006', 'GENERATE', 'PENDING', SYSDATE, NULL, NULL, 0, 3, NULL, NULL, 0, 'admin', SYSDATE, '待生成'),
('TASK008', 'TASK-2026-008', '提交财务年报', 'RPT008', 'AUTH005', 'SUBMIT', 'PAUSED', SYSDATE+30, NULL, NULL, 0, 3, '等待审批', NULL, 0, 'admin', SYSDATE, '已暂停'),
('TASK009', 'TASK-2026-009', '提交支付结算日报', 'RPT009', 'AUTH001', 'SUBMIT', 'PENDING', SYSDATE+1, NULL, NULL, 0, 3, NULL, NULL, 0, 'admin', SYSDATE, '待执行'),
('TASK010', 'TASK-2026-010', '归档投资情况季报', 'RPT010', 'AUTH003', 'ARCHIVE', 'COMPLETED', SYSDATE-25, SYSDATE-25, SYSDATE-25, 0, 3, NULL, NULL, 0, 'admin', SYSDATE, '已归档');

-- 7. 监管通知表 TBL_REGULATORY_NOTIFICATION
INSERT INTO TBL_REGULATORY_NOTIFICATION (NOTIFICATION_ID, NOTIFICATION_CODE, NOTIFICATION_TITLE, NOTIFICATION_TYPE, AUTHORITY_ID, NOTIFICATION_CONTENT, PRIORITY_LEVEL, SEND_STATUS, SEND_TIME, ACKNOWLEDGE_TIME, RECIPIENTS, COMPANY_ID, DELETE_FLAG, CREATED_BY, CREATED_TIME, REMARK) VALUES
('NOTIF001', 'NOTIF-2026-001', '关于调整现金流量报表格式的通知', 'POLICY', 'AUTH001', '根据最新监管要求，现金流量报表格式将于2026年2月1日起调整...', 'HIGH', 'SENT', SYSDATE-10, SYSDATE-9, 'dev@example.com', NULL, 0, 'admin', SYSDATE, '政策通知'),
('NOTIF002', 'NOTIF-2026-002', '2026年度报送时间安排', 'SCHEDULE', 'AUTH001', '现将2026年度各类报表报送时间安排通知如下...', 'NORMAL', 'SENT', SYSDATE-30, SYSDATE-28, 'dev@example.com', NULL, 0, 'admin', SYSDATE, '时间安排'),
('NOTIF003', 'NOTIF-2026-003', '外汇月报退回通知', 'REJECTION', 'AUTH004', '您提交的2025年12月外汇收支月报因数据格式问题被退回，请修正后重新提交...', 'HIGH', 'SENT', SYSDATE-15, SYSDATE-14, 'dev@example.com', NULL, 0, 'admin', SYSDATE, '退回通知'),
('NOTIF004', 'NOTIF-2026-004', '风险监测季报已通过审核', 'APPROVAL', 'AUTH002', '您提交的2025年Q4风险监测季报已通过审核...', 'NORMAL', 'SENT', SYSDATE-30, SYSDATE-29, 'dev@example.com', NULL, 0, 'admin', SYSDATE, '通过通知'),
('NOTIF005', 'NOTIF-2026-005', '系统维护通知', 'SYSTEM', 'AUTH001', '监管报送系统将于2026年1月25日进行维护升级...', 'NORMAL', 'DRAFT', NULL, NULL, 'dev@example.com', NULL, 0, 'admin', SYSDATE, '系统通知'),
('NOTIF006', 'NOTIF-2026-006', '报表提交截止提醒', 'REMINDER', 'AUTH001', '您有1份报表即将到期，请及时提交...', 'HIGH', 'SENT', SYSDATE-1, NULL, 'dev@example.com', NULL, 0, 'admin', SYSDATE, '提醒通知'),
('NOTIF007', 'NOTIF-2026-007', '新增报表模板通知', 'POLICY', 'AUTH002', '根据监管要求，新增资本充足率补充报表模板...', 'NORMAL', 'SENT', SYSDATE-20, SYSDATE-18, 'dev@example.com', NULL, 0, 'admin', SYSDATE, '模板通知'),
('NOTIF008', 'NOTIF-2026-008', '数据质量检查结果', 'QUALITY', 'AUTH001', '您提交的报表数据质量检查结果如下...', 'NORMAL', 'SENT', SYSDATE-5, SYSDATE-4, 'dev@example.com', NULL, 0, 'admin', SYSDATE, '质量通知'),
('NOTIF009', 'NOTIF-2026-009', '监管政策解读培训', 'TRAINING', 'AUTH002', '定于2026年2月举办监管政策解读培训...', 'LOW', 'SENT', SYSDATE-15, NULL, 'dev@example.com', NULL, 0, 'admin', SYSDATE, '培训通知'),
('NOTIF010', 'NOTIF-2026-010', '年度报送工作总结', 'SUMMARY', 'AUTH001', '2025年度监管报送工作总结如下...', 'LOW', 'DRAFT', NULL, NULL, 'dev@example.com', NULL, 0, 'admin', SYSDATE, '总结通知');

-- 8. 监管数据字典表 TBL_REGULATORY_DICTIONARY
INSERT INTO TBL_REGULATORY_DICTIONARY (DICTIONARY_ID, DICT_CODE, DICT_NAME, DICT_TYPE, DICT_VALUE, PARENT_ID, SORT_ORDER, IS_ENABLED, DESCRIPTION, COMPANY_ID, DELETE_FLAG, CREATED_BY, CREATED_TIME, REMARK) VALUES
('DICT001', 'REPORT_STATUS_DRAFT', '草稿', 'REPORT_STATUS', 'DRAFT', NULL, 1, 1, '报告草稿状态', NULL, 0, 'admin', SYSDATE, '状态'),
('DICT002', 'REPORT_STATUS_GENERATED', '已生成', 'REPORT_STATUS', 'GENERATED', NULL, 2, 1, '报告已生成', NULL, 0, 'admin', SYSDATE, '状态'),
('DICT003', 'REPORT_STATUS_VALIDATED', '已验证', 'REPORT_STATUS', 'VALIDATED', NULL, 3, 1, '报告已验证', NULL, 0, 'admin', SYSDATE, '状态'),
('DICT004', 'REPORT_STATUS_SUBMITTED', '已提交', 'REPORT_STATUS', 'SUBMITTED', NULL, 4, 1, '报告已提交', NULL, 0, 'admin', SYSDATE, '状态'),
('DICT005', 'REPORT_STATUS_ACCEPTED', '已通过', 'REPORT_STATUS', 'ACCEPTED', NULL, 5, 1, '报告已通过', NULL, 0, 'admin', SYSDATE, '状态'),
('DICT006', 'REPORT_STATUS_REJECTED', '已退回', 'REPORT_STATUS', 'REJECTED', NULL, 6, 1, '报告已退回', NULL, 0, 'admin', SYSDATE, '状态'),
('DICT007', 'FREQUENCY_DAILY', '日报', 'REPORT_FREQUENCY', 'DAILY', NULL, 1, 1, '每日报送', NULL, 0, 'admin', SYSDATE, '频率'),
('DICT008', 'FREQUENCY_MONTHLY', '月报', 'REPORT_FREQUENCY', 'MONTHLY', NULL, 2, 1, '每月报送', NULL, 0, 'admin', SYSDATE, '频率'),
('DICT009', 'FREQUENCY_QUARTERLY', '季报', 'REPORT_FREQUENCY', 'QUARTERLY', NULL, 3, 1, '每季报送', NULL, 0, 'admin', SYSDATE, '频率'),
('DICT010', 'FREQUENCY_ANNUAL', '年报', 'REPORT_FREQUENCY', 'ANNUAL', NULL, 4, 1, '每年报送', NULL, 0, 'admin', SYSDATE, '频率');

-- 提交事务
COMMIT;

-- =====================================================
-- 测试数据插入完成
-- 共插入 8 个表，每表 10 条数据，合计 80 条记录
-- =====================================================

