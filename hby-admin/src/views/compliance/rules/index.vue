<template>
  <div class="compliance-rules">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-document"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalRules }}</div>
              <div class="label">合规规则</div>
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
              <div class="number">{{ overviewData.activeRules }}</div>
              <div class="label">生效规则</div>
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
              <div class="number">{{ overviewData.violationCount }}</div>
              <div class="label">违规事项</div>
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
              <div class="number">{{ overviewData.complianceRate }}%</div>
              <div class="label">合规率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">合规规则管理</span>
        <div class="card-actions">
          <el-button type="primary" @click="createRule">新建规则</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="规则制定" name="formulation">
          <div class="tab-content">
            <p>合规规则制定管理，包括规则设计、条件配置、触发机制、执行逻辑等规则制定功能。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="规则分类" name="category">
          <div class="tab-content">
            <p>合规规则分类管理，包括法律法规、内部制度、行业标准、监管要求等规则分类体系。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="规则引擎" name="engine">
          <div class="tab-content">
            <p>合规规则引擎管理，包括规则解析、规则执行、结果判定、异常处理等引擎功能。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="规则测试" name="testing">
          <div class="tab-content">
            <p>合规规则测试验证，包括规则测试、场景验证、效果评估、优化调整等测试机制。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="规则维护" name="maintenance">
          <div class="tab-content">
            <p>合规规则维护管理，包括规则更新、版本控制、失效管理、归档处理等维护功能。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="规则监控" name="monitoring">
          <div class="tab-content">
            <p>合规规则监控分析，包括执行监控、效果分析、违规统计、趋势分析等监控功能。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">规则列表</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索规则名称或分类"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="ruleName" label="规则名称" width="200" />
        <el-table-column prop="ruleType" label="规则类型" width="120" />
        <el-table-column prop="priority" label="优先级" width="100" />
        <el-table-column prop="effectiveDate" label="生效日期" width="120" />
        <el-table-column prop="violationCount" label="违规次数" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="创建人" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewRule(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="editRule(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="testRule(scope.row)">测试</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ComplianceRules',
  data() {
    return {
      activeTab: 'formulation',
      searchText: '',
      overviewData: {
        totalRules: 234,
        activeRules: 198,
        violationCount: 45,
        complianceRate: 94.5
      },
      tableData: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      console.log('加载合规规则数据')
      this.tableData = this.generateMockData()
    },
    generateMockData() {
      const ruleNames = ['资金支付审批规则', '合同签署权限规则', '投资决策流程规则', '采购限额控制规则', '人员招聘审批规则', '资产处置审批规则']
      const ruleTypes = ['审批规则', '权限规则', '流程规则', '限额规则', '时间规则', '条件规则']
      const priorities = ['高', '中', '低']
      const statuses = ['生效', '草稿', '停用', '测试']
      const creators = ['合规部', '法务部', '风控部', '审计部']
      
      return Array.from({ length: 6 }, (_, index) => ({
        id: index + 1,
        ruleName: ruleNames[index],
        ruleType: ruleTypes[index],
        priority: priorities[Math.floor(Math.random() * priorities.length)],
        effectiveDate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-01`,
        violationCount: Math.floor(Math.random() * 20),
        status: statuses[Math.floor(Math.random() * statuses.length)],
        creator: creators[Math.floor(Math.random() * creators.length)]
      }))
    },
    createRule() {
      console.log('新建规则')
    },
    viewRule(row) {
      console.log('查看规则', row)
    },
    editRule(row) {
      console.log('编辑规则', row)
    },
    testRule(row) {
      console.log('测试规则', row)
    },
    handleSearch() {
      console.log('搜索', this.searchText)
    },
    getStatusType(status) {
      const typeMap = {
        '生效': 'success',
        '草稿': 'info',
        '停用': 'warning',
        '测试': 'primary'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.compliance-rules {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;
    
    .overview-card {
      height: 120px;
      
      &.gradient-theme {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
