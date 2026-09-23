<template>
  <div class="voucher-management-container">
    <h1>凭证管理页面测试</h1>
    <p>如果您能看到这个页面，说明路由配置正确</p>
    <div class="test-content">
      <el-button type="primary" @click="testClick">测试按钮</el-button>
      <p>{{ message }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'VoucherManagementTest',
  data() {
    return {
      message: '凭证管理页面加载成功！'
    }
  },
  mounted() {
    console.log('凭证管理测试页面已挂载')
  },
  methods: {
    testClick() {
      this.$message.success('按钮点击成功！')
      this.message = '测试通过！页面功能正常'
    }
  }
}
</script>

<style scoped>
.voucher-management-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: 100vh;
}

.test-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-top: 20px;
}

h1 {
  color: #303133;
  margin-bottom: 10px;
}

p {
  color: #606266;
  margin: 10px 0;
}
</style>