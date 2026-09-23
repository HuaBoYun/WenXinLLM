<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="disposalForm"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      size="small"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="处置单号" prop="disposalNumber">
            <el-input v-model="formData.disposalNumber" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="处置类型" prop="disposalType">
            <el-select v-model="formData.disposalType" placeholder="请选择处置类型" style="width: 100%" :disabled="isView">
              <el-option label="报废" value="SCRAP" />
              <el-option label="出售" value="SALE" />
              <el-option label="转让" value="TRANSFER" />
              <el-option label="捐赠" value="DONATION" />
              <el-option label="毁损" value="DAMAGE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产编码" prop="assetCode">
            <el-input v-model="formData.assetCode" placeholder="请输入资产编码" :disabled="isView">
              <el-button slot="append" icon="el-icon-search" @click="handleSelectAsset" :disabled="isView"></el-button>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产名称" prop="assetName">
            <el-input v-model="formData.assetName" placeholder="自动带出" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产原值" prop="originalValue">
            <el-input v-model="formData.originalValue" placeholder="自动带出" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="账面净值" prop="netBookValue">
            <el-input v-model="formData.netBookValue" placeholder="自动带出" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="处置收入" prop="disposalIncome">
            <el-input-number
              v-model="formData.disposalIncome"
              :precision="2"
              :min="0"
              controls-position="right"
              style="width: 100%"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="处置日期" prop="disposalDate">
            <el-date-picker
              v-model="formData.disposalDate"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="处置原因" prop="reason">
        <el-input
          v-model="formData.reason"
          type="textarea"
          :rows="3"
          placeholder="请输入处置原因"
          :disabled="isView"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="2"
          placeholder="请输入备注"
          :disabled="isView"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">{{ isView ? '关闭' : '取消' }}</el-button>
      <el-button v-if="!isView" type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AssetDisposalFormDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    mode: {
      type: String,
      default: 'add'
    },
    disposalData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      submitting: false,
      formData: this.getInitialFormData(),
      formRules: {
        disposalType: [{ required: true, message: '请选择处置类型', trigger: 'change' }],
        assetCode: [{ required: true, message: '请输入资产编码', trigger: 'blur' }],
        disposalDate: [{ required: true, message: '请选择处置日期', trigger: 'change' }],
        reason: [{ required: true, message: '请输入处置原因', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        add: '新增资产处置',
        edit: '编辑资产处置',
        view: '查看资产处置'
      }
      return titleMap[this.mode] || '资产处置'
    },
    isView() {
      return this.mode === 'view'
    }
  },
  watch: {
    visible(val) {
      if (val && this.mode !== 'add') {
        this.formData = { ...this.disposalData }
      }
    }
  },
  methods: {
    getInitialFormData() {
      return {
        disposalNumber: '',
        disposalType: '',
        assetCode: '',
        assetName: '',
        originalValue: '',
        netBookValue: '',
        disposalIncome: 0,
        disposalDate: '',
        reason: '',
        remark: ''
      }
    },
    handleSelectAsset() {
      this.$message.info('请在资产卡片列表中选择资产后点击确定')
    },
    handleSubmit() {
      this.$refs.disposalForm.validate(valid => {
        if (valid) {
          this.$emit('submit', this.formData)
        }
      })
    },
    handleClose() {
      this.$refs.disposalForm.resetFields()
      this.formData = this.getInitialFormData()
      this.$emit('update:visible', false)
    }
  }
}
</script>

