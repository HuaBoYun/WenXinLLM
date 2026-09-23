<template>
  <div class="document-version-comparison">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">
          <i class="el-icon-connection"></i>
          版本对比分析
        </h2>
        <p class="page-description">比较不同版本之间的差异和变更</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-document" @click="generateReport">生成报告</el-button>
        <el-button icon="el-icon-download" @click="exportComparison">导出对比</el-button>
      </div>
    </div>

    <!-- 版本选择区域 -->
    <div class="version-selector">
      <el-card>
        <div slot="header" class="card-header">
          <span>选择对比版本</span>
          <el-button type="text" @click="clearSelection">清空选择</el-button>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="version-select-item">
              <h4>源版本</h4>
              <el-select
                v-model="sourceVersionId"
                placeholder="请选择源版本"
                filterable
                remote
                :remote-method="searchVersions"
                :loading="searchLoading"
                @change="handleSourceChange"
                style="width: 100%"
              >
                <el-option
                  v-for="version in versionOptions"
                  :key="version.versionId"
                  :label="`${version.versionName} (${version.versionNumber})`"
                  :value="version.versionId"
                >
                  <span style="float: left">{{ version.versionName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ version.versionNumber }}</span>
                </el-option>
              </el-select>
              <div v-if="sourceVersion" class="version-info">
                <div class="version-card">
                  <div class="version-header">
                    <span class="version-name">{{ sourceVersion.versionName }}</span>
                    <el-tag :type="getVersionStatusColor(sourceVersion.versionStatus)" size="mini">
                      {{ formatVersionStatus(sourceVersion.versionStatus) }}
                    </el-tag>
                  </div>
                  <div class="version-details">
                    <p><strong>版本号:</strong> {{ sourceVersion.versionNumber }}</p>
                    <p><strong>文档:</strong> {{ sourceVersion.documentName }}</p>
                    <p><strong>创建时间:</strong> {{ formatTime(sourceVersion.createdTime) }}</p>
                    <p><strong>创建人:</strong> {{ sourceVersion.createdBy }}</p>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="version-select-item">
              <h4>目标版本</h4>
              <el-select
                v-model="targetVersionId"
                placeholder="请选择目标版本"
                filterable
                remote
                :remote-method="searchVersions"
                :loading="searchLoading"
                @change="handleTargetChange"
                style="width: 100%"
              >
                <el-option
                  v-for="version in versionOptions"
                  :key="version.versionId"
                  :label="`${version.versionName} (${version.versionNumber})`"
                  :value="version.versionId"
                >
                  <span style="float: left">{{ version.versionName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ version.versionNumber }}</span>
                </el-option>
              </el-select>
              <div v-if="targetVersion" class="version-info">
                <div class="version-card">
                  <div class="version-header">
                    <span class="version-name">{{ targetVersion.versionName }}</span>
                    <el-tag :type="getVersionStatusColor(targetVersion.versionStatus)" size="mini">
                      {{ formatVersionStatus(targetVersion.versionStatus) }}
                    </el-tag>
                  </div>
                  <div class="version-details">
                    <p><strong>版本号:</strong> {{ targetVersion.versionNumber }}</p>
                    <p><strong>文档:</strong> {{ targetVersion.documentName }}</p>
                    <p><strong>创建时间:</strong> {{ formatTime(targetVersion.createdTime) }}</p>
                    <p><strong>创建人:</strong> {{ targetVersion.createdBy }}</p>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
        <div class="comparison-actions">
          <el-button
            type="primary"
            icon="el-icon-connection"
            @click="compareVersions"
            :disabled="!canCompare"
            :loading="comparing"
          >
            开始对比
          </el-button>
          <el-button icon="el-icon-refresh" @click="resetComparison">重置</el-button>
        </div>
      </el-card>
    </div>

    <!-- 对比结果区域 -->
    <div v-if="comparisonResult" class="comparison-result">
      <!-- 对比概览 -->
      <div class="comparison-overview">
        <el-card>
          <div slot="header" class="card-header">
            <span>对比概览</span>
            <span class="comparison-time">对比时间: {{ formatTime(comparisonTime) }}</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="overview-item">
                <div class="overview-icon added">
                  <i class="el-icon-plus"></i>
                </div>
                <div class="overview-content">
                  <div class="overview-number">{{ comparisonResult.summary.added }}</div>
                  <div class="overview-label">新增项</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-item">
                <div class="overview-icon modified">
                  <i class="el-icon-edit"></i>
                </div>
                <div class="overview-content">
                  <div class="overview-number">{{ comparisonResult.summary.modified }}</div>
                  <div class="overview-label">修改项</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-item">
                <div class="overview-icon deleted">
                  <i class="el-icon-minus"></i>
                </div>
                <div class="overview-content">
                  <div class="overview-number">{{ comparisonResult.summary.deleted }}</div>
                  <div class="overview-label">删除项</div>
                </div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="overview-item">
                <div class="overview-icon similarity">
                  <i class="el-icon-pie-chart"></i>
                </div>
                <div class="overview-content">
                  <div class="overview-number">{{ comparisonResult.summary.similarity }}%</div>
                  <div class="overview-label">相似度</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </div>

      <!-- 详细对比 -->
      <div class="comparison-details">
        <el-card>
          <div slot="header" class="card-header">
            <span>详细对比</span>
            <el-radio-group v-model="viewMode" size="small">
              <el-radio-button label="side-by-side">并排对比</el-radio-button>
              <el-radio-button label="unified">统一视图</el-radio-button>
            </el-radio-group>
          </div>
          
          <el-tabs v-model="activeTab" type="card">
            <!-- 基本信息对比 -->
            <el-tab-pane label="基本信息" name="basic">
              <div class="comparison-table">
                <el-table :data="basicComparison" border>
                  <el-table-column prop="field" label="字段" width="150"></el-table-column>
                  <el-table-column prop="sourceValue" label="源版本" min-width="200">
                    <template slot-scope="scope">
                      <span :class="getValueClass(scope.row.status, 'source')">
                        {{ scope.row.sourceValue || '-' }}
                      </span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="targetValue" label="目标版本" min-width="200">
                    <template slot-scope="scope">
                      <span :class="getValueClass(scope.row.status, 'target')">
                        {{ scope.row.targetValue || '-' }}
                      </span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="status" label="状态" width="100">
                    <template slot-scope="scope">
                      <el-tag :type="getStatusTagType(scope.row.status)" size="mini">
                        {{ getStatusText(scope.row.status) }}
                      </el-tag>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>

            <!-- 内容对比 -->
            <el-tab-pane label="内容对比" name="content">
              <div class="content-comparison">
                <div v-if="viewMode === 'side-by-side'" class="side-by-side-view">
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <div class="content-panel">
                        <div class="panel-header">源版本内容</div>
                        <div class="panel-content">
                          <pre>{{ comparisonResult.content.source }}</pre>
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="12">
                      <div class="content-panel">
                        <div class="panel-header">目标版本内容</div>
                        <div class="panel-content">
                          <pre>{{ comparisonResult.content.target }}</pre>
                        </div>
                      </div>
                    </el-col>
                  </el-row>
                </div>
                <div v-else class="unified-view">
                  <div class="unified-content">
                    <div v-for="(line, index) in unifiedDiff" :key="index" 
                         :class="['diff-line', getDiffLineClass(line.type)]">
                      <span class="line-number">{{ line.number }}</span>
                      <span class="line-content">{{ line.content }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 变更历史 -->
            <el-tab-pane label="变更历史" name="history">
              <div class="change-history">
                <el-timeline>
                  <el-timeline-item
                    v-for="change in changeHistory"
                    :key="change.id"
                    :timestamp="formatTime(change.changeTime)"
                    :type="getChangeType(change.changeType)"
                  >
                    <div class="change-item">
                      <div class="change-title">{{ change.changeDescription }}</div>
                      <div class="change-details">
                        <span class="change-author">{{ change.changeBy }}</span>
                        <span class="change-type">{{ change.changeType }}</span>
                      </div>
                    </div>
                  </el-timeline-item>
                </el-timeline>
              </div>
            </el-tab-pane>

            <!-- 文件对比 -->
            <el-tab-pane label="文件信息" name="files">
              <div class="file-comparison">
                <el-table :data="fileComparison" border>
                  <el-table-column prop="property" label="属性" width="150"></el-table-column>
                  <el-table-column prop="sourceValue" label="源版本" min-width="200"></el-table-column>
                  <el-table-column prop="targetValue" label="目标版本" min-width="200"></el-table-column>
                  <el-table-column prop="difference" label="差异" min-width="200">
                    <template slot-scope="scope">
                      <span :class="getDifferenceClass(scope.row.difference)">
                        {{ scope.row.difference }}
                      </span>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else class="empty-state">
      <el-empty description="请选择版本进行对比">
        <el-button type="primary" @click="scrollToSelector">选择版本</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script>
import {
  getVersionPage,
  getVersionById,
  compareVersions,
  getVersionDiff,
  getVersionChangeHistory,
  generateComparisonReport,
  formatVersionStatus,
  formatFileSize
} from '@/api/managementAccountant/as/documentVersion'

export default {
  name: 'DocumentVersionComparison',
  data() {
    return {
      // 版本选择
      sourceVersionId: '',
      targetVersionId: '',
      sourceVersion: null,
      targetVersion: null,
      versionOptions: [],
      searchLoading: false,
      // 对比状态
      comparing: false,
      comparisonTime: null,
      comparisonResult: null,
      // 视图模式
      viewMode: 'side-by-side',
      activeTab: 'basic',
      // 对比数据
      basicComparison: [],
      changeHistory: [],
      fileComparison: [],
      unifiedDiff: []
    }
  },
  computed: {
    canCompare() {
      return this.sourceVersionId && this.targetVersionId && this.sourceVersionId !== this.targetVersionId
    }
  },
  created() {
    this.loadInitialVersions()
  },
  methods: {
    // 加载初始版本列表
    async loadInitialVersions() {
      try {
        const response = await getVersionPage({
          current: 1,
          size: 50,
          sortField: 'created_time',
          sortOrder: 'desc'
        })
        if (response.data && response.data.records) {
          this.versionOptions = response.data.records
        }
      } catch (error) {
        console.error('加载版本列表失败:', error)
      }
    },

    // 搜索版本
    async searchVersions(query) {
      if (!query) {
        this.loadInitialVersions()
        return
      }
      
      this.searchLoading = true
      try {
        const response = await getVersionPage({
          current: 1,
          size: 20,
          versionName: query
        })
        if (response.data && response.data.records) {
          this.versionOptions = response.data.records
        }
      } catch (error) {
        console.error('搜索版本失败:', error)
      } finally {
        this.searchLoading = false
      }
    },

    // 源版本变化
    async handleSourceChange(versionId) {
      if (versionId) {
        try {
          const response = await getVersionById(versionId)
          this.sourceVersion = response.data
        } catch (error) {
          this.$message.error('获取源版本信息失败')
        }
      } else {
        this.sourceVersion = null
      }
    },

    // 目标版本变化
    async handleTargetChange(versionId) {
      if (versionId) {
        try {
          const response = await getVersionById(versionId)
          this.targetVersion = response.data
        } catch (error) {
          this.$message.error('获取目标版本信息失败')
        }
      } else {
        this.targetVersion = null
      }
    },

    // 开始对比
    async compareVersions() {
      if (!this.canCompare) return
      
      this.comparing = true
      try {
        const [comparisonRes, diffRes, historyRes] = await Promise.all([
          compareVersions(this.sourceVersionId, this.targetVersionId),
          getVersionDiff(this.sourceVersionId, this.targetVersionId),
          getVersionChangeHistory(this.targetVersionId)
        ])
        
        this.comparisonResult = comparisonRes.data
        this.comparisonTime = new Date()
        this.changeHistory = historyRes.data || []
        
        // 处理基本信息对比
        this.processBasicComparison()
        
        // 处理文件对比
        this.processFileComparison()
        
        // 处理统一差异视图
        this.processUnifiedDiff(diffRes.data)
        
        this.$message.success('版本对比完成')
      } catch (error) {
        this.$message.error('版本对比失败: ' + error.message)
      } finally {
        this.comparing = false
      }
    },

    // 处理基本信息对比
    processBasicComparison() {
      const fields = [
        { key: 'versionName', label: '版本名称' },
        { key: 'versionNumber', label: '版本号' },
        { key: 'versionType', label: '版本类型' },
        { key: 'versionStatus', label: '版本状态' },
        { key: 'versionDescription', label: '版本描述' },
        { key: 'createdBy', label: '创建人' },
        { key: 'createdTime', label: '创建时间' }
      ]
      
      this.basicComparison = fields.map(field => {
        const sourceValue = this.sourceVersion[field.key]
        const targetValue = this.targetVersion[field.key]
        let status = 'unchanged'
        
        if (sourceValue !== targetValue) {
          if (!sourceValue) status = 'added'
          else if (!targetValue) status = 'deleted'
          else status = 'modified'
        }
        
        return {
          field: field.label,
          sourceValue: this.formatFieldValue(field.key, sourceValue),
          targetValue: this.formatFieldValue(field.key, targetValue),
          status
        }
      })
    },

    // 处理文件对比
    processFileComparison() {
      const fileFields = [
        { key: 'fileSize', label: '文件大小' },
        { key: 'fileFormat', label: '文件格式' },
        { key: 'filePath', label: '文件路径' },
        { key: 'storageLocation', label: '存储位置' }
      ]
      
      this.fileComparison = fileFields.map(field => {
        const sourceValue = this.sourceVersion[field.key]
        const targetValue = this.targetVersion[field.key]
        let difference = '无变化'
        
        if (sourceValue !== targetValue) {
          if (field.key === 'fileSize') {
            const sizeDiff = (targetValue || 0) - (sourceValue || 0)
            difference = sizeDiff > 0 ? `增加 ${formatFileSize(sizeDiff)}` : `减少 ${formatFileSize(Math.abs(sizeDiff))}`
          } else {
            difference = '已修改'
          }
        }
        
        return {
          property: field.label,
          sourceValue: this.formatFieldValue(field.key, sourceValue),
          targetValue: this.formatFieldValue(field.key, targetValue),
          difference
        }
      })
    },

    // 处理统一差异视图
    processUnifiedDiff(diffData) {
      // 这里应该处理实际的差异数据
      // 模拟差异数据
      this.unifiedDiff = [
        { number: 1, content: '版本名称: ' + this.sourceVersion.versionName, type: 'removed' },
        { number: 1, content: '版本名称: ' + this.targetVersion.versionName, type: 'added' },
        { number: 2, content: '版本描述: ' + (this.sourceVersion.versionDescription || ''), type: 'context' },
        { number: 3, content: '创建时间: ' + this.formatTime(this.sourceVersion.createdTime), type: 'context' }
      ]
    },

    // 格式化字段值
    formatFieldValue(key, value) {
      if (!value) return '-'
      
      switch (key) {
        case 'versionStatus':
          return formatVersionStatus(value)
        case 'fileSize':
          return formatFileSize(value)
        case 'createdTime':
        case 'updatedTime':
          return this.formatTime(value)
        default:
          return value
      }
    },

    // 清空选择
    clearSelection() {
      this.sourceVersionId = ''
      this.targetVersionId = ''
      this.sourceVersion = null
      this.targetVersion = null
      this.comparisonResult = null
    },

    // 重置对比
    resetComparison() {
      this.comparisonResult = null
      this.comparisonTime = null
      this.basicComparison = []
      this.changeHistory = []
      this.fileComparison = []
      this.unifiedDiff = []
    },

    // 生成报告
    async generateReport() {
      if (!this.comparisonResult) {
        this.$message.warning('请先进行版本对比')
        return
      }
      
      try {
        const response = await generateComparisonReport(
          [this.sourceVersionId, this.targetVersionId],
          'PDF'
        )
        this.$message.success('报告生成成功')
      } catch (error) {
        this.$message.error('生成报告失败: ' + error.message)
      }
    },

    // 导出对比
    exportComparison() {
      if (!this.comparisonResult) {
        this.$message.warning('请先进行版本对比')
        return
      }
      
      this.$message.info('导出功能开发中')
    },

    // 滚动到选择器
    scrollToSelector() {
      this.$el.querySelector('.version-selector').scrollIntoView({ behavior: 'smooth' })
    },

    // 工具方法
    formatTime(time) {
      if (!time) return '-'
      return this.$moment(time).format('YYYY-MM-DD HH:mm:ss')
    },

    getVersionStatusColor(status) {
      const colorMap = {
        'DRAFT': 'info',
        'UNDER_REVIEW': 'warning',
        'APPROVED': 'success',
        'PUBLISHED': 'primary',
        'ARCHIVED': 'info'
      }
      return colorMap[status] || 'info'
    },

    getValueClass(status, side) {
      if (status === 'unchanged') return ''
      if (status === 'added' && side === 'target') return 'value-added'
      if (status === 'deleted' && side === 'source') return 'value-deleted'
      if (status === 'modified') return 'value-modified'
      return ''
    },

    getStatusTagType(status) {
      const typeMap = {
        'unchanged': 'info',
        'added': 'success',
        'deleted': 'danger',
        'modified': 'warning'
      }
      return typeMap[status] || 'info'
    },

    getStatusText(status) {
      const textMap = {
        'unchanged': '无变化',
        'added': '新增',
        'deleted': '删除',
        'modified': '修改'
      }
      return textMap[status] || status
    },

    getDiffLineClass(type) {
      return {
        'diff-added': type === 'added',
        'diff-removed': type === 'removed',
        'diff-context': type === 'context'
      }
    },

    getChangeType(changeType) {
      const typeMap = {
        'CREATE': 'primary',
        'UPDATE': 'warning',
        'DELETE': 'danger'
      }
      return typeMap[changeType] || 'primary'
    },

    getDifferenceClass(difference) {
      if (difference === '无变化') return 'no-change'
      if (difference.includes('增加')) return 'increase'
      if (difference.includes('减少')) return 'decrease'
      return 'change'
    }
  }
}
</script>

<style lang="scss" scoped>
.document-version-comparison {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .header-left {
      .page-title {
        margin: 0 0 8px 0;
        font-size: 24px;
        font-weight: 600;
        color: #303133;

        i {
          margin-right: 8px;
          color: #409EFF;
        }
      }

      .page-description {
        margin: 0;
        color: #909399;
        font-size: 14px;
      }
    }
  }

  .version-selector {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .version-select-item {
      h4 {
        margin: 0 0 12px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }

      .version-info {
        margin-top: 16px;

        .version-card {
          padding: 16px;
          background: #F5F7FA;
          border-radius: 8px;
          border-left: 4px solid #409EFF;

          .version-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 12px;

            .version-name {
              font-weight: 600;
              color: #303133;
            }
          }

          .version-details {
            p {
              margin: 4px 0;
              font-size: 14px;
              color: #606266;

              strong {
                color: #303133;
                margin-right: 8px;
              }
            }
          }
        }
      }
    }

    .comparison-actions {
      margin-top: 20px;
      text-align: center;

      .el-button {
        margin: 0 8px;
      }
    }
  }

  .comparison-result {
    .comparison-overview {
      margin-bottom: 20px;

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .comparison-time {
          color: #909399;
          font-size: 14px;
        }
      }

      .overview-item {
        display: flex;
        align-items: center;
        padding: 16px;
        background: #F5F7FA;
        border-radius: 8px;

        .overview-icon {
          width: 50px;
          height: 50px;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;

          i {
            font-size: 20px;
            color: white;
          }

          &.added {
            background: #67C23A;
          }

          &.modified {
            background: #E6A23C;
          }

          &.deleted {
            background: #F56C6C;
          }

          &.similarity {
            background: #409EFF;
          }
        }

        .overview-content {
          .overview-number {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            line-height: 1;
          }

          .overview-label {
            font-size: 14px;
            color: #909399;
            margin-top: 4px;
          }
        }
      }
    }

    .comparison-details {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }

      .comparison-table {
        .value-added {
          background: #f0f9ff;
          color: #67C23A;
          padding: 2px 4px;
          border-radius: 4px;
        }

        .value-deleted {
          background: #fef0f0;
          color: #F56C6C;
          padding: 2px 4px;
          border-radius: 4px;
          text-decoration: line-through;
        }

        .value-modified {
          background: #fdf6ec;
          color: #E6A23C;
          padding: 2px 4px;
          border-radius: 4px;
        }
      }

      .content-comparison {
        .side-by-side-view {
          .content-panel {
            border: 1px solid #EBEEF5;
            border-radius: 4px;

            .panel-header {
              padding: 12px 16px;
              background: #F5F7FA;
              border-bottom: 1px solid #EBEEF5;
              font-weight: 600;
              color: #303133;
            }

            .panel-content {
              padding: 16px;
              max-height: 400px;
              overflow-y: auto;

              pre {
                margin: 0;
                font-family: 'Courier New', monospace;
                font-size: 14px;
                line-height: 1.5;
                white-space: pre-wrap;
                word-wrap: break-word;
              }
            }
          }
        }

        .unified-view {
          .unified-content {
            border: 1px solid #EBEEF5;
            border-radius: 4px;
            max-height: 500px;
            overflow-y: auto;

            .diff-line {
              display: flex;
              font-family: 'Courier New', monospace;
              font-size: 14px;
              line-height: 1.5;

              &.diff-added {
                background: #f0f9ff;
                color: #67C23A;
              }

              &.diff-removed {
                background: #fef0f0;
                color: #F56C6C;
              }

              &.diff-context {
                background: white;
              }

              .line-number {
                width: 60px;
                padding: 4px 8px;
                background: #F5F7FA;
                border-right: 1px solid #EBEEF5;
                color: #909399;
                text-align: right;
                flex-shrink: 0;
              }

              .line-content {
                padding: 4px 12px;
                flex: 1;
              }
            }
          }
        }
      }

      .change-history {
        .change-item {
          .change-title {
            font-weight: 500;
            color: #303133;
            margin-bottom: 4px;
          }

          .change-details {
            font-size: 14px;
            color: #909399;

            .change-author {
              margin-right: 12px;
            }

            .change-type {
              padding: 2px 6px;
              background: #F5F7FA;
              border-radius: 4px;
              font-size: 12px;
            }
          }
        }
      }

      .file-comparison {
        .no-change {
          color: #909399;
        }

        .increase {
          color: #67C23A;
        }

        .decrease {
          color: #F56C6C;
        }

        .change {
          color: #E6A23C;
        }
      }
    }
  }

  .empty-state {
    margin-top: 60px;
  }
}
</style>
