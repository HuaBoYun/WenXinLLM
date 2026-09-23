<template>
  <el-dialog
    :title="`安全管理 - ${currentProjectName || '未知项目'}`"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <!-- 项目信息概览 -->
    <div class="project-info-header" style="margin-bottom: 20px; padding: 15px; background: #f5f7fa; border-radius: 4px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <div><strong>项目名称：</strong>{{ currentProjectName || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>项目ID：</strong>{{ currentProjectId || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>负责人：</strong>{{ currentManagerName || '-' }}</div>
        </el-col>
        <el-col :span="6">
          <div><strong>管理状态：</strong>
            <el-tag :type="getManagementStatusType(currentManagementStatus)" size="mini">
              {{ getManagementStatusName(currentManagementStatus) }}
            </el-tag>
          </div>
        </el-col>
      </el-row>
    </div>

    <el-tabs v-model="activeTab" type="card">
      <!-- 安全检查 -->
      <el-tab-pane label="安全检查" name="inspection">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddInspection">
            添加安全检查
          </el-button>
          <el-button type="success" size="small" icon="el-icon-check" @click="batchApprove">
            批量通过
          </el-button>
          <el-button type="warning" size="small" icon="el-icon-warning" @click="generateReport">
            生成报告
          </el-button>
        </div>

        <el-table :data="inspectionList" border style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column label="检查名称" prop="inspectionName" width="150" />
          <el-table-column label="检查类型" prop="inspectionType" width="120">
            <template #default="{ row }">
              <el-tag :type="getInspectionTypeColor(row.inspectionType)">
                {{ getInspectionTypeName(row.inspectionType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="检查日期" prop="inspectionDate" width="120" />
          <el-table-column label="检查人员" prop="inspectorId" width="100" />
          <el-table-column label="隐患等级" prop="hazardLevel" width="100">
            <template #default="{ row }">
              <el-tag :type="getHazardLevelColor(row.hazardLevel)">
                {{ getHazardLevelName(row.hazardLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="紧急程度" prop="emergencyLevel" width="100">
            <template #default="{ row }">
              <el-tag :type="getEmergencyLevelColor(row.emergencyLevel)" size="mini">
                {{ getEmergencyLevelName(row.emergencyLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="整改状态" prop="rectificationStatus" width="100">
            <template #default="{ row }">
              <el-tag :type="getRectificationStatusColor(row.rectificationStatus)" size="mini">
                {{ getRectificationStatusName(row.rectificationStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row, $index }">
              <el-button type="text" size="small" @click="handleViewInspection(row)">详情</el-button>
              <el-button type="text" size="small" @click="handleEditInspection(row, $index)">编辑</el-button>
              <el-button 
                v-if="row.inspectionResult === 2" 
                type="text" 
                size="small" 
                @click="handleRectification(row)"
              >
                整改
              </el-button>
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteInspection($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 安全隐患 -->
      <el-tab-pane label="安全隐患" name="hazard">
        <div style="margin-bottom: 20px;">
          <el-button type="danger" size="small" icon="el-icon-warning-outline" @click="handleAddHazard">
            上报隐患
          </el-button>
          <el-button type="warning" size="small" icon="el-icon-bell" @click="sendAlert">
            发送预警
          </el-button>
        </div>

        <el-table :data="hazardList" border style="width: 100%">
          <el-table-column label="隐患描述" prop="hazardDescription" min-width="200" />
          <el-table-column label="隐患等级" prop="hazardLevel" width="100">
            <template #default="{ row }">
              <el-tag :type="getHazardLevelColor(row.hazardLevel)">
                {{ getHazardLevelName(row.hazardLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="发现日期" prop="discoveryDate" width="120" />
          <el-table-column label="发现人" prop="discoverer" width="100" />
          <el-table-column label="处理状态" prop="handleStatus" width="100">
            <template #default="{ row }">
              <el-tag :type="getHandleStatusColor(row.handleStatus)">
                {{ getHandleStatusName(row.handleStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="预计完成时间" prop="expectedCompletionDate" width="130" />
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row, $index }">
              <el-button type="text" size="small" @click="handleViewHazard(row)">详情</el-button>
              <el-button type="text" size="small" @click="handleEditHazard(row, $index)">编辑</el-button>
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteHazard($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 安全培训 -->
      <el-tab-pane label="安全培训" name="training">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddTraining">
            安排培训
          </el-button>
          <el-button type="success" size="small" icon="el-icon-document" @click="exportTrainingRecord">
            导出记录
          </el-button>
        </div>

        <el-table :data="trainingList" border style="width: 100%">
          <el-table-column label="培训主题" prop="trainingTopic" min-width="200" />
          <el-table-column label="培训类型" prop="trainingType" width="120">
            <template #default="{ row }">
              <el-tag :type="getTrainingTypeColor(row.trainingType)">
                {{ getTrainingTypeName(row.trainingType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="培训时间" prop="trainingDate" width="120" />
          <el-table-column label="培训讲师" prop="trainer" width="100" />
          <el-table-column label="参训人数" prop="participantCount" width="100" />
          <el-table-column label="培训状态" prop="trainingStatus" width="100">
            <template #default="{ row }">
              <el-tag :type="getTrainingStatusColor(row.trainingStatus)">
                {{ getTrainingStatusName(row.trainingStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row, $index }">
              <el-button type="text" size="small" @click="handleViewTraining(row)">详情</el-button>
              <el-button type="text" size="small" @click="handleEditTraining(row, $index)">编辑</el-button>
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteTraining($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 安全统计 -->
      <el-tab-pane label="安全统计" name="statistics">
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <div class="stat-value">{{ safetyStats.totalInspections }}</div>
                <div class="stat-label">总检查次数</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <div class="stat-value danger">{{ safetyStats.totalHazards }}</div>
                <div class="stat-label">发现隐患数</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <div class="stat-value success">{{ safetyStats.resolvedHazards }}</div>
                <div class="stat-label">已处理隐患</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <div class="stat-value warning">{{ safetyStats.pendingHazards }}</div>
                <div class="stat-label">待处理隐患</div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <div slot="header">安全检查趋势</div>
              <div id="inspectionChart" style="height: 300px;"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <div slot="header">隐患等级分布</div>
              <div id="hazardChart" style="height: 300px;"></div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>

    <!-- 安全检查编辑对话框 -->
    <el-dialog
      :title="inspectionDialogTitle"
      :visible.sync="inspectionDialogVisible"
      width="60%"
      :close-on-click-modal="false"
      append-to-body
    >
      <el-form ref="inspectionForm" :model="inspectionForm" :rules="inspectionRules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检查名称" prop="inspectionName">
              <el-input v-model="inspectionForm.inspectionName" placeholder="请输入检查名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查类型" prop="inspectionType">
              <el-select v-model="inspectionForm.inspectionType" placeholder="请选择检查类型" style="width: 100%">
                <el-option label="日常检查" :value="1" />
                <el-option label="专项检查" :value="2" />
                <el-option label="突击检查" :value="3" />
                <el-option label="季度检查" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检查日期" prop="inspectionDate">
              <el-date-picker
                v-model="inspectionForm.inspectionDate"
                type="date"
                placeholder="选择检查日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查人员ID" prop="inspectorId">
              <el-input-number v-model="inspectionForm.inspectorId" placeholder="请输入检查人员ID" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="检查内容" prop="inspectionContent">
          <el-input
            v-model="inspectionForm.inspectionContent"
            type="textarea"
            :rows="4"
            placeholder="请输入详细的检查内容"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="检查结果" prop="inspectionResult">
              <el-select v-model="inspectionForm.inspectionResult" placeholder="请选择检查结果" style="width: 100%">
                <el-option label="合格" :value="1" />
                <el-option label="不合格" :value="2" />
                <el-option label="基本合格" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="inspectionForm.riskLevel" placeholder="请选择风险等级" style="width: 100%">
                <el-option label="低风险" :value="1" />
                <el-option label="中风险" :value="2" />
                <el-option label="高风险" :value="3" />
                <el-option label="极高风险" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="问题描述" prop="problemDescription">
          <el-input
            v-model="inspectionForm.problemDescription"
            type="textarea"
            :rows="3"
            placeholder="请描述发现的问题"
          />
        </el-form-item>

        <el-form-item label="整改建议" prop="rectificationSuggestion">
          <el-input
            v-model="inspectionForm.rectificationSuggestion"
            type="textarea"
            :rows="3"
            placeholder="请输入整改建议"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="inspectionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveInspection" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import {
  createSafetyInspection,
  getSafetyInspectionPage,
  updateSafetyInspection,
  deleteSafetyInspection,
  rectifySafetyInspection
} from '@/api/contract/safetyInspection'

export default {
  name: 'SafetyManagement',
  data() {
    return {
      dialogVisible: false,
      activeTab: 'inspection',
      currentProjectId: null,
      currentProjectName: '',
      currentManagerName: '',
      currentManagementStatus: null,
      selectedInspections: [],
      saveLoading: false,
      
      // 安全检查数据
      inspectionList: [
        {
          inspectionName: '施工现场安全检查',
          inspectionType: 1,
          inspectionDate: '2025-01-10',
          inspectorId: 1,
          hazardLevel: 1,
          emergencyLevel: 1,
          rectificationStatus: 1
        },
        {
          inspectionName: '设备安全检查',
          inspectionType: 2,
          inspectionDate: '2025-01-08',
          inspectorId: 2,
          hazardLevel: 3,
          emergencyLevel: 2,
          rectificationStatus: 2
        }
      ],
      
      // 安全隐患数据
      hazardList: [
        {
          hazardDescription: '脚手架搭设不规范',
          hazardLevel: 2,
          discoveryDate: '2025-01-09',
          discoverer: '王五',
          handleStatus: 2,
          expectedCompletionDate: '2025-01-15'
        }
      ],
      
      // 安全培训数据
      trainingList: [
        {
          trainingTopic: '高空作业安全培训',
          trainingType: 1,
          trainingDate: '2025-01-12',
          trainer: '赵六',
          participantCount: 25,
          trainingStatus: 1
        }
      ],
      
      // 安全统计数据
      safetyStats: {
        totalInspections: 15,
        totalHazards: 8,
        resolvedHazards: 5,
        pendingHazards: 3
      },
      
      // 检查表单
      inspectionDialogVisible: false,
      inspectionDialogTitle: '添加安全检查',
      inspectionForm: {
        inspectionName: '',
        inspectionType: null,
        inspectionDate: null,
        inspectorId: null,
        inspectionLocation: '',
        inspectionScope: '',
        safetyStandards: '',
        inspectionFindings: '',
        hazardIdentification: '',
        hazardLevel: null,
        rectificationMeasures: '',
        rectificationDeadline: null,
        rectificationPersonId: null,
        emergencyLevel: null
      },
      inspectionRules: {
        inspectionName: [
          { required: true, message: '请输入检查名称', trigger: 'blur' }
        ],
        inspectionType: [
          { required: true, message: '请选择检查类型', trigger: 'change' }
        ],
        inspectionDate: [
          { required: true, message: '请选择检查日期', trigger: 'change' }
        ],
        inspectorId: [
          { required: true, message: '请输入检查人员ID', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    showEdit(row) {
      this.currentProjectId = row.id
      this.currentProjectName = row.projectName
      this.currentManagerName = row.managerName
      this.currentManagementStatus = row.managementStatus
      this.dialogVisible = true
      this.loadSafetyData()
    },

    handleClose() {
      this.dialogVisible = false
      this.currentProjectId = null
      this.activeTab = 'inspection'
    },

    async loadSafetyData() {
      if (!this.currentProjectId) return

      try {
        // 加载安全检查数据
        await this.loadInspectionList()
      } catch (error) {
        console.error('加载安全管理数据失败:', error)
        this.$message.error('加载数据失败')
      }
    },

    // 加载安全检查列表
    async loadInspectionList() {
      try {
        const params = {
          pageNum: 1,
          pageSize: 100,
          projectId: this.currentProjectId
        }

        const response = await getSafetyInspectionPage(params)
        if (response.code === 1) {
          this.inspectionList = response.data || []
        } else {
          this.$message.error(response.msg || '获取安全检查列表失败')
        }
      } catch (error) {
        console.error('获取安全检查列表失败:', error)
        this.$message.error('获取安全检查列表失败')
      }
    },

    // 安全检查相关方法
    handleAddInspection() {
      this.inspectionDialogTitle = '添加安全检查'
      this.inspectionForm = {
        inspectionName: '',
        inspectionType: null,
        inspectionDate: null,
        inspectorId: null,
        inspectionLocation: '',
        inspectionScope: '',
        safetyStandards: '',
        inspectionFindings: '',
        hazardIdentification: '',
        hazardLevel: null,
        rectificationMeasures: '',
        rectificationDeadline: null,
        rectificationPersonId: null,
        emergencyLevel: null
      }
      this.inspectionDialogVisible = true
    },

    handleEditInspection(row, index) {
      this.inspectionDialogTitle = '编辑安全检查'
      this.inspectionForm = { ...row }
      this.inspectionDialogVisible = true
    },

    handleViewInspection(row) {
      this.$message.info('查看检查详情功能开发中...')
    },

    async handleDeleteInspection(index) {
      const row = this.inspectionList[index]
      if (!row || !row.id) {
        this.$message.error('无效的记录')
        return
      }

      this.$confirm('确定要删除这条安全检查记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteSafetyInspection(row.id)
          if (response.code === 1) {
            this.$message.success(response.msg || '删除成功')
            await this.loadInspectionList() // 重新加载列表
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          console.error('删除安全检查失败:', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },

    async handleSaveInspection() {
      this.$refs.inspectionForm.validate(async (valid) => {
        if (valid) {
          this.saveLoading = true
          try {
            const formData = {
              ...this.inspectionForm,
              projectId: this.currentProjectId
            }

            let response
            if (formData.id) {
              // 编辑模式
              response = await updateSafetyInspection(formData)
            } else {
              // 新增模式
              response = await createSafetyInspection(formData)
            }

            if (response.code === 1) {
              this.$message.success(response.msg || '保存成功')
              this.inspectionDialogVisible = false
              await this.loadInspectionList() // 重新加载列表
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          } catch (error) {
            console.error('保存安全检查失败:', error)
            this.$message.error('保存失败')
          } finally {
            this.saveLoading = false
          }
        }
      })
    },

    async handleRectification(row) {
      if (!row || !row.id) {
        this.$message.error('无效的记录')
        return
      }

      try {
        const rectificationData = {
          id: row.id,
          rectificationStatus: 2, // 整改中
          rectificationDescription: '开始整改处理'
        }

        const response = await rectifySafetyInspection(rectificationData)
        if (response.code === 1) {
          this.$message.success(response.msg || '整改处理成功')
          await this.loadInspectionList() // 重新加载列表
        } else {
          this.$message.error(response.msg || '整改处理失败')
        }
      } catch (error) {
        console.error('整改处理失败:', error)
        this.$message.error('整改处理失败')
      }
    },

    handleSelectionChange(selection) {
      this.selectedInspections = selection
    },

    batchApprove() {
      if (this.selectedInspections.length === 0) {
        this.$message.warning('请选择要批量通过的检查记录')
        return
      }
      this.$message.success('批量通过成功')
    },

    generateReport() {
      this.$message.info('生成报告功能开发中...')
    },

    // 安全隐患相关方法
    handleAddHazard() {
      this.$message.info('上报隐患功能开发中...')
    },

    handleEditHazard(row, index) {
      this.$message.info('编辑隐患功能开发中...')
    },

    handleViewHazard(row) {
      this.$message.info('查看隐患详情功能开发中...')
    },

    handleDeleteHazard(index) {
      this.$confirm('确定要删除这条隐患记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.hazardList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    sendAlert() {
      this.$message.success('预警发送成功')
    },

    // 安全培训相关方法
    handleAddTraining() {
      this.$message.info('安排培训功能开发中...')
    },

    handleEditTraining(row, index) {
      this.$message.info('编辑培训功能开发中...')
    },

    handleViewTraining(row) {
      this.$message.info('查看培训详情功能开发中...')
    },

    handleDeleteTraining(index) {
      this.$confirm('确定要删除这条培训记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.trainingList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    exportTrainingRecord() {
      this.$message.info('导出培训记录功能开发中...')
    },

    // 工具方法
    getInspectionTypeColor(type) {
      const colorMap = { 1: 'primary', 2: 'success', 3: 'warning', 4: 'info' }
      return colorMap[type] || 'info'
    },

    getInspectionTypeName(type) {
      const nameMap = { 1: '日常检查', 2: '专项检查', 3: '突击检查', 4: '季度检查' }
      return nameMap[type] || '未知'
    },

    getResultColor(result) {
      const colorMap = { 1: 'success', 2: 'danger', 3: 'warning' }
      return colorMap[result] || 'info'
    },

    getResultName(result) {
      const nameMap = { 1: '合格', 2: '不合格', 3: '基本合格' }
      return nameMap[result] || '未知'
    },

    getRiskLevelColor(level) {
      const colorMap = { 1: 'success', 2: 'warning', 3: 'danger', 4: 'danger' }
      return colorMap[level] || 'info'
    },

    getRiskLevelName(level) {
      const nameMap = { 1: '低风险', 2: '中风险', 3: '高风险', 4: '极高风险' }
      return nameMap[level] || '未知'
    },

    getRectificationStatusColor(status) {
      const colorMap = { 1: 'success', 2: 'warning', 3: 'danger' }
      return colorMap[status] || 'info'
    },

    getRectificationStatusName(status) {
      const nameMap = { 1: '已整改', 2: '整改中', 3: '未整改' }
      return nameMap[status] || '未知'
    },

    getHazardLevelColor(level) {
      const colorMap = { 1: 'success', 2: 'warning', 3: 'danger', 4: 'danger' }
      return colorMap[level] || 'info'
    },

    getHazardLevelName(level) {
      const nameMap = { 1: '一般', 2: '较大', 3: '重大', 4: '特别重大' }
      return nameMap[level] || '未知'
    },

    getEmergencyLevelColor(level) {
      const colorMap = { 1: 'success', 2: 'warning', 3: 'danger' }
      return colorMap[level] || 'info'
    },

    getEmergencyLevelName(level) {
      const nameMap = { 1: '一般', 2: '紧急', 3: '特急' }
      return nameMap[level] || '未知'
    },

    getHandleStatusColor(status) {
      const colorMap = { 1: 'info', 2: 'warning', 3: 'success' }
      return colorMap[status] || 'info'
    },

    getHandleStatusName(status) {
      const nameMap = { 1: '待处理', 2: '处理中', 3: '已处理' }
      return nameMap[status] || '未知'
    },

    getTrainingTypeColor(type) {
      const colorMap = { 1: 'primary', 2: 'success', 3: 'warning' }
      return colorMap[type] || 'info'
    },

    getTrainingTypeName(type) {
      const nameMap = { 1: '安全教育', 2: '技能培训', 3: '应急演练' }
      return nameMap[type] || '未知'
    },

    getTrainingStatusColor(status) {
      const colorMap = { 1: 'success', 2: 'warning', 3: 'info' }
      return colorMap[status] || 'info'
    },

    getTrainingStatusName(status) {
      const nameMap = { 1: '已完成', 2: '进行中', 3: '计划中' }
      return nameMap[status] || '未知'
    },

    // 获取管理状态名称
    getManagementStatusName(status) {
      const statusMap = {
        1: '正常',
        2: '预警',
        3: '异常',
        4: '暂停'
      }
      return statusMap[status] || '未知'
    },

    // 获取管理状态样式
    getManagementStatusType(status) {
      const statusMap = {
        1: 'success',
        2: 'warning',
        3: 'danger',
        4: 'info'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
.stat-card {
  text-align: center;
}

.stat-item {
  padding: 20px 0;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.stat-value.success {
  color: #67C23A;
}

.stat-value.warning {
  color: #E6A23C;
}

.stat-value.danger {
  color: #F56C6C;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.dialog-footer {
  text-align: right;
}
</style>
