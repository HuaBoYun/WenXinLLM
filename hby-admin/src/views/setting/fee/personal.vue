<template>
  <div class="fee-personal-container">
    <!-- 汇总卡片 -->
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">本月费用</div>
            <div class="stat-value">¥ {{ totalFee || '0.00' }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">查询范围费用</div>
            <div class="stat-value">¥ {{ totalFee || '0.00' }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">记录总数</div>
            <div class="stat-value">{{ total }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 筛选条件 -->
    <div class="filter-bar">
      <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" style="margin-right: 10px;" />
      <el-select v-model="queryForm.moduleType" placeholder="大模块" clearable style="width: 150px; margin-right: 10px;">
        <el-option v-for="item in moduleList" :key="item.id" :label="item.projectName" :value="item.uniqueIdentification" />
      </el-select>
      <el-select v-model="queryForm.subModuleName" placeholder="小模块" clearable filterable style="width: 150px; margin-right: 10px;">
        <el-option v-for="item in subModuleList" :key="item.value" :label="item.label" :value="item.value" />
      </el-select>
      <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
      <el-button icon="el-icon-download" @click="handleExport">导出</el-button>
    </div>

    <!-- 图表区域 -->
    <div class="chart-section">
      <el-radio-group v-model="chartType" size="small" style="margin-bottom: 10px;">
        <el-radio-button label="pie">扇形</el-radio-button>
        <el-radio-button label="line">折线</el-radio-button>
        <el-radio-button label="bar">柱形</el-radio-button>
      </el-radio-group>
      <div ref="chart" style="width: 100%; height: 350px; margin: 15px 0;"></div>
    </div>

    <!-- 明细表格 -->
    <el-table v-loading="loading" :data="tableData" border style="width: 100%; margin-top: 15px;">
      <el-table-column label="大模块" width="130">
        <template slot-scope="{ row }">{{ translateName(row.moduleName) }}</template>
      </el-table-column>
      <el-table-column label="小模块" width="130">
        <template slot-scope="{ row }">{{ translateName(row.subModuleName) }}</template>
      </el-table-column>
      <el-table-column label="页面" width="130">
        <template slot-scope="{ row }">{{ translateName(row.pageName) }}</template>
      </el-table-column>
      <el-table-column prop="apiSummary" label="接口描述" min-width="180" />
      <el-table-column prop="feeAmount" label="费用(元)" width="100" align="right" />
      <el-table-column prop="pageRoute" label="页面路由" width="150" />
      <el-table-column label="调用时间" width="170" align="center">
        <template slot-scope="{ row }">{{ formatDate(row.createTime) }}</template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination style="margin-top: 15px; text-align: right;" @current-change="handlePageChange" @size-change="handleSizeChange" :current-page="queryForm.pageNum" :page-sizes="[20, 50, 100]" :page-size="queryForm.pageSize" :total="total" layout="total, sizes, prev, pager, next, jumper" />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getPersonalFeeRecords, exportFeeRecords, getFeeStandardList } from '@/api/setting/fee'
import { getModuleList } from '@/api/setting/system'

/**
 * 递归遍历费用标准页面树，建立完整的翻译映射
 * @param {Array} nodes - 树节点
 * @param {Object} map - path/id → 中文名 映射表
 * @param {Object} idNameMap - id → name 映射（用于查父节点名）
 */
function flattenStandardTree(nodes, map, idNameMap) {
  if (!nodes || !Array.isArray(nodes)) return
  nodes.forEach(function(node) {
    // path → name（路由段翻译）
    if (node.path && node.name) {
      map[node.path] = node.name
    }
    // id → name（rightId 翻译 + 父节点查找）
    if (node.id && node.name) {
      var idStr = String(node.id)
      map[idStr] = node.name
      idNameMap[idStr] = node.name
    }
    if (node.children) {
      flattenStandardTree(node.children, map, idNameMap)
    }
  })
}

export default {
  name: 'FeePersonal',
  data() {
    return {
      loading: false,
      moduleList: [],
      subModuleList: [],
      dateRange: [],
      queryForm: { startTime: null, endTime: null, moduleType: '', subModuleName: '', rightId: null, pageNum: 1, pageSize: 20 },
      tableData: [],
      total: 0,
      totalFee: '0.00',
      chartType: 'pie',
      chartInstance: null,
      serverChartData: [],
      nameMap: {},  // 英文路由段 → 中文菜单名 映射表
    }
  },
  watch: {
    chartType() { this.renderChart() },
    serverChartData() { this.renderChart() },
    'queryForm.moduleType'() {
      this.queryForm.subModuleName = ''
    },
  },
  created() {
    this.buildNameMap()
    this.fetchModuleList()
  },
  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.dispose()
      this.chartInstance = null
    }
  },
  methods: {
    /**
     * 从费用标准接口获取 TBL_SYSTEM_RIGHT 全量页面树，构建完整翻译映射
     * 这个接口和费用标准配置页面用的是同一个数据源，包含所有模块的所有页面
     */
    async buildNameMap() {
      const map = {}
      const idNameMap = {}
      try {
        // 1. 获取模块列表 → uniqueIdentification → projectName
        const modRes = await getModuleList({})
        const modules = (modRes.code === 1 || modRes.code === 200) ? (modRes.data || []) : []
        modules.forEach(function(m) {
          if (m.uniqueIdentification && m.projectName) {
            map[m.uniqueIdentification] = m.projectName
          }
        })

        // 2. 逐模块请求费用标准页面树（和 standard.vue 同一个接口）
        //    不传 moduletype 参数会返回全部，但数据量可能很大，按模块分批更稳
        const stdPromises = modules
          .filter(function(m) { return m.uniqueIdentification && m.uniqueIdentification !== 'wdyg' })
          .map(function(m) {
            return getFeeStandardList({ moduletype: m.uniqueIdentification }).catch(function() { return null })
          })
        const stdResults = await Promise.all(stdPromises)
        stdResults.forEach(function(res) {
          if (res && (res.code === 1 || res.code === 200) && res.data && res.data.list) {
            flattenStandardTree(res.data.list, map, idNameMap)
          }
        })
      } catch (e) {
        console.warn('[计费] 构建页面映射表失败', e)
      }
      this.nameMap = map
      this.handleSearch()
    },
    /**
     * 翻译名称：如果是英文路由段则查映射表，已经是中文则直接返回
     */
    translateName(val) {
      if (!val) return ''
      // 已经是中文，直接返回
      if (/[\u4e00-\u9fa5]/.test(val)) return val
      // 查映射表
      return this.nameMap[val] || val
    },
    aggregateByModule() {
      const map = {}
      this.tableData.forEach((row) => {
        const name = row.moduleName || '未知模块'
        const amount = parseFloat(row.feeAmount) || 0
        map[name] = (map[name] || 0) + amount
      })
      return Object.keys(map).map((name) => ({ name, value: parseFloat(map[name].toFixed(2)) }))
    },
    renderChart() {
      this.$nextTick(() => {
        if (this.chartInstance) {
          this.chartInstance.dispose()
          this.chartInstance = null
        }
        const el = this.$refs.chart
        if (!el) return
        this.chartInstance = echarts.init(el)
        const aggregated = this.serverChartData || []
        const names = aggregated.map((d) => d.name)
        const values = aggregated.map((d) => d.value)
        let option = {}
        if (this.chartType === 'pie') {
          // 图例分左右两列显示，避免遮挡饼图
          const half = Math.ceil(names.length / 2)
          const leftNames = names.slice(0, half)
          const rightNames = names.slice(half)
          option = {
            tooltip: { trigger: 'item', formatter: '{b}: ¥{c} ({d}%)' },
            legend: [
              {
                orient: 'vertical',
                left: 10,
                top: 'middle',
                data: leftNames,
                textStyle: { fontSize: 12 },
                itemWidth: 14,
                itemHeight: 10,
                formatter: function(name) {
                  return name.length > 8 ? name.substring(0, 8) + '...' : name
                },
              },
              {
                orient: 'vertical',
                right: 10,
                top: 'middle',
                data: rightNames,
                textStyle: { fontSize: 12 },
                itemWidth: 14,
                itemHeight: 10,
                formatter: function(name) {
                  return name.length > 8 ? name.substring(0, 8) + '...' : name
                },
              },
            ],
            series: [{
              type: 'pie',
              radius: ['30%', '55%'],
              center: ['50%', '50%'],
              data: aggregated,
              label: {
                show: true,
                formatter: '{b}: ¥{c}',
                fontSize: 11,
              },
              labelLine: {
                show: true,
                length: 15,
                length2: 10,
              },
              emphasis: {
                label: { show: true, fontSize: 13, fontWeight: 'bold', formatter: '{b}\n¥{c} ({d}%)' },
                itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.5)' },
              },
            }],
          }
        } else {
          option = {
            tooltip: { trigger: 'axis', formatter: function(params) { var p = params[0]; return p.name + ': ¥' + p.value } },
            xAxis: { type: 'category', data: names, axisLabel: { rotate: names.length > 5 ? 30 : 0 } },
            yAxis: { type: 'value', name: '费用(元)' },
            series: [{ type: this.chartType, data: values, barMaxWidth: 40 }],
          }
        }
        this.chartInstance.setOption(option)
      })
    },
    async fetchModuleList() {
      try {
        const res = await getModuleList({})
        if (res.code === 1 || res.code === 200) this.moduleList = res.data || []
      } catch (e) { console.error(e) }
    },

    async handleSearch() {
      if (this.dateRange && this.dateRange.length === 2) {
        this.queryForm.startTime = this.dateRange[0] + ' 00:00:00'
        this.queryForm.endTime = this.dateRange[1] + ' 23:59:59'
      } else {
        this.queryForm.startTime = null
        this.queryForm.endTime = null
      }
      this.queryForm.pageNum = 1
      this.fetchData()
    },
    async fetchData() {
      this.loading = true
      try {
        const res = await getPersonalFeeRecords(this.queryForm)
        if (res.code === 1 || res.code === 200) {
          const data = res.data || {}
          this.tableData = data.list || []
          this.total = data.total || 0
          this.totalFee = data.totalFee != null ? Number(data.totalFee).toFixed(2) : '0.00'
          // 用后端返回的全量聚合数据更新统计图
          var chartList = data.chartData || []
          this.serverChartData = chartList.map(item => ({
            name: this.translateName(item.SUB_MODULE_NAME) || '未知',
            value: parseFloat(item.TOTAL_FEE) || 0,
          }))
          if (!this.queryForm.subModuleName) {
            var self = this
            this.subModuleList = chartList
              .filter(function(item) { return item.SUB_MODULE_NAME })
              .map(function(item) {
                var translated = self.translateName(item.SUB_MODULE_NAME)
                return { label: translated || item.SUB_MODULE_NAME, value: item.SUB_MODULE_NAME }
              })
          }
        }
      } catch (e) { console.error(e) }
      this.loading = false
    },
    async handleExport() {
      try {
        const res = await exportFeeRecords(this.queryForm)
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '费用明细.xlsx'
        a.click()
        window.URL.revokeObjectURL(url)
      } catch (e) {
        this.$message.error('导出失败')
      }
    },
    handlePageChange(val) { this.queryForm.pageNum = val; this.fetchData() },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.queryForm.pageNum = 1; this.fetchData() },
    formatDate(val) {
      if (!val) return ''
      const d = new Date(val)
      const pad = (n) => (n < 10 ? '0' + n : n)
      return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds())
    },
  },
}
</script>

<style lang="scss" scoped>
.fee-personal-container { padding: 20px; }
.filter-bar { display: flex; align-items: center; flex-wrap: wrap; }
.stat-card { text-align: center; }
.stat-label { font-size: 14px; color: #909399; margin-bottom: 8px; }
.stat-value { font-size: 24px; font-weight: bold; color: #303133; }
.chart-section { margin-top: 15px; background: #fff; padding: 15px; border-radius: 4px; }
</style>
