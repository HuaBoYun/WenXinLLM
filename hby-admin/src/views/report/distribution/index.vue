<template>
  <div class="report-distribution">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-share"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalDistributions }}</div>
              <div class="label">分发总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-user"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.recipientCount }}</div>
              <div class="label">接收人数</div>
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
              <div class="number">{{ overviewData.readRate }}%</div>
              <div class="label">阅读率</div>
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
              <div class="number">{{ overviewData.deliveryRate }}%</div>
              <div class="label">送达率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">报告分发管理</span>
        <div class="card-actions">
          <el-button type="primary" @click="createDistribution">新建分发</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="分发配置" name="config">
          <div class="tab-content">
            <p>报告分发配置管理，包括分发规则、接收人配置、分发渠道、分发时机等分发策略设置。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="接收人管理" name="recipients">
          <div class="tab-content">
            <p>分发接收人管理，包括接收人列表、权限设置、分组管理、联系方式等接收人信息维护。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="分发渠道" name="channels">
          <div class="tab-content">
            <p>分发渠道管理，包括邮件分发、系统通知、短信提醒、文件共享等多种分发方式配置。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="自动分发" name="auto">
          <div class="tab-content">
            <p>自动分发设置，包括定时分发、触发分发、条件分发、批量分发等自动化分发机制。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="分发跟踪" name="tracking">
          <div class="tab-content">
            <p>分发跟踪管理，包括送达状态、阅读状态、反馈收集、统计分析等分发效果跟踪。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="权限控制" name="permission">
          <div class="tab-content">
            <p>分发权限控制，包括访问权限、下载权限、转发权限、打印权限等安全控制措施。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 图表分析 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">分发渠道统计</div>
          <div id="channelChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">阅读率趋势</div>
          <div id="readTrendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">分发记录</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索报告名称或接收人"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="reportName" label="报告名称" width="200" />
        <el-table-column prop="distributionType" label="分发类型" width="120" />
        <el-table-column prop="channel" label="分发渠道" width="120" />
        <el-table-column prop="recipientCount" label="接收人数" width="100" />
        <el-table-column prop="distributionTime" label="分发时间" width="150" />
        <el-table-column prop="deliveryRate" label="送达率" width="100">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.deliveryRate" :stroke-width="8" />
          </template>
        </el-table-column>
        <el-table-column prop="readRate" label="阅读率" width="100">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.readRate" :stroke-width="8" />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="distributor" label="分发人" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewDetail(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="viewRecipients(scope.row)">接收人</el-button>
            <el-button size="mini" type="warning" @click="resend(scope.row)">重发</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ReportDistribution',
  data() {
    return {
      activeTab: 'config',
      searchText: '',
      overviewData: {
        totalDistributions: 856,
        recipientCount: 1234,
        readRate: 87.5,
        deliveryRate: 96.8
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
      console.log('加载分发数据')
      this.tableData = this.generateMockData()
    },
    generateMockData() {
      const reportNames = ['2024年第三季度监管报告', '示例能源集团财务分析报告', '风险评估专项报告', '合规检查月度报告', '投资决策分析报告', '经营绩效评价报告', '内控制度执行报告', '党建工作总结报告']
      const distributionTypes = ['定时分发', '即时分发', '批量分发', '条件分发']
      const channels = ['邮件', '系统通知', '短信', '文件共享', '在线查看']
      const statuses = ['分发成功', '分发中', '部分失败', '分发失败']
      const distributors = ['系统自动', '张明', '李华', '王强', '刘芳']
      
      return Array.from({ length: 8 }, (_, index) => ({
        id: index + 1,
        reportName: reportNames[index],
        distributionType: distributionTypes[Math.floor(Math.random() * distributionTypes.length)],
        channel: channels[Math.floor(Math.random() * channels.length)],
        recipientCount: Math.floor(Math.random() * 100) + 20,
        distributionTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}`,
        deliveryRate: Math.floor(Math.random() * 20) + 80,
        readRate: Math.floor(Math.random() * 30) + 60,
        status: statuses[Math.floor(Math.random() * statuses.length)],
        distributor: distributors[Math.floor(Math.random() * distributors.length)]
      }))
    },
    initCharts() {
      console.log('初始化分发图表')
    },
    createDistribution() {
      console.log('新建分发')
    },
    viewDetail(row) {
      console.log('查看分发详情', row)
    },
    viewRecipients(row) {
      console.log('查看接收人', row)
    },
    resend(row) {
      console.log('重新分发', row)
    },
    handleSearch() {
      console.log('搜索', this.searchText)
    },
    getStatusType(status) {
      const typeMap = {
        '分发成功': 'success',
        '分发中': 'warning',
        '部分失败': 'info',
        '分发失败': 'danger'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.report-distribution {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;
    
    .overview-card {
      height: 120px;
      
      &.gradient-theme {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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
