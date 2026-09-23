<template>
  <div class="app-container">
    <h1>融资管理模块</h1>
    <p>融资管理模块主页面</p>
    
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="module-card" @click.native="navigateTo('/globalTreasurer/financing/config')">
          <div class="card-content">
            <i class="el-icon-setting"></i>
            <h3>基础配置管理</h3>
            <p>融资基础参数、债券类别管理、授信类别管理</p>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="module-card" @click.native="navigateTo('/globalTreasurer/financing/credit')">
          <div class="card-content">
            <i class="el-icon-document"></i>
            <h3>授信业务管理</h3>
            <p>授信申请、合同管理、额度管理、评估监控</p>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="module-card" @click.native="navigateTo('/globalTreasurer/financing/guarantee')">
          <div class="card-content">
            <i class="el-icon-lock"></i>
            <h3>担保业务管理</h3>
            <p>担保申请、合同管理、物权登记、监控分析</p>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="module-card" @click.native="navigateTo('/globalTreasurer/financing/monitoring')">
          <div class="card-content">
            <i class="el-icon-data-analysis"></i>
            <h3>融资监控分析</h3>
            <p>风险监控、融资分析、报表管理、综合分析</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'FinancingManagement',
  data() {
    return {}
  },
  methods: {
    navigateTo(path) {
      this.$router.push(path)
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.module-card {
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 20px;
}

.module-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.card-content {
  text-align: center;
  padding: 20px;
}

.card-content i {
  font-size: 48px;
  color: #409EFF;
  margin-bottom: 15px;
}

.card-content h3 {
  margin: 15px 0 10px 0;
  color: #303133;
}

.card-content p {
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
}
</style>
