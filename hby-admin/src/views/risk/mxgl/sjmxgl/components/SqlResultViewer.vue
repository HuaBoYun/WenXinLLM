<template>
  <div class="sql-result-viewer">
    <!-- 结果工具栏 -->
    <div class="result-toolbar">
      <div class="toolbar-left">
        <el-button-group>
          <el-button 
            :type="viewMode === 'table' ? 'primary' : ''" 
            size="mini" 
            @click="viewMode = 'table'"
          >
            <i class="el-icon-s-grid"></i> 表格视图
          </el-button>
          <el-button 
            :type="viewMode === 'json' ? 'primary' : ''" 
            size="mini" 
            @click="viewMode = 'json'"
          >
            <i class="el-icon-document"></i> JSON视图
          </el-button>
          <el-button 
            :type="viewMode === 'chart' ? 'primary' : ''" 
            size="mini" 
            @click="viewMode = 'chart'"
          >
            <i class="el-icon-data-line"></i> 图表视图
          </el-button>
        </el-button-group>
      </div>
      
      <div class="toolbar-center">
        <span v-if="resultData && resultData.records" class="result-info">
          <i class="el-icon-info"></i>
          共 {{ resultData.total || resultData.records.length }} 条记录
          <span v-if="executionTime">，执行耗时 {{ executionTime }}ms</span>
        </span>
      </div>
      
      <div class="toolbar-right">
        <el-dropdown @command="handleExport">
          <el-button size="mini">
            <i class="el-icon-download"></i> 导出 <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="excel">导出为Excel</el-dropdown-item>
            <el-dropdown-item command="csv">导出为CSV</el-dropdown-item>
            <el-dropdown-item command="json">导出为JSON</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        
        <el-button size="mini" @click="refreshData">
          <i class="el-icon-refresh"></i> 刷新
        </el-button>
      </div>
    </div>

    <!-- 结果内容 -->
    <div class="result-content" v-loading="loading">
      <!-- 无数据状态 -->
      <div v-if="!resultData || !resultData.records" class="empty-state">
        <div class="empty-icon">
          <i class="el-icon-data-line"></i>
        </div>
        <div class="empty-text">
          <p v-if="loading">正在执行SQL查询...</p>
          <p v-else>暂无查询结果</p>
          <p class="empty-tip">请先执行SQL语句查看结果</p>
        </div>
      </div>

      <!-- 表格视图 -->
      <div v-else-if="viewMode === 'table'" class="table-view">
        <el-table
          :data="currentPageData"
          border
          stripe
          size="mini"
          max-height="400"
          :header-cell-style="{ background: '#f8f9fa', color: '#333' }"
        >
          <el-table-column
            v-for="column in tableColumns"
            :key="column.prop"
            :prop="column.prop"
            :label="column.label"
            :width="column.width"
            :min-width="column.minWidth"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span v-if="isDateColumn(column.prop)" class="date-cell">
                {{ formatDate(scope.row[column.prop]) }}
              </span>
              <span v-else-if="isNumberColumn(column.prop)" class="number-cell">
                {{ formatNumber(scope.row[column.prop]) }}
              </span>
              <span v-else>
                {{ scope.row[column.prop] }}
              </span>
            </template>
          </el-table-column>
        </el-table>
        
        <!-- 分页 -->
        <div class="pagination-wrapper" v-if="resultData.records.length > pageSize">
          <el-pagination
            :current-page="currentPage"
            :page-size="pageSize"
            :total="resultData.records.length"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>

      <!-- JSON视图 -->
      <div v-else-if="viewMode === 'json'" class="json-view">
        <div class="json-toolbar">
          <el-button size="mini" @click="copyJSON">
            <i class="el-icon-document-copy"></i> 复制JSON
          </el-button>
          <el-button size="mini" @click="formatJSON">
            <i class="el-icon-magic-stick"></i> 格式化
          </el-button>
        </div>
        <pre class="json-content">{{ formattedJSON }}</pre>
      </div>

      <!-- 图表视图 -->
      <div v-else-if="viewMode === 'chart'" class="chart-view">
        <div class="chart-config">
          <el-form inline size="mini">
            <el-form-item label="图表类型">
              <el-select v-model="chartConfig.type" @change="updateChart">
                <el-option label="柱状图" value="bar" />
                <el-option label="折线图" value="line" />
                <el-option label="饼图" value="pie" />
                <el-option label="散点图" value="scatter" />
              </el-select>
            </el-form-item>
            <el-form-item label="X轴字段">
              <el-select v-model="chartConfig.xField" @change="updateChart">
                <el-option
                  v-for="column in tableColumns"
                  :key="column.prop"
                  :label="column.label"
                  :value="column.prop"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="Y轴字段">
              <el-select v-model="chartConfig.yField" @change="updateChart">
                <el-option
                  v-for="column in numberColumns"
                  :key="column.prop"
                  :label="column.label"
                  :value="column.prop"
                />
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        
        <div class="chart-container" ref="chartContainer">
          <!-- 这里可以集成ECharts或其他图表库 -->
          <div class="chart-placeholder">
            <i class="el-icon-data-analysis"></i>
            <p>图表功能开发中...</p>
            <p>请选择合适的字段配置图表</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 错误信息 -->
    <div v-if="errorMessage" class="error-message">
      <el-alert
        :title="errorMessage"
        type="error"
        :closable="false"
        show-icon
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'SqlResultViewer',
  props: {
    resultData: {
      type: Object,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    },
    executionTime: {
      type: Number,
      default: null
    },
    errorMessage: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      viewMode: 'table', // table, json, chart
      currentPage: 1,
      pageSize: 20,
      
      // 图表配置
      chartConfig: {
        type: 'bar',
        xField: '',
        yField: ''
      }
    }
  },
  computed: {
    // 表格列配置
    tableColumns() {
      if (!this.resultData || !this.resultData.records || this.resultData.records.length === 0) {
        return []
      }
      
      const firstRow = this.resultData.records[0]
      return Object.keys(firstRow).map(key => ({
        prop: key,
        label: this.formatColumnLabel(key),
        width: this.getColumnWidth(key, firstRow[key]),
        minWidth: 100
      }))
    },
    
    // 数字类型列
    numberColumns() {
      return this.tableColumns.filter(col => this.isNumberColumn(col.prop))
    },
    
    // 当前页数据
    currentPageData() {
      if (!this.resultData || !this.resultData.records) return []
      
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.resultData.records.slice(start, end)
    },
    
    // 格式化的JSON
    formattedJSON() {
      if (!this.resultData) return ''
      return JSON.stringify(this.resultData, null, 2)
    }
  },
  watch: {
    resultData: {
      handler(newData) {
        if (newData && newData.records && newData.records.length > 0) {
          this.currentPage = 1
          this.initChartConfig()
        }
      },
      immediate: true
    }
  },
  methods: {
    // 格式化列标签
    formatColumnLabel(key) {
      // 将下划线转换为空格，首字母大写
      return key.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase())
    },
    
    // 获取列宽度
    getColumnWidth(key, value) {
      const baseWidth = 120
      const keyLength = key.length * 8
      const valueLength = String(value).length * 8
      return Math.max(baseWidth, Math.max(keyLength, valueLength) + 40)
    },
    
    // 判断是否为日期列
    isDateColumn(prop) {
      const dateKeywords = ['time', 'date', 'created', 'updated', 'modified']
      return dateKeywords.some(keyword => prop.toLowerCase().includes(keyword))
    },
    
    // 判断是否为数字列
    isNumberColumn(prop) {
      if (!this.resultData || !this.resultData.records || this.resultData.records.length === 0) {
        return false
      }
      
      const firstValue = this.resultData.records[0][prop]
      return typeof firstValue === 'number' || !isNaN(Number(firstValue))
    },
    
    // 格式化日期
    formatDate(value) {
      if (!value) return ''
      const date = new Date(value)
      if (isNaN(date.getTime())) return value
      return date.toLocaleString()
    },
    
    // 格式化数字
    formatNumber(value) {
      if (value === null || value === undefined || value === '') return ''
      const num = Number(value)
      if (isNaN(num)) return value
      return num.toLocaleString()
    },
    
    // 分页大小变化
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
    },
    
    // 当前页变化
    handleCurrentChange(page) {
      this.currentPage = page
    },
    
    // 导出数据
    handleExport(format) {
      this.$emit('export', {
        format,
        data: this.resultData,
        viewMode: this.viewMode
      })
    },
    
    // 刷新数据
    refreshData() {
      this.$emit('refresh')
    },
    
    // 复制JSON
    copyJSON() {
      const json = this.formattedJSON
      if (navigator.clipboard) {
        navigator.clipboard.writeText(json).then(() => {
          this.$message.success('JSON已复制到剪贴板')
        })
      } else {
        // 兼容旧浏览器
        const textArea = document.createElement('textarea')
        textArea.value = json
        document.body.appendChild(textArea)
        textArea.select()
        document.execCommand('copy')
        document.body.removeChild(textArea)
        this.$message.success('JSON已复制到剪贴板')
      }
    },
    
    // 格式化JSON
    formatJSON() {
      // JSON已经是格式化的，这里可以添加其他格式化选项
      this.$message.success('JSON已格式化')
    },
    
    // 初始化图表配置
    initChartConfig() {
      if (this.tableColumns.length > 0) {
        this.chartConfig.xField = this.tableColumns[0].prop
        if (this.numberColumns.length > 0) {
          this.chartConfig.yField = this.numberColumns[0].prop
        }
      }
    },
    
    // 更新图表
    updateChart() {
      // 这里可以集成图表库来更新图表
      console.log('更新图表配置:', this.chartConfig)
    }
  }
}
</script>

<style scoped>
.sql-result-viewer {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #fff;
}

.result-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.toolbar-left,
.toolbar-center,
.toolbar-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.result-info {
  font-size: 13px;
  color: #666;
}

.result-content {
  flex: 1;
  overflow: hidden;
  position: relative;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #999;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-text {
  text-align: center;
}

.empty-text p {
  margin: 8px 0;
}

.empty-tip {
  font-size: 12px;
  color: #ccc;
}

.table-view {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.pagination-wrapper {
  padding: 16px;
  text-align: center;
  border-top: 1px solid #e4e7ed;
}

.json-view {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.json-toolbar {
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.json-content {
  flex: 1;
  padding: 16px;
  margin: 0;
  overflow: auto;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  line-height: 1.5;
  background: #f8f9fa;
}

.chart-view {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.chart-config {
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.chart-container {
  flex: 1;
  position: relative;
}

.chart-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #999;
}

.chart-placeholder i {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.error-message {
  padding: 16px;
}

.date-cell {
  color: #409eff;
}

.number-cell {
  color: #67c23a;
  text-align: right;
  font-family: 'Courier New', monospace;
}
</style>
