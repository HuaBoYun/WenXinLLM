<template>
  <el-dialog
    title="预算调整"
    :visible.sync="dialogVisible"
    width="700px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="预算名称">
        <el-input v-model="budgetData.budgetName" readonly></el-input>
      </el-form-item>
      
      <el-form-item label="调整类型" prop="adjustmentType">
        <el-radio-group v-model="form.adjustmentType">
          <el-radio label="increase">增加预算</el-radio>
          <el-radio label="decrease">减少预算</el-radio>
          <el-radio label="transfer">预算调拨</el-radio>
          <el-radio label="modify">修改预算</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="调整金额" prop="adjustmentAmount">
        <el-input-number
          v-model="form.adjustmentAmount"
          :precision="2"
          :step="1000"
          :min="0"
          controls-position="right"
          style="width: 100%;"
        ></el-input-number>
      </el-form-item>
      
      <el-form-item label="调整原因" prop="adjustmentReason">
        <el-select v-model="form.adjustmentReason" placeholder="请选择调整原因">
          <el-option label="业务需求变化" value="business_change"></el-option>
          <el-option label="市场环境变化" value="market_change"></el-option>
          <el-option label="政策调整" value="policy_change"></el-option>
          <el-option label="成本变化" value="cost_change"></el-option>
          <el-option label="其他原因" value="other"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="调整说明" prop="adjustmentDescription">
        <el-input
          v-model="form.adjustmentDescription"
          type="textarea"
          :rows="4"
          placeholder="请详细说明调整原因和必要性"
        ></el-input>
      </el-form-item>
      
      <el-form-item label="生效时间" prop="effectiveDate">
        <el-date-picker
          v-model="form.effectiveDate"
          type="date"
          placeholder="选择生效时间"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
        ></el-date-picker>
      </el-form-item>
      
      <el-form-item label="审批人" prop="approver">
        <el-select v-model="form.approver" placeholder="请选择审批人">
          <el-option label="张三" value="zhangsan"></el-option>
          <el-option label="李四" value="lisi"></el-option>
          <el-option label="王五" value="wangwu"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="附件上传">
        <el-upload
          class="upload-demo"
          :action="uploadUrl"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :file-list="fileList"
          multiple
        >
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">支持上传相关证明文件</div>
        </el-upload>
      </el-form-item>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">提交调整</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BudgetAdjustDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    budgetData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      uploadUrl: '/api/upload',
      fileList: [],
      form: {
        adjustmentType: 'increase',
        adjustmentAmount: 0,
        adjustmentReason: '',
        adjustmentDescription: '',
        effectiveDate: '',
        approver: ''
      },
      rules: {
        adjustmentType: [
          { required: true, message: '请选择调整类型', trigger: 'change' }
        ],
        adjustmentAmount: [
          { required: true, message: '请输入调整金额', trigger: 'blur' }
        ],
        adjustmentReason: [
          { required: true, message: '请选择调整原因', trigger: 'change' }
        ],
        adjustmentDescription: [
          { required: true, message: '请输入调整说明', trigger: 'blur' }
        ],
        effectiveDate: [
          { required: true, message: '请选择生效时间', trigger: 'change' }
        ],
        approver: [
          { required: true, message: '请选择审批人', trigger: 'change' }
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
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      this.form = {
        adjustmentType: 'increase',
        adjustmentAmount: 0,
        adjustmentReason: '',
        adjustmentDescription: '',
        effectiveDate: '',
        approver: ''
      }
      this.fileList = []
    },
    handlePreview(file) {
      console.log(file)
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },
    handleConfirm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.loading = true
          setTimeout(() => {
            this.loading = false
            this.$message.success('预算调整申请提交成功')
            this.handleClose()
            this.$emit('refresh')
          }, 1000)
        }
      })
    }
  }
}
</script>

<style scoped>
.upload-demo {
  margin-top: 10px;
}
</style>
