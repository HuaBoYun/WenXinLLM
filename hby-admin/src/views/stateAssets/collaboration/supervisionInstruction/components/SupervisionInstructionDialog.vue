<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose">
    
    <el-form
      ref="form"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      :disabled="dialogType === 'view'">
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="指令标题" prop="instructionTitle">
            <el-input v-model="formData.instructionTitle" placeholder="请输入指令标题"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标企业" prop="targetEnterprise">
            <el-select v-model="formData.targetEnterprise" placeholder="请选择目标企业" style="width: 100%">
              <el-option label="示例云科技有限公司" value="hbyun"></el-option>
              <el-option label="国资投资集团" value="gztz"></el-option>
              <el-option label="能源发展公司" value="nyfy"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="指令类型" prop="instructionType">
            <el-select v-model="formData.instructionType" placeholder="请选择指令类型" style="width: 100%">
              <el-option label="合规检查" value="compliance_check"></el-option>
              <el-option label="风险排查" value="risk_investigation"></el-option>
              <el-option label="财务审计" value="financial_audit"></el-option>
              <el-option label="信息报送" value="information_report"></el-option>
              <el-option label="整改要求" value="rectification_requirement"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="优先级" prop="priority">
            <el-select v-model="formData.priority" placeholder="请选择优先级" style="width: 100%">
              <el-option label="紧急" value="urgent"></el-option>
              <el-option label="高" value="high"></el-option>
              <el-option label="中" value="medium"></el-option>
              <el-option label="低" value="low"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="下发日期" prop="issueDate">
            <el-date-picker
              v-model="formData.issueDate"
              type="date"
              placeholder="选择下发日期"
              style="width: 100%">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="截止日期" prop="deadline">
            <el-date-picker
              v-model="formData.deadline"
              type="date"
              placeholder="选择截止日期"
              style="width: 100%">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input v-model="formData.responsiblePerson" placeholder="请输入负责人"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系方式" prop="contactInfo">
            <el-input v-model="formData.contactInfo" placeholder="请输入联系方式"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="指令内容" prop="instructionContent">
        <el-input
          v-model="formData.instructionContent"
          type="textarea"
          :rows="4"
          placeholder="请输入指令具体内容">
        </el-input>
      </el-form-item>
      
      <el-form-item label="执行要求" prop="executionRequirements">
        <el-checkbox-group v-model="formData.executionRequirements">
          <el-checkbox label="书面报告">书面报告</el-checkbox>
          <el-checkbox label="现场检查">现场检查</el-checkbox>
          <el-checkbox label="数据提交">数据提交</el-checkbox>
          <el-checkbox label="整改方案">整改方案</el-checkbox>
          <el-checkbox label="进度汇报">进度汇报</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="附件要求" prop="attachmentRequirements">
        <el-input
          v-model="formData.attachmentRequirements"
          type="textarea"
          :rows="3"
          placeholder="请输入附件要求">
        </el-input>
      </el-form-item>
      
      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="formData.remarks"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息">
        </el-input>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="dialogType !== 'view'" type="primary" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'SupervisionInstructionDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view
    },
    instructionData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      formData: {
        instructionTitle: '',
        targetEnterprise: '',
        instructionType: '',
        priority: '',
        issueDate: '',
        deadline: '',
        responsiblePerson: '',
        contactInfo: '',
        instructionContent: '',
        executionRequirements: [],
        attachmentRequirements: '',
        remarks: ''
      },
      formRules: {
        instructionTitle: [
          { required: true, message: '请输入指令标题', trigger: 'blur' }
        ],
        targetEnterprise: [
          { required: true, message: '请选择目标企业', trigger: 'change' }
        ],
        instructionType: [
          { required: true, message: '请选择指令类型', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请选择优先级', trigger: 'change' }
        ],
        issueDate: [
          { required: true, message: '请选择下发日期', trigger: 'change' }
        ],
        deadline: [
          { required: true, message: '请选择截止日期', trigger: 'change' }
        ],
        responsiblePerson: [
          { required: true, message: '请输入负责人', trigger: 'blur' }
        ],
        instructionContent: [
          { required: true, message: '请输入指令内容', trigger: 'blur' }
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
        add: '新增监管指令',
        edit: '编辑监管指令',
        view: '查看监管指令'
      }
      return titleMap[this.dialogType] || '监管指令'
    }
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        this.initFormData()
      }
    },
    instructionData: {
      handler(newVal) {
        if (newVal && Object.keys(newVal).length > 0) {
          this.formData = { ...newVal }
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    // 初始化表单数据
    initFormData() {
      if (this.dialogType === 'add') {
        this.formData = {
          instructionTitle: '',
          targetEnterprise: '',
          instructionType: '',
          priority: '',
          issueDate: '',
          deadline: '',
          responsiblePerson: '',
          contactInfo: '',
          instructionContent: '',
          executionRequirements: [],
          attachmentRequirements: '',
          remarks: ''
        }
      } else if (this.instructionData && Object.keys(this.instructionData).length > 0) {
        this.formData = { ...this.instructionData }
      }
    },
    
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          const action = this.dialogType === 'add' ? '新增' : '更新'
          this.$emit('submit', { ...this.formData })
          this.$message.success(`${action}监管指令成功`)
          this.handleClose()
        }
      })
    },
    
    // 关闭对话框
    handleClose() {
      this.$refs.form.resetFields()
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
