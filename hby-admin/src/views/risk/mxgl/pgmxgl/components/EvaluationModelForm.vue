<template>
  <div class="evaluation-model-form">
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      size="medium"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="模型来源" prop="modelSource">
            <el-select v-model="form.modelSource" placeholder="请选择模型来源" style="width: 100%" @change="handleSourceChange">
              <el-option label="数据模型管理" value="DATA_MODEL" />
              <el-option label="表达式管理" value="EXPRESSION" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="选择模型" prop="sourceModelId">
            <el-select
              v-model="form.sourceModelId"
              :placeholder="getModelPlaceholder()"
              style="width: 100%"
              filterable
              :disabled="!form.modelSource"
              @focus="loadSourceModels"
              @change="handleModelChange"
            >
              <el-option
                v-for="model in sourceModels"
                :key="model.id"
                :label="`${model.name} (${model.code})`"
                :value="model.id"
              >
                <span style="float: left">{{ model.name }}</span>
                <span style="float: right; color: #8492a6; font-size: 12px">{{ model.source }}</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 显示选中模型的基本信息 -->
      <el-row :gutter="20" v-if="selectedModel">
        <el-col :span="24">
          <el-alert
            :title="`已选择: ${selectedModel.name}`"
            type="info"
            :description="`模型编码: ${selectedModel.code} | 模型类型: ${selectedModel.type}`"
            show-icon
            :closable="false"
          />
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="模型编码" prop="modelCode" required>
            <el-input
              v-model="form.modelCode"
              placeholder="请输入模型编码（如：EVAL_001）"
              maxlength="50"
              show-word-limit
              @blur="generateModelCodeIfEmpty"
            />
            <div class="form-tip">模型编码用于唯一标识评估模型</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型名称" prop="modelName" required>
            <el-input
              v-model="form.modelName"
              placeholder="请输入模型名称"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="业务场景" prop="businessScenario" required>
            <el-select v-model="form.businessScenario" placeholder="请选择业务场景" style="width: 100%">
              <el-option label="财务风险评估" value="财务风险评估" />
              <el-option label="采购风险评估" value="采购风险评估" />
              <el-option label="审计风险评估" value="审计风险评估" />
              <el-option label="综合风险评估" value="综合风险评估" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>

      <!-- 参数配置区域 -->
      <el-divider v-if="selectedModel && selectedModel.source === '指标组合分析'">
        <i class="el-icon-setting"></i> 参数配置
      </el-divider>

      <div v-if="selectedModel && selectedModel.source === '指标组合分析'" class="parameter-config-section">
        <el-alert
          title="参数配置说明"
          type="info"
          :closable="false"
          show-icon
        >
          <template slot="default">
            以下是从指标组合中获取的参数配置，您可以根据实际需要修改参数的默认值。
          </template>
        </el-alert>

        <!-- 参数加载中 -->
        <div v-if="loadingParameters" class="loading-parameters">
          <i class="el-icon-loading"></i> 正在加载参数配置...
        </div>

        <!-- 参数列表 -->
        <div v-else-if="parameters.length > 0" class="parameter-list">
          <el-table
            :data="parameters"
            border
            stripe
            size="medium"
            style="width: 100%; margin-top: 15px;"
          >
            <el-table-column
              prop="name"
              label="参数名"
              width="180"
            >
              <template slot-scope="scope">
                <el-tag size="small" type="info">{{ scope.row.name }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column
              prop="type"
              label="类型"
              width="100"
            >
              <template slot-scope="scope">
                <el-tag size="small" :type="getTypeTagType(scope.row.type)">
                  {{ scope.row.type }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column
              prop="defaultValue"
              label="默认值"
              min-width="150"
            >
              <template slot-scope="scope">
                <el-input
                  v-model="scope.row.defaultValue"
                  size="small"
                  placeholder="请输入默认值"
                  @change="handleParameterChange"
                />
              </template>
            </el-table-column>
            <el-table-column
              prop="description"
              label="说明"
              min-width="200"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <span style="color: #606266; font-size: 12px;">{{ scope.row.description }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 无参数提示 -->
        <el-empty
          v-else
          description="该指标组合暂无参数配置"
          :image-size="80"
        />
      </div>
    </el-form>

    <div class="form-footer">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        {{ isEdit ? '更新' : '保存' }}
      </el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EvaluationModelForm',
  props: {
    formData: {
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
      submitting: false,
      sourceModels: [], // 可选择的模型列表（数据模型或表达式）
      selectedModel: null, // 选中的模型信息
      loadingParameters: false, // 参数加载状态
      parameters: [], // 参数配置列表（数组格式）
      parameterConfig: null, // 参数配置（JSON字符串格式）
      form: {
        evalModelId: '', // 评估模型ID
        modelSource: '', // 模型来源：DATA_MODEL 或 EXPRESSION
        sourceModelId: '', // 来源模型ID
        modelCode: '', // 模型编码（必填）
        modelName: '', // 模型名称（必填）
        businessScenario: '', // 业务场景（必填）
        dataModelId: '', // 关联的数据模型ID
        version: '1.0', // 版本号，默认1.0
        totalWeight: 100.00, // 总权重，默认100
        status: 'DRAFT', // 状态，默认草稿
        isEnabled: 'N', // 是否启用，默认否
        remark: '', // 备注
        parameterConfig: null // 参数配置（JSON字符串）
      },
      rules: {
        modelSource: [
          { required: true, message: '请选择模型来源', trigger: 'change' }
        ],
        sourceModelId: [
          { required: true, message: '请选择模型', trigger: 'change' }
        ],
        modelCode: [
          { required: true, message: '请输入模型编码', trigger: 'blur' },
          { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' },
          { pattern: /^[A-Z0-9_]+$/, message: '模型编码只能包含大写字母、数字和下划线', trigger: 'blur' }
        ],
        modelName: [
          { required: true, message: '请输入模型名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        businessScenario: [
          { required: true, message: '请选择业务场景', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    formData: {
      handler(newVal) {
        if (newVal && Object.keys(newVal).length > 0) {
          this.form = { ...this.form, ...newVal }
          // 编辑模式下，需要回显模型来源和选择的模型
          if (this.isEdit) {
            this.initEditMode(newVal)
          }
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    // 初始化编辑模式
    async initEditMode(formData) {
      console.log('初始化编辑模式，数据:', formData)

      try {
        // 1. 根据dataModelId判断模型来源
        if (formData.dataModelId) {
          // 有dataModelId说明来源是数据模型管理
          this.form.modelSource = 'DATA_MODEL'
          this.form.sourceModelId = formData.dataModelId

          console.log('设置模型来源为DATA_MODEL，sourceModelId:', formData.dataModelId)

          // 加载数据模型列表
          await this.loadSourceModels()

          // 等待一下确保数据加载完成
          await this.$nextTick()

          // 设置选中的模型信息
          this.setSelectedModel(formData.dataModelId)

          // 🔧 修复：编辑模式下加载参数配置
          // 优先使用已保存的参数配置，如果没有则从指标组合加载
          if (formData.parameterConfig) {
            console.log('✅ 从已保存的数据加载参数配置')
            this.loadParametersFromSaved(formData.parameterConfig)
          } else if (formData.dataModelId && formData.dataModelId.startsWith('COMB')) {
            console.log('✅ 从指标组合加载参数配置')
            await this.loadCombinationParameters(formData.dataModelId)
          }
        } else {
          // 没有dataModelId，可能是表达式管理或其他来源
          console.log('未找到dataModelId，可能是表达式管理来源')
          // 可以根据其他字段判断来源类型
        }
      } catch (error) {
        console.error('初始化编辑模式失败:', error)
      }
    },

    // 🔧 新增：从已保存的参数配置加载
    loadParametersFromSaved(parameterConfigStr) {
      try {
        console.log('📥 开始加载已保存的参数配置:', parameterConfigStr)

        this.parameterConfig = parameterConfigStr

        // 将JSON字符串转换为数组格式
        const paramConfig = JSON.parse(parameterConfigStr)
        this.parameters = Object.keys(paramConfig).map(key => ({
          name: key,
          type: paramConfig[key].type || 'STRING',
          defaultValue: paramConfig[key].defaultValue || '',
          description: paramConfig[key].description || ''
        }))

        console.log('✅ 参数配置加载成功，参数数量:', this.parameters.length)
        console.log('✅ 参数列表:', this.parameters)
      } catch (error) {
        console.error('❌ 加载已保存的参数配置失败:', error)
        this.$message.warning('参数配置格式错误')
      }
    },

    // 设置选中的模型信息
    setSelectedModel(modelId) {
      const model = this.sourceModels.find(m => m.id === modelId)
      if (model) {
        this.selectedModel = model
        console.log('设置选中模型:', model)
      }
    },

    // 获取模型选择的占位符文本
    getModelPlaceholder() {
      if (this.form.modelSource === 'DATA_MODEL') {
        return '请选择数据模型管理中的模型'
      } else if (this.form.modelSource === 'EXPRESSION') {
        return '请选择表达式管理中的模型'
      }
      return '请先选择模型来源'
    },

    // 处理模型来源变化
    handleSourceChange() {
      // 编辑模式下不清空已选择的模型，避免影响回显
      if (!this.isEdit) {
        // 清空已选择的模型
        this.form.sourceModelId = ''
        this.selectedModel = null
        this.sourceModels = []
      }
    },

    // 加载来源模型列表
    async loadSourceModels() {
      if (!this.form.modelSource) {
        this.$message.warning('请先选择模型来源')
        return
      }

      try {
        if (this.form.modelSource === 'DATA_MODEL') {
          // 加载数据模型管理和指标组合分析的内容
          await this.loadDataModels()
        } else if (this.form.modelSource === 'EXPRESSION') {
          // 加载表达式管理的内容
          await this.loadExpressionModels()
        }
      } catch (error) {
        console.error('加载模型列表失败:', error)
        this.$message.error('获取模型列表失败')
      }
    },

    // 加载数据模型（包括数据模型管理和指标组合分析）
    async loadDataModels() {
      this.sourceModels = []
      console.log('开始加载数据模型...')

      try {
        // 1. 加载数据模型管理中的模型
        await this.loadDataModelManagement()

        // 2. 加载指标组合分析中的内容
        await this.loadCombinationAnalysis()

        console.log('最终加载的模型列表:', this.sourceModels)

        if (this.sourceModels.length === 0) {
          this.$message.warning('暂无可用的数据模型，请先在数据模型管理或指标组合分析中创建模型')
        }
      } catch (error) {
        console.error('加载数据模型失败:', error)
        this.$message.error('获取数据模型列表失败: ' + error.message)
      }
    },

    // 加载数据模型管理中的模型
    async loadDataModelManagement() {
      try {
        const { getAvailableDataModels } = await import('@/api/mxgl')
        console.log('📊 评估模型表单 - 调用 getAvailableDataModels API...')

        const dataModelResponse = await getAvailableDataModels({
          pageNum: 1,
          pageSize: 100,
          status: 'PUBLISHED'
        })

        console.log('📊 评估模型表单 - 数据模型API响应:', dataModelResponse)

        // 兼容两种响应格式：
        // 1. 标准格式: {code: 1, data: {records: [...], total: ...}}
        // 2. 直接格式: {records: [...], total: ...} (mock数据)
        let dataModels = []

        if (dataModelResponse && dataModelResponse.code === 1) {
          // 标准格式
          const records = dataModelResponse.data?.records || dataModelResponse.data?.list || []
          dataModels = records.map(model => ({
            id: model.modelId,  // 数据模型的主键是modelId
            name: model.modelName,
            code: model.modelCode,
            type: model.modelType,
            source: '数据模型管理'
          }))
          console.log('✅ 数据模型管理加载成功(标准格式):', dataModels.length, '个')
        } else if (dataModelResponse && dataModelResponse.records) {
          // 直接格式（mock数据）
          dataModels = dataModelResponse.records.map(model => ({
            id: model.modelId,
            name: model.modelName,
            code: model.modelCode,
            type: model.modelType,
            source: '数据模型管理'
          }))
          console.log('✅ 数据模型管理加载成功(直接格式):', dataModels.length, '个')
        } else if (Array.isArray(dataModelResponse)) {
          // 纯数组格式
          dataModels = dataModelResponse.map(model => ({
            id: model.modelId,
            name: model.modelName,
            code: model.modelCode,
            type: model.modelType,
            source: '数据模型管理'
          }))
          console.log('✅ 数据模型管理加载成功(数组格式):', dataModels.length, '个')
        } else {
          console.warn('⚠️ 数据模型API返回格式不匹配:', dataModelResponse)
        }

        this.sourceModels.push(...dataModels)
      } catch (error) {
        console.error('❌ 加载数据模型管理失败:', error)
      }
    },

    // 加载指标组合分析中的内容
    async loadCombinationAnalysis() {
      try {
        // 直接调用指标组合分析API，使用正确的路径和参数方式
        const request = (await import('@/utils/request')).default
        console.log('📊 评估模型表单 - 调用指标组合分析API...')

        const combinationResponse = await request({
          url: '/riskcontrol/model/combination/list',  // 添加网关前缀
          method: 'post',
          params: {  // 使用params而不是data，因为后端使用@RequestParam
            pageNum: 1,
            pageSize: 100,
            status: 'ACTIVE'
          },
          headers: {
            'Content-Type': 'application/json;charset=UTF-8'
          }
        })

        console.log('📊 评估模型表单 - 指标组合分析API响应:', combinationResponse)

        // 兼容两种响应格式：
        // 1. 标准格式: {code: 1, data: {records: [...], total: ...}}
        // 2. 直接格式: {records: [...], total: ...} (mock数据)
        let combinations = []

        if (combinationResponse && combinationResponse.code === 1) {
          // 标准格式
          const records = combinationResponse.data?.records || combinationResponse.data?.list || []
          combinations = records.map(combination => ({
            id: combination.combinationId,
            name: combination.combinationName,
            code: combination.combinationCode,
            type: 'COMBINATION',
            source: '指标组合分析',
            category: combination.category,
            indicatorCount: combination.indicatorCount
          }))
          console.log('✅ 指标组合分析加载成功(标准格式):', combinations.length, '个')
        } else if (combinationResponse && combinationResponse.records) {
          // 直接格式（mock数据）
          combinations = combinationResponse.records.map(combination => ({
            id: combination.combinationId,
            name: combination.combinationName,
            code: combination.combinationCode,
            type: 'COMBINATION',
            source: '指标组合分析',
            category: combination.category,
            indicatorCount: combination.indicatorCount
          }))
          console.log('✅ 指标组合分析加载成功(直接格式):', combinations.length, '个')
        } else if (Array.isArray(combinationResponse)) {
          // 纯数组格式
          combinations = combinationResponse.map(combination => ({
            id: combination.combinationId,
            name: combination.combinationName,
            code: combination.combinationCode,
            type: 'COMBINATION',
            source: '指标组合分析',
            category: combination.category,
            indicatorCount: combination.indicatorCount
          }))
          console.log('✅ 指标组合分析加载成功(数组格式):', combinations.length, '个')
        } else {
          console.warn('⚠️ 指标组合分析API返回格式不匹配:', combinationResponse)
        }

        this.sourceModels.push(...combinations)
      } catch (error) {
        console.error('❌ 加载指标组合分析失败:', error)
        // 不抛出错误，继续执行
        console.log('指标组合分析加载失败，但继续执行')
      }
    },

    // 加载表达式模型
    async loadExpressionModels() {
      try {
        // TODO: 实现表达式管理的API调用
        this.$message.info('表达式管理功能开发中')
        this.sourceModels = []
      } catch (error) {
        console.error('加载表达式模型失败:', error)
        this.$message.error('获取表达式模型列表失败')
      }
    },

    // 处理模型选择变化
    async handleModelChange(modelId) {
      const selectedModel = this.sourceModels.find(model => model.id === modelId)
      if (selectedModel) {
        this.selectedModel = selectedModel

        // 根据选中的模型自动设置业务场景
        if (selectedModel.type === 'FINANCIAL') {
          this.form.businessScenario = '财务风险评估'
        } else if (selectedModel.type === 'RISK') {
          this.form.businessScenario = '综合风险评估'
        } else if (selectedModel.type === 'AUDIT') {
          this.form.businessScenario = '审计风险评估'
        }

        // 如果是指标组合分析，加载参数配置
        if (selectedModel.source === '指标组合分析') {
          await this.loadCombinationParameters(modelId)
        } else {
          // 清空参数配置
          this.parameters = []
          this.parameterConfig = null
        }
      }
    },

    // 加载指标组合的参数配置
    async loadCombinationParameters(combinationId) {
      this.loadingParameters = true
      this.parameters = []
      this.parameterConfig = null

      try {
        console.log('🔍 开始加载指标组合参数配置，combinationId:', combinationId)

        // 调用获取组合详情的API
        const { getCombinationDetail } = await import('@/api/mxgl')
        const response = await getCombinationDetail(combinationId)

        console.log('📡 组合详情API响应:', response)

        if (response.code === 1 && response.data) {
          const combinationDetail = response.data

          // 获取参数配置
          if (combinationDetail.parameterConfig) {
            this.parameterConfig = combinationDetail.parameterConfig
            console.log('✅ 获取到参数配置:', this.parameterConfig)

            // 将JSON字符串转换为数组格式
            try {
              const paramConfig = JSON.parse(this.parameterConfig)
              this.parameters = Object.keys(paramConfig).map(key => ({
                name: key,
                type: paramConfig[key].type || 'STRING',
                defaultValue: paramConfig[key].defaultValue || '',
                description: paramConfig[key].description || ''
              }))
              console.log('✅ 参数配置转换为数组格式:', this.parameters)
            } catch (parseError) {
              console.error('❌ 解析参数配置失败:', parseError)
              this.$message.warning('参数配置格式错误')
            }
          } else {
            console.log('⚠️ 该组合没有参数配置')
            this.$message.info('该指标组合暂无参数配置')
          }
        } else {
          console.error('❌ 获取组合详情失败:', response.msg)
          this.$message.error('获取参数配置失败: ' + response.msg)
        }
      } catch (error) {
        console.error('❌ 加载参数配置异常:', error)
        this.$message.error('加载参数配置失败: ' + error.message)
      } finally {
        this.loadingParameters = false
      }
    },

    // 处理参数变化
    handleParameterChange() {
      // 将参数数组转换为JSON字符串格式
      const paramConfig = {}
      this.parameters.forEach(param => {
        paramConfig[param.name] = {
          type: param.type,
          defaultValue: param.defaultValue,
          description: param.description
        }
      })
      this.parameterConfig = JSON.stringify(paramConfig)
      this.form.parameterConfig = this.parameterConfig
      console.log('📝 参数配置已更新:', this.parameterConfig)
    },

    // 获取类型标签颜色
    getTypeTagType(type) {
      const typeMap = {
        'STRING': 'primary',
        'NUMBER': 'success',
        'DATE': 'warning',
        'BOOLEAN': 'danger',
        'ARRAY': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 提交表单
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitting = true

          // 设置关联的数据模型ID
          this.form.dataModelId = this.form.sourceModelId

          // 如果有参数配置，确保更新到form中
          if (this.parameterConfig) {
            this.form.parameterConfig = this.parameterConfig
          }

          // 构造提交数据，只包含后端需要的字段
          const submitData = {
            evalModelId: this.form.evalModelId,
            modelCode: this.form.modelCode,
            modelName: this.form.modelName,
            businessScenario: this.form.businessScenario,
            dataModelId: this.form.dataModelId,
            version: this.form.version,
            totalWeight: this.form.totalWeight,
            status: this.form.status,
            isEnabled: this.form.isEnabled,
            remark: this.form.remark,
            parameterConfig: this.form.parameterConfig // 包含参数配置
          }

          console.log('📤 提交评估模型数据:', submitData)
          this.$emit('submit', submitData)
          this.submitting = false
        } else {
          this.$message.error('请完善表单信息')
        }
      })
    },
    // 取消
    handleCancel() {
      this.$emit('cancel')
    },
    // 重置表单
    resetForm() {
      this.$refs.form.resetFields()
    },

    // 自动生成模型编码（如果为空）
    generateModelCodeIfEmpty() {
      if (!this.form.modelCode && this.form.modelName) {
        // 基于模型名称生成编码
        const timestamp = Date.now().toString().slice(-6)
        const prefix = 'EVAL'
        this.form.modelCode = `${prefix}_${timestamp}`
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.evaluation-model-form {
  .form-footer {
    text-align: right;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;

    .el-button {
      margin-left: 10px;
    }
  }

  .form-tip {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
    line-height: 1.4;
  }

  .model-info {
    background: #f5f7fa;
    padding: 8px 12px;
    border-radius: 4px;
    margin-top: 8px;
    font-size: 12px;
    color: #606266;
  }

  // 参数配置区域样式
  .parameter-config-section {
    margin-top: 15px;
    padding: 15px;
    background: #f9fafc;
    border-radius: 4px;
    border: 1px solid #e4e7ed;

    .loading-parameters {
      text-align: center;
      padding: 30px 0;
      color: #909399;
      font-size: 14px;

      i {
        margin-right: 8px;
        font-size: 16px;
      }
    }

    .parameter-list {
      ::v-deep .el-table {
        th {
          background-color: #f5f7fa;
          color: #606266;
          font-weight: 600;
        }

        .el-input__inner {
          border-radius: 4px;
        }
      }
    }

    ::v-deep .el-alert {
      margin-bottom: 15px;
    }

    ::v-deep .el-empty {
      padding: 20px 0;
    }
  }

  ::v-deep .el-divider {
    margin: 25px 0 20px 0;

    .el-divider__text {
      background-color: #fff;
      color: #409eff;
      font-weight: 600;
      font-size: 14px;
    }

    i {
      margin-right: 5px;
    }
  }
}
</style>
