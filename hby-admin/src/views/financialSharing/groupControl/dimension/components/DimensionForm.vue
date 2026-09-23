<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form
      :model="form"
      :rules="formRules"
      ref="formRef"
      label-width="120px"
      :disabled="formType === 'view'"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="维度编码" prop="dimensionCode">
            <el-input
              v-model="form.dimensionCode"
              placeholder="请输入维度编码"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="维度名称" prop="dimensionName">
            <el-input
              v-model="form.dimensionName"
              placeholder="请输入维度名称"
              maxlength="100"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="维度类型" prop="dimensionType">
            <el-select
              v-model="form.dimensionType"
              placeholder="请选择维度类型"
              style="width: 100%"
            >
              <el-option label="账户" value="ACCOUNT" />
              <el-option label="组织" value="ORGANIZATION" />
              <el-option label="项目" value="PROJECT" />
              <el-option label="产品" value="PRODUCT" />
              <el-option label="客户" value="CUSTOMER" />
              <el-option label="供应商" value="SUPPLIER" />
              <el-option label="自定义" value="CUSTOM" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="维度分类" prop="dimensionCategory">
            <el-input
              v-model="form.dimensionCategory"
              placeholder="请输入维度分类"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否分层" prop="isHierarchy">
            <el-radio-group v-model="form.isHierarchy">
              <el-radio label="Y">是</el-radio>
              <el-radio label="N">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最大层级" prop="maxLevel" v-if="form.isHierarchy === 'Y'">
            <el-input-number
              v-model="form.maxLevel"
              :min="1"
              :max="10"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否默认" prop="isDefault">
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

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="来源系统" prop="sourceSystem">
            <el-input
              v-model="form.sourceSystem"
              placeholder="请输入来源系统"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="描述" prop="description">
            <el-input
              v-model="form.description"
              type="textarea"
              :rows="3"
              placeholder="请输入描述"
              maxlength="500"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer" v-if="formType !== 'view'">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitLoading">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveDimension, getDimensionDetail } from '@/api/financialSharing/groupControl/dimension'

export default {
  name: 'DimensionForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formType: {
      type: String,
      default: 'add' // add, edit, view
    },
    formData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      form: {
        dimensionId: null,
        dimensionCode: '',
        dimensionName: '',
        dimensionType: '',
        dimensionCategory: '',
        isHierarchy: 'N',
        maxLevel: 5,
        isDefault: 'N',
        status: 'ACTIVE',
        sourceSystem: '',
        description: ''
      },
      formRules: {
        dimensionCode: [
          { required: true, message: '请输入维度编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        dimensionName: [
          { required: true, message: '请输入维度名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        dimensionType: [
          { required: true, message: '请选择维度类型', trigger: 'change' }
        ],
        isHierarchy: [
          { required: true, message: '请选择是否分层', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        'add': '新增维度',
        'edit': '编辑维度',
        'view': '查看维度'
      }
      return titleMap[this.formType] || '维度信息'
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.initForm()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      if (this.formType === 'add') {
        this.resetForm()
      } else if (this.formData) {
        this.form = { ...this.formData }
      }
    },

    // 重置表单
    resetForm() {
      this.form = {
        dimensionId: null,
        dimensionCode: '',
        dimensionName: '',
        dimensionType: '',
        dimensionCategory: '',
        isHierarchy: 'N',
        maxLevel: 5,
        isDefault: 'N',
        status: 'ACTIVE',
        sourceSystem: '',
        description: ''
      }
      this.$nextTick(() => {
        if (this.$refs.formRef) {
          this.$refs.formRef.clearValidate()
        }
      })
    },

    // 提交表单
    handleSubmit() {
      this.$refs.formRef.validate(async (valid) => {
        if (!valid) {
          return false
        }

        this.submitLoading = true
        try {
          const res = await saveDimension(this.form)
          if (res.code === 1) {
            this.$message.success('保存成功')
            this.$emit('success')
            this.handleClose()
          } else {
            this.$message.error(res.msg || '保存失败')
          }
        } catch (error) {
          console.error('保存维度失败:', error)
          this.$message.error('保存失败')
        } finally {
          this.submitLoading = false
        }
      })
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    }
  }
}
</script>

<style scoped lang="scss">
.dialog-footer {
  text-align: right;
}
</style>
