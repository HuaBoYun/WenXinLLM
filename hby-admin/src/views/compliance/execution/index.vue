<template>
  <div class="compliance-execution">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-monitor"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalTasks }}</div>
              <div class="label">执行任务</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-success"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.completionRate }}%</div>
              <div class="label">完成率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-warning"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.riskCount }}</div>
              <div class="label">风险事项</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.avgScore }}</div>
              <div class="label">平均评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">内控执行监控</span>
        <div class="card-actions">
          <el-button type="primary" @click="createTask">新建任务</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="执行计划" name="plan">
          <div class="tab-content">
            <p>内控执行计划管理，包括执行计划制定、任务分解、时间安排、责任分工等计划管理。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="执行监控" name="monitoring">
          <div class="tab-content">
            <p>内控执行监控功能，包括实时监控、进度跟踪、异常预警、执行评估等监控机制。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="执行记录" name="records">
          <div class="tab-content">
            <p>内控执行记录管理，包括执行日志、操作记录、证据收集、档案管理等记录保存。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="问题管理" name="issues">
          <div class="tab-content">
            <p>执行问题管理，包括问题识别、问题分析、整改措施、跟踪验证等问题处理流程。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="效果评估" name="assessment">
          <div class="tab-content">
            <p>执行效果评估，包括执行质量评估、效果分析、改进建议、最佳实践等评估分析。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="报告生成" name="reporting">
          <div class="tab-content">
            <p>执行报告生成，包括执行报告、监控报告、问题报告、改进报告等报告管理。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 图表分析 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">执行进度统计</div>
          <div id="progressChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">问题分布分析</div>
          <div id="issueChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">执行记录</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索制度名称或执行人"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="systemName" label="制度名称" width="200" />
        <el-table-column prop="executionType" label="执行类型" width="120" />
        <el-table-column prop="executor" label="执行人" width="120" />
        <el-table-column prop="startDate" label="开始时间" width="120" />
        <el-table-column prop="progress" label="执行进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.progress" :stroke-width="8" />
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskType(scope.row.riskLevel)">
              {{ scope.row.riskLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="执行评分" width="100">
          <template slot-scope="scope">
            <el-tag :type="getScoreType(scope.row.score)">
              {{ scope.row.score }}分
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewDetail(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="monitor(scope.row)">监控</el-button>
            <el-button size="mini" type="warning" @click="evaluate(scope.row)">评估</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ComplianceExecution',
  data() {
    return {
      activeTab: 'plan',
      searchText: '',
      overviewData: {
        totalTasks: 456,
        completionRate: 87.5,
        riskCount: 23,
        avgScore: 89.2
      },
      tableData: []
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  methods: {
    loadData() {
      console.log('加载执行监控数据')
      this.tableData = this.generateMockData()
    },
    generateMockData() {
      const systemNames = ['财务管理制度', '人力资源管理制度', '采购管理制度', '投资管理制度', '风险管理制度', '信息系统管理制度', '合同管理制度', '资产管理制度']
      const executionTypes = ['定期执行', '专项执行', '临时执行', '应急执行']
      const executors = ['张明', '李华', '王强', '刘芳', '陈杰', '赵敏']
      const riskLevels = ['低风险', '中风险', '高风险']
      const statuses = ['执行中', '已完成', '暂停', '异常']
      
      return Array.from({ length: 8 }, (_, index) => ({
        id: index + 1,
        systemName: systemNames[index],
        executionType: executionTypes[Math.floor(Math.random() * executionTypes.length)],
        executor: executors[Math.floor(Math.random() * executors.length)],
        startDate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
        progress: Math.floor(Math.random() * 100),
        riskLevel: riskLevels[Math.floor(Math.random() * riskLevels.length)],
        score: Math.floor(Math.random() * 30) + 70,
        status: statuses[Math.floor(Math.random() * statuses.length)]
      }))
    },
    initCharts() {
      console.log('初始化执行图表')
    },
    createTask() {
      console.log('新建执行任务')
    },
    viewDetail(row) {
      console.log('查看执行详情', row)
    },
    monitor(row) {
      console.log('监控执行', row)
    },
    evaluate(row) {
      console.log('评估执行', row)
    },
    handleSearch() {
      console.log('搜索', this.searchText)
    },
    getRiskType(level) {
      const typeMap = {
        '低风险': 'success',
        '中风险': 'warning',
        '高风险': 'danger'
      }
      return typeMap[level] || 'info'
    },
    getScoreType(score) {
      if (score >= 90) return 'success'
      if (score >= 80) return 'warning'
      if (score >= 70) return 'info'
      return 'danger'
    },
    getStatusType(status) {
      const typeMap = {
        '执行中': 'warning',
        '已完成': 'success',
        '暂停': 'info',
        '异常': 'danger'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.compliance-execution {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;
    
    .overview-card {
      height: 120px;
      
      &.gradient-theme {
        background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
        color: #333;
        
        .card-content {
          display: flex;
          align-items: center;
          height: 100%;
          
          .icon-wrapper {
            font-size: 40px;
            margin-right: 15px;
            opacity: 0.8;
          }
          
          .data-wrapper {
            .number {
              font-size: 28px;
              font-weight: bold;
              line-height: 1;
            }
            
            .label {
              font-size: 14px;
              margin-top: 5px;
              opacity: 0.8;
            }
          }
        }
      }
    }
  }

  .function-card, .chart-section, .data-card {
    margin-bottom: 20px;
  }

  .chart-section {
    .chart-card {
      height: 380px;
    }
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .card-title {
      font-size: 16px;
      font-weight: bold;
    }
  }

  .search-wrapper {
    display: flex;
    align-items: center;
  }

  .tab-content {
    padding: 20px;
    min-height: 200px;
    color: #666;
    line-height: 1.6;
  }

  ::v-deep .el-tabs__content {
    padding: 0;
  }
}
</style>
