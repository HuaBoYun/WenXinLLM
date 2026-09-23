<template>
  <div class="app-container contract-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + ' 0%, ' + themeColor + 'cc 100%)' }">
      <div class="page-header-left"><i class="el-icon-view"></i><span>合同履行监控</span></div>
      <div class="page-header-desc">监控合同付款进度、逾期与违约情况</div>
    </div>
    <el-card shadow="never" class="search-card" :style="{ borderLeft: '3px solid ' + themeColor }">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="合同名称" prop="contractName">
          <el-input v-model="queryForm.contractName" placeholder="请输入" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="履行状态" prop="performStatus">
          <el-select v-model="queryForm.performStatus" placeholder="请选择" clearable style="width: 130px">
            <el-option label="正常" value="NORMAL" />
            <el-option label="逾期" value="OVERDUE" />
            <el-option label="违约" value="BREACH" />
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
        <el-table-column label="合同编号" prop="contractNo" width="140" />
        <el-table-column label="合同名称" prop="contractName" min-width="180" show-overflow-tooltip />
        <el-table-column label="合同金额(万元)" prop="contractAmount" width="130" align="right" />
        <el-table-column label="已付金额(万元)" prop="paidAmount" width="130" align="right" />
        <el-table-column label="付款进度" prop="paymentProgress" width="120" align="center">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.paymentProgress || 0" :stroke-width="14" :text-inside="true" />
          </template>
        </el-table-column>
        <el-table-column label="履行状态" prop="performStatus" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="statusTagType(scope.row.performStatus)" size="small">
              {{ statusLabel(scope.row.performStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="逾期天数" prop="overdueDays" width="80" align="center">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.overdueDays > 0 ? '#F56C6C' : '#303133' }">{{ scope.row.overdueDays || 0 }}</span>
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

    <!-- 履行详情弹窗 -->
    <el-dialog title="合同履行详情" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <div v-loading="detailLoading">
        <el-descriptions :column="2" border size="small" v-if="detailData.contractName">
          <el-descriptions-item label="合同编号">{{ detailData.contractNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="合同名称">{{ detailData.contractName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="合同类型">{{ typeLabel(detailData.contractType) }}</el-descriptions-item>
          <el-descriptions-item label="所属企业">{{ detailData.companyName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="对方单位">{{ detailData.counterpartyName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="合同状态">{{ contractStatusLabel(detailData.contractStatus) }}</el-descriptions-item>
          <el-descriptions-item label="签订日期">{{ detailData.signDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="生效日期">{{ detailData.effectiveDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ detailData.expiryDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="变更次数">{{ detailData.changeCount || 0 }}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">履行进度</el-divider>
        <el-row :gutter="20" style="margin-bottom: 16px">
          <el-col :span="8">
            <div class="detail-stat">
              <div class="detail-stat-label">合同金额(万元)</div>
              <div class="detail-stat-value">{{ detailData.contractAmount || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-stat">
              <div class="detail-stat-label">已付金额(万元)</div>
              <div class="detail-stat-value" style="color: #52c41a">{{ detailData.paidAmount || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-stat">
              <div class="detail-stat-label">已收金额(万元)</div>
              <div class="detail-stat-value" style="color: #1890ff">{{ detailData.receivedAmount || 0 }}</div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div style="margin-bottom: 8px; font-size: 13px; color: #666">付款进度</div>
            <el-progress :percentage="detailData.paymentProgress || 0" :stroke-width="18" :text-inside="true" />
          </el-col>
          <el-col :span="12">
            <div style="margin-bottom: 8px; font-size: 13px; color: #666">计划进度 vs 实际进度</div>
            <el-progress :percentage="Number(detailData.actualProgress) || 0" :stroke-width="18" :text-inside="true" status="success" />
            <div style="font-size: 12px; color: #999; margin-top: 4px">计划: {{ detailData.planProgress || 0 }}% / 实际: {{ detailData.actualProgress || 0 }}%</div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 16px">
          <el-col :span="12">
            <span style="font-size: 13px; color: #666">履行状态：</span>
            <el-tag :type="statusTagType(detailData.performStatus)" size="small">{{ statusLabel(detailData.performStatus) }}</el-tag>
          </el-col>
          <el-col :span="12">
            <span style="font-size: 13px; color: #666">逾期天数：</span>
            <span :style="{ color: detailData.overdueDays > 0 ? '#F56C6C' : '#303133', fontWeight: 600 }">{{ detailData.overdueDays || 0 }} 天</span>
          </el-col>
        </el-row>
      </div>
      <div slot="footer">
        <el-button @click="dialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import { getPerformanceList, getPerformanceDetail } from '@/api/stateAssets/contractPenetration'

export default {
  name: 'ContractPerformanceMonitor',
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
      queryForm: { pageNumber: 1, pageSize: 10, contractName: '', performStatus: '' },
      dialogVisible: false,
      detailLoading: false,
      detailData: {},
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    /** 查询列表 */
    async fetchData() {
      this.loading = true
      try {
        const res = await getPerformanceList(this.queryForm)
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
    /** 查看履行详情 */
    async handleView(row) {
      this.dialogVisible = true
      this.detailLoading = true
      this.detailData = {}
      try {
        const res = await getPerformanceDetail(row.contractId)
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
    statusTagType(status) {
      const map = { NORMAL: 'success', OVERDUE: 'warning', BREACH: 'danger' }
      return map[status] || 'info'
    },
    statusLabel(status) {
      const map = { NORMAL: '正常', OVERDUE: '逾期', BREACH: '违约' }
      return map[status] || status || '-'
    },
    typeLabel(type) {
      const map = { PURCHASE: '采购合同', SALES: '销售合同', ENGINEERING: '工程合同', SERVICE: '服务合同', OTHER: '其他' }
      return map[type] || type || '-'
    },
    contractStatusLabel(status) {
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
.detail-stat { text-align: center; padding: 12px; background: #fafafa; border-radius: 4px; }
.detail-stat-label { font-size: 12px; color: #999; margin-bottom: 4px; }
.detail-stat-value { font-size: 20px; font-weight: 600; color: #303133; }
</style>