import request from '@/utils/request'

// ==================== 常量定义 ====================

// 检索类型
export const SEARCH_TYPES = {
  FULL_TEXT: 'FULL_TEXT',
  SEMANTIC: 'SEMANTIC', 
  IMAGE: 'IMAGE',
  VOICE: 'VOICE',
  HYBRID: 'HYBRID'
}

// 检索状态
export const SEARCH_STATUS = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
  INDEXING: 'INDEXING',
  OPTIMIZING: 'OPTIMIZING',
  ERROR: 'ERROR'
}

// 检索引擎
export const SEARCH_ENGINES = {
  ELASTICSEARCH: 'ELASTICSEARCH',
  SOLR: 'SOLR',
  LUCENE: 'LUCENE',
  CUSTOM: 'CUSTOM'
}

// 索引状态
export const INDEX_STATUS = {
  BUILDING: 'BUILDING',
  READY: 'READY',
  UPDATING: 'UPDATING',
  OPTIMIZING: 'OPTIMIZING',
  ERROR: 'ERROR',
  DELETED: 'DELETED'
}

// 推荐算法
export const RECOMMENDATION_ALGORITHMS = {
  COLLABORATIVE: 'COLLABORATIVE',
  CONTENT_BASED: 'CONTENT_BASED',
  HYBRID: 'HYBRID'
}

// ==================== 基础CRUD接口 ====================

/**
 * 创建档案检索
 */
export function createArchiveSearch(data) {
  return request({
    url: '/accountant/as/archiveSearch/create',
    method: 'post',
    data
  })
}

/**
 * 更新档案检索
 */
export function updateArchiveSearch(data) {
  return request({
    url: '/accountant/as/archiveSearch/update',
    method: 'put',
    data
  })
}

/**
 * 删除档案检索
 */
export function deleteArchiveSearch(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/delete/${tenantId}/${searchId}`,
    method: 'delete'
  })
}

/**
 * 根据ID查询档案检索
 */
export function getArchiveSearchById(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/get/${tenantId}/${searchId}`,
    method: 'get'
  })
}

/**
 * 根据编号查询档案检索
 */
export function getArchiveSearchByCode(tenantId, searchCode) {
  return request({
    url: `/accountant/as/archiveSearch/getByCode/${tenantId}/${searchCode}`,
    method: 'get'
  })
}

/**
 * 分页查询档案检索
 */
export function getArchiveSearchPage(tenantId, params) {
  return request({
    url: `/accountant/as/archiveSearch/page/${tenantId}`,
    method: 'get',
    params
  })
}

// ==================== 检索管理接口 ====================

/**
 * 启动检索服务
 */
export function startSearchService(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/start/${tenantId}/${searchId}`,
    method: 'post'
  })
}

/**
 * 停止检索服务
 */
export function stopSearchService(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/stop/${tenantId}/${searchId}`,
    method: 'post'
  })
}

/**
 * 重启检索服务
 */
export function restartSearchService(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/restart/${tenantId}/${searchId}`,
    method: 'post'
  })
}

/**
 * 执行检索
 */
export function executeSearch(tenantId, searchId, searchParams) {
  return request({
    url: `/accountant/as/archiveSearch/execute/${tenantId}/${searchId}`,
    method: 'post',
    data: searchParams
  })
}

/**
 * 全文检索
 */
export function fullTextSearch(tenantId, searchId, keywords, filters = {}) {
  return request({
    url: `/accountant/as/archiveSearch/fullTextSearch/${tenantId}/${searchId}`,
    method: 'post',
    params: { keywords },
    data: filters
  })
}

/**
 * 语义检索
 */
export function semanticSearch(tenantId, searchId, query, context = {}) {
  return request({
    url: `/accountant/as/archiveSearch/semanticSearch/${tenantId}/${searchId}`,
    method: 'post',
    params: { query },
    data: context
  })
}

/**
 * 图像检索
 */
export function imageSearch(tenantId, searchId, imageData, options = {}) {
  return request({
    url: `/accountant/as/archiveSearch/imageSearch/${tenantId}/${searchId}`,
    method: 'post',
    params: { imageData },
    data: options
  })
}

/**
 * 语音检索
 */
export function voiceSearch(tenantId, searchId, audioData, options = {}) {
  return request({
    url: `/accountant/as/archiveSearch/voiceSearch/${tenantId}/${searchId}`,
    method: 'post',
    params: { audioData },
    data: options
  })
}

/**
 * 混合检索
 */
export function hybridSearch(tenantId, searchId, multiModalData) {
  return request({
    url: `/accountant/as/archiveSearch/hybridSearch/${tenantId}/${searchId}`,
    method: 'post',
    data: multiModalData
  })
}

// ==================== 索引管理接口 ====================

/**
 * 创建索引
 */
export function createIndex(tenantId, searchId, indexConfig) {
  return request({
    url: `/accountant/as/archiveSearch/createIndex/${tenantId}/${searchId}`,
    method: 'post',
    data: indexConfig
  })
}

/**
 * 重建索引
 */
export function rebuildIndex(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/rebuildIndex/${tenantId}/${searchId}`,
    method: 'post'
  })
}

/**
 * 优化索引
 */
export function optimizeIndex(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/optimizeIndex/${tenantId}/${searchId}`,
    method: 'post'
  })
}

/**
 * 删除索引
 */
export function deleteIndex(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/deleteIndex/${tenantId}/${searchId}`,
    method: 'delete'
  })
}

/**
 * 更新索引配置
 */
export function updateIndexConfig(tenantId, searchId, indexConfig) {
  return request({
    url: `/accountant/as/archiveSearch/updateIndexConfig/${tenantId}/${searchId}`,
    method: 'put',
    data: indexConfig
  })
}

/**
 * 获取索引状态
 */
export function getIndexStatus(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/getIndexStatus/${tenantId}/${searchId}`,
    method: 'get'
  })
}

/**
 * 获取索引统计
 */
export function getIndexStatistics(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/getIndexStatistics/${tenantId}/${searchId}`,
    method: 'get'
  })
}

// ==================== OCR处理接口 ====================

/**
 * 启动OCR处理
 */
export function startOcrProcessing(tenantId, searchId, ocrConfig) {
  return request({
    url: `/accountant/as/archiveSearch/startOcr/${tenantId}/${searchId}`,
    method: 'post',
    data: ocrConfig
  })
}

/**
 * 停止OCR处理
 */
export function stopOcrProcessing(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/stopOcr/${tenantId}/${searchId}`,
    method: 'post'
  })
}

/**
 * 执行OCR识别
 */
export function executeOcrRecognition(tenantId, searchId, documentPath) {
  return request({
    url: `/accountant/as/archiveSearch/executeOcr/${tenantId}/${searchId}`,
    method: 'post',
    params: { documentPath }
  })
}

/**
 * 批量OCR处理
 */
export function batchOcrProcessing(tenantId, searchId, documentPaths) {
  return request({
    url: `/accountant/as/archiveSearch/batchOcr/${tenantId}/${searchId}`,
    method: 'post',
    data: documentPaths
  })
}

/**
 * 获取OCR处理结果
 */
export function getOcrResults(tenantId, searchId, taskId) {
  return request({
    url: `/accountant/as/archiveSearch/getOcrResults/${tenantId}/${searchId}/${taskId}`,
    method: 'get'
  })
}

/**
 * 更新OCR配置
 */
export function updateOcrConfig(tenantId, searchId, ocrConfig) {
  return request({
    url: `/accountant/as/archiveSearch/updateOcrConfig/${tenantId}/${searchId}`,
    method: 'put',
    data: ocrConfig
  })
}

// ==================== 语义检索接口 ====================

/**
 * 初始化语义模型
 */
export function initializeSemanticModel(tenantId, searchId, modelConfig) {
  return request({
    url: `/accountant/as/archiveSearch/initSemanticModel/${tenantId}/${searchId}`,
    method: 'post',
    data: modelConfig
  })
}

/**
 * 更新语义模型
 */
export function updateSemanticModel(tenantId, searchId, modelVersion) {
  return request({
    url: `/accountant/as/archiveSearch/updateSemanticModel/${tenantId}/${searchId}`,
    method: 'put',
    params: { modelVersion }
  })
}

/**
 * 训练语义模型
 */
export function trainSemanticModel(tenantId, searchId, trainingData) {
  return request({
    url: `/accountant/as/archiveSearch/trainSemanticModel/${tenantId}/${searchId}`,
    method: 'post',
    data: trainingData
  })
}

/**
 * 计算语义相似度
 */
export function calculateSemanticSimilarity(tenantId, searchId, text1, text2) {
  return request({
    url: `/accountant/as/archiveSearch/calculateSimilarity/${tenantId}/${searchId}`,
    method: 'post',
    params: { text1, text2 }
  })
}

/**
 * 语义向量化
 */
export function semanticVectorization(tenantId, searchId, text) {
  return request({
    url: `/accountant/as/archiveSearch/vectorization/${tenantId}/${searchId}`,
    method: 'post',
    params: { text }
  })
}

// ==================== 推荐系统接口 ====================

/**
 * 启动推荐系统
 */
export function startRecommendationSystem(tenantId, searchId, recommendConfig) {
  return request({
    url: `/accountant/as/archiveSearch/startRecommendation/${tenantId}/${searchId}`,
    method: 'post',
    data: recommendConfig
  })
}

/**
 * 停止推荐系统
 */
export function stopRecommendationSystem(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/stopRecommendation/${tenantId}/${searchId}`,
    method: 'post'
  })
}

/**
 * 获取推荐结果
 */
export function getRecommendations(tenantId, searchId, userId, context = {}) {
  return request({
    url: `/accountant/as/archiveSearch/getRecommendations/${tenantId}/${searchId}`,
    method: 'get',
    params: { userId },
    data: context
  })
}

/**
 * 更新推荐算法
 */
export function updateRecommendationAlgorithm(tenantId, searchId, algorithm) {
  return request({
    url: `/accountant/as/archiveSearch/updateRecommendationAlgorithm/${tenantId}/${searchId}`,
    method: 'put',
    params: { algorithm }
  })
}

/**
 * 训练推荐模型
 */
export function trainRecommendationModel(tenantId, searchId, trainingData) {
  return request({
    url: `/accountant/as/archiveSearch/trainRecommendationModel/${tenantId}/${searchId}`,
    method: 'post',
    data: trainingData
  })
}

// ==================== 性能优化接口 ====================

/**
 * 启动性能优化
 */
export function startPerformanceOptimization(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/startOptimization/${tenantId}/${searchId}`,
    method: 'post'
  })
}

/**
 * 停止性能优化
 */
export function stopPerformanceOptimization(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/stopOptimization/${tenantId}/${searchId}`,
    method: 'post'
  })
}

/**
 * 配置缓存策略
 */
export function configureCacheStrategy(tenantId, searchId, cacheConfig) {
  return request({
    url: `/accountant/as/archiveSearch/configureCacheStrategy/${tenantId}/${searchId}`,
    method: 'post',
    data: cacheConfig
  })
}

/**
 * 清理缓存
 */
export function clearCache(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/clearCache/${tenantId}/${searchId}`,
    method: 'post'
  })
}

/**
 * 预热缓存
 */
export function warmupCache(tenantId, searchId, keywords) {
  return request({
    url: `/accountant/as/archiveSearch/warmupCache/${tenantId}/${searchId}`,
    method: 'post',
    data: keywords
  })
}

// ==================== 查询统计接口 ====================

/**
 * 根据检索类型查询
 */
export function getArchiveSearchesByType(tenantId, searchType) {
  return request({
    url: `/accountant/as/archiveSearch/getByType/${tenantId}`,
    method: 'get',
    params: { searchType }
  })
}

/**
 * 根据检索状态查询
 */
export function getArchiveSearchesByStatus(tenantId, searchStatus) {
  return request({
    url: `/accountant/as/archiveSearch/getByStatus/${tenantId}`,
    method: 'get',
    params: { searchStatus }
  })
}

/**
 * 根据检索引擎查询
 */
export function getArchiveSearchesByEngine(tenantId, searchEngine) {
  return request({
    url: `/accountant/as/archiveSearch/getByEngine/${tenantId}`,
    method: 'get',
    params: { searchEngine }
  })
}

/**
 * 查询活跃的检索配置
 */
export function getActiveArchiveSearches(tenantId) {
  return request({
    url: `/accountant/as/archiveSearch/getActive/${tenantId}`,
    method: 'get'
  })
}

/**
 * 查询正在索引的检索配置
 */
export function getIndexingArchiveSearches(tenantId) {
  return request({
    url: `/accountant/as/archiveSearch/getIndexing/${tenantId}`,
    method: 'get'
  })
}

// ==================== 统计分析接口 ====================

/**
 * 统计检索状态分布
 */
export function countBySearchStatus(tenantId) {
  return request({
    url: `/accountant/as/archiveSearch/countByStatus/${tenantId}`,
    method: 'get'
  })
}

/**
 * 统计检索类型分布
 */
export function countBySearchType(tenantId) {
  return request({
    url: `/accountant/as/archiveSearch/countByType/${tenantId}`,
    method: 'get'
  })
}

/**
 * 统计检索引擎分布
 */
export function countBySearchEngine(tenantId) {
  return request({
    url: `/accountant/as/archiveSearch/countByEngine/${tenantId}`,
    method: 'get'
  })
}

/**
 * 获取检索趋势
 */
export function getSearchTrend(tenantId, startDate, endDate, granularity = 'day') {
  return request({
    url: `/accountant/as/archiveSearch/getSearchTrend/${tenantId}`,
    method: 'get',
    params: { startDate, endDate, granularity }
  })
}

/**
 * 获取热门关键词
 */
export function getPopularKeywords(tenantId, limit = 10) {
  return request({
    url: `/accountant/as/archiveSearch/getPopularKeywords/${tenantId}`,
    method: 'get',
    params: { limit }
  })
}

/**
 * 获取性能指标
 */
export function getPerformanceMetrics(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/getPerformanceMetrics/${tenantId}/${searchId}`,
    method: 'get'
  })
}

/**
 * 获取性能趋势
 */
export function getPerformanceTrend(tenantId, startDate, endDate, granularity = 'day') {
  return request({
    url: `/accountant/as/archiveSearch/getPerformanceTrend/${tenantId}`,
    method: 'get',
    params: { startDate, endDate, granularity }
  })
}

/**
 * 获取检索排名
 */
export function getSearchRanking(tenantId, limit = 10) {
  return request({
    url: `/accountant/as/archiveSearch/getSearchRanking/${tenantId}`,
    method: 'get',
    params: { limit }
  })
}

// ==================== 批量操作接口 ====================

/**
 * 批量创建检索配置
 */
export function batchCreateArchiveSearches(archiveSearches) {
  return request({
    url: '/accountant/as/archiveSearch/batchCreate',
    method: 'post',
    data: archiveSearches
  })
}

/**
 * 批量更新检索状态
 */
export function batchUpdateStatus(tenantId, searchIds, status) {
  return request({
    url: `/accountant/as/archiveSearch/batchUpdateStatus/${tenantId}`,
    method: 'put',
    data: searchIds,
    params: { status }
  })
}

/**
 * 批量更新检索引擎
 */
export function batchUpdateEngine(tenantId, searchIds, engine) {
  return request({
    url: `/accountant/as/archiveSearch/batchUpdateEngine/${tenantId}`,
    method: 'put',
    data: searchIds,
    params: { engine }
  })
}

/**
 * 批量删除检索配置
 */
export function batchDeleteArchiveSearches(tenantId, searchIds) {
  return request({
    url: `/accountant/as/archiveSearch/batchDelete/${tenantId}`,
    method: 'delete',
    data: searchIds
  })
}

/**
 * 批量重建索引
 */
export function batchRebuildIndex(tenantId, searchIds) {
  return request({
    url: `/accountant/as/archiveSearch/batchRebuildIndex/${tenantId}`,
    method: 'post',
    data: searchIds
  })
}

// ==================== 数据管理接口 ====================

/**
 * 导出检索数据
 */
export function exportArchiveSearchData(tenantId, searchIds) {
  return request({
    url: `/accountant/as/archiveSearch/export/${tenantId}`,
    method: 'get',
    data: searchIds
  })
}

/**
 * 导入检索数据
 */
export function importArchiveSearchData(tenantId, searchData) {
  return request({
    url: `/accountant/as/archiveSearch/import/${tenantId}`,
    method: 'post',
    data: searchData
  })
}

/**
 * 清理过期数据
 */
export function cleanupExpiredData(tenantId, expiredDate) {
  return request({
    url: `/accountant/as/archiveSearch/cleanupExpiredData/${tenantId}`,
    method: 'post',
    params: { expiredDate }
  })
}

/**
 * 清理无效索引
 */
export function cleanupInvalidIndexes(tenantId) {
  return request({
    url: `/accountant/as/archiveSearch/cleanupInvalidIndexes/${tenantId}`,
    method: 'post'
  })
}

// ==================== 系统维护接口 ====================

/**
 * 获取系统概览
 */
export function getSystemOverview(tenantId) {
  return request({
    url: `/accountant/as/archiveSearch/getSystemOverview/${tenantId}`,
    method: 'get'
  })
}

/**
 * 生成检索报告
 */
export function generateSearchReport(tenantId, searchId) {
  return request({
    url: `/accountant/as/archiveSearch/generateReport/${tenantId}/${searchId}`,
    method: 'post'
  })
}

/**
 * 检查检索健康状态
 */
export function checkSearchHealth(tenantId) {
  return request({
    url: `/accountant/as/archiveSearch/checkHealth/${tenantId}`,
    method: 'get'
  })
}

// ==================== 工具函数 ====================

/**
 * 格式化检索类型显示文本
 */
export function formatSearchType(type) {
  const typeMap = {
    [SEARCH_TYPES.FULL_TEXT]: '全文检索',
    [SEARCH_TYPES.SEMANTIC]: '语义检索',
    [SEARCH_TYPES.IMAGE]: '图像检索',
    [SEARCH_TYPES.VOICE]: '语音检索',
    [SEARCH_TYPES.HYBRID]: '混合检索'
  }
  return typeMap[type] || type
}

/**
 * 格式化检索状态显示文本
 */
export function formatSearchStatus(status) {
  const statusMap = {
    [SEARCH_STATUS.ACTIVE]: '活跃',
    [SEARCH_STATUS.INACTIVE]: '非活跃',
    [SEARCH_STATUS.INDEXING]: '索引中',
    [SEARCH_STATUS.OPTIMIZING]: '优化中',
    [SEARCH_STATUS.ERROR]: '错误'
  }
  return statusMap[status] || status
}

/**
 * 格式化检索引擎显示文本
 */
export function formatSearchEngine(engine) {
  const engineMap = {
    [SEARCH_ENGINES.ELASTICSEARCH]: 'Elasticsearch',
    [SEARCH_ENGINES.SOLR]: 'Apache Solr',
    [SEARCH_ENGINES.LUCENE]: 'Apache Lucene',
    [SEARCH_ENGINES.CUSTOM]: '自定义引擎'
  }
  return engineMap[engine] || engine
}

/**
 * 格式化索引状态显示文本
 */
export function formatIndexStatus(status) {
  const statusMap = {
    [INDEX_STATUS.BUILDING]: '构建中',
    [INDEX_STATUS.READY]: '就绪',
    [INDEX_STATUS.UPDATING]: '更新中',
    [INDEX_STATUS.OPTIMIZING]: '优化中',
    [INDEX_STATUS.ERROR]: '错误',
    [INDEX_STATUS.DELETED]: '已删除'
  }
  return statusMap[status] || status
}

/**
 * 格式化推荐算法显示文本
 */
export function formatRecommendationAlgorithm(algorithm) {
  const algorithmMap = {
    [RECOMMENDATION_ALGORITHMS.COLLABORATIVE]: '协同过滤',
    [RECOMMENDATION_ALGORITHMS.CONTENT_BASED]: '基于内容',
    [RECOMMENDATION_ALGORITHMS.HYBRID]: '混合推荐'
  }
  return algorithmMap[algorithm] || algorithm
}

/**
 * 格式化准确率显示
 */
export function formatAccuracy(accuracy) {
  if (!accuracy) return '-'
  return `${(accuracy * 100).toFixed(2)}%`
}

/**
 * 格式化响应时间显示
 */
export function formatResponseTime(time) {
  if (!time) return '-'
  if (time < 1000) return `${time}ms`
  return `${(time / 1000).toFixed(2)}s`
}

/**
 * 格式化文件大小显示
 */
export function formatFileSize(size) {
  if (!size) return '-'
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let unitIndex = 0
  let fileSize = size

  while (fileSize >= 1024 && unitIndex < units.length - 1) {
    fileSize /= 1024
    unitIndex++
  }

  return `${fileSize.toFixed(2)} ${units[unitIndex]}`
}

/**
 * 计算成功率
 */
export function calculateSuccessRate(successCount, totalCount) {
  if (!totalCount || totalCount === 0) return 0
  return (successCount / totalCount * 100).toFixed(2)
}

/**
 * 验证检索配置
 */
export function validateSearchConfig(config) {
  const errors = []

  if (!config.searchName) {
    errors.push('检索名称不能为空')
  }

  if (!config.searchType) {
    errors.push('检索类型不能为空')
  }

  if (!config.searchEngine) {
    errors.push('检索引擎不能为空')
  }

  if (config.searchType === SEARCH_TYPES.SEMANTIC && !config.semanticConfig) {
    errors.push('语义检索需要配置语义模型')
  }

  if (config.searchType === SEARCH_TYPES.IMAGE && !config.ocrConfig) {
    errors.push('图像检索需要配置OCR识别')
  }

  return {
    isValid: errors.length === 0,
    errors
  }
}

// ==================== 快捷操作函数 ====================

/**
 * 快速创建全文检索配置
 */
export function quickCreateFullTextSearch(tenantId, name, indexName) {
  const searchConfig = {
    tenantId,
    searchName: name,
    searchType: SEARCH_TYPES.FULL_TEXT,
    searchEngine: SEARCH_ENGINES.ELASTICSEARCH,
    searchStatus: SEARCH_STATUS.INACTIVE,
    indexName,
    indexConfig: JSON.stringify({
      analyzer: 'ik_max_word',
      mappings: {
        properties: {
          title: { type: 'text', analyzer: 'ik_max_word' },
          content: { type: 'text', analyzer: 'ik_max_word' },
          createTime: { type: 'date' }
        }
      }
    })
  }

  return createArchiveSearch(searchConfig)
}

/**
 * 快速启动检索服务
 */
export function quickStartSearch(tenantId, searchId) {
  return startSearchService(tenantId, searchId)
    .then(() => createIndex(tenantId, searchId, {}))
    .then(() => ({ success: true, message: '检索服务启动成功' }))
    .catch(error => ({ success: false, message: error.message }))
}

/**
 * 快速执行全文检索
 */
export function quickFullTextSearch(tenantId, searchId, keywords) {
  return fullTextSearch(tenantId, searchId, keywords, {
    size: 20,
    highlight: {
      fields: {
        title: {},
        content: {}
      }
    }
  })
}

/**
 * 快速获取检索概览
 */
export function quickGetSearchOverview(tenantId) {
  return Promise.all([
    getSystemOverview(tenantId),
    countBySearchStatus(tenantId),
    countBySearchType(tenantId),
    countBySearchEngine(tenantId)
  ]).then(([overview, statusStats, typeStats, engineStats]) => ({
    overview,
    statusStats,
    typeStats,
    engineStats
  }))
}

/**
 * 快速批量操作
 */
export function quickBatchOperation(tenantId, searchIds, operation, params = {}) {
  switch (operation) {
    case 'start':
      return Promise.all(searchIds.map(id => startSearchService(tenantId, id)))
    case 'stop':
      return Promise.all(searchIds.map(id => stopSearchService(tenantId, id)))
    case 'delete':
      return batchDeleteArchiveSearches(tenantId, searchIds)
    case 'rebuildIndex':
      return batchRebuildIndex(tenantId, searchIds)
    case 'updateStatus':
      return batchUpdateStatus(tenantId, searchIds, params.status)
    case 'updateEngine':
      return batchUpdateEngine(tenantId, searchIds, params.engine)
    default:
      return Promise.reject(new Error('不支持的批量操作类型'))
  }
}
