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
            <el-option label="交易异常" value="transaction" />
            <el-option label="汇率异常" value="exchange_rate" />
            <el-option label="合规异常" value="compliance" />
            <el-option label="系统异常" value="system" />
          </el-select>
        </el-form-item>
        <el-form-item label="严重程度">
          <el-select v-model="searchForm.severity" placeholder="请选择严重程度" clearable>
            <el-option label="低" value="low" />
            <el-option label="中" value="medium" />
            <el-option label="高" value="high" />
            <el-option label="紧急" value="critical" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="searchForm.status" placeholder="请选择处理状态" clearable>
            <el-option label="待处理" value="pending" />
            <el-option label="处理中" value="processing" />
            <el-option label="已解决" value="resolved" />
            <el-option label="已关闭" value="closed" />
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
            <div class="stat-value">25</div>
            <div class="stat-label">待处理异常</div>
          </div>
          <i class="el-icon-warning stat-icon warning"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="never">
          <div class="stat-content">
            <div class="stat-value">8</div>
            <div class="stat-label">处理中异常</div>
          </div>
          <i class="el-icon-loading stat-icon info"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="never">
          <div class="stat-content">
            <div class="stat-value">156</div>
            <div class="stat-label">已解决异常</div>
          </div>
          <i class="el-icon-success stat-icon success"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="never">
          <div class="stat-content">
            <div class="stat-value">95.2%</div>
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
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增异常</el-button>
      </div>
      
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="exceptionId" label="异常编号" width="120" />
        <el-table-column prop="title" label="异常标题" min-width="150" />
        <el-table-column prop="exceptionType" label="异常类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getExceptionTypeTag(scope.row.exceptionType)">
              {{ getExceptionTypeName(scope.row.exceptionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="severity" label="严重程度" width="100">
          <template slot-scope="scope">
            <el-tag :type="getSeverityTag(scope.row.severity)">
              {{ getSeverityName(scope.row.severity) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="处理状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assignee" label="处理人" width="100" />
        <el-table-column prop="occurTime" label="发生时间" width="160" />
        <el-table-column prop="resolveTime" label="解决时间" width="160">
          <template slot-scope="scope">
            <span>{{ scope.row.resolveTime || '-' }}</span>
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
            <el-form-item label="异常标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入异常标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="异常类型" prop="exceptionType">
              <el-select v-model="form.exceptionType" placeholder="请选择异常类型">
                <el-option label="交易异常" value="transaction" />
                <el-option label="汇率异常" value="exchange_rate" />
                <el-option label="合规异常" value="compliance" />
                <el-option label="系统异常" value="system" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="严重程度" prop="severity">
              <el-select v-model="form.severity" placeholder="请选择严重程度">
                <el-option label="低" value="low" />
                <el-option label="中" value="medium" />
                <el-option label="高" value="high" />
                <el-option label="紧急" value="critical" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="处理人" prop="assignee">
              <el-input v-model="form.assignee" placeholder="请输入处理人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="发生时间" prop="occurTime">
              <el-date-picker
                v-model="form.occurTime"
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
                v-model="form.expectedResolveTime"
                type="datetime"
                placeholder="选择预计解决时间"
                format="yyyy-MM-dd HH:mm:ss"
                value-format="yyyy-MM-dd HH:mm:ss"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="异常描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请详细描述异常情况"
          />
        </el-form-item>
        <el-form-item label="处理方案">
          <el-input
            v-model="form.solution"
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
export default {
  name: 'GjycclManage',
  data() {
    return {
      loading: false,
      searchForm: {
        exceptionType: '',
        severity: '',
        status: '',
        dateRange: []
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      dialogVisible: false,
      processDialogVisible: false,
      dialogTitle: '新增异常',
      currentStep: 0,
      form: {
        title: '',
        exceptionType: '',
        severity: '',
        assignee: '',
        occurTime: '',
        expectedResolveTime: '',
        description: '',
        solution: ''
      },
      rules: {
        title: [
          { required: true, message: '请输入异常标题', trigger: 'blur' }
        ],
        exceptionType: [
          { required: true, message: '请选择异常类型', trigger: 'change' }
        ],
        severity: [
          { required: true, message: '请选择严重程度', trigger: 'change' }
        ],
        assignee: [
          { required: true, message: '请输入处理人', trigger: 'blur' }
        ],
        occurTime: [
          { required: true, message: '请选择发生时间', trigger: 'change' }
        ],
        description: [
          { required: true, message: '请输入异常描述', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      // 模拟数据
      setTimeout(() => {
        this.tableData = [
          {
            id: 1,
            exceptionId: 'EXC202400001',
            title: '汇率波动异常',
            exceptionType: 'exchange_rate',
            severity: 'high',
            status: 'processing',
            assignee: '张三',
            occurTime: '2024-04-01 10:30:00',
            resolveTime: null
          }
        ]
        this.pagination.total = 1
        this.loading = false
      }, 1000)
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        exceptionType: '',
        severity: '',
        status: '',
        dateRange: []
      }
      this.handleSearch()
    },
    handleExport() {
      this.$message.success('导出功能开发中...')
    },
    handleAdd() {
      this.dialogTitle = '新增异常'
      this.form = {
        title: '',
        exceptionType: '',
        severity: '',
        assignee: '',
        occurTime: '',
        expectedResolveTime: '',
        description: '',
        solution: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑异常'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$message.info('查看功能开发中...')
    },
    handleProcess(row) {
      this.currentStep = 2 // 示例步骤
      this.processDialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该异常记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.loadData()
      })
    },
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        }
      })
    },
    handleNextStep() {
      if (this.currentStep < 5) {
        this.currentStep++
        this.$message.success('步骤更新成功')
      } else {
        this.$message.success('异常处理完成')
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
        transaction: 'primary',
        exchange_rate: 'warning',
        compliance: 'danger',
        system: 'info'
      }
      return tags[type] || 'default'
    },
    getExceptionTypeName(type) {
      const names = {
        transaction: '交易异常',
        exchange_rate: '汇率异常',
        compliance: '合规异常',
        system: '系统异常'
      }
      return names[type] || type
    },
    getSeverityTag(severity) {
      const tags = {
        low: 'success',
        medium: 'warning',
        high: 'danger',
        critical: 'danger'
      }
      return tags[severity] || 'default'
    },
    getSeverityName(severity) {
      const names = {
        low: '低',
        medium: '中',
        high: '高',
        critical: '紧急'
      }
      return names[severity] || severity
    },
    getStatusTag(status) {
      const tags = {
        pending: 'warning',
        processing: 'primary',
        resolved: 'success',
        closed: 'info'
      }
      return tags[status] || 'default'
    },
    getStatusName(status) {
      const names = {
        pending: '待处理',
        processing: '处理中',
        resolved: '已解决',
        closed: '已关闭'
      }
      return names[status] || status
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
