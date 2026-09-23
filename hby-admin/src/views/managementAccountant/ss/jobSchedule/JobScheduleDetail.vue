<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="job-schedule-detail">
      <el-form
        ref="jobScheduleForm"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        size="small"
      >
        <el-tabs v-model="activeTab" type="border-card">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="作业编码" prop="jobCode">
                  <el-input v-model="formData.jobCode" :readonly="readonly" placeholder="请输入作业编码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="作业名称" prop="jobName">
                  <el-input v-model="formData.jobName" :readonly="readonly" placeholder="请输入作业名称" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="作业类型" prop="jobType">
                  <el-select v-model="formData.jobType" :disabled="readonly" placeholder="请选择作业类型" style="width: 100%">
                    <el-option label="批处理" value="BATCH" />
                    <el-option label="实时处理" value="REALTIME" />
                    <el-option label="定时任务" value="SCHEDULED" />
                    <el-option label="触发任务" value="TRIGGER" />
                    <el-option label="工作流" value="WORKFLOW" />
                    <el-option label="数据处理" value="ETL" />
                    <el-option label="备份任务" value="BACKUP" />
                    <el-option label="维护任务" value="MAINTENANCE" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="作业分类" prop="jobCategory">
                  <el-input v-model="formData.jobCategory" :readonly="readonly" placeholder="请输入作业分类" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="优先级" prop="priority">
                  <el-input-number
                    v-model="formData.priority"
                    :readonly="readonly"
                    :min="1"
                    :max="10"
                    placeholder="请输入优先级"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="优先级权重" prop="priorityWeight">
                  <el-input-number
                    v-model="formData.priorityWeight"
                    :readonly="readonly"
                    :precision="2"
                    :min="0"
                    :max="100"
                    placeholder="请输入优先级权重"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="作业描述" prop="jobDescription">
              <el-input
                v-model="formData.jobDescription"
                :readonly="readonly"
                type="textarea"
                :rows="3"
                placeholder="请输入作业描述"
              />
            </el-form-item>
          </el-tab-pane>

          <!-- 调度配置 -->
          <el-tab-pane label="调度配置" name="schedule">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="调度表达式" prop="scheduleExpression">
                  <el-input v-model="formData.scheduleExpression" :readonly="readonly" placeholder="请输入Cron表达式" />
                  <div class="form-tip">例如: 0 0 12 * * ? (每天中午12点执行)</div>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="调度类型" prop="scheduleType">
                  <el-select v-model="formData.scheduleType" :disabled="readonly" placeholder="请选择调度类型" style="width: 100%">
                    <el-option label="定时调度" value="CRON" />
                    <el-option label="固定间隔" value="FIXED_RATE" />
                    <el-option label="固定延迟" value="FIXED_DELAY" />
                    <el-option label="手动触发" value="MANUAL" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="调度策略" prop="scheduleStrategy">
                  <el-select v-model="formData.scheduleStrategy" :disabled="readonly" placeholder="请选择调度策略" style="width: 100%">
                    <el-option label="立即执行" value="IMMEDIATE" />
                    <el-option label="延迟执行" value="DELAYED" />
                    <el-option label="条件执行" value="CONDITIONAL" />
                    <el-option label="依赖执行" value="DEPENDENT" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="调度算法" prop="scheduleAlgorithm">
                  <el-select v-model="formData.scheduleAlgorithm" :disabled="readonly" placeholder="请选择调度算法" style="width: 100%">
                    <el-option label="先进先出" value="FIFO" />
                    <el-option label="最短作业优先" value="SJF" />
                    <el-option label="优先级调度" value="PRIORITY" />
                    <el-option label="轮询调度" value="ROUND_ROBIN" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="负载均衡策略" prop="loadBalanceStrategy">
                  <el-select v-model="formData.loadBalanceStrategy" :disabled="readonly" placeholder="请选择负载均衡策略" style="width: 100%">
                    <el-option label="轮询" value="ROUND_ROBIN" />
                    <el-option label="最少连接" value="LEAST_CONNECTIONS" />
                    <el-option label="加权轮询" value="WEIGHTED_ROUND_ROBIN" />
                    <el-option label="基于资源" value="RESOURCE_BASED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="执行节点" prop="executionNode">
                  <el-input v-model="formData.executionNode" :readonly="readonly" placeholder="请输入执行节点" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <!-- 执行配置 -->
          <el-tab-pane label="执行配置" name="execution">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="执行器类型" prop="executorType">
                  <el-select v-model="formData.executorType" :disabled="readonly" placeholder="请选择执行器类型" style="width: 100%">
                    <el-option label="Shell脚本" value="SHELL" />
                    <el-option label="Java程序" value="JAVA" />
                    <el-option label="Python脚本" value="PYTHON" />
                    <el-option label="HTTP请求" value="HTTP" />
                    <el-option label="SQL脚本" value="SQL" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="超时时间(秒)" prop="timeoutSeconds">
                  <el-input-number
                    v-model="formData.timeoutSeconds"
                    :readonly="readonly"
                    :min="1"
                    placeholder="请输入超时时间"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="重试次数" prop="retryCount">
                  <el-input-number
                    v-model="formData.retryCount"
                    :readonly="readonly"
                    :min="0"
                    :max="10"
                    placeholder="请输入重试次数"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="重试间隔(秒)" prop="retryInterval">
                  <el-input-number
                    v-model="formData.retryInterval"
                    :readonly="readonly"
                    :min="1"
                    placeholder="请输入重试间隔"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="失败策略" prop="failureStrategy">
                  <el-select v-model="formData.failureStrategy" :disabled="readonly" placeholder="请选择失败策略" style="width: 100%">
                    <el-option label="继续执行" value="CONTINUE" />
                    <el-option label="停止执行" value="STOP" />
                    <el-option label="重试执行" value="RETRY" />
                    <el-option label="跳过执行" value="SKIP" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="是否启用" prop="isEnabled">
                  <el-switch
                    v-model="formData.isEnabled"
                    :disabled="readonly"
                    active-text="启用"
                    inactive-text="禁用"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="执行命令" prop="executionCommand">
              <el-input
                v-model="formData.executionCommand"
                :readonly="readonly"
                type="textarea"
                :rows="3"
                placeholder="请输入执行命令"
              />
            </el-form-item>
            
            <el-form-item label="执行参数" prop="executionParams">
              <el-input
                v-model="formData.executionParams"
                :readonly="readonly"
                type="textarea"
                :rows="3"
                placeholder="请输入执行参数(JSON格式)"
              />
            </el-form-item>
            
            <el-form-item label="执行脚本" prop="executionScript">
              <el-input
                v-model="formData.executionScript"
                :readonly="readonly"
                type="textarea"
                :rows="5"
                placeholder="请输入执行脚本"
              />
            </el-form-item>
          </el-tab-pane>

          <!-- 资源配置 -->
          <el-tab-pane label="资源配置" name="resource">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="CPU需求" prop="cpuRequirement">
                  <el-input-number
                    v-model="formData.cpuRequirement"
                    :readonly="readonly"
                    :precision="2"
                    :min="0"
                    placeholder="请输入CPU需求"
                    style="width: 100%"
                  />
                  <div class="form-tip">单位: 核心数</div>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="内存需求(MB)" prop="memoryRequirement">
                  <el-input-number
                    v-model="formData.memoryRequirement"
                    :readonly="readonly"
                    :min="0"
                    placeholder="请输入内存需求"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="磁盘需求(MB)" prop="diskRequirement">
                  <el-input-number
                    v-model="formData.diskRequirement"
                    :readonly="readonly"
                    :min="0"
                    placeholder="请输入磁盘需求"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="网络需求" prop="networkRequirement">
                  <el-input v-model="formData.networkRequirement" :readonly="readonly" placeholder="请输入网络需求" />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-form-item label="资源需求" prop="resourceRequirements">
              <el-input
                v-model="formData.resourceRequirements"
                :readonly="readonly"
                type="textarea"
                :rows="3"
                placeholder="请输入资源需求描述(JSON格式)"
              />
            </el-form-item>
            
            <el-form-item label="SLA要求" prop="slaRequirements">
              <el-input
                v-model="formData.slaRequirements"
                :readonly="readonly"
                type="textarea"
                :rows="3"
                placeholder="请输入SLA要求(JSON格式)"
              />
            </el-form-item>
          </el-tab-pane>

          <!-- 依赖关系 -->
          <el-tab-pane label="依赖关系" name="dependency">
            <el-form-item label="依赖作业" prop="dependencyJobs">
              <el-input
                v-model="formData.dependencyJobs"
                :readonly="readonly"
                type="textarea"
                :rows="3"
                placeholder="请输入依赖作业ID列表(逗号分隔)"
              />
            </el-form-item>
            
            <el-form-item label="依赖条件" prop="dependencyCondition">
              <el-input
                v-model="formData.dependencyCondition"
                :readonly="readonly"
                type="textarea"
                :rows="3"
                placeholder="请输入依赖条件(JSON格式)"
              />
            </el-form-item>
          </el-tab-pane>

          <!-- 通知配置 -->
          <el-tab-pane label="通知配置" name="notification">
            <el-form-item label="通知配置" prop="notificationConfig">
              <el-input
                v-model="formData.notificationConfig"
                :readonly="readonly"
                type="textarea"
                :rows="5"
                placeholder="请输入通知配置(JSON格式)"
              />
            </el-form-item>
            
            <el-form-item label="告警配置" prop="alertConfig">
              <el-input
                v-model="formData.alertConfig"
                :readonly="readonly"
                type="textarea"
                :rows="5"
                placeholder="请输入告警配置(JSON格式)"
              />
            </el-form-item>
          </el-tab-pane>

          <!-- 统计信息 -->
          <el-tab-pane label="统计信息" name="statistics" v-if="!isCreate">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="执行次数">
                  <el-input :value="formData.executionCount || 0" readonly />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="成功次数">
                  <el-input :value="formData.successCount || 0" readonly />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="失败次数">
                  <el-input :value="formData.failureCount || 0" readonly />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="8">
                <el-form-item label="成功率">
                  <el-input :value="calculateSuccessRate(formData.successCount, formData.executionCount)" readonly />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="平均耗时">
                  <el-input :value="formatDuration(formData.avgDuration)" readonly />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="预期耗时">
                  <el-input :value="formatDuration(formData.expectedDuration)" readonly />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="最后执行时间">
                  <el-input :value="formData.lastExecutionTime | formatDateTime" readonly />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="下次执行时间">
                  <el-input :value="formData.nextExecutionTime | formatDateTime" readonly />
                </el-form-item>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="最后成功时间">
                  <el-input :value="formData.lastSuccessTime | formatDateTime" readonly />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="最后失败时间">
                  <el-input :value="formData.lastFailureTime | formatDateTime" readonly />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="!readonly" type="primary" @click="handleSave" :loading="saving">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
import jobScheduleApi from '@/api/managementAccountant/ss/jobSchedule'

export default {
  name: 'JobScheduleDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    jobId: {
      type: [String, Number],
      default: null
    },
    readonly: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      saving: false,
      activeTab: 'basic',
      formData: {
        jobCode: '',
        jobName: '',
        jobDescription: '',
        jobType: '',
        jobCategory: '',
        priority: 5,
        priorityWeight: 1.0,
        scheduleExpression: '',
        scheduleType: '',
        scheduleStrategy: '',
        scheduleAlgorithm: '',
        loadBalanceStrategy: '',
        executionNode: '',
        executorType: '',
        timeoutSeconds: 3600,
        retryCount: 3,
        retryInterval: 60,
        failureStrategy: 'RETRY',
        isEnabled: true,
        executionCommand: '',
        executionParams: '',
        executionScript: '',
        cpuRequirement: 0,
        memoryRequirement: 0,
        diskRequirement: 0,
        networkRequirement: '',
        resourceRequirements: '',
        slaRequirements: '',
        dependencyJobs: '',
        dependencyCondition: '',
        notificationConfig: '',
        alertConfig: '',
        tenantId: 1
      },
      formRules: {
        jobCode: [
          { required: true, message: '请输入作业编码', trigger: 'blur' }
        ],
        jobName: [
          { required: true, message: '请输入作业名称', trigger: 'blur' }
        ],
        jobType: [
          { required: true, message: '请选择作业类型', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请输入优先级', trigger: 'blur' }
        ],
        scheduleExpression: [
          { required: true, message: '请输入调度表达式', trigger: 'blur' }
        ],
        executorType: [
          { required: true, message: '请选择执行器类型', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    dialogTitle() {
      if (this.isCreate) {
        return '新建作业调度'
      } else if (this.readonly) {
        return '查看作业调度'
      } else {
        return '编辑作业调度'
      }
    },
    isCreate() {
      return !this.jobId
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadData()
      }
    }
  },
  methods: {
    /**
     * 加载数据
     */
    async loadData() {
      if (this.isCreate) {
        this.resetForm()
        // 生成作业编码
        this.formData.jobCode = jobScheduleApi.utils.generateJobCode()
        return
      }

      this.loading = true
      try {
        const response = await jobScheduleApi.getJobScheduleById(this.jobId)
        if (response.success) {
          this.formData = { ...this.formData, ...response.data }
        }
      } catch (error) {
        this.$message.error('加载数据失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },

    /**
     * 重置表单
     */
    resetForm() {
      this.formData = {
        jobCode: '',
        jobName: '',
        jobDescription: '',
        jobType: '',
        jobCategory: '',
        priority: 5,
        priorityWeight: 1.0,
        scheduleExpression: '',
        scheduleType: '',
        scheduleStrategy: '',
        scheduleAlgorithm: '',
        loadBalanceStrategy: '',
        executionNode: '',
        executorType: '',
        timeoutSeconds: 3600,
        retryCount: 3,
        retryInterval: 60,
        failureStrategy: 'RETRY',
        isEnabled: true,
        executionCommand: '',
        executionParams: '',
        executionScript: '',
        cpuRequirement: 0,
        memoryRequirement: 0,
        diskRequirement: 0,
        networkRequirement: '',
        resourceRequirements: '',
        slaRequirements: '',
        dependencyJobs: '',
        dependencyCondition: '',
        notificationConfig: '',
        alertConfig: '',
        tenantId: 1
      }
      this.activeTab = 'basic'
      this.$nextTick(() => {
        if (this.$refs.jobScheduleForm) {
          this.$refs.jobScheduleForm.clearValidate()
        }
      })
    },

    /**
     * 保存
     */
    async handleSave() {
      try {
        await this.$refs.jobScheduleForm.validate()

        // 验证调度表达式
        if (this.formData.scheduleExpression && !jobScheduleApi.utils.validateCronExpression(this.formData.scheduleExpression)) {
          this.$message.warning('调度表达式格式不正确')
          return
        }

        this.saving = true

        let response
        if (this.isCreate) {
          response = await jobScheduleApi.createJobSchedule(this.formData)
        } else {
          response = await jobScheduleApi.updateJobSchedule(this.jobId, this.formData)
        }

        if (response.success) {
          this.$message.success(this.isCreate ? '创建成功' : '更新成功')
          this.handleClose()
          this.$emit('refresh')
        }
      } catch (error) {
        if (error.message) {
          this.$message.error('保存失败: ' + error.message)
        }
      } finally {
        this.saving = false
      }
    },

    /**
     * 关闭对话框
     */
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    },

    /**
     * 计算成功率
     */
    calculateSuccessRate(successCount, totalCount) {
      return jobScheduleApi.utils.calculateSuccessRate(successCount, totalCount)
    },

    /**
     * 格式化持续时间
     */
    formatDuration(seconds) {
      return jobScheduleApi.utils.formatDuration(seconds)
    }
  }
}
</script>

<style scoped>
.job-schedule-detail {
  max-height: 600px;
  overflow-y: auto;
}

.form-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.dialog-footer {
  text-align: right;
}

.el-tabs--border-card {
  border: 1px solid #dcdfe6;
}

.el-tab-pane {
  padding: 20px;
}
</style>
</script>
