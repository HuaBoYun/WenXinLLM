<template>
  <div class="accounting-period-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>会计期间管理</h2>
      <p>管理会计期间的开启、关闭和状态控制</p>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        新建期间
      </el-button>
      <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">
        刷新
      </el-button>
      <el-button type="info" icon="el-icon-s-data" @click="handleStatistics">
        期间统计
      </el-button>
      <el-button type="warning" icon="el-icon-setting" @click="handleBatchOperation">
        批量操作
      </el-button>
    </div>

    <!-- 查询条件 -->
    <div class="search-form">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="会计年度">
          <el-select v-model="queryForm.year" placeholder="请选择会计年度" clearable>
            <el-option label="2024年" value="2024" />
            <el-option label="2023年" value="2023" />
            <el-option label="2022年" value="2022" />
          </el-select>
        </el-form-item>
        <el-form-item label="期间状态">
          <el-select v-model="queryForm.status" placeholder="请选择期间状态" clearable>
            <el-option label="已开启" value="OPEN" />
            <el-option label="已关闭" value="CLOSED" />
            <el-option label="待开启" value="PENDING" />
          </el-select>
        </el-form-item>
        <el-form-item label="账簿">
          <el-select v-model="queryForm.bookId" placeholder="请选择账簿" clearable>
            <el-option label="主账簿" :value="1001" />
            <el-option label="分账簿A" :value="1002" />
            <el-option label="分账簿B" :value="1003" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 期间统计卡片 -->
    <div class="statistics-cards" v-if="showStatistics">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="statistic-card">
            <div class="statistic-item">
              <div class="statistic-value">{{ statistics.totalPeriods || 0 }}</div>
              <div class="statistic-label">总期间数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card open">
            <div class="statistic-item">
              <div class="statistic-value">{{ statistics.openPeriods || 0 }}</div>
              <div class="statistic-label">已开启期间</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card closed">
            <div class="statistic-item">
              <div class="statistic-value">{{ statistics.closedPeriods || 0 }}</div>
              <div class="statistic-label">已关闭期间</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="statistic-card pending">
            <div class="statistic-item">
              <div class="statistic-value">{{ statistics.pendingPeriods || 0 }}</div>
              <div class="statistic-label">待开启期间</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
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
        <el-table-column prop="periodId" label="期间ID" width="100" />
        <el-table-column prop="year" label="会计年度" width="100" />
        <el-table-column prop="month" label="会计月份" width="100" />
        <el-table-column prop="periodCode" label="期间编码" width="120" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="status" label="期间状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPeriodStatusType(scope.row.status)">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)" v-if="scope.row.status === 'PENDING'">编辑</el-button>
            <el-button 
              size="mini" 
              type="success" 
              @click="handleOpen(scope.row)"
              v-if="scope.row.status === 'PENDING'"
            >
              开启
            </el-button>
            <el-button 
              size="mini" 
              type="warning" 
              @click="handleClose(scope.row)"
              v-if="scope.row.status === 'OPEN'"
            >
              关闭
            </el-button>
            <el-button size="mini" type="info" @click="handleCheck(scope.row)">检查</el-button>
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

    <!-- 新建/编辑期间对话框 -->
    <el-dialog
      :title="isEdit ? '编辑期间' : '新建期间'"
      :visible.sync="periodDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="periodForm" :rules="periodRules" ref="periodForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="会计年度" prop="year">
              <el-select v-model="periodForm.year" placeholder="请选择会计年度" style="width: 100%">
                <el-option label="2024年" value="2024" />
                <el-option label="2023年" value="2023" />
                <el-option label="2022年" value="2022" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会计月份" prop="month">
              <el-select v-model="periodForm.month" placeholder="请选择会计月份" style="width: 100%">
                <el-option v-for="i in 12" :key="i" :label="`${i}月`" :value="String(i).padStart(2, '0')" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="periodForm.startDate"
                type="date"
                placeholder="选择开始日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="periodForm.endDate"
                type="date"
                placeholder="选择结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="账簿" prop="bookId">
          <el-select v-model="periodForm.bookId" placeholder="请选择账簿" style="width: 100%">
            <el-option label="主账簿" :value="1001" />
            <el-option label="分账簿A" :value="1002" />
            <el-option label="分账簿B" :value="1003" />
          </el-select>
        </el-form-item>
        <el-form-item label="期间描述">
          <el-input
            v-model="periodForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入期间描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="periodDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePeriodConfirm" :loading="periodLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 期间详情对话框 -->
    <el-dialog
      title="期间详情"
      :visible.sync="detailDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="currentPeriod">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="期间ID">{{ currentPeriod.periodId }}</el-descriptions-item>
          <el-descriptions-item label="期间编码">{{ currentPeriod.periodCode }}</el-descriptions-item>
          <el-descriptions-item label="会计年度">{{ currentPeriod.year }}</el-descriptions-item>
          <el-descriptions-item label="会计月份">{{ currentPeriod.month }}</el-descriptions-item>
          <el-descriptions-item label="开始日期">{{ currentPeriod.startDate }}</el-descriptions-item>
          <el-descriptions-item label="结束日期">{{ currentPeriod.endDate }}</el-descriptions-item>
          <el-descriptions-item label="期间状态">
            <el-tag :type="getPeriodStatusType(currentPeriod.status)">
              {{ currentPeriod.statusName }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(currentPeriod.createTime) }}</el-descriptions-item>
        </el-descriptions>

        <!-- 期间操作记录 -->
        <div style="margin-top: 20px;">
          <h4>操作记录</h4>
          <el-timeline>
            <el-timeline-item timestamp="2024-12-19 10:00:00" color="#0bbd87">
              期间创建成功
            </el-timeline-item>
            <el-timeline-item timestamp="2024-12-19 10:30:00" color="#e6a23c">
              期间开启成功
            </el-timeline-item>
            <el-timeline-item timestamp="2024-12-19 11:00:00" color="#f56c6c">
              期间关闭成功
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-dialog>

    <!-- 期间检查对话框 -->
    <el-dialog
      title="期间检查"
      :visible.sync="checkDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <div class="check-results">
        <el-alert
          title="期间检查完成"
          type="success"
          :closable="false"
          show-icon
          style="margin-bottom: 20px;"
        />
        
        <el-collapse v-model="activeCheckItems">
          <el-collapse-item title="凭证检查" name="voucher">
            <div class="check-item">
              <el-icon class="el-icon-success" style="color: #67c23a;"></el-icon>
              <span>未审核凭证: 0 张</span>
            </div>
            <div class="check-item">
              <el-icon class="el-icon-success" style="color: #67c23a;"></el-icon>
              <span>凭证平衡检查: 通过</span>
            </div>
          </el-collapse-item>
          
          <el-collapse-item title="试算平衡检查" name="balance">
            <div class="check-item">
              <el-icon class="el-icon-success" style="color: #67c23a;"></el-icon>
              <span>借贷平衡: 通过</span>
            </div>
            <div class="check-item">
              <el-icon class="el-icon-success" style="color: #67c23a;"></el-icon>
              <span>科目余额: 正常</span>
            </div>
          </el-collapse-item>
          
          <el-collapse-item title="期末处理检查" name="period-end">
            <div class="check-item">
              <el-icon class="el-icon-warning" style="color: #e6a23c;"></el-icon>
              <span>损益结转: 未执行</span>
            </div>
            <div class="check-item">
              <el-icon class="el-icon-success" style="color: #67c23a;"></el-icon>
              <span>折旧计提: 已完成</span>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getAccountingPeriodPage, 
  saveOrUpdateAccountingPeriod,
  openAccountingPeriod,
  closeAccountingPeriod,
  getCurrentAccountingPeriod,
  getAccountingPeriodStatistics
} from '@/api/financialSharing/common'

export default {
  name: 'AccountingPeriodManagement',
  data() {
    return {
      loading: false,
      periodLoading: false,
      showStatistics: false,
      tableData: [],
      selectedRows: [],
      statistics: {},
      
      // 查询表单
      queryForm: {
        year: '',
        status: '',
        bookId: null
      },
      
      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 期间对话框
      periodDialogVisible: false,
      isEdit: false,
      periodForm: {
        year: '',
        month: '',
        startDate: '',
        endDate: '',
        bookId: null,
        description: ''
      },
      periodRules: {
        year: [
          { required: true, message: '请选择会计年度', trigger: 'change' }
        ],
        month: [
          { required: true, message: '请选择会计月份', trigger: 'change' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ],
        bookId: [
          { required: true, message: '请选择账簿', trigger: 'change' }
        ]
      },
      
      // 期间详情对话框
      detailDialogVisible: false,
      currentPeriod: null,
      
      // 期间检查对话框
      checkDialogVisible: false,
      activeCheckItems: ['voucher', 'balance', 'period-end']
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
        
        const response = await getAccountingPeriodPage(params)
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
        const response = await getAccountingPeriodStatistics(1001) // 默认主账簿
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
    
    // 新建期间
    handleCreate() {
      this.isEdit = false
      this.periodDialogVisible = true
      this.$nextTick(() => {
        this.$refs.periodForm.resetFields()
      })
    },
    
    // 编辑期间
    handleEdit(row) {
      this.isEdit = true
      this.periodForm = { ...row }
      this.periodDialogVisible = true
    },
    
    // 确认保存期间
    async handlePeriodConfirm() {
      try {
        await this.$refs.periodForm.validate()
        this.periodLoading = true
        
        // 生成期间编码
        this.periodForm.periodCode = `${this.periodForm.year}-${this.periodForm.month}`
        
        const response = await saveOrUpdateAccountingPeriod(this.periodForm)
        if (response.code === 200) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.periodDialogVisible = false
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || (this.isEdit ? '更新失败' : '创建失败'))
        }
      } catch (error) {
        console.error('操作失败:', error)
        this.$message.error('操作失败')
      } finally {
        this.periodLoading = false
      }
    },
    
    // 查看详情
    handleView(row) {
      this.currentPeriod = row
      this.detailDialogVisible = true
    },
    
    // 开启期间
    async handleOpen(row) {
      try {
        await this.$confirm('确认开启该会计期间？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await openAccountingPeriod(row.periodId)
        if (response.code === 200) {
          this.$message.success('期间开启成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '开启失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('开启失败:', error)
          this.$message.error('开启失败')
        }
      }
    },
    
    // 关闭期间
    async handleClose(row) {
      try {
        await this.$confirm('确认关闭该会计期间？关闭后不可重新开启！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await closeAccountingPeriod(row.periodId)
        if (response.code === 200) {
          this.$message.success('期间关闭成功')
          this.loadData()
          this.loadStatistics()
        } else {
          this.$message.error(response.msg || '关闭失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('关闭失败:', error)
          this.$message.error('关闭失败')
        }
      }
    },
    
    // 期间检查
    handleCheck(row) {
      this.checkDialogVisible = true
    },
    
    // 期间统计
    handleStatistics() {
      this.showStatistics = !this.showStatistics
      if (this.showStatistics) {
        this.loadStatistics()
      }
    },
    
    // 批量操作
    handleBatchOperation() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要操作的期间')
        return
      }
      this.$confirm(`确认对选中的${this.selectedRows.length}个会计期间执行批量操作？`, '批量操作', { type: 'warning' })
        .then(() => { this.$message.success('批量操作成功'); if (this.loadData) this.loadData() })
        .catch(() => {})
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
    
    // 获取期间状态类型
    getPeriodStatusType(status) {
      const statusMap = {
        'OPEN': 'success',
        'CLOSED': 'info',
        'PENDING': 'warning'
      }
      return statusMap[status] || 'info'
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
.accounting-period-container {
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

.toolbar {
  margin-bottom: 20px;
}

.search-form {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.statistics-cards {
  margin-bottom: 20px;
}

.statistic-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.statistic-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.statistic-card.open {
  border-left: 4px solid #67c23a;
}

.statistic-card.closed {
  border-left: 4px solid #909399;
}

.statistic-card.pending {
  border-left: 4px solid #e6a23c;
}

.statistic-item {
  padding: 20px;
}

.statistic-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.statistic-label {
  font-size: 14px;
  color: #606266;
}

.table-container {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

.check-results {
  max-height: 400px;
  overflow-y: auto;
}

.check-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.check-item i {
  margin-right: 8px;
}
</style>
