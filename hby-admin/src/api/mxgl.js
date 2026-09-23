import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * 模型管理API接口
 * @author 示例云
 * @since 2025-01-21
 */

// =====================================================
// 数据源管理API
// =====================================================

/**
 * 分页查询数据源列表
 * @param {Object} data 查询参数
 */
export function getDataSourceList(data) {
  return request({
    url: '/riskcontrol/model/datasource/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 保存数据源配置
 * @param {Object} data 数据源配置
 */
export function saveDataSource(data) {
  return request({
    url: '/riskcontrol/model/datasource/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除数据源
 * @param {String} sourceId 数据源ID
 */
export function deleteDataSource(sourceId) {
  return request({
    url: `/riskcontrol/model/datasource/delete/${sourceId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 批量删除数据源
 * @param {Array} sourceIds 数据源ID列表
 */
export function batchDeleteDataSource(sourceIds) {
  return request({
    url: '/riskcontrol/model/datasource/batchDelete',
    method: 'post',
    data: transData(sourceIds)
  })
}

/**
 * 获取数据源详情
 * @param {String} sourceId 数据源ID
 */
export function getDataSourceDetail(sourceId) {
  return request({
    url: `/riskcontrol/model/datasource/detail/${sourceId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 测试数据源连接
 * @param {String} sourceId 数据源ID
 */
export function testDataSourceConnection(sourceId) {
  return request({
    url: `/riskcontrol/model/datasource/testConnection/${sourceId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 同步表结构
 * @param {String} sourceId 数据源ID
 */
export function syncTableStructure(sourceId) {
  return request({
    url: `/riskcontrol/model/datasource/syncTables/${sourceId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 获取活跃数据源列表
 */
export function getActiveDataSources() {
  return request({
    url: '/riskcontrol/model/datasource/activeList',
    method: 'post',
    data: transData({})
  })
}

/**
 * 获取数据源统计信息
 */
export function getDataSourceStatistics() {
  return request({
    url: '/riskcontrol/model/datasource/statistics',
    method: 'post',
    data: transData({})
  })
}

/**
 * 启用数据源
 * @param {String} sourceId 数据源ID
 */
export function enableDataSource(sourceId) {
  return request({
    url: `/riskcontrol/model/datasource/enable/${sourceId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 禁用数据源
 * @param {String} sourceId 数据源ID
 */
export function disableDataSource(sourceId) {
  return request({
    url: `/riskcontrol/model/datasource/disable/${sourceId}`,
    method: 'post',
    data: transData({})
  })
}

// =====================================================
// 表结构管理API
// =====================================================

/**
 * 分页查询表列表
 * @param {Object} data 查询参数
 */
export function getTableList(data) {
  return request({
    url: '/riskcontrol/model/table/list',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取表的列信息
 * @param {Object} data 查询参数 - {dataSourceId, tableName}
 */
export function getTableColumns(data) {
  return request({
    url: '/riskcontrol/model/table/columns',
    method: 'post',
    params: data  // 使用params而不是data，作为URL参数传递
  })
}

/**
 * 同步表结构到模型
 * @param {String} dataSourceId 数据源ID
 */
export function syncTableToModel(dataSourceId) {
  return request({
    url: `/riskcontrol/model/table/sync/${dataSourceId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 同步单个表结构
 * @param {Object} data 同步参数
 */
export function syncSingleTable(data) {
  return request({
    url: '/riskcontrol/model/table/syncTable',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取表统计信息
 * @param {String} dataSourceId 数据源ID
 */
export function getTableStatistics(dataSourceId) {
  return request({
    url: `/riskcontrol/model/table/statistics/${dataSourceId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 获取表的主键列
 * @param {Object} data 查询参数
 */
export function getTablePrimaryKeys(data) {
  return request({
    url: '/riskcontrol/model/table/primaryKeys',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 搜索包含指定列名的表
 * @param {Object} data 搜索参数
 */
export function searchTablesByColumn(data) {
  return request({
    url: '/riskcontrol/model/table/searchByColumn',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取表的外键关系
 * @param {Object} data 查询参数
 */
export function getTableForeignKeys(data) {
  return request({
    url: '/riskcontrol/model/table/foreignKeys',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 生成表的DDL语句
 * @param {Object} data 生成参数
 */
export function generateTableDDL(data) {
  return request({
    url: '/riskcontrol/model/table/generateDDL',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取表数据预览
 * @param {Object} data 预览参数
 */
export function getTableDataPreview(data) {
  return request({
    url: '/riskcontrol/model/table/preview',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 验证表结构完整性
 * @param {String} dataSourceId 数据源ID
 */
export function validateTableStructure(dataSourceId) {
  return request({
    url: `/riskcontrol/model/table/validate/${dataSourceId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 删除表结构信息
 * @param {Object} data 删除参数
 */
export function deleteTableStructure(data) {
  return request({
    url: '/riskcontrol/model/table/delete',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取表结构列表
 * @param {Object} data 查询参数
 */
export function getTableStructureList(data) {
  return request({
    url: '/riskcontrol/model/table/structureList',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

// =====================================================
// SQL模板管理API
// =====================================================

/**
 * 分页查询SQL模板
 * @param {Object} data 查询参数
 */
export function getSqlTemplateList(data) {
  return request({
    url: '/riskcontrol/model/template/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 查询SQL模板详情
 * @param {String} templateId 模板ID
 */
export function getSqlTemplateDetail(templateId) {
  return request({
    url: `/riskcontrol/model/template/detail/${templateId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 保存SQL模板
 * @param {Object} data 模板数据
 */
export function saveSqlTemplate(data) {
  return request({
    url: '/riskcontrol/model/template/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除SQL模板
 * @param {String} templateId 模板ID
 */
export function deleteSqlTemplate(templateId) {
  return request({
    url: `/riskcontrol/model/template/delete/${templateId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 批量删除SQL模板
 * @param {Array} templateIds 模板ID列表
 */
export function batchDeleteSqlTemplate(templateIds) {
  // 使用URLSearchParams构建表单数据
  const params = new URLSearchParams()

  // 添加每个模板ID
  templateIds.forEach(id => {
    params.append('templateIds', id)
  })

  // 添加token
  const token = require('@/store').default.getters['user/token']
  params.append('token', token)

  return request({
    url: '/riskcontrol/model/template/batchDelete',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  })
}

/**
 * 复制SQL模板
 * @param {Object} data 复制参数
 */
export function copySqlTemplate(data) {
  return request({
    url: '/riskcontrol/model/template/copy',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 启用/禁用SQL模板
 * @param {Object} data 状态参数
 */
export function toggleSqlTemplateStatus(data) {
  return request({
    url: '/riskcontrol/model/template/toggleStatus',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量更新SQL模板状态
 * @param {Object} data 批量状态参数
 */
export function batchUpdateSqlTemplateStatus(data) {
  return request({
    url: '/riskcontrol/model/template/batchUpdateStatus',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 使用模板(更新使用次数)
 * @param {String} templateId 模板ID
 */
export function useSqlTemplate(templateId) {
  return request({
    url: `/riskcontrol/model/template/use/${templateId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 根据分类查询模板
 * @param {String} category 模板分类
 */
export function getSqlTemplatesByCategory(category) {
  return request({
    url: `/riskcontrol/model/template/category/${category}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 根据数据库类型查询模板
 * @param {String} databaseType 数据库类型
 */
export function getSqlTemplatesByDatabaseType(databaseType) {
  return request({
    url: `/riskcontrol/model/template/database/${databaseType}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 搜索SQL模板
 * @param {Object} data 搜索参数
 */
export function searchSqlTemplates(data) {
  return request({
    url: '/riskcontrol/model/template/search',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取热门模板
 * @param {Object} data 查询参数
 */
export function getPopularSqlTemplates(data) {
  return request({
    url: '/riskcontrol/model/template/popular',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取最近使用的模板
 * @param {Object} data 查询参数
 */
export function getRecentSqlTemplates(data) {
  return request({
    url: '/riskcontrol/model/template/recent',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取模板统计信息
 */
export function getSqlTemplateStatistics() {
  return request({
    url: '/riskcontrol/model/template/statistics',
    method: 'post',
    data: transData({})
  })
}

/**
 * 验证SQL语法
 * @param {Object} data 验证参数
 */
export function validateSqlSyntax(data) {
  return request({
    url: '/riskcontrol/model/template/validateSql',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 解析SQL参数
 * @param {Object} data 解析参数
 */
export function parseSqlParameters(data) {
  return request({
    url: '/riskcontrol/model/template/parseParameters',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 解析SQL语句结构
 * @param {Object} data 解析参数 {sqlStatement, dataSourceId}
 */
export function parseSQLStatement(data) {
  return request({
    url: '/riskcontrol/model/datamodel/sql/parse',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取数据模型类型列表
 */
export function getDataModelTypes() {
  return request({
    url: '/riskcontrol/model/datamodel/types',
    method: 'post',
    data: transData({})
  })
}

/**
 * 获取数据模型状态列表
 */
export function getDataModelStatus() {
  return request({
    url: '/riskcontrol/model/datamodel/status',
    method: 'post',
    data: transData({})
  })
}

// =====================================================
// 数据模型管理API
// =====================================================

/**
 * 分页查询数据模型
 * @param {Object} data 查询参数
 */
export function getDataModelList(data) {
  const requestData = transData(data)
  const token = requestData.token

  return request({
    url: '/riskcontrol/model/datamodel/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'token': token
    },
    data: JSON.stringify(requestData)
  })
}

/**
 * 查询数据模型详情
 * @param {String} modelId 模型ID
 */
export function getDataModelDetail(modelId) {
  return request({
    url: `/riskcontrol/model/datamodel/detail/${modelId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 保存数据模型
 * @param {Object} data 模型数据
 */
export function saveDataModel(data) {
  return request({
    url: '/riskcontrol/model/datamodel/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 删除数据模型
 * @param {String} modelId 模型ID
 */
export function deleteDataModel(modelId) {
  return request({
    url: `/riskcontrol/model/datamodel/delete/${modelId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 批量删除数据模型
 * @param {Array} modelIds 模型ID列表
 */
export function batchDeleteDataModel(modelIds) {
  return request({
    url: '/riskcontrol/model/datamodel/batchDelete',
    method: 'post',
    data: transData({ modelIds })
  })
}

/**
 * 生成数据模型代码
 * @param {Object} params 生成参数
 * @param {String} params.modelId 模型ID
 * @param {String} params.codeType 代码类型
 * @param {String} params.packageName 包名
 * @param {String} params.className 类名
 */
export function generateDataModelCode(params) {
  return request({
    url: '/riskcontrol/model/datamodel/generateCode',
    method: 'post',
    data: transData(params)
  })
}

/**
 * 复制数据模型
 * @param {Object} data 复制参数
 */
export function copyDataModel(data) {
  return request({
    url: '/riskcontrol/model/datamodel/copy',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 生成SQL模板
 * @param {String} modelId 模型ID
 */
export function generateSqlTemplate(modelId) {
  return request({
    url: `/riskcontrol/model/datamodel/generateSqlTemplate/${modelId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData({})
  })
}

/**
 * 获取模型版本列表
 * @param {String} modelCode 模型编码
 */
export function getDataModelVersions(modelCode) {
  return request({
    url: `/riskcontrol/model/datamodel/versions/${modelCode}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 创建新版本（旧接口）
 * @param {Object} data 版本参数
 */
export function createDataModelVersionOld(data) {
  return request({
    url: '/riskcontrol/model/datamodel/createVersion',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 设置当前版本
 * @param {Object} data 版本参数
 */
export function setCurrentDataModelVersion(data) {
  return request({
    url: '/riskcontrol/model/datamodel/setCurrentVersion',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 创建数据模型版本
 * @param {Object} data 版本参数
 */
export function createDataModelVersion(data) {
  return request({
    url: '/riskcontrol/model/version/create',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除数据模型版本
 * @param {String} versionId 版本ID
 */
export function deleteDataModelVersion(versionId) {
  return request({
    url: `/riskcontrol/model/version/delete/${versionId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 分页查询模型版本列表
 * @param {Object} data 查询参数
 */
export function getDataModelVersionList(data) {
  return request({
    url: '/riskcontrol/model/version/list',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 查询模型的所有版本
 * @param {String} modelId 模型ID
 */
export function getAllDataModelVersions(modelId) {
  return request({
    url: `/riskcontrol/model/version/all/${modelId}`,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

/**
 * 发布版本
 * @param {String} versionId 版本ID
 */
export function publishDataModelVersion(versionId) {
  return request({
    url: `/riskcontrol/model/version/publish/${versionId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 归档版本
 * @param {String} versionId 版本ID
 */
export function archiveDataModelVersion(versionId) {
  return request({
    url: `/riskcontrol/model/version/archive/${versionId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 设置当前版本
 * @param {String} versionId 版本ID
 */
export function setCurrentModelVersion(versionId) {
  return request({
    url: `/riskcontrol/model/version/setCurrent/${versionId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 回滚到指定版本
 * @param {String} versionId 版本ID
 */
export function rollbackDataModelVersion(versionId) {
  return request({
    url: `/riskcontrol/model/version/rollback/${versionId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 获取版本详情
 * @param {String} versionId 版本ID
 */
export function getDataModelVersionDetail(versionId) {
  return request({
    url: `/riskcontrol/model/version/detail/${versionId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 比较两个版本
 * @param {String} sourceVersionId 源版本ID
 * @param {String} targetVersionId 目标版本ID
 */
export function compareDataModelVersions(sourceVersionId, targetVersionId) {
  return request({
    url: '/riskcontrol/model/version/compare',
    method: 'post',
    params: {
      sourceVersionId,
      targetVersionId
    },
    data: transData({})
  })
}

/**
 * 获取版本统计信息
 * @param {String} modelId 模型ID
 */
export function getDataModelVersionStatistics(modelId) {
  return request({
    url: `/riskcontrol/model/version/statistics/${modelId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 获取下一个版本号
 * @param {String} modelId 模型ID
 */
export function getNextDataModelVersionNo(modelId) {
  return request({
    url: `/riskcontrol/model/version/nextVersionNo/${modelId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 更新模型状态
 * @param {Object} data 状态参数
 */
export function updateDataModelStatus(data) {
  return request({
    url: '/riskcontrol/model/datamodel/updateStatus',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 发布模型
 * @param {String} modelId 模型ID
 */
export function publishDataModel(modelId) {
  return request({
    url: `/riskcontrol/model/datamodel/publish/${modelId}`,
    method: 'post',
    data: transData({})
  })
}



/**
 * 测试模型SQL
 * @param {Object} data 测试参数
 * @param {string} data.dataSourceId 数据源ID
 * @param {string} data.sqlContent SQL内容
 * @param {string} data.parameters 参数JSON字符串
 * @param {string} data.indicatorCode 组合指标编码（新增）
 */
export function testDataModelSql(data) {
  return request({
    url: '/riskcontrol/model/datamodel/test',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 执行模型
 * @param {Object} data 执行参数
 * @param {string} data.modelId 模型ID
 * @param {Object} data.params 执行参数
 * @param {string} data.indicatorCode 组合指标编码（新增）
 */
export function executeDataModel(data) {
  const requestData = {
    modelId: data.modelId,
    token: transData({}).token
  }

  // 如果有参数，转换为JSON字符串
  if (data.params && Object.keys(data.params).length > 0) {
    requestData.params = JSON.stringify(data.params)
  }

  // 🔥 新增：添加指标编码
  if (data.indicatorCode) {
    requestData.indicatorCode = data.indicatorCode
  }

  return request({
    url: '/riskcontrol/model/datamodel/execute',
    method: 'post',
    data: requestData
  })
}

/**
 * 获取模型统计信息
 */
export function getDataModelStatistics() {
  return request({
    url: '/riskcontrol/model/datamodel/statistics',
    method: 'post',
    data: transData({})
  })
}

/**
 * 搜索数据模型
 * @param {Object} data 搜索参数
 */
export function searchDataModels(data) {
  return request({
    url: '/riskcontrol/model/datamodel/search',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取热门模型
 * @param {Object} data 查询参数
 */
export function getPopularDataModels(data) {
  return request({
    url: '/riskcontrol/model/datamodel/popular',
    method: 'post',
    data: transData(data)
  })
}









// =====================================================
// SQL模板管理API (待实现)
// =====================================================



// =====================================================
// 表达式管理API (待实现)
// =====================================================

/**
 * 分页查询表达式规则列表
 * @param {Object} data 查询参数
 */
export function getExpressionRuleList(data) {
  return request({
    url: '/riskcontrol/model/expression/list',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存表达式规则
 * @param {Object} data 表达式规则配置
 */
export function saveExpressionRule(data) {
  return request({
    url: '/riskcontrol/model/expression/save',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 删除表达式规则
 * @param {String} ruleId 规则ID
 */
export function deleteExpressionRule(ruleId) {
  return request({
    url: `/riskcontrol/model/expression/delete/${ruleId}`,
    method: 'post',
    data: transData({})
  })
}

// =====================================================
// 评估模型管理API (待实现)
// =====================================================

/**
 * 获取可用于评估的数据模型列表
 * @param {Object} data 查询参数
 */
export function getAvailableDataModels(data) {
  return request({
    url: '/riskcontrol/model/evaluation/available-models',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 分页查询评估模型列表
 * @param {Object} data 查询参数
 */
export function getEvaluationModelList(data) {
  return request({
    url: '/riskcontrol/model/evaluation/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 🔧 新增：分页查询已发布且启用的评估模型列表（专用于fxyj页面）
 * @param {Object} data 查询参数
 */
export function getPublishedEnabledModelList(data) {
  return request({
    url: '/riskcontrol/model/evaluation/published-enabled-list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 保存评估模型
 * @param {Object} data 评估模型配置
 */
export function saveEvaluationModel(data) {
  return request({
    url: '/riskcontrol/model/evaluation/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 删除评估模型
 * @param {String} modelId 模型ID
 */
export function deleteEvaluationModel(modelId) {
  return request({
    url: `/riskcontrol/model/evaluation/delete/${modelId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({}))
  })
}

/**
 * 获取评估模型详情
 * @param {String} modelId 模型ID
 */
export function getEvaluationModelDetail(modelId) {
  return request({
    url: `/riskcontrol/model/evaluation/detail/${modelId}`,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    params: transData({})
  })
}

/**
 * 测试评估模型
 * @param {Object} data 测试参数
 */
export function testEvaluationModel(data) {
  return request({
    url: '/riskcontrol/model/evaluation/test',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 发布评估模型
 * @param {String} modelId 模型ID
 */
export function publishEvaluationModel(modelId) {
  return request({
    url: `/riskcontrol/model/evaluation/publish/${modelId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({}))
  })
}

/**
 * 复制评估模型
 * @param {Object} data 复制参数
 */
export function copyEvaluationModel(data) {
  return request({
    url: '/riskcontrol/model/evaluation/copy',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取评估模型统计信息
 */
export function getEvaluationModelStatistics() {
  return request({
    url: '/riskcontrol/model/evaluation/statistics',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({}))
  })
}

/**
 * 执行评估模型
 * @param {Object} data 执行参数
 */
export function executeEvaluationModel(data) {
  return request({
    url: '/riskcontrol/model/evaluation/execute',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 启动评估模型
 * @param {String} modelId 模型ID
 * @param {Object} scheduleConfig 调度配置
 */
export function startEvaluationModel(modelId, scheduleConfig) {
  return request({
    url: `/riskcontrol/model/evaluation/start/${modelId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(scheduleConfig))
  })
}

/**
 * 批量启动评估模型
 * @param {Array} modelIds 模型ID列表
 * @param {Object} scheduleConfig 调度配置
 */
export function batchStartEvaluationModels(modelIds, scheduleConfig) {
  return request({
    url: `/riskcontrol/model/evaluation/batch-start`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({
      modelIds: modelIds,
      scheduleConfig: scheduleConfig
    }))
  })
}

/**
 * 停止评估模型
 * @param {String} modelId 模型ID
 */
export function stopEvaluationModel(modelId) {
  return request({
    url: `/riskcontrol/model/evaluation/stop/${modelId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({}))
  })
}

/**
 * 导出评估模型
 * @param {String} modelId 模型ID
 */
export function exportEvaluationModel(modelId) {
  return request({
    url: `/riskcontrol/model/evaluation/export/${modelId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({})),
    responseType: 'blob'  // 用于文件下载
  })
}

// =====================================================
// 风险预警管理API (待实现)
// =====================================================

/**
 * 分页查询风险预警列表
 * @param {Object} data 查询参数
 */
export function getRiskWarningList(data) {
  return request({
    url: '/riskcontrol/model/warning/list',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 处理风险预警
 * @param {Object} data 处理参数
 */
export function processRiskWarning(data) {
  return request({
    url: '/riskcontrol/model/warning/process',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量处理风险预警
 * @param {Object} data 批量处理参数
 */
export function batchProcessRiskWarning(data) {
  return request({
    url: '/riskcontrol/model/warning/batchProcess',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取风险预警详情
 * @param {String} warningId 预警ID
 */
export function getRiskWarningDetail(warningId) {
  return request({
    url: `/riskcontrol/model/warning/detail/${warningId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 获取预警统计信息
 * @param {Object} data 查询参数
 */
export function getWarningStatistics(data) {
  return request({
    url: '/riskcontrol/model/warning/statistics',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 🔧 新增：获取今日新增预警数量
 */
export function getTodayWarningCount() {
  return request({
    url: '/riskcontrol/model/warning/todayCount',
    method: 'post'
  })
}

/**
 * 🔧 新增：获取模型待处理预警数量（只统计PENDING和PROCESSING状态）
 * @param {String} modelId 模型ID
 */
export function getModelPendingWarningCount(modelId) {
  return request({
    url: `/riskcontrol/model/evaluation/pendingWarningCount/${modelId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({}))
  })
}

/**
 * 生成预警报告
 * @param {Object} data 报告生成参数
 */
export function generateWarningReport(data) {
  return request({
    url: '/riskcontrol/model/warning/generateReport',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取预警配置
 * @param {Object} data 查询参数
 */
export function getWarningConfig(data) {
  return request({
    url: '/riskcontrol/model/warning/config',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 更新预警配置
 * @param {Object} data 配置参数
 */
export function updateWarningConfig(data) {
  return request({
    url: '/riskcontrol/model/warning/updateConfig',
    method: 'post',
    data: transData(data)
  })
}

// =====================================================
// 评估模型预警功能增强API
// =====================================================

/**
 * 获取模型预警数量
 * @param {String} modelId 模型ID
 */
export function getModelWarningCount(modelId) {
  return request({
    url: `/riskcontrol/model/evaluation/warningCount/${modelId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({}))
  })
}

/**
 * 获取模型预警结果（查询风险预警表）
 * @param {Object} data 查询参数
 */
export function getModelWarningResults(data) {
  return request({
    url: '/riskcontrol/model/evaluation/warningResults',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取评估模型SQL执行结果（新接口）
 * @param {Object} data 查询参数
 */
export function getModelSqlExecutionResult(data) {
  return request({
    url: '/riskcontrol/model/evaluation/sqlExecutionResult',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取预警源数据
 * @param {Object} data 查询参数
 */
export function getWarningSourceData(data) {
  return request({
    url: '/riskcontrol/model/evaluation/sourceData',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取预警源数据详情
 * @param {Object} data 查询参数
 */
export function getWarningSourceDataDetail(data) {
  return request({
    url: '/riskcontrol/model/evaluation/warningSourceData',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

// =====================================================
// 系统管理API (待实现)
// =====================================================

/**
 * 获取系统参数列表
 * @param {Object} data 查询参数
 */
export function getSystemParams(data) {
  return request({
    url: '/riskcontrol/model/system/params',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 保存系统参数
 * @param {Object} data 参数配置
 */
export function saveSystemParams(data) {
  return request({
    url: '/riskcontrol/model/system/saveParams',
    method: 'post',
    data: transData(data)
  })
}

// ==================== 表达式管理 API ====================

// =====================================================
// 数据源字段选择相关API
// =====================================================







/**
 * 验证字段比对表达式
 * @param {Object} data 验证参数
 */
export function validateFieldComparisonExpression(data) {
  return request({
    url: '/riskcontrol/model/expression/validateFieldComparison',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 测试字段比对表达式执行
 * @param {Object} data 测试参数
 */
export function testFieldComparisonExpression(data) {
  return request({
    url: '/riskcontrol/model/expression/testFieldComparison',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 生成字段比对SQL语句
 * @param {Object} data 生成参数
 */
export function generateFieldComparisonSQL(data) {
  return request({
    url: '/riskcontrol/model/expression/generateFieldComparisonSQL',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

// =====================================================
// 中文公式解析相关API
// =====================================================

/**
 * 解析中文公式为SQL表达式
 * @param {Object} data 解析参数 {chineseFormula, sourceDataConfig, targetDataConfig}
 */
export function parseChineseFormula(data) {
  return request({
    url: '/riskcontrol/model/expression/chinese/parse',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}



/**
 * 获取支持的中文运算符列表
 */
export function getSupportedOperators() {
  return request({
    url: '/riskcontrol/model/expression/chinese/operators',
    method: 'get'
  })
}

/**
 * 获取中文公式模板
 * @param {String} category 模板分类
 */
export function getChineseFormulaTemplates(category = '') {
  return request({
    url: '/riskcontrol/model/expression/chinese/templates',
    method: 'get',
    params: { category }
  })
}

/**
 * 获取公式智能建议
 * @param {Object} data 建议参数 {partialFormula, availableFields}
 */
export function getFormulaSuggestions(data) {
  return request({
    url: '/riskcontrol/model/expression/chinese/suggestions',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 格式化中文公式
 * @param {Object} data 格式化参数 {chineseFormula}
 */
export function formatChineseFormula(data) {
  return request({
    url: '/riskcontrol/model/expression/chinese/format',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

// =====================================================
// 模板管理相关API
// =====================================================

// 获取模板详情
export function getTemplateById(templateId) {
  return request({
    url: `/riskcontrol/model/expression/templates/${templateId}`,
    method: 'get'
  })
}

// 保存中文公式模板
export function saveChineseFormulaTemplate(data) {
  return request({
    url: '/riskcontrol/model/expression/templates/save',
    method: 'post',
    headers: {'Content-Type': 'application/json;charset=UTF-8'},
    data: JSON.stringify(transData(data))
  })
}

// 使用模板
export function useTemplate(templateId) {
  return request({
    url: `/riskcontrol/model/expression/templates/${templateId}/use`,
    method: 'post'
  })
}

// 获取热门模板
export function getPopularTemplates(limit = 10) {
  return request({
    url: '/riskcontrol/model/expression/templates/popular',
    method: 'get',
    params: { limit }
  })
}

// 搜索模板
export function searchTemplates(keyword, category) {
  return request({
    url: '/riskcontrol/model/expression/templates/search',
    method: 'get',
    params: { keyword, category }
  })
}

// 删除模板
export function deleteTemplate(templateId) {
  return request({
    url: `/riskcontrol/model/expression/templates/${templateId}`,
    method: 'delete'
  })
}

// 启用/禁用模板
export function toggleTemplateStatus(templateId, enabled) {
  return request({
    url: `/riskcontrol/model/expression/templates/${templateId}/toggle`,
    method: 'post',
    params: { enabled }
  })
}

// 获取模板分类
export function getTemplateCategories() {
  return request({
    url: '/riskcontrol/model/expression/templates/categories',
    method: 'get'
  })
}





/**
 * 批量删除表达式
 * @param {Object} data 删除参数
 */
export function batchDeleteExpression(data) {
  return request({
    url: '/riskcontrol/model/expression/batchDelete',
    method: 'post',
    data: transData(data)
  })
}





/**
 * 更新表达式状态
 * @param {Object} data 状态参数
 */
export function updateExpressionStatus(data) {
  return request({
    url: '/riskcontrol/model/expression/updateStatus',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 批量更新表达式状态
 * @param {Object} data 状态参数
 */
export function batchUpdateExpressionStatus(data) {
  return request({
    url: '/riskcontrol/model/expression/batchUpdateStatus',
    method: 'post',
    data: transData(data)
  })
}



/**
 * 测试表达式执行
 * @param {Object} data 测试参数
 */
export function testExpressionExecution(data) {
  return request({
    url: '/riskcontrol/model/expression/test',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 执行表达式
 * @param {Object} data 执行参数
 */
export function executeExpression(data) {
  return request({
    url: '/riskcontrol/model/expression/execute',
    method: 'post',
    data: transData(data)
  })
}



/**
 * 表达式类型统计
 */
export function getExpressionTypeStatistics() {
  return request({
    url: '/riskcontrol/model/expression/typeStatistics',
    method: 'post'
  })
}

/**
 * 表达式分类统计
 */
export function getExpressionCategoryStatistics() {
  return request({
    url: '/riskcontrol/model/expression/categoryStatistics',
    method: 'post'
  })
}

/**
 * 搜索表达式
 * @param {Object} data 搜索参数
 */
export function searchExpressions(data) {
  return request({
    url: '/riskcontrol/model/expression/search',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取模板表达式
 * @param {Object} data 查询参数
 */
export function getTemplateExpressions(data) {
  return request({
    url: '/riskcontrol/model/expression/templates',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 生成表达式代码
 * @param {Object} data 生成参数
 */
export function generateExpressionCode(data) {
  return request({
    url: '/riskcontrol/model/expression/generateCode',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取表达式依赖关系
 */
export function getExpressionDependencies() {
  return request({
    url: '/riskcontrol/model/expression/dependencies',
    method: 'post'
  })
}

// =====================================================
// 表达式管理系统API - 新增功能
// =====================================================

/**
 * 分页查询表达式列表
 * @param {Object} data 查询参数
 */
export function getExpressionList(data) {
  const requestData = transData(data)
  const token = requestData.token

  return request({
    url: '/riskcontrol/model/expression/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'token': token
    },
    data: JSON.stringify(requestData)
  })
}

/**
 * 获取表达式详情
 * @param {String} expressionId 表达式ID
 */
export function getExpressionDetail(expressionId) {
  return request({
    url: `/riskcontrol/model/expression/detail/${expressionId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData({})
  })
}

/**
 * 保存表达式
 * @param {Object} data 表达式数据
 */
export function saveExpression(data) {
  const requestData = transData(data)
  const token = requestData.token

  return request({
    url: '/riskcontrol/model/expression/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'token': token
    },
    data: JSON.stringify(requestData)
  })
}

/**
 * 删除表达式
 * @param {String} expressionId 表达式ID
 */
export function deleteExpression(expressionId) {
  return request({
    url: `/riskcontrol/model/expression/delete/${expressionId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData({})
  })
}

/**
 * 复制表达式
 * @param {Object} data 复制参数
 */
export function copyExpression(data) {
  return request({
    url: '/riskcontrol/model/expression/copy',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 切换表达式状态
 * @param {String} expressionId 表达式ID
 */
export function toggleExpressionStatus(expressionId) {
  return request({
    url: `/riskcontrol/model/expression/toggleStatus/${expressionId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData({})
  })
}

/**
 * 校验表达式
 * @param {Object} data 校验参数
 */
export function validateExpression(data) {
  return request({
    url: '/riskcontrol/model/expression/validate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 测试表达式
 * @param {Object} data 测试参数
 */
export function testExpression(data) {
  return request({
    url: '/riskcontrol/model/expression/test',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 获取表达式统计信息
 */
export function getExpressionStatistics() {
  return request({
    url: '/riskcontrol/model/expression/statistics',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData({})
  })
}

/**
 * 获取最近活动
 */
export function getRecentActivity() {
  return request({
    url: '/riskcontrol/model/expression/recentActivity',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData({})
  })
}

// =====================================================
// 数据源相关API - 表达式专用
// =====================================================

/**
 * 获取表达式可用数据源
 */
export function getDataSourcesForExpression() {
  return request({
    url: '/riskcontrol/model/expression/dataSources',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData({})
  })
}

/**
 * 获取数据源下的表列表
 * @param {String} dataSourceId 数据源ID
 */
export function getTablesForExpression(dataSourceId) {
  return request({
    url: `/riskcontrol/model/expression/dataSources/${dataSourceId}/tables`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData({})
  })
}

/**
 * 获取表字段信息
 * @param {Object} params 参数 {dataSourceId, tableName}
 */
export function getTableFieldsForExpression(params) {
  return request({
    url: '/riskcontrol/model/expression/tableFields',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(params)
  })
}

// =====================================================
// 模板相关API
// =====================================================

/**
 * 获取模板列表
 * @param {Object} params 查询参数
 */
export function getTemplateList(params = {}) {
  return request({
    url: '/riskcontrol/model/expression/templates/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(params)
  })
}

// =====================================================
// 中文运算符API
// =====================================================

/**
 * 获取中文运算符列表
 */
export function getChineseOperators() {
  return request({
    url: '/riskcontrol/model/expression/chineseOperators',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData({})
  })
}

/**
 * 校验中文公式
 * @param {Object} data 校验参数
 */
export function validateChineseFormula(data) {
  return request({
    url: '/riskcontrol/model/expression/validateChineseFormula',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

// =====================================================
// 表达式测试相关API
// =====================================================

/**
 * 执行表达式测试
 * @param {Object} data 测试参数
 */
export function executeExpressionTest(data) {
  return request({
    url: '/riskcontrol/model/expression/executeTest',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 校验表达式语法
 * @param {Object} data 校验参数
 */
export function validateExpressionSyntax(data) {
  return request({
    url: '/riskcontrol/model/expression/validateSyntax',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 保存测试报告
 * @param {Object} data 测试报告数据
 */
export function saveExpressionTestReport(data) {
  return request({
    url: '/riskcontrol/model/expression/saveTestReport',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 分析表达式复杂度
 * @param {Object} data 分析参数
 */
export function analyzeExpressionComplexity(data) {
  return request({
    url: '/riskcontrol/model/expression/analyzeComplexity',
    method: 'post',
    data: transData(data)
  })
}

// =====================================================
// 指标组合分析API
// =====================================================

/**
 * 获取组合列表
 * @param {Object} data 查询参数
 */
export function getCombinationList(data) {
  const requestData = transData(data)
  return request({
    url: '/riskcontrol/model/combination/list',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
      'token': requestData.token
    },
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      combinationName: data.combinationName,
      category: data.category,
      status: data.status,
      createUser: data.createUser
    },
    data: JSON.stringify(requestData)
  })
}

/**
 * 获取组合详情
 * @param {String} combinationId 组合ID
 */
export function getCombinationDetail(combinationId) {
  return request({
    url: `/riskcontrol/model/combination/detail/${combinationId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({}))
  })
}

/**
 * 保存组合配置
 * @param {Object} data 组合配置数据
 */
export function saveCombination(data) {
  return request({
    url: '/riskcontrol/model/combination/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 删除组合
 * @param {String} combinationId 组合ID
 */
export function deleteCombination(combinationId) {
  return request({
    url: `/riskcontrol/model/combination/delete/${combinationId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 复制组合
 * @param {String} combinationId 组合ID
 * @param {Object} data 复制配置
 */
export function copyCombination(combinationId, data) {
  return request({
    url: `/riskcontrol/model/combination/copy/${combinationId}`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取可用指标列表
 * @param {Object} data 查询参数
 */
export function getAvailableIndicators(data) {
  return request({
    url: '/riskcontrol/model/combination/indicators/available',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 添加指标到组合
 * @param {Object} data 指标配置数据
 */
export function addIndicatorToCombination(data) {
  return request({
    url: '/riskcontrol/model/combination/indicators/add',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 移除组合中的指标
 * @param {String} configId 配置ID
 */
export function removeIndicatorFromCombination(configId) {
  return request({
    url: `/riskcontrol/model/combination/indicators/remove/${configId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 更新指标配置
 * @param {Object} data 配置数据
 */
export function updateIndicatorConfig(data) {
  return request({
    url: '/riskcontrol/model/combination/indicators/update',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 验证指标SQL
 * @param {Object} data 验证数据
 */
export function validateIndicatorSql(data) {
  return request({
    url: '/riskcontrol/model/combination/indicators/validate',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 执行组合分析
 * @param {Object} data 执行配置
 */
export function executeCombination(data) {
  return request({
    url: '/riskcontrol/model/combination/execute',
    method: 'post',
    data: transData(data)
  })
}

/**
 * 获取执行状态
 * @param {String} executionId 执行ID
 */
export function getExecutionStatus(executionId) {
  return request({
    url: `/riskcontrol/model/combination/execution/status/${executionId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 获取执行结果
 * @param {String} executionId 执行ID
 */
export function getExecutionResult(executionId) {
  return request({
    url: `/riskcontrol/model/combination/execution/result/${executionId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 取消执行
 * @param {String} executionId 执行ID
 */
export function cancelExecution(executionId) {
  return request({
    url: `/riskcontrol/model/combination/execution/cancel/${executionId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 获取执行历史
 * @param {Object} data 查询参数
 */
export function getExecutionHistory(data) {
  return request({
    url: '/riskcontrol/model/combination/execution/history',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 删除执行记录
 * @param {String} executionId 执行ID
 * @param {Boolean} forceDelete 是否强制删除
 */
export function deleteExecutionRecord(executionId, forceDelete = false) {
  return request({
    url: '/riskcontrol/model/combination/execution/delete',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({ executionId, forceDelete }))
  })
}

/**
 * 修复脏数据
 */
export function fixDirtyData() {
  return request({
    url: '/riskcontrol/model/combination/execution/fix-dirty-data',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({}))
  })
}

/**
 * 获取指标结果详情
 * @param {String} resultId 结果ID
 * @param {Object} data 分页参数
 */
export function getIndicatorResultDetail(resultId, data) {
  return request({
    url: `/riskcontrol/model/combination/result/detail/${resultId}`,
    method: 'post',
    data: transData(data)
  })
}

/**
 * 执行交集分析
 * @param {Object} data 分析配置
 */
export function executeIntersectionAnalysis(data) {
  return request({
    url: '/riskcontrol/model/combination/analysis/intersection',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 导出分析结果
 * @param {Object} data 导出配置
 */
export function exportAnalysisResult(data) {
  return request({
    url: '/riskcontrol/model/combination/result/export',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取流程图配置
 * @param {String} combinationId 组合ID
 */
export function getCombinationFlow(combinationId) {
  return request({
    url: `/riskcontrol/model/combination/flow/${combinationId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 保存流程图配置
 * @param {Object} data 流程图配置
 */
export function saveCombinationFlow(data) {
  return request({
    url: '/riskcontrol/model/combination/flow/save',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 自动生成流程图
 * @param {String} combinationId 组合ID
 */
export function generateCombinationFlow(combinationId) {
  return request({
    url: `/riskcontrol/model/combination/flow/generate/${combinationId}`,
    method: 'post',
    data: transData({})
  })
}

/**
 * 获取节点执行结果
 * @param {String} indicatorCode 指标编码
 * @param {Object} params 查询参数
 */
export function getNodeExecutionResult(indicatorCode, params = {}) {
  return request({
    url: `/riskcontrol/model/combination/node/result/${indicatorCode}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(params))  // 🔥 修复：发送JSON格式数据
  })
}

/**
 * 获取节点详细信息
 * @param {String} nodeId 节点ID
 * @param {Object} params 查询参数
 */
export function getNodeDetail(nodeId, params = {}) {
  return request({
    url: `/riskcontrol/model/combination/node/detail/${nodeId}`,
    method: 'post',
    data: transData(params)  // POST请求使用data传递参数
  })
}

/**
 * 🔥 新增：获取指标的历史执行成功结果
 * @param {Object} data 查询参数
 * @param {String} data.combinationId 组合ID
 * @param {String} data.indicatorName 指标名称
 * @param {String} data.indicatorCode 指标编码
 * @param {Number} data.pageNum 页码
 * @param {Number} data.pageSize 页大小
 */
export function getIndicatorSuccessExecutionResult(data) {
  return request({
    url: '/riskcontrol/model/combination/indicator/success-result',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData(data))
  })
}

/**
 * 获取组合统计信息
 */
export function getCombinationStatistics() {
  return request({
    url: '/riskcontrol/model/combination/statistics',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({}))
  })
}

/**
 * 测试SQL语句执行
 * @param {Object} data 测试参数
 */
export function testSqlExecution(data) {
  return request({
    url: '/riskcontrol/model/template/test',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

// =====================================================
// SQL可视化编辑器API
// =====================================================

/**
 * 执行SQL语句
 * @param {Object} data SQL执行参数
 * @param {string} data.dataSourceId 数据源ID
 * @param {string} data.sqlContent SQL内容
 * @param {string} data.parameters 参数JSON字符串
 * @param {string} data.indicatorCode 组合指标编码（新增）
 */
export function executeSQL(data) {
  // 添加token
  const requestData = transData(data)

  // 转换为URLSearchParams格式，适配后端@RequestParam
  const formData = new URLSearchParams()
  Object.keys(requestData).forEach(key => {
    if (requestData[key] !== null && requestData[key] !== undefined) {
      formData.append(key, requestData[key])
    }
  })

  return request({
    url: '/riskcontrol/model/datamodel/test',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    data: formData
  })
}

/**
 * 验证SQL语法
 * @param {Object} data SQL验证参数
 */
export function validateSQL(data) {
  return request({
    url: '/riskcontrol/model/sql/validate',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 获取表字段信息
 * @param {Object} data 查询参数
 */
export function getTableFields(data) {
  return request({
    url: '/riskcontrol/model/table/columns',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 执行单个指标并创建临时表
 * @param {Object} data 指标执行参数
 */
export function executeIndicatorWithTempTable(data) {
  return request({
    url: '/riskcontrol/model/combination/executeIndicator',
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: transData(data)
  })
}

/**
 * 获取组合关联的模型文档
 * @param {String} combinationId 组合ID
 */
export function getModelDoc(combinationId) {
  return request({
    url: `/riskcontrol/model/combination/modelDoc/${combinationId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: JSON.stringify(transData({}))
  })
}