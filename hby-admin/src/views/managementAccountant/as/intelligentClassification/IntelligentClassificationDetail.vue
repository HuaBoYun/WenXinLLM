<template>
  <div class="intelligent-classification-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <el-button icon="el-icon-arrow-left" @click="handleBack">返回</el-button>
          <h1 class="page-title">
            {{ isEdit ? '编辑分类' : (isCreate ? '创建分类' : '分类详情') }}
          </h1>
        </div>
        <div class="header-right" v-if="!isCreate">
          <el-button type="primary" icon="el-icon-cpu" @click="handleTrain" v-if="canTrain">
            训练模型
          </el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleDeploy" v-if="canDeploy">
            部署模型
          </el-button>
          <el-button type="warning" icon="el-icon-magic-stick" @click="handlePredict" v-if="canPredict">
            预测分类
          </el-button>
        </div>
      </div>
    </div>

    <!-- 表单区域 -->
    <div class="form-container">
      <el-form
        ref="classificationForm"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        v-loading="loading"
      >
        <el-row :gutter="20">
          <!-- 基本信息 -->
          <el-col :span="12">
            <div class="form-section">
              <h3 class="section-title">基本信息</h3>
              
              <el-form-item label="分类编号" prop="classificationCode">
                <el-input
                  v-model="formData.classificationCode"
                  :disabled="!isCreate"
                  placeholder="请输入分类编号"
                />
              </el-form-item>

              <el-form-item label="分类名称" prop="classificationName">
                <el-input
                  v-model="formData.classificationName"
                  :disabled="!isEdit && !isCreate"
                  placeholder="请输入分类名称"
                />
              </el-form-item>

              <el-form-item label="分类描述" prop="classificationDescription">
                <el-input
                  type="textarea"
                  v-model="formData.classificationDescription"
                  :disabled="!isEdit && !isCreate"
                  :rows="3"
                  placeholder="请输入分类描述"
                />
              </el-form-item>

              <el-form-item label="分类类型" prop="classificationType">
                <el-select
                  v-model="formData.classificationType"
                  :disabled="!isEdit && !isCreate"
                  placeholder="请选择分类类型"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in classificationTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="分类算法" prop="classificationAlgorithm">
                <el-select
                  v-model="formData.classificationAlgorithm"
                  :disabled="!isEdit && !isCreate"
                  placeholder="请选择分类算法"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in classificationAlgorithmOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>

              <el-form-item label="分类状态" prop="classificationStatus" v-if="!isCreate">
                <el-tag :type="getStatusTagType(formData.classificationStatus)">
                  {{ getStatusLabel(formData.classificationStatus) }}
                </el-tag>
              </el-form-item>
            </div>
          </el-col>

          <!-- 配置信息 -->
          <el-col :span="12">
            <div class="form-section">
              <h3 class="section-title">配置信息</h3>
              
              <el-form-item label="置信度阈值" prop="confidenceThreshold">
                <el-input-number
                  v-model="formData.confidenceThreshold"
                  :disabled="!isEdit && !isCreate"
                  :min="0"
                  :max="1"
                  :step="0.01"
                  :precision="2"
                  style="width: 100%"
                />
              </el-form-item>

              <el-form-item label="特征数量" prop="featureCount">
                <el-input-number
                  v-model="formData.featureCount"
                  :disabled="!isEdit && !isCreate"
                  :min="1"
                  style="width: 100%"
                />
              </el-form-item>

              <el-form-item label="分类数量" prop="categoryCount">
                <el-input-number
                  v-model="formData.categoryCount"
                  :disabled="!isEdit && !isCreate"
                  :min="2"
                  style="width: 100%"
                />
              </el-form-item>

              <el-form-item label="训练数据集大小" prop="trainingDatasetSize" v-if="!isCreate">
                <el-input
                  v-model="formData.trainingDatasetSize"
                  disabled
                />
              </el-form-item>

              <el-form-item label="测试数据集大小" prop="testDatasetSize" v-if="!isCreate">
                <el-input
                  v-model="formData.testDatasetSize"
                  disabled
                />
              </el-form-item>

              <el-form-item label="模型版本" prop="modelVersion" v-if="!isCreate">
                <el-input
                  v-model="formData.modelVersion"
                  disabled
                />
              </el-form-item>
            </div>
          </el-col>
        </el-row>

        <!-- 性能指标 -->
        <el-row :gutter="20" v-if="!isCreate">
          <el-col :span="24">
            <div class="form-section">
              <h3 class="section-title">性能指标</h3>
              
              <el-row :gutter="20">
                <el-col :span="6">
                  <el-form-item label="分类准确率">
                    <div class="metric-value">
                      <span :class="getAccuracyClass(formData.classificationAccuracy)">
                        {{ formatAccuracy(formData.classificationAccuracy) }}
                      </span>
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="预测次数">
                    <div class="metric-value">
                      {{ formData.predictionCount || 0 }}
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="成功预测">
                    <div class="metric-value">
                      {{ formData.successfulPredictions || 0 }}
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="失败预测">
                    <div class="metric-value">
                      {{ formData.failedPredictions || 0 }}
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="6">
                  <el-form-item label="平均预测时间">
                    <div class="metric-value">
                      {{ formatPredictionTime(formData.avgPredictionTime) }}
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="成功率">
                    <div class="metric-value">
                      {{ calculateSuccessRate(formData.successfulPredictions, formData.predictionCount) }}%
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="训练时间">
                    <div class="metric-value">
                      {{ formData.trainingTime || '-' }}
                    </div>
                  </el-form-item>
                </el-col>
                <el-col :span="6">
                  <el-form-item label="部署时间">
                    <div class="metric-value">
                      {{ formData.deploymentTime || '-' }}
                    </div>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>

        <!-- 高级配置 -->
        <el-row :gutter="20" v-if="isEdit || isCreate">
          <el-col :span="24">
            <div class="form-section">
              <h3 class="section-title">高级配置</h3>
              
              <el-form-item label="分类规则" prop="classificationRules">
                <el-input
                  type="textarea"
                  v-model="formData.classificationRules"
                  :rows="4"
                  placeholder="请输入分类规则（JSON格式）"
                />
              </el-form-item>

              <el-form-item label="特征配置" prop="featureConfig">
                <el-input
                  type="textarea"
                  v-model="formData.featureConfig"
                  :rows="4"
                  placeholder="请输入特征配置（JSON格式）"
                />
              </el-form-item>

              <el-form-item label="评估指标" prop="evaluationMetrics">
                <el-input
                  type="textarea"
                  v-model="formData.evaluationMetrics"
                  :rows="3"
                  placeholder="请输入评估指标（JSON格式）"
                />
              </el-form-item>
            </div>
          </el-col>
        </el-row>

        <!-- 操作按钮 -->
        <div class="form-actions" v-if="isEdit || isCreate">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="handleSave" :loading="saving">
            {{ isCreate ? '创建' : '保存' }}
          </el-button>
        </div>
      </el-form>
    </div>

    <!-- 操作历史 -->
    <div class="history-container" v-if="!isCreate">
      <h3 class="section-title">操作历史</h3>
      <el-timeline>
        <el-timeline-item
          v-for="item in operationHistory"
          :key="item.id"
          :timestamp="item.timestamp"
          :type="item.type"
        >
          <h4>{{ item.title }}</h4>
          <p>{{ item.description }}</p>
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script>
import {
  getClassificationById,
  createClassification,
  updateClassification,
  startTraining,
  deployModel,
  generateClassificationCode,
  CLASSIFICATION_TYPE_OPTIONS,
  CLASSIFICATION_ALGORITHM_OPTIONS,
  getClassificationStatusLabel,
  getClassificationStatusTagType,
  formatAccuracy,
  formatPredictionTime,
  calculateSuccessRate,
  validateClassificationConfig
} from '@/api/managementAccountant/as/intelligentClassification'

export default {
  name: 'IntelligentClassificationDetail',
  data() {
    return {
      formData: {
        classificationCode: '',
        classificationName: '',
        classificationDescription: '',
        classificationType: '',
        classificationAlgorithm: '',
        classificationStatus: 'INACTIVE',
        confidenceThreshold: 0.8,
        featureCount: 10,
        categoryCount: 5,
        trainingDatasetSize: 0,
        testDatasetSize: 0,
        modelVersion: '1.0.0',
        classificationAccuracy: null,
        predictionCount: 0,
        successfulPredictions: 0,
        failedPredictions: 0,
        avgPredictionTime: null,
        trainingTime: null,
        deploymentTime: null,
        classificationRules: '',
        featureConfig: '',
        evaluationMetrics: ''
      },
      formRules: {
        classificationCode: [
          { required: true, message: '请输入分类编号', trigger: 'blur' }
        ],
        classificationName: [
          { required: true, message: '请输入分类名称', trigger: 'blur' }
        ],
        classificationType: [
          { required: true, message: '请选择分类类型', trigger: 'change' }
        ],
        classificationAlgorithm: [
          { required: true, message: '请选择分类算法', trigger: 'change' }
        ],
        confidenceThreshold: [
          { required: true, message: '请输入置信度阈值', trigger: 'blur' },
          { type: 'number', min: 0, max: 1, message: '置信度阈值必须在0-1之间', trigger: 'blur' }
        ],
        featureCount: [
          { required: true, message: '请输入特征数量', trigger: 'blur' },
          { type: 'number', min: 1, message: '特征数量必须大于0', trigger: 'blur' }
        ],
        categoryCount: [
          { required: true, message: '请输入分类数量', trigger: 'blur' },
          { type: 'number', min: 2, message: '分类数量必须大于1', trigger: 'blur' }
        ]
      },
      classificationTypeOptions: CLASSIFICATION_TYPE_OPTIONS,
      classificationAlgorithmOptions: CLASSIFICATION_ALGORITHM_OPTIONS,
      loading: false,
      saving: false,
      operationHistory: []
    }
  },
  computed: {
    isCreate() {
      return this.$route.name === 'IntelligentClassificationCreate'
    },
    isEdit() {
      return this.$route.name === 'IntelligentClassificationEdit'
    },
    classificationId() {
      return this.$route.params.id
    },
    canTrain() {
      return ['INACTIVE', 'ACTIVE'].includes(this.formData.classificationStatus)
    },
    canDeploy() {
      return ['INACTIVE', 'ACTIVE'].includes(this.formData.classificationStatus)
    },
    canPredict() {
      return this.formData.classificationStatus === 'DEPLOYED'
    }
  },
  created() {
    if (this.isCreate) {
      this.initCreateForm()
    } else {
      this.loadData()
    }
  },
  methods: {
    // 初始化创建表单
    initCreateForm() {
      this.formData.classificationCode = generateClassificationCode('AUTO')
      this.formData.tenantId = this.$store.getters.tenantId
    },

    // 加载数据
    async loadData() {
      try {
        this.loading = true
        const tenantId = this.$store.getters.tenantId
        const result = await getClassificationById(tenantId, this.classificationId)
        
        if (result.data) {
          this.formData = { ...this.formData, ...result.data }
          this.loadOperationHistory()
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 加载操作历史
    loadOperationHistory() {
      // 模拟操作历史数据
      this.operationHistory = [
        {
          id: 1,
          title: '分类创建',
          description: '创建了智能分类',
          timestamp: this.formData.createdTime,
          type: 'primary'
        },
        {
          id: 2,
          title: '模型训练',
          description: '启动了模型训练',
          timestamp: this.formData.trainingTime,
          type: 'warning'
        },
        {
          id: 3,
          title: '模型部署',
          description: '部署了分类模型',
          timestamp: this.formData.deploymentTime,
          type: 'success'
        }
      ].filter(item => item.timestamp)
    },

    // 保存
    async handleSave() {
      try {
        await this.$refs.classificationForm.validate()
        
        // 验证配置
        const errors = validateClassificationConfig(this.formData)
        if (errors.length > 0) {
          this.$message.error(errors[0])
          return
        }
        
        this.saving = true
        
        if (this.isCreate) {
          await createClassification(this.formData)
          this.$message.success('创建成功')
        } else {
          await updateClassification(this.formData)
          this.$message.success('保存成功')
        }
        
        this.handleBack()
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error('保存失败')
      } finally {
        this.saving = false
      }
    },

    // 取消
    handleCancel() {
      this.handleBack()
    },

    // 返回
    handleBack() {
      this.$router.go(-1)
    },

    // 训练
    async handleTrain() {
      try {
        const tenantId = this.$store.getters.tenantId
        await startTraining(tenantId, this.classificationId)
        this.$message.success('训练启动成功')
        this.loadData()
      } catch (error) {
        console.error('训练启动失败:', error)
        this.$message.error('训练启动失败')
      }
    },

    // 部署
    async handleDeploy() {
      try {
        const tenantId = this.$store.getters.tenantId
        await deployModel(tenantId, this.classificationId)
        this.$message.success('部署成功')
        this.loadData()
      } catch (error) {
        console.error('部署失败:', error)
        this.$message.error('部署失败')
      }
    },

    // 预测
    handlePredict() {
      this.$router.push(`/management-accountant/as/intelligent-classification/prediction/${this.classificationId}`)
    },

    // 工具方法
    getStatusLabel(status) {
      return getClassificationStatusLabel(status)
    },

    getStatusTagType(status) {
      return getClassificationStatusTagType(status)
    },

    formatAccuracy(accuracy) {
      return formatAccuracy(accuracy)
    },

    formatPredictionTime(time) {
      return formatPredictionTime(time)
    },

    calculateSuccessRate(successful, total) {
      return calculateSuccessRate(successful, total)
    },

    getAccuracyClass(accuracy) {
      if (accuracy >= 0.9) return 'accuracy-excellent'
      if (accuracy >= 0.8) return 'accuracy-good'
      if (accuracy >= 0.7) return 'accuracy-normal'
      return 'accuracy-poor'
    }
  }
}
</script>

<style lang="scss" scoped>
.intelligent-classification-detail {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.page-header {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .page-title {
    margin: 0 0 0 10px;
    font-size: 20px;
    color: #303133;
  }
}

.form-container, .history-container {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.form-section {
  margin-bottom: 30px;

  .section-title {
    margin: 0 0 20px 0;
    font-size: 16px;
    color: #303133;
    border-bottom: 2px solid #409EFF;
    padding-bottom: 8px;
  }
}

.metric-value {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.accuracy-excellent {
  color: #67C23A;
}

.accuracy-good {
  color: #409EFF;
}

.accuracy-normal {
  color: #E6A23C;
}

.accuracy-poor {
  color: #F56C6C;
}

.form-actions {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #EBEEF5;
}
</style>
