<template>
  <div class="report-generation">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-document-add"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalReports }}</div>
              <div class="label">生成报告</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper">
              <i class="el-icon-loading"></i>
            </div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.processingCount }}</div>
              <div class="label">生成中</div>
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
              <div class="number">{{ overviewData.successRate }}%</div>
              <div class="label">成功率</div>
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
              <div class="number">{{ overviewData.avgTime }}</div>
              <div class="label">平均耗时(分钟)</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="报告名称">
          <el-input v-model="queryForm.reportName" placeholder="请输入报告名称" clearable />
        </el-form-item>
        <el-form-item label="报告类型">
          <el-select v-model="queryForm.reportType" placeholder="请选择报告类型" clearable>
            <el-option label="监管报告" value="监管报告" />
            <el-option label="财务报告" value="财务报告" />
            <el-option label="风险报告" value="风险报告" />
            <el-option label="合规报告" value="合规报告" />
          </el-select>
        </el-form-item>
        <el-form-item label="生成状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="已完成" value="已完成" />
            <el-option label="生成中" value="生成中" />
            <el-option label="待生成" value="待生成" />
            <el-option label="失败" value="失败" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">新增报告</el-button>
          <el-button type="warning" @click="handleExport" icon="el-icon-download">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">报告生成</span>
        <div class="card-actions">
          <el-button type="primary" @click="handleAdd">生成报告</el-button>
          <el-button type="success" @click="loadData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="自动生成" name="auto">
          <div class="tab-content">
            <p>自动报告生成功能，包括定时生成、触发生成、批量生成等自动化报告生成机制。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="手动生成" name="manual">
          <div class="tab-content">
            <p>手动报告生成功能，包括模板选择、数据配置、参数设置、即时生成等手动生成工具。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="数据源配置" name="datasource">
          <div class="tab-content">
            <p>报告数据源配置，包括数据库连接、API接口、文件导入、数据映射等数据源管理。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="生成规则" name="rules">
          <div class="tab-content">
            <p>报告生成规则配置，包括生成条件、生成频率、数据筛选、格式设置等规则管理。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="任务调度" name="schedule">
          <div class="tab-content">
            <p>报告生成任务调度，包括定时任务、任务队列、任务监控、任务重试等调度管理。</p>
          </div>
        </el-tab-pane>
        <el-tab-pane label="生成历史" name="history">
          <div class="tab-content">
            <p>报告生成历史记录，包括生成日志、执行记录、错误日志、性能统计等历史追踪。</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 图表分析 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">生成趋势分析</div>
          <div id="trendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header">报告类型分布</div>
          <div id="typeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">生成记录</span>
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
        <el-table-column prop="reportName" label="报告名称" width="200" />
        <el-table-column prop="reportType" label="报告类型" width="120" />
        <el-table-column prop="templateName" label="使用模板" width="180" />
        <el-table-column prop="generationType" label="生成方式" width="100" />
        <el-table-column prop="startTime" label="开始时间" width="150" />
        <el-table-column prop="duration" label="耗时" width="100" />
        <el-table-column prop="fileSize" label="文件大小" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="creator" label="创建人" width="100" />
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="downloadReport(scope.row)">下载</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'ReportGeneration',
  data() {
    return {
      activeTab: 'auto',
      searchText: '',
      loading: false,
      total: 0,
      queryForm: {
        reportName: '',
        reportType: '',
        status: '',
        pageNum: 1,
        pageSize: 20
      },
      overviewData: {
        totalReports: 1256,
        processingCount: 23,
        successRate: 96.8,
        avgTime: 3.5
      },
      tableData: []
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const response = await this.$http.post('/api/supervision-report/list', this.queryForm)
        if (response.code === 1) {
          this.tableData = response.data.list || []
          this.total = response.data.total || 0
        } else {
          this.$message.error(response.msg || '获取监管报告列表失败')
        }
      } catch (error) {
        console.error('获取监管报告列表异常:', error)
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
        reportName: '',
        reportType: '',
        status: '',
        pageNum: 1,
        pageSize: 20
      }
      this.loadData()
    },

    // 新增报告
    handleAdd() {
      this.$message.info('新增监管报告功能开发中...')
    },

    // 查看
    handleView(row) {
      this.$message.info(`查看报告：${row.reportName}`)
    },

    // 编辑
    handleEdit(row) {
      this.$message.info(`编辑报告：${row.reportName}`)
    },

    // 删除
    handleDelete(row) {
      this.$confirm(`确认删除报告"${row.reportName}"？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(async () => {
        try {
          const response = await this.$http.post('/api/supervision-report/delete', { id: row.id })
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除报告异常:', error)
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
    generateMockData() {
      const reportNames = ['2024年第三季度监管报告', '示例能源集团财务分析报告', '风险评估专项报告', '合规检查月度报告', '投资决策分析报告', '经营绩效评价报告', '内控制度执行报告', '党建工作总结报告']
      const reportTypes = ['监管报告', '财务报告', '风险报告', '合规报告', '决策报告', '绩效报告']
      const templateNames = ['月度监管报告模板', '季度财务报告模板', '年度风险评估报告', '合规检查报告模板', '专项审计报告模板', '投资决策报告模板']
      const generationTypes = ['自动生成', '手动生成', '定时生成', '触发生成']
      const statuses = ['生成成功', '生成中', '生成失败', '待生成']
      const creators = ['系统自动', '张明', '李华', '王强', '刘芳']
      
      return Array.from({ length: 8 }, (_, index) => ({
        id: index + 1,
        reportName: reportNames[index],
        reportType: reportTypes[Math.floor(Math.random() * reportTypes.length)],
        templateName: templateNames[Math.floor(Math.random() * templateNames.length)],
        generationType: generationTypes[Math.floor(Math.random() * generationTypes.length)],
        startTime: `2024-${String(Math.floor(Math.random() * 12) + 1).padStart(2, '0')}-${String(Math.floor(Math.random() * 28) + 1).padStart(2, '0')} ${String(Math.floor(Math.random() * 24)).padStart(2, '0')}:${String(Math.floor(Math.random() * 60)).padStart(2, '0')}`,
        duration: `${Math.floor(Math.random() * 10) + 1}分${Math.floor(Math.random() * 60)}秒`,
        fileSize: `${(Math.random() * 10 + 1).toFixed(1)}MB`,
        status: statuses[Math.floor(Math.random() * statuses.length)],
        creator: creators[Math.floor(Math.random() * creators.length)]
      }))
    },
    initCharts() {
      console.log('初始化生成图表')
    },
    generateReport() {
      console.log('生成报告')
    },
    viewReport(row) {
      console.log('查看报告', row)
    },
    downloadReport(row) {
      console.log('下载报告', row)
    },
    regenerateReport(row) {
      console.log('重新生成报告', row)
    },
    handleSearch() {
      console.log('搜索', this.searchText)
    },
    getStatusType(status) {
      const typeMap = {
        '生成成功': 'success',
        '生成中': 'warning',
        '生成失败': 'danger',
        '待生成': 'info'
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.report-generation {
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
