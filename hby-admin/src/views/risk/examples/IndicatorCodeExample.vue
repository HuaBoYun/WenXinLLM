<template>
  <div class="indicator-code-example">
    <el-card header="组合指标编码使用示例">
      <div class="example-section">
        <h3>当前页面信息</h3>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="当前路由">{{ $route.path }}</el-descriptions-item>
          <el-descriptions-item label="自动识别指标编码">{{ currentIndicatorCode }}</el-descriptions-item>
          <el-descriptions-item label="指标中文名称">{{ currentIndicatorName }}</el-descriptions-item>
          <el-descriptions-item label="组件名称">{{ $options.name }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <div class="example-section">
        <h3>手动指定指标编码</h3>
        <el-form :model="testForm" label-width="120px">
          <el-form-item label="指标编码">
            <el-select v-model="testForm.indicatorCode" placeholder="选择指标编码">
              <el-option
                v-for="(name, code) in indicatorCodeMap"
                :key="code"
                :label="`${code} - ${name}`"
                :value="code"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="数据源ID">
            <el-input v-model="testForm.dataSourceId" placeholder="请输入数据源ID" />
          </el-form-item>
          
          <el-form-item label="SQL内容">
            <el-input
              v-model="testForm.sqlContent"
              type="textarea"
              :rows="4"
              placeholder="请输入SQL语句"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="testSqlExecution" :loading="testing">
              测试SQL执行
            </el-button>
            <el-button @click="resetForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="example-section" v-if="testResult">
        <h3>执行结果</h3>
        <el-alert
          :title="testResult.success ? '执行成功' : '执行失败'"
          :type="testResult.success ? 'success' : 'error'"
          :description="testResult.message"
          show-icon
        />
        
        <div v-if="testResult.data" class="result-data">
          <h4>返回数据：</h4>
          <pre>{{ JSON.stringify(testResult.data, null, 2) }}</pre>
        </div>
      </div>

      <div class="example-section">
        <h3>指标编码映射表</h3>
        <el-table :data="indicatorTableData" border>
          <el-table-column prop="code" label="指标编码" width="300" />
          <el-table-column prop="name" label="中文名称" />
          <el-table-column prop="description" label="说明" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import { testDataModelSql } from '@/api/mxgl'
import { 
  getIndicatorCodeByRoute, 
  getIndicatorName, 
  INDICATOR_CODE_MAP 
} from '@/utils/indicatorCode'

export default {
  name: 'IndicatorCodeExample',
  data() {
    return {
      testForm: {
        indicatorCode: '',
        dataSourceId: '5504842711104fe39f8c3dd36866295c',
        sqlContent: 'SELECT COUNT(*) as total FROM TBL_CONTRACT_INFO'
      },
      testing: false,
      testResult: null
    }
  },
  computed: {
    currentIndicatorCode() {
      return getIndicatorCodeByRoute(this.$route)
    },
    
    currentIndicatorName() {
      return getIndicatorName(this.currentIndicatorCode)
    },
    
    indicatorCodeMap() {
      return INDICATOR_CODE_MAP
    },
    
    indicatorTableData() {
      return Object.entries(INDICATOR_CODE_MAP).map(([code, name]) => ({
        code,
        name,
        description: this.getIndicatorDescription(code)
      }))
    }
  },
  mounted() {
    // 设置默认指标编码
    this.testForm.indicatorCode = this.currentIndicatorCode
  },
  methods: {
    async testSqlExecution() {
      if (!this.testForm.sqlContent.trim()) {
        this.$message.error('请输入SQL语句')
        return
      }
      
      try {
        this.testing = true
        this.testResult = null
        
        const response = await testDataModelSql({
          dataSourceId: this.testForm.dataSourceId,
          sqlContent: this.testForm.sqlContent,
          parameters: '{}',
          indicatorCode: this.testForm.indicatorCode
        })
        
        this.testResult = {
          success: response.code === 1,
          message: response.msg,
          data: response.data
        }
        
        if (response.code === 1) {
          this.$message.success('SQL执行成功')
        } else {
          this.$message.error(response.msg || 'SQL执行失败')
        }
      } catch (error) {
        console.error('SQL执行失败:', error)
        this.testResult = {
          success: false,
          message: error.message || '网络错误',
          data: null
        }
        this.$message.error('SQL执行失败')
      } finally {
        this.testing = false
      }
    },
    
    resetForm() {
      this.testForm = {
        indicatorCode: this.currentIndicatorCode,
        dataSourceId: '5504842711104fe39f8c3dd36866295c',
        sqlContent: 'SELECT COUNT(*) as total FROM TBL_CONTRACT_INFO'
      }
      this.testResult = null
    },
    
    getIndicatorDescription(code) {
      const descriptions = {
        'CONTRACT_RISK_ANALYSIS': '用于合同风险分析相关的SQL执行',
        'SUPPLIER_EVALUATION': '用于供应商风险评估相关的SQL执行',
        'PROCUREMENT_SPLIT_WARNING': '用于采购拆分预警相关的SQL执行',
        'DEPARTMENT_FREQUENCY_WARNING': '用于部门申请频次预警相关的SQL执行',
        'DATA_MODEL_MANAGEMENT': '用于数据模型管理相关的SQL执行',
        'DATA_MODEL_TEST': '用于数据模型测试相关的SQL执行',
        'EVALUATION_MODEL_TEST': '用于评估模型测试相关的SQL执行',
        'DEFAULT_INDICATOR': '默认指标，用于未明确分类的SQL执行'
      }
      return descriptions[code] || '暂无说明'
    }
  }
}
</script>

<style scoped>
.indicator-code-example {
  padding: 20px;
}

.example-section {
  margin-bottom: 30px;
}

.example-section h3 {
  margin-bottom: 15px;
  color: #303133;
  border-left: 4px solid #409EFF;
  padding-left: 10px;
}

.result-data {
  margin-top: 15px;
}

.result-data pre {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  overflow-x: auto;
  font-size: 12px;
  line-height: 1.5;
}
</style>
