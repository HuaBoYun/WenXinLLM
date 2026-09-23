<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="80%"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="quality-control-detail">
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        size="small"
      >
        <el-tabs v-model="activeTab" type="border-card">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="basic">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="质量管控编码" prop="qualityCode">
                  <el-input v-model="form.qualityCode" :disabled="isView" placeholder="请输入质量管控编码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="质量管控名称" prop="qualityName">
                  <el-input v-model="form.qualityName" :disabled="isView" placeholder="请输入质量管控名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="质量类型" prop="qualityType">
                  <el-select v-model="form.qualityType" :disabled="isView" placeholder="请选择质量类型">
                    <el-option label="产品质量" value="PRODUCT_QUALITY" />
                    <el-option label="服务质量" value="SERVICE_QUALITY" />
                    <el-option label="过程质量" value="PROCESS_QUALITY" />
                    <el-option label="系统质量" value="SYSTEM_QUALITY" />
                    <el-option label="数据质量" value="DATA_QUALITY" />
                    <el-option label="环境质量" value="ENVIRONMENT_QUALITY" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="质量状态" prop="qualityStatus">
                  <el-select v-model="form.qualityStatus" :disabled="isView" placeholder="请选择质量状态">
                    <el-option label="草稿" value="DRAFT" />
                    <el-option label="活跃" value="ACTIVE" />
                    <el-option label="非活跃" value="INACTIVE" />
                    <el-option label="已完成" value="COMPLETED" />
                    <el-option label="已取消" value="CANCELLED" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="检测状态">
                  <el-select v-model="form.detectionStatus" :disabled="isView" placeholder="请选择检测状态">
                    <el-option label="待检测" value="PENDING" />
                    <el-option label="检测中" value="IN_DETECTION" />
                    <el-option label="已暂停" value="PAUSED" />
                    <el-option label="已完成" value="COMPLETED" />
                    <el-option label="已停止" value="STOPPED" />
                    <el-option label="检测失败" value="FAILED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="质量等级">
                  <el-select v-model="form.qualityLevel" :disabled="isView" placeholder="请选择质量等级">
                    <el-option label="优秀" value="EXCELLENT" />
                    <el-option label="良好" value="GOOD" />
                    <el-option label="一般" value="AVERAGE" />
                    <el-option label="较差" value="POOR" />
                    <el-option label="很差" value="VERY_POOR" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="优先级">
                  <el-input-number v-model="form.priority" :disabled="isView" :min="1" :max="10" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="优先级权重">
                  <el-input-number v-model="form.priorityWeight" :disabled="isView" :min="0.1" :max="10" :precision="2" :step="0.1" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="是否启用">
                  <el-switch v-model="form.isEnabled" :disabled="isView" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="风险等级">
                  <el-select v-model="form.riskLevel" :disabled="isView" placeholder="请选择风险等级">
                    <el-option label="低风险" value="LOW" />
                    <el-option label="中风险" value="MEDIUM" />
                    <el-option label="高风险" value="HIGH" />
                    <el-option label="极高风险" value="VERY_HIGH" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="描述">
              <el-input v-model="form.description" :disabled="isView" type="textarea" :rows="3" placeholder="请输入描述" />
            </el-form-item>
          </el-tab-pane>

          <!-- 质量标准 -->
          <el-tab-pane label="质量标准" name="standard">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="标准ID">
                  <el-input v-model="form.standardId" :disabled="isView" placeholder="请输入标准ID" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="标准名称">
                  <el-input v-model="form.standardName" :disabled="isView" placeholder="请输入标准名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="标准版本">
                  <el-input v-model="form.standardVersion" :disabled="isView" placeholder="请输入标准版本" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="标准类型">
                  <el-input v-model="form.standardType" :disabled="isView" placeholder="请输入标准类型" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="标准描述">
              <el-input v-model="form.standardDescription" :disabled="isView" type="textarea" :rows="3" placeholder="请输入标准描述" />
            </el-form-item>
          </el-tab-pane>

          <!-- 检测配置 -->
          <el-tab-pane label="检测配置" name="detection">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="检测方法">
                  <el-input v-model="form.detectionMethod" :disabled="isView" placeholder="请输入检测方法" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="检测工具">
                  <el-input v-model="form.detectionTool" :disabled="isView" placeholder="请输入检测工具" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="检测频率">
                  <el-input v-model="form.detectionFrequency" :disabled="isView" placeholder="请输入检测频率" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="检测周期">
                  <el-input v-model="form.detectionCycle" :disabled="isView" placeholder="请输入检测周期" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="抽检比例">
                  <el-input-number v-model="form.samplingRatio" :disabled="isView" :min="0" :max="100" :precision="2" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="抽检数量">
                  <el-input-number v-model="form.samplingQuantity" :disabled="isView" :min="0" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="检测规则">
              <el-input v-model="form.detectionRule" :disabled="isView" type="textarea" :rows="3" placeholder="请输入检测规则" />
            </el-form-item>
          </el-tab-pane>

          <!-- 人员配置 -->
          <el-tab-pane label="人员配置" name="personnel">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="负责人ID">
                  <el-input v-model="form.responsiblePersonId" :disabled="isView" placeholder="请输入负责人ID" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="负责人姓名">
                  <el-input v-model="form.responsiblePersonName" :disabled="isView" placeholder="请输入负责人姓名" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="检测人员ID">
                  <el-input v-model="form.inspectorId" :disabled="isView" placeholder="请输入检测人员ID" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="检测人员姓名">
                  <el-input v-model="form.inspectorName" :disabled="isView" placeholder="请输入检测人员姓名" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="审核人员ID">
                  <el-input v-model="form.reviewerId" :disabled="isView" placeholder="请输入审核人员ID" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="审核人员姓名">
                  <el-input v-model="form.reviewerName" :disabled="isView" placeholder="请输入审核人员姓名" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <!-- 质量结果 -->
          <el-tab-pane label="质量结果" name="result">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="质量评分">
                  <el-input-number v-model="form.qualityScore" :disabled="isView" :min="0" :max="100" :precision="2" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="合格率">
                  <el-input-number v-model="form.passRate" :disabled="isView" :min="0" :max="100" :precision="2" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="不合格数量">
                  <el-input-number v-model="form.failQuantity" :disabled="isView" :min="0" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="缺陷等级">
                  <el-select v-model="form.defectLevel" :disabled="isView" placeholder="请选择缺陷等级">
                    <el-option label="轻微" value="MINOR" />
                    <el-option label="一般" value="MAJOR" />
                    <el-option label="严重" value="CRITICAL" />
                    <el-option label="致命" value="FATAL" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="检测结果">
              <el-input v-model="form.detectionResult" :disabled="isView" type="textarea" :rows="3" placeholder="请输入检测结果" />
            </el-form-item>
            <el-form-item label="缺陷描述">
              <el-input v-model="form.defectDescription" :disabled="isView" type="textarea" :rows="3" placeholder="请输入缺陷描述" />
            </el-form-item>
          </el-tab-pane>

          <!-- 改进措施 -->
          <el-tab-pane label="改进措施" name="improvement">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="改进状态">
                  <el-select v-model="form.improvementStatus" :disabled="isView" placeholder="请选择改进状态">
                    <el-option label="计划中" value="PLANNED" />
                    <el-option label="进行中" value="IN_PROGRESS" />
                    <el-option label="已完成" value="COMPLETED" />
                    <el-option label="已取消" value="CANCELLED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="改进优先级">
                  <el-input-number v-model="form.improvementPriority" :disabled="isView" :min="1" :max="10" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="改进措施">
              <el-input v-model="form.improvementMeasures" :disabled="isView" type="textarea" :rows="3" placeholder="请输入改进措施" />
            </el-form-item>
            <el-form-item label="改进效果">
              <el-input v-model="form.improvementEffect" :disabled="isView" type="textarea" :rows="3" placeholder="请输入改进效果" />
            </el-form-item>
          </el-tab-pane>

          <!-- 成本分析 -->
          <el-tab-pane label="成本分析" name="cost">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="预防成本">
                  <el-input-number v-model="form.preventionCost" :disabled="isView" :min="0" :precision="2" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="评价成本">
                  <el-input-number v-model="form.appraisalCost" :disabled="isView" :min="0" :precision="2" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="内部失败成本">
                  <el-input-number v-model="form.internalFailureCost" :disabled="isView" :min="0" :precision="2" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="外部失败成本">
                  <el-input-number v-model="form.externalFailureCost" :disabled="isView" :min="0" :precision="2" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="质量成本">
                  <el-input-number v-model="form.qualityCost" :disabled="true" :min="0" :precision="2" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="成本节约">
                  <el-input-number v-model="form.costSaving" :disabled="isView" :min="0" :precision="2" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>

          <!-- 培训管理 -->
          <el-tab-pane label="培训管理" name="training">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="培训ID">
                  <el-input v-model="form.trainingId" :disabled="isView" placeholder="请输入培训ID" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="培训名称">
                  <el-input v-model="form.trainingName" :disabled="isView" placeholder="请输入培训名称" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="培训状态">
                  <el-select v-model="form.trainingStatus" :disabled="isView" placeholder="请选择培训状态">
                    <el-option label="已安排" value="ARRANGED" />
                    <el-option label="进行中" value="IN_PROGRESS" />
                    <el-option label="已完成" value="COMPLETED" />
                    <el-option label="已取消" value="CANCELLED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="培训完成率">
                  <el-input-number v-model="form.trainingCompletionRate" :disabled="isView" :min="0" :max="100" :precision="2" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="!isView" type="primary" @click="handleSave">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getQualityControlById,
  createQualityControl,
  updateQualityControl
} from '@/api/managementAccountant/ss/qualityControl'

export default {
  name: 'QualityControlDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    qualityControlId: {
      type: [String, Number],
      default: null
    },
    mode: {
      type: String,
      default: 'view' // view, edit, create
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'basic',
      form: {
        qualityId: null,
        qualityCode: '',
        qualityName: '',
        qualityType: '',
        qualityStatus: 'DRAFT',
        detectionStatus: 'PENDING',
        qualityLevel: '',
        priority: 5,
        priorityWeight: 1.0,
        isEnabled: true,
        riskLevel: '',
        description: '',
        // 质量标准
        standardId: null,
        standardName: '',
        standardVersion: '',
        standardType: '',
        standardDescription: '',
        // 检测配置
        detectionMethod: '',
        detectionTool: '',
        detectionFrequency: '',
        detectionCycle: '',
        samplingRatio: null,
        samplingQuantity: null,
        detectionRule: '',
        // 人员配置
        responsiblePersonId: null,
        responsiblePersonName: '',
        inspectorId: null,
        inspectorName: '',
        reviewerId: null,
        reviewerName: '',
        // 质量结果
        qualityScore: null,
        passRate: null,
        failQuantity: null,
        defectLevel: '',
        detectionResult: '',
        defectDescription: '',
        // 改进措施
        improvementStatus: '',
        improvementPriority: 5,
        improvementMeasures: '',
        improvementEffect: '',
        // 成本分析
        preventionCost: null,
        appraisalCost: null,
        internalFailureCost: null,
        externalFailureCost: null,
        qualityCost: null,
        costSaving: null,
        // 培训管理
        trainingId: null,
        trainingName: '',
        trainingStatus: '',
        trainingCompletionRate: null,
        // 系统字段
        tenantId: 1
      },
      rules: {
        qualityCode: [
          { required: true, message: '请输入质量管控编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        qualityName: [
          { required: true, message: '请输入质量管控名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        qualityType: [
          { required: true, message: '请选择质量类型', trigger: 'change' }
        ],
        qualityStatus: [
          { required: true, message: '请选择质量状态', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    },
    dialogTitle() {
      const titleMap = {
        view: '查看质量管控',
        edit: '编辑质量管控',
        create: '新增质量管控'
      }
      return titleMap[this.mode] || '质量管控详情'
    },
    isView() {
      return this.mode === 'view'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initData()
      }
    },
    qualityControlId(val) {
      if (val && this.visible) {
        this.loadData()
      }
    },
    // 监听成本字段变化，自动计算总成本
    'form.preventionCost': {
      handler() {
        this.calculateTotalCost()
      }
    },
    'form.appraisalCost': {
      handler() {
        this.calculateTotalCost()
      }
    },
    'form.internalFailureCost': {
      handler() {
        this.calculateTotalCost()
      }
    },
    'form.externalFailureCost': {
      handler() {
        this.calculateTotalCost()
      }
    }
  },
  methods: {
    // 初始化数据
    initData() {
      this.activeTab = 'basic'
      if (this.mode === 'create') {
        this.resetForm()
      } else if (this.qualityControlId) {
        this.loadData()
      }
    },

    // 加载数据
    async loadData() {
      if (!this.qualityControlId) return

      this.loading = true
      try {
        const response = await getQualityControlById(this.qualityControlId)
        if (response.success) {
          this.form = { ...this.form, ...response.data }
        } else {
          this.$message.error(response.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 重置表单
    resetForm() {
      this.form = {
        qualityId: null,
        qualityCode: '',
        qualityName: '',
        qualityType: '',
        qualityStatus: 'DRAFT',
        detectionStatus: 'PENDING',
        qualityLevel: '',
        priority: 5,
        priorityWeight: 1.0,
        isEnabled: true,
        riskLevel: '',
        description: '',
        // 质量标准
        standardId: null,
        standardName: '',
        standardVersion: '',
        standardType: '',
        standardDescription: '',
        // 检测配置
        detectionMethod: '',
        detectionTool: '',
        detectionFrequency: '',
        detectionCycle: '',
        samplingRatio: null,
        samplingQuantity: null,
        detectionRule: '',
        // 人员配置
        responsiblePersonId: null,
        responsiblePersonName: '',
        inspectorId: null,
        inspectorName: '',
        reviewerId: null,
        reviewerName: '',
        // 质量结果
        qualityScore: null,
        passRate: null,
        failQuantity: null,
        defectLevel: '',
        detectionResult: '',
        defectDescription: '',
        // 改进措施
        improvementStatus: '',
        improvementPriority: 5,
        improvementMeasures: '',
        improvementEffect: '',
        // 成本分析
        preventionCost: null,
        appraisalCost: null,
        internalFailureCost: null,
        externalFailureCost: null,
        qualityCost: null,
        costSaving: null,
        // 培训管理
        trainingId: null,
        trainingName: '',
        trainingStatus: '',
        trainingCompletionRate: null,
        // 系统字段
        tenantId: 1
      }
      this.$nextTick(() => {
        this.$refs.form && this.$refs.form.clearValidate()
      })
    },

    // 计算总成本
    calculateTotalCost() {
      const preventionCost = this.form.preventionCost || 0
      const appraisalCost = this.form.appraisalCost || 0
      const internalFailureCost = this.form.internalFailureCost || 0
      const externalFailureCost = this.form.externalFailureCost || 0

      this.form.qualityCost = preventionCost + appraisalCost + internalFailureCost + externalFailureCost
    },

    // 保存
    handleSave() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) {
          this.$message.warning('请完善必填信息')
          return
        }

        this.loading = true
        try {
          let response
          if (this.mode === 'create') {
            response = await createQualityControl(this.form)
          } else {
            response = await updateQualityControl(this.qualityControlId, this.form)
          }

          if (response.success) {
            this.$message.success(this.mode === 'create' ? '创建成功' : '更新成功')
            this.handleClose()
            this.$emit('refresh')
          } else {
            this.$message.error(response.message || '保存失败')
          }
        } catch (error) {
          console.error('保存失败:', error)
          this.$message.error('保存失败')
        } finally {
          this.loading = false
        }
      })
    },

    // 关闭
    handleClose() {
      this.dialogVisible = false
      this.resetForm()
    }
  }
}
</script>

<style lang="scss" scoped>
.quality-control-detail {
  .el-tabs {
    .el-tab-pane {
      padding: 20px 0;
    }
  }

  .el-form {
    .el-form-item {
      margin-bottom: 20px;
    }

    .el-form-item__label {
      font-weight: 500;
      color: #606266;
    }

    .el-input,
    .el-select,
    .el-textarea {
      width: 100%;
    }

    .el-input-number {
      width: 100%;
    }
  }

  .dialog-footer {
    text-align: right;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;
  }
}

// 全局样式覆盖
::v-deep {
  .el-dialog {
    .el-dialog__header {
      padding: 20px 20px 10px;
      border-bottom: 1px solid #ebeef5;

      .el-dialog__title {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
      }
    }

    .el-dialog__body {
      padding: 0 20px 20px;
      max-height: 70vh;
      overflow-y: auto;
    }
  }

  .el-tabs--border-card {
    border: 1px solid #dcdfe6;
    border-radius: 4px;

    .el-tabs__header {
      background-color: #f5f7fa;
      border-bottom: 1px solid #dcdfe6;
      margin: 0;

      .el-tabs__nav {
        .el-tabs__item {
          border-right: 1px solid #dcdfe6;
          padding: 0 20px;
          height: 40px;
          line-height: 40px;
          font-weight: 500;

          &.is-active {
            background-color: #fff;
            border-bottom-color: #fff;
            color: #409eff;
          }

          &:hover {
            color: #409eff;
          }
        }
      }
    }

    .el-tabs__content {
      padding: 0;

      .el-tab-pane {
        padding: 20px;
      }
    }
  }

  .el-form--label-width-120px {
    .el-form-item__label {
      width: 120px !important;
    }

    .el-form-item__content {
      margin-left: 120px !important;
    }
  }

  .el-row {
    .el-col {
      .el-form-item {
        margin-bottom: 18px;
      }
    }
  }

  .el-input {
    .el-input__inner {
      border-radius: 4px;
      transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);

      &:focus {
        border-color: #409eff;
        box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
      }
    }
  }

  .el-select {
    .el-input__inner {
      cursor: pointer;
    }
  }

  .el-textarea {
    .el-textarea__inner {
      border-radius: 4px;
      transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);

      &:focus {
        border-color: #409eff;
        box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
      }
    }
  }

  .el-input-number {
    .el-input__inner {
      text-align: left;
    }
  }

  .el-switch {
    .el-switch__core {
      border-radius: 10px;
      transition: all 0.3s;

      &::after {
        border-radius: 50%;
        transition: all 0.3s;
      }
    }

    &.is-checked {
      .el-switch__core {
        background-color: #409eff;
      }
    }
  }

  // 响应式设计
  @media (max-width: 768px) {
    .el-dialog {
      width: 95% !important;
      margin: 0 auto;

      .el-dialog__body {
        padding: 10px;
      }
    }

    .el-tabs--border-card {
      .el-tabs__header {
        .el-tabs__nav {
          .el-tabs__item {
            padding: 0 10px;
            font-size: 12px;
          }
        }
      }

      .el-tabs__content {
        .el-tab-pane {
          padding: 10px;
        }
      }
    }

    .el-form--label-width-120px {
      .el-form-item__label {
        width: 100px !important;
        font-size: 12px;
      }

      .el-form-item__content {
        margin-left: 100px !important;
      }
    }

    .el-row {
      .el-col {
        &:not(:last-child) {
          margin-bottom: 10px;
        }
      }
    }
  }

  @media (max-width: 480px) {
    .el-form--label-width-120px {
      .el-form-item__label {
        width: 80px !important;
        font-size: 11px;
      }

      .el-form-item__content {
        margin-left: 80px !important;
      }
    }

    .el-tabs--border-card {
      .el-tabs__header {
        .el-tabs__nav {
          .el-tabs__item {
            padding: 0 8px;
            font-size: 11px;
          }
        }
      }
    }
  }
}
</style>
