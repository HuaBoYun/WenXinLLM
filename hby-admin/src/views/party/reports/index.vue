<template>
  <div class="responsibility-reports">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-document"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalReports }}</div>
              <div class="label">报告总数</div>
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
              <div class="number">{{ overviewData.publishedReports }}</div>
              <div class="label">已发布</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-view"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalViews }}</div>
              <div class="label">阅读量</div>
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
        <span class="card-title">社会责任报告</span>
        <div class="card-actions">
          <el-button type="primary" @click="createReport">新建报告</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="报告编制" name="compilation">
          <div class="tab-content">
            <p>社会责任报告编制，包括报告框架设计、内容编写、数据收集、图表制作等编制工作。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="报告审核" name="review">
          <div class="tab-content">
            <p>社会责任报告审核，包括内容审核、数据验证、合规检查、质量控制等审核流程。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="报告发布" name="publication">
          <div class="tab-content">
            <p>社会责任报告发布，包括发布渠道、发布时间、发布形式、传播推广等发布管理。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="报告评估" name="assessment">
          <div class="tab-content">
            <p>社会责任报告评估，包括报告质量评估、社会反响评估、改进建议等评估分析。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="报告管理" name="management">
          <div class="tab-content">
            <p>社会责任报告管理，包括报告归档、版本管理、权限控制、历史查询等管理功能。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="报告分析" name="analysis">
          <div class="tab-content">
            <p>社会责任报告分析，包括趋势分析、对比分析、影响分析、价值评估等分析功能。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">报告列表</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索报告名称或类型"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="reportName" label="报告名称" width="250" />
        <el-table-column prop="reportType" label="报告类型" width="120" />
        <el-table-column prop="reportYear" label="报告年度" width="100" />
        <el-table-column prop="publishDate" label="发布时间" width="120" />
        <el-table-column prop="pageCount" label="页数" width="80" />
        <el-table-column prop="viewCount" label="阅读量" width="100" />
        <el-table-column prop="downloadCount" label="下载量" width="100" />
        <el-table-column prop="score" label="评分" width="100">
          <template slot-scope="scope">
            <el-tag :type="getScoreType(scope.row.score)">
              {{ scope.row.score }}分
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="编制人" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewReport(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="editReport(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="downloadReport(scope.row)">下载</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ResponsibilityReports',
  data() {
    return {
      activeTab: 'compilation',
      searchText: '',
      overviewData: {
        totalReports: 45,
        publishedReports: 38,
        totalViews: 12856,
        avgScore: 4.7
      },
      tableData: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      console.log('加载社会责任报告数据')
      this.tableData = this.generateMockData()
    },
    generateMockData() {
      const reportNames = ['2023年度社会责任报告', '环境保护专项报告', '扶贫助困工作报告', '员工关爱年度报告', '社区服务实践报告', '可持续发展报告', '公益慈善活动报告', '企业文化建设报告']
      const reportTypes = ['年度报告', '专项报告', '季度报告', '主题报告']
      const authors = ['企划部', '党委办公室', '人事部', '公关部', '社会责任部']
      const statuses = ['已发布', '审核中', '编制中', '草稿']
      
      return Array.from({ length: 8 }, (_, index) => ({
        id: index + 1,
        reportName: reportNames[index],
        reportType: reportTypes[Math.floor(Math.random() * reportTypes.length)],
        reportYear: 2024 - Math.floor(Math.random() * 3),
        publishDate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
        pageCount: Math.floor(Math.random() * 80) + 20,
        viewCount: Math.floor(Math.random() * 5000) + 500,
        downloadCount: Math.floor(Math.random() * 1000) + 100,
        score: (Math.random() * 1.5 + 3.5).toFixed(1),
        author: authors[Math.floor(Math.random() * authors.length)],
        status: statuses[Math.floor(Math.random() * statuses.length)]
      }))
    },
    createReport() {
      console.log('新建责任报告')
    },
    viewReport(row) {
      console.log('查看报告', row)
    },
    editReport(row) {
      console.log('编辑报告', row)
    },
    downloadReport(row) {
      console.log('下载报告', row)
    },
    handleSearch() {
      console.log('搜索', this.searchText)
    },
    getScoreType(score) {
      const scoreNum = parseFloat(score)
      if (scoreNum >= 4.5) return 'success'
      if (scoreNum >= 4.0) return 'warning'
      if (scoreNum >= 3.5) return 'info'
      return 'danger'
    },
    getStatusType(status) {
      const typeMap = {
        '已发布': 'success',
        '审核中': 'warning',
        '编制中': 'info',
        '草稿': 'info'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.responsibility-reports {
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
