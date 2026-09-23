<template>
  <div class="gjyccl-container">
    <div class="page-header">
      <h2>国际异常处理</h2>
      <p>处理和跟踪国际业务中的异常情况</p>
    </div>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="异常类型">
          <el-select v-model="searchForm.exceptionType" placeholder="请选择异常类型" clearable>
            <el-option label="转账失败" value="TRANSFER_FAILED" />
            <el-option label="超时" value="TIMEOUT" />
            <el-option label="数据错误" value="DATA_ERROR" />
            <el-option label="系统错误" value="SYSTEM_ERROR" />
          </el-select>
        </el-form-item>
        <el-form-item label="严重程度">
          <el-select v-model="searchForm.exceptionLevel" placeholder="请选择严重程度" clearable>
            <el-option label="普通" value="NORMAL" />
            <el-option label="重要" value="IMPORTANT" />
            <el-option label="紧急" value="URGENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="searchForm.exceptionStatus" placeholder="请选择处理状态" clearable>
            <el-option label="待处理" value="PENDING" />
            <el-option label="处理中" value="HANDLING" />
            <el-option label="已解决" value="RESOLVED" />
            <el-option label="已关闭" value="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="发生日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 异常统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card" shadow="never">
          <div class="stat-content">
            <div class="stat-value">{{ statistics.pending }}</div>
            <div class="stat-label">待处理异常</div>
          </div>
          <i class="el-icon-warning stat-icon warning"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="never">
          <div class="stat-content">
            <div class="stat-value">{{ statistics.processing }}</div>
            <div class="stat-label">处理中异常</div>
          </div>
          <i class="el-icon-loading stat-icon info"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="never">
          <div class="stat-content">
            <div class="stat-value">{{ statistics.resolved }}</div>
            <div class="stat-label">已解决异常</div>
          </div>
          <i class="el-icon-success stat-icon success"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="never">
          <div class="stat-content">
            <div class="stat-value">{{ statistics.resolveRate }}</div>
            <div class="stat-label">解决率</div>
          </div>
          <i class="el-icon-data-analysis stat-icon primary"></i>
        </el-card>
      </el-col>
    </el-row>

    <!-- 异常处理表格 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span>异常处理列表</span>
        <div>
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增异常</el-button>
          <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="exceptionNo" label="异常编号" width="120" />
        <el-table-column prop="exceptionTitle" label="异常标题" min-width="150" />
        <el-table-column prop="exceptionType" label="异常类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getExceptionTypeTag(scope.row.exceptionType)">
              {{ getExceptionTypeName(scope.row.exceptionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="exceptionLevel" label="严重程度" width="100">
          <template slot-scope="scope">
            <el-tag :type="getSeverityTag(scope.row.exceptionLevel)">
              {{ getSeverityName(scope.row.exceptionLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="exceptionStatus" label="处理状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.exceptionStatus)">
              {{ getStatusName(scope.row.exceptionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="handleBy" label="处理人" width="100" />
        <el-table-column prop="exceptionTime" label="发生时间" width="160">
          <template slot-scope="scope">
            <span>{{ formatDateTime(scope.row.exceptionTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="handleTime" label="解决时间" width="160">
          <template slot-scope="scope">
            <span>{{ formatDateTime(scope.row.handleTime) || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleProcess(scope.row)">处理</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="异常标题" prop="exceptionTitle">
              <el-input v-model="form.exceptionTitle" placeholder="请输入异常标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="异常类型" prop="exceptionType">
              <el-select v-model="form.exceptionType" placeholder="请选择异常类型">
                <el-option label="转账失败" value="TRANSFER_FAILED" />
                <el-option label="超时" value="TIMEOUT" />
                <el-option label="数据错误" value="DATA_ERROR" />
                <el-option label="系统错误" value="SYSTEM_ERROR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="严重程度" prop="exceptionLevel">
              <el-select v-model="form.exceptionLevel" placeholder="请选择严重程度">
                <el-option label="普通" value="NORMAL" />
                <el-option label="重要" value="IMPORTANT" />
                <el-option label="紧急" value="URGENT" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处理人" prop="handleBy">
              <el-input v-model="form.handleBy" placeholder="请输入处理人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="发生时间" prop="exceptionTime">
              <el-date-picker
                v-model="form.exceptionTime"
                type="datetime"
                placeholder="选择发生时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计解决时间">
              <el-date-picker
                v-model="form.nextRetryTime"
                type="datetime"
                placeholder="选择预计解决时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="异常描述" prop="exceptionDesc">
          <el-input
            v-model="form.exceptionDesc"
            type="textarea"
            :rows="4"
            placeholder="请详细描述异常情况"
          />
        </el-form-item>
        <el-form-item label="处理方案">
          <el-input
            v-model="form.handleResult"
            type="textarea"
            :rows="3"
            placeholder="请输入处理方案"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 处理进度对话框 -->
    <el-dialog
      title="异常处理进度"
      :visible.sync="processDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-steps :active="currentStep" direction="vertical">
        <el-step title="异常发现" description="系统自动检测或人工发现异常" />
        <el-step title="异常分析" description="分析异常原因和影响范围" />
        <el-step title="制定方案" description="制定具体的处理方案" />
        <el-step title="执行处理" description="按照方案执行处理措施" />
        <el-step title="验证结果" description="验证处理结果是否有效" />
        <el-step title="异常关闭" description="确认异常已完全解决" />
      </el-steps>
      <div slot="footer" class="dialog-footer">
        <el-button @click="processDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleNextStep">下一步</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getExceptionPage, getExceptionById, createException, updateException, deleteException, handleException, retryException, closeException, getExceptionStatistics, batchDeleteException, exportException } from '@/api/globalTreasurer/zjjz'

export default {
  name: 'GjycclManage',
  data() {
    return {
      loading: false,
      searchForm: {
        exceptionType: '',
        exceptionLevel: '',
        exceptionStatus: '',
        dateRange: []
      },
      tableData: [],
      multipleSelection: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      statistics: {
        pending: 0,
        processing: 0,
        resolved: 0,
        resolveRate: '0%'
      },
      dialogVisible: false,
      processDialogVisible: false,
      dialogTitle: '新增异常',
      currentStep: 0,
      currentRow: null,
      form: {
        exceptionTitle: '',
        exceptionType: '',
        exceptionLevel: '',
        handleBy: '',
        exceptionTime: '',
        nextRetryTime: '',
        exceptionDesc: '',
        handleResult: ''
      },
      rules: {
        exceptionTitle: [
          { required: true, message: '请输入异常标题', trigger: 'blur' }
        ],
        exceptionType: [
          { required: true, message: '请选择异常类型', trigger: 'change' }
        ],
        exceptionLevel: [
          { required: true, message: '请选择严重程度', trigger: 'change' }
        ],
        handleBy: [
          { required: true, message: '请输入处理人', trigger: 'blur' }
        ],
        exceptionTime: [
          { required: true, message: '请选择发生时间', trigger: 'change' }
        ],
        exceptionDesc: [
          { required: true, message: '请输入异常描述', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
    this.loadStatistics()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          pageNo: this.pagination.current,
          pageSize: this.pagination.size,
          exceptionType: this.searchForm.exceptionType || undefined,
          exceptionLevel: this.searchForm.exceptionLevel || undefined,
          exceptionStatus: this.searchForm.exceptionStatus || undefined,
          startDate: this.searchForm.dateRange && this.searchForm.dateRange[0] ? this.searchForm.dateRange[0] : undefined,
          endDate: this.searchForm.dateRange && this.searchForm.dateRange[1] ? this.searchForm.dateRange[1] : undefined
        }
        const res = await getExceptionPage(params)
        if (res.code === 1) {
          this.tableData = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载异常数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    async loadStatistics() {
      try {
        const res = await getExceptionStatistics()
        if (res.code === 1 && res.data) {
          this.statistics = {
            pending: res.data.pending || 0,
            processing: res.data.processing || 0,
            resolved: res.data.resolved || 0,
            resolveRate: res.data.resolveRate || '0%'
          }
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        exceptionType: '',
        exceptionLevel: '',
        exceptionStatus: '',
        dateRange: []
      }
      this.handleSearch()
    },
    handleExport() {
      const params = {
        exceptionType: this.searchForm.exceptionType || undefined,
        exceptionLevel: this.searchForm.exceptionLevel || undefined,
        exceptionStatus: this.searchForm.exceptionStatus || undefined,
        startDate: this.searchForm.dateRange && this.searchForm.dateRange[0] ? this.searchForm.dateRange[0] : undefined,
        endDate: this.searchForm.dateRange && this.searchForm.dateRange[1] ? this.searchForm.dateRange[1] : undefined
      }
      exportException(params).then(res => {
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '异常处理_' + new Date().getTime() + '.xlsx'
        a.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      }).catch(() => {
        this.$message.error('导出失败')
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的记录')
        return
      }
      this.$confirm(`确认批量删除选中的 ${this.multipleSelection.length} 条记录？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.multipleSelection.map(item => item.exceptionId)
          const res = await batchDeleteException(ids)
          if (res.code === 1) {
            this.$message.success('批量删除成功')
            this.loadData()
            this.loadStatistics()
          } else {
            this.$message.error(res.msg || '批量删除失败')
          }
        } catch (error) {
          this.$message.error('批量删除失败')
        }
      })
    },
    handleAdd() {
      this.dialogTitle = '新增异常'
      this.form = {
        exceptionTitle: '',
        exceptionType: '',
        exceptionLevel: '',
        handleBy: '',
        exceptionTime: '',
        nextRetryTime: '',
        exceptionDesc: '',
        handleResult: ''
      }
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.dialogTitle = '编辑异常'
      try {
        const res = await getExceptionById(row.exceptionId)
        if (res.code === 1 && res.data) {
          // 处理时间字段，将时间戳转换为日期字符串
          const data = { ...res.data }
          // 转换 exceptionTime
          if (data.exceptionTime) {
            const timestamp = typeof data.exceptionTime === 'number'
              ? data.exceptionTime
              : new Date(data.exceptionTime).getTime()
            const date = new Date(timestamp)
            const year = date.getFullYear()
            const month = String(date.getMonth() + 1).padStart(2, '0')
            const day = String(date.getDate()).padStart(2, '0')
            const hours = String(date.getHours()).padStart(2, '0')
            const minutes = String(date.getMinutes()).padStart(2, '0')
            const seconds = String(date.getSeconds()).padStart(2, '0')
            data.exceptionTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
          }
          // 转换 nextRetryTime（预计解决时间）
          if (data.nextRetryTime) {
            const timestamp = typeof data.nextRetryTime === 'number'
              ? data.nextRetryTime
              : new Date(data.nextRetryTime).getTime()
            const date = new Date(timestamp)
            const year = date.getFullYear()
            const month = String(date.getMonth() + 1).padStart(2, '0')
            const day = String(date.getDate()).padStart(2, '0')
            const hours = String(date.getHours()).padStart(2, '0')
            const minutes = String(date.getMinutes()).padStart(2, '0')
            const seconds = String(date.getSeconds()).padStart(2, '0')
            data.nextRetryTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
          }
          this.form = data
        } else {
          // 如果接口失败，直接使用行数据
          const data = { ...row }
          // 同样处理时间字段
          if (data.exceptionTime) {
            const timestamp = typeof data.exceptionTime === 'number'
              ? data.exceptionTime
              : new Date(data.exceptionTime).getTime()
            const date = new Date(timestamp)
            const year = date.getFullYear()
            const month = String(date.getMonth() + 1).padStart(2, '0')
            const day = String(date.getDate()).padStart(2, '0')
            const hours = String(date.getHours()).padStart(2, '0')
            const minutes = String(date.getMinutes()).padStart(2, '0')
            const seconds = String(date.getSeconds()).padStart(2, '0')
            data.exceptionTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
          }
          if (data.nextRetryTime) {
            const timestamp = typeof data.nextRetryTime === 'number'
              ? data.nextRetryTime
              : new Date(data.nextRetryTime).getTime()
            const date = new Date(timestamp)
            const year = date.getFullYear()
            const month = String(date.getMonth() + 1).padStart(2, '0')
            const day = String(date.getDate()).padStart(2, '0')
            const hours = String(date.getHours()).padStart(2, '0')
            const minutes = String(date.getMinutes()).padStart(2, '0')
            const seconds = String(date.getSeconds()).padStart(2, '0')
            data.nextRetryTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
          }
          this.form = data
        }
      } catch (error) {
        // 如果接口失败，直接使用行数据
        const data = { ...row }
        if (data.exceptionTime) {
          const timestamp = typeof data.exceptionTime === 'number'
            ? data.exceptionTime
            : new Date(data.exceptionTime).getTime()
          const date = new Date(timestamp)
          const year = date.getFullYear()
          const month = String(date.getMonth() + 1).padStart(2, '0')
          const day = String(date.getDate()).padStart(2, '0')
          const hours = String(date.getHours()).padStart(2, '0')
          const minutes = String(date.getMinutes()).padStart(2, '0')
          const seconds = String(date.getSeconds()).padStart(2, '0')
          data.exceptionTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
        }
        if (data.nextRetryTime) {
          const timestamp = typeof data.nextRetryTime === 'number'
            ? data.nextRetryTime
            : new Date(data.nextRetryTime).getTime()
          const date = new Date(timestamp)
          const year = date.getFullYear()
          const month = String(date.getMonth() + 1).padStart(2, '0')
          const day = String(date.getDate()).padStart(2, '0')
          const hours = String(date.getHours()).padStart(2, '0')
          const minutes = String(date.getMinutes()).padStart(2, '0')
          const seconds = String(date.getSeconds()).padStart(2, '0')
          data.nextRetryTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
        }
        this.form = data
      }
      this.dialogVisible = true
    },
    async handleView(row) {
      try {
        const res = await getExceptionById(row.exceptionId)
        if (res.code === 1) {
          this.$alert(`
            <div style="line-height: 2;">
              <p><strong>异常编号：</strong>${res.data.exceptionNo || '-'}</p>
              <p><strong>异常类型：</strong>${this.getExceptionTypeName(res.data.exceptionType)}</p>
              <p><strong>严重程度：</strong>${this.getSeverityName(res.data.exceptionLevel)}</p>
              <p><strong>处理状态：</strong>${this.getStatusName(res.data.exceptionStatus)}</p>
              <p><strong>异常描述：</strong>${res.data.exceptionDesc || '-'}</p>
              <p><strong>预计解决时间：</strong>${res.data.nextRetryTime ? this.formatDateTime(res.data.nextRetryTime) : '-'}</p>
              <p><strong>处理方案：</strong>${res.data.handleResult || '-'}</p>
            </div>
          `, '异常详情', {
            dangerouslyUseHTMLString: true,
            confirmButtonText: '关闭'
          })
        }
      } catch (error) {
        this.$message.error('获取详情失败')
      }
    },
    handleProcess(row) {
      this.currentRow = row
      this.currentStep = this.getStepByStatus(row.exceptionStatus)
      this.processDialogVisible = true
    },
    getStepByStatus(status) {
      const stepMap = { PENDING: 0, HANDLING: 2, RESOLVED: 4, CLOSED: 5 }
      return stepMap[status] || 0
    },
    handleDelete(row) {
      this.$confirm('确认删除该异常记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteException(row.exceptionId)
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
            this.loadStatistics()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            const api = this.form.exceptionId ? updateException : createException
            const res = await api(this.form)
            if (res.code === 1) {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.loadData()
              this.loadStatistics()
            } else {
              this.$message.error(res.msg || '保存失败')
            }
          } catch (error) {
            this.$message.error('保存失败')
          }
        }
      })
    },
    async handleNextStep() {
      if (this.currentStep < 5) {
        this.currentStep++
        if (this.currentRow) {
          try {
            const handleMethod = ['发现', '分析', '制定方案', '执行处理', '验证结果', '关闭'][this.currentStep]
            await handleException(this.currentRow.exceptionId, handleMethod, '处理中')
            this.$message.success('步骤更新成功')
          } catch (error) {
            console.error('更新步骤失败:', error)
          }
        }
      } else {
        if (this.currentRow) {
          try {
            await closeException(this.currentRow.exceptionId, '处理完成')
            this.$message.success('异常处理完成')
            this.loadData()
            this.loadStatistics()
          } catch (error) {
            this.$message.error('关闭异常失败')
          }
        }
        this.processDialogVisible = false
      }
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.loadData()
    },
    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },
    getExceptionTypeTag(type) {
      const tags = {
        TRANSFER_FAILED: 'danger',
        TIMEOUT: 'warning',
        DATA_ERROR: 'danger',
        SYSTEM_ERROR: 'info'
      }
      return tags[type] || 'default'
    },
    getExceptionTypeName(type) {
      const names = {
        TRANSFER_FAILED: '转账失败',
        TIMEOUT: '超时',
        DATA_ERROR: '数据错误',
        SYSTEM_ERROR: '系统错误'
      }
      return names[type] || type
    },
    getSeverityTag(severity) {
      const tags = {
        NORMAL: 'success',
        IMPORTANT: 'warning',
        URGENT: 'danger'
      }
      return tags[severity] || 'default'
    },
    getSeverityName(severity) {
      const names = {
        NORMAL: '普通',
        IMPORTANT: '重要',
        URGENT: '紧急'
      }
      return names[severity] || severity
    },
    getStatusTag(status) {
      const tags = {
        PENDING: 'warning',
        HANDLING: 'primary',
        RESOLVED: 'success',
        CLOSED: 'info'
      }
      return tags[status] || 'default'
    },
    getStatusName(status) {
      const names = {
        PENDING: '待处理',
        HANDLING: '处理中',
        RESOLVED: '已解决',
        CLOSED: '已关闭'
      }
      return names[status] || status
    },
    formatDateTime(timestamp) {
      if (!timestamp) return '-'
      // 如果是数字，转换为 Date 对象
      const date = typeof timestamp === 'number' ? new Date(timestamp) : new Date(timestamp)
      // 格式化为 yyyy-MM-dd HH:mm:ss
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style scoped>
.gjyccl-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-card {
  margin-bottom: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  position: relative;
  overflow: hidden;
}

.stat-content {
  padding: 20px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.stat-icon {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 40px;
  opacity: 0.3;
}

.stat-icon.success {
  color: #67C23A;
}

.stat-icon.warning {
  color: #E6A23C;
}

.stat-icon.info {
  color: #409EFF;
}

.stat-icon.primary {
  color: #409EFF;
}

.table-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
