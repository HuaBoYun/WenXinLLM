<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="800px"
    :before-close="handleClose"
    @closed="handleClosed"
  >
    <el-form
      ref="form"
      :model="formData"
      :rules="rules"
      label-width="120px"
      size="small"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="质控编码" prop="controlCode">
            <el-input
              v-model="formData.controlCode"
              placeholder="请输入质控编码"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="质控名称" prop="controlName">
            <el-input
              v-model="formData.controlName"
              placeholder="请输入质控名称"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="质控类型" prop="controlType">
            <el-select
              v-model="formData.controlType"
              placeholder="请选择质控类型"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="数据质量" value="DATA_QUALITY" />
              <el-option label="流程质量" value="PROCESS_QUALITY" />
              <el-option label="系统质量" value="SYSTEM_QUALITY" />
              <el-option label="服务质量" value="SERVICE_QUALITY" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="质控状态" prop="controlStatus">
            <el-select
              v-model="formData.controlStatus"
              placeholder="请选择质控状态"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="启用" value="ENABLED" />
              <el-option label="禁用" value="DISABLED" />
              <el-option label="测试" value="TESTING" />
              <el-option label="维护" value="MAINTENANCE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="检查频率" prop="checkFrequency">
            <el-select
              v-model="formData.checkFrequency"
              placeholder="请选择检查频率"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="实时" value="REALTIME" />
              <el-option label="每小时" value="HOURLY" />
              <el-option label="每日" value="DAILY" />
              <el-option label="每周" value="WEEKLY" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="质量阈值" prop="qualityThreshold">
            <el-input-number
              v-model="formData.qualityThreshold"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="请输入质量阈值"
              :disabled="isView"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="质控描述" prop="controlDescription">
            <el-input
              v-model="formData.controlDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入质控描述"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="检查规则" prop="checkRule">
            <el-input
              v-model="formData.checkRule"
              placeholder="请输入检查规则"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="告警级别" prop="alertLevel">
            <el-select
              v-model="formData.alertLevel"
              placeholder="请选择告警级别"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="信息" value="INFO" />
              <el-option label="警告" value="WARNING" />
              <el-option label="错误" value="ERROR" />
              <el-option label="严重" value="CRITICAL" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="!isView" type="primary" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'QualityControlForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    mode: {
      type: String,
      default: 'create' // create, edit, view
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        create: '新增质量控制',
        edit: '编辑质量控制',
        view: '查看质量控制'
      }
      return titleMap[this.mode] || '质量控制'
    },
    isView() {
      return this.mode === 'view'
    }
  },
  data() {
    return {
      rules: {
        controlCode: [
          { required: true, message: '请输入质控编码', trigger: 'blur' }
        ],
        controlName: [
          { required: true, message: '请输入质控名称', trigger: 'blur' }
        ],
        controlType: [
          { required: true, message: '请选择质控类型', trigger: 'change' }
        ],
        controlStatus: [
          { required: true, message: '请选择质控状态', trigger: 'change' }
        ],
        checkFrequency: [
          { required: true, message: '请选择检查频率', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$emit('submit', this.formData)
        }
      })
    },
    handleClose() {
      this.$emit('update:visible', false)
    },
    handleClosed() {
      this.$refs.form.resetFields()
      this.$emit('closed')
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}
</style>
