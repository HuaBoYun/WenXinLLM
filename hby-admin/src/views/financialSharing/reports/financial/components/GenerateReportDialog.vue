<template>
  <el-dialog
    title="生成财务报表"
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
    @close="resetForm">
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="100px">
      <el-form-item label="报表类型" prop="reportType">
        <el-select v-model="form.reportType" placeholder="请选择报表类型" style="width: 100%">
          <el-option label="资产负债表" value="balance_sheet"></el-option>
          <el-option label="利润表" value="income_statement"></el-option>
          <el-option label="现金流量表" value="cash_flow_statement"></el-option>
          <el-option label="财务报表附注" value="financial_notes"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="报表名称" prop="reportName">
        <el-input v-model="form.reportName" placeholder="请输入报表名称"></el-input>
      </el-form-item>
      
      <el-form-item label="会计期间" prop="period">
        <el-date-picker
          v-model="form.period"
          type="month"
          placeholder="选择会计期间"
          format="yyyy-MM"
          value-format="yyyy-MM"
          style="width: 100%">
        </el-date-picker>
      </el-form-item>
      
      <el-form-item label="会计主体" prop="entity">
        <el-select v-model="form.entity" placeholder="请选择会计主体" style="width: 100%">
          <el-option label="示例云科技有限公司" value="hbyun_tech"></el-option>
          <el-option label="示例云金融服务公司" value="hbyun_finance"></el-option>
          <el-option label="示例云投资管理公司" value="hbyun_investment"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="币种" prop="currency">
        <el-select v-model="form.currency" placeholder="请选择币种" style="width: 100%">
          <el-option label="人民币(CNY)" value="CNY"></el-option>
          <el-option label="美元(USD)" value="USD"></el-option>
          <el-option label="欧元(EUR)" value="EUR"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="金额单位" prop="unit">
        <el-select v-model="form.unit" placeholder="请选择金额单位" style="width: 100%">
          <el-option label="元" value="yuan"></el-option>
          <el-option label="千元" value="thousand"></el-option>
          <el-option label="万元" value="ten_thousand"></el-option>
          <el-option label="百万元" value="million"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="报表模板" prop="template">
        <el-select v-model="form.template" placeholder="请选择报表模板" style="width: 100%">
          <el-option label="标准模板" value="standard"></el-option>
          <el-option label="简化模板" value="simplified"></el-option>
          <el-option label="详细模板" value="detailed"></el-option>
          <el-option label="自定义模板" value="custom"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="包含科目">
        <el-checkbox-group v-model="form.includeSubjects">
          <el-checkbox label="资产类科目">资产类科目</el-checkbox>
          <el-checkbox label="负债类科目">负债类科目</el-checkbox>
          <el-checkbox label="所有者权益类科目">所有者权益类科目</el-checkbox>
          <el-checkbox label="损益类科目">损益类科目</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="生成选项">
        <el-checkbox-group v-model="form.options">
          <el-checkbox label="includeComparison">包含同期对比</el-checkbox>
          <el-checkbox label="includeAnalysis">包含分析说明</el-checkbox>
          <el-checkbox label="includeNotes">包含附注信息</el-checkbox>
          <el-checkbox label="autoSave">自动保存</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="备注">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息">
        </el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleConfirm">
        {{ loading ? '生成中...' : '生成报表' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  generateBalanceSheet,
  generateIncomeStatement,
  generateCashFlowStatement
} from '@/api/financialSharing/reports'

export default {
  name: 'GenerateReportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      form: {
        reportType: '',
        reportName: '',
        period: '',
        entity: 'hbyun_tech',
        currency: 'CNY',
        unit: 'yuan',
        template: 'standard',
        includeSubjects: ['资产类科目', '负债类科目', '所有者权益类科目'],
        options: ['autoSave'],
        remark: ''
      },
      rules: {
        reportType: [
          { required: true, message: '请选择报表类型', trigger: 'change' }
        ],
        reportName: [
          { required: true, message: '请输入报表名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        period: [
          { required: true, message: '请选择会计期间', trigger: 'change' }
        ],
        entity: [
          { required: true, message: '请选择会计主体', trigger: 'change' }
        ],
        currency: [
          { required: true, message: '请选择币种', trigger: 'change' }
        ],
        unit: [
          { required: true, message: '请选择金额单位', trigger: 'change' }
        ],
        template: [
          { required: true, message: '请选择报表模板', trigger: 'change' }
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
    }
  },
  watch: {
    'form.reportType'(newVal) {
      if (newVal) {
        this.updateReportName()
      }
    },
    'form.period'(newVal) {
      if (newVal) {
        this.updateReportName()
      }
    }
  },
  methods: {
    updateReportName() {
      if (this.form.reportType && this.form.period) {
        const typeNames = {
          balance_sheet: '资产负债表',
          income_statement: '利润表',
          cash_flow_statement: '现金流量表',
          financial_notes: '财务报表附注'
        }
        this.form.reportName = `${typeNames[this.form.reportType]}_${this.form.period}`
      }
    },
    async handleConfirm() {
      try {
        await this.$refs.form.validate()
        this.loading = true
        
        let response
        switch (this.form.reportType) {
          case 'balance_sheet':
            response = await generateBalanceSheet(this.form)
            break
          case 'income_statement':
            response = await generateIncomeStatement(this.form)
            break
          case 'cash_flow_statement':
            response = await generateCashFlowStatement(this.form)
            break
          default:
            throw new Error('不支持的报表类型')
        }
        
        if (response.code === 200) {
          this.$message.success('报表生成成功')
          this.$emit('confirm', response.data)
          this.handleClose()
        } else {
          throw new Error(response.message || '生成失败')
        }
      } catch (error) {
        if (error.message) {
          this.$message.error('生成失败：' + error.message)
        }
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.dialogVisible = false
    },
    resetForm() {
      this.$refs.form && this.$refs.form.resetFields()
      this.form = {
        reportType: '',
        reportName: '',
        period: '',
        entity: 'hbyun_tech',
        currency: 'CNY',
        unit: 'yuan',
        template: 'standard',
        includeSubjects: ['资产类科目', '负债类科目', '所有者权益类科目'],
        options: ['autoSave'],
        remark: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}

.el-checkbox-group {
  .el-checkbox {
    display: block;
    margin-bottom: 8px;
    margin-right: 0;
  }
}
</style>
