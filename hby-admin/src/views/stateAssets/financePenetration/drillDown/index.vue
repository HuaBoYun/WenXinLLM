<template>
  <div class="app-container finance-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-data-line"></i><span>财务穿透下钻</span></div>
      <div class="page-header-desc">按企业层级穿透查看财务指标</div>
    </div>
    <el-card shadow="never">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card shadow="hover" class="tree-card">
            <div slot="header"><span>企业层级</span></div>
            <el-input v-model="filterText" placeholder="搜索企业" size="small" prefix-icon="el-icon-search" clearable style="margin-bottom:8px" />
            <el-tree ref="tree" :data="treeData" :props="{ children: 'children', label: 'name' }" node-key="companyId" highlight-current :filter-node-method="filterNode" @node-click="handleNodeClick" />
          </el-card>
        </el-col>
        <el-col :span="16">
          <el-card shadow="hover">
            <div slot="header"><span>{{ currentNode.name || '请选择企业' }} — 财务概况</span></div>
            <el-row :gutter="16" style="margin-bottom: 20px">
              <el-col :span="6"><div class="stat-box"><div class="stat-label">总资产(万元)</div><div class="stat-value">{{ formatNum(nodeStats.totalAssets) }}</div></div></el-col>
              <el-col :span="6"><div class="stat-box"><div class="stat-label">营业收入(万元)</div><div class="stat-value">{{ formatNum(nodeStats.revenue) }}</div></div></el-col>
              <el-col :span="6"><div class="stat-box"><div class="stat-label">净利润(万元)</div><div class="stat-value">{{ formatNum(nodeStats.netProfit) }}</div></div></el-col>
              <el-col :span="6"><div class="stat-box"><div class="stat-label">资产负债率</div><div class="stat-value" :style="{ color: nodeStats.debtRatio > 70 ? '#F56C6C' : '#722ed1' }">{{ nodeStats.debtRatio || '0' }}%</div></div></el-col>
            </el-row>
            <el-table :data="nodeList" border style="width: 100%" size="small">
              <el-table-column label="报告期" prop="period" width="100" align="center" />
              <el-table-column label="总资产(万元)" prop="totalAssets" width="130" align="right">
                <template slot-scope="scope">{{ formatNum(scope.row.totalAssets) }}</template>
              </el-table-column>
              <el-table-column label="营业收入(万元)" prop="revenue" width="130" align="right">
                <template slot-scope="scope">{{ formatNum(scope.row.revenue) }}</template>
              </el-table-column>
              <el-table-column label="净利润(万元)" prop="netProfit" width="120" align="right">
                <template slot-scope="scope">{{ formatNum(scope.row.netProfit) }}</template>
              </el-table-column>
              <el-table-column label="总负债(万元)" prop="totalLiabilities" width="130" align="right">
                <template slot-scope="scope">{{ formatNum(scope.row.totalLiabilities) }}</template>
              </el-table-column>
              <el-table-column label="审计状态" prop="auditStatus" width="90" align="center">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.auditStatus === '已审计' ? 'success' : 'warning'" size="small">{{ scope.row.auditStatus }}</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
import request from '@/utils/request'
import { mapGetters } from 'vuex'
import { getFinanceStatementList } from '@/api/stateAssets/financePenetration'
export default {
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
  name: 'FinanceDrillDown',
  data() {
    return { filterText: '', treeData: [], currentNode: {}, nodeStats: {}, nodeList: [] }
  },
  watch: {
    filterText(val) { this.$refs.tree && this.$refs.tree.filter(val) }
  },
  created() { this.loadTree() },
  methods: {
    async loadTree() {
      try {
        const res = await request({ url: '/monitor/v1/supervision/financial/drill-down', method: 'get' })
        if (res && res.result === 200 && res.data) {
          this.treeData = res.data.children || []
        }
      } catch (e) { this.treeData = [] }
    },
    filterNode(value, data) {
      if (!value) return true
      return (data.name || '').indexOf(value) !== -1
    },
    async handleNodeClick(data) {
      this.currentNode = data
      // 从树节点数据中获取统计信息（后端 buildEnterpriseCard 已包含 totalAssets/totalLiabilities）
      this.nodeStats = {
        totalAssets: data.totalAssets || 0,
        revenue: data.revenue || 0,
        netProfit: data.netProfit || 0,
        debtRatio: data.debtRatio || 0,
      }
      // 使用 companyId 精确查询该企业的财务报表列表
      try {
        const params = { pageNumber: 1, pageSize: 50 }
        if (data.companyId) {
          params.enterpriseId = data.companyId
        } else {
          params.companyName = data.name
        }
        const res = await getFinanceStatementList(params)
        if (res && res.result === 200) this.nodeList = (res.data && res.data.tlist) || []
        else this.nodeList = []
      } catch (e) { this.nodeList = [] }
    },
    formatNum(val) { return val != null && val !== 0 ? Number(val).toLocaleString() : '-' },
  },
}
</script>
<style lang="scss" scoped>
.finance-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.tree-card { min-height: 500px; }
.stat-box { text-align: center; padding: 12px; background: #f9f0ff; border-radius: 6px; border: 1px solid #d3adf7; }
.stat-label { color: #909399; font-size: 13px; }
.stat-value { font-size: 22px; font-weight: bold; color: #722ed1; margin-top: 6px; }
::v-deep .el-table th { background: #f9f0ff; }
::v-deep .el-card { border-radius: 6px; }
</style>

