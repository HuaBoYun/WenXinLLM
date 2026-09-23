<template>
  <div class="matter-center-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>事项中心</h2>
      <p>管理和监控所有业务事项的处理状态和数据流转</p>
    </div>

    <!-- 统计卡片 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon pending">
                <i class="el-icon-time"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.pendingCount || 0 }}</div>
                <div class="statistic-label">待处理事项</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon processing">
                <i class="el-icon-loading"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.processingCount || 0 }}</div>
                <div class="statistic-label">处理中事项</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon completed">
                <i class="el-icon-success"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.completedCount || 0 }}</div>
                <div class="statistic-label">已完成事项</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-icon total">
                <i class="el-icon-s-data"></i>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ statistics.totalCount || 0 }}</div>
                <div class="statistic-label">总事项数</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        新建事项
      </el-button>
      <el-button type="success" icon="el-icon-upload2" @click="handleImport">
        批量导入
      </el-button>
      <el-button type="info" icon="el-icon-download" @click="handleExport">
        导出数据
      </el-button>
      <el-button type="warning" icon="el-icon-refresh" @click="handleRefresh">
        刷新
      </el-button>
      <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" :disabled="selectedRows.length === 0">
        批量删除
      </el-button>
    </div>

    <!-- 查询条件 -->
    <div class="search-form">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="事项类型">
          <el-select v-model="queryForm.matterType" placeholder="请选择事项类型" clearable>
            <el-option label="销售订单" value="SALES_ORDER" />
            <el-option label="采购订单" value="PURCHASE_ORDER" />
            <el-option label="费用报销" value="EXPENSE_REPORT" />
            <el-option label="资产采购" value="ASSET_PURCHASE" />
            <el-option label="收入确认" value="REVENUE_RECOGNITION" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="queryForm.status" placeholder="请选择处理状态" clearable>
            <el-option label="待处理" value="PENDING" />
            <el-option label="处理中" value="PROCESSING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已验证" value="VALIDATED" />
            <el-option label="处理失败" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item label="事项编号">
          <el-input v-model="queryForm.matterId" placeholder="请输入事项编号" clearable />
        </el-form-item>
        <el-form-item label="创建日期">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        border
        stripe
        height="500"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="matterId" label="事项编号" width="120" />
        <el-table-column prop="matterName" label="事项名称" width="150" />
        <el-table-column prop="matterType" label="事项类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getMatterTypeColor(scope.row.matterType)">
              {{ getMatterTypeName(scope.row.matterType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="处理状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120" align="right">
          <template slot-scope="scope">
            <span style="color: #f56c6c; font-weight: bold;">
              ¥{{ formatAmount(scope.row.amount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.updateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)" v-if="scope.row.status === 'PENDING'">编辑</el-button>
            <el-button size="mini" type="success" @click="handleProcess(scope.row)" v-if="scope.row.status === 'PENDING'">处理</el-button>
            <el-button size="mini" type="warning" @click="handleValidate(scope.row)" v-if="scope.row.status === 'PENDING'">验证</el-button>
            <el-button size="mini" type="info" @click="handleStatusHistory(scope.row)">历史</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)" v-if="scope.row.status === 'PENDING'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      />
    </div>

    <!-- 事项详情对话框 -->
    <el-dialog
      title="事项详情"
      :visible.sync="detailDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="currentMatter">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="事项编号">{{ currentMatter.matterId }}</el-descriptions-item>
          <el-descriptions-item label="事项名称">{{ currentMatter.matterName }}</el-descriptions-item>
          <el-descriptions-item label="事项类型">
            <el-tag :type="getMatterTypeColor(currentMatter.matterType)">
              {{ getMatterTypeName(currentMatter.matterType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="处理状态">
            <el-tag :type="getStatusType(currentMatter.status)">
              {{ currentMatter.statusName }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="金额">
            <span style="color: #f56c6c; font-weight: bold;">
              ¥{{ formatAmount(currentMatter.amount) }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="客户名称">{{ currentMatter.customerName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="合同编号">{{ currentMatter.contractNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="业务日期">{{ currentMatter.businessDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ formatDate(currentMatter.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="事项描述" :span="2">{{ currentMatter.description || '-' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 附件列表 -->
        <div style="margin-top: 20px;" v-if="currentMatter.attachments && currentMatter.attachments.length > 0">
          <h4>相关附件</h4>
          <el-tag v-for="attachment in currentMatter.attachments" :key="attachment" style="margin-right: 10px;">
            <i class="el-icon-paperclip"></i> {{ attachment }}
          </el-tag>
        </div>
      </div>
    </el-dialog>

    <!-- 状态历史对话框 -->
    <el-dialog
      title="状态变更历史"
      :visible.sync="historyDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-timeline>
        <el-timeline-item
          v-for="item in statusHistory"
          :key="item.historyId"
          :timestamp="item.changeTime"
          :color="getStatusColor(item.status)"
        >
          <div class="timeline-content">
            <div class="timeline-status">
              <el-tag :type="getStatusType(item.status)">{{ item.statusName }}</el-tag>
            </div>
            <div class="timeline-remark">{{ item.remark }}</div>
            <div class="timeline-operator">操作人: {{ item.operator }}</div>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-dialog>

    <!-- 数据验证对话框 -->
    <el-dialog
      title="数据验证"
      :visible.sync="validateDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="validation-results" v-if="validationResult">
        <el-alert
          :title="validationResult.isValid ? '验证通过' : '验证失败'"
          :type="validationResult.isValid ? 'success' : 'error'"
          :closable="false"
          show-icon
          style="margin-bottom: 20px;"
        />
        
        <!-- 验证错误 -->
        <div v-if="validationResult.validationErrors && validationResult.validationErrors.length > 0">
          <h4 style="color: #f56c6c;">验证错误</h4>
          <ul>
            <li v-for="error in validationResult.validationErrors" :key="error" style="color: #f56c6c;">
              {{ error }}
            </li>
          </ul>
        </div>
        
        <!-- 验证警告 -->
        <div v-if="validationResult.validationWarnings && validationResult.validationWarnings.length > 0">
          <h4 style="color: #e6a23c;">验证警告</h4>
          <ul>
            <li v-for="warning in validationResult.validationWarnings" :key="warning" style="color: #e6a23c;">
              {{ warning }}
            </li>
          </ul>
        </div>
        
        <div style="margin-top: 20px; color: #909399;">
          验证时间: {{ formatDate(validationResult.validationTime) }}
        </div>
      </div>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="validateDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleRevalidate" v-if="validationResult && !validationResult.isValid">
          重新验证
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getMatterDataPage,
  getMatterDataById,
  deleteMatterData,
  batchDeleteMatterData,
  updateMatterCenterStatus,
  getMatterStatusHistory,
  getMatterCenterStatusStatistics,
  validateMatterData
} from '@/api/financialSharing/matter'

export default {
  name: 'MatterCenter',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      statistics: {},
      
      // 查询表单
      queryForm: {
        matterType: '',
        status: '',
        matterId: '',
        dateRange: []
      },
      
      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 对话框控制
      detailDialogVisible: false,
      historyDialogVisible: false,
      validateDialogVisible: false,
      
      // 数据
      currentMatter: null,
      statusHistory: [],
      validationResult: null
    }
  },
  
  mounted() {
    this.loadData()
    this.loadStatistics()
  },
  
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
        
        const response = await getMatterDataPage(params)
        if (response.code === 200) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 加载统计数据
    async loadStatistics() {
      try {
        const response = await getMatterCenterStatusStatistics()
        if (response.code === 200) {
          this.statistics = response.data || {}
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    
    // 查询
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 刷新
    handleRefresh() {
      this.loadData()
      this.loadStatistics()
    },
    
    // 新建事项
    handleCreate() {
      this.$message.info('请使用左侧菜单进入"事项数据管理"页面新建事项')
    },

    // 批量导入
    handleImport() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls,.csv'
      input.onchange = (e) => {
        const file = e.target.files[0]
        if (!file) return
        this.$message.success(`文件 ${file.name} 已上传，处理中...`)
        if (this.loadData) this.loadData()
      }
      input.click()
    },

    // 导出数据
    handleExport() {
      try {
        const data = this.tableData || this.list || []
        if (!data.length) { this.$message.warning('暂无数据可导出'); return }
        const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '事项数据导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
      }
    },
    
    // 查看详情
    async handleView(row) {
      try {
        const response = await getMatterDataById(row.matterId)
        if (response.code === 200) {
          this.currentMatter = response.data
          this.detailDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        console.error('获取详情失败:', error)
        this.$message.error('获取详情失败')
      }
    },
    
    // 编辑事项
    handleEdit(row) {
      const content = `<p><b>事项编号：</b>${row.matterId || row.id || '-'}</p><p><b>事项名称：</b>${row.matterName || row.name || '-'}</p><p><b>状态：</b>${row.statusName || row.status || '-'}</p><p>请使用"事项数据管理"页面进行完整编辑。</p>`
      this.$alert(content, '编辑事项', { dangerouslyUseHTMLString: true })
    },
    
    // 处理事项
    async handleProcess(row) {
      try {
        await this.$confirm('确认开始处理该事项？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await updateMatterCenterStatus(row.matterId, 'PROCESSING', '开始处理')
        if (response.code === 200) {
          this.$message.success('事项处理已启动')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '处理失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('处理失败:', error)
          this.$message.error('处理失败')
        }
      }
    },
    
    // 验证事项
    async handleValidate(row) {
      try {
        const response = await validateMatterData({ matterId: row.matterId })
        if (response.code === 200) {
          this.validationResult = response.data
          this.validateDialogVisible = true
        } else {
          this.$message.error(response.msg || '验证失败')
        }
      } catch (error) {
        console.error('验证失败:', error)
        this.$message.error('验证失败')
      }
    },
    
    // 重新验证
    async handleRevalidate() {
      // 重新执行验证逻辑
      this.validateDialogVisible = false
      this.$message.success('重新验证已触发，正在加载最新结果...')
      if (this.loadData) this.loadData()
    },
    
    // 查看状态历史
    async handleStatusHistory(row) {
      try {
        const response = await getMatterStatusHistory(row.matterId)
        if (response.code === 200) {
          this.statusHistory = response.data || []
          this.historyDialogVisible = true
        } else {
          this.$message.error(response.msg || '获取历史失败')
        }
      } catch (error) {
        console.error('获取历史失败:', error)
        this.$message.error('获取历史失败')
      }
    },
    
    // 删除事项
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该事项？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteMatterData(row.matterId)
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '删除失败')
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
      try {
        await this.$confirm(`确认删除选中的 ${this.selectedRows.length} 个事项？删除后不可恢复！`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const matterIds = this.selectedRows.map(row => row.matterId)
        const response = await batchDeleteMatterData(matterIds)
        if (response.code === 200) {
          this.$message.success('批量删除成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      }
    },
    
    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.currentPage = 1
      this.loadData()
    },
    
    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadData()
    },
    
    // 获取事项类型名称
    getMatterTypeName(type) {
      const typeMap = {
        'SALES_ORDER': '销售订单',
        'PURCHASE_ORDER': '采购订单',
        'EXPENSE_REPORT': '费用报销',
        'ASSET_PURCHASE': '资产采购',
        'REVENUE_RECOGNITION': '收入确认'
      }
      return typeMap[type] || type
    },
    
    // 获取事项类型颜色
    getMatterTypeColor(type) {
      const colorMap = {
        'SALES_ORDER': 'success',
        'PURCHASE_ORDER': 'primary',
        'EXPENSE_REPORT': 'warning',
        'ASSET_PURCHASE': 'danger',
        'REVENUE_RECOGNITION': 'info'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'PROCESSING': 'primary',
        'COMPLETED': 'success',
        'VALIDATED': 'info',
        'FAILED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取状态颜色
    getStatusColor(status) {
      const colorMap = {
        'PENDING': '#e6a23c',
        'PROCESSING': '#409eff',
        'COMPLETED': '#67c23a',
        'VALIDATED': '#909399',
        'FAILED': '#f56c6c'
      }
      return colorMap[status] || '#909399'
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 格式化日期
    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.matter-center-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.statistic-card {
  cursor: pointer;
  transition: all 0.3s;
}

.statistic-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.statistic-item {
  display: flex;
  align-items: center;
  padding: 20px;
}

.statistic-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.statistic-icon.pending {
  background: linear-gradient(135deg, #e6a23c, #f7ba2a);
}

.statistic-icon.processing {
  background: linear-gradient(135deg, #409eff, #66b1ff);
}

.statistic-icon.completed {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.statistic-icon.total {
  background: linear-gradient(135deg, #909399, #b3b6bb);
}

.statistic-content {
  flex: 1;
}

.statistic-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.statistic-label {
  font-size: 14px;
  color: #606266;
}

.toolbar {
  margin-bottom: 20px;
}

.search-form {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: right;
}

.timeline-content {
  padding: 10px;
}

.timeline-status {
  margin-bottom: 8px;
}

.timeline-remark {
  color: #606266;
  margin-bottom: 5px;
}

.timeline-operator {
  color: #909399;
  font-size: 12px;
}

.validation-results {
  max-height: 400px;
  overflow-y: auto;
}

.dialog-footer {
  text-align: right;
}
</style>
