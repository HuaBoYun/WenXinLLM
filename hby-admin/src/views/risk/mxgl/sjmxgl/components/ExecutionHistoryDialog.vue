<template>
  <el-dialog
    title="执行历史"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    :modal-append-to-body="false"
    :append-to-body="true"
    :z-index="4000"
    custom-class="execution-history-dialog-wrapper"
    class="execution-history-dialog"
    @close="handleClose"
  >
    <div class="history-container">
      <!-- 搜索和筛选 -->
      <div class="search-bar">
        <el-form :model="searchForm" inline size="mini">
          <el-form-item label="执行名称">
            <el-input
              v-model="searchForm.executionName"
              placeholder="请输入执行名称"
              clearable
              style="width: 200px;"
            />
          </el-form-item>
          
          <el-form-item label="执行状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 150px;">
              <el-option label="成功" value="SUCCESS" />
              <el-option label="失败" value="FAILED" />
              <el-option label="运行中" value="RUNNING" />
              <el-option label="已取消" value="CANCELLED" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="执行时间">
            <el-date-picker
              v-model="searchForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              style="width: 240px;"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
            <el-button type="warning" @click="fixDirtyData">
              <i class="el-icon-tools"></i> 修复脏数据
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 执行历史列表 -->
      <div class="history-list">
        <el-table
          v-loading="loading"
          :data="historyList"
          border
          stripe
          @row-click="handleRowClick"
        >
          <el-table-column prop="executionName" label="执行名称" min-width="200">
            <template slot-scope="scope">
              <div class="execution-name">
                <span>{{ scope.row.executionName }}</span>
                <el-tag v-if="scope.row.executionMode" :type="scope.row.executionMode === 'SEQUENCE' ? 'primary' : 'success'" size="mini">
                  {{ scope.row.executionMode === 'SEQUENCE' ? '顺序' : '并行' }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="status" label="执行状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">
                {{ getStatusText(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="startTime" label="开始时间" width="160" />
          
          <el-table-column prop="endTime" label="结束时间" width="160">
            <template slot-scope="scope">
              {{ scope.row.endTime || '-' }}
            </template>
          </el-table-column>
          
          <el-table-column prop="totalDuration" label="总耗时" width="100" align="center">
            <template slot-scope="scope">
              {{ formatDuration(scope.row.totalDuration) }}
            </template>
          </el-table-column>
          
          <el-table-column label="执行结果" width="120" align="center">
            <template slot-scope="scope">
              <div class="result-summary">
                <span class="success-count">成功: {{ scope.row.successCount || 0 }}</span>
                <span class="failed-count">失败: {{ scope.row.failedCount || 0 }}</span>
              </div>
            </template>
          </el-table-column>
          
          <el-table-column prop="createUser" label="执行人" width="100" />
          
          <el-table-column label="操作" width="180" fixed="right">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click.stop="viewExecutionDetail(scope.row)">
                查看详情
              </el-button>
              <el-button type="text" size="mini" @click.stop="downloadExecutionResult(scope.row)">
                下载结果
              </el-button>
              <el-button type="text" size="mini" style="color: #f56c6c;" @click.stop="deleteExecution(scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination
          v-if="total > 0"
          :current-page="pageNum"
          :page-size="pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          style="margin-top: 16px; text-align: center;"
        />
      </div>
    </div>

    <!-- 执行详情对话框 -->
    <el-dialog
      title="执行详情"
      :visible.sync="detailDialogVisible"
      width="80%"
      :append-to-body="true"
      :z-index="4100"
      custom-class="execution-detail-dialog-wrapper"
      class="execution-detail-dialog"
    >
      <div v-if="currentExecution" class="execution-detail">
        <!-- 基本信息 -->
        <div class="detail-section">
          <h4>基本信息</h4>
          <el-descriptions :column="3" border>
            <el-descriptions-item label="执行名称">
              {{ currentExecution.executionName }}
            </el-descriptions-item>
            <el-descriptions-item label="执行模式">
              <el-tag :type="currentExecution.executionMode === 'SEQUENCE' ? 'primary' : 'success'">
                {{ currentExecution.executionMode === 'SEQUENCE' ? '顺序执行' : '并行执行' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="执行状态">
              <el-tag :type="getStatusType(currentExecution.status)">
                {{ getStatusText(currentExecution.status) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="开始时间">
              {{ currentExecution.startTime }}
            </el-descriptions-item>
            <el-descriptions-item label="结束时间">
              {{ currentExecution.endTime || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="总耗时">
              {{ formatDuration(currentExecution.totalDuration) }}
            </el-descriptions-item>
            <el-descriptions-item label="执行人">
              {{ currentExecution.createUser }}
            </el-descriptions-item>
            <el-descriptions-item label="成功数量">
              {{ currentExecution.successCount || 0 }}
            </el-descriptions-item>
            <el-descriptions-item label="失败数量">
              {{ currentExecution.failedCount || 0 }}
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 参数配置 -->
        <div v-if="currentExecution.parameters" class="detail-section">
          <h4>参数配置</h4>
          <el-table :data="parameterList" border size="mini">
            <el-table-column prop="name" label="参数名" width="150" />
            <el-table-column prop="value" label="参数值" />
            <el-table-column prop="type" label="类型" width="100" />
          </el-table>
        </div>

        <!-- 指标执行结果 -->
        <div class="detail-section">
          <h4>指标执行结果 ({{ (currentExecution.indicatorResults || []).length }}条)</h4>
          <div v-if="!currentExecution.indicatorResults || currentExecution.indicatorResults.length === 0" class="no-data">
            暂无指标执行结果
          </div>
          <el-table v-else :data="currentExecution.indicatorResults" border size="mini">
            <el-table-column prop="indicatorName" label="指标名称" min-width="150" />
            <el-table-column prop="status" label="状态" width="80" align="center">
              <template slot-scope="scope">
                <el-tag :type="getStatusType(scope.row.status)" size="mini">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="startTime" label="开始时间" width="140" />
            <el-table-column prop="endTime" label="结束时间" width="140" />
            <el-table-column prop="executionDuration" label="耗时" width="80" align="center">
              <template slot-scope="scope">
                {{ formatDuration(scope.row.executionDuration) }}
              </template>
            </el-table-column>
            <el-table-column prop="recordCount" label="记录数" width="80" align="center" />
            <el-table-column prop="errorMessage" label="错误信息" min-width="200">
              <template slot-scope="scope">
                <span v-if="scope.row.errorMessage" class="error-message">
                  {{ scope.row.errorMessage }}
                </span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" fixed="right">
              <template slot-scope="scope">
                <el-button type="text" size="mini" @click="viewIndicatorResult(scope.row)">
                  查看结果
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 执行日志 -->
        <div v-if="currentExecution.executionLogs" class="detail-section">
          <h4>执行日志</h4>
          <div class="execution-logs">
            <div
              v-for="log in currentExecution.executionLogs"
              :key="log.timestamp"
              :class="['log-item', log.level.toLowerCase()]"
            >
              <span class="log-time">{{ formatTime(log.timestamp) }}</span>
              <span class="log-level">{{ log.level }}</span>
              <span class="log-message">{{ log.message }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 指标结果查看对话框 -->
    <el-dialog
      title="指标结果详情"
      :visible.sync="indicatorResultDialogVisible"
      width="90%"
      :close-on-click-modal="false"
      :modal-append-to-body="false"
      :append-to-body="true"
      :z-index="4200"
      custom-class="indicator-result-detail-dialog-wrapper"
      class="indicator-result-detail-dialog"
    >
      <div v-if="currentIndicatorResult" class="indicator-result-container">
        <!-- 指标基本信息 -->
        <div class="result-header">
          <h3>{{ currentIndicatorResult.indicatorName }}</h3>
          <div class="result-meta">
            <el-tag :type="getStatusType(currentIndicatorResult.status)" size="small">
              {{ getStatusText(currentIndicatorResult.status) }}
            </el-tag>
            <span class="meta-item">
              <i class="el-icon-time"></i>
              耗时: {{ formatDuration(currentIndicatorResult.executionDuration) }}
            </span>
            <span class="meta-item">
              <i class="el-icon-document"></i>
              记录数: {{ currentIndicatorResult.recordCount || 0 }}
            </span>
          </div>
        </div>

        <!-- 结果数据表格 -->
        <div class="result-content">
          <div v-if="indicatorResultLoading" class="loading-container">
            <el-loading text="加载中..." />
          </div>
          <div v-else-if="!currentIndicatorResult.resultData || currentIndicatorResult.resultData.length === 0" class="no-data">
            <i class="el-icon-document"></i>
            <p>暂无结果数据</p>
          </div>
          <div v-else class="data-table-container">
            <el-table
              :data="currentIndicatorResult.resultData"
              border
              size="mini"
              max-height="400"
              style="width: 100%"
            >
              <!-- 动态生成列 -->
              <el-table-column
                v-for="(value, key) in getTableColumns(currentIndicatorResult.resultData)"
                :key="key"
                :prop="key"
                :label="formatColumnLabel(key)"
                :min-width="getColumnWidth(key, value)"
                show-overflow-tooltip
              >
                <template slot-scope="scope">
                  <span v-if="isDateField(key)" class="date-field">
                    {{ formatDate(scope.row[key]) }}
                  </span>
                  <span v-else-if="isNumberField(scope.row[key])" class="number-field">
                    {{ formatNumber(scope.row[key]) }}
                  </span>
                  <span v-else>{{ scope.row[key] }}</span>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination-container" style="margin-top: 20px; text-align: center;">
              <span class="total-info">
                共 {{ currentIndicatorResult.recordCount || 0 }} 条记录
              </span>
            </div>
          </div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="indicatorResultDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="exportIndicatorResult">
          <i class="el-icon-download"></i>
          导出数据
        </el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { getExecutionHistory, getExecutionResult, exportAnalysisResult, deleteExecutionRecord, getIndicatorResultDetail, fixDirtyData } from '@/api/mxgl'

export default {
  name: 'ExecutionHistoryDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    combination: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,

      // 搜索表单
      searchForm: {
        executionName: '',
        status: '',
        dateRange: []
      },

      // 修复脏数据状态
      fixingData: false,

      // 分页
      pageNum: 1,
      pageSize: 20,
      total: 0,

      // 数据
      historyList: [],

      // 详情对话框
      detailDialogVisible: false,
      currentExecution: null,

      // 指标结果对话框
      indicatorResultDialogVisible: false,
      indicatorResultLoading: false,
      currentIndicatorResult: null
    }
  },
  computed: {
    parameterList() {
      if (!this.currentExecution || !this.currentExecution.parameters) {
        return []
      }
      
      try {
        const params = typeof this.currentExecution.parameters === 'string'
          ? JSON.parse(this.currentExecution.parameters)
          : this.currentExecution.parameters
          
        return Object.keys(params).map(key => ({
          name: key,
          value: params[key],
          type: typeof params[key]
        }))
      } catch (error) {
        console.error('解析参数失败:', error)
        return []
      }
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val) {
          this.loadHistoryList()
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    // 加载执行历史
    async loadHistoryList() {
      if (!this.combination || !this.combination.combinationId) {
        return
      }

      try {
        this.loading = true
        console.log('📊 执行历史 - 查询参数:', {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          combinationId: this.combination.combinationId,
          executionName: this.searchForm.executionName,
          status: this.searchForm.status
        })

        const params = {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          combinationId: this.combination.combinationId,
          executionName: this.searchForm.executionName,
          status: this.searchForm.status
        }

        // 添加日期范围
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }

        const response = await getExecutionHistory(params)
        console.log('📊 执行历史 - 接口响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1) {
          this.historyList = response.data.records || []
          this.total = response.data.total || 0
          console.log('✅ 执行历史加载成功(标准格式):', this.historyList.length, '条记录')
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.historyList = response.records || []
          this.total = response.total || 0
          console.log('✅ 执行历史加载成功(直接格式):', this.historyList.length, '条记录')
        } else {
          this.$message.error(response.msg || '加载执行历史失败')
          console.error('❌ 执行历史加载失败:', response)
        }
      } catch (error) {
        console.error('❌ 加载执行历史失败:', error)
        this.$message.error('加载执行历史失败')
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pageNum = 1
      this.loadHistoryList()
    },

    // 重置搜索
    resetSearch() {
      this.searchForm = {
        executionName: '',
        status: '',
        dateRange: []
      }
      this.pageNum = 1
      this.loadHistoryList()
    },

    // 分页
    handleSizeChange(size) {
      this.pageSize = size
      this.pageNum = 1
      this.loadHistoryList()
    },

    handleCurrentChange(page) {
      this.pageNum = page
      this.loadHistoryList()
    },

    // 行点击
    handleRowClick(row) {
      this.viewExecutionDetail(row)
    },

    // 查看执行详情
    async viewExecutionDetail(execution) {
      try {
        console.log('📊 执行历史 - 查看执行详情:', execution)

        const response = await getExecutionResult(execution.executionId)
        console.log('📊 执行历史 - 执行详情响应:', response)

        // 兼容两种响应格式
        let data = null
        if (response && response.code === 1) {
          data = response.data
          console.log('✅ 执行详情加载成功(标准格式)')
        } else if (response && response.indicators) {
          // 直接格式（mock数据）
          data = response
          console.log('✅ 执行详情加载成功(直接格式)')
        } else {
          this.$message.error(response.msg || '加载执行详情失败')
          console.error('❌ 执行详情加载失败:', response)
          return
        }

        console.log('📊 开始处理响应数据:', data)

        // 先处理indicators数据映射
        let indicatorResults = []
        if (data.indicators && Array.isArray(data.indicators)) {
          console.log('✅ 找到indicators数据，数量:', data.indicators.length)
          indicatorResults = data.indicators.map(indicator => ({
            indicatorName: indicator.indicatorName,
            status: indicator.status,
            resultId: indicator.resultId,
            configId: indicator.configId,
            duration: indicator.duration,
            executionDuration: indicator.duration,  // 映射为表格期望的字段名
            resultCount: indicator.resultCount,
            recordCount: indicator.resultCount,     // 映射为表格期望的字段名
            hasResultData: indicator.hasResultData,
            errorMessage: indicator.errorMessage,
            // 注意：startTime和endTime是指标级别的，后端当前没有返回
            startTime: indicator.startTime || '-',
            endTime: indicator.endTime || '-'
          }))
          console.log('✅ 数据映射完成，indicators -> indicatorResults:', indicatorResults)
        } else {
          console.log('⚠️ 没有找到indicators数据或数据格式不正确')
          // 尝试从执行记录中获取
          if (execution.indicatorResults) {
            indicatorResults = execution.indicatorResults
            console.log('✅ 从执行记录中获取indicatorResults:', indicatorResults)
          }
        }

        // 合并执行基本信息和详细结果，确保indicatorResults正确设置
        this.currentExecution = {
          ...execution,
          ...data,
          indicatorResults: indicatorResults  // 明确设置映射后的数据
        }

        console.log('✅ 最终设置的indicatorResults:', this.currentExecution.indicatorResults)
        console.log('✅ 最终执行详情:', this.currentExecution)
        console.log('✅ 指标结果数组:', this.currentExecution.indicatorResults)
        console.log('✅ 指标结果数量:', (this.currentExecution.indicatorResults || []).length)

        // 延迟一下确保数据绑定完成
        this.$nextTick(() => {
          this.detailDialogVisible = true
        })
      } catch (error) {
        console.error('❌ 加载执行详情失败:', error)
        this.$message.error('加载执行详情失败: ' + (error.message || '未知错误'))
      }
    },

    // 下载执行结果
    async downloadExecutionResult(execution) {
      try {
        console.log('🔽 开始下载执行结果:', execution)

        // 检查执行状态
        if (execution.status !== 'SUCCESS') {
          this.$message.warning('只能下载执行成功的结果')
          return
        }

        // 先获取执行详情，获取指标结果信息
        console.log('📊 执行历史 - 获取执行详情以获取指标结果信息...')
        const detailResponse = await getExecutionResult(execution.executionId)
        console.log('📊 执行历史 - 执行详情响应:', detailResponse)

        // 兼容两种响应格式
        let data = null
        if (detailResponse && detailResponse.code === 1) {
          data = detailResponse.data
          console.log('✅ 执行详情加载成功(标准格式)')
        } else if (detailResponse && detailResponse.indicators) {
          // 直接格式（mock数据）
          data = detailResponse
          console.log('✅ 执行详情加载成功(直接格式)')
        } else {
          this.$message.error('获取执行详情失败: ' + (detailResponse.msg || '未知错误'))
          console.error('❌ 执行详情加载失败:', detailResponse)
          return
        }

        // 检查是否有指标结果
        const indicators = data.indicators || []

        if (!indicators || indicators.length === 0) {
          this.$message.warning('该执行记录没有可下载的结果')
          return
        }

        // 获取最后一个成功的指标结果作为最终结果
        // 指标组合分析是流水线处理，最后一个指标是最终输出结果
        const successResults = indicators.filter(result =>
          result.status === 'SUCCESS' && result.resultId
        )

        if (!successResults || successResults.length === 0) {
          this.$message.warning('没有找到成功的执行结果')
          return
        }

        // 取最后一个成功的指标结果作为最终结果
        const finalResult = successResults[successResults.length - 1]

        console.log('🔽 指标执行顺序:', indicators.map(i => i.indicatorName))
        console.log('🔽 使用最终结果 resultId:', finalResult.resultId, '指标名称:', finalResult.indicatorName)

        const response = await exportAnalysisResult({
          resultId: finalResult.resultId,  // 使用最终结果的resultId
          format: 'EXCEL',
          fileName: `${execution.executionName || '执行结果'}-${finalResult.indicatorName}`
        })

        console.log('🔽 导出响应:', response)

        if (response.code === 1) {
          // 构建完整的下载URL
          const protocol = window.location.protocol
          const host = window.location.host
          const downloadUrl = response.data.downloadUrl
          const fullDownloadUrl = downloadUrl.startsWith('http') ? downloadUrl :
            `${protocol}//${host}/vab-mock-server${downloadUrl}`

          console.log('🔽 下载URL:', fullDownloadUrl)

          const link = document.createElement('a')
          link.href = fullDownloadUrl
          link.download = response.data.fileName || `${execution.executionName}-执行结果.xlsx`
          link.target = '_blank'

          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)

          // 备用下载方式
          setTimeout(() => {
            window.open(fullDownloadUrl, '_blank')
          }, 1000)

          this.$message.success('导出成功，文件开始下载')
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      } catch (error) {
        console.error('导出执行结果失败:', error)
        this.$message.error('导出失败: ' + (error.message || '未知错误'))
      }
    },







    // 删除执行记录
    deleteExecution: async function(execution) {
      try {
        await this.$confirm(
          `确定要删除执行记录"${execution.executionName || '未命名'}"吗？\n删除后将无法恢复！`,
          '确认删除',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        console.log('📊 执行历史 - 删除执行记录:', execution.executionId)

        // 调用删除接口
        const response = await deleteExecutionRecord(execution.executionId)
        console.log('📊 执行历史 - 删除响应:', response)

        // 兼容两种响应格式
        if (response && (response.code === 1 || response.success === true)) {
          this.$message.success('删除成功')
          console.log('✅ 删除成功')
          // 重新加载列表
          this.loadHistoryList()
        } else {
          this.$message.error(response.msg || '删除失败')
          console.error('❌ 删除失败:', response)
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('❌ 删除执行记录失败:', error)
          this.$message.error(`删除失败: ${error.message || '未知错误'}`)
        }
      }
    },



    // 修复脏数据
    fixDirtyData: async function() {
      try {
        await this.$confirm(
          '确定要修复执行历史中的脏数据吗？\n\n修复内容包括：\n• 补充空的执行名称\n• 修正状态不一致的记录\n• 处理长时间运行的僵尸进程\n• 修复缺失的持续时间',
          '确认修复脏数据',
          {
            type: 'warning',
            confirmButtonText: '确定修复',
            cancelButtonText: '取消'
          }
        )

        console.log('📊 执行历史 - 开始修复脏数据')
        this.fixingData = true
        const response = await fixDirtyData()
        console.log('📊 执行历史 - 修复响应:', response)

        // 兼容两种响应格式
        let result = null
        if (response && response.code === 1) {
          result = response.data
          console.log('✅ 脏数据修复成功(标准格式)')
        } else if (response && response.totalFixed !== undefined) {
          // 直接格式（mock数据）
          result = response
          console.log('✅ 脏数据修复成功(直接格式)')
        } else {
          this.$message.error(response.msg || '修复失败')
          console.error('❌ 脏数据修复失败:', response)
          return
        }

        const totalFixed = result.totalFixed || 0

        if (totalFixed > 0) {
          this.$message.success(`修复完成！共修复 ${totalFixed} 条记录`)

          // 显示详细修复信息
          const details = []
          if (result.nameFixed > 0) details.push(`执行名称: ${result.nameFixed}条`)
          if (result.statusFixed > 0) details.push(`状态不一致: ${result.statusFixed}条`)
          if (result.zombieFixed > 0) details.push(`僵尸进程: ${result.zombieFixed}条`)
          if (result.durationFixed > 0) details.push(`持续时间: ${result.durationFixed}条`)

          if (details.length > 0) {
            this.$notify({
              title: '修复详情',
              message: details.join('\n'),
              type: 'success',
              duration: 5000
            })
          }
        } else {
          this.$message.info('没有发现需要修复的脏数据')
        }

        // 重新加载列表
        this.loadHistoryList()
      } catch (error) {
        if (error !== 'cancel') {
          console.error('修复脏数据失败:', error)
          this.$message.error('修复失败: ' + (error.message || '未知错误'))
        }
      } finally {
        this.fixingData = false
      }
    },

    // 查看指标结果
    async viewIndicatorResult(result) {
      try {
        console.log('📊 执行历史 - 查看指标结果:', result)

        if (!result.resultId) {
          this.$message.warning('该指标没有结果数据')
          return
        }

        this.indicatorResultLoading = true

        // 调用API获取指标结果详情
        const response = await getIndicatorResultDetail(result.resultId, {
          pageNum: 1,
          pageSize: 100  // 先获取前100条数据
        })

        console.log('📊 执行历史 - 指标结果详情响应:', response)

        // 兼容两种响应格式
        let data = null
        if (response && response.code === 1) {
          data = response.data
          console.log('✅ 指标结果详情加载成功(标准格式)')
        } else if (response && response.resultData) {
          // 直接格式（mock数据）
          data = response
          console.log('✅ 指标结果详情加载成功(直接格式)')
        } else {
          this.$message.error(response.msg || '获取指标结果失败')
          console.error('❌ 指标结果详情加载失败:', response)
          return
        }

        // 设置当前查看的指标结果
        this.currentIndicatorResult = {
          ...result,
          ...data,
          resultData: data.resultData || data.data || []
        }

        console.log('✅ 当前指标结果:', this.currentIndicatorResult)
        this.indicatorResultDialogVisible = true
      } catch (error) {
        console.error('❌ 获取指标结果失败:', error)
        this.$message.error('获取指标结果失败: ' + (error.message || '未知错误'))
      } finally {
        this.indicatorResultLoading = false
      }
    },

    // 导出指标结果
    async exportIndicatorResult() {
      try {
        if (!this.currentIndicatorResult || !this.currentIndicatorResult.resultId) {
          this.$message.warning('没有可导出的数据')
          return
        }

        const response = await exportAnalysisResult({
          resultId: this.currentIndicatorResult.resultId,
          format: 'EXCEL',
          fileName: this.currentIndicatorResult.indicatorName || '指标结果'
        })

        if (response.code === 1) {
          // 处理下载
          const downloadUrl = response.data.downloadUrl
          if (downloadUrl) {
            const protocol = window.location.protocol
            const host = window.location.host
            const fullDownloadUrl = downloadUrl.startsWith('http') ? downloadUrl :
              `${protocol}//${host}/vab-mock-server${downloadUrl}`

            const link = document.createElement('a')
            link.href = fullDownloadUrl
            link.download = response.data.fileName || `${this.currentIndicatorResult.indicatorName}-结果.xlsx`
            link.target = '_blank'

            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link)

            this.$message.success('导出成功')
          }
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      } catch (error) {
        console.error('导出指标结果失败:', error)
        this.$message.error('导出失败: ' + (error.message || '未知错误'))
      }
    },

    // 获取表格列信息
    getTableColumns(data) {
      if (!data || data.length === 0) return {}

      // 取第一行数据的所有键作为列
      const firstRow = data[0]
      const columns = {}

      Object.keys(firstRow).forEach(key => {
        columns[key] = firstRow[key]
      })

      return columns
    },

    // 格式化列标题
    formatColumnLabel(key) {
      // 将下划线转换为空格，首字母大写
      return key.replace(/_/g, ' ')
        .split(' ')
        .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
        .join(' ')
    },

    // 获取列宽度
    getColumnWidth(key, value) {
      const keyLength = key.length
      const valueLength = String(value).length
      const maxLength = Math.max(keyLength, valueLength)

      // 最小宽度100，最大宽度300
      return Math.min(Math.max(maxLength * 8 + 40, 100), 300)
    },

    // 判断是否为日期字段
    isDateField(key) {
      const dateFields = ['date', 'time', 'created', 'updated', 'start', 'end']
      return dateFields.some(field => key.toLowerCase().includes(field))
    },

    // 判断是否为数字字段
    isNumberField(value) {
      return typeof value === 'number' || (!isNaN(value) && !isNaN(parseFloat(value)))
    },

    // 格式化日期
    formatDate(value) {
      if (!value) return '-'
      try {
        const date = new Date(value)
        return date.toLocaleString('zh-CN')
      } catch (error) {
        return value
      }
    },

    // 格式化数字
    formatNumber(value) {
      if (value === null || value === undefined) return '-'
      const num = parseFloat(value)
      if (isNaN(num)) return value

      // 如果是整数，直接返回
      if (num % 1 === 0) return num.toLocaleString()

      // 如果是小数，保留2位小数
      return num.toLocaleString(undefined, { minimumFractionDigits: 0, maximumFractionDigits: 2 })
    },

    // 工具方法
    getStatusType(status) {
      const typeMap = {
        'SUCCESS': 'success',
        'RUNNING': 'primary',
        'FAILED': 'danger',
        'CANCELLED': 'info'
      }
      return typeMap[status] || 'info'
    },

    getStatusText(status) {
      const textMap = {
        'SUCCESS': '成功',
        'RUNNING': '运行中',
        'FAILED': '失败',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },

    formatDuration(duration) {
      if (!duration) return '-'
      const seconds = Math.floor(duration / 1000)
      const minutes = Math.floor(seconds / 60)
      const hours = Math.floor(minutes / 60)
      
      if (hours > 0) {
        return `${hours}h${minutes % 60}m`
      } else if (minutes > 0) {
        return `${minutes}m${seconds % 60}s`
      } else {
        return `${seconds}s`
      }
    },

    formatTime(timestamp) {
      const date = new Date(timestamp)
      return date.toLocaleString()
    },

    // 对话框关闭处理
    handleClose() {
      this.dialogVisible = false
      this.selectedExecution = null
      this.executionLogs = []
    }
  },
  created() {
    // 确保响应式属性正确初始化
    this.$set(this, 'fixingData', false)
  }
}
</script>

<style scoped>
.execution-history-dialog {
  .history-container {
    height: 80vh;
    display: flex;
    flex-direction: column;
  }

  .search-bar {
    padding: 16px;
    background-color: #f5f7fa;
    border-radius: 4px;
    margin-bottom: 16px;
  }

  .history-list {
    flex: 1;
    overflow: auto;
    min-height: 400px;

    .execution-name {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .result-summary {
      display: flex;
      flex-direction: column;
      gap: 2px;
      font-size: 12px;

      .success-count {
        color: #67c23a;
      }

      .failed-count {
        color: #f56c6c;
      }
    }
  }

  .execution-detail {
    .detail-section {
      margin-bottom: 24px;

      &:last-child {
        margin-bottom: 0;
      }

      h4 {
        margin: 0 0 12px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }

    .error-message {
      color: #f56c6c;
      font-size: 12px;
    }

    .execution-logs {
      max-height: 300px;
      overflow-y: auto;
      border: 1px solid #ebeef5;
      border-radius: 4px;
      padding: 8px;
      background-color: #f5f7fa;

      .log-item {
        display: flex;
        gap: 12px;
        margin-bottom: 4px;
        font-size: 12px;
        line-height: 1.4;

        &.error {
          color: #f56c6c;
        }

        &.warn {
          color: #e6a23c;
        }

        &.info {
          color: #606266;
        }

        .log-time {
          color: #909399;
          width: 80px;
          flex-shrink: 0;
        }

        .log-level {
          width: 50px;
          flex-shrink: 0;
          font-weight: 600;
        }

        .log-message {
          flex: 1;
        }
      }
    }
  }
}

/* 指标结果对话框样式 */
.indicator-result-dialog {
  .indicator-result-container {
    height: 70vh;
    display: flex;
    flex-direction: column;
  }

  .result-header {
    padding: 16px 0;
    border-bottom: 1px solid #ebeef5;
    margin-bottom: 16px;

    h3 {
      margin: 0 0 12px 0;
      color: #303133;
      font-size: 18px;
      font-weight: 600;
    }

    .result-meta {
      display: flex;
      align-items: center;
      gap: 16px;

      .meta-item {
        display: flex;
        align-items: center;
        color: #606266;
        font-size: 14px;

        i {
          margin-right: 4px;
          color: #909399;
        }
      }
    }
  }

  .result-content {
    flex: 1;
    overflow: hidden;
    display: flex;
    flex-direction: column;

    .loading-container {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .no-data {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #909399;

      i {
        font-size: 48px;
        margin-bottom: 16px;
      }

      p {
        margin: 0;
        font-size: 16px;
      }
    }

    .data-table-container {
      flex: 1;
      overflow: hidden;
      display: flex;
      flex-direction: column;

      .el-table {
        flex: 1;
      }

      .pagination-container {
        padding: 16px 0;
        border-top: 1px solid #ebeef5;

        .total-info {
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .date-field {
    color: #67c23a;
  }

  .number-field {
    color: #409eff;
    font-family: 'Courier New', monospace;
  }
}
</style>

<style>
/* 🔥 确保执行历史对话框显示在组合指标分析对话框之上 */
/* 注意：这里不使用 scoped，以便样式能够应用到 Element UI 动态创建的元素 */
.execution-history-dialog-wrapper {
  z-index: 4000 !important;
}

.execution-history-dialog-wrapper .el-dialog {
  z-index: 4001 !important;
}

.execution-history-dialog-wrapper .el-dialog__wrapper {
  z-index: 4000 !important;
}

/* 遮罩层也需要设置合适的 z-index */
.execution-history-dialog-wrapper + .v-modal {
  z-index: 3999 !important;
}

/* 🔥 确保执行详情对话框显示在执行历史对话框之上 */
.execution-detail-dialog-wrapper {
  z-index: 4100 !important;
}

.execution-detail-dialog-wrapper .el-dialog {
  z-index: 4101 !important;
}

.execution-detail-dialog-wrapper .el-dialog__wrapper {
  z-index: 4100 !important;
}

.execution-detail-dialog-wrapper + .v-modal {
  z-index: 4099 !important;
}

/* 🔥 确保指标结果详情对话框显示在执行详情对话框之上 */
.indicator-result-detail-dialog-wrapper {
  z-index: 4200 !important;
}

.indicator-result-detail-dialog-wrapper .el-dialog {
  z-index: 4201 !important;
}

.indicator-result-detail-dialog-wrapper .el-dialog__wrapper {
  z-index: 4200 !important;
}

.indicator-result-detail-dialog-wrapper + .v-modal {
  z-index: 4199 !important;
}
</style>
