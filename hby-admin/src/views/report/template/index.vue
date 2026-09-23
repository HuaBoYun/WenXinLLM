<template>
  <div class="report-template">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-document"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalTemplates }}</div>
              <div class="label">模板总数</div>
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
              <div class="number">{{ overviewData.activeTemplates }}</div>
              <div class="label">启用模板</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.usageCount }}</div>
              <div class="label">使用次数</div>
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
              <div class="number">{{ overviewData.avgRating }}</div>
              <div class="label">平均评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">报告模板管理</span>
        <div class="card-actions">
          <el-button type="primary" @click="createTemplate">新建模板</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="模板设计" name="design">
          <div class="tab-content">
            <p>报告模板设计功能，包括模板结构设计、字段配置、样式设置、版式布局等模板创建工具。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="模板分类" name="category">
          <div class="tab-content">
            <p>报告模板分类管理，包括监管报告、财务报告、风险报告、合规报告等不同类型模板分类。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="版本管理" name="version">
          <div class="tab-content">
            <p>模板版本管理，包括版本控制、历史版本、版本对比、版本回滚等版本管理功能。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="权限控制" name="permission">
          <div class="tab-content">
            <p>模板权限控制，包括使用权限、编辑权限、审批权限、发布权限等权限管理机制。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="模板审核" name="review">
          <div class="tab-content">
            <p>模板审核流程，包括模板提交、审核流程、审核意见、审核结果等模板质量控制。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="使用统计" name="statistics">
          <div class="tab-content">
            <p>模板使用统计，包括使用频率、使用效果、用户反馈、优化建议等使用情况分析。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 图表分析 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">模板使用统计</div>
          <div id="usageChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">模板类型分布</div>
          <div id="typeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">模板列表</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索模板名称或类型"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="templateName" label="模板名称" width="200" />
        <el-table-column prop="templateType" label="模板类型" width="120" />
        <el-table-column prop="version" label="版本" width="100" />
        <el-table-column prop="creator" label="创建人" width="120" />
        <el-table-column prop="createDate" label="创建时间" width="120" />
        <el-table-column prop="usageCount" label="使用次数" width="100" />
        <el-table-column prop="rating" label="评分" width="100">
          <template slot-scope="scope">
            <el-rate v-model="scope.row.rating" disabled show-score />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewTemplate(scope.row)">预览</el-button>
            <el-button size="mini" type="primary" @click="editTemplate(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="copyTemplate(scope.row)">复制</el-button>
            <el-button size="mini" type="success" @click="useTemplate(scope.row)">使用</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ReportTemplate',
  data() {
    return {
      activeTab: 'design',
      searchText: '',
      overviewData: {
        totalTemplates: 156,
        activeTemplates: 134,
        usageCount: 2856,
        avgRating: 4.6
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
      console.log('加载模板数据')
      this.tableData = this.generateMockData()
    },
    generateMockData() {
      const templateNames = ['月度监管报告模板', '季度财务报告模板', '年度风险评估报告', '合规检查报告模板', '专项审计报告模板', '投资决策报告模板', '经营分析报告模板', '绩效考核报告模板']
      const templateTypes = ['监管报告', '财务报告', '风险报告', '合规报告', '审计报告', '决策报告']
      const creators = ['张明', '李华', '王强', '刘芳', '陈杰', '赵敏']
      const statuses = ['启用', '停用', '草稿', '审核中']
      
      return Array.from({ length: 8 }, (_, index) => ({
        id: index + 1,
        templateName: templateNames[index],
        templateType: templateTypes[Math.floor(Math.random() * templateTypes.length)],
        version: `v${Math.floor(Math.random() * 5) + 1}.${Math.floor(Math.random() * 10)}`,
        creator: creators[Math.floor(Math.random() * creators.length)],
        createDate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
        usageCount: Math.floor(Math.random() * 500) + 50,
        rating: Math.floor(Math.random() * 2) + 4,
        status: statuses[Math.floor(Math.random() * statuses.length)]
      }))
    },
    initCharts() {
      console.log('初始化模板图表')
    },
    createTemplate() {
      console.log('新建模板')
    },
    viewTemplate(row) {
      console.log('预览模板', row)
    },
    editTemplate(row) {
      console.log('编辑模板', row)
    },
    copyTemplate(row) {
      console.log('复制模板', row)
    },
    useTemplate(row) {
      console.log('使用模板', row)
    },
    handleSearch() {
      console.log('搜索', this.searchText)
    },
    getStatusType(status) {
      const typeMap = {
        '启用': 'success',
        '停用': 'info',
        '草稿': 'warning',
        '审核中': 'primary'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.report-template {
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
