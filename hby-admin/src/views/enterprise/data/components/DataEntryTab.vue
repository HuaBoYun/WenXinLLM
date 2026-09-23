<template>
  <div class="data-entry-tab">
    <!-- 查询表单 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px" class="mb-20">
      <el-form-item label="数据类型">
        <el-select v-model="queryForm.dataType" placeholder="请选择数据类型" clearable style="width: 150px;">
          <el-option label="财务数据" value="FINANCIAL"></el-option>
          <el-option label="经营数据" value="BUSINESS"></el-option>
          <el-option label="人力资源" value="HR"></el-option>
          <el-option label="风险数据" value="RISK"></el-option>
          <el-option label="合规数据" value="COMPLIANCE"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="录入状态">
        <el-select v-model="queryForm.entryStatus" placeholder="请选择录入状态" clearable style="width: 150px;">
          <el-option label="草稿" value="DRAFT"></el-option>
          <el-option label="已提交" value="SUBMITTED"></el-option>
          <el-option label="审核中" value="REVIEWING"></el-option>
          <el-option label="已通过" value="APPROVED"></el-option>
          <el-option label="已拒绝" value="REJECTED"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="录入时间">
        <el-date-picker
          v-model="queryForm.entryDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 240px;">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
        <el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button>
        <el-button type="success" @click="handleAdd" icon="el-icon-plus">新增录入</el-button>
        <el-button type="warning" @click="handleBatchImport" icon="el-icon-upload2">批量导入</el-button>
        <el-button type="info" @click="handleTemplate" icon="el-icon-download">下载模板</el-button>
        <el-button type="danger" @click="handleBatchDelete" icon="el-icon-delete" :disabled="multipleSelection.length === 0">批量删除</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="handleSelectionChange"
      @sort-change="handleSortChange"
      stripe
      border
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="enterpriseName" label="企业名称" width="160" show-overflow-tooltip></el-table-column>
      <el-table-column prop="dataType" label="数据类型" width="120" align="center">
        <template slot-scope="scope">
          <el-tag :type="getDataTypeTag(scope.row.dataType)">
            {{ scope.row.dataType || getDataTypeText(scope.row.dataType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="dataCategory" label="数据类别" width="120" align="center"></el-table-column>
      <el-table-column prop="reportPeriod" label="报告期间" width="120" align="center"></el-table-column>
      <el-table-column prop="submitter" label="录入人" width="100" align="center"></el-table-column>
      <el-table-column prop="createTime" label="录入时间" width="160" align="center"></el-table-column>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="getStatusTag(scope.row.status)">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="qualityScore" label="质量评分" width="120" align="center" sortable="custom">
        <template slot-scope="scope">
          <span v-if="scope.row.qualityScore">{{ scope.row.qualityScore }}分</span>
          <span v-else style="color:#909399">-</span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip></el-table-column>
      <el-table-column label="操作" width="380" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)" icon="el-icon-edit" v-if="scope.row.status === '草稿'">编辑</el-button>
          <el-button size="mini" type="success" @click="handleSubmit(scope.row)" icon="el-icon-check" v-if="scope.row.entryStatus === 'DRAFT' || scope.row.status === '草稿'">提交</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)" icon="el-icon-delete" v-if="scope.row.entryStatus === 'DRAFT' || scope.row.status === '草稿'">删除</el-button>
          <el-dropdown @command="handleCommand" style="margin-left: 10px;">
            <el-button size="mini" type="info">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="{action: 'validate', row: scope.row}" icon="el-icon-circle-check">验证数据</el-dropdown-item>
              <el-dropdown-item :command="{action: 'history', row: scope.row}" icon="el-icon-time">查看历史</el-dropdown-item>
              <el-dropdown-item :command="{action: 'export', row: scope.row}" icon="el-icon-download">导出数据</el-dropdown-item>
              <el-dropdown-item :command="{action: 'copy', row: scope.row}" icon="el-icon-document-copy">复制录入</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 数据录入对话框 -->
    <DataEntryFormDialog
      :visible.sync="formDialogVisible"
      :edit-data="currentRow"
      :dialog-type="dialogType"
      :enterprise-id="enterpriseId"
      @refresh="getList"
    />

    <!-- 数据详情对话框 -->
    <DataDetailDialog
      :visible.sync="detailDialogVisible"
      :data-info="currentRow"
    />

    <!-- 数据历史对话框 -->
    <DataHistoryDialog
      :visible.sync="historyDialogVisible"
      :data-id="currentRow.dataEntryId"
    />

    <!-- 批量导入对话框 -->
    <BatchImportDialog
      :visible.sync="importDialogVisible"
      :enterprise-id="enterpriseId"
      @refresh="getList"
    />
  </div>
</template>

<script>
import {
  getDataEntryList,
  deleteDataEntry,
  submitDataSubmission,
  validateDataEntry,
  downloadTemplate,
  exportDataEntry,
  copyDataEntry
} from '@/api/enterprise/data'
import Pagination from '@/components/Pagination'
import DataEntryFormDialog from './DataEntryFormDialog'
import DataDetailDialog from './DataDetailDialog'
import DataHistoryDialog from './DataHistoryDialog'
import BatchImportDialog from './BatchImportDialog'

export default {
  name: 'DataEntryTab',
  components: {
    Pagination,
    DataEntryFormDialog,
    DataDetailDialog,
    DataHistoryDialog,
    BatchImportDialog
  },
  props: {
    enterpriseId: {
      type: String,
      default: ''
    },
    enterpriseName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      multipleSelection: [],
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        dataType: '',
        entryStatus: '',
        entryDateRange: null
      },
      
      // 对话框状态
      formDialogVisible: false,
      detailDialogVisible: false,
      historyDialogVisible: false,
      importDialogVisible: false,
      dialogType: 'add',
      currentRow: {}
    }
  },
  watch: {
    enterpriseId: {
      handler() {
        this.getList()
      },
      immediate: true
    }
  },
  methods: {
    // 获取列表数据
    getList() {
      this.loading = true
      const params = {
        ...this.queryForm
      }
      // 处理日期范围：转为 entryDateStart / entryDateEnd
      if (this.queryForm.entryDateRange && this.queryForm.entryDateRange.length === 2) {
        const fmt = (d) => { const dt = new Date(d); return dt.getFullYear() + '-' + String(dt.getMonth() + 1).padStart(2, '0') + '-' + String(dt.getDate()).padStart(2, '0') }
        params.entryDateStart = fmt(this.queryForm.entryDateRange[0])
        params.entryDateEnd = fmt(this.queryForm.entryDateRange[1])
      }
      delete params.entryDateRange

      getDataEntryList(params).then(response => {
        const data = response.data || {}
        this.tableData = data.tlist || data.records || []
        this.total = data.totalRecord || data.total || this.tableData.length
        this.loading = false
      }).catch(() => {
        this.tableData = []
        this.total = 0
        this.loading = false
      })
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },

    // 重置查询
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        pageNumber: 1,
        pageSize: 10,
        dataType: '',
        entryStatus: '',
        entryDateRange: null
      }
      this.getList()
    },

    // 新增
    handleAdd() {
      this.currentRow = {}
      this.dialogType = 'add'
      this.formDialogVisible = true
    },

    // 查看
    handleView(row) {
      // 确保传递完整的row对象,并获取最新数据
      this.currentRow = { ...row }
      this.detailDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      // 确保传递完整的row对象
      this.currentRow = { ...row }
      console.log('编辑数据:', this.currentRow)
      this.dialogType = 'edit'
      this.formDialogVisible = true
    },

    // 提交（草稿 → 已提交）
    handleSubmit(row) {
      this.$confirm('确认提交该数据录入？提交后将进入审核流程。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await submitDataSubmission({
            id: row.id || row.dataEntryId
          })
          if (response && response.result != 500) {
            this.$message.success('提交成功')
            this.getList()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '提交失败')
          }
        } catch (error) {
          this.$message.error('提交失败：' + (error.message || '未知错误'))
        }
      })
    },

    // 批量导入
    handleBatchImport() {
      this.importDialogVisible = true
    },

    // 下载模板
    async handleTemplate() {
      try {
        const response = await downloadTemplate('GENERAL')
        // 兼容blob响应：response可能是Blob本身或response.data
        const blobData = response instanceof Blob ? response : (response.data || response)
        const blob = blobData instanceof Blob ? blobData : new Blob([blobData], { type: 'text/csv' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '数据录入模板.csv'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        URL.revokeObjectURL(link.href)
        this.$message.success('模板下载成功')
      } catch (error) {
        this.$message.error('模板下载失败：' + (error.message || '请稍后重试'))
      }
    },

    // 下拉菜单命令处理
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'validate':
          this.handleValidate(row)
          break
        case 'history':
          this.currentRow = { ...row }
          this.historyDialogVisible = true
          break
        case 'export':
          this.handleExportData(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 验证数据
    async handleValidate(row) {
      try {
        const loading = this.$loading({
          lock: true,
          text: '正在验证数据...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        const response = await validateDataEntry(row)

        loading.close()

        if (response.result == 200 && response.data) {
          if (response.data.valid) {
            this.$message.success(`数据验证通过，质量评分：${response.data.qualityScore}分`)
            // 更新表格中的质量评分
            row.qualityScore = response.data.qualityScore
          } else {
            const errors = response.data.errors || []
            this.$message.warning(`数据验证未通过：${errors.join('；')}`)
          }
        } else {
          this.$message.error(response.msg || '验证失败')
        }
      } catch (error) {
        this.$message.error('数据验证失败')
      }
    },

    // 导出数据
    async handleExportData(row) {
      try {
        const loading = this.$loading({
          lock: true,
          text: '正在导出数据...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        const response = await exportDataEntry()
        loading.close()

        // 兼容blob响应
        const blobData = response instanceof Blob ? response : (response.data || response)
        const blob = blobData instanceof Blob ? blobData : new Blob([blobData], { type: 'text/csv' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `数据录入_${row.enterpriseName || ''}_${new Date().getTime()}.csv`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        this.$message({
          message: '导出成功',
          type: 'success',
          duration: 2000,
          showClose: true
        })
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      }
    },

    // 复制录入
    async handleCopy(row) {
      try {
        const loading = this.$loading({
          lock: true,
          text: '正在复制...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        
        const response = await copyDataEntry({ id: row.id || row.dataEntryId })
        loading.close()
        
        if (response.result == 200) {
          this.$message({
            message: '复制成功',
            type: 'success',
            duration: 2000,
            showClose: true
          })
          this.getList()
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败：' + (error.message || '未知错误'))
      }
    },

    // 删除
    handleDelete(row) {
      this.$confirm(`确认删除数据录入"${row.enterpriseName || ''}"？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        let loading = null
        try {
          loading = this.$loading({
            lock: true,
            text: '正在删除...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })

          const response = await deleteDataEntry(row.id || row.dataEntryId)
          loading.close()

          if (response && response.result != 500) {
            this.$message.success('删除成功')
            this.getList()
            this.$emit('refresh')
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          if (loading) loading.close()
          this.$message.error('删除失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {})
    },

    // 批量删除
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      
      // 检查是否所有选中的都是草稿状态
      const canDelete = this.multipleSelection.every(row => 
        row.entryStatus === 'DRAFT' || row.status === '草稿'
      )
      
      if (!canDelete) {
        this.$message.warning('只能删除草稿状态的数据')
        return
      }
      
      this.$confirm(`确认删除选中的 ${this.multipleSelection.length} 条数据录入？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const loading = this.$loading({
            lock: true,
            text: '正在批量删除...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          })
          
          let successCount = 0
          let failCount = 0
          
          // 逐个删除
          for (const row of this.multipleSelection) {
            try {
              const response = await deleteDataEntry(row.id || row.dataEntryId)
              if (response.result == 200) {
                successCount++
              } else {
                failCount++
              }
            } catch (error) {
              failCount++
            }
          }
          
          loading.close()
          
          if (failCount === 0) {
            this.$message({
              message: `成功删除 ${successCount} 条数据`,
              type: 'success',
              duration: 2000,
              showClose: true
            })
          } else {
            this.$message({
              message: `删除完成：成功 ${successCount} 条，失败 ${failCount} 条`,
              type: 'warning',
              duration: 3000,
              showClose: true
            })
          }
          
          this.getList()
          this.$emit('refresh')
        } catch (error) {
          this.$message.error('批量删除失败：' + (error.message || '未知错误'))
        }
      }).catch(() => {
        // 用户取消删除
      })
    },

    // 多选变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 排序变化
    handleSortChange({ column, prop, order }) {
      this.queryForm.orderBy = prop
      this.queryForm.orderDirection = order === 'ascending' ? 'ASC' : 'DESC'
      this.getList()
    },

    // 获取数据类型标签
    getDataTypeTag(type) {
      const tagMap = {
        'FINANCIAL': 'primary',
        'BUSINESS': 'success',
        'HR': 'warning',
        'RISK': 'danger',
        'COMPLIANCE': 'info'
      }
      return tagMap[type] || 'info'
    },

    // 获取数据类型文本
    getDataTypeText(type) {
      const textMap = {
        'FINANCIAL': '财务数据',
        'BUSINESS': '经营数据',
        'HR': '人力资源',
        'RISK': '风险数据',
        'COMPLIANCE': '合规数据'
      }
      return textMap[type] || type
    },

    // 获取状态标签
    getStatusTag(status) {
      const tagMap = {
        'DRAFT': 'info',
        'SUBMITTED': 'warning',
        'REVIEWING': 'primary',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return tagMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'SUBMITTED': '已提交',
        'REVIEWING': '审核中',
        'APPROVED': '已通过',
        'REJECTED': '已拒绝'
      }
      return textMap[status] || status
    },

    // 获取质量评分颜色
    getQualityColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 70) return '#E6A23C'
      return '#F56C6C'
    }
  }
}
</script>

<style scoped>
.data-entry-tab {
  padding: 20px 0;
}

.mb-20 {
  margin-bottom: 20px;
}
</style>
