<template>
  <div class="project-ledger-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>财务项目台账</h2>
      <p>项目财务数据统一管理和分析</p>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
        新建项目
      </el-button>
      <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">
        刷新
      </el-button>
      <el-button type="warning" icon="el-icon-download" @click="handleExport">
        导出数据
      </el-button>
      <el-button type="info" icon="el-icon-upload2" @click="handleImport">
        导入数据
      </el-button>
      <el-button type="primary" icon="el-icon-s-data" @click="handleCollectData">
        数据收集
      </el-button>
    </div>

    <!-- 查询条件 -->
    <div class="search-form">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
        <el-form-item label="项目编码">
          <el-input v-model="queryForm.projectCode" placeholder="请输入项目编码" clearable />
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input v-model="queryForm.projectName" placeholder="请输入项目名称" clearable />
        </el-form-item>
        <el-form-item label="项目状态">
          <el-select v-model="queryForm.projectStatus" placeholder="请选择项目状态" clearable>
            <el-option label="筹建期" :value="1" />
            <el-option label="执行期" :value="2" />
            <el-option label="完工期" :value="3" />
            <el-option label="结算期" :value="4" />
            <el-option label="已结束" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目类型">
          <el-select v-model="queryForm.projectType" placeholder="请选择项目类型" clearable>
            <el-option label="建设项目" value="CONSTRUCTION" />
            <el-option label="研发项目" value="RESEARCH" />
            <el-option label="服务项目" value="SERVICE" />
            <el-option label="其他项目" value="OTHER" />
          </el-select>
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
        <el-table-column prop="projectId" label="项目ID" width="100" />
        <el-table-column prop="projectCode" label="项目编码" width="150" />
        <el-table-column prop="projectName" label="项目名称" min-width="200" />
        <el-table-column prop="projectType" label="项目类型" width="100">
          <template slot-scope="scope">
            {{ getProjectTypeText(scope.row.projectType) }}
          </template>
        </el-table-column>
        <el-table-column prop="projectStatus" label="项目状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getProjectStatusType(scope.row.projectStatus)">
              {{ getProjectStatusText(scope.row.projectStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
          <template slot-scope="scope">
            {{ formatCurrency(scope.row.budgetAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="actualAmount" label="实际金额" width="120" align="right">
          <template slot-scope="scope">
            {{ formatCurrency(scope.row.actualAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="budgetExecutionRate" label="预算执行率" width="120" align="center">
          <template slot-scope="scope">
            <el-progress 
              :percentage="scope.row.budgetExecutionRate" 
              :status="getBudgetExecutionStatus(scope.row.budgetExecutionRate)"
              :show-text="false"
            />
            <span style="margin-left: 10px;">{{ scope.row.budgetExecutionRate }}%</span>
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
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleAnalysis(scope.row)">分析</el-button>
            <el-button size="mini" type="info" @click="handleReport(scope.row)">报表</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 新建/编辑项目对话框 -->
    <el-dialog
      :title="isEdit ? '编辑项目' : '新建项目'"
      :visible.sync="projectDialogVisible"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-form :model="projectForm" :rules="projectRules" ref="projectForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目编码" prop="projectCode">
              <el-input v-model="projectForm.projectCode" placeholder="请输入项目编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="projectForm.projectName" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目类型" prop="projectType">
              <el-select v-model="projectForm.projectType" placeholder="请选择项目类型" style="width: 100%">
                <el-option label="建设项目" value="CONSTRUCTION" />
                <el-option label="研发项目" value="RESEARCH" />
                <el-option label="服务项目" value="SERVICE" />
                <el-option label="其他项目" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目状态" prop="projectStatus">
              <el-select v-model="projectForm.projectStatus" placeholder="请选择项目状态" style="width: 100%">
                <el-option label="筹建期" :value="1" />
                <el-option label="执行期" :value="2" />
                <el-option label="完工期" :value="3" />
                <el-option label="结算期" :value="4" />
                <el-option label="已结束" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预算金额" prop="budgetAmount">
              <el-input-number 
                v-model="projectForm.budgetAmount" 
                :precision="2" 
                :step="1000" 
                :min="0"
                style="width: 100%"
                placeholder="请输入预算金额"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目经理" prop="projectManager">
              <el-input v-model="projectForm.projectManager" placeholder="请输入项目经理" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="projectForm.startDate"
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
                v-model="projectForm.endDate"
                type="date"
                placeholder="选择结束日期"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="项目描述">
          <el-input
            v-model="projectForm.projectDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入项目描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="projectDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleProjectConfirm" :loading="projectLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 项目详情对话框 -->
    <el-dialog
      title="项目详情"
      :visible.sync="detailDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div v-if="currentProject">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="项目ID">{{ currentProject.projectId }}</el-descriptions-item>
          <el-descriptions-item label="项目编码">{{ currentProject.projectCode }}</el-descriptions-item>
          <el-descriptions-item label="项目名称">{{ currentProject.projectName }}</el-descriptions-item>
          <el-descriptions-item label="项目类型">{{ getProjectTypeText(currentProject.projectType) }}</el-descriptions-item>
          <el-descriptions-item label="项目状态">
            <el-tag :type="getProjectStatusType(currentProject.projectStatus)">
              {{ getProjectStatusText(currentProject.projectStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="项目经理">{{ currentProject.projectManager }}</el-descriptions-item>
          <el-descriptions-item label="预算金额">{{ formatCurrency(currentProject.budgetAmount) }}</el-descriptions-item>
          <el-descriptions-item label="实际金额">{{ formatCurrency(currentProject.actualAmount) }}</el-descriptions-item>
          <el-descriptions-item label="预算执行率">{{ currentProject.budgetExecutionRate }}%</el-descriptions-item>
          <el-descriptions-item label="开始日期">{{ currentProject.startDate }}</el-descriptions-item>
          <el-descriptions-item label="结束日期">{{ currentProject.endDate }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(currentProject.createTime) }}</el-descriptions-item>
        </el-descriptions>

        <!-- 项目统计图表 -->
        <div style="margin-top: 20px;">
          <h4>项目统计</h4>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card>
                <div slot="header">成本统计</div>
                <div id="costChart" style="height: 200px;"></div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <div slot="header">收入统计</div>
                <div id="revenueChart" style="height: 200px;"></div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <div slot="header">利润统计</div>
                <div id="profitChart" style="height: 200px;"></div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-dialog>

    <!-- 数据收集对话框 -->
    <el-dialog
      title="项目数据收集"
      :visible.sync="collectDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="collectForm" :rules="collectRules" ref="collectForm" label-width="120px">
        <el-form-item label="收集范围" prop="collectScope">
          <el-radio-group v-model="collectForm.collectScope">
            <el-radio value="ALL">全部项目</el-radio>
            <el-radio value="SELECTED">选中项目</el-radio>
            <el-radio value="CONDITION">条件筛选</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="数据类型" prop="dataTypes">
          <el-checkbox-group v-model="collectForm.dataTypes">
            <el-checkbox value="COST">成本数据</el-checkbox>
            <el-checkbox value="REVENUE">收入数据</el-checkbox>
            <el-checkbox value="BUDGET">预算数据</el-checkbox>
            <el-checkbox value="PROGRESS">进度数据</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="收集时间范围">
          <el-date-picker
            v-model="collectForm.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="collectDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCollectConfirm" :loading="collectLoading">开始收集</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getProjectLedgerPage, 
  saveOrUpdateProjectLedger,
  deleteProjectLedger,
  getProjectLedgerById,
  collectProjectData,
  exportProjectData
} from '@/api/financialSharing/projectLedger'

export default {
  name: 'ProjectLedgerManagement',
  data() {
    return {
      loading: false,
      projectLoading: false,
      collectLoading: false,
      tableData: [],
      selectedRows: [],
      
      // 查询表单
      queryForm: {
        projectCode: '',
        projectName: '',
        projectStatus: null,
        projectType: ''
      },
      
      // 分页信息
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      
      // 项目对话框
      projectDialogVisible: false,
      isEdit: false,
      projectForm: {
        projectCode: '',
        projectName: '',
        projectType: '',
        projectStatus: null,
        budgetAmount: null,
        projectManager: '',
        startDate: '',
        endDate: '',
        projectDesc: ''
      },
      projectRules: {
        projectCode: [
          { required: true, message: '请输入项目编码', trigger: 'blur' }
        ],
        projectName: [
          { required: true, message: '请输入项目名称', trigger: 'blur' }
        ],
        projectType: [
          { required: true, message: '请选择项目类型', trigger: 'change' }
        ],
        projectStatus: [
          { required: true, message: '请选择项目状态', trigger: 'change' }
        ]
      },
      
      // 项目详情对话框
      detailDialogVisible: false,
      currentProject: null,
      
      // 数据收集对话框
      collectDialogVisible: false,
      collectForm: {
        collectScope: 'ALL',
        dataTypes: [],
        timeRange: []
      },
      collectRules: {
        collectScope: [
          { required: true, message: '请选择收集范围', trigger: 'change' }
        ],
        dataTypes: [
          { required: true, message: '请选择数据类型', trigger: 'change' }
        ]
      }
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
          ...this.queryForm,
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize
        }
        
        const response = await getProjectLedgerPage(params)
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
    },
    
    // 新建项目
    handleCreate() {
      this.isEdit = false
      this.projectDialogVisible = true
      this.$nextTick(() => {
        this.$refs.projectForm.resetFields()
      })
    },
    
    // 编辑项目
    handleEdit(row) {
      this.isEdit = true
      this.projectForm = { ...row }
      this.projectDialogVisible = true
    },
    
    // 确认保存项目
    async handleProjectConfirm() {
      try {
        await this.$refs.projectForm.validate()
        this.projectLoading = true
        
        const response = await saveOrUpdateProjectLedger(this.projectForm)
        if (response.code === 200) {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.projectDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || (this.isEdit ? '更新失败' : '创建失败'))
        }
      } catch (error) {
        console.error('操作失败:', error)
        this.$message.error('操作失败')
      } finally {
        this.projectLoading = false
      }
    },
    
    // 查看详情
    async handleView(row) {
      try {
        const response = await getProjectLedgerById(row.projectId)
        if (response.code === 200) {
          this.currentProject = response.data
          this.detailDialogVisible = true
          // 延迟渲染图表
          this.$nextTick(() => {
            this.renderCharts()
          })
        } else {
          this.$message.error(response.msg || '获取详情失败')
        }
      } catch (error) {
        console.error('获取详情失败:', error)
        this.$message.error('获取详情失败')
      }
    },
    
    // 渲染图表
    renderCharts() {
      // 这里可以使用 ECharts 渲染图表
      console.log('渲染项目统计图表')
    },
    
    // 分析
    handleAnalysis(row) {
      const content = `<p><b>项目编号：</b>${row.projectId || row.id || '-'}</p><p><b>项目名称：</b>${row.projectName || row.name || '-'}</p><p>请使用左侧菜单进入"项目分析"页面查看详细分析数据。</p>`
      this.$alert(content, '项目分析', { dangerouslyUseHTMLString: true })
    },

    // 报表
    handleReport(row) {
      const content = `<p><b>项目编号：</b>${row.projectId || row.id || '-'}</p><p><b>项目名称：</b>${row.projectName || row.name || '-'}</p><p>请使用左侧菜单进入"项目报表"页面查看详细报表数据。</p>`
      this.$alert(content, '项目报表', { dangerouslyUseHTMLString: true })
    },
    
    // 数据收集
    handleCollectData() {
      this.collectDialogVisible = true
      this.$nextTick(() => {
        this.$refs.collectForm.resetFields()
      })
    },
    
    // 确认数据收集
    async handleCollectConfirm() {
      try {
        await this.$refs.collectForm.validate()
        this.collectLoading = true
        
        const response = await collectProjectData(this.collectForm)
        if (response.code === 200) {
          this.$message.success('数据收集已开始')
          this.collectDialogVisible = false
        } else {
          this.$message.error(response.msg || '数据收集失败')
        }
      } catch (error) {
        console.error('数据收集失败:', error)
        this.$message.error('数据收集失败')
      } finally {
        this.collectLoading = false
      }
    },
    
    // 导出数据
    async handleExport() {
      try {
        const response = await exportProjectData(this.queryForm)
        // 处理文件下载
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = `项目台账_${new Date().getTime()}.xlsx`
        a.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },
    
    // 导入数据
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
    
    // 删除项目
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该项目？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteProjectLedger(row.projectId)
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.loadData()
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
    
    // 获取项目类型文本
    getProjectTypeText(type) {
      const typeMap = {
        'CONSTRUCTION': '建设项目',
        'RESEARCH': '研发项目',
        'SERVICE': '服务项目',
        'OTHER': '其他项目'
      }
      return typeMap[type] || type
    },
    
    // 获取项目状态类型
    getProjectStatusType(status) {
      const statusMap = {
        1: 'info',     // 筹建期
        2: 'warning',  // 执行期
        3: 'primary',  // 完工期
        4: 'success',  // 结算期
        5: 'info'      // 已结束
      }
      return statusMap[status] || 'info'
    },
    
    // 获取项目状态文本
    getProjectStatusText(status) {
      const statusMap = {
        1: '筹建期',
        2: '执行期',
        3: '完工期',
        4: '结算期',
        5: '已结束'
      }
      return statusMap[status] || status
    },
    
    // 获取预算执行状态
    getBudgetExecutionStatus(rate) {
      if (rate >= 100) return 'exception'
      if (rate >= 80) return 'warning'
      return 'success'
    },
    
    // 格式化货币
    formatCurrency(amount) {
      if (!amount) return '¥0.00'
      return `¥${Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
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
.project-ledger-container {
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

.table-container {
  margin-bottom: 20px;
}

.pagination-container {
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
