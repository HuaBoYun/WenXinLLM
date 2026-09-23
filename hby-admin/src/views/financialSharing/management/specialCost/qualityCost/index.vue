<template>
  <div class="quality-cost-container">
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
                v-model="queryForm.qualityName"
                clearable
                placeholder="质量成本名称"
              />
            </el-form-item>
            <el-form-item>
              <el-select
                v-model="queryForm.qualityType"
                clearable
                placeholder="质量成本类型"
                style="width: 180px"
              >
                <el-option label="预防成本" value="prevention" />
                <el-option label="鉴定成本" value="appraisal" />
                <el-option label="内部失败成本" value="internal_failure" />
                <el-option label="外部失败成本" value="external_failure" />
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
            新增质量成本
          </el-button>
          <el-button
            icon="el-icon-pie-chart"
            @click="handleCategoryStats"
          >
            分类统计
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
          prop="qualityCode"
          label="质量成本编码"
          min-width="120"
          sortable="custom"
          show-overflow-tooltip
        />
        <el-table-column
          prop="qualityName"
          label="质量成本名称"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="qualityType"
          label="质量成本类型"
          width="120"
        >
          <template slot-scope="scope">
            <el-tag :type="getQualityTypeTag(scope.row.qualityType)">
              {{ getQualityTypeName(scope.row.qualityType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="costAmount"
          label="成本金额"
          width="120"
          align="right"
        >
          <template slot-scope="scope">
            {{ formatAmount(scope.row.costAmount) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="department"
          label="所属部门"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="costCenter"
          label="成本中心"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="occurDate"
          label="发生日期"
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
    <quality-cost-edit
      ref="edit"
      @fetch-data="fetchData"
    />

    <!-- 质量成本详情对话框 -->
    <quality-cost-detail
      ref="detail"
    />

    <!-- 分类统计对话框 -->
    <quality-cost-stats
      ref="stats"
    />
  </div>
</template>

<script>
import { getQualityCostPage, deleteQualityCost } from '@/api/financialSharing/specialCost'
import QualityCostEdit from './components/QualityCostEdit'
import QualityCostDetail from './components/QualityCostDetail'
import QualityCostStats from './components/QualityCostStats'

export default {
  name: 'QualityCostIndex',
  components: {
    QualityCostEdit,
    QualityCostDetail,
    QualityCostStats
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
        qualityName: '',
        qualityType: '',
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
    handleCategoryStats() {
      this.$refs.stats.showStats()
    },
    async handleDelete(row) {
      if (
        await this.$baseConfirm('你确定要删除当前质量成本吗', null, {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
      ) {
        const { code, msg } = await deleteQualityCost(row.id)
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
        link.download = '质量成本数据.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$baseMessage('导出成功', 'success')
      } catch (error) {
        this.$baseMessage('导出失败', 'error')
      }
    },
    async fetchData() {
      this.listLoading = true
      const { code, data } = await getQualityCostPage(this.queryForm)
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
        qualityName: '',
        qualityType: '',
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
    getQualityTypeName(type) {
      const typeMap = {
        'prevention': '预防成本',
        'appraisal': '鉴定成本',
        'internal_failure': '内部失败成本',
        'external_failure': '外部失败成本'
      }
      return typeMap[type] || '未知'
    },
    getQualityTypeTag(type) {
      const tagMap = {
        'prevention': 'success',
        'appraisal': 'primary',
        'internal_failure': 'warning',
        'external_failure': 'danger'
      }
      return tagMap[type] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.quality-cost-container {
  padding: 0;
}
</style>
