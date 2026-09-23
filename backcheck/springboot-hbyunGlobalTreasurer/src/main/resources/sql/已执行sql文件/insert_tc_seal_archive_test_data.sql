-- ================================================================================
-- 印鉴档案表 (TC_SEAL_ARCHIVE_V2) 测试数据
-- 达梦数据库 DM8 兼容
-- 生成日期: 2025-12-25
-- 说明: 生成25条印鉴档案测试数据,涵盖5种印鉴类型,不同持有人和状态
-- ================================================================================

-- 插入测试数据
INSERT INTO TC_SEAL_ARCHIVE_V2 (
    ID, SEAL_CODE, SEAL_NAME, SEAL_TYPE_ID, SEAL_TYPE_NAME,
    OWNER_NAME, OWNER_POSITION, OWNER_ID_CARD,
    EFFECTIVE_DATE, EXPIRE_DATE, SEAL_IMAGE_URL,
    IS_ACTIVE, VERSION_NO, DESCRIPTION,
    CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK
) VALUES
-- 1. 公章类印鉴 (5条)
(
    '1001', 'YZGZ2024001', '华博云科技有限公司公章', 1, '公章',
    '张伟', '总经理', '110101198001011234',
    TO_DATE('2024-01-01', 'YYYY-MM-DD'), TO_DATE('2030-01-01', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_official_001.png',
    1, 1, '公司正式公章,用于对外文件签署',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-公章001'
),
(
    '1002', 'YZGZ2024002', '华博云北京分公司公章', 1, '公章',
    '李娜', '分公司总经理', '110101198502025678',
    TO_DATE('2024-02-01', 'YYYY-MM-DD'), TO_DATE('2029-12-31', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_official_002.png',
    1, 1, '北京分公司专用公章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-公章002'
),
(
    '1003', 'YZGZ2024003', '华博云上海分公司公章', 1, '公章',
    '王强', '分公司总经理', '310101198003039012',
    TO_DATE('2024-03-01', 'YYYY-MM-DD'), TO_DATE('2030-06-30', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_official_003.png',
    1, 1, '上海分公司专用公章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-公章003'
),
(
    '1004', 'YZGZ2024004', '华博云广州分公司公章', 1, '公章',
    '刘洋', '分公司总经理', '440101198204043456',
    TO_DATE('2024-04-01', 'YYYY-MM-DD'), TO_DATE('2029-08-31', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_official_004.png',
    1, 1, '广州分公司专用公章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-公章004'
),
(
    '1005', 'YZGZ2024005', '华博云深圳分公司公章', 1, '公章',
    '陈静', '分公司总经理', '440101198705057890',
    TO_DATE('2024-05-01', 'YYYY-MM-DD'), TO_DATE('2030-12-31', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_official_005.png',
    0, 1, '深圳分公司专用公章(已锁定)',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-公章005'
),

-- 2. 财务章类印鉴 (5条)
(
    '1006', 'YZCW2024001', '华博云财务专用章', 2, '财务章',
    '赵敏', '财务总监', '110101198206062345',
    TO_DATE('2024-01-15', 'YYYY-MM-DD'), TO_DATE('2030-01-15', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_finance_001.png',
    1, 1, '财务部专用章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-财务章001'
),
(
    '1007', 'YZCW2024002', '华博云发票专用章', 2, '财务章',
    '孙丽', '财务经理', '110101198907074567',
    TO_DATE('2024-02-15', 'YYYY-MM-DD'), TO_DATE('2029-12-15', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_finance_002.png',
    1, 1, '开具发票专用章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-财务章002'
),
(
    '1008', 'YZCW2024003', '华博云税务专用章', 2, '财务章',
    '周杰', '税务专员', '110101198508086789',
    TO_DATE('2024-03-15', 'YYYY-MM-DD'), TO_DATE('2030-03-15', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_finance_003.png',
    1, 1, '办理税务业务专用章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-财务章003'
),
(
    '1009', 'YZCW2024004', '华博云银行预留印鉴', 2, '财务章',
    '吴刚', '财务主管', '110101198309098901',
    TO_DATE('2024-04-15', 'YYYY-MM-DD'), TO_DATE('2029-10-15', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_finance_004.png',
    1, 1, '银行账户预留印鉴',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-财务章004'
),
(
    '1010', 'YZCW2024005', '华博云收讫专用章', 2, '财务章',
    '郑华', '出纳', '110101199110101123',
    TO_DATE('2024-05-15', 'YYYY-MM-DD'), TO_DATE('2030-05-15', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_finance_005.png',
    1, 1, '收款确认专用章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-财务章005'
),

-- 3. 合同章类印鉴 (5条)
(
    '1011', 'YZHT2024001', '华博云合同专用章A', 3, '合同章',
    '冯涛', '法务总监', '110101198111112234',
    TO_DATE('2024-01-20', 'YYYY-MM-DD'), TO_DATE('2030-01-20', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_contract_001.png',
    1, 1, '合同签署专用章-A级',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-合同章001'
),
(
    '1012', 'YZHT2024002', '华博云合同专用章B', 3, '合同章',
    '于海', '法务经理', '110101198612123456',
    TO_DATE('2024-02-20', 'YYYY-MM-DD'), TO_DATE('2029-11-20', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_contract_002.png',
    1, 1, '合同签署专用章-B级',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-合同章002'
),
(
    '1013', 'YZHT2024003', '华博云采购合同章', 3, '合同章',
    '袁伟', '采购总监', '110101198413134567',
    TO_DATE('2024-03-20', 'YYYY-MM-DD'), TO_DATE('2030-02-20', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_contract_003.png',
    1, 1, '采购合同专用章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-合同章003'
),
(
    '1014', 'YZHT2024004', '华博云销售合同章', 3, '合同章',
    '曹琳', '销售总监', '110101198814145678',
    TO_DATE('2024-04-20', 'YYYY-MM-DD'), TO_DATE('2029-09-20', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_contract_004.png',
    0, 1, '销售合同专用章(已停用)',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-合同章004'
),
(
    '1015', 'YZHT2024005', '华博云劳动合同章', 3, '合同章',
    '许强', '人力资源总监', '110101198015157890',
    TO_DATE('2024-05-20', 'YYYY-MM-DD'), TO_DATE('2030-04-20', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_contract_005.png',
    1, 1, '劳动合同专用章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-合同章005'
),

-- 4. 法人章类印鉴 (5条)
(
    '1016', 'YZFR2024001', '华博云法人章', 4, '法人章',
    '马云', '法定代表人', '110101197516168901',
    TO_DATE('2024-01-25', 'YYYY-MM-DD'), TO_DATE('2030-01-25', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_legal_001.png',
    1, 1, '法定代表人名章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-法人章001'
),
(
    '1017', 'YZFR2024002', '华博云董事长章', 4, '法人章',
    '马云', '董事长', '110101197516168901',
    TO_DATE('2024-02-25', 'YYYY-MM-DD'), TO_DATE('2029-12-25', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_legal_002.png',
    1, 1, '董事长名章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-法人章002'
),
(
    '1018', 'YZFR2024003', '华博云执行董事章', 4, '法人章',
    '张伟', '执行董事', '110101198001011234',
    TO_DATE('2024-03-25', 'YYYY-MM-DD'), TO_DATE('2030-03-25', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_legal_003.png',
    1, 1, '执行董事名章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-法人章003'
),
(
    '1019', 'YZFR2024004', '华博云监事章', 4, '法人章',
    '李娜', '监事', '110101198502025678',
    TO_DATE('2024-04-25', 'YYYY-MM-DD'), TO_DATE('2029-10-25', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_legal_004.png',
    1, 1, '监事名章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-法人章004'
),
(
    '1020', 'YZFR2024005', '华博股东代表章', 4, '法人章',
    '王强', '股东代表', '310101198003039012',
    TO_DATE('2024-05-25', 'YYYY-MM-DD'), TO_DATE('2030-05-25', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_legal_005.png',
    1, 1, '股东代表名章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-法人章005'
),

-- 5. 部门章类印鉴 (5条)
(
    '1021', 'YZBM2024001', '华博云研发部章', 5, '部门章',
    '郭明', '研发总监', '110101198217171234',
    TO_DATE('2024-01-30', 'YYYY-MM-DD'), TO_DATE('2030-01-30', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_dept_001.png',
    1, 1, '研发部内部章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-部门章001'
),
(
    '1022', 'YZBM2024002', '华博云市场部章', 5, '部门章',
    '梁芳', '市场总监', '110101199118182345',
    TO_DATE('2024-02-29', 'YYYY-MM-DD'), TO_DATE('2029-12-30', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_dept_002.png',
    1, 1, '市场部内部章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-部门章002'
),
(
    '1023', 'YZBM2024003', '华博云人力资源部章', 5, '部门章',
    '许强', '人力资源总监', '110101198015157890',
    TO_DATE('2024-03-30', 'YYYY-MM-DD'), TO_DATE('2030-03-30', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_dept_003.png',
    1, 1, '人力资源部内部章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-部门章003'
),
(
    '1024', 'YZBM2024004', '华博云行政部章', 5, '部门章',
    '黄丽', '行政总监', '110101198819194567',
    TO_DATE('2024-04-30', 'YYYY-MM-DD'), TO_DATE('2029-11-30', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_dept_004.png',
    1, 1, '行政部内部章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-部门章004'
),
(
    '1025', 'YZBM2024005', '华博云IT部章', 5, '部门章',
    '谢东', 'IT总监', '110101198320205678',
    TO_DATE('2024-05-30', 'YYYY-MM-DD'), TO_DATE('2030-06-30', 'YYYY-MM-DD'),
    'http://192.0.2.200/hbfk/seal/seal_dept_005.png',
    1, 1, 'IT部内部章',
    'system', SYSDATE, 'system', SYSDATE, '测试数据-部门章005'
);

COMMIT;

-- 验证插入结果
SELECT '印鉴档案表TBL_SEAL_ARCHIVE的行数: ' || COUNT(*) as result FROM TC_SEAL_ARCHIVE_V2;

-- 按类型统计
SELECT
    SEAL_TYPE_NAME as 印鉴类型,
    COUNT(*) as 数量,
    SUM(CASE WHEN IS_ACTIVE = 1 THEN 1 ELSE 0 END) as 启用数,
    SUM(CASE WHEN IS_ACTIVE = 0 THEN 1 ELSE 0 END) as 停用数
FROM TC_SEAL_ARCHIVE_V2
GROUP BY SEAL_TYPE_NAME
ORDER BY SEAL_TYPE_ID;

-- 说明:
-- 1. 本脚本生成25条测试数据,涵盖5种印鉴类型
-- 2. 印鉴类型分布: 公章5条,财务章5条,合同章5条,法人章5条,部门章5条
-- 3. 状态分布: 23条启用,2条停用(深圳分公司公章、销售合同章)
-- 4. 有效期覆盖: 2024年至2030年之间
-- 5. 持有人覆盖: 不同部门和职位的员工
-- 6. 图片URL: 指向测试服务器上的占位图片
