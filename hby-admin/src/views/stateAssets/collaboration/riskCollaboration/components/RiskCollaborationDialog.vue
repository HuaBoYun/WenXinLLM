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
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-select v-model="formData.enterpriseName" placeholder="请选择企业" style="width: 100%">
              <el-option label="示例云科技有限公司" value="示例云科技有限公司"></el-option>
              <el-option label="国资投资集团" value="国资投资集团"></el-option>
              <el-option label="能源发展公司" value="能源发展公司"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险名称" prop="riskName">
            <el-input v-model="formData.riskName" placeholder="请输入风险名称"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险类型" prop="riskType">
            <el-select v-model="formData.riskType" placeholder="请选择风险类型" style="width: 100%">
              <el-option label="市场风险" value="market_risk"></el-option>
              <el-option label="信用风险" value="credit_risk"></el-option>
              <el-option label="操作风险" value="operational_risk"></el-option>
              <el-option label="流动性风险" value="liquidity_risk"></el-option>
              <el-option label="合规风险" value="compliance_risk"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级" prop="riskLevel">
            <el-select v-model="formData.riskLevel" placeholder="请选择风险等级" style="width: 100%">
              <el-option label="高风险" value="high"></el-option>
              <el-option label="中风险" value="medium"></el-option>
              <el-option label="低风险" value="low"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="风险评分" prop="riskScore">
            <el-input-number
              v-model="formData.riskScore"
              :min="0"
              :max="100"
              :precision="1"
              style="width: 100%">
            </el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="协同状态" prop="collaborationStatus">
            <el-select v-model="formData.collaborationStatus" placeholder="请选择协同状态" style="width: 100%">
              <el-option label="待协同" value="pending"></el-option>
              <el-option label="协同中" value="collaborating"></el-option>
              <el-option label="已完成" value="completed"></el-option>
              <el-option label="已暂停" value="paused"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="识别日期" prop="identificationDate">
            <el-date-picker
              v-model="formData.identificationDate"
              type="date"
              placeholder="选择识别日期"
              style="width: 100%">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input v-model="formData.responsiblePerson" placeholder="请输入负责人"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="风险描述" prop="riskDescription">
        <el-input
          v-model="formData.riskDescription"
          type="textarea"
          :rows="4"
          placeholder="请输入风险描述">
        </el-input>
      </el-form-item>
      
      <el-form-item label="影响范围" prop="impactScope">
        <el-checkbox-group v-model="formData.impactScope">
          <el-checkbox label="financial">财务影响</el-checkbox>
          <el-checkbox label="operational">运营影响</el-checkbox>
          <el-checkbox label="reputation">声誉影响</el-checkbox>
          <el-checkbox label="legal">法律影响</el-checkbox>
          <el-checkbox label="strategic">战略影响</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      
      <el-form-item label="可能损失" prop="potentialLoss">
        <el-input
          v-model="formData.potentialLoss"
          placeholder="请输入可能造成的损失（万元）"
          suffix-icon="el-icon-money">
          <template slot="append">万元</template>
        </el-input>
      </el-form-item>
      
      <el-form-item label="应对措施" prop="countermeasures">
        <el-input
          v-model="formData.countermeasures"
          type="textarea"
          :rows="4"
          placeholder="请输入应对措施">
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
  name: 'RiskCollaborationDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view
    },
    riskData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      formData: {
        enterpriseName: '',
        riskName: '',
        riskType: '',
        riskLevel: '',
        riskScore: 0,
        collaborationStatus: '',
        identificationDate: '',
        responsiblePerson: '',
        riskDescription: '',
        impactScope: [],
        potentialLoss: '',
        countermeasures: '',
        remarks: ''
      },
      formRules: {
        enterpriseName: [
          { required: true, message: '请选择企业', trigger: 'change' }
        ],
        riskName: [
          { required: true, message: '请输入风险名称', trigger: 'blur' }
        ],
        riskType: [
          { required: true, message: '请选择风险类型', trigger: 'change' }
        ],
        riskLevel: [
          { required: true, message: '请选择风险等级', trigger: 'change' }
        ],
        riskScore: [
          { required: true, message: '请输入风险评分', trigger: 'blur' }
        ],
        collaborationStatus: [
          { required: true, message: '请选择协同状态', trigger: 'change' }
        ],
        identificationDate: [
          { required: true, message: '请选择识别日期', trigger: 'change' }
        ],
        responsiblePerson: [
          { required: true, message: '请输入负责人', trigger: 'blur' }
        ],
        riskDescription: [
          { required: true, message: '请输入风险描述', trigger: 'blur' }
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
        add: '新增风险协同',
        edit: '编辑风险协同',
        view: '查看风险协同'
      }
      return titleMap[this.dialogType] || '风险协同'
    }
  },
  watch: {
    visible(newVal) {
      if (newVal) {
        this.initFormData()
      }
    },
    riskData: {
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
          enterpriseName: '',
          riskName: '',
          riskType: '',
          riskLevel: '',
          riskScore: 0,
          collaborationStatus: '',
          identificationDate: '',
          responsiblePerson: '',
          riskDescription: '',
          impactScope: [],
          potentialLoss: '',
          countermeasures: '',
          remarks: ''
        }
      } else if (this.riskData && Object.keys(this.riskData).length > 0) {
        this.formData = { ...this.riskData }
      }
    },
    
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          const action = this.dialogType === 'add' ? '新增' : '更新'
          this.$emit('submit', { ...this.formData })
          this.$message.success(`${action}风险协同成功`)
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
