<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseId">
            <CompanyTreeModal
              v-model="form.enterpriseId"
              :company-name.sync="form.enterpriseName"
              placeholder="请选择企业"
              :disabled="dialogType === 'edit'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="监控名称" prop="monitoringName">
            <el-input v-model="form.monitoringName" placeholder="请输入监控名称" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="监控类型" prop="monitoringType">
            <el-select v-model="form.monitoringType" placeholder="请选择监控类型">
              <el-option label="实时监控" value="REAL_TIME" />
              <el-option label="定期监控" value="PERIODIC" />
              <el-option label="事件驱动" value="EVENT_DRIVEN" />
              <el-option label="阈值监控" value="THRESHOLD" />
              <el-option label="趋势监控" value="TREND" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="监控指标" prop="monitoringIndicator">
            <el-input v-model="form.monitoringIndicator" placeholder="请输入监控指标" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据源类型" prop="dataSourceType">
            <el-select v-model="form.dataSourceType" placeholder="请选择数据源类型">
              <el-option label="数据库" value="DATABASE" />
              <el-option label="API接口" value="API" />
              <el-option label="文件系统" value="FILE" />
              <el-option label="消息队列" value="MESSAGE_QUEUE" />
              <el-option label="外部系统" value="EXTERNAL_SYSTEM" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="监控频率" prop="monitoringFrequency">
            <el-select v-model="form.monitoringFrequency" placeholder="请选择监控频率">
              <el-option label="实时" value="REAL_TIME" />
              <el-option label="每分钟" value="EVERY_MINUTE" />
              <el-option label="每5分钟" value="EVERY_5_MINUTES" />
              <el-option label="每15分钟" value="EVERY_15_MINUTES" />
              <el-option label="每小时" value="HOURLY" />
              <el-option label="每日" value="DAILY" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="数据源配置" prop="dataSourceConfig">
        <el-input
          v-model="form.dataSourceConfig"
          type="textarea"
          :rows="3"
          placeholder="请输入数据源配置信息（JSON格式）"
        />
      </el-form-item>

      <!-- 阈值设置 -->
      <el-divider content-position="left">阈值设置</el-divider>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预警阈值" prop="warningThreshold">
            <el-input-number
              v-model="form.warningThreshold"
              :precision="2"
              placeholder="预警阈值"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="危险阈值" prop="dangerThreshold">
            <el-input-number
              v-model="form.dangerThreshold"
              :precision="2"
              placeholder="危险阈值"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="阈值比较方式" prop="thresholdComparison">
            <el-select v-model="form.thresholdComparison" placeholder="请选择比较方式">
              <el-option label="大于等于" value="GREATER_THAN_OR_EQUAL" />
              <el-option label="大于" value="GREATER_THAN" />
              <el-option label="小于等于" value="LESS_THAN_OR_EQUAL" />
              <el-option label="小于" value="LESS_THAN" />
              <el-option label="等于" value="EQUAL" />
              <el-option label="不等于" value="NOT_EQUAL" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位" prop="unit">
            <el-input v-model="form.unit" placeholder="请输入单位" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 预警配置 -->
      <el-divider content-position="left">预警配置</el-divider>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="启用预警" prop="alertEnabled">
            <el-switch
              v-model="form.alertEnabled"
              active-text="启用"
              inactive-text="禁用"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预警方式" prop="alertMethod">
            <el-select v-model="form.alertMethod" placeholder="请选择预警方式" multiple>
              <el-option label="系统通知" value="SYSTEM_NOTIFICATION" />
              <el-option label="邮件通知" value="EMAIL" />
              <el-option label="短信通知" value="SMS" />
              <el-option label="微信通知" value="WECHAT" />
              <el-option label="钉钉通知" value="DINGTALK" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="预警接收人" prop="alertRecipients">
        <el-input
          v-model="form.alertRecipients"
          placeholder="请输入预警接收人，多个用分号分隔"
        />
      </el-form-item>

      <el-form-item label="预警消息模板" prop="alertMessageTemplate">
        <el-input
          v-model="form.alertMessageTemplate"
          type="textarea"
          :rows="3"
          placeholder="请输入预警消息模板，支持变量：{enterpriseName}, {indicatorName}, {currentValue}, {threshold}"
        />
      </el-form-item>

      <!-- 异常检测配置 -->
      <el-divider content-position="left">异常检测配置</el-divider>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="启用异常检测" prop="anomalyDetectionEnabled">
            <el-switch
              v-model="form.anomalyDetectionEnabled"
              active-text="启用"
              inactive-text="禁用"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="检测算法" prop="anomalyDetectionAlgorithm">
            <el-select v-model="form.anomalyDetectionAlgorithm" placeholder="请选择检测算法">
              <el-option label="统计方法" value="STATISTICAL" />
              <el-option label="移动平均" value="MOVING_AVERAGE" />
              <el-option label="标准差" value="STANDARD_DEVIATION" />
              <el-option label="机器学习" value="MACHINE_LEARNING" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="历史数据窗口" prop="historicalDataWindow">
            <el-input-number
              v-model="form.historicalDataWindow"
              :min="1"
              placeholder="天数"
              style="width: 100%"
            />
            <span class="input-suffix">天</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="异常敏感度" prop="anomalySensitivity">
            <el-select v-model="form.anomalySensitivity" placeholder="请选择敏感度">
              <el-option label="低" value="LOW" />
              <el-option label="中" value="MEDIUM" />
              <el-option label="高" value="HIGH" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="监控描述" prop="monitoringDescription">
        <el-input
          v-model="form.monitoringDescription"
          type="textarea"
          :rows="3"
          placeholder="请输入监控描述和说明"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        {{ dialogType === 'add' ? '新增' : '更新' }}
      </el-button>
      <el-button v-if="dialogType === 'add'" type="success" @click="handleSubmitAndStart" :loading="loading">
        保存并启动监控
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addRiskMonitoring, updateRiskMonitoring, startRealTimeMonitoring } from '@/api/stateAssets/riskMonitoring'
import CompanyTreeModal from '@/components/CompanyTreeModal'

export default {
  name: 'RiskMonitoringDialog',
  components: {
    CompanyTreeModal
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit
    },
    formData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        enterpriseId: '',
        enterpriseName: '',
        monitoringName: '',
        monitoringType: '',
        monitoringIndicator: '',
        dataSourceType: '',
        dataSourceConfig: '',
        monitoringFrequency: '',
        warningThreshold: null,
        dangerThreshold: null,
        thresholdComparison: 'GREATER_THAN_OR_EQUAL',
        unit: '',
        alertEnabled: true,
        alertMethod: [],
        alertRecipients: '',
        alertMessageTemplate: '',
        anomalyDetectionEnabled: false,
        anomalyDetectionAlgorithm: '',
        historicalDataWindow: 30,
        anomalySensitivity: 'MEDIUM',
        monitoringDescription: ''
      },
      rules: {
        enterpriseId: [
          { required: true, message: '请选择企业', trigger: 'change' }
        ],
        monitoringName: [
          { required: true, message: '请输入监控名称', trigger: 'blur' }
        ],
        monitoringType: [
          { required: true, message: '请选择监控类型', trigger: 'change' }
        ],
        monitoringIndicator: [
          { required: true, message: '请输入监控指标', trigger: 'blur' }
        ],
        dataSourceType: [
          { required: true, message: '请选择数据源类型', trigger: 'change' }
        ],
        monitoringFrequency: [
          { required: true, message: '请选择监控频率', trigger: 'change' }
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
      return this.dialogType === 'add' ? '新增风险监控' : '编辑风险监控'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      if (this.dialogType === 'edit' && this.formData) {
        this.form = { ...this.formData }
        // 处理数组字段
        if (typeof this.form.alertMethod === 'string') {
          this.form.alertMethod = this.form.alertMethod ? this.form.alertMethod.split(',') : []
        }
      } else {
        this.resetForm()
        // 设置默认值
        this.form.alertMessageTemplate = '【风险预警】企业：{enterpriseName}，指标：{indicatorName}，当前值：{currentValue}，阈值：{threshold}，请及时关注！'
      }
    },

    // 重置表单
    resetForm() {
      this.form = {
        enterpriseId: '',
        enterpriseName: '',
        monitoringName: '',
        monitoringType: '',
        monitoringIndicator: '',
        dataSourceType: '',
        dataSourceConfig: '',
        monitoringFrequency: '',
        warningThreshold: null,
        dangerThreshold: null,
        thresholdComparison: 'GREATER_THAN_OR_EQUAL',
        unit: '',
        alertEnabled: true,
        alertMethod: [],
        alertRecipients: '',
        alertMessageTemplate: '',
        anomalyDetectionEnabled: false,
        anomalyDetectionAlgorithm: '',
        historicalDataWindow: 30,
        anomalySensitivity: 'MEDIUM',
        monitoringDescription: ''
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },

    // 提交表单
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.loading = true

        const formData = { ...this.form }
        
        // 处理数组字段
        if (Array.isArray(formData.alertMethod)) {
          formData.alertMethod = formData.alertMethod.join(',')
        }
        
        let response
        if (this.dialogType === 'add') {
          response = await addRiskMonitoring(formData)
        } else {
          response = await updateRiskMonitoring(formData)
        }

        if (response.code === 200) {
          this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('操作失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 保存并启动监控
    async handleSubmitAndStart() {
      try {
        await this.$refs.form.validate()
        this.loading = true

        const formData = { ...this.form }
        
        // 处理数组字段
        if (Array.isArray(formData.alertMethod)) {
          formData.alertMethod = formData.alertMethod.join(',')
        }
        
        // 先保存
        const saveResponse = await addRiskMonitoring(formData)
        if (saveResponse.code === 200) {
          // 再启动监控
          const monitoringId = saveResponse.data.riskMonitoringId
          const startResponse = await startRealTimeMonitoring({
            riskMonitoringId: monitoringId,
            startBy: this.$store.getters.userInfo.userName
          })
          
          if (startResponse.code === 200) {
            this.$message.success('保存成功并已启动监控')
            this.$emit('success')
            this.handleClose()
          }
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('操作失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}

.el-divider {
  margin: 20px 0;
}

.el-form {
  .el-form-item {
    margin-bottom: 18px;
  }
}

.input-suffix {
  margin-left: 8px;
  color: #909399;
  font-size: 14px;
}
</style>
