<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="任务编码">
        <el-input v-model="queryForm.taskCode" placeholder="请输入任务编码" clearable />
      </el-form-item>
      <el-form-item label="任务名称">
        <el-input v-model="queryForm.taskName" placeholder="请输入任务名称" clearable />
      </el-form-item>
      <el-form-item label="任务类型">
        <el-select v-model="queryForm.taskType" placeholder="请选择任务类型" clearable>
          <el-option label="手动" value="MANUAL" />
          <el-option label="定时" value="SCHEDULED" />
          <el-option label="实时" value="REALTIME" />
        </el-select>
      </el-form-item>
      <el-form-item label="数据源">
        <el-select v-model="queryForm.sourceId" placeholder="请选择数据源" clearable filterable>
          <el-option
            v-for="item in dataSourceList"
            :key="item.sourceId"
            :label="item.sourceName"
            :value="item.sourceId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="执行状态">
        <el-select v-model="queryForm.executeStatus" placeholder="请选择执行状态" clearable>
          <el-option label="待执行" value="PENDING" />
          <el-option label="运行中" value="RUNNING" />
          <el-option label="成功" value="SUCCESS" />
          <el-option label="失败" value="FAILED" />
        </el-select>
      </el-form-item>
      <el-form-item label="启用状态">
        <el-select v-model="queryForm.isEnabled" placeholder="请选择启用状态" clearable>
          <el-option label="启用" value="Y" />
          <el-option label="禁用" value="N" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      stripe
      style="width: 100%"
    >
      <el-table-column prop="taskCode" label="任务编码" width="150" />
      <el-table-column prop="taskName" label="任务名称" width="200" />
      <el-table-column prop="taskType" label="任务类型" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.taskType === 'MANUAL'" type="info">手动</el-tag>
          <el-tag v-else-if="scope.row.taskType === 'SCHEDULED'" type="primary">定时</el-tag>
          <el-tag v-else-if="scope.row.taskType === 'REALTIME'" type="success">实时</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sourceName" label="数据源" width="150" />
      <el-table-column prop="scheduleType" label="调度类型" width="100">
        <template slot-scope="scope">
          <span v-if="scope.row.scheduleType === 'ONCE'">一次</span>
          <span v-else-if="scope.row.scheduleType === 'DAILY'">每日</span>
          <span v-else-if="scope.row.scheduleType === 'WEEKLY'">每周</span>
          <span v-else-if="scope.row.scheduleType === 'MONTHLY'">每月</span>
          <span v-else-if="scope.row.scheduleType === 'CRON'">CRON</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column prop="executeStatus" label="执行状态" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.executeStatus === 'PENDING'" type="info">待执行</el-tag>
          <el-tag v-else-if="scope.row.executeStatus === 'RUNNING'" type="warning">运行中</el-tag>
          <el-tag v-else-if="scope.row.executeStatus === 'SUCCESS'" type="success">成功</el-tag>
          <el-tag v-else-if="scope.row.executeStatus === 'FAILED'" type="danger">失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="totalCount" label="总记录数" width="100" />
      <el-table-column prop="successCount" label="成功数" width="100" />
      <el-table-column prop="failedCount" label="失败数" width="100" />
      <el-table-column prop="nextExecuteTime" label="下次执行时间" width="160">
        <template slot-scope="scope">
          {{ scope.row.nextExecuteTime ? formatDate(scope.row.nextExecuteTime) : '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="isEnabled" label="启用状态" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isEnabled"
            active-value="Y"
            inactive-value="N"
            @change="handleToggleEnabled(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            size="mini"
            type="success"
            :disabled="scope.row.executeStatus === 'RUNNING' || scope.row.isEnabled === 'N'"
            @click="handleExecute(scope.row)"
          >
            执行
          </el-button>
          <el-button
            size="mini"
            type="warning"
            :disabled="scope.row.executeStatus !== 'RUNNING'"
            @click="handleStop(scope.row)"
          >
            停止
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="loadData"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-form-item label="任务编码" prop="taskCode">
          <el-input v-model="form.taskCode" placeholder="请输入任务编码" />
        </el-form-item>
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="form.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务类型" prop="taskType">
          <el-select v-model="form.taskType" placeholder="请选择任务类型" @change="handleTaskTypeChange">
            <el-option label="手动" value="MANUAL" />
            <el-option label="定时" value="SCHEDULED" />
            <el-option label="实时" value="REALTIME" />
          </el-select>
        </el-form-item>

        <!-- 数据配置 -->
        <el-divider content-position="left">数据配置</el-divider>
        <el-form-item label="数据源" prop="sourceId">
          <el-select v-model="form.sourceId" placeholder="请选择数据源" filterable @change="handleSourceChange">
            <el-option
              v-for="item in dataSourceList"
              :key="item.sourceId"
              :label="item.sourceName"
              :value="item.sourceId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="映射规则" prop="ruleIds">
          <el-select v-model="selectedRuleIds" placeholder="请选择映射规则" multiple filterable>
            <el-option
              v-for="item in mappingRuleList"
              :key="item.ruleId"
              :label="item.ruleName"
              :value="item.ruleId"
            />
          </el-select>
        </el-form-item>

        <!-- 调度配置（仅定时任务显示） -->
        <template v-if="form.taskType === 'SCHEDULED'">
          <el-divider content-position="left">调度配置</el-divider>
          <el-form-item label="调度类型" prop="scheduleType">
            <el-select v-model="form.scheduleType" placeholder="请选择调度类型" @change="handleScheduleTypeChange">
              <el-option label="一次" value="ONCE" />
              <el-option label="每日" value="DAILY" />
              <el-option label="每周" value="WEEKLY" />
              <el-option label="每月" value="MONTHLY" />
              <el-option label="CRON表达式" value="CRON" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="form.scheduleType === 'CRON'" label="CRON表达式" prop="scheduleExpression">
            <el-input v-model="form.scheduleExpression" placeholder="请输入CRON表达式，如：0 0 2 * * ?" />
            <span class="form-tip">示例：0 0 2 * * ? 表示每天凌晨2点执行</span>
          </el-form-item>
          <el-form-item label="下次执行时间">
            <el-input v-model="nextExecuteTimeDisplay" disabled />
            <el-button type="text" @click="handleCalculateNextTime">计算</el-button>
          </el-form-item>
        </template>

        <!-- 其他配置 -->
        <el-divider content-position="left">其他配置</el-divider>
        <el-form-item label="是否启用">
          <el-switch v-model="form.isEnabled" active-value="Y" inactive-value="N" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryCollectionTaskPage,
  queryCollectionTaskById,
  saveCollectionTask,
  deleteCollectionTask,
  toggleCollectionTaskEnabled,
  executeCollectionTask,
  stopCollectionTask,
  calculateNextExecuteTime
} from '@/api/financialSharing/dataCollection'
import { queryDataSourcePage } from '@/api/financialSharing/dataCollection'
import { queryMappingRulePage } from '@/api/financialSharing/dataCollection'
import Pagination from '@/components/Pagination'

export default {
  name: 'CollectionTask',
  components: {
    Pagination
  },
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryForm: {
        taskCode: '',
        taskName: '',
        taskType: '',
        sourceId: '',
        executeStatus: '',
        isEnabled: '',
        pageNumber: 1,
        pageSize: 10
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        taskId: '',
        taskCode: '',
        taskName: '',
        taskType: 'MANUAL',
        sourceId: '',
        ruleIds: '',
        scheduleType: '',
        scheduleExpression: '',
        isEnabled: 'Y'
      },
      selectedRuleIds: [],
      nextExecuteTimeDisplay: '',
      rules: {
        taskCode: [
          { required: true, message: '请输入任务编码', trigger: 'blur' }
        ],
        taskName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        taskType: [
          { required: true, message: '请选择任务类型', trigger: 'change' }
        ],
        sourceId: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ],
        ruleIds: [
          { required: true, message: '请选择映射规则', trigger: 'change' }
        ],
        scheduleType: [
          { required: true, message: '请选择调度类型', trigger: 'change' }
        ],
        scheduleExpression: [
          { required: true, message: '请输入CRON表达式', trigger: 'blur' }
        ]
      },
      dataSourceList: [],
      mappingRuleList: []
    }
  },
  created() {
    this.loadData()
    this.loadDataSources()
  },
  methods: {
    // 加载数据
    loadData() {
      this.loading = true
      queryCollectionTaskPage(this.queryForm).then(res => {
        if (res.code === 1) {
          this.tableData = res.data.records || []
          this.total = res.data.total || 0

          // 加载数据源名称
          this.tableData.forEach(item => {
            const source = this.dataSourceList.find(s => s.sourceId === item.sourceId)
            if (source) {
              item.sourceName = source.sourceName
            }
          })
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    // 加载数据源列表
    loadDataSources() {
      queryDataSourcePage({ isEnabled: 'Y', pageNumber: 1, pageSize: 1000 }).then(res => {
        if (res.code === 1) {
          this.dataSourceList = res.data.records || []
        }
      })
    },

    // 加载映射规则列表
    loadMappingRules(sourceId) {
      if (!sourceId) {
        this.mappingRuleList = []
        return
      }
      queryMappingRulePage({ sourceId: sourceId, isEnabled: 'Y', pageNumber: 1, pageSize: 1000 }).then(res => {
        if (res.code === 1) {
          this.mappingRuleList = res.data.records || []
        }
      })
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.queryForm = {
        taskCode: '',
        taskName: '',
        taskType: '',
        sourceId: '',
        executeStatus: '',
        isEnabled: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.loadData()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增归集任务'
      this.form = {
        taskId: '',
        taskCode: '',
        taskName: '',
        taskType: 'MANUAL',
        sourceId: '',
        ruleIds: '',
        scheduleType: '',
        scheduleExpression: '',
        isEnabled: 'Y'
      }
      this.selectedRuleIds = []
      this.nextExecuteTimeDisplay = ''
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.form.clearValidate()
      })
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑归集任务'
      queryCollectionTaskById({ taskId: row.taskId }).then(res => {
        if (res.code === 1) {
          this.form = { ...res.data }

          // 解析规则ID列表
          if (this.form.ruleIds) {
            try {
              this.selectedRuleIds = JSON.parse(this.form.ruleIds)
            } catch (e) {
              this.selectedRuleIds = []
            }
          }

          // 加载映射规则列表
          this.loadMappingRules(this.form.sourceId)

          // 显示下次执行时间
          if (this.form.nextExecuteTime) {
            this.nextExecuteTimeDisplay = this.formatDate(this.form.nextExecuteTime)
          }

          this.dialogVisible = true
        }
      })
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确定要删除该归集任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCollectionTask({ taskId: row.taskId }).then(res => {
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        })
      })
    },

    // 启用/禁用
    handleToggleEnabled(row) {
      toggleCollectionTaskEnabled({
        taskId: row.taskId,
        isEnabled: row.isEnabled
      }).then(res => {
        if (res.code === 1) {
          this.$message.success('操作成功')
          this.loadData()
        } else {
          this.$message.error(res.msg || '操作失败')
          // 恢复原状态
          row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
        }
      }).catch(() => {
        // 恢复原状态
        row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
      })
    },

    // 执行任务
    handleExecute(row) {
      this.$confirm('确定要立即执行该任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        executeCollectionTask({ taskId: row.taskId }).then(res => {
          if (res.code === 1) {
            this.$message.success('任务已提交执行')
            this.loadData()
          } else {
            this.$message.error(res.msg || '执行失败')
          }
        })
      })
    },

    // 停止任务
    handleStop(row) {
      this.$confirm('确定要停止该任务吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        stopCollectionTask({ taskId: row.taskId }).then(res => {
          if (res.code === 1) {
            this.$message.success('任务已停止')
            this.loadData()
          } else {
            this.$message.error(res.msg || '停止失败')
          }
        })
      })
    },

    // 提交
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 转换规则ID列表为JSON字符串
          this.form.ruleIds = JSON.stringify(this.selectedRuleIds)

          saveCollectionTask(this.form).then(res => {
            if (res.code === 1) {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(res.msg || '保存失败')
            }
          })
        }
      })
    },

    // 任务类型变化
    handleTaskTypeChange(value) {
      if (value !== 'SCHEDULED') {
        this.form.scheduleType = ''
        this.form.scheduleExpression = ''
        this.nextExecuteTimeDisplay = ''
      }
    },

    // 数据源变化
    handleSourceChange(value) {
      this.selectedRuleIds = []
      this.form.ruleIds = ''
      this.loadMappingRules(value)
    },

    // 调度类型变化
    handleScheduleTypeChange(value) {
      if (value !== 'CRON') {
        this.form.scheduleExpression = ''
      }
      this.nextExecuteTimeDisplay = ''
    },

    // 计算下次执行时间
    handleCalculateNextTime() {
      if (this.form.taskType !== 'SCHEDULED') {
        this.$message.warning('只有定时任务才需要计算下次执行时间')
        return
      }
      if (!this.form.scheduleType) {
        this.$message.warning('请先选择调度类型')
        return
      }
      if (this.form.scheduleType === 'CRON' && !this.form.scheduleExpression) {
        this.$message.warning('请先输入CRON表达式')
        return
      }

      calculateNextExecuteTime(this.form).then(res => {
        if (res.code === 1) {
          this.nextExecuteTimeDisplay = this.formatDate(res.data)
          this.$message.success('计算成功')
        } else {
          this.$message.error(res.msg || '计算失败')
        }
      })
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hour = String(d.getHours()).padStart(2, '0')
      const minute = String(d.getMinutes()).padStart(2, '0')
      const second = String(d.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.query-form {
  background: #f5f5f5;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.el-table {
  margin-bottom: 20px;
}

.dialog-footer {
  text-align: right;
}

.el-divider {
  margin: 20px 0;
}

.form-tip {
  font-size: 12px;
  color: #999;
  margin-left: 10px;
}
</style>

