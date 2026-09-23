<template>
  <el-dialog
    title="执行业务规则"
    :visible.sync="visible"
    width="500px"
    @close="handleClose"
  >
    <div class="execute-container">
      <el-form :model="form" label-width="100px">
        <el-form-item label="规则名称">
          <span>{{ ruleName }}</span>
        </el-form-item>
        <el-form-item label="执行参数">
          <el-input
            v-model="form.parameters"
            type="textarea"
            :rows="3"
            placeholder="请输入执行参数（JSON格式）"
          />
        </el-form-item>
        <el-form-item label="执行模式">
          <el-radio-group v-model="form.mode">
            <el-radio label="test">测试模式</el-radio>
            <el-radio label="production">生产模式</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleExecute" :loading="executing">
        {{ executing ? '执行中...' : '执 行' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BusinessRuleExecuteDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    ruleName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      executing: false,
      form: {
        parameters: '',
        mode: 'test'
      }
    }
  },
  methods: {
    handleClose() {
      this.$emit('update:visible', false)
      this.form = {
        parameters: '',
        mode: 'test'
      }
    },
    handleExecute() {
      this.executing = true
      // 模拟执行
      setTimeout(() => {
        this.executing = false
        this.$message.success('规则执行成功')
        this.$emit('execute', this.form)
        this.handleClose()
      }, 2000)
    }
  }
}
</script>

<style scoped>
.execute-container {
  padding: 20px 0;
}
</style>
