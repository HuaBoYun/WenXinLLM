<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      :disabled="isDetail"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="评估名称" prop="assessmentName">
            <el-input v-model="form.assessmentName" placeholder="请输入评估名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评估类型" prop="assessmentType">
            <el-select v-model="form.assessmentType" placeholder="请选择评估类型" style="width: 100%">
              <el-option label="承接前" :value="1" />
              <el-option label="执行中" :value="2" />
              <el-option label="结项后" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="相对方" prop="counterpartId">
            <CounterpartSelector
              v-model="form.counterpartId"
              :disabled="isDetail"
              @counterpart-selected="handleCounterpartSelected"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评估人" prop="assessorId">
            <el-select v-model="form.assessorId" placeholder="请选择评估人" style="width: 100%">
              <el-option label="张三" :value="1" />
              <el-option label="李四" :value="2" />
              <el-option label="王五" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="评估日期" prop="assessmentDate">
            <el-date-picker
              v-model="form.assessmentDate"
              type="date"
              placeholder="选择评估日期"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目ID" prop="projectId">
            <el-input v-model="form.projectId" placeholder="请输入项目ID" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
      </el-form-item>

      <!-- 风险明细 -->
      <el-divider content-position="left">风险明细</el-divider>
      <el-button
        type="primary"
        size="small"
        icon="el-icon-plus"
        @click="addRiskDetail"
        v-if="!isDetail"
        style="margin-bottom: 10px"
      >
        添加风险项
      </el-button>
      
      <el-table :data="form.riskDetails" border style="width: 100%">
        <el-table-column label="风险类别" width="120">
          <template #default="{ row, $index }">
            <el-select v-model="row.riskCategory" placeholder="选择类别" size="small" :disabled="isDetail">
              <el-option label="技术风险" :value="1" />
              <el-option label="政策风险" :value="2" />
              <el-option label="财务风险" :value="3" />
              <el-option label="市场风险" :value="4" />
              <el-option label="管理风险" :value="5" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="风险项目" width="150">
          <template #default="{ row, $index }">
            <el-input v-model="row.riskItem" placeholder="风险项目" size="small" :disabled="isDetail" />
          </template>
        </el-table-column>
        <el-table-column label="风险描述" min-width="200">
          <template #default="{ row, $index }">
            <el-input v-model="row.riskDescription" placeholder="风险描述" size="small" :disabled="isDetail" />
          </template>
        </el-table-column>
        <el-table-column label="发生概率(%)" width="120">
          <template #default="{ row, $index }">
            <el-input-number
              v-model="row.riskProbability"
              :min="0"
              :max="100"
              :precision="1"
              size="small"
              :disabled="isDetail"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column label="影响程度(%)" width="120">
          <template #default="{ row, $index }">
            <el-input-number
              v-model="row.riskImpact"
              :min="0"
              :max="100"
              :precision="1"
              size="small"
              :disabled="isDetail"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="100">
          <template #default="{ row, $index }">
            <el-tag :type="getRiskLevelType(row.riskLevel)">
              {{ getRiskLevelName(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80" v-if="!isDetail">
          <template #default="{ row, $index }">
            <el-button
              type="text"
              size="small"
              style="color: #f56c6c;"
              @click="removeRiskDetail($index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave" v-if="!isDetail">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    createRiskAssessment,
    updateRiskAssessment
  } from '@/api/contract/riskAssessment'
  import CounterpartSelector from './CounterpartSelector'

  export default {
    name: 'RiskAssessmentEdit',
    components: {
      CounterpartSelector
    },
    data() {
      return {
        dialogVisible: false,
        isDetail: false,
        title: '',
        form: {
          id: null,
          projectId: null,
          assessmentName: '',
          counterpartId: null,
          assessmentType: null,
          assessorId: null,
          assessmentDate: '',
          remarks: '',
          riskDetails: []
        },
        rules: {
          assessmentName: [
            { required: true, message: '请输入评估名称', trigger: 'blur' }
          ],
          assessmentType: [
            { required: true, message: '请选择评估类型', trigger: 'change' }
          ],
          counterpartId: [
            { required: false, message: '请选择相对方', trigger: 'change' }
          ],
          assessorId: [
            { required: true, message: '请选择评估人', trigger: 'change' }
          ],
          assessmentDate: [
            { required: true, message: '请选择评估日期', trigger: 'change' }
          ]
        },
        selectedCounterpart: null // 存储选中的相对方信息
      }
    },
    methods: {
      showEdit(type, data) {
        this.dialogVisible = true
        this.isDetail = type === 'detail'

        if (type === 'add') {
          this.title = '新建风险评估'
          this.resetForm()
        } else if (type === 'edit') {
          this.title = '编辑风险评估'
          this.form = { ...data }
          if (!this.form.riskDetails) {
            this.form.riskDetails = []
          }
        } else if (type === 'detail') {
          this.title = '风险评估详情'
          this.form = { ...data }
          if (!this.form.riskDetails) {
            this.form.riskDetails = []
          }
        }
      },

      resetForm() {
        this.form = {
          id: null,
          projectId: null,
          assessmentName: '',
          counterpartId: null,
          assessmentType: null,
          assessorId: null,
          assessmentDate: '',
          remarks: '',
          riskDetails: []
        }
        this.$nextTick(() => {
          this.$refs.form && this.$refs.form.clearValidate()
        })
      },

      handleClose() {
        this.dialogVisible = false
        this.resetForm()
      },

      async handleSave() {
        try {
          await this.$refs.form.validate()

          // 计算风险评分
          this.calculateRiskScores()

          let response
          if (this.form.id) {
            response = await updateRiskAssessment(this.form)
          } else {
            response = await createRiskAssessment(this.form)
          }

          console.log('风险评估保存响应:', response)

          // 检查多种成功状态码：200, 0, '200', '0', '1', 1, 2
          const successCodes = [200, 0, '200', '0', '1', 1, 2]
          if (successCodes.includes(response.code)) {
            this.$message.success(this.form.id ? '更新成功' : '创建成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.message || response.msg || '操作失败')
          }
        } catch (error) {
          console.error('风险评估保存错误:', error)
          if (error.message) {
            this.$message.error('操作失败：' + error.message)
          } else {
            this.$message.error('操作失败')
          }
        }
      },

      // 添加风险明细
      addRiskDetail() {
        this.form.riskDetails.push({
          riskCategory: null,
          riskItem: '',
          riskDescription: '',
          riskProbability: 0,
          riskImpact: 0,
          riskScore: 0,
          riskLevel: 1,
          mitigationMeasures: '',
          responsiblePerson: '',
          currentStatus: 1
        })
      },

      // 删除风险明细
      removeRiskDetail(index) {
        this.form.riskDetails.splice(index, 1)
      },

      // 计算风险评分
      calculateRiskScores() {
        this.form.riskDetails.forEach(detail => {
          detail.riskScore = (detail.riskProbability * detail.riskImpact) / 100

          // 根据评分确定风险等级
          if (detail.riskScore >= 60) {
            detail.riskLevel = 4 // 极高
          } else if (detail.riskScore >= 40) {
            detail.riskLevel = 3 // 高
          } else if (detail.riskScore >= 20) {
            detail.riskLevel = 2 // 中
          } else {
            detail.riskLevel = 1 // 低
          }
        })
      },

      // 处理相对方选择
      handleCounterpartSelected(counterpart) {
        this.selectedCounterpart = counterpart
        console.log('选中的相对方:', counterpart)

        // 可以在这里根据选中的相对方信息自动填充其他字段
        if (counterpart) {
          // 例如：自动生成评估名称（使用原有相对方模块的字段）
          if (!this.form.assessmentName) {
            this.form.assessmentName = `${counterpart.budgetname}风险评估`
          }
        }
      },

      // 获取风险等级名称
      getRiskLevelName(level) {
        const levelMap = {
          1: '低',
          2: '中',
          3: '高',
          4: '极高'
        }
        return levelMap[level] || '未知'
      },

      // 获取风险等级样式
      getRiskLevelType(level) {
        const typeMap = {
          1: 'success',
          2: 'warning',
          3: 'danger',
          4: 'danger'
        }
        return typeMap[level] || 'info'
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
