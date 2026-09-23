<template>
  <div class="archive-search-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button icon="el-icon-arrow-left" @click="handleBack">返回</el-button>
        <h1 class="page-title">{{ pageTitle }}</h1>
      </div>
      <div class="header-right">
        <el-button v-if="!isView" @click="handleCancel">取消</el-button>
        <el-button v-if="!isView" type="primary" @click="handleSave" :loading="saving">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
        <el-button v-if="isView" type="primary" @click="handleEdit">编辑</el-button>
      </div>
    </div>

    <!-- 表单内容 -->
    <div class="form-content">
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        :disabled="isView"
        label-width="120px"
        size="medium"
      >
        <!-- 基础信息 -->
        <el-card class="form-card" shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-info"></i>
            <span>基础信息</span>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="检索名称" prop="searchName">
                <el-input v-model="form.searchName" placeholder="请输入检索名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="检索编号" prop="searchCode">
                <el-input v-model="form.searchCode" placeholder="自动生成" :disabled="true" />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="检索类型" prop="searchType">
                <el-select v-model="form.searchType" placeholder="请选择检索类型" style="width: 100%">
                  <el-option
                    v-for="(label, value) in searchTypeOptions"
                    :key="value"
                    :label="label"
                    :value="value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="检索引擎" prop="searchEngine">
                <el-select v-model="form.searchEngine" placeholder="请选择检索引擎" style="width: 100%">
                  <el-option
                    v-for="(label, value) in searchEngineOptions"
                    :key="value"
                    :label="label"
                    :value="value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="检索描述" prop="searchDescription">
            <el-input
              v-model="form.searchDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入检索描述"
            />
          </el-form-item>
        </el-card>

        <!-- 索引配置 -->
        <el-card class="form-card" shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-setting"></i>
            <span>索引配置</span>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="索引名称" prop="indexName">
                <el-input v-model="form.indexName" placeholder="请输入索引名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="相似度阈值" prop="similarityThreshold">
                <el-input-number
                  v-model="form.similarityThreshold"
                  :min="0"
                  :max="1"
                  :step="0.01"
                  :precision="2"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="索引配置" prop="indexConfig">
            <el-input
              v-model="form.indexConfig"
              type="textarea"
              :rows="6"
              placeholder="请输入JSON格式的索引配置"
            />
          </el-form-item>
        </el-card>

        <!-- OCR配置 -->
        <el-card v-if="form.searchType === 'IMAGE' || form.searchType === 'HYBRID'" class="form-card" shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-camera"></i>
            <span>OCR配置</span>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="OCR准确率" prop="ocrAccuracy">
                <el-input-number
                  v-model="form.ocrAccuracy"
                  :min="0"
                  :max="1"
                  :step="0.01"
                  :precision="2"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="历史保留天数" prop="historyRetentionDays">
                <el-input-number
                  v-model="form.historyRetentionDays"
                  :min="1"
                  :max="365"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="OCR配置" prop="ocrConfig">
            <el-input
              v-model="form.ocrConfig"
              type="textarea"
              :rows="4"
              placeholder="请输入JSON格式的OCR配置"
            />
          </el-form-item>
        </el-card>

        <!-- 语义配置 -->
        <el-card v-if="form.searchType === 'SEMANTIC' || form.searchType === 'HYBRID'" class="form-card" shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-cpu"></i>
            <span>语义配置</span>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="语义模型版本" prop="semanticModelVersion">
                <el-input v-model="form.semanticModelVersion" placeholder="请输入语义模型版本" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="推荐算法" prop="recommendationAlgorithm">
                <el-select v-model="form.recommendationAlgorithm" placeholder="请选择推荐算法" style="width: 100%">
                  <el-option
                    v-for="(label, value) in recommendationAlgorithmOptions"
                    :key="value"
                    :label="label"
                    :value="value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="语义配置" prop="semanticConfig">
            <el-input
              v-model="form.semanticConfig"
              type="textarea"
              :rows="4"
              placeholder="请输入JSON格式的语义配置"
            />
          </el-form-item>
        </el-card>

        <!-- 性能配置 -->
        <el-card class="form-card" shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-monitor"></i>
            <span>性能配置</span>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="缓存命中率" prop="cacheHitRate">
                <el-input-number
                  v-model="form.cacheHitRate"
                  :min="0"
                  :max="1"
                  :step="0.01"
                  :precision="2"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="推荐准确率" prop="recommendationAccuracy">
                <el-input-number
                  v-model="form.recommendationAccuracy"
                  :min="0"
                  :max="1"
                  :step="0.01"
                  :precision="2"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="API成功率" prop="apiSuccessRate">
                <el-input-number
                  v-model="form.apiSuccessRate"
                  :min="0"
                  :max="1"
                  :step="0.01"
                  :precision="2"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="性能配置" prop="performanceConfig">
                <el-input
                  v-model="form.performanceConfig"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入JSON格式的性能配置"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="缓存配置" prop="cacheConfig">
                <el-input
                  v-model="form.cacheConfig"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入JSON格式的缓存配置"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 扩展配置 -->
        <el-card class="form-card" shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-more"></i>
            <span>扩展配置</span>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="扩展字段1" prop="extField1">
                <el-input v-model="form.extField1" placeholder="扩展字段1" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="扩展字段2" prop="extField2">
                <el-input v-model="form.extField2" placeholder="扩展字段2" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="扩展字段3" prop="extField3">
                <el-input v-model="form.extField3" placeholder="扩展字段3" />
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="备注信息" prop="remarks">
            <el-input
              v-model="form.remarks"
              type="textarea"
              :rows="3"
              placeholder="请输入备注信息"
            />
          </el-form-item>
        </el-card>
      </el-form>
    </div>

    <!-- 性能指标 -->
    <div v-if="isView && form.searchId" class="performance-metrics">
      <el-card shadow="never">
        <div slot="header" class="card-header">
          <i class="el-icon-data-analysis"></i>
          <span>性能指标</span>
        </div>
        
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-value">{{ form.totalCount || 0 }}</div>
              <div class="metric-label">总检索次数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-value">{{ form.successCount || 0 }}</div>
              <div class="metric-label">成功次数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-value">{{ formatAccuracy(form.searchAccuracy) }}</div>
              <div class="metric-label">检索准确率</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-value">{{ formatResponseTime(form.avgResponseTime) }}</div>
              <div class="metric-label">平均响应时间</div>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" style="margin-top: 20px">
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-value">{{ form.indexedDocs || 0 }}</div>
              <div class="metric-label">索引文档数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-value">{{ formatFileSize(form.indexSize) }}</div>
              <div class="metric-label">索引大小</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-value">{{ form.apiCallCount || 0 }}</div>
              <div class="metric-label">API调用次数</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="metric-item">
              <div class="metric-value">{{ formatDateTime(form.lastSearchTime) }}</div>
              <div class="metric-label">最后检索时间</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script>
import {
  getArchiveSearchById,
  createArchiveSearch,
  updateArchiveSearch,
  SEARCH_TYPES,
  SEARCH_ENGINES,
  RECOMMENDATION_ALGORITHMS,
  formatAccuracy,
  formatResponseTime,
  formatFileSize,
  validateSearchConfig
} from '@/api/managementAccountant/as/archiveSearch'

export default {
  name: 'ArchiveSearchDetail',
  data() {
    return {
      saving: false,
      form: {
        searchName: '',
        searchCode: '',
        searchDescription: '',
        searchType: '',
        searchEngine: '',
        indexName: '',
        indexConfig: '',
        similarityThreshold: 0.8,
        ocrConfig: '',
        ocrAccuracy: 0.95,
        semanticConfig: '',
        semanticModelVersion: '',
        recommendationAlgorithm: '',
        recommendationAccuracy: 0.8,
        performanceConfig: '',
        cacheConfig: '',
        cacheHitRate: 0.8,
        apiSuccessRate: 0.95,
        historyRetentionDays: 30,
        extField1: '',
        extField2: '',
        extField3: '',
        remarks: ''
      },
      rules: {
        searchName: [
          { required: true, message: '请输入检索名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        searchType: [
          { required: true, message: '请选择检索类型', trigger: 'change' }
        ],
        searchEngine: [
          { required: true, message: '请选择检索引擎', trigger: 'change' }
        ],
        indexName: [
          { required: true, message: '请输入索引名称', trigger: 'blur' }
        ]
      },
      searchTypeOptions: {
        [SEARCH_TYPES.FULL_TEXT]: '全文检索',
        [SEARCH_TYPES.SEMANTIC]: '语义检索',
        [SEARCH_TYPES.IMAGE]: '图像检索',
        [SEARCH_TYPES.VOICE]: '语音检索',
        [SEARCH_TYPES.HYBRID]: '混合检索'
      },
      searchEngineOptions: {
        [SEARCH_ENGINES.ELASTICSEARCH]: 'Elasticsearch',
        [SEARCH_ENGINES.SOLR]: 'Apache Solr',
        [SEARCH_ENGINES.LUCENE]: 'Apache Lucene',
        [SEARCH_ENGINES.CUSTOM]: '自定义引擎'
      },
      recommendationAlgorithmOptions: {
        [RECOMMENDATION_ALGORITHMS.COLLABORATIVE]: '协同过滤',
        [RECOMMENDATION_ALGORITHMS.CONTENT_BASED]: '基于内容',
        [RECOMMENDATION_ALGORITHMS.HYBRID]: '混合推荐'
      }
    }
  },
  computed: {
    searchId() {
      return this.$route.params.id
    },
    isCreate() {
      return this.$route.name === 'ArchiveSearchCreate'
    },
    isEdit() {
      return this.$route.name === 'ArchiveSearchEdit'
    },
    isView() {
      return this.$route.name === 'ArchiveSearchDetail'
    },
    pageTitle() {
      if (this.isCreate) return '创建检索配置'
      if (this.isEdit) return '编辑检索配置'
      return '检索配置详情'
    }
  },
  created() {
    this.initData()
  },
  methods: {
    async initData() {
      // 处理复制数据
      if (this.isCreate && this.$route.query.copyData) {
        try {
          const copyData = JSON.parse(this.$route.query.copyData)
          this.form = { ...this.form, ...copyData }
        } catch (error) {
          console.error('解析复制数据失败:', error)
        }
      }
      
      // 加载详情数据
      if (this.searchId && this.searchId !== 'create') {
        await this.loadDetail()
      }
    },
    
    async loadDetail() {
      try {
        const tenantId = this.$store.getters.tenantId
        const response = await getArchiveSearchById(tenantId, this.searchId)
        if (response.success) {
          this.form = { ...this.form, ...response.data }
        }
      } catch (error) {
        this.$message.error('加载详情失败: ' + error.message)
      }
    },
    
    async handleSave() {
      try {
        await this.$refs.form.validate()
        
        // 验证配置
        const validation = validateSearchConfig(this.form)
        if (!validation.isValid) {
          this.$message.error(validation.errors[0])
          return
        }
        
        this.saving = true
        const tenantId = this.$store.getters.tenantId
        const formData = { ...this.form, tenantId }
        
        let response
        if (this.isEdit) {
          response = await updateArchiveSearch(formData)
        } else {
          response = await createArchiveSearch(formData)
        }
        
        if (response.success) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.$router.push('/managementAccountant/as/archiveSearch/list')
        }
      } catch (error) {
        this.$message.error('保存失败: ' + error.message)
      } finally {
        this.saving = false
      }
    },
    
    handleCancel() {
      this.$router.back()
    },
    
    handleBack() {
      this.$router.back()
    },
    
    handleEdit() {
      this.$router.push(`/managementAccountant/as/archiveSearch/edit/${this.searchId}`)
    },
    
    // 格式化方法
    formatAccuracy,
    formatResponseTime,
    formatFileSize,
    
    formatDateTime(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.archive-search-detail {
  padding: 20px;
  
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
    
    .header-left {
      display: flex;
      align-items: center;
      
      .page-title {
        margin: 0 0 0 12px;
        font-size: 20px;
        font-weight: 600;
        color: #303133;
      }
    }
  }
  
  .form-content {
    .form-card {
      margin-bottom: 20px;
      
      .card-header {
        display: flex;
        align-items: center;
        
        i {
          margin-right: 8px;
          color: #409EFF;
        }
        
        span {
          font-weight: 600;
        }
      }
    }
  }
  
  .performance-metrics {
    margin-top: 20px;
    
    .metric-item {
      text-align: center;
      padding: 20px;
      background: #f8f9fa;
      border-radius: 8px;
      
      .metric-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 8px;
      }
      
      .metric-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}
</style>
