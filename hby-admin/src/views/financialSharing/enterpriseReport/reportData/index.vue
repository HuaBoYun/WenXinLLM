<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="报表任务">
          <el-select v-model="queryForm.taskId" placeholder="请选择报表任务" clearable @change="handleTaskChange">
            <el-option
              v-for="item in taskOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="表单模板">
          <el-select v-model="queryForm.templateId" placeholder="请选择表单模板" clearable>
            <el-option
              v-for="item in templateOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="指标">
          <el-select v-model="queryForm.indicatorId" placeholder="请选择指标" clearable>
            <el-option
              v-for="item in indicatorOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="期间">
          <el-input v-model="queryForm.period" placeholder="如:202401" clearable />
        </el-form-item>
        <el-form-item label="数据来源">
          <el-select v-model="queryForm.dataSource" placeholder="请选择数据来源" clearable>
            <el-option label="手工" value="MANUAL" />
            <el-option label="取数" value="FETCH" />
            <el-option label="计算" value="CALCULATION" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            icon="el-icon-upload2"
            size="mini"
            @click="handleBatchImport"
          >批量导入</el-button>
        </el-col>
      </el-row>

      <!-- 报表数据表格 -->
      <el-table
        v-loading="loading"
        :data="reportDataList"
        border
      >
        <el-table-column prop="taskName" label="所属任务" width="150" />
        <el-table-column prop="templateName" label="表单模板" width="150" />
        <el-table-column prop="indicatorName" label="指标名称" width="150" />
        <el-table-column prop="period" label="期间" width="100" />
        <el-table-column prop="dataValue" label="数据值" width="120" />
        <el-table-column prop="dataSource" label="数据来源" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.dataSource === 'MANUAL'" type="primary">手工</el-tag>
            <el-tag v-else-if="scope.row.dataSource === 'FETCH'" type="success">取数</el-tag>
            <el-tag v-else-if="scope.row.dataSource === 'CALCULATION'" type="warning">计算</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="isEditable" label="可编辑" width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isEditable === 'Y'" type="success">是</el-tag>
            <el-tag v-else type="info">否</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="cellColor" label="单元格颜色" width="100">
          <template slot-scope="scope">
            <div v-if="scope.row.cellColor" :style="{backgroundColor: scope.row.cellColor, width: '50px', height: '20px', border: '1px solid #ddd'}" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.isEditable === 'Y'"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加或修改报表数据对话框 -->
    <report-data-form
      ref="reportDataForm"
      :visible.sync="dialogVisible"
      :form-data="formData"
      :task-options="taskOptions"
      :template-options="templateOptions"
      :indicator-options="indicatorOptions"
      @success="handleSuccess"
    />
  </div>
</template>

<script>
import { getReportDataList, deleteReportData } from '@/api/financialSharing/enterpriseReport/reportData'
import { getReportTaskList } from '@/api/financialSharing/enterpriseReport/reportTask'
import { getFormTemplateList } from '@/api/financialSharing/enterpriseReport/formTemplate'
import { getIndicatorInfoList } from '@/api/financialSharing/enterpriseReport/indicatorInfo'
import ReportDataForm from './components/ReportDataForm'

export default {
  name: 'ReportData',
  components: {
    ReportDataForm
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 查询参数
      queryForm: {
        taskId: '',
        templateId: '',
        indicatorId: '',
        period: '',
        dataSource: ''
      },
      // 报表数据列表
      reportDataList: [],
      // 任务选项
      taskOptions: [],
      // 模板选项
      templateOptions: [],
      // 指标选项
      indicatorOptions: [],
      // 弹出层标题
      dialogVisible: false,
      // 表单参数
      formData: {}
    }
  },
  created() {
    this.getTaskOptions()
    this.getIndicatorOptions()
    this.getList()
  },
  methods: {
    /** 获取任务选项 */
    getTaskOptions() {
      getReportTaskList({ status: 'PUBLISHED' }).then(response => {
        if (response.code === 200) {
          this.taskOptions = (response.data || []).map(item => ({
            value: item.taskId,
            label: item.taskName,
            groupId: item.groupId
          }))
        }
      })
    },
    /** 任务变更时加载模板选项 */
    handleTaskChange(taskId) {
      if (taskId) {
        const task = this.taskOptions.find(t => t.value === taskId)
        if (task && task.groupId) {
          this.getTemplateOptions(task.groupId)
        }
      } else {
        this.templateOptions = []
      }
      this.queryForm.templateId = ''
    },
    /** 获取模板选项 */
    getTemplateOptions(groupId) {
      getFormTemplateList({ groupId, status: 'ACTIVE' }).then(response => {
        if (response.code === 200) {
          this.templateOptions = (response.data || []).map(item => ({
            value: item.templateId,
            label: item.templateName
          }))
        }
      })
    },
    /** 获取指标选项 */
    getIndicatorOptions() {
      getIndicatorInfoList({ status: 'ACTIVE' }).then(response => {
        if (response.code === 200) {
          this.indicatorOptions = (response.data || []).map(item => ({
            value: item.indicatorId,
            label: item.indicatorName
          }))
        }
      })
    },
    /** 查询报表数据列表 */
    getList() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      getReportDataList(this.queryForm).then(response => {
        this.loading = false
        if (response.code === 1 || response.code === 200) {
          this.reportDataList = response.data.list || response.data.records || response.data || []
        } else {
          this.$message.error(response.msg || '查询失败')
          this.reportDataList = []
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.reportDataList = []
      })
    },
    /** 查询按钮操作 */
    handleQuery() {
      this.getList()
    },
    /** 重置按钮操作 */
    handleReset() {
      this.queryForm = {
        taskId: '',
        templateId: '',
        indicatorId: '',
        period: '',
        dataSource: ''
      }
      this.templateOptions = []
      this.getList()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.formData = {
        dataId: '',
        taskId: '',
        templateId: '',
        indicatorId: '',
        orgId: '',
        period: '',
        dimensionValues: '',
        dataValue: '',
        dataSource: 'MANUAL',
        cellColor: '',
        isEditable: 'Y'
      }
      this.dialogVisible = true
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.formData = { ...row }
      this.dialogVisible = true
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除该报表数据?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return deleteReportData(row.dataId)
      }).then(response => {
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      }).catch(() => {})
    },
    /** 批量导入按钮操作 */
    handleBatchImport() {
      const reportDataApi = require('@/api/financialSharing/enterpriseReport/reportData').default
      if (reportDataApi && reportDataApi.importData) {
        reportDataApi.importData().then(res => {
          if (res.code === 1) {
            this.$message.success('批量导入成功')
            this.getList()
          } else {
            this.$message.error(res.msg || '导入失败')
          }
        }).catch(() => {
          this.$message.error('导入请求失败')
        })
      } else {
        this.$message.warning('批量导入功能需配置导入模板')
      }
    },
    /** 表单提交成功回调 */
    handleSuccess() {
      this.dialogVisible = false
      this.getList()
    },
    /** 时间格式化 */
    parseTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }
  }
}
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>


