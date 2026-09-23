<template>
  <el-dialog
    title="财务风险识别"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
  >
    <div class="identification-content">
      <el-steps :active="currentStep" finish-status="success">
        <el-step title="数据收集" description="收集财务数据"></el-step>
        <el-step title="风险识别" description="AI智能识别"></el-step>
        <el-step title="结果确认" description="确认识别结果"></el-step>
      </el-steps>

      <div class="step-content">
        <!-- 步骤1：数据收集 -->
        <div v-if="currentStep === 0" class="step-panel">
          <h4>财务数据收集</h4>
          <el-form :model="identificationForm" label-width="120px">
            <el-form-item label="企业名称">
              <el-input v-model="identificationForm.enterpriseName" disabled />
            </el-form-item>
            <el-form-item label="报告期间">
              <el-date-picker
                v-model="identificationForm.reportPeriod"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                style="width: 100%;"
              />
            </el-form-item>
            <el-form-item label="数据来源">
              <el-checkbox-group v-model="identificationForm.dataSources">
                <el-checkbox label="财务报表">财务报表</el-checkbox>
                <el-checkbox label="现金流量表">现金流量表</el-checkbox>
                <el-checkbox label="资产负债表">资产负债表</el-checkbox>
                <el-checkbox label="利润表">利润表</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form>
        </div>

        <!-- 步骤2：风险识别 -->
        <div v-if="currentStep === 1" class="step-panel">
          <h4>AI智能风险识别</h4>
          <div class="identification-progress">
            <el-progress
              :percentage="identificationProgress"
              :status="identificationStatus"
              :stroke-width="20"
              :indeterminate="identifying"
            />
            <p class="progress-text">{{ identificationText }}</p>
          </div>

          <div v-if="identificationComplete" class="identification-results">
            <h5>识别结果</h5>
            <el-table :data="identificationResults" border>
              <el-table-column label="风险类型" prop="riskType" />
              <el-table-column label="风险等级" prop="riskLevel">
                <template slot-scope="scope">
                  <el-tag :type="getRiskLevelTag(scope.row.riskLevel)" size="mini">
                    {{ getRiskLevelText(scope.row.riskLevel) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="风险评分" prop="riskScore" />
              <el-table-column label="更新时间" prop="updateTime" />
            </el-table>
          </div>
        </div>

        <!-- 步骤3：结果确认 -->
        <div v-if="currentStep === 2" class="step-panel">
          <h4>确认识别结果</h4>
          <el-form :model="confirmationForm" label-width="120px">
            <el-form-item label="确认状态">
              <el-radio-group v-model="confirmationForm.confirmStatus">
                <el-radio label="CONFIRMED">确认无误</el-radio>
                <el-radio label="PARTIAL">部分确认</el-radio>
                <el-radio label="REJECTED">拒绝结果</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="确认备注">
              <el-input
                v-model="confirmationForm.confirmNotes"
                type="textarea"
                :rows="3"
                placeholder="请输入确认备注"
              />
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="currentStep > 0" @click="prevStep">上一步</el-button>
      <el-button v-if="currentStep < 2" type="primary" :disabled="currentStep === 1 && !identificationComplete" @click="nextStep">下一步</el-button>
      <el-button v-if="currentStep === 2" type="primary" @click="handleSubmit">完成识别</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { identifyFinancialRisk } from '@/api/stateAssets/financialRisk'

export default {
  name: 'FinancialRiskIdentificationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    riskData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      currentStep: 0,
      identifying: false,
      identificationComplete: false,
      identificationProgress: 0,
      identificationStatus: '',
      identificationText: '准备开始识别...',
      identificationForm: {
        enterpriseName: '',
        reportPeriod: [],
        dataSources: []
      },
      confirmationForm: {
        confirmStatus: 'CONFIRMED',
        confirmNotes: ''
      },
      identificationResults: []
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
      }
    }
  },
  methods: {
    initForm() {
      this.currentStep = 0
      this.identifying = false
      this.identificationComplete = false
      this.identificationProgress = 0
      this.identificationStatus = ''
      this.identificationText = '准备开始识别...'
      this.identificationForm = {
        enterpriseName: this.riskData.enterpriseName || '',
        reportPeriod: [],
        dataSources: ['财务报表']
      }
      this.confirmationForm = {
        confirmStatus: 'CONFIRMED',
        confirmNotes: ''
      }
      this.identificationResults = []
    },

    nextStep() {
      if (this.currentStep === 0) {
        if (!this.identificationForm.reportPeriod.length) {
          this.$message.warning('请选择报告期间')
          return
        }
        if (!this.identificationForm.dataSources.length) {
          this.$message.warning('请选择数据来源')
          return
        }
        this.currentStep = 1
        this.startIdentification()
      } else if (this.currentStep === 1) {
        if (!this.identificationComplete) {
          this.$message.warning('请等待识别完成')
          return
        }
        this.currentStep = 2
      }
    },

    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },

    async startIdentification() {
      this.identifying = true
      this.identificationComplete = false
      this.identificationProgress = 50
      this.identificationStatus = ''
      this.identificationText = '正在进行风险识别，请稍候...'

      try {
        const res = await identifyFinancialRisk({ id: this.riskData.id })
        if (res.result === 200 || res.code === 1 || res.code === 200) {
          const result = res.data || {}
          this.identificationProgress = 100
          this.identificationStatus = 'success'
          this.identificationText = '识别完成'
          this.identificationResults = [{
            riskType: this.riskData.riskType || '-',
            riskLevel: result.status || this.riskData.riskLevel || '-',
            riskScore: this.riskData.riskScore || '-',
            updateTime: result.updateTime || '-'
          }]
          this.identificationComplete = true
        } else {
          this.identificationProgress = 0
          this.identificationStatus = 'exception'
          this.identificationText = res.msg || '识别失败，请重试'
        }
      } catch (error) {
        this.identificationProgress = 0
        this.identificationStatus = 'exception'
        this.identificationText = '识别请求失败，请检查网络后重试'
      } finally {
        this.identifying = false
      }
    },

    handleSubmit() {
      this.$message.success('风险识别完成')
      this.$emit('refresh')
      this.handleClose()
    },

    handleClose() {
      this.$emit('update:visible', false)
      this.currentStep = 0
      this.identificationProgress = 0
      this.identificationComplete = false
      this.identifying = false
    },

    getRiskLevelTag(level) {
      const tagMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return tagMap[level] || 'info'
    },

    getRiskLevelText(level) {
      const textMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '极高风险'
      }
      return textMap[level] || level
    }
  }
}
</script>

<style scoped>
.identification-content {
  padding: 20px 0;
}

.step-content {
  margin-top: 30px;
}

.step-panel {
  min-height: 300px;
}

.identification-progress {
  text-align: center;
  padding: 40px 0;
}

.progress-text {
  margin-top: 20px;
  font-size: 16px;
  color: #606266;
}

.identification-results {
  margin-top: 30px;
}

.identification-results h5 {
  margin-bottom: 15px;
  color: #303133;
}
</style>
