<template>
  <div class="party-activities">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-date"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalActivities }}</div>
              <div class="label">活动总数</div>
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
              <div class="number">{{ overviewData.totalParticipants }}</div>
              <div class="label">参与人次</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-time"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalHours }}</div>
              <div class="label">活动时长(小时)</div>
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
        <span class="card-title">党建活动管理</span>
        <div class="card-actions">
          <el-button type="primary" @click="createActivity">新建活动</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="活动策划" name="planning">
          <div class="tab-content">
            <p>党建活动策划管理，包括活动主题设计、方案制定、资源准备、时间安排等策划工作。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="活动组织" name="organization">
          <div class="tab-content">
            <p>党建活动组织实施，包括人员组织、场地安排、物资准备、流程控制等组织工作。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="活动执行" name="execution">
          <div class="tab-content">
            <p>党建活动执行管理，包括活动开展、过程监控、问题处理、效果跟踪等执行管理。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="活动评估" name="evaluation">
          <div class="tab-content">
            <p>党建活动评估分析，包括效果评估、满意度调查、经验总结、改进建议等评估工作。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="活动档案" name="archive">
          <div class="tab-content">
            <p>党建活动档案管理，包括活动记录、资料归档、照片视频、文档管理等档案工作。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="活动宣传" name="publicity">
          <div class="tab-content">
            <p>党建活动宣传推广，包括宣传策划、媒体发布、成果展示、经验分享等宣传工作。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">活动列表</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索活动名称或类型"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%">
        <el-table-column prop="activityName" label="活动名称" width="200" />
        <el-table-column prop="activityType" label="活动类型" width="120" />
        <el-table-column prop="organizer" label="主办单位" width="150" />
        <el-table-column prop="activityDate" label="活动时间" width="120" />
        <el-table-column prop="location" label="活动地点" width="150" />
        <el-table-column prop="participants" label="参与人数" width="100" />
        <el-table-column prop="duration" label="活动时长" width="100" />
        <el-table-column prop="budget" label="活动预算(元)" width="120" />
        <el-table-column prop="rating" label="活动评分" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRatingType(scope.row.rating)">
              {{ scope.row.rating }}分
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
            <el-button size="mini" type="primary" @click="editActivity(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="viewReport(scope.row)">报告</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'PartyActivities',
  data() {
    return {
      activeTab: 'planning',
      searchText: '',
      overviewData: {
        totalActivities: 156,
        totalParticipants: 2856,
        totalHours: 1245,
        avgRating: 4.6
      },
      tableData: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      console.log('加载党建活动数据')
      this.tableData = this.generateMockData()
    },
    generateMockData() {
      const activityNames = ['学习贯彻党的二十大精神专题讲座', '红色教育基地参观学习', '党员志愿服务活动', '廉政教育警示教育', '党史学习教育读书会', '主题党日活动', '民主生活会', '组织生活会']
      const activityTypes = ['理论学习', '实践活动', '志愿服务', '教育培训', '组织生活', '文体活动']
      const organizers = ['党委办公室', '第一党支部', '第二党支部', '机关党支部', '生产党支部']
      const locations = ['会议室A', '培训中心', '红色教育基地', '社区服务中心', '企业文化中心']
      const statuses = ['已完成', '进行中', '计划中', '已取消']
      
      return Array.from({ length: 8 }, (_, index) => ({
        id: index + 1,
        activityName: activityNames[index],
        activityType: activityTypes[Math.floor(Math.random() * activityTypes.length)],
        organizer: organizers[Math.floor(Math.random() * organizers.length)],
        activityDate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
        location: locations[Math.floor(Math.random() * locations.length)],
        participants: Math.floor(Math.random() * 100) + 20,
        duration: `${Math.floor(Math.random() * 6) + 1}小时`,
        budget: Math.floor(Math.random() * 10000) + 1000,
        rating: (Math.random() * 2 + 3).toFixed(1),
        status: statuses[Math.floor(Math.random() * statuses.length)]
      }))
    },
    createActivity() {
      console.log('新建党建活动')
    },
    viewDetail(row) {
      console.log('查看活动详情', row)
    },
    editActivity(row) {
      console.log('编辑活动', row)
    },
    viewReport(row) {
      console.log('查看活动报告', row)
    },
    handleSearch() {
      console.log('搜索', this.searchText)
    },
    getRatingType(rating) {
      const score = parseFloat(rating)
      if (score >= 4.5) return 'success'
      if (score >= 4.0) return 'warning'
      if (score >= 3.5) return 'info'
      return 'danger'
    },
    getStatusType(status) {
      const typeMap = {
        '已完成': 'success',
        '进行中': 'warning',
        '计划中': 'info',
        '已取消': 'danger'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.party-activities {
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
