<template>
  <el-dialog
    :title="isEdit ? '编辑SQL模板' : '新增SQL模板'"
    :visible.sync="visible"
    width="800px"
    :before-close="handleClose"
    append-to-body
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      size="small"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="模板名称" prop="templateName">
            <el-input v-model="form.templateName" placeholder="请输入模板名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模板编码" prop="templateCode">
            <el-input v-model="form.templateCode" placeholder="请输入模板编码" />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据源" prop="dataSourceId">
            <el-select
              v-model="form.dataSourceId"
              placeholder="请选择数据源"
              style="width: 100%"
              filterable
              @focus="loadDataSources"
              @change="handleDataSourceChange"
            >
              <el-option
                v-for="item in dataSourceOptions"
                :key="item.sourceId"
                :label="item.sourceName"
                :value="item.sourceId"
              >
                <span style="float: left">{{ item.sourceName }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">{{ item.sourceType }}</span>
              </el-option>
            </el-select>
            <!-- 调试信息 -->
            <div style="font-size: 12px; color: #999; margin-top: 5px;">
              当前数据源ID: {{ form.dataSourceId }} | 数据源选项数量: {{ dataSourceOptions.length }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模板类型" prop="templateType">
            <el-select v-model="form.templateType" placeholder="请选择模板类型" style="width: 100%">
              <el-option label="查询(SELECT)" value="SELECT" />
              <el-option label="插入(INSERT)" value="INSERT" />
              <el-option label="更新(UPDATE)" value="UPDATE" />
              <el-option label="删除(DELETE)" value="DELETE" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="分类" prop="category">
            <el-select
              v-model="form.category"
              placeholder="请选择分类"
              style="width: 100%"
              clearable
              @change="handleCategoryChange"
              @focus="handleCategoryFocus"
            >
              <el-option label="基础查询" value="BASIC" />
              <el-option label="统计分析" value="ANALYSIS" />
              <el-option label="报表查询" value="REPORT" />
              <el-option label="数据维护" value="MAINTENANCE" />
            </el-select>
            <!-- 调试信息 -->
            <div style="font-size: 12px; color: #999; margin-top: 5px;">
              当前分类值: {{ form.category }}
            </div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <!-- 预留位置，状态字段在下方使用单选按钮 -->
        </el-col>
      </el-row>
      
      <el-form-item label="模板描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入模板描述"
        />
      </el-form-item>
      
      <el-form-item label="SQL内容" prop="sqlContent">
        <el-input
          v-model="form.sqlContent"
          type="textarea"
          :rows="8"
          placeholder="请输入SQL语句，使用${参数名}定义参数"
        />
        <div class="sql-help">
          <el-button type="text" @click="showSqlHelp">SQL语法帮助</el-button>
          <el-button type="text" @click="validateSql">验证SQL</el-button>
          <el-button type="text" @click="formatSql">格式化SQL</el-button>
          <el-button type="text" @click="testSqlExecution" :loading="testLoading">
            <i class="el-icon-video-play"></i> 测试执行
          </el-button>
        </div>
      </el-form-item>
      
      <el-form-item label="参数定义">
        <el-table :data="form.parameters" border style="width: 100%">
          <el-table-column prop="name" label="参数名" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.name" placeholder="参数名" />
            </template>
          </el-table-column>
          <el-table-column prop="type" label="类型" width="100">
            <template slot-scope="scope">
              <el-select v-model="scope.row.type" placeholder="类型">
                <el-option label="String" value="STRING" />
                <el-option label="Number" value="NUMBER" />
                <el-option label="Date" value="DATE" />
                <el-option label="Boolean" value="BOOLEAN" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="value" label="测试值" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.value" placeholder="测试时使用的值" />
            </template>
          </el-table-column>
          <el-table-column prop="description" label="说明">
            <template slot-scope="scope">
              <el-input v-model="scope.row.description" placeholder="参数说明" />
            </template>
          </el-table-column>
          <el-table-column prop="example" label="示例" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.example" placeholder="示例值" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="text" @click="removeParameter(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 10px">
          <el-button type="text" @click="addParameter">+ 添加参数</el-button>
          <el-button type="text" @click="autoAddParametersFromSql" style="margin-left: 10px">
            <i class="el-icon-magic-stick"></i> 从SQL自动提取参数
          </el-button>
          <el-button type="text" @click="addTestParameters" style="margin-left: 10px">
            <i class="el-icon-plus"></i> 添加测试参数
          </el-button>
        </div>
      </el-form-item>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio label="DRAFT">草稿</el-radio>
              <el-radio label="ACTIVE">启用</el-radio>
              <el-radio label="INACTIVE">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否公开" prop="isPublic">
            <el-switch v-model="form.isPublic" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">确定</el-button>
    </div>
    
    <!-- SQL帮助对话框 -->
    <el-dialog
      title="SQL语法帮助"
      :visible.sync="helpVisible"
      width="600px"
      append-to-body
    >
      <div class="help-content">
        <h4>参数定义</h4>
        <p>使用 ${参数名} 的格式定义参数，如：${tableName}、${conditions}</p>
        
        <h4>常用SQL模式</h4>
        <ul>
          <li><strong>查询：</strong>SELECT ${columns} FROM ${tableName} WHERE ${conditions}</li>
          <li><strong>插入：</strong>INSERT INTO ${tableName} (${columns}) VALUES (${values})</li>
          <li><strong>更新：</strong>UPDATE ${tableName} SET ${setClause} WHERE ${conditions}</li>
          <li><strong>删除：</strong>DELETE FROM ${tableName} WHERE ${conditions}</li>
        </ul>
        
        <h4>达梦数据库特性</h4>
        <ul>
          <li>分页查询：OFFSET ${offset} ROWS FETCH NEXT ${pageSize} ROWS ONLY</li>
          <li>当前时间：SYSDATE</li>
          <li>字符串连接：CONCAT(str1, str2) 或 str1 || str2</li>
        </ul>
        
        <h4>示例模板</h4>
        <pre>
SELECT 
    USER_ID,
    USER_NAME,
    CREATE_TIME
FROM TBL_USER
WHERE STATUS = ${status}
    AND CREATE_TIME >= ${startDate}
ORDER BY CREATE_TIME DESC
OFFSET ${offset} ROWS FETCH NEXT ${pageSize} ROWS ONLY
        </pre>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="helpVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- SQL测试结果对话框 -->
    <el-dialog
      title="SQL测试结果"
      :visible.sync="testResultVisible"
      width="900px"
      append-to-body
      :before-close="handleTestResultClose"
    >
      <div class="test-result-content">
        <!-- 测试信息 -->
        <div class="test-info">
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="info-item">
                <span class="label">执行状态：</span>
                <el-tag :type="testResult.success ? 'success' : 'danger'">
                  {{ testResult.success ? '成功' : '失败' }}
                </el-tag>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">执行时间：</span>
                <span class="value">{{ testResult.executeTime || 0 }}ms</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <span class="label">影响行数：</span>
                <span class="value">{{ testResult.affectedRows || 0 }}</span>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 错误信息 -->
        <div v-if="!testResult.success && testResult.errorMessage" class="error-section">
          <h4>错误信息</h4>
          <el-alert
            :title="testResult.errorMessage"
            type="error"
            :closable="false"
            show-icon
          />
        </div>

        <!-- 查询结果 -->
        <div v-if="testResult.success && testResult.data && testResult.data.length > 0" class="result-section">
          <h4>查询结果 (共{{ testResult.data.length }}条{{ testResult.data.length >= 1000 ? '，已截取前1000条' : '' }})</h4>
          <el-table
            :data="testResult.data.slice(0, 200)"
            border
            stripe
            size="mini"
            max-height="400"
            style="width: 100%"
          >
            <el-table-column
              v-for="column in testResult.columns"
              :key="column"
              :prop="column"
              :label="column"
              :min-width="120"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <span>{{ formatCellValue(scope.row[column]) }}</span>
              </template>
            </el-table-column>
          </el-table>

          <div v-if="testResult.data.length > 200" class="result-tip">
            <el-alert
              :title="`为了页面性能，仅显示前200条记录。实际查询返回${testResult.data.length}条数据。`"
              type="info"
              :closable="false"
              show-icon
            />
          </div>
        </div>

        <!-- 执行成功但无数据 -->
        <div v-if="testResult.success && (!testResult.data || testResult.data.length === 0)" class="no-data-section">
          <el-empty description="SQL执行成功，但没有返回数据" />
        </div>

        <!-- 执行的SQL语句 -->
        <div class="sql-section">
          <h4>执行的SQL语句</h4>
          <el-input
            v-model="testResult.executedSql"
            type="textarea"
            :rows="4"
            readonly
            class="sql-display"
          />
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="testResultVisible = false">关闭</el-button>
        <el-button type="primary" @click="copyTestSql">复制SQL</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { saveSqlTemplate, getDataSourceList, testSqlExecution } from '@/api/mxgl'

export default {
  name: 'SqlTemplateEditDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    templateData: {
      type: Object,
      default: () => ({})
    },
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      helpVisible: false,
      testLoading: false,
      testResultVisible: false,
      dataSourceOptions: [], // 数据源选项
      testResult: {
        success: false,
        executeTime: 0,
        affectedRows: 0,
        errorMessage: '',
        data: [],
        columns: [],
        executedSql: ''
      },
      form: {
        templateId: '',
        templateName: '',
        templateCode: '',
        dataSourceId: '', // 新增数据源ID
        templateType: 'SELECT',
        category: 'BASIC',
        description: '',
        sqlContent: '',
        parameters: [],
        status: 'ACTIVE', // 默认启用状态
        isPublic: false
      },
      rules: {
        templateName: [
          { required: true, message: '请输入模板名称', trigger: 'blur' }
        ],
        templateCode: [
          { required: true, message: '请输入模板编码', trigger: 'blur' }
        ],
        dataSourceId: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ],
        templateType: [
          { required: true, message: '请选择模板类型', trigger: 'change' }
        ],
        category: [
          { required: true, message: '请选择分类', trigger: 'change' }
        ],
        sqlContent: [
          { required: true, message: '请输入SQL内容', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    // isEdit现在是prop，不需要computed
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadDataSources() // 先加载数据源
        this.initForm()
        // 强制更新表单
        this.$nextTick(() => {
          this.$forceUpdate()
        })
      }
    },
    templateData: {
      handler(newVal) {
        if (this.visible && this.isEdit) {
          this.loadDataSources() // 编辑时也要加载数据源
          this.initForm()
        }
      },
      deep: true
    }
  },
  methods: {
    initForm() {
      // 先重置表单验证状态
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })

      if (this.isEdit) {
        this.form = { ...this.templateData }
        this.form.parameters = this.templateData.parameters || []
        console.log('编辑模式，表单数据:', this.form)
      } else {
        this.form = {
          templateId: '',
          templateName: '',
          templateCode: '',
          dataSourceId: '',
          templateType: 'SELECT',
          category: 'BASIC',
          description: '',
          sqlContent: '',
          parameters: [],
          status: 'ACTIVE',
          isPublic: false
        }
        console.log('新增模式，表单数据:', this.form)
      }
    },

    // 加载数据源列表
    async loadDataSources() {
      try {
        console.log('开始加载数据源列表...')
        const response = await getDataSourceList({
          pageNum: 1,
          pageSize: 100,
          status: 'ACTIVE' // 只获取启用的数据源
        })

        console.log('数据源API响应:', response)
        if (response.code === 1 && response.data && response.data.records) {
          this.dataSourceOptions = response.data.records
          console.log('数据源选项已加载:', this.dataSourceOptions)
          console.log('当前表单数据源ID:', this.form.dataSourceId)
        } else {
          console.error('获取数据源列表失败:', response)
          this.$message.error('获取数据源列表失败')
        }
      } catch (error) {
        console.error('加载数据源失败:', error)
        this.$message.error('加载数据源失败')
      }
    },

    addParameter() {
      this.form.parameters.push({
        name: '',
        type: 'STRING',
        value: '',
        description: '',
        example: ''
      })
    },
    
    removeParameter(index) {
      this.form.parameters.splice(index, 1)
    },

    // 从SQL自动提取参数
    autoAddParametersFromSql() {
      if (!this.form.sqlContent) {
        this.$message.warning('请先输入SQL内容')
        return
      }

      // 使用正则表达式提取 ${参数名} 格式的参数
      const paramRegex = /\$\{([^}]+)\}/g
      const foundParams = new Set()
      let match

      while ((match = paramRegex.exec(this.form.sqlContent)) !== null) {
        foundParams.add(match[1])
      }

      if (foundParams.size === 0) {
        this.$message.info('SQL中没有找到参数占位符（${参数名}格式）')
        return
      }

      // 检查已存在的参数
      const existingParams = new Set(this.form.parameters.map(p => p.name))
      let addedCount = 0

      foundParams.forEach(paramName => {
        if (!existingParams.has(paramName)) {
          this.form.parameters.push({
            name: paramName,
            type: 'STRING',
            value: '',
            description: `从SQL自动提取的参数: ${paramName}`,
            example: ''
          })
          addedCount++
        }
      })

      if (addedCount > 0) {
        this.$message.success(`成功添加 ${addedCount} 个参数`)
      } else {
        this.$message.info('所有参数都已存在')
      }
    },

    // 添加测试参数（用于演示）
    addTestParameters() {
      const testParams = [
        { name: 'ENGINEERING_THRESHOLD', type: 'NUMBER', value: '4000000', description: '工程招标阈值', example: '4000000' },
        { name: 'MATERIAL_THRESHOLD', type: 'NUMBER', value: '1200000', description: '物资招标阈值', example: '1200000' },
        { name: 'SERVICE_THRESHOLD', type: 'NUMBER', value: '2000000', description: '服务招标阈值', example: '2000000' },
        { name: 'DEFAULT_THRESHOLD', type: 'NUMBER', value: '1000000', description: '默认招标阈值', example: '1000000' }
      ]

      const existingParams = new Set(this.form.parameters.map(p => p.name))
      let addedCount = 0

      testParams.forEach(param => {
        if (!existingParams.has(param.name)) {
          this.form.parameters.push(param)
          addedCount++
        }
      })

      if (addedCount > 0) {
        this.$message.success(`成功添加 ${addedCount} 个测试参数`)
      } else {
        this.$message.info('测试参数都已存在')
      }
    },
    
    showSqlHelp() {
      this.helpVisible = true
    },
    
    validateSql() {
      if (!this.form.sqlContent) {
        this.$message.warning('请先输入SQL内容')
        return
      }
      
      // 简单的SQL验证
      const sql = this.form.sqlContent.trim().toUpperCase()
      if (sql.startsWith('SELECT') || sql.startsWith('INSERT') || 
          sql.startsWith('UPDATE') || sql.startsWith('DELETE')) {
        this.$message.success('SQL语法检查通过')
      } else {
        this.$message.error('SQL语法可能有误，请检查')
      }
    },
    
    formatSql() {
      if (!this.form.sqlContent) {
        this.$message.warning('请先输入SQL内容')
        return
      }
      
      // 简单的SQL格式化
      let formatted = this.form.sqlContent
        .replace(/\s+/g, ' ')
        .replace(/,/g, ',\n    ')
        .replace(/\bFROM\b/gi, '\nFROM')
        .replace(/\bWHERE\b/gi, '\nWHERE')
        .replace(/\bAND\b/gi, '\n    AND')
        .replace(/\bOR\b/gi, '\n    OR')
        .replace(/\bORDER BY\b/gi, '\nORDER BY')
        .replace(/\bGROUP BY\b/gi, '\nGROUP BY')
        .replace(/\bHAVING\b/gi, '\nHAVING')
        .replace(/\bLIMIT\b/gi, '\nLIMIT')
        .replace(/\bOFFSET\b/gi, '\nOFFSET')
      
      this.form.sqlContent = formatted
      this.$message.success('SQL格式化完成')
    },

    // 测试SQL执行
    async testSqlExecution() {
      if (!this.form.sqlContent) {
        this.$message.warning('请先输入SQL内容')
        return
      }

      if (!this.form.dataSourceId) {
        this.$message.warning('请先选择数据源')
        return
      }

      // 检查SQL中是否有参数占位符
      const paramRegex = /\$\{([^}]+)\}/g
      const sqlParams = new Set()
      let match
      while ((match = paramRegex.exec(this.form.sqlContent)) !== null) {
        sqlParams.add(match[1])
      }

      // 如果SQL中有参数但参数表格为空，提示用户
      if (sqlParams.size > 0 && this.form.parameters.length === 0) {
        const result = await this.$confirm(
          `SQL中包含 ${sqlParams.size} 个参数占位符：${Array.from(sqlParams).join(', ')}。\n是否自动添加这些参数？`,
          '发现参数占位符',
          {
            confirmButtonText: '自动添加',
            cancelButtonText: '继续测试',
            type: 'warning'
          }
        ).catch(() => false)

        if (result) {
          this.autoAddParametersFromSql()
          this.$message.info('请填写参数值后再进行测试')
          return
        }
      }

      this.testLoading = true
      try {
        // 过滤掉空的参数
        const validParameters = (this.form.parameters || []).filter(param =>
          param.name && param.name.trim() &&
          param.value !== undefined && param.value !== null && param.value !== ''
        )

        const testData = {
          dataSourceId: this.form.dataSourceId,
          sqlContent: this.form.sqlContent,
          parameters: validParameters
        }

        console.log('测试SQL参数:', testData)
        console.log('原始参数:', this.form.parameters)
        console.log('有效参数:', validParameters)

        // 检查是否有未填写值的必需参数
        if (sqlParams.size > 0 && validParameters.length < sqlParams.size) {
          const definedParams = new Set(validParameters.map(p => p.name))
          const missingParams = Array.from(sqlParams).filter(p => !definedParams.has(p))

          if (missingParams.length > 0) {
            this.$message.warning(`以下参数缺少测试值：${missingParams.join(', ')}`)
          }
        }
        const response = await testSqlExecution(testData)
        console.log('测试SQL响应:', response)

        if (response.code === 1) {
          // 解析测试结果
          const result = response.data || {}
          this.testResult = {
            success: true,
            executeTime: result.executeTime || 0,
            affectedRows: result.affectedRows || 0,
            errorMessage: '',
            data: result.data || [],
            columns: result.columns || [],
            executedSql: result.executedSql || this.form.sqlContent
          }

          this.testResultVisible = true
          this.$message.success('SQL测试执行成功')
        } else {
          // 执行失败
          this.testResult = {
            success: false,
            executeTime: 0,
            affectedRows: 0,
            errorMessage: response.msg || '执行失败',
            data: [],
            columns: [],
            executedSql: this.form.sqlContent
          }

          this.testResultVisible = true
        }
      } catch (error) {
        console.error('测试SQL失败:', error)
        this.testResult = {
          success: false,
          executeTime: 0,
          affectedRows: 0,
          errorMessage: error.message || '网络错误',
          data: [],
          columns: [],
          executedSql: this.form.sqlContent
        }

        this.testResultVisible = true
      } finally {
        this.testLoading = false
      }
    },

    // 格式化单元格值
    formatCellValue(value) {
      if (value === null || value === undefined) {
        return 'NULL'
      }
      if (typeof value === 'object') {
        return JSON.stringify(value)
      }
      return String(value)
    },

    // 复制测试SQL
    copyTestSql() {
      if (this.testResult.executedSql) {
        navigator.clipboard.writeText(this.testResult.executedSql).then(() => {
          this.$message.success('SQL已复制到剪贴板')
        }).catch(() => {
          // 降级方案
          const textArea = document.createElement('textarea')
          textArea.value = this.testResult.executedSql
          document.body.appendChild(textArea)
          textArea.select()
          document.execCommand('copy')
          document.body.removeChild(textArea)
          this.$message.success('SQL已复制到剪贴板')
        })
      }
    },

    // 关闭测试结果对话框
    handleTestResultClose() {
      this.testResultVisible = false
      // 清空测试结果
      this.testResult = {
        success: false,
        executeTime: 0,
        affectedRows: 0,
        errorMessage: '',
        data: [],
        columns: [],
        executedSql: ''
      }
    },

    // 数据源变化处理
    handleDataSourceChange(value) {
      console.log('数据源选择变化:', value)
      this.form.dataSourceId = value
    },

    // 分类焦点处理
    handleCategoryFocus() {
      console.log('分类下拉框获得焦点，当前值:', this.form.category)
    },

    // 分类变化处理
    handleCategoryChange(value) {
      console.log('分类选择变化:', value)
      this.form.category = value
    },

    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            console.log('提交表单数据:', this.form)
            console.log('数据源ID:', this.form.dataSourceId)
            const response = await saveSqlTemplate(this.form)
            console.log('保存响应:', response)
            if (response.code === 1) {
              this.$message.success(this.isEdit ? '修改成功' : '新增成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(response.msg || '操作失败')
            }
          } catch (error) {
            console.error('保存失败:', error)
            this.$message.error('操作失败')
          } finally {
            this.loading = false
          }
        }
      })
    },
    
    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped>
.sql-help {
  margin-top: 5px;
  text-align: right;
}

.help-content h4 {
  color: #409EFF;
  margin-top: 20px;
  margin-bottom: 10px;
}

.help-content ul {
  margin-left: 20px;
}

.help-content pre {
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  margin-top: 10px;
  font-size: 12px;
}

.dialog-footer {
  text-align: right;
}

/* 测试结果样式 */
.test-result-content {
  max-height: 600px;
  overflow-y: auto;
}

.test-info {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.info-item .label {
  font-weight: bold;
  color: #606266;
  margin-right: 8px;
}

.info-item .value {
  color: #409EFF;
  font-weight: 500;
}

.error-section {
  margin-bottom: 20px;
}

.error-section h4 {
  color: #F56C6C;
  margin-bottom: 10px;
}

.result-section {
  margin-bottom: 20px;
}

.result-section h4 {
  color: #67C23A;
  margin-bottom: 10px;
}

.result-tip {
  margin-top: 10px;
}

.no-data-section {
  margin-bottom: 20px;
  text-align: center;
  padding: 20px;
}

.sql-section {
  margin-top: 20px;
}

.sql-section h4 {
  color: #409EFF;
  margin-bottom: 10px;
}

.sql-display {
  font-family: 'Courier New', monospace;
  font-size: 12px;
}

.sql-display .el-textarea__inner {
  background-color: #f5f5f5;
  border: 1px solid #dcdfe6;
}
</style>
