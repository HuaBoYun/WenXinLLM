-- =============================================
-- 电票账户配置表 - 测试数据插入脚本
-- 表名: TC_ETICKET_ACCOUNT
-- 描述: 插入多场景测试数据,用于前端页面功能测试
-- 作者: 华博云开发团队
-- 创建时间: 2024-12-24
-- 兼容性: 达梦数据库 (DM Database)
-- =============================================

-- 清空现有测试数据(可选)
-- DELETE FROM TC_ETICKET_ACCOUNT;
-- COMMIT;

-- =============================================
-- 测试数据说明:
-- 1. 覆盖所有电票系统类型: ECDS, BECP, BANK_ETICKET
-- 2. 覆盖所有账户类型: ACCEPTANCE, DISCOUNT, REDISCOUNT, PLEDGE, CUSTODY, MARGIN
-- 3. 覆盖所有主流银行: ICBC, CCB, ABC, BOC, BOCOM, CMB
-- 4. 包含不同账户状态: NORMAL, FROZEN, CLOSED
-- 5. 包含不同的余额和授信额度组合
-- =============================================

INSERT INTO TC_ETICKET_ACCOUNT (
    ETICKET_ACCOUNT_ID, ACCOUNT_NUMBER, ACCOUNT_NAME, ETICKET_SYSTEM, ACCOUNT_TYPE,
    BANK_NAME, BRANCH_NAME, BANK_CODE, BANK_ACCOUNT_NUMBER,
    ACCOUNT_BALANCE, CREDIT_LIMIT, OPEN_DATE, ACCOUNT_STATUS, IS_ACTIVE,
    CONTACT_PERSON, CONTACT_PHONE, CONTACT_EMAIL,
    REMARK, DESCRIPTION, SYNC_STATUS, ORG_ID,
    CREATE_TIME, UPDATE_TIME, CREATE_USER, UPDATE_USER
) VALUES
-- =============================================
-- ECDS系统测试数据 (3条)
-- =============================================
(
    1001,
    'ETK2024ECDS001',
    '工商银行ECDS承兑账户-北京总部',
    'ECDS',
    'ACCEPTANCE',
    '中国工商银行',
    '北京分行营业部',
    'ICBC',
    '6222020200001234567',
    1500000.00,
    3000.00,
    TO_DATE('2024-01-15', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '张经理',
    '13800138000',
    'dev@example.com',
    'ECDS系统主承兑账户',
    '用于ECDS系统的承兑业务,北京地区总部账户',
    'SUCCESS',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),
(
    1002,
    'ETK2024ECDS002',
    '建设银行ECDS贴现账户-上海分部',
    'ECDS',
    'DISCOUNT',
    '中国建设银行',
    '上海浦东分行',
    'CCB',
    '6217000012345678901',
    2800000.00,
    5000.00,
    TO_DATE('2024-02-20', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '李主管',
    '13800138000',
    'dev@example.com',
    'ECDS贴现主账户',
    '专门用于ECDS票据贴现业务',
    'SUCCESS',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),
(
    1003,
    'ETK2024ECDS003',
    '农业银行ECDS质押账户-广州营业部',
    'ECDS',
    'PLEDGE',
    '中国农业银行',
    '广州天河支行',
    'ABC',
    '6228480123456789012',
    950000.00,
    2000.00,
    TO_DATE('2024-03-10', 'YYYY-MM-DD'),
    'FROZEN',
    0,
    '王主任',
    '13800138000',
    'dev@example.com',
    'ECDS质押账户-暂时冻结',
    '因审计需要暂时冻结的质押账户',
    'FAILED',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),

-- =============================================
-- BECP系统测试数据 (3条)
-- =============================================
(
    2001,
    'ETK2024BECP001',
    '中国银行BECP转贴现账户-深圳分行',
    'BECP',
    'REDISCOUNT',
    '中国银行',
    '深圳福田分行',
    'BOC',
    '621661000012345678',
    3200000.00,
    6000.00,
    TO_DATE('2024-04-05', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '赵总监',
    '13800138000',
    'dev@example.com',
    'BECP转贴现专用账户',
    '用于BECP系统的转贴现业务',
    'SUCCESS',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),
(
    2002,
    'ETK2024BECP002',
    '交通银行BECP托管账户-杭州西湖支行',
    'BECP',
    'CUSTODY',
    '交通银行',
    '杭州西湖支行',
    'BOCOM',
    '622252000012345678',
    5800000.00,
    10000.00,
    TO_DATE('2024-05-12', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '孙助理',
    '13800138000',
    'dev@example.com',
    'BECP资金托管账户',
    '用于资金托管的专用账户',
    'SUCCESS',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),
(
    2003,
    'ETK2024BECP003',
    '招商银行BECP保证金账户-成都高新支行',
    'BECP',
    'MARGIN',
    '招商银行',
    '成都高新支行',
    'CMB',
    '621483012345678901',
    2100000.00,
    4000.00,
    TO_DATE('2024-06-18', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '周经理',
    '13800138000',
    'dev@example.com',
    'BECP保证金账户',
    '用于存放BECP业务保证金',
    'PENDING',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),

-- =============================================
-- BANK_ETICKET系统测试数据 (4条)
-- =============================================
(
    3001,
    'ETK2024BANK001',
    '工商银行电票承兑账户-南京分行',
    'BANK_ETICKET',
    'ACCEPTANCE',
    '中国工商银行',
    '南京鼓楼支行',
    'ICBC',
    '6222020200002345678',
    1800000.00,
    3500.00,
    TO_DATE('2024-07-01', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '吴主管',
    '13800138000',
    'dev@example.com',
    '银行电票系统承兑账户',
    '工商银行南京分行专用承兑账户',
    'SUCCESS',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),
(
    3002,
    'ETK2024BANK002',
    '建设银行电票贴现账户-武汉江汉支行',
    'BANK_ETICKET',
    'DISCOUNT',
    '中国建设银行',
    '武汉江汉支行',
    'CCB',
    '6217000012346789012',
    3500000.00,
    7000.00,
    TO_DATE('2024-07-15', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '郑主任',
    '13800138000',
    'dev@example.com',
    '银行电票贴现主账户',
    '武汉地区最大的贴现账户',
    'SUCCESS',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),
(
    3003,
    'ETK2024BANK003',
    '农业银行电票质押账户-西安雁塔支行',
    'BANK_ETICKET',
    'PLEDGE',
    '中国农业银行',
    '西安雁塔支行',
    'ABC',
    '6228480123457890123',
    1200000.00,
    2500.00,
    TO_DATE('2024-08-20', 'YYYY-MM-DD'),
    'CLOSED',
    0,
    '冯经理',
    '13800138000',
    'dev@example.com',
    '已关闭的质押账户',
    '业务调整已关闭',
    'FAILED',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),
(
    3004,
    'ETK2024BANK004',
    '中国银行电票转贴现账户-重庆渝北支行',
    'BANK_ETICKET',
    'REDISCOUNT',
    '中国银行',
    '重庆渝北支行',
    'BOC',
    '621661000023456789',
    4500000.00,
    9000.00,
    TO_DATE('2024-09-10', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '陈总监',
    '13800138000',
    'dev@example.com',
    '西南地区转贴现中心账户',
    '负责西南地区的转贴现业务',
    'SUCCESS',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),

-- =============================================
-- 补充测试数据 - 多场景覆盖 (5条)
-- =============================================
(
    4001,
    'ETK2024MIX001',
    '交通银行混合业务账户-苏州工业园区',
    'ECDS',
    'ACCEPTANCE',
    '交通银行',
    '苏州工业园区支行',
    'BOCOM',
    '622252000023456789',
    2600000.00,
    5500.00,
    TO_DATE('2024-10-01', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '褚助理',
    '13800138000',
    'dev@example.com',
    '长三角区域重点账户',
    '服务长三角地区业务',
    'SUCCESS',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),
(
    4002,
    'ETK2024MIX002',
    '招商银行承兑贴现一体账户-宁波分行',
    'BECP',
    'ACCEPTANCE',
    '招商银行',
    '宁波分行营业部',
    'CMB',
    '621483012345789012',
    4100000.00,
    8500.00,
    TO_DATE('2024-10-15', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '卫主管',
    '13800138000',
    'dev@example.com',
    '承兑贴现一体化账户',
    '支持承兑和贴现双业务',
    'SUCCESS',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),
(
    4003,
    'ETK2024MIX003',
    '工商银行大额质押账户-天津滨海支行',
    'BANK_ETICKET',
    'PLEDGE',
    '中国工商银行',
    '天津滨海支行',
    'ICBC',
    '6222020200003456789',
    8900000.00,
    15000.00,
    TO_DATE('2024-11-01', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '蒋经理',
    '13800138000',
    'dev@example.com',
    '环渤海区域大额质押账户',
    '重点服务大型企业客户',
    'PENDING',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),
(
    4004,
    'ETK2024MIX004',
    '建设银行测试账户-开发环境专用',
    'ECDS',
    'DISCOUNT',
    '中国建设银行',
    '北京开发测试支行',
    'CCB',
    '6217000012347890123',
    50000.00,
    100.00,
    TO_DATE('2024-11-20', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '沈工程师',
    '13800138000',
    'dev@example.com',
    '测试环境专用账户',
    '仅用于开发和测试,不参与实际业务',
    'SUCCESS',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
),
(
    4005,
    'ETK2024MIX005',
    '中国银行待激活账户-郑州分行',
    'BECP',
    'CUSTODY',
    '中国银行',
    '郑州东区支行',
    'BOC',
    '621661000034567890',
    750000.00,
    1500.00,
    TO_DATE('2024-12-01', 'YYYY-MM-DD'),
    'NORMAL',
    1,
    '韩主任',
    '13800138000',
    'dev@example.com',
    '新开户待激活',
    '刚完成开户,等待激活使用',
    'PENDING',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'admin',
    'admin'
);

-- 提交事务
COMMIT;

-- =============================================
-- 数据统计查询
-- =============================================

-- 查询插入的数据总数
-- SELECT COUNT(*) AS "总记录数" FROM TC_ETICKET_ACCOUNT;

-- 按电票系统统计
-- SELECT ETICKET_SYSTEM AS "电票系统", COUNT(*) AS "账户数量"
-- FROM TC_ETICKET_ACCOUNT
-- GROUP BY ETICKET_SYSTEM
-- ORDER BY COUNT(*) DESC;

-- 按账户类型统计
-- SELECT ACCOUNT_TYPE AS "账户类型", COUNT(*) AS "账户数量",
--        SUM(ACCOUNT_BALANCE) AS "总余额", SUM(CREDIT_LIMIT) AS "总授信额度"
-- FROM TC_ETICKET_ACCOUNT
-- GROUP BY ACCOUNT_TYPE
-- ORDER BY COUNT(*) DESC;

-- 按银行统计
-- SELECT BANK_NAME AS "开户银行", BANK_CODE AS "银行编码", COUNT(*) AS "账户数量",
--        SUM(ACCOUNT_BALANCE) AS "总余额", SUM(CREDIT_LIMIT) AS "总授信额度"
-- FROM TC_ETICKET_ACCOUNT
-- GROUP BY BANK_NAME, BANK_CODE
-- ORDER BY COUNT(*) DESC;

-- 按账户状态统计
-- SELECT ACCOUNT_STATUS AS "账户状态", COUNT(*) AS "账户数量"
-- FROM TC_ETICKET_ACCOUNT
-- GROUP BY ACCOUNT_STATUS
-- ORDER BY COUNT(*) DESC;

-- =============================================
-- 测试场景说明
-- =============================================
-- 1. 基础功能测试: 可以测试新增、编辑、删除、查询功能
-- 2. 搜索功能测试: 可以按账户编号、账户名称、电票系统、账户类型、银行编码搜索
-- 3. 分页功能测试: 总共15条数据,可以测试不同页大小
-- 4. 状态筛选测试: 包含NORMAL(正常)、FROZEN(冻结)、CLOSED(关闭)三种状态
-- 5. 统计功能测试: 顶部统计卡片会自动计算总数、正常数、总额度等
-- 6. 排序功能测试: 可以按账户编号、余额等字段排序
-- 7. 导出功能测试: 可以导出这15条完整的账户配置数据
-- =============================================
