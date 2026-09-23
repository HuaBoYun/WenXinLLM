<template>
  <div class="cost-allocation-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title">成本分摊管理</h2>
        <p class="page-description">管理企业成本分摊流程，支持多种分摊方法和智能化计算</p>
      </div>
      <div class="header-right">
        <el-button-group>
          <el-button
            :type="currentView === 'dashboard' ? 'primary' : 'default'"
            icon="el-icon-data-analysis"
            @click="switchView('dashboard')"
          >
            仪表板
          </el-button>
          <el-button
            :type="currentView === 'list' ? 'primary' : 'default'"
            icon="el-icon-s-grid"
            @click="switchView('list')"
          >
            列表视图
          </el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="page-content">
      <!-- 仪表板视图 -->
      <CostAllocationDashboard
        v-if="currentView === 'dashboard'"
        @view-allocation="handleViewAllocation"
        @create-allocation="handleCreateAllocation"
        @batch-start="handleBatchStart"
        @batch-stop="handleBatchStop"
        @batch-approve="handleBatchApprove"
        @generate-report="handleGenerateReport"
      />

      <!-- 列表视图 -->
      <CostAllocationList
        v-if="currentView === 'list'"
        ref="allocationList"
      />
    </div>

    <!-- 成本分摊详情对话框 -->
    <CostAllocationDetail
      :visible.sync="detailVisible"
      :cost-allocation-id="currentAllocationId"
      :mode="detailMode"
      @refresh="handleRefresh"
    />

    <!-- 批量操作对话框 -->
    <el-dialog title="批量操作" :visible.sync="batchDialogVisible" width="600px">
      <div class="batch-operation">
        <el-alert
          :title="`已选择 ${selectedAllocations.length} 个成本分摊`"
          type="info"
          :closable="false"
          style="margin-bottom: 20px"
        />
        
        <div class="operation-buttons">
          <el-button
            type="success"
            icon="el-icon-video-play"
            @click="confirmBatchStart"
          >
            批量开始计算
          </el-button>
          <el-button
            type="warning"
            icon="el-icon-video-pause"
            @click="confirmBatchStop"
          >
            批量停止计算
          </el-button>
          <el-button
            type="info"
            icon="el-icon-check"
            @click="confirmBatchApprove"
          >
            批量审批通过
          </el-button>
          <el-button
            type="danger"
            icon="el-icon-close"
            @click="confirmBatchReject"
          >
            批量审批拒绝
          </el-button>
        </div>

        <div v-if="batchOperation === 'approve' || batchOperation === 'reject'" class="batch-form">
          <el-form :model="batchForm" label-width="80px">
            <el-form-item :label="batchOperation === 'approve' ? '审批意见' : '拒绝原因'">
              <el-input
                v-model="batchForm.comments"
                type="textarea"
                :rows="4"
                :placeholder="batchOperation === 'approve' ? '请输入审批意见（可选）' : '请输入拒绝原因（必填）'"
              />
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button
          v-if="batchOperation"
          type="primary"
          @click="executeBatchOperation"
        >
          确定执行
        </el-button>
      </div>
    </el-dialog>

    <!-- 报告生成对话框 -->
    <el-dialog title="生成分摊报告" :visible.sync="reportDialogVisible" width="500px">
      <el-form :model="reportForm" label-width="100px">
        <el-form-item label="报告类型">
          <el-select v-model="reportForm.type" placeholder="请选择报告类型" style="width: 100%">
            <el-option label="分摊汇总报告" value="summary" />
            <el-option label="分摊明细报告" value="detail" />
            <el-option label="分摊分析报告" value="analysis" />
            <el-option label="分摊对比报告" value="comparison" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="reportForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="报告格式">
          <el-radio-group v-model="reportForm.format">
            <el-radio label="pdf">PDF</el-radio>
            <el-radio label="excel">Excel</el-radio>
            <el-radio label="word">Word</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="reportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="generateReport">生成报告</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import CostAllocationDashboard from './CostAllocationDashboard'
import CostAllocationList from './CostAllocationList'
import CostAllocationDetail from './CostAllocationDetail'
import {
  batchStartCalculation,
  batchStopCalculation,
  batchApprove,
  batchReject,
  generateBatchAllocationReport
} from '@/api/managementAccountant/ss/costAllocation'

export default {
  name: 'CostAllocationIndex',
  components: {
    CostAllocationDashboard,
    CostAllocationList,
    CostAllocationDetail
  },
  data() {
    return {
      currentView: 'dashboard', // dashboard, list
      // 详情对话框
      detailVisible: false,
      detailMode: 'view',
      currentAllocationId: null,
      // 批量操作
      batchDialogVisible: false,
      batchOperation: '', // start, stop, approve, reject
      selectedAllocations: [],
      batchForm: {
        comments: ''
      },
      // 报告生成
      reportDialogVisible: false,
      reportForm: {
        type: 'summary',
        dateRange: [],
        format: 'pdf'
      }
    }
  },
  methods: {
    // 切换视图
    switchView(view) {
      this.currentView = view
    },

    // 查看分摊详情
    handleViewAllocation(allocationId) {
      this.currentAllocationId = allocationId
      this.detailMode = 'view'
      this.detailVisible = true
    },

    // 创建分摊
    handleCreateAllocation() {
      this.currentAllocationId = null
      this.detailMode = 'create'
      this.detailVisible = true
    },

    // 批量开始
    handleBatchStart() {
      this.selectedAllocations = this.getSelectedAllocations()
      if (this.selectedAllocations.length === 0) {
        this.$message.warning('请先选择要操作的成本分摊')
        return
      }
      this.batchOperation = 'start'
      this.batchDialogVisible = true
    },

    // 批量停止
    handleBatchStop() {
      this.selectedAllocations = this.getSelectedAllocations()
      if (this.selectedAllocations.length === 0) {
        this.$message.warning('请先选择要操作的成本分摊')
        return
      }
      this.batchOperation = 'stop'
      this.batchDialogVisible = true
    },

    // 批量审批
    handleBatchApprove() {
      this.selectedAllocations = this.getSelectedAllocations()
      if (this.selectedAllocations.length === 0) {
        this.$message.warning('请先选择要操作的成本分摊')
        return
      }
      this.batchOperation = 'approve'
      this.batchDialogVisible = true
    },

    // 生成报告
    handleGenerateReport() {
      this.reportDialogVisible = true
    },

    // 确认批量开始
    confirmBatchStart() {
      this.batchOperation = 'start'
    },

    // 确认批量停止
    confirmBatchStop() {
      this.batchOperation = 'stop'
    },

    // 确认批量审批
    confirmBatchApprove() {
      this.batchOperation = 'approve'
    },

    // 确认批量拒绝
    confirmBatchReject() {
      this.batchOperation = 'reject'
    },

    // 执行批量操作
    async executeBatchOperation() {
      if (!this.batchOperation) return

      try {
        const ids = this.selectedAllocations.map(item => item.allocationId)
        let response

        switch (this.batchOperation) {
          case 'start':
            response = await batchStartCalculation(ids)
            break
          case 'stop':
            response = await batchStopCalculation(ids)
            break
          case 'approve':
            response = await batchApprove(ids, this.batchForm.comments)
            break
          case 'reject':
            if (!this.batchForm.comments.trim()) {
              this.$message.warning('请输入拒绝原因')
              return
            }
            response = await batchReject(ids, this.batchForm.comments)
            break
        }

        if (response && response.success) {
          this.$message.success('批量操作成功')
          this.batchDialogVisible = false
          this.batchOperation = ''
          this.batchForm.comments = ''
          this.handleRefresh()
        } else {
          this.$message.error(response?.message || '批量操作失败')
        }
      } catch (error) {
        console.error('批量操作失败:', error)
        this.$message.error('批量操作失败')
      }
    },

    // 生成报告
    async generateReport() {
      if (!this.reportForm.type) {
        this.$message.warning('请选择报告类型')
        return
      }

      try {
        const params = {
          type: this.reportForm.type,
          format: this.reportForm.format
        }

        if (this.reportForm.dateRange && this.reportForm.dateRange.length === 2) {
          params.startDate = this.reportForm.dateRange[0].toISOString().split('T')[0]
          params.endDate = this.reportForm.dateRange[1].toISOString().split('T')[0]
        }

        // 这里应该调用报告生成API
        this.$message.success('报告生成成功')
        this.reportDialogVisible = false
        this.reportForm = {
          type: 'summary',
          dateRange: [],
          format: 'pdf'
        }
      } catch (error) {
        console.error('生成报告失败:', error)
        this.$message.error('生成报告失败')
      }
    },

    // 获取选中的分摊
    getSelectedAllocations() {
      if (this.$refs.allocationList) {
        return this.$refs.allocationList.selectedRows || []
      }
      return []
    },

    // 刷新数据
    handleRefresh() {
      if (this.currentView === 'list' && this.$refs.allocationList) {
        this.$refs.allocationList.loadData()
      }
      // 仪表板会自动刷新
    }
  }
}
</script>

<style scoped>
.cost-allocation-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: white;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 20px;
}

.header-left {
  flex: 1;
}

.page-title {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.page-description {
  margin: 0;
  font-size: 14px;
  color: #606266;
}

.header-right {
  margin-left: 20px;
}

.page-content {
  flex: 1;
  overflow: hidden;
}

.batch-operation {
  padding: 20px 0;
}

.operation-buttons {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.batch-form {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.dialog-footer {
  text-align: right;
}
</style>
