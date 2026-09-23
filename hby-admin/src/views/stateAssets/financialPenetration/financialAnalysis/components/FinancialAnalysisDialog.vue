<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-form
      ref="analysisForm"
      :model="analysisForm"
      :rules="rules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="analysisForm.enterpriseName" placeholder="请输入企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告期间" prop="reportPeriod">
            <el-date-picker
              v-model="analysisForm.reportPeriod"
              type="month"
              placeholder="请选择报告期间"
              value-format="yyyy-MM"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="总营收(万元)" prop="totalRevenue">
            <el-input-number
              v-model="analysisForm.totalRevenue"
              :min="0"
              placeholder="请输入总营收"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="净利润(万元)" prop="netProfit">
            <el-input-number
              v-model="analysisForm.netProfit"
              placeholder="请输入净利润"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="总资产(万元)" prop="totalAssets">
            <el-input-number
              v-model="analysisForm.totalAssets"
              :min="0"
              placeholder="请输入总资产"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="净资产(万元)" prop="netAssets">
            <el-input-number
              v-model="analysisForm.netAssets"
              :min="0"
              placeholder="请输入净资产"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="ROE(%)" prop="roe">
            <el-input-number
              v-model="analysisForm.roe"
              :precision="2"
              placeholder="请输入ROE"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="ROA(%)" prop="roa">
            <el-input-number
              v-model="analysisForm.roa"
              :precision="2"
              placeholder="请输入ROA"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="分析类型" prop="statementType">
            <el-select v-model="analysisForm.statementType" placeholder="请选择分析类型" style="width: 100%;">
              <el-option label="合并报表" value="合并报表"></el-option>
              <el-option label="单体报表" value="单体报表"></el-option>
              <el-option label="年度报表" value="年度报表"></el-option>
              <el-option label="季度报表" value="季度报表"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计状态" prop="auditStatus">
            <el-select v-model="analysisForm.auditStatus" placeholder="请选择审计状态" style="width: 100%;">
              <el-option label="已审计" value="已审计"></el-option>
              <el-option label="未审计" value="未审计"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="财务状况" prop="financialStatus">
        <el-select v-model="analysisForm.financialStatus" placeholder="请选择财务状况" style="width: 100%;">
          <el-option label="优秀" value="EXCELLENT"></el-option>
          <el-option label="良好" value="GOOD"></el-option>
          <el-option label="一般" value="AVERAGE"></el-option>
          <el-option label="较差" value="POOR"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="分析备注" prop="analysisNotes">
        <el-input
          v-model="analysisForm.analysisNotes"
          type="textarea"
          :rows="3"
          placeholder="请输入分析备注"
        />
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addFinancialAnalysis, updateFinancialAnalysis } from '@/api/stateAssets/financialAnalysis'

export default {
  name: 'FinancialAnalysisDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    analysisData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add'
    }
  },
  data() {
    return {
      submitLoading: false,
      analysisForm: {
        enterpriseName: '',
        reportPeriod: '',
        totalRevenue: 0,
        netProfit: 0,
        totalAssets: 0,
        netAssets: 0,
        roe: 0,
        roa: 0,
        statementType: '',
        auditStatus: '',
        financialStatus: '',
        analysisNotes: ''
      },
      rules: {
        enterpriseName: [
          { required: true, message: '请输入企业名称', trigger: 'blur' }
        ],
        reportPeriod: [
          { required: true, message: '请选择报告期间', trigger: 'change' }
        ],
        totalRevenue: [
          { required: true, message: '请输入总营收', trigger: 'blur' }
        ],
        totalAssets: [
          { required: true, message: '请输入总资产', trigger: 'blur' }
        ],
        financialStatus: [
          { required: true, message: '请选择财务状况', trigger: 'change' }
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
      const titleMap = {
        add: '新增财务分析',
        edit: '编辑财务分析',
        analyze: '执行财务分析',
        view: '查看财务分析'
      }
      return titleMap[this.dialogType] || '财务分析'
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
      if (this.dialogType === 'add') {
        this.analysisForm = {
          enterpriseName: '',
          reportPeriod: '',
          totalRevenue: 0,
          netProfit: 0,
          totalAssets: 0,
          netAssets: 0,
          roe: 0,
          roa: 0,
          statementType: '',
          auditStatus: '',
          financialStatus: '',
          analysisNotes: ''
        }
      } else {
        const d = this.analysisData
        this.analysisForm = {
          enterpriseName: d.enterpriseName || d.companyName || '',
          reportPeriod: d.period || d.reportPeriod || '',
          totalRevenue: d.revenue || d.totalRevenue || 0,
          netProfit: d.netProfit || 0,
          totalAssets: d.totalAssets || 0,
          netAssets: d.netAssets || 0,
          roe: d.roe || 0,
          roa: d.roa || 0,
          statementType: d.analysisType || d.statementType || '',
          auditStatus: d.auditStatus || '',
          financialStatus: d.financialStatus || '',
          analysisNotes: d.analysisNotes || ''
        }
      }
    },
    
    handleSubmit() {
      this.$refs.analysisForm.validate((valid) => {
        if (!valid) return
        const totalLiabilities = (this.analysisForm.totalAssets || 0) - (this.analysisForm.netAssets || 0)
        const params = {
          companyName: this.analysisForm.enterpriseName,
          enterpriseName: this.analysisForm.enterpriseName,
          revenue: this.analysisForm.totalRevenue,
          totalRevenue: this.analysisForm.totalRevenue,
          period: this.analysisForm.reportPeriod,
          reportPeriod: this.analysisForm.reportPeriod,
          totalAssets: this.analysisForm.totalAssets,
          netProfit: this.analysisForm.netProfit,
          netAssets: this.analysisForm.netAssets,
          totalLiabilities: totalLiabilities,
          roe: this.analysisForm.roe,
          roa: this.analysisForm.roa,
          statementType: this.analysisForm.statementType,
          auditStatus: this.analysisForm.auditStatus,
          financialStatus: this.analysisForm.financialStatus,
          analysisNotes: this.analysisForm.analysisNotes
        }
        const isEdit = this.dialogType === 'edit' || this.dialogType === 'analyze'
        if (isEdit) {
          params.statementId = this.analysisData.statementId || this.analysisData.id
        }
        const apiFn = isEdit ? updateFinancialAnalysis : addFinancialAnalysis
        this.submitLoading = true
        apiFn(params).then((res) => {
          if (res && (res.result === 200 || res.result === 1 || res.code === 1)) {
            this.$message.success(isEdit ? '更新成功' : '新增成功')
            this.$emit('refresh')
            this.handleClose()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        }).catch(() => {
          this.$message.error('请求失败，请稍后重试')
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },
    
    handleClose() {
      this.$emit('update:visible', false)
      this.$refs.analysisForm.resetFields()
    }
  }
}
</script>
