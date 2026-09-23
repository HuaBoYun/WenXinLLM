<template>
  <el-dialog
    :title="isEdit ? '编辑数据模型' : '新增数据模型'"
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
          <el-form-item label="模型名称" prop="modelName">
            <el-input v-model="form.modelName" placeholder="请输入模型名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型编码" prop="modelCode">
            <el-input v-model="form.modelCode" placeholder="请输入模型编码" />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据源" prop="dataSourceId">
            <el-select v-model="form.dataSourceId" placeholder="请选择数据源" style="width: 100%">
              <el-option
                v-for="item in dataSourceList"
                :key="item.sourceId"
                :label="item.sourceName"
                :value="item.sourceId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型类型" prop="modelType">
            <el-select v-model="form.modelType" placeholder="请选择模型类型" style="width: 100%">
              <el-option
                v-for="type in modelTypeList"
                :key="type.value"
                :label="type.label"
                :value="type.value"
              >
                <span>{{ type.label }}</span>
                <span style="float: right; color: #8492a6; font-size: 12px">{{ type.description }}</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="模型描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入模型描述"
        />
      </el-form-item>
      
      <el-form-item label="SQL语句" prop="sqlStatement">
        <div class="sql-input-container">
          <el-input
            v-model="form.sqlStatement"
            type="textarea"
            :rows="8"
            placeholder="请输入SQL语句，或点击右侧按钮选择模板"
          />
          <div class="sql-actions">
            <el-button type="primary" size="mini" @click="showTemplateDialog" icon="el-icon-document">
              选择SQL模板
            </el-button>
            <el-button type="success" size="mini" @click="showParameterDialog" icon="el-icon-setting" :disabled="!hasParameters">
              设置参数
            </el-button>
          </div>
        </div>
      </el-form-item>

      <!-- SQL参数配置 -->
      <el-form-item v-if="sqlParameters.length > 0" label="SQL参数">
        <div class="parameter-list">
          <div v-for="(param, index) in sqlParameters" :key="index" class="parameter-item">
            <el-row :gutter="10">
              <el-col :span="6">
                <el-input v-model="param.name" placeholder="参数名" size="mini" readonly />
              </el-col>
              <el-col :span="4">
                <el-select v-model="param.type" placeholder="类型" size="mini">
                  <el-option label="字符串" value="String" />
                  <el-option label="数字" value="Number" />
                  <el-option label="日期" value="Date" />
                  <el-option label="布尔" value="Boolean" />
                </el-select>
              </el-col>
              <el-col :span="6">
                <el-input v-model="param.defaultValue" placeholder="默认值" size="mini" />
              </el-col>
              <el-col :span="8">
                <el-input v-model="param.description" placeholder="参数描述" size="mini" />
              </el-col>
            </el-row>
          </div>
        </div>
      </el-form-item>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
              <el-option
                v-for="status in statusList"
                :key="status.value"
                :label="status.label"
                :value="status.value"
                :disabled="!canSetStatus(status.value)"
              >
                <span>{{ status.label }}</span>
                <span style="float: right; color: #8492a6; font-size: 12px">{{ status.description }}</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否启用" prop="enabled">
            <el-switch v-model="form.enabled" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">确定</el-button>
    </div>

    <!-- SQL模板选择对话框 -->
    <SqlTemplateSelectDialog
      :visible.sync="templateDialogVisible"
      @select="handleTemplateSelect"
    />

    <!-- SQL参数设置对话框 -->
    <SqlParameterDialog
      :visible.sync="parameterDialogVisible"
      :parameters="sqlParameters"
      :sql-content="form.sqlStatement"
      @confirm="handleParameterConfirm"
    />
  </el-dialog>
</template>

<script>
import { saveDataModel, getDataSourceList, getSqlTemplateDetail, useSqlTemplate, getDataModelTypes, getDataModelStatus } from '@/api/mxgl'
import SqlTemplateSelectDialog from './SqlTemplateSelectDialog'
import SqlParameterDialog from './SqlParameterDialog'

export default {
  name: 'DataModelEditDialog',
  components: {
    SqlTemplateSelectDialog,
    SqlParameterDialog
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    modelData: {
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
      dataSourceList: [],
      modelTypeList: [], // 模型类型列表
      statusList: [ // 状态列表 - 默认值
        {
          value: 'DRAFT',
          label: '草稿',
          description: '初始状态，可编辑修改',
          color: '#909399'
        },
        {
          value: 'TESTING',
          label: '测试中',
          description: '正在测试验证中',
          color: '#E6A23C'
        },
        {
          value: 'PUBLISHED',
          label: '已发布',
          description: '正式发布，可执行',
          color: '#67C23A'
        }
      ],
      templateDialogVisible: false,
      parameterDialogVisible: false,
      selectedTemplate: null,
      sqlParameters: [],
      parametersInitialized: false, // 标记参数是否已初始化
      form: {
        modelId: '',
        modelName: '',
        modelCode: '',
        dataSourceId: '',
        modelType: 'FINANCIAL',
        description: '',
        sqlStatement: '',
        status: 'DRAFT',
        enabled: true,
        templateId: '', // 关联的模板ID
        parameterConfig: '' // 参数配置JSON
      },
      rules: {
        modelName: [
          { required: true, message: '请输入模型名称', trigger: 'blur' }
        ],
        modelCode: [
          { required: true, message: '请输入模型编码', trigger: 'blur' }
        ],
        dataSourceId: [
          { required: true, message: '请选择数据源', trigger: 'change' }
        ],
        sqlStatement: [
          { required: true, message: '请输入SQL语句', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    hasParameters() {
      return this.sqlParameters.length > 0
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
        this.loadDataSources()
        this.loadModelTypes()
        this.loadStatusList()
      }
    },
    modelData: {
      handler(newVal) {
        if (newVal && this.visible) {
          this.initForm()
        }
      },
      deep: true,
      immediate: true
    },
    'form.sqlStatement': {
      handler(newVal) {
        // 只在参数未初始化或者是新增模式时才重新解析参数
        if (!this.parametersInitialized || !this.isEdit) {
          if (newVal) {
            this.parseSqlParameters(newVal)
          } else {
            this.sqlParameters = []
          }
        }
      },
      immediate: false
    }
  },
  methods: {
    // 状态流转控制
    canSetStatus(targetStatus) {
      const currentStatus = this.form.status

      // 新建模型时，只能设置为草稿
      if (!this.isEdit) {
        return targetStatus === 'DRAFT'
      }

      // 状态流转规则
      switch (targetStatus) {
        case 'DRAFT':
          // 草稿状态可以保持，或者从测试中回退到草稿
          return currentStatus === 'DRAFT' || currentStatus === 'TESTING'
        case 'TESTING':
          // 草稿可以进入测试中，测试中可以保持
          return currentStatus === 'DRAFT' || currentStatus === 'TESTING'
        case 'PUBLISHED':
          // 测试中可以发布，已发布可以保持
          return currentStatus === 'TESTING' || currentStatus === 'PUBLISHED'
        default:
          return false
      }
    },

    initForm() {
      if (this.isEdit && this.modelData.modelId) {
        this.form = { ...this.modelData }

        // 处理字段名称差异
        if (this.form.businessMeaning) {
          this.form.description = this.form.businessMeaning
        }

        // 处理启用状态字段差异：IS_ENABLED (Y/N) -> enabled (boolean)
        if (this.form.isEnabled !== undefined) {
          this.form.enabled = this.form.isEnabled === 'Y'
        }

        // 解析已有的参数配置
        if (this.form.parameterConfig) {
          try {
            this.sqlParameters = JSON.parse(this.form.parameterConfig)
            this.parametersInitialized = true
            console.log('编辑模式：解析到的参数配置', this.sqlParameters)
          } catch (error) {
            console.error('解析参数配置失败:', error)
            this.sqlParameters = []
            this.parametersInitialized = false
          }
        } else {
          // 如果没有参数配置，从SQL中解析参数
          if (this.form.sqlStatement) {
            this.parseSqlParameters(this.form.sqlStatement)
          }
          this.parametersInitialized = true
        }
      } else {
        this.form = {
          modelId: '',
          modelName: '',
          modelCode: '',
          dataSourceId: '',
          modelType: 'FINANCIAL',
          description: '',
          sqlStatement: '',
          status: 'DRAFT',
          enabled: true,
          templateId: '',
          parameterConfig: ''
        }
        this.sqlParameters = []
        this.parametersInitialized = false
      }
    },
    
    async loadDataSources() {
      try {
        console.log('📊 数据模型编辑 - 加载数据源列表')
        const response = await getDataSourceList({
          pageNum: 1,
          pageSize: 1000,
          status: 'ACTIVE'
        })
        console.log('📊 数据模型编辑 - 数据源响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1) {
          this.dataSourceList = response.data?.records || []
          console.log('✅ 数据源列表加载成功(标准格式):', this.dataSourceList.length, '个数据源')
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.dataSourceList = response.records || []
          console.log('✅ 数据源列表加载成功(直接格式):', this.dataSourceList.length, '个数据源')
        } else {
          this.$message.error(response.msg || '加载数据源失败')
          console.error('❌ 数据源列表加载失败:', response)
        }
      } catch (error) {
        console.error('❌ 加载数据源失败:', error)
        this.$message.error('加载数据源失败')
      }
    },

    // 加载模型类型列表
    async loadModelTypes() {
      try {
        console.log('📊 数据模型编辑 - 加载模型类型列表')
        const response = await getDataModelTypes()
        console.log('📊 数据模型编辑 - 模型类型响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1) {
          this.modelTypeList = response.data || []
          console.log('✅ 模型类型列表加载成功(标准格式):', this.modelTypeList.length, '个类型')
        } else if (response && Array.isArray(response)) {
          // 直接格式（mock数据）
          this.modelTypeList = response || []
          console.log('✅ 模型类型列表加载成功(直接格式):', this.modelTypeList.length, '个类型')
        } else {
          console.error('❌ 模型类型列表加载失败:', response)
          this.$message.error(response.msg || '获取模型类型失败')
        }
      } catch (error) {
        console.error('❌ 获取模型类型失败:', error)
        this.$message.error('获取模型类型失败')
      }
    },

    // 加载状态列表
    async loadStatusList() {
      try {
        console.log('📊 数据模型编辑 - 加载状态列表')
        const response = await getDataModelStatus()
        console.log('📊 数据模型编辑 - 状态列表响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1 && response.data && response.data.length > 0) {
          this.statusList = response.data
          console.log('✅ 状态列表加载成功(标准格式):', this.statusList.length, '个状态')
        } else if (response && Array.isArray(response) && response.length > 0) {
          // 直接格式（mock数据）
          this.statusList = response
          console.log('✅ 状态列表加载成功(直接格式):', this.statusList.length, '个状态')
        } else {
          console.log('⚠️ API返回数据为空，使用默认状态列表')
        }
      } catch (error) {
        console.error('❌ 获取状态列表失败:', error)
        console.log('⚠️ API调用失败，使用默认状态列表')
      }
    },

    // 显示模板选择对话框
    showTemplateDialog() {
      this.templateDialogVisible = true
    },

    // 显示参数设置对话框
    showParameterDialog() {
      if (this.sqlParameters.length === 0) {
        this.$message.warning('当前SQL中没有检测到参数')
        return
      }
      this.parameterDialogVisible = true
    },

    // 处理模板选择
    async handleTemplateSelect(template) {
      try {
        this.loading = true

        // 获取模板详情
        const response = await getSqlTemplateDetail(template.templateId)
        if (response.code === 1) {
          const templateDetail = response.data

          // 应用模板内容
          this.form.sqlStatement = templateDetail.sqlContent
          this.form.templateId = template.templateId
          this.selectedTemplate = templateDetail

          // 解析SQL中的参数
          this.parseSqlParameters(templateDetail.sqlContent)

          // 如果模板有预定义参数配置，使用它
          if (templateDetail.parameterConfig) {
            try {
              const predefinedParams = JSON.parse(templateDetail.parameterConfig)
              this.mergeSqlParameters(predefinedParams)
            } catch (error) {
              console.error('解析模板参数配置失败:', error)
            }
          }

          // 记录模板使用
          await useSqlTemplate(template.templateId)

          this.$message.success('模板应用成功')
          this.templateDialogVisible = false
        } else {
          this.$message.error(response.msg || '获取模板详情失败')
        }
      } catch (error) {
        console.error('应用模板失败:', error)
        this.$message.error('应用模板失败')
      } finally {
        this.loading = false
      }
    },

    // 解析SQL中的参数
    parseSqlParameters(sqlStatement) {
      const paramRegex = /\$\{(\w+)\}/g
      const foundParams = new Set()
      let match

      while ((match = paramRegex.exec(sqlStatement)) !== null) {
        foundParams.add(match[1])
      }

      // 如果是编辑模式且已有参数配置，则合并参数而不是覆盖
      if (this.isEdit && this.sqlParameters.length > 0) {
        // 合并模式：保留已有参数的配置，只添加新发现的参数
        const existingParamNames = new Set(this.sqlParameters.map(p => p.name))

        foundParams.forEach(paramName => {
          if (!existingParamNames.has(paramName)) {
            this.sqlParameters.push({
              name: paramName,
              type: 'String',
              defaultValue: '',
              description: '',
              required: true
            })
          }
        })

        // 移除SQL中不再存在的参数
        this.sqlParameters = this.sqlParameters.filter(param => foundParams.has(param.name))
      } else {
        // 新增模式：直接创建参数对象
        this.sqlParameters = Array.from(foundParams).map(paramName => ({
          name: paramName,
          type: 'String',
          defaultValue: '',
          description: '',
          required: true
        }))
      }
    },

    // 合并预定义参数配置
    mergeSqlParameters(predefinedParams) {
      predefinedParams.forEach(predefined => {
        const existing = this.sqlParameters.find(p => p.name === predefined.name)
        if (existing) {
          Object.assign(existing, predefined)
        }
      })
    },

    // 处理参数确认
    handleParameterConfirm(parameters) {
      this.sqlParameters = parameters
      this.form.parameterConfig = JSON.stringify(parameters)
      this.parameterDialogVisible = false
      this.$message.success('参数配置已保存')
    },

    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            // 确保参数配置被保存
            if (this.sqlParameters.length > 0) {
              this.form.parameterConfig = JSON.stringify(this.sqlParameters)
            }

            // 准备提交数据，处理字段转换
            const submitData = { ...this.form }

            // 转换启用状态：enabled (boolean) -> isEnabled (Y/N)
            if (submitData.enabled !== undefined) {
              submitData.isEnabled = submitData.enabled ? 'Y' : 'N'
              delete submitData.enabled
            }

            // 转换描述字段：description -> businessMeaning
            if (submitData.description) {
              submitData.businessMeaning = submitData.description
            }

            const response = await saveDataModel(submitData)
            if (response.code === 1) {
              // 检查是否为评估模型类型
              if (submitData.modelType === 'EVALUATION') {
                this.$message.success('评估模型创建成功！您可以在评估模型管理中配置参数并启动模型。')
                // 可以在这里添加跳转到评估模型管理的逻辑
                // this.$router.push('/risk/evaluation-model')
              } else {
                this.$message.success(this.isEdit ? '修改成功' : '新增成功')
              }
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(response.msg || '操作失败')
            }
          } catch (error) {
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
.dialog-footer {
  text-align: right;
}

.sql-input-container {
  position: relative;
}

.sql-actions {
  margin-top: 8px;
  text-align: right;
}

.sql-actions .el-button {
  margin-left: 8px;
}

.parameter-list {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
  background-color: #fafafa;
}

.parameter-item {
  margin-bottom: 8px;
}

.parameter-item:last-child {
  margin-bottom: 0;
}
</style>
