<template>
  <div class="supply-risk-management" :style="themeVars">
    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="风险编号" prop="riskNo">
          <el-input v-model="queryForm.riskNo" placeholder="请输入风险编号" clearable />
        </el-form-item>
        <el-form-item label="供应商" prop="supplierName">
          <el-select v-model="queryForm.supplierName" placeholder="请选择供应商" clearable filterable>
            <el-option v-for="item in supplierOptions" :key="item.id" :label="item.name" :value="item.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险类型" prop="riskType">
          <el-select v-model="queryForm.riskType" placeholder="请选择风险类型" clearable>
            <el-option label="质量风险" value="质量风险" />
            <el-option label="交付风险" value="交付风险" />
            <el-option label="价格风险" value="价格风险" />
            <el-option label="合规风险" value="合规风险" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择风险等级" clearable>
            <el-option label="低风险" value="低风险" />
            <el-option label="中风险" value="中风险" />
            <el-option label="高风险" value="高风险" />
            <el-option label="极高风险" value="极高风险" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card shadow="never" class="action-card">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增风险</el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchHandle" :disabled="multipleSelection.length === 0">批量处理</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出风险数据</el-button>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button type="info" icon="el-icon-pie-chart" @click="handleRiskAnalysis">风险分析</el-button>
          <el-button type="primary" icon="el-icon-setting" @click="handleRiskMonitor">风险监控</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        :data="riskList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="riskNo" label="风险编号" width="120" />
        <el-table-column prop="riskName" label="风险名称" width="150" show-overflow-tooltip />
        <el-table-column prop="supplierName" label="供应商" width="150" />
        <el-table-column prop="riskType" label="风险类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTypeColor(scope.row.riskType)">{{ scope.row.riskType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getLevelType(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskScore" label="风险评分" width="100">
          <template slot-scope="scope">
            <span :class="getScoreClass(scope.row.riskScore)">{{ scope.row.riskScore }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="riskDescription" label="风险描述" width="200" show-overflow-tooltip />
        <el-table-column prop="identifyDate" label="识别日期" width="120" />
        <el-table-column prop="responsiblePerson" label="负责人" width="100" />
        <el-table-column prop="status" label="处理状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="controlMeasure" label="管控措施" width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleHandle(scope.row)" v-if="scope.row.status !== '已解决'">处理</el-button>
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
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
    </el-card>

    <!-- 对话框组件 -->
    <RiskDetailDialog v-if="detailDialogVisible" :visible="detailDialogVisible" :data="selectedRisk" @close="detailDialogVisible = false" />
    <RiskEditDialog v-if="editDialogVisible" :visible="editDialogVisible" :data="selectedRisk" :is-edit="!!selectedRisk" @close="editDialogVisible = false" @success="handleEditSuccess" />
    <RiskHandleDialog v-if="handleDialogVisible" :visible="handleDialogVisible" :data="selectedRisk" @close="handleDialogVisible = false" @success="handleHandleSuccess" />
    <RiskAnalysisDialog v-if="analysisDialogVisible" :visible="analysisDialogVisible" @close="analysisDialogVisible = false" />
    <RiskMonitorDialog v-if="monitorDialogVisible" :visible="monitorDialogVisible" @close="monitorDialogVisible = false" />
  </div>
</template>

<script>
import request from '@/utils/request'
import RiskDetailDialog from './RiskDetailDialog.vue'
import RiskEditDialog from './RiskEditDialog.vue'
import RiskHandleDialog from './RiskHandleDialog.vue'
import RiskAnalysisDialog from './RiskAnalysisDialog.vue'
import RiskMonitorDialog from './RiskMonitorDialog.vue'
import { investThemeMixin } from '@/views/stateAssets/themeMixin'

export default {
  name: 'SupplyRiskManagement',
  mixins: [investThemeMixin],
  components: { RiskDetailDialog, RiskEditDialog, RiskHandleDialog, RiskAnalysisDialog, RiskMonitorDialog },
  data() {
    return {
      loading: false,
      queryForm: { riskNo: '', supplierName: '', riskType: '', riskLevel: '' },
      riskList: [],
      multipleSelection: [],
      supplierOptions: [],
      pagination: { currentPage: 1, pageSize: 20, total: 0 },
      detailDialogVisible: false,
      editDialogVisible: false,
      handleDialogVisible: false,
      analysisDialogVisible: false,
      monitorDialogVisible: false,
      selectedRisk: null
    }
  },
  mounted() {
    this.loadRiskList()
    this.loadSupplierOptions()
  },
  methods: {
    async loadSupplierOptions() { try { const res = await request({ url: '/monitor/v1/enterprise/supply/options', method: 'get' }); if (res && res.data) this.supplierOptions = res.data } catch (e) { console.error(e) } },
    // 加载风险列表
    async loadRiskList() {
      this.loading = true
      try {
        const res = await request({
          url: '/monitor/v1/enterprise/supply/risk/list',
          method: 'post',
          headers: { 'Content-Type': 'application/json;charset=UTF-8' },
          data: { ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize }
        })
        if (res && res.data) {
          this.riskList = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.riskList = []
          this.pagination.total = 0
        }
      } catch (error) {
        console.error('加载风险数据失败', error)
        this.riskList = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadRiskList()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.loadRiskList()
    },

    // 新增风险
    handleAdd() {
      this.selectedRisk = null
      this.editDialogVisible = true
    },

    // 批量处理
    handleBatchHandle() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要处理的风险')
        return
      }
      this.selectedRisk = this.multipleSelection[0]
      this.handleDialogVisible = true
    },

    // 导出风险数据
    async handleExport() {
      try {
        const res = await request({
          url: '/monitor/v1/enterprise/supply/export',
          method: 'get',
          params: { ...this.queryForm },
          responseType: 'blob'
        })
        const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '风险数据.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
      } catch (error) {
        console.error('导出风险数据失败', error)
        this.$message.error('导出失败')
      }
    },

    // 风险分析
    handleRiskAnalysis() { this.analysisDialogVisible = true },
    // 风险监控
    handleRiskMonitor() { this.monitorDialogVisible = true },

    // 查看详情
    handleView(row) {
      this.selectedRisk = row
      this.detailDialogVisible = true
    },

    // 处理风险
    handleHandle(row) {
      this.selectedRisk = row
      this.handleDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.selectedRisk = row
      this.editDialogVisible = true
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadRiskList()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadRiskList()
    },

    // 获取风险类型颜色
    getTypeColor(type) {
      const typeMap = {
        '质量风险': 'danger',
        '交付风险': 'warning',
        '价格风险': 'primary',
        '合规风险': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 获取风险等级类型
    getLevelType(level) {
      const levelMap = {
        '低风险': 'success',
        '中风险': 'warning',
        '高风险': 'danger',
        '极高风险': 'danger'
      }
      return levelMap[level] || 'info'
    },

    // 获取风险评分样式
    getScoreClass(score) {
      if (score >= 80) return 'score-critical'
      if (score >= 60) return 'score-high'
      if (score >= 40) return 'score-medium'
      return 'score-low'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = { '待处理': 'warning', '处理中': 'primary', '已处理': 'success', '已关闭': 'info', '已识别': 'info', '已解决': 'success', '监控中': 'primary' }
      return statusMap[status] || 'info'
    },
    handleEditSuccess() { this.editDialogVisible = false; this.loadRiskList() },
    handleHandleSuccess() { this.handleDialogVisible = false; this.loadRiskList() },
    handleDelete(row) {
      this.$confirm('确认删除该风险记录吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        try {
          const res = await request({ url: `/monitor/v1/enterprise/supply/risk/${row.id}`, method: 'delete' })
          if (res && res.result === 200) { this.$message.success('删除成功'); this.loadRiskList() }
          else this.$message.error(res.msg || '删除失败')
        } catch (e) { this.$message.error('删除失败') }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.supply-risk-management {
  .search-card, .action-card, .table-card {
    margin-bottom: 16px;
  }

  .text-right {
    text-align: right;
  }

  .score-critical {
    color: #F56C6C;
    font-weight: bold;
    font-size: 16px;
  }

  .score-high {
    color: #E6A23C;
    font-weight: bold;
  }

  .score-medium {
    color: var(--ip-bright);
    font-weight: 500;
  }

  .score-low {
    color: #67C23A;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
