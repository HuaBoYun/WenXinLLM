<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="changeForm"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      size="small"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="变动单号" prop="changeNumber">
            <el-input v-model="formData.changeNumber" placeholder="自动生成" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="变动类型" prop="changeType">
            <el-select v-model="formData.changeType" placeholder="请选择变动类型" style="width: 100%" :disabled="isView">
              <el-option label="购置" value="PURCHASE" />
              <el-option label="调拨" value="TRANSFER" />
              <el-option label="改良" value="IMPROVEMENT" />
              <el-option label="维修" value="MAINTENANCE" />
              <el-option label="升级" value="UPGRADE" />
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
          <el-form-item label="变动金额" prop="changeAmount">
            <el-input-number
              v-model="formData.changeAmount"
              :precision="2"
              :min="0"
              controls-position="right"
              style="width: 100%"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="变动日期" prop="changeDate">
            <el-date-picker
              v-model="formData.changeDate"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              style="width: 100%"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20" v-if="formData.changeType === 'TRANSFER'">
        <el-col :span="12">
          <el-form-item label="原部门" prop="fromDepartment">
            <el-input v-model="formData.fromDepartment" placeholder="请输入原部门" :disabled="isView" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标部门" prop="toDepartment">
            <el-input v-model="formData.toDepartment" placeholder="请输入目标部门" :disabled="isView" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="变动原因" prop="reason">
        <el-input
          v-model="formData.reason"
          type="textarea"
          :rows="3"
          placeholder="请输入变动原因"
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
  name: 'AssetChangeFormDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    mode: {
      type: String,
      default: 'add'
    },
    changeData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      submitting: false,
      formData: this.getInitialFormData(),
      formRules: {
        changeType: [{ required: true, message: '请选择变动类型', trigger: 'change' }],
        assetCode: [{ required: true, message: '请输入资产编码', trigger: 'blur' }],
        changeAmount: [{ required: true, message: '请输入变动金额', trigger: 'blur' }],
        changeDate: [{ required: true, message: '请选择变动日期', trigger: 'change' }],
        reason: [{ required: true, message: '请输入变动原因', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        add: '新增资产变动',
        edit: '编辑资产变动',
        view: '查看资产变动'
      }
      return titleMap[this.mode] || '资产变动'
    },
    isView() {
      return this.mode === 'view'
    }
  },
  watch: {
    visible(val) {
      if (val && this.mode !== 'add') {
        this.formData = { ...this.changeData }
      }
    }
  },
  methods: {
    getInitialFormData() {
      return {
        changeNumber: '',
        changeType: '',
        assetCode: '',
        assetName: '',
        changeAmount: 0,
        changeDate: '',
        fromDepartment: '',
        toDepartment: '',
        reason: '',
        remark: ''
      }
    },
    handleSelectAsset() {
      this.$message.info('请在资产卡片列表中选择资产后点击确定')
    },
    handleSubmit() {
      this.$refs.changeForm.validate(valid => {
        if (valid) {
          this.$emit('submit', this.formData)
        }
      })
    },
    handleClose() {
      this.$refs.changeForm.resetFields()
      this.formData = this.getInitialFormData()
      this.$emit('update:visible', false)
    }
  }
}
</script>

