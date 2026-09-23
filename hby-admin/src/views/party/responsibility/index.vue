<template>
  <div class="social-responsibility">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-heart"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalProjects }}</div>
              <div class="label">责任项目</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-money"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalInvestment }}</div>
              <div class="label">投入金额(万元)</div>
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
              <div class="number">{{ overviewData.beneficiaries }}</div>
              <div class="label">受益人数</div>
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
              <div class="number">{{ overviewData.satisfactionRate }}%</div>
              <div class="label">满意度</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">社会责任管理</span>
        <div class="card-actions">
          <el-button type="primary" @click="createProject">新建项目</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="责任规划" name="planning">
          <div class="tab-content">
            <p>社会责任规划管理，包括责任战略、目标设定、计划制定、资源配置等规划工作。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="项目管理" name="projects">
          <div class="tab-content">
            <p>社会责任项目管理，包括项目立项、实施管理、进度跟踪、效果评估等项目管理。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="公益活动" name="charity">
          <div class="tab-content">
            <p>公益慈善活动，包括扶贫助困、教育支持、环境保护、社区服务等公益活动管理。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="环境保护" name="environment">
          <div class="tab-content">
            <p>环境保护责任，包括节能减排、绿色发展、环境治理、生态保护等环保责任履行。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="员工关怀" name="employee">
          <div class="tab-content">
            <p>员工关怀责任，包括员工福利、职业发展、安全保障、权益保护等员工关怀工作。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="责任报告" name="reporting">
          <div class="tab-content">
            <p>社会责任报告，包括责任履行报告、社会效益评估、利益相关方沟通等报告管理。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">责任项目列表</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索项目名称或类型"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="projectName" label="项目名称" width="200" />
        <el-table-column prop="projectType" label="项目类型" width="120" />
        <el-table-column prop="startDate" label="开始时间" width="120" />
        <el-table-column prop="investment" label="投入金额(万)" width="120" />
        <el-table-column prop="beneficiaries" label="受益人数" width="100" />
        <el-table-column prop="progress" label="项目进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.progress" :stroke-width="8" />
          </template>
        </el-table-column>
        <el-table-column prop="effect" label="项目效果" width="100">
          <template slot-scope="scope">
            <el-tag :type="getEffectType(scope.row.effect)">
              {{ scope.row.effect }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="manager" label="项目负责人" width="120" />
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
            <el-button size="mini" type="primary" @click="editProject(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="evaluate(scope.row)">评估</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'SocialResponsibility',
  data() {
    return {
      activeTab: 'planning',
      searchText: '',
      overviewData: {
        totalProjects: 156,
        totalInvestment: 2856.7,
        beneficiaries: 12856,
        satisfactionRate: 96.8
      },
      tableData: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      console.log('加载社会责任数据')
      this.tableData = this.generateMockData()
    },
    generateMockData() {
      const projectNames = ['乡村教育支持计划', '环境保护公益项目', '扶贫助困专项行动', '员工关爱计划', '社区服务项目', '绿色发展倡议', '文化传承保护', '科技创新支持']
      const projectTypes = ['教育支持', '环境保护', '扶贫助困', '员工关怀', '社区服务', '文化保护']
      const effects = ['显著', '良好', '一般', '待改进']
      const managers = ['张明', '李华', '王强', '刘芳', '陈杰', '赵敏']
      const statuses = ['进行中', '已完成', '计划中', '暂停']
      
      return Array.from({ length: 8 }, (_, index) => ({
        id: index + 1,
        projectName: projectNames[index],
        projectType: projectTypes[Math.floor(Math.random() * projectTypes.length)],
        startDate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
        investment: (Math.random() * 500 + 50).toFixed(1),
        beneficiaries: Math.floor(Math.random() * 2000) + 100,
        progress: Math.floor(Math.random() * 100),
        effect: effects[Math.floor(Math.random() * effects.length)],
        manager: managers[Math.floor(Math.random() * managers.length)],
        status: statuses[Math.floor(Math.random() * statuses.length)]
      }))
    },
    createProject() {
      console.log('新建责任项目')
    },
    viewDetail(row) {
      console.log('查看项目详情', row)
    },
    editProject(row) {
      console.log('编辑项目', row)
    },
    evaluate(row) {
      console.log('评估项目', row)
    },
    handleSearch() {
      console.log('搜索', this.searchText)
    },
    getEffectType(effect) {
      const typeMap = {
        '显著': 'success',
        '良好': 'warning',
        '一般': 'info',
        '待改进': 'danger'
      }
      return typeMap[effect] || 'info'
    },
    getStatusType(status) {
      const typeMap = {
        '进行中': 'warning',
        '已完成': 'success',
        '计划中': 'info',
        '暂停': 'danger'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.social-responsibility {
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
