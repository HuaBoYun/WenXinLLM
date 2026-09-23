<template>
  <div class="cost-allocation-list">
    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="分摊名称">
          <el-input
            v-model="searchForm.allocationName"
            placeholder="请输入分摊名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="分摊编码">
          <el-input
            v-model="searchForm.allocationCode"
            placeholder="请输入分摊编码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="分摊状态">
          <el-select v-model="searchForm.allocationStatus" placeholder="请选择分摊状态" clearable style="width: 150px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="活跃" value="ACTIVE" />
            <el-option label="计算中" value="CALCULATING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已暂停" value="SUSPENDED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="分摊类型">
          <el-select v-model="searchForm.allocationType" placeholder="请选择分摊类型" clearable style="width: 150px">
            <el-option label="直接分摊" value="DIRECT" />
            <el-option label="间接分摊" value="INDIRECT" />
            <el-option label="阶梯分摊" value="STEP" />
            <el-option label="交互分摊" value="RECIPROCAL" />
            <el-option label="作业分摊" value="ACTIVITY" />
            <el-option label="价值分摊" value="VALUE" />
          </el-select>
        </el-form-item>
        <el-form-item label="分摊方法">
          <el-select v-model="searchForm.allocationMethod" placeholder="请选择分摊方法" clearable style="width: 150px">
            <el-option label="平均分摊" value="EQUAL" />
            <el-option label="加权分摊" value="WEIGHTED" />
            <el-option label="比例分摊" value="PROPORTIONAL" />
            <el-option label="作业成本分摊" value="ACTIVITY_BASED" />
            <el-option label="标准分摊" value="STANDARD" />
            <el-option label="实际分摊" value="ACTUAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="searchForm.createTimeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作区域 -->
    <el-card class="operation-card">
      <div class="operation-buttons">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">新建分摊</el-button>
        <el-button type="success" icon="el-icon-video-play" :disabled="!hasSelection" @click="handleBatchStart">批量开始</el-button>
        <el-button type="warning" icon="el-icon-video-pause" :disabled="!hasSelection" @click="handleBatchStop">批量停止</el-button>
        <el-button type="info" icon="el-icon-check" :disabled="!hasSelection" @click="handleBatchApprove">批量审批</el-button>
        <el-button type="danger" icon="el-icon-delete" :disabled="!hasSelection" @click="handleBatchDelete">批量删除</el-button>
        <el-button type="primary" icon="el-icon-download" @click="handleExport">导出</el-button>
        <el-button type="success" icon="el-icon-upload2" @click="handleImport">导入</el-button>
      </div>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="allocationCode" label="分摊编码" width="150" sortable="custom" />
        <el-table-column prop="allocationName" label="分摊名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="allocationType" label="分摊类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getAllocationTypeTagType(scope.row.allocationType)" size="mini">
              {{ formatAllocationType(scope.row.allocationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="allocationMethod" label="分摊方法" width="120">
          <template slot-scope="scope">
            <el-tag type="info" size="mini">
              {{ formatAllocationMethod(scope.row.allocationMethod) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="allocationStatus" label="分摊状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getAllocationStatusTagType(scope.row.allocationStatus)" size="mini">
              {{ formatAllocationStatus(scope.row.allocationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="calculationStatus" label="计算状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getCalculationStatusTagType(scope.row.calculationStatus)" size="mini">
              {{ formatCalculationStatus(scope.row.calculationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalCostAmount" label="总成本金额" width="120" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.totalCostAmount">{{ formatAmount(scope.row.totalCostAmount) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="allocatedAmount" label="已分摊金额" width="120" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.allocatedAmount">{{ formatAmount(scope.row.allocatedAmount) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="allocationPercentage" label="分摊比例" width="100" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.allocationPercentage">{{ formatPercentage(scope.row.allocationPercentage) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="successRate" label="成功率" width="100" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.successRate">{{ formatPercentage(scope.row.successRate) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" align="center" />
        <el-table-column prop="createdTime" label="创建时间" width="160" sortable="custom">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-dropdown @command="(command) => handleDropdownCommand(command, scope.row)" trigger="click">
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="start" :disabled="!canStart(scope.row)">开始计算</el-dropdown-item>
                <el-dropdown-item command="stop" :disabled="!canStop(scope.row)">停止计算</el-dropdown-item>
                <el-dropdown-item command="pause" :disabled="!canPause(scope.row)">暂停计算</el-dropdown-item>
                <el-dropdown-item command="resume" :disabled="!canResume(scope.row)">恢复计算</el-dropdown-item>
                <el-dropdown-item command="recalculate" :disabled="!canRecalculate(scope.row)">重新计算</el-dropdown-item>
                <el-dropdown-item command="approve" :disabled="!canApprove(scope.row)">审批通过</el-dropdown-item>
                <el-dropdown-item command="reject" :disabled="!canReject(scope.row)">审批拒绝</el-dropdown-item>
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="report">生成报告</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 成本分摊详情对话框 -->
    <CostAllocationDetail
      :visible.sync="detailVisible"
      :cost-allocation-id="currentCostAllocationId"
      :mode="detailMode"
      @refresh="loadData"
    />

    <!-- 批量审批对话框 -->
    <el-dialog title="批量审批" :visible.sync="batchApprovalVisible" width="500px">
      <el-form :model="batchApprovalForm" label-width="80px">
        <el-form-item label="审批意见">
          <el-input
            v-model="batchApprovalForm.comments"
            type="textarea"
            :rows="4"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchApprovalVisible = false">取消</el-button>
        <el-button type="success" @click="confirmBatchApproval">审批通过</el-button>
        <el-button type="danger" @click="confirmBatchReject">审批拒绝</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog title="导入成本分摊" :visible.sync="importVisible" width="600px">
      <el-upload
        ref="upload"
        :action="uploadAction"
        :on-success="handleImportSuccess"
        :on-error="handleImportError"
        :before-upload="beforeUpload"
        :file-list="fileList"
        accept=".xlsx,.xls"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importVisible = false">取消</el-button>
        <el-button type="primary" @click="submitImport">确定导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCostAllocationPage,
  deleteCostAllocation,
  batchDeleteCostAllocation,
  startAllocationCalculation,
  stopAllocationCalculation,
  pauseAllocationCalculation,
  resumeAllocationCalculation,
  recalculateAllocation,
  batchStartCalculation,
  batchStopCalculation,
  approveAllocation,
  rejectAllocation,
  batchApprove,
  batchReject,
  copyCostAllocation,
  generateAllocationReport,
  exportCostAllocationData,
  importCostAllocationData,
  costAllocationUtils
} from '@/api/managementAccountant/ss/costAllocation'
import CostAllocationDetail from './CostAllocationDetail'

export default {
  name: 'CostAllocationList',
  components: {
    CostAllocationDetail
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        allocationName: '',
        allocationCode: '',
        allocationStatus: '',
        allocationType: '',
        allocationMethod: '',
        createTimeRange: []
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      sortField: '',
      sortOrder: '',
      // 对话框控制
      detailVisible: false,
      detailMode: 'view',
      currentCostAllocationId: null,
      batchApprovalVisible: false,
      batchApprovalForm: {
        comments: ''
      },
      importVisible: false,
      uploadAction: '/api/upload',
      fileList: []
    }
  },
  computed: {
    hasSelection() {
      return this.selectedRows.length > 0
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }

        // 处理时间范围
        if (this.searchForm.createTimeRange && this.searchForm.createTimeRange.length === 2) {
          params.startTime = this.searchForm.createTimeRange[0]
          params.endTime = this.searchForm.createTimeRange[1]
        }

        // 处理排序
        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }

        const response = await getCostAllocationPage(params)
        if (response.success) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.searchForm = {
        allocationName: '',
        allocationCode: '',
        allocationStatus: '',
        allocationType: '',
        allocationMethod: '',
        createTimeRange: []
      }
      this.pagination.current = 1
      this.loadData()
    },

    // 新建
    handleCreate() {
      this.currentCostAllocationId = null
      this.detailMode = 'create'
      this.detailVisible = true
    },

    // 查看
    handleView(row) {
      this.currentCostAllocationId = row.allocationId
      this.detailMode = 'view'
      this.detailVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.currentCostAllocationId = row.allocationId
      this.detailMode = 'edit'
      this.detailVisible = true
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个成本分摊吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteCostAllocation(row.allocationId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },

    // 批量删除
    async handleBatchDelete() {
      if (!this.hasSelection) {
        this.$message.warning('请选择要删除的记录')
        return
      }

      try {
        await this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 条记录吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const ids = this.selectedRows.map(row => row.allocationId)
        const response = await batchDeleteCostAllocation(ids)
        if (response.success) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      }
    },

    // 批量开始
    async handleBatchStart() {
      if (!this.hasSelection) {
        this.$message.warning('请选择要开始的记录')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.allocationId)
        const response = await batchStartCalculation(ids)
        if (response.success) {
          this.$message.success('批量开始成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量开始失败')
        }
      } catch (error) {
        console.error('批量开始失败:', error)
        this.$message.error('批量开始失败')
      }
    },

    // 批量停止
    async handleBatchStop() {
      if (!this.hasSelection) {
        this.$message.warning('请选择要停止的记录')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.allocationId)
        const response = await batchStopCalculation(ids)
        if (response.success) {
          this.$message.success('批量停止成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量停止失败')
        }
      } catch (error) {
        console.error('批量停止失败:', error)
        this.$message.error('批量停止失败')
      }
    },

    // 批量审批
    handleBatchApprove() {
      if (!this.hasSelection) {
        this.$message.warning('请选择要审批的记录')
        return
      }
      this.batchApprovalVisible = true
    },

    // 确认批量审批通过
    async confirmBatchApproval() {
      try {
        const ids = this.selectedRows.map(row => row.allocationId)
        const response = await batchApprove(ids, this.batchApprovalForm.comments)
        if (response.success) {
          this.$message.success('批量审批通过成功')
          this.batchApprovalVisible = false
          this.batchApprovalForm.comments = ''
          this.loadData()
        } else {
          this.$message.error(response.message || '批量审批通过失败')
        }
      } catch (error) {
        console.error('批量审批通过失败:', error)
        this.$message.error('批量审批通过失败')
      }
    },

    // 确认批量审批拒绝
    async confirmBatchReject() {
      if (!this.batchApprovalForm.comments.trim()) {
        this.$message.warning('请输入拒绝原因')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.allocationId)
        const response = await batchReject(ids, this.batchApprovalForm.comments)
        if (response.success) {
          this.$message.success('批量审批拒绝成功')
          this.batchApprovalVisible = false
          this.batchApprovalForm.comments = ''
          this.loadData()
        } else {
          this.$message.error(response.message || '批量审批拒绝失败')
        }
      } catch (error) {
        console.error('批量审批拒绝失败:', error)
        this.$message.error('批量审批拒绝失败')
      }
    },

    // 导出
    async handleExport() {
      try {
        const params = { ...this.searchForm }
        if (this.searchForm.createTimeRange && this.searchForm.createTimeRange.length === 2) {
          params.startTime = this.searchForm.createTimeRange[0]
          params.endTime = this.searchForm.createTimeRange[1]
        }

        const response = await exportCostAllocationData(params)
        if (response.success) {
          // 处理导出逻辑
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    // 导入
    handleImport() {
      this.importVisible = true
      this.fileList = []
    },

    // 提交导入
    async submitImport() {
      if (this.fileList.length === 0) {
        this.$message.warning('请选择要导入的文件')
        return
      }

      try {
        // 这里应该处理文件上传和数据导入
        this.$message.success('导入成功')
        this.importVisible = false
        this.loadData()
      } catch (error) {
        console.error('导入失败:', error)
        this.$message.error('导入失败')
      }
    },

    // 导入成功
    handleImportSuccess(response, file, fileList) {
      this.$message.success('文件上传成功')
    },

    // 导入失败
    handleImportError(error, file, fileList) {
      this.$message.error('文件上传失败')
    },

    // 上传前检查
    beforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                     file.type === 'application/vnd.ms-excel'
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isExcel) {
        this.$message.error('只能上传Excel文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('文件大小不能超过10MB!')
        return false
      }
      return true
    },

    // 下拉菜单命令处理
    async handleDropdownCommand(command, row) {
      switch (command) {
        case 'start':
          await this.handleStart(row)
          break
        case 'stop':
          await this.handleStop(row)
          break
        case 'pause':
          await this.handlePause(row)
          break
        case 'resume':
          await this.handleResume(row)
          break
        case 'recalculate':
          await this.handleRecalculate(row)
          break
        case 'approve':
          await this.handleApprove(row)
          break
        case 'reject':
          await this.handleReject(row)
          break
        case 'copy':
          await this.handleCopy(row)
          break
        case 'report':
          await this.handleGenerateReport(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 开始计算
    async handleStart(row) {
      try {
        const response = await startAllocationCalculation(row.allocationId)
        if (response.success) {
          this.$message.success('开始计算成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '开始计算失败')
        }
      } catch (error) {
        console.error('开始计算失败:', error)
        this.$message.error('开始计算失败')
      }
    },

    // 停止计算
    async handleStop(row) {
      try {
        const response = await stopAllocationCalculation(row.allocationId)
        if (response.success) {
          this.$message.success('停止计算成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '停止计算失败')
        }
      } catch (error) {
        console.error('停止计算失败:', error)
        this.$message.error('停止计算失败')
      }
    },

    // 暂停计算
    async handlePause(row) {
      try {
        const response = await pauseAllocationCalculation(row.allocationId)
        if (response.success) {
          this.$message.success('暂停计算成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '暂停计算失败')
        }
      } catch (error) {
        console.error('暂停计算失败:', error)
        this.$message.error('暂停计算失败')
      }
    },

    // 恢复计算
    async handleResume(row) {
      try {
        const response = await resumeAllocationCalculation(row.allocationId)
        if (response.success) {
          this.$message.success('恢复计算成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '恢复计算失败')
        }
      } catch (error) {
        console.error('恢复计算失败:', error)
        this.$message.error('恢复计算失败')
      }
    },

    // 重新计算
    async handleRecalculate(row) {
      try {
        const response = await recalculateAllocation(row.allocationId)
        if (response.success) {
          this.$message.success('重新计算成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '重新计算失败')
        }
      } catch (error) {
        console.error('重新计算失败:', error)
        this.$message.error('重新计算失败')
      }
    },

    // 审批通过
    async handleApprove(row) {
      try {
        const { value: comments } = await this.$prompt('请输入审批意见', '审批通过', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: '审批意见（可选）'
        })

        const response = await approveAllocation(row.allocationId, comments || '审批通过')
        if (response.success) {
          this.$message.success('审批通过成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '审批通过失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('审批通过失败:', error)
          this.$message.error('审批通过失败')
        }
      }
    },

    // 审批拒绝
    async handleReject(row) {
      try {
        const { value: comments } = await this.$prompt('请输入拒绝原因', '审批拒绝', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPlaceholder: '拒绝原因（必填）',
          inputValidator: (value) => {
            if (!value || !value.trim()) {
              return '请输入拒绝原因'
            }
            return true
          }
        })

        const response = await rejectAllocation(row.allocationId, comments)
        if (response.success) {
          this.$message.success('审批拒绝成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '审批拒绝失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('审批拒绝失败:', error)
          this.$message.error('审批拒绝失败')
        }
      }
    },

    // 复制
    async handleCopy(row) {
      try {
        const response = await copyCostAllocation(row.allocationId)
        if (response.success) {
          this.$message.success('复制成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '复制失败')
        }
      } catch (error) {
        console.error('复制失败:', error)
        this.$message.error('复制失败')
      }
    },

    // 生成报告
    async handleGenerateReport(row) {
      try {
        const response = await generateAllocationReport(row.allocationId)
        if (response.success) {
          this.$message.success('生成报告成功')
          console.log('报告内容:', response.data)
        } else {
          this.$message.error(response.message || '生成报告失败')
        }
      } catch (error) {
        console.error('生成报告失败:', error)
        this.$message.error('生成报告失败')
      }
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 排序变化
    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order === 'ascending' ? 'asc' : 'desc'
      this.loadData()
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 状态判断方法
    canStart(row) {
      return ['DRAFT', 'SUSPENDED'].includes(row.allocationStatus)
    },

    canStop(row) {
      return ['ACTIVE', 'CALCULATING'].includes(row.allocationStatus)
    },

    canPause(row) {
      return row.allocationStatus === 'CALCULATING'
    },

    canResume(row) {
      return row.allocationStatus === 'SUSPENDED'
    },

    canRecalculate(row) {
      return ['COMPLETED', 'CANCELLED'].includes(row.allocationStatus)
    },

    canApprove(row) {
      return row.approvalStatus === 'PENDING'
    },

    canReject(row) {
      return row.approvalStatus === 'PENDING'
    },

    // 格式化方法
    formatAllocationStatus: costAllocationUtils.formatAllocationStatus,
    formatAllocationType: costAllocationUtils.formatAllocationType,
    formatAllocationMethod: costAllocationUtils.formatAllocationMethod,
    formatCalculationStatus: costAllocationUtils.formatCalculationStatus,
    formatAmount: costAllocationUtils.formatAmount,
    formatPercentage: costAllocationUtils.formatPercentage,

    // 标签类型方法
    getAllocationStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'ACTIVE': 'success',
        'CALCULATING': 'warning',
        'COMPLETED': 'success',
        'SUSPENDED': 'warning',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'info'
    },

    getAllocationTypeTagType(type) {
      const typeMap = {
        'DIRECT': 'primary',
        'INDIRECT': 'success',
        'STEP': 'warning',
        'RECIPROCAL': 'info',
        'ACTIVITY': 'danger',
        'VALUE': 'primary'
      }
      return typeMap[type] || 'info'
    },

    getCalculationStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info',
        'RUNNING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'CANCELLED': 'info',
        'PAUSED': 'warning'
      }
      return typeMap[status] || 'info'
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString()
    }
  }
}
</script>

<style scoped>
.cost-allocation-list {
  padding: 20px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.operation-buttons {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
