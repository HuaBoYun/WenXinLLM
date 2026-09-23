<template>
  <el-dialog
    title="阈值配置"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <div class="threshold-config">
      <!-- 监控信息 -->
      <div class="monitoring-info">
        <h4>监控信息</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>企业名称：</label>
              <span>{{ monitoringData.enterpriseName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>监控名称：</label>
              <span>{{ monitoringData.monitoringName || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <label>监控指标：</label>
              <span>{{ monitoringData.monitoringIndicator || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <label>当前值：</label>
              <span class="current-value" :class="getCurrentValueClass()">
                {{ monitoringData.currentValue || '-' }}
              </span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 阈值配置表单 -->
      <div class="threshold-form">
        <h4>阈值设置</h4>
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          label-width="120px"
          v-loading="loading"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="预警阈值" prop="warningThreshold">
                <el-input-number
                  v-model="form.warningThreshold"
                  :precision="2"
                  placeholder="预警阈值"
                  style="width: 100%"
                  @change="handleThresholdChange"
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
                  @change="handleThresholdChange"
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

          <el-form-item label="阈值说明" prop="thresholdDescription">
            <el-input
              v-model="form.thresholdDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入阈值设置说明"
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 阈值可视化 -->
      <div class="threshold-visualization">
        <h4>阈值可视化</h4>
        <div class="threshold-chart">
          <div class="chart-container">
            <div class="threshold-bar">
              <div class="bar-section normal" :style="getNormalSectionStyle()">
                <span class="section-label">正常区间</span>
                <span class="section-range">{{ getNormalRange() }}</span>
              </div>
              <div class="bar-section warning" :style="getWarningSectionStyle()">
                <span class="section-label">预警区间</span>
                <span class="section-range">{{ getWarningRange() }}</span>
              </div>
              <div class="bar-section danger" :style="getDangerSectionStyle()">
                <span class="section-label">危险区间</span>
                <span class="section-range">{{ getDangerRange() }}</span>
              </div>
            </div>
            
            <!-- 当前值标记 -->
            <div v-if="monitoringData.currentValue" class="current-marker" :style="getCurrentMarkerStyle()">
              <div class="marker-line"></div>
              <div class="marker-label">
                <span>当前值</span>
                <div class="marker-value">{{ monitoringData.currentValue }}</div>
              </div>
            </div>
          </div>

          <!-- 阈值数值显示 -->
          <div class="threshold-values">
            <div class="value-item normal">
              <span class="value-label">正常范围</span>
              <span class="value-range">{{ getNormalRange() }}</span>
            </div>
            <div class="value-item warning">
              <span class="value-label">预警范围</span>
              <span class="value-range">{{ getWarningRange() }}</span>
            </div>
            <div class="value-item danger">
              <span class="value-label">危险范围</span>
              <span class="value-range">{{ getDangerRange() }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 预警配置 -->
      <div class="alert-config">
        <h4>预警配置</h4>
        <el-form
          ref="alertForm"
          :model="alertForm"
          label-width="120px"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="启用预警">
                <el-switch
                  v-model="alertForm.alertEnabled"
                  active-text="启用"
                  inactive-text="禁用"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预警延迟">
                <el-input-number
                  v-model="alertForm.alertDelay"
                  :min="0"
                  placeholder="秒"
                  style="width: 100%"
                />
                <span class="input-suffix">秒</span>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="预警方式">
            <el-checkbox-group v-model="alertForm.alertMethods">
              <el-checkbox label="SYSTEM_NOTIFICATION">系统通知</el-checkbox>
              <el-checkbox label="EMAIL">邮件通知</el-checkbox>
              <el-checkbox label="SMS">短信通知</el-checkbox>
              <el-checkbox label="WECHAT">微信通知</el-checkbox>
              <el-checkbox label="DINGTALK">钉钉通知</el-checkbox>
            </el-checkbox-group>
          </el-form-item>

          <el-form-item label="预警接收人">
            <el-input
              v-model="alertForm.alertRecipients"
              placeholder="请输入预警接收人，多个用分号分隔"
            />
          </el-form-item>

          <el-form-item label="预警频率限制">
            <el-select v-model="alertForm.alertFrequencyLimit" placeholder="请选择预警频率">
              <el-option label="无限制" value="UNLIMITED" />
              <el-option label="每分钟最多1次" value="ONCE_PER_MINUTE" />
              <el-option label="每5分钟最多1次" value="ONCE_PER_5_MINUTES" />
              <el-option label="每15分钟最多1次" value="ONCE_PER_15_MINUTES" />
              <el-option label="每小时最多1次" value="ONCE_PER_HOUR" />
              <el-option label="每天最多1次" value="ONCE_PER_DAY" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>

      <!-- 历史阈值记录 -->
      <div class="threshold-history">
        <h4>历史阈值记录</h4>
        <el-table :data="thresholdHistory" border stripe size="small">
          <el-table-column prop="changeTime" label="变更时间" width="150" />
          <el-table-column prop="warningThreshold" label="预警阈值" width="100" />
          <el-table-column prop="dangerThreshold" label="危险阈值" width="100" />
          <el-table-column prop="changeReason" label="变更原因" show-overflow-tooltip />
          <el-table-column prop="changeBy" label="变更人" width="100" />
        </el-table>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">
        保存配置
      </el-button>
      <el-button type="success" @click="handleTestAlert" :loading="loading">
        测试预警
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { setMonitoringThresholds, triggerAlert } from '@/api/stateAssets/riskMonitoring'

export default {
  name: 'ThresholdConfigDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    monitoringData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      form: {
        warningThreshold: null,
        dangerThreshold: null,
        thresholdComparison: 'GREATER_THAN_OR_EQUAL',
        unit: '',
        thresholdDescription: ''
      },
      alertForm: {
        alertEnabled: true,
        alertDelay: 0,
        alertMethods: [],
        alertRecipients: '',
        alertFrequencyLimit: 'UNLIMITED'
      },
      rules: {
        warningThreshold: [
          { required: true, message: '请输入预警阈值', trigger: 'blur' }
        ],
        dangerThreshold: [
          { required: true, message: '请输入危险阈值', trigger: 'blur' }
        ],
        thresholdComparison: [
          { required: true, message: '请选择阈值比较方式', trigger: 'change' }
        ]
      },
      thresholdHistory: []
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
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
        this.loadThresholdHistory()
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      if (this.monitoringData) {
        this.form = {
          warningThreshold: this.monitoringData.warningThreshold,
          dangerThreshold: this.monitoringData.dangerThreshold,
          thresholdComparison: this.monitoringData.thresholdComparison || 'GREATER_THAN_OR_EQUAL',
          unit: this.monitoringData.unit || '',
          thresholdDescription: ''
        }

        this.alertForm = {
          alertEnabled: this.monitoringData.alertEnabled !== false,
          alertDelay: this.monitoringData.alertDelay || 0,
          alertMethods: this.getAlertMethodsArray(this.monitoringData.alertMethod),
          alertRecipients: this.monitoringData.alertRecipients || '',
          alertFrequencyLimit: this.monitoringData.alertFrequencyLimit || 'UNLIMITED'
        }
      }
    },

    // 获取预警方式数组
    getAlertMethodsArray(alertMethod) {
      if (!alertMethod) return []
      return typeof alertMethod === 'string' ? alertMethod.split(',') : alertMethod
    },

    // 加载历史阈值记录
    loadThresholdHistory() {
      // 模拟历史数据
      this.thresholdHistory = [
        {
          changeTime: '2024-01-15 10:30:00',
          warningThreshold: 70,
          dangerThreshold: 85,
          changeReason: '根据最新风险评估调整',
          changeBy: '张三'
        },
        {
          changeTime: '2024-01-10 14:20:00',
          warningThreshold: 75,
          dangerThreshold: 90,
          changeReason: '初始配置',
          changeBy: '李四'
        }
      ]
    },

    // 阈值变化处理
    handleThresholdChange() {
      // 验证阈值逻辑
      if (this.form.warningThreshold && this.form.dangerThreshold) {
        if (this.form.thresholdComparison === 'GREATER_THAN_OR_EQUAL' || this.form.thresholdComparison === 'GREATER_THAN') {
          if (this.form.warningThreshold >= this.form.dangerThreshold) {
            this.$message.warning('预警阈值应小于危险阈值')
          }
        } else if (this.form.thresholdComparison === 'LESS_THAN_OR_EQUAL' || this.form.thresholdComparison === 'LESS_THAN') {
          if (this.form.warningThreshold <= this.form.dangerThreshold) {
            this.$message.warning('预警阈值应大于危险阈值')
          }
        }
      }
    },

    // 提交配置
    async handleSubmit() {
      try {
        await this.$refs.form.validate()
        this.loading = true

        const configData = {
          riskMonitoringId: this.monitoringData.riskMonitoringId,
          ...this.form,
          ...this.alertForm,
          alertMethod: this.alertForm.alertMethods.join(','),
          updateBy: this.$store.getters.userInfo.userName,
          changeReason: this.form.thresholdDescription || '阈值配置更新'
        }

        const response = await setMonitoringThresholds(configData)
        
        if (response.code === 200) {
          this.$message.success('阈值配置保存成功')
          this.$emit('success')
          this.handleClose()
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('保存配置失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 测试预警
    async handleTestAlert() {
      try {
        this.loading = true

        const testData = {
          riskMonitoringId: this.monitoringData.riskMonitoringId,
          alertLevel: 'WARNING',
          alertMessage: '这是一条测试预警消息',
          testMode: true
        }

        const response = await triggerAlert(testData)
        
        if (response.code === 200) {
          this.$message.success('测试预警发送成功')
        }
      } catch (error) {
        this.$message.error('测试预警失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.form = {
        warningThreshold: null,
        dangerThreshold: null,
        thresholdComparison: 'GREATER_THAN_OR_EQUAL',
        unit: '',
        thresholdDescription: ''
      }
      this.alertForm = {
        alertEnabled: true,
        alertDelay: 0,
        alertMethods: [],
        alertRecipients: '',
        alertFrequencyLimit: 'UNLIMITED'
      }
      this.thresholdHistory = []
    },

    // 可视化相关方法
    getNormalSectionStyle() {
      const maxValue = this.getMaxValue()
      const warningThreshold = this.form.warningThreshold || 0
      const width = maxValue > 0 ? (warningThreshold / maxValue) * 100 : 33.33
      return { width: `${Math.max(width, 10)}%` }
    },

    getWarningSectionStyle() {
      const maxValue = this.getMaxValue()
      const warningThreshold = this.form.warningThreshold || 0
      const dangerThreshold = this.form.dangerThreshold || 0
      const width = maxValue > 0 ? ((dangerThreshold - warningThreshold) / maxValue) * 100 : 33.33
      return { width: `${Math.max(width, 10)}%` }
    },

    getDangerSectionStyle() {
      const maxValue = this.getMaxValue()
      const dangerThreshold = this.form.dangerThreshold || 0
      const width = maxValue > 0 ? ((maxValue - dangerThreshold) / maxValue) * 100 : 33.33
      return { width: `${Math.max(width, 10)}%` }
    },

    getCurrentMarkerStyle() {
      const maxValue = this.getMaxValue()
      const currentValue = this.monitoringData.currentValue || 0
      const position = maxValue > 0 ? (currentValue / maxValue) * 100 : 0
      return { left: `${Math.min(position, 95)}%` }
    },

    getMaxValue() {
      const dangerThreshold = this.form.dangerThreshold || 0
      const currentValue = this.monitoringData.currentValue || 0
      return Math.max(dangerThreshold * 1.2, currentValue * 1.1, 100)
    },

    getNormalRange() {
      const warningThreshold = this.form.warningThreshold || 0
      return `0 - ${warningThreshold}`
    },

    getWarningRange() {
      const warningThreshold = this.form.warningThreshold || 0
      const dangerThreshold = this.form.dangerThreshold || 0
      return `${warningThreshold} - ${dangerThreshold}`
    },

    getDangerRange() {
      const dangerThreshold = this.form.dangerThreshold || 0
      return `> ${dangerThreshold}`
    },

    getCurrentValueClass() {
      const currentValue = this.monitoringData.currentValue
      const warningThreshold = this.form.warningThreshold
      const dangerThreshold = this.form.dangerThreshold

      if (!currentValue || !dangerThreshold) return ''
      
      if (currentValue >= dangerThreshold) return 'value-danger'
      if (warningThreshold && currentValue >= warningThreshold) return 'value-warning'
      return 'value-normal'
    }
  }
}
</script>

<style lang="scss" scoped>
.threshold-config {
  .monitoring-info,
  .threshold-form,
  .threshold-visualization,
  .alert-config,
  .threshold-history {
    margin-bottom: 24px;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 8px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .info-item {
    margin-bottom: 12px;
    
    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }

    .current-value {
      font-weight: 600;
      
      &.value-danger {
        color: #f56c6c;
      }
      
      &.value-warning {
        color: #e6a23c;
      }
      
      &.value-normal {
        color: #67c23a;
      }
    }
  }

  // 阈值可视化样式
  .threshold-visualization {
    .chart-container {
      position: relative;
      margin-bottom: 20px;

      .threshold-bar {
        display: flex;
        height: 60px;
        border-radius: 8px;
        overflow: hidden;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

        .bar-section {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          color: white;
          font-size: 12px;
          transition: all 0.3s ease;

          &.normal {
            background: linear-gradient(135deg, #67c23a, #85ce61);
          }

          &.warning {
            background: linear-gradient(135deg, #e6a23c, #ebb563);
          }

          &.danger {
            background: linear-gradient(135deg, #f56c6c, #f78989);
          }

          .section-label {
            font-weight: 600;
            margin-bottom: 4px;
          }

          .section-range {
            font-size: 10px;
            opacity: 0.9;
          }
        }
      }

      .current-marker {
        position: absolute;
        top: -15px;
        transform: translateX(-50%);
        z-index: 10;

        .marker-line {
          width: 2px;
          height: 90px;
          background: #303133;
          margin: 0 auto;
        }

        .marker-label {
          position: absolute;
          top: -40px;
          left: 50%;
          transform: translateX(-50%);
          background: #303133;
          color: white;
          padding: 6px 10px;
          border-radius: 4px;
          text-align: center;
          font-size: 12px;
          white-space: nowrap;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);

          &::after {
            content: '';
            position: absolute;
            top: 100%;
            left: 50%;
            transform: translateX(-50%);
            border: 4px solid transparent;
            border-top-color: #303133;
          }

          .marker-value {
            font-weight: 600;
            margin-top: 2px;
          }
        }
      }
    }

    .threshold-values {
      display: flex;
      justify-content: space-between;
      margin-top: 16px;

      .value-item {
        flex: 1;
        text-align: center;
        padding: 12px;
        border-radius: 6px;
        margin: 0 4px;

        &.normal {
          background: rgba(103, 194, 58, 0.1);
          border: 1px solid rgba(103, 194, 58, 0.3);
        }

        &.warning {
          background: rgba(230, 162, 60, 0.1);
          border: 1px solid rgba(230, 162, 60, 0.3);
        }

        &.danger {
          background: rgba(245, 108, 108, 0.1);
          border: 1px solid rgba(245, 108, 108, 0.3);
        }

        .value-label {
          display: block;
          font-size: 12px;
          color: #606266;
          margin-bottom: 4px;
        }

        .value-range {
          font-weight: 600;
          color: #303133;
        }
      }
    }
  }

  .input-suffix {
    margin-left: 8px;
    color: #909399;
    font-size: 14px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
