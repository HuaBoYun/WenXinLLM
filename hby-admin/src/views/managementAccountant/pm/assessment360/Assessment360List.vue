<template>
  <div class="assessment-360-list">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-title">
        <h2>360度评估</h2>
        <p>多维度全方位绩效评估管理</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
          新建评估
        </el-button>
        <el-button icon="el-icon-download" @click="handleExport">
          导出评估
        </el-button>
      </div>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="评估名称">
          <el-input
            v-model="searchForm.assessmentName"
            placeholder="请输入评估名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="被评估人">
          <el-input
            v-model="searchForm.assessedUserName"
            placeholder="请输入被评估人姓名"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="评估类型">
          <el-select v-model="searchForm.assessmentType" placeholder="请选择" clearable style="width: 120px">
            <el-option label="年度评估" value="ANNUAL" />
            <el-option label="季度评估" value="QUARTERLY" />
            <el-option label="月度评估" value="MONTHLY" />
            <el-option label="项目评估" value="PROJECT" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估状态">
          <el-select v-model="searchForm.assessmentStatus" placeholder="请选择" clearable style="width: 120px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="进行中" value="ONGOING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估年度">
          <el-date-picker
            v-model="searchForm.assessmentYear"
            type="year"
            placeholder="选择年度"
            style="width: 120px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column prop="assessmentCode" label="评估编码" width="150" />
        <el-table-column prop="assessmentName" label="评估名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="assessedUserName" label="被评估人" width="120" />
        <el-table-column prop="assessmentType" label="评估类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getAssessmentTypeTagType(scope.row.assessmentType)">
              {{ formatAssessmentType(scope.row.assessmentType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assessmentStatus" label="评估状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getAssessmentStatusTagType(scope.row.assessmentStatus)">
              {{ formatAssessmentStatus(scope.row.assessmentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="completionRate" label="完成进度" width="120">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.completionRate || 0" :stroke-width="6" />
          </template>
        </el-table-column>
        <el-table-column prop="weightedAverageScore" label="综合得分" width="100">
          <template slot-scope="scope">
            <span :style="{ color: getScoreColor(scope.row.weightedAverageScore) }">
              {{ scope.row.weightedAverageScore || '--' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="assessmentGrade" label="评估等级" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.assessmentGrade" :type="getGradeTagType(scope.row.assessmentGrade)">
              {{ formatAssessmentGrade(scope.row.assessmentGrade) }}
            </el-tag>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="120">
          <template slot-scope="scope">
            {{ formatDate(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="120">
          <template slot-scope="scope">
            {{ formatDate(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createdByName" label="创建人" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button 
              v-if="scope.row.assessmentStatus === 'DRAFT'" 
              size="mini" 
              type="primary" 
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              v-if="scope.row.assessmentStatus === 'DRAFT'" 
              size="mini" 
              type="success" 
              @click="handleStart(scope.row)"
            >
              启动
            </el-button>
            <el-button 
              v-if="scope.row.assessmentStatus === 'ONGOING'" 
              size="mini" 
              type="warning" 
              @click="handleComplete(scope.row)"
            >
              完成
            </el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'copy', row: scope.row}">复制</el-dropdown-item>
                <el-dropdown-item :command="{action: 'report', row: scope.row}">报告</el-dropdown-item>
                <el-dropdown-item 
                  v-if="scope.row.assessmentStatus !== 'COMPLETED'" 
                  :command="{action: 'delete', row: scope.row}"
                  divided
                >
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>
  </div>
</template>

<script>
import {
  query360AssessmentPage,
  delete360Assessment,
  exportAssessments,
  startAssessment,
  completeAssessment,
  copyAssessment,
  generateAssessmentReport
} from '@/api/managementAccountant/pm/assessment360'

export default {
  name: 'Assessment360List',
  data() {
    return {
      loading: false,
      tableData: [],
      searchForm: {
        assessmentName: '',
        assessedUserName: '',
        assessmentType: '',
        assessmentStatus: '',
        assessmentYear: null
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          assessmentName: this.searchForm.assessmentName,
          assessedUserName: this.searchForm.assessedUserName,
          assessmentType: this.searchForm.assessmentType,
          assessmentStatus: this.searchForm.assessmentStatus,
          assessmentYear: this.searchForm.assessmentYear ? this.searchForm.assessmentYear.getFullYear() : null
        }
        
        const response = await query360AssessmentPage(params)
        if (response.success) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        } else {
          this.$message.error(response.message || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.searchForm = {
        assessmentName: '',
        assessedUserName: '',
        assessmentType: '',
        assessmentStatus: '',
        assessmentYear: null
      }
      this.pagination.current = 1
      this.loadData()
    },

    // 新建
    handleCreate() {
      this.$router.push('/pm/360-assessment/create')
    },

    // 查看
    handleView(row) {
      this.$router.push(`/pm/360-assessment/detail/${row.assessmentId}`)
    },

    // 编辑
    handleEdit(row) {
      this.$router.push(`/pm/360-assessment/edit/${row.assessmentId}`)
    },

    // 启动评估
    async handleStart(row) {
      try {
        await this.$confirm('确认启动该评估？启动后将通知相关评估人员。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await startAssessment(row.assessmentId, {
          startReason: '评估启动',
          notifyParticipants: true
        })
        
        if (response.success) {
          this.$message.success('启动成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '启动失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('启动失败：' + error.message)
        }
      }
    },

    // 完成评估
    async handleComplete(row) {
      try {
        await this.$confirm('确认完成该评估？完成后将计算最终结果。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await completeAssessment(row.assessmentId, {
          completeReason: '评估完成',
          generateReport: true
        })
        
        if (response.success) {
          this.$message.success('完成成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '完成失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('完成失败：' + error.message)
        }
      }
    },

    // 下拉菜单命令处理
    async handleCommand(command) {
      const { action, row } = command
      
      switch (action) {
        case 'copy':
          await this.handleCopy(row)
          break
        case 'report':
          await this.handleReport(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 复制评估
    async handleCopy(row) {
      try {
        const response = await copyAssessment(row.assessmentId, {
          copyType: 'FULL',
          newAssessmentName: `${row.assessmentName}_副本`
        })
        
        if (response.success) {
          this.$message.success('复制成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 生成报告
    async handleReport(row) {
      try {
        const response = await generateAssessmentReport(row.assessmentId, {
          reportType: 'COMPREHENSIVE',
          includeCharts: true
        })
        
        if (response.success) {
          this.$message.success('报告生成成功')
          // 这里可以打开报告页面或下载报告
        } else {
          this.$message.error(response.message || '报告生成失败')
        }
      } catch (error) {
        this.$message.error('报告生成失败：' + error.message)
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该评估？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await delete360Assessment(row.assessmentId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 导出
    async handleExport() {
      try {
        const response = await exportAssessments({
          exportType: 'ALL',
          includeDetails: true
        })
        
        if (response.success) {
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 格式化方法
    formatAssessmentType(type) {
      const typeMap = {
        'ANNUAL': '年度',
        'QUARTERLY': '季度',
        'MONTHLY': '月度',
        'PROJECT': '项目'
      }
      return typeMap[type] || type
    },

    formatAssessmentStatus(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'ONGOING': '进行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },

    formatAssessmentGrade(grade) {
      const gradeMap = {
        'EXCELLENT': '优秀',
        'GOOD': '良好',
        'FAIR': '一般',
        'POOR': '较差'
      }
      return gradeMap[grade] || grade
    },

    formatDate(date) {
      if (!date) return ''
      return this.$moment(date).format('YYYY-MM-DD')
    },

    // 标签类型
    getAssessmentTypeTagType(type) {
      const typeMap = {
        'ANNUAL': 'danger',
        'QUARTERLY': 'primary',
        'MONTHLY': 'success',
        'PROJECT': 'warning'
      }
      return typeMap[type] || ''
    },

    getAssessmentStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'ONGOING': 'primary',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || ''
    },

    getGradeTagType(grade) {
      const gradeMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'FAIR': 'warning',
        'POOR': 'danger'
      }
      return gradeMap[grade] || ''
    },

    // 分数颜色
    getScoreColor(score) {
      if (!score) return '#c0c4cc'
      if (score >= 90) return '#67c23a'
      if (score >= 80) return '#409eff'
      if (score >= 70) return '#e6a23c'
      return '#f56c6c'
    }
  }
}
</script>

<style scoped>
.assessment-360-list {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.header-title h2 {
  margin: 0 0 5px 0;
  color: #303133;
}

.header-title p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-section {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.search-form {
  margin: 0;
}

.table-section {
  background: white;
  border-radius: 4px;
  overflow: hidden;
}

.pagination-section {
  padding: 20px;
  text-align: right;
  border-top: 1px solid #e4e7ed;
}
</style>
