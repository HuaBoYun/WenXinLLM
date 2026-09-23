<template>
  <div class="risk-model-screen-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <i class="el-icon-data-analysis"></i>
        <h2>风险模型大屏</h2>
      </div>
      <div class="header-right">
        <el-button
          type="primary"
          icon="el-icon-full-screen"
          @click="openFullScreenAll"
          class="screen-btn"
        >
          大屏展示
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 模型统计卡片 -->
      <div class="overview-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-header">
                <i class="el-icon-s-data"></i>
                <span>在用模型</span>
              </div>
              <div class="card-value">{{ statistics.totalModels || 0 }}<span class="unit">个</span></div>
              <div class="card-desc">已启用模型数量</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card warning">
              <div class="card-header">
                <i class="el-icon-warning"></i>
                <span>预警总数</span>
              </div>
              <div class="card-value">{{ statistics.totalWarnings || 0 }}<span class="unit">条</span></div>
              <div class="card-trend up">
                <i class="el-icon-top"></i>
                今日新增 {{ statistics.todayWarnings || 0 }}
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card pending">
              <div class="card-header">
                <i class="el-icon-time"></i>
                <span>待处理</span>
              </div>
              <div class="card-value">{{ statistics.pendingWarnings || 0 }}<span class="unit">条</span></div>
              <div class="card-desc">需要及时处理</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card success">
              <div class="card-header">
                <i class="el-icon-circle-check"></i>
                <span>已处理</span>
              </div>
              <div class="card-value">{{ statistics.processedWarnings || 0 }}<span class="unit">条</span></div>
              <div class="card-desc">处理率 {{ statistics.processRate || 0 }}%</div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 模型列表 -->
      <div class="model-list-section">
        <el-card>
          <div slot="header" class="card-title">
            <i class="el-icon-menu"></i>
            <span>在用模型列表</span>
            <el-input
              v-model="searchKeyword"
              placeholder="搜索模型名称"
              prefix-icon="el-icon-search"
              clearable
              style="width: 300px; float: right;"
              @input="handleSearch"
            />
          </div>
          <el-row :gutter="20">
            <el-col :span="6" v-for="model in filteredModels" :key="model.evalModelId">
              <div class="model-card" @click="handleModelClick(model)">
                <div class="model-icon">
                  <i :class="getModelIcon(model.businessScenario)"></i>
                </div>
                <div class="model-info">
                  <div class="model-name" :title="model.modelName">{{ model.modelName }}</div>
                  <div class="model-type">{{ getModelTypeName(model.businessScenario) }}</div>
                </div>
                <div class="model-stats">
                  <div class="stat-item">
                    <span class="label">预警数:</span>
                    <span class="value warning">{{ model.warningCount || 0 }}</span>
                  </div>
                  <div class="stat-item">
                    <span class="label">待处理:</span>
                    <span class="value pending">{{ model.pendingCount || 0 }}</span>
                  </div>
                </div>
                <div class="model-status" :class="model.status">
                  <el-tag :type="getStatusType(model.status)" size="mini">
                    {{ getStatusText(model.status) }}
                  </el-tag>
                </div>
              </div>
            </el-col>
          </el-row>
          <div v-if="filteredModels.length === 0" class="empty-data">
            <el-empty description="暂无模型数据"></el-empty>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 全屏大屏弹窗 -->
    <el-dialog
      :visible.sync="screenVisible"
      fullscreen
      :show-close="false"
      custom-class="screen-dialog"
      @close="handleScreenClose"
    >
      <screen-display
        v-if="screenVisible"
        :selected-model="selectedModel"
        :model-list="modelList"
        :show-all-models="showAllModels"
        @close="closeFullScreen"
        @model-change="handleModelChange"
      />
    </el-dialog>
  </div>
</template>

<script>
import ScreenDisplay from './components/ScreenDisplay.vue'
import {
  getEvaluationModelList,
  getRiskWarningList,
  getWarningStatistics,
  getTodayWarningCount,
  getEvaluationModelStatistics
} from '@/api/mxgl'

export default {
  name: 'RiskModelScreen',
  components: {
    ScreenDisplay
  },
  data() {
    return {
      loading: false,
      screenVisible: false,
      searchKeyword: '',
      selectedModel: null,
      showAllModels: false,  // 是否展示所有模型统计
      modelList: [],
      statistics: {
        totalModels: 0,
        totalWarnings: 0,
        todayWarnings: 0,
        pendingWarnings: 0,
        processedWarnings: 0,
        processRate: 0
      },
      // 模型图标映射
      modelIconMap: {
        'PROCUREMENT': 'el-icon-shopping-cart-2',
        'FINANCIAL': 'el-icon-coin',
        'CREDIT': 'el-icon-bank-card',
        'COMPLIANCE': 'el-icon-document-checked',
        'INVESTMENT': 'el-icon-s-finance',
        'GROUP_CONTROL': 'el-icon-office-building',
        'BUSINESS': 'el-icon-s-shop',
        'LEGAL': 'el-icon-s-order',
        'TRADE': 'el-icon-s-goods',
        'DEFAULT': 'el-icon-data-analysis'
      },
      // 模型类型名称映射
      modelTypeMap: {
        'PROCUREMENT': '采购风险',
        'FINANCIAL': '财务风险',
        'CREDIT': '信用风险',
        'COMPLIANCE': '合规风险',
        'INVESTMENT': '投资风险',
        'GROUP_CONTROL': '集团管控',
        'BUSINESS': '经营风险',
        'LEGAL': '法律风险',
        'TRADE': '贸易风险'
      }
    }
  },
  computed: {
    filteredModels() {
      if (!this.searchKeyword) {
        return this.modelList
      }
      return this.modelList.filter(model =>
        model.modelName.toLowerCase().includes(this.searchKeyword.toLowerCase())
      )
    }
  },
  created() {
    this.loadModelList()
    this.loadStatistics()
  },
  methods: {
    async loadModelList() {
      this.loading = true
      try {
        // 使用 getEvaluationModelList 并筛选已启用的模型
        const res = await getEvaluationModelList({
          pageNum: 1,
          pageSize: 100,
          isEnabled: 'Y'  // 只获取已启用的模型
        })

        if (res.code === 1 && res.data) {
          this.modelList = res.data.list || []

          // 加载每个模型的预警数量
          await this.loadWarningCounts()
        }
      } catch (error) {
        console.error('加载模型列表失败:', error)
        this.$message.error('加载模型列表失败')
      } finally {
        this.loading = false
      }
    },
    async loadWarningCounts() {
      const promises = this.modelList.map(async (model) => {
        try {
          // 调用 /model/warning/list 接口获取预警列表
          const warningRes = await getRiskWarningList({
            evalModelId: model.evalModelId,
            pageNum: 1,
            pageSize: 1000  // 获取所有预警数据
          })

          if (warningRes.code === 1 && warningRes.data) {
            const list = warningRes.data.list || []

            // 总预警数
            model.warningCount = list.length

            // 待处理数量 = PENDING + PROCESSING
            model.pendingCount = list.filter(item =>
              item.warningStatus === 'PENDING' || item.warningStatus === 'PROCESSING'
            ).length

            // 已处理数量
            model.processedCount = list.filter(item =>
              item.warningStatus === 'PROCESSED'
            ).length

            // 已忽略数量
            model.ignoredCount = list.filter(item =>
              item.warningStatus === 'IGNORED'
            ).length
          } else {
            model.warningCount = 0
            model.pendingCount = 0
            model.processedCount = 0
            model.ignoredCount = 0
          }
        } catch (error) {
          console.error(`加载模型 ${model.modelName} 预警数量失败:`, error)
          model.warningCount = 0
          model.pendingCount = 0
          model.processedCount = 0
          model.ignoredCount = 0
        }
      })

      await Promise.all(promises)
    },
    async loadStatistics() {
      try {
        // 1. 获取在用模型数量 - 调用 /model/evaluation/statistics
        const modelStatsRes = await getEvaluationModelStatistics()
        if (modelStatsRes.code === 1 && modelStatsRes.data) {
          this.statistics.totalModels = modelStatsRes.data.enabledCount || 0
        }

        // 2. 获取预警统计 - 调用 /model/warning/statistics
        const warningStatsRes = await getWarningStatistics()
        if (warningStatsRes.code === 1 && warningStatsRes.data) {
          const data = warningStatsRes.data

          // 总预警数
          this.statistics.totalWarnings = data.totalCount || 0

          // 待处理 = PENDING + PROCESSING
          const statusStats = data.statusStatistics || {}
          this.statistics.pendingWarnings = (statusStats.PENDING || 0) + (statusStats.PROCESSING || 0)

          // 已处理
          this.statistics.processedWarnings = statusStats.PROCESSED || 0

          // 处理率
          if (this.statistics.totalWarnings > 0) {
            this.statistics.processRate = Math.round(
              (this.statistics.processedWarnings / this.statistics.totalWarnings) * 100
            )
          } else {
            this.statistics.processRate = 0
          }

          // 今日新增
          this.statistics.todayWarnings = data.todayCount || 0
        }

        // 3. 如果今日新增为0,尝试单独获取
        if (this.statistics.todayWarnings === 0) {
          await this.loadTodayCount()
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    async loadTodayCount() {
      try {
        const response = await getTodayWarningCount()
        if (response.code === 1) {
          this.statistics.todayWarnings = response.data?.count || 0
        }
      } catch (error) {
        console.error('获取今日新增数量失败:', error)
      }
    },
    getModelIcon(businessScenario) {
      return this.modelIconMap[businessScenario] || this.modelIconMap.DEFAULT
    },
    getModelTypeName(businessScenario) {
      return this.modelTypeMap[businessScenario] || '其他'
    },
    getStatusType(status) {
      const typeMap = {
        'ENABLED': 'success',
        'DISABLED': 'info',
        'DRAFT': 'warning',
        'RUNNING': 'success',
        'STOPPED': 'info',
        'PUBLISHED': 'success',
        'UNPUBLISHED': 'warning'
      }
      return typeMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        'ENABLED': '已启用',
        'DISABLED': '已停用',
        'DRAFT': '草稿',
        'RUNNING': '运行中',
        'STOPPED': '已停止',
        'PUBLISHED': '已发布',
        'UNPUBLISHED': '未发布'
      }
      return textMap[status] || status
    },
    handleSearch() {
      // 搜索已通过computed实现
    },
    handleModelClick(model) {
      // 点击模型卡片 - 展示单个模型的统计数据
      this.selectedModel = model
      this.showAllModels = false
      this.screenVisible = true
    },
    handleModelChange(model) {
      this.selectedModel = model
    },
    openFullScreenAll() {
      // 点击右上角大屏展示按钮 - 展示所有模型的统计数据
      this.selectedModel = null
      this.showAllModels = true
      this.screenVisible = true
    },
    closeFullScreen() {
      this.screenVisible = false
      this.selectedModel = null
      this.showAllModels = false
    },
    handleScreenClose() {
      this.screenVisible = false
      this.selectedModel = null
      this.showAllModels = false
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-model-screen-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 15px 20px;
    background: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .header-left {
      display: flex;
      align-items: center;
      gap: 10px;

      i {
        font-size: 24px;
        color: #409eff;
      }

      h2 {
        margin: 0;
        font-size: 20px;
        color: #303133;
      }
    }
  }

  .main-content {
    .overview-section {
      margin-bottom: 20px;

      .overview-card {
        text-align: center;
        transition: all 0.3s;

        &:hover {
          transform: translateY(-5px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .card-header {
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 8px;
          font-size: 14px;
          color: #606266;
          margin-bottom: 15px;

          i {
            font-size: 20px;
          }
        }

        .card-value {
          font-size: 32px;
          font-weight: bold;
          color: #409eff;
          margin-bottom: 10px;

          .unit {
            font-size: 16px;
            margin-left: 4px;
          }
        }

        .card-trend {
          font-size: 14px;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 4px;

          &.up {
            color: #f56c6c;
          }
        }

        .card-desc {
          font-size: 12px;
          color: #909399;
        }

        &.warning .card-value {
          color: #e6a23c;
        }

        &.pending .card-value {
          color: #f56c6c;
        }

        &.success .card-value {
          color: #67c23a;
        }
      }
    }

    .model-list-section {
      .card-title {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        font-weight: bold;
        color: #303133;

        i {
          font-size: 18px;
          color: #409eff;
        }
      }

      .model-card {
        padding: 20px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        border-radius: 8px;
        margin-bottom: 20px;
        cursor: pointer;
        transition: all 0.3s;
        position: relative;
        overflow: hidden;
        color: #fff;

        &:hover {
          transform: translateY(-5px) scale(1.02);
          box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
        }

        &:nth-child(4n+1) {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }

        &:nth-child(4n+2) {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }

        &:nth-child(4n+3) {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }

        &:nth-child(4n+4) {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }

        .model-icon {
          font-size: 48px;
          margin-bottom: 15px;
          opacity: 0.9;

          i {
            color: #fff;
          }
        }

        .model-info {
          margin-bottom: 15px;

          .model-name {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 5px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }

          .model-type {
            font-size: 14px;
            opacity: 0.9;
          }
        }

        .model-stats {
          display: flex;
          justify-content: space-between;
          margin-bottom: 10px;

          .stat-item {
            .label {
              font-size: 12px;
              opacity: 0.8;
            }

            .value {
              font-size: 20px;
              font-weight: bold;
              margin-left: 5px;

              &.warning {
                color: #ffd700;
              }

              &.pending {
                color: #ff6b6b;
              }
            }
          }
        }

        .model-status {
          position: absolute;
          top: 10px;
          right: 10px;
        }
      }

      .empty-data {
        padding: 40px 0;
        text-align: center;
      }
    }
  }
}

::v-deep .screen-dialog {
  background: #0a0e27 !important;
  margin: 0 !important;
  padding: 0 !important;

  .el-dialog__header {
    display: none !important;
  }

  .el-dialog__body {
    padding: 0 !important;
    height: 100vh !important;
    overflow: hidden !important;
  }
}
</style>

