<template>
  <el-dialog
    :title="dimensionId ? '编辑维度' : '新增维度'"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-divider content-position="left">基本信息</el-divider>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="维度编码" prop="dimensionCode">
            <el-input v-model="form.dimensionCode" placeholder="请输入维度编码" :disabled="!!dimensionId" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="维度名称" prop="dimensionName">
            <el-input v-model="form.dimensionName" placeholder="请输入维度名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="维度类型" prop="dimensionType">
            <el-select v-model="form.dimensionType" placeholder="请选择维度类型" style="width: 100%">
              <el-option label="预置" value="PRESET" />
              <el-option label="自定义" value="CUSTOM" />
              <el-option label="档案引入" value="ARCHIVE" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="维度分类" prop="dimensionCategory">
            <el-select v-model="form.dimensionCategory" placeholder="请选择维度分类" style="width: 100%">
              <el-option label="科目" value="SUBJECT" />
              <el-option label="主体" value="ENTITY" />
              <el-option label="期间" value="PERIOD" />
              <el-option label="版本" value="VERSION" />
              <el-option label="币种" value="CURRENCY" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">层级配置</el-divider>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否启用层级" prop="isHierarchy">
            <el-radio-group v-model="form.isHierarchy">
              <el-radio label="Y">是</el-radio>
              <el-radio label="N">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最大层级数" prop="maxLevel">
            <el-input-number v-model="form.maxLevel" :min="1" :max="10" :disabled="form.isHierarchy === 'N'" style="width: 100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否默认体系" prop="isDefault">
            <el-radio-group v-model="form.isDefault">
              <el-radio label="Y">是</el-radio>
              <el-radio label="N">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio label="ACTIVE">启用</el-radio>
              <el-radio label="INACTIVE">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">其他信息</el-divider>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="描述" prop="description">
            <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getDimensionDetail, saveDimension } from '@/api/groupControl/dimension'

export default {
  name: 'DimensionForm',
  props: {
    visible: { type: Boolean, default: false },
    dimensionId: { type: String, default: null }
  },
  data() {
    return {
      dialogVisible: this.visible,
      submitting: false,
      form: {
        dimensionId: null,
        dimensionCode: '',
        dimensionName: '',
        dimensionType: 'CUSTOM',
        dimensionCategory: null,
        isHierarchy: 'N',
        maxLevel: 1,
        isDefault: 'N',
        status: 'ACTIVE',
        description: ''
      },
      rules: {
        dimensionCode: [{ required: true, message: '请输入维度编码', trigger: 'blur' }],
        dimensionName: [{ required: true, message: '请输入维度名称', trigger: 'blur' }],
        dimensionType: [{ required: true, message: '请选择维度类型', trigger: 'change' }]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    }
  },
  created() {
    if (this.dimensionId) {
      this.loadDetail()
    }
  },
  methods: {
    loadDetail() {
      getDimensionDetail({ dimensionId: this.dimensionId }).then(response => {
        if (response.code === 1 && response.data) {
          this.form = { ...response.data }
        }
      })
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitting = true
          saveDimension(this.form).then(response => {
            if (response.code === 1) {
              this.$message.success('保存成功')
              this.$emit('success')
              this.handleClose()
            }
            this.submitting = false
          }).catch(() => {
            this.submitting = false
          })
        }
      })
    },
    handleClose() {
      this.$emit('update:visible', false)
      this.$refs.form.resetFields()
    }
  }
}
</script>

