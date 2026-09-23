<template>
  <el-dialog
    :title="formData.dataId ? '修改报表数据' : '新增报表数据'"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报表任务" prop="taskId">
            <el-select
              v-model="formData.taskId"
              placeholder="请选择报表任务"
              style="width: 100%"
              @change="handleTaskChange"
            >
              <el-option
                v-for="item in taskOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="表单模板" prop="templateId">
            <el-select
              v-model="formData.templateId"
              placeholder="请选择表单模板"
              style="width: 100%"
            >
              <el-option
                v-for="item in localTemplateOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="指标" prop="indicatorId">
            <el-select
              v-model="formData.indicatorId"
              placeholder="请选择指标"
              style="width: 100%"
            >
              <el-option
                v-for="item in indicatorOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组织ID" prop="orgId">
            <el-input v-model="formData.orgId" placeholder="请输入组织ID" maxlength="32" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="期间" prop="period">
            <el-input v-model="formData.period" placeholder="如:202401" maxlength="20" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据值" prop="dataValue">
            <el-input v-model="formData.dataValue" placeholder="请输入数据值" maxlength="500" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据来源" prop="dataSource">
            <el-select v-model="formData.dataSource" placeholder="请选择数据来源" style="width: 100%">
              <el-option label="手工" value="MANUAL" />
              <el-option label="取数" value="FETCH" />
              <el-option label="计算" value="CALCULATION" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单元格颜色">
            <el-color-picker v-model="formData.cellColor" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="维度值">
            <el-input
              v-model="formData.dimensionValues"
              type="textarea"
              :rows="3"
              placeholder="请输入维度值(JSON格式)"
            />
            <span class="form-tip">维度值JSON格式,如:{"dim1":"value1","dim2":"value2"}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否可编辑" prop="isEditable">
            <el-radio-group v-model="formData.isEditable">
              <el-radio label="Y">是</el-radio>
              <el-radio label="N">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveReportData } from '@/api/financialSharing/enterpriseReport/reportData'
import { getFormTemplateList } from '@/api/financialSharing/enterpriseReport/formTemplate'

export default {
  name: 'ReportDataForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    taskOptions: {
      type: Array,
      default: () => []
    },
    templateOptions: {
      type: Array,
      default: () => []
    },
    indicatorOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      localTemplateOptions: [],
      rules: {
        taskId: [
          { required: true, message: '报表任务不能为空', trigger: 'change' }
        ],
        templateId: [
          { required: true, message: '表单模板不能为空', trigger: 'change' }
        ],
        indicatorId: [
          { required: true, message: '指标不能为空', trigger: 'change' }
        ],
        orgId: [
          { required: true, message: '组织ID不能为空', trigger: 'blur' }
        ],
        period: [
          { required: true, message: '期间不能为空', trigger: 'blur' }
        ],
        dataSource: [
          { required: true, message: '数据来源不能为空', trigger: 'change' }
        ],
        isEditable: [
          { required: true, message: '是否可编辑不能为空', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        // 对话框打开时,加载模板选项
        if (this.formData.taskId) {
          this.handleTaskChange(this.formData.taskId)
        }
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    /** 任务变更时加载模板选项 */
    handleTaskChange(taskId) {
      if (taskId) {
        const task = this.taskOptions.find(t => t.value === taskId)
        if (task && task.groupId) {
          this.getTemplateOptions(task.groupId)
        }
      } else {
        this.localTemplateOptions = []
      }
      this.formData.templateId = ''
    },
    /** 获取模板选项 */
    getTemplateOptions(groupId) {
      getFormTemplateList({ groupId, status: 'ACTIVE' }).then(response => {
        if (response.code === 200) {
          this.localTemplateOptions = (response.data || []).map(item => ({
            value: item.templateId,
            label: item.templateName
          }))
        }
      })
    },
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 验证维度值JSON格式
          if (this.formData.dimensionValues && this.formData.dimensionValues.trim()) {
            try {
              JSON.parse(this.formData.dimensionValues)
            } catch (e) {
              this.$message.error('维度值格式不正确,请输入有效的JSON格式')
              return
            }
          }

          this.submitLoading = true
          saveReportData(this.formData).then(response => {
            this.submitLoading = false
            if (response.code === 200) {
              this.$message.success(response.msg || '保存成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          }).catch(() => {
            this.submitLoading = false
          })
        }
      })
    },
    /** 关闭对话框 */
    handleClose() {
      this.$refs.form.resetFields()
      this.localTemplateOptions = []
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.form-tip {
  font-size: 12px;
  color: #999;
  line-height: 1.5;
  display: block;
  margin-top: 5px;
}
</style>


