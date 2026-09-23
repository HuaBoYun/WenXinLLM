<template>
  <div class="quality-management" :style="themeVars">
    <!-- 搜索区域 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="检验单号" prop="inspectionNo">
          <el-input v-model="queryForm.inspectionNo" placeholder="请输入检验单号" clearable />
        </el-form-item>
        <el-form-item label="供应商" prop="supplierName">
          <el-select v-model="queryForm.supplierName" placeholder="请选择供应商" clearable filterable>
            <el-option v-for="item in supplierOptions" :key="item.id" :label="item.name" :value="item.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="检验类型" prop="inspectionType">
          <el-select v-model="queryForm.inspectionType" placeholder="请选择检验类型" clearable>
            <el-option label="来料检验" value="来料检验" />
            <el-option label="过程检验" value="过程检验" />
            <el-option label="成品检验" value="成品检验" />
            <el-option label="出厂检验" value="出厂检验" />
          </el-select>
        </el-form-item>
        <el-form-item label="检验结果" prop="result">
          <el-select v-model="queryForm.result" placeholder="请选择检验结果" clearable>
            <el-option label="合格" value="合格" />
            <el-option label="不合格" value="不合格" />
            <el-option label="待检验" value="待检验" />
            <el-option label="复检" value="复检" />
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
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建检验</el-button>
          <el-button type="success" icon="el-icon-check" @click="handleBatchInspect" :disabled="multipleSelection.length === 0">批量检验</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出质检数据</el-button>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button type="info" icon="el-icon-pie-chart" @click="handleQualityAnalysis">质量分析</el-button>
          <el-button type="primary" icon="el-icon-setting" @click="handleQualityStandard">质量标准</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        :data="qualityList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="inspectionNo" label="检验单号" width="150" />
        <el-table-column prop="materialName" label="物料名称" width="150" />
        <el-table-column prop="supplierName" label="供应商" width="150" />
        <el-table-column prop="inspectionType" label="检验类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTypeColor(scope.row.inspectionType)">{{ scope.row.inspectionType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="batchNo" label="批次号" width="120" />
        <el-table-column prop="quantity" label="检验数量" width="100" />
        <el-table-column prop="qualifiedQuantity" label="合格数量" width="100">
          <template slot-scope="scope">
            <span class="qualified-quantity">{{ scope.row.qualifiedQuantity }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="qualificationRate" label="合格率" width="100">
          <template slot-scope="scope">
            <span :class="getQualificationRateClass(scope.row.qualificationRate)">{{ scope.row.qualificationRate }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="inspector" label="检验员" width="100" />
        <el-table-column prop="inspectionDate" label="检验日期" width="120" />
        <el-table-column prop="result" label="检验结果" width="100">
          <template slot-scope="scope">
            <el-tag :type="getResultType(scope.row.result)">{{ scope.row.result }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleInspect(scope.row)" v-if="scope.row.result === '待检验'">检验</el-button>
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)" v-if="scope.row.result !== '合格'">编辑</el-button>
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
    <QualityDetailDialog v-if="detailDialogVisible" :visible="detailDialogVisible" :data="selectedQuality" @close="detailDialogVisible = false" />
    <QualityEditDialog v-if="editDialogVisible" :visible="editDialogVisible" :data="selectedQuality" :is-edit="!!selectedQuality" @close="editDialogVisible = false" @success="handleEditSuccess" />
    <QualityAnalysisDialog v-if="analysisDialogVisible" :visible="analysisDialogVisible" @close="analysisDialogVisible = false" />
    <QualityStandardDialog v-if="standardDialogVisible" :visible="standardDialogVisible" @close="standardDialogVisible = false" />
  </div>
</template>

<script>
import request from '@/utils/request'
import QualityDetailDialog from './QualityDetailDialog.vue'
import QualityEditDialog from './QualityEditDialog.vue'
import QualityAnalysisDialog from './QualityAnalysisDialog.vue'
import QualityStandardDialog from './QualityStandardDialog.vue'
import { investThemeMixin } from '@/views/stateAssets/themeMixin'

export default {
  name: 'QualityManagement',
  mixins: [investThemeMixin],
  components: { QualityDetailDialog, QualityEditDialog, QualityAnalysisDialog, QualityStandardDialog },
  data() {
    return {
      loading: false,
      queryForm: { inspectionNo: '', supplierName: '', inspectionType: '', result: '' },
      qualityList: [],
      multipleSelection: [],
      supplierOptions: [],
      pagination: { currentPage: 1, pageSize: 20, total: 0 },
      detailDialogVisible: false,
      editDialogVisible: false,
      inspectionDialogVisible: false,
      analysisDialogVisible: false,
      standardDialogVisible: false,
      selectedQuality: null
    }
  },
  mounted() {
    this.loadQualityList()
    this.loadSupplierOptions()
  },
  methods: {
    async loadSupplierOptions() { try { const res = await request({ url: '/monitor/v1/enterprise/supply/options', method: 'get' }); if (res && res.data) this.supplierOptions = res.data } catch (e) { console.error(e) } },
    // 加载质检列表
    async loadQualityList() {
      this.loading = true
      try {
        const res = await request({
          url: '/monitor/v1/enterprise/supply/quality/list',
          method: 'post',
          headers: { 'Content-Type': 'application/json;charset=UTF-8' },
          data: { ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize }
        })
        if (res && res.data) {
          this.qualityList = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else {
          this.qualityList = []
          this.pagination.total = 0
        }
      } catch (error) {
        console.error('加载质检数据失败', error)
        this.qualityList = []
        this.pagination.total = 0
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadQualityList()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.loadQualityList()
    },

    // 新建检验
    handleAdd() {
      this.selectedQuality = null
      this.editDialogVisible = true
    },

    // 批量检验
    handleBatchInspect() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要检验的项目')
        return
      }
      this.selectedQuality = this.multipleSelection[0]
      this.editDialogVisible = true
    },

    // 导出质检数据
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
        link.download = '质检数据.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
      } catch (error) {
        console.error('导出质检数据失败', error)
        this.$message.error('导出失败')
      }
    },

    // 质量分析
    handleQualityAnalysis() { this.analysisDialogVisible = true },
    // 质量标准
    handleQualityStandard() { this.standardDialogVisible = true },

    // 查看详情
    handleView(row) {
      this.selectedQuality = row
      this.detailDialogVisible = true
    },

    // 检验
    handleInspect(row) {
      this.selectedQuality = row
      this.inspectionDialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.selectedQuality = row
      this.editDialogVisible = true
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 分页大小变化
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadQualityList()
    },

    // 当前页变化
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadQualityList()
    },

    // 获取检验类型颜色
    getTypeColor(type) {
      const typeMap = {
        '来料检验': 'primary',
        '过程检验': 'success',
        '成品检验': 'warning',
        '出厂检验': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 获取合格率样式
    getQualificationRateClass(rate) {
      if (rate >= 95) return 'rate-excellent'
      if (rate >= 85) return 'rate-good'
      if (rate >= 70) return 'rate-normal'
      return 'rate-poor'
    },

    // 获取检验结果类型
    getResultType(result) {
      const resultMap = { '合格': 'success', '不合格': 'danger', '待检验': 'warning', '复检': 'info', '待复检': 'warning' }
      return resultMap[result] || 'info'
    },
    handleEditSuccess() { this.editDialogVisible = false; this.loadQualityList() },
    handleDelete(row) {
      this.$confirm('确认删除该质检记录吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        try {
          const res = await request({ url: `/monitor/v1/enterprise/supply/quality/${row.id}`, method: 'delete' })
          if (res && res.result === 200) { this.$message.success('删除成功'); this.loadQualityList() }
          else this.$message.error(res.msg || '删除失败')
        } catch (e) { this.$message.error('删除失败') }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.quality-management {
  .search-card, .action-card, .table-card {
    margin-bottom: 16px;
  }

  .text-right {
    text-align: right;
  }

  .qualified-quantity {
    color: #67C23A;
    font-weight: 500;
  }

  .rate-excellent {
    color: #67C23A;
    font-weight: bold;
  }

  .rate-good {
    color: var(--ip-bright);
    font-weight: 500;
  }

  .rate-normal {
    color: #E6A23C;
    font-weight: 500;
  }

  .rate-poor {
    color: #F56C6C;
    font-weight: bold;
  }

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
