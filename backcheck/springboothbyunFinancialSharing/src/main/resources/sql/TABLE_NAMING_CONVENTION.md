# 成本中心模块表名规范

## 表名规范
根据项目命名规范，所有成本中心相关的数据表都使用 `T_` 前缀的大写命名格式。

## 表名映射

| 原表名 | 新表名 | 说明 |
|--------|--------|------|
| `cost_center` | `T_COST_CENTER` | 成本中心基础信息表 |
| `cost_collection` | `T_COST_COLLECTION` | 成本归集主表 |
| `cost_collection_detail` | `T_COST_COLLECTION_DETAIL` | 成本归集明细表 |
| `cost_budget` | `T_COST_BUDGET` | 成本预算表 |
| `cost_control` | `T_COST_CONTROL` | 成本控制表 |
| `cost_analysis` | `T_COST_ANALYSIS` | 成本分析结果表 |

## 索引命名规范
索引使用 `idx_` 前缀，后跟字段名称的组合：
- `idx_center_code` - 成本中心编码索引
- `idx_book_tenant` - 账簿租户复合索引
- `idx_collection_no` - 归集单号索引
- `idx_collection_period` - 归集期间索引
- 等等...

## 外键关系
- `T_COST_COLLECTION_DETAIL.COLLECTION_ID` → `T_COST_COLLECTION.COLLECTION_ID`
- `T_COST_COLLECTION.COST_CENTER_ID` → `T_COST_CENTER.CENTER_ID`
- `T_COST_BUDGET.COST_CENTER_ID` → `T_COST_CENTER.CENTER_ID`
- `T_COST_CONTROL.COST_CENTER_ID` → `T_COST_CENTER.CENTER_ID`
- `T_COST_ANALYSIS.COST_CENTER_ID` → `T_COST_CENTER.CENTER_ID`

## 字段命名规范
- 使用大写字母和下划线分隔
- ID字段使用 `_ID` 后缀
- 代码字段使用 `_CODE` 后缀
- 名称字段使用 `_NAME` 后缀
- 时间字段使用 `_TIME` 后缀
- 状态字段使用 `_STATUS` 后缀

## 已更新的文件

### SQL文件
- `src/main/resources/sql/cost_center_tables.sql` - 所有表名已更新

### Mapper XML文件
- `src/main/resources/oracle/CostCollectionMapper.xml` - 已更新所有SQL查询

### Java文件
- 所有相关的Java代码不需要修改，因为MyBatis通过XML映射文件处理表名转换

## 兼容性说明
- 表名更改后，如果数据库中已存在旧表，需要先备份数据，然后创建新表并迁移数据
- 建议在测试环境先验证表结构和数据迁移
- 生产环境部署前请务必进行完整的数据备份