<template>
  <div class="report-quality">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-medal"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.qualityScore }}</div>
              <div class="label">质量评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-check"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.passRate }}%</div>
              <div class="label">通过率</div>
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
              <div class="number">{{ overviewData.issueCount }}</div>
              <div class="label">质量问题</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-refresh"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.improvementRate }}%</div>
              <div class="label">改进率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">报告质量控制</span>
        <div class="card-actions">
          <el-button type="primary" @click="startQualityCheck">质量检查</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="质量标准" name="standards">
          <div class="tab-content">
            <p>报告质量标准管理，包括质量指标、评价标准、检查规则、质量要求等标准体系建设。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="自动检查" name="autocheck">
          <div class="tab-content">
            <p>自动质量检查功能，包括格式检查、数据校验、逻辑验证、完整性检查等自动化质量控制。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="人工审核" name="review">
          <div class="tab-content">
            <p>人工质量审核流程，包括专家审核、同行评议、多级审核、审核意见等人工质量保障。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="问题管理" name="issues">
          <div class="tab-content">
            <p>质量问题管理，包括问题识别、问题分类、问题跟踪、问题解决等问题处理流程。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="质量改进" name="improvement">
          <div class="tab-content">
            <p>质量改进管理，包括改进计划、改进措施、效果评估、持续改进等质量提升机制。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="质量报告" name="qualityreport">
          <div class="tab-content">
            <p>质量报告生成，包括质量分析报告、改进建议报告、质量趋势报告等质量管理报告。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 图表分析 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">质量趋势分析</div>
          <div id="qualityTrendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">问题分类统计</div>
          <div id="issueChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">质量检查记录</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索报告名称"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="reportName" label="报告名称" width="200" />
        <el-table-column prop="checkType" label="检查类型" width="120" />
        <el-table-column prop="checkDate" label="检查时间" width="150" />
        <el-table-column prop="qualityScore" label="质量评分" width="100">
          <template slot-scope="scope">
            <el-tag :type="getScoreType(scope.row.qualityScore)">
              {{ scope.row.qualityScore }}分
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="issueCount" label="问题数量" width="100" />
        <el-table-column prop="checkResult" label="检查结果" width="100">
          <template slot-scope="scope">
            <el-tag :type="getResultType(scope.row.checkResult)">
              {{ scope.row.checkResult }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checker" label="检查人" width="120" />
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
            <el-button size="mini" type="primary" @click="viewIssues(scope.row)">问题</el-button>
            <el-button size="mini" type="warning" @click="recheck(scope.row)">重检</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ReportQuality',
  data() {
    return {
      activeTab: 'standards',
      searchText: '',
      overviewData: {
        qualityScore: 87.5,
        passRate: 94.2,
        issueCount: 156,
        improvementRate: 78.5
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
      console.log('加载质量控制数据')
      this.tableData = this.generateMockData()
    },
    generateMockData() {
      const reportNames = ['2024年第三季度监管报告', '示例能源集团财务分析报告', '风险评估专项报告', '合规检查月度报告', '投资决策分析报告', '经营绩效评价报告', '内控制度执行报告', '党建工作总结报告']
      const checkTypes = ['自动检查', '人工审核', '专家评议', '同行评审', '综合检查']
      const checkResults = ['通过', '有条件通过', '不通过', '需要修改']
      const checkers = ['质控部', '张明', '李华', '王强', '专家组']
      const statuses = ['已完成', '检查中', '待整改', '已关闭']
      
      return Array.from({ length: 8 }, (_, index) => ({
        id: index + 1,
        reportName: reportNames[index],
        checkType: checkTypes[Math.floor(Math.random() * checkTypes.length)],
        checkDate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}`,
        qualityScore: Math.floor(Math.random() * 30) + 70,
        issueCount: Math.floor(Math.random() * 20),
        checkResult: checkResults[Math.floor(Math.random() * checkResults.length)],
        checker: checkers[Math.floor(Math.random() * checkers.length)],
        status: statuses[Math.floor(Math.random() * statuses.length)]
      }))
    },
    initCharts() {
      console.log('初始化质量图表')
    },
    startQualityCheck() {
      console.log('开始质量检查')
    },
    viewDetail(row) {
      console.log('查看质量详情', row)
    },
    viewIssues(row) {
      console.log('查看质量问题', row)
    },
    recheck(row) {
      console.log('重新检查', row)
    },
    handleSearch() {
      console.log('搜索', this.searchText)
    },
    getScoreType(score) {
      if (score >= 90) return 'success'
      if (score >= 80) return 'warning'
      if (score >= 70) return 'info'
      return 'danger'
    },
    getResultType(result) {
      const typeMap = {
        '通过': 'success',
        '有条件通过': 'warning',
        '不通过': 'danger',
        '需要修改': 'info'
      }
      return typeMap[result] || 'info'
    },
    getStatusType(status) {
      const typeMap = {
        '已完成': 'success',
        '检查中': 'warning',
        '待整改': 'info',
        '已关闭': 'info'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.report-quality {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;
    
    .overview-card {
      height: 120px;
      
      &.gradient-theme {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        color: white;
        
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
              opacity: 0.9;
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
