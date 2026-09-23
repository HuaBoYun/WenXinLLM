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
          <el-form-item label="项目名称" prop="projectName">
            <el-input v-model="form.projectName" placeholder="请输入项目名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经营类型" prop="operationsType">
            <el-select v-model="form.operationsType" placeholder="请选择经营类型" style="width: 100%">
              <el-option label="收入管理" :value="1" />
              <el-option label="成本管理" :value="2" />
              <el-option label="利润分析" :value="3" />
              <el-option label="现金流管理" :value="4" />
              <el-option label="风险控制" :value="5" />
              <el-option label="绩效评估" :value="6" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目ID" prop="projectId">
            <el-input v-model="form.projectId" placeholder="请输入项目ID" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="managerName">
            <el-input v-model="form.managerName" placeholder="请输入负责人姓名" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="经营编号" prop="operationsNo">
            <el-input v-model="form.operationsNo" placeholder="请输入经营编号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经营期间" prop="operationsPeriod">
            <el-input v-model="form.operationsPeriod" placeholder="请输入经营期间" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="合同金额" prop="contractAmount">
            <el-input-number
              v-model="form.contractAmount"
              :min="0"
              :precision="2"
              placeholder="请输入合同金额"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算收入" prop="budgetRevenue">
            <el-input-number
              v-model="form.budgetRevenue"
              :min="0"
              :precision="2"
              placeholder="请输入预算收入"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="实际收入" prop="actualRevenue">
            <el-input-number
              v-model="form.actualRevenue"
              :min="0"
              :precision="2"
              placeholder="请输入实际收入"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算成本" prop="budgetCost">
            <el-input-number
              v-model="form.budgetCost"
              :min="0"
              :precision="2"
              placeholder="请输入预算成本"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="实际成本" prop="actualCost">
            <el-input-number
              v-model="form.actualCost"
              :min="0"
              :precision="2"
              placeholder="请输入实际成本"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级" prop="riskLevel">
            <el-select v-model="form.riskLevel" placeholder="请选择风险等级" style="width: 100%">
              <el-option label="低" :value="1" />
              <el-option label="中" :value="2" />
              <el-option label="高" :value="3" />
              <el-option label="极高" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="经营状态" prop="operationsStatus">
            <el-select v-model="form.operationsStatus" placeholder="请选择经营状态" style="width: 100%">
              <el-option label="正常" :value="1" />
              <el-option label="预警" :value="2" />
              <el-option label="异常" :value="3" />
              <el-option label="停止" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="利润率(%)" prop="profitRate">
            <el-input-number
              v-model="form.profitRate"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="请输入利润率"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="分析报告" prop="analysisReport">
        <el-input
          v-model="form.analysisReport"
          type="textarea"
          :rows="3"
          placeholder="请输入分析报告"
        />
      </el-form-item>

      <el-form-item label="改进建议" prop="improvementSuggestions">
        <el-input
          v-model="form.improvementSuggestions"
          type="textarea"
          :rows="3"
          placeholder="请输入改进建议"
        />
      </el-form-item>

      <el-form-item label="风险提示" prop="riskWarnings">
        <el-input
          v-model="form.riskWarnings"
          type="textarea"
          :rows="2"
          placeholder="请输入风险提示"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave" v-if="!isDetail">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    createProjectOperations,
    updateProjectOperations
  } from '@/api/contract/operations'
  import { successCode } from '@/config'

  export default {
    name: 'ManagementEdit',
    data() {
      return {
        dialogVisible: false,
        isDetail: false,
        title: '',
        form: {
          id: null,
          operationsNo: '',
          projectId: null,
          projectName: '',
          operationsType: null,
          operationsPeriod: '',
          contractAmount: null,
          budgetRevenue: null,
          actualRevenue: null,
          budgetCost: null,
          actualCost: null,
          profitRate: null,
          riskLevel: null,
          operationsStatus: 1,
          managerName: '',
          analysisReport: '',
          improvementSuggestions: '',
          riskWarnings: '',
          remarks: ''
        },
        rules: {
          projectName: [
            { required: true, message: '请输入项目名称', trigger: 'blur' }
          ],
          operationsType: [
            { required: true, message: '请选择经营类型', trigger: 'change' }
          ],
          projectId: [
            { required: true, message: '请输入项目ID', trigger: 'blur' }
          ],
          managerName: [
            { required: true, message: '请输入负责人姓名', trigger: 'blur' }
          ],
          contractAmount: [
            { required: true, message: '请输入合同金额', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      /**
       * 检查响应是否成功
       * @param {Object} response 响应对象
       * @returns {boolean} 是否成功
       */
      isResponseSuccess(response) {
        const codes = Array.isArray(successCode) ? successCode : [successCode]
        return codes.includes(response.code)
      },

      showEdit(type, data) {
        this.dialogVisible = true
        this.isDetail = type === 'detail'
        
        if (type === 'add') {
          this.title = '新建项目经营管理'
          this.resetForm()
        } else if (type === 'edit') {
          this.title = '编辑项目经营管理'
          this.form = { ...data }
        } else if (type === 'detail') {
          this.title = '项目经营管理详情'
          this.form = { ...data }
        }
      },
      
      resetForm() {
        this.form = {
          id: null,
          operationsNo: '',
          projectId: null,
          projectName: '',
          operationsType: null,
          operationsPeriod: '',
          contractAmount: null,
          budgetRevenue: null,
          actualRevenue: null,
          budgetCost: null,
          actualCost: null,
          profitRate: null,
          riskLevel: null,
          operationsStatus: 1,
          managerName: '',
          analysisReport: '',
          improvementSuggestions: '',
          riskWarnings: '',
          remarks: ''
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

          let response
          if (this.form.id) {
            response = await updateProjectOperations(this.form)
          } else {
            response = await createProjectOperations(this.form)
          }

          console.log('项目经营保存响应:', response)
          console.log('保存响应状态码:', response.code)
          console.log('保存响应消息:', response.msg)

          // 使用统一的状态码判断方法
          if (this.isResponseSuccess(response)) {
            this.$message.success(this.form.id ? '更新成功' : '创建成功')
            this.handleClose()
            this.$emit('fetch-data')
          } else {
            this.$message.error(response.message || response.msg || '操作失败')
          }
        } catch (error) {
          console.error('项目经营保存错误:', error)
          if (error.message) {
            this.$message.error('操作失败：' + error.message)
          } else {
            this.$message.error('操作失败')
          }
        }
      }
    }
  }
</script>

<style scoped>
  .dialog-footer {
    text-align: right;
  }
</style>
