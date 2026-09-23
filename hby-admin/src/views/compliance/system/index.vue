<template>
  <div class="compliance-system">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-document"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalSystems }}</div>
              <div class="label">制度总数</div>
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
              <div class="number">{{ overviewData.activeSystems }}</div>
              <div class="label">有效制度</div>
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
              <div class="number">{{ overviewData.updateCount }}</div>
              <div class="label">本月更新</div>
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
              <div class="number">{{ overviewData.coverageRate }}%</div>
              <div class="label">覆盖率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">内控制度管理</span>
        <div class="card-actions">
          <el-button type="primary" @click="createSystem">新建制度</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="制度体系" name="framework">
          <div class="tab-content">
            <p>内控制度体系建设，包括制度框架、制度分类、制度层级、制度关联等体系化管理。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="制度制定" name="formulation">
          <div class="tab-content">
            <p>内控制度制定流程，包括需求分析、制度起草、内容审核、专家评议等制度制定过程。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="制度审批" name="approval">
          <div class="tab-content">
            <p>内控制度审批管理，包括审批流程、审批权限、审批记录、审批结果等审批过程控制。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="制度发布" name="publication">
          <div class="tab-content">
            <p>内控制度发布管理，包括发布渠道、发布范围、发布通知、生效管理等发布流程控制。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="制度维护" name="maintenance">
          <div class="tab-content">
            <p>内控制度维护管理，包括制度修订、版本控制、废止管理、归档管理等维护机制。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="制度评估" name="evaluation">
          <div class="tab-content">
            <p>内控制度评估分析，包括执行效果评估、适用性评估、完善性评估、改进建议等。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 图表分析 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">制度分类统计</div>
          <div id="categoryChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">制度更新趋势</div>
          <div id="updateTrendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">制度列表</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索制度名称或分类"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="systemName" label="制度名称" width="250" />
        <el-table-column prop="category" label="制度分类" width="120" />
        <el-table-column prop="version" label="版本" width="100" />
        <el-table-column prop="effectiveDate" label="生效日期" width="120" />
        <el-table-column prop="lastUpdate" label="最后更新" width="120" />
        <el-table-column prop="applicableScope" label="适用范围" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="制定人" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewSystem(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="editSystem(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="reviseSystem(scope.row)">修订</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ComplianceSystem',
  data() {
    return {
      activeTab: 'framework',
      searchText: '',
      overviewData: {
        totalSystems: 156,
        activeSystems: 134,
        updateCount: 23,
        coverageRate: 95.8
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
      console.log('加载内控制度数据')
      this.tableData = this.generateMockData()
    },
    generateMockData() {
      const systemNames = ['财务管理制度', '人力资源管理制度', '采购管理制度', '投资管理制度', '风险管理制度', '信息系统管理制度', '合同管理制度', '资产管理制度', '内部审计制度', '党建工作制度']
      const categories = ['财务管理', '人力资源', '采购管理', '投资管理', '风险控制', '信息管理', '合同管理', '资产管理']
      const statuses = ['有效', '修订中', '待审批', '已废止']
      const creators = ['法务部', '财务部', '人事部', '风控部', '审计部']
      const scopes = ['全集团', '子公司', '总部', '分支机构']
      
      return Array.from({ length: 10 }, (_, index) => ({
        id: index + 1,
        systemName: systemNames[index],
        category: categories[Math.floor(Math.random() * categories.length)],
        version: `v${Math.floor(Math.random() * 5) + 1}.${Math.floor(Math.random() * 10)}`,
        effectiveDate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-01`,
        lastUpdate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
        applicableScope: scopes[Math.floor(Math.random() * scopes.length)],
        status: statuses[Math.floor(Math.random() * statuses.length)],
        creator: creators[Math.floor(Math.random() * creators.length)]
      }))
    },
    initCharts() {
      console.log('初始化制度图表')
    },
    createSystem() {
      console.log('新建制度')
    },
    viewSystem(row) {
      console.log('查看制度', row)
    },
    editSystem(row) {
      console.log('编辑制度', row)
    },
    reviseSystem(row) {
      console.log('修订制度', row)
    },
    handleSearch() {
      console.log('搜索', this.searchText)
    },
    getStatusType(status) {
      const typeMap = {
        '有效': 'success',
        '修订中': 'warning',
        '待审批': 'info',
        '已废止': 'danger'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.compliance-system {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;
    
    .overview-card {
      height: 120px;
      
      &.gradient-theme {
        background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
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
