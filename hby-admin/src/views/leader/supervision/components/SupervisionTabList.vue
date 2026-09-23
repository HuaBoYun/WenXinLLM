<template>
  <div class="supervision-tab-list">
    <div class="tab-toolbar">
      <el-input
        v-model="searchText"
        placeholder="搜索负责人或企业"
        prefix-icon="el-icon-search"
        style="width: 240px;"
        clearable
        @input="handleSearch"
      />
      <span class="tab-summary">共 <b>{{ total }}</b> 条记录</span>
    </div>
    <el-table v-loading="loading" :data="tableData" stripe border size="small" style="width: 100%;">
      <el-table-column prop="leaderName" label="负责人" width="100" />
      <el-table-column prop="company" label="所属企业" min-width="140" show-overflow-tooltip />
      <el-table-column prop="supervisionDate" label="监管时间" width="110" />
      <el-table-column prop="riskLevel" label="风险等级" width="90">
        <template slot-scope="scope">
          <el-tag size="mini" :type="getRiskType(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="complianceStatus" label="整改状态" width="90">
        <template slot-scope="scope">
          <el-tag size="mini" :type="getStatusType(scope.row.complianceStatus)">{{ scope.row.complianceStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="supervisor" label="监管人" width="100" />
      <el-table-column prop="findingDesc" label="发现问题" min-width="150" show-overflow-tooltip />
      <el-table-column label="操作" width="130" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="$emit('view', scope.row)">查看</el-button>
          <el-button size="mini" type="warning" @click="$emit('report', scope.row)">报告</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="tab-pagination">
      <el-pagination
        small
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryForm.pageNumber"
        :page-sizes="[5, 10, 15]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next"
        :total="total"
      />
    </div>
  </div>
</template>

<script>
import { getLeaderSupervisionList } from '@/api/leader/index'

export default {
  name: 'SupervisionTabList',
  props: {
    supervisionType: { type: String, default: '' },
    statusFilter: { type: String, default: '' },
    tabLabel: { type: String, default: '监管' }
  },
  data() {
    return {
      loading: false,
      searchText: '',
      tableData: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 5 }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          searchText: this.searchText
        }
        if (this.supervisionType) params.supervisionType = this.supervisionType
        if (this.statusFilter) params.rectificationStatus = this.statusFilter
        const res = await getLeaderSupervisionList(params)
        if (res && res.data) {
          this.tableData = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        }
      } catch (e) {
        console.error('加载数据失败', e)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.queryForm.pageNumber = 1
      this.loadData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.queryForm.pageNumber = 1
      this.loadData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.loadData()
    },
    getRiskType(level) {
      const map = { '低风险': 'success', '中风险': 'warning', '高风险': 'danger', '极高风险': 'danger' }
      return map[level] || 'info'
    },
    getStatusType(status) {
      const map = { '合规': 'success', '基本合规': 'warning', '不合规': 'danger', '待整改': 'info', '已整改': 'success' }
      return map[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.supervision-tab-list {
  padding: 12px;
  .tab-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    .tab-summary { color: #909399; font-size: 13px; }
  }
  .tab-pagination {
    margin-top: 12px;
    text-align: right;
  }
}
</style>
