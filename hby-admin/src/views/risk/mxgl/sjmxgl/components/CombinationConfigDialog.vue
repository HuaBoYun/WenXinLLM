<template>
  <el-dialog
    :title="isEdit ? '编辑指标组合' : '新建指标组合'"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    :modal-append-to-body="false"
    :append-to-body="true"
    :z-index="3000"
    @close="handleClose"
  >
    <el-form
      ref="combinationForm"
      :model="formData"
      :rules="formRules"
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="组合名称" prop="combinationName">
            <el-input
              v-model="formData.combinationName"
              placeholder="请输入组合名称"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组合编码" prop="combinationCode">
            <el-input
              v-model="formData.combinationCode"
              placeholder="请输入组合编码"
              maxlength="50"
              :disabled="isEdit"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="领域分类" prop="category">
            <el-select v-model="formData.category" placeholder="请选择领域分类" style="width: 100%">
              <el-option label="投资穿透" value="INVESTMENT_PENETRATION" />
              <el-option label="产权穿透" value="PROPERTY_PENETRATION" />
              <el-option label="财务穿透" value="FINANCIAL_PENETRATION" />
              <el-option label="金融风险穿透" value="FINANCIAL_RISK_PENETRATION" />
              <el-option label="会计穿透" value="ACCOUNTING_PENETRATION" />
              <el-option label="薪酬分配" value="SALARY_DISTRIBUTION" />
              <el-option label="军品穿透" value="MILITARY_PENETRATION" />
              <el-option label="采购与供应链" value="PROCUREMENT_SUPPLY_CHAIN" />
              <el-option label="境外穿透" value="OVERSEAS_PENETRATION" />
              <el-option label="合同穿透" value="CONTRACT_PENETRATION" />
              <el-option label="行业穿透" value="INDUSTRY_PENETRATION" />
              <el-option label="资金穿透" value="FUND_PENETRATION" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="执行模式" prop="executionMode">
            <el-select v-model="formData.executionMode" placeholder="请选择执行模式" style="width: 100%">
              <el-option label="顺序执行" value="SEQUENCE">
                <span>顺序执行</span>
                <span style="float: right; color: #8492a6; font-size: 13px">按指标顺序依次执行</span>
              </el-option>
              <el-option label="并行执行" value="PARALLEL">
                <span>并行执行</span>
                <span style="float: right; color: #8492a6; font-size: 13px">所有指标同时执行</span>
              </el-option>
              <el-option label="混合执行" value="MIXED">
                <span>混合执行</span>
                <span style="float: right; color: #8492a6; font-size: 13px">部分顺序部分并行执行</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="组合描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入组合描述"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="参数配置">
        <div class="parameter-config">
          <div class="config-header">
            <span>全局参数配置</span>
            <div class="header-actions">
              <el-button type="text" size="mini" @click="addParameter">
                <i class="el-icon-plus"></i> 添加参数
              </el-button>
              <el-button type="text" size="mini" @click="extractIndicatorParameters" :loading="extracting">
                <i class="el-icon-magic-stick"></i> 提取指标参数
              </el-button>
              <el-button type="text" size="mini" @click="debugCombinationData" style="color: #909399;">
                <i class="el-icon-info"></i> 调试信息
              </el-button>
              <el-button type="text" size="mini" @click="reloadCombinationData" style="color: #67C23A;">
                <i class="el-icon-refresh"></i> 重新加载
              </el-button>
            </div>
          </div>
          
          <div v-if="formData.parameters && formData.parameters.length > 0" class="parameter-list">
            <div
              v-for="(param, index) in formData.parameters"
              :key="index"
              class="parameter-item"
            >
              <el-row :gutter="12">
                <el-col :span="6">
                  <el-input
                    v-model="param.name"
                    placeholder="参数名"
                    size="mini"
                  />
                </el-col>
                <el-col :span="4">
                  <el-select v-model="param.type" placeholder="类型" size="mini" style="width: 100%">
                    <el-option label="字符串" value="STRING" />
                    <el-option label="数字" value="NUMBER" />
                    <el-option label="日期" value="DATE" />
                    <el-option label="布尔" value="BOOLEAN" />
                  </el-select>
                </el-col>
                <el-col :span="6">
                  <el-input
                    v-model="param.defaultValue"
                    placeholder="默认值"
                    size="mini"
                  />
                </el-col>
                <el-col :span="6">
                  <el-input
                    v-model="param.description"
                    placeholder="参数说明"
                    size="mini"
                  />
                </el-col>
                <el-col :span="2">
                  <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-delete"
                    @click="removeParameter(index)"
                  />
                </el-col>
              </el-row>
            </div>
          </div>
          
          <div v-else class="empty-parameters">
            <p>暂无参数配置</p>
          </div>
        </div>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio label="DRAFT">草稿</el-radio>
          <el-radio label="ACTIVE">启用</el-radio>
          <el-radio label="INACTIVE">禁用</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="标签">
        <el-tag
          v-for="tag in formData.tags"
          :key="tag"
          closable
          @close="removeTag(tag)"
          style="margin-right: 8px;"
        >
          {{ tag }}
        </el-tag>
        <el-input
          v-if="inputVisible"
          ref="saveTagInput"
          v-model="inputValue"
          size="mini"
          style="width: 100px;"
          @keyup.enter.native="handleInputConfirm"
          @blur="handleInputConfirm"
        />
        <el-button v-else size="mini" @click="showInput">+ 添加标签</el-button>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :loading="saving" @click="handleSave">
        {{ isEdit ? '更新' : '创建' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveCombination, getCombinationDetail } from '@/api/mxgl'

export default {
  name: 'CombinationConfigDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    combination: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      saving: false,
      extracting: false, // 🔥 新增：提取参数的加载状态

      // 标签输入
      inputVisible: false,
      inputValue: '',
      
      // 表单数据
      formData: {
        combinationId: '',
        combinationName: '',
        combinationCode: '',
        category: '',
        executionMode: 'SEQUENCE',
        description: '',
        parameters: [],
        status: 'DRAFT',
        tags: []
      },
      
      // 表单验证规则
      formRules: {
        combinationName: [
          { required: true, message: '请输入组合名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        combinationCode: [
          { required: true, message: '请输入组合编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_]+$/, message: '编码只能包含大写字母、数字和下划线', trigger: 'blur' }
        ],
        category: [
          { required: true, message: '请选择领域分类', trigger: 'change' }
        ],
        executionMode: [
          { required: true, message: '请选择执行模式', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    isEdit() {
      return !!(this.combination && this.combination.combinationId)
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val) {
          this.initFormData()
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    // 初始化表单数据
    initFormData() {
      if (this.isEdit) {
        // 编辑模式：填充现有数据
        this.formData = {
          combinationId: this.combination.combinationId,
          combinationName: this.combination.combinationName,
          combinationCode: this.combination.combinationCode,
          category: this.combination.category,
          executionMode: this.combination.executionMode || 'SEQUENCE',
          description: this.combination.description || '',
          parameters: this.parseParameters(this.combination.parameterConfig),
          status: this.combination.status || 'DRAFT',
          tags: this.combination.tags ? this.combination.tags.split(',').filter(tag => tag.trim()) : []
        }
      } else {
        // 新建模式：重置表单
        this.formData = {
          combinationId: '',
          combinationName: '',
          combinationCode: '',
          category: '',
          executionMode: 'SEQUENCE',
          description: '',
          parameters: [],
          status: 'DRAFT',
          tags: []
        }
      }
      
      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.combinationForm) {
          this.$refs.combinationForm.clearValidate()
        }
      })
    },

    // 解析参数配置
    parseParameters(parameterConfig) {
      if (!parameterConfig) return []
      
      try {
        const config = typeof parameterConfig === 'string' ? JSON.parse(parameterConfig) : parameterConfig
        return Object.keys(config).map(key => ({
          name: key,
          type: config[key].type || 'STRING',
          defaultValue: config[key].defaultValue || '',
          description: config[key].description || ''
        }))
      } catch (error) {
        console.error('解析参数配置失败:', error)
        return []
      }
    },

    // 添加参数
    addParameter() {
      this.formData.parameters.push({
        name: '',
        type: 'STRING',
        defaultValue: '',
        description: ''
      })
    },

    // 移除参数
    removeParameter(index) {
      this.formData.parameters.splice(index, 1)
    },

    // 显示标签输入框
    showInput() {
      this.inputVisible = true
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },

    // 确认添加标签
    handleInputConfirm() {
      const inputValue = this.inputValue.trim()
      if (inputValue && !this.formData.tags.includes(inputValue)) {
        this.formData.tags.push(inputValue)
      }
      this.inputVisible = false
      this.inputValue = ''
    },

    // 移除标签
    removeTag(tag) {
      const index = this.formData.tags.indexOf(tag)
      if (index > -1) {
        this.formData.tags.splice(index, 1)
      }
    },

    // 保存
    async handleSave() {
      try {
        // 表单验证
        await this.$refs.combinationForm.validate()
        
        this.saving = true
        
        // 构建保存数据
        const saveData = {
          ...this.formData,
          parameterConfig: this.buildParameterConfig(),
          tags: this.formData.tags.join(',')
        }
        
        const response = await saveCombination(saveData)
        if (response.code === 1) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.$emit('saved', response.data)
          this.handleClose()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        if (error !== false) { // 不是表单验证错误
          console.error('保存组合失败:', error)
          this.$message.error('保存失败')
        }
      } finally {
        this.saving = false
      }
    },

    // 构建参数配置
    buildParameterConfig() {
      const config = {}
      this.formData.parameters.forEach(param => {
        if (param.name.trim()) {
          config[param.name] = {
            type: param.type,
            defaultValue: param.defaultValue,
            description: param.description
          }
        }
      })
      return JSON.stringify(config)
    },

    // 🔥 新增：提取指标参数 - 调用接口获取完整数据
    async extractIndicatorParameters() {
      if (!this.isEdit || !this.combination || !this.combination.combinationId) {
        this.$message.warning('请先选择要编辑的组合')
        return
      }

      try {
        this.extracting = true
        console.log('🔍 开始提取指标参数...')
        console.log('🔍 组合ID:', this.combination.combinationId)

        // 🔥 调用接口获取完整的组合详情数据
        const combinationDetail = await this.fetchCombinationDetail(this.combination.combinationId)
        console.log('📊 获取到的组合详情:', combinationDetail)

        if (!combinationDetail) {
          this.$message.error('获取组合详情失败，无法提取参数')
          return
        }

        // 🔥 从接口返回的完整数据中扫描SQL内容
        const allSqlContents = this.extractAllSqlFromObject(combinationDetail)
        console.log('📝 找到的所有SQL内容:', allSqlContents)

        // 🔥 特别检查indicators数组中的sqlContent字段
        if (combinationDetail.indicators && Array.isArray(combinationDetail.indicators)) {
          console.log('🔍 特别检查indicators数组:', combinationDetail.indicators)
          combinationDetail.indicators.forEach((indicator, index) => {
            if (indicator.sqlContent) {
              console.log(`📊 指标 ${index + 1} [${indicator.indicatorName || indicator.indicatorCode}]:`)
              console.log(`📝 SQL内容: ${indicator.sqlContent}`)

              // 确保这个SQL被包含在扫描结果中
              const existingSql = allSqlContents.find(sql => sql.content === indicator.sqlContent)
              if (!existingSql) {
                allSqlContents.push({
                  content: indicator.sqlContent,
                  source: `indicators[${index}].sqlContent`,
                  fieldName: 'sqlContent',
                  hasParameters: /\$\{[^}]+\}/g.test(indicator.sqlContent),
                  isSqlField: true,
                  indicatorName: indicator.indicatorName || indicator.indicatorCode || `指标${index + 1}`
                })
                console.log(`✅ 添加指标SQL到扫描结果: ${indicator.indicatorName}`)
              }
            }
          })
        }

        if (allSqlContents.length === 0) {
          this.$message.warning('在组合数据中未找到任何SQL内容，无法提取参数')
          console.warn('❌ 未找到SQL内容，组合数据可能不完整')
          console.warn('🔍 组合数据结构:', this.combination)
          return
        }

        // 🔥 新逻辑：从所有SQL内容中提取参数
        const allParameters = new Set()
        const parameterDetails = new Map()

        // 遍历所有找到的SQL内容，提取参数
        allSqlContents.forEach((sqlInfo, index) => {
          const displayName = sqlInfo.indicatorName || sqlInfo.source
          console.log(`📊 分析SQL ${index + 1}: ${displayName}`)
          console.log(`📝 SQL内容: ${sqlInfo.content.substring(0, 100)}...`)

          // 使用正则表达式提取 ${参数名} 格式的参数
          const paramRegex = /\$\{([^}]+)\}/g
          let match

          while ((match = paramRegex.exec(sqlInfo.content)) !== null) {
            const paramName = match[1]

            // 🔥 修复：排除步骤引用格式，只提取普通参数
            // 需要排除的引用格式（精确匹配）：
            // 1. STEP_N_RESULT - 例如：STEP_1_RESULT, STEP_2_RESULT
            // 2. PREV_RESULT - 引用上一个指标的结果
            // 3. MOCK_STEP_N_TABLE - 例如：MOCK_STEP_1_TABLE
            const isStepReference =
              /^STEP_\d+_RESULT$/i.test(paramName) ||           // STEP_1_RESULT, STEP_2_RESULT
              /^PREV_RESULT$/i.test(paramName) ||               // PREV_RESULT
              /^MOCK_STEP_\d+_TABLE$/i.test(paramName)          // MOCK_STEP_1_TABLE

            if (isStepReference) {
              console.log(`⏭️ 跳过步骤引用: ${paramName} (来源: ${displayName})`)
              continue // 跳过步骤引用，不添加到参数列表
            }

            allParameters.add(paramName)
            console.log(`🎯 发现参数: ${paramName} (来源: ${displayName})`)

            // 生成更友好的描述
            const friendlySource = sqlInfo.indicatorName ?
              `指标: ${sqlInfo.indicatorName}` :
              `字段: ${sqlInfo.source}`

            // 记录参数的详细信息
            if (!parameterDetails.has(paramName)) {
              parameterDetails.set(paramName, {
                name: paramName,
                type: 'STRING', // 默认类型
                defaultValue: '',
                description: friendlySource,
                sources: [friendlySource]
              })
            } else {
              // 如果参数已存在，添加来源
              const existing = parameterDetails.get(paramName)
              if (!existing.sources.includes(friendlySource)) {
                existing.sources.push(friendlySource)
                existing.description = existing.sources.join(', ')
              }
            }
          }
        })

        console.log('🎯 提取到的参数:', Array.from(allParameters))

        if (allParameters.size === 0) {
          this.$message.info('未在指标SQL中找到参数占位符（${参数名}格式）')
          return
        }

        // 检查已存在的参数，避免重复添加
        const existingParamNames = new Set(this.formData.parameters.map(p => p.name))
        let addedCount = 0

        // 将新参数添加到表单中
        allParameters.forEach(paramName => {
          if (!existingParamNames.has(paramName)) {
            const paramDetail = parameterDetails.get(paramName)
            this.formData.parameters.push({
              name: paramDetail.name,
              type: paramDetail.type,
              defaultValue: paramDetail.defaultValue,
              description: paramDetail.description
            })
            addedCount++
          }
        })

        if (addedCount > 0) {
          this.$message.success(`成功提取到 ${addedCount} 个新参数！`)
          console.log('✅ 参数提取完成，新增参数数量:', addedCount)
        } else {
          this.$message.info('所有参数都已存在，无需重复添加')
        }

      } catch (error) {
        console.error('❌ 提取指标参数失败:', error)
        this.$message.error('提取参数失败: ' + (error.message || '未知错误'))
      } finally {
        this.extracting = false
      }
    },

    // 🔥 新增：调试组合数据
    debugCombinationData() {
      if (!this.combination) {
        this.$message.warning('没有组合数据可调试')
        return
      }

      console.log('🔍 ===== 组合数据调试信息 =====')
      console.log('组合对象:', this.combination)
      console.log('组合ID:', this.combination.combinationId)
      console.log('组合名称:', this.combination.combinationName)

      // 🔥 检查组合数据的完整性
      console.log('🔍 检查组合数据完整性:')
      console.log('- isEdit:', this.isEdit)
      console.log('- combination存在:', !!this.combination)
      console.log('- combination类型:', typeof this.combination)
      console.log('- combination是否为空对象:', Object.keys(this.combination || {}).length === 0)

      // 检查所有可能的指标字段
      const possibleIndicatorFields = [
        'indicators',
        'indicatorConfigs',
        'configuredIndicators',
        'indicatorList',
        'configs'
      ]

      let foundIndicators = false
      possibleIndicatorFields.forEach(field => {
        if (this.combination[field]) {
          console.log(`✅ 找到指标字段 "${field}":`, this.combination[field])
          foundIndicators = true

          // 检查每个指标的结构
          if (Array.isArray(this.combination[field])) {
            this.combination[field].forEach((indicator, index) => {
              console.log(`📊 指标 ${index + 1} 结构:`, indicator)
              console.log(`📊 指标 ${index + 1} 所有字段:`, Object.keys(indicator))

              // 检查SQL字段
              const sqlFields = ['sqlContent', 'sql', 'sqlQuery', 'query']
              sqlFields.forEach(sqlField => {
                if (indicator[sqlField]) {
                  console.log(`📝 指标 ${index + 1} SQL字段 "${sqlField}":`, indicator[sqlField])
                }
              })
            })
          }
        } else {
          console.log(`❌ 未找到指标字段 "${field}"`)
        }
      })

      if (!foundIndicators) {
        console.log('❌ 未找到任何指标数据字段')
        console.log('📋 组合对象的所有字段:', Object.keys(this.combination))
      }

      // 显示调试信息对话框
      const debugInfo = {
        combinationId: this.combination.combinationId,
        combinationName: this.combination.combinationName,
        allFields: Object.keys(this.combination),
        indicatorFields: possibleIndicatorFields.filter(field => this.combination[field]),
        indicatorCount: this.getIndicatorCount()
      }

      this.$alert(`
        <div style="text-align: left; font-family: monospace; font-size: 12px;">
          <p><strong>组合ID:</strong> ${debugInfo.combinationId || '无'}</p>
          <p><strong>组合名称:</strong> ${debugInfo.combinationName || '无'}</p>
          <p><strong>所有字段:</strong> ${debugInfo.allFields.join(', ')}</p>
          <p><strong>找到的指标字段:</strong> ${debugInfo.indicatorFields.join(', ') || '无'}</p>
          <p><strong>指标数量:</strong> ${debugInfo.indicatorCount}</p>
          <hr>
          <p><strong>问题分析:</strong></p>
          <p style="color: red;">• 指标数量为0，说明组合没有加载到指标配置</p>
          <p style="color: orange;">• 可能原因：组合确实没有配置指标，或指标数据加载失败</p>
          <hr>
          <p><strong>解决建议:</strong></p>
          <p>1. <strong>检查组合配置:</strong> 返回主页面，确认组合是否有指标</p>
          <p>2. <strong>重新加载数据:</strong> 关闭对话框，重新选择组合</p>
          <p>3. <strong>检查网络请求:</strong> F12 > Network，查看API请求</p>
          <p>4. <strong>手动配置指标:</strong> 如果组合为空，先配置指标</p>
          <p>5. <strong>查看控制台:</strong> F12 > Console，查看详细日志</p>
        </div>
      `, '组合数据调试信息', {
        dangerouslyUseHTMLString: true,
        customClass: 'debug-info-dialog'
      })
    },

    // 🔥 新增：调用接口获取组合详情
    async fetchCombinationDetail(combinationId) {
      try {
        console.log(`🌐 调用接口获取组合详情: /riskcontrol/model/combination/detail/${combinationId}`)

        // 使用项目的API函数发送请求
        const response = await getCombinationDetail(combinationId)

        console.log('📡 接口响应:', response)

        if (response && response.code === 1) {
          console.log('✅ 成功获取组合详情数据')
          return response.data
        } else {
          console.error('❌ 接口返回错误:', response)
          this.$message.error(response?.msg || '获取组合详情失败')
          return null
        }
      } catch (error) {
        console.error('❌ 调用组合详情接口失败:', error)
        this.$message.error('网络请求失败: ' + (error.message || '未知错误'))
        return null
      }
    },

    // 🔥 新增：递归提取对象中的所有SQL内容
    extractAllSqlFromObject(obj, path = '', results = []) {
      if (!obj || typeof obj !== 'object') {
        return results
      }

      // 🔥 定义可能包含SQL的字段名
      const sqlFieldNames = [
        'sql', 'sqlContent', 'sqlQuery', 'query', 'sqlText', 'sqlStatement',
        'selectSql', 'insertSql', 'updateSql', 'deleteSql', 'script', 'command'
      ]

      // 遍历对象的所有属性
      for (const [key, value] of Object.entries(obj)) {
        const currentPath = path ? `${path}.${key}` : key

        if (typeof value === 'string') {
          // 🔥 检查字段名是否可能包含SQL
          const isLikelySqlField = sqlFieldNames.some(sqlField =>
            key.toLowerCase().includes(sqlField.toLowerCase())
          )

          // 🔥 检查内容是否包含SQL关键字或参数占位符
          const containsSqlKeywords = /\b(SELECT|INSERT|UPDATE|DELETE|FROM|WHERE|JOIN|GROUP BY|ORDER BY)\b/i.test(value)
          const containsParameters = /\$\{[^}]+\}/g.test(value)

          if ((isLikelySqlField || containsSqlKeywords || containsParameters) && value.trim().length > 0) {
            results.push({
              content: value,
              source: currentPath,
              fieldName: key,
              hasParameters: containsParameters,
              isSqlField: isLikelySqlField
            })
            console.log(`🔍 发现SQL内容: ${currentPath} = ${value.substring(0, 50)}...`)
          }
        } else if (Array.isArray(value)) {
          // 递归处理数组
          value.forEach((item, index) => {
            this.extractAllSqlFromObject(item, `${currentPath}[${index}]`, results)
          })
        } else if (typeof value === 'object' && value !== null) {
          // 递归处理对象
          this.extractAllSqlFromObject(value, currentPath, results)
        }
      }

      return results
    },

    // 🔥 新增：重新加载组合数据
    async reloadCombinationData() {
      if (!this.combination || !this.combination.combinationId) {
        this.$message.warning('没有组合ID，无法重新加载')
        return
      }

      try {
        this.$message.info('正在重新加载组合数据...')
        console.log('🔄 开始重新加载组合数据:', this.combination.combinationId)

        // 触发父组件重新加载数据
        this.$emit('reload-combination', this.combination.combinationId)

        // 等待一下让数据加载
        setTimeout(() => {
          console.log('🔄 重新加载完成，更新后的组合数据:', this.combination)
          this.$message.success('组合数据已重新加载，请再次尝试提取参数')
        }, 1000)

      } catch (error) {
        console.error('❌ 重新加载组合数据失败:', error)
        this.$message.error('重新加载失败: ' + (error.message || '未知错误'))
      }
    },

    // 🔥 辅助方法：获取指标数量
    getIndicatorCount() {
      if (!this.combination) return 0

      const possibleFields = ['indicators', 'indicatorConfigs', 'configuredIndicators', 'indicatorList', 'configs']
      for (const field of possibleFields) {
        if (this.combination[field] && Array.isArray(this.combination[field])) {
          return this.combination[field].length
        }
      }
      return 0
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.inputVisible = false
      this.inputValue = ''
    }
  }
}
</script>

<style scoped>
.parameter-config {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 16px;

  .config-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;
    font-weight: 600;

    .header-actions {
      display: flex;
      gap: 8px;
    }
  }

  .parameter-list {
    .parameter-item {
      margin-bottom: 8px;
      
      &:last-child {
        margin-bottom: 0;
      }
    }
  }

  .empty-parameters {
    text-align: center;
    color: #909399;
    padding: 20px 0;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
