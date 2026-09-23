<template>
  <el-dialog
    title="编辑业务货币"
    :visible.sync="visible"
    width="500px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="货币代码" prop="currencyCode">
        <el-input v-model="form.currencyCode" placeholder="请输入货币代码" />
      </el-form-item>
      <el-form-item label="货币名称" prop="currencyName">
        <el-input v-model="form.currencyName" placeholder="请输入货币名称" />
      </el-form-item>
      <el-form-item label="货币符号" prop="currencySymbol">
        <el-input v-model="form.currencySymbol" placeholder="请输入货币符号" />
      </el-form-item>
      <el-form-item label="小数位数" prop="decimalPlaces">
        <el-input-number v-model="form.decimalPlaces" :min="0" :max="8" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSave">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'YwhbEdit',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    data: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      form: {
        currencyCode: '',
        currencyName: '',
        currencySymbol: '',
        decimalPlaces: 2,
        status: 1,
        remark: ''
      },
      rules: {
        currencyCode: [
          { required: true, message: '请输入货币代码', trigger: 'blur' }
        ],
        currencyName: [
          { required: true, message: '请输入货币名称', trigger: 'blur' }
        ],
        currencySymbol: [
          { required: true, message: '请输入货币符号', trigger: 'blur' }
        ],
        decimalPlaces: [
          { required: true, message: '请输入小数位数', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    data: {
      handler(newVal) {
        if (newVal && Object.keys(newVal).length > 0) {
          this.form = { ...newVal }
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    handleClose() {
      this.$emit('update:visible', false)
      this.$refs.form.resetFields()
    },
    handleSave() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.$emit('save', this.form)
          this.handleClose()
        }
      })
    }
  }
}
</script>
