<template>
  <div class="rules-center-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-operation"></i>
          会计规则中心
        </h1>
        <p class="page-description">管理会计处理规则，提供智能化的会计业务处理能力</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="createRule">
          新建规则
        </el-button>
        <el-button type="success" icon="el-icon-magic-stick" @click="testRuleEngine">
          规则引擎测试
        </el-button>
      </div>
    </div>

    <!-- 规则统计概览 -->
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-s-operation"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.totalRules }}</div>
              <div class="stat-label">规则总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.activeRules }}</div>
              <div class="stat-label">启用规则</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon executed">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.executedToday }}</div>
              <div class="stat-label">今日执行</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon success-rate">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.successRate }}%</div>
              <div class="stat-label">执行成功率</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 规则类型模块 -->
    <div class="rule-modules">
      <el-row :gutter="24">
        <!-- 确认规则 -->
        <el-col :span="12">
          <div class="rule-card recognition-rules">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-circle-check"></i>
              </div>
              <div class="card-title">
                <h3>确认规则</h3>
                <span class="card-subtitle">定义业务事项的确认条件和时点</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.recognitionRules }}</span>
                <span class="stat-label">条规则</span>
              </div>
            </div>
            <div class="card-content">
              <div class="rule-features">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>收入确认规则</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>成本确认规则</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>资产确认规则</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>负债确认规则</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageRecognitionRules">
                  管理确认规则
                </el-button>
                <el-button type="text" size="small" @click="viewRecognitionStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 计量规则 -->
        <el-col :span="12">
          <div class="rule-card measurement-rules">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-scale-to-original"></i>
              </div>
              <div class="card-title">
                <h3>计量规则</h3>
                <span class="card-subtitle">定义会计要素的计量方法和标准</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.measurementRules }}</span>
                <span class="stat-label">条规则</span>
              </div>
            </div>
            <div class="card-content">
              <div class="rule-features">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>历史成本计量</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>公允价值计量</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>现值计量</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>重置成本计量</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageMeasurementRules">
                  管理计量规则
                </el-button>
                <el-button type="text" size="small" @click="viewMeasurementStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 24px;">
        <!-- 凭证规则 -->
        <el-col :span="12">
          <div class="rule-card voucher-rules">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-title">
                <h3>凭证规则</h3>
                <span class="card-subtitle">定义会计凭证的生成规则和模板</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.voucherRules }}</span>
                <span class="stat-label">条规则</span>
              </div>
            </div>
            <div class="card-content">
              <div class="rule-features">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>凭证模板配置</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>科目映射规则</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>分录生成规则</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>凭证校验规则</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="manageVoucherRules">
                  管理凭证规则
                </el-button>
                <el-button type="text" size="small" @click="viewVoucherStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>

        <!-- 过账规则 -->
        <el-col :span="12">
          <div class="rule-card posting-rules">
            <div class="card-header">
              <div class="card-icon">
                <i class="el-icon-s-promotion"></i>
              </div>
              <div class="card-title">
                <h3>过账规则</h3>
                <span class="card-subtitle">定义凭证过账的条件和流程</span>
              </div>
              <div class="card-stats">
                <span class="stat-number">{{ stats.postingRules }}</span>
                <span class="stat-label">条规则</span>
              </div>
            </div>
            <div class="card-content">
              <div class="rule-features">
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>过账条件检查</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>过账顺序控制</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>过账异常处理</span>
                </div>
                <div class="feature-item">
                  <i class="el-icon-circle-check"></i>
                  <span>过账结果验证</span>
                </div>
              </div>
              <div class="card-actions">
                <el-button type="primary" size="small" @click="managePostingRules">
                  管理过账规则
                </el-button>
                <el-button type="text" size="small" @click="viewPostingStats">
                  查看统计
                </el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 规则引擎管理 -->
    <div class="rule-engine-section">
      <div class="section-header">
        <h3>
          <i class="el-icon-cpu"></i>
          规则引擎管理
        </h3>
        <p>管理规则执行引擎，监控规则执行状态和性能</p>
      </div>
      
      <el-row :gutter="24">
        <el-col :span="8">
          <div class="engine-card">
            <div class="engine-icon">
              <i class="el-icon-magic-stick"></i>
            </div>
            <div class="engine-content">
              <h4>规则测试</h4>
              <p>测试规则的执行逻辑和结果</p>
              <el-button type="primary" size="small" @click="testRules">
                开始测试
              </el-button>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="engine-card">
            <div class="engine-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="engine-content">
              <h4>执行监控</h4>
              <p>监控规则执行状态和性能指标</p>
              <el-button type="primary" size="small" @click="viewExecutionMonitor">
                查看监控
              </el-button>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="engine-card">
            <div class="engine-icon">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="engine-content">
              <h4>执行日志</h4>
              <p>查看规则执行的详细日志记录</p>
              <el-button type="primary" size="small" @click="viewExecutionLogs">
                查看日志
              </el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RulesCenterIndex',
  data() {
    return {
      stats: {
        totalRules: 0,
        activeRules: 0,
        executedToday: 0,
        successRate: 0,
        recognitionRules: 0,
        measurementRules: 0,
        voucherRules: 0,
        postingRules: 0
      }
    }
  },
  mounted() {
    this.loadStats()
  },
  methods: {
    async loadStats() {
      try {
        // 暂未对接规则统计 API，先以空状态展示，待后端接口提供后接入
        this.stats = {
          totalRules: 0,
          activeRules: 0,
          executedToday: 0,
          successRate: 0,
          recognitionRules: 0,
          measurementRules: 0,
          voucherRules: 0,
          postingRules: 0
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    createRule() {
      this.$message.info('请在规则列表中点击"新建"按钮创建规则')
    },
    testRuleEngine() {
      this.$message.info('请在对应规则详情页面执行规则测试')
    },
    manageRecognitionRules() {
      this.$message.info('请使用左侧菜单导航至"确认规则"页面进行管理')
    },
    viewRecognitionStats() {
      this.$alert(`<p><b>确认规则总数：</b>${this.ruleStats.recognitionRules || 0}</p><p>详细统计请进入"确认规则"页面查看</p>`, '确认规则统计', { dangerouslyUseHTMLString: true })
    },
    manageMeasurementRules() {
      this.$message.info('请使用左侧菜单导航至"计量规则"页面进行管理')
    },
    viewMeasurementStats() {
      this.$alert(`<p><b>计量规则总数：</b>${this.ruleStats.measurementRules || 0}</p><p>详细统计请进入"计量规则"页面查看</p>`, '计量规则统计', { dangerouslyUseHTMLString: true })
    },
    manageVoucherRules() {
      this.$message.info('请使用左侧菜单导航至"凭证规则"页面进行管理')
    },
    viewVoucherStats() {
      this.$alert(`<p><b>凭证规则总数：</b>${this.ruleStats.voucherRules || 0}</p><p>详细统计请进入"凭证规则"页面查看</p>`, '凭证规则统计', { dangerouslyUseHTMLString: true })
    },
    managePostingRules() {
      this.$message.info('请使用左侧菜单导航至"过账规则"页面进行管理')
    },
    viewPostingStats() {
      this.$alert(`<p><b>过账规则总数：</b>${this.ruleStats.postingRules || 0}</p><p>详细统计请进入"过账规则"页面查看</p>`, '过账规则统计', { dangerouslyUseHTMLString: true })
    },
    testRules() {
      this.$message.info('请在对应规则详情页面进行规则测试')
    },
    viewExecutionMonitor() {
      this.$message.info('请在对应规则详情页面查看执行监控')
    },
    viewExecutionLogs() {
      this.$message.info('请在对应规则详情页面查看执行日志')
    }
  }
}
</script>

<style lang="scss" scoped>
.rules-center-container {
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

      &.active {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }

      &.executed {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.success-rate {
        background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
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

.rule-modules {
  margin-bottom: 32px;

  .rule-card {
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

    &.recognition-rules {
      border-left: 4px solid #67c23a;
    }

    &.measurement-rules {
      border-left: 4px solid #409eff;
    }

    &.voucher-rules {
      border-left: 4px solid #e6a23c;
    }

    &.posting-rules {
      border-left: 4px solid #f56c6c;
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

  .card-stats {
    text-align: right;

    .stat-number {
      display: block;
      font-size: 24px;
      font-weight: 600;
      color: #409eff;
    }

    .stat-label {
      font-size: 12px;
      color: #909399;
    }
  }
}

.card-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.rule-features {
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

  .el-button {
    margin-right: 12px;
  }
}

.rule-engine-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .section-header {
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

  .engine-card {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 20px;
    text-align: center;
    transition: all 0.3s ease;

    &:hover {
      background: #e9ecef;
      transform: translateY(-2px);
    }

    .engine-icon {
      margin-bottom: 16px;

      i {
        font-size: 32px;
        color: #409eff;
      }
    }

    .engine-content {
      h4 {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 8px 0;
      }

      p {
        font-size: 14px;
        color: #606266;
        margin: 0 0 16px 0;
        line-height: 1.4;
      }
    }
  }
}
</style>
