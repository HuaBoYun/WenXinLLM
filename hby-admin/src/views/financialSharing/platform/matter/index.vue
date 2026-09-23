<template>
  <div class="matter-center-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-document"></i>
          事项中心模块
        </h1>
        <p class="page-description">管理业务事项数据，提供事项处理和分录生成服务</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="createMatter">
          新建事项
        </el-button>
        <el-button type="success" icon="el-icon-upload2" @click="importMatters">
          批量导入
        </el-button>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalMatters }}</div>
              <div class="stat-label">事项总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.pendingMatters }}</div>
              <div class="stat-label">待处理事项</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon processing">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.processingMatters }}</div>
              <div class="stat-label">处理中事项</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon completed">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.completedMatters }}</div>
              <div class="stat-label">已完成事项</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块 -->
    <div class="function-modules">
      <el-row :gutter="24">
        <!-- 事项数据管理 -->
        <el-col :span="8">
          <div class="module-card">
            <div class="card-header">
              <div class="card-icon data-management">
                <i class="el-icon-s-data"></i>
              </div>
              <div class="card-title">
                <h3>事项数据管理</h3>
                <span class="card-subtitle">管理业务事项的基础数据和属性信息</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>事项数据录入</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>事项数据查询</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>事项数据修改</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>批量数据导入</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageMatterData">
                  管理事项数据
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 事项分录管理 -->
        <el-col :span="8">
          <div class="module-card">
            <div class="card-header">
              <div class="card-icon entry-management">
                <i class="el-icon-s-order"></i>
              </div>
              <div class="card-title">
                <h3>事项分录管理</h3>
                <span class="card-subtitle">管理事项对应的会计分录信息</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>分录自动生成</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>分录手工调整</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>分录审核确认</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>分录查询统计</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageMatterEntry">
                  管理事项分录
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 事项处理流程 -->
        <el-col :span="8">
          <div class="module-card">
            <div class="card-header">
              <div class="card-icon process-management">
                <i class="el-icon-s-operation"></i>
              </div>
              <div class="card-title">
                <h3>事项处理流程</h3>
                <span class="card-subtitle">配置和管理事项的处理流程</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>流程模板配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>流程实例管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>流程监控跟踪</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>异常处理机制</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageMatterProcess">
                  管理处理流程
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 事项类型管理 -->
        <el-col :span="8">
          <div class="module-card">
            <div class="card-header">
              <div class="card-icon type-management">
                <i class="el-icon-s-grid"></i>
              </div>
              <div class="card-title">
                <h3>事项类型管理</h3>
                <span class="card-subtitle">定义和管理不同类型的业务事项</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>事项类型定义</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>类型属性配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>类型规则设置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>类型关联管理</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageMatterType">
                  管理事项类型
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 事项模板管理 -->
        <el-col :span="8">
          <div class="module-card">
            <div class="card-header">
              <div class="card-icon template-management">
                <i class="el-icon-document-copy"></i>
              </div>
              <div class="card-title">
                <h3>事项模板管理</h3>
                <span class="card-subtitle">管理事项数据录入和处理模板</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>录入模板设计</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>处理模板配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>模板版本管理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>模板应用统计</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageMatterTemplate">
                  管理事项模板
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 事项监控分析 -->
        <el-col :span="8">
          <div class="module-card">
            <div class="card-header">
              <div class="card-icon monitor-analysis">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="card-title">
                <h3>事项监控分析</h3>
                <span class="card-subtitle">监控事项处理状态和分析处理效率</span>
              </div>
            </div>
            <div class="card-content">
              <div class="feature-list">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>实时状态监控</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>处理效率分析</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>异常事项预警</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>统计报表生成</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="viewMatterAnalysis">
                  查看监控分析
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MatterCenterIndex',
  data() {
    return {
      stats: {
        totalMatters: 0,
        pendingMatters: 0,
        processingMatters: 0,
        completedMatters: 0
      }
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        // 暂未对接事项统计 API，先以空状态展示，待后端接口提供后接入
        this.stats = {
          totalMatters: 0,
          pendingMatters: 0,
          processingMatters: 0,
          completedMatters: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    createMatter() {
      this.$message.info('请使用左侧菜单进入"事项数据管理"页面创建新事项')
    },
    importMatters() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls,.csv'
      input.onchange = (e) => {
        const file = e.target.files[0]
        if (!file) return
        this.$message.success(`文件 ${file.name} 已上传，处理中...`)
      }
      input.click()
    },
    manageMatterData() {
      this.$message.info('请使用左侧菜单进入"事项数据管理"页面')
    },
    manageMatterEntry() {
      this.$message.info('请使用左侧菜单进入"事项分录管理"页面')
    },
    manageMatterProcess() {
      this.$message.info('请使用左侧菜单进入"事项处理流程"页面')
    },
    manageMatterType() {
      this.$message.info('请使用左侧菜单进入"事项类型管理"页面')
    },
    manageMatterTemplate() {
      this.$message.info('请使用左侧菜单进入"事项模板管理"页面')
    },
    viewMatterAnalysis() {
      this.$message.info('请使用左侧菜单进入"事项监控分析"页面')
    }
  }
}
</script>

<style lang="scss" scoped>
.matter-center-container {
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

      &.total {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.pending {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.processing {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.completed {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
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
  .module-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    height: 320px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
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

    i {
      font-size: 24px;
      color: white;
    }

    &.data-management {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }

    &.entry-management {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }

    &.process-management {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }

    &.type-management {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }

    &.template-management {
      background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
    }

    &.monitor-analysis {
      background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
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
</style>
