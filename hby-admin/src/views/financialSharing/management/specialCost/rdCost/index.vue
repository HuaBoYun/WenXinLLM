<template>
  <div class="rd-cost-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item>
              <el-input
                v-model="queryForm.rdProjectName"
                clearable
                placeholder="研发项目名称"
              />
            </el-form-item>
            <el-form-item>
              <el-select
                v-model="queryForm.rdType"
                clearable
                placeholder="研发类型"
                style="width: 180px"
              >
                <el-option label="基础研究" value="basic_research" />
                <el-option label="应用研究" value="applied_research" />
                <el-option label="试验发展" value="experimental_development" />
                <el-option label="技术改进" value="technology_improvement" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.department"
                clearable
                placeholder="所属部门"
              />
            </el-form-item>
            <el-form-item>
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
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
                查询
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
        
        <vab-query-form-right-panel>
          <el-button
            icon="el-icon-plus"
            type="primary"
            @click="handleAdd"
          >
            新增研发项目
          </el-button>
          <el-button
            icon="el-icon-data-analysis"
            @click="handleRoiAnalysis"
          >
            投资回报分析
          </el-button>
          <el-button
            icon="el-icon-download"
            @click="handleExport"
          >
            导出
          </el-button>
          <el-button
            icon="el-icon-refresh-right"
            @click="fetchData"
          >
            刷新
          </el-button>
        </vab-query-form-right-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never">
      <el-table
        ref="tableSort"
        v-loading="listLoading"
        :data="list"
        :element-loading-text="elementLoadingText"
        @selection-change="setSelectRows"
        @sort-change="tableSortChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          label="序号"
          type="index"
          width="80"
          :index="getTableIndex"
        />
        <el-table-column
          prop="rdProjectCode"
          label="研发项目编码"
          min-width="120"
          sortable="custom"
          show-overflow-tooltip
        />
        <el-table-column
          prop="rdProjectName"
          label="研发项目名称"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="rdType"
          label="研发类型"
          width="120"
        >
          <template slot-scope="scope">
            <el-tag :type="getRdTypeTag(scope.row.rdType)">
              {{ getRdTypeName(scope.row.rdType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="rdCost"
          label="研发成本"
          width="120"
          align="right"
        >
          <template slot-scope="scope">
            {{ formatAmount(scope.row.rdCost) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="expectedBenefit"
          label="预期收益"
          width="120"
          align="right"
        >
          <template slot-scope="scope">
            {{ formatAmount(scope.row.expectedBenefit) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="roiRatio"
          label="投资回报率"
          width="100"
          align="right"
        >
          <template slot-scope="scope">
            <span :class="getRoiRatioClass(scope.row.roiRatio)">
              {{ getRoiRatio(scope.row.rdCost, scope.row.expectedBenefit) }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="department"
          label="所属部门"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="rdStartDate"
          label="开始日期"
          width="100"
        />
        <el-table-column
          prop="rdEndDate"
          label="结束日期"
          width="100"
        />
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="150"
          sortable="custom"
        />
        <el-table-column
          label="操作"
          width="180"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button
              type="text"
              @click="handleView(scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="text"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="text"
              style="color: #f56c6c"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :page-size="queryForm.pageSize"
        :layout="layout"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <rd-cost-edit
      ref="edit"
      @fetch-data="fetchData"
    />

    <!-- 研发成本详情对话框 -->
    <rd-cost-detail
      ref="detail"
    />

    <!-- 投资回报分析对话框 -->
    <rd-roi-analysis
      ref="roiAnalysis"
    />
  </div>
</template>

<script>
import { getRdCostPage, deleteRdCost } from '@/api/financialSharing/specialCost'
import RdCostEdit from './components/RdCostEdit'
import RdCostDetail from './components/RdCostDetail'
import RdRoiAnalysis from './components/RdRoiAnalysis'

export default {
  name: 'RdCostIndex',
  components: {
    RdCostEdit,
    RdCostDetail,
    RdRoiAnalysis
  },
  data() {
    return {
      list: [],
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      selectRows: '',
      elementLoadingText: '正在加载...',
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
        rdProjectName: '',
        rdType: '',
        department: '',
        dateRange: []
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    setSelectRows(val) {
      this.selectRows = val
    },
    handleAdd() {
      this.$refs.edit.showEdit()
    },
    handleEdit(row) {
      this.$refs.edit.showEdit(row)
    },
    handleView(row) {
      this.$refs.detail.showDetail(row)
    },
    handleRoiAnalysis() {
      this.$refs.roiAnalysis.showAnalysis()
    },
    async handleDelete(row) {
      if (
        await this.$baseConfirm('你确定要删除当前研发项目吗', null, {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
      ) {
        const { code, msg } = await deleteRdCost(row.id)
        if (code === 200) {
          this.$baseMessage(msg, 'success')
          await this.fetchData()
        } else {
          this.$baseMessage(msg, 'error')
        }
      }
    },
    handleExport() {
      try {
        const data = this.list || []
        if (data.length === 0) {
          this.$baseMessage('暂无数据可导出', 'warning')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '研发成本数据.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$baseMessage('导出成功', 'success')
      } catch (error) {
        this.$baseMessage('导出失败', 'error')
      }
    },
    async fetchData() {
      this.listLoading = true
      const { code, data } = await getRdCostPage(this.queryForm)
      if (code === 200) {
        this.list = data.records
        this.total = data.total
      }
      this.listLoading = false
    },
    resetSearch() {
      this.$refs.form.resetFields()
      this.queryForm = {
        pageNumber: 1,
        pageSize: 20,
        rdProjectName: '',
        rdType: '',
        department: '',
        dateRange: []
      }
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    tableSortChange({ column, prop, order }) {
      this.queryForm.orderBy = prop
      this.queryForm.orderType = order === 'ascending' ? 'asc' : 'desc'
      this.fetchData()
    },
    getTableIndex(index) {
      return (this.queryForm.pageNumber - 1) * this.queryForm.pageSize + index + 1
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return (amount / 10000).toFixed(2) + '万'
    },
    getRdTypeName(type) {
      const typeMap = {
        'basic_research': '基础研究',
        'applied_research': '应用研究',
        'experimental_development': '试验发展',
        'technology_improvement': '技术改进'
      }
      return typeMap[type] || '未知'
    },
    getRdTypeTag(type) {
      const tagMap = {
        'basic_research': 'primary',
        'applied_research': 'success',
        'experimental_development': 'warning',
        'technology_improvement': 'info'
      }
      return tagMap[type] || 'info'
    },
    getRoiRatio(cost, benefit) {
      if (!cost || cost === 0) return '0.00'
      return (((benefit || 0) - cost) / cost * 100).toFixed(2)
    },
    getRoiRatioClass(ratio) {
      const roiNum = parseFloat(ratio)
      if (roiNum >= 20) return 'text-success'
      if (roiNum >= 10) return 'text-warning'
      return 'text-danger'
    }
  }
}
</script>

<style lang="scss" scoped>
.rd-cost-container {
  padding: 0;
}

.text-success {
  color: #67c23a;
}

.text-warning {
  color: #e6a23c;
}

.text-danger {
  color: #f56c6c;
}
</style>
