<template>
  <div class="execute-summary-container">
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>执行预算数据汇总</span>
        <el-button type="text" icon="el-icon-back" @click="handleBack">返回</el-button>
      </div>

      <el-form ref="executeForm" :model="executeForm" :rules="rules" label-width="120px" style="max-width: 600px">
        <el-form-item label="预算模型" prop="modelId">
          <el-select v-model="executeForm.modelId" placeholder="请选择预算模型" filterable style="width: 100%">
            <el-option
              v-for="item in modelList"
              :key="item.modelId"
              :label="item.modelName"
              :value="item.modelId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="预算期间" prop="period">
          <el-input v-model="executeForm.period" placeholder="请输入预算期间，如：2026" />
        </el-form-item>

        <el-form-item label="预算版本" prop="version">
          <el-input v-model="executeForm.version" placeholder="请输入预算版本，如：V1.0" />
        </el-form-item>

        <el-form-item label="汇总类型" prop="summaryType">
          <el-select v-model="executeForm.summaryType" placeholder="请选择汇总类型" style="width: 100%">
            <el-option label="科目汇总" value="SUBJECT" />
            <el-option label="组织汇总" value="ORGANIZATION" />
            <el-option label="期间汇总" value="PERIOD" />
            <el-option label="自定义汇总" value="CUSTOM" />
          </el-select>
        </el-form-item>

        <el-form-item label="汇总方法" prop="summaryMethod">
          <el-select v-model="executeForm.summaryMethod" placeholder="请选择汇总方法" style="width: 100%">
            <el-option label="求和(SUM)" value="SUM" />
            <el-option label="平均(AVG)" value="AVG" />
            <el-option label="最大值(MAX)" value="MAX" />
            <el-option label="最小值(MIN)" value="MIN" />
            <el-option label="计数(COUNT)" value="COUNT" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="executing" @click="handleExecute">
            <i v-if="!executing" class="el-icon-s-operation"></i>
            执行汇总
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 执行进度 -->
      <el-alert
        v-if="showProgress"
        :title="progressTitle"
        :type="progressType"
        :closable="false"
        show-icon
        style="margin-top: 20px"
      >
        <div v-if="executing">
          <el-progress :percentage="progressPercent" :status="progressStatus" />
          <p style="margin-top: 10px; color: #909399">{{ progressMessage }}</p>
        </div>
        <div v-else>
          <p>{{ progressMessage }}</p>
        </div>
      </el-alert>
    </el-card>
  </div>
</template>

<script>
import { executeSummary } from '@/api/financialSharing/budgetPlanning/budgetSummary'
import { getModelList } from '@/api/financialSharing/budgetPlanning/budgetModel'

export default {
  name: 'ExecuteSummary',
  data() {
    return {
      executing: false,
      showProgress: false,
      progressPercent: 0,
      progressStatus: '',
      progressTitle: '',
      progressType: 'info',
      progressMessage: '',
      modelList: [],
      executeForm: {
        modelId: '',
        period: '',
        version: '',
        summaryType: '',
        summaryMethod: ''
      },
      rules: {
        modelId: [
          { required: true, message: '请选择预算模型', trigger: 'change' }
        ],
        period: [
          { required: true, message: '请输入预算期间', trigger: 'blur' }
        ],
        version: [
          { required: true, message: '请输入预算版本', trigger: 'blur' }
        ],
        summaryType: [
          { required: true, message: '请选择汇总类型', trigger: 'change' }
        ],
        summaryMethod: [
          { required: true, message: '请选择汇总方法', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.loadModelList()
  },
  methods: {
    // 加载预算模型列表
    async loadModelList() {
      try {
        const res = await getModelList({ pageNum: 1, pageSize: 100 })
        if (res.code === 1) {
          this.modelList = res.data.list || []
        }
      } catch (error) {
        console.error('加载预算模型列表失败:', error)
      }
    },
    // 执行汇总
    handleExecute() {
      this.$refs.executeForm.validate(async (valid) => {
        if (valid) {
          this.executing = true
          this.showProgress = true
          this.progressPercent = 0
          this.progressStatus = ''
          this.progressTitle = '正在执行汇总...'
          this.progressType = 'info'
          this.progressMessage = '正在准备数据...'

          // 处理过程异步等待（实际由后端 API 完成）
          const progressInterval = setInterval(() => {
            if (this.progressPercent < 90) {
              this.progressPercent += 10
              if (this.progressPercent === 30) {
                this.progressMessage = '正在查询预算数据...'
              } else if (this.progressPercent === 60) {
                this.progressMessage = '正在计算汇总值...'
              } else if (this.progressPercent === 90) {
                this.progressMessage = '正在保存汇总结果...'
              }
            }
          }, 500)

          try {
            const res = await executeSummary(this.executeForm)
            clearInterval(progressInterval)

            if (res.code === 1) {
              this.progressPercent = 100
              this.progressStatus = 'success'
              this.progressTitle = '汇总执行成功!'
              this.progressType = 'success'
              this.progressMessage = '汇总任务已完成，可以查看汇总结果。'
              this.$message.success('汇总执行成功')

              // 3秒后跳转到列表页
              setTimeout(() => {
                this.$router.push('/financialSharing/budgetPlanning/budgetSummary')
              }, 3000)
            } else {
              this.progressStatus = 'exception'
              this.progressTitle = '汇总执行失败!'
              this.progressType = 'error'
              this.progressMessage = res.msg || '汇总执行失败，请检查参数后重试。'
              this.$message.error(res.msg || '汇总执行失败')
            }
          } catch (error) {
            clearInterval(progressInterval)
            this.progressStatus = 'exception'
            this.progressTitle = '汇总执行失败!'
            this.progressType = 'error'
            this.progressMessage = '汇总执行失败: ' + error.message
            this.$message.error('汇总执行失败: ' + error.message)
          } finally {
            this.executing = false
          }
        }
      })
    },
    // 重置
    handleReset() {
      this.$refs.executeForm.resetFields()
      this.showProgress = false
    },
    // 返回
    handleBack() {
      this.$router.back()
    }
  }
}
</script>

<style lang="scss" scoped>
.execute-summary-container {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>

