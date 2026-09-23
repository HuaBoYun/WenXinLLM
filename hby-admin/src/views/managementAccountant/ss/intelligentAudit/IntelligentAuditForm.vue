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
          <el-form-item label="审计编码" prop="auditCode">
            <el-input
              v-model="formData.auditCode"
              placeholder="请输入审计编码"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计名称" prop="auditName">
            <el-input
              v-model="formData.auditName"
              placeholder="请输入审计名称"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="审计类型" prop="auditType">
            <el-select
              v-model="formData.auditType"
              placeholder="请选择审计类型"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="财务审计" value="FINANCIAL" />
              <el-option label="合规审计" value="COMPLIANCE" />
              <el-option label="运营审计" value="OPERATIONAL" />
              <el-option label="IT审计" value="IT" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计状态" prop="auditStatus">
            <el-select
              v-model="formData.auditStatus"
              placeholder="请选择审计状态"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="计划中" value="PLANNING" />
              <el-option label="执行中" value="EXECUTING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已暂停" value="SUSPENDED" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始日期" prop="startDate">
            <el-date-picker
              v-model="formData.startDate"
              type="date"
              placeholder="请选择开始日期"
              :disabled="isView"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束日期" prop="endDate">
            <el-date-picker
              v-model="formData.endDate"
              type="date"
              placeholder="请选择结束日期"
              :disabled="isView"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="审计描述" prop="auditDescription">
            <el-input
              v-model="formData.auditDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入审计描述"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="审计范围" prop="auditScope">
            <el-input
              v-model="formData.auditScope"
              placeholder="请输入审计范围"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级" prop="riskLevel">
            <el-select
              v-model="formData.riskLevel"
              placeholder="请选择风险等级"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
              <el-option label="极高风险" value="CRITICAL" />
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
  name: 'IntelligentAuditForm',
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
        create: '新增智能审计',
        edit: '编辑智能审计',
        view: '查看智能审计'
      }
      return titleMap[this.mode] || '智能审计'
    },
    isView() {
      return this.mode === 'view'
    }
  },
  data() {
    return {
      rules: {
        auditCode: [
          { required: true, message: '请输入审计编码', trigger: 'blur' }
        ],
        auditName: [
          { required: true, message: '请输入审计名称', trigger: 'blur' }
        ],
        auditType: [
          { required: true, message: '请选择审计类型', trigger: 'change' }
        ],
        auditStatus: [
          { required: true, message: '请选择审计状态', trigger: 'change' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
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
