<template>
  <div class="digital-employee-detail">
    <div class="detail-header">
      <div class="header-left">
        <el-button @click="goBack" icon="el-icon-arrow-left">返回</el-button>
        <h2>{{ form.robotName || '数字员工详情' }}</h2>
      </div>
      <div class="header-right">
        <el-button v-if="!isView" type="primary" @click="handleSave" :loading="saving">保存</el-button>
        <el-button v-if="isView" type="primary" @click="handleEdit">编辑</el-button>
        <el-dropdown @command="handleCommand" style="margin-left: 10px">
          <el-button type="info">
            操作<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-if="form.robotStatus === 'INACTIVE'" command="activate">激活</el-dropdown-item>
            <el-dropdown-item v-if="form.robotStatus === 'ACTIVE'" command="deactivate">停用</el-dropdown-item>
            <el-dropdown-item command="healthCheck">健康检查</el-dropdown-item>
            <el-dropdown-item command="monitor">监控</el-dropdown-item>
            <el-dropdown-item command="performance">性能评估</el-dropdown-item>
            <el-dropdown-item command="maintenance">维护</el-dropdown-item>
            <el-dropdown-item command="deploy">部署</el-dropdown-item>
            <el-dropdown-item command="clone">克隆</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <div class="detail-content" v-loading="loading">
      <el-tabs v-model="activeTab" type="card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form :model="form" :rules="rules" ref="basicForm" label-width="120px" class="detail-form">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="数字员工编码" prop="robotCode">
                  <el-input 
                    v-model="form.robotCode" 
                    :disabled="isView || isEdit"
                    placeholder="请输入数字员工编码"
                    @blur="checkCodeExists"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="数字员工名称" prop="robotName">
                  <el-input 
                    v-model="form.robotName" 
                    :disabled="isView"
                    placeholder="请输入数字员工名称"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="类型" prop="robotType">
                  <el-select v-model="form.robotType" :disabled="isView" placeholder="请选择类型" style="width: 100%">
                    <el-option label="RPA-机器人流程自动化" value="RPA" />
                    <el-option label="AI-人工智能" value="AI" />
                    <el-option label="CHATBOT-聊天机器人" value="CHATBOT" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="分类" prop="robotCategory">
                  <el-select v-model="form.robotCategory" :disabled="isView" placeholder="请选择分类" style="width: 100%">
                    <el-option label="DATA_ENTRY-数据录入" value="DATA_ENTRY" />
                    <el-option label="DOCUMENT_PROCESSING-文档处理" value="DOCUMENT_PROCESSING" />
                    <el-option label="CUSTOMER_SERVICE-客户服务" value="CUSTOMER_SERVICE" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="状态">
                  <el-tag :type="getStatusColor(form.robotStatus)" size="medium">
                    {{ formatRobotStatus(form.robotStatus) }}
                  </el-tag>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="健康状态">
                  <el-tag :type="getHealthStatusColor(form.healthStatus)" size="medium">
                    {{ formatHealthStatus(form.healthStatus) }}
                  </el-tag>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="优先级" prop="priority">
                  <el-input-number 
                    v-model="form.priority" 
                    :disabled="isView"
                    :min="1" 
                    :max="10" 
                    placeholder="1-10，数字越小优先级越高"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="版本号" prop="version">
                  <el-input 
                    v-model="form.version" 
                    :disabled="isView"
                    placeholder="请输入版本号"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="所属部门" prop="departmentName">
                  <el-input 
                    v-model="form.departmentName" 
                    :disabled="isView"
                    placeholder="请输入所属部门"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="负责人" prop="ownerName">
                  <el-input 
                    v-model="form.ownerName" 
                    :disabled="isView"
                    placeholder="请输入负责人"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="开发者" prop="developerName">
                  <el-input 
                    v-model="form.developerName" 
                    :disabled="isView"
                    placeholder="请输入开发者"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="标签" prop="tags">
                  <el-input 
                    v-model="form.tags" 
                    :disabled="isView"
                    placeholder="多个标签用逗号分隔"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="描述" prop="description">
              <el-input 
                v-model="form.description" 
                :disabled="isView"
                type="textarea" 
                :rows="3"
                placeholder="请输入描述"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 配置信息 -->
        <el-tab-pane label="配置信息" name="config">
          <el-form :model="form" ref="configForm" label-width="120px" class="detail-form">
            <el-form-item label="能力描述">
              <el-input 
                v-model="form.capabilities" 
                :disabled="isView"
                type="textarea" 
                :rows="4"
                placeholder="JSON格式的能力描述"
              />
            </el-form-item>
            <el-form-item label="自动化流程">
              <el-input 
                v-model="form.automationProcesses" 
                :disabled="isView"
                type="textarea" 
                :rows="4"
                placeholder="JSON格式的自动化流程配置"
              />
            </el-form-item>
            <el-form-item label="决策规则">
              <el-input 
                v-model="form.decisionRules" 
                :disabled="isView"
                type="textarea" 
                :rows="4"
                placeholder="JSON格式的决策规则"
              />
            </el-form-item>
            <el-form-item label="异常处理">
              <el-input 
                v-model="form.exceptionHandling" 
                :disabled="isView"
                type="textarea" 
                :rows="4"
                placeholder="JSON格式的异常处理配置"
              />
            </el-form-item>
            <el-form-item label="学习配置">
              <el-input 
                v-model="form.learningConfig" 
                :disabled="isView"
                type="textarea" 
                :rows="4"
                placeholder="JSON格式的学习配置"
              />
            </el-form-item>
            <el-form-item label="集成配置">
              <el-input 
                v-model="form.integrationConfig" 
                :disabled="isView"
                type="textarea" 
                :rows="4"
                placeholder="JSON格式的集成配置"
              />
            </el-form-item>
            <el-form-item label="安全配置">
              <el-input 
                v-model="form.securityConfig" 
                :disabled="isView"
                type="textarea" 
                :rows="4"
                placeholder="JSON格式的安全配置"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 性能指标 -->
        <el-tab-pane label="性能指标" name="performance">
          <div class="performance-metrics">
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.executionCount || 0 }}</div>
                  <div class="metric-label">执行次数</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.successCount || 0 }}</div>
                  <div class="metric-label">成功次数</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.failureCount || 0 }}</div>
                  <div class="metric-label">失败次数</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.successRate || 0 }}%</div>
                  <div class="metric-label">成功率</div>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px">
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.avgExecutionTime || 0 }}s</div>
                  <div class="metric-label">平均执行时间</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.cpuUsage || 0 }}%</div>
                  <div class="metric-label">CPU使用率</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.memoryUsage || 0 }}%</div>
                  <div class="metric-label">内存使用率</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.responseTime || 0 }}ms</div>
                  <div class="metric-label">响应时间</div>
                </div>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top: 20px">
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.capabilityScore || 0 }}</div>
                  <div class="metric-label">处理能力评分</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.stabilityScore || 0 }}</div>
                  <div class="metric-label">稳定性评分</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.securityScore || 0 }}</div>
                  <div class="metric-label">安全性评分</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="metric-card">
                  <div class="metric-value">{{ form.overallScore || 0 }}</div>
                  <div class="metric-label">综合评分</div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <!-- 执行历史 -->
        <el-tab-pane label="执行历史" name="history">
          <div class="execution-history">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="最后执行时间">
                  <span>{{ formatDateTime(form.lastExecutionTime) }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="下次执行时间">
                  <span>{{ formatDateTime(form.nextExecutionTime) }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="激活时间">
                  <span>{{ formatDateTime(form.activationTime) }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="停用时间">
                  <span>{{ formatDateTime(form.deactivationTime) }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="最后健康检查时间">
                  <span>{{ formatDateTime(form.lastHealthCheckTime) }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="最后错误时间">
                  <span>{{ formatDateTime(form.lastErrorTime) }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="健康检查结果">
              <el-input 
                v-model="form.healthCheckResult" 
                type="textarea" 
                :rows="3"
                readonly
              />
            </el-form-item>
            <el-form-item label="最后错误信息">
              <el-input 
                v-model="form.lastErrorMessage" 
                type="textarea" 
                :rows="3"
                readonly
              />
            </el-form-item>
          </div>
        </el-tab-pane>

        <!-- 其他信息 -->
        <el-tab-pane label="其他信息" name="other">
          <el-form :model="form" ref="otherForm" label-width="120px" class="detail-form">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="创建时间">
                  <span>{{ formatDateTime(form.createdTime) }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="更新时间">
                  <span>{{ formatDateTime(form.updatedTime) }}</span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="备注" prop="remarks">
              <el-input 
                v-model="form.remarks" 
                :disabled="isView"
                type="textarea" 
                :rows="4"
                placeholder="请输入备注"
              />
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="扩展字段1" prop="extField1">
                  <el-input 
                    v-model="form.extField1" 
                    :disabled="isView"
                    placeholder="扩展字段1"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="扩展字段2" prop="extField2">
                  <el-input 
                    v-model="form.extField2" 
                    :disabled="isView"
                    placeholder="扩展字段2"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="扩展字段3" prop="extField3">
                  <el-input 
                    v-model="form.extField3" 
                    :disabled="isView"
                    placeholder="扩展字段3"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="扩展字段4" prop="extField4">
                  <el-input 
                    v-model="form.extField4" 
                    :disabled="isView"
                    placeholder="扩展字段4"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="扩展字段5" prop="extField5">
              <el-input 
                v-model="form.extField5" 
                :disabled="isView"
                placeholder="扩展字段5"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {
  getDigitalEmployeeById,
  createDigitalEmployee,
  updateDigitalEmployee,
  activateDigitalEmployee,
  deactivateDigitalEmployee,
  performHealthCheck,
  checkCodeExists,
  formatRobotStatus,
  formatRobotType,
  formatRobotCategory,
  formatHealthStatus,
  getStatusColor,
  getHealthStatusColor
} from '@/api/managementAccountant/ss/digitalEmployee'

export default {
  name: 'DigitalEmployeeDetail',
  data() {
    return {
      loading: false,
      saving: false,
      activeTab: 'basic',
      form: {
        robotId: null,
        robotCode: '',
        robotName: '',
        robotType: '',
        robotCategory: '',
        robotStatus: 'INACTIVE',
        deploymentStatus: 'DEVELOPMENT',
        priority: 5,
        capabilities: '',
        automationProcesses: '',
        decisionRules: '',
        exceptionHandling: '',
        performanceMetrics: '',
        learningConfig: '',
        integrationConfig: '',
        securityConfig: '',
        workSchedule: '',
        taskQueueConfig: '',
        monitoringConfig: '',
        notificationConfig: '',
        version: '',
        description: '',
        tags: '',
        departmentId: null,
        departmentName: '',
        ownerId: null,
        ownerName: '',
        developerId: null,
        developerName: '',
        lastExecutionTime: null,
        nextExecutionTime: null,
        executionCount: 0,
        successCount: 0,
        failureCount: 0,
        successRate: 0,
        avgExecutionTime: 0,
        maxExecutionTime: 0,
        minExecutionTime: 0,
        cpuUsage: 0,
        memoryUsage: 0,
        diskUsage: 0,
        networkUsage: 0,
        errorRate: 0,
        availability: 0,
        responseTime: 0,
        throughput: 0,
        concurrency: 0,
        queueLength: 0,
        capabilityScore: 0,
        learningScore: 0,
        adaptabilityScore: 0,
        stabilityScore: 0,
        securityScore: 0,
        overallScore: 0,
        activationTime: null,
        deactivationTime: null,
        maintenanceStartTime: null,
        maintenanceEndTime: null,
        lastHealthCheckTime: null,
        healthStatus: 'UNKNOWN',
        healthCheckResult: '',
        lastErrorMessage: '',
        lastErrorTime: null,
        remarks: '',
        extField1: '',
        extField2: '',
        extField3: '',
        extField4: '',
        extField5: '',
        tenantId: null,
        createdBy: null,
        createdTime: null,
        updatedBy: null,
        updatedTime: null,
        isDeleted: 0
      },
      rules: {
        robotCode: [
          { required: true, message: '请输入数字员工编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        robotName: [
          { required: true, message: '请输入数字员工名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        robotType: [
          { required: true, message: '请选择数字员工类型', trigger: 'change' }
        ],
        robotCategory: [
          { required: true, message: '请选择数字员工分类', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    robotId() {
      return this.$route.params.robotId
    },
    mode() {
      return this.$route.params.mode || 'view'
    },
    isView() {
      return this.mode === 'view'
    },
    isEdit() {
      return this.mode === 'edit'
    },
    isCreate() {
      return this.mode === 'create'
    },
    tenantId() {
      return this.$store.getters.tenantId || 1
    }
  },
  created() {
    if (this.robotId && this.robotId !== 'create') {
      this.loadData()
    } else {
      this.form.tenantId = this.tenantId
    }
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const response = await getDigitalEmployeeById(this.robotId, this.tenantId)
        if (response.success && response.data) {
          this.form = { ...this.form, ...response.data }
        }
      } catch (error) {
        this.$message.error('加载数据失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 保存
    async handleSave() {
      try {
        // 验证表单
        await this.$refs.basicForm.validate()
        
        this.saving = true
        this.form.tenantId = this.tenantId

        let response
        if (this.isCreate) {
          response = await createDigitalEmployee(this.form, this.tenantId)
        } else {
          response = await updateDigitalEmployee(this.form, this.tenantId)
        }

        if (response.success) {
          this.$message.success(this.isCreate ? '创建成功' : '保存成功')
          if (this.isCreate) {
            this.$router.replace(`/managementAccountant/ss/digitalEmployee/detail/${response.data.robotId}`)
          } else {
            this.loadData()
          }
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error((this.isCreate ? '创建' : '保存') + '失败: ' + error.message)
        }
      } finally {
        this.saving = false
      }
    },

    // 编辑
    handleEdit() {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/edit/${this.robotId}`)
    },

    // 返回
    goBack() {
      this.$router.go(-1)
    },

    // 操作命令
    async handleCommand(command) {
      switch (command) {
        case 'activate':
          await this.handleActivate()
          break
        case 'deactivate':
          await this.handleDeactivate()
          break
        case 'healthCheck':
          await this.handleHealthCheck()
          break
        case 'monitor':
          this.handleMonitor()
          break
        case 'performance':
          this.handlePerformance()
          break
        case 'maintenance':
          this.handleMaintenance()
          break
        case 'deploy':
          this.handleDeploy()
          break
        case 'clone':
          this.handleClone()
          break
      }
    },

    // 激活
    async handleActivate() {
      try {
        const response = await activateDigitalEmployee(this.robotId, this.tenantId)
        if (response.success) {
          this.$message.success('激活成功')
          this.loadData()
        }
      } catch (error) {
        this.$message.error('激活失败: ' + error.message)
      }
    },

    // 停用
    async handleDeactivate() {
      try {
        const reason = await this.$prompt('请输入停用原因', '停用数字员工', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '停用原因不能为空'
        })

        const response = await deactivateDigitalEmployee(this.robotId, reason.value, this.tenantId)
        if (response.success) {
          this.$message.success('停用成功')
          this.loadData()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('停用失败: ' + error.message)
        }
      }
    },

    // 健康检查
    async handleHealthCheck() {
      try {
        this.$message.info('正在执行健康检查...')
        const response = await performHealthCheck(this.robotId, this.tenantId)
        if (response.success) {
          this.$message.success('健康检查完成')
          this.loadData()
        }
      } catch (error) {
        this.$message.error('健康检查失败: ' + error.message)
      }
    },

    // 监控
    handleMonitor() {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/monitor/${this.robotId}`)
    },

    // 性能评估
    handlePerformance() {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/performance/${this.robotId}`)
    },

    // 维护
    handleMaintenance() {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/maintenance/${this.robotId}`)
    },

    // 部署
    handleDeploy() {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/deploy/${this.robotId}`)
    },

    // 克隆
    handleClone() {
      this.$router.push(`/managementAccountant/ss/digitalEmployee/clone/${this.robotId}`)
    },

    // 检查编码是否存在
    async checkCodeExists() {
      if (!this.form.robotCode || this.isView) return

      try {
        const response = await checkCodeExists(this.form.robotCode, this.form.robotId, this.tenantId)
        if (response.success && response.data) {
          this.$message.warning('该编码已存在，请使用其他编码')
          this.$refs.basicForm.validateField('robotCode')
        }
      } catch (error) {
        console.error('检查编码失败:', error)
      }
    },

    // 格式化方法
    formatRobotStatus,
    formatRobotType,
    formatRobotCategory,
    formatHealthStatus,
    getStatusColor,
    getHealthStatusColor,

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString()
    }
  }
}
</script>

<style scoped>
.digital-employee-detail {
  padding: 20px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-left h2 {
  margin: 0;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
}

.detail-content {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  min-height: 600px;
}

.detail-form {
  padding: 20px;
}

.performance-metrics {
  padding: 20px;
}

.metric-card {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}

.metric-label {
  font-size: 14px;
  color: #666;
}

.execution-history {
  padding: 20px;
}

.execution-history .el-form-item {
  margin-bottom: 20px;
}
</style>
