<template>
  <el-dialog
    title="测试数据模型"
    :visible.sync="visible"
    width="1000px"
    :before-close="handleClose"
    append-to-body
  >
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane label="测试配置" name="config">
        <el-form ref="testForm" :model="testForm" label-width="120px" size="small">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="测试名称">
                <el-input v-model="testForm.testName" placeholder="请输入测试名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="测试类型">
                <el-select v-model="testForm.testType" placeholder="请选择测试类型" style="width: 100%">
                  <el-option label="功能测试" value="FUNCTION" />
                  <el-option label="性能测试" value="PERFORMANCE" />
                  <el-option label="压力测试" value="STRESS" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="测试参数">
            <el-table :data="testParams" border style="width: 100%">
              <el-table-column prop="paramName" label="参数名" width="120">
                <template slot-scope="scope">
                  <div>
                    <el-tag type="primary" size="mini">${{ scope.row.paramName }}</el-tag>
                    <div v-if="scope.row.required" class="param-required">
                      <el-tag type="danger" size="mini">必填</el-tag>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="paramType" label="类型" width="80">
                <template slot-scope="scope">
                  <el-tag size="mini">{{ scope.row.paramType || 'String' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="描述" width="150">
                <template slot-scope="scope">
                  <span class="param-desc">{{ scope.row.description || '无描述' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="paramValue" label="参数值">
                <template slot-scope="scope">
                  <el-input
                    v-model="scope.row.paramValue"
                    :placeholder="getParamPlaceholder(scope.row)"
                    size="mini"
                  />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="80">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="removeParam(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-button type="text" @click="addParam" style="margin-top: 10px">+ 添加参数</el-button>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="runTest" :loading="testing">执行测试</el-button>
            <el-button @click="clearResult">清空结果</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      
      <el-tab-pane label="测试结果" name="result">
        <div v-if="testResult">
          <el-descriptions :column="2" border style="margin-bottom: 20px">
            <el-descriptions-item label="测试状态">
              <el-tag :type="testResult.status === 'SUCCESS' ? 'success' : 'danger'">
                {{ testResult.status === 'SUCCESS' ? '成功' : '失败' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="执行时间">{{ testResult.executeTime }}ms</el-descriptions-item>
            <el-descriptions-item label="返回记录数">{{ testResult.recordCount }}</el-descriptions-item>
            <el-descriptions-item label="测试时间">{{ testResult.testTime }}</el-descriptions-item>
          </el-descriptions>
          
          <el-divider content-position="left">查询结果</el-divider>
          <el-table :data="testResult.data" border style="width: 100%" max-height="300">
            <el-table-column
              v-for="column in testResult.columns"
              :key="column.prop"
              :prop="column.prop"
              :label="column.label"
              :width="column.width"
            />
          </el-table>
        </div>
        <div v-else class="no-result">
          <el-empty description="暂无测试结果" />
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="执行日志" name="log">
        <div class="log-container">
          <el-input
            v-model="executeLogs"
            type="textarea"
            :rows="15"
            readonly
            placeholder="暂无执行日志"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { executeDataModel } from '@/api/mxgl'
import { getIndicatorCodeByRoute } from '@/utils/indicatorCode' // 🔥 新增

export default {
  name: 'DataModelTestDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    modelData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'config',
      testing: false,
      testForm: {
        testName: '',
        testType: 'FUNCTION'
      },
      testParams: [
        { paramName: '', paramValue: '' }
      ],
      testResult: null,
      executeLogs: ''
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initTest()
      }
    }
  },
  methods: {
    initTest() {
      // 设置测试名称
      this.testForm.testName = this.modelData.modelName ? `${this.modelData.modelName}_测试` : '未定义_测试'

      // 解析模型参数配置
      this.initTestParams()

      // 重置测试结果
      this.testResult = null
      this.executeLogs = ''
    },

    // 初始化测试参数
    initTestParams() {
      if (this.modelData.parameterConfig) {
        try {
          const parameterConfig = JSON.parse(this.modelData.parameterConfig)
          if (parameterConfig && parameterConfig.length > 0) {
            this.testParams = parameterConfig.map(param => ({
              paramName: param.name,
              paramValue: param.defaultValue || '',
              paramType: param.type || 'String',
              description: param.description || '',
              required: param.required || false
            }))
          } else {
            this.testParams = [{ paramName: '', paramValue: '' }]
          }
        } catch (error) {
          console.error('解析参数配置失败:', error)
          this.testParams = [{ paramName: '', paramValue: '' }]
        }
      } else {
        this.testParams = [{ paramName: '', paramValue: '' }]
      }
    },

    // 获取参数占位符
    getParamPlaceholder(param) {
      if (param.defaultValue) {
        return `默认值: ${param.defaultValue}`
      }

      const placeholders = {
        'String': '请输入字符串',
        'Number': '请输入数字',
        'Date': 'YYYY-MM-DD',
        'Boolean': 'true/false'
      }
      return placeholders[param.paramType] || '请输入参数值'
    },

    addParam() {
      this.testParams.push({
        paramName: '',
        paramValue: '',
        paramType: 'String',
        description: '',
        required: false
      })
    },
    
    removeParam(index) {
      this.testParams.splice(index, 1)
    },
    
    async runTest() {
      this.testing = true
      this.executeLogs = '开始执行测试...\n'
      
      try {
        const params = {}
        this.testParams.forEach(param => {
          if (param.paramName && param.paramValue) {
            params[param.paramName] = param.paramValue
          }
        })
        
        const startTime = Date.now()
        this.executeLogs += `执行SQL: ${this.modelData.sqlStatement || '未定义'}\n`
        this.executeLogs += `参数: ${JSON.stringify(params)}\n`

        const response = await executeDataModel({
          modelId: this.modelData.modelId,
          params: params,
          indicatorCode: getIndicatorCodeByRoute(this.$route) // 🔥 新增指标编码
        })
        
        const endTime = Date.now()
        const executeTime = endTime - startTime
        
        if (response.code === 1) {
          const resultData = response.data
          this.testResult = {
            status: 'SUCCESS',
            executeTime: resultData.executionTime || (executeTime / 1000 + 's'),
            recordCount: resultData.total || 0,
            testTime: new Date().toLocaleString(),
            data: resultData.data || [],
            columns: resultData.columns || this.generateColumns(resultData.data)
          }
          this.executeLogs += `执行成功，耗时: ${executeTime}ms\n`
          this.executeLogs += `返回记录数: ${response.data?.length || 0}\n`
          this.activeTab = 'result'
        } else {
          this.testResult = {
            status: 'FAILED',
            executeTime: executeTime,
            recordCount: 0,
            testTime: new Date().toLocaleString(),
            data: [],
            columns: []
          }
          this.executeLogs += `执行失败: ${response.msg}\n`
        }
      } catch (error) {
        this.executeLogs += `执行异常: ${error.message}\n`
        this.testResult = {
          status: 'FAILED',
          executeTime: 0,
          recordCount: 0,
          testTime: new Date().toLocaleString(),
          data: [],
          columns: []
        }
      } finally {
        this.testing = false
      }
    },
    
    generateColumns(data) {
      if (!data || data.length === 0) return []
      
      const firstRow = data[0]
      return Object.keys(firstRow).map(key => ({
        prop: key,
        label: key,
        width: 150
      }))
    },
    
    clearResult() {
      this.testResult = null
      this.executeLogs = ''
    },
    
    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped>
.log-container {
  margin: 10px 0;
}

.no-result {
  text-align: center;
  padding: 50px 0;
  color: #909399;
}

.param-required {
  margin-top: 2px;
}

.param-desc {
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
}

.el-table .el-tag {
  margin: 1px 0;
}

.dialog-footer {
  text-align: right;
}
</style>
