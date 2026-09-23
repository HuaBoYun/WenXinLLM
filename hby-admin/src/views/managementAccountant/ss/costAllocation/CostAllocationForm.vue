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
          <el-form-item label="分摊编码" prop="allocationCode">
            <el-input
              v-model="formData.allocationCode"
              placeholder="请输入分摊编码"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分摊名称" prop="allocationName">
            <el-input
              v-model="formData.allocationName"
              placeholder="请输入分摊名称"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="分摊类型" prop="allocationType">
            <el-select
              v-model="formData.allocationType"
              placeholder="请选择分摊类型"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="直接分摊" value="DIRECT" />
              <el-option label="间接分摊" value="INDIRECT" />
              <el-option label="比例分摊" value="PROPORTION" />
              <el-option label="阶梯分摊" value="LADDER" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分摊状态" prop="allocationStatus">
            <el-select
              v-model="formData.allocationStatus"
              placeholder="请选择分摊状态"
              :disabled="isView"
              style="width: 100%"
            >
              <el-option label="草稿" value="DRAFT" />
              <el-option label="生效" value="ACTIVE" />
              <el-option label="暂停" value="SUSPENDED" />
              <el-option label="失效" value="INACTIVE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="生效日期" prop="effectiveDate">
            <el-date-picker
              v-model="formData.effectiveDate"
              type="date"
              placeholder="请选择生效日期"
              :disabled="isView"
              style="width: 100%"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失效日期" prop="expiryDate">
            <el-date-picker
              v-model="formData.expiryDate"
              type="date"
              placeholder="请选择失效日期"
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
          <el-form-item label="分摊描述" prop="allocationDescription">
            <el-input
              v-model="formData.allocationDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入分摊描述"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="分摊基础" prop="allocationBasis">
            <el-input
              v-model="formData.allocationBasis"
              placeholder="请输入分摊基础"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分摊比例" prop="allocationRatio">
            <el-input-number
              v-model="formData.allocationRatio"
              :min="0"
              :max="100"
              :precision="2"
              placeholder="请输入分摊比例"
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
  name: 'CostAllocationForm',
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
        create: '新增成本分摊',
        edit: '编辑成本分摊',
        view: '查看成本分摊'
      }
      return titleMap[this.mode] || '成本分摊'
    },
    isView() {
      return this.mode === 'view'
    }
  },
  data() {
    return {
      rules: {
        allocationCode: [
          { required: true, message: '请输入分摊编码', trigger: 'blur' }
        ],
        allocationName: [
          { required: true, message: '请输入分摊名称', trigger: 'blur' }
        ],
        allocationType: [
          { required: true, message: '请选择分摊类型', trigger: 'change' }
        ],
        allocationStatus: [
          { required: true, message: '请选择分摊状态', trigger: 'change' }
        ],
        effectiveDate: [
          { required: true, message: '请选择生效日期', trigger: 'change' }
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
