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
          <el-form-item label="校准编码" prop="calibrationCode">
            <el-input
              v-model="formData.calibrationCode"
              placeholder="请输入校准编码"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="校准名称" prop="calibrationName">
            <el-input
              v-model="formData.calibrationName"
              placeholder="请输入校准名称"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="校准类型" prop="calibrationType">
            <el-select
              v-model="formData.calibrationType"
              placeholder="请选择校准类型"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="强制分布" value="FORCED_DISTRIBUTION" />
              <el-option label="相对排名" value="RELATIVE_RANKING" />
              <el-option label="绝对评分" value="ABSOLUTE_SCORING" />
              <el-option label="混合模式" value="HYBRID_MODE" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="校准状态" prop="calibrationStatus">
            <el-select
              v-model="formData.calibrationStatus"
              placeholder="请选择校准状态"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="计划中" value="PLANNING" />
              <el-option label="进行中" value="IN_PROGRESS" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已取消" value="CANCELLED" />
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
          <el-form-item label="校准描述" prop="calibrationDescription">
            <el-input
              v-model="formData.calibrationDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入校准描述"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="参与人数" prop="participantCount">
            <el-input-number
              v-model="formData.participantCount"
              :min="1"
              placeholder="请输入参与人数"
              :disabled="isView"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="校准权重" prop="calibrationWeight">
            <el-input-number
              v-model="formData.calibrationWeight"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="请输入校准权重"
              :disabled="isView"
              style="width: 100%"
            />
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
  name: 'PerformanceCalibrationForm',
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
        create: '新增绩效校准',
        edit: '编辑绩效校准',
        view: '查看绩效校准'
      }
      return titleMap[this.mode] || '绩效校准'
    },
    isView() {
      return this.mode === 'view'
    }
  },
  data() {
    return {
      rules: {
        calibrationCode: [
          { required: true, message: '请输入校准编码', trigger: 'blur' }
        ],
        calibrationName: [
          { required: true, message: '请输入校准名称', trigger: 'blur' }
        ],
        calibrationType: [
          { required: true, message: '请选择校准类型', trigger: 'change' }
        ],
        calibrationStatus: [
          { required: true, message: '请选择校准状态', trigger: 'change' }
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
