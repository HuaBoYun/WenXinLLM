<template>
  <div class="app-container contract-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + ' 0%, ' + themeColor + 'cc 100%)' }">
      <div class="page-header-left"><i class="el-icon-user"></i><span>对方信用分析</span></div>
      <div class="page-header-desc">评估合同对方信用等级与合作风险</div>
    </div>
    <el-card shadow="never" class="search-card" :style="{ borderLeft: '3px solid ' + themeColor }">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="对方名称" prop="counterpartyName">
          <el-input v-model="queryForm.counterpartyName" placeholder="请输入" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="信用等级" prop="creditLevel">
          <el-select v-model="queryForm.creditLevel" placeholder="请选择" clearable style="width: 120px">
            <el-option label="AAA" value="AAA" />
            <el-option label="AA" value="AA" />
            <el-option label="A" value="A" />
            <el-option label="BBB" value="BBB" />
            <el-option label="BB及以下" value="BB" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px" :style="{ '--table-header-bg': themeColorLight }">
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="对方名称" prop="counterpartyName" min-width="180" show-overflow-tooltip />
        <el-table-column label="统一社会信用代码" prop="creditCode" width="180" />
        <el-table-column label="信用等级" prop="creditLevel" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="levelTagType(scope.row.creditLevel)" size="small">{{ scope.row.creditLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="合作次数" prop="cooperationCount" width="80" align="center" />
        <el-table-column label="合同总额(万元)" prop="totalContractAmount" width="130" align="right" />
        <el-table-column label="逾期次数" prop="overdueCount" width="80" align="center">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.overdueCount > 0 ? '#F56C6C' : '#303133' }">{{ scope.row.overdueCount || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="纠纷次数" prop="disputeCount" width="80" align="center">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.disputeCount > 0 ? '#F56C6C' : '#303133' }">{{ scope.row.disputeCount || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top: 15px; text-align: right"
        :current-page="queryForm.pageNumber"
        :page-sizes="[10, 20, 50]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="val => { queryForm.pageSize = val; fetchData() }"
        @current-change="val => { queryForm.pageNumber = val; fetchData() }"
      />
    </el-card>

    <!-- 信用详情弹窗 -->
    <el-dialog title="对方信用详情" :visible.sync="dialogVisible" width="750px" :close-on-click-modal="false">
      <div v-loading="detailLoading">
        <el-descriptions :column="2" border size="small" v-if="detailData.counterpartyName">
          <el-descriptions-item label="对方名称">{{ detailData.counterpartyName }}</el-descriptions-item>
          <el-descriptions-item label="信用代码">{{ detailData.creditCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="信用等级">
            <el-tag :type="levelTagType(detailData.creditLevel)" size="small">{{ detailData.creditLevel || '-' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="合作次数">{{ detailData.contractCount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="合同总额(万元)">{{ detailData.totalContractAmount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="逾期次数">
            <span :style="{ color: detailData.overdueCount > 0 ? '#F56C6C' : '#303133' }">{{ detailData.overdueCount || 0 }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="纠纷次数">
            <span :style="{ color: detailData.disputeCount > 0 ? '#F56C6C' : '#303133' }">{{ detailData.disputeCount || 0 }}</span>
          </el-descriptions-item>
        </el-descriptions>
        <!-- 合作合同列表 -->
        <div v-if="detailData.contracts && detailData.contracts.length" style="margin-top: 16px">
          <h4 style="margin-bottom: 10px">合作合同记录</h4>
          <el-table :data="detailData.contracts" border size="small" max-height="300">
            <el-table-column label="合同编号" prop="contractNo" width="130" />
            <el-table-column label="合同名称" prop="contractName" min-width="150" show-overflow-tooltip />
            <el-table-column label="合同金额(万元)" prop="contractAmount" width="120" align="right" />
            <el-table-column label="合同类型" prop="contractType" width="90" align="center">
              <template slot-scope="scope">{{ typeLabel(scope.row.contractType) }}</template>
            </el-table-column>
            <el-table-column label="签订日期" prop="signDate" width="100" align="center" />
            <el-table-column label="状态" prop="contractStatus" width="80" align="center">
              <template slot-scope="scope">{{ statusLabel(scope.row.contractStatus) }}</template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="dialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import { getCreditList, getCreditDetail } from '@/api/stateAssets/contractPenetration'

export default {
  name: 'ContractCreditAnalysis',
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
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, counterpartyName: '', creditLevel: '' },
      dialogVisible: false,
      detailLoading: false,
      detailData: {},
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getCreditList(this.queryForm)
        if (res && res.result === 200) {
          const data = res.data || {}
          this.list = data.tlist || []
          this.total = data.totalRecord || 0
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    async handleView(row) {
      this.dialogVisible = true
      this.detailLoading = true
      this.detailData = {}
      try {
        const res = await getCreditDetail(row.counterpartyName)
        if (res && res.result === 200) {
          this.detailData = res.data || {}
        } else {
          this.$message.error(res.msg || '获取详情失败')
        }
      } catch (e) {
        this.$message.error('获取详情失败')
      } finally {
        this.detailLoading = false
      }
    },
    levelTagType(level) {
      const map = { AAA: 'success', AA: 'success', A: '', BBB: 'warning', BB: 'danger' }
      return map[level] || 'info'
    },
    typeLabel(type) {
      const map = { PURCHASE: '采购', SALES: '销售', ENGINEERING: '工程', SERVICE: '服务', OTHER: '其他' }
      return map[type] || type || '-'
    },
    statusLabel(status) {
      const map = { DRAFT: '草稿', EXECUTING: '执行中', COMPLETED: '已完成', TERMINATED: '已终止' }
      return map[status] || status || '-'
    },
  },
}
</script>
<style lang="scss" scoped>
.contract-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.search-card { }
::v-deep .el-table th { background: var(--table-header-bg, #e6f7ff); }
::v-deep .el-card { border-radius: 6px; }
</style>
