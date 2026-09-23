<template>
  <div class="project-cost-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item>
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
              />
            </el-form-item>
            <el-form-item>
              <el-select
                v-model="queryForm.projectType"
                clearable
                placeholder="项目类型"
                style="width: 180px"
              >
                <el-option label="研发项目" value="1" />
                <el-option label="建设项目" value="2" />
                <el-option label="投资项目" value="3" />
                <el-option label="其他项目" value="4" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select
                v-model="queryForm.status"
                clearable
                placeholder="项目状态"
                style="width: 180px"
              >
                <el-option label="立项中" value="1" />
                <el-option label="进行中" value="2" />
                <el-option label="已完成" value="3" />
                <el-option label="已暂停" value="4" />
                <el-option label="已取消" value="5" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                v-model="queryForm.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
        
        <vab-query-form-right-panel>
          <el-button
            icon="el-icon-plus"
            type="primary"
            @click="handleAdd"
          >
            新增项目
          </el-button>
          <el-button
            icon="el-icon-download"
            @click="handleExport"
          >
            导出
          </el-button>
          <el-button
            icon="el-icon-refresh-right"
            @click="fetchData"
          >
            刷新
          </el-button>
        </vab-query-form-right-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never">
      <el-table
        ref="tableSort"
        v-loading="listLoading"
        :data="list"
        :element-loading-text="elementLoadingText"
        @selection-change="setSelectRows"
        @sort-change="tableSortChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          label="序号"
          type="index"
          width="80"
          :index="getTableIndex"
        />
        <el-table-column
          prop="projectCode"
          label="项目编码"
          min-width="120"
          sortable="custom"
          show-overflow-tooltip
        />
        <el-table-column
          prop="projectName"
          label="项目名称"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="projectType"
          label="项目类型"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag :type="getProjectTypeTag(scope.row.projectType)">
              {{ getProjectTypeName(scope.row.projectType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="budgetAmount"
          label="预算金额"
          width="120"
          align="right"
        >
          <template slot-scope="scope">
            {{ formatAmount(scope.row.budgetAmount) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="actualAmount"
          label="实际成本"
          width="120"
          align="right"
        >
          <template slot-scope="scope">
            {{ formatAmount(scope.row.actualAmount) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="variance"
          label="预算差异"
          width="120"
          align="right"
        >
          <template slot-scope="scope">
            <span :class="getVarianceClass(scope.row.variance)">
              {{ formatAmount(scope.row.variance) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="progress"
          label="项目进度"
          width="100"
        >
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.progress"
              :color="getProgressColor(scope.row.progress)"
              :stroke-width="6"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          width="80"
        >
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="startDate"
          label="开始日期"
          width="100"
        />
        <el-table-column
          prop="endDate"
          label="结束日期"
          width="100"
        />
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="150"
          sortable="custom"
        />
        <el-table-column
          label="操作"
          width="200"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button
              type="text"
              @click="handleView(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="text"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              @click="handleAnalysis(scope.row)"
            >
              分析
            </el-button>
            <el-button
              type="text"
              style="color: #f56c6c"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :page-size="queryForm.pageSize"
        :layout="layout"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <project-cost-edit
      ref="edit"
      @fetch-data="fetchData"
    />

    <!-- 项目详情对话框 -->
    <project-cost-detail
      ref="detail"
    />

    <!-- 项目分析对话框 -->
    <project-cost-analysis
      ref="analysis"
    />
  </div>
</template>

<script>
import { getProjectCostPage, deleteProjectCost, batchDeleteProjectCost } from '@/api/financialSharing/specialCost'
import ProjectCostEdit from './components/ProjectCostEdit'
import ProjectCostDetail from './components/ProjectCostDetail'
import ProjectCostAnalysis from './components/ProjectCostAnalysis'

export default {
  name: 'ProjectCostIndex',
  components: {
    ProjectCostEdit,
    ProjectCostDetail,
    ProjectCostAnalysis
  },
  data() {
    return {
      list: [],
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      selectRows: '',
      elementLoadingText: '正在加载...',
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
        projectName: '',
        projectType: '',
        status: '',
        dateRange: []
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    setSelectRows(val) {
      this.selectRows = val
    },
    handleAdd() {
      this.$refs.edit.showEdit()
    },
    handleEdit(row) {
      this.$refs.edit.showEdit(row)
    },
    handleView(row) {
      this.$refs.detail.showDetail(row)
    },
    handleAnalysis(row) {
      this.$refs.analysis.showAnalysis(row)
    },
    async handleDelete(row) {
      if (
        await this.$baseConfirm('你确定要删除当前项目吗', null, {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
      ) {
        const { code, msg } = await deleteProjectCost(row.id)
        if (code === 200) {
          this.$baseMessage(msg, 'success')
          await this.fetchData()
        } else {
          this.$baseMessage(msg, 'error')
        }
      }
    },
    handleExport() {
      try {
        const data = this.list || []
        if (data.length === 0) {
          this.$baseMessage('暂无数据可导出', 'warning')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '项目成本数据.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$baseMessage('导出成功', 'success')
      } catch (error) {
        this.$baseMessage('导出失败', 'error')
      }
    },
    async fetchData() {
      this.listLoading = true
      const { code, data } = await getProjectCostPage(this.queryForm)
      if (code === 200) {
        this.list = data.records
        this.total = data.total
      }
      this.listLoading = false
    },
    resetSearch() {
      this.$refs.form.resetFields()
      this.queryForm = {
        pageNumber: 1,
        pageSize: 20,
        projectName: '',
        projectType: '',
        status: '',
        dateRange: []
      }
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    tableSortChange({ column, prop, order }) {
      this.queryForm.orderBy = prop
      this.queryForm.orderType = order === 'ascending' ? 'asc' : 'desc'
      this.fetchData()
    },
    getTableIndex(index) {
      return (this.queryForm.pageNumber - 1) * this.queryForm.pageSize + index + 1
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getProjectTypeName(type) {
      const typeMap = {
        '1': '研发项目',
        '2': '建设项目',
        '3': '投资项目',
        '4': '其他项目'
      }
      return typeMap[type] || '未知'
    },
    getProjectTypeTag(type) {
      const tagMap = {
        '1': 'primary',
        '2': 'success',
        '3': 'warning',
        '4': 'info'
      }
      return tagMap[type] || 'info'
    },
    getStatusName(status) {
      const statusMap = {
        '1': '立项中',
        '2': '进行中',
        '3': '已完成',
        '4': '已暂停',
        '5': '已取消'
      }
      return statusMap[status] || '未知'
    },
    getStatusTag(status) {
      const tagMap = {
        '1': 'info',
        '2': 'primary',
        '3': 'success',
        '4': 'warning',
        '5': 'danger'
      }
      return tagMap[status] || 'info'
    },
    getVarianceClass(variance) {
      if (variance > 0) return 'text-danger'
      if (variance < 0) return 'text-success'
      return ''
    },
    getProgressColor(progress) {
      if (progress < 30) return '#f56c6c'
      if (progress < 70) return '#e6a23c'
      return '#67c23a'
    }
  }
}
</script>

<style lang="scss" scoped>
.project-cost-container {
  padding: 0;
}

.text-danger {
  color: #f56c6c;
}

.text-success {
  color: #67c23a;
}
</style>
