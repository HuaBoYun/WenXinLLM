<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="140px"
      :disabled="formType === 'view'"
    >
      <el-form-item label="组织ID" prop="orgId">
        <el-input v-model="form.orgId" placeholder="请输入组织ID" maxlength="32" />
      </el-form-item>

      <el-form-item label="组织名称" prop="orgName">
        <el-input v-model="form.orgName" placeholder="请输入组织名称" maxlength="200" />
      </el-form-item>

      <el-form-item label="合并方法" prop="consolidationMethod">
        <el-select v-model="form.consolidationMethod" placeholder="请选择合并方法">
          <el-option label="完全合并" value="FULL" />
          <el-option label="比例合并" value="PROPORTIONAL" />
          <el-option label="权益法" value="EQUITY" />
        </el-select>
      </el-form-item>

      <el-form-item label="生效起始期间" prop="effectiveStartPeriod">
        <el-input v-model="form.effectiveStartPeriod" placeholder="如:202401" maxlength="20" />
      </el-form-item>

      <el-form-item label="生效终止期间" prop="effectiveEndPeriod">
        <el-input v-model="form.effectiveEndPeriod" placeholder="如:202412" maxlength="20" />
      </el-form-item>

      <el-form-item label="排序号" prop="sortOrder">
        <el-input-number v-model="form.sortOrder" :min="0" :max="9999" />
      </el-form-item>

      <el-form-item label="是否启用" prop="isActive">
        <el-radio-group v-model="form.isActive">
          <el-radio label="Y">是</el-radio>
          <el-radio label="N">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
          maxlength="500"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="formType !== 'view'" type="primary" :loading="submitLoading" @click="handleSubmit">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getScopeById, saveScope, updateScope } from '@/api/financialSharing/consolidationReport/consolidationScope'

export default {
  name: 'ConsolidationScopeForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    scopeId: {
      type: String,
      default: null
    },
    modelId: {
      type: String,
      default: null
    },
    formType: {
      type: String,
      default: 'add' // add/edit/view
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      form: {
        scopeId: null,
        modelId: '',
        orgId: '',
        orgName: '',
        consolidationMethod: '',
        effectiveStartPeriod: '',
        effectiveEndPeriod: '',
        isActive: 'Y',
        sortOrder: 0,
        remark: ''
      },
      rules: {
        orgId: [
          { required: true, message: '请输入组织ID', trigger: 'blur' }
        ],
        orgName: [
          { required: true, message: '请输入组织名称', trigger: 'blur' }
        ],
        consolidationMethod: [
          { required: true, message: '请选择合并方法', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        add: '新增合并范围配置',
        edit: '编辑合并范围配置',
        view: '查看合并范围配置'
      }
      return titleMap[this.formType] || '合并范围配置'
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        if (this.scopeId) {
          this.loadScopeData()
        } else {
          this.form.modelId = this.modelId
        }
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
      if (!val) {
        this.resetForm()
      }
    }
  },
  methods: {
    /** 加载范围配置数据 */
    loadScopeData() {
      getScopeById({ scopeId: this.scopeId }).then(res => {
        if (res.code === 200 && res.data) {
          this.form = { ...res.data }
        } else {
          this.$message.error(res.msg || '加载数据失败')
        }
      })
    },
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          const apiMethod = this.formType === 'add' ? saveScope : updateScope
          apiMethod(this.form).then(res => {
            this.submitLoading = false
            if (res.code === 200) {
              this.$message.success(this.formType === 'add' ? '新增成功' : '修改成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(res.msg || '操作失败')
            }
          }).catch(() => {
            this.submitLoading = false
            this.$message.error('操作失败')
          })
        }
      })
    },
    /** 关闭对话框 */
    handleClose() {
      this.dialogVisible = false
    },
    /** 重置表单 */
    resetForm() {
      this.form = {
        scopeId: null,
        modelId: '',
        orgId: '',
        orgName: '',
        consolidationMethod: '',
        effectiveStartPeriod: '',
        effectiveEndPeriod: '',
        isActive: 'Y',
        sortOrder: 0,
        remark: ''
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    }
  }
}
</script>

<style scoped>
</style>


