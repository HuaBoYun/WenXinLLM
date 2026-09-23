<template>
  <el-dialog title="出入库操作" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-descriptions :column="1" border v-if="data" style="margin-bottom:20px">
      <el-descriptions-item label="物料名称">{{ data.materialName }}</el-descriptions-item>
      <el-descriptions-item label="当前库存">{{ data.currentStock }} {{ data.unit }}</el-descriptions-item>
    </el-descriptions>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="操作类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio label="in">入库</el-radio>
          <el-radio label="out">出库</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="数量" prop="quantity">
        <el-input-number v-model="form.quantity" :min="1" style="width:100%" />
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确认操作</el-button>
    </span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'InventoryStockDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null } },
  data() {
    return {
      dialogVisible: this.visible, submitting: false,
      form: { type: 'in', quantity: 1 },
      rules: { quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }] }
    }
  },
  watch: { visible(val) { this.dialogVisible = val } },
  methods: {
    handleClose() { this.$emit('close') },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitting = true
        try {
          const res = await request({ url: '/monitor/v1/enterprise/supply/inventory/stockInOut', method: 'post', data: { id: this.data.id, ...this.form }, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
          if (res && res.result === 200) { this.$message.success('操作成功'); this.$emit('success') }
          else this.$message.error(res.msg || '操作失败')
        } catch (e) { this.$message.error('操作失败') } finally { this.submitting = false }
      })
    }
  }
}
</script>
