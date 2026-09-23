<template>
  <el-dialog
    title="折旧调整"
    :visible.sync="visible"
    width="700px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="adjustForm"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      size="small"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产编码" prop="assetCode">
            <el-input v-model="formData.assetCode" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产名称" prop="assetName">
            <el-input v-model="formData.assetName" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="计提期间" prop="period">
            <el-date-picker
              v-model="formData.period"
              type="month"
              placeholder="选择期间"
              format="yyyy-MM"
              value-format="yyyy-MM"
              style="width: 100%"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="原折旧额" prop="originalAmount">
            <el-input v-model="formData.originalAmount" disabled>
              <template slot="prepend">¥</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="调整后折旧额" prop="adjustedAmount">
            <el-input-number
              v-model="formData.adjustedAmount"
              :precision="2"
              :min="0"
              controls-position="right"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="调整金额">
            <el-input :value="adjustmentAmount" disabled>
              <template slot="prepend">¥</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="调整原因" prop="reason">
        <el-select v-model="formData.reason" placeholder="请选择调整原因" style="width: 100%">
          <el-option label="折旧方法变更" value="METHOD_CHANGE" />
          <el-option label="使用年限调整" value="LIFE_ADJUSTMENT" />
          <el-option label="残值率调整" value="RESIDUAL_ADJUSTMENT" />
          <el-option label="资产改良" value="IMPROVEMENT" />
          <el-option label="计算错误" value="CALCULATION_ERROR" />
          <el-option label="其他" value="OTHER" />
        </el-select>
      </el-form-item>
      <el-form-item label="调整说明" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入调整说明"
        />
      </el-form-item>
      <el-form-item label="附件">
        <el-upload
          class="upload-demo"
          action="#"
          :auto-upload="false"
          :on-change="handleFileChange"
          :file-list="fileList"
        >
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">支持上传jpg/png/pdf文件，且不超过5MB</div>
        </el-upload>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'DepreciationAdjustDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    depreciationData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      submitting: false,
      fileList: [],
      formData: {
        assetCode: '',
        assetName: '',
        period: '',
        originalAmount: '',
        adjustedAmount: 0,
        reason: '',
        description: ''
      },
      formRules: {
        adjustedAmount: [{ required: true, message: '请输入调整后折旧额', trigger: 'blur' }],
        reason: [{ required: true, message: '请选择调整原因', trigger: 'change' }],
        description: [{ required: true, message: '请输入调整说明', trigger: 'blur' }]
      }
    }
  },
  computed: {
    adjustmentAmount() {
      const original = parseFloat(this.formData.originalAmount) || 0
      const adjusted = parseFloat(this.formData.adjustedAmount) || 0
      const diff = adjusted - original
      return diff.toFixed(2)
    }
  },
  watch: {
    visible(val) {
      if (val && this.depreciationData) {
        this.formData = {
          assetCode: this.depreciationData.assetCode || '',
          assetName: this.depreciationData.assetName || '',
          period: this.depreciationData.period || '',
          originalAmount: this.depreciationData.depreciationAmount || '',
          adjustedAmount: parseFloat(this.depreciationData.depreciationAmount) || 0,
          reason: '',
          description: ''
        }
      }
    }
  },
  methods: {
    handleFileChange(file, fileList) {
      this.fileList = fileList
    },
    handleSubmit() {
      this.$refs.adjustForm.validate(valid => {
        if (valid) {
          this.$emit('submit', {
            ...this.formData,
            files: this.fileList
          })
        }
      })
    },
    handleClose() {
      this.$refs.adjustForm.resetFields()
      this.fileList = []
      this.$emit('update:visible', false)
    }
  }
}
</script>

