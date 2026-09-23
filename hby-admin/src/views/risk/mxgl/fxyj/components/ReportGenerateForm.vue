<template>
  <div class="report-generate-form">
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      size="medium"
    >
      <el-form-item label="报告类型" prop="reportType">
        <el-radio-group v-model="form.reportType">
          <el-radio label="SUMMARY">汇总报告</el-radio>
          <el-radio label="DETAILED">详细报告</el-radio>
          <el-radio label="TREND">趋势分析报告</el-radio>
          <el-radio label="PERFORMANCE">性能分析报告</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="时间范围" prop="timeRange">
        <el-radio-group v-model="form.timeRange" @change="handleTimeRangeChange">
          <el-radio label="TODAY">今天</el-radio>
          <el-radio label="WEEK">本周</el-radio>
          <el-radio label="MONTH">本月</el-radio>
          <el-radio label="QUARTER">本季度</el-radio>
          <el-radio label="CUSTOM">自定义</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="自定义时间" v-if="form.timeRange === 'CUSTOM'">
        <el-date-picker
          v-model="form.customDateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="预警类型">
        <el-checkbox-group v-model="form.warningTypes">
          <el-checkbox label="FINANCIAL_RISK">财务风险</el-checkbox>
          <el-checkbox label="PROCUREMENT_RISK">采购风险</el-checkbox>
          <el-checkbox label="CREDIT_RISK">信用风险</el-checkbox>
          <el-checkbox label="COMPLIANCE_RISK">合规风险</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="预警级别">
        <el-checkbox-group v-model="form.warningLevels">
          <el-checkbox label="HIGH">高风险</el-checkbox>
          <el-checkbox label="MEDIUM">中风险</el-checkbox>
          <el-checkbox label="LOW">低风险</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="预警状态">
        <el-checkbox-group v-model="form.warningStatuses">
          <el-checkbox label="PENDING">待处理</el-checkbox>
          <el-checkbox label="PROCESSING">处理中</el-checkbox>
          <el-checkbox label="PROCESSED">已处理</el-checkbox>
          <el-checkbox label="IGNORED">已忽略</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="企业范围">
        <el-select
          v-model="form.companyIds"
          multiple
          filterable
          placeholder="请选择企业（不选择表示全部企业）"
          style="width: 100%"
        >
          <el-option
            v-for="company in companyList"
            :key="company.companyId"
            :label="company.companyName"
            :value="company.companyId"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="报告格式" prop="reportFormat">
        <el-radio-group v-model="form.reportFormat">
          <el-radio label="PDF">PDF格式</el-radio>
          <el-radio label="EXCEL">Excel格式</el-radio>
          <el-radio label="WORD">Word格式</el-radio>
          <el-radio label="HTML">HTML格式</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="包含内容">
        <el-checkbox-group v-model="form.includeContents">
          <el-checkbox label="STATISTICS">统计概览</el-checkbox>
          <el-checkbox label="CHARTS">图表分析</el-checkbox>
          <el-checkbox label="DETAILS">详细数据</el-checkbox>
          <el-checkbox label="TRENDS">趋势分析</el-checkbox>
          <el-checkbox label="RECOMMENDATIONS">建议措施</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="报告标题" prop="reportTitle">
        <el-input
          v-model="form.reportTitle"
          placeholder="请输入报告标题"
          maxlength="100"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="报告描述">
        <el-input
          v-model="form.reportDescription"
          type="textarea"
          :rows="3"
          placeholder="请输入报告描述"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="生成方式" prop="generateMode">
        <el-radio-group v-model="form.generateMode">
          <el-radio label="IMMEDIATE">立即生成</el-radio>
          <el-radio label="SCHEDULED">定时生成</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="定时时间" v-if="form.generateMode === 'SCHEDULED'">
        <el-date-picker
          v-model="form.scheduledTime"
          type="datetime"
          placeholder="选择定时生成时间"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="邮件发送">
        <el-checkbox v-model="form.sendByEmail">生成后发送邮件</el-checkbox>
      </el-form-item>

      <el-form-item label="邮件接收人" v-if="form.sendByEmail">
        <el-input
          v-model="form.emailRecipients"
          placeholder="请输入邮件接收人，多个邮箱用逗号分隔"
        />
      </el-form-item>
    </el-form>

    <div class="form-footer">
      <el-button @click="handleCancel">取消</el-button>
      <el-button @click="handlePreview">预览</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        {{ form.generateMode === 'IMMEDIATE' ? '立即生成' : '设置定时' }}
      </el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReportGenerateForm',
  data() {
    return {
      submitting: false,
      selectedModel: null, // 🆕 新增：选中的模型信息
      form: {
        reportType: 'SUMMARY',
        timeRange: 'MONTH',
        customDateRange: null,
        warningTypes: ['FINANCIAL_RISK', 'PROCUREMENT_RISK'],
        warningLevels: ['HIGH', 'MEDIUM', 'LOW'],
        warningStatuses: ['PENDING', 'PROCESSING', 'PROCESSED'],
        companyIds: [],
        reportFormat: 'PDF',
        includeContents: ['STATISTICS', 'CHARTS', 'DETAILS'],
        reportTitle: '',
        reportDescription: '',
        generateMode: 'IMMEDIATE',
        scheduledTime: null,
        sendByEmail: false,
        emailRecipients: ''
      },
      rules: {
        reportType: [
          { required: true, message: '请选择报告类型', trigger: 'change' }
        ],
        timeRange: [
          { required: true, message: '请选择时间范围', trigger: 'change' }
        ],
        reportFormat: [
          { required: true, message: '请选择报告格式', trigger: 'change' }
        ],
        reportTitle: [
          { required: true, message: '请输入报告标题', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        generateMode: [
          { required: true, message: '请选择生成方式', trigger: 'change' }
        ]
      },
      companyList: [
        { companyId: '001', companyName: '示例云科技有限公司' },
        { companyId: '002', companyName: '测试企业A' },
        { companyId: '003', companyName: '测试企业B' }
      ]
    }
  },
  created() {
    this.initForm()
  },
  methods: {
    // 初始化表单
    initForm() {
      // 设置默认报告标题
      const now = new Date()
      const dateStr = now.toLocaleDateString('zh-CN')
      this.form.reportTitle = `风险预警分析报告_${dateStr}`
    },
    // 时间范围变化处理
    handleTimeRangeChange(value) {
      if (value !== 'CUSTOM') {
        this.form.customDateRange = null
      }
    },
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          // 验证自定义时间范围
          if (this.form.timeRange === 'CUSTOM' && !this.form.customDateRange) {
            this.$message.error('请选择自定义时间范围')
            return
          }

          // 验证定时时间
          if (this.form.generateMode === 'SCHEDULED' && !this.form.scheduledTime) {
            this.$message.error('请选择定时生成时间')
            return
          }

          // 验证邮件接收人
          if (this.form.sendByEmail && !this.form.emailRecipients) {
            this.$message.error('请输入邮件接收人')
            return
          }

          // 验证至少选择一种预警类型
          if (!this.form.warningTypes.length) {
            this.$message.error('请至少选择一种预警类型')
            return
          }

          // 验证至少选择一种包含内容
          if (!this.form.includeContents.length) {
            this.$message.error('请至少选择一种包含内容')
            return
          }

          this.submitting = true
          
          // 构建提交数据
          const submitData = { ...this.form }

          // 处理时间范围
          if (this.form.timeRange === 'CUSTOM' && this.form.customDateRange) {
            submitData.startDate = this.form.customDateRange[0]
            submitData.endDate = this.form.customDateRange[1]
          }

          // 🆕 新增：如果有选中的模型，添加模型信息
          if (this.selectedModel) {
            submitData.selectedModel = {
              evalModelId: this.selectedModel.evalModelId,
              modelName: this.selectedModel.modelName,
              modelType: this.selectedModel.modelType,
              businessScenario: this.selectedModel.businessScenario
            }
            console.log('包含选中模型的报告生成请求:', submitData)
          }

          this.$emit('submit', submitData)
          this.submitting = false
        } else {
          this.$message.error('请完善表单信息')
        }
      })
    },
    // 预览
    handlePreview() {
      this.$message.info('预览功能开发中')
    },
    // 取消
    handleCancel() {
      this.$emit('cancel')
    },
    // 重置表单
    resetForm() {
      this.$refs.form.resetFields()
      this.initForm()
    },

    // 🆕 新增：设置选中的模型（从操作列调用）
    setSelectedModel(model) {
      console.log('设置选中的模型:', model)

      // 根据模型类型预设预警类型
      const modelTypeMapping = {
        'PROCUREMENT': ['PROCUREMENT_RISK'],
        'FINANCIAL': ['FINANCIAL_RISK'],
        'CREDIT': ['CREDIT_RISK'],
        'COMPLIANCE': ['COMPLIANCE_RISK']
      }

      if (model.modelType && modelTypeMapping[model.modelType]) {
        this.form.warningTypes = modelTypeMapping[model.modelType]
      }

      // 设置报告标题，包含模型名称
      const now = new Date()
      const dateStr = now.toLocaleDateString('zh-CN')
      this.form.reportTitle = `${model.modelName}_风险预警分析报告_${dateStr}`

      // 设置报告描述
      this.form.reportDescription = `基于评估模型"${model.modelName}"生成的风险预警分析报告`

      // 存储模型信息，用于后续提交
      this.selectedModel = model
    }
  }
}
</script>

<style lang="scss" scoped>
.report-generate-form {
  .form-footer {
    text-align: right;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;
    
    .el-button {
      margin-left: 10px;
    }
  }
}
</style>
