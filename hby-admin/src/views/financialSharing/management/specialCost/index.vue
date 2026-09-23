<template>
  <div class="special-cost-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-star-on"></i>
          专项成本管理
        </h1>
        <p class="page-description">管理项目成本、作业成本、质量成本等专项成本核算和分析</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="createProject">
          新建专项
        </el-button>
        <el-button type="success" icon="el-icon-cpu" @click="costCalculation">
          成本核算
        </el-button>
        <el-button type="warning" icon="el-icon-data-analysis" @click="costAnalysis">
          专项分析
        </el-button>
      </div>
    </div>

    <!-- 专项成本统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-projects">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalProjects }}</div>
              <div class="stat-label">专项总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total-cost">
              <i class="el-icon-coin"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalCost) }}</div>
              <div class="stat-label">总成本</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active-projects">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.activeProjects }}</div>
              <div class="stat-label">进行中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon completion-rate">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.completionRate }}%</div>
              <div class="stat-label">完成率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 项目成本 -->
        <el-col :span="8">
          <div class="module-card project-cost">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-folder-opened"></i>
              </div>
              <div class="card-title">
                <h3>项目成本</h3>
                <span class="card-subtitle">管理项目全生命周期的成本核算</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>项目立项管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>项目成本预算</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>项目成本归集</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>项目成本分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageProjectCost">
                  管理项目成本
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 作业成本 -->
        <el-col :span="8">
          <div class="module-card activity-cost">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-operation"></i>
              </div>
              <div class="card-title">
                <h3>作业成本</h3>
                <span class="card-subtitle">基于作业的成本核算和管理</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>作业识别定义</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>作业成本动因</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>作业成本分配</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>作业绩效分析</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageActivityCost">
                  管理作业成本
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 质量成本 -->
        <el-col :span="8">
          <div class="module-card quality-cost">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-medal"></i>
              </div>
              <div class="card-title">
                <h3>质量成本</h3>
                <span class="card-subtitle">管理质量相关的成本核算和控制</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>预防成本管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>检验成本管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>内部失效成本</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>外部失效成本</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageQualityCost">
                  管理质量成本
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 环境成本 -->
        <el-col :span="8">
          <div class="module-card environment-cost">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-sunny"></i>
              </div>
              <div class="card-title">
                <h3>环境成本</h3>
                <span class="card-subtitle">管理环境保护相关的成本核算</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>环保设施成本</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>污染治理成本</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>环境损失成本</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>绿色效益评估</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageEnvironmentCost">
                  管理环境成本
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 研发成本 -->
        <el-col :span="8">
          <div class="module-card rd-cost">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-cpu"></i>
              </div>
              <div class="card-title">
                <h3>研发成本</h3>
                <span class="card-subtitle">管理研发项目的成本核算和分析</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>研发项目管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>研发费用归集</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>研发成果转化</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>研发效益评估</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageRdCost">
                  管理研发成本
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 专项分析 -->
        <el-col :span="8">
          <div class="module-card special-analysis">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-title">
                <h3>专项分析</h3>
                <span class="card-subtitle">综合分析各类专项成本的效益</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本效益分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>投入产出分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>专项对比分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>优化建议报告</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageSpecialAnalysis">
                  管理专项分析
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 快速操作面板 -->
    <div class="quick-actions-panel">
      <div class="panel-header">
        <h3>
          <i class="el-icon-lightning"></i>
          快速操作
        </h3>
        <p>常用的专项成本管理操作</p>
      </div>
      
      <el-row :gutter="16">
        <el-col :span="4" v-for="action in quickActions" :key="action.key">
          <div class="quick-action-item" @click="handleQuickAction(action)">
            <div class="action-icon">
              <i :class="action.icon"></i>
            </div>
            <div class="action-content">
              <div class="action-title">{{ action.title }}</div>
              <div class="action-desc">{{ action.desc }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getSpecialCostStats } from '@/api/financialSharing/specialCost'

export default {
  name: 'SpecialCostIndex',
  data() {
    return {
      stats: {
        totalProjects: 0,
        totalCost: 0,
        activeProjects: 0,
        completionRate: 0
      },
      quickActions: [
        { key: 'project', title: '项目成本', desc: '管理项目成本', icon: 'el-icon-folder-opened' },
        { key: 'activity', title: '作业成本', desc: '管理作业成本', icon: 'el-icon-s-operation' },
        { key: 'quality', title: '质量成本', desc: '管理质量成本', icon: 'el-icon-medal' },
        { key: 'environment', title: '环境成本', desc: '管理环境成本', icon: 'el-icon-sunny' },
        { key: 'rd', title: '研发成本', desc: '管理研发成本', icon: 'el-icon-cpu' },
        { key: 'analysis', title: '专项分析', desc: '综合分析报告', icon: 'el-icon-data-analysis' }
      ]
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        const response = await getSpecialCostStats()
        if (response.code === 1) {
          const data = response.data || {}
          this.stats = {
            totalProjects: data.totalProjects || 0,
            totalCost: data.totalCost || 0,
            activeProjects: data.activeProjects || 0,
            completionRate: data.completionRate || 0
          }
        } else {
          this.$message.error(response.msg || '加载统计数据失败')
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    formatAmount(amount) {
      return (amount / 10000).toFixed(2) + '万'
    },
    createProject() {
      this.$router.push('/management/projectCost')
    },
    costCalculation() {
      this.$router.push('/financialSharing/management/specialCost/projectCost')
    },
    costAnalysis() {
      this.$router.push('/financialSharing/management/specialCost/specialAnalysis')
    },
    manageProjectCost() {
      this.$router.push('/management/projectCost')
    },
    manageActivityCost() {
      this.$router.push('/management/activityCost')
    },
    manageQualityCost() {
      this.$router.push('/management/qualityCost')
    },
    manageEnvironmentCost() {
      this.$router.push('/management/environmentCost')
    },
    manageRdCost() {
      this.$router.push('/management/rdCost')
    },
    manageSpecialAnalysis() {
      this.$router.push('/management/specialAnalysis')
    },
    handleQuickAction(action) {
      const routeMap = {
        project: '/financialSharing/management/specialCost/projectCost',
        activity: '/financialSharing/management/specialCost/activityCost',
        quality: '/financialSharing/management/specialCost/qualityCost',
        environment: '/financialSharing/management/specialCost/environmentCost',
        rd: '/financialSharing/management/specialCost/rdCost',
        analysis: '/financialSharing/management/specialCost/specialAnalysis'
      }
      const path = routeMap[action.key]
      if (path) {
        this.$router.push(path)
      } else {
        this.$message.info(`${action.title}功能暂未开放`)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.special-cost-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.stats-overview {
  margin-bottom: 24px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 28px;
        color: white;
      }

      &.total-projects {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.total-cost {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.active-projects {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.completion-rate {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.function-modules {
  margin-bottom: 32px;

  .module-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    height: 280px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    &.project-cost {
      border-left: 4px solid #409eff;
    }

    &.activity-cost {
      border-left: 4px solid #67c23a;
    }

    &.quality-cost {
      border-left: 4px solid #e6a23c;
    }

    &.environment-cost {
      border-left: 4px solid #f56c6c;
    }

    &.rd-cost {
      border-left: 4px solid #909399;
    }

    &.special-analysis {
      border-left: 4px solid #9c27b0;
    }
  }
}

.card-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;

  .card-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 16px;
    flex-shrink: 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

    i {
      font-size: 24px;
      color: white;
    }
  }

  .card-title {
    flex: 1;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 4px 0;
    }

    .card-subtitle {
      font-size: 12px;
      color: #909399;
      line-height: 1.4;
    }
  }
}

.card-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.feature-list {
  flex: 1;

  .feature-item {
    display: flex;
    align-items: center;
    margin-bottom: 12px;
    font-size: 14px;
    color: #606266;

    i {
      color: #67c23a;
      margin-right: 8px;
      font-size: 16px;
    }
  }
}

.card-actions {
  margin-top: 16px;
}

.quick-actions-panel {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .panel-header {
    margin-bottom: 24px;

    h3 {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .quick-action-item {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 16px;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;

    &:hover {
      background: #e9ecef;
      transform: translateY(-2px);
    }

    .action-icon {
      width: 40px;
      height: 40px;
      border-radius: 8px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 12px;

      i {
        font-size: 20px;
        color: white;
      }
    }

    .action-content {
      flex: 1;

      .action-title {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .action-desc {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}
</style>
