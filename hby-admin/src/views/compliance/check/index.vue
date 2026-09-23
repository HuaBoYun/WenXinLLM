<template>
  <div class="compliance-check">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-search"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalChecks }}</div>
              <div class="label">检查总数</div>
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
              <div class="label">发现问题</div>
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
              <i class="el-icon-refresh"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.rectificationRate }}%</div>
              <div class="label">整改率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">合规性检查</span>
        <div class="card-actions">
          <el-button type="primary" @click="startCheck">发起检查</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="检查计划" name="plan">
          <div class="tab-content">
            <p>合规检查计划管理，包括检查计划制定、检查范围确定、检查时间安排、检查人员配置等。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="检查执行" name="execution">
          <div class="tab-content">
            <p>合规检查执行管理，包括现场检查、文档审查、数据分析、访谈调研等检查执行活动。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="问题识别" name="identification">
          <div class="tab-content">
            <p>合规问题识别管理，包括问题发现、问题分类、风险评估、影响分析等问题识别功能。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="整改跟踪" name="rectification">
          <div class="tab-content">
            <p>整改跟踪管理，包括整改计划、整改措施、进度跟踪、效果验证等整改过程管理。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="检查报告" name="reporting">
          <div class="tab-content">
            <p>检查报告管理，包括报告编制、问题汇总、建议提出、报告审核等报告生成功能。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="持续改进" name="improvement">
          <div class="tab-content">
            <p>持续改进管理，包括改进建议、最佳实践、经验总结、制度完善等改进机制。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">检查记录</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索检查项目或部门"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="checkProject" label="检查项目" width="200" />
        <el-table-column prop="checkType" label="检查类型" width="120" />
        <el-table-column prop="department" label="被检部门" width="150" />
        <el-table-column prop="checkDate" label="检查时间" width="120" />
        <el-table-column prop="issueCount" label="问题数量" width="100" />
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskType(scope.row.riskLevel)">
              {{ scope.row.riskLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkResult" label="检查结果" width="100">
          <template slot-scope="scope">
            <el-tag :type="getResultType(scope.row.checkResult)">
              {{ scope.row.checkResult }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checker" label="检查人" width="100" />
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
            <el-button size="mini" type="warning" @click="generateReport(scope.row)">报告</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ComplianceCheck',
  data() {
    return {
      activeTab: 'plan',
      searchText: '',
      overviewData: {
        totalChecks: 156,
        issueCount: 89,
        passRate: 78.5,
        rectificationRate: 92.3
      },
      tableData: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      console.log('加载合规检查数据')
      this.tableData = this.generateMockData()
    },
    generateMockData() {
      const checkProjects = ['财务管理合规检查', '采购流程合规检查', '人事制度合规检查', '投资决策合规检查', '信息安全合规检查', '环保合规检查']
      const checkTypes = ['定期检查', '专项检查', '突击检查', '跟踪检查']
      const departments = ['财务部', '采购部', '人事部', '投资部', '信息部', '安全部']
      const riskLevels = ['低风险', '中风险', '高风险']
      const checkResults = ['合规', '基本合规', '不合规', '严重违规']
      const checkers = ['合规部', '审计部', '监察部', '外部审计']
      const statuses = ['已完成', '检查中', '整改中', '已关闭']
      
      return Array.from({ length: 6 }, (_, index) => ({
        id: index + 1,
        checkProject: checkProjects[index],
        checkType: checkTypes[Math.floor(Math.random() * checkTypes.length)],
        department: departments[index],
        checkDate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
        issueCount: Math.floor(Math.random() * 20),
        riskLevel: riskLevels[Math.floor(Math.random() * riskLevels.length)],
        checkResult: checkResults[Math.floor(Math.random() * checkResults.length)],
        checker: checkers[Math.floor(Math.random() * checkers.length)],
        status: statuses[Math.floor(Math.random() * statuses.length)]
      }))
    },
    startCheck() {
      console.log('发起检查')
    },
    viewDetail(row) {
      console.log('查看检查详情', row)
    },
    viewIssues(row) {
      console.log('查看问题', row)
    },
    generateReport(row) {
      console.log('生成报告', row)
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
    getResultType(result) {
      const typeMap = {
        '合规': 'success',
        '基本合规': 'warning',
        '不合规': 'danger',
        '严重违规': 'danger'
      }
      return typeMap[result] || 'info'
    },
    getStatusType(status) {
      const typeMap = {
        '已完成': 'success',
        '检查中': 'warning',
        '整改中': 'info',
        '已关闭': 'info'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.compliance-check {
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

  .function-card, .data-card {
    margin-bottom: 20px;
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
