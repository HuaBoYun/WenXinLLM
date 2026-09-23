<template>
  <div class="target-management-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
        <div class="header-title">
          <h2>{{ isEdit ? '编辑目标' : (isCreate ? '新建目标' : '目标详情') }}</h2>
          <p v-if="!isCreate">{{ formData.targetName }}</p>
        </div>
      </div>
      <div class="header-actions">
        <el-button v-if="!isCreate && !isEdit" @click="handleEdit">编辑</el-button>
        <el-button v-if="isEdit || isCreate" @click="handleSave" type="primary" :loading="saving">
          保存
        </el-button>
        <el-button v-if="isEdit" @click="handleCancel">取消</el-button>
      </div>
    </div>

    <!-- 主要内容 -->
    <div class="main-content">
      <el-form
        ref="targetForm"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        :disabled="!isEdit && !isCreate"
      >
        <el-card class="form-card">
          <div slot="header">
            <span>基本信息</span>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="目标编码" prop="targetCode">
                <el-input v-model="formData.targetCode" placeholder="系统自动生成" :disabled="!isCreate" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="目标名称" prop="targetName">
                <el-input v-model="formData.targetName" placeholder="请输入目标名称" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="目标类型" prop="targetType">
                <el-select v-model="formData.targetType" placeholder="请选择目标类型" style="width: 100%">
                  <el-option label="战略目标" value="STRATEGIC" />
                  <el-option label="业务目标" value="BUSINESS" />
                  <el-option label="个人目标" value="PERSONAL" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="目标状态" prop="targetStatus">
                <el-select v-model="formData.targetStatus" placeholder="请选择目标状态" style="width: 100%">
                  <el-option label="草稿" value="DRAFT" />
                  <el-option label="进行中" value="IN_PROGRESS" />
                  <el-option label="已完成" value="COMPLETED" />
                  <el-option label="已暂停" value="PAUSED" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="开始日期" prop="startDate">
                <el-date-picker
                  v-model="formData.startDate"
                  type="date"
                  placeholder="选择开始日期"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束日期" prop="endDate">
                <el-date-picker
                  v-model="formData.endDate"
                  type="date"
                  placeholder="选择结束日期"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="目标权重">
                <el-input-number v-model="formData.targetWeight" :min="0" :max="100" style="width: 100%" />
                <span style="margin-left: 10px; color: #909399;">%</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="完成进度">
                <el-input-number v-model="formData.progress" :min="0" :max="100" style="width: 100%" />
                <span style="margin-left: 10px; color: #909399;">%</span>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="目标描述">
            <el-input
              v-model="formData.targetDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入目标描述"
            />
          </el-form-item>

          <el-form-item label="成功标准">
            <el-input
              v-model="formData.successCriteria"
              type="textarea"
              :rows="3"
              placeholder="请输入成功标准"
            />
          </el-form-item>
        </el-card>

        <el-card class="form-card" v-if="!isCreate">
          <div slot="header">
            <span>执行情况</span>
          </div>
          
          <el-row :gutter="20">
            <el-col :span="8">
              <div class="progress-item">
                <div class="progress-label">完成进度</div>
                <el-progress :percentage="formData.progress || 0" :stroke-width="8" />
              </div>
            </el-col>
            <el-col :span="8">
              <div class="progress-item">
                <div class="progress-label">时间进度</div>
                <el-progress :percentage="timeProgress" :stroke-width="8" color="#f56c6c" />
              </div>
            </el-col>
            <el-col :span="8">
              <div class="progress-item">
                <div class="progress-label">健康度</div>
                <el-progress :percentage="healthScore" :stroke-width="8" :color="healthColor" />
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="12">
              <el-form-item label="实际开始日期">
                <el-date-picker
                  v-model="formData.actualStartDate"
                  type="date"
                  placeholder="选择实际开始日期"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="实际结束日期">
                <el-date-picker
                  v-model="formData.actualEndDate"
                  type="date"
                  placeholder="选择实际结束日期"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="执行备注">
            <el-input
              v-model="formData.executionNotes"
              type="textarea"
              :rows="3"
              placeholder="请输入执行备注"
            />
          </el-form-item>
        </el-card>
      </el-form>
    </div>
  </div>
</template>

<script>
import {
  getTargetById,
  createTarget,
  updateTarget
} from '@/api/managementAccountant/pm/targetManagement'

export default {
  name: 'TargetManagementDetail',
  data() {
    return {
      loading: false,
      saving: false,
      isCreate: false,
      isEdit: false,
      targetId: null,
      
      formData: {
        targetId: null,
        targetCode: '',
        targetName: '',
        targetType: '',
        targetStatus: 'DRAFT',
        startDate: null,
        endDate: null,
        targetWeight: 0,
        progress: 0,
        targetDescription: '',
        successCriteria: '',
        actualStartDate: null,
        actualEndDate: null,
        executionNotes: ''
      },

      formRules: {
        targetName: [
          { required: true, message: '请输入目标名称', trigger: 'blur' }
        ],
        targetType: [
          { required: true, message: '请选择目标类型', trigger: 'change' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ]
      }
    }
  },

  computed: {
    // 时间进度
    timeProgress() {
      if (!this.formData.startDate || !this.formData.endDate) return 0
      
      const now = new Date()
      const start = new Date(this.formData.startDate)
      const end = new Date(this.formData.endDate)
      
      if (now < start) return 0
      if (now > end) return 100
      
      const total = end.getTime() - start.getTime()
      const elapsed = now.getTime() - start.getTime()
      
      return Math.round((elapsed / total) * 100)
    },

    // 健康度评分
    healthScore() {
      const progressScore = this.formData.progress || 0
      const timeScore = this.timeProgress
      
      // 简单的健康度计算：进度与时间进度的比较
      if (timeScore === 0) return 100
      
      const ratio = progressScore / timeScore
      if (ratio >= 1) return 100
      if (ratio >= 0.8) return 80
      if (ratio >= 0.6) return 60
      if (ratio >= 0.4) return 40
      return 20
    },

    // 健康度颜色
    healthColor() {
      if (this.healthScore >= 80) return '#67c23a'
      if (this.healthScore >= 60) return '#e6a23c'
      return '#f56c6c'
    }
  },

  created() {
    this.initPage()
  },

  methods: {
    // 初始化页面
    initPage() {
      const { mode, id } = this.$route.params
      
      if (mode === 'create') {
        this.isCreate = true
        this.isEdit = true
      } else if (mode === 'edit') {
        this.isEdit = true
        this.targetId = id
        this.loadData()
      } else {
        this.targetId = id
        this.loadData()
      }
    },

    // 加载数据
    async loadData() {
      if (!this.targetId) return
      
      this.loading = true
      try {
        const response = await getTargetById(this.targetId)
        if (response.success) {
          this.formData = { ...this.formData, ...response.data }
        } else {
          this.$message.error(response.message || '加载失败')
        }
      } catch (error) {
        this.$message.error('加载失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 返回
    goBack() {
      this.$router.go(-1)
    },

    // 编辑
    handleEdit() {
      this.isEdit = true
    },

    // 保存
    async handleSave() {
      try {
        await this.$refs.targetForm.validate()

        this.saving = true

        let response
        if (this.isCreate) {
          response = await createTarget(this.formData)
        } else {
          response = await updateTarget(this.formData)
        }

        if (response.success) {
          this.$message.success(this.isCreate ? '创建成功' : '保存成功')
          
          if (this.isCreate) {
            this.$router.replace(`/pm/target-management/detail/${response.data.targetId}`)
          } else {
            this.isEdit = false
            this.loadData()
          }
        } else {
          this.$message.error(response.message || '保存失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('保存失败：' + error.message)
        }
      } finally {
        this.saving = false
      }
    },

    // 取消
    handleCancel() {
      this.isEdit = false
      if (this.isCreate) {
        this.goBack()
      } else {
        this.loadData()
      }
    }
  }
}
</script>

<style scoped>
.target-management-detail {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-title h2 {
  margin: 0 0 5px 0;
  color: #303133;
}

.header-title p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.main-content {
  background: white;
  border-radius: 4px;
}

.form-card {
  margin-bottom: 20px;
}

.progress-item {
  text-align: center;
}

.progress-label {
  margin-bottom: 10px;
  color: #606266;
  font-size: 14px;
}
</style>
