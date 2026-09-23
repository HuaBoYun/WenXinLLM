<template>
  <div class="gzctzl-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <i class="el-icon-data-analysis"></i>
        <h2>国资穿透总览</h2>
      </div>
      <div class="header-right">
        <el-button
          type="primary"
          icon="el-icon-full-screen"
          @click="openFullScreen"
          class="screen-btn"
        >
          大屏
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 数据概览卡片 -->
      <div class="overview-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-header">
                <i class="el-icon-office-building"></i>
                <span>企业总数</span>
              </div>
              <div class="card-value">{{ overviewData.totalCompanies || 0 }}</div>
              <div class="card-desc">已纳入穿透监控</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-header">
                <i class="el-icon-warning"></i>
                <span>风险预警数</span>
              </div>
              <div class="card-value warning">{{ overviewData.riskWarnings || 0 }}</div>
              <div class="card-desc">需重点关注</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-header">
                <i class="el-icon-data-line"></i>
                <span>数据源接入</span>
              </div>
              <div class="card-value success">{{ overviewData.dataSources || 0 }}</div>
              <div class="card-desc">个数据源</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-header">
                <i class="el-icon-document-checked"></i>
                <span>模型数量</span>
              </div>
              <div class="card-value info">{{ overviewData.modelCount || 34 }}</div>
              <div class="card-desc">风险模型</div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 数据资源展示 -->
      <div class="data-resources-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card class="resource-card">
              <div slot="header" class="card-title">
                <i class="el-icon-share"></i>
                <span>社会侧数据资源</span>
              </div>
              <div class="resource-list">
                <div class="resource-item" v-for="item in socialDataResources" :key="item.name">
                  <div class="resource-name">{{ item.name }}</div>
                  <div class="resource-status" :class="item.status">{{ item.statusText }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="resource-card">
              <div slot="header" class="card-title">
                <i class="el-icon-office-building"></i>
                <span>企业侧数据资源</span>
              </div>
              <div class="resource-list">
                <div class="resource-item" v-for="item in enterpriseDataResources" :key="item.name">
                  <div class="resource-name">{{ item.name }}</div>
                  <div class="resource-status" :class="item.status">{{ item.statusText }}</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 风险模型展示 -->
      <div class="risk-models-section">
        <el-card>
          <div slot="header" class="card-title">
            <i class="el-icon-s-data"></i>
            <span>国资风险穿透模型（34个）</span>
          </div>
          <el-row :gutter="15">
            <el-col :span="4" v-for="model in riskModels" :key="model.id">
              <div class="model-item" :class="model.level">
                <div class="model-name">{{ model.name }}</div>
                <div class="model-count">{{ model.count }}</div>
              </div>
            </el-col>
          </el-row>
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
        :penetration-data="penetrationData"
        @close="closeFullScreen"
      />
    </el-dialog>
  </div>
</template>

<script>
import ScreenDisplay from './components/ScreenDisplay.vue'
import { getPenetrationData } from './api/penetration'

export default {
  name: 'Gzctzl',
  components: {
    ScreenDisplay
  },
  data() {
    return {
      loading: false,
      screenVisible: false,
      overviewData: {
        totalCompanies: 0,
        riskWarnings: 0,
        dataSources: 0,
        modelCount: 34
      },
      socialDataResources: [
        { name: '工商数据', status: 'active', statusText: '已接入' },
        { name: '经营数据', status: 'active', statusText: '已接入' },
        { name: '财务数据', status: 'active', statusText: '已接入' },
        { name: '征信数据', status: 'active', statusText: '已接入' }
      ],
      enterpriseDataResources: [
        { name: '自动网络', status: 'active', statusText: '已接入' },
        { name: '发票网络', status: 'active', statusText: '已接入' },
        { name: '财务网络', status: 'active', statusText: '已接入' },
        { name: '综合网络', status: 'active', statusText: '已接入' },
        { name: '人员网络', status: 'active', statusText: '已接入' },
        { name: '内部网络', status: 'active', statusText: '已接入' }
      ],
      riskModels: [],
      penetrationData: {}
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await getPenetrationData()
        if (res.code === 200) {
          this.overviewData = res.data.overview || this.overviewData
          this.riskModels = res.data.riskModels || this.generateRiskModels()
          this.penetrationData = res.data
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.riskModels = this.generateRiskModels()
      } finally {
        this.loading = false
      }
    },
    generateRiskModels() {
      const models = [
        { id: 1, name: '虚假贸易', count: 12, level: 'high' },
        { id: 2, name: '逾期应付', count: 8, level: 'medium' },
        { id: 3, name: '违规挂靠', count: 5, level: 'low' },
        { id: 4, name: '违规公款消费', count: 3, level: 'low' },
        { id: 5, name: '靠企吃企', count: 7, level: 'medium' },
        { id: 6, name: '超股比担保', count: 4, level: 'medium' },
        { id: 7, name: '超合同支付', count: 6, level: 'medium' },
        { id: 8, name: '逾期应收', count: 15, level: 'high' },
        { id: 9, name: '财务金融风险', count: 9, level: 'high' },
        { id: 10, name: '控股不控权', count: 2, level: 'low' },
        { id: 11, name: '捞偏门', count: 1, level: 'low' },
        { id: 12, name: '资产闲置', count: 11, level: 'high' },
        { id: 13, name: '境外风险', count: 3, level: 'medium' },
        { id: 14, name: '过度负债', count: 8, level: 'high' },
        { id: 15, name: '无关多元', count: 5, level: 'medium' },
        { id: 16, name: '多层架构', count: 6, level: 'medium' },
        { id: 17, name: '薪酬乱象', count: 4, level: 'low' },
        { id: 18, name: '对外投资', count: 7, level: 'medium' }
      ]
      return models
    },
    openFullScreen() {
      this.screenVisible = true
    },
    closeFullScreen() {
      this.screenVisible = false
    },
    handleScreenClose() {
      this.screenVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.gzctzl-container {
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

    .screen-btn {
      font-size: 14px;
    }
  }

  .main-content {
    .overview-section {
      margin-bottom: 20px;

      .overview-card {
        text-align: center;

        .card-header {
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 8px;
          font-size: 14px;
          color: #606266;
          margin-bottom: 15px;

          i {
            font-size: 18px;
          }
        }

        .card-value {
          font-size: 32px;
          font-weight: bold;
          color: #409eff;
          margin-bottom: 10px;

          &.warning {
            color: #e6a23c;
          }

          &.success {
            color: #67c23a;
          }

          &.info {
            color: #909399;
          }
        }

        .card-desc {
          font-size: 12px;
          color: #909399;
        }
      }
    }

    .data-resources-section {
      margin-bottom: 20px;

      .resource-card {
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

        .resource-list {
          display: grid;
          grid-template-columns: repeat(2, 1fr);
          gap: 15px;

          .resource-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 12px;
            background: #f5f7fa;
            border-radius: 4px;
            border-left: 3px solid #409eff;

            .resource-name {
              font-size: 14px;
              color: #303133;
            }

            .resource-status {
              font-size: 12px;
              padding: 2px 8px;
              border-radius: 3px;

              &.active {
                background: #e1f3d8;
                color: #67c23a;
              }

              &.inactive {
                background: #fef0f0;
                color: #f56c6c;
              }
            }
          }
        }
      }
    }

    .risk-models-section {
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

      .model-item {
        padding: 15px;
        text-align: center;
        border-radius: 4px;
        margin-bottom: 15px;
        cursor: pointer;
        transition: all 0.3s;

        &.high {
          background: rgba(245, 108, 108, 0.1);
          border: 1px solid #f56c6c;

          .model-count {
            color: #f56c6c;
          }
        }

        &.medium {
          background: rgba(230, 162, 60, 0.1);
          border: 1px solid #e6a23c;

          .model-count {
            color: #e6a23c;
          }
        }

        &.low {
          background: rgba(103, 194, 58, 0.1);
          border: 1px solid #67c23a;

          .model-count {
            color: #67c23a;
          }
        }

        &:hover {
          transform: translateY(-3px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }

        .model-name {
          font-size: 14px;
          color: #303133;
          margin-bottom: 8px;
        }

        .model-count {
          font-size: 24px;
          font-weight: bold;
        }
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

