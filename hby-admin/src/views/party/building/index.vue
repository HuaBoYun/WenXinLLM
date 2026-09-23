<template>
  <div class="party-building">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-user"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.partyMembers }}</div>
              <div class="label">党员人数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.partyBranches }}</div>
              <div class="label">党支部数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-calendar"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.activities }}</div>
              <div class="label">本月活动</div>
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
              <div class="number">{{ overviewData.participationRate }}%</div>
              <div class="label">参与率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">党建工作管理</span>
        <div class="card-actions">
          <el-button type="primary" @click="createActivity">新建活动</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="组织建设" name="organization">
          <div class="tab-content">
            <p>党组织建设管理，包括党支部设立、组织架构、人员配置、职责分工等组织建设工作。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="党员管理" name="members">
          <div class="tab-content">
            <p>党员管理功能，包括党员档案、发展党员、党员教育、党费收缴等党员管理工作。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="学习教育" name="education">
          <div class="tab-content">
            <p>党员学习教育，包括理论学习、主题教育、培训计划、学习效果等教育活动管理。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="制度建设" name="system">
          <div class="tab-content">
            <p>党建制度建设，包括制度制定、制度执行、制度监督、制度完善等制度建设工作。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="作风建设" name="conduct">
          <div class="tab-content">
            <p>党风廉政建设，包括作风监督、廉政教育、纪律检查、问题整改等作风建设工作。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="工作评估" name="assessment">
          <div class="tab-content">
            <p>党建工作评估，包括工作考核、效果评价、经验总结、改进建议等评估分析。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 查询表单 -->
    <el-card class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="活动名称">
          <el-input v-model="queryForm.activityName" placeholder="请输入活动名称" clearable />
        </el-form-item>
        <el-form-item label="活动类型">
          <el-select v-model="queryForm.activityType" placeholder="请选择活动类型" clearable>
            <el-option label="主题党日" value="主题党日" />
            <el-option label="理论学习" value="理论学习" />
            <el-option label="志愿服务" value="志愿服务" />
            <el-option label="党员大会" value="党员大会" />
            <el-option label="民主生活会" value="民主生活会" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="已完成" value="已完成" />
            <el-option label="进行中" value="进行中" />
            <el-option label="计划中" value="计划中" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">新增活动</el-button>
          <el-button type="warning" @click="handleExport" icon="el-icon-download">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">党建活动记录</span>
        <div class="search-wrapper">
          <el-input
            v-model="searchText"
            placeholder="搜索活动名称或支部"
            prefix-icon="el-icon-search"
            style="width: 300px;"
            @input="handleSearch"
          />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%" v-loading="loading">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="activityName" label="活动名称" width="200" />
        <el-table-column prop="activityType" label="活动类型" width="120" />
        <el-table-column prop="branch" label="组织支部" width="150" />
        <el-table-column prop="activityDate" label="活动时间" width="120" />
        <el-table-column prop="participants" label="参与人数" width="100" />
        <el-table-column prop="duration" label="活动时长" width="100" />
        <el-table-column prop="effect" label="活动效果" width="100">
          <template slot-scope="scope">
            <el-tag :type="getEffectType(scope.row.effect)">
              {{ scope.row.effect }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="organizer" label="组织者" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-button size="mini" type="warning" @click="viewSummary(scope.row)">总结</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryForm.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        style="margin-top: 20px; text-align: right;">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'PartyBuilding',
  data() {
    return {
      activeTab: 'organization',
      searchText: '',
      loading: false,
      total: 0,
      queryForm: {
        activityName: '',
        activityType: '',
        status: '',
        pageNum: 1,
        pageSize: 20
      },
      overviewData: {
        partyMembers: 1256,
        partyBranches: 45,
        activities: 23,
        participationRate: 94.5
      },
      tableData: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const response = await this.$http.post('/api/party-building/activities/list', this.queryForm)
        if (response.code === 1) {
          this.tableData = response.data.list || []
          this.total = response.data.total || 0
        } else {
          this.$message.error(response.msg || '获取党建活动列表失败')
        }
      } catch (error) {
        console.error('获取党建活动列表异常:', error)
        this.$message.error('获取数据失败')
        // 降级到模拟数据
        this.tableData = this.generateMockData()
        this.total = this.tableData.length
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        activityName: '',
        activityType: '',
        status: '',
        pageNum: 1,
        pageSize: 20
      }
      this.loadData()
    },

    // 新增
    handleAdd() {
      this.$message.info('新增党建活动功能开发中...')
    },

    // 查看
    handleView(row) {
      this.$message.info(`查看活动详情：${row.activityName}`)
    },

    // 编辑
    handleEdit(row) {
      this.$message.info(`编辑活动：${row.activityName}`)
    },

    // 删除
    handleDelete(row) {
      this.$confirm(`确认删除活动"${row.activityName}"？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(async () => {
        try {
          const response = await this.$http.post('/api/party-building/activities/delete', { id: row.id })
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除活动异常:', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },

    // 导出
    handleExport() {
      const loading = this.$loading({
        lock: true,
        text: '正在导出数据...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
      })

      setTimeout(() => {
        loading.close()
        this.$message.success('导出成功')
      }, 2000)
    },

    // 分页大小改变
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNum = 1
      this.loadData()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.queryForm.pageNum = val
      this.loadData()
    },
    generateMockData() {
      const activityNames = ['学习贯彻党的二十大精神', '主题党日活动', '党员志愿服务', '廉政教育专题学习', '党史学习教育', '民主生活会', '组织生活会', '党员发展大会']
      const activityTypes = ['理论学习', '主题教育', '志愿服务', '廉政教育', '组织生活', '民主评议']
      const branches = ['第一党支部', '第二党支部', '第三党支部', '机关党支部', '生产党支部']
      const effects = ['优秀', '良好', '一般', '待改进']
      const organizers = ['党委办公室', '组织部', '宣传部', '纪检部']
      const statuses = ['已完成', '进行中', '计划中', '已取消']
      
      return Array.from({ length: 8 }, (_, index) => ({
        id: index + 1,
        activityName: activityNames[index],
        activityType: activityTypes[Math.floor(Math.random() * activityTypes.length)],
        branch: branches[Math.floor(Math.random() * branches.length)],
        activityDate: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')}`,
        participants: Math.floor(Math.random() * 50) + 20,
        duration: `${Math.floor(Math.random() * 4) + 1}小时`,
        effect: effects[Math.floor(Math.random() * effects.length)],
        organizer: organizers[Math.floor(Math.random() * organizers.length)],
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
    viewSummary(row) {
      console.log('查看活动总结', row)
    },
    handleSearch() {
      console.log('搜索', this.searchText)
    },
    getEffectType(effect) {
      const typeMap = {
        '优秀': 'success',
        '良好': 'warning',
        '一般': 'info',
        '待改进': 'danger'
      }
      return typeMap[effect] || 'info'
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
.party-building {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;
    
    .overview-card {
      height: 120px;
      
      &.gradient-theme {
        background: linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%);
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
