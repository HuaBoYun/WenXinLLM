<template>
  <el-dialog
    :title="`结算管理 - ${currentProjectName || '未知项目'}`"
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
      <!-- 结算提醒 -->
      <el-tab-pane label="结算提醒" name="reminder">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddReminder">
            添加提醒
          </el-button>
          <el-button type="success" size="small" icon="el-icon-check" @click="batchProcess">
            批量处理
          </el-button>
          <el-button type="warning" size="small" icon="el-icon-bell" @click="sendNotification">
            发送通知
          </el-button>
        </div>

        <el-table :data="reminderList" border style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column label="结算项目" prop="settlementProject" min-width="200" />
          <el-table-column label="合同编号" prop="contractNumber" width="150" />
          <el-table-column label="结算类型" prop="settlementType" width="120">
            <template #default="{ row }">
              <el-tag :type="getSettlementTypeColor(row.settlementType)">
                {{ getSettlementTypeName(row.settlementType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="结算金额" prop="settlementAmount" width="120">
            <template #default="{ row }">
              {{ formatMoney(row.settlementAmount) }}
            </template>
          </el-table-column>
          <el-table-column label="计划结算日期" prop="plannedSettlementDate" width="130" />
          <el-table-column label="提醒状态" prop="reminderStatus" width="100">
            <template #default="{ row }">
              <el-tag :type="getReminderStatusColor(row.reminderStatus)" size="mini">
                {{ getReminderStatusName(row.reminderStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="剩余天数" prop="remainingDays" width="100">
            <template #default="{ row }">
              <span :class="getRemainingDaysClass(row.remainingDays)">
                {{ row.remainingDays }}天
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row, $index }">
              <el-button type="text" size="small" @click="handleViewReminder(row)">详情</el-button>
              <el-button type="text" size="small" @click="handleEditReminder(row, $index)">编辑</el-button>
              <el-button 
                v-if="row.reminderStatus === 1" 
                type="text" 
                size="small" 
                @click="handleProcessReminder(row)"
              >
                处理
              </el-button>
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteReminder($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 结算处理 -->
      <el-tab-pane label="结算处理" name="processing">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddSettlement">
            新增结算
          </el-button>
          <el-button type="success" size="small" icon="el-icon-check" @click="batchApprove">
            批量审核
          </el-button>
          <el-button type="info" size="small" icon="el-icon-document" @click="generateSettlementReport">
            生成报表
          </el-button>
        </div>

        <el-table :data="settlementList" border style="width: 100%" @selection-change="handleSettlementSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column label="结算单号" prop="settlementNumber" width="150" />
          <el-table-column label="结算项目" prop="settlementProject" min-width="200" />
          <el-table-column label="合同编号" prop="contractNumber" width="150" />
          <el-table-column label="结算金额" prop="settlementAmount" width="120">
            <template #default="{ row }">
              {{ formatMoney(row.settlementAmount) }}
            </template>
          </el-table-column>
          <el-table-column label="已付金额" prop="paidAmount" width="120">
            <template #default="{ row }">
              {{ formatMoney(row.paidAmount) }}
            </template>
          </el-table-column>
          <el-table-column label="未付金额" prop="unpaidAmount" width="120">
            <template #default="{ row }">
              {{ formatMoney(row.unpaidAmount) }}
            </template>
          </el-table-column>
          <el-table-column label="结算状态" prop="settlementStatus" width="100">
            <template #default="{ row }">
              <el-tag :type="getSettlementStatusColor(row.settlementStatus)">
                {{ getSettlementStatusName(row.settlementStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="审核状态" prop="approvalStatus" width="100">
            <template #default="{ row }">
              <el-tag :type="getApprovalStatusColor(row.approvalStatus)" size="mini">
                {{ getApprovalStatusName(row.approvalStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row, $index }">
              <el-button type="text" size="small" @click="handleViewSettlement(row)">详情</el-button>
              <el-button type="text" size="small" @click="handleEditSettlement(row, $index)">编辑</el-button>
              <el-button 
                v-if="row.approvalStatus === 1" 
                type="text" 
                size="small" 
                @click="handleApproveSettlement(row)"
              >
                审核
              </el-button>
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteSettlement($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 结算统计 -->
      <el-tab-pane label="结算统计" name="statistics">
        <el-row :gutter="20" style="margin-bottom: 20px;">
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <div class="stat-value">{{ settlementStats.totalSettlements }}</div>
                <div class="stat-label">总结算数</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <div class="stat-value success">{{ formatMoney(settlementStats.totalAmount) }}</div>
                <div class="stat-label">总结算金额</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <div class="stat-value warning">{{ formatMoney(settlementStats.paidAmount) }}</div>
                <div class="stat-label">已付金额</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stat-card">
              <div class="stat-item">
                <div class="stat-value danger">{{ formatMoney(settlementStats.unpaidAmount) }}</div>
                <div class="stat-label">未付金额</div>
              </div>
            </el-card>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <div slot="header">结算进度趋势</div>
              <div id="settlementChart" style="height: 300px;"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <div slot="header">结算状态分布</div>
              <div id="statusChart" style="height: 300px;"></div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <!-- 结算单据 -->
      <el-tab-pane label="结算单据" name="documents">
        <div style="margin-bottom: 20px;">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddDocument">
            上传单据
          </el-button>
          <el-button type="success" size="small" icon="el-icon-download" @click="batchDownload">
            批量下载
          </el-button>
        </div>

        <el-table :data="documentList" border style="width: 100%" @selection-change="handleDocumentSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column label="单据名称" prop="documentName" min-width="200" />
          <el-table-column label="单据类型" prop="documentType" width="120">
            <template #default="{ row }">
              <el-tag :type="getDocumentTypeColor(row.documentType)">
                {{ getDocumentTypeName(row.documentType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="关联结算" prop="settlementNumber" width="150" />
          <el-table-column label="文件大小" prop="fileSize" width="100" />
          <el-table-column label="上传时间" prop="uploadTime" width="150" />
          <el-table-column label="上传人" prop="uploader" width="100" />
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row, $index }">
              <el-button type="text" size="small" @click="handlePreviewDocument(row)">预览</el-button>
              <el-button type="text" size="small" @click="handleDownloadDocument(row)">下载</el-button>
              <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDeleteDocument($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 结算提醒编辑对话框 -->
    <el-dialog
      :title="reminderDialogTitle"
      :visible.sync="reminderDialogVisible"
      width="60%"
      :close-on-click-modal="false"
      append-to-body
    >
      <el-form ref="reminderForm" :model="reminderForm" :rules="reminderRules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="结算项目" prop="settlementProject">
              <el-input v-model="reminderForm.settlementProject" placeholder="请输入结算项目" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同编号" prop="contractNumber">
              <el-input v-model="reminderForm.contractNumber" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="结算类型" prop="settlementType">
              <el-select v-model="reminderForm.settlementType" placeholder="请选择结算类型" style="width: 100%">
                <el-option label="进度结算" :value="1" />
                <el-option label="完工结算" :value="2" />
                <el-option label="最终结算" :value="3" />
                <el-option label="专项结算" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结算金额" prop="settlementAmount">
              <el-input-number
                v-model="reminderForm.settlementAmount"
                :min="0"
                :precision="2"
                style="width: 100%"
                placeholder="请输入结算金额"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划结算日期" prop="plannedSettlementDate">
              <el-date-picker
                v-model="reminderForm.plannedSettlementDate"
                type="date"
                placeholder="选择计划结算日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提醒天数" prop="reminderDays">
              <el-input-number
                v-model="reminderForm.reminderDays"
                :min="1"
                :max="365"
                style="width: 100%"
                placeholder="提前多少天提醒"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="结算条件" prop="settlementConditions">
          <el-input
            v-model="reminderForm.settlementConditions"
            type="textarea"
            :rows="3"
            placeholder="请输入结算条件"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="reminderForm.remarks"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="reminderDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveReminder" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { formatMoney } from '@/utils/index'
import {
  createProjectSettlement,
  getProjectSettlementPage,
  updateProjectSettlement,
  deleteProjectSettlement,
  getProjectSettlementDetail,
  reviewProjectSettlement,
  getProjectSettlementStatistics,
  getPendingReviewSettlements,
  batchReviewSettlements,
  generateSettlementNo,
  createSettlementReminder,
  getSettlementReminderList,
  updateSettlementReminder,
  deleteSettlementReminder,
  processSettlementReminder,
  batchProcessReminders,
  sendSettlementNotification
} from '@/api/contract/settlement'

export default {
  name: 'SettlementManagement',
  data() {
    return {
      dialogVisible: false,
      activeTab: 'reminder',
      currentProjectId: null,
      currentProjectName: '',
      currentManagerName: '',
      currentManagementStatus: null,
      selectedReminders: [],
      selectedSettlements: [],
      selectedDocuments: [],
      saveLoading: false,
      
      // 结算提醒数据
      reminderList: [
        {
          settlementProject: '办公楼建设项目第一期结算',
          contractNumber: 'HT2025001',
          settlementType: 1,
          settlementAmount: 1500000,
          plannedSettlementDate: '2025-01-20',
          reminderStatus: 1,
          remainingDays: 10
        },
        {
          settlementProject: '道路改造工程完工结算',
          contractNumber: 'HT2025002',
          settlementType: 2,
          settlementAmount: 2800000,
          plannedSettlementDate: '2025-01-25',
          reminderStatus: 2,
          remainingDays: 15
        }
      ],
      
      // 结算处理数据
      settlementList: [
        {
          settlementNumber: 'JS2025001',
          settlementProject: '办公楼建设项目第一期结算',
          contractNumber: 'HT2025001',
          settlementAmount: 1500000,
          paidAmount: 1200000,
          unpaidAmount: 300000,
          settlementStatus: 2,
          approvalStatus: 2
        }
      ],
      
      // 结算统计数据
      settlementStats: {
        totalSettlements: 15,
        totalAmount: 25000000,
        paidAmount: 18000000,
        unpaidAmount: 7000000
      },
      
      // 结算单据数据
      documentList: [
        {
          documentName: '结算申请书.pdf',
          documentType: 1,
          settlementNumber: 'JS2025001',
          fileSize: '2.5MB',
          uploadTime: '2025-01-10 14:30:00',
          uploader: '张三'
        }
      ],
      
      // 提醒表单
      reminderDialogVisible: false,
      reminderDialogTitle: '添加结算提醒',
      reminderForm: {
        settlementProject: '',
        contractNumber: '',
        settlementType: null,
        settlementAmount: null,
        plannedSettlementDate: null,
        reminderDays: 7,
        settlementConditions: '',
        remarks: ''
      },
      reminderRules: {
        settlementProject: [
          { required: true, message: '请输入结算项目', trigger: 'blur' }
        ],
        contractNumber: [
          { required: true, message: '请输入合同编号', trigger: 'blur' }
        ],
        settlementType: [
          { required: true, message: '请选择结算类型', trigger: 'change' }
        ],
        settlementAmount: [
          { required: true, message: '请输入结算金额', trigger: 'blur' }
        ],
        plannedSettlementDate: [
          { required: true, message: '请选择计划结算日期', trigger: 'change' }
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
      this.loadSettlementData()
    },

    handleClose() {
      this.dialogVisible = false
      this.currentProjectId = null
      this.activeTab = 'reminder'
    },

    async loadSettlementData() {
      if (!this.currentProjectId) return

      try {
        // 加载结算处理数据
        await this.loadSettlementList()

        // 加载结算提醒数据
        await this.loadReminderList()

        // 加载结算统计数据
        await this.loadSettlementStats()

        console.log('项目结算管理数据加载完成:', this.currentProjectId)
      } catch (error) {
        console.error('加载结算管理数据失败:', error)
        this.$message.error('加载数据失败: ' + error.message)
      }
    },

    // 加载结算处理列表
    async loadSettlementList() {
      const params = {
        pageNum: 1,
        pageSize: 100,
        projectId: this.currentProjectId
      }

      const response = await getProjectSettlementPage(params)
      if (response.code === 1) {
        this.settlementList = response.data || []
      }
    },

    // 加载结算提醒列表
    async loadReminderList() {
      const params = {
        pageNum: 1,
        pageSize: 100,
        projectId: this.currentProjectId
      }

      try {
        const response = await getSettlementReminderList(params)
        if (response.code === 1) {
          this.reminderList = response.data || []
        }
      } catch (error) {
        // 如果提醒接口还未实现，使用默认数据
        console.warn('结算提醒接口未实现，使用默认数据')
      }
    },

    // 加载结算统计数据
    async loadSettlementStats() {
      try {
        const response = await getProjectSettlementStatistics(this.currentProjectId)
        if (response.code === 1) {
          this.settlementStats = response.data || {
            totalSettlements: 0,
            totalAmount: 0,
            paidAmount: 0,
            unpaidAmount: 0
          }
        }
      } catch (error) {
        console.warn('结算统计接口调用失败，使用默认数据')
      }
    },

    // 结算提醒相关方法
    handleAddReminder() {
      this.reminderDialogTitle = '添加结算提醒'
      this.reminderForm = {
        settlementProject: '',
        contractNumber: '',
        settlementType: null,
        settlementAmount: null,
        plannedSettlementDate: null,
        reminderDays: 7,
        settlementConditions: '',
        remarks: ''
      }
      this.reminderDialogVisible = true
    },

    handleEditReminder(row, index) {
      this.reminderDialogTitle = '编辑结算提醒'
      this.reminderForm = { ...row }
      this.reminderDialogVisible = true
    },

    handleViewReminder(row) {
      this.$message.info('查看提醒详情功能开发中...')
    },

    handleDeleteReminder(index) {
      this.$confirm('确定要删除这条结算提醒吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.reminderList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    async handleSaveReminder() {
      this.$refs.reminderForm.validate(async (valid) => {
        if (valid) {
          this.saveLoading = true
          try {
            const reminderData = {
              ...this.reminderForm,
              projectId: this.currentProjectId
            }

            let response
            if (this.reminderForm.id) {
              // 更新提醒
              response = await updateSettlementReminder(reminderData)
            } else {
              // 创建提醒
              response = await createSettlementReminder(reminderData)
            }

            if (response.code === 1) {
              this.$message.success('保存成功')
              this.reminderDialogVisible = false
              await this.loadReminderList()
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          } catch (error) {
            console.error('保存结算提醒失败:', error)
            this.$message.error('保存失败: ' + error.message)
          } finally {
            this.saveLoading = false
          }
        }
      })
    },

    handleProcessReminder(row) {
      this.$message.info('处理提醒功能开发中...')
    },

    handleSelectionChange(selection) {
      this.selectedReminders = selection
    },

    batchProcess() {
      if (this.selectedReminders.length === 0) {
        this.$message.warning('请选择要批量处理的提醒记录')
        return
      }
      this.$message.success('批量处理成功')
    },

    sendNotification() {
      this.$message.success('通知发送成功')
    },

    // 结算处理相关方法
    async handleAddSettlement() {
      try {
        // 生成结算编号
        const response = await generateSettlementNo()
        if (response.code === 1) {
          const settlementNo = response.data
          this.$message.info(`新增结算功能开发中... 建议编号: ${settlementNo}`)
        }
      } catch (error) {
        this.$message.info('新增结算功能开发中...')
      }
    },

    async handleEditSettlement(row, index) {
      try {
        const response = await getProjectSettlementDetail(row.id)
        if (response.code === 1) {
          this.$message.info('编辑结算功能开发中...')
          // 这里可以打开编辑对话框
        }
      } catch (error) {
        this.$message.error('获取结算详情失败: ' + error.message)
      }
    },

    async handleViewSettlement(row) {
      try {
        const response = await getProjectSettlementDetail(row.id)
        if (response.code === 1) {
          this.$message.info('查看结算详情功能开发中...')
          // 这里可以打开详情对话框
        }
      } catch (error) {
        this.$message.error('获取结算详情失败: ' + error.message)
      }
    },

    async handleDeleteSettlement(index) {
      const settlement = this.settlementList[index]
      this.$confirm('确定要删除这条结算记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteProjectSettlement(settlement.id)
          if (response.code === 1) {
            this.$message.success('删除成功')
            await this.loadSettlementList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败: ' + error.message)
        }
      }).catch(() => {})
    },

    async handleApproveSettlement(row) {
      this.$prompt('请输入审核意见', '结算审核', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputPlaceholder: '请输入审核意见'
      }).then(async ({ value }) => {
        try {
          const response = await reviewProjectSettlement(row.id, {
            reviewComments: value || '审核通过'
          })
          if (response.code === 1) {
            this.$message.success('审核成功')
            await this.loadSettlementList()
          } else {
            this.$message.error(response.msg || '审核失败')
          }
        } catch (error) {
          this.$message.error('审核失败: ' + error.message)
        }
      }).catch(() => {})
    },

    handleSettlementSelectionChange(selection) {
      this.selectedSettlements = selection
    },

    async batchApprove() {
      if (this.selectedSettlements.length === 0) {
        this.$message.warning('请选择要批量审核的结算记录')
        return
      }

      this.$prompt('请输入批量审核意见', '批量审核', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputPlaceholder: '请输入审核意见'
      }).then(async ({ value }) => {
        try {
          const ids = this.selectedSettlements.map(item => item.id)
          const response = await batchReviewSettlements({
            ids: ids,
            reviewComments: value || '批量审核通过'
          })

          if (response.code === 1) {
            this.$message.success('批量审核成功')
            await this.loadSettlementList()
            this.selectedSettlements = []
          } else {
            this.$message.error(response.msg || '批量审核失败')
          }
        } catch (error) {
          this.$message.error('批量审核失败: ' + error.message)
        }
      }).catch(() => {})
    },

    generateSettlementReport() {
      this.$message.info('生成结算报表功能开发中...')
    },

    // 结算单据相关方法
    handleAddDocument() {
      this.$message.info('上传单据功能开发中...')
    },

    handlePreviewDocument(row) {
      this.$message.info('预览单据功能开发中...')
    },

    handleDownloadDocument(row) {
      this.$message.info('下载单据功能开发中...')
    },

    handleDeleteDocument(index) {
      this.$confirm('确定要删除这个单据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.documentList.splice(index, 1)
        this.$message.success('删除成功')
      }).catch(() => {})
    },

    handleDocumentSelectionChange(selection) {
      this.selectedDocuments = selection
    },

    batchDownload() {
      if (this.selectedDocuments.length === 0) {
        this.$message.warning('请选择要批量下载的单据')
        return
      }
      this.$message.success('批量下载开始')
    },

    // 工具方法
    formatMoney,

    getSettlementTypeColor(type) {
      const colorMap = { 1: 'primary', 2: 'success', 3: 'warning', 4: 'info' }
      return colorMap[type] || 'info'
    },

    getSettlementTypeName(type) {
      const nameMap = { 1: '进度结算', 2: '完工结算', 3: '最终结算', 4: '专项结算' }
      return nameMap[type] || '未知'
    },

    getReminderStatusColor(status) {
      const colorMap = { 1: 'warning', 2: 'success', 3: 'danger' }
      return colorMap[status] || 'info'
    },

    getReminderStatusName(status) {
      const nameMap = { 1: '待处理', 2: '已处理', 3: '已过期' }
      return nameMap[status] || '未知'
    },

    getRemainingDaysClass(days) {
      if (days <= 3) return 'danger'
      if (days <= 7) return 'warning'
      return 'normal'
    },

    getSettlementStatusColor(status) {
      const colorMap = { 1: 'info', 2: 'warning', 3: 'success' }
      return colorMap[status] || 'info'
    },

    getSettlementStatusName(status) {
      const nameMap = { 1: '待结算', 2: '结算中', 3: '已结算' }
      return nameMap[status] || '未知'
    },

    getApprovalStatusColor(status) {
      const colorMap = { 1: 'warning', 2: 'success', 3: 'danger' }
      return colorMap[status] || 'info'
    },

    getApprovalStatusName(status) {
      const nameMap = { 1: '待审核', 2: '已通过', 3: '已拒绝' }
      return nameMap[status] || '未知'
    },

    getDocumentTypeColor(type) {
      const colorMap = { 1: 'primary', 2: 'success', 3: 'warning', 4: 'info' }
      return colorMap[type] || 'info'
    },

    getDocumentTypeName(type) {
      const nameMap = { 1: '结算申请', 2: '结算审核', 3: '结算单据', 4: '其他文件' }
      return nameMap[type] || '未知'
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

.danger {
  color: #F56C6C;
  font-weight: bold;
}

.warning {
  color: #E6A23C;
  font-weight: bold;
}

.normal {
  color: #606266;
}

.dialog-footer {
  text-align: right;
}
</style>
