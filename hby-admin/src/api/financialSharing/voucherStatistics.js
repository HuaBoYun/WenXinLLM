import request from '@/utils/request'

// 凭证汇总统计
export function getVoucherSummary(data) {
  return request({
    url: '/cwgxAi/voucher/statistics/summary',
    method: 'post',
    data
  })
}

// 凭证数量统计(按状态)
export function getVoucherCountByStatus(data) {
  return request({
    url: '/cwgxAi/voucher/statistics/count-by-status',
    method: 'post',
    data
  })
}

// 凭证数量统计(按期间)
export function getVoucherCountByPeriod(data) {
  return request({
    url: '/cwgxAi/voucher/statistics/count-by-period',
    method: 'post',
    data
  })
}

// 凭证数量统计(按凭证类型)
export function getVoucherCountByType(data) {
  return request({
    url: '/cwgxAi/voucher/statistics/count-by-type',
    method: 'post',
    data
  })
}

// 凭证数量统计(按制单人)
export function getVoucherCountByPreparer(data) {
  return request({
    url: '/cwgxAi/voucher/statistics/count-by-preparer',
    method: 'post',
    data
  })
}

// 分币种统计
export function getVoucherCountByCurrency(data) {
  return request({
    url: '/cwgxAi/voucher/statistics/count-by-currency',
    method: 'post',
    data
  })
}

// 凭证分录统计
export function getVoucherEntryStatistics(data) {
  return request({
    url: '/cwgxAi/voucher/statistics/entry-statistics',
    method: 'post',
    data
  })
}

// 凭证统计趋势
export function getVoucherStatisticsTrend(data) {
  return request({
    url: '/cwgxAi/voucher/statistics/trend',
    method: 'post',
    data
  })
}

// 获取统计筛选条件
export function getFilterOptions(bookId) {
  return request({
    url: '/cwgxAi/voucher/statistics/filter-options',
    method: 'get',
    params: { bookId }
  })
}

// 凭证统计报表导出
export function exportVoucherStatistics(data) {
  return request({
    url: '/cwgxAi/voucher/statistics/export',
    method: 'post',
    data
  })
}

// 获取凭证统计仪表盘数据
export function getVoucherStatisticsDashboard(data) {
  return request({
    url: '/cwgxAi/voucher/statistics/dashboard',
    method: 'post',
    data
  })
}

// ========== 兼容旧接口 ==========

// 获取凭证统计信息（兼容旧接口）
export function getVoucherStatistics(data) {
  return request({
    url: '/cwgxAi/voucher/statistics',
    method: 'post',
    data
  })
}

// 获取仪表板统计数据（兼容旧接口）
export function getDashboardStatistics(params) {
  return request({
    url: '/cwgxAi/voucher/statistics/dashboard',
    method: 'post',
    params
  })
}

// 获取凭证趋势数据（兼容旧接口）
export function getVoucherTrend(data) {
  return request({
    url: '/cwgxAi/voucher/statistics/trend',
    method: 'post',
    data
  })
}

// 获取凭证汇总数据（兼容旧接口）
export function getVoucherSummaryData(params) {
  return request({
    url: '/cwgxAi/voucher/statistics/summary',
    method: 'get',
    params
  })
}

// 按状态统计凭证（兼容旧接口）
export function getStatisticsByStatus(params) {
  return request({
    url: '/cwgxAi/voucher/statistics/by-status',
    method: 'get',
    params
  })
}

// 按类型统计凭证（兼容旧接口）
export function getStatisticsByType(params) {
  return request({
    url: '/cwgxAi/voucher/statistics/by-type',
    method: 'get',
    params
  })
}

// ========== 工具函数 ==========

// 格式化统计数据
export function formatStatisticsData(data, type) {
  if (!data) return []

  switch (type) {
    case 'status':
      return data.map(item => ({
        name: item.statusName || item.name,
        value: item.count,
        amount: item.amount || 0,
        percentage: calculatePercentage(item.count, getTotalCount(data))
      }))
    case 'period':
      return data.map(item => ({
        name: item.period || item.name,
        value: item.count,
        amount: item.amount || 0
      }))
    case 'type':
      return data.map(item => ({
        name: item.typeName || item.name,
        value: item.count,
        amount: item.amount || 0
      }))
    case 'preparer':
      return data.map(item => ({
        name: item.preparerName || item.name,
        value: item.count,
        amount: item.amount || 0
      }))
    case 'currency':
      return data.map(item => ({
        name: item.currencyName || item.name,
        value: item.count,
        debitAmount: item.debitAmount || 0,
        creditAmount: item.creditAmount || 0
      }))
    default:
      return data
  }
}

// 计算百分比
function calculatePercentage(value, total) {
  if (!total || total === 0) return 0
  return ((value / total) * 100).toFixed(2)
}

// 获取总数
function getTotalCount(data) {
  return data.reduce((sum, item) => sum + (item.count || 0), 0)
}

// 格式化金额
export function formatAmount(amount) {
  if (!amount) return '0.00'
  return parseFloat(amount).toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

// 生成统计图表配置
export function generateChartOptions(data, chartType, title) {
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#606266']

  switch (chartType) {
    case 'pie':
      return {
        title: {
          text: title,
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: title,
            type: 'pie',
            radius: '50%',
            data: data.map(item => ({
              value: item.value,
              name: item.name
            })),
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
    case 'bar':
      return {
        title: {
          text: title,
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.name)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '数量',
            type: 'bar',
            data: data.map(item => item.value),
            itemStyle: {
              color: colors[0]
            }
          }
        ]
      }
    case 'line':
      return {
        title: {
          text: title,
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.name)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '数量',
            type: 'line',
            data: data.map(item => item.value),
            smooth: true,
            itemStyle: {
              color: colors[0]
            }
          }
        ]
      }
    default:
      return {}
  }
}