<template>
  <el-dialog
    title="测试业务规则"
    :visible.sync="visible"
    width="600px"
    @close="handleClose"
  >
    <div class="test-container">
      <el-form :model="form" label-width="100px">
        <el-form-item label="规则名称">
          <span>{{ ruleName }}</span>
        </el-form-item>
        <el-form-item label="测试数据">
          <el-input
            v-model="form.testData"
            type="textarea"
            :rows="4"
            placeholder="请输入测试数据（JSON格式）"
          />
        </el-form-item>
      </el-form>
      
      <div v-if="testResult" class="test-result">
        <h4>测试结果：</h4>
        <el-alert
          :title="testResult.success ? '测试通过' : '测试失败'"
          :type="testResult.success ? 'success' : 'error'"
          :description="testResult.message"
          show-icon
        />
        <div v-if="testResult.details" class="result-details">
          <pre>{{ JSON.stringify(testResult.details, null, 2) }}</pre>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleTest" :loading="testing">
        {{ testing ? '测试中...' : '开始测试' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BusinessRuleTestDialog',
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
      testing: false,
      testResult: null,
      form: {
        testData: ''
      }
    }
  },
  methods: {
    handleClose() {
      this.$emit('update:visible', false)
      this.form = {
        testData: ''
      }
      this.testResult = null
    },
    handleTest() {
      if (!this.form.testData.trim()) {
        this.$message.warning('请输入测试数据')
        return
      }
      
      this.testing = true
      // 模拟测试
      setTimeout(() => {
        this.testing = false
        this.testResult = {
          success: true,
          message: '规则测试通过，所有条件均满足',
          details: {
            executionTime: '125ms',
            matchedRules: 3,
            processedRecords: 1
          }
        }
      }, 1500)
    }
  }
}
</script>

<style scoped>
.test-container {
  padding: 20px 0;
}

.test-result {
  margin-top: 20px;
  padding: 15px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.result-details {
  margin-top: 10px;
}

.result-details pre {
  background-color: #fff;
  padding: 10px;
  border-radius: 4px;
  font-size: 12px;
  max-height: 200px;
  overflow-y: auto;
}
</style>
